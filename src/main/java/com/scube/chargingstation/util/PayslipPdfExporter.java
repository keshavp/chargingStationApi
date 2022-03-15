/*
 * package com.scube.chargingstation.util;
 * 
 * import java.awt.Color; import java.io.FileOutputStream; import
 * java.nio.file.Path; import java.nio.file.Paths; import java.util.List; import
 * java.util.Set;
 * 
 * import javax.servlet.http.HttpServletResponse;
 * 
 * import org.slf4j.Logger; import org.slf4j.LoggerFactory; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.beans.factory.annotation.Value; import
 * org.springframework.core.io.Resource; import
 * org.springframework.core.io.UrlResource; import
 * org.springframework.stereotype.Service;
 * 
 * import com.lowagie.text.Chunk; import com.lowagie.text.Document; import
 * com.lowagie.text.Element; import com.lowagie.text.Font; import
 * com.lowagie.text.FontFactory; import com.lowagie.text.HeaderFooter; import
 * com.lowagie.text.Image; import com.lowagie.text.PageSize; import
 * com.lowagie.text.Paragraph; import com.lowagie.text.Phrase; import
 * com.lowagie.text.Rectangle; import com.lowagie.text.pdf.PdfPCell; import
 * com.lowagie.text.pdf.PdfPTable; import com.lowagie.text.pdf.PdfWriter; import
 * com.scube.chargingstation.dto.ConfigCompanyDto; import
 * com.scube.chargingstation.dto.ConfigureSalaryAdjustmentDto; import
 * com.scube.chargingstation.dto.DepartmentDto; import
 * com.scube.chargingstation.dto.DesignationDto; import
 * com.scube.chargingstation.dto.SalaryAdjustmentDto; import
 * com.scube.chargingstation.dto.SalarySetupDto; import
 * com.scube.chargingstation.dto.SalarySetupWithCTCDto; import
 * com.scube.chargingstation.exception.BRSException; import
 * com.scube.chargingstation.model.ConfigureSalaryAdjustmentEntity; import
 * com.scube.chargingstation.model.EmpInfoEntity; import
 * com.scube.chargingstation.model.SalaryProcessEmployeeEntity; import
 * com.scube.chargingstation.service.EmpInfoService; import
 * com.scube.chargingstation.service.SalaryAdjustmentService; import
 * com.scube.chargingstation.service.SalarySetupService; import
 * com.scube.chargingstation.service.SalaryStructureService;
 * 
 * import static com.scube.chargingstation.exception.EntityType.PAYSLIP; import
 * static com.scube.chargingstation.exception.ExceptionType.ENTITY_NOT_FOUND;
 * 
 * @Service public class PayslipPdfExporter {
 * 
 * private static final Logger logger =
 * LoggerFactory.getLogger(PayslipPdfExporter.class);
 * 
 * @Value("${file.storecompanylogo-dir}") private String companyLogoLocation;
 * 
 * @Autowired EmpInfoService empInfoService;
 * 
 * @Autowired SalaryStructureService salaryStructureService;
 * 
 * @Autowired SalarySetupService salarySetupService;
 * 
 * @Autowired SalaryAdjustmentService salaryAdjustmentService;
 * 
 * 
 * public void export(HttpServletResponse response, ConfigCompanyDto company,
 * String empcode, String month, String year) throws Exception { try {
 * 
 * Document document = new Document(PageSize.A4, 30, 30, 50, 50);
 * 
 * logger.info("pdf exporter--------->1"); document.setMargins(10, 10, 10, 10);
 * PdfWriter writer = PdfWriter.getInstance(document,
 * response.getOutputStream());
 * 
 * logger.info("pdf exporter--------->2");
 * 
 * // FONT SECTION Font headingFont15 =
 * FontFactory.getFont(FontFactory.HELVETICA_BOLD); headingFont15.setSize(15);
 * headingFont15.setColor(Color.BLACK);
 * 
 * Font font9 = FontFactory.getFont(FontFactory.HELVETICA); font9.setSize(9);
 * font9.setColor(Color.BLACK);
 * 
 * Font font11 = FontFactory.getFont(FontFactory.HELVETICA); font11.setSize(11);
 * font11.setColor(Color.BLACK);
 * 
 * Font font9bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
 * font9bold.setSize(9); font9bold.setColor(Color.BLACK);
 * 
 * logger.info("Fonts set above-------->3");
 * 
 * HeaderFooter footer = new HeaderFooter( new
 * Phrase("System generated document does not require signature.", font9),
 * false); footer.setAlignment(Element.ALIGN_CENTER);
 * footer.setBorder(Rectangle.NO_BORDER); document.setFooter(footer);
 * 
 * logger.info("footer set above-------->4");
 * 
 * document.open();
 * 
 * // input company img here Image logo; Path fileStorageLocation =
 * Paths.get(companyLogoLocation + "/" +
 * company.getLogo_url()).toAbsolutePath().normalize();
 * 
 * // Path filePath = fileStorageLocation.resolve(fileName).normalize();
 * 
 * Resource resource = new UrlResource(fileStorageLocation.toUri());
 * 
 * if(resource.exists()) { logo =
 * Image.getInstance(String.valueOf(fileStorageLocation));
 * logo.scaleAbsolute(100, 75); logo.setAbsolutePosition(10, 750);
 * 
 * document.add(logo); } // logger.info("--image------->"+companyLogoLocation +
 * "/" + company.getLogo_url()); //
 * logger.info("--image------->"+fileStorageLocation);
 * 
 * // input company address String name = company.getCompanyname(); String addr1
 * = company.getAddr1(); String location = company.getLocation(); String city =
 * company.getCity(); String state = company.getState(); String country =
 * company.getCountry(); String pincode = company.getPicode(); // String addr2 =
 * company.getAddr2(); // String addr3 = company.getAddr3();
 * 
 * Paragraph compName = new Paragraph(); compName.setFont(headingFont15);
 * compName.add(name);
 * 
 * Paragraph address = new Paragraph(); address.setFont(font9);
 * address.add(addr1 + "\r");
 * address.add(location+", "+city+" - "+pincode+"\r");
 * address.add(state+", "+country+". \r"); // address.add(addr2 + "\r"); //
 * address.add(addr3 + "\r"); compName.setAlignment(Paragraph.ALIGN_CENTER);
 * address.setAlignment(Paragraph.ALIGN_CENTER); address.add(Chunk.NEWLINE);
 * 
 * document.add(compName); document.add(address);
 * 
 * Paragraph subject = new Paragraph();
 * subject.setAlignment(Paragraph.ALIGN_CENTER); subject.setFont(font11); String
 * monthh = month; // if(month.equalsIgnoreCase("01") ||
 * month.equalsIgnoreCase("1")) { // monthh = "January"; // } //
 * if(month.equalsIgnoreCase("02") || month.equalsIgnoreCase("2")) { // monthh =
 * "February"; // } // if(month.equalsIgnoreCase("03") ||
 * month.equalsIgnoreCase("2")) { // monthh = "March"; // } //
 * if(month.equalsIgnoreCase("04") || month.equalsIgnoreCase("2")) { // monthh =
 * "April"; // } // if(month.equalsIgnoreCase("05") ||
 * month.equalsIgnoreCase("5")) { // monthh = "May"; // } //
 * if(month.equalsIgnoreCase("06") || month.equalsIgnoreCase("6")) { // monthh =
 * "June"; // } // if(month.equalsIgnoreCase("07") ||
 * month.equalsIgnoreCase("7")) { // monthh = "July"; // } //
 * if(month.equalsIgnoreCase("08") || month.equalsIgnoreCase("8")) { // monthh =
 * "August"; // } // if(month.equalsIgnoreCase("09") ||
 * month.equalsIgnoreCase("9")) { // monthh = "September"; // } //
 * if(month.equalsIgnoreCase("10")) { // monthh = "October"; // } //
 * if(month.equalsIgnoreCase("11")) { // monthh = "November"; // } //
 * if(month.equalsIgnoreCase("12")) { // monthh = "December"; // }
 * subject.add("Pay Slip for the month of "+ monthh); subject.add( " "+year);
 * subject.add(Chunk.NEWLINE); subject.add(Chunk.NEWLINE);
 * document.add(subject);
 * 
 * // Personal info box
 * 
 * EmpInfoEntity empInfo = empInfoService.getUserByEmpCode(empcode);
 * 
 * PdfPTable coverTable = new PdfPTable(1); coverTable.setWidthPercentage(100);
 * // coverTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
 * 
 * PdfPTable perTable = new PdfPTable(6); perTable.setWidthPercentage(100);
 * perTable.setWidths(new int[] {16,17,16,17,16,18});
 * 
 * // fetch employee info
 * 
 * PdfPCell cell1 = new PdfPCell(); cell1.addElement(new Phrase("Name : ",
 * font9bold)); cell1.setBorder(Rectangle.NO_BORDER);
 * cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell1.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell1Val = new PdfPCell(); cell1Val.addElement(new
 * Phrase(empInfo.getPer_first_name()+" "+empInfo.getPer_last_name(), font9));
 * cell1Val.setBorder(Rectangle.NO_BORDER);
 * cell1Val.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell1Val.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell2 = new PdfPCell(new Phrase("Employee Code : ", font9bold));
 * cell2.setBorder(Rectangle.NO_BORDER);
 * cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell2Val = new PdfPCell(); cell2Val.addElement(new Phrase(empcode,
 * font9)); cell2Val.setBorder(Rectangle.NO_BORDER);
 * cell2Val.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell2Val.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell3 = new PdfPCell(new Phrase("Designation : ", font9bold));
 * cell3.setBorder(Rectangle.NO_BORDER);
 * cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell3Val = new PdfPCell(); cell3Val.addElement(new
 * Phrase(empInfo.getDesignationEntity().getName(), font9));
 * cell3Val.setBorder(Rectangle.NO_BORDER);
 * cell3Val.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell3Val.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell4 = new PdfPCell(new Phrase("Date Of Joining : ", font9bold));
 * cell4.setBorder(Rectangle.NO_BORDER);
 * cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell4.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell4Val = new PdfPCell(); cell4Val.addElement(new
 * Phrase(empInfo.getOff_date_of_joining(), font9));
 * cell4Val.setBorder(Rectangle.NO_BORDER);
 * cell4Val.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell4Val.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell5 = new PdfPCell(new Phrase("Department : ", font9bold));
 * cell5.setBorder(Rectangle.NO_BORDER);
 * cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell5.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell5Val = new PdfPCell(); cell5Val.addElement(new
 * Phrase(empInfo.getDepartmentEntity().getName(), font9));
 * cell5Val.setBorder(Rectangle.NO_BORDER);
 * cell5Val.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell5Val.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell6 = new PdfPCell(new Phrase("Location : ", font9bold));
 * cell6.setBorder(Rectangle.NO_BORDER);
 * cell6.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell6.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell6Val = new PdfPCell(); cell6Val.addElement(new
 * Phrase(empInfo.getLocationEntity().getName(), font9));
 * cell6Val.setBorder(Rectangle.NO_BORDER);
 * cell6Val.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell6Val.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell7 = new PdfPCell(new Paragraph("PAN : ", font9bold));
 * cell7.setBorder(Rectangle.NO_BORDER);
 * cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell7.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell7Val = new PdfPCell(); cell7Val.addElement(new
 * Phrase(empInfo.getBank_pan_card_number(), font9));
 * cell7Val.setBorder(Rectangle.NO_BORDER);
 * cell7Val.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell7Val.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell8 = new PdfPCell(); cell8 = new PdfPCell(new
 * Phrase("A/C Number : ", font9bold)); cell8.setBorder(0);
 * cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell8.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell8Val = new PdfPCell(); cell8Val.setBorder(Rectangle.NO_BORDER);
 * cell8Val.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell8Val.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * if(empInfo.getBank_account_number() != null) { cell8Val.addElement(new
 * Phrase(empInfo.getBank_account_number(), font9)); }else { cell8Val = new
 * PdfPCell(new Phrase("")); } cell8Val.setBorder(Rectangle.NO_BORDER);
 * 
 * PdfPCell cell9 = new PdfPCell(); cell9.addElement(new Phrase("Bank Name : ",
 * font9bold)); cell9.setBorder(0);
 * cell9.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell9.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell9Val = new PdfPCell();
 * cell9Val.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell9Val.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * if(empInfo.getBank_name() != null) { cell9Val.addElement(new
 * Phrase(empInfo.getBank_name(), font9)); }else { cell9Val = new PdfPCell(new
 * Phrase("")); } cell9Val.setBorder(Rectangle.NO_BORDER);
 * 
 * PdfPCell cell10 = new PdfPCell(new Phrase("UAN : ", font9bold));
 * cell10.setBorder(Rectangle.NO_BORDER);
 * cell10.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell10.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell10Val = new PdfPCell(); cell10Val.addElement(new Phrase("",
 * font9)); cell10Val.setBorder(Rectangle.NO_BORDER);
 * cell10Val.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell10Val.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell11 = new PdfPCell(new Phrase("PF Number : ", font9bold));
 * cell11.setBorder(Rectangle.NO_BORDER);
 * cell11.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell11.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell11Val = new PdfPCell(); cell11Val.addElement(new Phrase("",
 * font9)); cell11Val.setBorder(Rectangle.NO_BORDER);
 * cell11Val.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell11Val.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell12 = new PdfPCell(new Paragraph(""));
 * cell12.setBorder(Rectangle.NO_BORDER);
 * cell12.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell12.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * PdfPCell cell12Val = new PdfPCell(); cell12Val.addElement(new Phrase("",
 * font9)); cell12Val.setBorder(Rectangle.NO_BORDER);
 * cell12Val.setHorizontalAlignment(Element.ALIGN_LEFT);
 * cell12Val.setVerticalAlignment(Element.ALIGN_BOTTOM);
 * 
 * perTable.addCell(cell1); perTable.addCell(cell1Val); perTable.addCell(cell2);
 * perTable.addCell(cell2Val); perTable.addCell(cell3);
 * perTable.addCell(cell3Val); perTable.addCell(cell4);
 * perTable.addCell(cell4Val); perTable.addCell(cell5);
 * perTable.addCell(cell5Val); perTable.addCell(cell6);
 * perTable.addCell(cell6Val); perTable.addCell(cell7);
 * perTable.addCell(cell7Val); perTable.addCell(cell8);
 * perTable.addCell(cell8Val); perTable.addCell(cell9);
 * perTable.addCell(cell9Val); perTable.addCell(cell10);
 * perTable.addCell(cell10Val); perTable.addCell(cell11);
 * perTable.addCell(cell11Val); perTable.addCell(cell12);
 * perTable.addCell(cell12Val);
 * 
 * coverTable.addCell(perTable);
 * 
 * Paragraph spacing = new Paragraph(); spacing.add(Chunk.NEWLINE);
 * 
 * document.add(coverTable); document.add(spacing);
 * 
 * 
 * // EARNINGS DEDUCTIONS TABLE PdfPTable outerEarnDed = new PdfPTable(2);
 * outerEarnDed.setWidthPercentage(100); outerEarnDed.setWidths(new int[]
 * {60,40});
 * 
 * // Fetch all parameters from salary_setup_emp table List<SalarySetupDto>
 * setupDtoList = salaryStructureService.getSalarySetupList(empcode);
 * logger.info("salary structure size--->"+ setupDtoList.size());
 * 
 * // Fetch all active parameters from salary_process_emp
 * 
 * SalaryProcessEmployeeEntity salProcessEmp =
 * salarySetupService.getSalaryProcessEmpEnt(empcode, monthh, year);
 * 
 * if(salProcessEmp == null) {
 * logger.error("No payslip available for year = "+year+" and month = "+month);
 * throw BRSException.throwException(PAYSLIP, ENTITY_NOT_FOUND, month, year,
 * empcode); }
 * 
 * // ############Earnings table##############
 * 
 * PdfPTable innerEarn = new PdfPTable(3); perTable.setWidthPercentage(100);
 * 
 * PdfPCell ernEarn = new PdfPCell(); ernEarn.addElement(new Phrase("Earnings",
 * font9bold));
 * 
 * PdfPCell ernMonth = new PdfPCell(); ernMonth.addElement(new
 * Phrase("Monthly Amount", font9bold));
 * 
 * PdfPCell ernActual = new PdfPCell(); ernActual.addElement(new
 * Phrase("Actual Amount", font9bold));
 * 
 * innerEarn.addCell(ernEarn); innerEarn.addCell(ernMonth);
 * innerEarn.addCell(ernActual);
 * 
 * for(int i=0;i < setupDtoList.size(); i++) {
 * if(setupDtoList.get(i).getType().equalsIgnoreCase("Earning")) {
 * 
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Basic")) { PdfPCell celll
 * = new PdfPCell(); celll.addElement(new Phrase(setupDtoList.get(i).getName(),
 * font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_basic().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("HRA")) { PdfPCell celll =
 * new PdfPCell(); celll.addElement(new Phrase(setupDtoList.get(i).getName(),
 * font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_hra().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("DA")) { PdfPCell celll =
 * new PdfPCell(); celll.addElement(new Phrase(setupDtoList.get(i).getName(),
 * font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_da().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Transport")) { PdfPCell
 * celll = new PdfPCell(); celll.addElement(new
 * Phrase(setupDtoList.get(i).getName(), font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_transport().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Medical Reimbursment")) {
 * PdfPCell celll = new PdfPCell(); celll.addElement(new
 * Phrase(setupDtoList.get(i).getName(), font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_medical_reimbursment().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Incentive")) { PdfPCell
 * celll = new PdfPCell(); celll.addElement(new
 * Phrase(setupDtoList.get(i).getName(), font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_incentive().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Reimbursment")) { PdfPCell
 * celll = new PdfPCell(); celll.addElement(new
 * Phrase(setupDtoList.get(i).getName(), font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_reimbursment().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Leave Encashment")) {
 * PdfPCell celll = new PdfPCell(); celll.addElement(new
 * Phrase(setupDtoList.get(i).getName(), font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_leave_encashment().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); } if(setupDtoList.get(i).getName().
 * equalsIgnoreCase("Child Education Allowances")) { PdfPCell celll = new
 * PdfPCell(); celll.addElement(new Phrase(setupDtoList.get(i).getName(),
 * font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_child_educations_allowance().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Uniform Allowances")) {
 * PdfPCell celll = new PdfPCell(); celll.addElement(new
 * Phrase(setupDtoList.get(i).getName(), font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_uniform_allowance().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Food Allowances")) {
 * PdfPCell celll = new PdfPCell(); celll.addElement(new
 * Phrase(setupDtoList.get(i).getName(), font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_food_allowance().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("LTA")) { PdfPCell celll =
 * new PdfPCell(); celll.addElement(new Phrase(setupDtoList.get(i).getName(),
 * font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_lta().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Driver")) { PdfPCell celll
 * = new PdfPCell(); celll.addElement(new Phrase(setupDtoList.get(i).getName(),
 * font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_driver().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Meal Voucher")) { PdfPCell
 * celll = new PdfPCell(); celll.addElement(new
 * Phrase(setupDtoList.get(i).getName(), font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_meal_voucher().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Variable Earnings")) {
 * PdfPCell celll = new PdfPCell(); celll.addElement(new
 * Phrase(setupDtoList.get(i).getName(), font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_variable_earnings().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Gratuity")) { PdfPCell
 * celll = new PdfPCell(); celll.addElement(new
 * Phrase(setupDtoList.get(i).getName(), font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_gratuity().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Gift Pass")) { PdfPCell
 * celll = new PdfPCell(); celll.addElement(new
 * Phrase(setupDtoList.get(i).getName(), font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_gift_pass().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Arrears")) { PdfPCell
 * celll = new PdfPCell(); celll.addElement(new
 * Phrase(setupDtoList.get(i).getName(), font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_arrears().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Performance Incentive")) {
 * PdfPCell celll = new PdfPCell(); celll.addElement(new
 * Phrase(setupDtoList.get(i).getName(), font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_performance_incentive().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Bonus")) { PdfPCell celll
 * = new PdfPCell(); celll.addElement(new Phrase(setupDtoList.get(i).getName(),
 * font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_bonus().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Overtime")) { PdfPCell
 * celll = new PdfPCell(); celll.addElement(new
 * Phrase(setupDtoList.get(i).getName(), font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_overtime().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Car")) { PdfPCell celll =
 * new PdfPCell(); celll.addElement(new Phrase(setupDtoList.get(i).getName(),
 * font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_car().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); }
 * 
 * 
 * 
 * } if(setupDtoList.get(i).getType().equalsIgnoreCase("Other")) { // other
 * earnings if loop
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Other Earnings")) {
 * PdfPCell celll = new PdfPCell(); celll.addElement(new
 * Phrase(setupDtoList.get(i).getName(), font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new
 * Phrase(salProcessEmp.getEar_others().toString(), font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); } } }
 * 
 * // Add adjustments here if present
 * 
 * Set<ConfigureSalaryAdjustmentDto> configuredAdjustments =
 * salaryAdjustmentService.getConfigureAdjustmentsByEmpcodeAndLotcode(empcode,
 * salProcessEmp.getLotscode());
 * 
 * if(configuredAdjustments != null) { for(ConfigureSalaryAdjustmentDto ent:
 * configuredAdjustments) {
 * 
 * SalaryAdjustmentDto adj =
 * salaryAdjustmentService.getAdjustmentById(ent.getFk_salaryadjusment());
 * 
 * PdfPCell celll = new PdfPCell(); celll.addElement(new Phrase(adj.getName(),
 * font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new Phrase("", font9));
 * 
 * PdfPCell celll2 = new PdfPCell(); celll2.addElement(new Phrase(ent.getSum(),
 * font9));
 * 
 * innerEarn.addCell(celll); innerEarn.addCell(celll1);
 * innerEarn.addCell(celll2); } }
 * 
 * SalarySetupWithCTCDto setupDto =
 * salarySetupService.getSalarySetupOnLode(empcode);
 * 
 * PdfPCell totalMonthly = new PdfPCell(); totalMonthly.addElement(new
 * Phrase("Total Earnings(Rs.)", font9bold));
 * totalMonthly.setBackgroundColor(new Color(211,211,211));
 * 
 * PdfPCell totalMon = new PdfPCell(); totalMon.addElement(new
 * Phrase(setupDto.getMontlyCtc(), font9bold)); totalMon.setBackgroundColor(new
 * Color(211,211,211));
 * 
 * PdfPCell totalYearly = new PdfPCell(); totalYearly.addElement(new
 * Phrase(salProcessEmp.getNet_salary().toString(), font9bold));
 * totalYearly.setBackgroundColor(new Color(211,211,211));
 * 
 * innerEarn.addCell(totalMonthly); innerEarn.addCell(totalMon);
 * innerEarn.addCell(totalYearly);
 * 
 * 
 * // ###########Deductions table###########
 * 
 * PdfPTable innerDed = new PdfPTable(2); innerDed.setWidthPercentage(100);
 * 
 * PdfPCell dedDeds = new PdfPCell(); dedDeds.addElement(new
 * Phrase("Deductions", font9bold));
 * 
 * PdfPCell dedAmount = new PdfPCell(); dedAmount.addElement(new
 * Phrase("Amount", font9bold));
 * 
 * innerDed.addCell(dedDeds); innerDed.addCell(dedAmount);
 * 
 * for(int i=0;i < setupDtoList.size(); i++) {
 * 
 * if(setupDtoList.get(i).getType().equalsIgnoreCase("Deduction")) { //
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("PF")) { PdfPCell celll =
 * new PdfPCell(); celll.addElement(new Phrase(setupDtoList.get(i).getName(),
 * font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(setupDtoList.get(i).getSalaryStructureDetailDto().getMonthlyAmount(),
 * font9));
 * 
 * innerDed.addCell(celll); innerDed.addCell(celll1); // } }
 * if(setupDtoList.get(i).getType().equalsIgnoreCase("Other")) { // other
 * earnings if loop
 * if(setupDtoList.get(i).getName().equalsIgnoreCase("Other Deductions")) {
 * PdfPCell celll = new PdfPCell(); celll.addElement(new
 * Phrase(setupDtoList.get(i).getName(), font9));
 * 
 * PdfPCell celll1 = new PdfPCell(); celll1.addElement(new
 * Phrase(salProcessEmp.getDed_other().toString(), font9));
 * 
 * innerDed.addCell(celll); innerDed.addCell(celll1); } } }
 * 
 * PdfPCell totalDed = new PdfPCell(); totalDed.addElement(new
 * Phrase("Total Deductions(Rs.)", font9bold)); totalDed.setBackgroundColor(new
 * Color(211,211,211));
 * 
 * PdfPCell totalDedAmt = new PdfPCell(); totalDedAmt.addElement(new
 * Phrase(String.valueOf(salProcessEmp.getDed_total()), font9bold));
 * totalDedAmt.setBackgroundColor(new Color(211,211,211)); //
 * totalDedAmt.addElement(new
 * Paragraph(salProcessEmp.getDed_total().toString())); // ask keshav how to get
 * total deduction value from salary_process_emp here
 * 
 * innerDed.addCell(totalDed); innerDed.addCell(totalDedAmt);
 * 
 * outerEarnDed.getDefaultCell().setBorder(0); outerEarnDed.addCell(innerEarn);
 * outerEarnDed.addCell(innerDed);
 * 
 * document.add(outerEarnDed);
 * 
 * Paragraph spacingOne = new Paragraph(); spacingOne.add(Chunk.NEWLINE);
 * spacingOne.add(Chunk.NEWLINE);
 * 
 * PdfPTable outerNetTable = new PdfPTable(1);
 * outerNetTable.setWidthPercentage(100); //
 * outerNetTable.getDefaultCell().setBorder(0);
 * 
 * PdfPTable netTable = new PdfPTable(3); netTable.setWidthPercentage(100);
 * netTable.getDefaultCell().setBorder(0);
 * 
 * netTable.addCell(""); netTable.addCell(""); netTable.addCell(new
 * Phrase("Net Total:     "+ salProcessEmp.getTol_net_salary(), font9bold)); //
 * netTable.addCell(""); // netTable.addCell(""); // netTable.addCell(new
 * Phrase("Reimbursment:  "+ salProcessEmp.getEar_reimbursment(), font9));
 * netTable.addCell(""); netTable.addCell(""); netTable.addCell(new
 * Phrase("Total Pay:     "+
 * (salProcessEmp.getTol_net_salary().add(salProcessEmp.getEar_reimbursment())),
 * font9bold));
 * 
 * outerNetTable.addCell(netTable); document.add(outerNetTable);
 * 
 * Paragraph spacingTwo = new Paragraph(); spacingTwo.add(Chunk.NEWLINE);
 * 
 * document.add(spacingTwo);
 * 
 * // PdfPTable outerLastTable = new PdfPTable(1); //
 * outerLastTable.setWidthPercentage(100); ////
 * outerNetTable.getDefaultCell().setBorder(0); // // PdfPTable lastTable = new
 * PdfPTable(3); // lastTable.setWidthPercentage(100); //
 * lastTable.getDefaultCell().setBorder(0); // // lastTable.addCell(new
 * Phrase("")); // lastTable.addCell(new Phrase("Income Tax Calculation",
 * font11)); // lastTable.addCell(""); // // lastTable.addCell(new
 * Phrase("Gross Salary							:		", font9bold)); //
 * lastTable.addCell(new
 * Phrase("Exemptions Under Section 10			:		", font9bold)); //
 * lastTable.addCell(new
 * Phrase("Income Chargeable under head salary	:		", font9bold)); //
 * lastTable.addCell(new Phrase("Paid Salary Till Date				:		",
 * font9)); // lastTable.addCell(new
 * Phrase("HRA Exemption						:		", font9)); //
 * lastTable.addCell(new
 * Phrase("Other Income							:		", font9)); //
 * lastTable.addCell(new Phrase("This month salary					:		",
 * font9)); // lastTable.addCell(new
 * Phrase("LTA Exemption						:		", font9)); //
 * lastTable.addCell(new Phrase("Investment Declaration				:		",
 * font9)); // lastTable.addCell(new
 * Phrase("Expected Salary						:		", font9)); //
 * lastTable.addCell(new Phrase("Child Education Exemption			:		",
 * font9)); // lastTable.addCell(new
 * Phrase("Standard Deduction					:		", font9)); //
 * lastTable.addCell("Perquisite							:		"); //
 * lastTable.addCell(""); //
 * lastTable.addCell("Net Taxable Income					:		"); //
 * lastTable.addCell("Paid Perquisite till date			:		"); //
 * lastTable.addCell("Total Amount Of Salary				:		"); //
 * lastTable.addCell("Tax									:		"); //
 * lastTable.addCell("This month Perquisite				:		"); //
 * lastTable.addCell("Professional Tax						:		"); //
 * lastTable.addCell("Cess									:		"); //
 * lastTable.addCell(""); //
 * lastTable.addCell("PT Paid so far						:		"); //
 * lastTable.addCell("Total Tax(including cess)			:		"); //
 * lastTable.addCell(""); //
 * lastTable.addCell("PT this Month						:		"); //
 * lastTable.addCell("Tax Paid so far						:		"); //
 * lastTable.addCell(""); //
 * lastTable.addCell("PT Remaining							:		"); //
 * lastTable.addCell("Tax this Month						:		"); //
 * lastTable.addCell(""); // lastTable.addCell(""); //
 * lastTable.addCell("Tax Remaining						:		"); // //
 * lastTable.addCell(new Paragraph()); // // //
 * outerLastTable.addCell(lastTable); // document.add(outerLastTable);
 * 
 * document.close(); logger.info("document closed above-------->3");
 * 
 * } catch (Exception ex) { logger.info("--------->" , ex.getMessage());
 * 
 * ex.printStackTrace(); } }
 * 
 * }
 */