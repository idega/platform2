package is.idega.idegaweb.travel.block.search.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;


/**
 * @author gimmi
 */
public class ServiceSearchEngineStaffGroupHomeImpl extends IDOFactory implements ServiceSearchEngineStaffGroupHome {

	protected Class getEntityInterfaceClass() {
		return ServiceSearchEngineStaffGroup.class;
	}

	public ServiceSearchEngineStaffGroup create() throws javax.ejb.CreateException {
		return (ServiceSearchEngineStaffGroup) super.createIDO();
	}

	public ServiceSearchEngineStaffGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (ServiceSearchEngineStaffGroup) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findGroupsByName(String name) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ServiceSearchEngineStaffGroupBMPBean) entity).ejbFindGroupsByName(name);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findGroupsByNameAndDescription(String name, String description) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ServiceSearchEngineStaffGroupBMPBean) entity).ejbFindGroupsByNameAndDescription(
				name, description);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
