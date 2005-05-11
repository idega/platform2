package se.idega.idegaweb.commune.adulteducation;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.block.school.data.SchoolStudyPathGroupHome;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;

/**
 * Last modified: $Date: 2005/05/11 07:16:48 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class IWBundleStarter implements IWBundleStartable {

	public void start(IWBundle starterBundle) {
		insertStartData();
	}
	
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}

	protected void insertStartData() {
		insertSchoolCategory(AdultEducationConstants.ADULT_EDUCATION_CATEGORY);
		insertStudyPathGroup(AdultEducationConstants.STUDY_PATH_GROUP_ECONOMICS);
		insertStudyPathGroup(AdultEducationConstants.STUDY_PATH_GROUP_IT);
		insertStudyPathGroup(AdultEducationConstants.STUDY_PATH_GROUP_LANGUAGES);
		insertStudyPathGroup(AdultEducationConstants.STUDY_PATH_GROUP_MATHEMATICS);
		insertStudyPathGroup(AdultEducationConstants.STUDY_PATH_GROUP_PROFESSION_TRAINING);
		insertStudyPathGroup(AdultEducationConstants.STUDY_PATH_GROUP_SCIENCE);
		insertStudyPathGroup(AdultEducationConstants.STUDY_PATH_GROUP_SOCIAL_SCIENCES);
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
}