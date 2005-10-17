/*
 * $Id: ProviderBusinessHome.java,v 1.3 2005/10/17 09:53:08 palli Exp $
 * Created on Oct 17, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.school.business;




import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2005/10/17 09:53:08 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.3 $
 */
public interface ProviderBusinessHome extends IBOHome {

	public ProviderBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}
