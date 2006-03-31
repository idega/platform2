/**
 * 
 */
package se.idega.idegaweb.commune.accounting.posting.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;


import com.idega.data.IDOHome;

/**
 * @author bluebottle
 *
 */
public interface PostingParametersHome extends IDOHome {
	public PostingParameters create() throws javax.ejb.CreateException;

	public PostingParameters findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#ejbFindPostingParametersByPeriod
	 */
	public Collection findPostingParametersByPeriod(Date from, Date to)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#ejbFindPostingParametersByPeriodAndOperationalID
	 */
	public Collection findPostingParametersByPeriodAndOperationalID(Date from,
			Date to, String opID) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#ejbFindPostingParametersByDate
	 */
	public Collection findPostingParametersByDate(Date date)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#ejbFindAllPostingParameters
	 */
	public Collection findAllPostingParameters() throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#ejbFindPostingParameter
	 */
	public PostingParameters findPostingParameter(Date date, int act_id,
			int reg_id, String com_id, int com_bel_id, int school_year_id1,
			int school_year_id2) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#ejbFindPostingParameter
	 */
	public PostingParameters findPostingParameter(Date date, int act_id,
			int reg_id, String com_id, int com_bel_id, int school_year,
			int study_path_id, boolean no_study_path) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#ejbFindPostingParameter
	 */
	public PostingParameters findPostingParameter(Date date, int act_id,
			int reg_id, String com_id, int com_bel_id, int school_year,
			int study_path_id, boolean no_study_path, int age, int careTimeID)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#ejbFindPostingParameter
	 */
	public PostingParameters findPostingParameter(int act, int reg, int comt,
			int comb) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#ejbFindPostingParameter
	 */
	public PostingParameters findPostingParameter(Date from, Date to,
			String ownPosting, String doublePosting, int activityType,
			int regSpecType, String companyType, int communeBelonging,
			int schoolYear1, int schoolYear2, int studyPath)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#ejbFindPostingParameter
	 */
	public PostingParameters findPostingParameter(Date from, Date to,
			String ownPosting, String doublePosting, int activityType,
			int regSpecType, String companyType, int communeBelonging,
			int schoolYear1, int schoolYear2, int studyPath, int ageFrom,
			int ageTo, int careTime) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#ejbFindPostingParameter
	 */
	public PostingParameters findPostingParameter(int id)
			throws FinderException;

}
