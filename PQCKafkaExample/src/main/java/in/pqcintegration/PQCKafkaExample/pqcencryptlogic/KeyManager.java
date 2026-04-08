package in.pqcintegration.PQCKafkaExample.pqcencryptlogic;

import java.util.concurrent.ConcurrentHashMap;

public class KeyManager {

    private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    private final long rotationInterval = 60000; 
    private final long expiryTime = 120000;

    public static class Session {
        public byte[] sharedSecret;
        public long createdAt;

        public Session(byte[] secret) {
            this.sharedSecret = secret;
            this.createdAt = System.currentTimeMillis();
        }
    }

    public void storeSession(String clientId, byte[] sharedSecret) {
        sessions.put(clientId, new Session(sharedSecret));
    }

    public byte[] getValidSession(String clientId) {
        Session session = sessions.get(clientId);

        if (session == null) return null;

        long now = System.currentTimeMillis();

        if (now - session.createdAt > expiryTime) {
            sessions.remove(clientId);
            return null;
        }

        return session.sharedSecret;
    }

    public boolean shouldRotate(String clientId) {
        Session session = sessions.get(clientId);

        if (session == null) return true;

        long now = System.currentTimeMillis();

        return (now - session.createdAt) > rotationInterval;
    }
}
