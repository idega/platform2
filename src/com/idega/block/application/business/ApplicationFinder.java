/*
 * $Id: ApplicationFinder.java,v 1.3 2001/06/25 18:06:08 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.business;

import com.idega.block.application.data.ApplicationSubject;
import java.sql.SQLException;
import java.util.List;
import com.idega.data.EntityFinder;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApplicationFinder {
  public static List listOfSubject(){
    try {
      return(EntityFinder.findAll(new ApplicationSubject()));
    }
    catch(SQLException e){
      return(null);
    }
  }

}