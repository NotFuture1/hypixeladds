package mod.future.commands;

import com.google.gson.JsonObject;
import mod.future.utils.ApiHelper;
import mod.future.utils.ChatHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Status extends CommandBase {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    @Override
    public String getCommandName() {
        return "status";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/status [username]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (!(sender instanceof EntityPlayer)) return;
        if (args.length < 1) {
            ChatHelper.addChatMessage("Usage: " + getCommandUsage(sender), "error");
            return;
        }
    
        String playerName = args[0];
        new Thread(() -> {
            String uuid = ApiHelper.getUUID(playerName);

            if (uuid == null) {
                ChatHelper.addChatMessage("Player not found: " + playerName, "error");
                return;
            }

            JsonObject response = ApiHelper.getJsonResponse("https://api.hypixel.net/v2/status?uuid=" + uuid + "&key=" + ApiHelper.key);
            if (response == null || !response.get("success").getAsBoolean()) {
                ChatHelper.addChatMessage("Failed to fetch status for player: " + playerName, "error");
                return;
            }

            JsonObject session = response.getAsJsonObject("session");
            if (session == null || !session.has("online")) {
                ChatHelper.addChatMessage("No session data available for player: " + playerName, "error");
                return;
            }

            boolean isOnline = session.get("online").getAsBoolean();
            if (isOnline) {
                String gameType = session.has("gameType") ? session.get("gameType").getAsString() : "Unknown";
                String mode = session.has("mode") ? session.get("mode").getAsString() : "Unknown";

                ChatHelper.addChatMessage(EnumChatFormatting.BOLD + "" + EnumChatFormatting.WHITE + playerName + " is " +
                        EnumChatFormatting.GREEN + "online" + EnumChatFormatting.WHITE + "!", "info");
                ChatHelper.addChatMessage(EnumChatFormatting.BOLD + "" + EnumChatFormatting.WHITE + "Game Type: " +
                        EnumChatFormatting.RESET + EnumChatFormatting.YELLOW + gameType, "info");
                ChatHelper.addChatMessage(EnumChatFormatting.BOLD + "" + EnumChatFormatting.WHITE + "Mode: " +
                        EnumChatFormatting.RESET + EnumChatFormatting.GOLD + mode, "info");
            } else {
                ChatHelper.addChatMessage(EnumChatFormatting.BOLD + "" + EnumChatFormatting.WHITE + playerName + " is " +
                        EnumChatFormatting.RED + "offline" + EnumChatFormatting.RESET + ".", "info");
            }
        }).start();
    }



    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(@NotNull ICommand o) {
        return 0;
    }
}
