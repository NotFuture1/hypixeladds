package mod.future.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mod.future.utils.ApiHelper;
import mod.future.utils.ChatHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class BanCount extends CommandBase {

    @Override
    public String getCommandName() {
        return "banstats";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/banstats";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("bs");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (sender != null) {
            new Thread(() -> {
                String response = String.valueOf(ApiHelper.getJsonResponse("https://api.hypixel.net/v2/punishmentstats?key=" + ApiHelper.key));
                if (response == null) {
                    ChatHelper.addChatMessage("Failed to fetch ban statistics!", "error");
                    return;
                }

                try {
                    JsonObject json = new JsonParser().parse(response).getAsJsonObject();
                    if (json.get("success").getAsBoolean()) {
                        int watchdogLastMinute = json.get("watchdog_lastMinute").getAsInt();
                        int staffRollingDaily = json.get("staff_rollingDaily").getAsInt();
                        int watchdogRollingDaily = json.get("watchdog_rollingDaily").getAsInt();

                        Minecraft.getMinecraft().addScheduledTask(() -> {
                            ChatHelper.addChatMessage(EnumChatFormatting.BOLD + "" + EnumChatFormatting.GRAY + "Staff Daily: " +
                                    EnumChatFormatting.RESET + EnumChatFormatting.YELLOW + staffRollingDaily, "info");
                            ChatHelper.addChatMessage(EnumChatFormatting.BOLD + "" + EnumChatFormatting.GRAY + "Watchdog Daily: " +
                                    EnumChatFormatting.RESET + EnumChatFormatting.RED + watchdogRollingDaily, "info");
                            ChatHelper.addChatMessage(EnumChatFormatting.BOLD + "" + EnumChatFormatting.GRAY + "Watchdog Last Minute: " +
                                    EnumChatFormatting.RESET + EnumChatFormatting.GOLD + watchdogLastMinute, "info");
                        });
                    } else {
                        ChatHelper.addChatMessage("Failed to fetch ban statistics!", "error");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ChatHelper.addChatMessage("An error occurred while parsing ban statistics!", "error");
                }
            }).start();
        }
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
