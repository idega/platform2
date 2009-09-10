/*
 * $Id: ContractFinder.java,v 1.10.4.1 2007/01/12 19:32:12 idegaweb Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.contract.business;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.FinderException;


import com.idega.block.contract.data.Contract;
import com.idega.block.contract.data.ContractCategory;
import com.idega.block.contract.data.ContractCategoryHome;
import com.idega.block.contract.data.ContractHome;
import com.idega.block.contract.data.ContractTag;
import com.idega.block.contract.data.ContractText;

import com.idega.core.component.data.ICObjectInstance;
import com.idega.core.component.data.ICObjectInstanceHome;
import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
;
/**

 * Title:        idegaclasses

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:      idega

 * @author <a href="aron@idega.is">Aron Birkir</a>

 * @version 1.0

 */
/**
  * @deprecated The home interfaces, or the service bean should be used instead
  */
public abstract class ContractFinder
{
	
	public static Contract getContract(int id)
	{
		if (id > 0)
		{
			try
			{
				return (
					(ContractHome) IDOLookup.getHome(Contract.class)).findByPrimaryKey(new Integer(id));
			}
			catch (Exception ex)
			{
			}
		}
		return null;
	}
	public static int countContractsInCategory(int iCategoryId)
	{
		try
		{
			return ((ContractHome)IDOLookup.getHome(Contract.class)).getCountByCategory(iCategoryId);
		}
		catch (Exception ex)
		{
		}
		return 0;
	}
	public static int getObjectInstanceCategoryId(int iObjectInstanceId, boolean CreateNew)
	{
		int id = -1;
		try
		{
			ICObjectInstance obj =
				((com.idega.core.component.data.ICObjectInstanceHome) IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(
					iObjectInstanceId);
			id = getObjectInstanceCategoryId(obj);
			if (id <= 0 && CreateNew)
			{
				id = ContractBusiness.createCategory(iObjectInstanceId);
			}
		}
		catch (Exception ex)
		{
		}
		return id;
	}
	public static int getObjectInstanceCategoryId(ICObjectInstance eObjectInstance)
	{
		try
		{
			Collection categories = getContractCategoryHome().findByObjectInstance(eObjectInstance);
			
			if (categories != null)
			{
				return ((Integer)((ContractCategory) categories.iterator().next()).getPrimaryKey()).intValue();
			}
			else {
				return -1;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return -2;
		}
	}
	public static int getObjectInstanceCategoryId(int iObjectInstanceId)
	{
		try
		{
			ICObjectInstance obj =
				((com.idega.core.component.data.ICObjectInstanceHome) IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(
					iObjectInstanceId);
			return getObjectInstanceCategoryId(obj);
		}
		catch (Exception ex)
		{
		}
		return -1;
	}
	public static int getObjectInstanceIdFromCategoryId(int iCategoryId)
	{
		try
		{
			Collection instances = getContractCategoryHome().findByPrimaryKey(new Integer(iCategoryId)).getRelatedObjectInstances();
			
			if (instances != null)
			{
				return ((Integer)((ICObjectInstance) instances.iterator().next()).getPrimaryKey()).intValue();
			}
			else {
				return -1;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return -2;
		}
	}
	public static ContractCategory getContractCategory(int iCategoryId)
	{
		if (iCategoryId > 0)
		{
			try
			{
				return (
					(ContractCategoryHome) IDOLookup.getHome(
						ContractCategory.class)).findByPrimaryKey(new Integer(iCategoryId));
			}
			catch (Exception ex)
			{
			}
		}
		return null;
	}
	
	
	public static Collection listOfStatusContracts(String S, int iCategoryId)
	{
		try
		{
			return ((ContractHome)IDOLookup.getHome(Contract.class)).findAllByCategoryAndStatus(iCategoryId,S);
		}
		catch (Exception e)
		{}
			
		return null;
	}
	
	public static List listOfContractTags(int iCategoryId)
	{
		try
		{
			return EntityFinder.findAllByColumn(
				((com.idega.block.contract.data.ContractTagHome) IDOLookup.getHomeLegacy(ContractTag.class)).create(),
				com.idega.block.contract.data.ContractTagBMPBean.getColumnNameCategoryId(),
				iCategoryId);
		}
		catch (Exception ex)
		{
		}
		return null;
	}
	public static List listOfContractTagsInUse(int iCategoryId)
	{
		try
		{
			EntityFinder.debug = true;
			List L =
				EntityFinder.findAllByColumn(
					((com.idega.block.contract.data.ContractTagHome) IDOLookup.getHomeLegacy(ContractTag.class)).create(),
					com.idega.block.contract.data.ContractTagBMPBean.getColumnNameInUse(),
					"Y",
					com.idega.block.contract.data.ContractTagBMPBean.getColumnNameCategoryId(),
					iCategoryId);
			EntityFinder.debug = false;
						
			return L;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
	public static List listOfContractTagsInList(int iCategoryId)
	{
		try
		{
			return EntityFinder.findAllByColumn(
				((com.idega.block.contract.data.ContractTagHome) IDOLookup.getHomeLegacy(ContractTag.class)).create(),
				com.idega.block.contract.data.ContractTagBMPBean.getColumnNameCategoryId(),
				String.valueOf(iCategoryId),
				com.idega.block.contract.data.ContractTagBMPBean.getColumnNameInList(),
				"Y");
		}
		catch (Exception ex)
		{
		}
		return null;
	}
	public static Collection listOfEntityForObjectInstanceId(int instanceid)
	{
		try
		{
			ICObjectInstance obj =
				((ICObjectInstanceHome) IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKey(new Integer(instanceid));
			return listOfEntityForObjectInstanceId(obj);
		}
		catch (Exception ex)
		{
			return null;
		}
	}
	public static Collection listOfEntityForObjectInstanceId(ICObjectInstance obj)
	{
		try
		{
			return getContractCategoryHome().findByObjectInstance(obj);
			
		}
		catch (Exception ex)
		{
			return null;
		}
	}
	public static Collection listOfContractCategories()
	{
		try
		{
			return getContractCategoryHome().findAll();
		}
		catch (Exception ex)
		{
		}
		return null;
	}
	public static Map mapOfContracts(int iCategoryId)
	{
		try {
			Collection L = getContractHome().findAllByCategory(iCategoryId);
			if (L != null)
			{
				Hashtable H = new Hashtable();
				Iterator iter = L.iterator();
				while(iter.hasNext())
				{
					Contract C = (Contract) iter.next();
					H.put( C.getPrimaryKey(), C);
				}
				return H;
			}
			else {
				return null;
			}
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Map mapOfContractTagsInUse(int iCategoryId)
	{
		List L = listOfContractTagsInUse(iCategoryId);
		if (L != null)
		{
			Hashtable H = new Hashtable(L.size());
			Iterator I = L.iterator();
			while (I.hasNext())
			{
				ContractTag tag = (ContractTag) I.next();
				H.put(new Integer(tag.getID()), tag);
			}
			return H;
		}
		return null;
	}
	public static ContractText getContractText(int id)
	{
		try
		{
			return (
				(com.idega.block.contract.data.ContractTextHome) IDOLookup.getHomeLegacy(
					ContractText.class)).findByPrimaryKeyLegacy(
				id);
		}
		catch (Exception ex)
		{
		}
		return null;
	}
	public static Collection listOfContractFiles(Contract eContract)
	{
		if (eContract != null)
		{
			try
			{
				return getContractHome().findFiles(((Integer)eContract.getPrimaryKey()).intValue());
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return null;
	}
	public static int getContractTextMaxOrdinal()
	{
		try
		{
			return ((com.idega.block.contract.data.ContractTextHome) IDOLookup.getHomeLegacy(ContractText.class))
				.create()
				.getMaxColumnValue(com.idega.block.contract.data.ContractTextBMPBean.getOrdinalColumnName());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return 0;
	}
	public static List listOfContractTexts(int iCategoryId)
	{
		try
		{
			EntityFinder.debug = true;
			List L =
				EntityFinder.findAllByColumnOrdered(
					((com.idega.block.contract.data.ContractTextHome) IDOLookup.getHomeLegacy(ContractText.class)).create(),
					com.idega.block.contract.data.ContractTextBMPBean.getColumnNameCategoryId(),
					iCategoryId,
					com.idega.block.contract.data.ContractTextBMPBean.getOrdinalColumnName());
			EntityFinder.debug = false;
			return L;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
	public static List listOfContractTextsOrdered(int iCategoryId)
	{
		try
		{
			return EntityFinder.findAllByColumnOrdered(
				((com.idega.block.contract.data.ContractTextHome) IDOLookup.getHomeLegacy(ContractText.class)).create(),
				com.idega.block.contract.data.ContractTextBMPBean.getColumnNameCategoryId(),
				iCategoryId,
				com.idega.block.contract.data.ContractTextBMPBean.getOrdinalColumnName());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
	
	public static Collection findContractsByUserId(int userId) {
		try {
			return getContractHome().findAllByUser(userId);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
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
