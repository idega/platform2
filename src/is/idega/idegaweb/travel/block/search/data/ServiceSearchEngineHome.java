package is.idega.idegaweb.travel.block.search.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.user.data.Group;


/**
 * @author gimmi
 */
public interface ServiceSearchEngineHome extends IDOHome {

	public ServiceSearchEngine create() throws javax.ejb.CreateException;

	public ServiceSearchEngine findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#ejbFindByName
	 */
	public ServiceSearchEngine findByName(String name) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#ejbFindByCode
	 */
	public ServiceSearchEngine findByCode(String code) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#ejbFindAll
	 */
	public Collection findAll(Group supplierManager) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineBMPBean#ejbFindByGroupID
	 */
	public ServiceSearchEngine findByGroupID(int groupID) throws FinderException;
}
