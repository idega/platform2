package is.idega.idegaweb.travel.block.search.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;
import com.idega.user.data.Group;


/**
 * @author gimmi
 */
public class ServiceSearchEngineHomeImpl extends IDOFactory implements ServiceSearchEngineHome {

	protected Class getEntityInterfaceClass() {
		return ServiceSearchEngine.class;
	}

	public ServiceSearchEngine create() throws javax.ejb.CreateException {
		return (ServiceSearchEngine) super.createIDO();
	}

	public ServiceSearchEngine findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (ServiceSearchEngine) super.findByPrimaryKeyIDO(pk);
	}

	public ServiceSearchEngine findByName(String name) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ServiceSearchEngineBMPBean) entity).ejbFindByName(name);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public ServiceSearchEngine findByCode(String code) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ServiceSearchEngineBMPBean) entity).ejbFindByCode(code);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ServiceSearchEngineBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAll(Group supplierManager) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ServiceSearchEngineBMPBean) entity).ejbFindAll(supplierManager);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public ServiceSearchEngine findByGroupID(int groupID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ServiceSearchEngineBMPBean) entity).ejbFindByGroupID(groupID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}
