/*
 * $Id: ContractFinder.java,v 1.3 2001/07/13 11:05:37 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.allocation;

import is.idegaweb.campus.entity.*;
import java.sql.SQLException;
import java.util.List;
import com.idega.data.EntityFinder;
import java.util.Vector;
import java.util.Hashtable;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public abstract class ContractFinder {

  public static List listOfContracts(){
    try {
      return(EntityFinder.findAll(new Contract()));
    }
    catch(SQLException e){
      return(null);
    }
  }

  public static List listOfStatusContracts(String S){
    try {
      return(EntityFinder.findAllByColumn(new Contract(),Contract.getStatusColumnName(),S));
    }
    catch(SQLException e){
      return(null);
    }
  }

  public static Hashtable hashOfContracts(){
    List L = listOfContracts();
    if(L!=null){
      Hashtable H = new Hashtable();
      int len = L.size();
      for (int i = 0; i < len; i++) {
        Contract C = (Contract) L.get(i);
        H.put(new Integer(C.getID()),C);
      }
      return H;
    }
    else
      return null;
  }

  public static Hashtable hashOfApartmentsContracts(){
    List L = listOfContracts();
    if(L!=null){
      Hashtable H = new Hashtable();
      int len = L.size();
      for (int i = 0; i < len; i++) {
        Contract C = (Contract) L.get(i);
        H.put((C.getApartmentId()),C);
      }
      return H;
    }
    else
      return null;
  }

  public static Hashtable hashOfApplicantsContracts(){
    List L = listOfContracts();
    if(L!=null){
      Hashtable H = new Hashtable();
      int len = L.size();
      for (int i = 0; i < len; i++) {
        Contract C = (Contract) L.get(i);
        H.put((C.getApplicantId()),C);
      }
      return H;
    }
    else
      return null;
  }

  public static List listOfApplicantContracts(int iApplicantId){
    try {
      Contract C = new Contract();
      return EntityFinder.findAllByColumn(C,C.getApplicantIdColumnName(),iApplicantId);
    }
    catch(SQLException e){
      return(null);
    }
  }


}




