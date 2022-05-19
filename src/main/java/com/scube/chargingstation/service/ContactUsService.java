package com.scube.chargingstation.service;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import com.scube.chargingstation.dto.incoming.ContactUsIncomingDto;

public interface ContactUsService {

	boolean getContactUs(@Valid ContactUsIncomingDto contactUsIncomingDto);
    boolean sendEmail(@Valid @RequestBody ContactUsIncomingDto contactUsIncomingDto)throws  Exception;
}
