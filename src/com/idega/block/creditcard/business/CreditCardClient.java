package com.idega.block.creditcard.business;

import java.util.Collection;

import com.idega.block.creditcard.data.CreditCardMerchant;

/**
 * @author gimmi
 */
/**
 * @author gimmi
 */
public interface CreditCardClient {

//	public void setupClient(String merchant, String location, String user, String password, String terminal, String extraInfo);
	public Collection getValidCardTypes();
	public CreditCardMerchant getCreditCardMerchant();
	
	/**
	 * Tries to get refund the amount from the card. 
	 * 
	 * @param nameOnCard Name on card
	 * @param cardnumber Card number, should only contain numbers. Example 1234123412341234
	 * @param monthExpires Expire date MONTH, example 06
	 * @param yearExpires	 Expire date YEAR, example 05
	 * @param ccVerifyNumber	Creditcard verification code, example 123
	 * @param amount	Amount
	 * @param currency	Currency
	 * @param referenceNumber	Reference number
	 * @return Creditcard Authorization Number
	 * @throws CreditCardAuthorizationException
	 */
	public String doRefund(String cardnumber, String monthExpires, String yearExpires, String ccVerifyNumber, double amount, String currency, Object parentDataPK, String extraField) throws CreditCardAuthorizationException;
	
	/**
	 * Tries to get deduct the amount from the card. 
	 * 
	 * @param nameOnCard Name on card
	 * @param cardnumber Card number, should only contain numbers. Example 1234123412341234
	 * @param monthExpires Expire date MONTH, example 06
	 * @param yearExpires	 Expire date YEAR, example 05
	 * @param ccVerifyNumber	Creditcard verification code, example 123
	 * @param amount	Amount
	 * @param currency	Currency
	 * @param referenceNumber	Reference number
	 * @return Creditcard Authorization Number
	 * @throws CreditCardAuthorizationException
	 */
	public String doSale(String nameOnCard, String cardnumber, String monthExpires, String yearExpires, String ccVerifyNumber, double amount, String currency, String referenceNumber) throws CreditCardAuthorizationException;
	
	public String creditcardAuthorization(String nameOnCard, String cardnumber, String monthExpires, String yearExpires, String ccVerifyNumber, double amount, String currency, String referenceNumber) throws CreditCardAuthorizationException;
	public void finishTransaction(String properties) throws CreditCardAuthorizationException;	
}
