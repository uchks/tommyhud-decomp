package codes.biscuit.tommyhud.render;

import codes.biscuit.tommyhud.*;
import codes.biscuit.tommyhud.gui.buttons.*;
import net.minecraft.client.renderer.*;
import codes.biscuit.tommyhud.core.*;
import net.minecraft.client.*;
import codes.biscuit.tommyhud.misc.scheduler.*;
import org.apache.commons.lang3.text.*;
import codes.biscuit.tommyhud.misc.*;
import net.minecraft.entity.*;
import java.text.*;
import net.minecraft.world.chunk.*;
import net.minecraft.client.network.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.minecraft.potion.*;
import net.minecraft.client.resources.*;
import codes.biscuit.tommyhud.util.*;
import java.util.*;
import net.minecraft.item.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.settings.*;
import org.lwjgl.input.*;
import net.minecraft.client.gui.*;

public class Renderer
{
    private TommyHUD main;
    private double lastSecondVelocityVertical;
    private double lastSecondVelocityHorizontal;
    private double velocityVertical;
    private double velocityHorizontal;
    protected static final ResourceLocation inventoryBackground;
    
    public Renderer() {
        this.main = TommyHUD.getInstance();
    }
    
    public void drawAllElements() {
        for (final Element element : Element.values()) {
            this.drawElement(element);
        }
    }
    
    public void drawElement(final Element element) {
        this.drawElement(element, null);
    }
    
    public void drawElement(final Element element, final ButtonLocation buttonLocation) {
        float scale = this.main.getConfigManager().getConfigValues().getGuiScale(element);
        if (this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.LARGE_GUI_AND_CROSSHAIR)) {
            scale += (float)0.6;
        }
        GlStateManager.func_179094_E();
        GlStateManager.func_179152_a(scale, scale, 1.0f);
        if (element.getGuiDefault().getDrawType() == DrawType.TEXT) {
            this.drawText(element, scale, buttonLocation);
        }
        else if (element.getGuiDefault().getDrawType() == DrawType.ICON_TEXT) {
            this.drawIconText(element, scale, buttonLocation);
        }
        else if (element.getGuiDefault().getDrawType() == DrawType.POTION) {
            this.drawPotions(scale, buttonLocation);
        }
        else if (element.getGuiDefault().getDrawType() == DrawType.ARMOR) {
            this.drawArmorStatus(scale, buttonLocation);
        }
        else if (element.getGuiDefault().getDrawType() == DrawType.KEYSTROKES) {
            this.drawKeystrokes(scale, buttonLocation);
        }
        GlStateManager.func_179121_F();
    }
    
    public void drawText(final Element element, final float scale, final ButtonLocation buttonLocation) {
        final boolean isPreview = buttonLocation != null;
        final Minecraft mc = Minecraft.func_71410_x();
        String text = "Default";
        if (element == Element.FPS) {
            text = Minecraft.func_175610_ah() + " FPS";
        }
        else if (element == Element.CPS_LEFT) {
            text = this.main.getListener().getLeftClickCPS() + " CPS (LMB)";
        }
        else if (element == Element.CPS_RIGHT) {
            text = this.main.getListener().getRightClickCPS() + " CPS (RMB)";
        }
        else if (element == Element.COORDINATES) {
            final Entity player = (Entity)Minecraft.func_71410_x().field_71439_g;
            text = "X: " + Math.round(player.field_70165_t) + "\nY: " + Math.round(player.field_70163_u) + "\nZ: " + Math.round(player.field_70161_v);
        }
        else if (element == Element.CLOCK) {
            final DateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
            text = dateFormat.format(new Date());
        }
        else if (element == Element.HORIZONTAL_SPEED) {
            final DecimalFormat decimalFormat = new DecimalFormat("#.##");
            double velocity = 0.0;
            if (mc.field_71439_g != null && this.main.getListener().getLastSecondPosition() != null) {
                final Vec3 lastSecondPosition = this.main.getListener().getLastSecondPosition();
                final Vec3 currentPosition = mc.field_71439_g.func_174791_d();
                velocity = Math.sqrt(Math.pow(lastSecondPosition.field_72450_a - currentPosition.field_72450_a, 2.0) + Math.pow(lastSecondPosition.field_72449_c - currentPosition.field_72449_c, 2.0));
                this.velocityHorizontal = velocity;
                final double finalVelocity = velocity;
                this.main.getScheduler().scheduleDelayedTask(new SkyblockRunnable() {
                    @Override
                    public void run() {
                        Renderer.this.lastSecondVelocityHorizontal = finalVelocity;
                    }
                }, 10);
            }
            else {
                this.velocityHorizontal = -1.0;
                this.main.getScheduler().scheduleDelayedTask(new SkyblockRunnable() {
                    @Override
                    public void run() {
                        Renderer.this.lastSecondVelocityHorizontal = -1.0;
                    }
                }, 10);
            }
            text = "Horiz. Velocity: " + decimalFormat.format(velocity) + " m/s";
        }
        else if (element == Element.VERTICAL_SPEED) {
            final DecimalFormat decimalFormat = new DecimalFormat("#.##");
            double velocity = 0.0;
            if (mc.field_71439_g != null && this.main.getListener().getLastSecondPosition() != null) {
                final Vec3 lastSecondPosition = this.main.getListener().getLastSecondPosition();
                final Vec3 currentPosition = mc.field_71439_g.func_174791_d();
                velocity = Math.abs(lastSecondPosition.field_72448_b - currentPosition.field_72448_b);
                this.velocityVertical = velocity;
                final double finalVelocity = velocity;
                this.main.getScheduler().scheduleDelayedTask(new SkyblockRunnable() {
                    @Override
                    public void run() {
                        Renderer.this.lastSecondVelocityVertical = finalVelocity;
                    }
                }, 10);
            }
            else {
                this.velocityVertical = -1.0;
                this.main.getScheduler().scheduleDelayedTask(new SkyblockRunnable() {
                    @Override
                    public void run() {
                        Renderer.this.lastSecondVelocityVertical = -1.0;
                    }
                }, 10);
            }
            text = "Vert. Velocity: " + decimalFormat.format(velocity) + " m/s";
        }
        else if (element == Element.CHUNK_COORDINATES) {
            final Entity player = (Entity)Minecraft.func_71410_x().field_71439_g;
            text = "Chunk:\nX: " + player.field_70176_ah + "\nY: " + player.field_70162_ai + "\nZ: " + player.field_70164_aj;
        }
        else if (element == Element.HORIZONTAL_ACCELERATION) {
            final DecimalFormat decimalFormat = new DecimalFormat("#.##");
            double acceleration = 0.0;
            if (this.lastSecondVelocityHorizontal != -1.0 && this.velocityHorizontal != -1.0) {
                final double lastSecondVelocity = this.lastSecondVelocityHorizontal;
                final double velocity2 = this.velocityHorizontal;
                acceleration = velocity2 - lastSecondVelocity;
            }
            text = "Horiz. Accel: " + decimalFormat.format(acceleration) + " m/s/s";
        }
        else if (element == Element.VERTICAL_ACCELERATION) {
            final DecimalFormat decimalFormat = new DecimalFormat("#.##");
            double acceleration = 0.0;
            if (this.lastSecondVelocityVertical != -1.0 && this.velocityVertical != -1.0) {
                final double lastSecondVelocity = this.lastSecondVelocityVertical;
                final double velocity2 = this.velocityVertical;
                acceleration = velocity2 - lastSecondVelocity;
            }
            text = "Vert. Accel: " + decimalFormat.format(acceleration) + " m/s/s";
        }
        else if (element == Element.LIGHT_LEVEL) {
            if (mc.func_175606_aa() != null && mc.field_71441_e != null) {
                final BlockPos blockPos = new BlockPos(mc.func_175606_aa().field_70165_t, mc.func_175606_aa().func_174813_aQ().field_72338_b, mc.func_175606_aa().field_70161_v);
                final Chunk chunk = mc.field_71441_e.func_175726_f(blockPos);
                text = "Light Level: " + chunk.func_177443_a(blockPos, 0);
            }
        }
        else if (element == Element.BIOME) {
            if (mc.func_175606_aa() != null && mc.field_71441_e != null) {
                final BlockPos blockPos = new BlockPos(mc.func_175606_aa().field_70165_t, mc.func_175606_aa().func_174813_aQ().field_72338_b, mc.func_175606_aa().field_70161_v);
                final Chunk chunk = mc.field_71441_e.func_175726_f(blockPos);
                text = "Biome: " + chunk.func_177411_a(blockPos, mc.field_71441_e.func_72959_q()).field_76791_y;
            }
        }
        else if (element == Element.MEMORY) {
            final long max = Runtime.getRuntime().maxMemory();
            final long total = Runtime.getRuntime().totalMemory();
            final long free = Runtime.getRuntime().freeMemory();
            final long remaining = total - free;
            text = String.format("Memory: %03d/%03d MB", remaining / 1024L / 1024L, max / 1024L / 1024L);
        }
        else if (element == Element.PING) {
            final NetworkPlayerInfo networkPlayerInfo = mc.field_71439_g.field_71174_a.func_175102_a(Minecraft.func_71410_x().field_71439_g.func_110124_au());
            int ping = 0;
            if (networkPlayerInfo != null) {
                ping = networkPlayerInfo.func_178853_c();
            }
            text = ping + " Ping";
        }
        else if (element == Element.SUBSCRIBE_TO_TOMMYINNIT) {
            text = "Subscribe to TommyInnit!!!";
        }
        else if (element == Element.POKIMANE_SUBSCRIPTION_TIER) {
            text = "Pokimane Subscription Tier: 3";
        }
        else if (element == Element.DATE) {
            final DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMMM, yyyy", Locale.US);
            text = dateFormat.format(new Date());
        }
        else if (element == Element.DIRECTION_FACING) {
            String direction = "Unknown";
            if (mc.func_175606_aa() != null) {
                final EnumFacing facing = mc.func_175606_aa().func_174811_aO();
                direction = WordUtils.capitalize(facing.func_176610_l());
            }
            text = "Direction: " + direction;
        }
        else if (element == Element.YAW) {
            float yaw = 0.0f;
            if (mc.field_71439_g != null) {
                yaw = MathHelper.func_76142_g(mc.field_71439_g.field_70177_z);
            }
            text = "Yaw: " + String.format("%.1f", yaw);
        }
        else if (element == Element.PITCH) {
            float pitch = 0.0f;
            if (mc.field_71439_g != null) {
                pitch = MathHelper.func_76142_g(mc.field_71439_g.field_70125_A);
            }
            text = "Pitch: " + String.format("%.1f", pitch);
        }
        else if (element == Element.PLAYERS) {
            int players = 0;
            if (mc.field_71439_g != null && mc.field_71439_g.field_71174_a != null && mc.field_71439_g.field_71174_a.func_175106_d() != null) {
                players = mc.field_71439_g.field_71174_a.func_175106_d().size();
            }
            text = "Players: " + players;
        }
        float x = this.main.getConfigManager().getConfigValues().getActualX(element);
        float y = this.main.getConfigManager().getConfigValues().getActualY(element);
        int width = mc.field_71466_p.func_78256_a(text);
        final int textHeight = 8;
        final int lineSpacing = 2;
        int height = textHeight;
        String[] lines = null;
        if (text.contains("\n")) {
            lines = text.split("\n");
            height = (textHeight + lineSpacing) * lines.length - lineSpacing;
            width = 0;
            for (final String line : lines) {
                width = Math.max(width, mc.field_71466_p.func_78256_a(line));
            }
        }
        final boolean drawBackground = true;
        if (drawBackground) {
            width += 4;
            height += 4;
        }
        x = this.transformX(x, width, scale, element);
        y = this.transformY(y, height, scale, element);
        if (isPreview) {
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            buttonLocation.checkHoveredAndDrawBox(x, x + width, y, y + height, scale);
        }
        if (drawBackground) {
            this.main.getUtils().drawRect(x, y, x + width, y + height, 1711276032, this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.CRAZY_CHROMA_MODE));
            x += 2.0f;
            y += 2.0f;
        }
        GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        ChromaManager.renderingText(element);
        final int color = this.main.getConfigManager().getConfigValues().getColor(element).getRGB();
        if (lines == null) {
            this.main.getUtils().drawTextWithStyle(text, x, y, color);
        }
        else {
            for (int line2 = 0; line2 < lines.length; ++line2) {
                final float currentY = y + (textHeight + lineSpacing) * line2;
                this.main.getUtils().drawTextWithStyle(lines[line2], x, currentY, color);
            }
        }
        ChromaManager.doneRenderingText();
    }
    
    public void drawIconText(final Element element, final float scale, final ButtonLocation buttonLocation) {
        final boolean isPreview = buttonLocation != null;
        final Minecraft mc = Minecraft.func_71410_x();
        final ResourceLocation resourceLocation = null;
        ItemStack itemStack = null;
        String text = "Default";
        if (element == Element.ARROW_COUNT) {
            itemStack = new ItemStack(Items.field_151032_g);
            int arrowCount = 0;
            if (mc.field_71439_g != null) {
                for (final ItemStack inventoryStack : mc.field_71439_g.field_71071_by.field_70462_a) {
                    if (inventoryStack != null && inventoryStack.func_77973_b() == Items.field_151032_g) {
                        arrowCount += inventoryStack.field_77994_a;
                    }
                }
            }
            text = String.valueOf(arrowCount);
        }
        float x = this.main.getConfigManager().getConfigValues().getActualX(element);
        float y = this.main.getConfigManager().getConfigValues().getActualY(element);
        int width = 18 + mc.field_71466_p.func_78256_a(text);
        final int textHeight = 8;
        final int lineSpacing = 2;
        int height = 16;
        String[] lines = null;
        if (text.contains("\n")) {
            lines = text.split("\n");
            height = (textHeight + lineSpacing) * lines.length - lineSpacing;
            width = 0;
            for (final String line : lines) {
                width = Math.max(width, mc.field_71466_p.func_78256_a(line));
            }
        }
        final boolean drawBackground = true;
        if (drawBackground) {
            width += 4;
            height += 4;
        }
        x = this.transformX(x, width, scale, element);
        y = this.transformY(y, height, scale, element);
        if (isPreview) {
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            buttonLocation.checkHoveredAndDrawBox(x, x + width, y, y + height, scale);
        }
        if (drawBackground) {
            this.main.getUtils().drawRect(x, y, x + width, y + height, 1711276032, this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.CRAZY_CHROMA_MODE));
            x += 2.0f;
            y += 2.0f;
        }
        GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        if (resourceLocation != null) {
            mc.func_110434_K().func_110577_a(resourceLocation);
            this.main.getUtils().drawTexturedRectangle(x, y, 0.0f, 0.0f, 16.0f, 16.0f, 16.0f, 16.0f);
        }
        else if (itemStack != null) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(x, y, 0.0f);
            mc.func_175599_af().func_175042_a(itemStack, 0, 0);
            GlStateManager.func_179121_F();
        }
        GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        ChromaManager.renderingText(element);
        final int color = this.main.getConfigManager().getConfigValues().getColor(element).getRGB();
        if (lines == null) {
            this.main.getUtils().drawTextWithStyle(text, x + 16.0f + 2.0f, y + height / 2.0f - 4.0f, color);
        }
        else {
            for (int line2 = 0; line2 < lines.length; ++line2) {
                final float currentY = y + (textHeight + lineSpacing) * line2;
                this.main.getUtils().drawTextWithStyle(lines[line2], x + 16.0f + 2.0f, currentY, color);
            }
        }
        ChromaManager.doneRenderingText();
    }
    
    public void drawPotions(final float scale, final ButtonLocation buttonLocation) {
        final boolean isPreview = buttonLocation != null;
        final Minecraft mc = Minecraft.func_71410_x();
        float x = this.main.getConfigManager().getConfigValues().getActualX(Element.POTION_DISPLAY);
        float y = this.main.getConfigManager().getConfigValues().getActualY(Element.POTION_DISPLAY);
        Collection<PotionEffect> potions = (Collection<PotionEffect>)mc.field_71439_g.func_70651_bq();
        if (potions.isEmpty()) {
            if (!isPreview) {
                return;
            }
            potions = Collections.singletonList(new PotionEffect(Potion.field_76424_c.func_76396_c(), 600));
        }
        int longestPotionName = 0;
        for (final PotionEffect effect : potions) {
            final Potion potion = Potion.field_76425_a[effect.func_76456_a()];
            if (!potion.shouldRender(effect)) {
                continue;
            }
            final String potionName = I18n.func_135052_a(potion.func_76393_a(), new Object[0]) + " " + effect.func_76458_c();
            longestPotionName = Math.max(longestPotionName, mc.field_71466_p.func_78256_a(potionName));
            final String durationString = Potion.func_76389_a(effect);
            longestPotionName = Math.max(longestPotionName, mc.field_71466_p.func_78256_a(durationString));
        }
        final int lineHeight = 18;
        final int lineSpacing = 4;
        int width = 22 + longestPotionName;
        int height = (lineHeight + lineSpacing) * potions.size() - lineSpacing;
        final boolean drawBackground = true;
        if (drawBackground) {
            width += 4;
            height += 4;
        }
        x = this.transformX(x, width, scale, Element.POTION_DISPLAY);
        y = this.transformY(y, height, scale, Element.POTION_DISPLAY);
        if (isPreview) {
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            buttonLocation.checkHoveredAndDrawBox(x, x + width, y, y + height, scale);
        }
        if (drawBackground) {
            this.main.getUtils().drawRect(x, y, x + width, y + height, 1711276032, this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.CRAZY_CHROMA_MODE));
            x += 2.0f;
            y += 2.0f;
        }
        GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.func_179140_f();
        for (final PotionEffect effect2 : potions) {
            final Potion potion2 = Potion.field_76425_a[effect2.func_76456_a()];
            if (!potion2.shouldRender(effect2)) {
                continue;
            }
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            if (potion2.func_76400_d()) {
                final int i1 = potion2.func_76392_e();
                mc.func_110434_K().func_110577_a(Renderer.inventoryBackground);
                this.main.getUtils().drawTexturedModalRect(x, y, i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
            }
            if (!potion2.shouldRenderInvText(effect2)) {
                y += lineHeight + lineSpacing;
            }
            else {
                final String potionName2 = I18n.func_135052_a(potion2.func_76393_a(), new Object[0]) + " " + (effect2.func_76458_c() + 1);
                mc.field_71466_p.func_175063_a(potionName2, x + 4.0f + 18.0f, y, ColorCode.WHITE.getRGB());
                final String durationString2 = Potion.func_76389_a(effect2);
                mc.field_71466_p.func_175063_a(durationString2, x + 4.0f + 18.0f, y + 10.0f, ColorCode.WHITE.getRGB());
                y += lineHeight + lineSpacing;
            }
        }
    }
    
    public void drawArmorStatus(final float scale, final ButtonLocation buttonLocation) {
        final boolean isPreview = buttonLocation != null;
        final Minecraft mc = Minecraft.func_71410_x();
        final EntityPlayerSP player = mc.field_71439_g;
        float x = this.main.getConfigManager().getConfigValues().getActualX(Element.ARMOR_DISPLAY);
        float y = this.main.getConfigManager().getConfigValues().getActualY(Element.ARMOR_DISPLAY);
        final ItemStack[] armorInventory = player.func_70035_c().clone();
        boolean foundPiece = false;
        for (int armorPiece = 0; armorPiece < 4; ++armorPiece) {
            if (armorInventory[armorPiece] != null) {
                foundPiece = true;
                break;
            }
        }
        if (!foundPiece) {
            if (!isPreview) {
                return;
            }
            armorInventory[0] = new ItemStack((Item)Items.field_151175_af);
            if (armorInventory[1] != null) {
                armorInventory[1] = new ItemStack((Item)Items.field_151173_ae);
            }
            if (armorInventory[2] != null) {
                armorInventory[2] = new ItemStack((Item)Items.field_151163_ad);
            }
            if (armorInventory[3] != null) {
                armorInventory[3] = new ItemStack((Item)Items.field_151161_ac);
            }
        }
        int longestLine = 0;
        int armorPieces = 0;
        for (int armorPiece2 = 0; armorPiece2 < 4; ++armorPiece2) {
            final ItemStack itemStack = armorInventory[armorPiece2];
            if (itemStack != null) {
                ++armorPieces;
                longestLine = Math.max(longestLine, mc.field_71466_p.func_78256_a(this.getDurabilityPercent(itemStack) + "%"));
            }
        }
        int width = 18 + longestLine;
        final int lineSpacing = 2;
        int height = (16 + lineSpacing) * armorPieces - lineSpacing;
        final boolean drawBackground = true;
        if (drawBackground) {
            width += 4;
            height += 4;
        }
        x = this.transformX(x, width, scale, Element.ARMOR_DISPLAY);
        y = this.transformY(y, height, scale, Element.ARMOR_DISPLAY);
        if (isPreview) {
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            buttonLocation.checkHoveredAndDrawBox(x, x + width, y, y + height, scale);
        }
        if (drawBackground) {
            this.main.getUtils().drawRect(x, y, x + width, y + height, 1711276032, this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.CRAZY_CHROMA_MODE));
            x += 2.0f;
            y += 2.0f;
        }
        GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        final int color = this.main.getConfigManager().getConfigValues().getColor(Element.ARMOR_DISPLAY).getRGB();
        float currentY = y;
        for (int armorPiece3 = 3; armorPiece3 >= 0; --armorPiece3) {
            final ItemStack itemStack2 = armorInventory[armorPiece3];
            if (itemStack2 != null) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179109_b(x, currentY, 0.0f);
                mc.func_175599_af().func_175042_a(itemStack2, 0, 0);
                GlStateManager.func_179121_F();
                ChromaManager.renderingText(Element.ARMOR_DISPLAY);
                final String damage = this.getDurabilityPercent(itemStack2) + "%";
                this.main.getUtils().drawTextWithStyle(damage, x + 16.0f + 2.0f, currentY + 8.0f - 4.0f, color);
                ChromaManager.doneRenderingText();
                currentY += 16 + lineSpacing;
            }
        }
    }
    
    public int getDurabilityPercent(final ItemStack itemStack) {
        if (itemStack.func_77958_k() == 0) {
            return 100;
        }
        return 100 - (int)(itemStack.func_77952_i() / (float)itemStack.func_77958_k() * 100.0f);
    }
    
    public void drawKeystrokes(final float scale, final ButtonLocation buttonLocation) {
        final boolean isPreview = buttonLocation != null;
        final Minecraft mc = Minecraft.func_71410_x();
        float x = this.main.getConfigManager().getConfigValues().getActualX(Element.KEYSTROKES);
        float y = this.main.getConfigManager().getConfigValues().getActualY(Element.KEYSTROKES);
        final int boxSize = 12;
        final int spacing = 1;
        final int spaceHeight = 8;
        final int width = (boxSize + spacing) * 3 - spacing;
        final int height = (boxSize + spacing) * 3 + (spaceHeight + spacing) - spacing;
        x = this.transformX(x, width, scale, Element.KEYSTROKES);
        y = this.transformY(y, height, scale, Element.KEYSTROKES);
        if (isPreview) {
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            buttonLocation.checkHoveredAndDrawBox(x, x + width, y, y + height, scale);
        }
        final int color = this.main.getConfigManager().getConfigValues().getColor(Element.POTION_DISPLAY).getRGB();
        final int unpressedColor = 1711276032;
        final int pressedColor = 1721342361;
        float startingX = x + boxSize + spacing;
        float startingY = y;
        this.drawKeystrokesRect(mc.field_71474_y.field_74351_w, startingX, startingY, startingX + boxSize, startingY + boxSize, unpressedColor, pressedColor);
        this.drawKeystrokesString(mc.field_71474_y.field_74351_w, startingX, (float)boxSize, startingY, (float)boxSize, color);
        startingX = x;
        startingY += boxSize + spacing;
        this.drawKeystrokesRect(mc.field_71474_y.field_74370_x, startingX, startingY, startingX + boxSize, startingY + boxSize, unpressedColor, pressedColor);
        this.drawKeystrokesString(mc.field_71474_y.field_74370_x, startingX, (float)boxSize, startingY, (float)boxSize, color);
        startingX += boxSize + spacing;
        this.drawKeystrokesRect(mc.field_71474_y.field_74368_y, startingX, startingY, startingX + boxSize, startingY + boxSize, unpressedColor, pressedColor);
        this.drawKeystrokesString(mc.field_71474_y.field_74368_y, startingX, (float)boxSize, startingY, (float)boxSize, color);
        startingX += boxSize + spacing;
        this.drawKeystrokesRect(mc.field_71474_y.field_74366_z, startingX, startingY, startingX + boxSize, startingY + boxSize, unpressedColor, pressedColor);
        this.drawKeystrokesString(mc.field_71474_y.field_74366_z, startingX, (float)boxSize, startingY, (float)boxSize, color);
        startingX = x;
        startingY += boxSize + spacing;
        this.drawKeystrokesRect(mc.field_71474_y.field_74312_F, startingX, startingY, startingX + width / 2.0f - spacing / 2.0f, startingY + boxSize, unpressedColor, pressedColor);
        this.drawKeystrokesString(mc.field_71474_y.field_74312_F, startingX, width / 2.0f, startingY, (float)boxSize, color);
        startingX += width / 2.0f + spacing / 2.0f;
        this.drawKeystrokesRect(mc.field_71474_y.field_74313_G, startingX, startingY, startingX + width / 2.0f - spacing / 2.0f, startingY + boxSize, unpressedColor, pressedColor);
        this.drawKeystrokesString(mc.field_71474_y.field_74313_G, startingX, width / 2.0f, startingY, (float)boxSize, color);
        startingX = x;
        startingY += boxSize + spacing;
        this.drawKeystrokesRect(mc.field_71474_y.field_74314_A, startingX, startingY, startingX + width, startingY + spaceHeight, unpressedColor, pressedColor);
        final int spacebarMiddleWidth = 26;
        final int spacebarMiddleHeight = 2;
        this.main.getUtils().drawRect(startingX + width / 2.0f - spacebarMiddleWidth / 2.0f, startingY + spaceHeight / 2.0f - spacebarMiddleHeight / 2.0f, startingX + width / 2.0f + spacebarMiddleWidth / 2.0f, startingY + spaceHeight / 2.0f + spacebarMiddleHeight / 2.0f, color);
    }
    
    private void drawKeystrokesRect(final KeyBinding keyBinding, final double left, final double top, final double right, final double bottom, final int unpressedColor, final int pressedColor) {
        final boolean pressed = keyBinding.func_151468_f() || keyBinding.func_151470_d();
        this.main.getUtils().drawRect(left, top, right, bottom, pressed ? pressedColor : unpressedColor, this.main.getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.CRAZY_CHROMA_MODE));
    }
    
    private void drawKeystrokesString(final KeyBinding keyBinding, final float startingX, final float width, final float startingY, final float height, final int color) {
        final Minecraft mc = Minecraft.func_71410_x();
        ChromaManager.renderingText(Element.ARMOR_DISPLAY);
        final String keyName = this.getKeyName(keyBinding);
        GlStateManager.func_179094_E();
        final float scale = 0.75f;
        GlStateManager.func_179152_a(scale, scale, 1.0f);
        final float x = startingX / scale + width / 2.0f / scale - mc.field_71466_p.func_78256_a(keyName) / 2.0f + 0.5f;
        final float y = startingY / scale + height / 2.0f / scale - 4.0f;
        this.main.getUtils().drawTextWithStyle(keyName, x, y, color);
        GlStateManager.func_179121_F();
        ChromaManager.doneRenderingText();
    }
    
    public String getKeyName(final KeyBinding keyBinding) {
        int keyCode = keyBinding.func_151463_i();
        if (keyCode >= 0) {
            return Keyboard.getKeyName(keyCode);
        }
        keyCode += 100;
        if (keyCode == 0) {
            return "LMB";
        }
        if (keyCode == 1) {
            return "RMB";
        }
        return Mouse.getButtonName(keyCode);
    }
    
    public float transformX(float x, final int width, final float scale, final Element element) {
        final float minecraftScale = (float)new ScaledResolution(Minecraft.func_71410_x()).func_78325_e();
        x -= this.main.getUtils().getXOffsetFromAnchorPoint((float)width, scale, this.main.getConfigManager().getConfigValues().getAnchorPoint(element));
        x = Math.round(x * minecraftScale) / minecraftScale;
        return x / scale;
    }
    
    public float transformY(float y, final int height, final float scale, final Element element) {
        final float minecraftScale = (float)new ScaledResolution(Minecraft.func_71410_x()).func_78325_e();
        y -= this.main.getUtils().getYOffsetFromAnchorPoint((float)height, scale, this.main.getConfigManager().getConfigValues().getAnchorPoint(element));
        y = Math.round(y * minecraftScale) / minecraftScale;
        return y / scale;
    }
    
    static {
        inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");
    }
}
