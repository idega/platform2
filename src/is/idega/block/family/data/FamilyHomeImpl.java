/*
 * $Id: FamilyHomeImpl.java,v 1.1 2004/08/27 16:15:24 joakim Exp $
 * Created on 27.8.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.family.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;


/**
 * 
 *  Last modified: $Date: 2004/08/27 16:15:24 $ by $Author: joakim $
 * 
 * @author <a href="mailto:Joakim@idega.com">Joakim</a>
 * @version $Revision: 1.1 $
 */
public class FamilyHomeImpl extends IDOFactory implements FamilyHome {

	protected Class getEntityInterfaceClass() {
		return Family.class;
	}

	public Family create() throws javax.ejb.CreateException {
		return (Family) super.createIDO();
	}

	public Family findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Family) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((FamilyBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
