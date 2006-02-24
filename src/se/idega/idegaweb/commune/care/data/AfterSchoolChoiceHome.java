/**
 * 
 */
package se.idega.idegaweb.commune.care.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.data.School;
import com.idega.data.IDOHome;


/**
 * <p>
 * TODO Dainis Describe Type AfterSchoolChoiceHome
 * </p>
 *  Last modified: $Date: 2006/02/24 11:41:50 $ by $Author: dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.2.2.1 $
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
}
