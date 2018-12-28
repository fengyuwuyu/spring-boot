package com.ll.spring.boot.core.util;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ll.spring.boot.core.consts.Consts;
import com.ll.spring.boot.core.util.entity.BrowserType;

public class DownLoadUtil {

    private static Logger log = LoggerFactory.getLogger(DownLoadUtil.class);

    /**
     * 判断当前程序运行的操作系统类型是否是Windows
     */
    public static boolean isWindowsOS() {
        boolean flag = false;
        java.util.Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        if (os.startsWith("win") || os.startsWith("Win")) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    public static void exportExcelAndDownload(String title, String[] headers, String[] fields, List<Map<String, Object>> dataset, 
            HttpServletResponse response, HttpServletRequest request, String tmpPath, String fileName) throws Exception {
        exportExcelAndDownload(title, headers, fields, dataset, response, request, tmpPath, fileName, false);
    }

    public static void exportExcelAndDownload(String title, String[] headers, String[] fields, List<Map<String, Object>> dataset, 
            HttpServletResponse response, HttpServletRequest request, String tmpPath, String fileName, boolean isOnline, String pattern) throws Exception {
        fileName = DateUtil.getDay() + "-" + fileName;
        File file = new File(tmpPath + File.separator + fileName);
        OutputStream out = new FileOutputStream(file);
        
        EETemplate<T> eeTemplate = new EETemplate<>();
        eeTemplate.callExportExcel(title, headers, fields, dataset, out, pattern);
        
        DownLoadUtil.download(file, response, request, isOnline, fileName);
    }

    public static void exportExcelAndDownload(String title, String[] headers, String[] fields, List<Map<String, Object>> dataset, 
            HttpServletResponse response, HttpServletRequest request, String tmpPath, String fileName, boolean isOnline) throws Exception {
        exportExcelAndDownload(title, headers, fields, dataset, response, request, tmpPath, fileName, isOnline, Consts.DATE_PATTERN);
    }

    /**
     * 文件下载功能
     * 
     * @param file
     * @param response
     * @param isOnline
     * @param fileName
     * @param fileType
     */
    public synchronized static void download(File file, HttpServletResponse response, HttpServletRequest request, boolean isOnline,
            String fileName) {
        int index = fileName.indexOf(Consts.UPLOAD_FILE_SEPERATOR);
        if (index != -1) {
            int lastIndex = fileName.lastIndexOf(".");
            fileName = fileName.substring(0, index) + fileName.substring(lastIndex);
        }
        BufferedInputStream br = null;
        OutputStream out = null;
        try {
            if (!file.exists()) {
                response.sendError(404, "File not found!");
                return;
            }
            br = new BufferedInputStream(new FileInputStream(file));
            byte[] buf = new byte[1024];
            int len = 0;
            response.reset();
            // 判断文件是否为图片
            Image image = ImageIO.read(file);
            if (image != null) {
                response.setContentType("image/*");// 不同类型的文件对应不同的MIME类型
            }

            if (isOnline) { // 在线打开方式
                URL u = new URL("file:///" + file.getAbsolutePath());
                response.setContentType(u.openConnection().getContentType());
                if (BrowserType.Firefox.equals(BrowserUtils.getBrowserType(request))
                        || BrowserType.Chrome.equals(BrowserUtils.getBrowserType(request))) {
                    response.setHeader("Content-Disposition",
                            "inline; filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
                } else {
                    response.setHeader("Content-Disposition",
                            "inline; filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
                }

            } else {
                response.setContentType("application/vnd.ms-excel;charset=gb2312");
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
            }
            out = response.getOutputStream();
            while ((len = br.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            log.error(String.format("下载文件失败, filename = %s, isOnline = %s", fileName, isOnline), e);
        } finally {
            try {
                br.close();
                out.close();
            } catch (IOException e) {
                log.error(String.format("下载文件失败, filename = %s, isOnline = %s", fileName, isOnline), e);
            }
        }
    }
}
