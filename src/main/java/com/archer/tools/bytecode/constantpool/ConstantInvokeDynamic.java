package com.archer.tools.bytecode.constantpool;

import com.archer.net.Bytes;

public class ConstantInvokeDynamic extends ConstantInfo {
	public ConstantInvokeDynamic() {
		super(ConstantInfo.CONSTANT_InvokeDynamic);
	}
	private int bootstrapMethodAttrIndex;
	private int nameAndTypeIndex;

    public int getBootstrapMethodAttrIndex() {
		return bootstrapMethodAttrIndex;
	}
	public int getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}
	public void setBootstrapMethodAttrIndex(int bootstrapMethodAttrIndex) {
		this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
	}
	public void setNameAndTypeIndex(int nameAndTypeIndex) {
		this.nameAndTypeIndex = nameAndTypeIndex;
	}
	@Override
    public void read(Bytes bytes) {
        bootstrapMethodAttrIndex = bytes.readInt16();
        nameAndTypeIndex = bytes.readInt16();
    }
    @Override
    public void write(Bytes bytes) {
    	bytes.writeInt32(bootstrapMethodAttrIndex);
    	bytes.writeInt32(nameAndTypeIndex);
    }
}
