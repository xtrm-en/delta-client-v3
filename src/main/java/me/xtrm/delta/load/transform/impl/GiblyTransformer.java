package me.xtrm.delta.load.transform.impl;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import me.xtrm.xeon.loader.api.transform.ITransformer;

public class GiblyTransformer implements ITransformer {

	@Override
	public void transform(ClassNode classNode, String className) {
		InsnList list = new InsnList();
	    list.add(new VarInsnNode(ILOAD, 3));
	    LabelNode laelNode = new LabelNode();
	    list.add(new JumpInsnNode(IFNE, laelNode));
	    list.add(new InsnNode(ICONST_0));
	    list.add(new InsnNode(IRETURN));
	    list.add(laelNode);
	    for(MethodNode methodNode : classNode.methods) {
	    	if(methodNode != null && (methodNode.desc.equals("(I[BIZB)Z"))) {
	    		methodNode.instructions.insert(list);
	    	}
	    }   
	}
	

}
