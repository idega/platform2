/*
 * $Id: ContractFinder.java,v 1.8 2003/05/21 13:56:36 roar Exp $
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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idega.block.contract.data.Contract;
import com.idega.block.contract.data.ContractCategory;
import com.idega.block.contract.data.ContractTag;
import com.idega.block.contract.data.ContractText;
import com.idega.core.data.ICFile;
import com.idega.core.data.ICObjectInstance;
import com.idega.data.EntityFinder;
/**

 * Title:        idegaclasses

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:      idega

 * @author <a href="aron@idega.is">Aron Birkir</a>

 * @version 1.0

 */
public abstract class ContractFinder
{
	public static List listOfContractFiles(int iContractId)
	{
		try
		{
			return EntityFinder.findRelated(
				((com.idega.block.contract.data.ContractHome) com.idega.data.IDOLookup.getHomeLegacy(Contract.class)).findByPrimaryKeyLegacy(
					iContractId),
				((com.idega.core.data.ICFileHome) com.idega.data.IDOLookup.getHomeLegacy(com.idega.core.data.ICFile.class)).createLegacy());
		}
		catch (Exception ex)
		{
		}
		return null;
	}
	public static Contract getContract(int id)
	{
		if (id > 0)
		{
			try
			{
				return (
					(com.idega.block.contract.data.ContractHome) com.idega.data.IDOLookup.getHomeLegacy(Contract.class)).findByPrimaryKeyLegacy(
					id);
			}
			catch (SQLException ex)
			{
			}
		}
		return null;
	}
	public static int countContractsInCategory(int iCategoryId)
	{
		try
		{
			Contract eContract = (Contract) com.idega.block.contract.data.ContractBMPBean.getStaticInstance(Contract.class);
			return eContract.getNumberOfRecords(
				com.idega.block.contract.data.ContractBMPBean.getColumnNameCategoryId(),
				String.valueOf(iCategoryId));
		}
		catch (SQLException ex)
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
				((com.idega.core.data.ICObjectInstanceHome) com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(
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
			List L =
				EntityFinder.findRelated(
					eObjectInstance,
					((com.idega.block.contract.data.ContractCategoryHome) com.idega.data.IDOLookup.getHomeLegacy(ContractCategory.class))
						.createLegacy());
			if (L != null)
			{
				return ((ContractCategory) L.get(0)).getID();
			}
			else
				return -1;
		}
		catch (SQLException ex)
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
				((com.idega.core.data.ICObjectInstanceHome) com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(
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
			ContractCategory nw =
				(
					(com.idega.block.contract.data.ContractCategoryHome) com.idega.data.IDOLookup.getHomeLegacy(
						ContractCategory.class)).findByPrimaryKeyLegacy(
					iCategoryId);
			List L =
				EntityFinder.findRelated(
					nw,
					((com.idega.core.data.ICObjectInstanceHome) com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).createLegacy());
			if (L != null)
			{
				return ((ICObjectInstance) L.get(0)).getID();
			}
			else
				return -1;
		}
		catch (SQLException ex)
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
					(com.idega.block.contract.data.ContractCategoryHome) com.idega.data.IDOLookup.getHomeLegacy(
						ContractCategory.class)).findByPrimaryKeyLegacy(
					iCategoryId);
			}
			catch (SQLException ex)
			{
			}
		}
		return null;
	}
	public static List listOfContracts(int iCategoryId)
	{
		try
		{
			return (
				EntityFinder.findAllByColumn(
					((com.idega.block.contract.data.ContractHome) com.idega.data.IDOLookup.getHomeLegacy(Contract.class)).createLegacy(),
					com.idega.block.contract.data.ContractCategoryBMPBean.getEntityTableName(),
					iCategoryId));
		}
		catch (SQLException e)
		{
			return (null);
		}
	}
	public static List listOfStatusContracts(String S)
	{
		try
		{
			return (
				EntityFinder.findAllByColumn(
					((com.idega.block.contract.data.ContractHome) com.idega.data.IDOLookup.getHomeLegacy(Contract.class)).createLegacy(),
					com.idega.block.contract.data.ContractBMPBean.getColumnNameStatus(),
					S));
		}
		catch (SQLException e)
		{
			return (null);
		}
	}
	public static List listOfStatusContracts(String S, int iCategoryId)
	{
		try
		{
			return (
				EntityFinder.findAllByColumn(
					((com.idega.block.contract.data.ContractHome) com.idega.data.IDOLookup.getHomeLegacy(Contract.class)).createLegacy(),
					com.idega.block.contract.data.ContractBMPBean.getColumnNameStatus(),
					S,
					com.idega.block.contract.data.ContractBMPBean.getColumnNameCategoryId(),
					iCategoryId));
		}
		catch (SQLException e)
		{
			return (null);
		}
	}
	public static List listOfStatusContracts(String S, String s)
	{
		try
		{
			return (
				EntityFinder.findAllByColumn(
					((com.idega.block.contract.data.ContractHome) com.idega.data.IDOLookup.getHomeLegacy(Contract.class)).createLegacy(),
					com.idega.block.contract.data.ContractBMPBean.getColumnNameStatus(),
					S));
		}
		catch (SQLException e)
		{
			return (null);
		}
	}
	public static List listOfContractTags(int iCategoryId)
	{
		try
		{
			return EntityFinder.findAllByColumn(
				((com.idega.block.contract.data.ContractTagHome) com.idega.data.IDOLookup.getHomeLegacy(ContractTag.class)).createLegacy(),
				com.idega.block.contract.data.ContractTagBMPBean.getColumnNameCategoryId(),
				iCategoryId);
		}
		catch (SQLException ex)
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
					((com.idega.block.contract.data.ContractTagHome) com.idega.data.IDOLookup.getHomeLegacy(ContractTag.class)).createLegacy(),
					com.idega.block.contract.data.ContractTagBMPBean.getColumnNameInUse(),
					"Y",
					com.idega.block.contract.data.ContractTagBMPBean.getColumnNameCategoryId(),
					iCategoryId);
			EntityFinder.debug = false;
						
			return L;
		}
		catch (SQLException ex)
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
				((com.idega.block.contract.data.ContractTagHome) com.idega.data.IDOLookup.getHomeLegacy(ContractTag.class)).createLegacy(),
				com.idega.block.contract.data.ContractTagBMPBean.getColumnNameCategoryId(),
				String.valueOf(iCategoryId),
				com.idega.block.contract.data.ContractTagBMPBean.getColumnNameInList(),
				"Y");
		}
		catch (SQLException ex)
		{
		}
		return null;
	}
	public static List listOfEntityForObjectInstanceId(int instanceid)
	{
		try
		{
			ICObjectInstance obj =
				((com.idega.core.data.ICObjectInstanceHome) com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(
					instanceid);
			return listOfEntityForObjectInstanceId(obj);
		}
		catch (SQLException ex)
		{
			return null;
		}
	}
	public static List listOfEntityForObjectInstanceId(ICObjectInstance obj)
	{
		try
		{
			List L =
				EntityFinder.findRelated(
					obj,
					((com.idega.block.contract.data.ContractCategoryHome) com.idega.data.IDOLookup.getHomeLegacy(ContractCategory.class))
						.createLegacy());
			return L;
		}
		catch (SQLException ex)
		{
			return null;
		}
	}
	public static List listOfContractCategories()
	{
		try
		{
			return EntityFinder.findAll(
				((com.idega.block.contract.data.ContractCategoryHome) com.idega.data.IDOLookup.getHomeLegacy(ContractCategory.class))
					.createLegacy());
		}
		catch (SQLException ex)
		{
		}
		return null;
	}
	public static Map mapOfContracts(int iCategoryId)
	{
		List L = listOfContracts(iCategoryId);
		if (L != null)
		{
			Hashtable H = new Hashtable();
			int len = L.size();
			for (int i = 0; i < len; i++)
			{
				Contract C = (Contract) L.get(i);
				H.put(new Integer(C.getID()), C);
			}
			return H;
		}
		else
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
				(com.idega.block.contract.data.ContractTextHome) com.idega.data.IDOLookup.getHomeLegacy(
					ContractText.class)).findByPrimaryKeyLegacy(
				id);
		}
		catch (SQLException ex)
		{
		}
		return null;
	}
	public static List listOfContractFiles(Contract eContract)
	{
		if (eContract != null)
		{
			try
			{
				return EntityFinder.findRelated(
					eContract,
					((com.idega.core.data.ICFileHome) com.idega.data.IDOLookup.getHomeLegacy(ICFile.class)).createLegacy());
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
			return ((com.idega.block.contract.data.ContractTextHome) com.idega.data.IDOLookup.getHomeLegacy(ContractText.class))
				.createLegacy()
				.getMaxColumnValue(com.idega.block.contract.data.ContractTextBMPBean.getOrdinalColumnName());
		}
		catch (SQLException ex)
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
					((com.idega.block.contract.data.ContractTextHome) com.idega.data.IDOLookup.getHomeLegacy(ContractText.class)).createLegacy(),
					com.idega.block.contract.data.ContractTextBMPBean.getColumnNameCategoryId(),
					iCategoryId,
					com.idega.block.contract.data.ContractTextBMPBean.getOrdinalColumnName());
			EntityFinder.debug = false;
			return L;
		}
		catch (SQLException ex)
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
				((com.idega.block.contract.data.ContractTextHome) com.idega.data.IDOLookup.getHomeLegacy(ContractText.class)).createLegacy(),
				com.idega.block.contract.data.ContractTextBMPBean.getColumnNameCategoryId(),
				iCategoryId,
				com.idega.block.contract.data.ContractTextBMPBean.getOrdinalColumnName());
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
	
	public static Collection findContractsByUserId(int userId) {
		try {
			System.out.println("findContractsByUserId");
			return EntityFinder.findAllByColumnOrdered(
				((com.idega.block.contract.data.ContractHome) com.idega.data.IDOLookup.getHomeLegacy(Contract.class)).createLegacy(),
				com.idega.block.contract.data.ContractBMPBean.getColumnNameUserId(),
				userId,
				com.idega.block.contract.data.ContractBMPBean.getColumnNameValidFrom());
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		return null;			
	}
}
