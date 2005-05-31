/*
 * $Id: AccountingBusinessHomeImpl.java,v 1.6 2005/05/31 10:18:00 palli Exp $
 * Created on May 31, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.accounting.business;




import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2005/05/31 10:18:00 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.6 $
 */
public class AccountingBusinessHomeImpl extends IBOHomeImpl implements AccountingBusinessHome {

	protected Class getBeanInterfaceClass() {
		return AccountingBusiness.class;
	}

	public AccountingBusiness create() throws javax.ejb.CreateException {
		return (AccountingBusiness) super.createIBO();
	}

}
