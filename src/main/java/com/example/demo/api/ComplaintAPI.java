package com.example.demo.api;

import com.example.demo.commands.FileComplaintCommand;
import com.example.demo.models.ComplaintQueryObject;
import com.example.demo.repositories.ComplaintQueryObjectRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class ComplaintAPI {
    private final ComplaintQueryObjectRepository repository;
    private final CommandGateway commandGateway;

    public ComplaintAPI(ComplaintQueryObjectRepository repository, CommandGateway commandGateway) {
        this.repository = repository;
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public CompletableFuture<Object> fileComplaint(@RequestBody Map<String, String> request) {
        String id = UUID.randomUUID().toString();
        return commandGateway.send(new FileComplaintCommand(id, request.get("company"), request.get("description")));
    }

    @GetMapping
    public List<ComplaintQueryObject> findAll() {
        return repository.findAll();
    }


    @GetMapping("/{id}")
    public ComplaintQueryObject find(@PathVariable String id) {
        return repository.findOne(id);
    }
}
