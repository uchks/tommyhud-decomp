package codes.biscuit.tommyhud.util;

import codes.biscuit.tommyhud.util.objects.*;
import com.google.common.cache.*;
import java.awt.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import codes.biscuit.tommyhud.misc.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;
import codes.biscuit.tommyhud.*;
import codes.biscuit.tommyhud.core.*;
import java.util.concurrent.*;
import codes.biscuit.tommyhud.config.*;
import net.minecraft.util.*;

public class Utils
{
    private Cache<Float, IntPair[]> glitchyStringsCache;
    
    public Utils() {
        this.glitchyStringsCache = (Cache<Float, IntPair[]>)CacheBuilder.newBuilder().expireAfterWrite(50L, TimeUnit.MILLISECONDS).build();
    }
    
    public int getDefaultColor(final float alphaFloat) {
        final int alpha = (int)alphaFloat;
        return new Color(255, 128, 128, alpha).getRGB();
    }
    
    public void drawRect(final double left, final double top, final double right, final double bottom, final int color) {
        this.drawRect(left, top, right, bottom, color, false);
    }
    
    public void drawRect(final double left, final double top, final double right, final double bottom, final int color, final boolean chroma) {
        this.drawRect(left, top, right, bottom, color, chroma, 255);
    }
    
    public void drawRect(double left, double top, double right, double bottom, final int color, final boolean chroma, final int chromaAlpha) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }
        if (!chroma) {
            final float f3 = (color >> 24 & 0xFF) / 255.0f;
            final float f4 = (color >> 16 & 0xFF) / 255.0f;
            final float f5 = (color >> 8 & 0xFF) / 255.0f;
            final float f6 = (color & 0xFF) / 255.0f;
            GlStateManager.func_179131_c(f4, f5, f6, f3);
        }
        final Tessellator tessellator = Tessellator.func_178181_a();
        final WorldRenderer worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        if (chroma) {
            GlStateManager.func_179103_j(7425);
            worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
            this.posChromaColor(worldrenderer, left, bottom, chromaAlpha);
            this.posChromaColor(worldrenderer, right, bottom, chromaAlpha);
            this.posChromaColor(worldrenderer, right, top, chromaAlpha);
            this.posChromaColor(worldrenderer, left, top, chromaAlpha);
            tessellator.func_78381_a();
        }
        else {
            worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
            worldrenderer.func_181662_b(left, bottom, 0.0).func_181675_d();
            worldrenderer.func_181662_b(right, bottom, 0.0).func_181675_d();
            worldrenderer.func_181662_b(right, top, 0.0).func_181675_d();
            worldrenderer.func_181662_b(left, top, 0.0).func_181675_d();
            tessellator.func_78381_a();
        }
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }
    
    public void posChromaColor(final WorldRenderer worldRenderer, final double x, final double y) {
        this.posChromaColor(worldRenderer, x, y, 255);
    }
    
    public void posChromaColor(final WorldRenderer worldRenderer, final double x, final double y, final int alpha) {
        final int color = ChromaManager.getChromaColor((float)x, (float)y);
        final float f = (color >> 16 & 0xFF) / 255.0f;
        final float f2 = (color >> 8 & 0xFF) / 255.0f;
        final float f3 = (color & 0xFF) / 255.0f;
        worldRenderer.func_181662_b(x, y, 0.0).func_181666_a(f, f2, f3, alpha / 255.0f).func_181675_d();
    }
    
    public void drawTexturedRectangle(final float x, final float y, final float u, final float v, final float width, final float height, final float textureWidth, final float textureHeight) {
        this.drawTexturedRectangle(x, y, u, v, width, height, textureWidth, textureHeight, false);
    }
    
    public void drawTexturedRectangle(final float x, final float y, final float u, final float v, final float width, final float height, final float textureWidth, final float textureHeight, final boolean linearTexture) {
        if (linearTexture) {
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
        }
        final float f = 1.0f / textureWidth;
        final float f2 = 1.0f / textureHeight;
        final Tessellator tessellator = Tessellator.func_178181_a();
        final WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b((double)x, (double)(y + height), 0.0).func_181673_a((double)(u * f), (double)((v + height) * f2)).func_181675_d();
        worldrenderer.func_181662_b((double)(x + width), (double)(y + height), 0.0).func_181673_a((double)((u + width) * f), (double)((v + height) * f2)).func_181675_d();
        worldrenderer.func_181662_b((double)(x + width), (double)y, 0.0).func_181673_a((double)((u + width) * f), (double)(v * f2)).func_181675_d();
        worldrenderer.func_181662_b((double)x, (double)y, 0.0).func_181673_a((double)(u * f), (double)(v * f2)).func_181675_d();
        tessellator.func_78381_a();
        if (linearTexture) {
            GL11.glTexParameteri(3553, 10241, 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
        }
    }
    
    public void drawCenteredString(final String text, final float x, final float y, final int color) {
        Minecraft.func_71410_x().field_71466_p.func_175065_a(text, x - Minecraft.func_71410_x().field_71466_p.func_78256_a(text) / 2.0f, y, color, true);
    }
    
    public void drawTextWithStyle(final String text, final float x, final float y, final int color) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b(x, y, 0.0f);
        Minecraft.func_71410_x().field_71466_p.func_175065_a(text, 0.0f, 0.0f, color, true);
        if (TommyHUD.getInstance().getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.GLITCHY_MODE)) {
            IntPair[] glitchyStrings = (IntPair[])this.glitchyStringsCache.getIfPresent((Object)(x + y));
            if (glitchyStrings == null) {
                glitchyStrings = new IntPair[2];
                for (int glitchedString = 0; glitchedString < 2; ++glitchedString) {
                    final int randomXOffset = ThreadLocalRandom.current().nextInt(-10, 10);
                    final int randomYOffset = ThreadLocalRandom.current().nextInt(-10, 10);
                    glitchyStrings[glitchedString] = new IntPair(randomXOffset, randomYOffset);
                }
                this.glitchyStringsCache.put((Object)(x + y), (Object)glitchyStrings);
            }
            for (final IntPair glitchyString : glitchyStrings) {
                GlStateManager.func_179141_d();
                GlStateManager.func_179147_l();
                Minecraft.func_71410_x().field_71466_p.func_175065_a(text, (float)glitchyString.getX(), (float)glitchyString.getY(), this.getColorWithAlpha(color, 128), false);
            }
        }
        GlStateManager.func_179121_F();
    }
    
    public void playSound(final String sound, final double pitch) {
        Minecraft.func_71410_x().field_71439_g.func_85030_a(sound, 1.0f, (float)pitch);
    }
    
    public void playSound(final String sound, final double volume, final double pitch) {
        Minecraft.func_71410_x().field_71439_g.func_85030_a(sound, (float)volume, (float)pitch);
    }
    
    public void drawTexturedModalRect(final float xCoord, final float yCoord, final int minU, final int minV, final int maxU, final int maxV) {
        final int zLevel = 0;
        final float f = 0.00390625f;
        final float f2 = 0.00390625f;
        final Tessellator tessellator = Tessellator.func_178181_a();
        final WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b((double)(xCoord + 0.0f), (double)(yCoord + maxV), (double)zLevel).func_181673_a((double)((minU + 0) * f), (double)((minV + maxV) * f2)).func_181675_d();
        worldrenderer.func_181662_b((double)(xCoord + maxU), (double)(yCoord + maxV), (double)zLevel).func_181673_a((double)((minU + maxU) * f), (double)((minV + maxV) * f2)).func_181675_d();
        worldrenderer.func_181662_b((double)(xCoord + maxU), (double)(yCoord + 0.0f), (double)zLevel).func_181673_a((double)((minU + maxU) * f), (double)((minV + 0) * f2)).func_181675_d();
        worldrenderer.func_181662_b((double)(xCoord + 0.0f), (double)(yCoord + 0.0f), (double)zLevel).func_181673_a((double)((minU + 0) * f), (double)((minV + 0) * f2)).func_181675_d();
        tessellator.func_78381_a();
    }
    
    public float getXOffsetFromAnchorPoint(final float width, final float scale, final AnchorPoint anchorPoint) {
        if (anchorPoint == AnchorPoint.TOP_RIGHT || anchorPoint == AnchorPoint.BOTTOM_RIGHT) {
            return width * scale;
        }
        if (anchorPoint == AnchorPoint.TOP_LEFT || anchorPoint == AnchorPoint.BOTTOM_LEFT) {
            return 0.0f;
        }
        return width * scale / 2.0f;
    }
    
    public float getYOffsetFromAnchorPoint(final float height, final float scale, final AnchorPoint anchorPoint) {
        if (anchorPoint == AnchorPoint.BOTTOM_LEFT || anchorPoint == AnchorPoint.BOTTOM_RIGHT || anchorPoint == AnchorPoint.BOTTOM_MIDDLE) {
            return height * scale;
        }
        if (anchorPoint == AnchorPoint.TOP_LEFT || anchorPoint == AnchorPoint.TOP_RIGHT) {
            return 0.0f;
        }
        return height * scale / 2.0f;
    }
    
    public float denormalizeScale(final float value, final float min, final float max, final float step) {
        return this.snapToStepClamp(min + (max - min) * MathHelper.func_76131_a(value, 0.0f, 1.0f), min, max, step);
    }
    
    private float snapToStepClamp(float value, final float min, final float max, final float step) {
        value = step * Math.round(value / step);
        return MathHelper.func_76131_a(value, min, max);
    }
    
    public int getColorWithAlpha(final int color, final int alpha) {
        return color + (alpha << 24 & 0xFF000000);
    }
}
