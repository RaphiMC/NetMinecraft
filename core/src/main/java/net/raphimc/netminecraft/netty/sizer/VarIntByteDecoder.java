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
package net.raphimc.netminecraft.netty.sizer;

import io.netty.util.ByteProcessor;

// https://github.com/VelocityPowered/Velocity/blob/dev/3.0.0/proxy/src/main/java/com/velocitypowered/proxy/protocol/netty/VarintByteDecoder.java
public class VarIntByteDecoder implements ByteProcessor {

    private int readVarint;
    private int bytesRead;
    private DecodeResult result = DecodeResult.TOO_SHORT;

    @Override
    public boolean process(byte k) {
        if (k == 0 && bytesRead == 0) {
            // tentatively say it's invalid, but there's a possibility of redemption
            result = DecodeResult.RUN_OF_ZEROES;
            return true;
        }
        if (result == DecodeResult.RUN_OF_ZEROES) {
            return false;
        }
        readVarint |= (k & 0x7F) << bytesRead++ * 7;
        if (bytesRead > 3) {
            result = DecodeResult.TOO_BIG;
            return false;
        }
        if ((k & 0x80) != 128) {
            result = DecodeResult.SUCCESS;
            return false;
        }
        return true;
    }

    public int getReadVarint() {
        return readVarint;
    }

    public int getBytesRead() {
        return bytesRead;
    }

    public DecodeResult getResult() {
        return result;
    }

    public enum DecodeResult {
        SUCCESS,
        TOO_SHORT,
        TOO_BIG,
        RUN_OF_ZEROES
    }

}
