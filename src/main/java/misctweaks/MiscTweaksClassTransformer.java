package misctweaks;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.util.Arrays;

public class MiscTweaksClassTransformer implements IClassTransformer {
    private static final String[] classesBeingTransformed =
            {
                    "techguns.TGArmors"
            };

    @Override
    public byte[] transform(String name, String transformedName, byte[] classBeingTransformed) {
        boolean isObfuscated = !name.equals(transformedName);
        int index = Arrays.asList(classesBeingTransformed).indexOf(transformedName);
        return index != -1 ? transform(index, classBeingTransformed, isObfuscated) : classBeingTransformed;
    }

    private byte[] transform(int index, byte[] classBeingTransformed, boolean isObfuscated) {
        System.out.println("Transforming: " + classesBeingTransformed[index]);
        try {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(classBeingTransformed);
            classReader.accept(classNode, 0);

            switch (index) {
                case 0:
                    transformTGArmors(classNode, isObfuscated);
                    break;
            }

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classBeingTransformed;
    }

    private void transformTGArmors(ClassNode tgArmorsClass, boolean isObfuscated) {
        // Find init method
        MethodNode method = findMethodNodeOfClass(tgArmorsClass, "init", "()V");
        int lastMethod = 0;

        AbstractInsnNode[] instructions = method.instructions.toArray();

        for (int i = 0; i < instructions.length; i++)
        {
            AbstractInsnNode instruction = instructions[i];

            // Find all methods in init method
            if (instruction instanceof MethodInsnNode)
            {
                String name = ((MethodInsnNode) instruction).name;

                // Find these specific methods
                if (name.equals("setSpeedBoni") || name.equals("setWaterspeedBonus"))
                {
                    // Find their arguments
                    for (int j = lastMethod+1; j < i; j++) {
                        if (instructions[j] instanceof LdcInsnNode)
                        {
                            LdcInsnNode loadinsn = ((LdcInsnNode)instructions[j]);

                            // And quarter them!
                            float val = (float)loadinsn.cst;

                            method.instructions.insert(loadinsn, new LdcInsnNode(val/4));
                            method.instructions.remove(loadinsn);
                        }
                    }
                }

                lastMethod = i;
            }
        }
    }

    private MethodNode findMethodNodeOfClass(ClassNode classNode, String methodName, String methodDesc) {
        for (MethodNode method : classNode.methods) {
            if (method.name.equals(methodName) && method.desc.equals(methodDesc)) {
                return method;
            }
        }
        return null;
    }
}