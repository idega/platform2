package com.idega.block.trade.stockroom.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;


/**
 * @author gimmi
 */
public class ResellerStaffGroupHomeImpl extends IDOFactory implements ResellerStaffGroupHome {

	protected Class getEntityInterfaceClass() {
		return ResellerStaffGroup.class;
	}

	public ResellerStaffGroup create() throws javax.ejb.CreateException {
		return (ResellerStaffGroup) super.createIDO();
	}

	public ResellerStaffGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (ResellerStaffGroup) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findGroupsByName(String name) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ResellerStaffGroupBMPBean) entity).ejbFindGroupsByName(name);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findGroupsByNameAndDescription(String name, String description) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ResellerStaffGroupBMPBean) entity).ejbFindGroupsByNameAndDescription(name,
				description);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
