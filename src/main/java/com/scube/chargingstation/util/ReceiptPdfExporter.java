package com.scube.chargingstation.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import javax.swing.text.StyleConstants.ColorConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.kernel.pdf.canvas.parser.listener.TextChunk;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.BorderCollapsePropertyValue;
import com.itextpdf.layout.properties.BorderRadius;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;


import com.scube.chargingstation.entity.ChargingRequestEntity;

@Service
public class ReceiptPdfExporter {
	
	private static final Logger logger = LoggerFactory.getLogger(ReceiptPdfExporter.class);

	private Path fileStorageLocation;
	
	private final String fileBaseLocation;
	
	/*
	 * @Autowired public PayslipPdfExporter(FileStorageProperties
	 * fileStorageProperties) { this.fileBaseLocation =
	 * fileStorageProperties.getUploadDir(); }
	 */
	
	  public ReceiptPdfExporter(FileStorageProperties fileStorageProperties) 
	  {    
	  this.fileBaseLocation = fileStorageProperties.getUploadDir();
	  }
	
	public final static String fileExtension = ".pdf";
	
	String imageFile = "C:/itext/logo.png"; 
	
	// private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
	// private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.RED);
	// private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.BOLD);
	 //private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
	 //private static Font smallNorm = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
	    
	public ChargingRequestEntity generatePdf(ChargingRequestEntity chargingRequestEntity) throws Exception {	
		
//		String filename = bookScheduleResponse.getBookingreff().concat(fileExtension);
		
		String filename = "invoice_"+new Date().getTime()+"_"+chargingRequestEntity.getId()+"".concat(fileExtension);
		
		//String filename = "invoice_"+chargingRequestEntity.getId()+"".concat(fileExtension);
		
		logger.info("filename: "+filename);
		
		String newPath = this.fileBaseLocation+"/"+UploadPathContUtils.FILE_RECEIPT_DIR;
		
		this.fileStorageLocation = Paths.get(newPath).toAbsolutePath().normalize();
		 
		logger.info("file newPath: "+newPath);
		   
		Files.createDirectories(this.fileStorageLocation);
		
		String dest = newPath+"/"+filename;
		
		logger.info("file dest: "+dest);
		
		PdfWriter writer = new PdfWriter(dest); 
		PdfDocument pdfDocument = new PdfDocument(writer);		
		Document layoutDocument = new Document(pdfDocument, PageSize.A4);
		
		PdfCanvas  canvas = new PdfCanvas(pdfDocument.addNewPage());

		//PdfService.addHeader(layoutDocument,pdfDocument);
		
		//PdfService.addTitle(layoutDocument);
		
		Table maintTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
		
		/*
		 * Cell cellImage1 = new Cell(); cellImage1.setBorder(Border.NO_BORDER);
		 * maintTable.addCell(cellImage1);
		 */		
		Cell cellImage = new Cell();
		//cellImage.set
		cellImage.setTextAlignment(TextAlignment.CENTER);
		ImageData data = ImageDataFactory.create(imageFile);
		Image img = new Image(data); 
		//img.setHeight(1f);
		//img.scaleAbsolute(70f, 50f);
		img.scaleAbsolute(130f, 125f);
		img.setTextAlignment(TextAlignment.CENTER);
		cellImage.setBorder(Border.NO_BORDER);
		cellImage.add(img);
		
		maintTable.addCell(cellImage);
		/*
		 * Cell cellImage2 = new Cell(); cellImage2.setBorder(Border.NO_BORDER);
		 * maintTable.addCell(cellImage2);
		 */
		
		layoutDocument.add(maintTable);

		
		Table maintInvoice = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
		
		// Populating row 1 and adding it to the table
		/*
		 * Cell cell1 = new Cell(); cell1.setBorder(Border.NO_BORDER); cell1.add(img);
		 * table.addCell(cell1);
		 */

		Cell cellInvoiceH = new Cell();
//		cell2.setPaddingTop(35);
		cellInvoiceH.setBorder(Border.NO_BORDER);
		
//		Color color = WebColors.getRGBColor("#161a47");
		Paragraph paraInvoiceH = new Paragraph("Receipt");
//		para.setPaddingTop(5);
//		para.setPaddingLeft(55);
//		para.setPaddingBottom(5);
		paraInvoiceH.setBold();
		paraInvoiceH.setTextAlignment(TextAlignment.CENTER);
//		para.setFontColor(Color.WHITE);
//		para.setBackgroundColor(color);
		cellInvoiceH.add(paraInvoiceH);
		maintInvoice.addCell(cellInvoiceH);
		
		Cell cellInvoiceVal = new Cell();
		cellInvoiceVal.setBorder(Border.NO_BORDER);
//		Color color = WebColors.getRGBColor("#161a47");
		
		String receiptNo = chargingRequestEntity.getId()+"_"+RandomNumber.getRandomNumberString()+RandomStringUtil.getAlphaNumericString(4, "EVDock");
		
		Paragraph paraInvoiceVal = new Paragraph(receiptNo);
//		invoiceIdpara2.setPaddingTop(5);
//		invoiceIdpara2.setBorder(Border.NO_BORDER);
//		invoiceIdpara2.setPaddingLeft(55);
//		invoiceIdpara2.setPaddingBottom(5);
//		invoiceIdpara2.setBold();
		paraInvoiceVal.setTextAlignment(TextAlignment.CENTER);
//		para.setFontColor(Color.WHITE);
//		para.setBackgroundColor(color);
		cellInvoiceVal.add(paraInvoiceVal);
		maintInvoice.addCell(cellInvoiceVal);
		
		layoutDocument.add(maintInvoice);
		/*
		 * // Initial point of the line canvas.moveTo(100, 300); // Drawing the line
		 * canvas.lineTo(500, 300); // Closing the path stroke canvas.closePathStroke();
		 */
		
		Table tableAll = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth(); 
		tableAll.setFontSize(8);
		
		Table table1 = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth(); 
		table1.setFontSize(8);
		
//	    table1.addCell(getCellBold("Charge Point :"));
//	    table1.addCell(getCellWithOutBold("{Charge Point }"));
	  //  table1.addCell(new Paragraph(chargingRequestEntity.getChargingPointEntity().getChargingPointId()));
	    // layoutDocument.add(table1);
	    
//	    table1.addCell(getCellBold("Connector :"));
//	    table1.addCell(getCellWithOutBold("Connector :"));
	    //table1.addCell(new Paragraph(chargingRequestEntity.getConnectorEntity().getConnectorId()));
	    // layoutDocument.add(table1);
	    
//	    tableAll.addCell(table1);
//		table1.addCell(getCellLabelVal("Charge Point  :",chargingRequestEntity.getChargingPointEntity().getChargingPointId()));
//		table1.addCell(getCellLabelVal("Connector :",chargingRequestEntity.getConnectorEntity().getConnectorId()));
//		layoutDocument.add(table1);
		
		table1.addCell(new Cell().add(new Paragraph("EV Dock GSTN  :")));
		table1.addCell(new Cell().add(new Paragraph("27AAJCT1560G1ZB")));
		
		table1.addCell(new Cell().add(new Paragraph("Name  :")));
		table1.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getCustName()))));
		table1.addCell(new Cell().add(new Paragraph("Mobile No.  :")));
		table1.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getMobileNo()))));
		table1.addCell(new Cell().add(new Paragraph("Vehicle No.  :")));
		table1.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getVehicleNO()))));
		
		table1.addCell(new Cell().add(new Paragraph("Charge Point  :")));
		table1.addCell(new Cell().add(new Paragraph(chargingRequestEntity.getChargingPointEntity().getChargingPointId())));
		table1.addCell(new Cell().add(new Paragraph("Connector  :")));
		table1.addCell(new Cell().add(new Paragraph(chargingRequestEntity.getConnectorEntity().getConnectorId())));
				
		layoutDocument.add(table1);
		
	    Table tableTime = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth(); 
	    tableTime.setFontSize(8); 
	    
//		tableTime.addCell(getCellBold("Charging Time :"));
//		tableTime.addCell(getCellWithOutBold("Charging Time :"));
	    //table1.addCell(new Paragraph(String.valueOf(chargingRequestEntity.getMeterStop())));
	    //  layoutDocument.add(table1);
	    
//		tableTime.addCell(getCellBold("Energy Delivered / kwh :"));
//		tableTime.addCell(getCellWithOutBold("Energy Delivered / kwh :"));
	    //table1.addCell(new Paragraph(String.valueOf(chargingRequestEntity.getTransactionsEntity().getAllowedCharge())));
	    // layoutDocument.add(table1);
        
//		tableAll.addCell(tableTime);
	
	//	tableTime.addCell(getCellLabelVal("Charging Time :",String.valueOf(chargingRequestEntity.getMeterStop())));
	//	tableTime.addCell(getCellLabelVal("Energy Delivered / kwh :",String.valueOf(chargingRequestEntity.getTransactionsEntity().getAllowedCharge())));
		
		
		tableTime.addCell(new Cell().add(new Paragraph("Charging Time  :")));
		tableTime.addCell(new Cell().add(new Paragraph(String.valueOf(chargingRequestEntity.getChargingTime()))));
		tableTime.addCell(new Cell().add(new Paragraph("Energy Delivered / kwh :")));
		tableTime.addCell(new Cell().add(new Paragraph(String.valueOf(chargingRequestEntity.getFinalKwh()))));
		
		layoutDocument.add(tableTime);
		
		Table tableCost = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth(); 
		tableCost.setFontSize(8); 
	    
//		tableCost.addCell(getCellBold("Charging fee :"));
//		tableCost.addCell(getCellWithOutBold("Charging fee :"));
	    //table1.addCell(new Paragraph(String.valueOf(chargingRequestEntity.getRequestAmount())));
	   // layoutDocument.add(table1);
	    
	    
	    //table1.addCell(new Paragraph(String.valueOf(chargingRequestEntity.getRequestAmount())));
	   // layoutDocument.add(table1);
	    
		double finalAmount = chargingRequestEntity.getFinalAmount();
		
		double WithoutGSTAmount = Math.round(finalAmount/1.18);
		
		double GSTAmount = Math.round(WithoutGSTAmount*0.18);
		
		
//		tableCost.addCell(getCellLabelVal("Charging fee :",String.valueOf(WithoutGSTAmount)));
//		layoutDocument.add(tableCost.addCell(getCellLabelVal("Idling fee :","0")));
//		tableCost.addCell(getCellLabelVal("GST 18% :",String.valueOf(GSTAmount)));
//		tableCost.addCell(getCellLabelVal("Total Cost",String.valueOf(finalAmount)));

		tableCost.addCell(new Cell().add(new Paragraph("Charging fee  :")));
		tableCost.addCell(new Cell().add(new Paragraph(String.valueOf(WithoutGSTAmount))));
		tableCost.addCell(new Cell().add(new Paragraph("GST 18%   :")));
		tableCost.addCell(new Cell().add(new Paragraph(String.valueOf(GSTAmount))));
		tableCost.addCell(new Cell().add(new Paragraph("Total Cost  :")));
		tableCost.addCell(new Cell().add(new Paragraph(String.valueOf(finalAmount))));
		
		layoutDocument.add(tableCost);
		
		Table tablePayStatus = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth(); 
		tablePayStatus.setFontSize(8);
		
	    layoutDocument.add(tablePayStatus.addCell(getCellLabelVal("Payment status","Success")));
	    
	    
		Cell cellRound = new Cell();
//		cell2.setPaddingTop(35);
		cellRound.setBorder(Border.NO_BORDER);
		
//		Color color = WebColors.getRGBColor("#161a47");
		Paragraph paraRound = new Paragraph("*Total cost is result for Rounding off (Charging fee + GST)");
		paraRound.setFontSize(3);
//		para.setPaddingTop(5);
//		para.setPaddingLeft(55);
//		para.setPaddingBottom(5);
//		paraRound.setBold();
		paraRound.setTextAlignment(TextAlignment.RIGHT);
//		para.setFontColor(Color.WHITE);
//		para.setBackgroundColor(color);
		cellRound.add(paraRound);
	    
		 layoutDocument.add(cellRound);
	    
		layoutDocument.close();
		
		System.out.println("filename==="+filename);
		System.out.println("receiptNo==="+receiptNo);
		
		chargingRequestEntity.setInvoiceFilePath(filename);
		chargingRequestEntity.setReceiptNo(receiptNo);
		
		
		return chargingRequestEntity;
	}

	/*
	 * private static Cell getCell(String content) { Cell cell = new Cell().add(new
	 * Paragraph(content)); cell.setBorderTopRightRadius(new BorderRadius(4));
	 * cell.setBorderTopLeftRadius(new BorderRadius(4)); return cell; }
	 */
	
	private static Cell getCellBold(String content) {
 //       Cell cell = new Cell().add(new Paragraph(content));
 //       cell.setBorderTopRightRadius(new BorderRadius(4));
 //       cell.setBorderTopLeftRadius(new BorderRadius(4));
        
        Cell cell = new Cell().add(new Paragraph(content).setBold());
//		cell2.setPaddingTop(35);
        cell.setBorder(Border.NO_BORDER);
//		para.setPaddingBottom(5);
//        cell.setBold();
        cell.setTextAlignment(TextAlignment.CENTER);
//		para.setFontColor(Color.WHITE);
//		para.setBackgroundColor(color);
        
        return cell;
    }
	
	private static Cell getCellWithOutBold(String content) {
 //     Cell cell = new Cell().add(new Paragraph(content));
 //     cell.setBorderTopRightRadius(new BorderRadius(4));
 //     cell.setBorderTopLeftRadius(new BorderRadius(4));
		        
    	Cell cell = new Cell().add(new Paragraph(content));
//		cell2.setPaddingTop(35);
        cell.setBorder(Border.NO_BORDER);
//		para.setPaddingBottom(5);
//        cell.setBold();
        cell.setTextAlignment(TextAlignment.CENTER);
//		para.setFontColor(Color.WHITE);
//		para.setBackgroundColor(color);
        return cell;
    }
	
	
	private static Table getCellLabelVal(String labelContent , String valContent) {
 //     Cell cell = new Cell().add(new Paragraph(content));
 //     cell.setBorderTopRightRadius(new BorderRadius(4));
 //     cell.setBorderTopLeftRadius(new BorderRadius(4));
		
		Table cell = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();;
		
//		cell.setBorder(new SolidBorder(1));
    	Cell cellLabel = new Cell().add(new Paragraph(labelContent));
//		cell2.setPaddingTop(35);
		cellLabel.setBorder(Border.NO_BORDER);
//		para.setPaddingBottom(5);
//        cell.setBold();
		cellLabel.setTextAlignment(TextAlignment.CENTER);
//		para.setFontColor(Color.WHITE);
//		para.setBackgroundColor(color);
		
		
		Cell cellVal = new Cell().add(new Paragraph(valContent));
//		cell2.setPaddingTop(35);
		cellVal.setBorder(Border.NO_BORDER);
//		para.setPaddingBottom(5);
//        cell.setBold();
		cellVal.setTextAlignment(TextAlignment.CENTER);
//		para.setFontColor(Color.WHITE);
//		para.setBackgroundColor(color);
		
	//	cell.addCell(cellLabel);
	//	cell.addCell(cellVal);
		cell.addCell(new Cell().add(new Paragraph(labelContent)));
		cell.addCell(new Cell().add(new Paragraph(valContent)));
		
		
		
        return cell;
    }
	

	
    private static class TableBorderRenderer extends TableRenderer {
        public TableBorderRenderer(Table modelElement) {
            super(modelElement);
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new TableBorderRenderer((Table) modelElement);
        }

        @Override
        protected void drawBorders(DrawContext drawContext) {
            Rectangle rect = getOccupiedAreaBBox();
            drawContext.getCanvas()
                    .saveState()
                    .rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight())
                    .stroke()
                    .restoreState();
        }
    }
	
	/*
	 * private static void addHeader(Document layoutDocument, PdfDocument
	 * pdfDocument) { Paragraph header = new
	 * Paragraph("Copy").setFontSize(8).setFontColor(Color.RED);
	 * 
	 * for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) { Rectangle
	 * pageSize = pdfDocument.getPage(i).getPageSize(); float x =
	 * pageSize.getWidth() / 2; float y = pageSize.getTop() - 20;
	 * layoutDocument.showTextAligned(header, x, y, i, TextAlignment.LEFT,
	 * VerticalAlignment.BOTTOM, 0); }
	 * 
	 * }
	 * 
	 * private static void addFooter(Document layoutDocument, PdfDocument
	 * pdfDocument) {
	 * 
	 * layoutDocument.add(new Paragraph("")); layoutDocument.add(new Paragraph(""));
	 * 
	 * //layoutDocument.add(new
	 * Paragraph(" Login | Edit Booking | View Charges | Contact Us").setBorder(
	 * Border.NO_BORDER).setFontSize(8).setTextAlignment(TextAlignment.CENTER));
	 * layoutDocument.add(new
	 * Paragraph("Copyright 2021 © BookMyCargo.online LLP All rights reserved.").
	 * setBorder(Border.NO_BORDER).setFontSize(8).setTextAlignment(TextAlignment.
	 * CENTER));
	 * 
	 * }
	 */
}
