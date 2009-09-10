/*
 * $Id: ContractBusiness.java,v 1.13.4.1 2007/01/12 19:32:12 idegaweb Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.contract.business;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.CreateException;

import com.idega.block.contract.data.Contract;
import com.idega.block.contract.data.ContractCategory;
import com.idega.block.contract.data.ContractCategoryHome;
import com.idega.block.contract.data.ContractHome;
import com.idega.block.contract.data.ContractTag;
import com.idega.block.contract.data.ContractTagHome;
import com.idega.block.contract.data.ContractText;
import com.idega.block.contract.data.ContractTextHome;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.core.component.data.ICObjectInstanceHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.IWTimestamp;
/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved

 * Company:      idega

  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>

 * @version 1.1

 */
/**
  * @deprecated
  */
public class ContractBusiness
{
	public static String[] getTags()
	{
		return new String[0];
	}
	public static void setContractStatus(int iContractId, String status)
	{
		try
		{
			getContractHome().setStatus(iContractId,status);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public static void endContract(int iContractId)
	{
		try
		{
			Contract C = getContractHome().findByPrimaryKey(new Integer(iContractId));
			C.setStatusEnded();
			C.store();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public static void resignContract(int iContractId)
	{
		try
		{
			Contract C = getContractHome().findByPrimaryKey(new Integer(iContractId));
			C.setStatusResigned();
			C.store();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public static boolean deleteTag(int id)
	{
		if (id > 0)
		{
			try
			{
				((com.idega.block.contract.data.ContractTagHome) com.idega.data.IDOLookup.getHome(ContractTag.class))
					.findByPrimaryKey(id)
					.delete();
				return true;
			}
			catch (Exception ex)
			{
			}
		}
		return false;
	}
	public static void saveTag(int iTagId, String sName, String sInfo, boolean inUse, boolean inList, int iCategoryId)
	{
		try
		{
			ContractTag tag =
				((ContractTagHome) IDOLookup.getHome(ContractTag.class)).create();
			if (iTagId > 0)
			{
				tag =((ContractTagHome) IDOLookup.getHome(ContractTag.class)).findByPrimaryKey(new Integer(iTagId));

			}
			tag.setName(sName);
			tag.setInfo(sInfo);
			tag.setInUse(inUse);
			tag.setInList(inList);
			tag.setCategoryId(iCategoryId);
			tag.store();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static ContractCategory findCategory(int catId){
		ContractCategory cat = null;
		try
		{
			cat =
				((com.idega.block.contract.data.ContractCategoryHome) com.idega.data.IDOLookup.getHome(ContractCategory.class))
					.findByPrimaryKey(new Integer(catId));
				
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return cat;
		
	}
	
	public static int saveCategory(int iCategoryId, int iObjectInstanceId, String Name, String info)
	{

		
		int id = -1;
		try
		{
			ContractCategory cat = getContractCategoryHome().create();
		
			if (iCategoryId > 0)
			{
				cat =getContractCategoryHome().findByPrimaryKey(new Integer(iCategoryId));
				
			}
			cat.setName(Name);
			cat.setDescription(info);
			
				cat.store();
			// Binding category to instanceId
			if (iObjectInstanceId > 0)
			{
				ICObjectInstance objIns =	((ICObjectInstanceHome) IDOLookup.getHome(ICObjectInstance.class)).findByPrimaryKey(new Integer(iObjectInstanceId));
				// Allows only one category per instanceId
				objIns.removeFrom(ContractCategory.class);
				objIns.addTo(ContractCategory.class,((Integer)cat.getPrimaryKey()).intValue());
			}
			id = ((Integer)cat.getPrimaryKey()).intValue();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return id;
	}
	public static void saveContract(int iCategoryId, IWTimestamp ValFrom, IWTimestamp ValTo, String sStatus, Map map)
	{
		try
		{
			Contract C = ((ContractHome) IDOLookup.getHome(Contract.class)).create();
			C.setStatus(sStatus);
			C.setValidFrom(ValFrom.getSQLDate());
			C.setValidTo(ValTo.getSQLDate());
			C.setCategoryId(iCategoryId);
			Iterator I = map.entrySet().iterator();
			while (I.hasNext())
			{
				Map.Entry me = (Map.Entry) I.next();
				C.setMetaData(me.getKey().toString(), me.getValue().toString());
			}
			C.store();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public static void saveContractStatus(int iContractId, String status)
	{
		try
		{
			Contract C =
				((ContractHome) IDOLookup.getHome(Contract.class)).findByPrimaryKey(
					new Integer(iContractId));
			C.setStatus(status);
			C.store();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public static boolean disconnectBlock(int instanceid)
	{
		Collection categories = ContractFinder.listOfEntityForObjectInstanceId(instanceid);
		if (categories != null)
		{
			Iterator I = categories.iterator();
			while (I.hasNext())
			{
				ContractCategory N = (ContractCategory) I.next();
				disconnectCategory(N, instanceid);
			}
			return true;
		}
		else {
			return false;
		}
	}
	public static boolean disconnectCategory(ContractCategory Cat, int iObjectInstanceId)
	{
		try
		{
			Cat.setValid(false);
			Cat.store();
			if (iObjectInstanceId > 0)
			{
				ICObjectInstance obj =
					(
						(ICObjectInstanceHome)IDOLookup.getHome(
							ICObjectInstance.class)).findByPrimaryKey(
						iObjectInstanceId);
				obj.removeFrom(ContractCategory.class,((Integer)Cat.getPrimaryKey()).intValue());
				
			}
			return true;
		}
		catch (Exception ex)
		{
		}
		return false;
	}
	public static boolean deleteBlock(int instanceid)
	{
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
	public static void deleteCategory(int iCategoryId)
	{
		deleteCategory(iCategoryId, ContractFinder.getObjectInstanceIdFromCategoryId(iCategoryId));
	}
	public static void deleteCategory(int iCategoryID, int iObjectInstanceId)
	{
		javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
		try
		{
			t.begin();
			//  List O = TextFinder.listOfObjectInstanceTexts();
			ContractCategory nc =((ContractCategoryHome) IDOLookup.getHome(ContractCategory.class)).findByPrimaryKey(new Integer(iCategoryID));
			Collection L = ((ContractHome) IDOLookup.getHome(Contract.class)).findAllByCategory(iCategoryID);
			if (L != null)
			{
				Iterator iter = L.iterator();
				while(iter.hasNext()){
					((Contract) iter.next()).remove();				
				}
			}
			if (iObjectInstanceId > 0)
			{
				ICObjectInstance obj =((ICObjectInstanceHome) IDOLookup.getHome(ICObjectInstance.class)).findByPrimaryKey(	iObjectInstanceId);
				obj.removeFrom(ContractCategory.class,iCategoryID);
			}
			nc.remove();
			t.commit();
		}
		catch (Exception e)
		{
			try
			{
				t.rollback();
			}
			catch (javax.transaction.SystemException ex)
			{
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	public static int createCategory(int iObjectInstanceId)
	{
		return saveCategory(-1, iObjectInstanceId, "Contracts", "Contracts");
	}
	/// Contract Text part //////////////////////////////////////////
	public static void saveContractText(int iTextId, int iCategoryId, String sName, String sText, int iOrdinal, boolean useTags)
	{
		ContractText CT = null;
		boolean bInsert = true;
		if (iTextId > 0)
		{
			try
			{
				CT =((ContractTextHome) IDOLookup.getHome(ContractText.class)).findByPrimaryKey(new Integer(iTextId));
				bInsert = false;
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		else
		{
			try {
				CT = ((ContractTextHome) IDOLookup.getHome(ContractText.class)).create();
				CT.setCategoryId(iCategoryId);
				bInsert = true;
			}
			catch (IDOLookupException e) {
				e.printStackTrace();
			}
			catch (CreateException e) {
				e.printStackTrace();
			}
		}
		if (CT != null)
		{
			CT.setName(sName);
			CT.setText(sText);
			CT.setOrdinal(iOrdinal);
			CT.setLanguage("IS");
			CT.setUseTags(useTags);
			try
			{
				if (bInsert) {
					CT.insert();
				}
				else {
					CT.update();
				}
			}
			catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	public static boolean deleteText(int iTextId)
	{
		if (iTextId > 0)
		{
			try
			{
				((ContractTextHome) IDOLookup.getHome(ContractText.class))
					.findByPrimaryKey(iTextId)
					.delete();
				return true;
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return false;
	}
	public static boolean updateCategoryDescription(int id, String description)
	{
		try
		{
			ContractCategory cat =
				(
					(ContractCategoryHome) IDOLookup.getHome(
						ContractCategory.class)).findByPrimaryKey(new Integer(id));
			cat.setDescription(description);
			cat.store();
			return true;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	
	public static Contract createAndPrintContract(int userID, int iCategoryId){
		IWTimestamp today = IWTimestamp.RightNow();
		IWTimestamp tomorrow = IWTimestamp.RightNow().getNextDay();
		Contract theReturn = createContract(userID,iCategoryId,today,tomorrow,"C",(Map)null);
		ContractWriter.writePDF(((Integer)theReturn.getPrimaryKey()).intValue(),iCategoryId,"ContractX.PDF");
		return theReturn;
	}
	
	public static Contract createContract(int userID, int iCategoryID, IWTimestamp ValFrom, IWTimestamp ValTo, String sStatus, Map map)
	{
			try {
				return ((ContractHome)IDOLookup.getHome(Contract.class)).create(userID,iCategoryID,ValFrom,ValTo,sStatus,map);
			}
			catch (IDOLookupException e) {
				e.printStackTrace();
			}
			
		return null;
	}	
	
	public static Contract createContract(int iCategoryID, IWTimestamp ValFrom, IWTimestamp ValTo, String sStatus, Map map)
	{
		try {
						return ((ContractHome)IDOLookup.getHome(Contract.class)).create(-1,iCategoryID,ValFrom,ValTo,sStatus,map);
					}
					catch (IDOLookupException e) {
						e.printStackTrace();
					}
			
				return null;
	}	
	
	public static Contract createContract(int userID, int iCategoryID, IWTimestamp ValFrom, IWTimestamp ValTo, String sStatus, String text)
	{
		try {
				return ((ContractHome)IDOLookup.getHome(Contract.class)).create(userID,iCategoryID,ValFrom,ValTo,sStatus,text);
			}
			catch (IDOLookupException e) {
				e.printStackTrace();
			}
			
		return null;
	}
	
	public static ContractHome getContractHome() throws IDOLookupException{
			return (ContractHome) IDOLookup.getHome(Contract.class);
		}
	
	public static ContractCategoryHome getContractCategoryHome() throws IDOLookupException{
			return (ContractCategoryHome)IDOLookup.getHome(ContractCategory.class);
		}	
}
	
