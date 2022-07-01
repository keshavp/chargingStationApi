package com.scube.chargingstation.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

import javax.swing.GroupLayout.Alignment;
import javax.swing.plaf.LayerUI;
import javax.swing.text.StyleConstants.ColorConstants;
import javax.swing.text.html.parser.DTD;

import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.apache.poi.hssf.util.HSSFColor.BLACK;
import org.apache.poi.hssf.util.HSSFColor.BLUE;
import org.apache.poi.hssf.util.HSSFColor.GREY_25_PERCENT;
import org.apache.poi.hssf.util.HSSFColor.WHITE;
import org.apache.poi.ss.format.CellFormatPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.itextpdf.forms.xfdf.ElementContentEncodingFormat;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import com.itextpdf.kernel.pdf.canvas.parser.listener.TextChunk;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.Border3D;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
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
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;


import com.scube.chargingstation.entity.ChargingRequestEntity;

import net.bytebuddy.agent.builder.AgentBuilder.FallbackStrategy.Simple;

@Service
public class ReceiptPdfExporter {
	
	private static final Logger logger = LoggerFactory.getLogger(ReceiptPdfExporter.class);

	private Path fileStorageLocation;
	
	private final String fileBaseLocation;
	
	
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
		
		String receiptNo = chargingRequestEntity.getId()+"_"+RandomNumber.getRandomNumberString()+RandomStringUtil.getAlphaNumericString(4, "EVDock");
		
		logger.info("Receipt No : "+ receiptNo);
		
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
		drawBorderLine.setWidth(500);
		
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
/*		canvas.moveTo(100, 500);
		canvas.lineTo(500, 300);
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
		
		
		// Table for Invoice Data
		Table invoiceDataTableVal = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth(); 
		invoiceDataTableVal.setFontSize(10);
		invoiceDataTableVal.setHorizontalAlignment(HorizontalAlignment.CENTER);
		invoiceDataTableVal.setWidth(500);
		invoiceDataTableVal.setBorder(Border.NO_BORDER);
		invoiceDataTableVal.setTextAlignment(TextAlignment.LEFT);

		// Invoice No
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph())
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
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(chargingRequestEntity.getId()))));
		

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
		
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(" Charge Point ID : "))
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT)
				.setBold()
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(chargingPointId))
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT)
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
		
		invoiceDataTableVal.addCell(new Cell().add(new Paragraph(" Address "))
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
		
		
		double finalAmount = chargingRequestEntity.getFinalAmount();
		
		double WithoutGSTAmount = finalAmount/1.18;
		
		String roundOffWithoutGSTAmount = String.format("%.2f", WithoutGSTAmount);
		
		logger.info("Amount is : " + roundOffWithoutGSTAmount);
		
		double CGSTAmount = WithoutGSTAmount*0.09;
		
		String roundOfCGSTAmount = String.format("%.2f", CGSTAmount);
		
		logger.info("Amount is : " + roundOfCGSTAmount);
		
		double SGSTAmount = WithoutGSTAmount*0.09;
		
		String roundOfSGSTAmount = String.format("%.2f", SGSTAmount);
		
		logger.info("Amount is : " + roundOfSGSTAmount);
		
		double totalAmount = finalAmount + CGSTAmount + SGSTAmount;

		String totalChargeAmount = String.format("%.2f", totalAmount);
		
		logger.info("Amount is : " + totalChargeAmount);
		
		// Description Table
		Table chargingDescriptionTable = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
		chargingDescriptionTable.setFontSize(10);
		chargingDescriptionTable.setTextAlignment(TextAlignment.CENTER);
		chargingDescriptionTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
		chargingDescriptionTable.setWidth(500);		
		
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
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph()));
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(String.valueOf(String.format("%.3f",chargingRequestEntity.getFinalKwh())))));
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(String.valueOf(roundOffWithoutGSTAmount))));
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(" CGST (9.00%)"))
				.setTextAlignment(TextAlignment.LEFT)
				.setBold());
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph()));
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph()));
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(String.valueOf(roundOfCGSTAmount))));
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(" SGST (9.00%)"))
				.setTextAlignment(TextAlignment.LEFT)
				.setBold());
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph()));
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph()));
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(String.valueOf(roundOfSGSTAmount))));
		
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
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(String.valueOf(finalAmount)))
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
		
		Paragraph totalAmtPaidMsg = new Paragraph(" Total Amount Paid (INR) : " + finalAmount);
		totalAmtPaidMsg.setBold();
		totalAmtPaidMsg.setFontColor(new DeviceRgb(34, 139, 34));
		totalAmtPaidMsg.setTextAlignment(TextAlignment.CENTER);
		totalAmtPaidMsg.setFontSize(20);
		
		totalAmtMsgCell.add(totalAmtPaidMsg);
		
		totalAmtMsgTable.addCell(totalAmtMsgCell);
		
		layoutDocument.add(totalAmtMsgTable);
		
		layoutDocument.close();
		
		chargingRequestEntity.setInvoiceFilePath(filename);
		chargingRequestEntity.setReceiptNo(receiptNo);
		
		return chargingRequestEntity;      
		
	}
}
