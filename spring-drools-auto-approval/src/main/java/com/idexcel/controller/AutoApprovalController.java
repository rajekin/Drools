package com.idexcel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.idexcel.model.CreditApplication;
import com.idexcel.service.AutoApprovalService;

@RestController
public class AutoApprovalController {

	private final AutoApprovalService autoApprovalService;

	@Autowired
	public AutoApprovalController(AutoApprovalService autoApprovalService) {
		this.autoApprovalService = autoApprovalService;
	}

	
	@RequestMapping(value = "/autoapprovalflow", method = RequestMethod.POST, produces = "application/json")
	public CreditApplication getQuestion(@RequestBody(required = true) String payload) {

		CreditApplication app = new Gson().fromJson(payload, CreditApplication.class);

		autoApprovalService.getDiscount(app);

		return app;
	}
	

}
