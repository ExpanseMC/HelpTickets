package com.expansemc.helptickets.plugin.command;

import com.expansemc.helptickets.api.Comment;
import com.expansemc.helptickets.api.HelpTicketsAPI;
import com.expansemc.helptickets.api.Ticket;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.time.Instant;

public class CommandTicketCreate implements CommandExecutor {

    private static final Parameter.Value<String> PARAM_MESSAGE =
            Parameter.remainingJoinedStrings()
                    .setKey("message")
                    .build();

    public static final Command.Parameterized COMMAND = Command.builder()
            .setPermission("helptickets.ticket.create")
            .setExecutor(new CommandTicketCreate())
            .parameters(PARAM_MESSAGE)
            .build();

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        ServerPlayer player = context.getCause().first(ServerPlayer.class)
                .orElseThrow(() -> new CommandException(TextComponent.of("Only players can use this command!")));

        String message = context.requireOne(PARAM_MESSAGE);

        Comment.Template comment = Comment.Template.builder()
                .creator(player.getUser())
                .message(message)
                .location(player.getServerLocation())
                .build();

        Ticket.Template ticket = Ticket.Template.builder()
                .creator(player.getUser())
                .createdAt(Instant.now())
                .closed(false)
                .comments(comment)
                .build();

        Ticket added = HelpTicketsAPI.getInstance().addTicket(ticket);

        context.sendMessage(TextComponent.of("Ticket #" + added.getId() + " created!"));

        return CommandResult.success();
    }
}