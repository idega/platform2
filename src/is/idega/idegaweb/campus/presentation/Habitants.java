/*
 * $Id: Habitants.java,v 1.1 2002/02/15 11:05:19 palli Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.presentation;

import com.idega.presentation.AbstractMenuBlock;
import com.idega.presentation.IWContext;
import is.idega.idegaweb.campus.block.request.presentation.RequestAdminView;
import is.idega.idegaweb.campus.presentation.TenantsHabitants;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class Habitants extends AbstractMenuBlock {
  /**
   *
   */
  public Habitants() {
  }

  /**
   *
   */
  public void addStandardObjects() {
    addBlockObject(new TenantsHabitants());
    addBlockObject(new RequestAdminView());
  }

  /**
   *
   */
  public Class getDefaultBlockClass() {
    return(null);
  }

  /**
   *
   */
  public void main(IWContext iwc) {

  }
}