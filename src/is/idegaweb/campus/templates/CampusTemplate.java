/*
 * $Id: CampusTemplate.java,v 1.7 2001/08/23 13:50:46 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.templates;

import is.idegaweb.campus.templates.MainTemplate;
import is.idegaweb.campus.templates.CampusPage;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusTemplate extends MainTemplate{

   public void initializePage(){
    setPage(new CampusPage());
  }
}