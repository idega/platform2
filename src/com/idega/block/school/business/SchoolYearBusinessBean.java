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

public class SchoolYearBusinessBean extends IBOServiceBean implements SchoolYearBusiness{

  public SchoolYear getSchoolYear(Object primaryKey) throws java.rmi.RemoteException{
     try{
      SchoolYearHome shome = getSchoolYearHome();
      return shome.findByPrimaryKey(primaryKey);
    }
    catch(Exception ex){
      return null;
    }
  }
  

  public void removeSchoolYear(int id) throws java.rmi.RemoteException{
    try{
      SchoolYearHome shome = getSchoolYearHome();
      SchoolYear area = getSchoolYear(new Integer(id));
      area.remove();
    }
    catch(Exception ex){
      ex.printStackTrace();

    }
  }

  public Collection findAllSchoolYears()   throws java.rmi.RemoteException{
    try{
      SchoolYearHome shome = getSchoolYearHome();
      return shome.findAllSchoolYears();
    }
    catch(Exception ex){
      ex.printStackTrace();
      return new java.util.Vector();
    }
  }

   public Collection findAllSchoolYearsByAge(int age)  throws java.rmi.RemoteException{
    try{
      SchoolYearHome shome = getSchoolYearHome();
      return shome.findAllByAge(age);
    }
    catch(Exception ex){
      ex.printStackTrace();
      return new java.util.Vector();
    }
  }

  public void storeSchoolYear(int pk,String name,String info,int age) throws java.rmi.RemoteException{

      SchoolYearHome shome = getSchoolYearHome();
      SchoolYear newYear;
      try{
        if(pk>0){
          newYear = shome.findByPrimaryKey(new Integer(pk));
        }
        else{
          newYear = shome.create();

        }
      }
      catch(javax.ejb.FinderException fe){
        throw new java.rmi.RemoteException(fe.getMessage());
      }
      catch(javax.ejb.CreateException ce){
        throw new java.rmi.RemoteException(ce.getMessage());
      }

      newYear.setSchoolYearName(name);
      newYear.setSchoolYearInfo(info);
      newYear.setSchoolYearAge(age);
      newYear.store();
    }
    
    public SchoolYearHome getSchoolYearHome() throws java.rmi.RemoteException{
    	return (SchoolYearHome) IDOLookup.getHome(SchoolYear.class);
    } 


}