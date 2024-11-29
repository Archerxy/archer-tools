package com.archer.tools.bytecode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.archer.net.Bytes;
import com.archer.tools.bytecode.constantpool.ConstantClass;
import com.archer.tools.bytecode.constantpool.ConstantInfo;
import com.archer.tools.bytecode.constantpool.ConstantPool;
import com.archer.tools.bytecode.constantpool.ConstantUtf8;


public class ClassBytecode {
	
	private static ClassLoader loader;
	private static Method defineClass;
	
	static {
		//defineClass(String name, byte[] b, int off, int len)
		loader = Thread.currentThread().getContextClassLoader();
		try {
			defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
			defineClass.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int magic;
    private int minorVersion;
    private int majorVersion;
    private ConstantPool constantPool;
    private int accessFlag;
    private int classIndex;
    private int superIndex;
    private int interfaceCount;
    private int[] interfaceIndexArr;
    
    private int fieldCount;
    private MemberInfo[] fields;
    private int methodCount;
    private MemberInfo[] methods;
    
    private ClassEnd classEnd;
    
    
    
    private String className;
    private String superClass;
    private String[] interfaces;
    
	public ClassBytecode() {}
	
	public int getMagic() {
		return magic;
	}
	public int getMinorVersion() {
		return minorVersion;
	}
	public int getMajorVersion() {
		return majorVersion;
	}
	public ConstantPool getConstantPool() {
		return constantPool;
	}
	public int getAccessFlag() {
		return accessFlag;
	}
	public String getClassName() {
		return className;
	}
	public String getSuperClass() {
		return superClass;
	}
	public int getInterfaceCount() {
		return interfaceCount;
	}
	public String[] getInterfaces() {
		return interfaces;
	}
	public int getFieldCount() {
		return fieldCount;
	}
	public MemberInfo[] getFields() {
		return fields;
	}
	public int getMethodCount() {
		return methodCount;
	}
	public MemberInfo[] getMethods() {
		return methods;
	}
	
	
	public int getClassIndex() {
		return classIndex;
	}

	public int getSuperIndex() {
		return superIndex;
	}

	public int[] getInterfaceIndexArr() {
		return interfaceIndexArr;
	}

	public ClassEnd getClassEnd() {
		return classEnd;
	}

	public void setMagic(int magic) {
		this.magic = magic;
	}

	public void setMinorVersion(int minorVersion) {
		this.minorVersion = minorVersion;
	}

	public void setMajorVersion(int majorVersion) {
		this.majorVersion = majorVersion;
	}

	public void setConstantPool(ConstantPool constantPool) {
		this.constantPool = constantPool;
	}

	public void setAccessFlag(int accessFlag) {
		this.accessFlag = accessFlag;
	}

	public void setClassIndex(int classIndex) {
		this.classIndex = classIndex;
	}

	public void setSuperIndex(int superIndex) {
		this.superIndex = superIndex;
	}

	public void setInterfaceCount(int interfaceCount) {
		this.interfaceCount = interfaceCount;
	}

	public void setInterfaceIndexArr(int[] interfaceIndexArr) {
		this.interfaceIndexArr = interfaceIndexArr;
	}

	public void setFieldCount(int fieldCount) {
		this.fieldCount = fieldCount;
	}

	public void setFields(MemberInfo[] fields) {
		this.fields = fields;
	}

	public void setMethodCount(int methodCount) {
		this.methodCount = methodCount;
	}

	public void setMethods(MemberInfo[] methods) {
		this.methods = methods;
	}

	public void setClassEnd(ClassEnd classEnd) {
		this.classEnd = classEnd;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setSuperClass(String superClass) {
		this.superClass = superClass;
	}

	public void setInterfaces(String[] interfaces) {
		this.interfaces = interfaces;
	}

	public void decodeClassBytes(Bytes bytes) {
    	this.magic = bytes.readInt32();
    	this.minorVersion = bytes.readInt16();
    	this.majorVersion = bytes.readInt16();
    	this.constantPool = new ConstantPool();
        constantPool.read(bytes);
        
        ConstantInfo[] cpInfo = constantPool.getCpInfo();
        
        //获取类信息
        this.accessFlag = bytes.readInt16();
        this.classIndex = bytes.readInt16();
        ConstantClass clazz = (ConstantClass) cpInfo[classIndex];
        ConstantUtf8 className = (ConstantUtf8) cpInfo[clazz.getNameIndex()];
        this.className = className.getValue();
        
        //获取父类信息
        this.superIndex = bytes.readInt16();
        ConstantClass superClazz = (ConstantClass)cpInfo[superIndex];
        ConstantUtf8 superclassName = (ConstantUtf8)cpInfo[superClazz.getNameIndex()];
        this.superClass = superclassName.getValue();
    	
        //获取接口信息
        this.interfaceCount = bytes.readInt16();
        this.interfaceIndexArr = new int[interfaceCount];
        String[] interfaceArr = new String[interfaceCount];
        for (int i = 0; i < interfaceCount; i++) {
            int interfaceIndex = bytes.readInt16();
            interfaceIndexArr[i] = interfaceIndex;
            ConstantClass interfaceClazz = (ConstantClass)cpInfo[interfaceIndex];
            ConstantUtf8 interfaceName = (ConstantUtf8)cpInfo[interfaceClazz.getNameIndex()];
            interfaceArr[i] = interfaceName.getValue();
        }
        this.interfaces = interfaceArr;

        //获取字段信息
        this.fieldCount = bytes.readInt16();
        MemberInfo[] fields = new MemberInfo[fieldCount];
        for (int i = 0; i < fieldCount; i++) {
        	fields[i] = new MemberInfo();
        	fields[i].read(bytes, cpInfo);
        }
        this.fields = fields;

        //获取方法信息
        this.methodCount = bytes.readInt16();
        MemberInfo[] methods = new MemberInfo[methodCount];
        for (int i = 0; i < methodCount; i++) {
        	methods[i] = new MemberInfo();
        	methods[i].read(bytes, cpInfo);
        }
        this.methods = methods;
        
        this.classEnd = new ClassEnd();
        this.classEnd.read(bytes);
    }
	
	
	public Bytes encodeClassBytes() {
		Bytes opcode = new Bytes();
		//写入头部
		opcode.writeInt32(magic);
		opcode.writeInt16(minorVersion);
		opcode.writeInt16(majorVersion);
		
		//写入常量池
		this.constantPool.write(opcode);
		
		
		//写入类信息
		opcode.writeInt16(accessFlag);
		opcode.writeInt16(classIndex);

		//写入父类信息
		opcode.writeInt16(superIndex);
		
		//写入接口信息
		opcode.writeInt16(interfaceCount);
		for(int i = 0; i < interfaceCount; i++) {
			opcode.writeInt16(interfaceIndexArr[i]);
		}

		//写入获取字段信息
		opcode.writeInt16(fieldCount);
		for (int i = 0; i < fieldCount; i++) {
			fields[i].write(opcode);
	    }

		//写入获取方法信息
		opcode.writeInt16(methodCount);
		for (int i = 0; i < methodCount; i++) {
			methods[i].write(opcode);
	    }
		
		classEnd.write(opcode);
		
		return opcode;
	}
	
	
	public Class<?> loadSelfClass() {
		Bytes codeBs = encodeClassBytes();
		try {
			return (Class<?>) defineClass.invoke(loader, className, codeBs.array(), 0, codeBs.available());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
