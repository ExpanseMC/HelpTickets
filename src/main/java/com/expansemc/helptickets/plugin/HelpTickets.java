package com.expansemc.helptickets.plugin;

import com.expansemc.helptickets.plugin.command.CommandTicket;
import com.expansemc.helptickets.plugin.config.TicketsConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.jvm.Plugin;

@Plugin("helptickets")
public class HelpTickets {
    public static final Logger LOGGER = LogManager.getLogger("helptickets");

    public static TicketsConfig TICKETS_CONFIG = null;

    private PluginContainer container;

    @Listener
    public void onConstruct(ConstructPluginEvent event) {
        LOGGER.info("Constructing plugin...");

        this.container = event.getPlugin();

        TICKETS_CONFIG = new TicketsConfig();
    }

    @Listener
    public void onRegisterCommand(RegisterCommandEvent<Command.Parameterized> event) {
        LOGGER.info("Registering commands...");

        event.register(this.container, CommandTicket.COMMAND, "ticket");
    }
}