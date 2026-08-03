package com.hostel.complaint.util;

import com.hostel.complaint.model.Complaint;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.PageSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Report Generator utility class
 * Handles PDF and Excel report generation
 */
public class ReportGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ReportGenerator.class);
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Generate Excel report for complaints
     * @param complaints List of complaints
     * @param outputPath Output file path
     * @throws IOException if file operation fails
     */
    public static void generateComplaintExcelReport(List<Complaint> complaints, String outputPath) 
            throws IOException {
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Complaints Report");
            
            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            // Create headers
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "Complaint ID", "Complaint Number", "Student Name", "Category",
                "Title", "Priority", "Status", "Block", "Room",
                "Submitted Date", "Assigned Staff", "Resolution Time (Hours)"
            };
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            int rowNum = 1;
            for (Complaint complaint : complaints) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(complaint.getComplaintId());
                row.createCell(1).setCellValue(complaint.getComplaintNumber());
                row.createCell(2).setCellValue(complaint.getStudentName() != null ? complaint.getStudentName() : "");
                row.createCell(3).setCellValue(complaint.getCategoryName() != null ? complaint.getCategoryName() : "");
                row.createCell(4).setCellValue(complaint.getTitle());
                row.createCell(5).setCellValue(complaint.getPriority().name());
                row.createCell(6).setCellValue(complaint.getStatus().name());
                row.createCell(7).setCellValue(complaint.getBlockName() != null ? complaint.getBlockName() : "");
                row.createCell(8).setCellValue(complaint.getRoomNumber() != null ? complaint.getRoomNumber() : "");
                
                if (complaint.getSubmittedAt() != null) {
                    row.createCell(9).setCellValue(complaint.getSubmittedAt().format(DATE_FORMATTER));
                }
                
                row.createCell(10).setCellValue(complaint.getStaffName() != null ? complaint.getStaffName() : "");
                
                long resolutionTime = complaint.getResolutionTimeHours();
                if (resolutionTime >= 0) {
                    row.createCell(11).setCellValue(resolutionTime);
                }
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write to file
            try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
                workbook.write(outputStream);
            }
            
            logger.info("Excel report generated successfully: {}", outputPath);
        }
    }
    
    /**
     * Generate PDF report for complaints
     * @param complaints List of complaints
     * @param outputPath Output file path
     * @throws IOException if file operation fails
     * @throws DocumentException if PDF generation fails
     */
    public static void generateComplaintPdfReport(List<Complaint> complaints, String outputPath) 
            throws IOException, DocumentException {
        
        Document document = new Document(PageSize.A4.rotate());
        
        try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            
            // Add title
            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
            Paragraph title = new Paragraph("Complaints Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            // Add timestamp
            com.itextpdf.text.Font timestampFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10);
            Paragraph timestamp = new Paragraph(
                "Generated on: " + LocalDateTime.now().format(DATE_FORMATTER), 
                timestampFont
            );
            timestamp.setAlignment(Element.ALIGN_CENTER);
            document.add(timestamp);
            
            document.add(Chunk.NEWLINE);
            
            // Create table
            PdfPTable table = new PdfPTable(11);
            table.setWidthPercentage(100);
            
            // Add headers
            String[] headers = {
                "ID", "Number", "Student", "Category", "Title", 
                "Priority", "Status", "Block", "Room", "Submitted", "Staff"
            };
            
            com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD);
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }
            
            // Add data rows
            com.itextpdf.text.Font dataFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 8);
            for (Complaint complaint : complaints) {
                table.addCell(new PdfPCell(new Phrase(String.valueOf(complaint.getComplaintId()), dataFont)));
                table.addCell(new PdfPCell(new Phrase(complaint.getComplaintNumber(), dataFont)));
                table.addCell(new PdfPCell(new Phrase(
                    complaint.getStudentName() != null ? complaint.getStudentName() : "", dataFont)));
                table.addCell(new PdfPCell(new Phrase(
                    complaint.getCategoryName() != null ? complaint.getCategoryName() : "", dataFont)));
                table.addCell(new PdfPCell(new Phrase(complaint.getTitle(), dataFont)));
                table.addCell(new PdfPCell(new Phrase(complaint.getPriority().name(), dataFont)));
                table.addCell(new PdfPCell(new Phrase(complaint.getStatus().name(), dataFont)));
                table.addCell(new PdfPCell(new Phrase(
                    complaint.getBlockName() != null ? complaint.getBlockName() : "", dataFont)));
                table.addCell(new PdfPCell(new Phrase(
                    complaint.getRoomNumber() != null ? complaint.getRoomNumber() : "", dataFont)));
                table.addCell(new PdfPCell(new Phrase(
                    complaint.getSubmittedAt() != null ? 
                    complaint.getSubmittedAt().format(DATE_FORMATTER) : "", dataFont)));
                table.addCell(new PdfPCell(new Phrase(
                    complaint.getStaffName() != null ? complaint.getStaffName() : "", dataFont)));
            }
            
            document.add(table);
            document.close();
            
            logger.info("PDF report generated successfully: {}", outputPath);
        }
    }
    
    /**
     * Generate statistics Excel report
     * @param statistics Statistics map
     * @param outputPath Output file path
     * @throws IOException if file operation fails
     */
    public static void generateStatisticsExcelReport(Map<String, Object> statistics, String outputPath) 
            throws IOException {
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Statistics Report");
            
            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            // Create headers
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Metric");
            headerRow.createCell(1).setCellValue("Value");
            
            sheet.getRow(0).getCell(0).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(1).setCellStyle(headerStyle);
            
            // Create data rows
            int rowNum = 1;
            for (Map.Entry<String, Object> entry : statistics.entrySet()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entry.getKey());
                
                Object value = entry.getValue();
                if (value instanceof Number) {
                    row.createCell(1).setCellValue(((Number) value).doubleValue());
                } else if (value != null) {
                    row.createCell(1).setCellValue(value.toString());
                } else {
                    row.createCell(1).setCellValue("");
                }
            }
            
            // Auto-size columns
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            
            // Write to file
            try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
                workbook.write(outputStream);
            }
            
            logger.info("Statistics Excel report generated successfully: {}", outputPath);
        }
    }
    
    /**
     * Generate statistics PDF report
     * @param statistics Statistics map
     * @param outputPath Output file path
     * @throws IOException if file operation fails
     * @throws DocumentException if PDF generation fails
     */
    public static void generateStatisticsPdfReport(Map<String, Object> statistics, String outputPath) 
            throws IOException, DocumentException {
        
        Document document = new Document();
        
        try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            
            // Add title
            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
            Paragraph title = new Paragraph("Statistics Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            // Add timestamp
            com.itextpdf.text.Font timestampFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10);
            Paragraph timestamp = new Paragraph(
                "Generated on: " + LocalDateTime.now().format(DATE_FORMATTER), 
                timestampFont
            );
            timestamp.setAlignment(Element.ALIGN_CENTER);
            document.add(timestamp);
            
            document.add(Chunk.NEWLINE);
            
            // Create table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(50);
            
            // Add headers
            com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD);
            table.addCell(new PdfPCell(new Phrase("Metric", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Value", headerFont)));
            
            // Add data rows
            com.itextpdf.text.Font dataFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10);
            for (Map.Entry<String, Object> entry : statistics.entrySet()) {
                table.addCell(new PdfPCell(new Phrase(entry.getKey(), dataFont)));
                
                Object value = entry.getValue();
                String valueStr = value != null ? value.toString() : "";
                table.addCell(new PdfPCell(new Phrase(valueStr, dataFont)));
            }
            
            document.add(table);
            document.close();
            
            logger.info("Statistics PDF report generated successfully: {}", outputPath);
        }
    }
    
    /**
     * Generate category-wise Excel report
     * @param categoryStats Map of category to count
     * @param outputPath Output file path
     * @throws IOException if file operation fails
     */
    public static void generateCategoryExcelReport(Map<String, Long> categoryStats, String outputPath) 
            throws IOException {
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Category Report");
            
            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            // Create headers
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Category");
            headerRow.createCell(1).setCellValue("Count");
            
            sheet.getRow(0).getCell(0).setCellStyle(headerStyle);
            sheet.getRow(0).getCell(1).setCellStyle(headerStyle);
            
            // Create data rows
            int rowNum = 1;
            for (Map.Entry<String, Long> entry : categoryStats.entrySet()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue());
            }
            
            // Auto-size columns
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            
            // Write to file
            try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
                workbook.write(outputStream);
            }
            
            logger.info("Category Excel report generated successfully: {}", outputPath);
        }
    }
}
