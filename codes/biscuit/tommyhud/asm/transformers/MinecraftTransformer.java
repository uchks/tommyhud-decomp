package codes.biscuit.tommyhud.asm.transformers;

import codes.biscuit.tommyhud.asm.*;
import codes.biscuit.tommyhud.asm.utils.*;
import org.objectweb.asm.tree.*;
import java.util.*;

public class MinecraftTransformer implements ITransformer
{
    @Override
    public String[] getClassName() {
        return new String[] { TransformerClass.Minecraft.getTransformerName() };
    }
    
    @Override
    public void transform(final ClassNode classNode, final String name) {
        for (final MethodNode methodNode : classNode.methods) {
            if (TransformerMethod.getRenderViewEntity.matches(methodNode)) {
                for (final AbstractInsnNode abstractNode : methodNode.instructions) {
                    if (abstractNode instanceof InsnNode && abstractNode.getOpcode() == 176) {
                        methodNode.instructions.insertBefore(abstractNode, (AbstractInsnNode)new MethodInsnNode(184, "codes/biscuit/tommyhud/asm/hooks/MinecraftHook", "getRenderViewEntity", "(" + TransformerClass.Entity.getName() + ")" + TransformerClass.Entity.getName(), false));
                    }
                }
            }
        }
    }
}
