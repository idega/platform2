/*

 * $Id: WaitingListFinder.java,v 1.2 2002/04/06 19:11:13 tryggvil Exp $

 *

 * Copyright (C) 2001 Idega hf. All Rights Reserved.

 *

 * This software is the proprietary information of Idega hf.

 * Use is subject to license terms.

 *

 */

package is.idega.idegaweb.campus.block.allocation.business;





import is.idega.idegaweb.campus.block.application.data.WaitingList;

import java.sql.SQLException;

import java.util.List;

import com.idega.data.EntityFinder;

import java.util.Vector;

import java.util.Hashtable;

import java.util.Map;

import java.util.Iterator;

import com.idega.core.user.data.User;

import com.idega.block.application.data.Applicant;



/**

 *

 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>

 * @version 1.0

 */

public abstract class WaitingListFinder {

  public final static int APPLICANT = 1,APARTMENTTYPE=2,COMPLEX = 4;



  public static List listOfWaitinglist(){

    try {

      return(EntityFinder.findAll(((is.idega.idegaweb.campus.block.application.data.WaitingListHome)com.idega.data.IDOLookup.getHomeLegacy(WaitingList.class)).createLegacy()));

    }

    catch(SQLException e){

      e.printStackTrace();

      return(null);

    }

  }



  public static List listOfWaitingList(int FIELDS,int iApplicantId,int iTypeId,int iComplexId){

     try {

      return(EntityFinder.findAll(((is.idega.idegaweb.campus.block.application.data.WaitingListHome)com.idega.data.IDOLookup.getHomeLegacy(WaitingList.class)).createLegacy(),getSQL(FIELDS ,iApplicantId,iTypeId,iComplexId) ));

    }

    catch(SQLException e){

      e.printStackTrace();

      return(null);

    }

  }



  public static String getSQL(int FIELDS,int iApplicantId,int iTypeId,int iComplexId){

    StringBuffer sql = new StringBuffer("select * from ");

    sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getEntityTableName());

    int count = 0;

    if( FIELDS > 0 ){

      sql.append(" where ");

      if((FIELDS & APPLICANT) == APPLICANT && iApplicantId > 0){

        sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getApplicantIdColumnName());

        sql.append(" = ");

        sql.append(iApplicantId);

        count++;

      }

      if((FIELDS & APARTMENTTYPE )== APARTMENTTYPE && iTypeId > 0){

        sql.append(count > 0?" and ": "  ");

        sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getApartmentTypeIdColumnName());

        sql.append(" = ");

        sql.append(iTypeId);

        count++;

      }

      if((FIELDS & COMPLEX ) == COMPLEX && iComplexId > 0 ){

        sql.append(count > 0?" and ": "  ");

        sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getComplexIdColumnName());

        sql.append(" = ");

        sql.append(iComplexId);

        count++;

      }

    }

    //System.err.println(sql.toString());

    return sql.toString();

  }







}







