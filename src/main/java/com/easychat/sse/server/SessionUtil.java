package com.easychat.sse.server;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionUtil {
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>(500);

    public static void putIfAbsent(String userId, Session session) {
        sessions.putIfAbsent(userId, session);
    }

    public static Session remove(String userId) {
        return sessions.remove(userId);
    }

    public static Session get(String targetUser) {
        return sessions.get(targetUser);
    }
}
