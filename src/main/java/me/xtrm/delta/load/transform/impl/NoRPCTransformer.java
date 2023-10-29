package me.xtrm.delta.load.transform.impl;

import java.util.Iterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import me.xtrm.xeon.loader.api.transform.ITransformer;

public class NoRPCTransformer implements ITransformer {

	@Override
	public boolean isTarget(String className, boolean isSubclass) {
		return className.startsWith("fr.paladium.");
	}
	
	@Override
	public void transform(ClassNode classNode, String className) {
		for(MethodNode methodNode : classNode.methods) {
			Iterator<AbstractInsnNode> iter = methodNode.instructions.iterator();
			boolean yay = false;
			while(iter.hasNext()) {
				AbstractInsnNode abstractInsnNode = iter.next();
				if(abstractInsnNode instanceof MethodInsnNode) {
					MethodInsnNode methodInsn = (MethodInsnNode) abstractInsnNode;
					if(methodInsn.owner.equalsIgnoreCase("club/minnced/discord/rpc/DiscordRPC")){
						if(methodInsn.name.equalsIgnoreCase("Discord_Initialize")) {
							if(methodInsn.desc.equalsIgnoreCase("(Ljava/lang/String;Lclub/minnced/discord/rpc/DiscordEventHandlers;ZLjava/lang/String;)V")) {
								yay = true;
								break;
							}
						}
					}
				}
			}
			if(yay) {
				InsnList list = new InsnList();
				list.add(new InsnNode(RETURN));
				methodNode.instructions = list;
			}
		}
	}

}
