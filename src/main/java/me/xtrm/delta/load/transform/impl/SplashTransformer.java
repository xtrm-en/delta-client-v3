package me.xtrm.delta.load.transform.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import me.xtrm.delta.client.utils.ASMUtils;
import me.xtrm.delta.wrapper.WrapManager;
import me.xtrm.xeon.loader.api.transform.ITransformer;

public class SplashTransformer implements ITransformer {
	
	@Override
	public boolean isTarget(String className, boolean isSubclass) {
		return "cpw.mods.fml.client.SplashProgress".equals(className);
	}
	
	@Override
	public void transform(ClassNode classNode, String className) {
		for(MethodNode methodNode : classNode.methods) {
			if(methodNode.name.equalsIgnoreCase("getString")) {
				InsnList list = new InsnList();
				list.add(new VarInsnNode(ALOAD, 0));
				list.add(new MethodInsnNode(INVOKESTATIC, WrapManager.INSTANCE.get("getString").getParent().getName().replace('.', '/'), WrapManager.INSTANCE.get("getString").getMethod().getName(), "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", false));
				methodNode.instructions.insertBefore(ASMUtils.getLastAReturn(methodNode), list);
			}
			if(methodNode.name.equalsIgnoreCase("start")) { // patch that fucking annoying crash report
				// GETSTATIC
				// ALOAD
				// INVOKEVIRTUAL
				// INVOKEVIRTUAL
				List<AbstractInsnNode> rem = new ArrayList<>();
				
				Iterator<AbstractInsnNode> absNodes = methodNode.instructions.iterator();
				while(absNodes.hasNext()) {
					AbstractInsnNode absInsn = absNodes.next();
					if(absInsn.getOpcode() == GETSTATIC) {
						AbstractInsnNode aloadNode = absInsn.getNext();
						if(aloadNode != null && aloadNode.getOpcode() == ALOAD) {
							AbstractInsnNode iv1Node = aloadNode.getNext();
							if(iv1Node != null && iv1Node.getOpcode() == INVOKEVIRTUAL) {
								AbstractInsnNode iv2Node = iv1Node.getNext();
								if(iv2Node != null && iv2Node.getOpcode() == INVOKEVIRTUAL) {
									rem.add(absInsn);
									rem.add(aloadNode);
									rem.add(iv1Node);
									rem.add(iv2Node);
									break;
								}
							}
						}
					}
				}
				
				rem.forEach(methodNode.instructions::remove);
			}
		}
	}

}
