package com.expansemc.helptickets.plugin.command;

import com.expansemc.helptickets.api.HelpTicketsAPI;
import com.expansemc.helptickets.api.Ticket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.service.pagination.PaginationList;

import java.util.List;
import java.util.stream.Collectors;

public class CommandTicketInfoAssigned implements CommandExecutor {

    private static final Parameter.Value<Integer> PARAM_TICKET_ID =
            Parameter.integerNumber()
                    .setKey("ticket-id")
                    .setSuggestions(context ->
                            HelpTicketsAPI.getInstance().getTickets()
                                    .stream()
                                    .map(ticket -> Integer.toString(ticket.getId()))
                                    .collect(Collectors.toList()))
                    .build();

    public static final Command.Parameterized COMMAND = Command.builder()
            .setPermission("helptickets.ticket.info.assigned")
            .setExecutor(new CommandTicketInfoAssigned())
            .parameters(PARAM_TICKET_ID)
            .build();

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        int ticketId = context.requireOne(PARAM_TICKET_ID);

        Ticket ticket = HelpTicketsAPI.getInstance().getTicket(ticketId)
                .orElseThrow(() -> new CommandException(TextComponent.of("No ticket with id " + ticketId + ".")));

        List<Component> contents = ticket.getAssigned().stream()
                .map(user -> TextComponent.of(user.getName(), NamedTextColor.GREEN))
                .collect(Collectors.toList());

        PaginationList.builder()
                .title(TextComponent.of("Assigned to Ticket #" + ticketId, NamedTextColor.DARK_GREEN))
                .padding(TextComponent.of("=", NamedTextColor.GOLD))
                .contents(contents)
                .build()
                .sendTo(context.getCause().getAudience());

        return CommandResult.success();
    }
}