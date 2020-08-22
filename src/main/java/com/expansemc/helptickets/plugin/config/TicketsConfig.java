package com.expansemc.helptickets.plugin.config;

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

@ConfigSerializable
@DefaultQualifier(NonNull.class)
public class TicketsConfig {

    @Setting
    private int nextId = 1;

    @Setting
    public Map<Integer, Ticket> tickets = new HashMap<>();

    public int addTicket(Ticket ticket) {
        int id = this.nextId++;
        ticket.id = id;
        tickets.put(id, ticket);
        return id;
    }

    @ConfigSerializable
    public static class Ticket {

        /**
         * The unique id of this ticket.
         */
        @Setting
        public int id;

        /**
         * The player that created this ticket.
         */
        @Setting
        public UUID creator;

        /**
         * When this ticket was created.
         */
        @Setting
        public Instant createdAt = Instant.now();

        /**
         * The player(s) that has been assigned this ticket.
         */
        @Setting
        public List<UUID> assigned = new ArrayList<>();

        /**
         * All comments on this ticket.
         */
        @Setting
        public List<Comment> comments = new ArrayList<>();

        /**
         * Whether this ticket has been closed.
         */
        @Setting
        public boolean isClosed = false;

        public User getCreatorUser() {
            return Sponge.getServer().getUserManager().getOrCreate(GameProfile.of(this.creator));
        }

        @ConfigSerializable
        public static class Comment {

            /**
             * The player that created this comment.
             */
            @Setting
            public UUID creator;

            /**
             * This comment's message.
             */
            @Setting
            public String message;

            /**
             * The location where this comment was created.
             */
            @Setting
            @Nullable
            public ServerLocation location = null;
        }
    }
}