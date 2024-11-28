package com.archer.tools.bytecode.constantpool;

import com.archer.net.Bytes;

public class ConstantInteger extends ConstantInfo {
	public ConstantInteger() {
		super(ConstantInfo.CONSTANT_Integer);
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
