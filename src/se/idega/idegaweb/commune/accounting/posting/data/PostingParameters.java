/**
 * 
 */
package se.idega.idegaweb.commune.accounting.posting.data;

import java.sql.Date;
import java.sql.Timestamp;


import se.idega.idegaweb.commune.accounting.regulations.data.CareTime;
import se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;

import com.idega.block.school.data.SchoolManagementType;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface PostingParameters extends IDOEntity {
	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#getPostingString
	 */
	public String getPostingString();

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#setPostingString
	 */
	public void setPostingString(String data);

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#getDoublePostingString
	 */
	public String getDoublePostingString();

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#setDoublePostingString
	 */
	public void setDoublePostingString(String data);

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#getChangedDate
	 */
	public Timestamp getChangedDate();

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#setChangedDate
	 */
	public void setChangedDate(Timestamp date);

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#getChangedSign
	 */
	public String getChangedSign();

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#setChangedSign
	 */
	public void setChangedSign(String sign);

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#setPeriodFrom
	 */
	public void setPeriodFrom(Date period);

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#setPeriodTo
	 */
	public void setPeriodTo(Date period);

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#setActivity
	 */
	public void setActivity(int id);

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#setRegSpecType
	 */
	public void setRegSpecType(int id);

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#setCompanyType
	 */
	public void setCompanyType(String id);

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#setCommuneBelonging
	 */
	public void setCommuneBelonging(int id);

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#setSchoolYear1
	 */
	public void setSchoolYear1(int id);

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#setSchoolYear2
	 */
	public void setSchoolYear2(int id);

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#setStudyPath
	 */
	public void setStudyPath(int id);

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#getPeriodFrom
	 */
	public Date getPeriodFrom();

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#getPeriodTo
	 */
	public Date getPeriodTo();

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#getActivity
	 */
	public SchoolType getActivity();

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#getRegSpecType
	 */
	public RegulationSpecType getRegSpecType();

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#getCompanyType
	 */
	public SchoolManagementType getCompanyType();

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#getSchoolYear1
	 */
	public SchoolYear getSchoolYear1();

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#getSchoolYear2
	 */
	public SchoolYear getSchoolYear2();

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#getStudyPath
	 */
	public SchoolStudyPath getStudyPath();

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#getCommuneBelonging
	 */
	public CommuneBelongingType getCommuneBelonging();

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#setAgeFrom
	 */
	public void setAgeFrom(int ageFrom);

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#getAgeFrom
	 */
	public int getAgeFrom();

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#setAgeTo
	 */
	public void setAgeTo(int ageTo);

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#getAgeTo
	 */
	public int getAgeTo();

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#setCareTimeID
	 */
	public void setCareTimeID(int id);

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#getCareTimeID
	 */
	public int getCareTimeID();

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#setCareTime
	 */
	public void setCareTime(CareTime careTime);

	/**
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParametersBMPBean#getCareTime
	 */
	public CareTime getCareTime();

}
