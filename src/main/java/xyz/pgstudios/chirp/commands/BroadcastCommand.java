package xyz.pgstudios.chirp.commands;

import kotlin._Assertions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.pgstudios.chirp.Chirp;

public class BroadcastCommand implements CommandExecutor {
    private final Chirp chirp;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public BroadcastCommand(Chirp chirp) {
        this.chirp = chirp;
    }

    private Component getPrefix() {
        MiniMessage mm = MiniMessage.miniMessage();
        String prefix = chirp.getConfig().getString("prefix", "<#1be7ff><b>Chirp</#1be7ff></b> ");
        return mm.deserialize(prefix);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(getPrefix().append(mm.deserialize("<red>You must add a message to send!")));
            return true;
        }
        String fullmessage = String.join(" ", args);
        Bukkit.broadcast(getPrefix().append(mm.deserialize("<gray>| <reset>" + fullmessage)));
        return true;
    }
}
