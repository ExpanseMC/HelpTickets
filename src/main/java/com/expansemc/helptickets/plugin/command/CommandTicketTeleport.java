package com.expansemc.helptickets.plugin.command;

import com.expansemc.helptickets.api.Comment;
import com.expansemc.helptickets.api.HelpTicketsAPI;
import com.expansemc.helptickets.api.Ticket;
import com.expansemc.helptickets.plugin.HelpTickets;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.ServerLocation;

import java.util.Collections;
import java.util.stream.Collectors;

public class CommandTicketTeleport implements CommandExecutor {

    private static final Parameter.Value<Integer> PARAM_TICKET_ID =
            Parameter.integerNumber()
                    .setKey("ticket-id")
                    .setSuggestions(context ->
                            HelpTicketsAPI.getInstance().getTickets()
                                    .stream()
                                    .map(ticket -> Integer.toString(ticket.getId()))
                                    .collect(Collectors.toList()))
                    .build();

    private static final Parameter.Value<Integer> PARAM_COMMENT_ID =
            Parameter.integerNumber()
                    .setKey("comment-id")
                    .setSuggestions(context ->
                            HelpTicketsAPI.getInstance().getTicket(context.requireOne(PARAM_TICKET_ID))
                                    .map(ticket -> ticket.getComments().stream()
                                            .map(comment -> Integer.toString(comment.getId()))
                                            .collect(Collectors.toList()))
                                    .orElse(Collections.emptyList()))
                    .build();

    public static final Command.Parameterized COMMAND = Command.builder()
            .setPermission("helptickets.ticket.teleport")
            .setExecutor(new CommandTicketTeleport())
            .parameter(PARAM_TICKET_ID)
            .build();

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        Entity entity = context.getCause().first(Entity.class)
                .orElseThrow(() -> new CommandException(TextComponent.of("Only entities can use this command!")));

        int ticketId = context.requireOne(PARAM_TICKET_ID);
        int commentId = context.requireOne(PARAM_COMMENT_ID);

        Ticket ticket = HelpTicketsAPI.getInstance().getTicket(ticketId)
                .orElseThrow(() -> new CommandException(TextComponent.of("No ticket with id " + ticketId + ".")));
        Comment comment = ticket.getComment(commentId)
                .orElseThrow(() -> new CommandException(TextComponent.of("No comment with id " + commentId + ".")));

        ServerLocation location = comment.getLocation()
                .orElseThrow(() -> new CommandException(TextComponent.of("Comment has no location.")));

        entity.setLocation(location);

        return CommandResult.success();
    }
}