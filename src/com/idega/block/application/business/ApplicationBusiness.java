package com.idega.block.application.business;

import com.idega.block.application.data.ApplicationSubject;
import com.idega.block.application.data.ApplicationSubjectHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;

import java.sql.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
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
		 ApplicationSubject subject = ((ApplicationSubjectHome)IDOLookup.getHome(ApplicationSubject.class)).create();
		  if(id > 0){
		    subject = ((ApplicationSubjectHome)IDOLookup.getHome(ApplicationSubject.class)).findByPrimaryKey(new Integer(id));
		  }
		  subject.setExpires(expires);
		  subject.setDescription(sName);
		  subject.store();
		
		  return subject;
	}
	catch (IDOLookupException e) {
		e.printStackTrace();
	}
	catch (IDOStoreException e) {
		e.printStackTrace();
	}
	catch (CreateException e) {
		e.printStackTrace();
	}
	catch (FinderException e) {
		e.printStackTrace();
	}
    
    return null;
  }

  public static boolean deleteApplicationSubject(int id){
  try {
      ((ApplicationSubjectHome)IDOLookup.getHome(ApplicationSubject.class)).findByPrimaryKey(new  Integer(id)).remove();
      return true;
    }
    catch (Exception ex) {

    }
    return false;
  }
}
