package com.expansemc.helptickets.api;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.util.ResettableBuilder;
import org.spongepowered.api.world.ServerLocation;

import java.util.Optional;

@DefaultQualifier(NonNull.class)
public interface Comment {

    int getId();

    /**
     * The player that created this comment.
     */
    User getCreator();

    /**
     * This comment's message.
     */
    String getMessage();

    /**
     * The location where this comment was created.
     */
    Optional<ServerLocation> getLocation();

    interface Template {

        static Builder builder() {
            return Sponge.getRegistry().getBuilderRegistry().provideBuilder(Builder.class);
        }

        User getCreator();

        String getMessage();

        Optional<ServerLocation> getLocation();

        interface Builder extends ResettableBuilder<Template, Builder> {

            Builder creator(User creator);

            Builder message(String message);

            Builder location(@Nullable ServerLocation location);

            Template build();
        }
    }
}