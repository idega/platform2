/*
 *  $Id: TPosAuthorisationEntriesBeanBMPBean.java,v 1.5 2004/11/01 12:37:49 gimmi Exp $
 *
 *  Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 *
 */
package com.idega.block.creditcard.data;


import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.util.IWTimestamp;

/**
 * @author    <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version   1.0
 */

public class TPosAuthorisationEntriesBeanBMPBean extends GenericEntity implements TPosAuthorisationEntriesBean,com.idega.block.creditcard.data.TPosAuthorisationEntries {

  private final static String ENTITY_NAME = "tpos_auth_entries";
  private final static String AUTHORISATION_AMOUNT = "auth_amount";
  private final static String AUTHORISATION_CURRENCY = "auth_currency";
  private final static String AUTHORISATION_CODE = "auth_code";
  private final static String AUTHORISATION_ID_RSP = "auth_id_rsp";
  private final static String AUTHORISATION_PATH_REASON_CODE = "auth_path_res_code";
  private final static String BATCH_NUMBER = "batch_nr";
  private final static String CARD_BRAND_ID = "card_brand_id";
  private final static String CARD_BRAND_NAME = "card_brand_name";
  private final static String CARD_CHARACTERISTICS = "card_caracter";
  private final static String CARD_TYPE_ID = "card_type_id";
  private final static String CARD_TYPE_NAME = "card_type_name";
  private final static String ENTRY_DATE = "entry_date";
  private final static String DETAIL_EXPECTED = "detail";
  private final static String ERROR_NR = "error_nr";
  private final static String ERROR_TEXT = "error_text";
  private final static String CARD_EXPIRES = "card_expires";
  private final static String LOCATION_NR = "location_nr";
  private final static String MERCHANT_NR_AUTHORISATION = "merc_auth";
  private final static String MERCHANT_NR_OTHER_SERVICES = "merc_other";
  private final static String MERCHANT_NR_SUBMISSION = "merc_sub";
  private final static String ATTACHMENT_COUNT = "att_count";
  private final static String PAN = "pan";
  private final static String POS_NR = "pos_nr";
  private final static String POS_SERIAL_NR = "pos_ser_nr";
  private final static String PRINT_DATA = "print_data";
  private final static String SUBMISSION_AMOUNT = "sub_amount";
  private final static String SUBMISSION_CURRENCY = "sub_currency";
  private final static String ENTRY_TIME = "entry_time";
  private final static String TOTAL_RESPONSE_CODE = "rsp_code";
  private final static String TRANSACTION_NR = "transact_nr";
  private final static String VOIDED_ATHORISATION_ID_RESPONSE = "void_rsp";
  private final static String VOIDED_TRANSACTION_NR = "void_trans";
  private final static String XML_ATTACHMENT = "xml";
  private final static String CARD_NUMBER = "card_number";
  private final static String PARENT_ID = "parent_id";

  /**
   * Constructor for the TPosAuthorisationEntriesBean object
   */

  public TPosAuthorisationEntriesBeanBMPBean() {
    super();
  }

  /**
   * Constructor for the TPosAuthorisationEntriesBean object
   *
   * @param id                Description of the Parameter
   * @exception SQLException  Description of the Exception
   */

  public TPosAuthorisationEntriesBeanBMPBean(int id) throws SQLException {
    super(id);
  }

  /**
   * Description of the Method
   */
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getAuthorisationAmountColumnName(), "", true, true, java.lang.String.class, 20);
    addAttribute(getAuthorisationCurrencyColumnName(), "", true, true, java.lang.String.class, 6);
    addAttribute(getAuthorisationCodeColumnName(), "", true, true, java.lang.String.class, 3);
    addAttribute(getAuthorisationIdRspColumnName(), "", true, true, java.lang.String.class, 10);
    addAttribute(getAuthorisationPathReasonCodeColumnName(), "", true, true, java.lang.String.class, 5);
    addAttribute(getBatchNumberColumnName(), "", true, true, java.lang.String.class, 5);
    addAttribute(getBrandIdColumnName(), "", true, true, java.lang.String.class, 20);
    addAttribute(getBrandNameColumnName(), "", true, true, java.lang.String.class, 20);
    addAttribute(getCardCharacteristicsColumnName(), "", true, true, java.lang.String.class, 5);
    addAttribute(getCardTypeColumnName(), "", true, true, java.lang.String.class, 10);
    addAttribute(getCardNameColumnName(), "", true, true, java.lang.String.class, 20);
    addAttribute(getEntryDateColumnName(), "", true, true, java.lang.String.class, 8);
    addAttribute(getDetailExpectedColumnName(), "", true, true, java.lang.String.class);
    addAttribute(getErrorNoColumnName(), "", true, true, java.lang.String.class, 10);
    addAttribute(getErrorTextColumnName(), "", true, true, java.lang.String.class);
    addAttribute(getCardExpiresColumnName(), "", true, true, java.lang.String.class, 4);
    addAttribute(getLocationNrColumnName(), "", true, true, java.lang.String.class, 10);
    addAttribute(getMerchantNrAuthorisationColumnName(), "", true, true, java.lang.String.class, 10);
    addAttribute(getMerchantNrOtherServicesColumnName(), "", true, true, java.lang.String.class, 10);
    addAttribute(getMerchantNrSubmissionColumnName(), "", true, true, java.lang.String.class, 20);
    addAttribute(getAttachmentCountColumnName(), "", true, true, java.lang.String.class);
    addAttribute(getPanColumnName(), "", true, true, java.lang.String.class);
    addAttribute(getPosNrColumnName(), "", true, true, java.lang.String.class, 20);
    addAttribute(getPosSerialNrColumnName(), "", true, true, java.lang.String.class, 20);
    addAttribute(getPrintDataColumnName(), "", true, true, java.lang.String.class);
    addAttribute(getSubmissionAmountColumnName(), "", true, true, java.lang.String.class, 10);
    addAttribute(getSubmissionCurrencyColumnName(), "", true, true, java.lang.String.class, 5);
    addAttribute(getEntryTimeColumnName(), "", true, true, java.lang.String.class, 6);
    addAttribute(getTotalResponseCodeColumnName(), "", true, true, java.lang.String.class, 6);
    addAttribute(getTransactionNrColumnName(), "", true, true, java.lang.String.class, 6);
    addAttribute(getVoidedAuthorisationIdResponseColumnName(), "", true, true, java.lang.String.class);
    addAttribute(getVoidedTransactionNrColumnName(), "", true, true, java.lang.String.class);
    addAttribute(getXMLAttachmentColumnName(), "", true, true, java.lang.String.class);
    addAttribute(CARD_NUMBER, "card_number", true, true, String.class, 50);
    
    this.addOneToOneRelationship(PARENT_ID, TPosAuthorisationEntriesBean.class);
  }

  /**
   * Gets the entityName attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The entityName value
   */
  public String getEntityName() {
    return ENTITY_NAME;
  }

  /**
   * Gets the authorisationAmountColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The authorisationAmountColumnName value
   */
  public static String getAuthorisationAmountColumnName() {
    return AUTHORISATION_AMOUNT;
  }

  /**
   * Gets the authorisationCurrencyColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The authorisationCurrencyColumnName value
   */
  public static String getAuthorisationCurrencyColumnName() {
    return AUTHORISATION_CURRENCY;
  }

  /**
   * Gets the authorisationCodeColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The authorisationCodeColumnName value
   */
  public static String getAuthorisationCodeColumnName() {
    return AUTHORISATION_CODE;
  }

  /**
   * Gets the authorisationIdRspColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The authorisationIdRspColumnName value
   */
  public static String getAuthorisationIdRspColumnName() {
    return AUTHORISATION_ID_RSP;
  }

  /**
   * Gets the authorisationPathReasonCodeColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The authorisationPathReasonCodeColumnName value
   */
  public static String getAuthorisationPathReasonCodeColumnName() {
    return AUTHORISATION_PATH_REASON_CODE;
  }

  /**
   * Gets the batchNumberColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The batchNumberColumnName value
   */
  public static String getBatchNumberColumnName() {
    return BATCH_NUMBER;
  }

  /**
   * Gets the brandIdColumnName attribute of the TPosAuthorisationEntriesBean
   * class
   *
   * @return   The brandIdColumnName value
   */
  public static String getBrandIdColumnName() {
    return CARD_BRAND_ID;
  }

  /**
   * Gets the brandNameColumnName attribute of the TPosAuthorisationEntriesBean
   * class
   *
   * @return   The brandNameColumnName value
   */
  public static String getBrandNameColumnName() {
    return CARD_BRAND_NAME;
  }

  /**
   * Gets the cardCharacteristicsColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The cardCharacteristicsColumnName value
   */
  public static String getCardCharacteristicsColumnName() {
    return CARD_CHARACTERISTICS;
  }

  /**
   * Gets the cardTypeColumnName attribute of the TPosAuthorisationEntriesBean
   * class
   *
   * @return   The cardTypeColumnName value
   */
  public static String getCardTypeColumnName() {
    return CARD_TYPE_ID;
  }

  /**
   * Gets the cardNameColumnName attribute of the TPosAuthorisationEntriesBean
   * class
   *
   * @return   The cardNameColumnName value
   */
  public static String getCardNameColumnName() {
    return CARD_TYPE_NAME;
  }

  /**
   * Gets the entryDateColumnName attribute of the TPosAuthorisationEntriesBean
   * class
   *
   * @return   The entryDateColumnName value
   */
  public static String getEntryDateColumnName() {
    return ENTRY_DATE;
  }

  /**
   * Gets the detailExpectedColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The detailExpectedColumnName value
   */
  public static String getDetailExpectedColumnName() {
    return DETAIL_EXPECTED;
  }

  /**
   * Gets the errorNoColumnName attribute of the TPosAuthorisationEntriesBean
   * class
   *
   * @return   The errorNoColumnName value
   */
  public static String getErrorNoColumnName() {
    return ERROR_NR;
  }

  /**
   * Gets the errorTextColumnName attribute of the TPosAuthorisationEntriesBean
   * class
   *
   * @return   The errorTextColumnName value
   */
  public static String getErrorTextColumnName() {
    return ERROR_TEXT;
  }

  /**
   * Gets the cardExpiresColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The cardExpiresColumnName value
   */
  public static String getCardExpiresColumnName() {
    return CARD_EXPIRES;
  }

  /**
   * Gets the locationNrColumnName attribute of the TPosAuthorisationEntriesBean
   * class
   *
   * @return   The locationNrColumnName value
   */
  public static String getLocationNrColumnName() {
    return LOCATION_NR;
  }

  /**
   * Gets the merchantNrAuthorisationColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The merchantNrAuthorisationColumnName value
   */
  public static String getMerchantNrAuthorisationColumnName() {
    return MERCHANT_NR_AUTHORISATION;
  }

  /**
   * Gets the merchantNrOtherServicesColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The merchantNrOtherServicesColumnName value
   */
  public static String getMerchantNrOtherServicesColumnName() {
    return MERCHANT_NR_OTHER_SERVICES;
  }

  /**
   * Gets the merchantNrSubmissionColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The merchantNrSubmissionColumnName value
   */
  public static String getMerchantNrSubmissionColumnName() {
    return MERCHANT_NR_SUBMISSION;
  }

  /**
   * Gets the attachmentCountColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The attachmentCountColumnName value
   */
  public static String getAttachmentCountColumnName() {
    return ATTACHMENT_COUNT;
  }

  /**
   * Gets the panColumnName attribute of the TPosAuthorisationEntriesBean class
   *
   * @return   The panColumnName value
   */
  public static String getPanColumnName() {
    return PAN;
  }

  /**
   * Gets the posNrColumnName attribute of the TPosAuthorisationEntriesBean
   * class
   *
   * @return   The posNrColumnName value
   */
  public static String getPosNrColumnName() {
    return POS_NR;
  }

  /**
   * Gets the posSerialNrColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The posSerialNrColumnName value
   */
  public static String getPosSerialNrColumnName() {
    return POS_SERIAL_NR;
  }

  /**
   * Gets the printDataColumnName attribute of the TPosAuthorisationEntriesBean
   * class
   *
   * @return   The printDataColumnName value
   */
  public static String getPrintDataColumnName() {
    return PRINT_DATA;
  }

  /**
   * Gets the submissionAmountColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The submissionAmountColumnName value
   */
  public static String getSubmissionAmountColumnName() {
    return SUBMISSION_AMOUNT;
  }

  /**
   * Gets the submissionCurrencyColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The submissionCurrencyColumnName value
   */
  public static String getSubmissionCurrencyColumnName() {
    return SUBMISSION_CURRENCY;
  }

  /**
   * Gets the entryTimeColumnName attribute of the TPosAuthorisationEntriesBean
   * class
   *
   * @return   The entryTimeColumnName value
   */
  public static String getEntryTimeColumnName() {
    return ENTRY_TIME;
  }

  /**
   * Gets the totalResponseCodeColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The totalResponseCodeColumnName value
   */
  public static String getTotalResponseCodeColumnName() {
    return TOTAL_RESPONSE_CODE;
  }

  /**
   * Gets the transactionNrColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The transactionNrColumnName value
   */
  public static String getTransactionNrColumnName() {
    return TRANSACTION_NR;
  }

  /**
   * Gets the voidedAuthorisationIdResponseColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The voidedAuthorisationIdResponseColumnName value
   */
  public static String getVoidedAuthorisationIdResponseColumnName() {
    return VOIDED_ATHORISATION_ID_RESPONSE;
  }

  /**
   * Gets the voidedTransactionNrColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The voidedTransactionNrColumnName value
   */
  public static String getVoidedTransactionNrColumnName() {
    return VOIDED_TRANSACTION_NR;
  }

  /**
   * Gets the xMLAttachmentColumnName attribute of the
   * TPosAuthorisationEntriesBean class
   *
   * @return   The xMLAttachmentColumnName value
   */
  public static String getXMLAttachmentColumnName() {
    return XML_ATTACHMENT;
  }

  /**
   * Gets the authorisationAmount attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @return   The authorisationAmount value
   */
  public String getAuthorisationAmount() {
    return getStringColumnValue(getAuthorisationAmountColumnName());
  }

  /**
   * Gets the authorisationCurrency attribute of the
   * TPosAuthorisationEntriesBean object
   *
   * @return   The authorisationCurrency value
   */
  public String getAuthorisationCurrency() {
    return getStringColumnValue(getAuthorisationCurrencyColumnName());
  }

  /**
   * Gets the authorisationCode attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @return   The authorisationCode value
   */
  public String getAuthorisationCode() {
    return getStringColumnValue(getAuthorisationCodeColumnName());
  }

  /**
   * Gets the authorisationIdRsp attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @return   The authorisationIdRsp value
   */
  public String getAuthorisationIdRsp() {
    return getStringColumnValue(getAuthorisationIdRspColumnName());
  }

  /**
   * Gets the authorisationPathReasonCode attribute of the
   * TPosAuthorisationEntriesBean object
   *
   * @return   The authorisationPathReasonCode value
   */
  public String getAuthorisationPathReasonCode() {
    return getStringColumnValue(getAuthorisationPathReasonCodeColumnName());
  }

  /**
   * Gets the batchNumber attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The batchNumber value
   */
  public String getBatchNumber() {
    return getStringColumnValue(getBatchNumberColumnName());
  }

  /**
   * Gets the brandId attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The brandId value
   */
  public String getBrandId() {
    return getStringColumnValue(getBrandIdColumnName());
  }

  /**
   * Gets the brandName attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The brandName value
   */
  public String getBrandName() {
    return getStringColumnValue(getBrandNameColumnName());
  }

  /**
   * Gets the cardCharacteristics attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @return   The cardCharacteristics value
   */
  public String getCardCharacteristics() {
    return getStringColumnValue(getCardCharacteristicsColumnName());
  }

  /**
   * Gets the cardType attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The cardType value
   */
  public String getCardType() {
    return getStringColumnValue(getCardTypeColumnName());
  }

  /**
   * Gets the cardName attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The cardName value
   */
  public String getCardName() {
    return getStringColumnValue(getCardNameColumnName());
  }

  /**
   * Gets the entryDate attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The entryDate value
   */
  public String getEntryDate() {
    return getStringColumnValue(getEntryDateColumnName());
  }

  /**
   * Gets the detailExpected attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @return   The detailExpected value
   */
  public String getDetailExpected() {
    return getStringColumnValue(getDetailExpectedColumnName());
  }

  /**
   * Gets the errorNo attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The errorNo value
   */
  public String getErrorNo() {
    return getStringColumnValue(getErrorNoColumnName());
  }

  /**
   * Gets the errorText attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The errorText value
   */
  public String getErrorText() {
    return getStringColumnValue(getErrorTextColumnName());
  }

  /**
   * Gets the cardExpires attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The cardExpires value
   */
  public String getCardExpires() {
    return getStringColumnValue(getCardExpiresColumnName());
  }

  /**
   * Gets the locationNr attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The locationNr value
   */
  public String getLocationNr() {
    return getStringColumnValue(getLocationNrColumnName());
  }

  /**
   * Gets the merchantNrAuthorisation attribute of the
   * TPosAuthorisationEntriesBean object
   *
   * @return   The merchantNrAuthorisation value
   */
  public String getMerchantNrAuthorisation() {
    return getStringColumnValue(getMerchantNrAuthorisationColumnName());
  }

  /**
   * Gets the merchantNrOtherServices attribute of the
   * TPosAuthorisationEntriesBean object
   *
   * @return   The merchantNrOtherServices value
   */
  public String getMerchantNrOtherServices() {
    return getStringColumnValue(getMerchantNrOtherServicesColumnName());
  }

  /**
   * Gets the merchantNrSubmission attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @return   The merchantNrSubmission value
   */
  public String getMerchantNrSubmission() {
    return getStringColumnValue(getMerchantNrSubmissionColumnName());
  }

  /**
   * Gets the attachmentCount attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @return   The attachmentCount value
   */
  public String getAttachmentCount() {
    return getStringColumnValue(getAttachmentCountColumnName());
  }

  /**
   * Gets the pan attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The pan value
   */
  public String getPan() {
    return getStringColumnValue(getPanColumnName());
  }

  /**
   * Gets the posNr attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The posNr value
   */
  public String getPosNr() {
    return getStringColumnValue(getPosNrColumnName());
  }

  /**
   * Gets the posSerialNr attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The posSerialNr value
   */
  public String getPosSerialNr() {
    return getStringColumnValue(getPosSerialNrColumnName());
  }

  /**
   * Gets the printData attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The printData value
   */
  public String getPrintData() {
    return getStringColumnValue(getPrintDataColumnName());
  }

  /**
   * Gets the submissionAmount attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @return   The submissionAmount value
   */
  public String getSubmissionAmount() {
    return getStringColumnValue(getSubmissionAmountColumnName());
  }

  /**
   * Gets the submissionCurrency attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @return   The submissionCurrency value
   */
  public String getSubmissionCurrency() {
    return getStringColumnValue(getSubmissionCurrencyColumnName());
  }

  /**
   * Gets the entryTime attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The entryTime value
   */
  public String getEntryTime() {
    return getStringColumnValue(getEntryTimeColumnName());
  }

  /**
   * Gets the totalResponseCode attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @return   The totalResponseCode value
   */
  public String getTotalResponseCode() {
    return getStringColumnValue(getTotalResponseCodeColumnName());
  }

  /**
   * Gets the transactionNr attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The transactionNr value
   */
  public String getTransactionNr() {
    return getStringColumnValue(getTransactionNrColumnName());
  }

  /**
   * Gets the voidedAuthorisationIdResponse attribute of the
   * TPosAuthorisationEntriesBean object
   *
   * @return   The voidedAuthorisationIdResponse value
   */
  public String getVoidedAuthorisationIdResponse() {
    return getStringColumnValue(getVoidedAuthorisationIdResponseColumnName());
  }

  /**
   * Gets the voidedTransactionNr attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @return   The voidedTransactionNr value
   */
  public String getVoidedTransactionNr() {
    return getStringColumnValue(getVoidedTransactionNrColumnName());
  }

  /**
   * Gets the xMLAttachment attribute of the TPosAuthorisationEntriesBean object
   *
   * @return   The xMLAttachment value
   */
  public String getXMLAttachment() {
    return getStringColumnValue(getXMLAttachmentColumnName());
  }

  /**
   * Sets the authorisationAmount attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @param amount  The new authorisationAmount value
   */
  public void setAuthorisationAmount(String amount) {
    setColumn(getAuthorisationAmountColumnName(), amount);
  }

  /**
   * Sets the authorisationCurrency attribute of the
   * TPosAuthorisationEntriesBean object
   *
   * @param currency  The new authorisationCurrency value
   */
  public void setAuthorisationCurrency(String currency) {
    setColumn(getAuthorisationCurrencyColumnName(), currency);
  }

  /**
   * Sets the authorisationCode attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @param code  The new authorisationCode value
   */
  public void setAuthorisationCode(String code) {
    setColumn(getAuthorisationCodeColumnName(), code);
  }

  /**
   * Sets the authorisationIdRsp attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @param rsp  The new authorisationIdRsp value
   */
  public void setAuthorisationIdRsp(String rsp) {
    setColumn(getAuthorisationIdRspColumnName(), rsp);
  }

  /**
   * Sets the authorisationPathReasonCode attribute of the
   * TPosAuthorisationEntriesBean object
   *
   * @param code  The new authorisationPathReasonCode value
   */
  public void setAuthorisationPathReasonCode(String code) {
    setColumn(getAuthorisationPathReasonCodeColumnName(), code);
  }

  /**
   * Sets the batchNumber attribute of the TPosAuthorisationEntriesBean object
   *
   * @param number  The new batchNumber value
   */
  public void setBatchNumber(String number) {
    setColumn(getBatchNumberColumnName(), number);
  }

  /**
   * Sets the brandId attribute of the TPosAuthorisationEntriesBean object
   *
   * @param id  The new brandId value
   */
  public void setBrandId(String id) {
    setColumn(getBrandIdColumnName(), id);
  }

  /**
   * Sets the brandName attribute of the TPosAuthorisationEntriesBean object
   *
   * @param name  The new brandName value
   */
  public void setBrandName(String name) {
    setColumn(getBrandNameColumnName(), name);
  }

  /**
   * Sets the cardCharacteristics attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @param characteristics  The new cardCharacteristics value
   */
  public void setCardCharacteristics(String characteristics) {
    setColumn(getCardCharacteristicsColumnName(), characteristics);
  }

  /**
   * Sets the cardType attribute of the TPosAuthorisationEntriesBean object
   *
   * @param type  The new cardType value
   */
  public void setCardType(String type) {
    setColumn(getCardTypeColumnName(), type);
  }

  /**
   * Sets the cardName attribute of the TPosAuthorisationEntriesBean object
   *
   * @param name  The new cardName value
   */
  public void setCardName(String name) {
    setColumn(getCardNameColumnName(), name);
  }

  /**
   * Sets the entryDate attribute of the TPosAuthorisationEntriesBean object
   *
   * @param date  The new entryDate value
   */
  public void setEntryDate(String date) {
    setColumn(getEntryDateColumnName(), date);
  }

  /**
   * Sets the detailExpected attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @param expected  The new detailExpected value
   */
  public void setDetailExpected(String expected) {
    setColumn(getDetailExpectedColumnName(), expected);
  }

  /**
   * Sets the errorNo attribute of the TPosAuthorisationEntriesBean object
   *
   * @param no  The new errorNo value
   */
  public void setErrorNo(String no) {
    setColumn(getErrorNoColumnName(), no);
  }

  /**
   * Sets the errorText attribute of the TPosAuthorisationEntriesBean object
   *
   * @param text  The new errorText value
   */
  public void setErrorText(String text) {
    setColumn(getErrorTextColumnName(), text);
  }

  /**
   * Sets the cardExpires attribute of the TPosAuthorisationEntriesBean object
   *
   * @param expires  The new cardExpires value
   */
  public void setCardExpires(String expires) {
    setColumn(getCardExpiresColumnName(), expires);
  }

  /**
   * Sets the locationNr attribute of the TPosAuthorisationEntriesBean object
   *
   * @param location  The new locationNr value
   */
  public void setLocationNr(String location) {
    setColumn(getLocationNrColumnName(), location);
  }

  /**
   * Sets the merchantNrAuthorisation attribute of the
   * TPosAuthorisationEntriesBean object
   *
   * @param nr  The new merchantNrAuthorisation value
   */
  public void setMerchantNrAuthorisation(String nr) {
    setColumn(getMerchantNrAuthorisationColumnName(), nr);
  }

  /**
   * Sets the merchantNrOtherServices attribute of the
   * TPosAuthorisationEntriesBean object
   *
   * @param nr  The new merchantNrOtherServices value
   */
  public void setMerchantNrOtherServices(String nr) {
    setColumn(getMerchantNrOtherServicesColumnName(), nr);
  }

  /**
   * Sets the merchantNrSubmission attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @param nr  The new merchantNrSubmission value
   */
  public void setMerchantNrSubmission(String nr) {
    setColumn(getMerchantNrSubmissionColumnName(), nr);
  }

  /**
   * Sets the attachmentCount attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @param count  The new attachmentCount value
   */
  public void setAttachmentCount(String count) {
    setColumn(getAttachmentCountColumnName(), count);
  }

  /**
   * Sets the pan attribute of the TPosAuthorisationEntriesBean object
   *
   * @param pan  The new pan value
   */
  public void setPan(String pan) {
    setColumn(getPanColumnName(), pan);
  }

  /**
   * Sets the posNr attribute of the TPosAuthorisationEntriesBean object
   *
   * @param nr  The new posNr value
   */
  public void setPosNr(String nr) {
    setColumn(getPosNrColumnName(), nr);
  }

  /**
   * Sets the posSerialNr attribute of the TPosAuthorisationEntriesBean object
   *
   * @param nr  The new posSerialNr value
   */
  public void setPosSerialNr(String nr) {
    setColumn(getPosSerialNrColumnName(), nr);
  }

  /**
   * Sets the printData attribute of the TPosAuthorisationEntriesBean object
   *
   * @param data  The new printData value
   */
  public void setPrintData(String data) {
    setColumn(getPrintDataColumnName(), data);
  }

  /**
   * Sets the submissionAmount attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @param amount  The new submissionAmount value
   */
  public void setSubmissionAmount(String amount) {
    setColumn(getSubmissionAmountColumnName(), amount);
  }

  /**
   * Sets the submissionCurrency attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @param currency  The new submissionCurrency value
   */
  public void setSubmissionCurrency(String currency) {
    setColumn(getSubmissionCurrencyColumnName(), currency);
  }

  /**
   * Sets the entryTime attribute of the TPosAuthorisationEntriesBean object
   *
   * @param time  The new entryTime value
   */
  public void setEntryTime(String time) {
    setColumn(getEntryTimeColumnName(), time);
  }

  /**
   * Sets the totalResponseCode attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @param code  The new totalResponseCode value
   */
  public void setTotalResponseCode(String code) {
    setColumn(getTotalResponseCodeColumnName(), code);
  }

  /**
   * Sets the transactionNr attribute of the TPosAuthorisationEntriesBean object
   *
   * @param nr  The new transactionNr value
   */
  public void setTransactionNr(String nr) {
    setColumn(getTransactionNrColumnName(), nr);
  }

  /**
   * Sets the voidedAuthorisationIdResponse attribute of the
   * TPosAuthorisationEntriesBean object
   *
   * @param response  The new voidedAuthorisationIdResponse value
   */
  public void setVoidedAuthorisationIdResponse(String response) {
    setColumn(getVoidedAuthorisationIdResponseColumnName(), response);
  }

  /**
   * Sets the voidedTransactionNr attribute of the TPosAuthorisationEntriesBean
   * object
   *
   * @param nr  The new voidedTransactionNr value
   */
  public void setVoidedTransactionNr(String nr) {
    setColumn(getVoidedTransactionNrColumnName(), nr);
  }

  /**
   * Sets the xMLAttachment attribute of the TPosAuthorisationEntriesBean object
   *
   * @param xml  The new xMLAttachment value
   */
  public void setXMLAttachment(String xml) {
    setColumn(getXMLAttachmentColumnName(), xml);
  }
  
	public double getAmount() {
		String amount = getAuthorisationAmount();
		try {
			return Double.parseDouble(amount);
		} catch (NumberFormatException n) {
			return -1;
		}
	}
	public String getCurrency() {
		return this.getAuthorisationCurrency();
	}
	public Date getDate() {
		String date = this.getEntryDate();
		String time = this.getEntryTime();
		
		// Trying to make a string like this : yyyy-mm-dd hh:mm:ss
		try {
			StringBuffer sqlString = new StringBuffer();
			sqlString.append(date.substring(0, 4)).append("-").append(date.substring(4, 6)).append("-").append(date.substring(6, 8));
			if (time != null) {
				sqlString.append(" ").append(time.substring(0, 2)).append(":").append(time.substring(2, 4)).append(":").append(time.substring(4, 6));
			}
			
			IWTimestamp stamp = new IWTimestamp(sqlString.toString());
			return stamp.getDate();
		} catch (Exception e) {
			return null;
		}
	}
	
	public String getAuthorizationCode() {
		return getAuthorisationIdRsp();
	}

	public Object ejbFindByAuthorisationIdRsp(String authIdRsp, IWTimestamp stamp) throws FinderException {
		String dateString = stamp.getDateString("yyyyMMdd");
		Table table = new Table(this);
		Column auth = new Column(table, AUTHORISATION_ID_RSP);
		Column date = new Column(table, ENTRY_DATE);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(auth, MatchCriteria.EQUALS, authIdRsp));
		query.addCriteria(new MatchCriteria(date, MatchCriteria.EQUALS, dateString));

		return this.idoFindOnePKBySQL(query.toString());
		//return this.idoFindOnePKByColumnBySQL(AUTHORISATION_ID_RSP, authIdRsp);
	}

	/**
	 * Set the creditcard number, which should be encrypted FIRST
   * @param cardNumber  The new cardNumber value	 */
	public void setCardNumber(String cardNumber) {
		setColumn(CARD_NUMBER, cardNumber);
	}
	
  /**
   * Gets the creditCard value
   * TPosAuthorisationEntriesBean object
   *
   * @return   The creditCard value
   */
	public String getCardNumber() {
		return getStringColumnValue(CARD_NUMBER);
	}

	public int getParentID() {
		return getIntColumnValue(PARENT_ID);
	}

	public CreditCardAuthorizationEntry getParent() {
		return (TPosAuthorisationEntriesBean) getColumnValue(PARENT_ID);
	}
	
	public void setParentID(int id) {
		setColumn(PARENT_ID, id);
	}

	public String getErrorNumber() {
		return getErrorNo();
	}
	
	public CreditCardAuthorizationEntry getChild() throws FinderException {
		Object obj = this.idoFindOnePKByColumnBySQL(PARENT_ID, this.getPrimaryKey().toString());
		if (obj != null) {
			TPosAuthorisationEntriesBeanHome home;
			try {
				home = (TPosAuthorisationEntriesBeanHome) IDOLookup.getHome(TPosAuthorisationEntriesBean.class);
				return home.findByPrimaryKey(obj);
			}
			catch (IDOLookupException e) {
				throw new FinderException(e.getMessage());
			}
		}
		return null;
	}
	
	public String getExtraField() {
		// NOT USED
		return null;
	}
	
	public Collection ejbFindRefunds(IWTimestamp from, IWTimestamp to) throws FinderException {
		to.addDays(1);

		String fromDate = from.getDateString("yyyyMMdd");
		String toDate = to.getDateString("yyyyMMdd");

		Table table = new Table(this);
		Column date = new Column(ENTRY_DATE);
		Column code = new Column(AUTHORISATION_CODE);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(code, MatchCriteria.EQUALS, "T5"));
		query.addCriteria(new MatchCriteria(date, MatchCriteria.GREATEREQUAL, fromDate));
		query.addCriteria(new MatchCriteria(date, MatchCriteria.LESSEQUAL, toDate));
		
		return this.idoFindIDsBySQL(query.toString());
	}
}
