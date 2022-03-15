/*
 * package com.scube.chargingstation.util;
 * 
 * import static com.scube.chargingstation.exception.EntityType.FILESIZE; import
 * static com.scube.chargingstation.exception.ExceptionType.FILE_SIZE_EXCEPTION;
 * import java.io.IOException; import java.nio.file.Files; import
 * java.nio.file.Path; import java.nio.file.Paths; import
 * java.nio.file.StandardCopyOption;
 * 
 * import org.slf4j.Logger; import org.slf4j.LoggerFactory; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.beans.factory.annotation.Value; import
 * org.springframework.core.io.Resource; import
 * org.springframework.core.io.UrlResource; import
 * org.springframework.stereotype.Service; import
 * org.springframework.util.StringUtils; import
 * org.springframework.web.multipart.MultipartFile;
 * 
 * import com.scube.chargingstation.exception.BRSException; import
 * com.scube.chargingstation.model.ConfigCompanyInfoEntity; import
 * com.scube.chargingstation.model.EmpInfoDocEntity; import
 * com.scube.chargingstation.model.EmpInfoEntity; import
 * com.scube.chargingstation.repository.ConfigCompanyRepository; import
 * com.scube.chargingstation.repository.EmpDocInfoRepository; import
 * com.scube.chargingstation.repository.EmpInfoRepository;
 * 
 * @Service public class FileBankService {
 * 
 * private static final Logger logger =
 * LoggerFactory.getLogger(FileBankService.class);
 * 
 * private Path fileStorageLocation; private Path fileBaseLocation;
 * 
 * RandomStringUtil randomStringUtil = null;
 * 
 * @Autowired EmpInfoRepository empInfoRepo;
 * 
 * @Autowired EmpDocInfoRepository empInfoDocRepo;
 * 
 * @Autowired ConfigCompanyRepository configCompanyRepo;
 * 
 * @Value("${file.storeprofileimagepath-dir}") private String profileLocation;
 * 
 * @Value("${file.storedocumentpath-dir}") private String docLocation;
 * 
 * @Value("${file.storetaxdocumentpath-dir}") private String taxDocLocation;
 * 
 * @Value("${file.storecompanylogo-dir}") private String companyLogoLocation;
 * 
 * @Value("${file.storeexcel-dir}") private String excelLocation;
 * 
 * public Resource loadFileAsResource(String id, String flag, String empcode)
 * throws Exception { // id = id(PK) - to find by id if flag = doc // flag =
 * flag to know if its DOCUMENTID or PROFILEID // empcode = to find by empcode
 * if flag = pro
 * logger.info("*******FileBankService loadFileAsResource*******"); String
 * newPath = ""; String fileName = "";
 * 
 * if(flag.equalsIgnoreCase("excel")) { newPath = excelLocation;
 * 
 * fileName = "temporary.xlsx"; logger.info("EXCEL------fileName----->"+
 * fileName);
 * 
 * Path fileStorageLocation = Paths.get(newPath).toAbsolutePath().normalize();
 * logger.info("fileStorageLocation"+ fileStorageLocation);
 * 
 * Path filePath = fileStorageLocation.resolve(fileName).normalize();
 * 
 * Resource resource = new UrlResource(filePath.toUri()); if(resource.exists())
 * { logger.info("Inside IF(resource.exists)"); return resource; } else {
 * logger.info("Inside else()"); throw new Exception("File not found " +
 * fileName); } }
 * 
 * if(flag.equalsIgnoreCase("logo")) { newPath = companyLogoLocation;
 * 
 * ConfigCompanyInfoEntity configCompany =
 * configCompanyRepo.findByStatus("Active"); fileName =
 * configCompany.getLogo_url();
 * 
 * logger.info("PROFILE------fileName----->"+ fileName); Path
 * fileStorageLocation = Paths.get(newPath).toAbsolutePath().normalize();
 * 
 * logger.info("newPath"+ newPath); logger.info("fileStorageLocation"+
 * fileStorageLocation); Path filePath =
 * fileStorageLocation.resolve(fileName).normalize(); logger.info("filePath"+
 * filePath);
 * 
 * Resource resource = new UrlResource(filePath.toUri()); if(resource.exists())
 * { logger.info("Inside IF(resource.exists)"); return resource; } else {
 * logger.info("Inside else()"); throw new Exception("File not found " +
 * fileName); }
 * 
 * }
 * 
 * if(flag.equalsIgnoreCase("pro")) { newPath = profileLocation;
 * 
 * EmpInfoEntity emp = empInfoRepo.findByEmpcode(empcode); fileName =
 * emp.getEmp_photo_url(); logger.info("PROFILE------fileName----->"+ fileName);
 * Path fileStorageLocation = Paths.get(newPath).toAbsolutePath().normalize();
 * 
 * logger.info("newPath"+ newPath); logger.info("fileStorageLocation"+
 * fileStorageLocation); Path filePath =
 * fileStorageLocation.resolve(fileName).normalize(); logger.info("filePath"+
 * filePath);
 * 
 * Resource resource = new UrlResource(filePath.toUri()); if(resource.exists())
 * { logger.info("Inside IF(resource.exists)"); return resource; } else {
 * logger.info("Inside else()"); throw new Exception("File not found " +
 * fileName); }
 * 
 * }if(flag.equalsIgnoreCase("doc")) { newPath = docLocation;
 * 
 * EmpInfoDocEntity emp = empInfoDocRepo.findById(id); fileName =
 * emp.getUrl_path();
 * 
 * logger.info("PROFILE------fileName----->"+ fileName); Path
 * fileStorageLocation = Paths.get(newPath).toAbsolutePath().normalize();
 * 
 * logger.info("newPath"+ newPath); logger.info("fileStorageLocation"+
 * fileStorageLocation);
 * 
 * Path filePath = fileStorageLocation.resolve(fileName).normalize();
 * logger.info("filePath"+ filePath);
 * 
 * Resource resource = new UrlResource(filePath.toUri()); if(resource.exists())
 * { logger.info("Inside IF(resource.exists)"); return resource; } else {
 * logger.info("Inside else()"); throw new Exception("File not found " +
 * fileName); }
 * 
 * }if(!flag.equalsIgnoreCase("doc") && flag.equalsIgnoreCase("pro")) {
 * logger.info("throw error- Invalid Flag Value"); }
 * 
 * 
 * 
 * return null;
 * 
 * }
 * 
 * public String storeFile(MultipartFile file,String flag) throws Exception { //
 * id = id // flag = flag to know if its DOCUMENTID or PROFILEID
 * logger.info("*******FileBankService loadFileAsResource*******"); String
 * basePath = "";
 * 
 * if(flag.equalsIgnoreCase("pro")) { // save in empInfoRepo - EmpInfoEntity -
 * Return filePath basePath = profileLocation;
 * 
 * if(file.getSize() > 2097152) {
 * logger.error("File size should be less than 2 MB."); throw
 * BRSException.throwException(FILESIZE, FILE_SIZE_EXCEPTION, "2"); }
 * 
 * }if(flag.equalsIgnoreCase("doc")) { // save in empDocInfoRepo -
 * EmpInfoDocRepo - Return filePath basePath = docLocation;
 * 
 * if(file.getSize() > 5242880) {
 * logger.error("File size should be less than 5 MB."); throw
 * BRSException.throwException(FILESIZE, FILE_SIZE_EXCEPTION, "5"); }
 * 
 * }if(flag.equalsIgnoreCase("logo")) { basePath = companyLogoLocation;
 * 
 * if(file.getSize() > 2097152) {
 * logger.error("File size should be less than 2 MB."); throw
 * BRSException.throwException(FILESIZE, FILE_SIZE_EXCEPTION, "2"); }
 * 
 * }if(flag.equalsIgnoreCase("empTax")) { // save in empDocInfoRepo -
 * EmpInfoDocRepo - Return filePath basePath = taxDocLocation;
 * 
 * if(file.getSize() > 5242880) {
 * logger.error("File size should be less than 5 MB."); throw
 * BRSException.throwException(FILESIZE, FILE_SIZE_EXCEPTION, "5"); }
 * 
 * }
 * 
 * // get the file String fileName =
 * StringUtils.cleanPath(file.getOriginalFilename());
 * 
 * String filename = fileName.split("\\.")[0]; String extension =
 * fileName.split("\\.")[1]; System.out.println(fileName);
 * 
 * logger.info("neither" + basePath);
 * 
 * 
 * 
 * if(fileName.contains("..")) { // throw new
 * FileStorageException("Sorry! File Name contains invalid path sequence!");
 * logger.info("wrong fileName"); } String random =
 * randomStringUtil.getAlphaNumericString(10, flag); logger.info(random);
 * 
 * String newPath = basePath+random+"/"; this.fileStorageLocation =
 * Paths.get(newPath).toAbsolutePath().normalize(); try {
 * Files.createDirectories(this.fileStorageLocation); Path targetLocation =
 * this.fileStorageLocation.resolve(fileName);
 * 
 * Files.copy(file.getInputStream(), targetLocation,
 * StandardCopyOption.REPLACE_EXISTING);
 * 
 * System.out.println("fileName" + fileName+ " ---filePath" + targetLocation);
 * 
 * String returnPath = random+"/"+ fileName; return returnPath;
 * 
 * } catch(Error e) { System.out.println(e); } return null; }
 * 
 * public boolean deleteByDocPath(String url_path) {
 * 
 * logger.info("*******FileBankService deleteByDocPath*******" + url_path);
 * 
 * String basePath = docLocation; String fullPath = basePath + url_path;
 * logger.info(fullPath);
 * 
 * Path newPath = Paths.get(fullPath).toAbsolutePath().normalize();
 * logger.info(String.valueOf(newPath)); try { Files.delete(newPath); return
 * true; } catch (IOException e) { e.printStackTrace(); } return false; }
 * 
 * }
 */