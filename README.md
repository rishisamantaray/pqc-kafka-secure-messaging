# pqc-kafka-secure-messaging
# 🔐 Post-Quantum Secure Kafka Messaging (Kyber KEM)

## 🚀 Overview

This project demonstrates a **secure Kafka-based messaging system** using **Post-Quantum Cryptography (PQC)**.

It uses:

* **CRYSTALS-Kyber (KEM)** for key exchange
* **AES** for fast message encryption
* **Apache Kafka** for real-time streaming

---

## 🧠 Architecture

Producer
→ Kyber Encapsulation
→ AES Encryption
→ Kafka Topic
→ Consumer
→ Kyber Decapsulation
→ AES Decryption

---

## 🔐 Security Design

* **Kyber KEM** ensures quantum-resistant key exchange
* **AES (symmetric)** ensures performance
* Encapsulation is transmitted via Kafka headers

---

## ⚙️ Tech Stack

* Java
* Apache Kafka
* Bouncy Castle PQC
* Maven

---

## ▶️ How to Run

### 1. Start Kafka

```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties
```

### 2. Create Topic

```bash
kafka-topics.sh --create --topic pqc-topic --bootstrap-server localhost:9092
```

### 3. Run Consumer

* Copy generated public key

### 4. Paste into Producer (real world scenario --official public key is required) 

```java
String consumerKeyBase64 = "...";
```

### 5. Run Producer

---

## ✅ Output

```text
📤 Sent secure message
📥 Received: FINAL PQC KEM WORKING 🚀
```

---

## 🚧 Challenges Solved

* Kyber KEM vs KeyAgreement confusion
* JCA vs Bouncy Castle API mismatch
* Key encoding/decoding issues
* Kafka header-based key exchange
* AES key mismatch debugging

---

## 🌍 Use Cases

* Secure financial systems
* Real-time event streaming
* Future-proof microservices security

---

## 🔥 Future Enhancements

* Add Dilithium signatures
* Spring Boot microservices
* Schema Registry integration
* PQC-based TLS

---

## 👨‍💻 Author

Built as part of exploration into **Post-Quantum Cryptography + Distributed Systems**
