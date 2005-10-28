/*
 * $Id: AccountingBusinessHome.java,v 1.7 2005/10/28 11:03:46 palli Exp $
 * Created on Oct 21, 2005
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
 *  Last modified: $Date: 2005/10/28 11:03:46 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.7 $
 */
public interface AccountingBusinessHome extends IBOHome {

	public AccountingBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}
