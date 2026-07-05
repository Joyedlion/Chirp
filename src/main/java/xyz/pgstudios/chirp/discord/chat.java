package xyz.pgstudios.chirp.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import xyz.pgstudios.chirp.Chirp;

public class chat extends ListenerAdapter {
    private final Chirp chirp;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public chat(Chirp chirp) {
        this.chirp = chirp;
    }
    private Component getPrefix() {
        MiniMessage mm = MiniMessage.miniMessage();
        String prefix = chirp.getDiscordConfig().getString("chat.prefix", "<#1be7ff><b>Chirp</#1be7ff></b> ");
        return mm.deserialize(prefix);
    }
    public void OnMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;

        String chatid = chirp.getDiscordConfig().getString("chat.channel_id");

        if (e.getChannel().getId().equals(chatid)) {
            String userTag = e.getAuthor().getName();
            String messageContent = e.getMessage().getContentRaw();
            String format = getPrefix() + userTag + "<gray>: <reset>" + messageContent;
            Bukkit.broadcast(getPrefix().append(mm.deserialize(format)));
        }
    }
}
