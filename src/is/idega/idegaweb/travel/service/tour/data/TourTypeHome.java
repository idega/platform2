/*
 * $Id: TourTypeHome.java,v 1.2 2005/06/21 18:20:16 gimmi Exp $
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
import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;


/**
 * 
 *  Last modified: $Date: 2005/06/21 18:20:16 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.2 $
 */
public interface TourTypeHome extends IDOHome {

	public TourType create() throws javax.ejb.CreateException;

	public TourType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourTypeBMPBean#ejbFindByCategory
	 */
	public Collection findByCategory(String category) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourTypeBMPBean#ejbFindAll
	 */
	public Collection findAll() throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourTypeBMPBean#ejbFindByCategoryUsedBySuppliers
	 */
	public Collection findByCategoryUsedBySuppliers(String category, Collection suppliers)
			throws IDOCompositePrimaryKeyException, IDORelationshipException, FinderException;
}
