package com.idega.block.application.business;

import com.idega.block.application.data.ApplicationSubject;
import java.sql.SQLException;
import java.sql.Date;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class ApplicationBusiness {

  public ApplicationBusiness() {
  }

  public static ApplicationSubject saveApplicationSubject(int id,String sName, Date expires){
    try {
      ApplicationSubject subject = ((com.idega.block.application.data.ApplicationSubjectHome)com.idega.data.IDOLookup.getHomeLegacy(ApplicationSubject.class)).createLegacy();
      boolean update = false;
      if(id > 0){
        subject = ((com.idega.block.application.data.ApplicationSubjectHome)com.idega.data.IDOLookup.getHomeLegacy(ApplicationSubject.class)).findByPrimaryKeyLegacy(id);
        update = true;
      }
      subject.setExpires(expires);
      subject.setDescription(sName);
      if(update)
        subject.update();
      else
        subject.insert();

      return subject;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static boolean deleteApplicationSubject(int id){
  try {
      ((com.idega.block.application.data.ApplicationSubjectHome)com.idega.data.IDOLookup.getHomeLegacy(ApplicationSubject.class)).findByPrimaryKeyLegacy(id).delete();
      return true;
    }
    catch (Exception ex) {

    }
    return false;
  }
}
