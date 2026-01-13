package org.lab.service;

import org.lab.model.Ticket;
import org.lab.model.TicketStatus;
import org.lab.model.User;
import org.lab.repository.TicketRepository;
import java.util.List;
import java.util.HashSet;

public class TicketService {
    
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(Long milestoneId, TicketStatus status) {
        if (milestoneId == null) {
            throw new IllegalArgumentException("Milestone ID cannot be null");
        }

        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        Ticket ticket = new Ticket();
        ticket.setMilestoneId(milestoneId);
        ticket.setStatus(status);
        ticket.setAssignees(new HashSet<>());

        return ticketRepository.save(ticket);
    }

    public Ticket findById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public List<Ticket> findByMilestoneId(Long milestoneId) {
        if (milestoneId == null) {
            throw new IllegalArgumentException("Milestone ID cannot be null");
        }
        return ticketRepository.findByMilestoneId(milestoneId);
    }

    public List<Ticket> findByStatus(TicketStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        return ticketRepository.findByStatus(status);
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Ticket assignUser(Long ticketId, User user) {
        if (ticketId == null) {
            throw new IllegalArgumentException("Ticket ID cannot be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        Ticket ticket = findById(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket with ID '" + ticketId + "' does not exist");
        }

        ticket.getAssignees().add(user);
        return ticketRepository.save(ticket);
    }

    public Ticket updateStatus(Long ticketId, TicketStatus status) {
        if (ticketId == null) {
            throw new IllegalArgumentException("Ticket ID cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        Ticket ticket = findById(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket with ID '" + ticketId + "' does not exist");
        }

        ticket.setStatus(status);
        return ticketRepository.save(ticket);
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (!ticketRepository.existsById(id)) {
            throw new IllegalArgumentException("Ticket with ID '" + id + "' does not exist");
        }
        ticketRepository.deleteById(id);
    }
}