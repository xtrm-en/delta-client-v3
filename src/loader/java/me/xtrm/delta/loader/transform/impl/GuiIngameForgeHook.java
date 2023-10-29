package me.xtrm.delta.loader.transform.impl;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import me.xtrm.delta.loader.api.event.events.lifecycle.EventMCPostLoading;
import me.xtrm.delta.loader.utils.ASMHelper;
import me.xtrm.xeon.loader.api.transform.ITransformer;

public class GuiIngameForgeHook implements ITransformer {

	@Override
	public void transform(ClassNode classNode, String clazz) {
		for(MethodNode methodNode : classNode.methods) {
			if(methodNode.name.equalsIgnoreCase("<init>")) {
				String className = EventMCPostLoading.class.getName().replace('.', '/');
				InsnList list = new InsnList();
				list.add(new TypeInsnNode(NEW, className));
				list.add(new InsnNode(DUP));
				list.add(new MethodInsnNode(INVOKESPECIAL, className, "<init>", "()V", false));
				list.add(new MethodInsnNode(INVOKEVIRTUAL, className, "call", "()V", false));
				methodNode.instructions.insertBefore(ASMHelper.getLastReturn(methodNode), list);
			}
		}
	}

	@Override
	public boolean isTarget(String className, boolean isSubclass) {
		return className.equalsIgnoreCase("net.minecraftforge.client.GuiIngameForge") && !isSubclass;
	}
}
