package com.archer.tools.bytecode.constantpool;

import com.archer.net.Bytes;

public class ConstantPool {
	private int constantPoolCount;
	private ConstantInfo[] cpInfo;

    public ConstantPool() {}

    public ConstantPool(int constantPoolCount, ConstantInfo[] cpInfo) {
		this.constantPoolCount = constantPoolCount;
		this.cpInfo = cpInfo;
	}

	public int getConstantPoolCount() {
		return constantPoolCount;
	}

	public ConstantInfo[] getCpInfo() {
		return cpInfo;
	}
	public void setConstantPoolCount(int constantPoolCount) {
		this.constantPoolCount = constantPoolCount;
	}

	public void setCpInfo(ConstantInfo[] cpInfo) {
		this.cpInfo = cpInfo;
	}



	public void read(Bytes bytes) {
        constantPoolCount = bytes.readInt16();
        cpInfo = new ConstantInfo[constantPoolCount];
        for (int i = 1; i < constantPoolCount; i++) {
            int tag = (int) bytes.readInt8();
            ConstantInfo constantInfo = ConstantInfo.getConstantInfo(tag);
            constantInfo.read(bytes);
            cpInfo[i] = constantInfo;
            if (tag == ConstantInfo.CONSTANT_Double || tag == ConstantInfo.CONSTANT_Long) {
                i++;
            }
        }
    }
    
    public void write(Bytes bytes) {
    	bytes.writeInt16(constantPoolCount);
        for (int i = 1; i < constantPoolCount; i++) {
            ConstantInfo constantInfo = cpInfo[i];
            int tag = constantInfo.tag;
        	bytes.writeInt8(constantInfo.tag);
            constantInfo.write(bytes);
            if (tag == ConstantInfo.CONSTANT_Double || tag == ConstantInfo.CONSTANT_Long) {
                i++;
            }
        }
    }

}
