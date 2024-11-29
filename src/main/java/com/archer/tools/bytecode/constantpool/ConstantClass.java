package com.archer.tools.bytecode.constantpool;

import com.archer.net.Bytes;

public class ConstantClass extends ConstantInfo {
    public ConstantClass() {
		super(ConstantInfo.CONSTANT_Class);
	}
    
	private int nameIndex;

    public int getNameIndex() {
		return nameIndex;
	}

	public void setNameIndex(int nameIndex) {
		this.nameIndex = nameIndex;
	}

	@Override
    public void read(Bytes bytes) {
        nameIndex =  bytes.readInt16();
    }

	@Override
	public void write(Bytes bytes) {
		bytes.writeInt16(nameIndex);
	}
}
