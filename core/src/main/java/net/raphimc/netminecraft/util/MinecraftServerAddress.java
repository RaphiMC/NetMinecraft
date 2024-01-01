/*
 * This file is part of NetMinecraft - https://github.com/RaphiMC/NetMinecraft
 * Copyright (C) 2022-2024 RK_01/RaphiMC and contributors
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
import java.net.InetSocketAddress;
import java.util.Hashtable;

public class MinecraftServerAddress extends InetSocketAddress {

    private static final String DNS_CONTEXT_FACTORY_CLASS = "com.sun.jndi.dns.DnsContextFactory";

    MinecraftServerAddress(final String address, final int port) {
        super(address, port);
    }

    public static MinecraftServerAddress ofUnresolved(final String address, final int port) {
        return new MinecraftServerAddress(address, port);
    }

    public static MinecraftServerAddress ofUnresolved(final String address) {
        String[] addressParts = address.split(":");
        if (address.startsWith("[")) {
            final int index = address.indexOf("]");
            if (index > 0) {
                final String ipv6Part = address.substring(1, index);
                String extraData = address.substring(index + 1).trim();
                if (extraData.startsWith(":")) {
                    extraData = extraData.substring(1);
                    addressParts = new String[]{ipv6Part, extraData};
                } else {
                    addressParts = new String[]{ipv6Part};
                }
            }
        }

        if (addressParts.length > 2) {
            addressParts = new String[]{address};
        }

        final int port = addressParts.length > 1 ? portOrDefault(addressParts[1], 25565) : 25565;
        return new MinecraftServerAddress(addressParts[0], port);
    }

    public static MinecraftServerAddress ofResolved(final String address, final int port) {
        return resolveSrv(ofUnresolved(address, port));
    }

    public static MinecraftServerAddress ofResolved(final String address) {
        return resolveSrv(ofUnresolved(address));
    }

    private static MinecraftServerAddress resolveSrv(final MinecraftServerAddress unresolved) {
        if (unresolved.getPort() == 25565) {
            try {
                Class.forName(DNS_CONTEXT_FACTORY_CLASS);
                final Hashtable<String, String> hashtable = new Hashtable<>();
                hashtable.put("java.naming.factory.initial", DNS_CONTEXT_FACTORY_CLASS);
                hashtable.put("java.naming.provider.url", "dns:");
                hashtable.put("com.sun.jndi.dns.timeout.retries", "1");
                final DirContext dirContext = new InitialDirContext(hashtable);
                final Attributes attributes = dirContext.getAttributes("_minecraft._tcp." + unresolved.getHostString(), new String[]{"SRV"});
                final String[] srvRecord = attributes.get("srv").get().toString().split(" ", 4);
                return new MinecraftServerAddress(srvRecord[3], portOrDefault(srvRecord[2], 25565));
            } catch (Throwable ignored) {
            }
        }

        return unresolved;
    }

    private static int portOrDefault(final String port, final int defaultPort) {
        try {
            return Integer.parseInt(port.trim());
        } catch (NumberFormatException e) {
            return defaultPort;
        }
    }

}
