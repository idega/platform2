/*
 * $Id: AfterSchoolCareDaysHome.java,v 1.1 2005/08/09 16:35:19 laddi Exp $
 * Created on Aug 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.data;

import java.util.Collection;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import com.idega.data.IDOHome;


/**
 * Last modified: $Date: 2005/08/09 16:35:19 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface AfterSchoolCareDaysHome extends IDOHome {

	public AfterSchoolCareDays create() throws javax.ejb.CreateException;

	public AfterSchoolCareDays findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.data.AfterSchoolCareDaysBMPBean#ejbFindAllByApplication
	 */
	public Collection findAllByApplication(ChildCareApplication application) throws FinderException;
}
