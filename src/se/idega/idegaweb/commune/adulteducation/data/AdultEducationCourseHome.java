/**
 * 
 */
package se.idega.idegaweb.commune.adulteducation.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.block.school.data.SchoolType;
import com.idega.data.IDOHome;


/**
 * <p>
 * TODO Dainis Describe Type AdultEducationCourseHome
 * </p>
 *  Last modified: $Date: 2006/03/08 11:10:00 $ by $Author: dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.3.2.1 $
 */
public interface AdultEducationCourseHome extends IDOHome {

	public AdultEducationCourse create() throws javax.ejb.CreateException;

	public AdultEducationCourse findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindBySeasonAndCode
	 */
	public AdultEducationCourse findBySeasonAndCode(SchoolSeason season, String code) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindBySeasonAndCode
	 */
	public AdultEducationCourse findBySeasonAndCode(Object season, String code) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindAllBySeasonAndSchool
	 */
	public Collection findAllBySeasonAndSchool(SchoolSeason season, School school) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindAllBySeasonAndSchool
	 */
	public Collection findAllBySeasonAndSchool(Object season, Object school) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindAllBySeasonAndTypeAndSchoolAndStudyPathGroup
	 */
	public Collection findAllBySeasonAndTypeAndSchoolAndStudyPathGroup(SchoolSeason season, SchoolType type,
			School school, SchoolStudyPathGroup group) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindAllBySeasonAndTypeAndSchoolAndStudyPathGroup
	 */
	public Collection findAllBySeasonAndTypeAndSchoolAndStudyPathGroup(Object season, Object type, Object school,
			Object group) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindAllBySchoolAndSeasonAndStudyPathGroupConnectedToChoices
	 */
	public Collection findAllBySchoolAndSeasonAndStudyPathGroupConnectedToChoices(Object school, Object season,
			Object group, Object[] statuses) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindAllBySchoolAndSeasonAndStudyPathGroupConnectedToStudents
	 */
	public Collection findAllBySchoolAndSeasonAndStudyPathGroupConnectedToStudents(Object school, Object season,
			Object group) throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindAllBySeasonAndSchoolAndStudyPath
	 */
	public Collection findAllBySeasonAndSchoolAndStudyPath(SchoolSeason season, School school, SchoolStudyPath studyPath)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindAllBySeasonAndSchoolAndStudyPath
	 */
	public Collection findAllBySeasonAndSchoolAndStudyPath(Object season, Object school, Object studyPath)
			throws FinderException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean#ejbFindAllAvailableCoursesByParameters
	 */
	public Collection findAllAvailableCoursesByParameters(SchoolType schoolType, SchoolSeason schoolSeason,
			SchoolStudyPathGroup studyPathGroup, SchoolStudyPath studyPath, School school) throws FinderException;
}
