package com.ll.spring.boot.core.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {

    private static Logger log = LoggerFactory.getLogger(FileUtil.class);
    
    /**
     * 获取byte数组的md5 hash
     * @param datas
     * @return
     */
    public static String getMd5ByFile(byte[] datas){
		return DigestUtils.md5Hex(datas);
	}
    
    public static boolean deleteFile(File file) {
        if (file != null && file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * NIO way
     */
    public static byte[] toByteArray(String filename) {

        File f = new File(filename);
        if (!f.exists()) {
            log.error("文件未找到！" + filename);
            throw new RuntimeException("文件未找到！" + filename);
        }
        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
            return byteBuffer.array();
        } catch (IOException e) {
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
            }
            try {
                fs.close();
            } catch (IOException e) {
            }
        }
        return null;
    }

    /**
     * 删除目录
     *
     * @author 
     * @Date 2017/10/30 下午4:15
     */
    public static boolean deleteDir(File dir) {
    	return deleteDir(dir, false);
    }

    /**
     * 删除目录内文件，保留目录
     *
     * @author 
     * @Date 2017/10/30 下午4:15
     */
    public static boolean deleteDir(File dir, boolean keepRootDir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        if (!keepRootDir) {
        	return dir.delete();
        }
        return true;
    }
    
    public static String getAbsolutePath(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                return null;
            }
            return file.getCanonicalPath();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 返回一个相对于relatedPath的路径 如果path为绝对路径，那么使用path
     * 如果path为相对路径，那么返回相对于releatedPath的绝对路径 *
     */
    public static String getAbsolutePathRelated(String relatedPath, String path) {
        if (new File(path).isAbsolute())
            return path;

        File relatedFile = new File(relatedPath);
        if (relatedFile.isDirectory()) {
            try {
                return new File(relatedFile + "/" + path).getCanonicalPath();
            } catch (IOException e) {
                return null;
            }
        } else {
            try {
                return new File(relatedFile.getParentFile() + "/" + path).getCanonicalPath();
            } catch (IOException e) {
                return null;
            }
        }
    }

    public static Properties loadProperties(String path) {
        Properties prop = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(path));
            prop = new Properties();
            prop.load(inputStream);
        } catch (IOException e) {
            IOException ex = new IOException("加载" + path + "时，文件未找到", e);
            ex.printStackTrace();
            System.exit(0);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return prop;
    }

    /**
     * 
     * 描述:这个方法是获取一个文件的文件名，不带格式
     *
     * @param fileName
     * @return
     * @author Terry
     * @time 2016年7月22日-下午7:32:37
     */
    public static String getFileName(File file) {
        String fileName = file.getName();
        String[] names = fileName.split("\\.");
        return names[0];
    }

    /**
     * 
     * 描述:将文件的内容作为String 读出
     *
     * @param pathFile
     * @return
     * @throws FileNotFoundException
     * @author LaoWang
     * @throws UnsupportedEncodingException
     * @time 2016年8月17日-下午3:40:41
     */
    public static String getFileText(String pathFile) throws FileNotFoundException, UnsupportedEncodingException {

        InputStream is = new FileInputStream(pathFile);
        String fileContext = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String line;
        try {
            line = reader.readLine();
            while (line != null) { // 如果 line 为空说明读完了
                fileContext += line;
                fileContext += '\n';
                line = reader.readLine(); // 读取下一行
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            return "";
        } // 读取第一行

        return fileContext;
    }

    /**
     * 
     * 描述:写入text到文件。如果文件存在，之前的内容将被替换。
     *
     * @param pathFile
     * @param text
     * @author LaoWang
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     * @time 2016年8月17日-下午3:42:07
     */
    public static void setFileText(String pathFile, String text)
            throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(pathFile, "UTF-8");
        writer.println(text);
        writer.close();
    }

    /**
     * 
     * 描述：向一个文件中写入字节，它每次写入的时候都会覆盖原来的数据，生成一个新的文件。
     * 
     * @param fileBytes
     *            要写入的内容
     * @param fileName
     *            要写入的文件名，包括扩展名
     * @param filePath
     *            要写入的文件路径，如果这个路径不存在，会新建一个。
     * 
     *            2017年2月18日 下午5:38:35
     */
    public static void createFile(byte[] fileBytes, String fileName, String filePath) {

        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "/" + fileName);

            ByteArrayInputStream bin = new ByteArrayInputStream(fileBytes);
            FileUtils.copyInputStreamToFile(bin, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * 描述：将一个文件转化为byte数组
     * 
     * @param path
     * @return
     * 
     *      2017年2月20日 上午10:19:15
     */
    public static byte[] fileToBytes(String path) {
        File file = new File(path);
        FileInputStream fin = null;
        ByteArrayOutputStream bos = null;
        try {
            fin = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] cache = new byte[1024];
            int len = -1;
            while ((len = fin.read(cache)) != -1) {
                bos.write(cache, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 根据路径创建目录
     * 
     * @param pathname
     */
    public static void createPath(String pathname) {
        if (StringUtil.isNullEmpty(pathname)) {
            return;
        }
        File path = new File(pathname);
        if (!path.exists()) {
            path.mkdirs();
        }
    }

    public static void createFile(String filename) throws IOException {
        if (StringUtil.isNullEmpty(filename)) {
            return;
        }
        String path = filename.substring(0, filename.lastIndexOf(File.separator));
        createPath(path);
        File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
    }
    
    public static void search(String str, String file) throws IOException {
    	search(str, new File(file));
    }
    
    public static void search(String str, File file) throws IOException {
    	if (!file.exists()) {
    		log.warn("not exist file, filename = " + file.getName());
    		return;
    	}
    	try (FileInputStream in = new FileInputStream(file); InputStreamReader inReader = new InputStreamReader(in, Charset.forName("utf-8")); BufferedReader reader = new BufferedReader(inReader);){
    		reader.lines().forEach((line) -> {
    			if (line.indexOf(str) != -1) {
        			System.out.println(line);
        		}
    		});
		} catch (Exception e) {
			log.error("read file fail.", e);
		}
    }
    
    public static void main(String[] args) throws IOException {
		try (
				FileInputStream in = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\sqlserver\\a.txt")); 
				InputStreamReader inReader = new InputStreamReader(in); 
				BufferedReader reader = new BufferedReader(inReader);
			) {
			Set<String> set = new HashSet<>();
			reader.lines().forEach((line) -> {
				if (set.contains(line)) {
					System.out.println(line);
				} else {
					set.add(line);
				}
    		});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public static List<File> scanPath(String path, FilenameFilter filter) {
    	File file = new File(path);
    	if (!file.exists()) {
    		return Collections.emptyList();
    	}
    	List<File> result = new ArrayList<>();
    	scanPath(file, result, filter);
    	return result;
    }
    
    private static void scanPath(File dir, List<File> list, FilenameFilter filter) {
    	if (dir.exists()) {
    		if (dir.isFile()) {
    			if (filter.accept(dir, dir.getName())) {
    				list.add(dir);
    			}
    		} else {
    			for (File f : dir.listFiles()) {
    				scanPath(f, list, filter);
    			}
    		}
    	}
    }
    
    public static String joinFilePath(String... path) {
        String separator = File.separator;
        StringBuilder sb = new StringBuilder();
        for (String string : path) {
            sb.append(string + separator);
        }
        return sb.substring(0, sb.length() - separator.length());
    }

    public static byte[] readRaw(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        stream.read(data);
        stream.close();
        return data;
    }
}