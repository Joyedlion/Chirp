package xyz.pgstudios.chirp.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.pgstudios.chirp.Chirp;

import java.util.ArrayList;
import java.util.List;

public class ChirpCommand implements CommandExecutor, TabCompleter {

    private final Chirp chirp;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public ChirpCommand(Chirp plugin) {
        this.chirp = plugin;
    }

    private Component getPrefix() {
        MiniMessage mm = MiniMessage.miniMessage();
        String prefix = chirp.getConfig().getString("prefix", "<#1be7ff><b>Chirp</#1be7ff></b> ");
        return mm.deserialize(prefix);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("chirp.admin.reload")) {
                sender.sendMessage(getPrefix().append(mm.deserialize("<red>You do not have permission to run <b>/chirp reload")));
                return true;
            }

            chirp.reloadPluginConfigs();

            sender.sendMessage(getPrefix().append(mm.deserialize("<green>The config has been reloaded!")));
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(mm.deserialize("<gradient:#1be7ff:#fffff>           PGstudios Help"));
            sender.sendMessage(mm.deserialize("<white>Support:").append(mm.deserialize("<#1be7ff> https://dsc.gg/pgstudios")));
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("author")) {
            sender.sendMessage(mm.deserialize("<gradient:#1be7ff:#fffff>                PGstudios"));
            sender.sendMessage(mm.deserialize("<red><b>Youtube </b><click:open_url:'https://youtube.com/@pgstudios'>https://youtube.com/@pgstudios"));
            sender.sendMessage(mm.deserialize("<green><b>Store </b><click:open_url:'https://store.pgstudios.xyz'>https://store.pgstudios.xyz"));
            sender.sendMessage(mm.deserialize("<blue><b>Discord </b><click:open_url:'https://dsc.gg/pgstudios'>https://dsc.gg/pgstudios"));
            return true;
        }
        if (args.length == 0){
            sender.sendMessage(getPrefix().append(mm.deserialize("<red>Usage: <b>/chirp help")));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            if (sender.hasPermission("staffx.reload") && "reload".startsWith(input)) {
                completions.add("reload");
            }
            completions.add("author");
            completions.add("help");
        }
        return completions;
    }
}
