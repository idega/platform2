/*
 * $Id: TourTypeHomeImpl.java,v 1.2 2005/06/21 18:20:16 gimmi Exp $
 * Created on 21.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.service.tour.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOFactory;
import com.idega.data.IDORelationshipException;


/**
 * 
 *  Last modified: $Date: 2005/06/21 18:20:16 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.2 $
 */
public class TourTypeHomeImpl extends IDOFactory implements TourTypeHome {

	protected Class getEntityInterfaceClass() {
		return TourType.class;
	}

	public TourType create() throws javax.ejb.CreateException {
		return (TourType) super.createIDO();
	}

	public TourType findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (TourType) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findByCategory(String category) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((TourTypeBMPBean) entity).ejbFindByCategory(category);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((TourTypeBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByCategoryUsedBySuppliers(String category, Collection suppliers)
			throws IDOCompositePrimaryKeyException, IDORelationshipException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((TourTypeBMPBean) entity).ejbFindByCategoryUsedBySuppliers(category, suppliers);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
