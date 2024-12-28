package mod.future.hypixel;

import mod.future.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoFriend {

    Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onFriend(ClientChatReceivedEvent e) {
        if (mc.thePlayer != null && mc.theWorld != null) {
            String message = e.message.getUnformattedText();

            Pattern pattern = Pattern.compile("Friend request from \\[[^]]+] (\\w+)");
            Matcher matcher = pattern.matcher(message);

            if (matcher.find()) {
                String username = matcher.group(1);

                if (ModConfig.autoF) {
                    mc.thePlayer.sendChatMessage("/f accept " + username);
                }

            }
        }
    }

}
