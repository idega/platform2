/*
 * $Id: ExportBusinessHome.java,v 1.2 2005/10/13 08:09:38 palli Exp $
 * Created on Oct 11, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.export.business;




import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2005/10/13 08:09:38 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.2 $
 */
public interface ExportBusinessHome extends IBOHome {

	public ExportBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}
