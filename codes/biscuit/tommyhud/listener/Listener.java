package codes.biscuit.tommyhud.listener;

import codes.biscuit.tommyhud.*;
import codes.biscuit.tommyhud.misc.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.gameevent.*;
import codes.biscuit.tommyhud.gui.*;
import net.minecraft.client.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import codes.biscuit.tommyhud.misc.scheduler.*;
import net.minecraft.client.gui.*;
import codes.biscuit.tommyhud.core.*;
import net.minecraft.client.renderer.*;

public class Listener
{
    private TommyHUD main;
    private GUIType guiToOpen;
    private int leftClickCPS;
    private int rightClickCPS;
    private Vec3 lastSecondPosition;
    
    public Listener() {
        this.main = TommyHUD.getInstance();
        this.leftClickCPS = 0;
        this.rightClickCPS = 0;
    }
    
    @SubscribeEvent
    public void onRender(final TickEvent.RenderTickEvent e) {
        GuiScreen guiScreen = null;
        if (this.guiToOpen == GUIType.MAIN) {
            guiScreen = new MainGui();
        }
        else if (this.guiToOpen == GUIType.EDITING) {
            guiScreen = new LocationEditGui();
        }
        if (guiScreen != null) {
            Minecraft.func_71410_x().func_147108_a(guiScreen);
        }
        this.guiToOpen = null;
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post e) {
    }
    
    @SubscribeEvent
    public void onMouseEvent(final MouseEvent e) {
        if (e.buttonstate) {
            if (e.button == 0) {
                ++this.leftClickCPS;
                this.main.getScheduler().scheduleDelayedTask(new SkyblockRunnable() {
                    @Override
                    public void run() {
                        Listener.this.leftClickCPS--;
                    }
                }, 21);
            }
            else if (e.button == 1) {
                ++this.rightClickCPS;
                this.main.getScheduler().scheduleDelayedTask(new SkyblockRunnable() {
                    @Override
                    public void run() {
                        Listener.this.rightClickCPS--;
                    }
                }, 21);
            }
        }
    }
    
    @SubscribeEvent
    public void ticker(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START && Minecraft.func_71410_x().field_71439_g != null) {
            final Vec3 position = Minecraft.func_71410_x().field_71439_g.func_174791_d();
            this.main.getScheduler().scheduleDelayedTask(new SkyblockRunnable() {
                @Override
                public void run() {
                    Listener.this.lastSecondPosition = position;
                }
            }, 10);
        }
    }
    
    @SubscribeEvent
    public void onRenderPre(final RenderGameOverlayEvent.Pre event) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        final Minecraft mc = Minecraft.func_71410_x();
        final ScaledResolution sr = new ScaledResolution(mc);
        if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.UPSIDE_DOWN_MODE)) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(sr.func_78326_a() / 2.0f, sr.func_78328_b() / 2.0f, 0.0f);
            GlStateManager.func_179114_b(180.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.func_179109_b(-sr.func_78326_a() / 2.0f, -sr.func_78328_b() / 2.0f, 0.0f);
        }
        if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.EARTHQUAKE_MODE)) {
            float translation = 15.0f;
            final int animationTime = (int)(System.currentTimeMillis() % 200L);
            if (animationTime < 100) {
                final float progress = animationTime / 100.0f;
                translation *= progress;
            }
            else {
                final float progress = 1.0f - (animationTime - 100) / 100.0f;
                translation *= progress;
            }
            translation -= 7.5;
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(0.0f, translation, 0.0f);
        }
        if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.ROCKING_BOAT_MODE)) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(sr.func_78326_a() / 2.0f, sr.func_78328_b() / 2.0f, 0.0f);
            float rotation = 40.0f;
            final int animationTime = (int)(System.currentTimeMillis() % 3000L);
            if (animationTime < 1500) {
                final float progress = animationTime / 1500.0f;
                rotation *= progress;
            }
            else {
                final float progress = 1.0f - (animationTime - 1500) / 1500.0f;
                rotation *= progress;
            }
            rotation -= 20.0f;
            GlStateManager.func_179114_b(rotation, 0.0f, 0.0f, 1.0f);
            GlStateManager.func_179109_b(-sr.func_78326_a() / 2.0f, -sr.func_78328_b() / 2.0f, 0.0f);
        }
        if (event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.EARTHQUAKE_MODE)) {
                float translation = 40.0f;
                final int animationTime = (int)(System.currentTimeMillis() % 500L);
                if (animationTime < 250) {
                    final float progress = animationTime / 250.0f;
                    translation *= progress;
                }
                else {
                    final float progress = 1.0f - (animationTime - 250) / 250.0f;
                    translation *= progress;
                }
                GlStateManager.func_179094_E();
                GlStateManager.func_179109_b(translation, 0.0f, 0.0f);
            }
            if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.LARGE_GUI_AND_CROSSHAIR)) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179109_b(sr.func_78326_a() / 2.0f, sr.func_78328_b() / 2.0f, 0.0f);
                GlStateManager.func_179152_a(3.0f, 3.0f, 3.0f);
                GlStateManager.func_179109_b(-sr.func_78326_a() / 2.0f, -sr.func_78328_b() / 2.0f, 0.0f);
            }
            if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.HELICOPTER_CROSSHAIR)) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179109_b(sr.func_78326_a() / 2.0f, sr.func_78328_b() / 2.0f, 0.0f);
                GlStateManager.func_179114_b(System.currentTimeMillis() % 500L / 1.3888888f, 0.0f, 0.0f, 1.0f);
                GlStateManager.func_179109_b(-sr.func_78326_a() / 2.0f, -sr.func_78328_b() / 2.0f, 0.0f);
            }
        }
        if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.TINY_GUI)) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(sr.func_78326_a() / 2.0f, sr.func_78328_b() / 2.0f, 0.0f);
            GlStateManager.func_179152_a(0.5f, 0.5f, 0.0f);
            GlStateManager.func_179109_b(-sr.func_78326_a() / 2.0f, -sr.func_78328_b() / 2.0f, 0.0f);
        }
    }
    
    @SubscribeEvent
    public void onRenderPost(final RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.EXPERIENCE || event.type == RenderGameOverlayEvent.ElementType.JUMPBAR) {
            this.main.getRenderer().drawAllElements();
        }
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.CHROMA_SCREEN_MODE)) {
                final Minecraft mc = Minecraft.func_71410_x();
                final ScaledResolution sr = new ScaledResolution(mc);
                this.main.getUtils().drawRect(0.0, 0.0, sr.func_78326_a(), sr.func_78328_b(), -1, true, 100);
            }
            return;
        }
        if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.UPSIDE_DOWN_MODE)) {
            GlStateManager.func_179121_F();
        }
        if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.EARTHQUAKE_MODE)) {
            GlStateManager.func_179121_F();
        }
        if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.ROCKING_BOAT_MODE)) {
            GlStateManager.func_179121_F();
        }
        if (event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.EARTHQUAKE_MODE)) {
                GlStateManager.func_179121_F();
            }
            if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.LARGE_GUI_AND_CROSSHAIR)) {
                GlStateManager.func_179121_F();
            }
            if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.HELICOPTER_CROSSHAIR)) {
                GlStateManager.func_179121_F();
            }
        }
        if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.TINY_GUI)) {
            GlStateManager.func_179121_F();
        }
    }
    
    public void setGuiToOpen(final GUIType guiToOpen) {
        this.guiToOpen = guiToOpen;
    }
    
    public int getLeftClickCPS() {
        return this.leftClickCPS;
    }
    
    public int getRightClickCPS() {
        return this.rightClickCPS;
    }
    
    public Vec3 getLastSecondPosition() {
        return this.lastSecondPosition;
    }
}
