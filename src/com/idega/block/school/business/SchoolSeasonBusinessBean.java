package com.idega.block.school.business;

import com.idega.block.school.data.*;
import com.idega.data.IDOLookup;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Vector;

import com.idega.business.IBOServiceBean;
import java.util.Date;

import javax.ejb.FinderException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolSeasonBusinessBean extends IBOServiceBean implements SchoolSeasonBusiness{

  public SchoolSeason getSchoolSeason(Object primaryKey){
     try{
      SchoolSeasonHome shome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
      return shome.findByPrimaryKey(primaryKey);
    }
    catch(Exception ex){
      return null;
    }
  }

  public void removeSchoolSeason(int id){
    try{
      SchoolSeasonHome shome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
      SchoolSeason season = getSchoolSeason(new Integer(id));
      season.remove();
    }
    catch(Exception ex){
      ex.printStackTrace();

    }
  }

  public Collection findAllSchoolSeasons()  {
    try{
      SchoolSeasonHome shome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
      return shome.findAllSchoolSeasons();
    }
    catch(Exception ex){
      ex.printStackTrace();
      return new java.util.Vector();
    }
  }
  
  public Collection findAllPreviousSchoolSeasons(SchoolSeason schoolSeason) {
  	try{
     SchoolSeasonHome shome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
      return shome.findAllPreviousSchoolSeasons(schoolSeason);
    }
    catch(Exception ex){
      ex.printStackTrace();
      return new java.util.Vector();
    }
  }
  
  public Collection findAllPreviousSchoolSeasons(int schoolSeasonID) throws RemoteException {
  	try {
    	SchoolSeasonHome shome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
    	SchoolSeason season = shome.findByPrimaryKey(new Integer(schoolSeasonID));
    	return shome.findAllPreviousSchoolSeasons(season);
  	}
  	catch (FinderException fe) {
  		return new Vector();
  	}
  }

  public void storeSchoolSeason(int id,String name,Date start,Date end,Date due_date) throws java.rmi.RemoteException{

      SchoolSeasonHome shome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
      SchoolSeason newSeason;
      try{
        if(id > 0){
          newSeason = shome.findByPrimaryKey(new Integer(id));
        }
        else{
          newSeason = shome.create();
        }
      }
      catch(javax.ejb.FinderException fe){
        throw new java.rmi.RemoteException(fe.getMessage());
      }
      catch(javax.ejb.CreateException ce){
        throw new java.rmi.RemoteException(ce.getMessage());
      }
      newSeason.setSchoolSeasonName(name);
      newSeason.setSchoolSeasonStart(start);
      newSeason.setSchoolSeasonEnd(end);
      newSeason.setSchoolSeasonDueDate(due_date);
      newSeason.store();
    }


}