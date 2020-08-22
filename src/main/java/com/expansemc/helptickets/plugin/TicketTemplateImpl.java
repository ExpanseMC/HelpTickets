package com.expansemc.helptickets.plugin;

import com.expansemc.helptickets.api.Comment;
import com.expansemc.helptickets.api.Ticket;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.api.entity.living.player.User;

import java.time.Instant;
import java.util.*;

@DefaultQualifier(NonNull.class)
public class TicketTemplateImpl implements Ticket.Template {

    private final User creator;
    private final Instant createdAt;
    private final boolean closed;
    private final Set<User> assigned;
    private final Set<Comment.Template> comments;

    public TicketTemplateImpl(User creator, Instant createdAt, boolean closed, Set<User> assigned, Set<Comment.Template> comments) {
        this.creator = creator;
        this.createdAt = createdAt;
        this.closed = closed;
        this.assigned = assigned;
        this.comments = comments;
    }

    @Override
    public User getCreator() {
        return this.creator;
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
    public Collection<User> getAssigned() {
        return this.assigned;
    }

    @Override
    public Collection<Comment.Template> getComments() {
        return this.comments;
    }

    public static class BuilderImpl implements Ticket.Template.Builder {

        @Nullable
        private User creator = null;
        private Instant createdAt = Instant.now();
        private boolean closed = false;
        private final Set<User> assigned = new HashSet<>();
        private final Set<Comment.Template> comments = new HashSet<>();

        @Override
        public Builder creator(User creator) {
            this.creator = Objects.requireNonNull(creator, "creator");
            return this;
        }

        @Override
        public Builder createdAt(Instant createdAt) {
            this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
            return this;
        }

        @Override
        public Builder closed(boolean closed) {
            this.closed = closed;
            return this;
        }

        @Override
        public Builder assigned(Iterable<User> users) {
            for (User user : users) {
                this.assigned.add(user);
            }
            return this;
        }

        @Override
        public Builder assigned(User... users) {
            Collections.addAll(this.assigned, users);
            return this;
        }

        @Override
        public Builder comments(Iterable<Comment.Template> comments) {
            for (Comment.Template comment : comments) {
                this.comments.add(comment);
            }
            return this;
        }

        @Override
        public Builder comments(Comment.Template... comments) {
            Collections.addAll(this.comments, comments);
            return this;
        }

        @Override
        public Ticket.Template build() {
            return new TicketTemplateImpl(
                    Objects.requireNonNull(this.creator, "creator"),
                    Objects.requireNonNull(this.createdAt, "createdAt"),
                    this.closed,
                    this.assigned,
                    this.comments
            );
        }

        @Override
        public Builder reset() {
            this.creator = null;
            this.createdAt = Instant.now();
            this.closed = false;
            this.assigned.clear();
            this.comments.clear();
            return this;
        }
    }
}