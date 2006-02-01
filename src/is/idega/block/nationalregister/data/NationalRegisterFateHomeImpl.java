/**
 * 
 */
package is.idega.block.nationalregister.data;


import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class NationalRegisterFateHomeImpl extends IDOFactory implements
		NationalRegisterFateHome {
	protected Class getEntityInterfaceClass() {
		return NationalRegisterFate.class;
	}

	public NationalRegisterFate create() throws javax.ejb.CreateException {
		return (NationalRegisterFate) super.createIDO();
	}

	public NationalRegisterFate findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (NationalRegisterFate) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((NationalRegisterFateBMPBean) entity)
				.ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public NationalRegisterFate findByFateCode(String fateCode)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((NationalRegisterFateBMPBean) entity)
				.ejbFindByFateCode(fateCode);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

}
