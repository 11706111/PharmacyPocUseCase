package org.pharmacy.poc.controller;

import org.pharmacy.poc.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PaymentService service;


}
