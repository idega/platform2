package com.idega.block.trade.stockroom.data;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import com.idega.block.trade.data.CreditCardInformation;
import com.idega.core.contact.data.Email;
import com.idega.core.location.data.Address;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.Group;


/**
 * @author gimmi
 */
public interface Supplier extends IDOLegacyEntity {

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getDescription
	 */
	public String getDescription();

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#setDescription
	 */
	public void setDescription(String description);

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#setGroupId
	 */
	public void setGroupId(int id);

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getGroupId
	 */
	public int getGroupId();

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#setIsValid
	 */
	public void setIsValid(boolean isValid);

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getIsValid
	 */
	public boolean getIsValid();

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getSupplierManagerID
	 */
	public int getSupplierManagerID();

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getSupplierManager
	 */
	public Group getSupplierManager();

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#setSupplierManager
	 */
	public void setSupplierManager(Group group);

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#setSupplierManagerPK
	 */
	public void setSupplierManagerPK(Object pk);

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getAddress
	 */
	public Address getAddress() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getAddresses
	 */
	public List getAddresses() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getPhones
	 */
	public List getPhones() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getHomePhone
	 */
	public List getHomePhone() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getFaxPhone
	 */
	public List getFaxPhone() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getWorkPhone
	 */
	public List getWorkPhone() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getMobilePhone
	 */
	public List getMobilePhone() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getPhones
	 */
	public List getPhones(int PhoneTypeId) throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getEmail
	 */
	public Email getEmail() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getEmails
	 */
	public List getEmails() throws SQLException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getTPosMerchantId
	 */
	public int getTPosMerchantId();

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getSettings
	 */
	public Settings getSettings() throws FinderException, RemoteException, CreateException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#setCreditCardInformation
	 */
	public void setCreditCardInformation(Collection pks) throws IDORemoveRelationshipException,
			IDOAddRelationshipException, EJBException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#addCreditCardInformationPK
	 */
	public void addCreditCardInformationPK(Object pk) throws IDOAddRelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#addCreditCardInformation
	 */
	public void addCreditCardInformation(CreditCardInformation info) throws IDOAddRelationshipException, EJBException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getCreditCardInformation
	 */
	public Collection getCreditCardInformation() throws IDORelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getProductCategories
	 */
	public Collection getProductCategories() throws IDORelationshipException;

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#getOrganizationID
	 */
	public String getOrganizationID();

	/**
	 * @see com.idega.block.trade.stockroom.data.SupplierBMPBean#setOrganizationID
	 */
	public void setOrganizationID(String organizationId);
}
