/*
 * $Id: WorkReportBusinessHomeImpl.java,v 1.3 2004/11/25 23:38:41 eiki Exp $
 * Created on Nov 25, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.reports.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2004/11/25 23:38:41 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.3 $
 */
public class WorkReportBusinessHomeImpl extends IBOHomeImpl implements WorkReportBusinessHome {

	protected Class getBeanInterfaceClass() {
		return WorkReportBusiness.class;
	}

	public WorkReportBusiness create() throws javax.ejb.CreateException {
		return (WorkReportBusiness) super.createIBO();
	}
}
