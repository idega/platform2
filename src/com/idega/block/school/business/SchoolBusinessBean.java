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

public class SchoolBusinessBean extends IBOServiceBean implements SchoolBusiness{

  public School getSchool(Object primaryKey){
     try{
      SchoolHome shome = (SchoolHome) IDOLookup.getHome(School.class);
      return shome.findByPrimaryKey(primaryKey);
    }
    catch(Exception ex){
      return null;
    }
  }

  public void removeSchool(int id){
    try{
      SchoolHome shome = (SchoolHome) IDOLookup.getHome(School.class);
      School school = getSchool(new Integer(id));
      school.remove();
    }
    catch(Exception ex){
      ex.printStackTrace();

    }
  }

  public Collection findAllSchools()  {
    try{
      SchoolHome shome = (SchoolHome) IDOLookup.getHome(School.class);
      return shome.findAllSchools();
    }
    catch(Exception ex){
      return new java.util.Vector();
    }
  }

  public void storeSchool(
              int id,
              String name,
              String info,
              String address,
              String zipcode,
              String ziparea,
              String phone,
              String keycode,
              String latitude,
              String longitude,
              int type_id,
              int area_id
      ) throws java.rmi.RemoteException{

      SchoolHome shome = (SchoolHome) IDOLookup.getHome(School.class);
      School newSchool;
      try{
        if(id > 0){
          newSchool = shome.findByPrimaryKey(new Integer(id));
        }
        else{
          newSchool = shome.create();
        }
      }
      catch(javax.ejb.FinderException fe){
        throw new java.rmi.RemoteException(fe.getMessage());
      }
      catch(javax.ejb.CreateException ce){
        throw new java.rmi.RemoteException(ce.getMessage());
      }
      newSchool.setSchoolAddress(address);
      newSchool.setSchoolAreaId(area_id);
      newSchool.setSchoolInfo(info);
      newSchool.setSchoolKeyCode(keycode);
      newSchool.setSchoolLatitude(latitude);
      newSchool.setSchoolLongitude(longitude);
      newSchool.setSchoolName(name);
      newSchool.setSchoolPhone(phone);
      newSchool.setSchoolTypeId(type_id);
      newSchool.setSchoolZipArea(ziparea);
      newSchool.setSchoolZipCode(zipcode);

      newSchool.store();
    }


}