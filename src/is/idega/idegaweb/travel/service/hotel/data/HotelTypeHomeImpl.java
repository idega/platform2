/*
 * $Id: HotelTypeHomeImpl.java,v 1.2 2005/06/21 13:02:10 gimmi Exp $
 * Created on 21.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.service.hotel.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOFactory;
import com.idega.data.IDORelationshipException;


/**
 * 
 *  Last modified: $Date: 2005/06/21 13:02:10 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.2 $
 */
public class HotelTypeHomeImpl extends IDOFactory implements HotelTypeHome {

	protected Class getEntityInterfaceClass() {
		return HotelType.class;
	}

	public HotelType create() throws javax.ejb.CreateException {
		return (HotelType) super.createIDO();
	}

	public HotelType findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (HotelType) super.findByPrimaryKeyIDO(pk);
	}

	public HotelType findByLocalizationKey(String locKey) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((HotelTypeBMPBean) entity).ejbFindByLocalizationKey(locKey);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((HotelTypeBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllUsedBySuppliers(Collection suppliers) throws IDOCompositePrimaryKeyException,
			IDORelationshipException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((HotelTypeBMPBean) entity).ejbFindAllUsedBySuppliers(suppliers);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
