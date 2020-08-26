package com.expansemc.helptickets.plugin;

import com.expansemc.helptickets.api.Comment;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.world.ServerLocation;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@DefaultQualifier(NonNull.class)
public class CommentTemplateImpl implements Comment.Template {

    private final User creator;
    private final Instant createdAt;
    private final String message;
    @Nullable
    private final ServerLocation location;

    public CommentTemplateImpl(User creator, Instant createdAt, String message, @Nullable ServerLocation location) {
        this.creator = creator;
        this.createdAt = createdAt;
        this.message = message;
        this.location = location;
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
    public String getMessage() {
        return this.message;
    }

    @Override
    public Optional<ServerLocation> getLocation() {
        return Optional.ofNullable(this.location);
    }

    public static class BuilderImpl implements Comment.Template.Builder {

        @Nullable
        private User creator = null;
        private Instant createdAt = Instant.now();
        @Nullable
        private String message = null;
        @Nullable
        private ServerLocation location = null;

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
        public Builder message(String message) {
            this.message = Objects.requireNonNull(message, "message");
            return this;
        }

        @Override
        public Builder location(@Nullable ServerLocation location) {
            this.location = location;
            return this;
        }

        @Override
        public Comment.Template build() {
            return new CommentTemplateImpl(
                    Objects.requireNonNull(this.creator, "creator"),
                    this.createdAt,
                    Objects.requireNonNull(this.message, "message"),
                    this.location
            );
        }

        @Override
        public Builder reset() {
            this.creator = null;
            this.createdAt = Instant.now();
            this.message = null;
            this.location = null;
            return this;
        }
    }
}