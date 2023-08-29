package codes.biscuit.tommyhud.misc;

import codes.biscuit.tommyhud.*;
import java.awt.*;
import codes.biscuit.tommyhud.core.*;

public class ChromaManager
{
    private static final int CHROMA_SPEED = 2;
    private static final int CHROMA_WIDTH = 10;
    private TommyHUD main;
    private static boolean coloringTextChroma;
    private static float[] defaultColorHSB;
    
    public ChromaManager() {
        this.main = TommyHUD.getInstance();
    }
    
    public static int getChromaColor(final float x, final float y) {
        ChromaManager.defaultColorHSB[1] = 0.8f;
        ChromaManager.defaultColorHSB[2] = 0.8f;
        return getChromaColor(x, y, ChromaManager.defaultColorHSB);
    }
    
    public static int getChromaColor(final float x, final float y, final float[] currentHSB) {
        final float chromaWidth = TommyHUD.getInstance().getUtils().denormalizeScale(10.0f, 1.0f, 42.0f, 1.0f) / 360.0f;
        final float chromaSpeed = TommyHUD.getInstance().getUtils().denormalizeScale(2.0f, 0.1f, 10.0f, 0.5f) / 360.0f;
        final long ticks = TommyHUD.getInstance().getScheduler().getTotalTicks();
        final float newHue = (x / 4.0f * chromaWidth + y / 4.0f * chromaWidth - ticks * chromaSpeed) % 1.0f;
        return Color.HSBtoRGB(newHue, currentHSB[1], currentHSB[2]);
    }
    
    public static void renderingText(final Element element) {
        if (TommyHUD.getInstance().getConfigManager().getConfigValues().getChromaFeatures().contains(element)) {
            ChromaManager.coloringTextChroma = true;
        }
    }
    
    public static void doneRenderingText() {
        ChromaManager.coloringTextChroma = false;
    }
    
    static {
        ChromaManager.defaultColorHSB = new float[] { 0.0f, 0.72f, 0.9f };
    }
}
