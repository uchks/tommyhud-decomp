package codes.biscuit.tommyhud.config;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.config.AnchorPoint;
import codes.biscuit.tommyhud.config.ConfigManager;
import codes.biscuit.tommyhud.core.CrazyMode;
import codes.biscuit.tommyhud.core.Element;
import codes.biscuit.tommyhud.gui.buttons.ButtonLocation;
import codes.biscuit.tommyhud.util.ColorCode;
import codes.biscuit.tommyhud.util.objects.FloatPair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

public class ConfigManager {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static float DEFAULT_GUI_SCALE = normalizeValueNoStep(1.35F);
    private static final float GUI_SCALE_MINIMUM = 0.5F;
    private static final float GUI_SCALE_MAXIMUM = 5.0F;
    private TommyHUD main = TommyHUD.getInstance();
    private File configValuesFile;
    private ConfigManager.ConfigValues configValues = new ConfigManager.ConfigValues();
    private ConfigManager.ConfigValues defaultValues;

    public ConfigManager(File configValuesFile) {
        this.configValuesFile = configValuesFile;
    }

    public void loadValues() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("default.json")));
        this.defaultValues = (ConfigManager.ConfigValues)GSON.fromJson(bufferedReader, ConfigManager.ConfigValues.class);
        if (this.configValuesFile.exists()) {
            try {
                FileReader reader = new FileReader(this.configValuesFile);
                Throwable var3 = null;

                try {
                    this.configValues = (ConfigManager.ConfigValues)GSON.fromJson(reader, ConfigManager.ConfigValues.class);
                } catch (Throwable var13) {
                    var3 = var13;
                    throw var13;
                } finally {
                    if (reader != null) {
                        if (var3 != null) {
                            try {
                                reader.close();
                            } catch (Throwable var12) {
                                var3.addSuppressed(var12);
                            }
                        } else {
                            reader.close();
                        }
                    }

                }
            } catch (IllegalStateException | IOException | JsonParseException var15) {
                var15.printStackTrace();
                this.main.getLogger().error("There was an error loading the config. Resetting all settings to default.");
                this.addDefaultsAndSave();
            }
        } else {
            this.addDefaultsAndSave();
        }

    }

    private void addDefaultsAndSave() {
        for(Element element : Element.values()) {
            ColorCode color = element.getGuiDefault().getColor();
            if (color != null) {
                this.configValues.colors.put(element, color.getRGB());
            }
        }

        this.configValues.disabledFeatures.clear();
        this.configValues.scales.clear();
        this.configValues.chromaFeatures.clear();
        this.configValues.positions = new HashMap();

        for(Entry<Element, FloatPair> entry : this.defaultValues.positions.entrySet()) {
            this.configValues.positions.put(entry.getKey(), ((FloatPair)entry.getValue()).cloneCoords());
        }

        this.configValues.anchorPoints = new HashMap(this.defaultValues.anchorPoints);
        this.configValues.scales = new HashMap(this.defaultValues.scales);
        this.saveConfig();
    }

    public void saveConfig() {
        try {
            this.configValuesFile.createNewFile();
            FileWriter writer = new FileWriter(this.configValuesFile);
            Throwable var2 = null;

            try {
                GSON.toJson(this.configValues, writer);
            } catch (Throwable var12) {
                var2 = var12;
                throw var12;
            } finally {
                if (writer != null) {
                    if (var2 != null) {
                        try {
                            writer.close();
                        } catch (Throwable var11) {
                            var2.addSuppressed(var11);
                        }
                    } else {
                        writer.close();
                    }
                }

            }
        } catch (Exception var14) {
            var14.printStackTrace();
            this.main.getLogger().error("An error occurred while attempting to save the config!");
        }

    }

    public static float normalizeValueNoStep(float value) {
        return MathHelper.clamp_float((snapNearOne(value) - 0.5F) / 4.5F, 0.0F, 1.0F);
    }

    public static float denormalizeScale(float value) {
        return snapNearOne(0.5F + 4.5F * MathHelper.clamp_float(value, 0.0F, 1.0F));
    }

    public static float snapNearOne(float value) {
        return value != 1.0F && (double)value > 0.95D && (double)value < 1.05D ? 1.0F : value;
    }

    public ConfigManager.ConfigValues getConfigValues() {
        return this.configValues;
    }

    public static class ConfigValues {
        private Set<CrazyMode> enabledCrazyModes = EnumSet.noneOf(CrazyMode.class);
        private Set<Element> disabledFeatures = EnumSet.noneOf(Element.class);
        private Map<Element, Integer> colors = new HashMap();
        private Map<Element, Float> scales = new EnumMap(Element.class);
        private Map<Element, FloatPair> positions = new EnumMap(Element.class);
        private Map<Element, AnchorPoint> anchorPoints = new EnumMap(Element.class);
        private Set<Element> chromaFeatures = EnumSet.noneOf(Element.class);

        public boolean isEnabled(Element element) {
            return !this.isDisabled(element);
        }

        public boolean isDisabled(Element element) {
            return this.disabledFeatures.contains(element);
        }

        public float getGuiScale(Element element) {
            return this.getGuiScale(element, true);
        }

        public float getGuiScale(Element element, boolean denormalized) {
            float value = ConfigManager.DEFAULT_GUI_SCALE;
            if (this.scales.containsKey(element)) {
                value = this.scales.get(element);
            }

            if (denormalized) {
                value = ConfigManager.denormalizeScale(value);
            }

            return value;
        }

        public AnchorPoint getAnchorPoint(Element element) {
            AnchorPoint defaultPoint = (AnchorPoint)TommyHUD.getInstance().getConfigManager().defaultValues.anchorPoints.getOrDefault(element, AnchorPoint.TOP_LEFT);
            if (defaultPoint == null) {
                defaultPoint = AnchorPoint.BOTTOM_MIDDLE;
            }

            return (AnchorPoint)this.anchorPoints.getOrDefault(element, defaultPoint);
        }

        public float getActualX(Element element) {
            int maxX = (new ScaledResolution(Minecraft.getMinecraft())).getScaledWidth();
            return (float)this.getAnchorPoint(element).getX(maxX) + this.getRelativeCoords(element).getX();
        }

        public float getActualY(Element element) {
            int maxY = (new ScaledResolution(Minecraft.getMinecraft())).getScaledHeight();
            return (float)this.getAnchorPoint(element).getY(maxY) + this.getRelativeCoords(element).getY();
        }

        public FloatPair getRelativeCoords(Element element) {
            if (this.positions.containsKey(element)) {
                return (FloatPair)this.positions.get(element);
            } else {
                this.putDefaultCoordinates(element);
                return this.positions.containsKey(element) ? (FloatPair)this.positions.get(element) : new FloatPair(0.0F, 0.0F);
            }
        }

        public void resetAllElements() {
            ConfigManager configManager = TommyHUD.getInstance().getConfigManager();
            this.positions = new HashMap();

            for(Entry<Element, FloatPair> entry : configManager.defaultValues.positions.entrySet()) {
                this.positions.put(entry.getKey(), ((FloatPair)entry.getValue()).cloneCoords());
            }

            this.anchorPoints = new HashMap(configManager.defaultValues.anchorPoints);
            this.scales = new HashMap(configManager.defaultValues.scales);
        }

        public void putDefaultCoordinates(Element element) {
            FloatPair coords = (FloatPair)TommyHUD.getInstance().getConfigManager().defaultValues.positions.get(element);
            if (coords == null) {
                new FloatPair(100.0F, 100.0F);
            } else {
                coords = coords.cloneCoords();
            }

            if (coords != null) {
                this.positions.put(element, coords);
            }

        }

        public void setCoords(Element element, float x, float y) {
            if (this.positions.containsKey(element)) {
                ((FloatPair)this.positions.get(element)).setX(x);
                ((FloatPair)this.positions.get(element)).setY(y);
            } else {
                this.positions.put(element, new FloatPair(x, y));
            }

        }

        public void setClosestAnchorPoint(Element element, ButtonLocation buttonLocation) {
            float x1 = this.getActualX(element);
            float y1 = this.getActualY(element);
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            int maxX = sr.getScaledWidth();
            int maxY = sr.getScaledHeight();
            double shortestDistance = -1.0D;
            AnchorPoint closestAnchorPoint = AnchorPoint.BOTTOM_MIDDLE;

            for(AnchorPoint point : AnchorPoint.values()) {
                double distance = Point2D.distance((double)x1, (double)y1, (double)point.getX(maxX), (double)point.getY(maxY));
                if (shortestDistance == -1.0D || distance < shortestDistance) {
                    closestAnchorPoint = point;
                    shortestDistance = distance;
                }
            }

            AnchorPoint oldAnchorPoint = this.getAnchorPoint(element);
            if (oldAnchorPoint != closestAnchorPoint) {
                float targetX = this.getActualX(element);
                float targetY = this.getActualY(element);
                float x = targetX - (float)closestAnchorPoint.getX(maxX);
                float y = targetY - (float)closestAnchorPoint.getY(maxY);
                this.anchorPoints.put(element, closestAnchorPoint);
                float width = buttonLocation.getBoxXTwo() - buttonLocation.getBoxXOne();
                float oldXOffset = TommyHUD.getInstance().getUtils().getXOffsetFromAnchorPoint(width, buttonLocation.getScale(), oldAnchorPoint);
                float newXOffset = TommyHUD.getInstance().getUtils().getXOffsetFromAnchorPoint(width, buttonLocation.getScale(), closestAnchorPoint);
                x += newXOffset - oldXOffset;
                float height = buttonLocation.getBoxYTwo() - buttonLocation.getBoxYOne();
                float oldYOffset = TommyHUD.getInstance().getUtils().getYOffsetFromAnchorPoint(height, buttonLocation.getScale(), oldAnchorPoint);
                float newYOffset = TommyHUD.getInstance().getUtils().getYOffsetFromAnchorPoint(height, buttonLocation.getScale(), closestAnchorPoint);
                y += newYOffset - oldYOffset;
                this.setCoords(element, x, y);
            }
        }

        public void setGuiScale(Element element, float scale) {
            this.scales.put(element, scale);
        }

        public void setChroma(Element element, boolean enabled) {
            if (enabled) {
                this.chromaFeatures.add(element);
            } else {
                this.chromaFeatures.remove(element);
            }

        }

        public Set<Element> getChromaFeatures() {
            return this.chromaFeatures;
        }

        public Color getColor(Element element) {
            if (this.chromaFeatures.contains(element)) {
                return Color.WHITE;
            } else {
                ColorCode defaultColor = element.getGuiDefault().getColor();
                return new Color(this.colors.getOrDefault(element, defaultColor != null ? defaultColor.getRGB() : ColorCode.RED.getRGB()));
            }
        }

        public void setColor(Element element, int color) {
            this.colors.put(element, color);
        }

        public Set<CrazyMode> getEnabledCrazyModes() {
            return this.enabledCrazyModes;
        }

        public boolean isCrazyModeEnabled(CrazyMode crazyMode) {
            return this.enabledCrazyModes.contains(crazyMode);
        }
    }
}