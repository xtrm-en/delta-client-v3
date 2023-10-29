package me.xtrm.delta.client.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import me.xtrm.delta.client.utils.reflect.Java9Fix;

public class ASMUtils {
	
	public static AbstractInsnNode getLastReturn(MethodNode mn) {
		AbstractInsnNode last = mn.instructions.getLast();
		while (last.getOpcode() != Opcodes.RETURN)
			last = last.getPrevious();
		return last;
	}
	
	public static AbstractInsnNode getLastAReturn(MethodNode mn) {
		AbstractInsnNode last = mn.instructions.getLast();
		while (last.getOpcode() != Opcodes.ARETURN)
			last = last.getPrevious();
		return last;
	}

	public static AbstractInsnNode getLastIReturn(MethodNode mn) {
		AbstractInsnNode last = mn.instructions.getLast();
		while (last.getOpcode() != Opcodes.IRETURN)
			last = last.getPrevious();
		return last;
	}
	
	private static Map<Integer, String> opcodes;
	
	public static String getOpcodeAsStr(int opcode) {
		if(opcodes == null) {
			try {
				opcodes = new HashMap<>();
				
				int count = 0;
				for(Field f : Opcodes.class.getDeclaredFields()) {
					if(count < 60) {
						count++;
						continue;
					}
					
					Java9Fix.setAccessible(f);
					int i = (int)f.get(null);
					opcodes.put(i, f.getName());
				}
			} catch(ReflectiveOperationException e) {
				opcodes = null;
				e.printStackTrace();
			}
		}
		return opcodes.get(opcode);
	}
}
