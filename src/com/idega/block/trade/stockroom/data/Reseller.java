package com.idega.block.trade.stockroom.data;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.core.contact.data.Email;
import com.idega.core.location.data.Address;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.TreeableEntity;
import com.idega.user.data.Group;


/**
 * @author gimmi
 */
public interface Reseller extends IDOLegacyEntity, TreeableEntity {

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getDescription
	 */
	public String getDescription();

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#setDescription
	 */
	public void setDescription(String description);

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#setGroupId
	 */
	public void setGroupId(int id);

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getGroupId
	 */
	public int getGroupId();

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getPhones
	 */
	public List getPhones() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getPhones
	 */
	public List getPhones(int PhoneTypeId) throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getEmails
	 */
	public List getEmails() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getEmail
	 */
	public Email getEmail() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#setIsValid
	 */
	public void setIsValid(boolean isValid);

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getIsValid
	 */
	public boolean getIsValid();

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getReferenceNumber
	 */
	public String getReferenceNumber();

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#setReferenceNumber
	 */
	public void setReferenceNumber(String key);

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getAddress
	 */
	public Address getAddress() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getAddresses
	 */
	public List getAddresses() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getHomePhone
	 */
	public List getHomePhone() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getFaxPhone
	 */
	public List getFaxPhone() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getParent
	 */
	public Reseller getParent();

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getSupplierManagerID
	 */
	public int getSupplierManagerID();

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getSupplierManager
	 */
	public Group getSupplierManager();

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#setSupplierManager
	 */
	public void setSupplierManager(Group group);

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#setSupplierManagerPK
	 */
	public void setSupplierManagerPK(Object pk);

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getSettings
	 */
	public Settings getSettings() throws FinderException, RemoteException, CreateException;

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#getOrganizationID
	 */
	public String getOrganizationID();

	/**
	 * @see com.idega.block.trade.stockroom.data.ResellerBMPBean#setOrganizationID
	 */
	public void setOrganizationID(String organizationId);
}
