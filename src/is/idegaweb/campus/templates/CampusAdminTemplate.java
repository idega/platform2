/*
 * $Id: CampusAdminTemplate.java,v 1.2 2001/08/29 21:18:24 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.templates;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public  class CampusAdminTemplate extends AdminTemplate{

  public void initializePage(){
    setPage(new CampusAdminPage());
  }
}