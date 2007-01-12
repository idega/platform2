package com.idega.block.staff.business;



import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.transaction.TransactionManager;

import com.idega.block.staff.data.StaffEntity;
import com.idega.block.staff.data.StaffInfo;
import com.idega.block.staff.data.StaffLocalized;
import com.idega.block.staff.data.StaffMeta;
import com.idega.block.staff.data.StaffMetaData;
import com.idega.block.staff.data.StaffMetaDataBMPBean;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.business.UserGroupBusiness;
import com.idega.data.GenericEntity;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.util.IWTimestamp;



/**

 * Title:        User

 * Copyright:    Copyright (c) 2000 idega.is All Rights Reserved

 * Company:      idega margmi�lun

 * @author

 * @version 1.0

 */



public class StaffBusiness {



public static final String PARAMETER_MODE = "mode";

public static final String PARAMETER_SAVE = "save";

public static final String PARAMETER_UPDATE = "update";

public static final String PARAMETER_DELETE = "delete";

public static final String PARAMETER_CLOSE = "close";

public static final String PARAMETER_LOCALE_DROP = "locale_drop";

public static final String PARAMETER_LETTER = "letter";



public static final String PARAMETER_USER_ID = "user_id";

public static final String PARAMETER_TITLE = "title";

public static final String PARAMETER_EDUCATION = "education";

public static final String PARAMETER_AREA = "area";

public static final String PARAMETER_BEGAN_WORK = "began_work";

public static final String PARAMETER_IMAGE_ID = "image_id";

public static final String PARAMETER_META_VALUE = "value";

public static final String PARAMETER_META_ATTRIBUTE = "attribute";



  public static void updateStaff(int user_id, String title, String education, String school, String area, IWTimestamp began_work) throws RemoteException{

    StaffInfo staffToAdd = null;


    try {

      staffToAdd = ((com.idega.block.staff.data.StaffInfoHome)com.idega.data.IDOLookup.getHomeLegacy(StaffInfo.class)).findByPrimaryKeyLegacy(user_id);

    }

    catch (SQLException e) {

      staffToAdd = ((com.idega.block.staff.data.StaffInfoHome)com.idega.data.IDOLookup.getHomeLegacy(StaffInfo.class)).createLegacy();

      staffToAdd.setID(user_id);

    }



    if(title != null){

      staffToAdd.setTitle(title);

    }

    if(education != null){

      staffToAdd.setEducation(education);

    }

    if(school != null){

      staffToAdd.setSchool(school);

    }

    if(area != null){

      staffToAdd.setArea(area);

    }

    if(began_work != null){

      staffToAdd.setBeganWork(began_work.getSQLDate());

    }

//    if(!update){
//
//      staffToAdd.setImageID(-1);
//
//    }

    staffToAdd.store();

  }



  public static void saveStaff(int localeID, int userID, String title, String education, String area, IWTimestamp began_work, String imageID) {

    StaffEntity staffToAdd = null;

    boolean update = false;

		int staffImageID = -1;
		try {
			staffImageID = Integer.parseInt(imageID);
		}
		catch (NumberFormatException ne) {
			staffImageID = -1;
		}

    try {

      staffToAdd = ((com.idega.block.staff.data.StaffEntityHome)com.idega.data.IDOLookup.getHomeLegacy(StaffEntity.class)).findByPrimaryKeyLegacy(userID);

      update = true;

    }

    catch (SQLException e) {

      staffToAdd = ((com.idega.block.staff.data.StaffEntityHome)com.idega.data.IDOLookup.getHomeLegacy(StaffEntity.class)).createLegacy();

      staffToAdd.setID(userID);

      update = false;

    }



    if(began_work != null){

      staffToAdd.setBeganWork(began_work.getSQLDate());

    }

    if(staffImageID != -1){

      staffToAdd.setImageID(Integer.parseInt(imageID));

    }



    if ( update ) {
		try {

		    staffToAdd.update();

		  }

		  catch (SQLException e) {

		    e.printStackTrace(System.err);

		  }
	}
	else {
		try {

		    staffToAdd.insert();

		  }

		  catch (SQLException e) {

		    e.printStackTrace(System.err);

		  }
	}



    StaffLocalized locText = StaffFinder.getLocalizedStaff(staffToAdd,localeID);

    boolean newLocText = false;

    if ( locText == null ) {

      locText = ((com.idega.block.staff.data.StaffLocalizedHome)com.idega.data.IDOLookup.getHomeLegacy(StaffLocalized.class)).createLegacy();

      newLocText = true;

    }



    if(title != null){

      locText.setTitle(title);

    }

    if(education != null){

      locText.setEducation(education);

    }

    if(area != null){

      locText.setArea(area);

    }



    if ( newLocText ) {

      locText.setLocaleId(localeID);

      try {

        locText.insert();

        locText.addTo(staffToAdd);

      }

      catch (SQLException e) {

        e.printStackTrace(System.err);

      }

    }

    else {

      try {

        locText.update();

      }

      catch (SQLException e) {

        e.printStackTrace(System.err);

      }

    }

  }



  public static void updateImage(int userId,String imageId) {

    StaffInfo staffToAdd = null;

    boolean update = false;



    try {

      staffToAdd = ((com.idega.block.staff.data.StaffInfoHome)com.idega.data.IDOLookup.getHomeLegacy(StaffInfo.class)).findByPrimaryKeyLegacy(userId);

      update = true;

    }

    catch (SQLException e) {

      staffToAdd = ((com.idega.block.staff.data.StaffInfoHome)com.idega.data.IDOLookup.getHomeLegacy(StaffInfo.class)).createLegacy();

      staffToAdd.setID(userId);

      update = false;

    }



    if ( imageId != null ) {

      try {

        staffToAdd.setImageID(Integer.parseInt(imageId));

      }

      catch (NumberFormatException ex) {

        staffToAdd.setImageID(-1);

      }

    }



    try {

      if ( update ) {

        staffToAdd.update();

      }

      else {

        staffToAdd.insert();

      }

    }

    catch (SQLException ex) {

      ex.printStackTrace(System.err);

    }

  }



  public static void saveImage(int userId,String imageId) {

    StaffEntity staffToAdd = null;

    boolean update = false;



    try {

      staffToAdd = ((com.idega.block.staff.data.StaffEntityHome)com.idega.data.IDOLookup.getHomeLegacy(StaffEntity.class)).findByPrimaryKeyLegacy(userId);

      update = true;

    }

    catch (SQLException e) {

      staffToAdd = ((com.idega.block.staff.data.StaffEntityHome)com.idega.data.IDOLookup.getHomeLegacy(StaffEntity.class)).createLegacy();

      staffToAdd.setID(userId);

      update = false;

    }



    if ( imageId != null ) {

      try {

        staffToAdd.setImageID(Integer.parseInt(imageId));

      }

      catch (NumberFormatException ex) {

        staffToAdd.setImageID(-1);

      }

    }



    try {

      if ( update ) {

        staffToAdd.update();

      }

      else {

        staffToAdd.insert();

      }

    }

    catch (SQLException ex) {

      ex.printStackTrace(System.err);

    }

  }



  public static void updateMetaData(int userID,String a1,String v1,String a2,String v2,String a3,String v3,String a4,String v4,String a5,String v5,String a6,String v6) {

    try {

      com.idega.block.staff.data.StaffMetaDataBMPBean.getStaticInstance().deleteMultiple(com.idega.block.staff.data.StaffMetaDataBMPBean.getColumnNameUserID(),Integer.toString(userID));

    }

    catch (SQLException e) {

      e.printStackTrace(System.err);

    }


    TransactionManager t = IdegaTransactionManager.getInstance();

    StaffMetaData meta = null;


    try {
    	
    	t.begin();
    	
 	    if ( a1 != null && a1.length() > 0 ) {
	
	      meta = ((com.idega.block.staff.data.StaffMetaDataHome)com.idega.data.IDOLookup.getHomeLegacy(StaffMetaData.class)).createLegacy();
	
	      meta.setUserID(userID);
	
	      meta.setAttribute(a1);
	
	      if ( v1 != null ) {
			meta.setValue(v1);
		}
	      meta.insert();
	
	    }
	
	
	
	    if ( a2 != null && a2.length() > 0 ) {
	
	      meta = ((com.idega.block.staff.data.StaffMetaDataHome)com.idega.data.IDOLookup.getHomeLegacy(StaffMetaData.class)).createLegacy();
	
	      meta.setUserID(userID);
	
	      meta.setAttribute(a2);
	
	      if ( v2 != null ) {
			meta.setValue(v2);
		}
	      	
	      meta.insert();
	
	    }
	
	
	
	    if ( a3 != null && a3.length() > 0 ) {
	
	      meta = ((com.idega.block.staff.data.StaffMetaDataHome)com.idega.data.IDOLookup.getHomeLegacy(StaffMetaData.class)).createLegacy();
	
	      meta.setUserID(userID);
	
	      meta.setAttribute(a3);
	
	      if ( v3 != null ) {
			meta.setValue(v3);
		}

	      meta.insert();
	
	    }
	
	
	
	    if ( a4 != null && a4.length() > 0 ) {
	
	      meta = ((com.idega.block.staff.data.StaffMetaDataHome)com.idega.data.IDOLookup.getHomeLegacy(StaffMetaData.class)).createLegacy();
	
	      meta.setUserID(userID);
	
	      meta.setAttribute(a4);
	
	      if ( v4 != null ) {
			meta.setValue(v4);
		}
	
	      meta.insert();
	
	    }
	
	
	
	    if ( a5 != null && a5.length() > 0 ) {
	
	      meta = ((com.idega.block.staff.data.StaffMetaDataHome)com.idega.data.IDOLookup.getHomeLegacy(StaffMetaData.class)).createLegacy();
	
	      meta.setUserID(userID);
	
	      meta.setAttribute(a5);
	
	      if ( v5 != null ) {
			meta.setValue(v5);
		}
	
	      meta.insert();
	
	    }
	
	
	
	    if ( a6 != null && a6.length() > 0 ) {
	
	      meta = ((com.idega.block.staff.data.StaffMetaDataHome)com.idega.data.IDOLookup.getHomeLegacy(StaffMetaData.class)).createLegacy();
	
	      meta.setUserID(userID);
	
	      meta.setAttribute(a6);
	
	      if ( v6 != null ) {
			meta.setValue(v6);
		}
	
	      meta.insert();
	
	    }

	    t.commit();
    } catch (Exception e) {
    	e.printStackTrace(System.err);
      
      try {
      	t.rollback();
      } catch (Exception e1) {
      	e1.printStackTrace(System.err);
      }

    }


}



  public static void saveMetaData(int localeID,int userID,String[] attributes,String[] values) {

    try {

      GenericEntity.getStaticInstance(StaffMeta.class).deleteMultiple(StaffMetaDataBMPBean.getColumnNameUserID(),Integer.toString(userID),com.idega.block.staff.data.StaffMetaBMPBean.getColumnNameLocaleId(),Integer.toString(localeID));

    }

    catch (SQLException e) {

      e.printStackTrace(System.err);

    }


    TransactionManager t = IdegaTransactionManager.getInstance();

    StaffMeta meta = null;


    try {
    	
    	t.begin();

	
	    for ( int a = 0; a < values.length; a++ ) {
	
	      if ( attributes[a] != null && attributes[a].length() > 0 ) {
	
	        meta = ((com.idega.block.staff.data.StaffMetaHome)com.idega.data.IDOLookup.getHomeLegacy(StaffMeta.class)).createLegacy();
	
	        meta.setUserID(userID);
	
	        meta.setLocaleId(localeID);
	
	        meta.setAttribute(attributes[a]);
	
	        if ( values[a] != null ) {
				meta.setValue(values[a]);
			}
	
	        meta.insert();
	
	      }
	
	    }


	    t.commit();
    } catch (Exception e) {
    	e.printStackTrace(System.err);
      
      try {
      	t.rollback();
      } catch (Exception e1) {
      	e1.printStackTrace(System.err);
      }

    }

  }



  public static void deleteGroup(int groupID) {

    try {

      UserGroupBusiness.deleteGroup(groupID);

    }

    catch (SQLException e) {

      e.printStackTrace(System.err);

    }

  }



  public static void deleteStaff(int userId) {

    try {

      StaffInfo delStaff = ((com.idega.block.staff.data.StaffInfoHome)com.idega.data.IDOLookup.getHomeLegacy(StaffInfo.class)).findByPrimaryKeyLegacy(userId);

      delStaff.delete();

    }

    catch (SQLException e) {

    }



    try {

      UserBusiness.deleteUser(userId);

    }

    catch (SQLException e) {

    }

  }



  public static void delete(int userId) {
    try {
      StaffEntity delStaff = ((com.idega.block.staff.data.StaffEntityHome)com.idega.data.IDOLookup.getHomeLegacy(StaffEntity.class)).findByPrimaryKeyLegacy(userId);
      delStaff.delete();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } catch (NullPointerException e) {
      // No staffEntity
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      StaffInfo delStaff = ((com.idega.block.staff.data.StaffInfoHome)com.idega.data.IDOLookup.getHomeLegacy(StaffInfo.class)).findByPrimaryKeyLegacy(userId);
      delStaff.delete();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } catch (NullPointerException e) {
      // No staffEntity
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

} // Class StaffBusiness
