package com.archer.tools.bytecode.constantpool;

import com.archer.net.Bytes;

public class ConstantMethodHandle extends ConstantInfo {
	public ConstantMethodHandle() {
		super(ConstantInfo.CONSTANT_MethodHandle);
	}
	private int referenceKind;
	private int referenceIndex;

    public int getReferenceKind() {
		return referenceKind;
	}
	public int getReferenceIndex() {
		return referenceIndex;
	}
	public void setReferenceKind(int referenceKind) {
		this.referenceKind = referenceKind;
	}
	public void setReferenceIndex(int referenceIndex) {
		this.referenceIndex = referenceIndex;
	}
	@Override
    public void read(Bytes bytes) {
        referenceKind = bytes.readInt8();
        referenceIndex = bytes.readInt16();
    }
    @Override
    public void write(Bytes bytes) {
    	bytes.writeInt8(referenceKind);
    	bytes.writeInt16(referenceIndex);
    }
}
