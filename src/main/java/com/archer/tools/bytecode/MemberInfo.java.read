package com.archer.tools.bytecode;

import com.archer.net.Bytes;
import com.archer.tools.bytecode.constantpool.ConstantInfo;
import com.archer.tools.bytecode.constantpool.ConstantUtf8;


public class MemberInfo {
    private int accessFlags;
    private int nameIndex;
    private int descriptorIndex;
    private int attributesCount;
    
    private AttributeInfo[] attributes;
    

    private String name;
    private String desc;
    
    
    
    public int getAccessFlags() {
		return accessFlags;
	}

	public int getNameIndex() {
		return nameIndex;
	}

	public int getDescriptorIndex() {
		return descriptorIndex;
	}

	public int getAttributesCount() {
		return attributesCount;
	}

	public AttributeInfo[] getAttributes() {
		return attributes;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public void setAccessFlags(int accessFlags) {
		this.accessFlags = accessFlags;
	}

	public void setNameIndex(int nameIndex) {
		this.nameIndex = nameIndex;
	}

	public void setDescriptorIndex(int descriptorIndex) {
		this.descriptorIndex = descriptorIndex;
	}

	public void setAttributesCount(int attributesCount) {
		this.attributesCount = attributesCount;
	}

	public void setAttributes(AttributeInfo[] attributes) {
		this.attributes = attributes;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static class AttributeInfo {
		private int nameIndex;
		private int length;
		private byte[] info;
        
		private String name;

		public int getNameIndex() {
			return nameIndex;
		}

		public int getLength() {
			return length;
		}

		public byte[] getInfo() {
			return info;
		}

		public String getName() {
			return name;
		}

		public void setNameIndex(int nameIndex) {
			this.nameIndex = nameIndex;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public void setInfo(byte[] info) {
			this.info = info;
		}

		public void setName(String name) {
			this.name = name;
		}
    }
    
    public static class CodeAttribute extends AttributeInfo {
    	private int maxStack;
    	private int maxLocals;
    	private int codeLength;
    	private byte[] code;
    	private int excepetionTableLength;
    	private byte[] exception;
    	private int attributesCount;
    	private AttributeInfo[] attributes;
		public int getMaxStack() {
			return maxStack;
		}
		public int getMaxLocals() {
			return maxLocals;
		}
		public int getCodeLength() {
			return codeLength;
		}
		public byte[] getCode() {
			return code;
		}
		public int getExcepetionTableLength() {
			return excepetionTableLength;
		}
		public byte[] getException() {
			return exception;
		}
		public int getAttributesCount() {
			return attributesCount;
		}
		public AttributeInfo[] getAttributes() {
			return attributes;
		}
		public void setMaxStack(int maxStack) {
			this.maxStack = maxStack;
		}
		public void setMaxLocals(int maxLocals) {
			this.maxLocals = maxLocals;
		}
		public void setCodeLength(int codeLength) {
			this.codeLength = codeLength;
		}
		public void setCode(byte[] code) {
			this.code = code;
		}
		public void setExcepetionTableLength(int excepetionTableLength) {
			this.excepetionTableLength = excepetionTableLength;
		}
		public void setException(byte[] exception) {
			this.exception = exception;
		}
		public void setAttributesCount(int attributesCount) {
			this.attributesCount = attributesCount;
		}
		public void setAttributes(AttributeInfo[] attributes) {
			this.attributes = attributes;
		}
    	
    }
    
    public static class LineNumAttribute extends AttributeInfo {
    	private int lineNumTableCount;
    	/*
    	 * {
    	 * 		u2 start_pc  字节码位置   
    	 *      u2 line_number   源代码位置
    	 * } []
    	 * */
    	private int lineNumTable[][];
		public int getLineNumTableCount() {
			return lineNumTableCount;
		}
		public int[][] getLineNumTable() {
			return lineNumTable;
		}
		public void setLineNumTableCount(int lineNumTableCount) {
			this.lineNumTableCount = lineNumTableCount;
		}
		public void setLineNumTable(int[][] lineNumTable) {
			this.lineNumTable = lineNumTable;
		}
    	
    }
    
    public static class LocalVarAttribute extends AttributeInfo {
    	private int localVarTableCount;
    	/*
    	 * {
    	 * 		u2 start_pc  字节码位置   
    	 *      u2 length   源代码位置
    	 *      u2 name_index 
    	 *      u2 desc_index
    	 *      u2 index
    	 * } []
    	 * */
    	private int localVarTable[][];
		public int getLocalVarTableCount() {
			return localVarTableCount;
		}
		public int[][] getLocalVarTable() {
			return localVarTable;
		}
		public void setLocalVarTableCount(int localVarTableCount) {
			this.localVarTableCount = localVarTableCount;
		}
		public void setLocalVarTable(int[][] localVarTable) {
			this.localVarTable = localVarTable;
		}
    }
    
    public void read(Bytes bytes, ConstantInfo[] cpInfo) {
    	accessFlags = bytes.readInt16();
    	nameIndex = bytes.readInt16();
    	descriptorIndex = bytes.readInt16();
    	attributesCount = bytes.readInt16();
    	attributes = new AttributeInfo[attributesCount];
    	
    	this.name = ((ConstantUtf8) cpInfo[nameIndex]).getValue();
    	this.desc = ((ConstantUtf8) cpInfo[descriptorIndex]).getValue();
    	
    	readAttributes(bytes, cpInfo, attributes);
    }

    public void write(Bytes bytes) {
    	bytes.writeInt16(accessFlags);
    	bytes.writeInt16(nameIndex);
    	bytes.writeInt16(descriptorIndex);
    	bytes.writeInt16(attributesCount);
    	
    	writeAttributes(bytes, attributes);
    }
    
	private void readAttributes(Bytes bytes, ConstantInfo[] cpInfo, AttributeInfo[] attrs) {
		for (int j = 0; j < attrs.length; j++) {
    		int nameIndex = bytes.readInt16();
    		String name = ((ConstantUtf8) cpInfo[nameIndex]).getValue();
    		if("Code".equals(name)) {
    			CodeAttribute attr = new CodeAttribute();
    			attr.setName(name);
    			attr.setNameIndex(nameIndex);
    			attr.setLength(bytes.readInt32());
    			attr.setMaxStack(bytes.readInt16());
    			attr.setMaxLocals(bytes.readInt16());
    			attr.setCodeLength(bytes.readInt32());
    			attr.setCode(bytes.read(attr.codeLength));
    			
    			attr.setExcepetionTableLength(bytes.readInt16());
    			// 2bytes startPc  2bytes endPc  2bytes handlerPc  2bytes catchType
    			attr.setException(bytes.read(attr.excepetionTableLength * 8));
    			
    			attr.setAttributesCount(bytes.readInt16());
    			attr.setAttributes(new AttributeInfo[attr.attributesCount]);
    			readAttributes(bytes, cpInfo, attr.attributes);
    			
    			attrs[j] = attr;
    		} else if("LineNumberTable".equals(name)) {
    			LineNumAttribute attr = new LineNumAttribute();
    			attr.setName(name);
    			attr.setNameIndex(nameIndex);
    			attr.setLength(bytes.readInt32());
    			attr.setLineNumTableCount(bytes.readInt16());
    			int[][] lineNumTable = new int[attr.lineNumTableCount][2];
    			attr.setLineNumTable(lineNumTable);
    			for(int i = 0; i < attr.lineNumTableCount; i++) {
    				lineNumTable[i][0] = bytes.readInt16();
    				lineNumTable[i][1] = bytes.readInt16();
    			}
    			attrs[j] = attr;
    		} else if("LocalVariableTable".equals(name)) {
    			LocalVarAttribute attr = new LocalVarAttribute();
    			attr.setName(name);
    			attr.setNameIndex(nameIndex);
    			attr.setLength(bytes.readInt32());
    			attr.setLocalVarTableCount(bytes.readInt16());
    			int[][] localVarTable = new int[attr.localVarTableCount][5];
    			attr.setLocalVarTable(localVarTable);
    			for(int i = 0; i < attr.localVarTableCount; i++) {
    				localVarTable[i][0] = bytes.readInt16();
    				localVarTable[i][1] = bytes.readInt16();
    				localVarTable[i][2] = bytes.readInt16();
    				localVarTable[i][3] = bytes.readInt16();
    				localVarTable[i][4] = bytes.readInt16();
    			}
    			attrs[j] = attr;
    		} else {
    			AttributeInfo attr = new AttributeInfo();
    			attr.setName(name);
    			attr.setNameIndex(nameIndex);
    			attr.setLength(bytes.readInt32());
    			attr.setInfo(bytes.read(attr.length));
    			attrs[j] = attr;
    		}
        }
	}
	
	private void writeAttributes(Bytes bytes, AttributeInfo[] attrs) {
		for (int j = 0; j < attrs.length; j++) {
			AttributeInfo attr = attrs[j];
			bytes.writeInt16(attr.nameIndex);
			bytes.writeInt32(attr.length);
			if(attr instanceof CodeAttribute) {
				CodeAttribute cattr = (CodeAttribute) attr;
				bytes.writeInt16(cattr.maxStack);
				bytes.writeInt16(cattr.maxLocals);
				
				bytes.writeInt32(cattr.codeLength);
				bytes.write(cattr.code);
				
				bytes.writeInt16(cattr.excepetionTableLength);
				bytes.write(cattr.exception);
				

				bytes.writeInt16(cattr.attributesCount);
				writeAttributes(bytes, cattr.attributes);
			} else if(attr instanceof LineNumAttribute) {
				LineNumAttribute lnattr = (LineNumAttribute) attr;
				bytes.writeInt16(lnattr.lineNumTableCount);
				for(int i = 0; i < lnattr.lineNumTableCount; i++) {
					bytes.writeInt16(lnattr.lineNumTable[i][0]);
					bytes.writeInt16(lnattr.lineNumTable[i][1]);
				}
			} else if(attr instanceof LocalVarAttribute) {
				LocalVarAttribute lvattr = (LocalVarAttribute) attr;
				bytes.writeInt16(lvattr.localVarTableCount);
				for(int i = 0; i < lvattr.localVarTableCount; i++) {
					bytes.writeInt16(lvattr.localVarTable[i][0]);
					bytes.writeInt16(lvattr.localVarTable[i][1]);
					bytes.writeInt16(lvattr.localVarTable[i][2]);
					bytes.writeInt16(lvattr.localVarTable[i][3]);
					bytes.writeInt16(lvattr.localVarTable[i][4]);
				}
			} else {
				bytes.write(attr.info);
			}
        }
	}
	
}
