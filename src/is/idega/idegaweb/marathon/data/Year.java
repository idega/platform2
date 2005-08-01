/*
 * $Id: Year.java,v 1.1 2005/08/01 17:38:19 laddi Exp $
 * Created on Jul 31, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.data;

import java.sql.Date;
import com.idega.user.data.Group;


/**
 * Last modified: $Date: 2005/08/01 17:38:19 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface Year extends Group {

	/**
	 * @see is.idega.idegaweb.marathon.data.YearBMPBean#getRunDate
	 */
	public Date getRunDate();

	/**
	 * @see is.idega.idegaweb.marathon.data.YearBMPBean#getLastRegistrationDate
	 */
	public Date getLastRegistrationDate();

	/**
	 * @see is.idega.idegaweb.marathon.data.YearBMPBean#setRunDate
	 */
	public void setRunDate(Date date);

	/**
	 * @see is.idega.idegaweb.marathon.data.YearBMPBean#setLastRegistrationDate
	 */
	public void setLastRegistrationDate(Date date);
}
