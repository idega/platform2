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

import com.idega.block.school.data.School;
import com.idega.user.data.User;

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
	
	public Collection findRegularPaymentsForPeriodeAndUser(Date from, Date to, int userId) throws FinderException{
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RegularPaymentEntryBMPBean)entity).ejbFindByPeriodeAndUser(from, to, userId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findRegularPaymentsForPeriode(Date from, Date to){
		
		return null;		
	}
	
	public RegularPaymentEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
	 return (RegularPaymentEntry) super.findByPrimaryKeyIDO(pk);
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularPaymentEntryHome#findRegularPaymentsForPeriodeAndProvider(java.sql.Date, java.sql.Date, com.idega.block.school.data.School)
	 */
	public Collection findRegularPaymentsForPeriodeAndProvider(Date from, Date to, School provider)  throws FinderException{
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RegularPaymentEntryBMPBean)entity).ejbFindByPeriodeAndProvider(from, to, provider);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	
	/*  (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.RegularPaymentEntryHome#findOngoingRegularPaymentForUserAndProviderByDate(com.idega.user.data.User, com.idega.block.school.data.School, java.sql.Date)
	 */
	public Collection findOngoingRegularPaymentsForUserAndProviderByDate(User child, School provider, Date date)throws javax.ejb.FinderException{
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((RegularPaymentEntryBMPBean)entity).ejbFindOngoingByUserAndProviderAndDate(child, provider, date);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	

}
