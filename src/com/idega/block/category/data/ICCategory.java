package com.idega.block.category.data;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Locale;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.MetaDataCapable;
import com.idega.data.TreeableEntity;


/**
 * @author gimmi
 */
public interface ICCategory extends TreeableEntity, IDOLegacyEntity, Category, MetaDataCapable {

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#getBusinessId
	 */
	public int getBusinessId();

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#setBusinessId
	 */
	public void setBusinessId(int id);

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#getParentId
	 */
	public int getParentId();

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#setParentId
	 */
	public void setParentId(int id);

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#getLocaleId
	 */
	public int getLocaleId();

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#setLocaleId
	 */
	public void setLocaleId(int id);

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#getDescription
	 */
	public String getDescription();

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#setDescription
	 */
	public void setDescription(String description);

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#getValid
	 */
	public boolean getValid();

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#setValid
	 */
	public void setValid(boolean valid);

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#getCreated
	 */
	public java.sql.Timestamp getCreated();

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#setCreated
	 */
	public void setCreated(java.sql.Timestamp created);

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#getInvalidationDate
	 */
	public java.sql.Timestamp getInvalidationDate();

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#setInvalidationDate
	 */
	public void setInvalidationDate(java.sql.Timestamp date);

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#getType
	 */
	public String getType();

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#setType
	 */
	public void setType(String type);

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#getCategoryType
	 */
	public String getCategoryType();

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#getName
	 */
	public String getName(Locale locale);

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#getDescription
	 */
	public String getDescription(Locale locale);

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#getCategoryTranslation
	 */
	public ICCategoryTranslation getCategoryTranslation(Locale locale) throws RemoteException;

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#setOwnerGroupId
	 */
	public void setOwnerGroupId(int ownerGroupId);

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#getOwnerGroupId
	 */
	public int getOwnerGroupId();

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#addFile
	 */
	public void addFile(ICFile file) throws IDOAddRelationshipException;

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#removeFile
	 */
	public void removeFile(ICFile file) throws IDORemoveRelationshipException;

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#getFiles
	 */
	public Collection getFiles() throws IDORelationshipException;
}
