package com.expansemc.helptickets.plugin.command;

import com.expansemc.helptickets.api.HelpTicketsAPI;
import com.expansemc.helptickets.api.Ticket;
import com.expansemc.helptickets.plugin.util.Texts;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.service.pagination.PaginationList;

import static com.expansemc.helptickets.plugin.command.Parameters.PARAM_TICKET_ID;

public class CommandTicketInfoComments implements CommandExecutor {

    public static final Command.Parameterized COMMAND = Command.builder()
            .setPermission("helptickets.ticket.info.comments")
            .setExecutor(new CommandTicketInfoComments())
            .parameters(PARAM_TICKET_ID)
            .build();

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        int ticketId = context.requireOne(PARAM_TICKET_ID);

        Ticket ticket = HelpTicketsAPI.getInstance().getTicket(ticketId)
                .orElseThrow(() -> new CommandException(Texts.errorUnknownTicket(ticketId)));

        PaginationList.builder()
                .title(Texts.headerTicketComments(ticket))
                .padding(Texts.PADDING)
                .contents(Texts.ticketComments(ticket))
                .build()
                .sendTo(context.getCause().getAudience());

        return CommandResult.success();
    }
}