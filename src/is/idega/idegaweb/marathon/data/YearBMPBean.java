/*
 * $Id: YearBMPBean.java,v 1.2 2005/08/16 14:09:36 laddi Exp $
 * Created on May 22, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.data;

import java.sql.Timestamp;
import com.idega.user.data.GroupBMPBean;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2005/08/16 14:09:36 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class YearBMPBean extends GroupBMPBean  implements Year{

	private static final String METADATA_RUN_DATE = "run_date";
	private static final String METADATA_LAST_REGISTRATION_DATE = "last_registration_date";

	public Timestamp getRunDate() {
		String date = getMetaData(METADATA_RUN_DATE);
		if (date != null) {
			IWTimestamp stamp = new IWTimestamp(date);
			return stamp.getTimestamp();
		}
		return null;
	}

	public Timestamp getLastRegistrationDate() {
		String date = getMetaData(METADATA_LAST_REGISTRATION_DATE);
		if (date != null) {
			IWTimestamp stamp = new IWTimestamp(date);
			return stamp.getTimestamp();
		}
		return null;
	}

	public void setRunDate(Timestamp date) {
		IWTimestamp stamp = new IWTimestamp(date);
		setMetaData(METADATA_RUN_DATE, stamp.toSQLDateString(), "java.sql.Timestamp");
	}

	public void setLastRegistrationDate(Timestamp date) {
		IWTimestamp stamp = new IWTimestamp(date);
		setMetaData(METADATA_LAST_REGISTRATION_DATE, stamp.toSQLDateString(), "java.sql.Timestamp");
	}
}