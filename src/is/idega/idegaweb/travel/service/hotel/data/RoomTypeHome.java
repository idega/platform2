/*
 * $Id: RoomTypeHome.java,v 1.2 2005/06/21 13:02:10 gimmi Exp $
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
import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;


/**
 * 
 *  Last modified: $Date: 2005/06/21 13:02:10 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.2 $
 */
public interface RoomTypeHome extends IDOHome {

	public RoomType create() throws javax.ejb.CreateException;

	public RoomType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.RoomTypeBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.RoomTypeBMPBean#ejbFindAllUsedBySuppliers
	 */
	public Collection findAllUsedBySuppliers(Collection suppliers) throws IDOCompositePrimaryKeyException,
			IDORelationshipException, FinderException;
}
