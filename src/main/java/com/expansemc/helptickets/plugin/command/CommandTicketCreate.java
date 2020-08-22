package com.expansemc.helptickets.plugin.command;

import com.expansemc.helptickets.plugin.HelpTickets;
import com.expansemc.helptickets.plugin.config.TicketsConfig;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.Player;

public class CommandTicketCreate implements CommandExecutor {

    private static final Parameter.Value<String> PARAM_MESSAGE = Parameter.string().setKey("message").build();

    public static final Command.Parameterized COMMAND = Command.builder()
            .setPermission("helptickets.ticket.create")
            .setExecutor(new CommandTicketCreate())
            .parameters(PARAM_MESSAGE)
            .build();

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        Player player = context.getCause().first(Player.class)
                .orElseThrow(() -> new CommandException(TextComponent.of("Only players can use this command!")));

        String message = context.requireOne(PARAM_MESSAGE);

        TicketsConfig.Ticket.Comment comment = new TicketsConfig.Ticket.Comment();
        comment.creator = player.getUniqueId();
        comment.message = message;
        comment.location = player.getServerLocation();

        TicketsConfig.Ticket ticket = new TicketsConfig.Ticket();
        ticket.creator = player.getUniqueId();
        ticket.comments.add(comment);

        int id = HelpTickets.TICKETS_CONFIG.addTicket(ticket);

        context.sendMessage(TextComponent.of("Ticket #" + id + " created!"));

        return CommandResult.success();
    }
}