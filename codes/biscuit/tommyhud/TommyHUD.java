package codes.biscuit.tommyhud;

import net.minecraftforge.fml.common.*;
import codes.biscuit.tommyhud.config.*;
import codes.biscuit.tommyhud.listener.*;
import codes.biscuit.tommyhud.render.*;
import codes.biscuit.tommyhud.util.*;
import codes.biscuit.tommyhud.misc.scheduler.*;
import org.apache.logging.log4j.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.common.*;
import net.minecraftforge.client.*;
import codes.biscuit.tommyhud.command.*;
import net.minecraft.command.*;

@Mod(modid = "tommyhud", name = "TommyHUD", version = "1.0", clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]")
public class TommyHUD
{
    private static TommyHUD instance;
    private final Logger logger;
    private ConfigManager configManager;
    private Listener listener;
    private Renderer renderer;
    private Utils utils;
    private Scheduler scheduler;
    
    public TommyHUD() {
        TommyHUD.instance = this;
        this.logger = LogManager.getLogger("TommyHUD");
        this.listener = new Listener();
        this.utils = new Utils();
        this.renderer = new Renderer();
        this.scheduler = new Scheduler();
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent e) {
        (this.configManager = new ConfigManager(e.getSuggestedConfigurationFile())).loadValues();
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register((Object)this.listener);
        MinecraftForge.EVENT_BUS.register((Object)this.scheduler);
        ClientCommandHandler.instance.func_71560_a((ICommand)new TommyHUDCommand());
    }
    
    public Listener getListener() {
        return this.listener;
    }
    
    public static TommyHUD getInstance() {
        return TommyHUD.instance;
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
