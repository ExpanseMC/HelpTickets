package com.expansemc.helptickets.plugin.command;

import com.expansemc.helptickets.api.HelpTicketsAPI;
import com.expansemc.helptickets.api.Ticket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.service.pagination.PaginationList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandTicketInfo implements CommandExecutor {

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
            .child(CommandTicketInfoAssigned.COMMAND, "assigned")
            .child(CommandTicketInfoComments.COMMAND, "comments")
            .setPermission("helptickets.ticket.info.base")
            .setExecutor(new CommandTicketInfo())
            .parameters(PARAM_TICKET_ID)
            .build();

    private static final Component FIELD_ID = TextComponent.of("ID: ", NamedTextColor.GRAY);
    private static final Component FIELD_CREATOR = TextComponent.of("Creator: ", NamedTextColor.GRAY);
    private static final Component FIELD_CREATED_AT = TextComponent.of("Created At: ", NamedTextColor.GRAY);
    private static final Component FIELD_ASSIGNED = TextComponent.of("Assigned: ", NamedTextColor.GRAY);
    private static final Component FIELD_COMMENTS = TextComponent.of("Comments: ", NamedTextColor.GRAY);
    private static final Component FIELD_IS_CLOSED = TextComponent.of("Closed? ", NamedTextColor.GRAY);

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        int ticketId = context.requireOne(PARAM_TICKET_ID);

        Ticket ticket = HelpTicketsAPI.getInstance().getTicket(ticketId)
                .orElseThrow(() -> new CommandException(TextComponent.of("No ticket with id " + ticketId + ".")));

        List<Component> contents = new ArrayList<>();
        contents.add(FIELD_ID.append(TextComponent.of(ticketId, NamedTextColor.GREEN)));
        contents.add(FIELD_CREATOR.append(TextComponent.of(ticket.getCreator().getName(), NamedTextColor.GREEN)));
        contents.add(FIELD_CREATED_AT.append(TextComponent.of(ticket.getCreatedAt().toString(), NamedTextColor.GREEN)));
        contents.add(FIELD_ASSIGNED.append(TextComponent.of("[" + ticket.getAssigned().size() + " players]", NamedTextColor.AQUA)
                .clickEvent(ClickEvent.runCommand("/ticket info assigned " + ticketId))));
        contents.add(FIELD_COMMENTS.append(TextComponent.of("[" + ticket.getComments().size() + " comments]", NamedTextColor.AQUA)
                .clickEvent(ClickEvent.runCommand("/ticket info comments " + ticketId))));
        contents.add(FIELD_IS_CLOSED.append(TextComponent.of(ticket.isClosed(), NamedTextColor.GREEN)));

        PaginationList.builder()
                .title(TextComponent.of("Ticket #" + ticketId, NamedTextColor.DARK_GREEN))
                .padding(TextComponent.of("=", NamedTextColor.GOLD))
                .contents(contents)
                .build()
                .sendTo(context.getCause().getAudience());

        return CommandResult.success();
    }
}