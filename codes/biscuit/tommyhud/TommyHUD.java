package codes.biscuit.tommyhud;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.command.TommyHUDCommand;
import codes.biscuit.tommyhud.config.ConfigManager;
import codes.biscuit.tommyhud.listener.Listener;
import codes.biscuit.tommyhud.misc.scheduler.Scheduler;
import codes.biscuit.tommyhud.render.Renderer;
import codes.biscuit.tommyhud.util.Utils;
import java.util.Map;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = "tommyhud",
    name = "TommyHUD",
    version = "1.0",
    clientSideOnly = true,
    acceptedMinecraftVersions = "[1.8.9]"
)
public class TommyHUD {
    private static TommyHUD instance;
    private final Logger logger;
    private ConfigManager configManager;
    private Listener listener;
    private Renderer renderer;
    private Utils utils;
    private Scheduler scheduler;

    public TommyHUD() {
        instance = this;
        this.logger = LogManager.getLogger("TommyHUD");
        this.listener = new Listener();
        this.utils = new Utils();
        this.renderer = new Renderer();
        this.scheduler = new Scheduler();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        this.configManager = new ConfigManager(e.getSuggestedConfigurationFile());
        this.configManager.loadValues();
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this.listener);
        MinecraftForge.EVENT_BUS.register(this.scheduler);
        ClientCommandHandler.instance.func_71560_a(new TommyHUDCommand());
    }

    public Listener getListener() {
        return this.listener;
    }

    public static TommyHUD getInstance() {
        return instance;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public Renderer getRenderer() {
        return this.renderer;
    }

    public Utils getUtils() {
        return this.utils;
    }

    public Scheduler getScheduler() {
        return this.scheduler;
    }
}
 