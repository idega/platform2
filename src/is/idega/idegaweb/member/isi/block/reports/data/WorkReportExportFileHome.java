/*
 * $Id: WorkReportExportFileHome.java,v 1.3 2004/11/27 19:42:01 eiki Exp $
 * Created on Nov 27, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;


/**
 * 
 *  Last modified: $Date: 2004/11/27 19:42:01 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.3 $
 */
public interface WorkReportExportFileHome extends IDOHome {

	public WorkReportExportFile create() throws javax.ejb.CreateException;

	public WorkReportExportFile findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileBMPBean#ejbFindWorkReportExportFileByUnionIdAndYear
	 */
	public Collection findWorkReportExportFileByUnionIdAndYear(int unionId, int year) throws FinderException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileBMPBean#ejbFindWorkReportExportFileByClubIdAndYear
	 */
	public WorkReportExportFile findWorkReportExportFileByClubIdAndYear(int clubId, int year) throws FinderException;
}
