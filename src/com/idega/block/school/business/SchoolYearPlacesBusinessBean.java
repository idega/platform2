package com.idega.block.school.business;

import com.idega.block.school.data.*;
import com.idega.data.IDOLookup;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import com.idega.business.IBOServiceBean;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolYearPlacesBusinessBean extends IBOServiceBean implements SchoolYearPlacesBusiness{

  public SchoolYearPlaces getSchoolYearPlaces(Object primaryKey){
     try{
      SchoolYearPlacesHome shome = (SchoolYearPlacesHome) IDOLookup.getHome(SchoolYearPlaces.class);
      return shome.findByPrimaryKey(primaryKey);
    }
    catch(Exception ex){
      return null;
    }
  }

  public void removeSchool(int id){
    try{
      SchoolYearPlacesHome shome = (SchoolYearPlacesHome) IDOLookup.getHome(SchoolYearPlaces.class);
      SchoolYearPlaces schoolYearPlaces = getSchoolYearPlaces(new Integer(id));
      schoolYearPlaces.remove();
    }
    catch(Exception ex){
      ex.printStackTrace();

    }
  }

  public Collection findAllSchoolYearPlaces(int iSchoolId)  {
    try{
      SchoolYearPlacesHome shome = getSchoolYearPlacesHome();
      return shome.findAllBySchool(iSchoolId);
    }
    catch(Exception ex){
      ex.printStackTrace();
      return new java.util.Vector();
    }
  }

  public Map getMapOfSchoolYearPlaces(int iSchoolId){
    Collection c = findAllSchoolYearPlaces(iSchoolId);
    Map m= new HashMap();
    try {
      Iterator iter = c.iterator();
      while (iter.hasNext()) {
        SchoolYearPlaces p = (SchoolYearPlaces) iter.next();
        m.put((Integer) p.getPrimaryKey(),p);
      }

    }
    catch (Exception ex) {

    }
    return m;
  }

  public SchoolYearPlacesHome getSchoolYearPlacesHome()throws java.rmi.RemoteException{
    return (SchoolYearPlacesHome) IDOLookup.getHome(SchoolYearPlaces.class);
  }

  public void storeSchoolYearPlaces(
              int id,
              int school_id,
              int school_year_id,
              int places
      ) throws java.rmi.RemoteException{

      SchoolYearPlacesHome shome = getSchoolYearPlacesHome();
      SchoolYearPlaces newSchoolYearPlaces;
      try{
        if(id > 0){
          newSchoolYearPlaces = shome.findByPrimaryKey(new Integer(id));
        }
        else{
          newSchoolYearPlaces = shome.create();
        }
      }
      catch(javax.ejb.FinderException fe){
        throw new java.rmi.RemoteException(fe.getMessage());
      }
      catch(javax.ejb.CreateException ce){
        throw new java.rmi.RemoteException(ce.getMessage());
      }
      newSchoolYearPlaces.setSchoolId(school_id);
      newSchoolYearPlaces.setSchoolYearId(school_year_id);
      newSchoolYearPlaces.setPlaces(places);
      newSchoolYearPlaces.store();

    }
}