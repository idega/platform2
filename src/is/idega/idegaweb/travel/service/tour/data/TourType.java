/*
 * $Id: TourType.java,v 1.2 2005/06/21 18:20:16 gimmi Exp $
 * Created on 21.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.service.tour.data;

import com.idega.data.IDOEntity;


/**
 * 
 *  Last modified: $Date: 2005/06/21 18:20:16 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.2 $
 */
public interface TourType extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourTypeBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourTypeBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourTypeBMPBean#getLocalizationKey
	 */
	public String getLocalizationKey();

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourTypeBMPBean#setLocalizationKey
	 */
	public void setLocalizationKey(String key);

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourTypeBMPBean#getTourCategory
	 */
	public String getTourCategory();

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourTypeBMPBean#setTourCategory
	 */
	public void setTourCategory(String category);
}
