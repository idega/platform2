/*
 * $Id: ContractBusiness.java,v 1.1 2001/11/17 14:50:05 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.contract.business;

import com.idega.block.contract.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.core.data.ICObjectInstance;
import com.idega.core.data.ICFile;
import java.sql.SQLException;
import java.util.List;
import java.util.Iterator;
import java.util.Map;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */
public  class ContractBusiness {

	public static String[] getTags(){
	  return new String[0];
	}
	public static void setContractStatus(int iContractId,String status){
	  try {
      Contract C = new Contract(iContractId );
      C.setStatus(status);
      C.update();
    }
    catch (SQLException ex) {
      ex.printStackTrace( );
    }
	}
  public static void endContract(int iContractId){
    try {
      Contract C = new Contract(iContractId );
      C.setStatusEnded();
      C.update();
    }
    catch (SQLException ex) {
      ex.printStackTrace( );
    }
  }

  public static void resignContract(int iContractId){
    try {
      Contract C = new Contract(iContractId );
      C.setStatusResigned();
      C.update();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

	public static boolean  deleteTag(int id){
	  if(id > 0){
			try {
				new ContractTag(id).delete();
				return true;
		  }
		  catch (Exception ex) {

		  }
	  }
		return false;
	}

	public static void saveTag(int iTagId,String sName,String sInfo,boolean inUse,boolean inList,int iCategoryId){
	  try {
			ContractTag tag = new ContractTag();
			boolean update = false;
			if(iTagId > 0){
				tag = new ContractTag(iTagId);
				update = true;
			}
		  tag.setName(sName);
			tag.setInfo(sInfo);
			tag.setInUse(inUse);
			tag.setInList(inList);
			tag.setCategoryId(iCategoryId);
			if(update)
				tag.update();
			else
				tag.insert();

		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public static int saveCategory(int iCategoryId,int iObjectInstanceId,String Name,String info){
	  int id = -1;
		try {
			ContractCategory cat = new ContractCategory();
			boolean update = false;
			if(iCategoryId > 0){
			  cat = new ContractCategory(iCategoryId);
				update = true;
			}
			cat.setName(Name);
			cat.setDescription(info);
			if(update)
				cat.update();
			else
				cat.insert();
				// Binding category to instanceId
			if(iObjectInstanceId > 0){
				ICObjectInstance objIns = new ICObjectInstance(iObjectInstanceId);
				// Allows only one category per instanceId
				objIns.removeFrom(new ContractCategory());
        cat.addTo(objIns);
      }
			id = cat.getID();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		return id;
	}

	public static void saveContract(int iCategoryId,idegaTimestamp ValFrom,idegaTimestamp ValTo,String sStatus,Map map){
	  try {
			Contract C = new Contract();
			C.setStatus(sStatus);
			C.setValidFrom(ValFrom.getSQLDate());
			C.setValidTo(ValTo.getSQLDate());
			C.setCategoryId(iCategoryId);
			Iterator I = map.entrySet().iterator();
			while(I.hasNext()){
			  Map.Entry me = (Map.Entry) I.next();
				C.addMetaData(me.getKey().toString(),me.getValue().toString());
			}
		  C.insert();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public static void saveContractStatus(int iContractId,String status){
	  try {
			Contract C = new Contract(iContractId);
			C.setStatus(status);
			C.update();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public static boolean disconnectBlock(int instanceid){
    List L = ContractFinder.listOfEntityForObjectInstanceId(instanceid);
    if(L!= null){
      Iterator I = L.iterator();
      while(I.hasNext()){
        ContractCategory N = (ContractCategory) I.next();
        disconnectCategory(N,instanceid);
      }
      return true;
    }
    else
      return false;

  }



	public static boolean disconnectCategory(ContractCategory newsCat,int iObjectInstanceId){
    try {
      newsCat.setValid(false);
      newsCat.update();
      if(iObjectInstanceId > 0  ){
        ICObjectInstance obj = new ICObjectInstance(iObjectInstanceId);
        newsCat.removeFrom(obj);
      }

      return true;
    }
    catch (SQLException ex) {

    }
    return false;
  }


  public static boolean deleteBlock(int instanceid){
		/*
    List L = ContractFinder.listOfEntityForObjectInstanceId(instanceid);
    if(L!= null){
      Iterator I = L.iterator();
      while(I.hasNext()){
        ContractCategory N = (ContractCategory) I.next();
        deleteCategory(N.getID(),instanceid );
      }
      return true;
    }
    else
      return false;
		*/
		return disconnectBlock(instanceid);
  }

	public static void deleteCategory(int iCategoryId){
    deleteCategory(iCategoryId ,ContractFinder.getObjectInstanceIdFromCategoryId(iCategoryId));
  }

	public static void deleteCategory(int iCategoryId ,int iObjectInstanceId) {
    javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
    try {
      t.begin();
    //  List O = TextFinder.listOfObjectInstanceTexts();
      ContractCategory nc = new ContractCategory( iCategoryId );
      List L = ContractFinder.listOfContracts(nc.getID());
      if(L != null){
        Contract con;

        for (int i = 0; i < L.size(); i++) {
          con = (Contract) L.get(i);
          deleteContract(con.getID());
        }
      }

      if(iObjectInstanceId > 0  ){
        ICObjectInstance obj = new ICObjectInstance(iObjectInstanceId);
        nc.removeFrom(obj);
      }
      nc.delete();
     t.commit();
    }
    catch(Exception e) {
      try {
        t.rollback();
      }
      catch(javax.transaction.SystemException ex) {
        ex.printStackTrace();
      }
      e.printStackTrace();
    }
  }

	 public static boolean deleteContract(int iContractId){
    try {
      new Contract(iContractId ).delete();
      return true;
    }
    catch (SQLException ex) {
      return false;
    }
  }

	public static int createCategory(int iObjectInstanceId){
    return saveCategory(-1,iObjectInstanceId,"Contracts","Contracts" );
  }

	/// Contract Text part //////////////////////////////////////////
	public static void saveContractText(int iTextId,int iCategoryId,String sName,String sText,int iOrdinal,boolean useTags){
		ContractText CT = null;
    boolean bInsert = true;
    if(iTextId > 0){
      try{
        CT = new ContractText(iTextId);
        bInsert = false;
      }
      catch(SQLException ex){ex.printStackTrace();}
    }
    else{
      CT = new ContractText();
			CT.setCategoryId(iCategoryId);
      bInsert = true;
    }
    if(CT !=null){
      CT.setName(sName);
      CT.setText(sText);
      CT.setOrdinal(iOrdinal);
      CT.setLanguage("IS");
      CT.setUseTags(useTags);
      try {
        if(bInsert)
          CT.insert();
        else
          CT.update();
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
	}

	public static boolean deleteText(int iTextId){
    if(iTextId > 0){
      try {
        new ContractText(iTextId).delete();
				return true;
      }
      catch (SQLException ex) {
				ex.printStackTrace();
      }
    }
		return false;
  }

	public static boolean updateCategoryDescription(int id ,String description){
	  try {
			ContractCategory cat = new ContractCategory(id);
			cat.setDescription(description);
			cat.update();
			return true;
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;

	}

	public static boolean deleteContractFile(int iFileId,int iContractId){
		try {
			Contract C = new Contract(iContractId);
			ICFile file = new ICFile(iFileId);
			file.removeFrom(C);
			file.delete();
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

}

