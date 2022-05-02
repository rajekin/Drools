package com.idexcel.model;

import java.math.BigDecimal;

public class CreditApplication {
	
	private int creditScore;
	private BigDecimal income;
	private BigDecimal monthlyDebtPayment;
	private BigDecimal debtToIncomeRatio;
	private boolean autoApprovalElgible;
	private String reason;
	private String errorCode;
	
	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * @return the creditScore
	 */
	public int getCreditScore() {
		return creditScore;
	}
	/**
	 * @param creditScore the creditScore to set
	 */
	public void setCreditScore(int creditScore) {
		this.creditScore = creditScore;
	}
	/**
	 * @return the income
	 */
	public BigDecimal getIncome() {
		return income;
	}
	/**
	 * @param income the income to set
	 */
	public void setIncome(BigDecimal income) {
		this.income = income;
	}
	/**
	 * @return the monthlyDebtPayment
	 */
	public BigDecimal getMonthlyDebtPayment() {
		return monthlyDebtPayment;
	}
	/**
	 * @param monthlyDebtPayment the monthlyDebtPayment to set
	 */
	public void setMonthlyDebtPayment(BigDecimal monthlyDebtPayment) {
		this.monthlyDebtPayment = monthlyDebtPayment;
	}
	/**
	 * @return the debtToIncomeRatio
	 */
	public BigDecimal getDebtToIncomeRatio() {
		return debtToIncomeRatio;
	}
	/**
	 * @param debtToIncomeRatio the debtToIncomeRatio to set
	 */
	public void setDebtToIncomeRatio(BigDecimal debtToIncomeRatio) {
		this.debtToIncomeRatio = debtToIncomeRatio;
	}
	/**
	 * @return the autoApprovalElgible
	 */
	public boolean isAutoApprovalElgible() {
		return autoApprovalElgible;
	}
	/**
	 * @param autoApprovalElgible the autoApprovalElgible to set
	 */
	public void setAutoApprovalElgible(boolean autoApprovalElgible) {
		this.autoApprovalElgible = autoApprovalElgible;
	}

}
