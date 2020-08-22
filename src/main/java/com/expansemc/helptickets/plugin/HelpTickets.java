package com.expansemc.helptickets.plugin;

import com.expansemc.helptickets.api.Comment;
import com.expansemc.helptickets.api.HelpTicketsAPI;
import com.expansemc.helptickets.api.Ticket;
import com.expansemc.helptickets.plugin.command.CommandTicket;
import com.expansemc.helptickets.plugin.config.TicketsConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.api.event.lifecycle.RegisterBuilderEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.RegisterFactoryEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.jvm.Plugin;

/**
 * A simple ticket management plugin.
 *
 * The {@link Plugin} annotation has changed. Now, only the plugin id is
 * specified in it. Everything else is specified in the "META-INF/plugins.json".
 */
@Plugin("helptickets")
public class HelpTickets {
    public static final Logger LOGGER = LogManager.getLogger("helptickets");

    private PluginContainer container;

    /**
     * Semi-replacement for API-7's
     * {@link org.spongepowered.api.event.game.state.GameConstructionEvent}.
     */
    @Listener
    public void onConstruct(ConstructPluginEvent event) {
        LOGGER.info("Constructing plugin...");

        this.container = event.getPlugin();
    }

    @Listener
    public void onRegisterBuilder(RegisterBuilderEvent event) {
        event.register(Comment.Template.Builder.class, CommentTemplateImpl.BuilderImpl::new);
        event.register(Ticket.Template.Builder.class, TicketTemplateImpl.BuilderImpl::new);
    }

    @Listener
    public void onRegisterFactory(RegisterFactoryEvent event) {
        event.register(HelpTicketsAPI.class, new TicketsConfig());
    }

    /**
     * There is now a special registry event for {@link Command}s, rather than
     * the old {@link org.spongepowered.api.command.CommandManager}.
     */
    @Listener
    public void onRegisterCommand(RegisterCommandEvent<Command.Parameterized> event) {
        LOGGER.info("Registering commands...");

        event.register(this.container, CommandTicket.COMMAND, "ticket");
    }
}