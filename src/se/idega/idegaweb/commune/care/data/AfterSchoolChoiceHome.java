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
import com.idega.data.IDOHome;


/**
 * <p>
 * TODO Dainis Describe Type AfterSchoolChoiceHome
 * </p>
 *  Last modified: $Date: 2006/04/05 15:28:39 $ by $Author: dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.2.2.2 $
 */
public interface AfterSchoolChoiceHome extends IDOHome {

	public AfterSchoolChoice create() throws javax.ejb.CreateException;

	public AfterSchoolChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindByChildAndSeason
	 */
	public Collection findByChildAndSeason(Integer childID, Integer seasonID) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindByChildAndChoiceNumberAndSeason
	 */
	public AfterSchoolChoice findByChildAndChoiceNumberAndSeason(Integer childID, Integer choiceNumber, Integer seasonID)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindByChildAndChoiceNumberAndSeason
	 */
	public AfterSchoolChoice findByChildAndChoiceNumberAndSeason(Integer childID, Integer choiceNumber,
			Integer seasonID, String[] caseStatus) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindByChildAndProviderAndSeason
	 */
	public AfterSchoolChoice findByChildAndProviderAndSeason(int childID, int providerID, int seasonID,
			String[] caseStatus) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindAllCasesByProviderAndStatus
	 */
	public Collection findAllCasesByProviderAndStatus(int providerId, CaseStatus caseStatus) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindAllCasesByProviderAndStatus
	 */
	public Collection findAllCasesByProviderAndStatus(School provider, String caseStatus) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindAllCasesByProviderAndStatus
	 */
	public Collection findAllCasesByProviderAndStatus(School provider, CaseStatus caseStatus) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindAllCasesByProviderAndStatus
	 */
	public Collection findAllCasesByProviderAndStatus(int providerId, String caseStatus) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindAllCasesByProviderAndNotInStatus
	 */
	public Collection findAllCasesByProviderAndNotInStatus(int providerId, String[] caseStatus) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindAllCasesByProviderAndNotInStatus
	 */
	public Collection findAllCasesByProviderAndNotInStatus(int providerId, String[] caseStatus, String sorting)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindByProviderAndSeasonAndStatuses
	 */
	public Collection findByProviderAndSeasonAndStatuses(School provider, SchoolSeason season,
			String[] applicationStatus, Date terminationDate) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbFindAllByDatesAndStatus
	 */
	public Collection findAllByDatesAndStatus(Date fromDate, Date toDate, String[] statuses) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#ejbHomeGetChoiceStatistics
	 */
	public int getChoiceStatistics(SchoolSeason season, String[] statuses) throws IDOException;
}
