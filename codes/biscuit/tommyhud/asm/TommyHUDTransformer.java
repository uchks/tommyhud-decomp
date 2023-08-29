package codes.biscuit.tommyhud.asm;

import net.minecraft.launchwrapper.*;
import org.apache.logging.log4j.*;
import com.google.common.collect.*;
import codes.biscuit.tommyhud.asm.transformers.*;
import org.objectweb.asm.tree.*;
import org.apache.commons.lang3.mutable.*;
import org.objectweb.asm.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public class TommyHUDTransformer implements IClassTransformer
{
    private static boolean DEOBFUSCATED;
    private static boolean USING_NOTCH_MAPPINGS;
    private Logger logger;
    private final Multimap<String, ITransformer> transformerMap;
    
    public TommyHUDTransformer() {
        this.logger = LogManager.getLogger("SkyblockAddons Transformer");
        this.transformerMap = (Multimap<String, ITransformer>)ArrayListMultimap.create();
        this.registerTransformer(new RenderGlobalTransformer());
    }
    
    private void registerTransformer(final ITransformer transformer) {
        for (final String cls : transformer.getClassName()) {
            this.transformerMap.put((Object)cls, (Object)transformer);
        }
    }
    
    public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        final Collection<ITransformer> transformers = (Collection<ITransformer>)this.transformerMap.get((Object)transformedName);
        if (transformers.isEmpty()) {
            return bytes;
        }
        this.logger.info("Found {} transformers for {}", new Object[] { transformers.size(), transformedName });
        final ClassReader reader = new ClassReader(bytes);
        final ClassNode node = new ClassNode();
        reader.accept((ClassVisitor)node, 8);
        final MutableInt classWriterFlags = new MutableInt(3);
        final ClassNode classNode;
        transformers.forEach(transformer -> {
            this.logger.info("Applying transformer {} on {}...", new Object[] { transformer.getClass().getName(), transformedName });
            transformer.transform(classNode, transformedName);
            return;
        });
        final ClassWriter writer = new ClassWriter((int)classWriterFlags.getValue());
        try {
            node.accept((ClassVisitor)writer);
        }
        catch (Throwable t) {
            this.logger.error("Exception when transforming " + transformedName + " : " + t.getClass().getSimpleName());
            t.printStackTrace();
            this.outputBytecode(transformedName, writer);
            return bytes;
        }
        this.outputBytecode(transformedName, writer);
        return writer.toByteArray();
    }
    
    private void outputBytecode(final String transformedName, final ClassWriter writer) {
        if (isDeobfuscated()) {
            try {
                final File bytecodeDirectory = new File("bytecode");
                final File bytecodeOutput = new File(bytecodeDirectory, transformedName + ".class");
                if (!bytecodeDirectory.exists()) {
                    return;
                }
                if (!bytecodeOutput.exists()) {
                    bytecodeOutput.createNewFile();
                }
                final FileOutputStream os = new FileOutputStream(bytecodeOutput);
                os.write(writer.toByteArray());
                os.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static boolean isDeobfuscated() {
        return TommyHUDTransformer.DEOBFUSCATED;
    }
    
    public static boolean isUsingNotchMappings() {
        return TommyHUDTransformer.USING_NOTCH_MAPPINGS;
    }
    
    static {
        TommyHUDTransformer.DEOBFUSCATED = false;
        boolean foundLaunchClass = false;
        try {
            final Class<?> launch = Class.forName("net.minecraft.launchwrapper.Launch");
            final Field blackboardField = launch.getField("blackboard");
            final Map<String, Object> blackboard = (Map<String, Object>)blackboardField.get(null);
            TommyHUDTransformer.DEOBFUSCATED = blackboard.get("fml.deobfuscatedEnvironment");
            foundLaunchClass = true;
        }
        catch (ClassNotFoundException ex) {}
        catch (NoSuchFieldException ex2) {}
        catch (IllegalAccessException ex3) {}
        TommyHUDTransformer.USING_NOTCH_MAPPINGS = !TommyHUDTransformer.DEOBFUSCATED;
    }
}
