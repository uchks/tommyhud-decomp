package codes.biscuit.tommyhud.asm.utils;

import codes.biscuit.tommyhud.asm.utils.TransformerClass;
import java.util.Map;
import org.objectweb.asm.tree.FieldInsnNode;

public enum TransformerField {
    width("width", "width", "l", "I"),
    height("height", "height", "m", "I"),
    buttonList("buttonList", "buttonList", "n", "Ljava/util/List;"),
    id("id", "id", "k", "I"),
    lowerChestInventory("lowerChestInventory", "lowerChestInventory", "w", TransformerClass.IInventory.getName()),
    guiLeft("guiLeft", "guiLeft", "i", "I"),
    guiTop("guiTop", "guiTop", "r", "I"),
    fontRendererObj("fontRendererObj", "fontRendererObj", "q", TransformerClass.FontRenderer.getName()),
    inventorySlots("inventorySlots", "inventorySlots", "h", TransformerClass.Container.getName()),
    theSlot("theSlot", "theSlot", "u", TransformerClass.Slot.getName()),
    xSize("xSize", "xSize", "f", "I"),
    ySize("ySize", "ySize", "g", "I"),
    mcResourceManager("mcResourceManager", "mcResourceManager", "ay", TransformerClass.IReloadableResourceManager.getName()),
    red("red", "red", "m", "F"),
    green("green", "green", "b", "F"),
    blue("blue", "blue", "n", "F"),
    alpha("alpha", "alpha", "p", "F"),
    hurtTime("hurtTime", "hurtTime ", "au", "I"),
    currentItem("currentItem", "currentItem", "c", "I"),
    NULL((String)null, (String)null, (String)null, (String)null);

    private String name;
    private String type;

    private TransformerField(String deobfName, String seargeName, String notchName18, String type) {
        this.type = type;
        if (TommyHUDTransformer.isDeobfuscated()) {
            this.name = deobfName;
        } else if (TommyHUDTransformer.isUsingNotchMappings()) {
            this.name = notchName18;
        } else {
            this.name = seargeName;
        }

    }

    public FieldInsnNode getField(TransformerClass currentClass) {
        return new FieldInsnNode(180, currentClass.getNameRaw(), this.name, this.type);
    }

    public FieldInsnNode putField(TransformerClass currentClass) {
        return new FieldInsnNode(181, currentClass.getNameRaw(), this.name, this.type);
    }

    public boolean matches(FieldInsnNode fieldInsnNode) {
        return this.name.equals(fieldInsnNode.name) && this.type.equals(fieldInsnNode.desc);
    }
}