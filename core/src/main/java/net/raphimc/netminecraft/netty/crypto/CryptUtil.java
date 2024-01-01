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
package net.raphimc.netminecraft.netty.crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.function.Function;

public class CryptUtil {

    private static final Base64.Encoder RSA_KEY_BASE64 = Base64.getMimeEncoder(76, "\n".getBytes(StandardCharsets.UTF_8));

    public static final PublicKey MOJANG_PUBLIC_KEY;
    public static final Signature MOJANG_PUBLIC_KEY_SIGNATURE;

    static {
        try {
            final InputStream is = CryptUtil.class.getClassLoader().getResourceAsStream("yggdrasil_session_pubkey.der");
            final ByteArrayOutputStream publicKeyBytes = new ByteArrayOutputStream();
            final byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) publicKeyBytes.write(buffer, 0, len);
            is.close();

            MOJANG_PUBLIC_KEY = decodeRsaPublicKey(publicKeyBytes.toByteArray());
            MOJANG_PUBLIC_KEY_SIGNATURE = Signature.getInstance("SHA1withRSA");
            MOJANG_PUBLIC_KEY_SIGNATURE.initVerify(MOJANG_PUBLIC_KEY);
        } catch (Throwable e) {
            throw new IllegalStateException("Failed to load mojang public key and signature", e);
        }
    }

    public static SecretKey generateSecretKey() {
        try {
            final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            return keyGenerator.generateKey();
        } catch (Throwable e) {
            throw new IllegalStateException("Failed to generate AES secret key", e);
        }
    }

    public static KeyPair generateKeyPair() {
        try {
            final KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
            keyGenerator.initialize(1024);
            return keyGenerator.generateKeyPair();
        } catch (Throwable e) {
            throw new IllegalStateException("Failed to generate RSA key pair", e);
        }
    }

    public static PublicKey decodeRsaPublicKey(final byte[] key) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key));
        } catch (Throwable e) {
            throw new IllegalStateException("Failed to decode RSA public key", e);
        }
    }

    private static PrivateKey decodeRsaPrivateKey(final byte[] key) {
        try {
            final EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(key);
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(encodedKeySpec);
        } catch (Throwable e) {
            throw new IllegalStateException("Failed to decode RSA private key", e);
        }
    }

    public static PublicKey decodeRsaPublicKeyPem(final String key) {
        return decodePem(key, "-----BEGIN RSA PUBLIC KEY-----", "-----END RSA PUBLIC KEY-----", CryptUtil::decodeRsaPublicKey);
    }

    public static PrivateKey decodeRsaPrivateKeyPem(final String key) {
        return decodePem(key, "-----BEGIN RSA PRIVATE KEY-----", "-----END RSA PRIVATE KEY-----", CryptUtil::decodeRsaPrivateKey);
    }

    public static String encodeRsaPublicKey(final PublicKey key) {
        if (!"RSA".equals(key.getAlgorithm())) throw new IllegalArgumentException("Public key must be RSA");
        return "-----BEGIN RSA PUBLIC KEY-----\n" + RSA_KEY_BASE64.encodeToString(key.getEncoded()) + "\n-----END RSA PUBLIC KEY-----\n";
    }

    public static String encodeRsaPrivateKey(final PrivateKey key) {
        if (!"RSA".equals(key.getAlgorithm())) throw new IllegalArgumentException("Private key must be RSA");
        return "-----BEGIN RSA PRIVATE KEY-----\n" + RSA_KEY_BASE64.encodeToString(key.getEncoded()) + "\n-----END RSA PRIVATE KEY-----\n";
    }

    public static SecretKey decryptSecretKey(final PrivateKey privateKey, final byte[] encryptedSecretKey) {
        try {
            return new SecretKeySpec(decryptData(privateKey, encryptedSecretKey), "AES");
        } catch (Throwable e) {
            throw new IllegalStateException("Failed to decrypt AES secret key", e);
        }
    }

    public static byte[] encryptData(final Key key, final byte[] data) {
        return cryptData(Cipher.ENCRYPT_MODE, key, data);
    }

    public static byte[] decryptData(final Key key, final byte[] data) {
        return cryptData(Cipher.DECRYPT_MODE, key, data);
    }

    public static byte[] computeServerIdHash(final String serverId, final PublicKey publicKey, final SecretKey secretKey) {
        try {
            return hash(serverId.getBytes(StandardCharsets.ISO_8859_1), secretKey.getEncoded(), publicKey.getEncoded());
        } catch (Throwable e) {
            throw new IllegalStateException("Failed to compute server id hash", e);
        }
    }

    public static boolean verifySignedNonce(final PublicKey publicKey, final byte[] nonce, final long salt, final byte[] signature) {
        try {
            final Signature signer = Signature.getInstance("SHA256withRSA");
            signer.initVerify(publicKey);

            signer.update(nonce);
            signer.update(long2ByteArray(salt));

            return signer.verify(signature);
        } catch (Throwable e) {
            throw new IllegalArgumentException("Failed to verify signed nonce", e);
        }
    }

    public static byte[] signNonce(final PrivateKey privateKey, final byte[] nonce, final long salt) {
        try {
            final Signature signer = Signature.getInstance("SHA256withRSA");
            signer.initSign(privateKey);

            signer.update(nonce);
            signer.update(long2ByteArray(salt));

            return signer.sign();
        } catch (Throwable e) {
            throw new IllegalArgumentException("Failed to sign nonce", e);
        }
    }

    private static byte[] cryptData(final int mode, final Key key, final byte[] data) {
        try {
            final Cipher cipher = Cipher.getInstance(key.getAlgorithm());
            cipher.init(mode, key);
            return cipher.doFinal(data);
        } catch (Throwable e) {
            throw new IllegalStateException("Failed to crypt data", e);
        }
    }

    private static byte[] hash(final byte[]... data) {
        try {
            final MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            for (byte[] array : data) sha1.update(array);
            return sha1.digest();
        } catch (Throwable e) {
            throw new IllegalStateException("Failed to hash data", e);
        }
    }

    private static <T extends Key> T decodePem(String key, final String prefix, final String suffix, final Function<byte[], T> decoder) {
        int start = key.indexOf(prefix);
        if (start != -1) {
            start += prefix.length();
            final int end = key.indexOf(suffix, start);
            key = key.substring(start, end + 1);
        }

        return decoder.apply(Base64.getMimeDecoder().decode(key));
    }

    private static byte[] long2ByteArray(long value) {
        final byte[] result = new byte[8];
        for (int i = 7; i >= 0; --i) {
            result[i] = (byte) ((int) (value & 255L));
            value >>= 8;
        }
        return result;
    }

}
