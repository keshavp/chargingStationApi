/*
 * package com.scube.chargingstation.service;
 * 
 * import javax.validation.Valid;
 * 
 * import java.nio.charset.StandardCharsets; import java.time.Instant; import
 * java.time.temporal.ChronoUnit; import java.util.Base64;
 * 
 * import org.slf4j.Logger; import org.slf4j.LoggerFactory; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.security.authentication.AuthenticationManager; import
 * org.springframework.security.authentication.
 * UsernamePasswordAuthenticationToken; import
 * org.springframework.security.core.Authentication; import
 * org.springframework.security.core.context.SecurityContextHolder; import
 * org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import
 * org.springframework.stereotype.Service;
 * 
 * import com.scube.chargingstation.dto.AuthUserDto; import
 * com.scube.chargingstation.dto.UserLoginIncomingDto; import
 * com.scube.chargingstation.dto.incoming.SetNewPasswordIncomingDto; import
 * com.scube.chargingstation.dto.mapper.AuthUserMapper; import
 * com.scube.chargingstation.exception.BRSException; import
 * com.scube.chargingstation.model.EmpInfoEntity; import
 * com.scube.chargingstation.model.RefreshToken; import
 * com.scube.chargingstation.repository.EmpInfoRepository; import
 * com.scube.chargingstation.security.JwtUtils; import
 * com.scube.chargingstation.util.EmailService;
 * 
 * @Service public class AuthServiceImpl implements AuthService {
 * 
 * 
 * private static final Logger logger =
 * LoggerFactory.getLogger(AuthServiceImpl.class);
 * 
 * Base64.Encoder baseEncoder = Base64.getEncoder();
 * 
 * Base64.Decoder baseDecoder = Base64.getDecoder();
 * 
 * BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
 * 
 * @Autowired JwtUtils jwtUtils;
 * 
 * @Autowired EmailService emailService;
 * 
 * @Autowired EmpInfoRepository empInfoRepository ;
 * 
 * AuthUserMapper authUserMapper=null;
 * 
 * @Autowired RefreshTokenService refreshTokenService;
 * 
 * @Autowired AuthenticationManager authenticationManager;
 * 
 * @Autowired DesignationService designationService ;
 * 
 * 
 * @Override public AuthUserDto authenticateUser(UserLoginIncomingDto
 * loginRequest) {
 * 
 * logger.info("****AuthServiceImpl authenticateUser****");
 * 
 * authUserMapper=new AuthUserMapper();
 * 
 * AuthUserDto response=new AuthUserDto();
 * 
 * if(loginRequest.getUsername().equalsIgnoreCase("")) { throw
 * BRSException.throwException("Error: Login Id cannot be empty!"); }
 * if(loginRequest.getPassword().equalsIgnoreCase("")) { throw
 * BRSException.throwException("Error: Password cannot be empty!"); }
 * 
 * EmpInfoEntity masterEntity =
 * empInfoRepository.findByOffemail(loginRequest.getUsername());
 * 
 * if(masterEntity == null) {
 * 
 * throw BRSException.
 * throwException("Error: The email address provided is not registered to your organization!"
 * ); }
 * 
 * if(masterEntity.getEmpstatus().equalsIgnoreCase("Inactive")){ throw
 * BRSException.throwException("Error: User status is inactive."); }
 * 
 * 
 * Authentication authentication = authenticationManager.authenticate( new
 * UsernamePasswordAuthenticationToken(
 * loginRequest.getUsername(),loginRequest.getPassword()));
 * 
 * SecurityContextHolder.getContext().setAuthentication(authentication); String
 * jwt = jwtUtils.generateJwtToken(authentication);
 * 
 * UserDetailsImpl userDetails = (UserDetailsImpl)
 * authentication.getPrincipal();
 * 
 * RefreshToken refreshToken =
 * refreshTokenService.createRefreshToken(userDetails.getId());
 * if(masterEntity!=null) {
 * 
 * response=authUserMapper.toUserLoginDto(masterEntity,
 * jwt,refreshToken.getToken()); }
 * 
 * return response ; }
 * 
 * 
 * @Override public boolean resetPassword(String email) {
 * 
 * logger.info("*****AuthServiceImpl resetPassword*****"+ email);
 * 
 * EmpInfoEntity emp = empInfoRepository.findByOffemail(email);
 * 
 * if(emp == null) { logger.info("Error: Email Id is not registered. Email = "+
 * email); throw BRSException.
 * throwException("Error: The email address provided is not registered to your organization!"
 * ); } logger.info("Email existance check complete."); String encodeEmail =
 * baseEncoder.encodeToString(email.getBytes(StandardCharsets.UTF_8)) ;
 * 
 * emailService.sendResetPasswordMail(encodeEmail,email);
 * 
 * emp.setReset_password("Y"); // emp.setResetPassInstance();
 * empInfoRepository.save(emp);
 * 
 * logger.info("Reset password mail sent."); return true; }
 * 
 * 
 * @Override public boolean checkResetPasswordConditions(String email) {
 * 
 * logger.info("*****AuthServiceImpl checkResetPasswordConditions*****"+ email);
 * 
 * String emailId = new String (baseDecoder.decode(email));
 * 
 * EmpInfoEntity emp = empInfoRepository.findByOffemail(emailId);
 * 
 * if(emp == null) { logger.info("Email id is not registered. email = "+ email);
 * throw BRSException.
 * throwException("Error: The email address provided is not registered to your organization!"
 * ); } logger.info("Email id check complete.");
 * 
 * if(emp.getReset_password().equalsIgnoreCase("N")) {
 * logger.info("Reset flag has wrong value = "+ emp.getReset_password()); throw
 * BRSException.
 * throwException("Error: Reset flag has wrong value for record with email="
 * +email+"."); } logger.info("Reset flag check complete.");
 * 
 * Instant resetInstance = emp.getResetPassInstance();
 * 
 * Instant value = resetInstance.plus(1, ChronoUnit.HOURS);
 * logger.info(resetInstance+"-----"+ value+"----"+
 * Instant.now()+"-----  "+Instant.now().compareTo(value)); //
 * now.compareto(value) = 1 then now is greater than value //
 * now.compareto(value) = -1 then now is less than value
 * if(Instant.now().compareTo(value) < 0) { logger.info("Send true");
 * }if(Instant.now().compareTo(value) > 0 ) {
 * logger.info("Reset token has expired."); throw BRSException.
 * throwException("Error: Reset token expired. Please start the reset Password process again."
 * ); } logger.info("Password check completed.");
 * 
 * return true; }
 * 
 * 
 * @Override public boolean setNewPassword(SetNewPasswordIncomingDto
 * setNewPasswordIncomingDto) {
 * 
 * logger.info("*****AuthServiceImpl setNewPassword*****"+
 * setNewPasswordIncomingDto.getConfirmpassword());
 * 
 * String emailId = new String
 * (baseDecoder.decode(setNewPasswordIncomingDto.getEmail()));
 * 
 * EmpInfoEntity emp = empInfoRepository.findByOffemail(emailId);
 * 
 * if(emp == null) {
 * logger.info("Error: The email address provided is not registered."); throw
 * BRSException.
 * throwException("Error: The email address provided is not registered to the organization!"
 * ); }
 * 
 * if(!setNewPasswordIncomingDto.getPassword().equalsIgnoreCase(
 * setNewPasswordIncomingDto.getConfirmpassword())) {
 * logger.info("Error: Passwords Do not match."); throw
 * BRSException.throwException("Error: Passwords do not match!"); }
 * 
 * emp.setReset_password("N");
 * emp.setOffpassword(encoder.encode(setNewPasswordIncomingDto.getPassword()));
 * emp.setReset_password_count(emp.getReset_password_count() + 1);
 * empInfoRepository.save(emp); logger.info("Password has been reset."); return
 * true; }
 * 
 * }
 */