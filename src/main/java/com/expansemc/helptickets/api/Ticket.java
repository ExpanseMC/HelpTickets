package com.expansemc.helptickets.api;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.util.ResettableBuilder;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

@DefaultQualifier(NonNull.class)
public interface Ticket {

    /**
     * The unique id of this ticket.
     */
    int getId();

    /**
     * The player that created this ticket.
     */
    User getCreator();

    /**
     * When this ticket was created.
     */
    Instant getCreatedAt();

    /**
     * Whether this ticket has been closed.
     */
    boolean isClosed();

    void setClosed(boolean closed);

    /**
     * The players that has been assigned this ticket.
     */
    Collection<User> getAssigned();

    void assign(User user);

    void unassign(User user);

    /**
     * All comments on this ticket.
     */
    Collection<? extends Comment> getComments();

    Optional<Comment> getComment(int id);

    void removeComment(int id);

    Comment addComment(Comment.Template template);

    interface Template {

        static Builder builder() {
            return Sponge.getRegistry().getBuilderRegistry().provideBuilder(Builder.class);
        }

        User getCreator();

        Instant getCreatedAt();

        boolean isClosed();

        Collection<User> getAssigned();

        Collection<Comment.Template> getComments();

        interface Builder extends ResettableBuilder<Template, Builder> {

            Builder creator(User creator);

            Builder createdAt(Instant createdAt);

            Builder closed(boolean closed);

            Builder assigned(Iterable<User> users);

            Builder assigned(User... users);

            Builder comments(Iterable<Comment.Template> comments);

            Builder comments(Comment.Template... comments);

            Template build();
        }
    }
}