package com.expansemc.helptickets.plugin.command;

import com.expansemc.helptickets.plugin.HelpTickets;
import com.expansemc.helptickets.plugin.config.TicketsConfig;
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

    private static final Parameter.Value<Integer> PARAM_ID =
            Parameter.integerNumber()
                    .setKey("id")
                    .setSuggestions(context -> HelpTickets.TICKETS_CONFIG.tickets.keySet()
                            .stream()
                            .map(Object::toString)
                            .collect(Collectors.toList()))
                    .build();

    public static final Command.Parameterized COMMAND = Command.builder()
            .setPermission("helptickets.ticket.info")
            .setExecutor(new CommandTicketInfo())
            .parameters(PARAM_ID)
            .build();

    private static final Component FIELD_ID = TextComponent.of("ID: ", NamedTextColor.GRAY);
    private static final Component FIELD_CREATOR = TextComponent.of("Creator: ", NamedTextColor.GRAY);
    private static final Component FIELD_CREATED_AT = TextComponent.of("Created At: ", NamedTextColor.GRAY);
    private static final Component FIELD_ASSIGNED = TextComponent.of("Assigned: ", NamedTextColor.GRAY);
    private static final Component FIELD_COMMENTS = TextComponent.of("Comments: ", NamedTextColor.GRAY);
    private static final Component FIELD_IS_CLOSED = TextComponent.of("Closed? ", NamedTextColor.GRAY);

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        int id = context.requireOne(PARAM_ID);

        TicketsConfig.Ticket ticket = HelpTickets.TICKETS_CONFIG.tickets.get(id);

        if (ticket == null) {
            throw new CommandException(TextComponent.of("No ticket with id " + id + "."));
        }

        List<Component> contents = new ArrayList<>();
        contents.add(FIELD_ID.append(TextComponent.of(id, NamedTextColor.GREEN)));
        contents.add(FIELD_CREATOR.append(TextComponent.of(ticket.getCreatorUser().getName(), NamedTextColor.GREEN)));
        contents.add(FIELD_CREATED_AT.append(TextComponent.of(ticket.createdAt.toString(), NamedTextColor.GREEN)));
        contents.add(FIELD_ASSIGNED.append(TextComponent.of("[" + ticket.assigned.size() + " players]", NamedTextColor.AQUA)
                .clickEvent(ClickEvent.runCommand("/ticket list assigned " + id))));
        contents.add(FIELD_ASSIGNED.append(TextComponent.of("[" + ticket.comments.size() + " comments]", NamedTextColor.AQUA)
                .clickEvent(ClickEvent.runCommand("/ticket list comments " + id))));
        contents.add(FIELD_IS_CLOSED.append(TextComponent.of(ticket.isClosed, NamedTextColor.GREEN)));

        PaginationList.builder()
                .title(TextComponent.of("Ticket #" + id, NamedTextColor.DARK_GREEN))
                .padding(TextComponent.of("=", NamedTextColor.GOLD))
                .contents(contents)
                .build()
                .sendTo(context.getCause().getAudience());

        return CommandResult.success();
    }
}