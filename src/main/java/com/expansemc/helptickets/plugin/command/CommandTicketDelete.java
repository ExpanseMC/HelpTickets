package com.expansemc.helptickets.plugin.command;

import com.expansemc.helptickets.api.HelpTicketsAPI;
import com.expansemc.helptickets.api.Ticket;
import com.expansemc.helptickets.plugin.util.Texts;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import static com.expansemc.helptickets.plugin.command.Parameters.PARAM_TICKET_ID;

public class CommandTicketDelete implements CommandExecutor {

    public static final Command.Parameterized COMMAND = Command.builder()
            .setPermission("helptickets.ticket.delete")
            .setExecutor(new CommandTicketDelete())
            .parameters(PARAM_TICKET_ID)
            .build();

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        int ticketId = context.requireOne(PARAM_TICKET_ID);

        Ticket ticket = HelpTicketsAPI.getInstance().getTicket(ticketId)
                .orElseThrow(() -> new CommandException(Texts.errorUnknownTicket(ticketId)));

        context.sendMessage(TextComponent.of("Ticket #" + ticket.getId() + " deleted."));

        return CommandResult.success();
    }
}