package com.example.demo.aggregates;

import com.example.demo.commands.FileComplaintCommand;
import com.example.demo.events.ComplaintFiledEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;


import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class Complaint {

    @AggregateIdentifier
    private String complaintId;

    public Complaint() {
    }

    @CommandHandler
    public Complaint(FileComplaintCommand cmd) {
        apply(new ComplaintFiledEvent(cmd.getId(), cmd.getCompany(), cmd.getDescription()));
    }

    @EventSourcingHandler
    public void on(ComplaintFiledEvent event) {
        this.complaintId = event.getId();

    }

}
