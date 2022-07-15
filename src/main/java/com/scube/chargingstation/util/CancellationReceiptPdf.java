package com.scube.chargingstation.util;

import java.io.IOException;
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
import com.scube.chargingstation.dto.ChargingPointConnectorRateDto;
import com.scube.chargingstation.entity.BookingRequestEntity;
import com.scube.chargingstation.entity.ChargingRequestEntity;

import net.bytebuddy.agent.builder.AgentBuilder.FallbackStrategy.Simple;

@Service
public class CancellationReceiptPdf {
	
	private static final Logger logger = LoggerFactory.getLogger(CancellationReceiptPdf.class);  

	private Path fileStorageLocation;
	
	private final String fileBaseLocation;
	
	
	 private static final DecimalFormat df = new DecimalFormat("0.00");
	
	
	/*
	 * @Autowired public PayslipPdfExporter(FileStorageProperties
	 * fileStorageProperties) { this.fileBaseLocation =
	 * fileStorageProperties.getUploadDir(); }
	 */
	
	  public CancellationReceiptPdf(FileStorageProperties fileStorageProperties) {    
		  
		  this.fileBaseLocation = fileStorageProperties.getUploadDir();
	  
	  }
	
	public final static String fileExtension = ".pdf";
	
	String imageFile = "C:/itext/logo.png"; 
	
	// private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
	// private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.RED);
	// private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.BOLD);
	 //private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
	 //private static Font smallNorm = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
	    
	public BookingRequestEntity generatePdf(BookingRequestEntity bookingRequestEntity) throws Exception{	
		
//		String filename = bookScheduleResponse.getBookingreff().concat(fileExtension);
		
		String filename = "booking_cancel_receipt_"+new Date().getTime()+"_"+bookingRequestEntity.getId()+"".concat(fileExtension);
		
		//String filename = "receipt_"+chargingRequestEntity.getId()+"".concat(fileExtension);
		
		logger.info("filename: "+filename);
		
		String newPath = this.fileBaseLocation+"/"+UploadPathContUtils.FILE_RECEIPT_DIR;
		
		this.fileStorageLocation = Paths.get(newPath).toAbsolutePath().normalize();
		 
		logger.info("file newPath: "+newPath);
		   
		Files.createDirectories(this.fileStorageLocation);
		
		String dest = newPath+"/"+filename;
		
		logger.info("file dest: "+dest);
		
		String receiptNo = bookingRequestEntity.getId()+"_"+RandomNumber.getRandomNumberString()+RandomStringUtil.getAlphaNumericString(4, "EVDock");
		
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
		
		String customerName = bookingRequestEntity.getCustName();
		
		// Thank You Message
		Paragraph customerNameMsgParagraph = new Paragraph(" Cancellation Receipt ");
		customerNameMsgParagraph.setBold();
		customerNameMsgParagraph.setFontColor(new DeviceRgb(34, 139, 34));
		customerNameMsgParagraph.setTextAlignment(TextAlignment.CENTER);
		customerNameMsgParagraph.setFontSize(20);
		
		customerMsgCell.add(customerNameMsgParagraph);
		
		customerMsgTable.addCell(customerMsgCell);
		
		layoutDocument.add(customerMsgTable);
		
		// Blank Space
		layoutDocument.add(new Paragraph());	
		
		// Receipt Date Format
		//Instant myReceiptDate = chargingRequestEntity.getCreatedAt();
		
		
		
		String myReceiptRoundOffDateAndTime = DateUtils.formattedInstantToDateTimeString(bookingRequestEntity.getBookingTime());
		
		logger.info("The Date is :-  " + myReceiptRoundOffDateAndTime);
		
		/*
		 
		SimpleDateFormat myReceiptDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		myReceiptDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		
		String dateFormatRoundOff = myReceiptDateFormat.format(myReceiptDate);
		
		logger.info(" The Receipt Date is : " + dateFormatRoundOff);

		 */
		
		// Charging Date and Time Format
		
		Instant chargingStartDateAndTime = bookingRequestEntity.getBookingTime();
		Instant chargingStopDateAndTime = bookingRequestEntity.getBookingEndtime();
		
		
		String chargingStartDateAndTimeRoundOff = DateUtils.formattedInstantToDateTimeString(chargingStartDateAndTime);
		String chargingStopDateAndTimeRoundOff = DateUtils.formattedInstantToDateTimeString(chargingStopDateAndTime);
		
		String roundOffChargingDateAndTime = chargingStartDateAndTimeRoundOff + " - " + chargingStopDateAndTimeRoundOff;
		
		logger.info("The Charging Date & Time is :-  " + roundOffChargingDateAndTime);
		
		
		// Table for Receipt Data
		Table receiptDataTableVal = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth(); 
		receiptDataTableVal.setFontSize(10);
		receiptDataTableVal.setHorizontalAlignment(HorizontalAlignment.CENTER);
		receiptDataTableVal.setWidth(500);
		receiptDataTableVal.setBorder(Border.NO_BORDER);
		receiptDataTableVal.setTextAlignment(TextAlignment.LEFT);

		// Receipt No
		receiptDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph("Receipt No"))
				.setBold()
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(bookingRequestEntity.getId() + " _ " + 
				RandomNumber.getRandomNumberString()+RandomStringUtil.getAlphaNumericString(4, "EVDock")))));
		
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		// Receipt Date 
		receiptDataTableVal.addCell(new Cell().add(new Paragraph("Receipt Date"))
				.setBold()
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph(String.valueOf(myReceiptRoundOffDateAndTime))));
		
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		// Booking ID
		receiptDataTableVal.addCell(new Cell().add(new Paragraph("Booking ID"))
				.setBold()
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(bookingRequestEntity.getId()))));
		

		receiptDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));

		receiptDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));

		receiptDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER));
		
		
		// Customer Details Tag
		receiptDataTableVal.addCell(new Cell().add(new Paragraph(" Receipt To "))
				.setBorder(Border.NO_BORDER)
				.setBold()
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		receiptDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER)
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		receiptDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER)
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		receiptDataTableVal.addCell(new Cell().add(new Paragraph())
				.setBorder(Border.NO_BORDER)
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		/*
		 * receiptDataTableVal.addCell(new Cell().add(new
		 * Paragraph(" Charge Point ID : ")) .setBorder(Border.NO_BORDER)
		 * .setTextAlignment(TextAlignment.LEFT) .setBold() .setBackgroundColor(new
		 * DeviceRgb(211, 211, 211)));
		 * 
		 * 
		 * receiptDataTableVal.addCell(new Cell().add(new Paragraph(chargingPointId))
		 * .setBorder(Border.NO_BORDER) .setTextAlignment(TextAlignment.LEFT)
		 * .setBackgroundColor(new DeviceRgb(211, 211, 211)));
		 */
		
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph(" Customer Name "))
				.setBorder(Border.NO_BORDER)
				.setBold()
				.setTextAlignment(TextAlignment.LEFT));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(bookingRequestEntity.getCustName())))
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT));

		receiptDataTableVal.addCell(new Cell().add(new Paragraph(" Charge Spot Name "))
				.setBold()
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(bookingRequestEntity.getChargingPointEntity().getName())))
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT));
				
		receiptDataTableVal.addCell(new Cell().add(new Paragraph(" Contact No "))
				.setBorder(Border.NO_BORDER)
				.setBold()
				.setTextAlignment(TextAlignment.LEFT));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(bookingRequestEntity.getMobileNo())))
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph(" Address "))
				.setBorder(Border.NO_BORDER)
				.setBold()
				.setTextAlignment(TextAlignment.LEFT));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(bookingRequestEntity.getChargingPointEntity().getAddress() + 
				" " + bookingRequestEntity.getChargingPointEntity().getAddress2() + " " + bookingRequestEntity.getChargingPointEntity().getPincode())))
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph(" Email ID "))
				.setBorder(Border.NO_BORDER)
				.setBold()
				.setTextAlignment(TextAlignment.LEFT));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(bookingRequestEntity.getUserInfoEntity().getEmail())))
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT));
		
		/*
		 * receiptDataTableVal.addCell(new Cell().add(new
		 * Paragraph(" Charging Date & Time ")) .setBorder(Border.NO_BORDER) .setBold()
		 * .setTextAlignment(TextAlignment.LEFT));
		 * 
		 * receiptDataTableVal.addCell(new Cell().add(new
		 * Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(
		 * roundOffChargingDateAndTime))) .setBorder(Border.NO_BORDER)
		 * .setTextAlignment(TextAlignment.LEFT));
		 */

		receiptDataTableVal.addCell(new Cell().add(new Paragraph(" Vehicle No "))
				.setBorder(Border.NO_BORDER)
				.setBold()
				.setTextAlignment(TextAlignment.LEFT));
		
		receiptDataTableVal.addCell(new Cell().add(new Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(bookingRequestEntity.getVehicleNO())))
				.setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.LEFT));
			
		/*
		 * receiptDataTableVal.addCell(new Cell().add(new
		 * Paragraph(" Charging Duration (HH:MM:SS) ")) .setBorder(Border.NO_BORDER)
		 * .setBold() .setTextAlignment(TextAlignment.LEFT));
		 * 
		 * receiptDataTableVal.addCell(new Cell().add(new
		 * Paragraph(StringNullEmpty.stringNullAndEmptyToBlank(DateUtils.
		 * formattedInstantToDateTimeString(bookingRequestEntity.getBookingTime()))))
		 * .setBorder(Border.NO_BORDER) .setTextAlignment(TextAlignment.LEFT));
		 */
		
		
		layoutDocument.add(receiptDataTableVal);
		
		layoutDocument.add(new Paragraph());
		
		
		double bookingAmountRound = RoundUtil.doubleRound(bookingRequestEntity.getBookingAmount() ,2);
		
		double cAmountRound = RoundUtil.doubleRound(bookingAmountRound / 1.18 ,2);
		
		double cgstAmountRound = RoundUtil.doubleRound(cAmountRound * 0.09 ,2) ;
		
		double sgstAmountRound = RoundUtil.doubleRound(cAmountRound * 0.09 ,2) ;
		
		
		
		// Description Table
		Table chargingDescriptionTable = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
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
		
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(" Amount (INR) "))
				.setTextAlignment(TextAlignment.CENTER)
				.setBold()
				.setBackgroundColor(new DeviceRgb(211, 211, 211)));
		
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(" Cancellation Charges ")).setTextAlignment(TextAlignment.LEFT).setBold());
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(String.valueOf(cAmountRound))));
		
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(" CGST (9.00%)")).setTextAlignment(TextAlignment.LEFT).setBold());
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(String.valueOf(cgstAmountRound))));
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(" SGST (9.00%)")).setTextAlignment(TextAlignment.LEFT).setBold());
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(String.valueOf(sgstAmountRound))));
		
		/*
		 * chargingDescriptionTable.addCell(new Cell(1,3).add(new Paragraph())
		 * .setBorder(Border.NO_BORDER) .setBackgroundColor(new DeviceRgb(63,169,219)));
		 */
		
		/*
		 * chargingDescriptionTable.addCell(new Cell().add(new Paragraph())
		 * .setBorder(Border.NO_BORDER) .setBackgroundColor(new DeviceRgb(63,169,219)));
		 */
		
		/*
		 * chargingDescriptionTable.addCell(new Cell(1,3).add(new
		 * Paragraph(" Total Payable Amount ")) .setBackgroundColor(new
		 * DeviceRgb(63,169,219)) .setBold() .setTextAlignment(TextAlignment.RIGHT));
		 */
		
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(" Total Payable Amount ")).setBackgroundColor(new DeviceRgb(63,169,219)).setBold().setTextAlignment(TextAlignment.RIGHT));
		chargingDescriptionTable.addCell(new Cell().add(new Paragraph(String.valueOf(bookingAmountRound))).setBackgroundColor(new DeviceRgb(63,169,219)));
		
		
		layoutDocument.add(chargingDescriptionTable);
		
		// Blank Space
		layoutDocument.add(new Paragraph());
		layoutDocument.add(new Paragraph());
		
		Table totalAmtMsgTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
		
		Cell totalAmtMsgCell = new Cell();
		totalAmtMsgCell.setBold();
		totalAmtMsgCell.setBorder(null);
		
		
//		double finalPaidAmount = chargingRequestEntity.getFinalAmount();
		
		Paragraph totalAmtPaidMsg = new Paragraph(" Total Amount Paid (INR) : " + bookingRequestEntity.getBookingAmount());
		totalAmtPaidMsg.setBold();
		totalAmtPaidMsg.setFontColor(new DeviceRgb(34, 139, 34));
		totalAmtPaidMsg.setTextAlignment(TextAlignment.CENTER);
		totalAmtPaidMsg.setFontSize(20);
		
		totalAmtMsgCell.add(totalAmtPaidMsg);
		
		totalAmtMsgTable.addCell(totalAmtMsgCell);
		
		layoutDocument.add(totalAmtMsgTable);
		
		layoutDocument.close();
		
		bookingRequestEntity.setInvoiceFilePath(filename);
		bookingRequestEntity.setReceiptNo(receiptNo);
		
		return bookingRequestEntity;      
		
	}
}
