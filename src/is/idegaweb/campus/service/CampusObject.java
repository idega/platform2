/*
 * $Id: CampusObject.java,v 1.3 2001/11/08 15:40:40 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.service;


import com.idega.presentation.Block;
import com.idega.presentation.PresentationObject;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public abstract class CampusObject extends Block {

  public String iObjectName = "Campus";
  public abstract PresentationObject getTabs();

  public String getObjectName(){
      return iObjectName;
  }

}
