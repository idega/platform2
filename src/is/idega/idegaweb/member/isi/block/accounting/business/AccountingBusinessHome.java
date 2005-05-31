/*
 * $Id: AccountingBusinessHome.java,v 1.5 2005/05/31 09:59:59 palli Exp $
 * Created on May 27, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.accounting.business;




import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2005/05/31 09:59:59 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.5 $
 */
public interface AccountingBusinessHome extends IBOHome {

	public AccountingBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}
