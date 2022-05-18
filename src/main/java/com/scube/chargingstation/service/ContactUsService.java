package com.scube.chargingstation.service;

import javax.validation.Valid;

import com.scube.chargingstation.dto.incoming.ContactUsIncomingDto;

public interface ContactUsService {

	boolean getContactUs(@Valid ContactUsIncomingDto contactUsIncomingDto);

}
