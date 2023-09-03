/*
 * This file is part of NetMinecraft - https://github.com/RaphiMC/NetMinecraft
 * Copyright (C) 2023 RK_01/RaphiMC and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.raphimc.netminecraft.util;

import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
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
