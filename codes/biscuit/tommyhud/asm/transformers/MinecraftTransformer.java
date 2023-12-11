package codes.biscuit.tommyhud.asm.transformers;

import codes.biscuit.tommyhud.asm.utils.TransformerClass;
import codes.biscuit.tommyhud.asm.utils.TransformerMethod;
import java.util.Iterator;
import java.util.Map;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class MinecraftTransformer implements ITransformer {
    public String[] getClassName() {
        return new String[]{TransformerClass.Minecraft.getTransformerName()};
    }

    public void transform(ClassNode classNode, String name) {
        for(MethodNode methodNode : classNode.methods) {
            if (TransformerMethod.getRenderViewEntity.matches(methodNode)) {
                Iterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();

                while(iterator.hasNext()) {
                    AbstractInsnNode abstractNode = (AbstractInsnNode)iterator.next();
                    if (abstractNode instanceof InsnNode && abstractNode.getOpcode() == 176) {
                        methodNode.instructions.insertBefore(abstractNode, new MethodInsnNode(184, "codes/biscuit/tommyhud/asm/hooks/MinecraftHook", "getRenderViewEntity", "(" + TransformerClass.Entity.getName() + ")" + TransformerClass.Entity.getName(), false));
                    }
                }
            }
        }

    }
}