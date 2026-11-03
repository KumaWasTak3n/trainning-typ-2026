package com.quannet.demo.service;

import com.quannet.demo.model.Session;

public interface SessionService {
    public void save(Session session);

    public Session startSession(String username, Long computerId);

    public Session endSession(Long id);
}
