package mod.future.hypixel;

import mod.future.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.HashMap;

public class AutoGuildWelcome {

    Minecraft mc = Minecraft.getMinecraft();
    private final HashMap<String, Long> lastJoinTimestamps = new HashMap<>();

    public void onGuildMemeberJoin(ClientChatReceivedEvent e) {
        String[] parts = e.message.getUnformattedText().split(" ");
        if (parts.length > 2 && parts[0].equals("Guild") && parts[1].equals(">")) {
            String username = parts[2];

            long now = System.currentTimeMillis();

            if (!lastJoinTimestamps.containsKey(username) || (now - lastJoinTimestamps.get(username)) > 5 * 60 * 1000) {
                if (ModConfig.autoG) {
                    mc.thePlayer.sendChatMessage("/gc Hello " + username + "! ^_^");
                }
                lastJoinTimestamps.put(username, now);
            }
        }
    }
}
