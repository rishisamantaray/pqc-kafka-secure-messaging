package in.pqcintegration.PQCKafkaExample.pqcproducer;
import in.pqcintegration.PQCKafkaExample.pqcencryptlogic.PQCCryptoService;
import org.apache.kafka.clients.producer.*;

import java.security.PublicKey;
import java.util.Base64;
import java.util.Properties;

public class PQCProducer {

    public static void main(String[] args) throws Exception {

        PQCCryptoService crypto = new PQCCryptoService();

        //consumer public key here, in realworld scenario requires an offical public key to be called.
        String consumerKeyBase64 = "XXXXX";

        byte[] keyBytes = Base64.getDecoder().decode(consumerKeyBase64);
        PublicKey consumerPublicKey = crypto.decodeKyberPublicKey(keyBytes);

        //encapsulate
        PQCCryptoService.EncapsulationResult result =
                crypto.encapsulate(consumerPublicKey);

        byte[] sharedSecret = result.sharedSecret;

        //encrypt
        String message = "pratyush060@gmail.com";
        byte[] encrypted =
                crypto.encrypt(message.getBytes(), sharedSecret);

        Producer<String, byte[]> producer =
                new KafkaProducer<>(props());

        ProducerRecord<String, byte[]> record =
                new ProducerRecord<>("pqc-topic1", encrypted);

        //Send encapsulation header
        record.headers().add("encapsulation", result.encapsulation);

        producer.send(record);

        System.out.println("Sent secure message");

        producer.close();
    }

    private static Properties props() {
        Properties p = new Properties();
        p.put("bootstrap.servers", "XXXXX:9092");//kafka broker details
        p.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        p.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        return p;
    }
}