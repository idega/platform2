package se.idega.idegaweb.commune.adulteducation;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusiness;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceReason;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceReasonHome;
import com.idega.block.process.business.CaseCodeManager;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.block.school.data.SchoolStudyPathGroupHome;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;

/**
 * Last modified: $Date: 2005/05/16 13:42:54 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public class IWBundleStarter implements IWBundleStartable {

	public void start(IWBundle starterBundle) {
		insertStartData();
	}
	
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}

	protected void insertStartData() {
		CaseCodeManager caseCodeManager = CaseCodeManager.getInstance();
		caseCodeManager.addCaseBusinessForCode(AdultEducationConstants.ADULT_EDUCATION_CASE_CODE, AdultEducationBusiness.class);

		insertSchoolCategory(AdultEducationConstants.ADULT_EDUCATION_CATEGORY);
		insertStudyPathGroup(AdultEducationConstants.STUDY_PATH_GROUP_ECONOMICS);
		insertStudyPathGroup(AdultEducationConstants.STUDY_PATH_GROUP_IT);
		insertStudyPathGroup(AdultEducationConstants.STUDY_PATH_GROUP_LANGUAGES);
		insertStudyPathGroup(AdultEducationConstants.STUDY_PATH_GROUP_MATHEMATICS);
		insertStudyPathGroup(AdultEducationConstants.STUDY_PATH_GROUP_PROFESSION_TRAINING);
		insertStudyPathGroup(AdultEducationConstants.STUDY_PATH_GROUP_SCIENCE);
		insertStudyPathGroup(AdultEducationConstants.STUDY_PATH_GROUP_SOCIAL_SCIENCES);
		
		insertReason(AdultEducationConstants.REASON_FULFILL_STUDIES);
		insertReason(AdultEducationConstants.REASON_COMPLEMENT_HIGH_SCHOOL);
		insertReason(AdultEducationConstants.REASON_QUALIFICATION_COURSE);
		insertReason(AdultEducationConstants.REASON_WORK_RELATED_KNOWLEDGE);
		insertReason(AdultEducationConstants.REASON_TO_GET_EMPLOYMENT);
		insertReason(AdultEducationConstants.REASON_GET_FINAL_GRADE);
	}

	private void insertSchoolCategory(String category){
		try {
			SchoolCategoryHome cHome = (SchoolCategoryHome) com.idega.data.IDOLookup.getHome(SchoolCategory.class);
			SchoolCategory cat;
			try {
				cat = cHome.findByPrimaryKey(category); 
			}
			catch (FinderException fe) {
				try {
					cat = cHome.create();
					cat.setCategory(category);
					cat.setName("Adult education");
					cat.setLocalizedKey("school_category." + category);
					cat.store();
					System.out.println("[IWBundleStarter]ÊInserted school category for adult education");
				}
				catch (CreateException ce) {
					ce.printStackTrace();
				}
			}
		}
		catch (IDOLookupException ile) {
			ile.printStackTrace();
		}
	}

	private void insertStudyPathGroup(String name){
		try {
			SchoolStudyPathGroupHome cHome = (SchoolStudyPathGroupHome) com.idega.data.IDOLookup.getHome(SchoolStudyPathGroup.class);
			SchoolStudyPathGroup group;
			try {
				group = cHome.findByGroupName(name); 
			}
			catch (FinderException fe) {
				try {
					group = cHome.create();
					group.setGroupName(name);
					group.setLocalizationKey("study_path_group." + name);
					group.store();
					System.out.println("[IWBundleStarter]ÊInserted study path group = " + name);
				}
				catch (CreateException ce) {
					ce.printStackTrace();
				}
			}
		}
		catch (IDOLookupException ile) {
			ile.printStackTrace();
		}
	}

	private void insertReason(String name){
		try {
			AdultEducationChoiceReasonHome cHome = (AdultEducationChoiceReasonHome) com.idega.data.IDOLookup.getHome(AdultEducationChoiceReason.class);
			AdultEducationChoiceReason reason;
			try {
				reason = cHome.findByName(name); 
			}
			catch (FinderException fe) {
				try {
					reason = cHome.create();
					reason.setName(name);
					reason.setLocalizedKey("vux_choice_reason." + name);
					reason.store();
					System.out.println("[IWBundleStarter]ÊInserted reason = " + name);
				}
				catch (CreateException ce) {
					ce.printStackTrace();
				}
			}
		}
		catch (IDOLookupException ile) {
			ile.printStackTrace();
		}
	}
}