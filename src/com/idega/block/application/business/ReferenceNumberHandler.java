/*
 * $Id: ReferenceNumberHandler.java,v 1.4 2001/11/29 11:16:16 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.business;

import com.idega.block.application.presentation.ReferenceNumber;
import com.idega.presentation.IWContext;
import com.idega.business.IWEventListener;
import com.idega.idegaweb.IWException;
import com.idega.idegaweb.IWBundle;
import com.idega.util.CypherText;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ReferenceNumberHandler implements IWEventListener {
  private static final String referenceNumber_ = "referenceNumber";
  private static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.application";

  /**
   *
   */
  public ReferenceNumberHandler() {
  }

  /**
   *
   */
  public void actionPerformed(IWContext iwc) throws IWException {
    String ref = iwc.getParameter(ReferenceNumber.CAM_REF_NUMBER);
    CypherText cyph = new CypherText();
    String cypherKey = getCypherKey(iwc);

    ref = cyph.doDeCypher(ref,cypherKey);
    iwc.setSessionAttribute(referenceNumber_,ref);
  }

  /**
   *
   */
  public static String getCypherKey(IWContext iwc) {
    IWBundle iwb = iwc.getApplication().getBundle(IW_BUNDLE_IDENTIFIER);
    CypherText cyph = new CypherText();

    String cypherKey = iwb.getProperty("cypherKey");
    if ((cypherKey == null) || (cypherKey.equalsIgnoreCase(""))) {
      cypherKey = cyph.getKey(100);
      iwb.setProperty("cypherKey",cypherKey);
    }

    return(cypherKey);
  }

  public String getBundleIdentifier() {
    return(IW_BUNDLE_IDENTIFIER);
  }

  /**
   * Gets the decyphered reference number from the IWContext and does than
   * delete the sessionAttribute.
   *
   * @param iwc The IWContext for the session
   * @return
   */
  public static String getReferenceNumber(IWContext iwc) {
    String ref = (String)iwc.getSessionAttribute(referenceNumber_);
    iwc.removeSessionAttribute(referenceNumber_);
    return(ref);
  }
}
