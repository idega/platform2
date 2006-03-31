/**
 * 
 */
package se.idega.idegaweb.commune.accounting.posting.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;


import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class PostingParametersHomeImpl extends IDOFactory implements
		PostingParametersHome {
	protected Class getEntityInterfaceClass() {
		return PostingParameters.class;
	}

	public PostingParameters create() throws javax.ejb.CreateException {
		return (PostingParameters) super.createIDO();
	}

	public PostingParameters findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (PostingParameters) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findPostingParametersByPeriod(Date from, Date to)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PostingParametersBMPBean) entity)
				.ejbFindPostingParametersByPeriod(from, to);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findPostingParametersByPeriodAndOperationalID(Date from,
			Date to, String opID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PostingParametersBMPBean) entity)
				.ejbFindPostingParametersByPeriodAndOperationalID(from, to,
						opID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findPostingParametersByDate(Date date)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PostingParametersBMPBean) entity)
				.ejbFindPostingParametersByDate(date);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllPostingParameters() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((PostingParametersBMPBean) entity)
				.ejbFindAllPostingParameters();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public PostingParameters findPostingParameter(Date date, int act_id,
			int reg_id, String com_id, int com_bel_id, int school_year_id1,
			int school_year_id2) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PostingParametersBMPBean) entity)
				.ejbFindPostingParameter(date, act_id, reg_id, com_id,
						com_bel_id, school_year_id1, school_year_id2);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public PostingParameters findPostingParameter(Date date, int act_id,
			int reg_id, String com_id, int com_bel_id, int school_year,
			int study_path_id, boolean no_study_path) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PostingParametersBMPBean) entity)
				.ejbFindPostingParameter(date, act_id, reg_id, com_id,
						com_bel_id, school_year, study_path_id, no_study_path);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public PostingParameters findPostingParameter(Date date, int act_id,
			int reg_id, String com_id, int com_bel_id, int school_year,
			int study_path_id, boolean no_study_path, int age, int careTimeID)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PostingParametersBMPBean) entity)
				.ejbFindPostingParameter(date, act_id, reg_id, com_id,
						com_bel_id, school_year, study_path_id, no_study_path,
						age, careTimeID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public PostingParameters findPostingParameter(int act, int reg, int comt,
			int comb) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PostingParametersBMPBean) entity)
				.ejbFindPostingParameter(act, reg, comt, comb);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public PostingParameters findPostingParameter(Date from, Date to,
			String ownPosting, String doublePosting, int activityType,
			int regSpecType, String companyType, int communeBelonging,
			int schoolYear1, int schoolYear2, int studyPath)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PostingParametersBMPBean) entity)
				.ejbFindPostingParameter(from, to, ownPosting, doublePosting,
						activityType, regSpecType, companyType,
						communeBelonging, schoolYear1, schoolYear2, studyPath);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public PostingParameters findPostingParameter(Date from, Date to,
			String ownPosting, String doublePosting, int activityType,
			int regSpecType, String companyType, int communeBelonging,
			int schoolYear1, int schoolYear2, int studyPath, int ageFrom,
			int ageTo, int careTime) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PostingParametersBMPBean) entity)
				.ejbFindPostingParameter(from, to, ownPosting, doublePosting,
						activityType, regSpecType, companyType,
						communeBelonging, schoolYear1, schoolYear2, studyPath,
						ageFrom, ageTo, careTime);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public PostingParameters findPostingParameter(int id)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((PostingParametersBMPBean) entity)
				.ejbFindPostingParameter(id);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

}
