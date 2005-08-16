/*
 * $Id: Year.java,v 1.2 2005/08/16 14:09:36 laddi Exp $
 * Created on Jul 31, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.data;

import java.sql.Timestamp;
import com.idega.user.data.Group;


/**
 * Last modified: $Date: 2005/08/16 14:09:36 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface Year extends Group {

	/**
	 * @see is.idega.idegaweb.marathon.data.YearBMPBean#getRunDate
	 */
	public Timestamp getRunDate();

	/**
	 * @see is.idega.idegaweb.marathon.data.YearBMPBean#getLastRegistrationDate
	 */
	public Timestamp getLastRegistrationDate();

	/**
	 * @see is.idega.idegaweb.marathon.data.YearBMPBean#setRunDate
	 */
	public void setRunDate(Timestamp date);

	/**
	 * @see is.idega.idegaweb.marathon.data.YearBMPBean#setLastRegistrationDate
	 */
	public void setLastRegistrationDate(Timestamp date);
}
