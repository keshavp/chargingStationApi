package com.scube.chargingstation.service;
import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scube.chargingstation.entity.RefreshToken;
import com.scube.chargingstation.repository.RefreshTokenRepository;
import com.scube.chargingstation.repository.UserInfoRepository;
import com.scube.chargingstation.security.SecurityConstants;
import com.scube.chargingstation.security.TokenRefreshException;

@Service
public class RefreshTokenService {

	  @Autowired
	  private RefreshTokenRepository refreshTokenRepository;

	  @Autowired
	  private UserInfoRepository userRepository;

	  public RefreshToken findByToken(String token) {
	    return refreshTokenRepository.findByToken(token);
	  }

	  public RefreshToken createRefreshToken(String userId) {
	    RefreshToken refreshToken = new RefreshToken();

	    refreshToken.setUser(userRepository.findByUsername(userId));
	    refreshToken.setExpiryDate(Instant.now().plusMillis(SecurityConstants.REFRESH_EXPIRATION_TIME));
	    refreshToken.setToken(UUID.randomUUID().toString());

	    refreshToken = refreshTokenRepository.save(refreshToken);
	    return refreshToken;
	  }

	  public RefreshToken verifyExpiration(RefreshToken token) {
	    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
	      refreshTokenRepository.delete(token);
	      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
	    }

	    return token;
	  }

	  @Transactional
	  public int deleteByUserId(String userId) {
	    return refreshTokenRepository.deleteByUser(userRepository.findById(userId));
	  }
	}
