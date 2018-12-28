package com.ll.spring.boot.core.util;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
  
  
/**
 * doc docx格式转换 
 */
public class DocConverter {
    private static final int environment = 1;// 环境 1：Windows 2：Linux
    private String fileString;// (只涉及PDF2swf路径问题)
    private String outputPath = "";// 输入路径 ，如果不设置就输出在默认 的位置
    private String fileName;
    private File pdfFile;
    private File swfFile;
    private File docFile;
    private Logger log = LoggerFactory.getLogger(getClass());
  
    public DocConverter(String fileString, String swfOutputFile, String fileName) {
        ini(fileString, swfOutputFile, fileName);
    }
  
    /**
     *  * 初始化
     *
     * @param fileString
     *          
     */
    private void ini(String fileString, String swfOutputFile, String fileName) {
        this.fileString = fileString;
        if (StringUtil.isNullEmpty(swfOutputFile)) {
        	swfOutputFile = fileString.substring(0, fileString.lastIndexOf("/"));
        }
        docFile = new File(fileString);
        pdfFile = new File(swfOutputFile + File.separator + fileName+ ".pdf");
        swfFile = new File(swfOutputFile + File.separator + fileName+ ".swf");
    }
  
    /**
     *  转为PDF
     *
     * @param file
     *      
     */
    public void doc2pdf() throws Exception {
        if (docFile.exists()) {
            if (!pdfFile.exists()) {
                OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
                try {
                    connection.connect();
                    DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
                    converter.convert(docFile, pdfFile);
                    // close the connection
                    connection.disconnect();
                    log.debug("****pdf转换成功，PDF输出： "+ pdfFile.getPath() + "****");
                } catch (java.net.ConnectException e) {
                    e.printStackTrace();
                    log.debug("****swf转换器异常，openoffice 服务未启动！****");
                    throw e;
                } catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {
                    e.printStackTrace();
                    log.debug("****swf转换器异常，读取转换文件 失败****");
                    throw e;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            } else {
                log.debug("****已经转换为pdf，不需要再进行转化 ****");
            }
        } else {
            log.debug("****swf转换器异常，需要转换的文档不存在， 无法转换****");
        }
    }
  
    /** * 转换成 swf */
    @SuppressWarnings("unused")
    private void pdf2swf() throws Exception {
        Runtime r = Runtime.getRuntime();
        if (swfFile.exists()) {
        	swfFile.delete();
        } 
        if (pdfFile.exists()) {
            if (environment == 1) {// windows环境处理
                try {
                    Process p = r.exec("D:/Program Files (x86)/SWFTools/pdf2swf.exe "+ pdfFile.getPath() + " -o "+ swfFile.getPath() + " -T 9");
                     System.out.print(loadStream(p.getInputStream()));
                     System.err.print(loadStream(p.getErrorStream()));
                     System.out.print(loadStream(p.getInputStream()));
                     System.err.println("****swf转换成功，文件输出： "+swfFile.getPath() + "****");
                    if (pdfFile.exists()){
//                        pdfFile.delete();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            } else if (environment == 2) {// linux环境处理
                try {
                    Process p = r.exec("pdf2swf" + pdfFile.getPath()+ " -o " + swfFile.getPath() + " -T 9");
                     System.out.print(loadStream(p.getInputStream()));
                     System.err.print(loadStream(p.getErrorStream()));
                     System.err.println("****swf转换成功，文件输出： "+ swfFile.getPath() + "****");
                    if (pdfFile.exists()) {
                        pdfFile.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        } else {
            log.debug("****pdf不存在,无法转换****");
        }
    }
  
    String loadStream(InputStream in) throws IOException {
        int ptr = 0;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();
        while ((ptr = in.read()) != -1) {
            buffer.append((char) ptr);
        }
        return buffer.toString();
    }
  
    /**
     * * 转换主方法
     */
    @SuppressWarnings("unused")
    public boolean conver() {
        if (swfFile.exists()) {
            log.debug("****swf转换器开始工作，该文件已经转换为 swf****");
            return true;
        }
        if (environment == 1) {
            log.debug("****swf转换器开始工作，当前设置运行环境 windows****");
        } else {
            log.debug("****swf转换器开始工作，当前设置运行环境 linux****");
        }
        try {
            doc2pdf();
            pdf2swf();
        } catch (Exception e) {
              e.printStackTrace();
              return false;
        }
        log.debug("文件存在吗？"+swfFile);
        if (swfFile.exists()) {
            log.debug("存在");
            return true;
        } else {
            log.debug("不存在");
            return false;
        }
    }
  
    /**
     *返回文件路径      
     * @param     
     */
    public String getswfPath(){
        if (this.swfFile.exists()){
            String tempString = swfFile.getPath();
            tempString = tempString.replaceAll("\\\\", "/");
            log.debug("最后文件路径为"+tempString);
            return tempString;
        } else {
            return "文件不存在";
        }
    }
    
    public static void main(String[] args) throws Exception {
		DocConverter converter = new DocConverter("D:/bdtd/upload/export/员工就餐汇总-2018年10月15日 16点18分25秒.xls", "D:/bdtd/upload/swf", System.currentTimeMillis() + "");
		converter.doc2pdf();
	}
}