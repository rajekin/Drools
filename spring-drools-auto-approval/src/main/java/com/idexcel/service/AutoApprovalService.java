package com.idexcel.service;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idexcel.model.CreditApplication;

@Service
public class AutoApprovalService {

	private final KieContainer kieContainer;

	@Autowired
	public AutoApprovalService(KieContainer kieContainer) {
		this.kieContainer = kieContainer;
	}

	
	public CreditApplication getDiscount(CreditApplication product) {
		KieSession kieSession = kieContainer.newKieSession("rulesSession");
		kieSession.insert(product);
		kieSession.fireAllRules();
		kieSession.dispose();
		return product;
	}
}