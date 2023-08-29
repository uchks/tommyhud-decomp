package codes.biscuit.tommyhud.config;

import codes.biscuit.tommyhud.*;
import codes.biscuit.tommyhud.util.objects.*;
import codes.biscuit.tommyhud.util.*;
import java.io.*;
import net.minecraft.util.*;
import com.google.gson.*;
import codes.biscuit.tommyhud.core.*;
import java.util.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import codes.biscuit.tommyhud.gui.buttons.*;
import java.awt.geom.*;
import java.awt.*;

public class ConfigManager
{
    private static final Gson GSON;
    private static float DEFAULT_GUI_SCALE;
    private static final float GUI_SCALE_MINIMUM = 0.5f;
    private static final float GUI_SCALE_MAXIMUM = 5.0f;
    private TommyHUD main;
    private File configValuesFile;
    private ConfigValues configValues;
    private ConfigValues defaultValues;
    
    public ConfigManager(final File configValuesFile) {
        this.main = TommyHUD.getInstance();
        this.configValues = new ConfigValues();
        this.configValuesFile = configValuesFile;
    }
    
    public void loadValues() {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("default.json")));
        this.defaultValues = (ConfigValues)ConfigManager.GSON.fromJson((Reader)bufferedReader, (Class)ConfigValues.class);
        if (this.configValuesFile.exists()) {
            try (final FileReader reader = new FileReader(this.configValuesFile)) {
                this.configValues = (ConfigValues)ConfigManager.GSON.fromJson((Reader)reader, (Class)ConfigValues.class);
            }
            catch (JsonParseException | IllegalStateException | IOException ex3) {
                final Exception ex2;
                final Exception ex = ex2;
                ex.printStackTrace();
                this.main.getLogger().error("There was an error loading the config. Resetting all settings to default.");
                this.addDefaultsAndSave();
            }
        }
        else {
            this.addDefaultsAndSave();
        }
    }
    
    private void addDefaultsAndSave() {
        for (final Element element : Element.values()) {
            final ColorCode color = element.getGuiDefault().getColor();
            if (color != null) {
                this.configValues.colors.put(element, color.getRGB());
            }
        }
        this.configValues.disabledFeatures.clear();
        this.configValues.scales.clear();
        this.configValues.chromaFeatures.clear();
        this.configValues.positions = (Map<Element, FloatPair>)new HashMap();
        for (final Map.Entry<Element, FloatPair> entry : this.defaultValues.positions.entrySet()) {
            this.configValues.positions.put(entry.getKey(), entry.getValue().cloneCoords());
        }
        this.configValues.anchorPoints = (Map<Element, AnchorPoint>)new HashMap(this.defaultValues.anchorPoints);
        this.configValues.scales = (Map<Element, Float>)new HashMap(this.defaultValues.scales);
        this.saveConfig();
    }
    
    public void saveConfig() {
        try {
            this.configValuesFile.createNewFile();
            try (final FileWriter writer = new FileWriter(this.configValuesFile)) {
                ConfigManager.GSON.toJson((Object)this.configValues, (Appendable)writer);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            this.main.getLogger().error("An error occurred while attempting to save the config!");
        }
    }
    
    public static float normalizeValueNoStep(final float value) {
        return MathHelper.func_76131_a((snapNearOne(value) - 0.5f) / 4.5f, 0.0f, 1.0f);
    }
    
    public static float denormalizeScale(final float value) {
        return snapNearOne(0.5f + 4.5f * MathHelper.func_76131_a(value, 0.0f, 1.0f));
    }
    
    public static float snapNearOne(final float value) {
        if (value != 1.0f && value > 0.95 && value < 1.05) {
            return 1.0f;
        }
        return value;
    }
    
    public ConfigValues getConfigValues() {
        return this.configValues;
    }
    
    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
        ConfigManager.DEFAULT_GUI_SCALE = normalizeValueNoStep(1.35f);
    }
    
    public static class ConfigValues
    {
        private Set<CrazyMode> enabledCrazyModes;
        private Set<Element> disabledFeatures;
        private Map<Element, Integer> colors;
        private Map<Element, Float> scales;
        private Map<Element, FloatPair> positions;
        private Map<Element, AnchorPoint> anchorPoints;
        private Set<Element> chromaFeatures;
        
        public ConfigValues() {
            this.enabledCrazyModes = EnumSet.noneOf(CrazyMode.class);
            this.disabledFeatures = EnumSet.noneOf(Element.class);
            this.colors = new HashMap<Element, Integer>();
            this.scales = new EnumMap<Element, Float>(Element.class);
            this.positions = new EnumMap<Element, FloatPair>(Element.class);
            this.anchorPoints = new EnumMap<Element, AnchorPoint>(Element.class);
            this.chromaFeatures = EnumSet.noneOf(Element.class);
        }
        
        public boolean isEnabled(final Element element) {
            return !this.isDisabled(element);
        }
        
        public boolean isDisabled(final Element element) {
            return this.disabledFeatures.contains(element);
        }
        
        public float getGuiScale(final Element element) {
            return this.getGuiScale(element, true);
        }
        
        public float getGuiScale(final Element element, final boolean denormalized) {
            float value = ConfigManager.DEFAULT_GUI_SCALE;
            if (this.scales.containsKey(element)) {
                value = this.scales.get(element);
            }
            if (denormalized) {
                value = ConfigManager.denormalizeScale(value);
            }
            return value;
        }
        
        public AnchorPoint getAnchorPoint(final Element element) {
            AnchorPoint defaultPoint = TommyHUD.getInstance().getConfigManager().defaultValues.anchorPoints.getOrDefault(element, AnchorPoint.TOP_LEFT);
            if (defaultPoint == null) {
                defaultPoint = AnchorPoint.BOTTOM_MIDDLE;
            }
            return this.anchorPoints.getOrDefault(element, defaultPoint);
        }
        
        public float getActualX(final Element element) {
            final int maxX = new ScaledResolution(Minecraft.func_71410_x()).func_78326_a();
            return this.getAnchorPoint(element).getX(maxX) + this.getRelativeCoords(element).getX();
        }
        
        public float getActualY(final Element element) {
            final int maxY = new ScaledResolution(Minecraft.func_71410_x()).func_78328_b();
            return this.getAnchorPoint(element).getY(maxY) + this.getRelativeCoords(element).getY();
        }
        
        public FloatPair getRelativeCoords(final Element element) {
            if (this.positions.containsKey(element)) {
                return this.positions.get(element);
            }
            this.putDefaultCoordinates(element);
            if (this.positions.containsKey(element)) {
                return this.positions.get(element);
            }
            return new FloatPair(0.0f, 0.0f);
        }
        
        public void resetAllElements() {
            final ConfigManager configManager = TommyHUD.getInstance().getConfigManager();
            this.positions = new HashMap<Element, FloatPair>();
            for (final Map.Entry<Element, FloatPair> entry : configManager.defaultValues.positions.entrySet()) {
                this.positions.put(entry.getKey(), entry.getValue().cloneCoords());
            }
            this.anchorPoints = new HashMap<Element, AnchorPoint>(configManager.defaultValues.anchorPoints);
            this.scales = new HashMap<Element, Float>(configManager.defaultValues.scales);
        }
        
        public void putDefaultCoordinates(final Element element) {
            FloatPair coords = TommyHUD.getInstance().getConfigManager().defaultValues.positions.get(element);
            if (coords == null) {
                new FloatPair(100.0f, 100.0f);
            }
            else {
                coords = coords.cloneCoords();
            }
            if (coords != null) {
                this.positions.put(element, coords);
            }
        }
        
        public void setCoords(final Element element, final float x, final float y) {
            if (this.positions.containsKey(element)) {
                this.positions.get(element).setX(x);
                this.positions.get(element).setY(y);
            }
            else {
                this.positions.put(element, new FloatPair(x, y));
            }
        }
        
        public void setClosestAnchorPoint(final Element element, final ButtonLocation buttonLocation) {
            final float x1 = this.getActualX(element);
            final float y1 = this.getActualY(element);
            final ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
            final int maxX = sr.func_78326_a();
            final int maxY = sr.func_78328_b();
            double shortestDistance = -1.0;
            AnchorPoint closestAnchorPoint = AnchorPoint.BOTTOM_MIDDLE;
            for (final AnchorPoint point : AnchorPoint.values()) {
                final double distance = Point2D.distance(x1, y1, point.getX(maxX), point.getY(maxY));
                if (shortestDistance == -1.0 || distance < shortestDistance) {
                    closestAnchorPoint = point;
                    shortestDistance = distance;
                }
            }
            final AnchorPoint oldAnchorPoint = this.getAnchorPoint(element);
            if (oldAnchorPoint == closestAnchorPoint) {
                return;
            }
            final float targetX = this.getActualX(element);
            final float targetY = this.getActualY(element);
            float x2 = targetX - closestAnchorPoint.getX(maxX);
            float y2 = targetY - closestAnchorPoint.getY(maxY);
            this.anchorPoints.put(element, closestAnchorPoint);
            final float width = buttonLocation.getBoxXTwo() - buttonLocation.getBoxXOne();
            final float oldXOffset = TommyHUD.getInstance().getUtils().getXOffsetFromAnchorPoint(width, buttonLocation.getScale(), oldAnchorPoint);
            final float newXOffset = TommyHUD.getInstance().getUtils().getXOffsetFromAnchorPoint(width, buttonLocation.getScale(), closestAnchorPoint);
            x2 += newXOffset - oldXOffset;
            final float height = buttonLocation.getBoxYTwo() - buttonLocation.getBoxYOne();
            final float oldYOffset = TommyHUD.getInstance().getUtils().getYOffsetFromAnchorPoint(height, buttonLocation.getScale(), oldAnchorPoint);
            final float newYOffset = TommyHUD.getInstance().getUtils().getYOffsetFromAnchorPoint(height, buttonLocation.getScale(), closestAnchorPoint);
            y2 += newYOffset - oldYOffset;
            this.setCoords(element, x2, y2);
        }
        
        public void setGuiScale(final Element element, final float scale) {
            this.scales.put(element, scale);
        }
        
        public void setChroma(final Element element, final boolean enabled) {
            if (enabled) {
                this.chromaFeatures.add(element);
            }
            else {
                this.chromaFeatures.remove(element);
            }
        }
        
        public Set<Element> getChromaFeatures() {
            return this.chromaFeatures;
        }
        
        public Color getColor(final Element element) {
            if (this.chromaFeatures.contains(element)) {
                return Color.WHITE;
            }
            final ColorCode defaultColor = element.getGuiDefault().getColor();
            return new Color(this.colors.getOrDefault(element, (defaultColor != null) ? defaultColor.getRGB() : ColorCode.RED.getRGB()));
        }
        
        public void setColor(final Element element, final int color) {
            this.colors.put(element, color);
        }
        
        public Set<CrazyMode> getEnabledCrazyModes() {
            return this.enabledCrazyModes;
        }
        
        public boolean isCrazyModeEnabled(final CrazyMode crazyMode) {
            return this.enabledCrazyModes.contains(crazyMode);
        }
    }
}
