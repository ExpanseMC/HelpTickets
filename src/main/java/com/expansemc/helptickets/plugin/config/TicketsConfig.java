package com.expansemc.helptickets.plugin.config;

import com.expansemc.helptickets.api.Comment;
import com.expansemc.helptickets.api.HelpTicketsAPI;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.world.ServerLocation;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Configs are still basically the same (for now?)!
 */
@ConfigSerializable
@DefaultQualifier(NonNull.class)
public class TicketsConfig implements HelpTicketsAPI {

    @Setting
    private int nextTicketId = 1;
    @Setting
    private Map<Integer, Ticket> tickets = new HashMap<>();

    @Override
    public Collection<Ticket> getTickets() {
        return this.tickets.values();
    }

    @Override
    public Optional<com.expansemc.helptickets.api.Ticket> getTicket(int id) {
        return Optional.ofNullable(this.tickets.get(id));
    }

    @Override
    public void removeTicket(int id) {
        this.tickets.remove(id);
    }

    @Override
    public Ticket addTicket(com.expansemc.helptickets.api.Ticket.Template template) {
        int ticketId = this.nextTicketId++;

        Ticket ticket = new Ticket(
                ticketId,
                template.getCreator().getUniqueId(),
                template.getCreatedAt(),
                template.getAssigned().stream().map(User::getUniqueId).collect(Collectors.toSet()),
                new LinkedHashMap<>(),
                template.isClosed()
        );

        for (Comment.Template comment : template.getComments()) {
            ticket.addComment(comment);
        }

        this.tickets.put(ticketId, ticket);

        return ticket;
    }

    @ConfigSerializable
    public static class Ticket implements com.expansemc.helptickets.api.Ticket {

        @Setting
        private int id;
        @Setting
        private UUID creator;
        @Setting
        private Instant createdAt = Instant.now();
        @Setting
        private Set<UUID> assigned = new HashSet<>();
        @Setting
        private int nextCommentId = 1;
        @Setting
        private Map<Integer, Comment> comments = new LinkedHashMap<>();
        @Setting
        private boolean closed = false;

        public Ticket() {
        }

        public Ticket(int id, UUID creator, Instant createdAt, Set<UUID> assigned, Map<Integer, Comment> comments, boolean closed) {
            this.id = id;
            this.creator = creator;
            this.createdAt = createdAt;
            this.assigned = assigned;
            this.comments = comments;
            this.closed = closed;
        }

        @Override
        public int getId() {
            return this.id;
        }

        @Override
        public User getCreator() {
            return Sponge.getServer().getUserManager().getOrCreate(GameProfile.of(this.creator));
        }

        @Override
        public Instant getCreatedAt() {
            return this.createdAt;
        }

        @Override
        public boolean isClosed() {
            return this.closed;
        }

        @Override
        public void setClosed(boolean closed) {
            this.closed = closed;
        }

        @Override
        public Collection<User> getAssigned() {
            return this.assigned.stream()
                    .map(uuid -> Sponge.getServer().getUserManager().getOrCreate(GameProfile.of(uuid)))
                    .collect(Collectors.toList());
        }

        @Override
        public void assign(User user) {
            this.assigned.add(user.getUniqueId());
        }

        @Override
        public void unassign(User user) {
            this.assigned.remove(user.getUniqueId());
        }

        @Override
        public Collection<Comment> getComments() {
            return this.comments.values();
        }

        @Override
        public Optional<com.expansemc.helptickets.api.Comment> getComment(int id) {
            return Optional.ofNullable(this.comments.get(id));
        }

        @Override
        public void removeComment(int id) {
            this.comments.remove(id);
        }

        @Override
        public Comment addComment(com.expansemc.helptickets.api.Comment.Template template) {
            int commentId = this.nextCommentId++;

            Comment comment = new Comment(
                    commentId,
                    template.getCreator().getUniqueId(),
                    template.getCreatedAt(),
                    template.getMessage(),
                    template.getLocation().orElse(null)
            );

            this.comments.put(commentId, comment);

            return comment;
        }

        @ConfigSerializable
        public static class Comment implements com.expansemc.helptickets.api.Comment {

            @Setting
            private int id;

            @Setting
            private UUID creator;

            @Setting
            private Instant createdAt;

            @Setting
            private String message;

            @Setting
            @Nullable
            private ServerLocation location = null;

            public Comment() {
            }

            public Comment(int id, UUID creator, Instant createdAt, String message, @Nullable ServerLocation location) {
                this.id = id;
                this.creator = creator;
                this.createdAt = createdAt;
                this.message = message;
                this.location = location;
            }

            @Override
            public int getId() {
                return this.id;
            }

            @Override
            public User getCreator() {
                return Sponge.getServer().getUserManager().getOrCreate(GameProfile.of(this.creator));
            }

            @Override
            public Instant getCreatedAt() {
                return this.createdAt;
            }

            @Override
            public String getMessage() {
                return this.message;
            }

            @Override
            public Optional<ServerLocation> getLocation() {
                return Optional.ofNullable(this.location);
            }
        }
    }
}