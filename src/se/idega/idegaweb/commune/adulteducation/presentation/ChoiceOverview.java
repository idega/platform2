/*
 * $Id: ChoiceOverview.java,v 1.1 2005/05/11 17:44:48 laddi Exp $
 * Created on May 11, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoice;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourse;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;


/**
 * Last modified: $Date: 2005/05/11 17:44:48 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class ChoiceOverview extends AdultEducationBlock {
	
	private static final String PARAMETER_REMOVE = "co_remove";
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.adulteducation.presentation.AdultEducationBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		try {
			if (iwc.isParameterSet(PARAMETER_REMOVE)) {
				remove(iwc);
			}
			showOverview(iwc);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private void showOverview(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setColumns(3);
		int row = 1;
		
		Collection seasons = getBusiness().getPendingSeasons();
		Iterator iter = seasons.iterator();
		while (iter.hasNext()) {
			SchoolSeason season = (SchoolSeason) iter.next();
			Collection choices = getBusiness().getChoices(iwc.getCurrentUser(), season);
			if (!choices.isEmpty()) {
				table.add(getSmallHeader(season.getSchoolSeasonName()), 1, row++);
				table.setHeight(row++, 6);
				
				Iterator iterator = choices.iterator();
				while (iterator.hasNext()) {
					AdultEducationChoice choice = (AdultEducationChoice) iterator.next();
					AdultEducationCourse course = choice.getCourse();
					SchoolStudyPath path = course.getStudyPath();
					
					table.add(getSmallText(path.getDescription() + ", " + path.getPoints()), 1, row);
					
					if (!choice.hasAllGranted()) {
						if (getResponsePage() != null) {
							Link edit = new Link(getEditIcon(localize("edit_choice", "Edit choice")));
							edit.addParameter(PARAMETER_CHOICE, choice.getPrimaryKey().toString());
							edit.addParameter(PARAMETER_STUDY_PATH, path.getPrimaryKey().toString());
							edit.setPage(getResponsePage());
							table.add(edit, 2, row);
						}

						Link delete = new Link(getDeleteIcon(localize("edit_choice", "Edit choice")));
						delete.addParameter(PARAMETER_CHOICE, choice.getPrimaryKey().toString());
						delete.addParameter(PARAMETER_REMOVE, Boolean.TRUE.toString());
						table.add(delete, 3, row);
					}
					row++;
				}
				
				table.setHeight(row++, 16);
			}
		}
		
		add(table);
	}
	
	private void remove(IWContext iwc) throws RemoteException {
		getBusiness().removeChoice(iwc.getParameter(PARAMETER_CHOICE), iwc.getCurrentUser());
	}
}