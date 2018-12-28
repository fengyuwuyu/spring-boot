package com.ll.spring.boot.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClasspathPackageScanner implements PackageScanner {
    private Logger logger = LoggerFactory.getLogger(ClasspathPackageScanner.class);
    private String basePackage;
    private ClassLoader cl;

    /**
     * 初始化
     * 
     * @param basePackage
     */
    public ClasspathPackageScanner(String basePackage) {
        this.basePackage = basePackage;
        this.cl = getClass().getClassLoader();
    }

    public ClasspathPackageScanner(String basePackage, ClassLoader cl) {
        this.basePackage = basePackage;
        this.cl = cl;
    }

    @Override
    public List<String> getFullyQualifiedClassNameList(EnumClassType classType) throws IOException {
        logger.info("开始扫描包{}下的所有类", basePackage);
        return filter(doScan(basePackage, new ArrayList<String>()), classType);
    }

    /**
     * 获取指定包下的所有字节码文件的全类名
     */
    public List<String> getFullyQualifiedClassNameList() throws IOException {
        return getFullyQualifiedClassNameList(EnumClassType.BOTH);
    }
    
    @SuppressWarnings("rawtypes")
    private List<String> filter(List<String> fullPathClassName, EnumClassType classType) {
        if (fullPathClassName == null || fullPathClassName.size() == 0 || classType ==null || classType == EnumClassType.BOTH) {
            return fullPathClassName;
        }
        
        return fullPathClassName.stream().filter((item) -> {
            Class clazz = null;
            try {
                clazz = Class.forName(item);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            switch (classType) {
            case CLASS:
                return !clazz.isEnum();
            case ENUM:
                return clazz.isEnum();
            default:
                return false;
            }
        }).collect(Collectors.toList());
        
    }

    /**
     * doScan函数
     * 
     * @param basePackage
     * @param nameList
     * @param isEnum 
     * @return
     * @throws IOException
     */
    private List<String> doScan(String basePackage, List<String> nameList) throws IOException {
        String splashPath = StringUtil.dotToSplash(basePackage);
        URL url = cl.getResource(splashPath); // file:/D:/WorkSpace/java/ScanTest/target/classes/com/scan
        String filePath = StringUtil.getRootPath(url);
        List<String> names = null; // contains the name of the class file. e.g., Apple.class will be stored as
                                   // "Apple"
        if (isJarFile(filePath)) {// 先判断是否是jar包，如果是jar包，通过JarInputStream产生的JarEntity去递归查询所有类
            if (logger.isTraceEnabled()) {
                logger.trace("{} 是一个JAR包", filePath);
            }
            names = readFromJarFile(filePath, splashPath);
        } else {
            if (logger.isTraceEnabled()) {
                logger.trace("{} 是一个目录", filePath);
            }
            names = readFromDirectory(filePath);
        }
        for (String name : names) {
            if (isClassFile(name)) {
                nameList.add(toFullyQualifiedName(name, basePackage));
            } else {
                doScan(basePackage + "." + name, nameList);
            }
        }
        if (logger.isDebugEnabled()) {
            for (String n : nameList) {
                logger.trace("找到{}", n);
            }
        }
        return nameList;
    }

    private String toFullyQualifiedName(String shortName, String basePackage) {
        StringBuilder sb = new StringBuilder(basePackage);
        sb.append('.');
        sb.append(StringUtil.trimExtension(shortName));
        // 打印出结果
        return sb.toString();
    }

    @SuppressWarnings("resource")
    private List<String> readFromJarFile(String jarPath, String splashedPackageName) throws IOException {
        if (logger.isTraceEnabled()) {
            logger.trace("从JAR包中读取类: {}", jarPath);
        }
        JarInputStream jarIn = new JarInputStream(new FileInputStream(jarPath));
        JarEntry entry = jarIn.getNextJarEntry();
        List<String> nameList = new ArrayList<String>();
        while (null != entry) {
            String name = entry.getName();
            if (name.startsWith(splashedPackageName) && isClassFile(name)) {
                nameList.add(name);
            }

            entry = jarIn.getNextJarEntry();
        }

        return nameList;
    }

    private List<String> readFromDirectory(String path) {
        File file = new File(path);
        String[] names = file.list();

        if (null == names) {
            return null;
        }

        return Arrays.asList(names);
    }

    private boolean isClassFile(String name) {
        return name.endsWith(".class");
    }

    private boolean isJarFile(String name) {
        return name.endsWith(".jar");
    }

}
