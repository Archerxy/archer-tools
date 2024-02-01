package com.archer.tools.java;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.archer.tools.util.PathUtil;

/**
 * @author xuyi
 */
public class ClassUtil {

    private static final String JAR = ".jar";
    private static final String CLASS = ".class";
    private static final char DOT = '.';

    public static List<Class<?>> findImplementsClass(Class<?> clazz) {
        List<Class<?>> collected = new LinkedList<>();
        String root = PathUtil.getCurrentWorkDir();
        recursePath(new File(root), clazz, collected);
        return collected;
    }

    @SuppressWarnings("resource")
	private static void recursePath(File path, Class<?> clazz, List<Class<?>> classList) {
        if(path.isDirectory()) {
            File[] subPaths = path.listFiles();
            if(subPaths == null) {
                return ;
            }
            for(File subPath: subPaths) {
                recursePath(subPath, clazz, classList);
            }
        }
        if(path.getName().endsWith(JAR)) {
            try {
                JarFile jar = new JarFile(path.getAbsolutePath());
                Enumeration<JarEntry> entries = jar.entries();
                while(entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if(entry.getName().endsWith(CLASS)) {
                        collectClass(entry.getName(), clazz, classList);
                    }
                }
            } catch (IOException ignore) {}
        }
        if(path.getName().endsWith(CLASS)) {
            collectClass(path.getAbsolutePath(), clazz, classList);
        }
    }

    private static void collectClass(String className, Class<?> clazz, List<Class<?>> classList) {
        String classPath = PathUtil.getClassPath();
        if(className.startsWith(classPath)) {
            className = className.replace(classPath, "");
        }
        className = className.replace(File.separatorChar, DOT);
        className = className.replace(CLASS, "");
        try {
            Class<?> implClass = Class.forName(className);
            if(clazz.isAssignableFrom(implClass)
                    && !implClass.isInterface()
                    && !Modifier.isAbstract(implClass.getModifiers())) {
                classList.add(implClass);
            }
        } catch (ClassNotFoundException ignore) {}
    }
}

