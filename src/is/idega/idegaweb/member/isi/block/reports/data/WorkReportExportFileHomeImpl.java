/*
 * $Id: WorkReportExportFileHomeImpl.java,v 1.3 2004/11/27 19:42:01 eiki Exp $
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
import com.idega.data.IDOFactory;


/**
 * 
 *  Last modified: $Date: 2004/11/27 19:42:01 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.3 $
 */
public class WorkReportExportFileHomeImpl extends IDOFactory implements WorkReportExportFileHome {

	protected Class getEntityInterfaceClass() {
		return WorkReportExportFile.class;
	}

	public WorkReportExportFile create() throws javax.ejb.CreateException {
		return (WorkReportExportFile) super.createIDO();
	}

	public WorkReportExportFile findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (WorkReportExportFile) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findWorkReportExportFileByUnionIdAndYear(int unionId, int year) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((WorkReportExportFileBMPBean) entity).ejbFindWorkReportExportFileByUnionIdAndYear(
				unionId, year);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public WorkReportExportFile findWorkReportExportFileByClubIdAndYear(int clubId, int year) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((WorkReportExportFileBMPBean) entity).ejbFindWorkReportExportFileByClubIdAndYear(clubId, year);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}
