/*
 * $Id: Distance.java,v 1.1 2005/05/24 12:06:29 laddi Exp $
 * Created on May 23, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.data;

import java.util.Locale;
import com.idega.user.data.Group;


/**
 * Last modified: $Date: 2005/05/24 12:06:29 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface Distance extends Group {

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#isUseChip
	 */
	public boolean isUseChip();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setUseChip
	 */
	public void setUseChip(boolean useChip);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#getPrice
	 */
	public float getPrice(Locale locale);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setPriceInISK
	 */
	public void setPriceInISK(float price);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setPriceInEUR
	 */
	public void setPriceInEUR(float price);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#getChildrenPrice
	 */
	public float getChildrenPrice(Locale locale);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setChildrenPriceInISK
	 */
	public void setChildrenPriceInISK(float price);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setChildrenPriceInEUR
	 */
	public void setChildrenPriceInEUR(float price);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#isFamilyDiscount
	 */
	public boolean isFamilyDiscount();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setFamilyDiscount
	 */
	public void setFamilyDiscount(boolean discount);
}
