/*
 * Created on 1.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.util.Collection;
import java.sql.Date;

import javax.ejb.FinderException;

/**
 * @author Roar
 *
*/
public class RegularPaymentEntryHomeImpl  extends com.idega.data.IDOFactory implements RegularPaymentEntryHome{

	/* (non-Javadoc)
	 * @see com.idega.data.IDOFactory#getEntityInterfaceClass()
	 */
	protected Class getEntityInterfaceClass() {
		return RegularPaymentEntry.class;
	}
	
	public RegularPaymentEntry create() throws javax.ejb.CreateException{
	 return (RegularPaymentEntry) super.createIDO();
	}	
	
	public Collection findRegularInvoicesForPeriodeAndUser(Date from, Date to, int userId) throws FinderException{
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RegularInvoiceEntryBMPBean)entity).ejbFindByPeriodeAndUser(from, to, userId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findRegularInvoicesForPeriode(Date from, Date to){
		
		return null;		
	}
	
	public RegularPaymentEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
	 return (RegularPaymentEntry) super.findByPrimaryKeyIDO(pk);
	}	

}
