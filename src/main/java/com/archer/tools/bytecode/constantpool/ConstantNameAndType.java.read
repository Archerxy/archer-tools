package com.archer.tools.bytecode.constantpool;

import com.archer.net.Bytes;

public class ConstantNameAndType extends ConstantInfo {
	public ConstantNameAndType() {
		super(ConstantInfo.CONSTANT_NameAndType);
	}
	private int nameIndex;
	private int descIndex;

    public int getNameIndex() {
		return nameIndex;
	}
	public int getDescIndex() {
		return descIndex;
	}
	public void setNameIndex(int nameIndex) {
		this.nameIndex = nameIndex;
	}
	public void setDescIndex(int descIndex) {
		this.descIndex = descIndex;
	}
	@Override
    public void read(Bytes bytes) {
        nameIndex = bytes.readInt16();
        descIndex = bytes.readInt16();
    }
    @Override
    public void write(Bytes bytes) {
    	bytes.writeInt16(nameIndex);
    	bytes.writeInt16(descIndex);
    }
}
