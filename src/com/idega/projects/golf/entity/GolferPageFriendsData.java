package com.idega.projects.golf.entity;

import com.idega.data.*;
import java.lang.Character;
import java.lang.Boolean;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.SQLException;


/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class GolferPageFriendsData extends GenericEntity {

  public final String FRIENDS_DATA_NAME = "FRIENDS_DATA_NAME";
  public final String FRIENDS_DATA_EMAIL = "FRIENDS_DATA_EMAIL";
  public final String FRIENDS_DATA_ADRESS = "FRIENDS_DATA_ADRESS";
  public final String FRIENDS_DATA_SIGNING_DATE = "FRIENDS_DATA_SIGNING_DATE";
  public final String FRIENDS_DATA_CREDIT_CARD_TYPE = "FRIENDS_CREDIT_CARD_TYPE";
  public final String FRIENDS_DATA_CREDIT_CARD_EXP_DATE = "FRIENDS_CREDIT_CARD_EXP_DATE";
  public final String FRIENDS_DATA_CREDIT_CARD_NUMBER = "FRIENDS_CREDIT_CARD_NUMBER";
  public final String FRIENDS_DATA_NAME_APPEARANCE = "FRIENDS_NAME_APPEARANCE";
 //public final String FRIENDS_DATA_PAYMENT_TYPE = "FRIENDS_PAYMENT_TYPE";
  public final String FRIENDS_DATA_PAYMENT_DURATION = "FRIENDS_PAYMENT_DURATION";
  public final String FRIENDS_DATA_BANK_ACCOUNT = "FRIENDS_BANK_ACCOUNT";
  public final String FRIENDS_DATA_BANK_ACCOUNT_SS_NUMBER = "FRIENDS_BANK_ACCOUNT_SS_NUMBER";
  public final String FRIENDS_DATA_GIRO_PAYMENT = "FRIENDS_GIRO_PAYMENT";
  public final String FRIENDS_DATA_SOCIAL_SECURITY_NUMBER = "FRIENDS_SOCIAL_SECURITY_NUMBER";
  public final String FRIENDS_DATA_PAYMENT_AMOUNT = "FRIENDS_PAYMENT_AMOUNT";

  public GolferPageFriendsData() {
  }

  public GolferPageFriendsData(int id) throws SQLException{
    super(id);
  }

  public void setName(String name){
    setColumn(FRIENDS_DATA_NAME, name);
  }

  public void setSocialSecurityNumber(String socialSecurityNumber){
    setColumn(FRIENDS_DATA_SOCIAL_SECURITY_NUMBER, socialSecurityNumber);
  }

  public void setEmail(String email){
    setColumn(FRIENDS_DATA_EMAIL, email);
  }

  public void setAdress(String adress){
    setColumn(FRIENDS_DATA_ADRESS, adress);
  }

  public void setSigningDate(Timestamp signingDate){
    setColumn(FRIENDS_DATA_SIGNING_DATE, signingDate);
  }

  public void setCreditCardType(String creditCardType){
    setColumn(FRIENDS_DATA_CREDIT_CARD_TYPE, creditCardType);
  }

  public void setCreditCardExpDate(String creditCardExpDate){
    setColumn(FRIENDS_DATA_CREDIT_CARD_EXP_DATE, creditCardExpDate);
  }

  public void setCreditCardNumber(String creditCardNumber){
    setColumn(FRIENDS_DATA_CREDIT_CARD_NUMBER, creditCardNumber);
  }

  public void setNameAppearance(boolean nameAppearance){
    setColumn(FRIENDS_DATA_NAME_APPEARANCE, new Boolean(nameAppearance));
  }

/*  public void setPaymentType(char paymentType){
    if ((paymentType !='a') &&(paymentType !='b')) paymentType ='c';
    setColumn(FRIENDS_DATA_PAYMENT_TYPE, new Character(paymentType));
  }*/

  public void setPaymentDuration(String duration){
    setColumn(FRIENDS_DATA_PAYMENT_DURATION, duration);
  }

  public void setBankAccount(String bankAccount){
    setColumn(FRIENDS_DATA_BANK_ACCOUNT, bankAccount);
  }

  public void setGiroPayment(boolean isGiroPayment){
    setColumn(FRIENDS_DATA_GIRO_PAYMENT, new Boolean(isGiroPayment));
  }

  public void setBankAccountSSNumber(String bankAccountSSNumber){
    setColumn(FRIENDS_DATA_BANK_ACCOUNT_SS_NUMBER, bankAccountSSNumber);
  }

  public void setPaymentAmount(String paymentAmount){
    setColumn(FRIENDS_DATA_PAYMENT_AMOUNT, paymentAmount);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(FRIENDS_DATA_NAME, "name", true, true, String.class, 100);
    addAttribute(FRIENDS_DATA_SOCIAL_SECURITY_NUMBER, "social_security_number", true, false, String.class, 10);
    addAttribute(FRIENDS_DATA_EMAIL, "email", true, true, String.class, 100);
    addAttribute(FRIENDS_DATA_ADRESS, "adress", true, false, String.class);
    addAttribute(FRIENDS_DATA_SIGNING_DATE, "signing_date", true, false, Timestamp.class);

    addAttribute(FRIENDS_DATA_CREDIT_CARD_TYPE, "credit_card_type", true, false, String.class);
    //It dosn't have to be as accurate as the timestamp, what can I use???
    addAttribute(FRIENDS_DATA_CREDIT_CARD_EXP_DATE, "credit_card_exp_date", true, false, String.class);
    //Isn't String a better choice here since the number is so large??
    addAttribute(FRIENDS_DATA_CREDIT_CARD_NUMBER, "credit_card_number", true, false, String.class, 20);
    addAttribute(FRIENDS_DATA_NAME_APPEARANCE, "name_appearance", true, true, Boolean.class);

/*    //Key: 'a' means cash, 'b' means giro payment, 'c' means creditCard.  Default is giroPayment.
    addAttribute(FRIENDS_DATA_PAYMENT_TYPE, "payment_type", true, false, Character.class);*/

    //Number of monthly payments. Or something else???
    addAttribute(FRIENDS_DATA_PAYMENT_DURATION, "duration", true, false, String.class);

    addAttribute(FRIENDS_DATA_BANK_ACCOUNT, "bank_account", true, false, String.class, 20);
    addAttribute(FRIENDS_DATA_GIRO_PAYMENT, "giro_payment", true, false, Boolean.class);
    addAttribute(FRIENDS_DATA_BANK_ACCOUNT_SS_NUMBER, "bank_account_ss_number", true, false, String.class, 10);

    addAttribute(FRIENDS_DATA_PAYMENT_AMOUNT, "payment_amount", true, false, String.class);
  }

  public String getEntityName() {
    return "golfer_page_friends_data";
  }

  public String getPaymentAmount(){
    return ((!isNull(FRIENDS_DATA_PAYMENT_AMOUNT)) ? getStringColumnValue(FRIENDS_DATA_PAYMENT_AMOUNT) : "");
  }

  public String getBankAccount(){
    return ((!isNull(FRIENDS_DATA_BANK_ACCOUNT)) ? getStringColumnValue(FRIENDS_DATA_BANK_ACCOUNT) : "");
  }

  public boolean getGiroPayment(){
    return getBooleanColumnValue(FRIENDS_DATA_GIRO_PAYMENT);
  }

  public String getBankAccountSSNumber(){
    return ((!isNull(FRIENDS_DATA_BANK_ACCOUNT_SS_NUMBER)) ? getStringColumnValue(FRIENDS_DATA_BANK_ACCOUNT_SS_NUMBER) : "");
  }

  public String getName(){
    return ((!isNull(FRIENDS_DATA_NAME)) ? getStringColumnValue(FRIENDS_DATA_NAME) : "");
  }

  public String getSocialSecurityNumber(){
    return ((!isNull(FRIENDS_DATA_SOCIAL_SECURITY_NUMBER)) ? getStringColumnValue(FRIENDS_DATA_SOCIAL_SECURITY_NUMBER) : "");
  }

  public String getEmail(){
    return ((!isNull(FRIENDS_DATA_EMAIL)) ? getStringColumnValue(FRIENDS_DATA_EMAIL) : "");
  }

  public String getAdress(){
    return ((!isNull(FRIENDS_DATA_ADRESS)) ? getStringColumnValue(FRIENDS_DATA_ADRESS) : "");
  }

//This is a possible nullpointerproblem??
  public Timestamp getSigningDate(){
  //  return ((!isNull(FRIENDS_DATA_SIGNING_DATE)) ? getStringColumnValue(FRIENDS_DATA_SIGNING_DATE) : "");
    return (Timestamp) getColumnValue(FRIENDS_DATA_SIGNING_DATE);
  }

  public String getCreditCardType(){
    return ((!isNull(FRIENDS_DATA_CREDIT_CARD_TYPE)) ? getStringColumnValue(FRIENDS_DATA_CREDIT_CARD_TYPE) : "");
  }

 //This is a possible nullpointerproblem??
  public String getCreditCardExpDate(){
    return ((!isNull(FRIENDS_DATA_CREDIT_CARD_EXP_DATE)) ? getStringColumnValue(FRIENDS_DATA_CREDIT_CARD_EXP_DATE) : "");
  //  return (Date) getColumnValue(FRIENDS_DATA_CREDIT_CARD_EXP_DATE);
  }

  public String getCreditCardNumber(){
    return ((!isNull(FRIENDS_DATA_CREDIT_CARD_NUMBER)) ? getStringColumnValue(FRIENDS_DATA_CREDIT_CARD_NUMBER) : "");
  }

  public boolean getNameAppearance(){
    return getBooleanColumnValue(FRIENDS_DATA_NAME_APPEARANCE);
  }

  /*public char getPaymentType(){
    return getCharColumnValue(FRIENDS_DATA_PAYMENT_TYPE);
  }*/

  public String getPaymentDuration(){
    return ((!isNull(FRIENDS_DATA_PAYMENT_DURATION)) ? getStringColumnValue(FRIENDS_DATA_PAYMENT_DURATION) : "");
  }

}