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

public class SchoolTypeBusinessBean extends IBOServiceBean implements SchoolTypeBusiness{

  public SchoolType getSchoolType(Object primaryKey){
     try{
      SchoolTypeHome shome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
      return shome.findByPrimaryKey(primaryKey);
    }
    catch(Exception ex){
      ex.printStackTrace();
      return null;
    }
  }

  public void removeSchoolType(int id){
    try{
      SchoolTypeHome shome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
      SchoolType type = getSchoolType(new Integer(id));
      type.remove();
    }
    catch(Exception ex){
      ex.printStackTrace();

    }
  }

  public Collection findAllSchoolTypes()  {
    try{
      SchoolTypeHome shome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
      return shome.findAllSchoolTypes();
    }
    catch(Exception ex){
      return new java.util.Vector();
    }
  }

  public Collection findAllSchoolTypesInCategory(int CategoryID)  {
    try{
      SchoolTypeHome shome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
      return shome.findAllByCategory(CategoryID);
    }
    catch(Exception ex){
      return new java.util.Vector();
    }
  }

  public void storeSchoolType(int id,String name,String info,int categoryId,String locKey) throws java.rmi.RemoteException{

      SchoolTypeHome shome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
      SchoolType newType;

      try{
        if(id > 0){
          newType = shome.findByPrimaryKey(new Integer(id));
        }
        else{
          newType = shome.create();
        }
      }
      catch(javax.ejb.FinderException fe){
        throw new java.rmi.RemoteException(fe.getMessage());
      }
      catch(javax.ejb.CreateException ce){
        throw new java.rmi.RemoteException(ce.getMessage());
      }
      newType.setSchoolTypeInfo(info);
      newType.setSchoolTypeName(name);
      newType.setSchoolCategoryId(categoryId);
      newType.setLocalizationKey(locKey);
      newType.store();
    }


}