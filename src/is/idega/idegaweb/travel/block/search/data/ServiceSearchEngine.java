package is.idega.idegaweb.travel.block.search.data;

import java.util.Collection;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.Group;


/**
 * @author gimmi
 */
public interface ServiceSearchEngine extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#setCode
	 */
	public void setCode(String code);

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#setStaffGroupID
	 */
	public void setStaffGroupID(int groupID);

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getStaffGroupID
	 */
	public int getStaffGroupID();

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getCode
	 */
	public String getCode();

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getIsValid
	 */
	public boolean getIsValid();

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#addSupplier
	 */
	public void addSupplier(Supplier supplier) throws IDOAddRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#removeSupplier
	 */
	public void removeSupplier(Supplier supplier) throws IDORemoveRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#removeAllSuppliers
	 */
	public void removeAllSuppliers() throws IDORemoveRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getSupplierManagerID
	 */
	public int getSupplierManagerID();

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getSupplierManager
	 */
	public Group getSupplierManager();

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#setSupplierManager
	 */
	public void setSupplierManager(Group group);

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#setSupplierManagerPK
	 */
	public void setSupplierManagerPK(Object pk);

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#setURL
	 */
	public void setURL(String URL);

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getURL
	 */
	public String getURL();

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getCountries
	 */
	public Collection getCountries() throws IDORelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#setCountries
	 */
	public void setCountries(Collection countries) throws IDORemoveRelationshipException, IDOAddRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#getSuppliers
	 */
	public Collection getSuppliers() throws IDORelationshipException;
}
