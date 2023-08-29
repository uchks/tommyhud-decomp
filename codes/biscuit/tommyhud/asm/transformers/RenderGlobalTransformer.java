package codes.biscuit.tommyhud.asm.transformers;

import codes.biscuit.tommyhud.asm.*;
import codes.biscuit.tommyhud.asm.utils.*;
import org.objectweb.asm.tree.*;
import java.util.*;

public class RenderGlobalTransformer implements ITransformer
{
    private boolean foundEntitiesProfiler;
    
    public RenderGlobalTransformer() {
        this.foundEntitiesProfiler = false;
    }
    
    @Override
    public String[] getClassName() {
        return new String[] { TransformerClass.RenderGlobal.getTransformerName() };
    }
    
    @Override
    public void transform(final ClassNode classNode, final String name) {
        for (final MethodNode methodNode : classNode.methods) {
            if (TransformerMethod.renderEntities.matches(methodNode)) {
                final Iterator<AbstractInsnNode> iterator = (Iterator<AbstractInsnNode>)methodNode.instructions.iterator();
                while (iterator.hasNext()) {
                    final AbstractInsnNode abstractNode = iterator.next();
                    if (abstractNode instanceof LdcInsnNode) {
                        final LdcInsnNode ldcInsnNode = (LdcInsnNode)abstractNode;
                        if (!ldcInsnNode.cst.equals("entities")) {
                            continue;
                        }
                        this.foundEntitiesProfiler = true;
                    }
                    else {
                        if (!this.foundEntitiesProfiler || !(abstractNode instanceof MethodInsnNode)) {
                            continue;
                        }
                        final MethodInsnNode methodInsnNode = (MethodInsnNode)abstractNode;
                        if (TransformerMethod.renderEntitySimple.matches(methodInsnNode)) {
                            methodNode.instructions.insert(abstractNode, (AbstractInsnNode)new MethodInsnNode(184, "codes/biscuit/tommyhud/asm/hooks/RenderGlobalHook", "renderEntitySimple", "(" + TransformerClass.RenderManager.getName() + TransformerClass.Entity.getName() + "F)Z", false));
                            iterator.remove();
                            break;
                        }
                        continue;
                    }
                }
            }
        }
    }
}
