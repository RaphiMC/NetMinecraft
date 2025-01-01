/*
 * This file is part of NetMinecraft - https://github.com/RaphiMC/NetMinecraft
 * Copyright (C) 2022-2025 RK_01/RaphiMC and contributors
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
package net.raphimc.netminecraft.netty.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.security.GeneralSecurityException;
import java.security.Key;

/**
 * An encryption implementation using "AES/CFB8/NoPadding" encryption.
 */
public class AESEncryption {

    private final Cipher inCipher;
    private final Cipher outCipher;

    /**
     * Creates a new AESEncryption instance.
     *
     * @param key Key to use when encrypting/decrypting data.
     * @throws GeneralSecurityException If a security error occurs.
     */
    public AESEncryption(final Key key) throws GeneralSecurityException {
        this.inCipher = Cipher.getInstance("AES/CFB8/NoPadding");
        this.inCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(key.getEncoded()));
        this.outCipher = Cipher.getInstance("AES/CFB8/NoPadding");
        this.outCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(key.getEncoded()));
    }

    public int getDecryptOutputSize(final int length) {
        return this.inCipher.getOutputSize(length);
    }

    public int getEncryptOutputSize(final int length) {
        return this.outCipher.getOutputSize(length);
    }

    public int decrypt(final byte[] input, final int inputOffset, final int inputLength, final byte[] output, final int outputOffset) throws Exception {
        return this.inCipher.update(input, inputOffset, inputLength, output, outputOffset);
    }

    public int encrypt(final byte[] input, final int inputOffset, final int inputLength, final byte[] output, final int outputOffset) throws Exception {
        return this.outCipher.update(input, inputOffset, inputLength, output, outputOffset);
    }

}
