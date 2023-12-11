package codes.biscuit.tommyhud.misc;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.core.Element;
import java.awt.Color;
import java.util.Map;

public class ChromaManager {
    private static final int CHROMA_SPEED = 2;
    private static final int CHROMA_WIDTH = 10;
    private TommyHUD main = TommyHUD.getInstance();
    private static boolean coloringTextChroma;
    private static float[] defaultColorHSB = new float[]{0.0F, 0.72F, 0.9F};

    public static int getChromaColor(float x, float y) {
        defaultColorHSB[1] = 0.8F;
        defaultColorHSB[2] = 0.8F;
        return getChromaColor(x, y, defaultColorHSB);
    }

    public static int getChromaColor(float x, float y, float[] currentHSB) {
        float chromaWidth = TommyHUD.getInstance().getUtils().denormalizeScale(10.0F, 1.0F, 42.0F, 1.0F) / 360.0F;
        float chromaSpeed = TommyHUD.getInstance().getUtils().denormalizeScale(2.0F, 0.1F, 10.0F, 0.5F) / 360.0F;
        long ticks = TommyHUD.getInstance().getScheduler().getTotalTicks();
        float newHue = (x / 4.0F * chromaWidth + y / 4.0F * chromaWidth - (float)ticks * chromaSpeed) % 1.0F;
        return Color.HSBtoRGB(newHue, currentHSB[1], currentHSB[2]);
    }

    public static void renderingText(Element element) {
        if (TommyHUD.getInstance().getConfigManager().getConfigValues().getChromaFeatures().contains(element)) {
            coloringTextChroma = true;
        }

    }

    public static void doneRenderingText() {
        coloringTextChroma = false;
    }
}
 