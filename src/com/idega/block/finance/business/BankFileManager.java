/*
 * Created on Nov 30, 2004
 *
 */
package com.idega.block.finance.business;

import java.sql.Timestamp;
import java.util.Calendar;



/**
 * @author birna
 *
 */
public interface BankFileManager {
	
	Integer[] getInvoiceNumbers(int bunchNumber);
	int[] getInvoiceNumbers(String claimantsSSN, Timestamp dueDate);
	
	//<notandanafn>
	String getUsername(int groupId);
	//<lykilord>
	String getPassword(int groupId);
	//<session_id>
	String getSessionId();
	
	//<kt_krofuhafa>
	String getClaimantSSN(int groupId);
	String getClaimantName(int groupId);
	//<faerslugerd>
	String getBookkeepingType(int invoiceNumber);
	//<banki>
	String getBankBranchNumber(int groupId);
	//<hofudbok>
	int getAccountBook(int groupId);
	//<numer>
	String getClaimNumber(int invoiceNumber);
	//<gjalddagi>
	Calendar getDueDate(int invoiceNumber);
	//<gjalddagi_fra>
	String getDueDateFrom(int invoiceNumber);
	//<gjalddagi_til>
	String getDueDateTo(int invoiceNumber);
	//<kt_greidanda>
	String getPayersSSN(int invoiceNumber);
	//<nidurfellingardagur>
	String getDisallowanceDate(int invoiceNumber);
	//<audkenni>
	String getClaimantsAccountId(int groupId);
	//<hreyfingardagur_fra>
	String getTransactionDayFrom(int invoiceNumber);
	//<hreyfingardagur_til>
	String getTransactionDayTo(int invoiceNumber);
	//<astand>
	String getStatus(int invoiceNumber);
	//<upphaed>
	String getAmount(int invoiceNumber);
	//<tilvisunarnumer>
	int getBatchNumber(int invoiceNumber);
	//<eindagi>
	Calendar getFinalDueDate(int invoiceNumber);
	//<sedilnumer>
	String getNoteNumber(int invoiceNumber);
	//<vidskiptanumer>
	String getTradeNumber(int invoiceNumber);
	/**
	 * fee for the calculation and printing of the bill (when the bill is on paper)
	 * @param invoiceNumber
	 * @return
	 */
	//<tilkynningar_og_greidslugjald_1>
	double getNotificationAndPaymentFee1(int invoiceNumber);
	/**
	 * fee for the calculation and sending of the bill (when the bill is not printed on paper)
	 * @param invoiceNumber
	 * @return
	 */
	//<tilkynningar_og_greidslugjald_2>
	double getNotificationAndPaymentFee2(int invoiceNumber);
	/**
	 * other cost is to keep special fees that should be charged from the payer. 
	 * other cost is kept with the invoice and overdue is not calculated from it
	 * @param invoiceNumber
	 * @return
	 */
	//<annar_kostnadur>
	double getOtherCost(int invoiceNumber);
	//<annar_vanskila_kostnadur>
	String getOtherOverdueCost(int invoiceNumber);
	//<innborgunarregla>
	String getDepositRule(int invoiceNumber);
	/**
	 * 	code that tells if the invoice may be payed with existing older invoices
	 *  
	 * @param invoiceNumber
	 * @return:
	 * ' ' - the invoice may not be payed if there exists an older invoice
	 *  '1' - the invoice may be payed in any case - the default!
	 */
	//<greidsluregla>
	String getPaymentCode(int invoiceNumber);
	//<vanskilagjald>
	String getOverdueFee(int invoiceNumber);
	//<vanskilagjald><vanskilagjald_1>
	String getOverdueFee1(int invoiceNumber);
	//<vanskilagjald><vanskilagjald_2>
	String getOverdueFee2(int invoiceNumber);
	//<drattarvextir>
	String getPenalIntrest(int invoiceNumber);
	//<drattarvextir><prosent>
	double getPenalIntrestProsent(int invoiceNumber);
	/**
	* controls if drattavextir (penal intrest) is calculated from the amount of the invoice or
	*  the amount + vanskilagjald (overdue fee)
	*  '' - drattavextir calculated from the amount - the default
	*  '1' - drattavextir calculated from the amount + vanskilagjald
	*/
//<drattarvextir><vaxtastofn>
	String getPenalIntrestCode(int invoiceNumber);
	/**
	 * 	the values in drattavaxtaregla may be:
	 * @param invoiceNumber
	 * @return:
	 * 	'' - drattavextir(penal intrest) taken after eindaga (final due date), but are calculated from gjalddaga (due date).
	 *		     if eindagi and gjalddagi are the same day and on a bank holiday then the invoice may be payed without
	 *       drattavextir the next bank day.
	 *  '1' - no drattavextir - this is the default
	 *  '2' - if past the final due date then the drattavextir are calculated from eindagi (eindagi included) with
	 *        the rule 360/360
	 */
	//<drattarvextir><regla>
	String getPenalIntrestRule(int invoiceNumber);
	//<gengiskrafa>
	String getExchangeClaim(int invoiceNumber);
	//<gengiskrafa><gengistegund>
	String getExchangeType(int invoiceNumber);
	//<gengiskrafa><mynt>
	String getExchangeCurrency(int invoiceNumber);
	//<gengiskrafa><gengisbanki>
	String getExchangeBank(int invoiceNumber);
	//<gengiskrafa><gengisregla>
	String getExchangeRule(int invoiceNumber);
	//<afslattur>
	String getDiscount(int invoiceNumber);
	//<afslattur><afslattur_1>
	String getDiscount1(int invoiceNumber);
	//<afslattur><afslattur_2>
	String getDiscount2(int claimNumber);
	//<birtingarkerfi>
	String getDisplayForm(int invoiceNumber);
	//<birtingarkerfi><tegund>
	String getDisplayFormType(int invoiceNumber);
	//<birtingarkerfi><slod>
	String getDisplayFormURL(int invoiceNumber);
	//<birting>
	String getDisplay(int invoiceNumber);
	
	String getInvoiceStatus(int invoiceNumber);
	void setInvoiceStatus(String stada, int invoiceNumber);
}
