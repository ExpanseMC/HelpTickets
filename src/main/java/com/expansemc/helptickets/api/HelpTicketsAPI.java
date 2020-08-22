package com.expansemc.helptickets.api;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.api.Sponge;

import java.util.Collection;
import java.util.Optional;

@DefaultQualifier(NonNull.class)
public interface HelpTicketsAPI {

    static HelpTicketsAPI getInstance() {
        return Sponge.getRegistry().getFactoryRegistry().provideFactory(HelpTicketsAPI.class);
    }

    Collection<? extends Ticket> getTickets();

    Optional<Ticket> getTicket(int id);

    void removeTicket(int id);

    Ticket addTicket(Ticket.Template template);
}