package com.archer.tools.bytecode.constantpool;

import com.archer.net.Bytes;

public class ConstantDouble extends ConstantInfo {
	public ConstantDouble() {
		super(ConstantInfo.CONSTANT_Double);
	}
	private int highValue;
	private int lowValue;

    public int getHighValue() {
		return highValue;
	}
	public int getLowValue() {
		return lowValue;
	}
	public void setHighValue(int highValue) {
		this.highValue = highValue;
	}
	public void setLowValue(int lowValue) {
		this.lowValue = lowValue;
	}
	@Override
    public void read(Bytes bytes) {
        highValue = bytes.readInt32();
        lowValue = bytes.readInt32();
    }
    @Override
    public void write(Bytes bytes) {
    	bytes.writeInt32(highValue);
    	bytes.writeInt32(lowValue);
    }
}
