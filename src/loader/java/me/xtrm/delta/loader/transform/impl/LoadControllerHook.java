package me.xtrm.delta.loader.transform.impl;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import me.xtrm.delta.loader.api.event.data.EventType;
import me.xtrm.delta.loader.api.event.events.lifecycle.EventFMLPreInitialization;
import me.xtrm.xeon.loader.api.transform.ITransformer;

public class LoadControllerHook implements ITransformer {

	@Override
	public void transform(ClassNode classNode, String clazz) {
		for(MethodNode mn : classNode.methods) {
			if(mn.name.equalsIgnoreCase("sendEventToModContainer")) {
				String eventName = EventFMLPreInitialization.class.getName().replace('.', '/');
				InsnList list = new InsnList();
				list.add(new VarInsnNode(ALOAD, 1));
				list.add(new TypeInsnNode(INSTANCEOF, "cpw/mods/fml/common/event/FMLPreInitializationEvent"));
				LabelNode l1 = new LabelNode();
				list.add(new JumpInsnNode(IFEQ, l1));
				LabelNode l2 = new LabelNode();
				list.add(l2);
				list.add(new TypeInsnNode(NEW, eventName));
				list.add(new InsnNode(DUP));
				list.add(new FieldInsnNode(GETSTATIC, EventType.class.getName().replace('.', '/'), EventType.ON.toString(), "L" + EventType.class.getName().replace('.', '/') + ";"));
				list.add(new VarInsnNode(ALOAD, 1));
				list.add(new TypeInsnNode(CHECKCAST, "cpw/mods/fml/common/event/FMLPreInitializationEvent"));
				list.add(new MethodInsnNode(INVOKESPECIAL, eventName, "<init>", "(L" + EventType.class.getName().replace('.', '/') + ";Lcpw/mods/fml/common/event/FMLPreInitializationEvent;)V", false));
				list.add(new MethodInsnNode(INVOKEVIRTUAL, eventName, "call", "()V", false));
				list.add(l1);
				mn.instructions.insert(list);
			}
		}
	}
	
	@Override
	public boolean isTarget(String name, boolean isSubclass) {
		return name.equalsIgnoreCase("cpw.mods.fml.common.LoadController") && !isSubclass;
	}

}
