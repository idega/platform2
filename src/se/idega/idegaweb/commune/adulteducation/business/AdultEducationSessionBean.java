/*
 * $Id: AdultEducationSessionBean.java,v 1.11 2005/07/07 08:41:42 laddi Exp $
 * Created on May 24, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.business;

import java.rmi.RemoteException;
import java.sql.Date;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.accounting.school.business.StudyPathBusiness;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoice;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourse;
import se.idega.idegaweb.commune.adulteducation.data.CoursePackage;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2005/07/07 08:41:42 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.11 $
 */
public class AdultEducationSessionBean extends IBOSessionBean  implements AdultEducationSession{
	
	private Object iAdministratorPK = null;
	private School iCurrentSchool = null;
	
	private Object iSeasonPK = null;
	private SchoolSeason iSeason = null;
	
	private Object iCourseSeasonPK = null;
	private SchoolSeason iCourseSeason = null;
	
	private Object iChoicePK = null;
	private AdultEducationChoice iChoice = null;
	
	private String iUserUniqueID = null;
	private User iUser = null;
	private Object iUserPK = null;
	
	private Object iSchoolTypePK = null;
	private SchoolType iSchoolType = null;
	
	private Object iStudyPathGroupPK = null;
	private SchoolStudyPathGroup iStudyPathGroup = null;
	
	private Object iCoursePK = null;
	private AdultEducationCourse iCourse = null;
	
	private Object iSchoolPK = null;
	private School iSchool = null;
	
	private Object iCoursePackagePK = null;
	private CoursePackage iCoursePackage = null;
	
	private Object iSchoolClassPK = null;
	private SchoolClass iSchoolClass = null;
	
	private Object iSchoolClassMemberPK = null;
	private SchoolClassMember iSchoolClassMember = null;
	
	private Date iFromDate = null;
	private Date iToDate = null;
	
	private int iSort = -1;

	public SchoolSeason getSchoolSeason() {
		if (iSeason == null && iSeasonPK != null) {
			try {
				iSeason = getSchoolBusiness().getSchoolSeason(new Integer(iSeasonPK.toString()));
			}
			catch (RemoteException re) {
				iSeason = null;
			}
		}
		else if (iSeason == null) {
			try {
				iSeason = getSchoolBusiness().getSchoolSeasonHome().findNextSeason(getSchoolBusiness().getCategoryAdultEducation(), new IWTimestamp().getDate());
			}
			catch (FinderException fe) {
				//Nothing found...
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		return iSeason;
	}

	public void setSeason(Object seasonPK) {
		if (iSeasonPK != null && !iSeasonPK.equals(seasonPK)) {
			setSchoolClass(null);
			setCourse(null);
			setCourseSeason(null);
		}
		iSeasonPK = seasonPK;
		iSeason = null;
	}
	
	public SchoolSeason getCourseSeason() {
		if (iCourseSeason == null && iCourseSeasonPK != null) {
			try {
				iCourseSeason = getSchoolBusiness().getSchoolSeason(new Integer(iCourseSeasonPK.toString()));
			}
			catch (RemoteException re) {
				iCourseSeason = null;
			}
		}
		return iCourseSeason;
	}

	public void setCourseSeason(Object seasonPK) {
		iCourseSeasonPK = seasonPK;
		iCourseSeason = null;
	}
	
	public SchoolType getSchoolType() {
		if (iSchoolType == null && iSchoolTypePK != null) {
			try {
				iSchoolType = getSchoolBusiness().getSchoolType(new Integer(iSchoolTypePK.toString()));
			}
			catch (RemoteException re) {
				iSchoolType = null;
			}
		}
		return iSchoolType;
	}

	public void setSchoolType(Object schoolTypePK) {
		iSchoolTypePK = schoolTypePK;
		iSchoolType= null;
	}
	
	public SchoolStudyPathGroup getStudyPathGroup() {
		if (iStudyPathGroup == null && iStudyPathGroupPK != null) {
			try {
				iStudyPathGroup = getStudyPathBusiness().findStudyPathGroup(new Integer(iStudyPathGroupPK.toString()));
			}
			catch (RemoteException re) {
				iStudyPathGroup = null;
			}
		}
		return iStudyPathGroup;
	}

	public void setStudyPathGroup(Object studyPathGroupPK) {
		if (iStudyPathGroupPK != null && !iStudyPathGroupPK.equals(studyPathGroupPK)) {
			setSchoolClass(null);
			setCourse(null);
		}
		iStudyPathGroupPK = studyPathGroupPK;
		iStudyPathGroup= null;
	}
	
	public AdultEducationCourse getCourse() {
		if (iCourse == null && iCoursePK != null) {
			try {
				iCourse = getAdultEducationBusiness().getCourse(iCoursePK);
			}
			catch (RemoteException re) {
				iCourse = null;
			}
			catch (FinderException fe) {
				fe.printStackTrace();
				iCourse = null;
			}
		}
		return iCourse;
	}

	public void setCourse(Object coursePK) {
		iCoursePK = coursePK;
		iCourse= null;
	}
	
	public SchoolClass getSchoolClass() {
		if (iSchoolClass == null && iSchoolClassPK != null) {
			try {
				iSchoolClass = getSchoolBusiness().getSchoolClassHome().findByPrimaryKey(new Integer(iSchoolClassPK.toString()));
			}
			catch (RemoteException re) {
				iSchoolClass = null;
			}
			catch (FinderException fe) {
				fe.printStackTrace();
				iSchoolClass = null;
			}
		}
		return iSchoolClass;
	}

	public void setSchoolClass(Object schoolClassPK) {
		iSchoolClassPK = schoolClassPK;
		iSchoolClass= null;
	}
	
	public School getChosenSchool() {
		if (iSchool == null && iSchoolPK != null) {
			try {
				iSchool = getSchoolBusiness().getSchool(iSchoolPK);
			}
			catch (RemoteException re) {
				iSchool = null;
			}
		}
		return iSchool;
	}

	public void setChosenSchool(Object schoolPK) {
		iSchoolPK = schoolPK;
		iSchool= null;
	}
	
	public CoursePackage getCoursePackage() {
		if (iCoursePackage == null && iCoursePackagePK != null) {
			try {
				iCoursePackage = getAdultEducationBusiness().getCoursePackage(iCoursePackagePK);
			}
			catch (RemoteException re) {
				iCoursePackage = null;
			}
			catch (FinderException fe) {
				iCoursePackage = null;
			}
		}
		return iCoursePackage;
	}

	public void setCoursePackage(Object coursePackagePK) {
		iCoursePackagePK = coursePackagePK;
		iCoursePackage= null;
	}
	
	
	public SchoolClassMember getSchoolClassMember() {
		if (iSchoolClassMember == null && iSchoolClassMemberPK != null) {
			try {
				iSchoolClassMember = getSchoolBusiness().getSchoolClassMemberHome().findByPrimaryKey(new Integer(iSchoolClassMemberPK.toString()));
			}
			catch (RemoteException re) {
				iSchoolClassMember = null;
			}
			catch (FinderException fe) {
				fe.printStackTrace();
				iSchoolClassMember = null;
			}
		}
		return iSchoolClassMember;
	}

	public void setSchoolClassMember(Object schoolClassMemberPK) {
		iSchoolClassMemberPK = schoolClassMemberPK;
		iSchoolClassMember= null;
	}
	
	public Date getFromDate() {
		if (iFromDate == null) {
			iFromDate = new IWTimestamp().getDate();
		}
		return iFromDate;
	}
	
	public void setFromDate(Date date) {
		iFromDate = date;
	}
	
	public Date getToDate() {
		if (iToDate == null) {
			iToDate = new IWTimestamp().getDate();
		}
		return iToDate;
	}
	
	public void setToDate(Date date) {
		iToDate = date;
	}
	
	public int getSort() {
		return iSort;
	}
	
	public void setSort(int sort) {
		iSort = sort;
	}
	
	public AdultEducationChoice getChoice() {
		if (iChoice == null && iChoicePK != null) {
			try {
				iChoice = getAdultEducationBusiness().getChoice(new Integer(iChoicePK.toString()));
			}
			catch (FinderException fe) {
				fe.printStackTrace();
				iChoice = null;
			}
			catch (RemoteException re) {
				iChoice = null;
			}
		}
		return iChoice;
	}

	public void setChoice(Object choicePK) {
		iChoicePK = choicePK;
		iChoice = null;
	}

	public User getStudent() {
		if (iUser == null && iUserPK != null) {
			try {
				iUser = getUserBusiness().getUser(new Integer(iUserPK.toString()));
			}
			catch (RemoteException re) {
				iUser = null;
			}
		}
		return iUser;
	}

	public void setStudent(String userPK) {
		iUserPK = userPK;
		iUser = null;
	}

	public void setStudentUniqueID(String userUniqueID) {
		iUserUniqueID = userUniqueID;
		try {
			iUser = getUserBusiness().getUserByUniqueId(iUserUniqueID);
			iUserPK = iUser.getPrimaryKey();
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			iUser = null;
			iUserPK = null;
		}
		catch (RemoteException re) {
			iUser = null;
			iUserPK = null;
		}
	}
	
	public School getSchool() {
		if (getUserContext().isLoggedOn()) {
			User user = getUserContext().getCurrentUser();
			Object userPK = user.getPrimaryKey();
			
			if (iAdministratorPK != null && iAdministratorPK.equals(userPK)) {
				if (iCurrentSchool != null) {
					return iCurrentSchool;
				}
				else {
					return getSchoolFromUser(user);
				}
			}
			else {
				iAdministratorPK = userPK;
				return getSchoolFromUser(user);
			}
		}
		else {
			return iCurrentSchool;	
		}
	}

	/**
	 * Returns the schoolID.
	 * @return int
	 */
	public Object getSchoolPK() {
		if (iCurrentSchool != null) {
			return iCurrentSchool.getPrimaryKey();
		}
		return null;
	}
	
	private School getSchoolFromUser(User user) {
		if (user != null) {
			try {
				School school = getAdultEducationBusiness().getSchoolForUser(user);
				if (school != null) {
					iCurrentSchool = school;
				}
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		return iCurrentSchool;
	}

	private CommuneUserBusiness getUserBusiness() {
		try {
			return (CommuneUserBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), CommuneUserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), SchoolBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private StudyPathBusiness getStudyPathBusiness() {
		try {
			return (StudyPathBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), StudyPathBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private AdultEducationBusiness getAdultEducationBusiness() {
		try {
			return (AdultEducationBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), AdultEducationBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

}