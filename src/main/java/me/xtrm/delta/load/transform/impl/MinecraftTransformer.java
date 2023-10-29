package me.xtrm.delta.load.transform.impl;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import me.xtrm.delta.client.utils.ASMUtils;
import me.xtrm.delta.wrapper.WrapData;
import me.xtrm.delta.wrapper.WrapManager;
import me.xtrm.xeon.loader.api.transform.ITransformer;
import scala.actors.threadpool.Arrays;

public class MinecraftTransformer implements ITransformer {
	
	@Override
	public void transform(ClassNode classNode, String className) {
		switch(className) {
			case "net.minecraft.client.Minecraft":
		    	for(MethodNode methodNode : classNode.methods) {
		    		AbstractInsnNode zeNode = null;
		    		Iterator<AbstractInsnNode> absInsnList = methodNode.instructions.iterator();
		    		while(absInsnList.hasNext()) {
		    			AbstractInsnNode absInsn = absInsnList.next();
		    			if(absInsn.getOpcode() == INVOKESTATIC) {
							MethodInsnNode nowode = (MethodInsnNode) absInsn;
							if(nowode.owner.equalsIgnoreCase("org/lwjgl/opengl/Display") && nowode.name.equalsIgnoreCase("setTitle") && nowode.desc.equalsIgnoreCase("(Ljava/lang/String;)V")) {
								zeNode = nowode;
								break;
							}
		    			}
		    		}
		    		if(zeNode == null) continue;
		    		
		    		WrapData data = WrapManager.INSTANCE.get("overrideDisplay");
		    		
		    		InsnList owo = new InsnList();
		    		owo.add(new MethodInsnNode(INVOKESTATIC, data.getParent().getName().replace('.', '/'), data.getMethod().getName(), "()V", false));
		    		
		    		methodNode.instructions.insert(zeNode, owo);
		    	}
		    	break;
			case "net.minecraft.client.entity.EntityClientPlayerMP":
				WrapData data = WrapManager.INSTANCE.get("factoryMotionUpdate");
				
		    	InsnList listPre = new InsnList();
			    listPre.add(new VarInsnNode(ALOAD, 0));
			    listPre.add(new InsnNode(ICONST_1));
			    listPre.add(new MethodInsnNode(INVOKESTATIC, data.getParent().getName().replace('.', '/'), data.getMethod().getName(), "(Ljava/lang/Object;Z)Z", false));
			    LabelNode labelPre = new LabelNode();
			    listPre.add(new JumpInsnNode(IFEQ, labelPre));
			    listPre.add(new InsnNode(RETURN));
			    listPre.add(labelPre);
			    
			    InsnList listPost = new InsnList();
			    listPost.add(new VarInsnNode(ALOAD, 0));
			    listPost.add(new InsnNode(ICONST_0));
			    listPost.add(new MethodInsnNode(INVOKESTATIC, data.getParent().getName().replace('.', '/'), data.getMethod().getName(), "(Ljava/lang/Object;Z)Z", false));
			    listPost.add(new InsnNode(POP));
			    
			    for(MethodNode methodNode : classNode.methods) { 
			    	if(methodNode != null && (methodNode.name.equalsIgnoreCase("func_71166_b") || (methodNode.name.equalsIgnoreCase("a") && methodNode.desc.equalsIgnoreCase("()V")) || methodNode.name.equalsIgnoreCase("sendMotionUpdates"))) {
			    		methodNode.instructions.insert(listPre);
			    		methodNode.instructions.insertBefore(ASMUtils.getLastReturn(methodNode), listPost);
			    	}
			    }
		    	break;
			case "net.minecraft.block.Block":
				WrapData data1 = WrapManager.INSTANCE.get("shouldRender");
				WrapData data2 = WrapManager.INSTANCE.get("factoryShouldBeOpaque");
				
		    	InsnList list = new InsnList();
			    list.add(new VarInsnNode(ALOAD, 0));
			    list.add(new VarInsnNode(ALOAD, 1)); 
			    list.add(new VarInsnNode(ILOAD, 2));
			    list.add(new VarInsnNode(ILOAD, 3));
			    list.add(new VarInsnNode(ILOAD, 4));
			    list.add(new VarInsnNode(ILOAD, 5));
			    list.add(new MethodInsnNode(INVOKESTATIC, data1.getParent().getName().replace('.', '/'), data1.getMethod().getName(), "(ZLjava/lang/Object;Ljava/lang/Object;IIII)Z", false));
			    
			    InsnList list2 = new InsnList();
			    list2.add(new MethodInsnNode(INVOKESTATIC, data2.getParent().getName().replace('.', '/'), data2.getMethod().getName(), "(Ljava/lang/Object;)Z", false));
			    LabelNode lblNode = new LabelNode();
			    list2.add(new JumpInsnNode(IFNE, lblNode));
			    list2.add(new InsnNode(ICONST_0));
			    list2.add(new InsnNode(IRETURN));
			    list2.add(lblNode);
			    
			    for(MethodNode methodNode : classNode.methods) {
			    	if(methodNode.name.equalsIgnoreCase("isNormalCube") || methodNode.name.equalsIgnoreCase("func_149721_r") || (methodNode.name.equalsIgnoreCase("r") && methodNode.desc.equalsIgnoreCase("()Z"))) {
			    		methodNode.instructions.insert(list2);
			    	}
			    	if(methodNode.name.equalsIgnoreCase("isOpaqueCube") || methodNode.name.equalsIgnoreCase("func_149662_c") || (methodNode.name.equalsIgnoreCase("c") && methodNode.desc.equalsIgnoreCase("()Z"))) {
			    		methodNode.instructions.insert(list2);
			    	}
			    	if(methodNode.name.equalsIgnoreCase("shouldSideBeRendered") || methodNode.name.equalsIgnoreCase("func_149646_a")) {
			    		methodNode.instructions.insertBefore(ASMUtils.getLastIReturn(methodNode), list);
			    	}
			    }
		    	break;
			case "net.minecraft.network.NetworkManager":
				WrapData d0ta = WrapManager.INSTANCE.get("factoryPacket");
				for(MethodNode mn : classNode.methods) {
					if(mn.name.equals("func_150725_a") || mn.name.equals("scheduleOutboundPacket")) {
						InsnList list69 = new InsnList();
						list69.add(new VarInsnNode(ALOAD, 1));
						list69.add(new InsnNode(ICONST_0));
						list69.add(new MethodInsnNode(INVOKESTATIC, d0ta.getParent().getName().replace('.', '/'), d0ta.getMethod().getName(), "(Ljava/lang/Object;Z)Z", false));
						LabelNode label = new LabelNode();
						list69.add(new JumpInsnNode(IFEQ, label));
						list69.add(new InsnNode(RETURN));
						list69.add(label);
						
						mn.instructions.insert(list69);
					}
					if(mn.name.equals("channelRead0") && mn.desc.equals("(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V")) {
						InsnList list69 = new InsnList();
						list69.add(new VarInsnNode(ALOAD, 2));
						list69.add(new InsnNode(ICONST_1));
						list69.add(new MethodInsnNode(INVOKESTATIC, d0ta.getParent().getName().replace('.', '/'), d0ta.getMethod().getName(), "(Ljava/lang/Object;Z)Z", false));
						LabelNode label = new LabelNode();
						list69.add(new JumpInsnNode(IFEQ, label));
						list69.add(new InsnNode(RETURN));
						list69.add(label);
						
						mn.instructions.insert(list69);
					}
				}
				break;
		}
	}

	private final List<String> target = Arrays.asList(new String[] {
			"net.minecraft.network.NetworkManager",
			"net.minecraft.client.entity.EntityClientPlayerMP",
			"net.minecraft.block.Block",
			"net.minecraft.client.Minecraft"
	});
	
	@Override
	public boolean isTarget(String className, boolean isSubclass) {
		return !isSubclass && target.contains(className);
	}

}
