package codes.biscuit.tommyhud.asm;

import codes.biscuit.tommyhud.asm.transformers.RenderGlobalTransformer;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class TommyHUDTransformer implements IClassTransformer {
    private static boolean DEOBFUSCATED = false;
    private static boolean USING_NOTCH_MAPPINGS;
    private Logger logger = LogManager.getLogger("SkyblockAddons Transformer");
    private final Multimap<String, ITransformer> transformerMap = ArrayListMultimap.create();

    public TommyHUDTransformer() {
        this.registerTransformer(new RenderGlobalTransformer());
    }

    private void registerTransformer(ITransformer transformer) {
        for(String cls : transformer.getClassName()) {
            this.transformerMap.put(cls, transformer);
        }

    }

    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            Collection<ITransformer> transformers = this.transformerMap.get(transformedName);
            if (transformers.isEmpty()) {
                return bytes;
            } else {
                this.logger.info("Found {} transformers for {}", new Object[]{transformers.size(), transformedName});
                ClassReader reader = new ClassReader(bytes);
                ClassNode node = new ClassNode();
                reader.accept(node, 8);
                MutableInt classWriterFlags = new MutableInt(3);
                transformers.forEach((transformer) -> {
                    this.logger.info("Applying transformer {} on {}...", new Object[]{transformer.getClass().getName(), transformedName});
                    transformer.transform(node, transformedName);
                });
                ClassWriter writer = new ClassWriter(classWriterFlags.getValue());

                try {
                    node.accept(writer);
                } catch (Throwable var10) {
                    this.logger.error("Exception when transforming " + transformedName + " : " + var10.getClass().getSimpleName());
                    var10.printStackTrace();
                    this.outputBytecode(transformedName, writer);
                    return bytes;
                }

                this.outputBytecode(transformedName, writer);
                return writer.toByteArray();
            }
        }
    }

    private void outputBytecode(String transformedName, ClassWriter writer) {
        if (isDeobfuscated()) {
            try {
                File bytecodeDirectory = new File("bytecode");
                File bytecodeOutput = new File(bytecodeDirectory, transformedName + ".class");
                if (!bytecodeDirectory.exists()) {
                    return;
                }

                if (!bytecodeOutput.exists()) {
                    bytecodeOutput.createNewFile();
                }

                FileOutputStream os = new FileOutputStream(bytecodeOutput);
                os.write(writer.toByteArray());
                os.close();
            } catch (IOException var6) {
                var6.printStackTrace();
            }
        }

    }

    public static boolean isDeobfuscated() {
        return DEOBFUSCATED;
    }

    public static boolean isUsingNotchMappings() {
        return USING_NOTCH_MAPPINGS;
    }

    static {
        boolean foundLaunchClass = false;

        try {
            Class<?> launch = Class.forName("net.minecraft.launchwrapper.Launch");
            Field blackboardField = launch.getField("blackboard");
            Map<String, Object> blackboard = (Map)blackboardField.get((Object)null);
            DEOBFUSCATED = blackboard.get("fml.deobfuscatedEnvironment");
            foundLaunchClass = true;
        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException var4) {
        }

        USING_NOTCH_MAPPINGS = !DEOBFUSCATED;
    }
}