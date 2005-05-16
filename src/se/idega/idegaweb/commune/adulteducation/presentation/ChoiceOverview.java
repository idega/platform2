/*
 * $Id: ChoiceOverview.java,v 1.7 2005/05/16 16:39:20 laddi Exp $
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
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;


/**
 * Last modified: $Date: 2005/05/16 16:39:20 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.7 $
 */
public class ChoiceOverview extends AdultEducationBlock {
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.adulteducation.presentation.AdultEducationBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		try {
			if (iwc.isParameterSet(PARAMETER_STUDY_PATH)) {
				remove(iwc);
			}
			showOverview(iwc);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private void showOverview(IWContext iwc) throws RemoteException {
		Form form = new Form();
		
		Table table = new Table();
		table.setColumns(3);
		form.add(table);
		int row = 1;
		
		Collection seasons = getBusiness().getPendingSeasons();
		Iterator iter = seasons.iterator();
		while (iter.hasNext()) {
			SchoolSeason season = (SchoolSeason) iter.next();
			Collection choices = getBusiness().getChoices(iwc.getCurrentUser(), season);
			if (!choices.isEmpty()) {
				table.add(getSmallHeader(season.getSchoolSeasonName()), 1, row);
				table.add(Text.getNonBrakingSpace(), 1, row);
				
				Link pdf = new Link(getPDFIcon(localize("get_pdf", "Get PDF")));
				//pdf.setWindow(getFileWindow());
				//pdf.addParameter(MediaWritable.PRM_WRITABLE_CLASS, IWMainApplication.getEncryptedClassName(PDFOverviewCreator.class));
				pdf.setWindowToOpen(PDFCreationWindow.class);
				pdf.addParameter(PARAMETER_SCHOOL_SEASON, season.getPrimaryKey().toString());
				table.add(pdf, 1, row++);
				
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
							edit.addParameter(PARAMETER_SCHOOL_SEASON, course.getSchoolSeason().getPrimaryKey().toString());
							edit.setPage(getResponsePage());
							table.add(edit, 2, row);
						}

						SubmitButton delete = new SubmitButton(getDeleteIcon(localize("delete_choices", "Delete choices")), PARAMETER_STUDY_PATH, path.getPrimaryKey().toString());
						delete.setDescription(localize("delete_choices", "Delete choices"));
						delete.setSubmitConfirm(localize("confirm_choice_delete", "Are you sure you want to remove the choices?"));
						table.add(delete, 3, row);
					}
					row++;
				}
				
				table.setHeight(row++, 16);
			}
		}
		
		add(form);
	}
	
	private void remove(IWContext iwc) throws RemoteException {
		getBusiness().removeChoices(iwc.getParameter(PARAMETER_STUDY_PATH), iwc.getCurrentUser());
	}
}