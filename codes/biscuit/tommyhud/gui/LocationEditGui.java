package codes.biscuit.tommyhud.gui;

import codes.biscuit.tommyhud.*;
import codes.biscuit.tommyhud.core.*;
import net.minecraft.client.*;
import codes.biscuit.tommyhud.gui.buttons.*;
import java.awt.*;
import codes.biscuit.tommyhud.util.*;
import net.minecraft.client.gui.*;
import org.lwjgl.input.*;
import codes.biscuit.tommyhud.config.*;
import java.io.*;
import codes.biscuit.tommyhud.misc.*;
import java.util.*;
import com.google.common.collect.*;

public class LocationEditGui extends GuiScreen
{
    private EditMode editMode;
    private boolean showColorIcons;
    private boolean enableSnapping;
    private TommyHUD main;
    private Element dragging;
    private boolean resizing;
    private ButtonResize.Corner resizingCorner;
    private float xOffset;
    private float yOffset;
    private Map<Element, ButtonLocation> buttonLocations;
    private boolean closing;
    private static final int SNAPPING_RADIUS = 120;
    private static final int SNAP_PULL = 1;
    
    public LocationEditGui() {
        this.editMode = EditMode.RESCALE;
        this.showColorIcons = true;
        this.enableSnapping = true;
        this.main = TommyHUD.getInstance();
        this.buttonLocations = new EnumMap<Element, ButtonLocation>(Element.class);
        this.closing = false;
    }
    
    public void func_73866_w_() {
        for (final Element feature : Element.values()) {
            final ButtonLocation buttonLocation = new ButtonLocation(feature);
            this.field_146292_n.add(buttonLocation);
            this.buttonLocations.put(feature, buttonLocation);
        }
        if (this.editMode == EditMode.RESCALE) {
            this.addResizeButtonsToAllElements();
        }
        this.addColorWheelsToAllElements();
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        final int boxHeight = 20;
        final int numButtons = 3;
        int y = scaledResolution.func_78328_b() / 2;
        if (numButtons % 2 == 0) {
            y -= (int)(Math.round(numButtons / 2.0f * (boxHeight + 5)) - 2.5);
        }
        else {
            y -= Math.round((numButtons - 1) / 2.0f * (boxHeight + 5)) + 10;
        }
        String text = "Reset Locations";
        int boxWidth = this.field_146297_k.field_71466_p.func_78256_a(text) + 10;
        int x = scaledResolution.func_78326_a() / 2 - boxWidth / 2;
        this.field_146292_n.add(new ButtonRegular(x, y, boxWidth, boxHeight, text, () -> this.main.getConfigManager().getConfigValues().resetAllElements()));
        text = "Rescale Elements";
        boxWidth = this.field_146297_k.field_71466_p.func_78256_a(text) + 10;
        x = scaledResolution.func_78326_a() / 2 - boxWidth / 2;
        y += boxHeight + 5;
        this.field_146292_n.add(new ButtonRegular(x, y, boxWidth, boxHeight, text, () -> {
            if (this.editMode != EditMode.RESCALE) {
                this.editMode = EditMode.RESCALE;
                this.addResizeButtonsToAllElements();
            }
            else {
                this.editMode = null;
                this.clearAllResizeButtons();
            }
            return;
        }));
        text = "Show Color Icons";
        boxWidth = this.field_146297_k.field_71466_p.func_78256_a(text) + 10;
        x = scaledResolution.func_78326_a() / 2 - boxWidth / 2;
        y += boxHeight + 5;
        this.field_146292_n.add(new ButtonRegular(x, y, boxWidth, boxHeight, text, () -> {
            if (this.showColorIcons) {
                this.showColorIcons = false;
                this.clearAllColorWheelButtons();
            }
            else {
                this.showColorIcons = true;
                this.addColorWheelsToAllElements();
            }
        }));
    }
    
    private void clearAllResizeButtons() {
        this.field_146292_n.removeIf(button -> button instanceof ButtonResize);
    }
    
    private void clearAllColorWheelButtons() {
        this.field_146292_n.removeIf(button -> button instanceof ButtonColorWheel);
    }
    
    private void addResizeButtonsToAllElements() {
        this.clearAllResizeButtons();
        for (final Element feature : Element.values()) {
            if (!this.main.getConfigManager().getConfigValues().isDisabled(feature)) {
                this.addResizeCorners(feature);
            }
        }
    }
    
    private void addColorWheelsToAllElements() {
        for (final ButtonLocation buttonLocation : this.buttonLocations.values()) {
            final Element feature = buttonLocation.getElement();
            if (feature.getGuiDefault() != null) {
                if (feature.getGuiDefault().getColor() == null) {
                    continue;
                }
                final AnchorPoint anchorPoint = this.main.getConfigManager().getConfigValues().getAnchorPoint(feature);
                final float y = buttonLocation.getBoxYOne() + (buttonLocation.getBoxYTwo() - buttonLocation.getBoxYOne()) / 2.0f - ButtonColorWheel.getSize() / 2.0f;
                float x;
                if (anchorPoint == AnchorPoint.TOP_LEFT || anchorPoint == AnchorPoint.BOTTOM_LEFT) {
                    x = buttonLocation.getBoxXOne() + (buttonLocation.getBoxXTwo() - buttonLocation.getBoxXOne());
                }
                else {
                    x = buttonLocation.getBoxXOne() - ButtonColorWheel.getSize();
                }
                this.field_146292_n.add(new ButtonColorWheel((float)Math.round(x), (float)Math.round(y), feature));
            }
        }
    }
    
    private void addResizeCorners(final Element feature) {
        this.field_146292_n.removeIf(button -> button instanceof ButtonResize && button.getElement() == feature);
        final ButtonLocation buttonLocation = this.buttonLocations.get(feature);
        if (buttonLocation == null) {
            return;
        }
        final float boxXOne = buttonLocation.getBoxXOne();
        final float boxXTwo = buttonLocation.getBoxXTwo();
        final float boxYOne = buttonLocation.getBoxYOne();
        final float boxYTwo = buttonLocation.getBoxYTwo();
        this.field_146292_n.add(new ButtonResize(boxXOne, boxYOne, feature, ButtonResize.Corner.TOP_LEFT));
        this.field_146292_n.add(new ButtonResize(boxXTwo, boxYOne, feature, ButtonResize.Corner.TOP_RIGHT));
        this.field_146292_n.add(new ButtonResize(boxXOne, boxYTwo, feature, ButtonResize.Corner.BOTTOM_LEFT));
        this.field_146292_n.add(new ButtonResize(boxXTwo, boxYTwo, feature, ButtonResize.Corner.BOTTOM_RIGHT));
    }
    
    private void recalculateResizeButtons() {
        for (final GuiButton button : this.field_146292_n) {
            if (button instanceof ButtonResize) {
                final ButtonResize buttonResize = (ButtonResize)button;
                final ButtonResize.Corner corner = buttonResize.getCorner();
                final Element feature = buttonResize.getElement();
                final ButtonLocation buttonLocation = this.buttonLocations.get(feature);
                if (buttonLocation == null) {
                    continue;
                }
                final float boxXOne = buttonLocation.getBoxXOne();
                final float boxXTwo = buttonLocation.getBoxXTwo();
                final float boxYOne = buttonLocation.getBoxYOne();
                final float boxYTwo = buttonLocation.getBoxYTwo();
                if (corner == ButtonResize.Corner.TOP_LEFT) {
                    buttonResize.x = boxXOne;
                    buttonResize.y = boxYOne;
                }
                else if (corner == ButtonResize.Corner.TOP_RIGHT) {
                    buttonResize.x = boxXTwo;
                    buttonResize.y = boxYOne;
                }
                else if (corner == ButtonResize.Corner.BOTTOM_LEFT) {
                    buttonResize.x = boxXOne;
                    buttonResize.y = boxYTwo;
                }
                else {
                    if (corner != ButtonResize.Corner.BOTTOM_RIGHT) {
                        continue;
                    }
                    buttonResize.x = boxXTwo;
                    buttonResize.y = boxYTwo;
                }
            }
        }
    }
    
    private void recalculateColorWheels() {
        for (final GuiButton button : this.field_146292_n) {
            if (button instanceof ButtonColorWheel) {
                final ButtonColorWheel buttonColorWheel = (ButtonColorWheel)button;
                final Element feature = buttonColorWheel.getElement();
                final ButtonLocation buttonLocation = this.buttonLocations.get(feature);
                if (buttonLocation == null) {
                    continue;
                }
                final AnchorPoint anchorPoint = this.main.getConfigManager().getConfigValues().getAnchorPoint(feature);
                final float y = buttonLocation.getBoxYOne() + (buttonLocation.getBoxYTwo() - buttonLocation.getBoxYOne()) / 2.0f - ButtonColorWheel.getSize() / 2.0f;
                float x;
                if (anchorPoint == AnchorPoint.TOP_LEFT || anchorPoint == AnchorPoint.BOTTOM_LEFT) {
                    x = buttonLocation.getBoxXTwo() + 2.0f;
                }
                else {
                    x = buttonLocation.getBoxXOne() - ButtonColorWheel.getSize() - 2.0f;
                }
                buttonColorWheel.x = x;
                buttonColorWheel.y = y;
            }
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        final Snap[] snaps = null;
        this.onMouseMove(mouseX, mouseY, snaps);
        if (this.editMode == EditMode.RESCALE) {
            this.recalculateResizeButtons();
        }
        this.recalculateColorWheels();
        final int startColor = new Color(0, 0, 0, 128).getRGB();
        final int endColor = new Color(0, 0, 0, 128).getRGB();
        this.func_73733_a(0, 0, this.field_146294_l, this.field_146295_m, startColor, endColor);
        for (final AnchorPoint anchorPoint : AnchorPoint.values()) {
            final ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
            final int x = anchorPoint.getX(sr.func_78326_a());
            final int y = anchorPoint.getY(sr.func_78328_b());
            int color = ColorCode.RED.getColor(127).getRGB();
            final Element lastHovered = ButtonLocation.getLastHoveredElement();
            if (lastHovered != null && this.main.getConfigManager().getConfigValues().getAnchorPoint(lastHovered) == anchorPoint) {
                color = ColorCode.YELLOW.getColor(127).getRGB();
            }
            Gui.func_73734_a(x - 4, y - 4, x + 4, y + 4, color);
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
        if (snaps != null) {
            for (final Snap snap : snaps) {
                if (snap != null) {
                    float left = snap.getRectangle().get(Edge.LEFT);
                    float top = snap.getRectangle().get(Edge.TOP);
                    float right = snap.getRectangle().get(Edge.RIGHT);
                    float bottom = snap.getRectangle().get(Edge.BOTTOM);
                    if (snap.getWidth() < 0.5) {
                        final float averageX = (left + right) / 2.0f;
                        left = averageX - 0.25f;
                        right = averageX + 0.25f;
                    }
                    if (snap.getHeight() < 0.5) {
                        final float averageY = (top + bottom) / 2.0f;
                        top = averageY - 0.25f;
                        bottom = averageY + 0.25f;
                    }
                    if (right - left == 0.5 || bottom - top == 0.5) {
                        this.main.getUtils().drawRect(left, top, right, bottom, -16711936);
                    }
                    else {
                        this.main.getUtils().drawRect(left, top, right, bottom, -65536);
                    }
                }
            }
        }
    }
    
    public Snap[] checkSnapping() {
        if (!this.enableSnapping) {
            return null;
        }
        if (this.dragging == null) {
            return null;
        }
        final ButtonLocation thisButton = this.buttonLocations.get(this.dragging);
        if (thisButton == null) {
            return null;
        }
        Snap horizontalSnap = null;
        Snap verticalSnap = null;
        for (final Map.Entry<Element, ButtonLocation> buttonLocationEntry : this.buttonLocations.entrySet()) {
            final ButtonLocation otherButton = buttonLocationEntry.getValue();
            if (otherButton == thisButton) {
                continue;
            }
            for (final Edge otherEdge : Edge.getHorizontalEdges()) {
                for (final Edge thisEdge : Edge.getHorizontalEdges()) {
                    final float deltaX = otherEdge.getCoordinate(otherButton) - thisEdge.getCoordinate(thisButton);
                    if (Math.abs(deltaX) <= 1.0f) {
                        final float deltaY = Edge.TOP.getCoordinate(otherButton) - Edge.TOP.getCoordinate(thisButton);
                        float topY;
                        float bottomY;
                        if (deltaY > 0.0f) {
                            topY = Edge.BOTTOM.getCoordinate(thisButton);
                            bottomY = Edge.TOP.getCoordinate(otherButton);
                        }
                        else {
                            topY = Edge.BOTTOM.getCoordinate(otherButton);
                            bottomY = Edge.TOP.getCoordinate(thisButton);
                        }
                        final float snapX = otherEdge.getCoordinate(otherButton);
                        final Snap thisSnap = new Snap(otherEdge.getCoordinate(otherButton), topY, thisEdge.getCoordinate(thisButton), bottomY, thisEdge, otherEdge, snapX);
                        if (thisSnap.getHeight() >= 120.0f || (horizontalSnap != null && thisSnap.getHeight() >= horizontalSnap.getHeight())) {
                            continue;
                        }
                        horizontalSnap = thisSnap;
                    }
                }
            }
            for (final Edge otherEdge : Edge.getVerticalEdges()) {
                for (final Edge thisEdge : Edge.getVerticalEdges()) {
                    final float deltaY2 = otherEdge.getCoordinate(otherButton) - thisEdge.getCoordinate(thisButton);
                    if (Math.abs(deltaY2) <= 1.0f) {
                        final float deltaX2 = Edge.LEFT.getCoordinate(otherButton) - Edge.LEFT.getCoordinate(thisButton);
                        float leftX;
                        float rightX;
                        if (deltaX2 > 0.0f) {
                            leftX = Edge.RIGHT.getCoordinate(thisButton);
                            rightX = Edge.LEFT.getCoordinate(otherButton);
                        }
                        else {
                            leftX = Edge.RIGHT.getCoordinate(otherButton);
                            rightX = Edge.LEFT.getCoordinate(thisButton);
                        }
                        final float snapY = otherEdge.getCoordinate(otherButton);
                        final Snap thisSnap = new Snap(leftX, otherEdge.getCoordinate(otherButton), rightX, thisEdge.getCoordinate(thisButton), thisEdge, otherEdge, snapY);
                        if (thisSnap.getWidth() >= 120.0f || (verticalSnap != null && thisSnap.getWidth() >= verticalSnap.getWidth())) {
                            continue;
                        }
                        verticalSnap = thisSnap;
                    }
                }
            }
        }
        return new Snap[] { horizontalSnap, verticalSnap };
    }
    
    protected void func_146284_a(final GuiButton abstractButton) {
        if (abstractButton instanceof ButtonLocation) {
            final ButtonLocation buttonLocation = (ButtonLocation)abstractButton;
            this.dragging = buttonLocation.getElement();
            final ScaledResolution sr = new ScaledResolution(this.field_146297_k);
            final float minecraftScale = (float)sr.func_78325_e();
            final float floatMouseX = Mouse.getX() / minecraftScale;
            final float floatMouseY = (this.field_146297_k.field_71440_d - Mouse.getY()) / minecraftScale;
            this.xOffset = floatMouseX - this.main.getConfigManager().getConfigValues().getActualX(buttonLocation.getElement());
            this.yOffset = floatMouseY - this.main.getConfigManager().getConfigValues().getActualY(buttonLocation.getElement());
        }
        else if (abstractButton instanceof ButtonResize) {
            final ButtonResize buttonResize = (ButtonResize)abstractButton;
            this.dragging = buttonResize.getElement();
            this.resizing = true;
            final ScaledResolution sr = new ScaledResolution(this.field_146297_k);
            final float minecraftScale = (float)sr.func_78325_e();
            final float floatMouseX = Mouse.getX() / minecraftScale;
            final float floatMouseY = (this.field_146297_k.field_71440_d - Mouse.getY()) / minecraftScale;
            final float scale = this.main.getConfigManager().getConfigValues().getGuiScale(buttonResize.getElement());
            if (this.editMode == EditMode.RESCALE) {
                this.xOffset = (floatMouseX - buttonResize.getX() * scale) / scale;
                this.yOffset = (floatMouseY - buttonResize.getY() * scale) / scale;
            }
            else {
                this.xOffset = floatMouseX;
                this.yOffset = floatMouseY;
            }
            this.resizingCorner = buttonResize.getCorner();
        }
        else if (abstractButton instanceof ButtonColorWheel) {
            final ButtonColorWheel buttonColorWheel = (ButtonColorWheel)abstractButton;
            this.closing = true;
            this.field_146297_k.func_147108_a((GuiScreen)new ColorSelectionGui(buttonColorWheel.getElement()));
        }
    }
    
    protected void onMouseMove(final int mouseX, final int mouseY, final Snap[] snaps) {
        final ScaledResolution sr = new ScaledResolution(this.field_146297_k);
        final float minecraftScale = (float)sr.func_78325_e();
        float floatMouseX = Mouse.getX() / minecraftScale;
        float floatMouseY = (this.field_146297_k.field_71440_d - Mouse.getY()) / minecraftScale;
        if (this.resizing) {
            if (this.editMode == EditMode.RESCALE) {
                final ButtonLocation buttonLocation = this.buttonLocations.get(this.dragging);
                if (buttonLocation == null) {
                    return;
                }
                final float boxXOne = buttonLocation.getBoxXOne();
                final float boxXTwo = buttonLocation.getBoxXTwo();
                final float boxYOne = buttonLocation.getBoxYOne();
                final float boxYTwo = buttonLocation.getBoxYTwo();
                final float scale = buttonLocation.getScale();
                float scaledX1 = boxXOne * buttonLocation.getScale();
                float scaledY1 = boxYOne * buttonLocation.getScale();
                final float scaledX2 = boxXTwo * buttonLocation.getScale();
                final float scaledY2 = boxYTwo * buttonLocation.getScale();
                final float scaledWidth = scaledX2 - scaledX1;
                final float scaledHeight = scaledY2 - scaledY1;
                final float width = boxXTwo - boxXOne;
                final float height = boxYTwo - boxYOne;
                final AnchorPoint anchorPoint = this.main.getConfigManager().getConfigValues().getAnchorPoint(this.dragging);
                if (anchorPoint == AnchorPoint.TOP_LEFT || anchorPoint == AnchorPoint.BOTTOM_LEFT) {
                    scaledX1 -= scaledWidth / 2.0f;
                    floatMouseX -= width / 2.0f;
                }
                else if (anchorPoint == AnchorPoint.TOP_RIGHT || anchorPoint == AnchorPoint.BOTTOM_RIGHT) {
                    scaledX1 += scaledWidth / 2.0f;
                    floatMouseX += width / 2.0f;
                }
                if (anchorPoint == AnchorPoint.TOP_LEFT || anchorPoint == AnchorPoint.TOP_RIGHT) {
                    scaledY1 -= scaledHeight / 2.0f;
                    floatMouseY -= height / 2.0f;
                }
                else if (anchorPoint == AnchorPoint.BOTTOM_LEFT || anchorPoint == AnchorPoint.BOTTOM_RIGHT || anchorPoint == AnchorPoint.BOTTOM_MIDDLE) {
                    scaledY1 += scaledHeight / 2.0f;
                    floatMouseY += height / 2.0f;
                }
                final float middleX = scaledX1 + scaledWidth / 2.0f;
                final float middleY = scaledY1 + scaledHeight / 2.0f;
                float xOffset = floatMouseX - this.xOffset * scale - middleX;
                float yOffset = floatMouseY - this.yOffset * scale - middleY;
                if (this.resizingCorner == ButtonResize.Corner.TOP_LEFT) {
                    xOffset *= -1.0f;
                    yOffset *= -1.0f;
                }
                else if (this.resizingCorner == ButtonResize.Corner.TOP_RIGHT) {
                    yOffset *= -1.0f;
                }
                else if (this.resizingCorner == ButtonResize.Corner.BOTTOM_LEFT) {
                    xOffset *= -1.0f;
                }
                final float newWidth = xOffset * 2.0f;
                final float newHeight = yOffset * 2.0f;
                final float scaleX = newWidth / width;
                final float scaleY = newHeight / height;
                final float newScale = Math.max(scaleX, scaleY);
                final float normalizedScale = ConfigManager.normalizeValueNoStep(newScale);
                this.main.getConfigManager().getConfigValues().setGuiScale(this.dragging, normalizedScale);
                buttonLocation.func_146112_a(this.field_146297_k, mouseX, mouseY);
                this.recalculateResizeButtons();
            }
        }
        else if (this.dragging != null) {
            final ButtonLocation buttonLocation = this.buttonLocations.get(this.dragging);
            if (buttonLocation == null) {
                return;
            }
            float x = floatMouseX - this.main.getConfigManager().getConfigValues().getAnchorPoint(this.dragging).getX(sr.func_78326_a());
            float y = floatMouseY - this.main.getConfigManager().getConfigValues().getAnchorPoint(this.dragging).getY(sr.func_78328_b());
            x -= this.xOffset;
            y -= this.yOffset;
            final AnchorPoint oldAnchorPoint = this.main.getConfigManager().getConfigValues().getAnchorPoint(this.dragging);
            this.main.getConfigManager().getConfigValues().setCoords(this.dragging, x, y);
            this.main.getConfigManager().getConfigValues().setClosestAnchorPoint(this.dragging, buttonLocation);
            final AnchorPoint newAnchorPoint = this.main.getConfigManager().getConfigValues().getAnchorPoint(this.dragging);
            final float width2 = buttonLocation.getBoxXTwo() - buttonLocation.getBoxXOne();
            final float oldXOffset = TommyHUD.getInstance().getUtils().getXOffsetFromAnchorPoint(width2, buttonLocation.getScale(), oldAnchorPoint);
            final float newXOffset = TommyHUD.getInstance().getUtils().getXOffsetFromAnchorPoint(width2, buttonLocation.getScale(), newAnchorPoint);
            this.xOffset -= newXOffset - oldXOffset;
            final float height2 = buttonLocation.getBoxYTwo() - buttonLocation.getBoxYOne();
            final float oldYOffset = TommyHUD.getInstance().getUtils().getYOffsetFromAnchorPoint(height2, buttonLocation.getScale(), oldAnchorPoint);
            final float newYOffset = TommyHUD.getInstance().getUtils().getYOffsetFromAnchorPoint(height2, buttonLocation.getScale(), newAnchorPoint);
            this.yOffset -= newYOffset - oldYOffset;
        }
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        super.func_73869_a(typedChar, keyCode);
        final Element hoveredElement = ButtonLocation.getLastHoveredElement();
        if (hoveredElement != null) {
            final ButtonLocation buttonLocation = this.buttonLocations.get(hoveredElement);
            if (buttonLocation == null) {
                return;
            }
            int xOffset = 0;
            int yOffset = 0;
            if (keyCode == 203) {
                --xOffset;
            }
            else if (keyCode == 200) {
                --yOffset;
            }
            else if (keyCode == 205) {
                ++xOffset;
            }
            else if (keyCode == 208) {
                ++yOffset;
            }
            if (keyCode == 30) {
                xOffset -= 20;
            }
            else if (keyCode == 17) {
                yOffset -= 20;
            }
            else if (keyCode == 32) {
                xOffset += 20;
            }
            else if (keyCode == 31) {
                yOffset += 20;
            }
            final float minecraftScale = (float)new ScaledResolution(Minecraft.func_71410_x()).func_78325_e();
            this.main.getConfigManager().getConfigValues().setCoords(hoveredElement, this.main.getConfigManager().getConfigValues().getRelativeCoords(hoveredElement).getX() + xOffset * (1.0f / minecraftScale), this.main.getConfigManager().getConfigValues().getRelativeCoords(hoveredElement).getY() + yOffset * (1.0f / minecraftScale));
            this.main.getConfigManager().getConfigValues().setClosestAnchorPoint(hoveredElement, buttonLocation);
        }
    }
    
    protected void func_146286_b(final int mouseX, final int mouseY, final int state) {
        super.func_146286_b(mouseX, mouseY, state);
        this.dragging = null;
        this.resizing = false;
    }
    
    public void func_146281_b() {
        this.main.getConfigManager().saveConfig();
        if (!this.closing) {
            this.main.getListener().setGuiToOpen(GUIType.MAIN);
        }
    }
    
    enum Edge
    {
        LEFT, 
        TOP, 
        RIGHT, 
        BOTTOM, 
        HORIZONTAL_MIDDLE, 
        VERTICAL_MIDDLE;
        
        private static final Set<Edge> verticalEdges;
        private static final Set<Edge> horizontalEdges;
        
        public float getCoordinate(final ButtonLocation button) {
            switch (this) {
                case LEFT: {
                    return button.getBoxXOne() * button.getScale();
                }
                case TOP: {
                    return button.getBoxYOne() * button.getScale();
                }
                case RIGHT: {
                    return button.getBoxXTwo() * button.getScale();
                }
                case BOTTOM: {
                    return button.getBoxYTwo() * button.getScale();
                }
                case HORIZONTAL_MIDDLE: {
                    return Edge.TOP.getCoordinate(button) + (Edge.BOTTOM.getCoordinate(button) - Edge.TOP.getCoordinate(button)) / 2.0f;
                }
                case VERTICAL_MIDDLE: {
                    return Edge.LEFT.getCoordinate(button) + (Edge.RIGHT.getCoordinate(button) - Edge.LEFT.getCoordinate(button)) / 2.0f;
                }
                default: {
                    return 0.0f;
                }
            }
        }
        
        public static Set<Edge> getVerticalEdges() {
            return Edge.verticalEdges;
        }
        
        public static Set<Edge> getHorizontalEdges() {
            return Edge.horizontalEdges;
        }
        
        static {
            verticalEdges = Sets.newHashSet((Object[])new Edge[] { Edge.TOP, Edge.BOTTOM, Edge.HORIZONTAL_MIDDLE });
            horizontalEdges = Sets.newHashSet((Object[])new Edge[] { Edge.LEFT, Edge.RIGHT, Edge.VERTICAL_MIDDLE });
        }
    }
    
    static class Snap
    {
        private Edge thisSnapEdge;
        private Edge otherSnapEdge;
        private float snapValue;
        private Map<Edge, Float> rectangle;
        
        public Snap(final float left, final float top, final float right, final float bottom, final Edge thisSnapEdge, final Edge otherSnapEdge, final float snapValue) {
            (this.rectangle = new EnumMap<Edge, Float>(Edge.class)).put(Edge.LEFT, left);
            this.rectangle.put(Edge.TOP, top);
            this.rectangle.put(Edge.RIGHT, right);
            this.rectangle.put(Edge.BOTTOM, bottom);
            this.rectangle.put(Edge.HORIZONTAL_MIDDLE, top + this.getHeight() / 2.0f);
            this.rectangle.put(Edge.VERTICAL_MIDDLE, left + this.getWidth() / 2.0f);
            this.otherSnapEdge = otherSnapEdge;
            this.thisSnapEdge = thisSnapEdge;
            this.snapValue = snapValue;
        }
        
        public float getHeight() {
            return this.rectangle.get(Edge.BOTTOM) - this.rectangle.get(Edge.TOP);
        }
        
        public float getWidth() {
            return this.rectangle.get(Edge.RIGHT) - this.rectangle.get(Edge.LEFT);
        }
        
        public Edge getThisSnapEdge() {
            return this.thisSnapEdge;
        }
        
        public Edge getOtherSnapEdge() {
            return this.otherSnapEdge;
        }
        
        public float getSnapValue() {
            return this.snapValue;
        }
        
        public Map<Edge, Float> getRectangle() {
            return this.rectangle;
        }
    }
    
    private enum EditMode
    {
        RESCALE;
    }
}
