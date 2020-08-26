package com.expansemc.helptickets.plugin.command;

import com.expansemc.helptickets.api.HelpTicketsAPI;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.Collections;
import java.util.stream.Collectors;

public final class Parameters {

    public static final Parameter.Value<Integer> PARAM_TICKET_ID =
            Parameter.integerNumber()
                    .setKey("ticket-id")
                    .setSuggestions(context ->
                            HelpTicketsAPI.getInstance().getTickets()
                                    .stream()
                                    .map(ticket -> Integer.toString(ticket.getId()))
                                    .collect(Collectors.toList()))
                    .build();

    public static final Parameter.Value<Integer> PARAM_COMMENT_ID =
            Parameter.integerNumber()
                    .setKey("comment-id")
                    .setSuggestions(context ->
                            HelpTicketsAPI.getInstance().getTicket(context.requireOne(PARAM_TICKET_ID))
                                    .map(ticket -> ticket.getComments().stream()
                                            .map(comment -> Integer.toString(comment.getId()))
                                            .collect(Collectors.toList()))
                                    .orElse(Collections.emptyList()))
                    .build();

    private Parameters() {
    }
}