package com.expansemc.helptickets.plugin.command;

import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

public class CommandTicket implements CommandExecutor {

    public static final Command.Parameterized COMMAND = Command.builder()
            .child(CommandTicketCreate.COMMAND, "create")
            .child(CommandTicketDelete.COMMAND, "delete")
            .child(CommandTicketInfo.COMMAND, "info")
            .child(CommandTicketList.COMMAND, "list")
            .child(CommandTicketReply.COMMAND, "reply")
            .child(CommandTicketTeleport.COMMAND, "teleport")
            .setPermission("helptickets.ticket.base")
            .setExecutor(new CommandTicket())
            .build();

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        context.sendMessage(TextComponent.of("TODO!"));
        return CommandResult.success();
    }
}