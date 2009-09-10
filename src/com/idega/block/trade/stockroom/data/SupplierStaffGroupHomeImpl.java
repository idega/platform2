package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;


/**
 * @author gimmi
 */
public class SupplierStaffGroupHomeImpl extends IDOFactory implements SupplierStaffGroupHome {

	protected Class getEntityInterfaceClass() {
		return SupplierStaffGroup.class;
	}

	public SupplierStaffGroup create() throws javax.ejb.CreateException {
		return (SupplierStaffGroup) super.createIDO();
	}

	public SupplierStaffGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (SupplierStaffGroup) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findGroupsByName(String name) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SupplierStaffGroupBMPBean) entity).ejbFindGroupsByName(name);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findGroupsByNameAndDescription(String name, String description) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SupplierStaffGroupBMPBean) entity).ejbFindGroupsByNameAndDescription(name,
				description);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
