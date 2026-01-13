package org.lab.repository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.stream.Collectors;

import org.lab.model.Ticket;
import org.lab.model.TicketStatus;

public class TicketRepository {
    private final Map<Long, Ticket> tickets = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Ticket save(Ticket ticket) {
        if (ticket.getId() == null) {
            ticket.setId(idGenerator.getAndIncrement());
        }
        tickets.put(ticket.getId(), ticket);
        return ticket;
    }

    public Optional<Ticket> findById(Long id) {
        return Optional.ofNullable(tickets.get(id));
    }

    public List<Ticket> findByMilestoneId(Long milestoneId) {
        return tickets.values().stream()
                .filter(ticket -> Objects.equals(ticket.getMilestoneId(), milestoneId))
                .collect(Collectors.toList());
    }

    public List<Ticket> findByStatus(TicketStatus status) {
        return tickets.values().stream()
                .filter(ticket -> ticket.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public List<Ticket> findAll() {
        return tickets.values().stream().collect(Collectors.toList());
    }

    public boolean existsById(Long id) {
        return tickets.containsKey(id);
    }

    public void deleteById(Long id) {
        tickets.remove(id);
    }
}