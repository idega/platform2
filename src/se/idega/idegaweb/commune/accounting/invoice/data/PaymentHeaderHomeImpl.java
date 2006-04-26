/**
 * 
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.data.IDOException;
import com.idega.data.IDOFactory;
import com.idega.data.IDOLookupException;
import com.idega.util.CalendarMonth;
import com.idega.util.TimePeriod;

/**
 * @author bluebottle
 *
 */
public class PaymentHeaderHomeImpl extends IDOFactory implements
		PaymentHeaderHome {
	protected Class getEntityInterfaceClass() {
		return PaymentHeader.class;
	}

	public PaymentHeader create() throws javax.ejb.CreateException {
		return (PaymentHeader) super.createIDO();
	}

	public PaymentHeader findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (PaymentHeader) super.findByPrimaryKeyIDO(pk);
	}

	public PaymentHeader findBySchoolCategorySchoolPeriod(School school,
			SchoolCategory schoolCategory, Date period) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PaymentHeaderBMPBean) entity)
				.ejbFindBySchoolCategorySchoolPeriod(school, schoolCategory,
						period);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public PaymentHeader findBySchoolCategoryAndSchoolAndPeriodAndStatus(
			School school, SchoolCategory schoolCategory, TimePeriod period,
			String status) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PaymentHeaderBMPBean) entity)
				.ejbFindBySchoolCategoryAndSchoolAndPeriodAndStatus(school,
						schoolCategory, period, status);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllBySchoolCategoryAndSchoolAndPeriodAndStatus(
			School school, SchoolCategory schoolCategory, TimePeriod period,
			String status) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PaymentHeaderBMPBean) entity)
				.ejbFindAllBySchoolCategoryAndSchoolAndPeriodAndStatus(school,
						schoolCategory, period, status);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getProviderCountForSchoolCategoryAndPeriod(
			String schoolCategoryID, Date period) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((PaymentHeaderBMPBean) entity)
				.ejbHomeGetProviderCountForSchoolCategoryAndPeriod(
						schoolCategoryID, period);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getPlacementCountForSchoolAndPeriod(int schoolID, Date period,
			String schoolCategoryID) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((PaymentHeaderBMPBean) entity)
				.ejbHomeGetPlacementCountForSchoolAndPeriod(schoolID, period,
						schoolCategoryID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findBySchoolAndSchoolCategoryPKAndStatus(Object schoolPK,
			Object schoolCategoryPK, String status) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PaymentHeaderBMPBean) entity)
				.ejbFindBySchoolAndSchoolCategoryPKAndStatus(schoolPK,
						schoolCategoryPK, status);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolCategoryAndPeriodForPrivate(
			SchoolCategory schoolCategory, Date period)
			throws IDOLookupException, EJBException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PaymentHeaderBMPBean) entity)
				.ejbFindBySchoolCategoryAndPeriodForPrivate(schoolCategory,
						period);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByStatusAndSchoolId(char status, int schoolID)
			throws EJBException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PaymentHeaderBMPBean) entity)
				.ejbFindByStatusAndSchoolId(status, schoolID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolCategoryAndSchoolAndPeriod(
			String schoolCategory, Integer providerId, Date startPeriod,
			Date endPeriod) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PaymentHeaderBMPBean) entity)
				.ejbFindBySchoolCategoryAndSchoolAndPeriod(schoolCategory,
						providerId, startPeriod, endPeriod);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolCategoryStatusInCommuneWithCommunalManagement(
			String schoolCategory, char status) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PaymentHeaderBMPBean) entity)
				.ejbFindBySchoolCategoryStatusInCommuneWithCommunalManagement(
						schoolCategory, status);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolCategoryStatusInCommuneWithoutCommunalManagement(
			String schoolCategory, char status) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PaymentHeaderBMPBean) entity)
				.ejbFindBySchoolCategoryStatusInCommuneWithoutCommunalManagement(
						schoolCategory, status);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolCategoryStatusOutsideCommuneOrWithoutCommunalManagement(
			String schoolCategory, char status) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PaymentHeaderBMPBean) entity)
				.ejbFindBySchoolCategoryStatusOutsideCommuneOrWithoutCommunalManagement(
						schoolCategory, status);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolCategoryAndStatus(String schoolCategory,
			char status) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PaymentHeaderBMPBean) entity)
				.ejbFindBySchoolCategoryAndStatus(schoolCategory, status);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolCategoryAndPeriod(String sc, Date period)
			throws EJBException, FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PaymentHeaderBMPBean) entity)
				.ejbFindBySchoolCategoryAndPeriod(sc, period);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByMonthAndSchoolCategory(CalendarMonth month,
			SchoolCategory schoolCategory) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PaymentHeaderBMPBean) entity)
				.ejbFindByMonthAndSchoolCategory(month, schoolCategory);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}
