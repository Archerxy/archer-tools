package com.archer.tools.bytecode.constantpool;

import com.archer.net.Bytes;

public class ConstantMethodType extends ConstantInfo {
	public ConstantMethodType() {
		super(ConstantInfo.CONSTANT_MethodType);
	}
	private int descType;

    public int getDescType() {
		return descType;
	}
	public void setDescType(int descType) {
		this.descType = descType;
	}
	@Override
    public void read(Bytes bytes) {
        descType= bytes.readInt16();
    }    
    @Override
    public void write(Bytes bytes) {
    	bytes.writeInt16(descType);
    }
}
