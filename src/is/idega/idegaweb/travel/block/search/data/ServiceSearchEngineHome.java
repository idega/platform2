package is.idega.idegaweb.travel.block.search.data;

import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.core.location.data.Country;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.Order;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
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
