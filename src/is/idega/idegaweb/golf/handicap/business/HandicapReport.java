/*
 * $Id: HandicapReport.java,v 1.1 2005/02/07 11:20:28 laddi Exp $
 * Created on 7.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.handicap.business;

import is.idega.idegaweb.golf.entity.Union;

import java.sql.Date;


import com.idega.block.datareport.util.ReportableCollection;
import com.idega.business.IBOSession;


/**
 * Last modified: $Date: 2005/02/07 11:20:28 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface HandicapReport extends IBOSession {

	/**
	 * @see is.idega.idegaweb.golf.handicap.business.HandicapReportBean#getClubReport
	 */
	public ReportableCollection getClubReport(Union union, String gender, Integer yearFrom, Integer yearTo, Float handicapFrom, Float handicapTo) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.golf.handicap.business.HandicapReportBean#getGolferReport
	 */
	public ReportableCollection getGolferReport(String personalID, Date dateFrom, Date dateTo) throws java.rmi.RemoteException;

}
