package codes.biscuit.tommyhud.asm;

import java.util.Map;
import org.objectweb.asm.tree.ClassNode;

public interface ITransformer {
    String[] getClassName();

    void transform(ClassNode var1, String var2);

    default boolean nameMatches(String method, String... names) {
        for(String name : names) {
            if (method.equals(name)) {
                return true;
            }
        }

        return false;
    }
}