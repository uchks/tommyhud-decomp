package codes.biscuit.tommyhud.listener;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.core.CrazyMode;
import codes.biscuit.tommyhud.gui.LocationEditGui;
import codes.biscuit.tommyhud.gui.MainGui;
import codes.biscuit.tommyhud.misc.GUIType;
import codes.biscuit.tommyhud.misc.scheduler.SkyblockRunnable;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class Listener {
    private TommyHUD main = TommyHUD.getInstance();
    private GUIType guiToOpen;
    private int leftClickCPS = 0;
    private int rightClickCPS = 0;
    private Vec3 lastSecondPosition;

    @SubscribeEvent
    public void onRender(RenderTickEvent e) {
        GuiScreen guiScreen = null;
        if (this.guiToOpen == GUIType.MAIN) {
            guiScreen = new MainGui();
        } else if (this.guiToOpen == GUIType.EDITING) {
            guiScreen = new LocationEditGui();
        }

        if (guiScreen != null) {
            Minecraft.getMinecraft().displayGuiScreen(guiScreen);
        }

        this.guiToOpen = null;
    }

    @SubscribeEvent
    public void onRender(Post e) {
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent e) {
        if (e.buttonstate) {
            if (e.button == 0) {
                ++this.leftClickCPS;
                this.main.getScheduler().scheduleDelayedTask(new SkyblockRunnable() {
                    public void run() {
                        Listener.this.leftClickCPS--;
                    }
                }, 21);
            } else if (e.button == 1) {
                ++this.rightClickCPS;
                this.main.getScheduler().scheduleDelayedTask(new SkyblockRunnable() {
                    public void run() {
                        Listener.this.rightClickCPS--;
                    }
                }, 21);
            }
        }

    }

    @SubscribeEvent
    public void ticker(ClientTickEvent event) {
        if (event.phase == Phase.START && Minecraft.getMinecraft().thePlayer != null) {
            final Vec3 position = Minecraft.getMinecraft().thePlayer.getPositionVector();
            this.main.getScheduler().scheduleDelayedTask(new SkyblockRunnable() {
                public void run() {
                    Listener.this.lastSecondPosition = position;
                }
            }, 10);
        }

    }

    @SubscribeEvent
    public void onRenderPre(Pre event) {
        if (event.type != ElementType.ALL) {
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution sr = new ScaledResolution(mc);
            if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.UPSIDE_DOWN_MODE)) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)sr.getScaledWidth() / 2.0F, (float)sr.getScaledHeight() / 2.0F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.translate((float)(-sr.getScaledWidth()) / 2.0F, (float)(-sr.getScaledHeight()) / 2.0F, 0.0F);
            }

            if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.EARTHQUAKE_MODE)) {
                float translation = 15.0F;
                int animationTime = (int)(System.currentTimeMillis() % 200L);
                if (animationTime < 100) {
                    float progress = (float)animationTime / 100.0F;
                    translation *= progress;
                } else {
                    float progress = 1.0F - (float)(animationTime - 100) / 100.0F;
                    translation *= progress;
                }

                translation = (float)((double)translation - 7.5D);
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0F, translation, 0.0F);
            }

            if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.ROCKING_BOAT_MODE)) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)sr.getScaledWidth() / 2.0F, (float)sr.getScaledHeight() / 2.0F, 0.0F);
                float rotation = 40.0F;
                int animationTime = (int)(System.currentTimeMillis() % 3000L);
                if (animationTime < 1500) {
                    float progress = (float)animationTime / 1500.0F;
                    rotation *= progress;
                } else {
                    float progress = 1.0F - (float)(animationTime - 1500) / 1500.0F;
                    rotation *= progress;
                }

                rotation -= 20.0F;
                GlStateManager.rotate(rotation, 0.0F, 0.0F, 1.0F);
                GlStateManager.translate((float)(-sr.getScaledWidth()) / 2.0F, (float)(-sr.getScaledHeight()) / 2.0F, 0.0F);
            }

            if (event.type == ElementType.CROSSHAIRS) {
                if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.EARTHQUAKE_MODE)) {
                    float translation = 40.0F;
                    int animationTime = (int)(System.currentTimeMillis() % 500L);
                    if (animationTime < 250) {
                        float progress = (float)animationTime / 250.0F;
                        translation *= progress;
                    } else {
                        float progress = 1.0F - (float)(animationTime - 250) / 250.0F;
                        translation *= progress;
                    }

                    GlStateManager.pushMatrix();
                    GlStateManager.translate(translation, 0.0F, 0.0F);
                }

                if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.LARGE_GUI_AND_CROSSHAIR)) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float)sr.getScaledWidth() / 2.0F, (float)sr.getScaledHeight() / 2.0F, 0.0F);
                    GlStateManager.scale(3.0F, 3.0F, 3.0F);
                    GlStateManager.translate((float)(-sr.getScaledWidth()) / 2.0F, (float)(-sr.getScaledHeight()) / 2.0F, 0.0F);
                }

                if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.HELICOPTER_CROSSHAIR)) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float)sr.getScaledWidth() / 2.0F, (float)sr.getScaledHeight() / 2.0F, 0.0F);
                    GlStateManager.rotate((float)(System.currentTimeMillis() % 500L) / 1.3888888F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.translate((float)(-sr.getScaledWidth()) / 2.0F, (float)(-sr.getScaledHeight()) / 2.0F, 0.0F);
                }
            }

            if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.TINY_GUI)) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)sr.getScaledWidth() / 2.0F, (float)sr.getScaledHeight() / 2.0F, 0.0F);
                GlStateManager.scale(0.5F, 0.5F, 0.0F);
                GlStateManager.translate((float)(-sr.getScaledWidth()) / 2.0F, (float)(-sr.getScaledHeight()) / 2.0F, 0.0F);
            }

        }
    }

    @SubscribeEvent
    public void onRenderPost(Post event) {
        if (event.type == ElementType.EXPERIENCE || event.type == ElementType.JUMPBAR) {
            this.main.getRenderer().drawAllElements();
        }

        if (event.type == ElementType.ALL) {
            if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.CHROMA_SCREEN_MODE)) {
                Minecraft mc = Minecraft.getMinecraft();
                ScaledResolution sr = new ScaledResolution(mc);
                this.main.getUtils().drawRect(0.0D, 0.0D, (double)sr.getScaledWidth(), (double)sr.getScaledHeight(), -1, true, 100);
            }

        } else {
            if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.UPSIDE_DOWN_MODE)) {
                GlStateManager.popMatrix();
            }

            if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.EARTHQUAKE_MODE)) {
                GlStateManager.popMatrix();
            }

            if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.ROCKING_BOAT_MODE)) {
                GlStateManager.popMatrix();
            }

            if (event.type == ElementType.CROSSHAIRS) {
                if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.EARTHQUAKE_MODE)) {
                    GlStateManager.popMatrix();
                }

                if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.LARGE_GUI_AND_CROSSHAIR)) {
                    GlStateManager.popMatrix();
                }

                if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.HELICOPTER_CROSSHAIR)) {
                    GlStateManager.popMatrix();
                }
            }

            if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.TINY_GUI)) {
                GlStateManager.popMatrix();
            }

        }
    }

    public void setGuiToOpen(GUIType guiToOpen) {
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
 