/*
 * $Id: CampusTemplate.java,v 1.8 2001/08/29 21:15:58 aron Exp $
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
public class CampusTemplate extends MainTemplate{

   public void initializePage(){
    setPage(new CampusPage());
  }
}