package in.pqcintegration.PQCKafkaExample.pqcconsumer;

import in.pqcintegration.PQCKafkaExample.pqcencryptlogic.PQCCryptoService;
import org.apache.kafka.clients.consumer.*;

import java.security.KeyPair;
import java.time.Duration;
import java.util.*;

public class PQCConsumer {

    public static void main(String[] args) throws Exception {

        PQCCryptoService crypto = new PQCCryptoService();

        //Generate consumer keypair
        KeyPair keyPair = crypto.generateKyberKeyPair();

        //public key generation-- poc purpose 
        String pubKeyBase64 = Base64.getEncoder()
                .encodeToString(crypto.encodePublicKey(keyPair.getPublic()));

        System.out.println("Consumer Public Key:");
        System.out.println(pubKeyBase64);//printing public key, not recommended, for poc purpose.

        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(props());
        consumer.subscribe(List.of("pqc-topic1"));

        while (true) {
            for (ConsumerRecord<String, byte[]> record :
                    consumer.poll(Duration.ofMillis(500))) {

                byte[] encapsulation =
                        record.headers().lastHeader("encapsulation").value();

                //Decapsulate the key
                byte[] sharedSecret =
                        crypto.decapsulate(keyPair.getPrivate(), encapsulation);

                //Decrypt the key
                byte[] decrypted =
                        crypto.decrypt(record.value(), sharedSecret);

                System.out.println("📥 Received: " + new String(decrypted));
            }
        }
    }

    private static Properties props() {
        Properties p = new Properties();
        p.put("bootstrap.servers", "xxxxx:9092");//kafka broker dtls
        p.put("group.id", "pqc-group");
        p.put("auto.offset.reset", "latest");
        p.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        p.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        return p;
    }
}