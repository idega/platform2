/*
 * $Id: CampusAdminTemplate.java,v 1.1 2001/11/08 14:43:05 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.templates;


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
