package codes.biscuit.tommyhud.gui;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.config.AnchorPoint;
import codes.biscuit.tommyhud.config.ConfigManager;
import codes.biscuit.tommyhud.core.Element;
import codes.biscuit.tommyhud.gui.ColorSelectionGui;
import codes.biscuit.tommyhud.gui.LocationEditGui;
import codes.biscuit.tommyhud.gui.buttons.ButtonColorWheel;
import codes.biscuit.tommyhud.gui.buttons.ButtonLocation;
import codes.biscuit.tommyhud.gui.buttons.ButtonRegular;
import codes.biscuit.tommyhud.gui.buttons.ButtonResize;
import codes.biscuit.tommyhud.misc.GUIType;
import codes.biscuit.tommyhud.util.ColorCode;
import com.google.common.collect.Sets;
import java.awt.Color;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public class LocationEditGui extends GuiScreen {
    private LocationEditGui.EditMode editMode = LocationEditGui.EditMode.RESCALE;
    private boolean showColorIcons = true;
    private boolean enableSnapping = true;
    private TommyHUD main = TommyHUD.getInstance();
    private Element dragging;
    private boolean resizing;
    private ButtonResize.Corner resizingCorner;
    private float xOffset;
    private float yOffset;
    private Map<Element, ButtonLocation> buttonLocations = new EnumMap(Element.class);
    private boolean closing = false;
    private static final int SNAPPING_RADIUS = 120;
    private static final int SNAP_PULL = 1;

    public void initGui() {
        for(Element feature : Element.values()) {
            ButtonLocation buttonLocation = new ButtonLocation(feature);
            this.buttonList.add(buttonLocation);
            this.buttonLocations.put(feature, buttonLocation);
        }

        if (this.editMode == LocationEditGui.EditMode.RESCALE) {
            this.addResizeButtonsToAllElements();
        }

        this.addColorWheelsToAllElements();
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        int boxHeight = 20;
        int numButtons = 3;
        int y = scaledResolution.getScaledHeight() / 2;
        if (numButtons % 2 == 0) {
            y = (int)((double)y - ((double)Math.round((float)numButtons / 2.0F * (float)(boxHeight + 5)) - 2.5D));
        } else {
            y -= Math.round((float)(numButtons - 1) / 2.0F * (float)(boxHeight + 5)) + 10;
        }

        String text = "Reset Locations";
        int boxWidth = this.mc.fontRendererObj.getStringWidth(text) + 10;
        int x = scaledResolution.getScaledWidth() / 2 - boxWidth / 2;
        this.buttonList.add(new ButtonRegular((double)x, (double)y, boxWidth, boxHeight, text, () -> this.main.getConfigManager().getConfigValues().resetAllElements()));
        text = "Rescale Elements";
        boxWidth = this.mc.fontRendererObj.getStringWidth(text) + 10;
        x = scaledResolution.getScaledWidth() / 2 - boxWidth / 2;
        y += boxHeight + 5;
        this.buttonList.add(new ButtonRegular((double)x, (double)y, boxWidth, boxHeight, text, () -> {
            if (this.editMode != LocationEditGui.EditMode.RESCALE) {
                this.editMode = LocationEditGui.EditMode.RESCALE;
                this.addResizeButtonsToAllElements();
            } else {
                this.editMode = null;
                this.clearAllResizeButtons();
            }

        }));
        text = "Show Color Icons";
        boxWidth = this.mc.fontRendererObj.getStringWidth(text) + 10;
        x = scaledResolution.getScaledWidth() / 2 - boxWidth / 2;
        y += boxHeight + 5;
        this.buttonList.add(new ButtonRegular((double)x, (double)y, boxWidth, boxHeight, text, () -> {
            if (this.showColorIcons) {
                this.showColorIcons = false;
                this.clearAllColorWheelButtons();
            } else {
                this.showColorIcons = true;
                this.addColorWheelsToAllElements();
            }

        }));
    }

    private void clearAllResizeButtons() {
        this.buttonList.removeIf((button) -> button instanceof ButtonResize);
    }

    private void clearAllColorWheelButtons() {
        this.buttonList.removeIf((button) -> button instanceof ButtonColorWheel);
    }

    private void addResizeButtonsToAllElements() {
        this.clearAllResizeButtons();

        for(Element feature : Element.values()) {
            if (!this.main.getConfigManager().getConfigValues().isDisabled(feature)) {
                this.addResizeCorners(feature);
            }
        }

    }

    private void addColorWheelsToAllElements() {
        for(ButtonLocation buttonLocation : this.buttonLocations.values()) {
            Element feature = buttonLocation.getElement();
            if (feature.getGuiDefault() != null && feature.getGuiDefault().getColor() != null) {
                AnchorPoint anchorPoint = this.main.getConfigManager().getConfigValues().getAnchorPoint(feature);
                float y = buttonLocation.getBoxYOne() + (buttonLocation.getBoxYTwo() - buttonLocation.getBoxYOne()) / 2.0F - (float)ButtonColorWheel.getSize() / 2.0F;
                float x;
                if (anchorPoint != AnchorPoint.TOP_LEFT && anchorPoint != AnchorPoint.BOTTOM_LEFT) {
                    x = buttonLocation.getBoxXOne() - (float)ButtonColorWheel.getSize();
                } else {
                    x = buttonLocation.getBoxXOne() + (buttonLocation.getBoxXTwo() - buttonLocation.getBoxXOne());
                }

                this.buttonList.add(new ButtonColorWheel((float)Math.round(x), (float)Math.round(y), feature));
            }
        }

    }

    private void addResizeCorners(Element feature) {
        this.buttonList.removeIf((button) -> button instanceof ButtonResize && ((ButtonResize)button).getElement() == feature);
        ButtonLocation buttonLocation = (ButtonLocation)this.buttonLocations.get(feature);
        if (buttonLocation != null) {
            float boxXOne = buttonLocation.getBoxXOne();
            float boxXTwo = buttonLocation.getBoxXTwo();
            float boxYOne = buttonLocation.getBoxYOne();
            float boxYTwo = buttonLocation.getBoxYTwo();
            this.buttonList.add(new ButtonResize(boxXOne, boxYOne, feature, ButtonResize.Corner.TOP_LEFT));
            this.buttonList.add(new ButtonResize(boxXTwo, boxYOne, feature, ButtonResize.Corner.TOP_RIGHT));
            this.buttonList.add(new ButtonResize(boxXOne, boxYTwo, feature, ButtonResize.Corner.BOTTOM_LEFT));
            this.buttonList.add(new ButtonResize(boxXTwo, boxYTwo, feature, ButtonResize.Corner.BOTTOM_RIGHT));
        }
    }

    private void recalculateResizeButtons() {
        for(GuiButton button : this.buttonList) {
            if (button instanceof ButtonResize) {
                ButtonResize buttonResize = (ButtonResize)button;
                ButtonResize.Corner corner = buttonResize.getCorner();
                Element feature = buttonResize.getElement();
                ButtonLocation buttonLocation = (ButtonLocation)this.buttonLocations.get(feature);
                if (buttonLocation != null) {
                    float boxXOne = buttonLocation.getBoxXOne();
                    float boxXTwo = buttonLocation.getBoxXTwo();
                    float boxYOne = buttonLocation.getBoxYOne();
                    float boxYTwo = buttonLocation.getBoxYTwo();
                    if (corner == ButtonResize.Corner.TOP_LEFT) {
                        buttonResize.x = boxXOne;
                        buttonResize.y = boxYOne;
                    } else if (corner == ButtonResize.Corner.TOP_RIGHT) {
                        buttonResize.x = boxXTwo;
                        buttonResize.y = boxYOne;
                    } else if (corner == ButtonResize.Corner.BOTTOM_LEFT) {
                        buttonResize.x = boxXOne;
                        buttonResize.y = boxYTwo;
                    } else if (corner == ButtonResize.Corner.BOTTOM_RIGHT) {
                        buttonResize.x = boxXTwo;
                        buttonResize.y = boxYTwo;
                    }
                }
            }
        }

    }

    private void recalculateColorWheels() {
        for(GuiButton button : this.buttonList) {
            if (button instanceof ButtonColorWheel) {
                ButtonColorWheel buttonColorWheel = (ButtonColorWheel)button;
                Element feature = buttonColorWheel.getElement();
                ButtonLocation buttonLocation = (ButtonLocation)this.buttonLocations.get(feature);
                if (buttonLocation != null) {
                    AnchorPoint anchorPoint = this.main.getConfigManager().getConfigValues().getAnchorPoint(feature);
                    float y = buttonLocation.getBoxYOne() + (buttonLocation.getBoxYTwo() - buttonLocation.getBoxYOne()) / 2.0F - (float)ButtonColorWheel.getSize() / 2.0F;
                    float x;
                    if (anchorPoint != AnchorPoint.TOP_LEFT && anchorPoint != AnchorPoint.BOTTOM_LEFT) {
                        x = buttonLocation.getBoxXOne() - (float)ButtonColorWheel.getSize() - 2.0F;
                    } else {
                        x = buttonLocation.getBoxXTwo() + 2.0F;
                    }

                    buttonColorWheel.x = x;
                    buttonColorWheel.y = y;
                }
            }
        }

    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        LocationEditGui.Snap[] snaps = null;
        this.onMouseMove(mouseX, mouseY, snaps);
        if (this.editMode == LocationEditGui.EditMode.RESCALE) {
            this.recalculateResizeButtons();
        }

        this.recalculateColorWheels();
        int startColor = (new Color(0, 0, 0, 128)).getRGB();
        int endColor = (new Color(0, 0, 0, 128)).getRGB();
        this.func_73733_a(0, 0, this.width, this.height, startColor, endColor);

        for(AnchorPoint anchorPoint : AnchorPoint.values()) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            int x = anchorPoint.getX(sr.getScaledWidth());
            int y = anchorPoint.getY(sr.getScaledHeight());
            int color = ColorCode.RED.getColor(127).getRGB();
            Element lastHovered = ButtonLocation.getLastHoveredElement();
            if (lastHovered != null && this.main.getConfigManager().getConfigValues().getAnchorPoint(lastHovered) == anchorPoint) {
                color = ColorCode.YELLOW.getColor(127).getRGB();
            }

            Gui.drawRect(x - 4, y - 4, x + 4, y + 4, color);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
        if (snaps != null) {
            for(LocationEditGui.Snap snap : snaps) {
                if (snap != null) {
                    float left = snap.getRectangle().get(LocationEditGui.Edge.LEFT);
                    float top = snap.getRectangle().get(LocationEditGui.Edge.TOP);
                    float right = snap.getRectangle().get(LocationEditGui.Edge.RIGHT);
                    float bottom = snap.getRectangle().get(LocationEditGui.Edge.BOTTOM);
                    if ((double)snap.getWidth() < 0.5D) {
                        float averageX = (left + right) / 2.0F;
                        left = averageX - 0.25F;
                        right = averageX + 0.25F;
                    }

                    if ((double)snap.getHeight() < 0.5D) {
                        float averageY = (top + bottom) / 2.0F;
                        top = averageY - 0.25F;
                        bottom = averageY + 0.25F;
                    }

                    if ((double)(right - left) != 0.5D && (double)(bottom - top) != 0.5D) {
                        this.main.getUtils().drawRect((double)left, (double)top, (double)right, (double)bottom, -65536);
                    } else {
                        this.main.getUtils().drawRect((double)left, (double)top, (double)right, (double)bottom, -16711936);
                    }
                }
            }
        }

    }

    public LocationEditGui.Snap[] checkSnapping() {
        if (!this.enableSnapping) {
            return null;
        } else if (this.dragging == null) {
            return null;
        } else {
            ButtonLocation thisButton = (ButtonLocation)this.buttonLocations.get(this.dragging);
            if (thisButton == null) {
                return null;
            } else {
                LocationEditGui.Snap horizontalSnap = null;
                LocationEditGui.Snap verticalSnap = null;

                for(Entry<Element, ButtonLocation> buttonLocationEntry : this.buttonLocations.entrySet()) {
                    ButtonLocation otherButton = (ButtonLocation)buttonLocationEntry.getValue();
                    if (otherButton != thisButton) {
                        for(LocationEditGui.Edge otherEdge : LocationEditGui.Edge.getHorizontalEdges()) {
                            for(LocationEditGui.Edge thisEdge : LocationEditGui.Edge.getHorizontalEdges()) {
                                float deltaX = otherEdge.getCoordinate(otherButton) - thisEdge.getCoordinate(thisButton);
                                if (Math.abs(deltaX) <= 1.0F) {
                                    float deltaY = LocationEditGui.Edge.TOP.getCoordinate(otherButton) - LocationEditGui.Edge.TOP.getCoordinate(thisButton);
                                    float topY;
                                    float bottomY;
                                    if (deltaY > 0.0F) {
                                        topY = LocationEditGui.Edge.BOTTOM.getCoordinate(thisButton);
                                        bottomY = LocationEditGui.Edge.TOP.getCoordinate(otherButton);
                                    } else {
                                        topY = LocationEditGui.Edge.BOTTOM.getCoordinate(otherButton);
                                        bottomY = LocationEditGui.Edge.TOP.getCoordinate(thisButton);
                                    }

                                    float snapX = otherEdge.getCoordinate(otherButton);
                                    LocationEditGui.Snap thisSnap = new LocationEditGui.Snap(otherEdge.getCoordinate(otherButton), topY, thisEdge.getCoordinate(thisButton), bottomY, thisEdge, otherEdge, snapX);
                                    if (thisSnap.getHeight() < 120.0F && (horizontalSnap == null || thisSnap.getHeight() < horizontalSnap.getHeight())) {
                                        horizontalSnap = thisSnap;
                                    }
                                }
                            }
                        }

                        for(LocationEditGui.Edge otherEdge : LocationEditGui.Edge.getVerticalEdges()) {
                            for(LocationEditGui.Edge thisEdge : LocationEditGui.Edge.getVerticalEdges()) {
                                float deltaY = otherEdge.getCoordinate(otherButton) - thisEdge.getCoordinate(thisButton);
                                if (Math.abs(deltaY) <= 1.0F) {
                                    float deltaX = LocationEditGui.Edge.LEFT.getCoordinate(otherButton) - LocationEditGui.Edge.LEFT.getCoordinate(thisButton);
                                    float leftX;
                                    float rightX;
                                    if (deltaX > 0.0F) {
                                        leftX = LocationEditGui.Edge.RIGHT.getCoordinate(thisButton);
                                        rightX = LocationEditGui.Edge.LEFT.getCoordinate(otherButton);
                                    } else {
                                        leftX = LocationEditGui.Edge.RIGHT.getCoordinate(otherButton);
                                        rightX = LocationEditGui.Edge.LEFT.getCoordinate(thisButton);
                                    }

                                    float snapY = otherEdge.getCoordinate(otherButton);
                                    LocationEditGui.Snap thisSnap = new LocationEditGui.Snap(leftX, otherEdge.getCoordinate(otherButton), rightX, thisEdge.getCoordinate(thisButton), thisEdge, otherEdge, snapY);
                                    if (thisSnap.getWidth() < 120.0F && (verticalSnap == null || thisSnap.getWidth() < verticalSnap.getWidth())) {
                                        verticalSnap = thisSnap;
                                    }
                                }
                            }
                        }
                    }
                }

                return new LocationEditGui.Snap[]{horizontalSnap, verticalSnap};
            }
        }
    }

    protected void actionPerformed(GuiButton abstractButton) {
        if (abstractButton instanceof ButtonLocation) {
            ButtonLocation buttonLocation = (ButtonLocation)abstractButton;
            this.dragging = buttonLocation.getElement();
            ScaledResolution sr = new ScaledResolution(this.mc);
            float minecraftScale = (float)sr.getScaleFactor();
            float floatMouseX = (float)Mouse.getX() / minecraftScale;
            float floatMouseY = (float)(this.mc.displayHeight - Mouse.getY()) / minecraftScale;
            this.xOffset = floatMouseX - this.main.getConfigManager().getConfigValues().getActualX(buttonLocation.getElement());
            this.yOffset = floatMouseY - this.main.getConfigManager().getConfigValues().getActualY(buttonLocation.getElement());
        } else if (abstractButton instanceof ButtonResize) {
            ButtonResize buttonResize = (ButtonResize)abstractButton;
            this.dragging = buttonResize.getElement();
            this.resizing = true;
            ScaledResolution sr = new ScaledResolution(this.mc);
            float minecraftScale = (float)sr.getScaleFactor();
            float floatMouseX = (float)Mouse.getX() / minecraftScale;
            float floatMouseY = (float)(this.mc.displayHeight - Mouse.getY()) / minecraftScale;
            float scale = this.main.getConfigManager().getConfigValues().getGuiScale(buttonResize.getElement());
            if (this.editMode == LocationEditGui.EditMode.RESCALE) {
                this.xOffset = (floatMouseX - buttonResize.getX() * scale) / scale;
                this.yOffset = (floatMouseY - buttonResize.getY() * scale) / scale;
            } else {
                this.xOffset = floatMouseX;
                this.yOffset = floatMouseY;
            }

            this.resizingCorner = buttonResize.getCorner();
        } else if (abstractButton instanceof ButtonColorWheel) {
            ButtonColorWheel buttonColorWheel = (ButtonColorWheel)abstractButton;
            this.closing = true;
            this.mc.displayGuiScreen(new ColorSelectionGui(buttonColorWheel.getElement()));
        }

    }

    protected void onMouseMove(int mouseX, int mouseY, LocationEditGui.Snap[] snaps) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        float minecraftScale = (float)sr.getScaleFactor();
        float floatMouseX = (float)Mouse.getX() / minecraftScale;
        float floatMouseY = (float)(this.mc.displayHeight - Mouse.getY()) / minecraftScale;
        if (this.resizing) {
            if (this.editMode == LocationEditGui.EditMode.RESCALE) {
                ButtonLocation buttonLocation = (ButtonLocation)this.buttonLocations.get(this.dragging);
                if (buttonLocation == null) {
                    return;
                }

                float boxXOne = buttonLocation.getBoxXOne();
                float boxXTwo = buttonLocation.getBoxXTwo();
                float boxYOne = buttonLocation.getBoxYOne();
                float boxYTwo = buttonLocation.getBoxYTwo();
                float scale = buttonLocation.getScale();
                float scaledX1 = boxXOne * buttonLocation.getScale();
                float scaledY1 = boxYOne * buttonLocation.getScale();
                float scaledX2 = boxXTwo * buttonLocation.getScale();
                float scaledY2 = boxYTwo * buttonLocation.getScale();
                float scaledWidth = scaledX2 - scaledX1;
                float scaledHeight = scaledY2 - scaledY1;
                float width = boxXTwo - boxXOne;
                float height = boxYTwo - boxYOne;
                AnchorPoint anchorPoint = this.main.getConfigManager().getConfigValues().getAnchorPoint(this.dragging);
                if (anchorPoint != AnchorPoint.TOP_LEFT && anchorPoint != AnchorPoint.BOTTOM_LEFT) {
                    if (anchorPoint == AnchorPoint.TOP_RIGHT || anchorPoint == AnchorPoint.BOTTOM_RIGHT) {
                        scaledX1 += scaledWidth / 2.0F;
                        floatMouseX += width / 2.0F;
                    }
                } else {
                    scaledX1 -= scaledWidth / 2.0F;
                    floatMouseX -= width / 2.0F;
                }

                if (anchorPoint != AnchorPoint.TOP_LEFT && anchorPoint != AnchorPoint.TOP_RIGHT) {
                    if (anchorPoint == AnchorPoint.BOTTOM_LEFT || anchorPoint == AnchorPoint.BOTTOM_RIGHT || anchorPoint == AnchorPoint.BOTTOM_MIDDLE) {
                        scaledY1 += scaledHeight / 2.0F;
                        floatMouseY += height / 2.0F;
                    }
                } else {
                    scaledY1 -= scaledHeight / 2.0F;
                    floatMouseY -= height / 2.0F;
                }

                float middleX = scaledX1 + scaledWidth / 2.0F;
                float middleY = scaledY1 + scaledHeight / 2.0F;
                float xOffset = floatMouseX - this.xOffset * scale - middleX;
                float yOffset = floatMouseY - this.yOffset * scale - middleY;
                if (this.resizingCorner == ButtonResize.Corner.TOP_LEFT) {
                    xOffset *= -1.0F;
                    yOffset *= -1.0F;
                } else if (this.resizingCorner == ButtonResize.Corner.TOP_RIGHT) {
                    yOffset *= -1.0F;
                } else if (this.resizingCorner == ButtonResize.Corner.BOTTOM_LEFT) {
                    xOffset *= -1.0F;
                }

                float newWidth = xOffset * 2.0F;
                float newHeight = yOffset * 2.0F;
                float scaleX = newWidth / width;
                float scaleY = newHeight / height;
                float newScale = Math.max(scaleX, scaleY);
                float normalizedScale = ConfigManager.normalizeValueNoStep(newScale);
                this.main.getConfigManager().getConfigValues().setGuiScale(this.dragging, normalizedScale);
                buttonLocation.drawButton(this.mc, mouseX, mouseY);
                this.recalculateResizeButtons();
            }
        } else if (this.dragging != null) {
            ButtonLocation buttonLocation = (ButtonLocation)this.buttonLocations.get(this.dragging);
            if (buttonLocation == null) {
                return;
            }

            float x = floatMouseX - (float)this.main.getConfigManager().getConfigValues().getAnchorPoint(this.dragging).getX(sr.getScaledWidth());
            float y = floatMouseY - (float)this.main.getConfigManager().getConfigValues().getAnchorPoint(this.dragging).getY(sr.getScaledHeight());
            x -= this.xOffset;
            y -= this.yOffset;
            AnchorPoint oldAnchorPoint = this.main.getConfigManager().getConfigValues().getAnchorPoint(this.dragging);
            this.main.getConfigManager().getConfigValues().setCoords(this.dragging, x, y);
            this.main.getConfigManager().getConfigValues().setClosestAnchorPoint(this.dragging, buttonLocation);
            AnchorPoint newAnchorPoint = this.main.getConfigManager().getConfigValues().getAnchorPoint(this.dragging);
            float width = buttonLocation.getBoxXTwo() - buttonLocation.getBoxXOne();
            float oldXOffset = TommyHUD.getInstance().getUtils().getXOffsetFromAnchorPoint(width, buttonLocation.getScale(), oldAnchorPoint);
            float newXOffset = TommyHUD.getInstance().getUtils().getXOffsetFromAnchorPoint(width, buttonLocation.getScale(), newAnchorPoint);
            this.xOffset -= newXOffset - oldXOffset;
            float height = buttonLocation.getBoxYTwo() - buttonLocation.getBoxYOne();
            float oldYOffset = TommyHUD.getInstance().getUtils().getYOffsetFromAnchorPoint(height, buttonLocation.getScale(), oldAnchorPoint);
            float newYOffset = TommyHUD.getInstance().getUtils().getYOffsetFromAnchorPoint(height, buttonLocation.getScale(), newAnchorPoint);
            this.yOffset -= newYOffset - oldYOffset;
        }

    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        Element hoveredElement = ButtonLocation.getLastHoveredElement();
        if (hoveredElement != null) {
            ButtonLocation buttonLocation = (ButtonLocation)this.buttonLocations.get(hoveredElement);
            if (buttonLocation == null) {
                return;
            }

            int xOffset = 0;
            int yOffset = 0;
            if (keyCode == 203) {
                --xOffset;
            } else if (keyCode == 200) {
                --yOffset;
            } else if (keyCode == 205) {
                ++xOffset;
            } else if (keyCode == 208) {
                ++yOffset;
            }

            if (keyCode == 30) {
                xOffset -= 20;
            } else if (keyCode == 17) {
                yOffset -= 20;
            } else if (keyCode == 32) {
                xOffset += 20;
            } else if (keyCode == 31) {
                yOffset += 20;
            }

            float minecraftScale = (float)(new ScaledResolution(Minecraft.getMinecraft())).getScaleFactor();
            this.main.getConfigManager().getConfigValues().setCoords(hoveredElement, this.main.getConfigManager().getConfigValues().getRelativeCoords(hoveredElement).getX() + (float)xOffset * (1.0F / minecraftScale), this.main.getConfigManager().getConfigValues().getRelativeCoords(hoveredElement).getY() + (float)yOffset * (1.0F / minecraftScale));
            this.main.getConfigManager().getConfigValues().setClosestAnchorPoint(hoveredElement, buttonLocation);
        }

    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.dragging = null;
        this.resizing = false;
    }

    public void onGuiClosed() {
        this.main.getConfigManager().saveConfig();
        if (!this.closing) {
            this.main.getListener().setGuiToOpen(GUIType.MAIN);
        }

    }

    static enum Edge {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        HORIZONTAL_MIDDLE,
        VERTICAL_MIDDLE;

        private static final Set<LocationEditGui.Edge> verticalEdges = Sets.newHashSet(new LocationEditGui.Edge[]{TOP, BOTTOM, HORIZONTAL_MIDDLE});
        private static final Set<LocationEditGui.Edge> horizontalEdges = Sets.newHashSet(new LocationEditGui.Edge[]{LEFT, RIGHT, VERTICAL_MIDDLE});

        public float getCoordinate(ButtonLocation button) {
            switch(this) {
            case LEFT:
                return button.getBoxXOne() * button.getScale();
            case TOP:
                return button.getBoxYOne() * button.getScale();
            case RIGHT:
                return button.getBoxXTwo() * button.getScale();
            case BOTTOM:
                return button.getBoxYTwo() * button.getScale();
            case HORIZONTAL_MIDDLE:
                return TOP.getCoordinate(button) + (BOTTOM.getCoordinate(button) - TOP.getCoordinate(button)) / 2.0F;
            case VERTICAL_MIDDLE:
                return LEFT.getCoordinate(button) + (RIGHT.getCoordinate(button) - LEFT.getCoordinate(button)) / 2.0F;
            default:
                return 0.0F;
            }
        }

        public static Set<LocationEditGui.Edge> getVerticalEdges() {
            return verticalEdges;
        }

        public static Set<LocationEditGui.Edge> getHorizontalEdges() {
            return horizontalEdges;
        }
    }

    private static enum EditMode {
        RESCALE;
    }

    static class Snap {
        private LocationEditGui.Edge thisSnapEdge;
        private LocationEditGui.Edge otherSnapEdge;
        private float snapValue;
        private Map<LocationEditGui.Edge, Float> rectangle = new EnumMap(LocationEditGui.Edge.class);

        public Snap(float left, float top, float right, float bottom, LocationEditGui.Edge thisSnapEdge, LocationEditGui.Edge otherSnapEdge, float snapValue) {
            this.rectangle.put(LocationEditGui.Edge.LEFT, left);
            this.rectangle.put(LocationEditGui.Edge.TOP, top);
            this.rectangle.put(LocationEditGui.Edge.RIGHT, right);
            this.rectangle.put(LocationEditGui.Edge.BOTTOM, bottom);
            this.rectangle.put(LocationEditGui.Edge.HORIZONTAL_MIDDLE, top + this.getHeight() / 2.0F);
            this.rectangle.put(LocationEditGui.Edge.VERTICAL_MIDDLE, left + this.getWidth() / 2.0F);
            this.otherSnapEdge = otherSnapEdge;
            this.thisSnapEdge = thisSnapEdge;
            this.snapValue = snapValue;
        }

        public float getHeight() {
            return this.rectangle.get(LocationEditGui.Edge.BOTTOM) - this.rectangle.get(LocationEditGui.Edge.TOP);
        }

        public float getWidth() {
            return this.rectangle.get(LocationEditGui.Edge.RIGHT) - this.rectangle.get(LocationEditGui.Edge.LEFT);
        }

        public LocationEditGui.Edge getThisSnapEdge() {
            return this.thisSnapEdge;
        }

        public LocationEditGui.Edge getOtherSnapEdge() {
            return this.otherSnapEdge;
        }

        public float getSnapValue() {
            return this.snapValue;
        }

        public Map<LocationEditGui.Edge, Float> getRectangle() {
            return this.rectangle;
        }
    }
}
 