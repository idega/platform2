package is.idega.idegaweb.travel.block.search.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;


/**
 * @author gimmi
 */
public interface ServiceSearchEngineStaffGroupHome extends IDOHome {

	public ServiceSearchEngineStaffGroup create() throws javax.ejb.CreateException;

	public ServiceSearchEngineStaffGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineStaffGroupBMPBean#ejbFindGroupsByName
	 */
	public Collection findGroupsByName(String name) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineStaffGroupBMPBean#ejbFindGroupsByNameAndDescription
	 */
	public Collection findGroupsByNameAndDescription(String name, String description) throws FinderException;
}
