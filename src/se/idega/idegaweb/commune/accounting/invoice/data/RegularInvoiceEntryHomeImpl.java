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
public class RegularInvoiceEntryHomeImpl  extends com.idega.data.IDOFactory implements RegularInvoiceEntryHome{

	/* (non-Javadoc)
	 * @see com.idega.data.IDOFactory#getEntityInterfaceClass()
	 */
	protected Class getEntityInterfaceClass() {
		return RegularInvoiceEntry.class;
	}
	
	public RegularInvoiceEntry create() throws javax.ejb.CreateException{
	 return (RegularInvoiceEntry) super.createIDO();
	}	
	
	public Collection findRegularInvoicesForPeriodeUserAndCategory(Date from, Date to, int userId, String schoolCategoryId) throws FinderException{
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RegularInvoiceEntryBMPBean)entity).ejbFindByPeriodeAndUser(from, to, userId, schoolCategoryId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

//	public Collection findRegularInvoicesForPeriode(Date from, Date to){
//		
//		return null;		
//	}
	
	public RegularInvoiceEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
	 return (RegularInvoiceEntry) super.findByPrimaryKeyIDO(pk);
	}	

}
