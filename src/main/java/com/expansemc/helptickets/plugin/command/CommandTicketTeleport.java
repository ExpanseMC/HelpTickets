package com.expansemc.helptickets.plugin.command;

import com.expansemc.helptickets.api.Comment;
import com.expansemc.helptickets.api.HelpTicketsAPI;
import com.expansemc.helptickets.api.Ticket;
import com.expansemc.helptickets.plugin.util.Texts;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.ServerLocation;

import static com.expansemc.helptickets.plugin.command.Parameters.PARAM_COMMENT_ID;
import static com.expansemc.helptickets.plugin.command.Parameters.PARAM_TICKET_ID;

public class CommandTicketTeleport implements CommandExecutor {

    public static final Command.Parameterized COMMAND = Command.builder()
            .setPermission("helptickets.ticket.teleport")
            .setExecutor(new CommandTicketTeleport())
            .parameter(PARAM_TICKET_ID)
            .build();

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        Entity entity = context.getCause().first(Entity.class)
                .orElseThrow(() -> new CommandException(Texts.ERROR_ONLY_ENTITIES));

        int ticketId = context.requireOne(PARAM_TICKET_ID);
        int commentId = context.requireOne(PARAM_COMMENT_ID);

        Ticket ticket = HelpTicketsAPI.getInstance().getTicket(ticketId)
                .orElseThrow(() -> new CommandException(Texts.errorUnknownTicket(ticketId)));
        Comment comment = ticket.getComment(commentId)
                .orElseThrow(() -> new CommandException(TextComponent.of("No comment with id " + commentId + ".")));

        ServerLocation location = comment.getLocation()
                .orElseThrow(() -> new CommandException(TextComponent.of("Comment has no location.")));

        entity.setLocation(location);

        return CommandResult.success();
    }
}