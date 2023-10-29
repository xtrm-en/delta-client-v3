package me.xtrm.delta.loader.transform.impl;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import me.xtrm.delta.loader.api.event.events.lifecycle.EventMCPreLoading;
import me.xtrm.delta.loader.api.event.events.lifecycle.EventMCShutdown;
import me.xtrm.xeon.loader.api.transform.ITransformer;

public class MinecraftHook implements ITransformer {

	@Override
	public void transform(ClassNode classNode, String clazz) {
		for (MethodNode methodNode : classNode.methods) {
			if (methodNode.name.equalsIgnoreCase("shutdownMinecraftApplet") || methodNode.name.equalsIgnoreCase("func_71405_e")) {
				String ecn = EventMCShutdown.class.getName().replace('.', '/');
				InsnList list = new InsnList();
				list.add(new TypeInsnNode(NEW, ecn));
				list.add(new InsnNode(DUP));
				list.add(new MethodInsnNode(INVOKESPECIAL, ecn, "<init>", "()V", false));
				list.add(new MethodInsnNode(INVOKEVIRTUAL, ecn, "call", "()V", false));
				methodNode.instructions.insert(list);
			}
			if (methodNode.name.equalsIgnoreCase("startGame") || methodNode.name.equalsIgnoreCase("func_71384_a")) {
				String ecn = EventMCPreLoading.class.getName().replace('.', '/');
				InsnList list = new InsnList();
				list.add(new TypeInsnNode(NEW, ecn));
				list.add(new InsnNode(DUP));
				list.add(new MethodInsnNode(INVOKESPECIAL, ecn, "<init>", "()V", false));
				list.add(new MethodInsnNode(INVOKEVIRTUAL, ecn, "call", "()V", false));
				methodNode.instructions.insert(list);
			}
		}
	}
	
	@Override
	public boolean isTarget(String name, boolean isSubclass) {
		return name.equalsIgnoreCase("net.minecraft.client.Minecraft") && !isSubclass;
	}

}
