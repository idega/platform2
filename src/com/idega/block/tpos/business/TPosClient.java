package com.idega.block.tpos.business;

import com.tpos.client.TPOS3Client;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.block.tpos.business.TPosException;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class TPosClient {
  public static final String TPOS_USER_ID = "tpos_userid";
  public static final String TPOS_PASSWD = "tpos_passwd";
  public static final String TPOS_MERCHANT_ID = "tpos_merchantid";
  public static final String TPOS_LOCATION_ID = "tpos_locationid";
  public static final String TPOS_POS_ID = "tpos_posid";
  public static final String TPOS_KEY_RECEIVE_PASSWD = "tpos_keyreceivepasswd";
  private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.tpos";
  private TPOS3Client _client = null;
  private IWBundle _iwb = null;
  private IWResourceBundle _iwrb = null;

  private String _userId = null;
  private String _passwd = null;
  private String _merchantId = null;
  private String _locationId = null;
  private String _posId = null;
  private String _receivePasswd = null;

  /**
   *
   */
  public TPosClient(IWContext iwc) throws Exception {
    _iwb = iwc.getApplication().getBundle(IW_BUNDLE_IDENTIFIER);
    if (_iwb != null)
      _iwrb = _iwb.getResourceBundle(iwc);

    String path = _iwb.getPropertiesRealPath();
    if (path == null)
      throw new Exception("Unable to find properties file");

    String seperator = java.io.File.separator;
    if (!path.endsWith(seperator))
      path = path + seperator + _iwb.getProperty("properties_file");
    else
      path = path + _iwb.getProperty("properties_file");

    try {
      _client = new TPOS3Client(path);
      _client.setIPSet(2);
      _userId = _iwb.getProperty(TPOS_USER_ID);
      _passwd = _iwb.getProperty(TPOS_PASSWD);
      _merchantId = _iwb.getProperty(TPOS_MERCHANT_ID);
      _locationId = _iwb.getProperty(TPOS_LOCATION_ID);
      _posId = _iwb.getProperty(TPOS_POS_ID);
      _receivePasswd = _iwb.getProperty(TPOS_KEY_RECEIVE_PASSWD);
    }
    catch(Exception e) {
      System.out.println("Got an exception trying to create client");
    }
  }

  public String createNewBatch() {
    _client.setProperty(TPOS3Client.PN_USERID,_userId);
    _client.setProperty(TPOS3Client.PN_PASSWORD,_passwd);
    _client.setProperty(TPOS3Client.PN_MERCHANTID,_merchantId);
    _client.setProperty(TPOS3Client.PN_LOCATIONID,_locationId);
    _client.setProperty(TPOS3Client.PN_POSID,_posId);

    boolean created = _client.sendNewBatchReq();
    if (!created) {
      System.err.println("Error no: " + _client.getProperty(TPOS3Client.PN_ERRORNUMBER));
      System.err.println("Error string : " + _client.getProperty(TPOS3Client.PN_ERRORTEXT));

      return(null);
    }

    String newBatchNumber = _client.getProperty(TPOS3Client.PN_OPENEDBATCHNR);
    String oldBatchNumber = _client.getProperty(TPOS3Client.PN_CLOSINGBATCHNR);

    return(newBatchNumber);
  }

  public boolean getCACertifycate() {
    _client.setProperty(TPOS3Client.PN_MERCHANTID,_merchantId);
    _client.setProperty(TPOS3Client.PN_LOCATIONID,_locationId);
    _client.setProperty(TPOS3Client.PN_POSID,_posId);

    boolean valid = _client.sendCACertificateReq();

    if (!valid) {
      System.err.println("Error no: " + _client.getProperty(TPOS3Client.PN_ERRORNUMBER));
      System.err.println("Error string : " + _client.getProperty(TPOS3Client.PN_ERRORTEXT));
    }

    return(valid);
  }

  public boolean getKeys() {
    _client.setProperty(TPOS3Client.PN_MERCHANTID,_merchantId);
    _client.setProperty(TPOS3Client.PN_LOCATIONID,_locationId);
    _client.setProperty(TPOS3Client.PN_POSID,_posId);

    _client.setProperty(TPOS3Client.PN_KEYRECEIVEPASSWORD,_receivePasswd);

    boolean valid = _client.sendKeyPairReq();

    if (!valid) {
      System.err.println("Error no: " + _client.getProperty(TPOS3Client.PN_ERRORNUMBER));
      System.err.println("Error string : " + _client.getProperty(TPOS3Client.PN_ERRORTEXT));
    }

    return(valid);
  }

  /**
   * @return The authorisation code as a String
   */
  public String doSale(String cardnumber, String monthExpires, String yearExpires, double amount, String currency) throws TPosException {
    return(doAuth(cardnumber,monthExpires,yearExpires,amount,currency,"1"));
  }

  /**
   * @return The authorisation code as a String
   */
  public String doRefund(String cardnumber, String monthExpires, String yearExpires, double amount, String currency) throws TPosException {
    return(doAuth(cardnumber,monthExpires,yearExpires,amount,currency,"3"));
  }

  /**
   *
   */
  public String getBundleIdentifier(){
    return(IW_BUNDLE_IDENTIFIER);
  }

  /**
   *
   */
  private String doAuth(String cardnumber, String monthExpires, String yearExpires, double amount, String currency, String transactionType) throws TPosException {
/*    _client.setProperty(TPOS3Client.PN_USERID, "IDE");
    _client.setProperty(TPOS3Client.PN_PASSWORD, "IDE");
    _client.setProperty(TPOS3Client.PN_MERCHANTID, "IDE");
    _client.setProperty(TPOS3Client.PN_LOCATIONID, "0000000001");
    _client.setProperty(TPOS3Client.PN_POSID, "IDE001001");*/

    _client.setProperty(TPOS3Client.PN_USERID,_userId);
    _client.setProperty(TPOS3Client.PN_PASSWORD,_passwd);
    _client.setProperty(TPOS3Client.PN_MERCHANTID,_merchantId);
    _client.setProperty(TPOS3Client.PN_LOCATIONID,_locationId);
    _client.setProperty(TPOS3Client.PN_POSID,_posId);

    _client.setProperty(TPOS3Client.PN_PAN,cardnumber);
    _client.setProperty(TPOS3Client.PN_EXPIRE, yearExpires+monthExpires);
    amount *= 100;
    String stringAmount = Integer.toString((int)amount);
    _client.setProperty(TPOS3Client.PN_AMOUNT,stringAmount);
    _client.setProperty(TPOS3Client.PN_CURRENCY,currency);
    _client.setProperty(TPOS3Client.PN_TRANSACTIONTYPE,transactionType);
    _client.setProperty(TPOS3Client.PN_CARDHOLDERCODE,"2");

    TPosException ex = new TPosException();
    TPosException ex2 = new TPosException("test");

    boolean valid = _client.sendAuthorisationReq();
    if (!valid) {
      TPosException e = new TPosException("Error in authorisation");
      e.setErrorNumber(_client.getProperty(TPOS3Client.PN_ERRORNUMBER));
      e.setErrorMessage(_client.getProperty(TPOS3Client.PN_ERRORTEXT));
      e.setDisplayError("Error in authorisation (" + _client.getProperty(TPOS3Client.PN_ERRORNUMBER) + ")");

      throw e;
    }

    TPosException tposEx = null;

    switch (Integer.parseInt(_client.getProperty(TPOS3Client.PN_TOTALRESPONSECODE),10)) {
      case 0 :
        return(_client.getProperty(TPOS3Client.PN_AUTHORIDENTIFYRSP));
      case 1 :
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

    return("-1");
  }

/*  private String doRefund(String cardnumber, String monthExpires, String yearExpires, double amount, String currency, String transactionType) throws TPosException {
    _client.setProperty(TPOS3Client.PN_USERID, "IDE");
    _client.setProperty(TPOS3Client.PN_PASSWORD, "IDE");
    _client.setProperty(TPOS3Client.PN_MERCHANTID, "IDE");
    _client.setProperty(TPOS3Client.PN_LOCATIONID, "0000000001");
    _client.setProperty(TPOS3Client.PN_POSID, "IDE001001");

    _client.setProperty(TPOS3Client.PN_PAN,cardnumber);
    _client.setProperty(TPOS3Client.PN_EXPIRE, yearExpires+monthExpires);
    amount *= 100;
    String stringAmount = Integer.toString((int)amount);
    _client.setProperty(TPOS3Client.PN_AMOUNT,stringAmount);
    _client.setProperty(TPOS3Client.PN_CURRENCY,currency);
    _client.setProperty(TPOS3Client.PN_TRANSACTIONTYPE,transactionType);
    _client.setProperty(TPOS3Client.PN_CARDHOLDERCODE,"2");

    TPosException ex = new TPosException();
    TPosException ex2 = new TPosException("test");

    boolean valid = _client.sendAuthorisationReq();
    if (!valid) {
      TPosException e = new TPosException("Error in authorisation");
      e.setErrorNumber(_client.getProperty(TPOS3Client.PN_ERRORNUMBER));
      e.setErrorMessage(_client.getProperty(TPOS3Client.PN_ERRORTEXT));
      e.setDisplayError("Error in authorisation (" + _client.getProperty(TPOS3Client.PN_ERRORNUMBER) + ")");

      throw e;
    }

    TPosException tposEx = null;

    switch (Integer.parseInt(_client.getProperty(TPOS3Client.PN_TOTALRESPONSECODE),10)) {
      case 0 :
        return(_client.getProperty(TPOS3Client.PN_AUTHORIDENTIFYRSP));
      case 1 :
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

    return("-1");
  }*/
}