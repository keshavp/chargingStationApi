package com.scube.chargingstation.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.scube.chargingstation.entity.ChargingRequestEntity;

@Service
public class PayslipPdfExporter {
	
	private static final Logger logger = LoggerFactory.getLogger(PayslipPdfExporter.class);

	private Path fileStorageLocation;
	
	private final String fileBaseLocation = "/uploads";
	
	/*
	 * @Autowired public PayslipPdfExporter(FileStorageProperties
	 * fileStorageProperties) { this.fileBaseLocation =
	 * fileStorageProperties.getUploadDir(); }
	 */
	
	public final static String fileExtension = ".pdf";
	
	String imageFile = "C:/itext/logo.png"; 
	
	// private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
	// private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.RED);
	// private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.BOLD);
	 //private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
	 //private static Font smallNorm = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
	    
	public String generatePdf(ChargingRequestEntity chargingRequestEntity) throws Exception {	
		
//		String filename = bookScheduleResponse.getBookingreff().concat(fileExtension);
		
		String filename = "invoice_"+new Date().getTime()+"_"+chargingRequestEntity.getId()+"".concat(fileExtension);
		
		
		logger.info("filename: "+filename);
		
		String newPath = this.fileBaseLocation+"/"+UploadPathContUtils.FILE_BOOKING_DIR;
		
		this.fileStorageLocation = Paths.get(newPath).toAbsolutePath().normalize();
		 
		logger.info("file newPath: "+newPath);
		   
		Files.createDirectories(this.fileStorageLocation);
		
		String dest = newPath+"/"+filename;
		
		logger.info("file dest: "+dest);
		
		PdfWriter writer = new PdfWriter(dest); 
		PdfDocument pdfDocument = new PdfDocument(writer);		
		Document layoutDocument = new Document(pdfDocument, PageSize.A4);

		//PdfService.addHeader(layoutDocument,pdfDocument);
		
		//PdfService.addTitle(layoutDocument);
		
		ImageData data = ImageDataFactory.create(imageFile);
		Image img = new Image(data); 
		img.setHeight(1f);
		//img.scaleAbsolute(70f, 50f);
		img.scaleAbsolute(142f, 71f);
		
		// Creating a table
//		float [] pointColumnWidths = {150f, 370f};
		Table table = new Table(1);
		

		// Populating row 1 and adding it to the table
		/*
		 * Cell cell1 = new Cell(); cell1.setBorder(Border.NO_BORDER); cell1.add(img);
		 * table.addCell(cell1);
		 */

		Cell cell2 = new Cell();
		cell2.setPaddingTop(35);
		cell2.setBorder(Border.NO_BORDER);
		
//		Color color = WebColors.getRGBColor("#161a47");
		Paragraph para = new Paragraph("INVOICE");
		para.setPaddingTop(5);
		para.setPaddingLeft(55);
		para.setPaddingBottom(5);
		para.setBold();
		para.setTextAlignment(TextAlignment.CENTER);
//		para.setFontColor(Color.WHITE);
//		para.setBackgroundColor(color);
		cell2.add(para);
		table.addCell(cell2);
		
		layoutDocument.add(table);
		
		Table table1 = new Table(2); 
		table1.setFontSize(8);
		
	    table1.addCell(new Paragraph("Charge Point :")).setBold();
	    table1.addCell(new Paragraph(chargingRequestEntity.getChargingPointEntity().getChargingPointId()));
	    // layoutDocument.add(table1);
	    
	    table1.addCell(new Paragraph("Connector :")).setBold();
	    table1.addCell(new Paragraph(chargingRequestEntity.getConnectorEntity().getConnectorId()));
	    // layoutDocument.add(table1);
	    
	    
	    
	    table1.addCell(new Paragraph("Charging Time :")).setBold();
	    table1.addCell(new Paragraph(String.valueOf(chargingRequestEntity.getMeterStop())));
	    //  layoutDocument.add(table1);
	    
	    table1.addCell(new Paragraph("Energy Delivered / kwh :")).setBold();
	    table1.addCell(new Paragraph(String.valueOf(chargingRequestEntity.getTransactionsEntity().getAllowedCharge())));
	    // layoutDocument.add(table1);
        
	    
	    table1.addCell(new Paragraph("Charging fee :")).setBold();
	    table1.addCell(new Paragraph(chargingRequestEntity.getRequestAmount()));
	   // layoutDocument.add(table1);
	    
	    
	    table1.addCell(new Paragraph("Total Cost")).setBold();
	    table1.addCell(new Paragraph(chargingRequestEntity.getRequestAmount()));
	   // layoutDocument.add(table1);
	    
	    table1.addCell(new Paragraph("Payment status")).setBold();
	    table1.addCell(new Paragraph("Success"));
	   
	    layoutDocument.add(table1);
        
		layoutDocument.close();
		
		return filename;
	}

	

	private static void addHeader(Document layoutDocument, PdfDocument pdfDocument) {
		Paragraph header = new Paragraph("Copy").setFontSize(8).setFontColor(Color.RED);

        for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
            Rectangle pageSize = pdfDocument.getPage(i).getPageSize();
            float x = pageSize.getWidth() / 2;
            float y = pageSize.getTop() - 20;
            layoutDocument.showTextAligned(header, x, y, i, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
        }
		
	}

	private static void addFooter(Document layoutDocument, PdfDocument pdfDocument) {
		
		layoutDocument.add(new Paragraph(""));
		layoutDocument.add(new Paragraph(""));
		
		//layoutDocument.add(new Paragraph(" Login | Edit Booking | View Charges | Contact Us").setBorder(Border.NO_BORDER).setFontSize(8).setTextAlignment(TextAlignment.CENTER));
		layoutDocument.add(new Paragraph("Copyright 2021 © BookMyCargo.online LLP All rights reserved.").setBorder(Border.NO_BORDER).setFontSize(8).setTextAlignment(TextAlignment.CENTER));
		
	}
}
