package codes.biscuit.tommyhud.asm.utils;

import codes.biscuit.tommyhud.asm.*;
import org.objectweb.asm.tree.*;

public enum TransformerMethod
{
    updateScreen("updateScreen", "func_73876_c", "e", "()V"), 
    onGuiClosed("onGuiClosed", "func_146281_b", "m", "()V"), 
    drawScreen("drawScreen", "func_73863_a", "a", "(IIF)V"), 
    initGui("initGui", "func_73866_w", "b", "()V"), 
    keyTyped("keyTyped", "func_73869_a", "a", "(CI)V", true), 
    handleMouseClick("handleMouseClick", "func_146984_a", "a", "(Lnet/minecraft/inventory/Slot;III)V", "(" + TransformerClass.Slot.getName() + "III)V"), 
    mouseClicked("mouseClicked", "func_73864_a", "a", "(III)V", true), 
    drawGradientRect("drawGradientRect", "func_73733_a", "a", "(IIIIII)V"), 
    drawSlot("drawSlot", "func_146977_a", "a", "(Lnet/minecraft/inventory/Slot;)V", "(" + TransformerClass.Slot.getName() + ")V"), 
    checkHotbarKeys("checkHotbarKeys", "func_146983_a", "b", "(I)Z"), 
    actionPerformed("actionPerformed", "func_146284_a", "a", "(Lnet/minecraft/client/gui/GuiButton;)V", "(" + TransformerClass.GuiButton.getName() + ")V"), 
    handleMouseInput("handleMouseInput", "func_178039_p", "p", "()V", true), 
    mouseClickMove("mouseClickMove", "func_146273_a", "a", "(IIIJ)V"), 
    mouseReleased("mouseReleased", "func_146286_b", "b", "(III)V"), 
    drawGuiContainerBackgroundLayer("drawGuiContainerBackgroundLayer", "func_146976_a", "a", "(FII)V"), 
    renderToolTip("renderToolTip", "func_146285_a", "a", "(Lnet/minecraft/item/ItemStack;II)V", "(" + TransformerClass.ItemStack.getName() + "II)V"), 
    handleComponentClick("handleComponentClick", "func_175276_a", "a", "(Lnet/minecraft/util/IChatComponent;)Z", "(" + TransformerClass.IChatComponent.getName() + ")Z"), 
    renderItemAndEffectIntoGUI("renderItemAndEffectIntoGUI", "func_180450_b", "b", "(Lnet/minecraft/item/ItemStack;II)V", "(" + TransformerClass.ItemStack.getName() + "II)V"), 
    drawGuiContainerForegroundLayer("drawGuiContainerForegroundLayer", "func_146979_b", "b", "(II)V"), 
    renderItemModelTransform("renderItemModelTransform", "func_175040_a", "a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V", "(" + TransformerClass.ItemStack.getName() + TransformerClass.IBakedModel.getName() + TransformerClass.ItemCameraTransforms$TransformType.getName() + ")V"), 
    renderItem("renderItem", "func_180454_a", "a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V", "(" + TransformerClass.ItemStack.getName() + TransformerClass.IBakedModel.getName() + ")V"), 
    renderModel_RenderItem("renderModel", "func_175036_a", "a", "(Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V", "(" + TransformerClass.IBakedModel.getName() + TransformerClass.ItemStack.getName() + ")V"), 
    color("color", "func_179131_c", "c", "(FFFF)V"), 
    drawString("drawString", "func_78276_b", "a", "(Ljava/lang/String;III)I"), 
    getUnformattedText("getUnformattedText", "func_150260_c", "c", "()Ljava/lang/String;"), 
    isItemDamaged("isItemDamaged", "func_77951_h", "g", "()Z"), 
    getNormalizedVolume("getNormalizedVolume", "func_148594_a", "a", "(Lnet/minecraft/client/audio/ISound;Lnet/minecraft/client/audio/SoundPoolEntry;Lnet/minecraft/client/audio/SoundCategory;)F", "(" + TransformerClass.ISound.getName() + TransformerClass.SoundPoolEntry.getName() + TransformerClass.SoundCategory.getName() + ")F"), 
    bindTexture("bindTexture", "func_147499_a", "a", "(Lnet/minecraft/util/ResourceLocation;)V", "(" + TransformerClass.ResourceLocation.getName() + ")V"), 
    renderAll("renderAll", "func_78231_a", "a", "()V"), 
    isPotionActive("isPotionActive", "func_70644_a", "a", "(Lnet/minecraft/potion/Potion;)Z", "(" + TransformerClass.Potion.getName() + ")Z"), 
    dropOneItem("dropOneItem", "func_71040_bB", "a", "(Z)Lnet/minecraft/entity/item/EntityItem;", "(Z)" + TransformerClass.EntityItem.getName()), 
    setPlayerSPHealth("setPlayerSPHealth", "func_71150_b", "n", "(F)V"), 
    getMouseOver("getMouseOver", "func_78473_a", "a", "(F)V"), 
    getNightVisionBrightness("getNightVisionBrightness", "func_180438_a", "a", "(" + TransformerClass.EntityLivingBase.getName() + "F)F", "(Lnet/minecraft/entity/EntityLivingBase;F)F"), 
    printChatMessageWithOptionalDeletion("printChatMessageWithOptionalDeletion", "func_146234_a", "a", "(Lnet/minecraft/util/IChatComponent;I)V", "(" + TransformerClass.IChatComponent.getName() + "I)V"), 
    refreshResources("refreshResources", "func_110436_a", "e", "()V"), 
    rightClickMouse("rightClickMouse", "func_147121_ag", "ax", "()V"), 
    isIntegratedServerRunning("isIntegratedServerRunning", "func_71387_A", "E", "()Z"), 
    runTick("runTick", "func_71407_l", "s", "()V"), 
    clickMouse("clickMouse", "func_147116_af", "aw", "()V"), 
    sendClickBlockToController("sendClickBlockToController", "func_147115_a", "b", "(Z)V"), 
    ungrabMouseCursor("ungrabMouseCursor", "func_74373_b", "b", "()V"), 
    handleSetSlot("handleSetSlot", "func_147266_a", "a", "(Lnet/minecraft/network/play/server/S2FPacketSetSlot;)V", "(" + TransformerClass.S2FPacketSetSlot.getName() + ")V"), 
    handleWindowItems("handleWindowItems", "func_147241_a", "a", "(Lnet/minecraft/network/play/server/S30PacketWindowItems;)V", "(" + TransformerClass.S30PacketWindowItems.getName() + ")V"), 
    clickBlock("clickBlock", "func_180511_b", "b", "(Lnet/minecraft/util/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", "(" + TransformerClass.BlockPos.getName() + TransformerClass.EnumFacing.getName() + ")Z"), 
    onPlayerDestroyBlock("onPlayerDestroyBlock", "func_178888_a", "a", "(Lnet/minecraft/util/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", "(" + TransformerClass.BlockPos.getName() + TransformerClass.EnumFacing.getName() + ")Z"), 
    windowClick("windowClick", "func_78753_a", "a", "(IIIILnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;", "(IIII" + TransformerClass.EntityPlayer.getName() + ")" + TransformerClass.ItemStack.getName()), 
    rotateCorpse("rotateCorpse", "func_77043_a", "a", "(Lnet/minecraft/entity/EntityLivingBase;FFF)V", "(" + TransformerClass.EntityLivingBase.getName() + "FFF)V"), 
    isWearing("isWearing", "func_175148_a", "a", "(Lnet/minecraft/entity/player/EnumPlayerModelParts;)Z", "(" + TransformerClass.EnumPlayerModelParts.getName() + ")Z"), 
    renderModel_RendererLivingEntity("renderModel", "func_77036_a", "a", "(Lnet/minecraft/entity/EntityLivingBase;FFFFFF)V", "(" + TransformerClass.EntityLivingBase.getName() + "FFFFFF)V"), 
    shouldRender("shouldRender", "func_178635_a", "a", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;DDD)Z", "(" + TransformerClass.Entity.getName() + TransformerClass.ICamera.getName() + "DDD)Z"), 
    playSound("playSound", "func_148611_c", "c", "(Lnet/minecraft/client/audio/ISound;)V", "(" + TransformerClass.ISound.getName() + ")V"), 
    renderTileEntityAt("renderTileEntityAt", "func_180535_a", "a", "(Lnet/minecraft/tileentity/TileEntityEnderChest;DDDFI)V", "(" + TransformerClass.TileEntityEnderChest.getName() + "DDDFI)V"), 
    renderChar("renderChar", "func_181559_a", "a", "(CZ)F"), 
    handleStatusUpdate("handleStatusUpdate", "func_70103_a", "a", "(B)V"), 
    init("<init>", "<init>", "<init>", "()V"), 
    changeCurrentItem("changeCurrentItem", "func_70453_c", "d", "(I)V"), 
    getEntityTexture_RenderEnderman("getEntityTexture", "func_110775_a", "a", "(Lnet/minecraft/entity/monster/EntityEnderman;)Lnet/minecraft/util/ResourceLocation;", "(" + TransformerClass.EntityEnderman.getName() + ")" + TransformerClass.ResourceLocation.getName()), 
    render("render", "func_78088_a", "a", "(Lnet/minecraft/entity/Entity;FFFFFF)V", "(" + TransformerClass.Entity.getName() + "FFFFFF)V"), 
    renderEntities("renderEntities", "func_180446_a", "a", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;F)V", "(" + TransformerClass.Entity.getName() + TransformerClass.ICamera.getName() + "F)V"), 
    renderEntitySimple("renderEntitySimple", "func_147937_a", "a", "(Lnet/minecraft/entity/Entity;F)Z", "(" + TransformerClass.Entity.getName() + "F)Z"), 
    getRenderViewEntity("getRenderViewEntity", "func_175606_aa", "ac", "()Lnet/minecraft/entity/Entity;", "()" + TransformerClass.Entity.getName()), 
    renderName("renderName", "func_177067_a", "a", "(Lnet/minecraft/entity/Entity;DDD)V", "(" + TransformerClass.Entity.getName() + "DDD)V"), 
    NULL((String)null, (String)null, (String)null, (String)null, false);
    
    private String name;
    private String description;
    private String[] exceptions;
    
    private TransformerMethod(final String deobfMethod, final String seargeMethod, final String notchMethod18, final String seargeDescription) {
        this(deobfMethod, seargeMethod, notchMethod18, seargeDescription, seargeDescription, false);
    }
    
    private TransformerMethod(final String deobfMethod, final String seargeMethod, final String notchMethod18, final String seargeDescription, final String notchDescription) {
        this(deobfMethod, seargeMethod, notchMethod18, seargeDescription, notchDescription, false);
    }
    
    private TransformerMethod(final String deobfMethod, final String seargeMethod, final String notchMethod18, final String seargeDescription, final boolean ioException) {
        this(deobfMethod, seargeMethod, notchMethod18, seargeDescription, seargeDescription, ioException);
    }
    
    private TransformerMethod(final String deobfMethod, final String seargeMethod, final String notchMethod18, final String seargeDescription, final String notchDescription, final boolean ioException) {
        this.exceptions = null;
        if (TommyHUDTransformer.isDeobfuscated()) {
            this.name = deobfMethod;
            this.description = seargeDescription;
        }
        else if (TommyHUDTransformer.isUsingNotchMappings()) {
            this.name = notchMethod18;
            this.description = notchDescription;
        }
        else {
            this.name = seargeMethod;
            this.description = seargeDescription;
        }
        if (ioException) {
            this.exceptions = new String[] { "java/io/IOException" };
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public MethodNode createMethodNode() {
        return new MethodNode(1, this.name, this.description, (String)null, this.exceptions);
    }
    
    public boolean matches(final MethodInsnNode methodInsnNode) {
        return this.name.equals(methodInsnNode.name) && this.description.equals(methodInsnNode.desc);
    }
    
    public boolean matches(final MethodNode methodNode) {
        return this.name.equals(methodNode.name) && (this.description.equals(methodNode.desc) || this == TransformerMethod.init);
    }
}
