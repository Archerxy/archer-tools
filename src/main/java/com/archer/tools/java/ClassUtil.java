package com.archer.tools.java;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class ClassUtil {
    private static final char DOT = '.';
	public static final String CLASS_FILE_SUFFIX = ".class";
	
	private static Map<Class<?>, Field[]> fieldsCache = new ConcurrentHashMap<>(96);


	public static List<Class<?>> listAllClasses() {
		List<Class<?>> classes = new LinkedList<>();
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        if (classLoader instanceof URLClassLoader) {
            URL[] urls = ((URLClassLoader) classLoader).getURLs();
            for (URL url : urls) {
            	String path = url.getPath();
            	try {
                	if(new File(path).isDirectory()) {
                		classes.addAll(getClassesFromPath(path, null));
                	} else if(path.endsWith(".jar")) {
                		classes.addAll(getClassesFromJar(path));
                	}
            	} catch(Exception ignore) {}
            }
        }
        
        return classes;
	}

	public static List<Class<?>> getClassesFromPath(String path, String parentPkg) {
        List<Class<?>> classes = new ArrayList<>();
        File directory = new File(path);
        if(parentPkg == null) {
        	parentPkg = "";
        } else {
        	parentPkg += ".";
        }
        File[] files = directory.listFiles();
        if(files == null) {
        	return classes;
        }
        for (File file : files) {
            if (file.getName().endsWith(".class")) {
                String className = parentPkg + file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                } catch (ClassNotFoundException ignore) {}
            } else if(file.isDirectory()) {
            	
            	List<Class<?>> subClasses = getClassesFromPath(file.getAbsolutePath(), parentPkg + file.getName());
            	classes.addAll(subClasses);
            }
        }
        return classes;
    }
	
	public static List<Class<?>> getClassesFromJar(String jarFilePath) throws IOException {
    	List<Class<?>> classes = new LinkedList<>();
        try(JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    try {
                    	String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                    } catch (ClassNotFoundException ignore) {}
                }
            }
        }
        return classes;
    }
	
    public static List<Class<?>> findImplementsClass(Class<?> clazz) {
        List<Class<?>> collected = new LinkedList<>();
        List<Class<?>> classes = listAllClasses();
        for(Class<?> cls: classes) {
			if(clazz.isAssignableFrom(cls) && !cls.isInterface() && !Modifier.isAbstract(cls.getModifiers())) {
				collected.add(cls);
			}
        }
        return collected;
    }
    
    public static String getClassFileName(Class<?> clazz) {
		String className = clazz.getName();
		int lastDotIndex = className.lastIndexOf(DOT);
		return className.substring(lastDotIndex + 1) + CLASS_FILE_SUFFIX;
	}
 
    public static Object newInstance(Class<?> cls) {
		int paramCount = cls.isMemberClass() ? 1 : 0;
		Constructor<?>[] constructors = cls.getConstructors();
		for(Constructor<?> constructor : constructors) {
			if(constructor.getParameterCount() == paramCount) {
				Object[] params = new Object[paramCount];
				try {
					return constructor.newInstance(params);
				} catch (Exception e) {
					throw new RuntimeException("can not construct class '" +
							cls.getName() + "'", e);
				}
			}
		}
		throw new RuntimeException(
				"no arguments constructor is required with class '" 
				+ cls.getName() + "'"); 
	}
    
    public static <T> T newInstanceFromColums(Object src, Class<T> cls) 
    		throws IllegalArgumentException, IllegalAccessException {
    	Field[] fields = fieldsCache.getOrDefault(cls, null);
    	if(fields == null) {
    		fields = cls.getDeclaredFields();
    		fieldsCache.put(cls, fields);
    	}
    	
    	@SuppressWarnings("unchecked")
		T ins = (T) newInstance(cls);
    	
    	for(Field f: fields) {
    		Object srcF = null;
    		try {
    			Field srcColum = src.getClass().getDeclaredField(f.getName());
    			srcColum.setAccessible(true);
    			srcF = srcColum.get(src);
			} catch (Exception ignore) {
				continue ;
			}
    		if(f.getType().isAssignableFrom(srcF.getClass())) {
				f.set(ins, srcF);
    		}
    	}
    	return ins;
    }
}

