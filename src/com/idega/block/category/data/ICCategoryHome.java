package com.idega.block.category.data;

import java.util.Collection;
import java.util.List;
import javax.ejb.FinderException;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.data.IDOHome;


/**
 * @author gimmi
 */
public interface ICCategoryHome extends IDOHome {

	public ICCategory create() throws javax.ejb.CreateException;

	public ICCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	public ICCategory findByPrimaryKey(int id) throws javax.ejb.FinderException;

	public ICCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#ejbHomeGetListOfCategoryForObjectInstance
	 */
	public List getListOfCategoryForObjectInstance(ICObjectInstance obj, boolean order) throws FinderException;

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#ejbHomeGetOrderNumber
	 */
	public int getOrderNumber(Category category, ICObjectInstance instance) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#ejbHomeGetOrderNumber
	 */
	public int getOrderNumber(Category category, String objectInstanceId) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#ejbHomeSetOrderNumber
	 */
	public boolean setOrderNumber(Category category, ICObjectInstance instance, int orderNumber)
			throws com.idega.data.IDOException;

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#ejbFindRootsByType
	 */
	public Collection findRootsByType(String type) throws FinderException;

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#ejbFindAllByObjectInstance
	 */
	public Collection findAllByObjectInstance(int iObjectInstanceID) throws FinderException;

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#ejbFindAllByObjectInstance
	 */
	public Collection findAllByObjectInstance(ICObjectInstance instance) throws FinderException;

	/**
	 * @see com.idega.block.category.data.ICCategoryBMPBean#ejbHomeFindAll
	 */
	public Collection findAll() throws FinderException;
}
