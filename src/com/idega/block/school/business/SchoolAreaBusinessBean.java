package com.idega.block.school.business;

import com.idega.block.school.data.*;
import com.idega.data.IDOLookup;
import java.util.Collection;
import com.idega.business.IBOServiceBean;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolAreaBusinessBean extends IBOServiceBean implements SchoolAreaBusiness{

  public SchoolArea getSchoolArea(Object primaryKey){
     try{
      SchoolAreaHome shome = (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
      return shome.findByPrimaryKey(primaryKey);
    }
    catch(Exception ex){
      return null;
    }
  }

  public void removeSchoolArea(int id){
    try{
      SchoolAreaHome shome = (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
      SchoolArea area = getSchoolArea(new Integer(id));
      area.remove();
    }
    catch(Exception ex){
      ex.printStackTrace();

    }
  }

  public Collection findAllSchoolAreas()  {
    try{
      SchoolAreaHome shome = (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
      return shome.findAllSchoolAreas();
    }
    catch(Exception ex){
      return new java.util.Vector();
    }
  }

  public void storeSchoolArea(int id,String name,String info,String city) throws java.rmi.RemoteException{

      SchoolAreaHome shome = (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
      SchoolArea newArea;
      try{
        if(id > 0){
          newArea = shome.findByPrimaryKey(new Integer(id));
        }
        else{
          newArea = shome.create();
        }
      }
      catch(javax.ejb.FinderException fe){
        throw new java.rmi.RemoteException(fe.getMessage());
      }
      catch(javax.ejb.CreateException ce){
        throw new java.rmi.RemoteException(ce.getMessage());
      }
      newArea.setSchoolAreaCity(city);
      newArea.setSchoolAreaInfo(info);
      newArea.setSchoolAreaName(name);
      newArea.store();
    }


}