package codes.biscuit.tommyhud.util;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.config.AnchorPoint;
import codes.biscuit.tommyhud.core.CrazyMode;
import codes.biscuit.tommyhud.misc.ChromaManager;
import codes.biscuit.tommyhud.util.objects.IntPair;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class Utils {
    private Cache<Float, IntPair[]> glitchyStringsCache = CacheBuilder.newBuilder().expireAfterWrite(50L, TimeUnit.MILLISECONDS).build();

    public int getDefaultColor(float alphaFloat) {
        int alpha = (int)alphaFloat;
        return (new Color(255, 128, 128, alpha)).getRGB();
    }

    public void drawRect(double left, double top, double right, double bottom, int color) {
        this.drawRect(left, top, right, bottom, color, false);
    }

    public void drawRect(double left, double top, double right, double bottom, int color, boolean chroma) {
        this.drawRect(left, top, right, bottom, color, chroma, 255);
    }

    public void drawRect(double left, double top, double right, double bottom, int color, boolean chroma, int chromaAlpha) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        if (!chroma) {
            float f3 = (float)(color >> 24 & 255) / 255.0F;
            float f = (float)(color >> 16 & 255) / 255.0F;
            float f1 = (float)(color >> 8 & 255) / 255.0F;
            float f2 = (float)(color & 255) / 255.0F;
            GlStateManager.color(f, f1, f2, f3);
        }

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        if (chroma) {
            GlStateManager.shadeModel(7425);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            this.posChromaColor(worldrenderer, left, bottom, chromaAlpha);
            this.posChromaColor(worldrenderer, right, bottom, chromaAlpha);
            this.posChromaColor(worldrenderer, right, top, chromaAlpha);
            this.posChromaColor(worldrenderer, left, top, chromaAlpha);
            tessellator.draw();
        } else {
            worldrenderer.begin(7, DefaultVertexFormats.POSITION);
            worldrenderer.pos(left, bottom, 0.0D).endVertex();
            worldrenderer.pos(right, bottom, 0.0D).endVertex();
            worldrenderer.pos(right, top, 0.0D).endVertex();
            worldrenderer.pos(left, top, 0.0D).endVertex();
            tessellator.draw();
        }

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public void posChromaColor(WorldRenderer worldRenderer, double x, double y) {
        this.posChromaColor(worldRenderer, x, y, 255);
    }

    public void posChromaColor(WorldRenderer worldRenderer, double x, double y, int alpha) {
        int color = ChromaManager.getChromaColor((float)x, (float)y);
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        worldRenderer.pos(x, y, 0.0D).color(f, f1, f2, (float)alpha / 255.0F).endVertex();
    }

    public void drawTexturedRectangle(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        this.drawTexturedRectangle(x, y, u, v, width, height, textureWidth, textureHeight, false);
    }

    public void drawTexturedRectangle(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight, boolean linearTexture) {
        if (linearTexture) {
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
        }

        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)x, (double)(y + height), 0.0D).tex((double)(u * f), (double)((v + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((u + width) * f), (double)((v + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)y, 0.0D).tex((double)((u + width) * f), (double)(v * f1)).endVertex();
        worldrenderer.pos((double)x, (double)y, 0.0D).tex((double)(u * f), (double)(v * f1)).endVertex();
        tessellator.draw();
        if (linearTexture) {
            GL11.glTexParameteri(3553, 10241, 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
        }

    }

    public void drawCenteredString(String text, float x, float y, int color) {
        Minecraft.getMinecraft().fontRendererObj.drawString(text, x - (float)Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) / 2.0F, y, color, true);
    }

    public void drawTextWithStyle(String text, float x, float y, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0.0F);
        Minecraft.getMinecraft().fontRendererObj.drawString(text, 0.0F, 0.0F, color, true);
        if (TommyHUD.getInstance().getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.GLITCHY_MODE)) {
            IntPair[] glitchyStrings = (IntPair[])this.glitchyStringsCache.getIfPresent(x + y);
            if (glitchyStrings == null) {
                glitchyStrings = new IntPair[2];

                for(int glitchedString = 0; glitchedString < 2; ++glitchedString) {
                    int randomXOffset = ThreadLocalRandom.current().nextInt(-10, 10);
                    int randomYOffset = ThreadLocalRandom.current().nextInt(-10, 10);
                    glitchyStrings[glitchedString] = new IntPair(randomXOffset, randomYOffset);
                }

                this.glitchyStringsCache.put(x + y, glitchyStrings);
            }

            for(IntPair glitchyString : glitchyStrings) {
                GlStateManager.enableAlpha();
                GlStateManager.enableBlend();
                Minecraft.getMinecraft().fontRendererObj.drawString(text, (float)glitchyString.getX(), (float)glitchyString.getY(), this.getColorWithAlpha(color, 128), false);
            }
        }

        GlStateManager.popMatrix();
    }

    public void playSound(String sound, double pitch) {
        Minecraft.getMinecraft().thePlayer.playSound(sound, 1.0F, (float)pitch);
    }

    public void playSound(String sound, double volume, double pitch) {
        Minecraft.getMinecraft().thePlayer.playSound(sound, (float)volume, (float)pitch);
    }

    public void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV) {
        int zLevel = 0;
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)(xCoord + 0.0F), (double)(yCoord + (float)maxV), (double)zLevel).tex((double)((float)(minU + 0) * f), (double)((float)(minV + maxV) * f1)).endVertex();
        worldrenderer.pos((double)(xCoord + (float)maxU), (double)(yCoord + (float)maxV), (double)zLevel).tex((double)((float)(minU + maxU) * f), (double)((float)(minV + maxV) * f1)).endVertex();
        worldrenderer.pos((double)(xCoord + (float)maxU), (double)(yCoord + 0.0F), (double)zLevel).tex((double)((float)(minU + maxU) * f), (double)((float)(minV + 0) * f1)).endVertex();
        worldrenderer.pos((double)(xCoord + 0.0F), (double)(yCoord + 0.0F), (double)zLevel).tex((double)((float)(minU + 0) * f), (double)((float)(minV + 0) * f1)).endVertex();
        tessellator.draw();
    }

    public float getXOffsetFromAnchorPoint(float width, float scale, AnchorPoint anchorPoint) {
        if (anchorPoint != AnchorPoint.TOP_RIGHT && anchorPoint != AnchorPoint.BOTTOM_RIGHT) {
            return anchorPoint != AnchorPoint.TOP_LEFT && anchorPoint != AnchorPoint.BOTTOM_LEFT ? width * scale / 2.0F : 0.0F;
        } else {
            return width * scale;
        }
    }

    public float getYOffsetFromAnchorPoint(float height, float scale, AnchorPoint anchorPoint) {
        if (anchorPoint != AnchorPoint.BOTTOM_LEFT && anchorPoint != AnchorPoint.BOTTOM_RIGHT && anchorPoint != AnchorPoint.BOTTOM_MIDDLE) {
            return anchorPoint != AnchorPoint.TOP_LEFT && anchorPoint != AnchorPoint.TOP_RIGHT ? height * scale / 2.0F : 0.0F;
        } else {
            return height * scale;
        }
    }

    public float denormalizeScale(float value, float min, float max, float step) {
        return this.snapToStepClamp(min + (max - min) * MathHelper.clamp_float(value, 0.0F, 1.0F), min, max, step);
    }

    private float snapToStepClamp(float value, float min, float max, float step) {
        value = step * (float)Math.round(value / step);
        return MathHelper.clamp_float(value, min, max);
    }

    public int getColorWithAlpha(int color, int alpha) {
        return color + (alpha << 24 & -16777216);
    }
}
 