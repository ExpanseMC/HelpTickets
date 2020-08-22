package com.expansemc.helptickets.plugin.command;

import com.expansemc.helptickets.plugin.HelpTickets;
import com.expansemc.helptickets.plugin.config.TicketsConfig;
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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CommandTicketList implements CommandExecutor {

    private static final Parameter.Value<SortBy> PARAM_SORT_BY =
            Parameter.enumValue(SortBy.class).orDefault(SortBy.ID).consumeAllRemaining().setKey("sort-by").build();

    public static final Command.Parameterized COMMAND = Command.builder()
            .setPermission("helptickets.ticket.list")
            .setExecutor(new CommandTicketList())
            .parameters(PARAM_SORT_BY)
            .build();

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        Comparator<TicketsConfig.Ticket> sorter = context.getAll(PARAM_SORT_BY).stream()
                .map(SortBy::getComparator)
                .reduce(Comparator::thenComparing)
                .orElseThrow(() -> new CommandException(TextComponent.of("No sort-by provided!")));

        List<Component> ticketTexts = HelpTickets.TICKETS_CONFIG.tickets.values().stream()
                .sorted(sorter)
                .map(ticket -> TextComponent.of("#" + ticket.id + ": " + ticket.getCreatorUser().getName() + " at " + ticket.createdAt))
                .collect(Collectors.toList());

        PaginationList.builder()
                .title(TextComponent.of("Tickets", NamedTextColor.DARK_GREEN))
                .padding(TextComponent.of("-", NamedTextColor.GOLD))
                .contents(ticketTexts)
                .build()
                .sendTo(context.getCause().getAudience());

        return CommandResult.success();
    }

    private enum SortBy {
        ID((a, b) -> Integer.compare(a.id, b.id)),
        CREATOR_NAME((a, b) -> a.getCreatorUser().getName().compareTo(b.getCreatorUser().getName())),
        CREATED_AT((a, b) -> a.createdAt.compareTo(b.createdAt)),
        IS_ASSIGNED((a, b) -> Integer.compare(a.assigned.size(), b.assigned.size())),
        IS_OPEN((a, b) -> Boolean.compare(!a.isClosed, !b.isClosed));

        private final Comparator<TicketsConfig.Ticket> comparator;

        SortBy(Comparator<TicketsConfig.Ticket> comparator) {
            this.comparator = comparator;
        }

        public Comparator<TicketsConfig.Ticket> getComparator() {
            return comparator;
        }
    }
}