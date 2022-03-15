/*
 * package com.scube.chargingstation.util;
 * 
 * 
 * import java.io.File; import java.io.FileNotFoundException; import
 * java.io.FileOutputStream; import java.io.IOException; import
 * java.nio.file.Files; import java.nio.file.Path; import java.nio.file.Paths;
 * import java.text.ParseException; import java.text.SimpleDateFormat; import
 * java.util.ArrayList; import java.util.Arrays; import java.util.Date; import
 * java.util.List; import java.util.Map; import java.util.Set; import
 * java.util.TreeMap;
 * 
 * import javax.servlet.http.HttpServletResponse;
 * 
 * import org.apache.poi.ss.usermodel.Cell; import
 * org.apache.poi.ss.usermodel.CellStyle; import
 * org.apache.poi.ss.usermodel.Row; import
 * org.apache.poi.xssf.usermodel.XSSFSheet; import
 * org.apache.poi.xssf.usermodel.XSSFWorkbook; import org.slf4j.Logger; import
 * org.slf4j.LoggerFactory; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.beans.factory.annotation.Value; import
 * org.springframework.core.io.Resource; import
 * org.springframework.core.io.UrlResource; import
 * org.springframework.stereotype.Service;
 * 
 * import com.scube.chargingstation.dto.ConfigureSalaryAdjustmentDto; import
 * com.scube.chargingstation.dto.MonthlyAttendanceDto; import
 * com.scube.chargingstation.dto.SalaryAdjustmentDto; import
 * com.scube.chargingstation.model.ConfigureSalaryAdjustmentEntity; import
 * com.scube.chargingstation.model.EmpInfoEntity; import
 * com.scube.chargingstation.model.MasterSalaryAdjustmentEntity; import
 * com.scube.chargingstation.model.SalaryProcessEmployeeEntity; import
 * com.scube.chargingstation.model.SalaryProcessLotEntity; import
 * com.scube.chargingstation.service.EmpInfoService; import
 * com.scube.chargingstation.service.SalaryAdjustmentService;
 * 
 * 
 * 
 * @Service public class ExcelDownloadService {
 * 
 * private static final Logger logger =
 * LoggerFactory.getLogger(ExcelDownloadService.class);
 * 
 * @Autowired FileBankService fileBankService;
 * 
 * @Autowired SalaryAdjustmentService salaryAdjustmentService;
 * 
 * private Path fileStorageLocation;
 * 
 * RandomStringUtil randomStringUtil = null;
 * 
 * @Autowired EmpInfoService empInfoService;
 * 
 * @Value("${file.storeexcel-dir}") private String excelLocation;
 * 
 * @Value("${file.storeattendance-dir}") private String attendanceLocation;
 * 
 * 
 * public Resource getAttendanceByDates(List<MonthlyAttendanceDto> list, String
 * empcode) throws FileNotFoundException, IOException, ParseException {
 * 
 * logger.info("***ExcelDownloadService getAttendanceByDates***");
 * 
 * XSSFWorkbook workbook = new XSSFWorkbook();
 * 
 * XSSFSheet sheet = workbook.createSheet(empcode+"_attendance" );
 * 
 * 
 * Row row0 = sheet.createRow(0); row0.createCell(0).setCellValue("Sr No.");
 * row0.createCell(1).setCellValue("DATE");
 * row0.createCell(2).setCellValue("EMP CODE");
 * row0.createCell(3).setCellValue("EMP Name");
 * row0.createCell(4).setCellValue("CHECKIN TIME");
 * row0.createCell(5).setCellValue("CHECKOUT TIME");
 * row0.createCell(6).setCellValue("TOTAL BREAK TIME");
 * row0.createCell(7).setCellValue("LOGIN TIME");
 * row0.createCell(8).setCellValue("TOTAL WORK TIME");
 * 
 * 
 * int rowNum = 1; for(MonthlyAttendanceDto attendanceDto : list) {
 * 
 * Row row = sheet.createRow(rowNum++);
 * 
 * SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); String
 * isoDatePattern = "yyyy-MM-dd"; SimpleDateFormat simpleDateFormat = new
 * SimpleDateFormat(isoDatePattern); String dateString =
 * simpleDateFormat.format(attendanceDto.getDate());
 * 
 * EmpInfoEntity emp =
 * empInfoService.getUserByEmpCode(attendanceDto.getEmpcode());
 * 
 * 
 * row.createCell(0).setCellValue(rowNum-1);
 * row.createCell(1).setCellValue(dateString);
 * row.createCell(2).setCellValue(attendanceDto.getEmpcode());
 * row.createCell(3).setCellValue(emp.getPer_first_name() + " " +
 * emp.getPer_last_name());
 * row.createCell(4).setCellValue(attendanceDto.getCheckin());
 * row.createCell(5).setCellValue(attendanceDto.getCheckout());
 * row.createCell(6).setCellValue(attendanceDto.getTotalbreaktime());
 * row.createCell(7).setCellValue(attendanceDto.getLoginTime());
 * row.createCell(8).setCellValue(attendanceDto.getTotalworktime());
 * 
 * }
 * 
 * sheet.autoSizeColumn(1); sheet.autoSizeColumn(2); sheet.autoSizeColumn(3);
 * sheet.autoSizeColumn(4); sheet.autoSizeColumn(5); sheet.autoSizeColumn(6);
 * sheet.autoSizeColumn(7); sheet.autoSizeColumn(8);
 * 
 * try {
 * 
 * String random = randomStringUtil.getAlphaNumericString(10, "attendance");
 * logger.info(random);
 * 
 * String newPath = attendanceLocation+random+"/"; this.fileStorageLocation =
 * Paths.get(newPath).toAbsolutePath().normalize();
 * logger.info(String.valueOf(this.fileStorageLocation));
 * Files.createDirectories(this.fileStorageLocation);
 * 
 * FileOutputStream out = new FileOutputStream(new
 * File(this.fileStorageLocation+"/"+empcode+".xlsx")); workbook.write(out);
 * out.close();
 * 
 * Path filePath = fileStorageLocation.resolve(empcode+".xlsx").normalize();
 * logger.info("filePath"+ filePath);
 * 
 * Resource resource = new UrlResource(filePath.toUri());
 * 
 * if(resource.exists()) { logger.info("Inside IF(resource.exists)"); return
 * resource; } else { logger.info("Inside else()"); throw new
 * Exception("File not found " + String.valueOf(filePath)); }
 * 
 * }catch(Exception e) { e.printStackTrace(); }
 * 
 * return null; }
 * 
 * public Resource downloadAdvice(SalaryProcessLotEntity processLotInfo,
 * List<SalaryProcessEmployeeEntity> processEmp) throws FileNotFoundException,
 * IOException {
 * 
 * logger.info("***ExcelDownloadService downloadAdvice***");
 * 
 * XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
 * workbook.createSheet("Bank Advice");
 * 
 * Map<String, Object[]> data = new TreeMap<String, Object[]>(); //
 * data.put("1", new Object[]{ "", "NAME", "LASTNAME" }); data.put("1", new
 * Object[] {"EMP CODE", "EMP NAME", "PROCESSED DATE", "NET SALARY",
 * "INCOME TAX"});
 * 
 * for(int i = 0, j=2; i<processEmp.size(); i++, j++) { EmpInfoEntity emp =
 * empInfoService.getUserByEmpCode(processEmp.get(i).getEmpcode());
 * 
 * data.put(String.valueOf(j),new Object[]{processEmp.get(i).getEmpcode() ,
 * emp.getPer_first_name()+" " + emp.getPer_last_name(),
 * String.valueOf(processEmp.get(i).getProcessed_date()),String.valueOf(
 * processEmp.get(i).getTol_net_salary()),String.valueOf(processEmp.get(i).
 * getDed_income_tax())});
 * 
 * }
 * 
 * Set<String> keyset = data.keySet(); int rownum = 0;
 * 
 * for (String key : keyset) { Row row = sheet.createRow(rownum++); Object[]
 * objArr = data.get(key); int cellnum = 0;
 * 
 * for (Object obj : objArr) { Cell cell = row.createCell(cellnum++); if (obj
 * instanceof String) { cell.setCellValue((String)obj);
 * 
 * } else if (obj instanceof Integer) { cell.setCellValue((Integer)obj); } } }
 * sheet.autoSizeColumn(1); sheet.autoSizeColumn(2); sheet.autoSizeColumn(3);
 * sheet.autoSizeColumn(4); sheet.autoSizeColumn(5); try {
 * 
 * String random = randomStringUtil.getAlphaNumericString(10, "Bank Advice");
 * logger.info(random);
 * 
 * String newPath = excelLocation+random+"/"; this.fileStorageLocation =
 * Paths.get(newPath).toAbsolutePath().normalize();
 * logger.info(String.valueOf(this.fileStorageLocation));
 * Files.createDirectories(this.fileStorageLocation);
 * 
 * FileOutputStream out = new FileOutputStream(new
 * File(this.fileStorageLocation+"/"+"BankAdvice.xlsx")); workbook.write(out);
 * out.close();
 * 
 * Path filePath = fileStorageLocation.resolve("BankAdvice.xlsx").normalize();
 * logger.info("filePath"+ filePath);
 * 
 * Resource resource = new UrlResource(filePath.toUri());
 * 
 * if(resource.exists()) { logger.info("Inside IF(resource.exists)"); return
 * resource; } else { logger.info("Inside else()"); throw new
 * Exception("File not found " + String.valueOf(filePath)); }
 * 
 * // String excelStoragePath = fileBankService.storeExcel(workbook);
 * }catch(Exception e) { e.printStackTrace(); }
 * 
 * return null;
 * 
 * 
 * }
 * 
 * public Resource downloadReport(SalaryProcessLotEntity processLotInfo,
 * List<SalaryProcessEmployeeEntity> processEmp) throws FileNotFoundException,
 * IOException {
 * 
 * logger.info("***ExcelDownloadService downloadReport***");
 * 
 * // Check here if the excel has already been created // If yes, send that
 * file, otherwise create and send String checkNewPath = excelLocation; String
 * fileName = processLotInfo.getLots_name() + ".xlsx"; Path fileStorageLocation
 * = Paths.get(checkNewPath).toAbsolutePath().normalize();
 * logger.info("fileStorageLocation"+ fileStorageLocation);
 * 
 * Path checkFilePath = fileStorageLocation.resolve(fileName).normalize();
 * 
 * Resource checkResource = new UrlResource(checkFilePath.toUri());
 * 
 * if(checkResource.exists()) {
 * logger.info("Salary report for lot name="+processLotInfo.getLots_name()
 * +" already exists."); return checkResource; }else {
 * 
 * 
 * XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
 * workbook.createSheet("Salary Report");
 * 
 * // Map<String, Object[]> data = new TreeMap<String, Object[]>(); //
 * data.put("1", new Object[]{ "", "NAME", "LASTNAME" }); // data.put("1", new
 * Object[] {"EMP CODE", "EMP NAME", "NET SALARY", "LOT NAME", "MONTH",
 * "PAYMENT DATE", // "PAYMENT MODE", "REF NUMBER", "YEAR", // "Basic", "HRA",
 * "DA", "Transport", "Medical Reimbursment", // "Incentive","Reimbursment",
 * "Leave Encashment", "Other Earnings", "Child Education Allowances", //
 * "Uniform Allowances", "Food Allowances", "LTA", "Driver", "Meal Voucher",
 * "Variable Earnings", // "Gratuity", "Gift Pass", "Arrears",
 * "Performance Incentive", "Bonus", "Overtime", "Car","Total Adjustment", // //
 * DEDUCTIONS // "PF", "Employer PF", "Employer Pention Scheme",
 * "EPF Admin Charges", "EDLI Charges", "PT", "Income Tax", // "Food Coupon",
 * "Other Deductions", "Education Cess", "EDLI Admin Charges", "ESI",
 * "ESI @ 3.25", "Loan Deduction", // "VPF"}); Map<String, List<String>> data =
 * new TreeMap<String, List<String>>();
 * 
 * List<String> headers = new ArrayList<>(Arrays.asList("EMP CODE", "EMP NAME",
 * "NET SALARY", "LOT NAME", "MONTH", "PAYMENT DATE", "PAYMENT MODE",
 * "REF NUMBER", "YEAR", "Basic", "HRA", "DA", "Transport",
 * "Medical Reimbursment", "Incentive","Reimbursment", "Leave Encashment",
 * "Other Earnings", "Child Education Allowances", "Uniform Allowances",
 * "Food Allowances", "LTA", "Driver", "Meal Voucher", "Variable Earnings",
 * "Gratuity", "Gift Pass", "Arrears", "Performance Incentive", "Bonus",
 * "Overtime", "Car","Total Adjustment", // // DEDUCTIONS "PF", "Employer PF",
 * "Employer Pention Scheme", "EPF Admin Charges", "EDLI Charges", "PT",
 * "Income Tax", "Food Coupon", "Other Deductions", "Education Cess",
 * "EDLI Admin Charges", "ESI", "ESI @ 3.25", "Loan Deduction", "VPF"));
 * 
 * data.put("1", headers);
 * 
 * // Add adjustment names here List<SalaryAdjustmentDto> AdjList =
 * salaryAdjustmentService.getAllList();
 * 
 * for(SalaryAdjustmentDto ent: AdjList) { data.get("1").add(ent.getName()); }
 * 
 * // Add all values here for(int i = 0, j=2; i<processEmp.size(); i++, j++) {
 * EmpInfoEntity emp =
 * empInfoService.getUserByEmpCode(processEmp.get(i).getEmpcode());
 * 
 * List<ConfigureSalaryAdjustmentEntity> adjustFullList =
 * salaryAdjustmentService.getAllByEmpInfoEntity(emp);
 * 
 * List<String> adjustmentValues = new
 * ArrayList<>(Arrays.asList(processEmp.get(i).getEmpcode() ,
 * emp.getPer_first_name()+" " + emp.getPer_last_name(),
 * String.valueOf(processEmp.get(i).getTol_net_salary()),
 * processLotInfo.getLots_name(), processLotInfo.getMonth(),
 * processLotInfo.getPayment_date(), processLotInfo.getPayment_mode(),
 * processLotInfo.getRef_number(), processLotInfo.getYear(),
 * String.valueOf(processEmp.get(i).getEar_basic()),
 * String.valueOf(processEmp.get(i).getEar_hra()),
 * String.valueOf(processEmp.get(i).getEar_da()),
 * String.valueOf(processEmp.get(i).getEar_transport()),
 * String.valueOf(processEmp.get(i).getEar_medical_reimbursment()),
 * String.valueOf(processEmp.get(i).getEar_incentive()),
 * String.valueOf(processEmp.get(i).getEar_reimbursment()),
 * String.valueOf(processEmp.get(i).getEar_leave_encashment()),
 * String.valueOf(processEmp.get(i).getEar_others()),
 * String.valueOf(processEmp.get(i).getEar_child_educations_allowance()),
 * String.valueOf(processEmp.get(i).getEar_uniform_allowance()),
 * String.valueOf(processEmp.get(i).getEar_food_allowance()),
 * String.valueOf(processEmp.get(i).getEar_lta()),
 * String.valueOf(processEmp.get(i).getEar_driver()),
 * String.valueOf(processEmp.get(i).getEar_meal_voucher()),
 * String.valueOf(processEmp.get(i).getEar_variable_earnings()),
 * String.valueOf(processEmp.get(i).getEar_gratuity()),
 * String.valueOf(processEmp.get(i).getEar_gift_pass()),
 * String.valueOf(processEmp.get(i).getEar_arrears()),
 * String.valueOf(processEmp.get(i).getEar_performance_incentive()),
 * String.valueOf(processEmp.get(i).getEar_bonus()),
 * String.valueOf(processEmp.get(i).getEar_overtime()),
 * String.valueOf(processEmp.get(i).getEar_car()),
 * 
 * String.valueOf(processEmp.get(i).getTol_adjusment()),
 * 
 * String.valueOf(processEmp.get(i).getDed_pf()),
 * String.valueOf(processEmp.get(i).getDed_emp_pf()),
 * String.valueOf(processEmp.get(i).getDed_emp_pension_scheme()),
 * String.valueOf(processEmp.get(i).getDed_epf_admin_charges()),
 * String.valueOf(processEmp.get(i).getDed_edli_charges()),
 * String.valueOf(processEmp.get(i).getDed_pt()),
 * String.valueOf(processEmp.get(i).getDed_income_tax()),
 * String.valueOf(processEmp.get(i).getDed_food_coupon()),
 * String.valueOf(processEmp.get(i).getDed_other()),
 * String.valueOf(processEmp.get(i).getDed_education_cess()),
 * String.valueOf(processEmp.get(i).getDed_edli_admin_charges()),
 * String.valueOf(processEmp.get(i).getDed_esi()), String.valueOf(""),
 * String.valueOf(processEmp.get(i).getDed_loan()),
 * String.valueOf(processEmp.get(i).getDed_vpf())));
 * 
 * for(SalaryAdjustmentDto ent: AdjList) { // 5 // all active adjustments freom
 * master // 1// id // 0010// emp_code // 1425// lot_code String configAdjEnt =
 * salaryAdjustmentService.getByEmpIndoEntityLotCodeId(emp, ent,
 * processEmp.get(i).getLotscode());
 * logger.info("ConfigureSalaryAdjustmentDto made here.");
 * adjustmentValues.add(configAdjEnt); }
 * 
 * data.put(String.valueOf(j), adjustmentValues);
 * 
 * }
 * 
 * Set<String> keyset = data.keySet(); int rownum = 0;
 * 
 * for (String key : keyset) { Row row = sheet.createRow(rownum++); List<String>
 * objArr = data.get(key); int cellnum = 0;
 * 
 * for (Object obj : objArr) { Cell cell = row.createCell(cellnum++); if (obj
 * instanceof String) { cell.setCellValue((String)obj);
 * 
 * } else if (obj instanceof Integer) { cell.setCellValue((Integer)obj); } } }
 * 
 * // adjust column widths here sheet.autoSizeColumn(1);
 * sheet.autoSizeColumn(2); sheet.autoSizeColumn(3); sheet.autoSizeColumn(4);
 * sheet.autoSizeColumn(5); sheet.autoSizeColumn(6); sheet.autoSizeColumn(7);
 * sheet.autoSizeColumn(8); sheet.autoSizeColumn(9); sheet.autoSizeColumn(10);
 * sheet.autoSizeColumn(11); sheet.autoSizeColumn(12); sheet.autoSizeColumn(13);
 * sheet.autoSizeColumn(14); sheet.autoSizeColumn(15); sheet.autoSizeColumn(16);
 * sheet.autoSizeColumn(17); sheet.autoSizeColumn(18); sheet.autoSizeColumn(19);
 * sheet.autoSizeColumn(20); sheet.autoSizeColumn(21); sheet.autoSizeColumn(22);
 * sheet.autoSizeColumn(23); sheet.autoSizeColumn(24); sheet.autoSizeColumn(25);
 * sheet.autoSizeColumn(26); sheet.autoSizeColumn(27); sheet.autoSizeColumn(28);
 * sheet.autoSizeColumn(29); sheet.autoSizeColumn(30); sheet.autoSizeColumn(31);
 * sheet.autoSizeColumn(32); sheet.autoSizeColumn(33); sheet.autoSizeColumn(34);
 * sheet.autoSizeColumn(35); sheet.autoSizeColumn(36); sheet.autoSizeColumn(37);
 * sheet.autoSizeColumn(38); sheet.autoSizeColumn(39); sheet.autoSizeColumn(40);
 * sheet.autoSizeColumn(41); sheet.autoSizeColumn(42); sheet.autoSizeColumn(43);
 * sheet.autoSizeColumn(44); sheet.autoSizeColumn(45); sheet.autoSizeColumn(46);
 * sheet.autoSizeColumn(47);
 * 
 * try {
 * 
 * 
 * 
 * String newPath = excelLocation; this.fileStorageLocation =
 * Paths.get(newPath).toAbsolutePath().normalize();
 * logger.info(String.valueOf(this.fileStorageLocation));
 * Files.createDirectories(this.fileStorageLocation);
 * 
 * FileOutputStream out = new FileOutputStream(new
 * File(this.fileStorageLocation+"/"+processLotInfo.getLots_name()+".xlsx"));
 * workbook.write(out); out.close();
 * 
 * Path filePath =
 * fileStorageLocation.resolve(processLotInfo.getLots_name()+".xlsx").normalize(
 * ); logger.info("filePath"+ filePath);
 * 
 * Resource resource = new UrlResource(filePath.toUri());
 * 
 * if(resource.exists()) { logger.info("Inside IF(resource.exists)"); return
 * resource; } else { logger.info("Inside else()"); throw new
 * Exception("File not found " + String.valueOf(filePath)); }
 * 
 * // String excelStoragePath = fileBankService.storeExcel(workbook);
 * }catch(Exception e) { e.printStackTrace(); } } return null;
 * 
 * }
 * 
 * 
 * 
 * }
 */