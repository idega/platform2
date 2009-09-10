package com.idega.block.trade.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;
import com.idega.user.data.Group;


/**
 * @author gimmi
 */
public interface CreditCardInformationHome extends IDOHome {

	public CreditCardInformation create() throws javax.ejb.CreateException;

	public CreditCardInformation findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see com.idega.block.trade.data.CreditCardInformationBMPBean#ejbFindBySupplierManager
	 */
	public Collection findBySupplierManager(Group supplierManager) throws FinderException;
}
