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
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import static com.expansemc.helptickets.plugin.command.Parameters.PARAM_TICKET_ID;

public class CommandTicketReply implements CommandExecutor {

    private static final Parameter.Value<String> PARAM_MESSAGE =
            Parameter.remainingJoinedStrings()
                    .setKey("message")
                    .build();

    public static final Command.Parameterized COMMAND = Command.builder()
            .setPermission("helptickets.ticket.reply")
            .setExecutor(new CommandTicketReply())
            .parameters(PARAM_TICKET_ID, PARAM_MESSAGE)
            .build();

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        ServerPlayer player = context.getCause().first(ServerPlayer.class)
                .orElseThrow(() -> new CommandException(Texts.ERROR_ONLY_PLAYERS));

        int ticketId = context.requireOne(PARAM_TICKET_ID);

        Ticket ticket = HelpTicketsAPI.getInstance().getTicket(ticketId)
                .orElseThrow(() -> new CommandException(Texts.errorUnknownTicket(ticketId)));

        String message = context.requireOne(PARAM_MESSAGE);

        Comment.Template template = Comment.Template.builder()
                .creator(player.getUser())
                .message(message)
                .location(player.getServerLocation())
                .build();

        ticket.addComment(template);

        context.sendMessage(TextComponent.of("Replied to ticket #" + ticket.getId() + "."));

        return CommandResult.success();
    }
}