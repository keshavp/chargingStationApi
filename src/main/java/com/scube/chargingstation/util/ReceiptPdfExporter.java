package com.scube.chargingstation.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

import javax.swing.GroupLayout.Alignment;
import javax.swing.plaf.LayerUI;
import javax.swing.text.StyleConstants.ColorConstants;
import javax.swing.text.StyleConstants.FontConstants;
import javax.swing.text.html.parser.DTD;

import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.apache.poi.hssf.util.HSSFColor.BLACK;
import org.apache.poi.hssf.util.HSSFColor.BLUE;
import org.apache.poi.hssf.util.HSSFColor.GREY_25_PERCENT;
import org.apache.poi.hssf.util.HSSFColor.WHITE;
import org.apache.poi.ss.format.CellFormatPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.forms.xfdf.ElementContentEncodingFormat;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import com.itextpdf.kernel.pdf.canvas.parser.listener.TextChunk;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.Border3D;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.BorderCollapsePropertyValue;
import com.itextpdf.layout.properties.BorderRadius;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import com.itextpdf.layout.renderer.TableRenderer;
import com.scube.chargingstation.dto.ChargingPointConnectorRateDto;   
import com.scube.chargingstation.entity.ChargingRequestEntity;
import com.scube.chargingstation.service.ChargingRequestService;

import net.bytebuddy.agent.builder.AgentBuilder.FallbackStrategy.Simple;

@Service
public class ReceiptPdfExporter {
	
	private static final Logger logger = LoggerFactory.getLogger(ReceiptPdfExporter.class);  

	private Path fileStorageLocation;
	
	private final String fileBaseLocation;
	
	@Autowired
	ChargingRequestService chargingRequestService;
	
	
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	
	/*
	 * @Autowired public PayslipPdfExporter(FileStorageProperties
	 * fileStorageProperties) { this.fileBaseLocation =
	 * fileStorageProperties.getUploadDir(); }
	 */
	
	  public ReceiptPdfExporter(FileStorageProperties fileStorageProperties) {    
		  
		  this.fileBaseLocation = fileStorageProperties.getUploadDir();
	  
	  }
	
	public final static String fileExtension = ".pdf";
	
	String imageFile = "C:/itext/logo.png"; 
	
	// private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
	// private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.RED);
	// private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.BOLD);
	 //private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
	 //private static Font smallNorm = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
	    
	public ChargingRequestEntity generatePdf(ChargingRequestEntity chargingRequestEntity, ChargingPointConnectorRateDto oneKwhchargingPointConnectorRateDto) throws Exception {	
		
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
		
/*		String receiptNo = chargingRequestEntity.getId()+"_"+RandomNumber.getRandomNumberString()+RandomStringUtil.getAlphaNumericString(4, "EVDock");
		logger.info("Receipt No : "+ receiptNo); */
		
		//financial year
		LocalDate currentDate = LocalDate.now();
		int year = currentDate.getYear();
		String financialYear;

		if (currentDate.getMonthValue() >= 4) {
		    financialYear = year + "-" + (year + 1);
		} else {
		    financialYear = (year - 1) + "-" + year;
		}

		System.out.println("----------Financial year--------: " + financialYear);
		
		// Get Count for Sequence
		int sequenceNo = chargingRequestService.getCountByChargingPointIDAndConnectorID(chargingRequestEntity.getChargingPointEntity(), 
				chargingRequestEntity.getConnectorEntity()) + 1;
		
		String invoiceNO = "INV"+"-"+ financialYear + "-"+chargingRequestEntity.getChargingPointEntity().getChargingPointId() + "-" +
				chargingRequestEntity.getConnectorEntity().getConnectorId() + "_" + sequenceNo;
		
		// PDF Creation		
		PdfWriter writer = new PdfWriter(dest);
		
		PdfDocument pdfDocument = new PdfDocument(writer);
		
		Document layoutDocument = new Document(pdfDocument,PageSize.A4);   
		 
		PdfCanvas canvas = new PdfCanvas(pdfDocument.addNewPage());		
		
		// Table for Image
		Table mainTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
		
		Cell cellImage = new Cell();
		cellImage.setTextAlignment(TextAlignment.CENTER);
		
		ImageData data = ImageDataFactory.create(imageFile);
		
		Image img = new Image(data); 
		
		img.scaleAbsolute(130f, 125f);
		img.setTextAlignment(TextAlignment.CENTER);
//		img.setHorizontalAlignment(HorizontalAlignment.CENTER);   // --> Image Center Alignment
		cellImage.setBorder(Border.NO_BORDER);
		cellImage.add(img);
		
		mainTable.addCell(cellImage);
		
		layoutDocument.add(mainTable);
		
		layoutDocument.add(new Paragraph());
		
		// Draw Border Line
		Table drawBorderLine = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
		drawBorderLine.setWidth(520);
		
		// Cell for Border
		drawBorderLine.addCell(new Cell().add(new Paragraph())
				.setBold()
				.setBorder(Border.NO_BORDER)
				.setBackgroundColor(new DeviceRgb(143, 188, 143)));
		
		drawBorderLine.addCell(new Cell().add(new Paragraph())
				.setBold()
				.setBorder(Border.NO_BORDER)
				.setBackgroundColor(new DeviceRgb(143, 188, 143)));
		
		drawBorderLine.addCell(new Cell().add(new Paragraph())
				.setBold()
				.setBorder(Border.NO_BORDER)
				.setBackgroundColor(new DeviceRgb(143, 188, 143)));
		
		drawBorderLine.addCell(new Cell().add(new Paragraph())
				.setBold()
				.setBorder(Border.NO_BORDER)
				.setBackgroundColor(new DeviceRgb(143, 188, 143)));
		
		layoutDocument.add(drawBorderLine);

		// Drawing Line below the Logo
/*		canvas.moveTo(100, 520);
		canvas.lineTo(520, 300);
		canvas.closePathStroke();
		
	*/	

		// Table for Customer Thank You Message
		Table customerMsgTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
		// Cell for Paragraph
		Cell customerMsgCell = new Cell();
		customerMsgCell.setBold();
		customerMsgCell.setBorder(Border.NO_BORDER);
//		customerMsgCell.setBackgroundColor(null);
		
		String customerName = chargingRequestEntity.getCustName();
		String chargingPointId = chargingRequestEntity.getChargingPointEntity().getChargingPointId();
		
		// Thank You Message
		Paragraph customerNameMsgParagraph = new Paragraph(" Thank you for charging with us, " + customerName + "!");
		customerNameMsgParagraph.setBold();
		customerNameMsgParagraph.setFontColor(new DeviceRgb(34, 139, 34));
		customerNameMsgParagraph.setTextAlignment(TextAlignment.CENTER);
		customerNameMsgParagraph.setFontSize(20);
		
		customerMsgCell.add(customerNameMsgParagraph);
		
		customerMsgTable.addCell(customerMsgCell);
		
		layoutDocument.add(customerMsgTable);
		
		// Blank Space
		layoutDocument.add(new Paragraph());	
		
		// Invoice Date Format
		//Instant myInvoiceDate = chargingRequestEntity.getCreatedAt();
		
		
		
		String myInvoiceRoundOffDateAndTime = DateUtils.formattedInstantToDateTimeString(chargingRequestEntity.getCreatedAt());
		
		logger.info("The Date is :-  " + myInvoiceRoundOffDateAndTime);
		
		/*
		 
		SimpleDateFormat myInvoiceDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		myInvoiceDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		
		String dateFormatRoundOff = myInvoiceDateFormat.format(myInvoiceDate);
		
		logger.info(" The Invoice Date is : " + dateFormatRoundOff);

		 */
		
		// Charging Date and Time Format
		
		Instant chargingStartDateAndTime = chargingRequestEntity.getStartTime();
		Instant chargingStopDateAndTime = chargingRequestEntity.getStopTime();
		
		
		String chargingStartDateAndTimeRoundOff = DateUtils.formattedInstantToDateTimeString(chargingStartDateAndTime);
		String chargingStopDateAndTimeRoundOff = DateUtils.formattedInstantToDateTimeString(chargingStopDateAndTime);
		
		String roundOffChargingDateAndTime = chargingStartDateAndTimeRoundOff + " - " + chargingStopDateAndTimeRoundOff;
		
		logger.info("The Charging Date & Time is :-  " + roundOffChargingDateAndTime);
		
		//Table For Registered Address
		/*
		 * Table regeisterAddTable = new
		 * Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
		 * regeisterAddTable.setWidth(250);
		 * regeisterAddTable.setTextAlignment(TextAlignment.LEFT);
		 * regeisterAddTable.setFontSize(10);
		 * 
		 * regeisterAddTable.addCell(new Cell().add(new
		 * Paragraph("Tritan EV Dock Private Limited")) .setBold()
		 * .setBorder(Border.NO_BORDER));
		 * 
		 * regeisterAddTable.addCell(new Cell().add(new
		 * Paragraph("Tritan Dreams Building, 1st Floor, Plot No. 303 Nr. Celebration Hall, Service Road, Panchpakhadi,Thane(W)"
		 * )) .setBorder(Border.NO_BORDER));
		 * 
		 * 
		 * layoutDocument.add(regeisterAddTable);
		 */		        
		float[] columnWidth = {250,130,130};
	    Table regeisterAddTable = new Table(UnitValue.createPercentArray(columnWidth)).useAllAvailableWidth();
		 	  // regeisterAddTable.setWidth(520);
		 	   regeisterAddTable.setFontSize(10);
		 
		regeisterAddTable.addCell(new Cell().add(new Paragraph())
					      .setBorder(Border.NO_BORDER));
		
		regeisterAddTable.addCell(new Cell().add(new Paragraph())
					      .setBorder(Border.NO_BORDER));
		
		regeisterAddTable.addCell(new Cell().add(new Paragraph())
					      .setBorder(Border.NO_BORDER));
		 	   	 	   
		 regeisterAddTable.addCell(new Cell().add(new Paragraph("Tritan EV Dock Private Limited"))
				          .setBorder(Border.NO_BORDER)
						  .setBold());
		 
		/*
		 * regeisterAddTable.addCell(new Cell().add(new Paragraph())
		 * .setBorder(Border.NO_BORDER));
		 * 
		 * regeisterAddTable.addCell(new Cell().add(new Paragraph())
		 * .setBorder(Border.NO_BORDER));
		 */
		 
		 regeisterAddTable.addCell(new Cell().add(new Paragraph("Invoice No"))
				  .setBold()
			      .setBackgroundColor(new DeviceRgb(211, 211, 211)));

		//regeisterAddTable.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getId() + " _ " + 
		//RandomNumber.getRandomNumberString()+RandomStringUtil.getAlphaNumericString(4, "EVDock")))));
		 
	     regeisterAddTable.addCell(new Cell().add(new Paragraph(invoiceNO)));
		  
		 regeisterAddTable.addCell(new Cell().add(new Paragraph("Tritan Dreams Building, 1st Floor, Plot No. 303 Nr. Celebration Hall, Service Road, Panchpakhadi,Thane(W)"))
				          .setBorder(Border.NO_BORDER)); 
		 

		/*
		 * regeisterAddTable.addCell(new Cell().add(new Paragraph())
		 * .setBorder(Border.NO_BORDER));
		 */
		 
		 regeisterAddTable.addCell(new Cell().add(new Paragraph("Invoice Date"))
					.setBold()
					.setBackgroundColor(new DeviceRgb(211, 211, 211)));
			
		 regeisterAddTable.addCell(new Cell().add(new Paragraph(String.valueOf(myInvoiceRoundOffDateAndTime))));
		 
		 /*regeisterAddTable.addCell(new Cell().add(new Paragraph("GSTIN/UIN: 27AAJCT1560G1ZB"))
				          .setBold()
			              .setBorder(Border.NO_BORDER));*/
		 
		 String text = "GSTIN/UIN: 27AAJCT1560G1ZB";
		 Paragraph paragraph1 = new Paragraph();

		 Text boldText = new Text("GSTIN/UIN: ").setBold();
		 paragraph1.add(boldText);

		 Text regularText = new Text(text.substring(10));
		 paragraph1.add(regularText);

		 Cell cell1 = new Cell().add(paragraph1).setBorder(Border.NO_BORDER);
		 regeisterAddTable.addCell(cell1);
 
		 regeisterAddTable.addCell(new Cell().add(new Paragraph("Booking ID"))
					.setBold()
					.setBackgroundColor(new DeviceRgb(211, 211, 211)));
			
		 regeisterAddTable.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getId()))));
		 
		/* regeisterAddTable.addCell(new Cell().add(new Paragraph("State Name: Maharashtra, Code: 27"))
				          .setBold()
	                      .setBorder(Border.NO_BORDER));*/
		 
		 String text1 = "State Name: Maharashtra, Code: 27";
		 Paragraph paragraph2 = new Paragraph();

		 // Add the "State Name:" text with bold formatting
		 Text boldText1 = new Text("State Name: ").setBold();
		 paragraph2.add(boldText1);

		 // Add the rest of the text without bold formatting up to the comma
		 Text regularText1 = new Text(text1.substring(12, text1.indexOf(',')));
		 paragraph2.add(regularText1);

		 // Add the comma separator without bold formatting
		 Text regularText2 = new Text(",");
		 paragraph2.add(regularText2);

		 // Add the "Code:" text with bold formatting
		 Text boldText2 = new Text(" Code: ").setBold();
		 paragraph2.add(boldText2);

		 // Add the rest of the text without bold formatting after the comma and space
		 Text regularText3 = new Text(text1.substring(text1.indexOf("Code: ") + 6));
		 paragraph2.add(regularText3);

		 // Add the paragraph to the cell
		 Cell cell3 = new Cell().add(paragraph2).setBorder(Border.NO_BORDER);
		 regeisterAddTable.addCell(cell3);

		 
		 regeisterAddTable.addCell(new Cell().add(new Paragraph(" HSN/SAC "))
				 		  .setBold()
					      .setBackgroundColor(new DeviceRgb(211, 211, 211)));
			
		 regeisterAddTable.addCell(new Cell().add(new Paragraph(" 996911 ")));

		 
		/* regeisterAddTable.addCell(new Cell().add(new Paragraph("CIN: U40108MH2022PTC375945"))
				          .setBold()
                 		  .setBorder(Border.NO_BORDER));*/
		 
		 String text3 = "CIN: U40108MH2022PTC375945";
		 Paragraph paragraph3 = new Paragraph();

		 Text boldText3 = new Text("CIN: ").setBold();
		 paragraph3.add(boldText3);

		 Text regularText31 = new Text(text3.substring(4));
		 paragraph3.add(regularText31);

		 Cell cell11 = new Cell().add(paragraph3).setBorder(Border.NO_BORDER);
		 regeisterAddTable.addCell(cell11);
 
		 regeisterAddTable.addCell(new Cell().add(new Paragraph())
			      		  .setBorder(Border.NO_BORDER));
	
		 regeisterAddTable.addCell(new Cell().add(new Paragraph())
			      		  .setBorder(Border.NO_BORDER));
		 
		 /*regeisterAddTable.addCell(new Cell().add(new Paragraph("E-mail: evdockin@gmail.com"))
				          .setBold()
                          .setBorder(Border.NO_BORDER));*/
		 
		 String text31 = "E-mail: evdockin@gmail.com";
		 Paragraph paragraph31 = new Paragraph();

		 Text boldText31 = new Text("E-mail: ").setBold();
		 paragraph31.add(boldText31);

		 Text regularText311 = new Text(text31.substring(7));
		 paragraph31.add(regularText311);

		 Cell cell111 = new Cell().add(paragraph31).setBorder(Border.NO_BORDER);
		 regeisterAddTable.addCell(cell111);

		 regeisterAddTable.addCell(new Cell().add(new Paragraph())
			              .setBorder(Border.NO_BORDER));
	
		 regeisterAddTable.addCell(new Cell().add(new Paragraph())
			              .setBorder(Border.NO_BORDER));
		   
		layoutDocument.add(regeisterAddTable);
		layoutDocument.add(new Paragraph());
		
		// Table for Invoice Data
		Table invoiceDataTableVal = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth(); 
		invoiceDataTableVal.setFontSize(10);
		invoiceDataTableVal.setHorizontalAlignment(HorizontalAlignment.CENTER);
		invoiceDataTableVal.setWidth(520);
		invoiceDataTableVal.setBorder(Border.NO_BORDER);
		invoiceDataTableVal.setTextAlignment(TextAlignment.LEFT);

		// Invoice No
	/*	invoiceDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph("Invoice No"))
				.setBold()
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getId() + " _ " + 
				RandomNumber.getRandomNumberString()+RandomStringUtil.getAlphaNumericString(4, "EVDock")))));
		
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		// Invoice Date 
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph("Invoice Date"))
				.setBold()
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(String.valueOf(myInvoiceRoundOffDateAndTime))));
		
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		// Booking ID
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph("Booking ID"))
				.setBold()
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getId()))));*/
		

		invoiceDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));

		invoiceDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));

		invoiceDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		
		// Customer Details Tag
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(" Invoice To "))
				.setBorder(Border.NO_BORDER)
				.setBold()
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER)
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(" Charging Station Info "))
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT)
				.setBold()
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER)
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(" Customer Name "))
				.setBorder(Border.NO_BORDER)
				.setBold()
				.setTextAlignment(TextAlignment.LEFT));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getCustName())))
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT));

		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(" Charge Spot Name "))
				.setBold()
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getChargingPointEntity().getName())))
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT));
				
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(" Contact No "))
				.setBorder(Border.NO_BORDER)
				.setBold()
				.setTextAlignment(TextAlignment.LEFT));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getMobileNo())))
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph("Address"))
				.setBorder(Border.NO_BORDER)
				.setBold()
				.setTextAlignment(TextAlignment.LEFT));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getChargingPointEntity().getAddress() + 
				" " + chargingRequestEntity.getChargingPointEntity().getAddress2() + " " + chargingRequestEntity.getChargingPointEntity().getPincode())))
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(" Email ID "))
				.setBorder(Border.NO_BORDER)
				.setBold()
				.setTextAlignment(TextAlignment.LEFT));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getUserInfoEntity().getEmail())))
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(" Charging Date & Time "))
				.setBorder(Border.NO_BORDER)
				.setBold()
				.setTextAlignment(TextAlignment.LEFT));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(roundOffChargingDateAndTime)))
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT));

		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(" Vehicle No "))
				.setBorder(Border.NO_BORDER)
				.setBold()
				.setTextAlignment(TextAlignment.LEFT));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getVehicleNO())))
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT));
			
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(" Charging Duration (HH:MM:SS) "))
				.setBorder(Border.NO_BORDER)
				.setBold()
				.setTextAlignment(TextAlignment.LEFT));
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getChargingTime())))
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT));
		
		
		layoutDocument.add(invoiceDataTableVal);
		
		layoutDocument.add(new Paragraph());
		
		
		// Description Table
		Table chargingDescriptionTable = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
		chargingDescriptionTable.setFontSize(10);
		chargingDescriptionTable.setTextAlignment(TextAlignment.CENTER);
		chargingDescriptionTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
		chargingDescriptionTable.setWidth(520);		
		
		// Cell for Description Table
		Style cellDescriptionVal = new Style();
		cellDescriptionVal.setBorder(Border.NO_BORDER);
		cellDescriptionVal.setTextAlignment(TextAlignment.CENTER);
		
		chargingDescriptionTable.addStyle(cellDescriptionVal);
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(" Description "))
				.setTextAlignment(TextAlignment.LEFT)
				.setBold()
				.setFontSize(12)
				.setBorder(Border.NO_BORDER));
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph())
				.setTextAlignment(TextAlignment.CENTER)
				.setBold()
				.setBorder(Border.NO_BORDER));
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph())
				.setTextAlignment(TextAlignment.CENTER)
				.setBold()
				.setBorder(Border.NO_BORDER));
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph())
				.setTextAlignment(TextAlignment.CENTER)
				.setBold()
				.setBorder(Border.NO_BORDER));
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph())
				.setTextAlignment(TextAlignment.CENTER)
				.setBold()
				.setBorder(Border.NO_BORDER));
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph())
				.setTextAlignment(TextAlignment.CENTER)
				.setBold()
				.setBorder(Border.NO_BORDER));
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph())
				.setTextAlignment(TextAlignment.CENTER)
				.setBold()
				.setBorder(Border.NO_BORDER));
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph())
				.setTextAlignment(TextAlignment.CENTER)
				.setBold()
				.setBorder(Border.NO_BORDER));
		

		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(" Description "))
				.setTextAlignment(TextAlignment.CENTER)
				.setBold()
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		

		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(" Price Per Unit (INR) "))
				.setTextAlignment(TextAlignment.CENTER)
				.setBold()
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(" Unit Consumed (kWh) "))
				.setTextAlignment(TextAlignment.CENTER)
				.setBold()
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(" Amount (INR) "))
				.setTextAlignment(TextAlignment.CENTER)
				.setBold()
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(" Service Charges "))
				.setTextAlignment(TextAlignment.LEFT)
				.setBold());
		

		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(String.valueOf(oneKwhchargingPointConnectorRateDto.getChargingAmount()))));
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(String.valueOf(String.format("%.3f",chargingRequestEntity.getFinalKwh())))));
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(String.valueOf(chargingRequestEntity.getFinalAmountWithOutGst()))));
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(" CGST (9.00%)"))
				.setTextAlignment(TextAlignment.LEFT)
				.setBold());

		chargingDescriptionTable.addCell(new Cell().add(new Paragraph()));
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph()));
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(String.valueOf(chargingRequestEntity.getFinalAmountCGST()))));
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(" SGST (9.00%)"))
				.setTextAlignment(TextAlignment.LEFT)
				.setBold());

		chargingDescriptionTable.addCell(new Cell().add(new Paragraph()));
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph()));
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(String.valueOf(chargingRequestEntity.getFinalAmountSGST()))));
		
		/*
		 * chargingDescriptionTable.addCell(new Cell(1,3).add(new Paragraph())
		 * .setBorder(Border.NO_BORDER) .setBackgroundColor(new DeviceRgb(63,169,219)));
		 */
		
		/*
		 * chargingDescriptionTable.addCell(new Cell().add(new Paragraph())
		 * .setBorder(Border.NO_BORDER) .setBackgroundColor(new DeviceRgb(63,169,219)));
		 */
		
		chargingDescriptionTable.addCell(new Cell(1,3).add(new Paragraph(" Total Payable Amount "))
				.setBackgroundColor(new DeviceRgb(63,169,219))
				.setBold()
				.setTextAlignment(TextAlignment.RIGHT));
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(String.valueOf(chargingRequestEntity.getFinalAmount())))
				.setBackgroundColor(new DeviceRgb(63,169,219)));
		
		
		layoutDocument.add(chargingDescriptionTable);
		
		// Blank Space
		layoutDocument.add(new Paragraph());
		layoutDocument.add(new Paragraph());
		
		Table totalAmtMsgTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
		
		Cell totalAmtMsgCell = new Cell();
		totalAmtMsgCell.setBold();
		totalAmtMsgCell.setBorder(null);
		
		
//		double finalPaidAmount = chargingRequestEntity.getFinalAmount();
		
		Paragraph totalAmtPaidMsg = new Paragraph(" Total Amount Paid (INR) : " + chargingRequestEntity.getFinalAmount());
		totalAmtPaidMsg.setBold();
		totalAmtPaidMsg.setFontColor(new DeviceRgb(34, 139, 34));
		totalAmtPaidMsg.setTextAlignment(TextAlignment.CENTER);
		totalAmtPaidMsg.setFontSize(20);
		
		totalAmtMsgCell.add(totalAmtPaidMsg);
		
		totalAmtMsgTable.addCell(totalAmtMsgCell);
		
		layoutDocument.add(totalAmtMsgTable);
		
		//footer
		
		Table table = new Table(1).useAllAvailableWidth();
		
  		table.addCell(new Cell().add(new Paragraph())
				.setBold()
				.setBorder(Border.NO_BORDER));
  		
		  /*Cell cell = new Cell().add(new Paragraph("For any dispute, please email us at evdockin@gmail.com or Call us on Helpline No. +91 99 0391 0391")) 
  		.setBorder(Border.NO_BORDER) .setFontSize(10) .setFont(font)
  		.setTextAlignment(TextAlignment.CENTER);*/
  		
  		/*PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
  		Paragraph paragraph = new Paragraph();
  		Link link = new Link("evdockin@gmail.com", PdfAction.createURI("mailto:evdockin@gmail.com"));
  		paragraph.add(link.setFont(font).setUnderline());
  		
  		Cell cell = new Cell().add(new Paragraph("For any dispute, please email us at "))
  		        .add(paragraph)
  		        .add(new Paragraph(" or Call us on Helpline No. +91 99 0391 0391"))
  		        .setBorder(Border.NO_BORDER)
  		        .setFontSize(10)
  		        .setTextAlignment(TextAlignment.CENTER);*/
  		
		
		//new
		PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
		Paragraph paragraph = new Paragraph()
		    .add(new Text("For any dispute, please email us at "))
		    .add(new Link("evdockin@gmail.com", PdfAction.createURI("mailto:evdockin@gmail.com"))
		        .setFont(font)
		        .setUnderline()
		        .setFontColor(new DeviceRgb(0, 102, 204)))
		    .add(new Text(" or Call us on Helpline No. +91 99 0391 0391"))
		    .setBorder(new SolidBorder(new DeviceRgb(255, 255, 255), 1))
		    .setFontSize(10)
		    .setTextAlignment(TextAlignment.CENTER);

		
		
		Cell cell = new Cell().add(paragraph);
  		
  		
  		
  		table.addCell(cell);
  		
  		//layoutDocument.add(table);
  		
  		pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new TableFooterEventHandler(table));
				
		layoutDocument.close();
		
		chargingRequestEntity.setInvoiceFilePath(filename);
		chargingRequestEntity.setReceiptNo(invoiceNO);
		
		return chargingRequestEntity;      
		
	}
	

	
		private static class TableFooterEventHandler implements IEventHandler {
	        private Table table;

	        public TableFooterEventHandler(Table table) {
	            this.table = table;
	        }

	        @Override
	        public void handleEvent(Event currentEvent) {
	            PdfDocumentEvent docEvent = (PdfDocumentEvent) currentEvent;
	            PdfDocument pdfDocument = docEvent.getDocument();
	            PdfPage page = docEvent.getPage();
	            PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDocument);

	            System.out.println("======getPageSize====="+page.getPageSize().getWidth());
	            
	            new Canvas(canvas, new Rectangle(50, 0, page.getPageSize().getWidth() -100, 90))
	                    .add(table)
	                    .close();
	        }

	    }
	
}
