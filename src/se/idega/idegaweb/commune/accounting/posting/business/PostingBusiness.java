/**
 * 
 */
package se.idega.idegaweb.commune.accounting.posting.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;


import se.idega.idegaweb.commune.accounting.posting.data.PostingField;
import se.idega.idegaweb.commune.accounting.posting.data.PostingParameters;
import se.idega.idegaweb.commune.accounting.school.data.Provider;

import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOService;

/**
 * @author bluebottle
 *
 */
public interface PostingBusiness extends IBOService {
	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#generateString
	 */
	public String generateString(String first, String second, Date date)
			throws RemoteException, PostingException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#findFieldInStringByName
	 */
	public String findFieldInStringByName(String postingString, String name)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#validateString
	 */
	public void validateString(String postingString, Date date)
			throws PostingException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#extractField
	 */
	public String extractField(String ps, int readPointer, int fieldLength,
			PostingField field) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#getPostingParameter
	 */
	public PostingParameters getPostingParameter(Date date, int act_id,
			int reg_id, String com_id, int com_bel_id)
			throws PostingParametersException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#getPostingParameter
	 */
	public PostingParameters getPostingParameter(Date date, int act_id,
			int reg_id, String com_id, int com_bel_id, int schoolYear1_id,
			int schoolYear2_id) throws PostingParametersException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#getPostingParameter
	 */
	public PostingParameters getPostingParameter(Date date, int act_id,
			int reg_id, String com_id, int com_bel_id, int schoolYear1_id)
			throws PostingParametersException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#getPostingParameterWithoutStudypath
	 */
	public PostingParameters getPostingParameterWithoutStudypath(Date date,
			int act_id, int reg_id, String com_id, int com_bel_id,
			int schoolYear1_id) throws PostingParametersException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#getPostingParameterWithoutStudypath
	 */
	public PostingParameters getPostingParameterWithoutStudypath(Date date,
			int act_id, int reg_id, String com_id, int com_bel_id,
			int schoolYear1_id, int age, int careTimeID)
			throws PostingParametersException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#getPostingParameterWithStudypath
	 */
	public PostingParameters getPostingParameterWithStudypath(Date date,
			int act_id, int reg_id, String com_id, int com_bel_id,
			int schoolYear1_id, int studyPathID)
			throws PostingParametersException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#getPostingParameterWithStudypath
	 */
	public PostingParameters getPostingParameterWithStudypath(Date date,
			int act_id, int reg_id, String com_id, int com_bel_id,
			int schoolYear1_id, int studyPathID, int age, int careTimeID)
			throws PostingParametersException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#savePostingParameter
	 */
	public void savePostingParameter(String sppID, Date periodFrom,
			Date periodTo, String changedSign, String activityID,
			String regSpecTypeID, String companyTypeID,
			String communeBelongingID, String schoolYear1ID,
			String schoolYear2ID, String studyPathID, String ownPostingString,
			String doublePostingString) throws PostingParametersException,
			RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#savePostingParameter
	 */
	public void savePostingParameter(String sppID, Date periodFrom,
			Date periodTo, String changedSign, String activityID,
			String regSpecTypeID, String companyTypeID,
			String communeBelongingID, String schoolYear1ID,
			String schoolYear2ID, String studyPathID, String ageFrom,
			String ageTo, String careTime, String ownPostingString,
			String doublePostingString) throws PostingParametersException,
			RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#deletePostingParameter
	 */
	public void deletePostingParameter(int ppID)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#findPostingParametersByPeriod
	 */
	public Collection findPostingParametersByPeriod(Date from, Date to)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#findPostingParametersByPeriod
	 */
	public Collection findPostingParametersByPeriod(Date from, Date to,
			String opID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#findAllPostingParameters
	 */
	public Collection findAllPostingParameters()
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#findPostingParameter
	 */
	public Object findPostingParameter(int id) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#findPostingParameterByPeriod
	 */
	public Object findPostingParameterByPeriod(Date from, Date to)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#getPostingFieldByDateAndFieldNo
	 */
	public int getPostingFieldByDateAndFieldNo(Date date, int fieldNo)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#getAllPostingFieldsByDate
	 */
	public Collection getAllPostingFieldsByDate(Date date)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#pad
	 */
	public String pad(String in, PostingField postingField)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#trim
	 */
	public String trim(String in, PostingField postingField)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#getPostingStrings
	 */
	public String[] getPostingStrings(SchoolCategory category, SchoolType type,
			int regSpecType, Provider provider, Date date)
			throws PostingException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#getPostingStrings
	 */
	public String[] getPostingStrings(SchoolCategory category, SchoolType type,
			int regSpecType, Provider provider, Date date, int age,
			int careTimeID) throws PostingException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#getPostingStrings
	 */
	public String[] getPostingStrings(SchoolCategory category, SchoolType type,
			int regSpecType, Provider provider, Date date, int schoolYearId,
			int studyPathId, boolean noStudyPathId) throws PostingException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#getPostingStrings
	 */
	public String[] getPostingStrings(SchoolCategory category, SchoolType type,
			int regSpecType, Provider provider, Date date, int schoolYearId,
			int studyPathId, boolean noStudyPathId, int age, int careTimeID)
			throws PostingException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessBean#getPostingStrings
	 */
	public String[] getPostingStrings(SchoolCategory category, SchoolType type,
			int regSpecType, Provider provider, Date date, int schoolYearId)
			throws PostingException, java.rmi.RemoteException;

}
