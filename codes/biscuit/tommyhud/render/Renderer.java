package codes.biscuit.tommyhud.render;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.core.CrazyMode;
import codes.biscuit.tommyhud.core.DrawType;
import codes.biscuit.tommyhud.core.Element;
import codes.biscuit.tommyhud.gui.buttons.ButtonLocation;
import codes.biscuit.tommyhud.misc.ChromaManager;
import codes.biscuit.tommyhud.misc.scheduler.SkyblockRunnable;
import codes.biscuit.tommyhud.util.ColorCode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.chunk.Chunk;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Renderer {
    private TommyHUD main = TommyHUD.getInstance();
    private double lastSecondVelocityVertical;
    private double lastSecondVelocityHorizontal;
    private double velocityVertical;
    private double velocityHorizontal;
    protected static final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");

    public void drawAllElements() {
        for(Element element : Element.values()) {
            this.drawElement(element);
        }

    }

    public void drawElement(Element element) {
        this.drawElement(element, (ButtonLocation)null);
    }

    public void drawElement(Element element, ButtonLocation buttonLocation) {
        float scale = this.main.getConfigManager().getConfigValues().getGuiScale(element);
        if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.LARGE_GUI_AND_CROSSHAIR)) {
            scale = (float)((double)scale + 0.6D);
        }

        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0F);
        if (element.getGuiDefault().getDrawType() == DrawType.TEXT) {
            this.drawText(element, scale, buttonLocation);
        } else if (element.getGuiDefault().getDrawType() == DrawType.ICON_TEXT) {
            this.drawIconText(element, scale, buttonLocation);
        } else if (element.getGuiDefault().getDrawType() == DrawType.POTION) {
            this.drawPotions(scale, buttonLocation);
        } else if (element.getGuiDefault().getDrawType() == DrawType.ARMOR) {
            this.drawArmorStatus(scale, buttonLocation);
        } else if (element.getGuiDefault().getDrawType() == DrawType.KEYSTROKES) {
            this.drawKeystrokes(scale, buttonLocation);
        }

        GlStateManager.popMatrix();
    }

    public void drawText(Element element, float scale, ButtonLocation buttonLocation) {
        boolean isPreview = buttonLocation != null;
        Minecraft mc = Minecraft.getMinecraft();
        String text = "Default";
        if (element == Element.FPS) {
            text = Minecraft.getDebugFPS() + " FPS";
        } else if (element == Element.CPS_LEFT) {
            text = this.main.getListener().getLeftClickCPS() + " CPS (LMB)";
        } else if (element == Element.CPS_RIGHT) {
            text = this.main.getListener().getRightClickCPS() + " CPS (RMB)";
        } else if (element == Element.COORDINATES) {
            Entity player = Minecraft.getMinecraft().thePlayer;
            text = "X: " + Math.round(player.posX) + "\nY: " + Math.round(player.posY) + "\nZ: " + Math.round(player.posZ);
        } else if (element == Element.CLOCK) {
            DateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
            text = dateFormat.format(new Date());
        } else if (element == Element.HORIZONTAL_SPEED) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            final double velocity = 0.0D;
            if (mc.thePlayer != null && this.main.getListener().getLastSecondPosition() != null) {
                Vec3 lastSecondPosition = this.main.getListener().getLastSecondPosition();
                Vec3 currentPosition = mc.thePlayer.getPositionVector();
                velocity = Math.sqrt(Math.pow(lastSecondPosition.xCoord - currentPosition.xCoord, 2.0D) + Math.pow(lastSecondPosition.zCoord - currentPosition.zCoord, 2.0D));
                this.velocityHorizontal = velocity;
                this.main.getScheduler().scheduleDelayedTask(new SkyblockRunnable() {
                    public void run() {
                        Renderer.this.lastSecondVelocityHorizontal = velocity;
                    }
                }, 10);
            } else {
                this.velocityHorizontal = -1.0D;
                this.main.getScheduler().scheduleDelayedTask(new SkyblockRunnable() {
                    public void run() {
                        Renderer.this.lastSecondVelocityHorizontal = -1.0D;
                    }
                }, 10);
            }

            text = "Horiz. Velocity: " + decimalFormat.format(velocity) + " m/s";
        } else if (element == Element.VERTICAL_SPEED) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            final double velocity = 0.0D;
            if (mc.thePlayer != null && this.main.getListener().getLastSecondPosition() != null) {
                Vec3 lastSecondPosition = this.main.getListener().getLastSecondPosition();
                Vec3 currentPosition = mc.thePlayer.getPositionVector();
                velocity = Math.abs(lastSecondPosition.yCoord - currentPosition.yCoord);
                this.velocityVertical = velocity;
                this.main.getScheduler().scheduleDelayedTask(new SkyblockRunnable() {
                    public void run() {
                        Renderer.this.lastSecondVelocityVertical = velocity;
                    }
                }, 10);
            } else {
                this.velocityVertical = -1.0D;
                this.main.getScheduler().scheduleDelayedTask(new SkyblockRunnable() {
                    public void run() {
                        Renderer.this.lastSecondVelocityVertical = -1.0D;
                    }
                }, 10);
            }

            text = "Vert. Velocity: " + decimalFormat.format(velocity) + " m/s";
        } else if (element == Element.CHUNK_COORDINATES) {
            Entity player = Minecraft.getMinecraft().thePlayer;
            text = "Chunk:\nX: " + player.chunkCoordX + "\nY: " + player.chunkCoordY + "\nZ: " + player.chunkCoordZ;
        } else if (element == Element.HORIZONTAL_ACCELERATION) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            double acceleration = 0.0D;
            if (this.lastSecondVelocityHorizontal != -1.0D && this.velocityHorizontal != -1.0D) {
                double lastSecondVelocity = this.lastSecondVelocityHorizontal;
                double velocity = this.velocityHorizontal;
                acceleration = velocity - lastSecondVelocity;
            }

            text = "Horiz. Accel: " + decimalFormat.format(acceleration) + " m/s/s";
        } else if (element == Element.VERTICAL_ACCELERATION) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            double acceleration = 0.0D;
            if (this.lastSecondVelocityVertical != -1.0D && this.velocityVertical != -1.0D) {
                double lastSecondVelocity = this.lastSecondVelocityVertical;
                double velocity = this.velocityVertical;
                acceleration = velocity - lastSecondVelocity;
            }

            text = "Vert. Accel: " + decimalFormat.format(acceleration) + " m/s/s";
        } else if (element == Element.LIGHT_LEVEL) {
            if (mc.getRenderViewEntity() != null && mc.theWorld != null) {
                BlockPos blockPos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY, mc.getRenderViewEntity().posZ);
                Chunk chunk = mc.theWorld.getChunkFromBlockCoords(blockPos);
                text = "Light Level: " + chunk.getLightSubtracted(blockPos, 0);
            }
        } else if (element == Element.BIOME) {
            if (mc.getRenderViewEntity() != null && mc.theWorld != null) {
                BlockPos blockPos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY, mc.getRenderViewEntity().posZ);
                Chunk chunk = mc.theWorld.getChunkFromBlockCoords(blockPos);
                text = "Biome: " + chunk.getBiome(blockPos, mc.theWorld.getWorldChunkManager()).biomeName;
            }
        } else if (element == Element.MEMORY) {
            long max = Runtime.getRuntime().maxMemory();
            long total = Runtime.getRuntime().totalMemory();
            long free = Runtime.getRuntime().freeMemory();
            long remaining = total - free;
            text = String.format("Memory: %03d/%03d MB", remaining / 1024L / 1024L, max / 1024L / 1024L);
        } else if (element == Element.PING) {
            NetworkPlayerInfo networkPlayerInfo = mc.thePlayer.sendQueue.getPlayerInfo(Minecraft.getMinecraft().thePlayer.getUniqueID());
            int ping = 0;
            if (networkPlayerInfo != null) {
                ping = networkPlayerInfo.getResponseTime();
            }

            text = ping + " Ping";
        } else if (element == Element.SUBSCRIBE_TO_TOMMYINNIT) {
            text = "Subscribe to TommyInnit!!!";
        } else if (element == Element.POKIMANE_SUBSCRIPTION_TIER) {
            text = "Pokimane Subscription Tier: 3";
        } else if (element == Element.DATE) {
            DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMMM, yyyy", Locale.US);
            text = dateFormat.format(new Date());
        } else if (element == Element.DIRECTION_FACING) {
            String direction = "Unknown";
            if (mc.getRenderViewEntity() != null) {
                EnumFacing facing = mc.getRenderViewEntity().getHorizontalFacing();
                direction = WordUtils.capitalize(facing.getName());
            }

            text = "Direction: " + direction;
        } else if (element == Element.YAW) {
            float yaw = 0.0F;
            if (mc.thePlayer != null) {
                yaw = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);
            }

            text = "Yaw: " + String.format("%.1f", yaw);
        } else if (element == Element.PITCH) {
            float pitch = 0.0F;
            if (mc.thePlayer != null) {
                pitch = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch);
            }

            text = "Pitch: " + String.format("%.1f", pitch);
        } else if (element == Element.PLAYERS) {
            int players = 0;
            if (mc.thePlayer != null && mc.thePlayer.sendQueue != null && mc.thePlayer.sendQueue.getPlayerInfoMap() != null) {
                players = mc.thePlayer.sendQueue.getPlayerInfoMap().size();
            }

            text = "Players: " + players;
        }

        float x = this.main.getConfigManager().getConfigValues().getActualX(element);
        float y = this.main.getConfigManager().getConfigValues().getActualY(element);
        int width = mc.fontRendererObj.getStringWidth(text);
        int textHeight = 8;
        int lineSpacing = 2;
        int height = textHeight;
        String[] lines = null;
        if (text.contains("\n")) {
            lines = text.split("\n");
            height = (textHeight + lineSpacing) * lines.length - lineSpacing;
            width = 0;

            for(String line : lines) {
                width = Math.max(width, mc.fontRendererObj.getStringWidth(line));
            }
        }

        boolean drawBackground = true;
        if (drawBackground) {
            width += 4;
            height += 4;
        }

        x = this.transformX(x, width, scale, element);
        y = this.transformY(y, height, scale, element);
        if (isPreview) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            buttonLocation.checkHoveredAndDrawBox(x, x + (float)width, y, y + (float)height, scale);
        }

        if (drawBackground) {
            this.main.getUtils().drawRect((double)x, (double)y, (double)(x + (float)width), (double)(y + (float)height), 1711276032, this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.CRAZY_CHROMA_MODE));
            x += 2.0F;
            y += 2.0F;
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ChromaManager.renderingText(element);
        int color = this.main.getConfigManager().getConfigValues().getColor(element).getRGB();
        if (lines == null) {
            this.main.getUtils().drawTextWithStyle(text, x, y, color);
        } else {
            for(int line = 0; line < lines.length; ++line) {
                float currentY = y + (float)((textHeight + lineSpacing) * line);
                this.main.getUtils().drawTextWithStyle(lines[line], x, currentY, color);
            }
        }

        ChromaManager.doneRenderingText();
    }

    public void drawIconText(Element element, float scale, ButtonLocation buttonLocation) {
        boolean isPreview = buttonLocation != null;
        Minecraft mc = Minecraft.getMinecraft();
        ResourceLocation resourceLocation = null;
        ItemStack itemStack = null;
        String text = "Default";
        if (element == Element.ARROW_COUNT) {
            itemStack = new ItemStack(Items.arrow);
            int arrowCount = 0;
            if (mc.thePlayer != null) {
                for(ItemStack inventoryStack : mc.thePlayer.inventory.mainInventory) {
                    if (inventoryStack != null && inventoryStack.getItem() == Items.arrow) {
                        arrowCount += inventoryStack.stackSize;
                    }
                }
            }

            text = String.valueOf(arrowCount);
        }

        float x = this.main.getConfigManager().getConfigValues().getActualX(element);
        float y = this.main.getConfigManager().getConfigValues().getActualY(element);
        int width = 18 + mc.fontRendererObj.getStringWidth(text);
        int textHeight = 8;
        int lineSpacing = 2;
        int height = 16;
        String[] lines = null;
        if (text.contains("\n")) {
            lines = text.split("\n");
            height = (textHeight + lineSpacing) * lines.length - lineSpacing;
            width = 0;

            for(String line : lines) {
                width = Math.max(width, mc.fontRendererObj.getStringWidth(line));
            }
        }

        boolean drawBackground = true;
        if (drawBackground) {
            width += 4;
            height += 4;
        }

        x = this.transformX(x, width, scale, element);
        y = this.transformY(y, height, scale, element);
        if (isPreview) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            buttonLocation.checkHoveredAndDrawBox(x, x + (float)width, y, y + (float)height, scale);
        }

        if (drawBackground) {
            this.main.getUtils().drawRect((double)x, (double)y, (double)(x + (float)width), (double)(y + (float)height), 1711276032, this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.CRAZY_CHROMA_MODE));
            x += 2.0F;
            y += 2.0F;
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (resourceLocation != null) {
            mc.getTextureManager().bindTexture(resourceLocation);
            this.main.getUtils().drawTexturedRectangle(x, y, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F, 16.0F);
        } else if (itemStack != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 0.0F);
            mc.getRenderItem().renderItemIntoGUI(itemStack, 0, 0);
            GlStateManager.popMatrix();
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ChromaManager.renderingText(element);
        int color = this.main.getConfigManager().getConfigValues().getColor(element).getRGB();
        if (lines == null) {
            this.main.getUtils().drawTextWithStyle(text, x + 16.0F + 2.0F, y + (float)height / 2.0F - 4.0F, color);
        } else {
            for(int line = 0; line < lines.length; ++line) {
                float currentY = y + (float)((textHeight + lineSpacing) * line);
                this.main.getUtils().drawTextWithStyle(lines[line], x + 16.0F + 2.0F, currentY, color);
            }
        }

        ChromaManager.doneRenderingText();
    }

    public void drawPotions(float scale, ButtonLocation buttonLocation) {
        boolean isPreview = buttonLocation != null;
        Minecraft mc = Minecraft.getMinecraft();
        float x = this.main.getConfigManager().getConfigValues().getActualX(Element.POTION_DISPLAY);
        float y = this.main.getConfigManager().getConfigValues().getActualY(Element.POTION_DISPLAY);
        Collection<PotionEffect> potions = mc.thePlayer.func_70651_bq();
        if (potions.isEmpty()) {
            if (!isPreview) {
                return;
            }

            potions = Collections.singletonList(new PotionEffect(Potion.moveSpeed.getId(), 600));
        }

        int longestPotionName = 0;

        for(PotionEffect effect : potions) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            if (potion.shouldRender(effect)) {
                String potionName = I18n.format(potion.getName(), new Object[0]) + " " + effect.getAmplifier();
                longestPotionName = Math.max(longestPotionName, mc.fontRendererObj.getStringWidth(potionName));
                String durationString = Potion.getDurationString(effect);
                longestPotionName = Math.max(longestPotionName, mc.fontRendererObj.getStringWidth(durationString));
            }
        }

        int lineHeight = 18;
        int lineSpacing = 4;
        int width = 22 + longestPotionName;
        int height = (lineHeight + lineSpacing) * potions.size() - lineSpacing;
        boolean drawBackground = true;
        if (drawBackground) {
            width += 4;
            height += 4;
        }

        x = this.transformX(x, width, scale, Element.POTION_DISPLAY);
        y = this.transformY(y, height, scale, Element.POTION_DISPLAY);
        if (isPreview) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            buttonLocation.checkHoveredAndDrawBox(x, x + (float)width, y, y + (float)height, scale);
        }

        if (drawBackground) {
            this.main.getUtils().drawRect((double)x, (double)y, (double)(x + (float)width), (double)(y + (float)height), 1711276032, this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.CRAZY_CHROMA_MODE));
            x += 2.0F;
            y += 2.0F;
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();

        for(PotionEffect effect : potions) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            if (potion.shouldRender(effect)) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                if (potion.hasStatusIcon()) {
                    int i1 = potion.getStatusIconIndex();
                    mc.getTextureManager().bindTexture(inventoryBackground);
                    this.main.getUtils().drawTexturedModalRect(x, y, i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                }

                if (!potion.shouldRenderInvText(effect)) {
                    y += (float)(lineHeight + lineSpacing);
                } else {
                    String potionName = I18n.format(potion.getName(), new Object[0]) + " " + (effect.getAmplifier() + 1);
                    mc.fontRendererObj.drawStringWithShadow(potionName, x + 4.0F + 18.0F, y, ColorCode.WHITE.getRGB());
                    String durationString = Potion.getDurationString(effect);
                    mc.fontRendererObj.drawStringWithShadow(durationString, x + 4.0F + 18.0F, y + 10.0F, ColorCode.WHITE.getRGB());
                    y += (float)(lineHeight + lineSpacing);
                }
            }
        }

    }

    public void drawArmorStatus(float scale, ButtonLocation buttonLocation) {
        boolean isPreview = buttonLocation != null;
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        float x = this.main.getConfigManager().getConfigValues().getActualX(Element.ARMOR_DISPLAY);
        float y = this.main.getConfigManager().getConfigValues().getActualY(Element.ARMOR_DISPLAY);
        ItemStack[] armorInventory = (ItemStack[])player.getInventory().clone();
        boolean foundPiece = false;

        for(int armorPiece = 0; armorPiece < 4; ++armorPiece) {
            if (armorInventory[armorPiece] != null) {
                foundPiece = true;
                break;
            }
        }

        if (!foundPiece) {
            if (!isPreview) {
                return;
            }

            armorInventory[0] = new ItemStack(Items.diamond_boots);
            if (armorInventory[1] != null) {
                armorInventory[1] = new ItemStack(Items.diamond_leggings);
            }

            if (armorInventory[2] != null) {
                armorInventory[2] = new ItemStack(Items.diamond_chestplate);
            }

            if (armorInventory[3] != null) {
                armorInventory[3] = new ItemStack(Items.diamond_helmet);
            }
        }

        int longestLine = 0;
        int armorPieces = 0;

        for(int armorPiece = 0; armorPiece < 4; ++armorPiece) {
            ItemStack itemStack = armorInventory[armorPiece];
            if (itemStack != null) {
                ++armorPieces;
                longestLine = Math.max(longestLine, mc.fontRendererObj.getStringWidth(this.getDurabilityPercent(itemStack) + "%"));
            }
        }

        int width = 18 + longestLine;
        int lineSpacing = 2;
        int height = (16 + lineSpacing) * armorPieces - lineSpacing;
        boolean drawBackground = true;
        if (drawBackground) {
            width += 4;
            height += 4;
        }

        x = this.transformX(x, width, scale, Element.ARMOR_DISPLAY);
        y = this.transformY(y, height, scale, Element.ARMOR_DISPLAY);
        if (isPreview) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            buttonLocation.checkHoveredAndDrawBox(x, x + (float)width, y, y + (float)height, scale);
        }

        if (drawBackground) {
            this.main.getUtils().drawRect((double)x, (double)y, (double)(x + (float)width), (double)(y + (float)height), 1711276032, this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.CRAZY_CHROMA_MODE));
            x += 2.0F;
            y += 2.0F;
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int color = this.main.getConfigManager().getConfigValues().getColor(Element.ARMOR_DISPLAY).getRGB();
        float currentY = y;

        for(int armorPiece = 3; armorPiece >= 0; --armorPiece) {
            ItemStack itemStack = armorInventory[armorPiece];
            if (itemStack != null) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(x, currentY, 0.0F);
                mc.getRenderItem().renderItemIntoGUI(itemStack, 0, 0);
                GlStateManager.popMatrix();
                ChromaManager.renderingText(Element.ARMOR_DISPLAY);
                String damage = this.getDurabilityPercent(itemStack) + "%";
                this.main.getUtils().drawTextWithStyle(damage, x + 16.0F + 2.0F, currentY + 8.0F - 4.0F, color);
                ChromaManager.doneRenderingText();
                currentY += (float)(16 + lineSpacing);
            }
        }

    }

    public int getDurabilityPercent(ItemStack itemStack) {
        return itemStack.getMaxDamage() == 0 ? 100 : 100 - (int)((float)itemStack.getItemDamage() / (float)itemStack.getMaxDamage() * 100.0F);
    }

    public void drawKeystrokes(float scale, ButtonLocation buttonLocation) {
        boolean isPreview = buttonLocation != null;
        Minecraft mc = Minecraft.getMinecraft();
        float x = this.main.getConfigManager().getConfigValues().getActualX(Element.KEYSTROKES);
        float y = this.main.getConfigManager().getConfigValues().getActualY(Element.KEYSTROKES);
        int boxSize = 12;
        int spacing = 1;
        int spaceHeight = 8;
        int width = (boxSize + spacing) * 3 - spacing;
        int height = (boxSize + spacing) * 3 + spaceHeight + spacing - spacing;
        x = this.transformX(x, width, scale, Element.KEYSTROKES);
        y = this.transformY(y, height, scale, Element.KEYSTROKES);
        if (isPreview) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            buttonLocation.checkHoveredAndDrawBox(x, x + (float)width, y, y + (float)height, scale);
        }

        int color = this.main.getConfigManager().getConfigValues().getColor(Element.POTION_DISPLAY).getRGB();
        int unpressedColor = 1711276032;
        int pressedColor = 1721342361;
        float startingX = x + (float)boxSize + (float)spacing;
        this.drawKeystrokesRect(mc.gameSettings.keyBindForward, (double)startingX, (double)y, (double)(startingX + (float)boxSize), (double)(y + (float)boxSize), unpressedColor, pressedColor);
        this.drawKeystrokesString(mc.gameSettings.keyBindForward, startingX, (float)boxSize, y, (float)boxSize, color);
        float startingY = y + (float)(boxSize + spacing);
        this.drawKeystrokesRect(mc.gameSettings.keyBindLeft, (double)x, (double)startingY, (double)(x + (float)boxSize), (double)(startingY + (float)boxSize), unpressedColor, pressedColor);
        this.drawKeystrokesString(mc.gameSettings.keyBindLeft, x, (float)boxSize, startingY, (float)boxSize, color);
        startingX = x + (float)(boxSize + spacing);
        this.drawKeystrokesRect(mc.gameSettings.keyBindBack, (double)startingX, (double)startingY, (double)(startingX + (float)boxSize), (double)(startingY + (float)boxSize), unpressedColor, pressedColor);
        this.drawKeystrokesString(mc.gameSettings.keyBindBack, startingX, (float)boxSize, startingY, (float)boxSize, color);
        startingX += (float)(boxSize + spacing);
        this.drawKeystrokesRect(mc.gameSettings.keyBindRight, (double)startingX, (double)startingY, (double)(startingX + (float)boxSize), (double)(startingY + (float)boxSize), unpressedColor, pressedColor);
        this.drawKeystrokesString(mc.gameSettings.keyBindRight, startingX, (float)boxSize, startingY, (float)boxSize, color);
        startingY += (float)(boxSize + spacing);
        this.drawKeystrokesRect(mc.gameSettings.keyBindAttack, (double)x, (double)startingY, (double)(x + (float)width / 2.0F - (float)spacing / 2.0F), (double)(startingY + (float)boxSize), unpressedColor, pressedColor);
        this.drawKeystrokesString(mc.gameSettings.keyBindAttack, x, (float)width / 2.0F, startingY, (float)boxSize, color);
        startingX = x + (float)width / 2.0F + (float)spacing / 2.0F;
        this.drawKeystrokesRect(mc.gameSettings.keyBindUseItem, (double)startingX, (double)startingY, (double)(startingX + (float)width / 2.0F - (float)spacing / 2.0F), (double)(startingY + (float)boxSize), unpressedColor, pressedColor);
        this.drawKeystrokesString(mc.gameSettings.keyBindUseItem, startingX, (float)width / 2.0F, startingY, (float)boxSize, color);
        startingY += (float)(boxSize + spacing);
        this.drawKeystrokesRect(mc.gameSettings.keyBindJump, (double)x, (double)startingY, (double)(x + (float)width), (double)(startingY + (float)spaceHeight), unpressedColor, pressedColor);
        int spacebarMiddleWidth = 26;
        int spacebarMiddleHeight = 2;
        this.main.getUtils().drawRect((double)(x + (float)width / 2.0F - (float)spacebarMiddleWidth / 2.0F), (double)(startingY + (float)spaceHeight / 2.0F - (float)spacebarMiddleHeight / 2.0F), (double)(x + (float)width / 2.0F + (float)spacebarMiddleWidth / 2.0F), (double)(startingY + (float)spaceHeight / 2.0F + (float)spacebarMiddleHeight / 2.0F), color);
    }

    private void drawKeystrokesRect(KeyBinding keyBinding, double left, double top, double right, double bottom, int unpressedColor, int pressedColor) {
        boolean pressed = keyBinding.isPressed() || keyBinding.isKeyDown();
        this.main.getUtils().drawRect(left, top, right, bottom, pressed ? pressedColor : unpressedColor, this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.CRAZY_CHROMA_MODE));
    }

    private void drawKeystrokesString(KeyBinding keyBinding, float startingX, float width, float startingY, float height, int color) {
        Minecraft mc = Minecraft.getMinecraft();
        ChromaManager.renderingText(Element.ARMOR_DISPLAY);
        String keyName = this.getKeyName(keyBinding);
        GlStateManager.pushMatrix();
        float scale = 0.75F;
        GlStateManager.scale(scale, scale, 1.0F);
        float x = startingX / scale + width / 2.0F / scale - (float)mc.fontRendererObj.getStringWidth(keyName) / 2.0F + 0.5F;
        float y = startingY / scale + height / 2.0F / scale - 4.0F;
        this.main.getUtils().drawTextWithStyle(keyName, x, y, color);
        GlStateManager.popMatrix();
        ChromaManager.doneRenderingText();
    }

    public String getKeyName(KeyBinding keyBinding) {
        int keyCode = keyBinding.getKeyCode();
        if (keyCode < 0) {
            keyCode += 100;
            if (keyCode == 0) {
                return "LMB";
            } else {
                return keyCode == 1 ? "RMB" : Mouse.getButtonName(keyCode);
            }
        } else {
            return Keyboard.getKeyName(keyCode);
        }
    }

    public float transformX(float x, int width, float scale, Element element) {
        float minecraftScale = (float)(new ScaledResolution(Minecraft.getMinecraft())).getScaleFactor();
        x -= this.main.getUtils().getXOffsetFromAnchorPoint((float)width, scale, this.main.getConfigManager().getConfigValues().getAnchorPoint(element));
        x = (float)Math.round(x * minecraftScale) / minecraftScale;
        return x / scale;
    }

    public float transformY(float y, int height, float scale, Element element) {
        float minecraftScale = (float)(new ScaledResolution(Minecraft.getMinecraft())).getScaleFactor();
        y -= this.main.getUtils().getYOffsetFromAnchorPoint((float)height, scale, this.main.getConfigManager().getConfigValues().getAnchorPoint(element));
        y = (float)Math.round(y * minecraftScale) / minecraftScale;
        return y / scale;
    }
}
 