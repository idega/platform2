/*
 * $Id: IWBundleStarter.java,v 1.1 2005/05/24 12:06:29 laddi Exp $
 * Created on May 23, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon;

import is.idega.idegaweb.marathon.util.IWMarathonConstants;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.creditcard.data.KortathjonustanMerchant;
import com.idega.block.creditcard.data.KortathjonustanMerchantHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;


/**
 * Last modified: $Date: 2005/05/24 12:06:29 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class IWBundleStarter implements IWBundleStartable {
	
	/* (non-Javadoc)
	 * @see com.idega.idegaweb.IWBundleStartable#start(com.idega.idegaweb.IWBundle)
	 */
	public void start(IWBundle starterBundle) {
		String merchantPK = starterBundle.getProperty(IWMarathonConstants.PROPERTY_MERCHANT_PK);
		
		try {
			KortathjonustanMerchant merchant = null;
			if (merchantPK == null) {
				merchant = ((KortathjonustanMerchantHome) IDOLookup.getHome(KortathjonustanMerchant.class)).create();
			}
			else {
				merchant = ((KortathjonustanMerchantHome) IDOLookup.getHome(KortathjonustanMerchant.class)).findByPrimaryKey(new Integer(merchantPK));
			}
			
			merchant.setName("Marathon.is");
			merchant.store();
			starterBundle.setProperty(IWMarathonConstants.PROPERTY_MERCHANT_PK, merchant.getPrimaryKey().toString());
		}
		catch (IDOLookupException ile) {
			ile.printStackTrace();
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
		catch (CreateException ce) {
			ce.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.idegaweb.IWBundleStartable#stop(com.idega.idegaweb.IWBundle)
	 */
	public void stop(IWBundle starterBundle) {
	}
}
