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
  private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.tpos";
  private TPOS3Client _client = null;
  private IWBundle _iwb = null;
  private IWResourceBundle _iwrb = null;

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
    }
    catch(Exception e) {
      System.out.println("Got an exception trying to create client");
    }
  }

  /**
   * @return The authorisation code as a String
   */
  public String doSale(String cardnumber, String monthExpires, String yearExpires, double amount, String currency) throws TPosException {
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
      _client.setProperty(TPOS3Client.PN_TRANSACTIONTYPE,"1");
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

  /**
   *
   */
  public String getBundleIdentifier(){
    return(IW_BUNDLE_IDENTIFIER);
  }
}