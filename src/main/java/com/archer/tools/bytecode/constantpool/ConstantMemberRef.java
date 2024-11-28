package com.archer.tools.bytecode.constantpool;

import com.archer.net.Bytes;

public class ConstantMemberRef extends ConstantInfo{
    public ConstantMemberRef(int tag) {
		super(tag);
	}
    private int classIndex;
    private int nameAndTypeIndex;

    public int getClassIndex() {
		return classIndex;
	}
	public int getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}
	public void setClassIndex(int classIndex) {
		this.classIndex = classIndex;
	}
	public void setNameAndTypeIndex(int nameAndTypeIndex) {
		this.nameAndTypeIndex = nameAndTypeIndex;
	}
	@Override
    public void read(Bytes bytes) {
        classIndex = bytes.readInt16();
        nameAndTypeIndex = bytes.readInt16();
    }
    @Override
    public void write(Bytes bytes) {
    	bytes.writeInt16(classIndex);
    	bytes.writeInt16(nameAndTypeIndex);
    }
}
