/**
 * 
 */
package se.idega.idegaweb.commune.care.data;

import java.sql.Date;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.data.IDOException;
import com.idega.data.IDOFactory;


/**
 * <p>
 * TODO Dainis Describe Type AfterSchoolChoiceHomeImpl
 * </p>
 *  Last modified: $Date: 2006/04/05 15:28:39 $ by $Author: dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.2.2.2 $
 */
public class AfterSchoolChoiceHomeImpl extends IDOFactory implements AfterSchoolChoiceHome {

	protected Class getEntityInterfaceClass() {
		return AfterSchoolChoice.class;
	}

	public AfterSchoolChoice create() throws javax.ejb.CreateException {
		return (AfterSchoolChoice) super.createIDO();
	}

	public AfterSchoolChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (AfterSchoolChoice) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findByChildAndSeason(Integer childID, Integer seasonID) throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AfterSchoolChoiceBMPBean) entity).ejbFindByChildAndSeason(childID, seasonID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public AfterSchoolChoice findByChildAndChoiceNumberAndSeason(Integer childID, Integer choiceNumber, Integer seasonID)
			throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((AfterSchoolChoiceBMPBean) entity).ejbFindByChildAndChoiceNumberAndSeason(childID, choiceNumber,
				seasonID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public AfterSchoolChoice findByChildAndChoiceNumberAndSeason(Integer childID, Integer choiceNumber,
			Integer seasonID, String[] caseStatus) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((AfterSchoolChoiceBMPBean) entity).ejbFindByChildAndChoiceNumberAndSeason(childID, choiceNumber,
				seasonID, caseStatus);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public AfterSchoolChoice findByChildAndProviderAndSeason(int childID, int providerID, int seasonID,
			String[] caseStatus) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((AfterSchoolChoiceBMPBean) entity).ejbFindByChildAndProviderAndSeason(childID, providerID,
				seasonID, caseStatus);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findAllCasesByProviderAndStatus(int providerId, CaseStatus caseStatus) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AfterSchoolChoiceBMPBean) entity).ejbFindAllCasesByProviderAndStatus(providerId,
				caseStatus);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllCasesByProviderAndStatus(School provider, String caseStatus) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AfterSchoolChoiceBMPBean) entity).ejbFindAllCasesByProviderAndStatus(provider,
				caseStatus);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllCasesByProviderAndStatus(School provider, CaseStatus caseStatus) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AfterSchoolChoiceBMPBean) entity).ejbFindAllCasesByProviderAndStatus(provider,
				caseStatus);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllCasesByProviderAndStatus(int providerId, String caseStatus) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AfterSchoolChoiceBMPBean) entity).ejbFindAllCasesByProviderAndStatus(providerId,
				caseStatus);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllCasesByProviderAndNotInStatus(int providerId, String[] caseStatus) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AfterSchoolChoiceBMPBean) entity).ejbFindAllCasesByProviderAndNotInStatus(
				providerId, caseStatus);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllCasesByProviderAndNotInStatus(int providerId, String[] caseStatus, String sorting)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AfterSchoolChoiceBMPBean) entity).ejbFindAllCasesByProviderAndNotInStatus(
				providerId, caseStatus, sorting);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByProviderAndSeasonAndStatuses(School provider, SchoolSeason season,
			String[] applicationStatus, Date terminationDate) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AfterSchoolChoiceBMPBean) entity).ejbFindByProviderAndSeasonAndStatuses(provider,
				season, applicationStatus, terminationDate);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByDatesAndStatus(Date fromDate, Date toDate, String[] statuses) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((AfterSchoolChoiceBMPBean) entity).ejbFindAllByDatesAndStatus(fromDate, toDate,
				statuses);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getChoiceStatistics(SchoolSeason season, String[] statuses) throws IDOException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((AfterSchoolChoiceBMPBean) entity).ejbHomeGetChoiceStatistics(season, statuses);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}
}
