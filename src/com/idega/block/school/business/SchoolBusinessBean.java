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
      ex.printStackTrace();
      return new java.util.Vector();
    }
  }

  public Collection findAllSchoolsByAreaAndType(int area,int type){
    try{
      SchoolHome shome = (SchoolHome) IDOLookup.getHome(School.class);
      return shome.findAllByAreaAndType(area,type);
    }
    catch(Exception ex){
      ex.printStackTrace();
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
              int area_id,
              int[] type_ids
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
      newSchool.setSchoolZipArea(ziparea);
      newSchool.setSchoolZipCode(zipcode);
      newSchool.store();

      newSchool.addSchoolTypesRemoveOther(type_ids);
    }

    public Map getSchoolRelatedSchoolTypes(School school){
      try{
        Collection types = school.findRelatedSchoolTypes();
        if(types!=null && !types.isEmpty()){
          HashMap map = new HashMap(types.size());
          Iterator iter = types.iterator();
          SchoolType type;
          while(iter.hasNext()){
            type = (SchoolType) iter.next();
            map.put((Integer)type.getPrimaryKey(),type);
          }
          return map;
        }
      }
      catch(Exception ex){
        ex.printStackTrace();
      }
      return null;
    }
}