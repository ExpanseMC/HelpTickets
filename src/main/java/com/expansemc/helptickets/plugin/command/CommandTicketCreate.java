package com.expansemc.helptickets.plugin.command;

import com.expansemc.helptickets.api.Comment;
import com.expansemc.helptickets.api.HelpTicketsAPI;
import com.expansemc.helptickets.api.Ticket;
import com.expansemc.helptickets.plugin.util.Texts;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
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
                .orElseThrow(() -> new CommandException(Texts.ERROR_ONLY_PLAYERS));

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

        TextComponent successMessage = TextComponent.builder()
                .append(TextComponent.of("Ticket "))
                .append(TextComponent.builder()
                        .content("#" + added.getId())
                        .color(NamedTextColor.AQUA)
                        .hoverEvent(HoverEvent.showText(
                                TextComponent.of("Click here to see ticket information!", NamedTextColor.AQUA)))
                        .clickEvent(ClickEvent.runCommand("/ticket info " + added.getId()))
                        .build())
                .append(" created.")
                .build();

        context.sendMessage(successMessage);

        return CommandResult.success();
    }
}