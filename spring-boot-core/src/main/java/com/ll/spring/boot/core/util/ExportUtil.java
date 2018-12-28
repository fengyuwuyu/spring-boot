package com.ll.spring.boot.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ll.spring.boot.core.util.entity.ExcelEntity;

public class ExportUtil {
	
	private static final String SUFFIX_PATH = "/export";
	private static final String PDF_SUFFIX_PATH = "/swf";

	private static ExportUtil instance = new ExportUtil();

	private ExportUtil() {
	}

	public static ExportUtil getInstance() {
		return instance;
	}
	
	public <T> void exportExcelAndDownload(String title, String[] headers, String[] fields, List<T> dataset, 
            HttpServletResponse response, HttpServletRequest request, String tmpPath, String fileName, boolean isOnline, String pattern, 
			List<ExcelEntity> headerList, boolean showSequence, boolean hasTotal, Boolean landScape) throws Exception {
        File file = new File(tmpPath + SUFFIX_PATH + File.separator + ((fileName.indexOf("xls") == -1) ? fileName + ".xls" : fileName));
        OutputStream out = new FileOutputStream(file);
        
        EETemplate<T> eeTemplate = new EETemplate<>();
        eeTemplate.callExportExcel(title, headers, fields, dataset, out, pattern, headerList, showSequence, hasTotal, landScape);
        
        if (isOnline) {
        	DocConverter converter = new DocConverter(file.getAbsolutePath(), tmpPath + PDF_SUFFIX_PATH, fileName);
        	converter.doc2pdf();
        } else {
        	DownLoadUtil.download(file, response, request, isOnline, fileName);
        }
	}
}
