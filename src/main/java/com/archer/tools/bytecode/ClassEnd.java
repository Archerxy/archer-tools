package com.archer.tools.bytecode;

import com.archer.net.Bytes;

public class ClassEnd {
	private int sourceLen;
	private int sourceIndex;
	private int sourceNop;
	private int sourceNameIndex;
	private int innerClassRawNameIndex;
	
	private int innerClassLength;
	private int innerClassCount;
	private int[][] innerClassTable;  //innerClassLength = innerClassCount * 8bytes,  {index:2bytes, {0,0,0,0,0,0}:6bytes}
	
	public ClassEnd() {}
	
	
	public int getSourceLen() {
		return sourceLen;
	}


	public int getSourceIndex() {
		return sourceIndex;
	}


	public int getSourceNop() {
		return sourceNop;
	}


	public int getSourceNameIndex() {
		return sourceNameIndex;
	}


	public void setSourceLen(int sourceLen) {
		this.sourceLen = sourceLen;
	}


	public void setSourceIndex(int sourceIndex) {
		this.sourceIndex = sourceIndex;
	}


	public void setSourceNop(int sourceNop) {
		this.sourceNop = sourceNop;
	}


	public void setSourceNameIndex(int sourceNameIndex) {
		this.sourceNameIndex = sourceNameIndex;
	}

	public int getInnerClassRawNameIndex() {
		return innerClassRawNameIndex;
	}


	public void setInnerClassRawNameIndex(int innerClassRawNameIndex) {
		this.innerClassRawNameIndex = innerClassRawNameIndex;
	}


	public int getInnerClassLength() {
		return innerClassLength;
	}


	public int getInnerClassCount() {
		return innerClassCount;
	}


	public int[][] getInnerClassTable() {
		return innerClassTable;
	}


	public void setInnerClassLength(int innerClassLength) {
		this.innerClassLength = innerClassLength;
	}


	public void setInnerClassCount(int innerClassCount) {
		this.innerClassCount = innerClassCount;
	}


	public void setInnerClassTable(int[][] innerClassTable) {
		this.innerClassTable = innerClassTable;
	}

	public void read(Bytes bytes) {
		sourceLen = bytes.readInt16();
		sourceIndex = bytes.readInt16();
		sourceNop = bytes.readInt32();
		
		sourceNameIndex = bytes.readInt16();
		if(bytes.available() > 0) {
			innerClassRawNameIndex = bytes.readInt16();
			innerClassLength = bytes.readInt32();
			innerClassCount = bytes.readInt16();
			innerClassTable = new int[innerClassCount][3];
			for(int i = 0; i < innerClassCount; i++) {
				innerClassTable[i][0] = bytes.readInt16();
				innerClassTable[i][1] = bytes.readInt16();
				innerClassTable[i][2] = bytes.readInt32();
			}
		}
	}
	
	public void write(Bytes bytes) {
		bytes.writeInt16(sourceLen);
		bytes.writeInt16(sourceIndex);
		bytes.writeInt32(sourceNop);
		bytes.writeInt16(sourceNameIndex);
		if(innerClassRawNameIndex > 0) {
			bytes.writeInt16(innerClassRawNameIndex);
			bytes.writeInt32(innerClassLength);
			bytes.writeInt16(innerClassCount);
			for(int i = 0; i < innerClassCount; i++) {
				bytes.writeInt16(innerClassTable[i][0]);
				bytes.writeInt16(0);
				bytes.writeInt32(0);
			}
		}
	}
	
}
