package codes.biscuit.tommyhud.asm.transformers;

import codes.biscuit.tommyhud.asm.utils.TransformerClass;
import codes.biscuit.tommyhud.asm.utils.TransformerMethod;
import java.util.Iterator;
import java.util.Map;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class RenderGlobalTransformer implements ITransformer {
    private boolean foundEntitiesProfiler = false;

    public String[] getClassName() {
        return new String[]{TransformerClass.RenderGlobal.getTransformerName()};
    }

    public void transform(ClassNode classNode, String name) {
        for(MethodNode methodNode : classNode.methods) {
            if (TransformerMethod.renderEntities.matches(methodNode)) {
                Iterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();

                while(iterator.hasNext()) {
                    AbstractInsnNode abstractNode = (AbstractInsnNode)iterator.next();
                    if (abstractNode instanceof LdcInsnNode) {
                        LdcInsnNode ldcInsnNode = (LdcInsnNode)abstractNode;
                        if (ldcInsnNode.cst.equals("entities")) {
                            this.foundEntitiesProfiler = true;
                        }
                    } else if (this.foundEntitiesProfiler && abstractNode instanceof MethodInsnNode) {
                        MethodInsnNode methodInsnNode = (MethodInsnNode)abstractNode;
                        if (TransformerMethod.renderEntitySimple.matches(methodInsnNode)) {
                            methodNode.instructions.insert(abstractNode, new MethodInsnNode(184, "codes/biscuit/tommyhud/asm/hooks/RenderGlobalHook", "renderEntitySimple", "(" + TransformerClass.RenderManager.getName() + TransformerClass.Entity.getName() + "F)Z", false));
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
        }

    }
}