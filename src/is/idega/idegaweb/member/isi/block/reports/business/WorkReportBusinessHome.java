/*
 * $Id: WorkReportBusinessHome.java,v 1.2 2004/09/07 23:04:37 eiki Exp $
 * Created on Sep 7, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.reports.business;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2004/09/07 23:04:37 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.2 $
 */
public interface WorkReportBusinessHome extends IBOHome {

	public WorkReportBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
