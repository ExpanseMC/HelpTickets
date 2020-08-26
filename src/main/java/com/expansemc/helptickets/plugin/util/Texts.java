package com.expansemc.helptickets.plugin.util;

import com.expansemc.helptickets.api.Comment;
import com.expansemc.helptickets.api.Ticket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.entity.living.player.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Texts {

    // --- SEPARATORS

    public static final TextComponent SEP_SPACE = TextComponent.space();
    public static final TextComponent SEP_COLON = TextComponent.of(": ", NamedTextColor.WHITE);

    // --- ERRORS

    public static final TextComponent ERROR_ONLY_ENTITIES = TextComponent.of("Only entities can use this command!");
    public static final TextComponent ERROR_ONLY_PLAYERS = TextComponent.of("Only players can use this command!");

    public static TextComponent errorUnknownTicket(int id) {
        return TextComponent.of("No ticket found with an id of " + id + ".");
    }

    // --- PAGINATION

    public static final TextComponent PADDING = TextComponent.of("-", NamedTextColor.GOLD);

    public static final TextComponent HEADER_TICKETS = TextComponent.of("Tickets", NamedTextColor.DARK_GREEN);

    public static TextComponent headerTicket(Ticket ticket) {
        return TextComponent.of("Ticket #" + ticket.getId(), NamedTextColor.DARK_GREEN);
    }

    public static TextComponent headerTicketAssignees(Ticket ticket) {
        return TextComponent.of("Ticket #" + ticket.getId() + " Assignees", NamedTextColor.DARK_GREEN);
    }

    public static TextComponent headerTicketComments(Ticket ticket) {
        return TextComponent.of("Ticket #" + ticket.getId() + " Comments", NamedTextColor.DARK_GREEN);
    }

    // --- GENERIC FIELDS

    public static TextComponent id(int id) {
        return TextComponent.of("#" + id, NamedTextColor.WHITE);
    }

    public static TextComponent createdAt(Instant createdAt) {
        return TextComponent.of(
                DateFormats.PRIMARY.format(LocalDateTime.ofInstant(createdAt, ZoneOffset.UTC)),
                NamedTextColor.GOLD
        );
    }

    public static TextComponent user(User user) {
        return TextComponent.of(user.getName(), NamedTextColor.GREEN);
    }

    // --- TICKETS

    public static final TextComponent TICKET_ID = TextComponent.of("ID: ", NamedTextColor.GRAY);
    public static final TextComponent TICKET_CREATOR = TextComponent.of("Creator: ", NamedTextColor.GRAY);
    public static final TextComponent TICKET_CREATED_AT = TextComponent.of("Created-At: ", NamedTextColor.GRAY);
    public static final TextComponent TICKET_ASSIGNED = TextComponent.of("Assigned: ", NamedTextColor.GRAY);
    public static final TextComponent TICKET_COMMENTS = TextComponent.of("Comments: ", NamedTextColor.GRAY);
    public static final TextComponent TICKET_IS_CLOSED = TextComponent.of("Closed? ", NamedTextColor.GRAY);

    public static List<Component> ticketInfo(Ticket ticket) {
        return Arrays.asList(
                TICKET_ID.append(id(ticket.getId())),
                TICKET_CREATOR.append(user(ticket.getCreator())),
                TICKET_CREATED_AT.append(createdAt(ticket.getCreatedAt())),
                TICKET_ASSIGNED.append(ticketAssigneeCount(ticket)),
                TICKET_COMMENTS.append(ticketCommentsCount(ticket)),
                TICKET_IS_CLOSED.append(TextComponent.of(ticket.isClosed(), NamedTextColor.WHITE))
        );
    }

    public static TextComponent ticketListItem(Ticket ticket) {
        return TextComponent.builder()
                .append(id(ticket.getId()).hoverEvent(createdAt(ticket.getCreatedAt())))
                .append(SEP_SPACE)
                .append(user(ticket.getCreator()))
                .append(SEP_COLON)
                .append(ticketMessage(ticket))
                .build();
    }

    public static List<Component> ticketAssignees(Ticket ticket) {
        return ticket.getAssigned().stream()
                .map(Texts::user)
                .collect(Collectors.toList());
    }

    public static TextComponent ticketAssigneeCount(Ticket ticket) {
        return TextComponent.of("[" + ticket.getAssigned().size() + " players]", NamedTextColor.AQUA)
                .clickEvent(ClickEvent.runCommand("/ticket info assigned " + ticket.getId()));
    }

    public static List<Component> ticketComments(Ticket ticket) {
        return ticket.getComments().stream()
                .map(comment -> commentListItem(ticket, comment))
                .collect(Collectors.toList());
    }

    public static TextComponent ticketCommentsCount(Ticket ticket) {
        return TextComponent.of("[" + ticket.getComments().size() + " comments]", NamedTextColor.AQUA)
                .clickEvent(ClickEvent.runCommand("/ticket info comments " + ticket.getId()));
    }

    public static TextComponent ticketMessage(Ticket ticket) {
        return TextComponent.of(ticket.getComments().iterator().next().getMessage(), NamedTextColor.WHITE);
    }

    // --- COMMENTS

    public static TextComponent commentListItem(Ticket ticket, Comment comment) {
        TextComponent.Builder builder = TextComponent.builder()
                .append(Texts.id(comment.getId()).hoverEvent(createdAt(comment.getCreatedAt())))
                .append(Texts.SEP_SPACE)
                .append(user(comment.getCreator()))
                .append(Texts.SEP_COLON)
                .append(commentMessage(comment));

        comment.getLocation().ifPresent(location -> {
            builder.hoverEvent(HoverEvent.showText(TextComponent.of("Click to teleport to this comment's location!", NamedTextColor.AQUA)));
            builder.clickEvent(ClickEvent.runCommand("/ticket teleport " + ticket.getId() + " " + comment.getId()));
        });

        return builder.build();
    }

    public static TextComponent commentMessage(Comment comment) {
        return TextComponent.of(comment.getMessage(), NamedTextColor.WHITE);
    }

    private Texts() {
    }
}