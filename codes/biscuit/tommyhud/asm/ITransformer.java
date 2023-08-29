package codes.biscuit.tommyhud.asm;

import org.objectweb.asm.tree.*;

public interface ITransformer
{
    String[] getClassName();
    
    void transform(final ClassNode p0, final String p1);
    
    default boolean nameMatches(final String method, final String... names) {
        for (final String name : names) {
            if (method.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
