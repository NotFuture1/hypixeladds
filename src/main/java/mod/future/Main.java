package mod.future;

import mod.future.commands.BanCount;
import mod.future.commands.Status;
import mod.future.hypixel.AutoFriend;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.client.ClientCommandHandler;


@Mod(name = Main.NAME, modid = Main.MODID, version = Main.VERSION, useMetadata = true)
public class Main {

    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";
    public static final String MODID = "@ID@";
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new AutoFriend());

        ClientCommandHandler.instance.registerCommand(new BanCount());
        ClientCommandHandler.instance.registerCommand(new Status());
    }
}
