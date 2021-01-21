package com.siyueren.demo.redisdistributedlocker.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ExcelExport {
    
    private static final Logger log = LoggerFactory.getLogger(ExcelExport.class);
    
    private HttpServletResponse response;
    private HSSFWorkbook workBook;
    private String fileName;
    
    public ExcelExport(HttpServletResponse response, String fileName) {
        this.response = response;
        this.workBook = new HSSFWorkbook();
        this.fileName = fileName;
    }
    
    /**
     * @param sheetName 分页，可多页
     * @param data      数据
     * @param handlers  title-取值 映射
     *
     * @author 乔健勇
     * @date 16:22 2021/1/21
     * @email qjyoung@163.com
     */
    public <T> HSSFWorkbook writeToFile(String sheetName, List<T> data, ValueHandlerList<T> handlers) {
        HSSFSheet sheet = workBook.createSheet(sheetName);
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = workBook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCell cell;
        
        // header line
        for (int i = 0; i < handlers.size(); i++) {
            ValueHandler<T> handler = handlers.get(i);
            cell = row.createCell(i);
            cell.setCellValue(handler.getTitle());
            cell.setCellStyle(style);
            if (handler.getChars() > 0 && handler.getChars() < 256) {
                sheet.setColumnWidth(i, handler.getChars() * 256);
            }
        }
        
        // body
        for (int i = 0; i < data.size(); i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < handlers.size(); j++) {
                row.createCell(j).setCellValue(handlers.get(j).getMapper().apply(data.get(i)));
            }
        }
        return workBook;
    }
    
    public void export() {
        try (OutputStream outputStream = response.getOutputStream()) {
            if (fileName == null) {
                fileName = "excel.xls";
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "iso8859-1"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            workBook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            workBook.close();
        } catch (IOException e) {
            log.error("export() error", e);
        }
    }
    
    public static class ValueHandlerList<T> extends ArrayList<ValueHandler<T>> {
        public ValueHandlerList<T> append(String title, Function<T, String> mapper) {
            add(new ValueHandler<>(title, mapper));
            return this;
        }
        
        public ValueHandlerList<T> append(String title, int chars, Function<T, String> mapper) {
            add(new ValueHandler<>(title, chars, mapper));
            return this;
        }
    }
    
    public static class ValueHandler<T> {
        private String title;
        private int chars;
        private Function<T, String> mapper;
        
        public ValueHandler() {
        }
        
        public ValueHandler(String title, Function<T, String> mapper) {
            this(title, 0, mapper);
        }
        
        public ValueHandler(String title, int chars, Function<T, String> mapper) {
            this.title = title;
            this.chars = chars;
            this.mapper = mapper;
        }
        
        public String getTitle() {
            return title;
        }
        
        public void setTitle(String title) {
            this.title = title;
        }
        
        public int getChars() {
            return chars;
        }
        
        public void setChars(int chars) {
            this.chars = chars;
        }
        
        public Function<T, String> getMapper() {
            return mapper;
        }
        
        public void setMapper(Function<T, String> mapper) {
            this.mapper = mapper;
        }
    }
}