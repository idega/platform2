package se.idega.idegaweb.commune.adulteducation.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.block.school.data.SchoolType;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.util.SelectorUtility;


public class AvailableCourseFinder extends AdultEducationBlock {

	private static final String PARAMETER_STUDY_PATH_GROUP = "ce_study_path_group";
	private static final String PARAMETER_SCHOOL = "ce_school";	
	
	private SchoolType iSchoolType;
	
	private Object iSchoolSeasonPK;
	
	private Object iStudyPathGroupPK;
	private SchoolStudyPathGroup iStudyPathGroup;
	
	private Object iStudyPathPK;
	
	public void present(IWContext iwc) {
		try {
			add(showForm());
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}		
	}
	
	private PresentationObject showForm() throws RemoteException {
		Form form = new Form();
		
		SelectorUtility util = new SelectorUtility();
		
		Table table = new Table();
		table.setCellspacing(5);
		table.setBorder(2);
		
		table.add(getSmallHeader(localize("season", "Season") + ":"), 1, 1); // aka Period
		table.add(new Break(), 1, 1);
		DropdownMenu seasons = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_SCHOOL_SEASON), getBusiness().getCurrentSeasons(), "getSeasonName"));
		seasons.addMenuElementFirst("", localize("select_season", "Select season"));
		if (iSchoolSeasonPK != null) {
			seasons.setSelectedElement(iSchoolSeasonPK.toString());
		}
		seasons.setToSubmit();
		table.add(seasons, 1, 1);		
		///////////////////////////
		table.add(getSmallHeader(localize("study_path_group", "Study path group") + ":"), 2, 1); // aka Amnesomrade
		table.add(new Break(), 2, 1);
		DropdownMenu groups = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_STUDY_PATH_GROUP), getBusiness().getStudyPathsGroups(), "getLocalizationKey", getResourceBundle()));
		groups.addMenuElementFirst("", localize("select_study_path_group", "Select group"));
		if (iStudyPathGroupPK != null) {
			groups.setSelectedElement(iStudyPathGroupPK.toString());
		}
		groups.setToSubmit();
		table.add(groups, 2, 1);
		
		///////////////////////////////
		table.add(getSmallHeader(localize("study_path", "Study path") + ":"), 1, 2); //aka Kurs
		table.add(new Break(), 1, 2);
		Collection paths = getBusiness().getStudyPaths(iSchoolType, iStudyPathGroup);		
		DropdownMenu studyPaths = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_STUDY_PATH));
		studyPaths.addMenuElementFirst("", localize("select_study_path", "Select study path"));
		Iterator iter = paths.iterator();
		while (iter.hasNext()) {
			SchoolStudyPath path = (SchoolStudyPath) iter.next();
			studyPaths.addMenuElement(path.getPrimaryKey().toString(), path.getDescription() + ", " + path.getPoints());
		}
		studyPaths.setToSubmit();
		if (iStudyPathPK != null) {
			studyPaths.setSelectedElement(iStudyPathPK.toString());
		}
		table.add(studyPaths, 1, 2);
		//////////////////////
		
		
		table.add(getSmallHeader(localize("school", "School") + ":"), 2, 2); // adka Anordnare
		table.add(new Break(), 2, 2);
		Collection schools = null;
		if (iStudyPathPK != null && iSchoolSeasonPK != null) {
			schools = getBusiness().getAvailableSchools(iStudyPathPK, iSchoolSeasonPK);
		}
		School chosenSchool = null;		
		DropdownMenu school = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_SCHOOL + "_" + "1"));
		school.addMenuElementFirst("", localize("select_school", "Select school"));
		if (schools != null) {
			Iterator iterator = schools.iterator();
			while (iterator.hasNext()) {
				School element = (School) iterator.next();
				school.addMenuElement(element.getPrimaryKey().toString(), element.getSchoolName());
			}
		}
		if (chosenSchool != null) {
			school.setSelectedElement(chosenSchool.getPrimaryKey().toString());
		}
		table.add(school, 2, 2);
		
		
		form.add(table);
		return form;	
	}
	
}
