/*
 * $Id: HotelType.java,v 1.2 2005/06/21 13:02:10 gimmi Exp $
 * Created on 21.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.service.hotel.data;

import com.idega.data.IDOEntity;


/**
 * 
 *  Last modified: $Date: 2005/06/21 13:02:10 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.2 $
 */
public interface HotelType extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelTypeBMPBean#getLocalizationKey
	 */
	public String getLocalizationKey();

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelTypeBMPBean#getUseRating
	 */
	public boolean getUseRating();

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelTypeBMPBean#setLocalizationKey
	 */
	public void setLocalizationKey(String locKey);

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelTypeBMPBean#setUseRating
	 */
	public void setUseRating(boolean useRating);
}
