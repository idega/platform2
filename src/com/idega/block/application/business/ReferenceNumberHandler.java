/*
 * $Id: ReferenceNumberHandler.java,v 1.2 2001/07/30 11:46:38 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.business;

import com.idega.jmodule.object.ModuleInfo;
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
  public void actionPerformed(ModuleInfo modinfo) throws IWException {
    String ref = modinfo.getParameter("reference");
    CypherText cyph = new CypherText();
    String cypherKey = getCypherKey(modinfo);

    ref = cyph.doDeCypher(ref,cypherKey);
    modinfo.setSessionAttribute(referenceNumber_,ref);
  }

  /**
   *
   */
  public static String getCypherKey(ModuleInfo modinfo) {
    IWBundle iwb = modinfo.getApplication().getBundle(IW_BUNDLE_IDENTIFIER);
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
   * Gets the decyphered reference number from the ModuleInfo and does than
   * delete the sessionAttribute.
   *
   * @param modinfo The ModuleInfo for the session
   * @return
   */
  public static String getReferenceNumber(ModuleInfo modinfo) {
    String ref = (String)modinfo.getSessionAttribute(referenceNumber_);
    modinfo.removeSessionAttribute(referenceNumber_);
    return(ref);
  }
}
