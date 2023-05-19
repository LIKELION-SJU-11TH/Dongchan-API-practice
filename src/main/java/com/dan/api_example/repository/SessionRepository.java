package com.dan.api_example.repository;

import org.springframework.boot.web.servlet.server.Session;

import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * InMemoryRepository
 * ConcurrentHashMap을 통해 저장소 구현
 * HashMap을 사용하지 않는 이유 -> 동시성 문제
 * 싱글톤으로 구현
 */
public class SessionRepository {
    private static Map<Long, HttpSession> store = new ConcurrentHashMap<>();
    private static long sequence = 0L;

    private static final SessionRepository instance = new SessionRepository();

    public static SessionRepository getInstance() {
        return instance;
    }

    private SessionRepository() {
    }

    public HttpSession save(HttpSession session) {
        store.put(++sequence, session);
        return session;
    }

    public HttpSession findById(Long id) {
        return store.get(id);
    }

    public HttpSession findByUserId(Long userId) {
        for (HttpSession session : store.values()) {
            Long sessionUserId = (Long) session.getAttribute("userId");
            if (sessionUserId != null && sessionUserId.equals(userId)) {
                return session;
            }
        }
        return null;
    }

    public void deleteSessionBySessionId(String sessionId) {
        for (Iterator<Map.Entry<Long, HttpSession>> iterator = store.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<Long, HttpSession> entry = iterator.next();
            HttpSession session = entry.getValue();
            if (session.getId().equals(sessionId)) {
                iterator.remove();
                break;
            }
        }
    }
}
