/*
 * $Id: CampusApplicationFinder.java,v 1.1 2001/06/27 14:40:43 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.application;

import is.idegaweb.campus.entity.SpouseOccupation;
import is.idegaweb.campus.entity.CurrentResidency;
import java.sql.SQLException;
import java.util.List;
import com.idega.data.EntityFinder;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public abstract class CampusApplicationFinder {
  public static List listOfSpouseOccupations(){
    try {
      return(EntityFinder.findAll(new SpouseOccupation()));
    }
    catch(SQLException e){
      return(null);
    }
  }

  public static List listOfResidences(){
    try {
      return(EntityFinder.findAll(new CurrentResidency()));
    }
    catch(SQLException e){
      return(null);
    }
  }
}




