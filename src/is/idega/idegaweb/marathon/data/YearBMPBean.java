/*
 * $Id: YearBMPBean.java,v 1.1 2005/08/01 17:38:19 laddi Exp $
 * Created on May 22, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.data;

import java.sql.Date;
import com.idega.user.data.GroupBMPBean;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2005/08/01 17:38:19 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class YearBMPBean extends GroupBMPBean  implements Year{

	private static final String METADATA_RUN_DATE = "run_date";
	private static final String METADATA_LAST_REGISTRATION_DATE = "last_registration_date";

	public Date getRunDate() {
		String date = this.getMetaData(METADATA_RUN_DATE);
		if (date != null) {
			IWTimestamp stamp = new IWTimestamp(date);
			return stamp.getDate();
		}
		return null;
	}

	public Date getLastRegistrationDate() {
		String date = this.getMetaData(METADATA_LAST_REGISTRATION_DATE);
		if (date != null) {
			IWTimestamp stamp = new IWTimestamp(date);
			return stamp.getDate();
		}
		return null;
	}

	public void setRunDate(Date date) {
		IWTimestamp stamp = new IWTimestamp(date);
		setMetaData(METADATA_RUN_DATE, stamp.toSQLDateString(), "java.sql.Date");
	}

	public void setLastRegistrationDate(Date date) {
		IWTimestamp stamp = new IWTimestamp(date);
		setMetaData(METADATA_LAST_REGISTRATION_DATE, stamp.toSQLDateString(), "java.sql.Date");
	}
}