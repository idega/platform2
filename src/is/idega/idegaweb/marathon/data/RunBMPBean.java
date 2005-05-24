/*
 * $Id: RunBMPBean.java,v 1.27 2005/05/24 12:06:29 laddi Exp $
 * Created on May 22, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.data;

import com.idega.user.data.GroupBMPBean;


/**
 * Last modified: $Date: 2005/05/24 12:06:29 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.27 $
 */
public class RunBMPBean extends GroupBMPBean  implements Run{

	private static final String METADATA_FAMILY_DISCOUNT = "family_discount";

	public float getFamilyDiscount() {
		String discount = this.getMetaData(METADATA_FAMILY_DISCOUNT);
		if (discount != null) {
			return Float.parseFloat(discount);
		}
		return 0;
	}

	public void setFamilyDiscount(float discount) {
		setMetaData(METADATA_FAMILY_DISCOUNT, String.valueOf(discount), "java.lang.Float");
	}
}