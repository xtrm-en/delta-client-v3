package me.xtrm.xeon.loader.classloading.transform;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import me.xtrm.xeon.loader.api.transform.ITransformer;

public class XeonPatcher implements ITransformer {

	private final Logger logger = LogManager.getLogger("XeonPatcher");
	
	@Override
	public boolean isTarget(String name, boolean isSubclass) {
		return name.equalsIgnoreCase("net.minecraftforge.gradle.GradleStartCommon$AccessTransformerTransformer") && isSubclass;
	}
	
	@Override
	public void transform(ClassNode classNode, String className) {
		switch(className) {
			case "net.minecraftforge.gradle.GradleStartCommon$AccessTransformerTransformer":
				for(MethodNode mn : classNode.methods) { // for fucks sake why forgeeeeeeeeeee
					if(mn.name.equalsIgnoreCase("doStuff")) {
						for(int i = 0; i < mn.instructions.size(); i++) {
							AbstractInsnNode abstractInsnNode = mn.instructions.get(i);
							if(abstractInsnNode.getOpcode() == INVOKEVIRTUAL) {
								MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;
								if(methodInsnNode.owner.equalsIgnoreCase("java/lang/Class")
										&& methodInsnNode.name.equalsIgnoreCase("getCanonicalName")) {
									methodInsnNode.name = "getName";
									this.logger.info("Patched {}", className + "." + mn.name + mn.desc);
								}
							}
						}
					}
				}
				break;
			case "net.minecraft.client.Minecraft":
				for(MethodNode mn : classNode.methods) {
					if(mn.name.equalsIgnoreCase("displayCrashReport") || mn.name.equalsIgnoreCase("func_71377_b") || mn.name.equalsIgnoreCase("c")) {
						if(mn.desc.equalsIgnoreCase("(Lnet/minecraft/crash/CrashReport;)V") || mn.desc.equalsIgnoreCase("(Lb;)V")) {
							InsnList list = new InsnList();
							list.add(new VarInsnNode(ALOAD, 0));
							list.add(new VarInsnNode(ALOAD, 1));
							list.add(new MethodInsnNode(INVOKESTATIC, "me/xtrm/xeon/loader/classloading/transform/XeonHandler", "handle", "(Lnet/minecraft/client/Minecraft;Lnet/minecraft/crash/CrashReport;)V", false));
							mn.instructions.insert(list);
						}
					}
				}
		}
	}
	
}
