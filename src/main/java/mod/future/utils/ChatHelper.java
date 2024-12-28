package mod.future.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ChatHelper {

    protected static final Minecraft mc = Minecraft.getMinecraft();
    public static EnumChatFormatting color = EnumChatFormatting.WHITE;
    public static void addChatMessage(String message, String type) {
        if (mc.thePlayer != null && mc.theWorld != null) {
            switch (type) {
                case "info":
                    color = EnumChatFormatting.YELLOW;
                    break;
                case "error":
                    color = EnumChatFormatting.RED;
                    break;
                case "debug":
                    color = EnumChatFormatting.GREEN;
                    break;
                default:
                    color = EnumChatFormatting.WHITE;
                    break;
            }

            mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "[" + color + "PT" + EnumChatFormatting.DARK_GRAY + "] " + message));
        }
    }
    
}
