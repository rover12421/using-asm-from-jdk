package plus.rover.shaka.blog;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.util.Printer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by rover12421 on 3/23/20.
 */
public class SimpleAsm {
    /**
     * 打印所有的方法调用信息
     * @param classBuffer
     */
    public static void printAllMethodCall(InputStream classBuffer) throws IOException {
        ClassReader cr = new ClassReader(classBuffer);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        List<MethodNode> methodNodes = cn.methods;
        for (MethodNode methodNode : methodNodes) {
            AbstractInsnNode node = methodNode.instructions.getFirst();
            while (node != null) {
                if (node instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) node;
                    System.out.println(
                            String.format("[%s][%s] %s : %s.%s%s ", cn.name, methodNode.name,
                                    Printer.OPCODES[methodInsnNode.getOpcode()],
                                    methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc)
                    );
                }
                node = node.getNext();
            }
        }
    }
    public static void main(String[] args) {
        String simpleAsmClassResName = SimpleAsm.class.getName().replace(".", "/") + ".class";
        System.out.println("simpleAsmClassResName : " + simpleAsmClassResName);
        ClassLoader classLoader = SimpleAsm.class.getClassLoader();
        try (
                InputStream is = classLoader.getResourceAsStream(simpleAsmClassResName);
        ) {
            printAllMethodCall(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
