package com.archer.tools.bytecode;

import java.util.Arrays;

import com.archer.tools.bytecode.MemberInfo.AttributeInfo;
import com.archer.tools.bytecode.MemberInfo.CodeAttribute;
import com.archer.tools.bytecode.MemberInfo.LineNumAttribute;
import com.archer.tools.bytecode.MemberInfo.LocalVarAttribute;
import com.archer.tools.bytecode.constantpool.ConstantClass;
import com.archer.tools.bytecode.constantpool.ConstantDouble;
import com.archer.tools.bytecode.constantpool.ConstantFloat;
import com.archer.tools.bytecode.constantpool.ConstantInfo;
import com.archer.tools.bytecode.constantpool.ConstantInteger;
import com.archer.tools.bytecode.constantpool.ConstantInvokeDynamic;
import com.archer.tools.bytecode.constantpool.ConstantLong;
import com.archer.tools.bytecode.constantpool.ConstantMemberRef;
import com.archer.tools.bytecode.constantpool.ConstantMethodHandle;
import com.archer.tools.bytecode.constantpool.ConstantMethodType;
import com.archer.tools.bytecode.constantpool.ConstantNameAndType;
import com.archer.tools.bytecode.constantpool.ConstantPool;
import com.archer.tools.bytecode.constantpool.ConstantString;
import com.archer.tools.bytecode.constantpool.ConstantUtf8;

public class ClassBytecodePrinter {

    public static void printConstantPool(ConstantPool pool){
    	ConstantInfo[] cpInfo = pool.getCpInfo();
    	for(int i=1;i< cpInfo.length; i++){
    		ConstantInfo info = cpInfo[i];
	        switch (info.tag) {
	            case ConstantInfo.CONSTANT_Class:
	            	System.out.println("#"+i+" class         #"+((ConstantClass)info).getNameIndex());
	            	break ;  
	            case ConstantInfo.CONSTANT_Fieldref:
	            	System.out.println("#"+i+" FieldRef      #"+((ConstantMemberRef)info).getClassIndex() + ".#" + ((ConstantMemberRef)info).getNameAndTypeIndex());
	            	break ;
	            case ConstantInfo.CONSTANT_Methodref: {
	            	ConstantClass clazz = ((ConstantClass)cpInfo[((ConstantMemberRef)info).getClassIndex()]);
	            	String className = ((ConstantUtf8)cpInfo[clazz.getNameIndex()]).getValue();
	            	ConstantNameAndType cnt = ((ConstantNameAndType)cpInfo[((ConstantMemberRef)info).getNameAndTypeIndex()]);
	            	String methodName = ((ConstantUtf8)cpInfo[cnt.getNameIndex()]).getValue();
	            	String methodDesc = ((ConstantUtf8)cpInfo[cnt.getDescIndex()]).getValue();
	            	System.out.println("#"+i+" MethodRef     #"+((ConstantMemberRef)info).getClassIndex() + ".#" + ((ConstantMemberRef)info).getNameAndTypeIndex()+ "    //" + className + "."+methodName+":"+methodDesc);
	            	break ;
	            }
	            case ConstantInfo.CONSTANT_InterfaceMethodref:
	            	System.out.println("#"+i+" InterfaceRef  #"+((ConstantMemberRef)info).getClassIndex() + ".#" + ((ConstantMemberRef)info).getNameAndTypeIndex());
	            	break ;
	            case ConstantInfo.CONSTANT_Long:
	            	System.out.println("#"+i+" long          "+((ConstantLong)info).getHighValue() + " " + ((ConstantLong)info).getLowValue());
	            	break ;
	            case ConstantInfo.CONSTANT_Double:
	            	System.out.println("#"+i+" double        "+((ConstantDouble)info).getHighValue() + " " + ((ConstantDouble)info).getLowValue());
	            	break ;
	            case ConstantInfo.CONSTANT_String:
	            	System.out.println("#"+i+" string        "+((ConstantString)info).getNameIndex());
	            	break ;
	            case ConstantInfo.CONSTANT_Integer:
	            	System.out.println("#"+i+" int           "+((ConstantInteger)info).getValue());
	            	break ;
	            case ConstantInfo.CONSTANT_Float:
	            	System.out.println("#"+i+" float         "+((ConstantFloat)info).getValue());
	            	break ;
	            case ConstantInfo.CONSTANT_NameAndType:
	            	ConstantNameAndType cnt = ((ConstantNameAndType)info);
	            	String methodName = ((ConstantUtf8)cpInfo[cnt.getNameIndex()]).getValue();
	            	String methodDesc = ((ConstantUtf8)cpInfo[cnt.getDescIndex()]).getValue();
	            	System.out.println("#"+i+" NameAndType   #"+((ConstantNameAndType)info).getNameIndex() + ".#" +((ConstantNameAndType)info).getDescIndex() + "    //" + methodName +":"+methodDesc);
	            	break ;
	            case ConstantInfo.CONSTANT_Utf8:
	            	System.out.println("#"+i+" utf8          "+((ConstantUtf8)info).getValue());
	            	break ;
	            case ConstantInfo.CONSTANT_MethodHandle:
	            	System.out.println("#"+i+" MethodHandle  #"+((ConstantMethodHandle)info).getReferenceIndex() + ".#" +((ConstantMethodHandle)info).getReferenceKind());
	            	break ;
	            case ConstantInfo.CONSTANT_MethodType:
	            	System.out.println("#"+i+" MethodType    "+((ConstantMethodType)info).getDescType());
	            	break ;
	            case ConstantInfo.CONSTANT_InvokeDynamic:
	            	System.out.println("#"+i+" InvokeDynamic "+((ConstantInvokeDynamic)info).getNameAndTypeIndex() + " "+((ConstantInvokeDynamic)info).getBootstrapMethodAttrIndex());
	            	break ;
	            default:
	                System.out.println("#"+i+" null");
	        }
    	}
    }
    
	public static void printFields(MemberInfo[] fields) {
		for(int i = 0; i < fields.length; i++) {
			MemberInfo fieldInfo = fields[i];
			System.out.println("\nfield: " + fieldInfo.getName() + ": #" + fieldInfo.getNameIndex() + ".#" + fieldInfo.getDescriptorIndex() + ", access: " + fieldInfo.getAccessFlags() + ", attrCount: " + fieldInfo.getAttributesCount());
			printAttrbutes(fieldInfo.getAttributes(), 0);
		}
	}
	
	public static void printMethods(MemberInfo[] methods) {
		for(int i = 0; i < methods.length; i++) {
			MemberInfo fieldInfo = methods[i];
			System.out.println("\nmethod: " + fieldInfo.getName() + ", index: #" + fieldInfo.getNameIndex() + ", desc: #" + fieldInfo.getDescriptorIndex() + ", access: " + fieldInfo.getAccessFlags() + ", attrCount: " + fieldInfo.getAttributesCount());
			printAttrbutes(fieldInfo.getAttributes(), 0);
		}
	}

	
    private static void printAttrbutes(AttributeInfo[] attributes, int depth) {
    	String tab = "    ";
    	for(int i = 0; i < depth; i++) {
    		tab += "    ";
    	}
    	for (int j = 0; j < attributes.length; j++) {
    		String name = attributes[j].getName();
            if (attributes[j] instanceof CodeAttribute) {
                CodeAttribute codeAttribute = (CodeAttribute) attributes[j];
            	System.out.println(tab + name + ": nameIndex: #" + codeAttribute.getNameIndex() + ", length: " + codeAttribute.getLength()
            			+ ", maxStack: " + codeAttribute.getMaxStack() + ", maxLocals: " + codeAttribute.getMaxLocals()
            			+ ", exception: " + Arrays.toString(codeAttribute.getException()) + ", code: " + Arrays.toString(codeAttribute.getCode()) + " {");
                byte[] codes = codeAttribute.getCode();
            	for (int m = 0; m < codeAttribute.getCodeLength(); m++) {
                    int code = codes[m];
                    code = code < 0 ? code + 256 : code;
                    System.out.println(tab +"  " + InstructionTable.get(code) + "("+code+")");
                }
                System.out.println(tab +"}");
            	printAttrbutes(codeAttribute.getAttributes(), depth + 1);
            } else if(attributes[j] instanceof LineNumAttribute) {
    			LineNumAttribute lnattr = (LineNumAttribute) attributes[j];
            	System.out.println(tab + name + ": nameIndex: #" + lnattr.getNameIndex() + ", length: " + lnattr.getLength() + " [");
            	for(int i = 0; i < lnattr.getLineNumTableCount(); i++) {
            		int[] ln = lnattr.getLineNumTable()[i];
                	System.out.println(tab + "  start_pc: " + ln[0] + ", line: " + ln[1]);
            	}
                System.out.println(tab +"]");
    		} else if (attributes[j] instanceof LocalVarAttribute) {
            	LocalVarAttribute lvattr = (LocalVarAttribute) attributes[j];
            	System.out.println(tab + name + ": nameIndex: #" + lvattr.getNameIndex() + ", length: " + lvattr.getLength() + " [");
            	for(int i = 0; i < lvattr.getLocalVarTableCount(); i++) {
            		int[] lv = lvattr.getLocalVarTable()[i];
                	System.out.println(tab + "  start_pc: " + lv[0] + ", len: " + lv[1] + ", name_index: " + lv[2] +", desc_index: " + lv[3] + ", index: "+lv[4]);
            	}
                System.out.println(tab +"]");
            } else {
            	AttributeInfo attr = attributes[j];
            	System.out.println(tab + name + ": nameIndex: #" + attr.getNameIndex() + ", length: " + attr.getLength() 
            			+ ", info: " + Arrays.toString(attr.getInfo()));
            }
        }
    }
	
}
