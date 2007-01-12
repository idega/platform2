/*
 *  $Id: TPosClient.java,v 1.12.2.1 2007/01/12 19:31:19 idegaweb Exp $
 *
 *  Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 *
 */
package com.idega.block.creditcard.business;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.data.TPosAuthorisationEntriesBean;
import com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.util.IWTimestamp;
import com.tpos.client.TPOS3Client;

/**
 * @author    <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version   1.0
 */
public class TPosClient implements CreditCardClient{

  public final static String TPOS_USER_ID = "tpos_userid";
  public final static String TPOS_PASSWD = "tpos_passwd";
  public final static String TPOS_MERCHANT_ID = "tpos_merchantid";
  public final static String TPOS_LOCATION_ID = "tpos_locationid";
  public final static String TPOS_POS_ID = "tpos_posid";
  public final static String TPOS_KEY_RECEIVE_PASSWD = "tpos_keyreceivepasswd";
  public final static String TPOS_IP_SET = "tpos_ipset";

  private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.creditcard";
  private TPOS3Client _client = null;
  private IWBundle _iwb = null;
  private IWApplicationContext _iwc = null;
  //private IWResourceBundle _iwrb = null;
  
  private String _userId = null;
  private String _passwd = null;
  private String _merchantId = null;
  private String _locationId = null;
  private String _posId = null;
  private String _receivePasswd = null;
  private CreditCardMerchant _merchant = null;

  private int amountMultiplier = 100;

  /**
   * Constructor for the TPosClient object
   *
   * @param iwc            Description of the Parameter
   * @exception Exception  Description of the Exception
   */
  public TPosClient(IWApplicationContext iwc, CreditCardMerchant merchant) throws Exception {
    this._merchant = merchant;
    init(iwc);
  }

  public TPosClient(IWApplicationContext iwc) throws Exception {
    init(iwc);
  }

  private void init(IWApplicationContext iwc) throws Exception{
  		this._iwc = iwc;
    this._iwb = iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
    //if (_iwb != null) {
    //  _iwrb = _iwb.getResourceBundle(iwc);
    //}

    String path = this._iwb.getPropertiesRealPath();
    if (path == null) {
      throw new Exception("Unable to find properties file");
    }

    String seperator = java.io.File.separator;
    if (!path.endsWith(seperator)) {
      path = path + seperator + this._iwb.getProperty("properties_file");
    }
    else {
      path = path + this._iwb.getProperty("properties_file");
    }

    try {
      this._client = new TPOS3Client(path);
      String ipset = this._iwb.getProperty(TPOS_IP_SET);
      this._client.setIPSet(Integer.parseInt(ipset));


      if (this._merchant == null) {
      	System.out.println("TPosClient : Using default TPosMerchant, ipset = "+ipset);
        this._userId = this._iwb.getProperty(TPOS_USER_ID);
        this._passwd = this._iwb.getProperty(TPOS_PASSWD);
        this._merchantId = this._iwb.getProperty(TPOS_MERCHANT_ID);
        this._locationId = this._iwb.getProperty(TPOS_LOCATION_ID);
        this._posId = this._iwb.getProperty(TPOS_POS_ID);
        this._receivePasswd = this._iwb.getProperty(TPOS_KEY_RECEIVE_PASSWD);
      }else {
      	System.out.println("TPosClient : Using TPosMerchant "+this._merchant.getName()+", ipset = "+ipset);
        this._userId = this._merchant.getUser();
        this._passwd = this._merchant.getPassword();
        this._merchantId = this._merchant.getMerchantID();
        this._locationId = this._merchant.getLocation();
        this._posId = this._merchant.getTerminalID();
        this._receivePasswd = this._merchant.getExtraInfo();
      }
    }

    catch (Exception e) {
      System.out.println("Got an exception trying to create client");
      e.printStackTrace();
    }
  }

  /*
  public void setupClient(String merchant, String location, String user, String password, String terminal, String extraInfo) {
    _userId = _TPosMerchant.getUserID();
    _passwd = _TPosMerchant.getPassword();
    _merchantId = merchant;
    _locationId = lc;
    _posId = _TPosMerchant.getPosID();
    _receivePasswd = _TPosMerchant.getKeyReceivedPassword();
  }
*/
  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public String createNewBatch() {
    this._client.setProperty(TPOS3Client.PN_USERID, this._userId);
    this._client.setProperty(TPOS3Client.PN_PASSWORD, this._passwd);
    this._client.setProperty(TPOS3Client.PN_MERCHANTID, this._merchantId);
    this._client.setProperty(TPOS3Client.PN_LOCATIONID, this._locationId);
    this._client.setProperty(TPOS3Client.PN_POSID, this._posId);

    boolean created = this._client.sendNewBatchReq();
    if (!created) {
      System.err.println("Error no: " + this._client.getProperty(TPOS3Client.PN_ERRORNUMBER));
      System.err.println("Error string : " + this._client.getProperty(TPOS3Client.PN_ERRORTEXT));

      return (null);
    }

    String newBatchNumber = this._client.getProperty(TPOS3Client.PN_OPENEDBATCHNR);

    return (newBatchNumber);
  }

  /**
   * Gets the cACertificate attribute of the TPosClient object
   *
   * @return   The cACertificate value
   */
  public boolean getCACertificate() {
    this._client.setProperty(TPOS3Client.PN_MERCHANTID, this._merchantId);
    this._client.setProperty(TPOS3Client.PN_LOCATIONID, this._locationId);
    this._client.setProperty(TPOS3Client.PN_POSID, this._posId);

    boolean valid = this._client.sendCACertificateReq();

    if (!valid) {
      System.err.println("Error no: " + this._client.getProperty(TPOS3Client.PN_ERRORNUMBER));
      System.err.println("Error string : " + this._client.getProperty(TPOS3Client.PN_ERRORTEXT));
    }

    if (Integer.parseInt(this._client.getProperty(TPOS3Client.PN_TOTALRESPONSECODE), 10) == 0) {
      this._client.confirmCACertificate();
    }
    else {
      System.err.println("Error no: " + this._client.getProperty(TPOS3Client.PN_ERRORNUMBER));
      System.err.println("Error string : " + this._client.getProperty(TPOS3Client.PN_ERRORTEXT));

      valid = false;
    }
    return (valid);
  }

  /**
   * Gets the keys attribute of the TPosClient object
   *
   * @return   The keys value
   */
  public boolean getKeys() {
    this._client.setProperty(TPOS3Client.PN_MERCHANTID, this._merchantId);
    this._client.setProperty(TPOS3Client.PN_LOCATIONID, this._locationId);
    this._client.setProperty(TPOS3Client.PN_POSID, this._posId);

    this._client.setProperty(TPOS3Client.PN_KEYRECEIVEPASSWORD, this._receivePasswd);

    boolean valid = this._client.sendKeyPairReq();

    if (!valid) {
      System.err.println("Error no: " + this._client.getProperty(TPOS3Client.PN_ERRORNUMBER));
      System.err.println("Error string : " + this._client.getProperty(TPOS3Client.PN_ERRORTEXT));
    }

    return (valid);
  }
  public String creditcardAuthorization(String nameOnCard, String cardnumber, String monthExpires, String yearExpires, String ccVerifyNumber, double amount, String currency, String referenceNumber) throws CreditCardAuthorizationException{
  	String authID = (doAuth(cardnumber, monthExpires, yearExpires, amount, currency, "5", null, null));
  	
  	StringBuffer buffer = getPropertyString(this._client);
  	buffer.append(TPOS3Client.PN_AUTHORIDENTIFYRSP).append("=").append(authID);
  	
  	return buffer.toString();
  }
  
  
  private StringBuffer getPropertyString(TPOS3Client client) {
  	StringBuffer buffer = new StringBuffer();
//  	buffer.append(TPOS3Client.PN_USERID).append("=").append(client.getProperty(TPOS3Client.PN_PASSWORD));
//  	buffer.append(TPOS3Client.PN_PASSWORD).append("=").append(client.getProperty(TPOS3Client.PN_PASSWORD));
//  	buffer.append(TPOS3Client.PN_MERCHANTID).append("=").append(client.getProperty(TPOS3Client.PN_LOCATIONID));
//  	buffer.append(TPOS3Client.PN_LOCATIONID).append("=").append(client.getProperty(TPOS3Client.PN_LOCATIONID));
//  	buffer.append(TPOS3Client.PN_POSID).append("=").append(client.getProperty(TPOS3Client.PN_POSID));

  	//Encoding creditcardnumber here since I can not store it in the DB
  	buffer.append(TPOS3Client.PN_PAN).append("=").append(client.getProperty(TPOS3Client.PN_PAN)).append("&");
  	buffer.append(TPOS3Client.PN_EXPIRE).append("=").append(client.getProperty(TPOS3Client.PN_EXPIRE)).append("&");

  	buffer.append(TPOS3Client.PN_AMOUNT).append("=").append(client.getProperty(TPOS3Client.PN_AMOUNT)).append("&");
  	buffer.append(TPOS3Client.PN_CURRENCY).append("=").append(client.getProperty(TPOS3Client.PN_CURRENCY)).append("&");
  	buffer.append(TPOS3Client.PN_TRANSACTIONTYPE).append("=").append(client.getProperty(TPOS3Client.PN_TRANSACTIONTYPE)).append("&");
  	buffer.append(TPOS3Client.PN_CARDHOLDERCODE).append("=").append(client.getProperty(TPOS3Client.PN_CARDHOLDERCODE)).append("&");
  	
  	return buffer;
  }
  	

  /**
   * Description of the Method
   *
   * @param cardnumber         Description of the Parameter
   * @param monthExpires       Description of the Parameter
   * @param yearExpires        Description of the Parameter
   * @param amount             Description of the Parameter
   * @param currency           Description of the Parameter
   * @return                   Description of the Return Value
   * @exception TPosException  Description of the Exception
   */
  public String doSale(String nameOnCard, String cardnumber, String monthExpires, String yearExpires, String ccVerifyNumber, double amount, String currency, String referenceNumber) throws TPosException {
  	return (doAuth(cardnumber, monthExpires, yearExpires, amount, currency, "1", null, null));
  }
/*
  public String doSale(String nameOnCard, String cardnumber, String monthExpires, String yearExpires, double amount, String currency, String referenceNumber, String merchantId) throws TPosException {
    this._merchantId = merchantId;
    return doAuth(cardnumber, monthExpires, yearExpires, amount, currency, "1");
  }
*/
  /**
   * Description of the Method
   *
   * @param cardnumber         Description of the Parameter
   * @param monthExpires       Description of the Parameter
   * @param yearExpires        Description of the Parameter
   * @param amount             Description of the Parameter
   * @param currency           Description of the Parameter
   * @return                   Description of the Return Value
   * @exception TPosException  Description of the Exception
   */
  public String doRefund(String cardnumber, String monthExpires, String yearExpires, String ccVerifyNumber, double amount, String currency, Object parentDataPK, String captureProperties) throws TPosException {
  		System.out.println("Warning : TPosClient is NOT using CVC number");
  		return doAuth(cardnumber, monthExpires, yearExpires, amount, currency, "3", parentDataPK, null);
  }
  
  public void finishTransaction(String properties) throws CreditCardAuthorizationException {
  	HashMap map = parseProperties(properties);
  	
  	String cardnumber = (String) map.get(TPOS3Client.PN_PAN);
  	String expires = (String) map.get(TPOS3Client.PN_EXPIRE);
  	String amount = (String) map.get(TPOS3Client.PN_AMOUNT);
  	String currency = (String) map.get(TPOS3Client.PN_CURRENCY);
  	String monthExpires = expires.substring(0, 2);
  	String yearExpires = expires.substring(2, 4);
  	String authIDRsp = (String) map.get(TPOS3Client.PN_AUTHORIDENTIFYRSP);

    doAuth(cardnumber, monthExpires, yearExpires, Double.parseDouble(amount) / this.amountMultiplier, currency, "1", null, authIDRsp);
  }
  
	private HashMap parseProperties(String response) {
		HashMap responseElements = new HashMap();
		int index = 0;
		int tmpIndex = 0;
		String tmpString;
		String key, value;
		while (index >= 0) {
			tmpIndex = response.indexOf("&");
			tmpString = response.substring(0, tmpIndex);
			response = response.substring(tmpIndex + 1, response.length());
			index = response.indexOf("&");
			if (tmpString.indexOf("=") > -1) {
				key = tmpString.substring(0, tmpString.indexOf("="));
				value = tmpString.substring(tmpString.indexOf("=") + 1, tmpString.length());
//				System.out.println(tmpString+" ("+key+","+value+")");
				responseElements.put(key, value);
			}
		}
		if (response.indexOf("=") > -1) {
			key = response.substring(0, response.indexOf("="));
			value = response.substring(response.indexOf("=") + 1, response.length());
//			System.out.println(response + " (" + key + "," + value + ")");
			responseElements.put(key, value);
		}
		return responseElements;
	}
  
  /**
   * Gets the bundleIdentifier attribute of the TPosClient object
   *
   * @return   The bundleIdentifier value
   */
  public String getBundleIdentifier() {
    return (IW_BUNDLE_IDENTIFIER);
  }

  /**
   * Description of the Method
   *
   * @param cardnumber         Description of the Parameter
   * @param monthExpires       Description of the Parameter
   * @param yearExpires        Description of the Parameter
   * @param amount             Description of the Parameter
   * @param currency           Description of the Parameter
   * @param transactionType    Description of the Parameter
   * @param parentDataPK       Description of the Parameter
   * @param authIDRsp			     Description of the Parameter
   * @return                   Description of the Return Value
   * @exception TPosException  Description of the Exception
   */
  private String doAuth(String cardnumber, String monthExpires, String yearExpires, double amount, String currency, String transactionType, Object parentDataPK, String authIDRsp) throws TPosException {
  	if(this._client != null) {

      this._client.setProperty(TPOS3Client.PN_USERID, this._userId);
      this._client.setProperty(TPOS3Client.PN_PASSWORD, this._passwd);
      this._client.setProperty(TPOS3Client.PN_MERCHANTID, this._merchantId);
      this._client.setProperty(TPOS3Client.PN_LOCATIONID, this._locationId);
      this._client.setProperty(TPOS3Client.PN_POSID, this._posId);
      
    	String encodedCardNumber = CreditCardBusinessBean.encodeCreditCardNumber(cardnumber);
      this._client.setProperty(TPOS3Client.PN_PAN, cardnumber);
      this._client.setProperty(TPOS3Client.PN_EXPIRE, monthExpires + yearExpires);
      if (authIDRsp != null) {
      	this._client.setProperty(TPOS3Client.PN_AUTHORIDENTIFYRSP, authIDRsp);
      }
      //_client.setProperty(TPOS3Client.PN_EXPIRE, yearExpires + monthExpires);
      amount *= this.amountMultiplier;
      String stringAmount = Integer.toString((int)amount);
      this._client.setProperty(TPOS3Client.PN_AMOUNT, stringAmount);
      this._client.setProperty(TPOS3Client.PN_CURRENCY, currency);
      this._client.setProperty(TPOS3Client.PN_TRANSACTIONTYPE, transactionType);
      if (transactionType.equals("2")) {
        this._client.setProperty(TPOS3Client.PN_CARDHOLDERCODE, "2");
      }

      boolean valid = false;
      try {
      	valid = this._client.sendAuthorisationReq();
      } catch (IllegalArgumentException e) {
      	getKeys();
      	createNewBatch();
        this._client.setProperty(TPOS3Client.PN_USERID, this._userId);
        this._client.setProperty(TPOS3Client.PN_PASSWORD, this._passwd);
        this._client.setProperty(TPOS3Client.PN_MERCHANTID, this._merchantId);
        this._client.setProperty(TPOS3Client.PN_LOCATIONID, this._locationId);
        this._client.setProperty(TPOS3Client.PN_POSID, this._posId);

        if (authIDRsp == null) {
  	      this._client.setProperty(TPOS3Client.PN_PAN, cardnumber);
  	      this._client.setProperty(TPOS3Client.PN_EXPIRE, monthExpires + yearExpires);
        } else {
        	// Setting authID, _client should by created already
        	this._client.setProperty(TPOS3Client.PN_AUTHORIDENTIFYRSP, authIDRsp);
        }

        //_client.setProperty(TPOS3Client.PN_EXPIRE, yearExpires + monthExpires);
        this._client.setProperty(TPOS3Client.PN_AMOUNT, stringAmount);
        this._client.setProperty(TPOS3Client.PN_CURRENCY, currency);
        this._client.setProperty(TPOS3Client.PN_TRANSACTIONTYPE, transactionType);
        if (transactionType.equals("2")) {
          this._client.setProperty(TPOS3Client.PN_CARDHOLDERCODE, "2");
        }
      	valid = this._client.sendAuthorisationReq();
      }
      boolean inserted = false;
      
      TPosAuthorisationEntriesBean entry;
      try {
    	  TPosAuthorisationEntriesBeanHome home = (TPosAuthorisationEntriesBeanHome) IDOLookup.getHome(TPosAuthorisationEntriesBean.class);
    	  entry = home.create();
//  	    	entry = TPosAuthorisationEntriesHome.getInstance().getNewElement();
  	//    entry.setAttachmentCount(_client.getProperty(TPOS3Client.pn));
  	    entry.setAuthorisationAmount(this._client.getProperty(TPOS3Client.PN_AUTHORAMOUNT));
  	    entry.setAuthorisationCode(this._client.getProperty(TPOS3Client.PN_AUTHORISATIONCODE));
  	    entry.setAuthorisationCurrency(this._client.getProperty(TPOS3Client.PN_AUTHORCURRENCY));
  	    entry.setAuthorisationIdRsp(this._client.getProperty(TPOS3Client.PN_AUTHORIDENTIFYRSP));
  	    entry.setAuthorisationPathReasonCode(this._client.getProperty(TPOS3Client.PN_AUTHPATHREASONCODE));
  	    entry.setBatchNumber(this._client.getProperty(TPOS3Client.PN_BATCHNUMBER));
  	    entry.setBrandId(this._client.getProperty(TPOS3Client.PN_CARDBRANDID));
  	    entry.setBrandName(this._client.getProperty(TPOS3Client.PN_CARDBRANDNAME));
  	    entry.setCardCharacteristics(this._client.getProperty(TPOS3Client.PN_CARDCHARACTER));
  	    entry.setCardExpires(this._client.getProperty(TPOS3Client.PN_EXPIRE));
  	    entry.setCardName(this._client.getProperty(TPOS3Client.PN_CARDTYPENAME));
  	    entry.setCardType(this._client.getProperty(TPOS3Client.PN_CARDTYPEID));
  	    entry.setDetailExpected(this._client.getProperty(TPOS3Client.PN_DETAILEXPECTED));
  	    entry.setEntryDate(this._client.getProperty(TPOS3Client.PN_DATE));
  	    entry.setEntryTime(this._client.getProperty(TPOS3Client.PN_TIME));
  	    entry.setErrorNo(this._client.getProperty(TPOS3Client.PN_ERRORNUMBER));
  	    entry.setErrorText(this._client.getProperty(TPOS3Client.PN_ERRORTEXT));
  	    entry.setLocationNr(this._client.getProperty(TPOS3Client.PN_LOCATIONNUMBER));
  	    entry.setMerchantNrAuthorisation(this._client.getProperty(TPOS3Client.PN_MERCHANTNUMBERAUTHOR));
  	    entry.setMerchantNrOtherServices(this._client.getProperty(TPOS3Client.PN_MERCHANTNUMBEROTHERSERVICES));
  	    entry.setMerchantNrSubmission(this._client.getProperty(TPOS3Client.PN_MERCHANTNUMBERSUBMISSION));
  	//    entry.setPan("***********");
  	    entry.setPosNr(this._client.getProperty(TPOS3Client.PN_POSNUMBER));
  	    entry.setPosSerialNr(this._client.getProperty(TPOS3Client.PN_POSSERIAL));
  	//    entry.setPrintData(_client.getProperty(TPOS3Client.pn_p));
  	    entry.setSubmissionAmount(this._client.getProperty(TPOS3Client.PN_SUBMISSIONAMOUNT));
  	    entry.setSubmissionCurrency(this._client.getProperty(TPOS3Client.PN_SUBMISSIONCURRENCY));
  	    entry.setTotalResponseCode(this._client.getProperty(TPOS3Client.PN_TOTALRESPONSECODE));
  	    entry.setTransactionNr(this._client.getProperty(TPOS3Client.PN_TRANSACTIONNUMBER));
  	    entry.setVoidedAuthorisationIdResponse(this._client.getProperty(TPOS3Client.PN_VOIDEDAUTHIDRSP));
  	    entry.setVoidedTransactionNr(this._client.getProperty(TPOS3Client.PN_VOIDEDTRANSNUMBER));
  	//    entry.setXMLAttachment(_client.getProperty(TPOS3Client.));
  	    entry.setCardNumber(encodedCardNumber);
  	    if (parentDataPK != null) {
  	    		try {
  	    			entry.setParentID(((Integer) parentDataPK).intValue());
  	    		} catch (Exception e) {
  	    			System.out.println("TPosClient : could not set parentID : "+parentDataPK);
  	    		}
  	    }
  	    entry.store();
//  	    inserted = TPosAuthorisationEntriesHome.getInstance().insert(entry);

//      	String tmpTest;
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_AUTHORAMOUNT);
//  	    System.out.println("PN_AUTHORAMOUNT : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_AUTHORCURRENCY);
//  	    System.out.println("PN_AUTHORCURRENCY : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_AUTHORIDENTIFYRSP);
//  	    System.out.println("PN_AUTHORIDENTIFYRSP : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_AUTHPATHREASONCODE);
//  	    System.out.println("PN_AUTHPATHREASONCODE : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_BATCHNUMBER);
//  	    System.out.println("PN_BATCHNUMBER : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_CARDBRANDID);
//  	    System.out.println("PN_CARDBRANDNAME : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_CARDBRANDNAME);
//  	    System.out.println("PN_CARDBRANDNAME : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_CARDCHARACTER);
//  	    System.out.println("PN_CARDCHARACTER : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_EXPIRE);
//  	    System.out.println("PN_EXPIRE : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_CARDTYPENAME);
//  	    System.out.println("PN_CARDTYPENAME : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_CARDTYPEID);
//  	    System.out.println("PN_CARDTYPEID : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_DETAILEXPECTED);
//  	    System.out.println("PN_DETAILEXPECTED : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_DATE);
//  	    System.out.println("PN_DATE : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_TIME);
//  	    System.out.println("PN_TIME : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_ERRORNUMBER);
//  	    System.out.println("PN_ERRORNUMBER : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_ERRORTEXT);
//  	    System.out.println("PN_ERRORTEXT : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_LOCATIONNUMBER);
//  	    System.out.println("PN_LOCATIONNUMBER : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_MERCHANTNUMBERAUTHOR);
//  	    System.out.println("PN_MERCHANTNUMBEROTHERSERVICES : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_MERCHANTNUMBEROTHERSERVICES);
//  	    System.out.println("PN_MERCHANTNUMBEROTHERSERVICES : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_MERCHANTNUMBERSUBMISSION);
//  	    System.out.println("PN_MERCHANTNUMBERSUBMISSION : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_POSNUMBER);
//  	    System.out.println("PN_POSNUMBER : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_POSSERIAL);
//  	    System.out.println("PN_POSSERIAL : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_SUBMISSIONAMOUNT);
//  	    System.out.println("PN_SUBMISSIONAMOUNT : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_SUBMISSIONCURRENCY);
//  	    System.out.println("PN_SUBMISSIONCURRENCY : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_TOTALRESPONSECODE);
//  	    System.out.println("PN_TOTALRESPONSECODE : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_TRANSACTIONNUMBER);
//  	    System.out.println("PN_TRANSACTIONNUMBER : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_VOIDEDAUTHIDRSP);
//  	    System.out.println("PN_VOIDEDAUTHIDRSP : "+tmpTest.length());
//  	    tmpTest = _client.getProperty(TPOS3Client.PN_VOIDEDTRANSNUMBER);
//  	    System.out.println("PN_VOIDEDTRANSNUMBER : "+tmpTest.length());
  	    inserted = true;
      } catch (Exception e) {
    	  inserted = false;
      		e.printStackTrace();
      }

      if (!inserted) {
        System.err.println("Unable to save entry to database");
      }

      if (!valid) {
        TPosException e = new TPosException("Error in authorisation");
        e.setErrorNumber(this._client.getProperty(TPOS3Client.PN_ERRORNUMBER));
        e.setErrorMessage(this._client.getProperty(TPOS3Client.PN_ERRORTEXT));
        e.setDisplayError("Error in authorisation (" + this._client.getProperty(TPOS3Client.PN_ERRORNUMBER) + ")");

        throw e;
      }

      TPosException tposEx = null;

      switch (Integer.parseInt(this._client.getProperty(TPOS3Client.PN_TOTALRESPONSECODE), 10)) {
          case 0:
            return (this._client.getProperty(TPOS3Client.PN_AUTHORIDENTIFYRSP));
          case 1:
            tposEx = new TPosException("Authorisation denied");
            tposEx.setErrorNumber(this._client.getProperty(TPOS3Client.PN_ERRORNUMBER));
            tposEx.setErrorMessage(this._client.getProperty(TPOS3Client.PN_ERRORTEXT));
            tposEx.setDisplayError("Authorisation denied (" + this._client.getProperty(TPOS3Client.PN_ERRORNUMBER) + ")");
            throw tposEx;
          case 2:
            tposEx = new TPosException("Authorisation denied, pick up card");
            tposEx.setErrorNumber(this._client.getProperty(TPOS3Client.PN_ERRORNUMBER));
            tposEx.setErrorMessage(this._client.getProperty(TPOS3Client.PN_ERRORTEXT));
            tposEx.setDisplayError("Authorisation denied (" + this._client.getProperty(TPOS3Client.PN_ERRORNUMBER) + ")");
            throw tposEx;
          case 3:
            tposEx = new TPosException("Authorisation denied, call for manual authorisation");
            tposEx.setErrorNumber(this._client.getProperty(TPOS3Client.PN_ERRORNUMBER));
            tposEx.setErrorMessage(this._client.getProperty(TPOS3Client.PN_ERRORTEXT));
            tposEx.setDisplayError("Authorisation denied (" + this._client.getProperty(TPOS3Client.PN_ERRORNUMBER) + ")");
            throw tposEx;
      }

      
    
  	}
  	return ("-1");
  	}

  /**
   * Gets the amount attribute of the TPosClient object
   *
   * @return   The amount value
   */
  private String getAmount() {
    return Integer.toString(Integer.parseInt(this._client.getProperty(TPOS3Client.PN_AMOUNT)) / this.amountMultiplier);
  }

  /**
   * Gets the cardBrandName attribute of the TPosClient object
   *
   * @return   The cardBrandName value
   */
  private String getCardBrandName() {
    return this._client.getProperty(TPOS3Client.PN_CARDBRANDNAME);
  }

  /**
   * Gets the cardCharacter attribute of the TPosClient object
   *
   * @return   The cardCharacter value
   */
  private String getCardCharacter() {
    return this._client.getProperty(TPOS3Client.PN_CARDCHARACTER);
  }

  /**
   * Gets the authorisationCode attribute of the TPosClient object
   *
   * @return   The authorisationCode value
   */
  private String getAuthorisationCode() {
    return this._client.getProperty(TPOS3Client.PN_AUTHORISATIONCODE);
  }

  /**
   * Gets the autorIdentifyRSP attribute of the TPosClient object
   *
   * @return   The autorIdentifyRSP value
   */
  private String getAutorIdentifyRSP() {
    return this._client.getProperty(TPOS3Client.PN_AUTHORIDENTIFYRSP);
  }

  /**
   * Gets the cardTypeName attribute of the TPosClient object
   *
   * @return   The cardTypeName value
   */
  private String getCardTypeName() {
    return this._client.getProperty(TPOS3Client.PN_CARDTYPENAME);
  }

  /**
   * Gets the currency attribute of the TPosClient object
   *
   * @return   The currency value
   */
  private String getCurrency() {
    return this._client.getProperty(TPOS3Client.PN_CURRENCY);
  }

  /**
   * Gets the date attribute of the TPosClient object
   *
   * @return   The date value
   */
  private String getDate() {
    return this._client.getProperty(TPOS3Client.PN_DATE);
  }

  /**
   * Gets the time attribute of the TPosClient object
   *
   * @return   The time value
   */
  private String getTime() {
    return this._client.getProperty(TPOS3Client.PN_TIME);
  }

  /**
   * Gets the expire attribute of the TPosClient object
   *
   * @return   The expire value
   */
  private String getExpire() {
    return this._client.getProperty(TPOS3Client.PN_EXPIRE);
  }

  /**
   * Gets the pan attribute of the TPosClient object
   *
   * @return   The pan value
   */
  private String getPan() {
    return this._client.getProperty(TPOS3Client.PN_PAN);
  }

  /**
   * Gets the cCNumber attribute of the TPosClient object
   *
   * @return   The cCNumber value
   */
  private String getCCNumber() {
    return getPan();
  }

  /**
   * Created from getDate() and getTime()
   *
   * @return   The IWTimestamp value
   */
  private IWTimestamp getIdegaTimestamp() {
    IWTimestamp stamp = new IWTimestamp();
    try {
      String date = getDate();
      String time = getTime();

      stamp.setYear(Integer.parseInt(date.substring(0, 4)));
      stamp.setMonth(Integer.parseInt(date.substring(4, 6)));
      stamp.setDay(Integer.parseInt(date.substring(6, 8)));
      stamp.setHour(Integer.parseInt(time.substring(0, 2)));
      stamp.setMinute(Integer.parseInt(time.substring(2, 4)));
      stamp.setSecond(Integer.parseInt(time.substring(4, 6)));
    }
    catch (Exception e) {
      stamp = IWTimestamp.RightNow();
    }

    return stamp;
  }

  private CreditCardBusiness getCreditCardBusiness(IWApplicationContext iwac) {
		try {
		return (CreditCardBusiness) IBOLookup.getServiceInstance(iwac, CreditCardBusiness.class);
	} catch (IBOLookupException e) {
		throw  new IBORuntimeException(e);
	}

  }

	public Collection getValidCardTypes() {
		Vector tmp = new Vector();
		tmp.add(CreditCardBusiness.CARD_TYPE_VISA);
		tmp.add(CreditCardBusiness.CARD_TYPE_MASTERCARD);
		tmp.add(CreditCardBusiness.CARD_TYPE_AMERICAN_EXPRESS);
		return tmp;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.creditcard.business.CreditCardClient#getCreditCardMerchant()
	 */
	public CreditCardMerchant getCreditCardMerchant() {
		return this._merchant;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.creditcard.business.CreditCardClient#supportsDelayedTransactions()
	 */
	public boolean supportsDelayedTransactions() {
		return true;
	} 
}
