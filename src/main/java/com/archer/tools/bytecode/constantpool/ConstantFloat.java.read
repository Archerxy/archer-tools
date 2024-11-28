package com.archer.tools.bytecode.constantpool;

import com.archer.net.Bytes;

public class ConstantFloat extends ConstantInfo {
	public ConstantFloat() {
		super(ConstantInfo.CONSTANT_Float);
	}

	private int value;

    public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
    public void read(Bytes bytes) {
        value = bytes.readInt32();
    }

	@Override
	public void write(Bytes bytes) {
		bytes.writeInt32(value);
	}
}
