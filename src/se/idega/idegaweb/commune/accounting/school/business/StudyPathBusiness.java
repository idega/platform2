/**
 * 
 */
package se.idega.idegaweb.commune.accounting.school.business;

import java.util.Collection;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOService;


/**
 * <p>
 * TODO Dainis Describe Type StudyPathBusiness
 * </p>
 *  Last modified: $Date: 2006/03/08 11:05:20 $ by $Author: dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.7.2.1 $
 */
public interface StudyPathBusiness extends IBOService {

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findAllStudyPaths
	 */
	public Collection findAllStudyPaths() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathsByOperation
	 */
	public Collection findStudyPathsByOperation(int operation) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathsByOperations
	 */
	public Collection findStudyPathsByOperations(Collection operations) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathsByType
	 */
	public Collection findStudyPathsByType(SchoolType type, SchoolStudyPathGroup group) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathsBySchoolTypeAndSchoolStudyPathGroup
	 */
	public Collection findStudyPathsBySchoolTypeAndSchoolStudyPathGroup(SchoolType type, SchoolStudyPathGroup group)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathsBySchool
	 */
	public Collection findStudyPathsBySchool(School school) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathsBySchool
	 */
	public Collection findStudyPathsBySchool(School school, SchoolStudyPathGroup group) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findAllOperations
	 */
	public Collection findAllOperations() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findAllStudyPathGroups
	 */
	public Collection findAllStudyPathGroups() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathGroupByID
	 */
	public SchoolStudyPathGroup findStudyPathGroupByID(int groupId) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathGroup
	 */
	public SchoolStudyPathGroup findStudyPathGroup(Object groupPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPath
	 */
	public SchoolStudyPath findStudyPath(Object pathPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#saveStudyPath
	 */
	public void saveStudyPath(String studyPathId, String operation, String studyPathCode, String description,
			String points, String studypathgroup) throws StudyPathException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#deleteStudyPath
	 */
	public void deleteStudyPath(String id) throws StudyPathException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#getStudyPath
	 */
	public SchoolStudyPath getStudyPath(String id) throws StudyPathException, java.rmi.RemoteException;
}
