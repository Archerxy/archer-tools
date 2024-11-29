package com.archer.tools.bytecode;

public class InstructionTable {
	private static final String[] TABLE = {
		    "nop",
		    "aconst_null",
		    "iconst_m1",
		    "iconst_0",
		    "iconst_1",
		    "iconst_2",
		    "iconst_3",
		    "iconst_4",
		    "iconst_5",
		    "lconst_0",
		    "lconst_1",
		    "fconst_0",
		    "fconst_1",
		    "fconst_2",
		    "dconst_0",
		    "dconst_1",
		    "bipush",
		    "sipush",
		    "ldc",
		    "ldc_w",
		    "ldc2_w",
		    "iload",
		    "lload",
		    "fload",
		    "dload",
		    "aload",
		    "iload_0",
		    "iload_1",
		    "iload_2",
		    "iload_3",
		    "lload_0",
		    "lload_1",
		    "lload_2",
		    "lload_3",
		    "fload_0",
		    "fload_1",
		    "fload_2",
		    "fload_3",
		    "dload_0",
		    "dload_1",
		    "dload_2",
		    "dload_3",
		    "aload_0",
		    "aload_1",
		    "aload_2",
		    "aload_3",
		    "iaload",
		    "laload",
		    "faload",
		    "daload",
		    "aaload",
		    "baload",
		    "caload",
		    "saload",
		    "istore",
		    "lstore",
		    "fstore",
		    "dstore",
		    "astore",
		    "istore_0",
		    "istore_1",
		    "istore_2",
		    "istore_3",
		    "lstore_0",
		    "lstore_1",
		    "lstore_2",
		    "lstore_3",
		    "fstore_0",
		    "fstore_1",
		    "fstore_2",
		    "fstore_3",
		    "dstore_0",
		    "dstore_1",
		    "dstore_2",
		    "dstore_3",
		    "astore_0",
		    "astore_1",
		    "astore_2",
		    "astore_3",
		    "iastore",
		    "lastore",
		    "fastore",
		    "dastore",
		    "aastore",
		    "bastore",
		    "castore",
		    "sastore",
		    "pop",
		    "pop2",
		    "dup",
		    "dup_x1",
		    "dup_x2",
		    "dup2",
		    "dup2_x1",
		    "dup2_x2",
		    "swap",
		    "iadd",
		    "ladd",
		    "fadd",
		    "dadd",
		    "isub",
		    "lsub",
		    "fsub",
		    "dsub",
		    "imul",
		    "lmul",
		    "fmul",
		    "dmul",
		    "idiv",
		    "ldiv",
		    "fdiv",
		    "ddiv",
		    "irem",
		    "lrem",
		    "frem",
		    "drem",
		    "ineg",
		    "lneg",
		    "fneg",
		    "dneg",
		    "ishl",
		    "lshl",
		    "ishr",
		    "lshr",
		    "iushr",
		    "lushr",
		    "iand",
		    "land",
		    "ior",
		    "lor",
		    "ixor",
		    "lxor",
		    "iinc",
		    "i2l",
		    "i2f",
		    "i2d",
		    "l2i",
		    "l2f",
		    "l2d",
		    "f2i",
		    "f2l",
		    "f2d",
		    "d2i",
		    "d2l",
		    "d2f",
		    "i2b",
		    "i2c",
		    "i2s",
		    "lcmp",
		    "fcmpl",
		    "fcmpg",
		    "dcmpl",
		    "dcmpg",
		    "ifeq",
		    "ifne",
		    "iflt",
		    "ifge",
		    "ifgt",
		    "ifle",
		    "if_icmpeq",
		    "if_icmpne",
		    "if_icmplt",
		    "if_icmpge",
		    "if_icmpgt",
		    "if_icmple",
		    "if_acmpeq",
		    "if_acmpne",
		    "goto",
		    "jsr",
		    "ret",
		    "tableswitch",
		    "lookupswitch",
		    "ireturn",
		    "lreturn",
		    "freturn",
		    "dreturn",
		    "areturn",
		    "return",
		    "getstatic",
		    "putstatic",
		    "getfield",
		    "putfield",
		    "invokevirtual",
		    "invokespecial",
		    "invokestatic",
		    "invokeinterface",
		    "--",
		    "new",
		    "newarray",
		    "anewarray",
		    "arraylength",
		    "athrow",
		    "checkcast",
		    "instanceof",
		    "monitorenter",
		    "monitorexit",
		    "wide",
		    "multianewarray",
		    "ifnull",
		    "ifnonnull",
		    "goto_w",
		    "jsr_w"
		   };
	public static String get(int index) {
		return TABLE[index];
	}
	
	public static byte getInstructionCode(String instruction) {
		for(int i = 0; i < TABLE.length; i++) {
			if(TABLE[i].equals(instruction)) {
				return (byte) i;
			}
		}
		throw new IllegalArgumentException("invalid instruction " + instruction);
	}
	
	public static byte[] decodeLoadIns(String type, int index) {
		if(type.length() == 1) {
			byte c = type.getBytes()[0];
			if(c == 'Z' || c == 'B' || c == 'C' || c == 'S' || c == 'I') {
				if(index == 0) {
					return new byte[] {getInstructionCode("iload_1")};
				}
				if(index == 1) {
					return new byte[] {getInstructionCode("iload_2")};
				}
				if(index == 2) {
					return new byte[] {getInstructionCode("iload_3")};
				}
				return new byte[] {getInstructionCode("iload"), (byte) (index + 1)};
			}
			if(c == 'J') {
				if(index == 0) {
					return new byte[] {getInstructionCode("lload_1")};
				}
				if(index == 1) {
					return new byte[] {getInstructionCode("lload_2")};
				}
				if(index == 2) {
					return new byte[] {getInstructionCode("lload_3")};
				}
				return new byte[] {getInstructionCode("lload"), (byte) (index + 1)};
			}
			if(c == 'F') {
				if(index == 0) {
					return new byte[] {getInstructionCode("fload_1")};
				}
				if(index == 1) {
					return new byte[] {getInstructionCode("fload_2")};
				}
				if(index == 2) {
					return new byte[] {getInstructionCode("fload_3")};
				}
				return new byte[] {getInstructionCode("fload"), (byte) (index + 1)};
			} 
			if(c == 'D') {
				if(index == 0) {
					return new byte[] {getInstructionCode("dload_1")};
				}
				if(index == 1) {
					return new byte[] {getInstructionCode("dload_2")};
				}
				if(index == 2) {
					return new byte[] {getInstructionCode("dload_3")};
				}
				return new byte[] {getInstructionCode("dload"), (byte) (index + 1)};
			} 
			throw new IllegalArgumentException("invalid instruction load type " + type);
		}
		if(index == 0) {
			return new byte[] {getInstructionCode("aload_1")};
		}
		if(index == 1) {
			return new byte[] {getInstructionCode("aload_2")};
		}
		if(index == 2) {
			return new byte[] {getInstructionCode("aload_3")};
		}
		return new byte[] {getInstructionCode("aload"), (byte) (index + 1)};
	}
}
