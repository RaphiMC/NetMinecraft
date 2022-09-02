package net.raphimc.netminecraft.util;

import javax.naming.directory.*;
import java.net.IDN;
import java.net.InetSocketAddress;
import java.util.Hashtable;

public class ServerAddress {

    private final String address;
    private final int port;

    public ServerAddress(final String address, final int port) {
        this.address = address;
        this.port = port;
    }

    public String getRawAddress() {
        return this.address;
    }

    public String getAddress() {
        try {
            return IDN.toASCII(this.address);
        } catch (IllegalArgumentException ignored) {
            return "";
        }
    }

    public int getPort() {
        return this.port;
    }

    public InetSocketAddress toSocketAddress() {
        return new InetSocketAddress(this.getAddress(), this.getPort());
    }


    public static ServerAddress fromSRV(final String address) {
        if (address == null) {
            return null;
        } else {
            String[] addressParts = address.split(":");
            if (address.startsWith("[")) {
                int index = address.indexOf("]");
                if (index > 0) {
                    String ipv6Part = address.substring(1, index);
                    String extraData = address.substring(index + 1).trim();
                    if (extraData.startsWith(":")) {
                        extraData = extraData.substring(1);
                        addressParts = new String[]{ipv6Part, extraData};
                    } else {
                        addressParts = new String[]{ipv6Part};
                    }
                }
            }

            if (addressParts.length > 2) addressParts = new String[]{address};

            String tmpAddress = addressParts[0];
            int port = addressParts.length > 1 ? portOrDefault(addressParts[1], 25565) : 25565;
            if (port == 25565) {
                String[] resolvedIp = resolveSrv(tmpAddress);
                tmpAddress = resolvedIp[0];
                port = portOrDefault(resolvedIp[1], 25565);
            }

            return new ServerAddress(tmpAddress, port);
        }
    }

    private static String[] resolveSrv(String address) {
        try {
            String dnsContextFactoryName = "com.sun.jndi.dns.DnsContextFactory";
            Class.forName(dnsContextFactoryName);
            Hashtable<String, String> hashtable = new Hashtable<>();
            hashtable.put("java.naming.factory.initial", dnsContextFactoryName);
            hashtable.put("java.naming.provider.url", "dns:");
            hashtable.put("com.sun.jndi.dns.timeout.retries", "1");
            DirContext dirContext = new InitialDirContext(hashtable);
            Attributes attributes = dirContext.getAttributes("_minecraft._tcp." + address, new String[]{"SRV"});
            String[] srvRecord = attributes.get("srv").get().toString().split(" ", 4);
            return new String[]{srvRecord[3], srvRecord[2]};
        } catch (Throwable t) {
            return new String[]{address, Integer.toString(25565)};
        }
    }

    private static int portOrDefault(final String port, final int def) {
        try {
            return Integer.parseInt(port.trim());
        } catch (Exception e) {
            return def;
        }
    }

}
