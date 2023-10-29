package me.xtrm.delta.loader.transform.impl;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import me.xtrm.delta.loader.api.event.data.EventType;
import me.xtrm.delta.loader.api.event.events.lifecycle.EventFMLInitialization;
import me.xtrm.delta.loader.api.event.events.lifecycle.EventFMLLoad;
import me.xtrm.delta.loader.api.event.events.lifecycle.EventFMLPostInitialization;
import me.xtrm.delta.loader.api.event.events.lifecycle.EventFMLPreInitialization;
import me.xtrm.xeon.loader.api.transform.ITransformer;

public class LoaderHook implements ITransformer {
	
	@Override
	public void transform(ClassNode classNode, String clazz) {
		MethodNode node = null;
		for(MethodNode mn : classNode.methods) {
			if(mn.name.equalsIgnoreCase("initializeMods")) {
				node = mn;
				break;
			}
		}
		classNode.methods.remove(node);
		
		String init = EventFMLInitialization.class.getName().replace('.', '/');
		String post = EventFMLPostInitialization.class.getName().replace('.', '/');
		
		MethodVisitor mv = classNode.visitMethod(ACC_PUBLIC, "initializeMods", "()V", null, null);
		{
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitTypeInsn(NEW, init);
			mv.visitInsn(DUP);
			mv.visitFieldInsn(GETSTATIC, "me/xtrm/delta/loader/api/event/data/EventType", "PRE", "Lme/xtrm/delta/loader/api/event/data/EventType;");
			mv.visitMethodInsn(INVOKESPECIAL, init, "<init>", "(Lme/xtrm/delta/loader/api/event/data/EventType;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, init, "call", "()V", false);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "cpw/mods/fml/common/Loader", "progressBar", "Lcpw/mods/fml/common/ProgressManager$ProgressBar;");
			mv.visitLdcInsn("Initializing mods Phase 2");
			mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/common/ProgressManager$ProgressBar", "step", "(Ljava/lang/String;)V", false);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "cpw/mods/fml/common/Loader", "modController", "Lcpw/mods/fml/common/LoadController;");
			mv.visitFieldInsn(GETSTATIC, "cpw/mods/fml/common/LoaderState", "INITIALIZATION", "Lcpw/mods/fml/common/LoaderState;");
			mv.visitInsn(ICONST_0);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/common/LoadController", "distributeStateMessage", "(Lcpw/mods/fml/common/LoaderState;[Ljava/lang/Object;)V", false);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitTypeInsn(NEW, init);
			mv.visitInsn(DUP);
			mv.visitFieldInsn(GETSTATIC, "me/xtrm/delta/loader/api/event/data/EventType", "POST", "Lme/xtrm/delta/loader/api/event/data/EventType;");
			mv.visitMethodInsn(INVOKESPECIAL, init, "<init>", "(Lme/xtrm/delta/loader/api/event/data/EventType;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, init, "call", "()V", false);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitTypeInsn(NEW, post);
			mv.visitInsn(DUP);
			mv.visitFieldInsn(GETSTATIC, "me/xtrm/delta/loader/api/event/data/EventType", "PRE", "Lme/xtrm/delta/loader/api/event/data/EventType;");
			mv.visitMethodInsn(INVOKESPECIAL, post, "<init>", "(Lme/xtrm/delta/loader/api/event/data/EventType;)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, post, "call", "()V", false);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "cpw/mods/fml/common/Loader", "progressBar", "Lcpw/mods/fml/common/ProgressManager$ProgressBar;");
			mv.visitLdcInsn("Initializing mods Phase 3");
			mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/common/ProgressManager$ProgressBar", "step", "(Ljava/lang/String;)V", false);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "cpw/mods/fml/common/Loader", "modController", "Lcpw/mods/fml/common/LoadController;");
			mv.visitFieldInsn(GETSTATIC, "cpw/mods/fml/common/LoaderState", "POSTINITIALIZATION", "Lcpw/mods/fml/common/LoaderState;");
			mv.visitInsn(ICONST_0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/common/LoadController", "transition", "(Lcpw/mods/fml/common/LoaderState;Z)V", false);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "cpw/mods/fml/common/Loader", "modController", "Lcpw/mods/fml/common/LoadController;");
			mv.visitLdcInsn(Type.getType("Lcpw/mods/fml/common/event/FMLInterModComms$IMCEvent;"));
			mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/common/LoadController", "distributeStateMessage", "(Ljava/lang/Class;)V", false);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitFieldInsn(GETSTATIC, "cpw/mods/fml/common/registry/ItemStackHolderInjector", "INSTANCE", "Lcpw/mods/fml/common/registry/ItemStackHolderInjector;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/common/registry/ItemStackHolderInjector", "inject", "()V", false);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "cpw/mods/fml/common/Loader", "modController", "Lcpw/mods/fml/common/LoadController;");
			mv.visitFieldInsn(GETSTATIC, "cpw/mods/fml/common/LoaderState", "POSTINITIALIZATION", "Lcpw/mods/fml/common/LoaderState;");
			mv.visitInsn(ICONST_0);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/common/LoadController", "distributeStateMessage", "(Lcpw/mods/fml/common/LoaderState;[Ljava/lang/Object;)V", false);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitTypeInsn(NEW, post);
			mv.visitInsn(DUP);
			mv.visitFieldInsn(GETSTATIC, "me/xtrm/delta/loader/api/event/data/EventType", "POST", "Lme/xtrm/delta/loader/api/event/data/EventType;");
			mv.visitMethodInsn(INVOKESPECIAL, post, "<init>", "(Lme/xtrm/delta/loader/api/event/data/EventType;)V", false); // srx mdr
			mv.visitMethodInsn(INVOKEVIRTUAL, post, "call", "()V", false);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "cpw/mods/fml/common/Loader", "progressBar", "Lcpw/mods/fml/common/ProgressManager$ProgressBar;");
			mv.visitLdcInsn("Finishing up");
			mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/common/ProgressManager$ProgressBar", "step", "(Ljava/lang/String;)V", false);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "cpw/mods/fml/common/Loader", "modController", "Lcpw/mods/fml/common/LoadController;");
			mv.visitFieldInsn(GETSTATIC, "cpw/mods/fml/common/LoaderState", "AVAILABLE", "Lcpw/mods/fml/common/LoaderState;");
			mv.visitInsn(ICONST_0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/common/LoadController", "transition", "(Lcpw/mods/fml/common/LoaderState;Z)V", false);
			Label l13 = new Label();
			mv.visitLabel(l13);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "cpw/mods/fml/common/Loader", "modController", "Lcpw/mods/fml/common/LoadController;");
			mv.visitFieldInsn(GETSTATIC, "cpw/mods/fml/common/LoaderState", "AVAILABLE", "Lcpw/mods/fml/common/LoaderState;");
			mv.visitInsn(ICONST_0);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/common/LoadController", "distributeStateMessage", "(Lcpw/mods/fml/common/LoaderState;[Ljava/lang/Object;)V", false);
			Label l14 = new Label();
			mv.visitLabel(l14);
			mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/common/registry/GameData", "freezeData", "()V", false);
			Label l15 = new Label();
			mv.visitLabel(l15);
			mv.visitLdcInsn("Forge Mod Loader has successfully loaded %d mod%s");
			mv.visitInsn(ICONST_2);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "cpw/mods/fml/common/Loader", "mods", "Ljava/util/List;");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "size", "()I", true);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitInsn(AASTORE);
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "cpw/mods/fml/common/Loader", "mods", "Ljava/util/List;");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "size", "()I", true);
			mv.visitInsn(ICONST_1);
			Label l16 = new Label();
			mv.visitJumpInsn(IF_ICMPNE, l16);
			mv.visitLdcInsn("");
			Label l17 = new Label();
			mv.visitJumpInsn(GOTO, l17);
			mv.visitLabel(l16);
			mv.visitLdcInsn("s");
			mv.visitLabel(l17);
			mv.visitInsn(AASTORE);
			mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/common/FMLLog", "info", "(Ljava/lang/String;[Ljava/lang/Object;)V", false);
			Label l18 = new Label();
			mv.visitLabel(l18);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "cpw/mods/fml/common/Loader", "progressBar", "Lcpw/mods/fml/common/ProgressManager$ProgressBar;");
			mv.visitLdcInsn("Completing Minecraft initialization");
			mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/common/ProgressManager$ProgressBar", "step", "(Ljava/lang/String;)V", false);
			Label l19 = new Label();
			mv.visitLabel(l19);
			mv.visitInsn(RETURN);
			Label l20 = new Label();
			mv.visitLabel(l20);
			mv.visitEnd();
		}
		
		for(MethodNode mn : classNode.methods) {
			if(mn.name.equalsIgnoreCase("loadMods")) {
				String className = EventFMLLoad.class.getName().replace('.', '/');
				InsnList list = new InsnList();
				list.add(new TypeInsnNode(NEW, className));
				list.add(new InsnNode(DUP));
				list.add(new MethodInsnNode(INVOKESPECIAL, className, "<init>", "()V", false));
				list.add(new MethodInsnNode(INVOKEVIRTUAL, className, "call", "()V", false));
				mn.instructions.insert(list);
			}
			if(mn.name.equalsIgnoreCase("preinitializeMods")) {
				// PRE
				String className = EventFMLPreInitialization.class.getName().replace('.', '/');
				InsnList list = new InsnList();
				list.add(new TypeInsnNode(NEW, className));
				list.add(new InsnNode(DUP));
				list.add(new FieldInsnNode(GETSTATIC, EventType.class.getName().replace('.', '/'), EventType.PRE.toString(), "L" + EventType.class.getName().replace('.', '/') + ";"));
				list.add(new InsnNode(ACONST_NULL));
				list.add(new MethodInsnNode(INVOKESPECIAL, className, "<init>", "(L" + EventType.class.getName().replace('.', '/') + ";Lcpw/mods/fml/common/event/FMLPreInitializationEvent;)V", false));
				list.add(new MethodInsnNode(INVOKEVIRTUAL, className, "call", "()V", false));
				mn.instructions.insert(list);
				
				// POST
				AbstractInsnNode insnTarget = null;
				for(int i = 0; i < mn.instructions.size(); i++) {
					AbstractInsnNode curr = mn.instructions.get(i);
					if(curr.getOpcode() == INVOKEVIRTUAL) {
						MethodInsnNode methodInsn = (MethodInsnNode)curr;
						if(methodInsn.owner.equalsIgnoreCase("cpw/mods/fml/common/registry/ItemStackHolderInjector") && methodInsn.name.equalsIgnoreCase("inject")) {
							insnTarget = curr;
							break;
						}
					}
				}
				
				InsnList list2 = new InsnList();
				list2.add(new TypeInsnNode(NEW, className));
				list2.add(new InsnNode(DUP));
				list2.add(new FieldInsnNode(GETSTATIC, EventType.class.getName().replace('.', '/'), EventType.POST.toString(), "L" + EventType.class.getName().replace('.', '/') + ";"));
				list2.add(new InsnNode(ACONST_NULL));
				list2.add(new MethodInsnNode(INVOKESPECIAL, className, "<init>", "(L" + EventType.class.getName().replace('.', '/') + ";Lcpw/mods/fml/common/event/FMLPreInitializationEvent;)V", false));
				list2.add(new MethodInsnNode(INVOKEVIRTUAL, className, "call", "()V", false));
				mn.instructions.insertBefore(insnTarget, list2);
			}
		}
	}
	
	@Override
	public boolean isTarget(String name, boolean isSubclass) {
		return name.equalsIgnoreCase("cpw.mods.fml.common.Loader") && !isSubclass;
	}

}
