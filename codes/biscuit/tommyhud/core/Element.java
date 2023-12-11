package codes.biscuit.tommyhud.core;

import codes.biscuit.tommyhud.core.DrawType;
import codes.biscuit.tommyhud.core.GuiDefault;
import codes.biscuit.tommyhud.util.ColorCode;
import java.util.Map;

public enum Element {
    CPS_LEFT("Left Click CPS Counter", new GuiDefault(ColorCode.AQUA)),
    CPS_RIGHT("Right Click CPS Counter", new GuiDefault(ColorCode.AQUA)),
    FPS("FPS Counter", new GuiDefault(ColorCode.RED)),
    CLOCK("Clock", new GuiDefault(ColorCode.YELLOW)),
    COORDINATES("Coordinates", new GuiDefault(ColorCode.GREEN)),
    CHUNK_COORDINATES("Chunk Coordinates", new GuiDefault(ColorCode.GREEN)),
    HORIZONTAL_SPEED("Horizontal Speed", new GuiDefault(ColorCode.WHITE)),
    DIRECTION_FACING("Direction Facing", new GuiDefault(ColorCode.LIGHT_PURPLE)),
    LIGHT_LEVEL("Light Level", new GuiDefault(ColorCode.YELLOW)),
    BIOME("Biome", new GuiDefault(ColorCode.GREEN)),
    MEMORY("Memory Usage", new GuiDefault(ColorCode.RED)),
    PING("Ping", new GuiDefault(ColorCode.GREEN)),
    ARROW_COUNT("Arrow Count", new GuiDefault(DrawType.ICON_TEXT, ColorCode.WHITE)),
    SUBSCRIBE_TO_TOMMYINNIT("Subscribe To TommyInnit", new GuiDefault(ColorCode.RED)),
    POKIMANE_SUBSCRIPTION_TIER("Pokimane Subscription Tier", new GuiDefault(ColorCode.LIGHT_PURPLE)),
    DATE("Date", new GuiDefault(ColorCode.YELLOW)),
    VERTICAL_SPEED("Vertical Speed", new GuiDefault(ColorCode.WHITE)),
    HORIZONTAL_ACCELERATION("Horizontal Acceleration", new GuiDefault(ColorCode.WHITE)),
    VERTICAL_ACCELERATION("Vertical Acceleration", new GuiDefault(ColorCode.WHITE)),
    YAW("Yaw (Horizontal Rotation)", new GuiDefault(ColorCode.GOLD)),
    PITCH("Pitch (Vertical Rotation)", new GuiDefault(ColorCode.GOLD)),
    PLAYERS("Players in Your Server", new GuiDefault(ColorCode.RED)),
    POTION_DISPLAY("Potion Display", new GuiDefault(DrawType.POTION, ColorCode.YELLOW)),
    ARMOR_DISPLAY("Armor Display", new GuiDefault(DrawType.ARMOR, ColorCode.AQUA)),
    KEYSTROKES("Keystrokes", new GuiDefault(DrawType.KEYSTROKES, ColorCode.WHITE));

    private String name;
    private GuiDefault guiDefault;

    private Element(String name, GuiDefault guiDefault) {
        this.name = name;
        this.guiDefault = guiDefault;
    }

    public String getName() {
        return this.name;
    }

    public GuiDefault getGuiDefault() {
        return this.guiDefault;
    }
}