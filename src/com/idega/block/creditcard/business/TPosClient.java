/*
 *  $Id: TPosClient.java,v 1.4 2004/05/27 09:15:25 gimmi Exp $
 *
 *  Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 *
 */
package com.idega.block.creditcard.business;

import java.util.Collection;
import java.util.Vector;

import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.data.TPosAuthorisationEntriesBean;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
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
  		_iwc = iwc;
    _iwb = iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
    //if (_iwb != null) {
    //  _iwrb = _iwb.getResourceBundle(iwc);
    //}

    String path = _iwb.getPropertiesRealPath();
    if (path == null) {
      throw new Exception("Unable to find properties file");
    }

    String seperator = java.io.File.separator;
    if (!path.endsWith(seperator)) {
      path = path + seperator + _iwb.getProperty("properties_file");
    }
    else {
      path = path + _iwb.getProperty("properties_file");
    }

    try {
      _client = new TPOS3Client(path);
      String ipset = _iwb.getProperty(TPOS_IP_SET);
      _client.setIPSet(Integer.parseInt(ipset));


      if (_merchant == null) {
      	System.out.println("TPosClient : Using default TPosMerchant");
        _userId = _iwb.getProperty(TPOS_USER_ID);
        _passwd = _iwb.getProperty(TPOS_PASSWD);
        _merchantId = _iwb.getProperty(TPOS_MERCHANT_ID);
        _locationId = _iwb.getProperty(TPOS_LOCATION_ID);
        _posId = _iwb.getProperty(TPOS_POS_ID);
        _receivePasswd = _iwb.getProperty(TPOS_KEY_RECEIVE_PASSWD);
      }else {
      	System.out.println("TPosClient : Using TPosMerchant "+_merchant.getName());
        _userId = _merchant.getUser();
        _passwd = _merchant.getPassword();
        _merchantId = _merchant.getMerchantID();
        _locationId = _merchant.getLocation();
        _posId = _merchant.getTerminalID();
        _receivePasswd = _merchant.getExtraInfo();
      }
    }


    catch (Exception e) {
      System.out.println("Got an exception trying to create client");
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
    _client.setProperty(TPOS3Client.PN_USERID, _userId);
    _client.setProperty(TPOS3Client.PN_PASSWORD, _passwd);
    _client.setProperty(TPOS3Client.PN_MERCHANTID, _merchantId);
    _client.setProperty(TPOS3Client.PN_LOCATIONID, _locationId);
    _client.setProperty(TPOS3Client.PN_POSID, _posId);

    boolean created = _client.sendNewBatchReq();
    if (!created) {
      System.err.println("Error no: " + _client.getProperty(TPOS3Client.PN_ERRORNUMBER));
      System.err.println("Error string : " + _client.getProperty(TPOS3Client.PN_ERRORTEXT));

      return (null);
    }

    String newBatchNumber = _client.getProperty(TPOS3Client.PN_OPENEDBATCHNR);

    return (newBatchNumber);
  }

  /**
   * Gets the cACertificate attribute of the TPosClient object
   *
   * @return   The cACertificate value
   */
  public boolean getCACertificate() {
    _client.setProperty(TPOS3Client.PN_MERCHANTID, _merchantId);
    _client.setProperty(TPOS3Client.PN_LOCATIONID, _locationId);
    _client.setProperty(TPOS3Client.PN_POSID, _posId);

    boolean valid = _client.sendCACertificateReq();

    if (!valid) {
      System.err.println("Error no: " + _client.getProperty(TPOS3Client.PN_ERRORNUMBER));
      System.err.println("Error string : " + _client.getProperty(TPOS3Client.PN_ERRORTEXT));
    }

    if (Integer.parseInt(_client.getProperty(TPOS3Client.PN_TOTALRESPONSECODE), 10) == 0) {
      _client.confirmCACertificate();
    }
    else {
      System.err.println("Error no: " + _client.getProperty(TPOS3Client.PN_ERRORNUMBER));
      System.err.println("Error string : " + _client.getProperty(TPOS3Client.PN_ERRORTEXT));

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
    _client.setProperty(TPOS3Client.PN_MERCHANTID, _merchantId);
    _client.setProperty(TPOS3Client.PN_LOCATIONID, _locationId);
    _client.setProperty(TPOS3Client.PN_POSID, _posId);

    _client.setProperty(TPOS3Client.PN_KEYRECEIVEPASSWORD, _receivePasswd);

    boolean valid = _client.sendKeyPairReq();

    if (!valid) {
      System.err.println("Error no: " + _client.getProperty(TPOS3Client.PN_ERRORNUMBER));
      System.err.println("Error string : " + _client.getProperty(TPOS3Client.PN_ERRORTEXT));
    }

    return (valid);
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
  	return (doAuth(cardnumber, monthExpires, yearExpires, amount, currency, "1", null));
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
  		return doAuth(cardnumber, monthExpires, yearExpires, amount, currency, "3", parentDataPK);
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
   * @return                   Description of the Return Value
   * @exception TPosException  Description of the Exception
   */
  private String doAuth(String cardnumber, String monthExpires, String yearExpires, double amount, String currency, String transactionType, Object parentDataPK) throws TPosException {
    _client.setProperty(TPOS3Client.PN_USERID, _userId);
    _client.setProperty(TPOS3Client.PN_PASSWORD, _passwd);
    _client.setProperty(TPOS3Client.PN_MERCHANTID, _merchantId);
    _client.setProperty(TPOS3Client.PN_LOCATIONID, _locationId);
    _client.setProperty(TPOS3Client.PN_POSID, _posId);

    _client.setProperty(TPOS3Client.PN_PAN, cardnumber);
    _client.setProperty(TPOS3Client.PN_EXPIRE, monthExpires + yearExpires);
    //_client.setProperty(TPOS3Client.PN_EXPIRE, yearExpires + monthExpires);
    amount *= amountMultiplier;
    String stringAmount = Integer.toString((int)amount);
    _client.setProperty(TPOS3Client.PN_AMOUNT, stringAmount);
    _client.setProperty(TPOS3Client.PN_CURRENCY, currency);
    _client.setProperty(TPOS3Client.PN_TRANSACTIONTYPE, transactionType);
    if (transactionType.equals("2")) {
      _client.setProperty(TPOS3Client.PN_CARDHOLDERCODE, "2");
    }

    boolean valid = _client.sendAuthorisationReq();
    boolean inserted = false;
    
    TPosAuthorisationEntriesBean entry;
    try {
	    	entry = TPosAuthorisationEntriesHome.getInstance().getNewElement();
	//    entry.setAttachmentCount(_client.getProperty(TPOS3Client.pn));
	    entry.setAuthorisationAmount(_client.getProperty(TPOS3Client.PN_AUTHORAMOUNT));
	    entry.setAuthorisationCode(_client.getProperty(TPOS3Client.PN_AUTHORISATIONCODE));
	    entry.setAuthorisationCurrency(_client.getProperty(TPOS3Client.PN_AUTHORCURRENCY));
	    entry.setAuthorisationIdRsp(_client.getProperty(TPOS3Client.PN_AUTHORIDENTIFYRSP));
	    entry.setAuthorisationPathReasonCode(_client.getProperty(TPOS3Client.PN_AUTHPATHREASONCODE));
	    entry.setBatchNumber(_client.getProperty(TPOS3Client.PN_BATCHNUMBER));
	    entry.setBrandId(_client.getProperty(TPOS3Client.PN_CARDBRANDID));
	    entry.setBrandName(_client.getProperty(TPOS3Client.PN_CARDBRANDNAME));
	    entry.setCardCharacteristics(_client.getProperty(TPOS3Client.PN_CARDCHARACTER));
	    entry.setCardExpires(_client.getProperty(TPOS3Client.PN_EXPIRE));
	    entry.setCardName(_client.getProperty(TPOS3Client.PN_CARDTYPENAME));
	    entry.setCardType(_client.getProperty(TPOS3Client.PN_CARDTYPEID));
	    entry.setDetailExpected(_client.getProperty(TPOS3Client.PN_DETAILEXPECTED));
	    entry.setEntryDate(_client.getProperty(TPOS3Client.PN_DATE));
	    entry.setEntryTime(_client.getProperty(TPOS3Client.PN_TIME));
	    entry.setErrorNo(_client.getProperty(TPOS3Client.PN_ERRORNUMBER));
	    entry.setErrorText(_client.getProperty(TPOS3Client.PN_ERRORTEXT));
	    entry.setLocationNr(_client.getProperty(TPOS3Client.PN_LOCATIONNUMBER));
	    entry.setMerchantNrAuthorisation(_client.getProperty(TPOS3Client.PN_MERCHANTNUMBERAUTHOR));
	    entry.setMerchantNrOtherServices(_client.getProperty(TPOS3Client.PN_MERCHANTNUMBEROTHERSERVICES));
	    entry.setMerchantNrSubmission(_client.getProperty(TPOS3Client.PN_MERCHANTNUMBERSUBMISSION));
	//    entry.setPan("***********");
	    entry.setPosNr(_client.getProperty(TPOS3Client.PN_POSNUMBER));
	    entry.setPosSerialNr(_client.getProperty(TPOS3Client.PN_POSSERIAL));
	//    entry.setPrintData(_client.getProperty(TPOS3Client.pn_p));
	    entry.setSubmissionAmount(_client.getProperty(TPOS3Client.PN_SUBMISSIONAMOUNT));
	    entry.setSubmissionCurrency(_client.getProperty(TPOS3Client.PN_SUBMISSIONCURRENCY));
	    entry.setTotalResponseCode(_client.getProperty(TPOS3Client.PN_TOTALRESPONSECODE));
	    entry.setTransactionNr(_client.getProperty(TPOS3Client.PN_TRANSACTIONNUMBER));
	    entry.setVoidedAuthorisationIdResponse(_client.getProperty(TPOS3Client.PN_VOIDEDAUTHIDRSP));
	    entry.setVoidedTransactionNr(_client.getProperty(TPOS3Client.PN_VOIDEDTRANSNUMBER));
	//    entry.setXMLAttachment(_client.getProperty(TPOS3Client.));
	    entry.setCardNumber(CreditCardBusinessBean.encodeCreditCardNumber(cardnumber));
	    if (parentDataPK != null) {
	    		try {
	    			entry.setParentID(((Integer) parentDataPK).intValue());
	    		} catch (Exception e) {
	    			System.out.println("TPosClient : could not set parentID : "+parentDataPK);
	    		}
	    }
	
	    inserted = TPosAuthorisationEntriesHome.getInstance().insert(entry);
	    
    } catch (Exception e) {
    		e.printStackTrace();
    }

    if (!inserted) {
      System.err.println("Unable to save entry to database");
    }

    if (!valid) {
      TPosException e = new TPosException("Error in authorisation");
      e.setErrorNumber(_client.getProperty(TPOS3Client.PN_ERRORNUMBER));
      e.setErrorMessage(_client.getProperty(TPOS3Client.PN_ERRORTEXT));
      e.setDisplayError("Error in authorisation (" + _client.getProperty(TPOS3Client.PN_ERRORNUMBER) + ")");

      throw e;
    }

    TPosException tposEx = null;

    switch (Integer.parseInt(_client.getProperty(TPOS3Client.PN_TOTALRESPONSECODE), 10)) {
        case 0:
          return (_client.getProperty(TPOS3Client.PN_AUTHORIDENTIFYRSP));
        case 1:
          tposEx = new TPosException("Authorisation denied");
          tposEx.setErrorNumber(_client.getProperty(TPOS3Client.PN_ERRORNUMBER));
          tposEx.setErrorMessage(_client.getProperty(TPOS3Client.PN_ERRORTEXT));
          tposEx.setDisplayError("Authorisation denied (" + _client.getProperty(TPOS3Client.PN_ERRORNUMBER) + ")");
          throw tposEx;
        case 2:
          tposEx = new TPosException("Authorisation denied, pick up card");
          tposEx.setErrorNumber(_client.getProperty(TPOS3Client.PN_ERRORNUMBER));
          tposEx.setErrorMessage(_client.getProperty(TPOS3Client.PN_ERRORTEXT));
          tposEx.setDisplayError("Authorisation denied (" + _client.getProperty(TPOS3Client.PN_ERRORNUMBER) + ")");
          throw tposEx;
        case 3:
          tposEx = new TPosException("Authorisation denied, call for manual authorisation");
          tposEx.setErrorNumber(_client.getProperty(TPOS3Client.PN_ERRORNUMBER));
          tposEx.setErrorMessage(_client.getProperty(TPOS3Client.PN_ERRORTEXT));
          tposEx.setDisplayError("Authorisation denied (" + _client.getProperty(TPOS3Client.PN_ERRORNUMBER) + ")");
          throw tposEx;
    }

    return ("-1");
  }

  /**
   * Gets the amount attribute of the TPosClient object
   *
   * @return   The amount value
   */
  public String getAmount() {
    return Integer.toString(Integer.parseInt(_client.getProperty(TPOS3Client.PN_AMOUNT)) / amountMultiplier);
  }

  /**
   * Gets the cardBrandName attribute of the TPosClient object
   *
   * @return   The cardBrandName value
   */
  public String getCardBrandName() {
    return _client.getProperty(TPOS3Client.PN_CARDBRANDNAME);
  }

  /**
   * Gets the cardCharacter attribute of the TPosClient object
   *
   * @return   The cardCharacter value
   */
  public String getCardCharacter() {
    return _client.getProperty(TPOS3Client.PN_CARDCHARACTER);
  }

  /**
   * Gets the authorisationCode attribute of the TPosClient object
   *
   * @return   The authorisationCode value
   */
  public String getAuthorisationCode() {
    return _client.getProperty(TPOS3Client.PN_AUTHORISATIONCODE);
  }

  /**
   * Gets the autorIdentifyRSP attribute of the TPosClient object
   *
   * @return   The autorIdentifyRSP value
   */
  public String getAutorIdentifyRSP() {
    return _client.getProperty(TPOS3Client.PN_AUTHORIDENTIFYRSP);
  }

  /**
   * Gets the cardTypeName attribute of the TPosClient object
   *
   * @return   The cardTypeName value
   */
  public String getCardTypeName() {
    return _client.getProperty(TPOS3Client.PN_CARDTYPENAME);
  }

  /**
   * Gets the currency attribute of the TPosClient object
   *
   * @return   The currency value
   */
  public String getCurrency() {
    return _client.getProperty(TPOS3Client.PN_CURRENCY);
  }

  /**
   * Gets the date attribute of the TPosClient object
   *
   * @return   The date value
   */
  protected String getDate() {
    return _client.getProperty(TPOS3Client.PN_DATE);
  }

  /**
   * Gets the time attribute of the TPosClient object
   *
   * @return   The time value
   */
  protected String getTime() {
    return _client.getProperty(TPOS3Client.PN_TIME);
  }

  /**
   * Gets the expire attribute of the TPosClient object
   *
   * @return   The expire value
   */
  public String getExpire() {
    return _client.getProperty(TPOS3Client.PN_EXPIRE);
  }

  /**
   * Gets the pan attribute of the TPosClient object
   *
   * @return   The pan value
   */
  public String getPan() {
    return _client.getProperty(TPOS3Client.PN_PAN);
  }

  /**
   * Gets the cCNumber attribute of the TPosClient object
   *
   * @return   The cCNumber value
   */
  public String getCCNumber() {
    return getPan();
  }

  /**
   * Created from getDate() and getTime()
   *
   * @return   The IWTimestamp value
   */
  public IWTimestamp getIdegaTimestamp() {
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

  protected CreditCardBusiness getCreditCardBusiness(IWApplicationContext iwac) {
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
		return _merchant;
	} 
}
