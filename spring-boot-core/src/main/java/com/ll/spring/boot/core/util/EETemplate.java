package com.ll.spring.boot.core.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ll.spring.boot.core.consts.Consts;
import com.ll.spring.boot.core.util.entity.Entity;
import com.ll.spring.boot.core.util.entity.ExcelEntity;

/** 
 * 动态生成模版并导出excel文档 
 * 
 * 本类的简要描述： 
 * 	利用开源组件poi生成
 * 	jar包：poi-3.8-20120326.jar
 *  
 */ 
public class EETemplate<T> {
	private static final int DEFAULT_BYTE_WIDTH = 550;
	
	private Logger log = LoggerFactory.getLogger(getClass());
    
    public void callExportExcel(String title, String[] headers, String[] fields, Collection<T> dataset, OutputStream out, String pattern, List<ExcelEntity> headerList, 
    		boolean showSequence, boolean hasTotal, Boolean landScape) throws Exception {
        exportExcel(title, headers, fields, dataset, out, pattern, headerList, showSequence, hasTotal, landScape);
    }
    
    //末尾添加一行总计
    /**
     * 
     * @param title
     * @param headers
     * @param fields
     * @param dataset
     * @param out
     * @param pattern
     * @param total  总金额
     * @param cou  第几列
     * @throws Exception
     */
    public void callExportExcel(String title, String[] headers, String[] fields, Collection<T> dataset, OutputStream out, String pattern,
    		String total,Integer cou) throws Exception {
        exportExcelTotal(title, headers, fields, dataset, out, pattern, total,cou);
    }

    /**
     * 利用了java的反射机制将放置在集合中并且符合一定条件的数据以excel的形式输出到指定io设备
     * 
     * @param title 表格标题名
     * @param headers 表格列名数组
     * @param fields 表格列名对应属性名数组
     * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象
     * @param out 与输出设备关联的流对象，可以将excel文档导出到服务器本地或者网络
     * @param pattern 如果有时间数据，设定输出格式(默认为yyy-MM-dd)
     * @param hasTotal 
     * @throws Exception 
     */
    @SuppressWarnings({ "deprecation", "resource" })
	public void exportExcel(String title, String[] headers, String[] fields, Collection<T> dataset, OutputStream out, String pattern, 
			List<ExcelEntity> headerList, boolean showSequence, boolean hasTotal, Boolean landScape) throws Exception {
    	pattern = StringUtil.isNullEmpty(pattern) ? Consts.DATE_PATTERN : pattern;
    	
        //声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        
        //生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        
//        sheet.setDisplayGridlines(false);
//        sheet.setPrintGridlines(false);
        
        //设置垂直水平居中
        sheet.setHorizontallyCenter(true);
//        sheet.setVerticallyCenter(true);
        HSSFPrintSetup ps = sheet.getPrintSetup();
        ps.setLandscape(landScape == null ? false : landScape);
        ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);  
        
        //设置页头
//        if (headerList != null && headerList.size() > 0) {
//        	HSSFHeader h = sheet.getHeader();
//        	for (int i = 0; i < headerList.size(); i++) {
//        		ExcelEntity entity = headerList.get(i);
//        		h.setCenter(entity.getEntitys().get(0).getText());
//        		if (i < headerList.size() - 1) {
//        			HSSFHeader.startUnderline();
//        		}
//			}
//        }
        //设置页脚
//        HSSFFooter footer = sheet.getFooter();
//        footer.setRight( "第 " + HSSFFooter.page() + " 页，共 " + HSSFFooter.numPages() + " 页");//页脚
        
        //设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(9); 

        int beginIndex = showSequence ? 1 : 0;
        for (int i = 0; i < headers.length; i++) {
        	String header = headers[i];
        	if ("部门".equals(header) || "餐厅".equals(header) || "设备".equals(header) || "菜品".equals(header) || "消费时间".equals(header) || "菜品编号".equals(header)) {
        		sheet.setColumnWidth(i + beginIndex, 8 * DEFAULT_BYTE_WIDTH);
        	} else if ("日期".equals(header) || "员工".equals(header) || "总金额".equals(header) || "姓名".equals(header) || "余额".equals(header) || "操作员".equals(header)) {
        		sheet.setColumnWidth(i + beginIndex, 5 * DEFAULT_BYTE_WIDTH);
        	} else if ("编号".equals(header) || "工号".equals(header) || "金额".equals(header) || "类型".equals(header)) {
        		sheet.setColumnWidth(i + beginIndex, 4 * DEFAULT_BYTE_WIDTH);
        	} else {
        		sheet.setColumnWidth(i + beginIndex, header.length() * DEFAULT_BYTE_WIDTH);
        	}
        }
        
        if (dataset != null && dataset.size() > 0) {
        	T t = dataset.iterator().next();
        	for (int i = 0; i < headers.length; i++) {
        		String fieldName = fields[i];
        		Field f = t.getClass().getDeclaredField(fieldName);
        		if (f.getType() == Date.class) {
        			sheet.setColumnWidth(i + 1, 9 * DEFAULT_BYTE_WIDTH);
        		} else if (f.getType() == java.sql.Date.class) {
        			sheet.setColumnWidth(i + 1, 5 * DEFAULT_BYTE_WIDTH);
        		}
        	}
        }
        
        if (showSequence) {
        	sheet.setColumnWidth(0, 3 * DEFAULT_BYTE_WIDTH);
        }
        
        //生成并设置一个字体
        HSSFFont headerFont = workbook.createFont();
        headerFont.setColor(HSSFColor.BLACK.index);
        headerFont.setFontHeightInPoints((short) 20);
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerFont.setFontName("宋体");
        
        HSSFCellStyle headerStyle = workbook.createCellStyle();       
        headerStyle.setFillForegroundColor(IndexedColors.WHITE.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFont(headerFont);
        
        //生成并设置一个字体
        HSSFFont secondaryHeaderFont = workbook.createFont();
        secondaryHeaderFont.setColor(HSSFColor.BLACK.index);
        secondaryHeaderFont.setFontHeightInPoints((short) 8);
        secondaryHeaderFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        secondaryHeaderFont.setFontName("宋体");
        
        HSSFCellStyle secondaryHeaderStyle = workbook.createCellStyle();       
        secondaryHeaderStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//        secondaryHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        secondaryHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
        secondaryHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        secondaryHeaderStyle.setFont(secondaryHeaderFont);
        
        
        
        
        //生成并设置一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 8);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        
        //生成并设置一个样式
        HSSFCellStyle style = workbook.createCellStyle();       
        style.setFillForegroundColor(IndexedColors.WHITE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        
        //把字体应用到当前的样式
        style.setFont(font);
        
        //生成并设置另一个字体
        HSSFFont font2 = workbook.createFont();        
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        font2.setFontHeightInPoints((short) 8);
        font2.setFontName("宋体");
        
        //生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(IndexedColors.WHITE.index);
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        style2.setWrapText(true);
              
        //把字体应用到当前的样式
        style2.setFont(font2);
        
        //生成并设置另一个字体
        HSSFFont font3 = workbook.createFont();
        font3.setColor(HSSFColor.BLACK.index);
        font3.setFontHeightInPoints((short) 8);
        font3.setFontName("宋体");
        
        dealMergeCell(headerList, dataset, sheet);

        AtomicInteger rowIndex = new AtomicInteger(0);
        
        //生成header
        dealExcelEntityList(headerList, sheet, rowIndex, headerStyle, secondaryHeaderStyle);
        //产生表格标题行
        HSSFRow row = sheet.createRow(rowIndex.getAndIncrement());
        if (showSequence) {
        	HSSFCell cell = row.createCell(0);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(0); 
            HSSFRichTextString text = new HSSFRichTextString("序号");
            cell.setCellValue(text);
        }
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i + beginIndex);
            sheet.autoSizeColumn(i + beginIndex); 
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        //遍历集合数据并产生数据行
        Iterator<T> it = dataset.iterator();
        int sequence = 1;
        while (it.hasNext()) {
            T t = (T) it.next();
            
            row = sheet.createRow(rowIndex.getAndIncrement());
            
            if (showSequence) {
            	HSSFCell cell = row.createCell(0);
                cell.setCellStyle(style2);
                String text = sequence + "";
                if (hasTotal && !it.hasNext()) {
                	text = "";
                }
            	HSSFRichTextString richString = new HSSFRichTextString(text);
                richString.applyFont(font3);
                cell.setCellValue(richString);          
                sequence++;
            }
            
            for (int i = 0; i < fields.length; i++) {
                HSSFCell cell = row.createCell(i + beginIndex);
                cell.setCellStyle(style2);
                String fieldName = fields[i];
                Object value = getValue(t, fieldName);
                if(value != null){
                	String textValue = "";
                    if (value instanceof Float) {
                    	textValue = value.toString();
                    	textValue = CommonUtils.trimZero(textValue);
                    } else if (value instanceof Double) {
                    	textValue = value.toString();
                    	textValue = CommonUtils.trimZero(textValue);
                    } else if (value instanceof BigDecimal) {
                    	textValue = value.toString();
                    	textValue = CommonUtils.trimZero(textValue);
                    } else if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);                                                    
                    }else if(value instanceof Long && (fieldName.equals("statDate")  ||  fieldName.equals("createDate") || fieldName.equals("updateDate") )){
                    	Date date = new Date();
                    	date.setTime((long) value);
                    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    	textValue = sdf.format(date);
                    } else if(value instanceof Integer && (fieldName.equals("channel") || fieldName.equals("appType") || fieldName.equals("partnerType") || fieldName.equals("distributionType"))){
                    		if(fieldName.equals("channel")){
                    			switch((Integer)value){
                    			case 1:textValue = "海马平台苹果商店";break;
                    			case 2:textValue = "爱思助手";break;
                    			case 3:textValue = "xy苹果助手"; break;
                    			}
                    		}
                    		if(fieldName.equals("appType")){
                    			switch((Integer)value){
                    			case 1:textValue = "IOS";break;
                    			case 2:textValue = "Android";break;
                    			}
                    		}
                    		if(fieldName.equals("partnerType")){
                    			switch((Integer)value){
                    			case 1:textValue = "海马平台苹果商店";break;
                    			case 2:textValue = "爱思助手";break;
                    			case 3:textValue = "xy苹果助手"; break;
                    			}
                    		}
                    		if(fieldName.equals("distributionType")){
                    			switch((Integer)value){
                    			case 1:textValue = "ios";break;
                    			case 2:textValue = "android";break;
                    			}
                    		}
                    } else if(value instanceof Long && (fieldName.equals("channel") || fieldName.equals("appType") || fieldName.equals("partnerType") || fieldName.equals("distributionType"))){
                		if(fieldName.equals("channel")){
                			if(0 == Long.compare(1, (Long) value)){
                				textValue = "海马平台苹果商店";
                			}
                			if(0 == Long.compare(2, (Long) value)){
                				textValue = "爱思助手";
                			}
                			if(0 == Long.compare(3, (Long) value)){
                				textValue = "xy苹果助手";
                			}
                		}
                		if(fieldName.equals("appType")){
                			if(0 == Long.compare(1, (Long) value)){
                				textValue = "IOS";
                			}
                			if(0 == Long.compare(2, (Long) value)){
                				textValue = "Android";
                			}
                		}
                }else {
                        // 其它数据类型都当作字符串简单处理
                        textValue = value.toString();                                                                       
                    }      
                    HSSFRichTextString richString = new HSSFRichTextString(textValue);
                    richString.applyFont(font3);
                    cell.setCellValue(richString);                                              
                }                
            }
        }
        
        //生成header
//        dealExcelEntityList(footerList, sheet, rowIndex, style2);
        workbook.write(out);  
        try {
			out.close();
		} catch (Exception e) {
		}
    }
    
//    private int getMaxHeight(int i, Collection<T> dataset, String[] headers, String[] fields, int n) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//    	int height = 0;
//    	String fieldName = fields[i];
//    	for (T t : dataset) {
//    		Object value = getValue(t, fieldName);
//    		if (value != null) {
//    			int tmp = value.toString().length();
//    			if (tmp % n == 0) {
//    				tmp = tmp / n;
//    			} else {
//    				tmp = tmp / n + 1;
//    			}
//    			if (tmp > height) {
//    				height = tmp;
//    			}
//    		}
//    	}
//		return height;
//	}


//	private int getMaxLen(int i, Collection<T> dataset, String[] headers, String[] fields) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//    	int len = headers[i].length();
//    	String fieldName = fields[i];
//    	for (T t : dataset) {
//    		Object value = getValue(t, fieldName);
//    		if (value != null) {
//    			int tmp = value.toString().length();
//    			if (tmp > len) {
//    				len = tmp;
//    			}
//    		}
//    	}
//		return len;
//	}

	private Object getValue(T t, String fieldName) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);              
        Method getMethod = t.getClass().getMethod(getMethodName,new Class[] {});
        return getMethod.invoke(t, new Object[] {});
    }
    
    private void dealMergeCell(List<ExcelEntity> headerList, Collection<T> dataset, HSSFSheet sheet) {
    	AtomicInteger index = new AtomicInteger(0);
    	dealMergeCell(headerList, sheet, index);
    	
//    	if (footerList != null && footerList.size() > 0) {
//    		int size = 0;
//    		if (dataset != null && dataset.size() > 0) {
//    			size = dataset.size();
//    		}
//    		index.addAndGet(size + 1);
//
//        	dealMergeCell(footerList, sheet, index);
//    	}
    }
    
    @SuppressWarnings("deprecation")
	private void dealMergeCell(List<ExcelEntity> entityList, HSSFSheet sheet, AtomicInteger index) {
    	if (entityList != null && entityList.size() > 0) {
    		for (ExcelEntity headerEntity : entityList) {
				for (Entity entity : headerEntity.getEntitys()) {
					if (entity.getMergeIndexList() != null && entity.getMergeIndexList().size() > 0) {
						CellRangeAddress cra =new CellRangeAddress(index.get(), index.get(), entity.getMergeIndexList().get(0), entity.getMergeIndexList().get(1));
						sheet.addMergedRegion(cra);
						RegionUtil.setBorderBottom(1, cra, sheet); // 下边框		
						RegionUtil.setBorderLeft(1, cra, sheet); // 左边框		
						RegionUtil.setBorderRight(1, cra, sheet); // 有边框		
						RegionUtil.setBorderTop(1, cra, sheet); // 上边框
					}
				}
    			index.incrementAndGet();
			}
    	}
    }
    
    

    private void dealExcelEntityList(List<ExcelEntity> excelEntityList, HSSFSheet sheet, AtomicInteger rowIndex, HSSFCellStyle firstStyle, HSSFCellStyle secondaryStyle) {
		if (excelEntityList == null || excelEntityList.size() == 0) {
			return ;
		}
		
		for (int i = 0; i < excelEntityList.size(); i++) {
			ExcelEntity excelEntity = excelEntityList.get(i);
			HSSFCellStyle style = secondaryStyle;
			if (i == 0) {
				style = firstStyle;
			}
			dealEntityList(excelEntity.getEntitys(), sheet, rowIndex, style);
		}
	}

	private void dealEntityList(List<Entity> entitys, HSSFSheet sheet, AtomicInteger rowIndex, HSSFCellStyle style) {
		if (entitys != null && entitys.size() > 0) {
			HSSFRow row = sheet.createRow(rowIndex.getAndIncrement());
			entitys.forEach((entity) -> {
				HSSFCell cell = row.createCell(entity.getColIndex());
				cell.setCellStyle(style);
				cell.setCellValue(entity.getText());
			});
		}
	}

	@SuppressWarnings({ "deprecation", "resource" })
    public void exportExcelTotal(String title, String[] headers, String[] fields, Collection<T> dataset, OutputStream out, String pattern,String total,Integer  cou) throws Exception {
        //声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        
        //生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        
        //设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);    
        
        //生成并设置一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        
        //生成并设置一个样式
        HSSFCellStyle style = workbook.createCellStyle();       
        style.setFillForegroundColor(IndexedColors.WHITE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        
        //把字体应用到当前的样式
        style.setFont(font);
        
        //生成并设置另一个字体
        HSSFFont font2 = workbook.createFont();        
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        font2.setFontName("宋体");
        
        //生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.WHITE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
              
        //把字体应用到当前的样式
        style2.setFont(font2);
        
        //生成并设置另一个字体
        HSSFFont font3 = workbook.createFont();
        font3.setColor(HSSFColor.BLUE.index);
        font3.setFontName("宋体");

        //产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        //遍历集合数据并产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            for (int i = 0; i < fields.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style2);
                String fieldName = fields[i];
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);              
                Method getMethod = t.getClass().getMethod(getMethodName,new Class[] {});
                Object value = getMethod.invoke(t, new Object[] {});
                if(value != null){
                	//判断值的类型后进行强制类型转换
                    String textValue = "";
                    if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);                                                    
                    } else if(value instanceof Long && (fieldName.equals("statDate")  ||  fieldName.equals("createDate") || fieldName.equals("updateDate") )){
                    	Date date = new Date();
                    	date.setTime((long) value);
                    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    	textValue = sdf.format(date);
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        textValue = value.toString();                                                                       
                    }      
                    HSSFRichTextString richString = new HSSFRichTextString(textValue);
                    richString.applyFont(font3);
                    cell.setCellValue(richString);                                              
                }                
            }
        }
        if(!it.hasNext()){
        	 row = sheet.createRow(index+1);
             HSSFCell  cell = row.createCell(cou);
             cell.setCellValue(total);
             cell.setCellStyle(style2);
        }
        workbook.write(out);       
    }

    @SuppressWarnings({ "resource", "deprecation" })
	public void callExportExcel(String title, String[] headers, String[] fields, List<Map<String, Object>> dataset,
            OutputStream out, String pattern) throws IOException {
        //声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        
        //生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        
        //设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);    
        
        //生成并设置一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        
        //生成并设置一个样式
        HSSFCellStyle style = workbook.createCellStyle();       
        style.setFillForegroundColor(IndexedColors.WHITE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        
        //把字体应用到当前的样式
        style.setFont(font);
        
        //生成并设置另一个字体
        HSSFFont font2 = workbook.createFont();        
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        font2.setFontName("宋体");
        
        //生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(IndexedColors.WHITE.index);
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
              
        //把字体应用到当前的样式
        style2.setFont(font2);
        
        //生成并设置另一个字体
        HSSFFont font3 = workbook.createFont();
        font3.setColor(HSSFColor.BLACK.index);
        font3.setFontName("宋体");

        //产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        //遍历集合数据并产生数据行
        Iterator<Map<String, Object>> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            Map<String, Object> t = it.next();
            for (int i = 0; i < fields.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style2);
                String fieldName = fields[i];
                Object value = t.get(fieldName);
                if(value != null){
                    //判断值的类型后进行强制类型转换
                    String textValue = "";
                    if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);                                                    
                    }else if(value instanceof Long && (fieldName.equals("statDate")  ||  fieldName.equals("createDate") || fieldName.equals("updateDate") )){
                        Date date = new Date();
                        date.setTime((long) value);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        textValue = sdf.format(date);
                    } else if(value instanceof Integer && (fieldName.equals("channel") || fieldName.equals("appType") || fieldName.equals("partnerType") || fieldName.equals("distributionType"))){
                            if(fieldName.equals("channel")){
                                switch((Integer)value){
                                case 1:textValue = "海马平台苹果商店";break;
                                case 2:textValue = "爱思助手";break;
                                case 3:textValue = "xy苹果助手"; break;
                                }
                            }
                            if(fieldName.equals("appType")){
                                switch((Integer)value){
                                case 1:textValue = "IOS";break;
                                case 2:textValue = "Android";break;
                                }
                            }
                            if(fieldName.equals("partnerType")){
                                switch((Integer)value){
                                case 1:textValue = "海马平台苹果商店";break;
                                case 2:textValue = "爱思助手";break;
                                case 3:textValue = "xy苹果助手"; break;
                                }
                            }
                            if(fieldName.equals("distributionType")){
                                switch((Integer)value){
                                case 1:textValue = "ios";break;
                                case 2:textValue = "android";break;
                                }
                            }
                    } else if(value instanceof Long && (fieldName.equals("channel") || fieldName.equals("appType") || fieldName.equals("partnerType") || fieldName.equals("distributionType"))){
                        if(fieldName.equals("channel")){
                            if(0 == Long.compare(1, (Long) value)){
                                textValue = "海马平台苹果商店";
                            }
                            if(0 == Long.compare(2, (Long) value)){
                                textValue = "爱思助手";
                            }
                            if(0 == Long.compare(3, (Long) value)){
                                textValue = "xy苹果助手";
                            }
                        }
                        if(fieldName.equals("appType")){
                            if(0 == Long.compare(1, (Long) value)){
                                textValue = "IOS";
                            }
                            if(0 == Long.compare(2, (Long) value)){
                                textValue = "Android";
                            }
                        }
                }else {
                        // 其它数据类型都当作字符串简单处理
                        textValue = value.toString();                                                                       
                    }      
                    HSSFRichTextString richString = new HSSFRichTextString(textValue);
                    richString.applyFont(font3);
                    cell.setCellValue(richString);                                              
                }                
            }
        }
        workbook.write(out);       
    }
    
}