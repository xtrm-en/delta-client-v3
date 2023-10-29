package me.xtrm.delta.loader.utils;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ASMHelper implements Opcodes {
	
	public static AbstractInsnNode getLastReturn(MethodNode mn) {
		AbstractInsnNode last = mn.instructions.getLast();
		while (last.getOpcode() != RETURN)
			last = last.getPrevious();
		return last;
	}
	
	public static AbstractInsnNode getLastAReturn(MethodNode mn) {
		AbstractInsnNode last = mn.instructions.getLast();
		while (last.getOpcode() != ARETURN)
			last = last.getPrevious();
		return last;
	}

	public static AbstractInsnNode getLastIReturn(MethodNode mn) {
		AbstractInsnNode last = mn.instructions.getLast();
		while (last.getOpcode() != IRETURN)
			last = last.getPrevious();
		return last;
	}
	
}
