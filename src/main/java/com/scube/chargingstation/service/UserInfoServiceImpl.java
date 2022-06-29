 package com.scube.chargingstation.service;

import static com.scube.chargingstation.exception.ExceptionType.ALREADY_EXIST;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.dto.AuthUserDto;
import com.scube.chargingstation.dto.UserInfoOtpDto;
import com.scube.chargingstation.dto.incoming.OtpVerificationIncomingDto;
import com.scube.chargingstation.dto.incoming.UserInfoIncomingDto;
import com.scube.chargingstation.dto.mapper.AuthUserMapper;
import com.scube.chargingstation.entity.PartnerInfoEntity;
import com.scube.chargingstation.entity.RoleEntity;
import com.scube.chargingstation.entity.UserInfoEntity;
import com.scube.chargingstation.entity.UserInfoOtpEntity;
import com.scube.chargingstation.exception.BRSException;
import com.scube.chargingstation.exception.EntityType;
import com.scube.chargingstation.repository.RoleRepository;
import com.scube.chargingstation.repository.UserInfoRepository;
import com.scube.chargingstation.service.api.SmsService;
import com.scube.chargingstation.util.RandomNumber;

@Service
public class UserInfoServiceImpl implements UserInfoService {

	private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
	
	@Autowired
	RoleService	 roleService;
	
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserInfoOtpService userInfoOtpService;
	
	@Autowired
	SmsService	smsService;
	
	@Autowired
	PartnerService partnerService;
	
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Override
	public boolean addUserInfo(@Valid UserInfoIncomingDto userInfoIncomingDto) {
		
		logger.info("********UserInfoServiceImpl addUserInfo********");
		
		UserInfoOtpDto userInfoOtpDto = new UserInfoOtpDto();
		
		if(userInfoIncomingDto.getMobilenumber() == "") {
			
			throw BRSException.throwException("user Mobile Number can't be blank");
		}
		
		if(userInfoIncomingDto.getRole() == "") {
			
			throw BRSException.throwException("user role can't be blank");
		}
		
		if(userInfoIncomingDto.getPassword() == "") {
			
			throw BRSException.throwException("user password can't be blank");
		}
		
		
		/*
		 * UserInfoEntity userCodeDuplicateCheck =
		 * userInfoRepository.findByUsername(userInfoIncomingDto.getUsername());
		 * if(userCodeDuplicateCheck != null) {
		 * 
		 * logger.error("throw error that user already exists for Username = "+
		 * userInfoIncomingDto.getUsername()); throw
		 * BRSException.throwException(EntityType.USER, ALREADY_EXIST,
		 * userInfoIncomingDto.getUsername()); }
		 */
		
		UserInfoEntity userEmailDuplicateCheck = userInfoRepository.findByMobilenumber(userInfoIncomingDto.getMobilenumber());
		if(userEmailDuplicateCheck != null) {
			logger.error("throw error that Mobilenumber already exists for Mobilenumber = "+ userInfoIncomingDto.getMobilenumber());
			throw BRSException.throwException(EntityType.USER, ALREADY_EXIST, userInfoIncomingDto.getMobilenumber());
		}
		
		UserInfoEntity userInfoEntity = new UserInfoEntity();
		
		
		userInfoEntity.setUsername(userInfoIncomingDto.getUsername()); 

		userInfoEntity.setEmail(userInfoIncomingDto.getEmail());
		userInfoEntity.setMobilenumber(userInfoIncomingDto.getMobilenumber());
		System.out.println("-------------userInfoIncomingDto.getPassword()---------------"+userInfoIncomingDto.getPassword());
		
		userInfoEntity.setPassword(encoder.encode(userInfoIncomingDto.getPassword()));
		System.out.println("-------------userInfoIncomingDto.getPassword()---------------"+userInfoEntity.getPassword() );
		userInfoEntity.setStatus("ACTIVE");
		
		RoleEntity	roleEntity =  	roleService.findRoleNameByCode(userInfoIncomingDto.getRole());
		
		userInfoEntity.setRole(roleEntity);
		
		userInfoEntity.setResetpasswordcount(0);
		userInfoEntity.setVersion(1);
		userInfoEntity.setResetpassword("N");
		
		
		if(roleEntity.getNameCode().equals("MU")) 
		 { 
			 userInfoEntity.setVerified("N");
		 }
		 
		 else { 
			 userInfoEntity.setVerified("Y"); 
		}
		 
		/*
		 * if(roleEntity.getName().equals("User")) { userInfoEntity.setVerified("N"); }
		 * else { userInfoEntity.setVerified("Y"); }
		 */
		
		if(roleEntity.getNameCode().equals("PU")) {
			
			PartnerInfoEntity partnerInfoEntity = partnerService.getPartnersById(userInfoIncomingDto.getPartnerId());
			
			 userInfoEntity.setPartner(partnerInfoEntity);
	//		 userInfoEntity.setPartnerInfoEntity(partnerInfoEntity);
		}
		
		userInfoEntity = userInfoRepository.save(userInfoEntity);


		if(roleEntity.getNameCode().equals("MU")) {
			String otpCode = "";
			
			otpCode	= RandomNumber.getRandomNumberString();
			
			UserInfoOtpEntity	userInfoOtpEntity = new UserInfoOtpEntity();
			
			userInfoOtpEntity.setMobilenumber(userInfoIncomingDto.getMobilenumber());
			userInfoOtpEntity.setOtpCode(otpCode);
			userInfoOtpEntity.setUserInfoEntity(userInfoEntity);
			userInfoOtpEntity.setStatus("open");
			
			userInfoOtpService.insertOtpDate(userInfoOtpEntity);
			
			smsService.sendSignupOTPMobile(otpCode,userInfoIncomingDto.getMobilenumber());
		}
		
		return true;
	}

	@Override
	public boolean moblieOtpVerify(@Valid OtpVerificationIncomingDto otpVerificationIncomingDto) {
		// TODO Auto-generated method stub
		
		return userInfoOtpService.moblieOtpVerify(otpVerificationIncomingDto);
	}

	@Override
	public boolean editUserProfile(UserInfoIncomingDto userInfoIncomingDto) {
		
		if(userInfoIncomingDto.getMobilenumber() == "") {
			
			throw BRSException.throwException("Error : User Mobile Number can't be blank");
		}
		UserInfoEntity userInfoEntity = userInfoRepository.findByMobilenumber(userInfoIncomingDto.getMobilenumber());

		if(userInfoEntity==null)
		{
			throw BRSException.throwException("Error : User with this mobile number does not exist");
		}
		
		if(userInfoIncomingDto.getEmail() == "") {
			
			throw BRSException.throwException("Error : Email id can't be blank");
		}
		
		if(userInfoIncomingDto.getUsername() == "") {
			
			throw BRSException.throwException("Error : Name can't be blank");
		}
		
		
		userInfoEntity.setEmail(userInfoIncomingDto.getEmail());
		userInfoEntity.setUsername(userInfoIncomingDto.getUsername());
		userInfoEntity.setStatus(userInfoIncomingDto.getStatus());
//		userInfoEntity.setPartner(userInfoIncomingDto.getPartnerName());
		userInfoRepository.save(userInfoEntity);
		
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean deleteUserProfile(String userId) {
		// TODO Auto-generated method stub
		UserInfoEntity userInfoEntity = userInfoRepository.findById(userId);
		
		if(userInfoEntity.getId() == "" || userInfoEntity.getId().trim().isEmpty()) {
			
			throw BRSException.throwException("Error : User ID can't be blank");
		}
	
		
		/*
		 * userInfoEntity.setIsdeleted("Y"); userInfoEntity.setStatus("INACTIVE");
		 * userInfoRepository.save(userInfoEntity);
		 */
		
		userInfoRepository.delete(userInfoEntity);
		
		// TODO Auto-generated method stub   
		return true;
	}

	@Override
	public List<AuthUserDto> getAllUser() {
		// TODO Auto-generated method stub
		
		List<UserInfoEntity> userInfoEntities=userInfoRepository.findAll();
		return AuthUserMapper.toUserLoginDto(userInfoEntities);
	
	}
	
	@Override
	public int findCountForWeekNewAddedUser() {
		// TODO Auto-generated method stub
		return userInfoRepository.findCountForWeekNewAddedUser();
	
	}

	@Override
	public AuthUserDto getUserById(String userId) {
		// TODO Auto-generated method stub
		
		UserInfoEntity userInfo=userInfoRepository.findById(userId);	
		AuthUserDto authUserDto = new AuthUserDto();
		authUserDto.setUserId(userInfo.getId());
//		authUserDto.setPartnerId(userInfo.getPartner().getId());
		authUserDto.setUsername(userInfo.getUsername());
		authUserDto.setRole(userInfo.getRole().getNameCode());
		authUserDto.setMobileno(userInfo.getMobilenumber());
		authUserDto.setEmail(userInfo.getEmail());
		authUserDto.setStatus(userInfo.getStatus());
		authUserDto.setRolename(userInfo.getRole().getName());
		
		if(userInfo.getPartner() != null) {
			authUserDto.setPartnerName(userInfo.getPartner().getPartnerName());
		}
		
		RoleEntity rolentityEntity=roleRepository.findByNameCode(userInfo.getRole().getNameCode());
		
		String rolName=rolentityEntity.getName();
		
		authUserDto.setRolename(rolName);
		
	//	return AuthUserMapper.toUserLoginDto(userInfoEntity);
		
		return authUserDto;
		
		
	}
	
	
	@Override
	public List<AuthUserDto> getPartnerUsersByRoleCode(String nameCode) {
		
		RoleEntity roleEntity = roleRepository.findByNameCode(nameCode);
		
		List<UserInfoEntity> userInfoEntity = userInfoRepository.findByRoleAndStatus(roleEntity,"Active");
		
		return AuthUserMapper.toUserLoginDto(userInfoEntity);
		
	}
	
	@Override
	public List<AuthUserDto> getAllPartnerUsers(String nameCode) {
		
		RoleEntity roleEntity = roleRepository.findByNameCode(nameCode);
		
		List<UserInfoEntity> userInfoEntity = userInfoRepository.findByRoleAndStatus(roleEntity,"Active");
		
		return AuthUserMapper.toUserLoginDto(userInfoEntity);
		
	}

	@Override
	public AuthUserDto getPartnerUserById(String userId) {
		// TODO Auto-generated method stub
		
		UserInfoEntity userInfoEntity = userInfoRepository.findById(userId);
		
		AuthUserDto authUserDto = new AuthUserDto();
		
		authUserDto.setUserId(userInfoEntity.getId());
		authUserDto.setUsername(userInfoEntity.getUsername());
		authUserDto.setRole(userInfoEntity.getRole().getNameCode())  	;
		authUserDto.setMobileno(userInfoEntity.getMobilenumber());
		authUserDto.setEmail(userInfoEntity.getEmail());
		authUserDto.setStatus(userInfoEntity.getStatus());
		authUserDto.setRolename(userInfoEntity.getRole().getName());
		
		RoleEntity rolEntity = roleRepository.findByNameCode(userInfoEntity.getRole().getNameCode());
		
		String roleName = rolEntity.getName();
		
		authUserDto.setRolename(roleName);
		
		return authUserDto;
	}

	@Override
	public UserInfoEntity getChargingStationByPartnerId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserInfoEntity getUserByMobilenumber(String mobilenumber) {
		// TODO Auto-generated method stub
		return userInfoRepository.findByMobilenumber(mobilenumber);
	}
	
	@Override
	public UserInfoEntity getUserInfoEntityById(String userId) {
		// TODO Auto-generated method stub
		return userInfoRepository.findById(userId);
	}
	
}
