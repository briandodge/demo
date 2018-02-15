package com.example.demo.dao;

import com.example.demo.events.ComplaintFiledEvent;
import com.example.demo.models.ComplaintQueryObject;
import com.example.demo.repositories.ComplaintQueryObjectRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class ComplaintQueryObjectUpdater {
    private final ComplaintQueryObjectRepository repository;

    public ComplaintQueryObjectUpdater(ComplaintQueryObjectRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(ComplaintFiledEvent event) {
        repository.save(new ComplaintQueryObject(event.getId(), event.getCompany(), event.getDescription()));
    }
}
