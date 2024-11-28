package com.archer.tools.bytecode.constantpool;

import java.nio.charset.StandardCharsets;

import com.archer.net.Bytes;


public class ConstantUtf8 extends ConstantInfo {
	public ConstantUtf8() {
		super(ConstantInfo.CONSTANT_Utf8);
	}
	private String value;

    public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
    public void read(Bytes bytes) {
        int length = bytes.readInt16();
        value = new String(bytes.read(length), StandardCharsets.UTF_8);
    }
    @Override
    public void write(Bytes bytes) {
    	bytes.writeInt16(value.length());
    	bytes.write(value.getBytes(StandardCharsets.UTF_8));
    }
}
