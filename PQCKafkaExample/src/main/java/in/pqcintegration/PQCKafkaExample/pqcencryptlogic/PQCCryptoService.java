package in.pqcintegration.PQCKafkaExample.pqcencryptlogic;


import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.bouncycastle.pqc.jcajce.spec.KyberParameterSpec;
import org.bouncycastle.jcajce.SecretKeyWithEncapsulation;
import org.bouncycastle.jcajce.spec.KEMGenerateSpec;
import org.bouncycastle.jcajce.spec.KEMExtractSpec;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class PQCCryptoService {

    static {
        Security.addProvider(new BouncyCastlePQCProvider());
    }

    // 🔑 Generate Kyber KeyPair
    public KeyPair generateKyberKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("Kyber", "BCPQC");
        kpg.initialize(KyberParameterSpec.kyber512);
        return kpg.generateKeyPair();
    }

    // 🔐 Producer: Encapsulation
    public EncapsulationResult encapsulate(PublicKey publicKey) throws Exception {

        KeyGenerator keyGen = KeyGenerator.getInstance("Kyber", "BCPQC");
        keyGen.init(new KEMGenerateSpec(publicKey, "AES"));

        SecretKeyWithEncapsulation sec =
                (SecretKeyWithEncapsulation) keyGen.generateKey();

        return new EncapsulationResult(
                sec.getEncoded(),
                sec.getEncapsulation()
        );
    }

    // 🔓 Consumer: Decapsulation
    public byte[] decapsulate(PrivateKey privateKey, byte[] encapsulation) throws Exception {

        KeyGenerator keyGen = KeyGenerator.getInstance("Kyber", "BCPQC");
        keyGen.init(new KEMExtractSpec(privateKey, encapsulation, "AES"));

        SecretKey secretKey = keyGen.generateKey();
        return secretKey.getEncoded();
    }

    // 🔐 AES Encrypt
    public byte[] encrypt(byte[] data, byte[] sharedSecret) throws Exception {
        byte[] keyBytes = Arrays.copyOf(sharedSecret, 16);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, "AES"));
        return cipher.doFinal(data);
    }

    // 🔓 AES Decrypt
    public byte[] decrypt(byte[] data, byte[] sharedSecret) throws Exception {
        byte[] keyBytes = Arrays.copyOf(sharedSecret, 16);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "AES"));
        return cipher.doFinal(data);
    }

    // 🔁 Encode/Decode Public Key
    public byte[] encodePublicKey(PublicKey key) {
        return key.getEncoded();
    }

    public PublicKey decodeKyberPublicKey(byte[] data) throws Exception {
        KeyFactory kf = KeyFactory.getInstance("Kyber", "BCPQC");
        return kf.generatePublic(new X509EncodedKeySpec(data));
    }

    // Helper class
    public static class EncapsulationResult {
        public byte[] sharedSecret;
        public byte[] encapsulation;

        public EncapsulationResult(byte[] s, byte[] e) {
            this.sharedSecret = s;
            this.encapsulation = e;
        }
    }
}