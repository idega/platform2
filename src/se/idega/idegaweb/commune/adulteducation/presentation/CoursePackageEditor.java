/*
 * $Id: CoursePackageEditor.java,v 1.2.2.1 2006/10/16 13:41:58 palli Exp $
 * Created on Jul 6, 2005
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
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import se.idega.idegaweb.commune.adulteducation.data.CoursePackage;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * Last modified: $Date: 2006/10/16 13:41:58 $ by $Author: palli $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2.2.1 $
 */
public class CoursePackageEditor extends AdultEducationBlock {

	private static final String PARAMETER_ACTION = "cpe_action";

	private static final String PARAMETER_NAME = "cpe_name";

	private static final int ACTION_FORM = 1;

	private static final int ACTION_STORE = 2;

	private static final int ACTION_REMOVE = 3;

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.idega.idegaweb.commune.adulteducation.presentation.AdultEducationBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		try {
			switch (parseAction(iwc)) {
			case ACTION_FORM:
				showForm();
				break;

			case ACTION_STORE:
				storePackage(iwc);
				showForm();
				break;

			case ACTION_REMOVE:
				removePackage(iwc);
				showForm();
				break;
			}
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private void showForm() throws RemoteException {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_STORE));

		Table table = new Table(2, 3);
		form.add(table);

		TextInput name = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_NAME));
		// TextInput localizedKey = (TextInput) getStyledInterface(new
		// TextInput(PARAMETER_LOCALIZED_KEY));
		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize(
				"save_package", "Save package")));

		table.add(
				getSmallHeader(localize("package_name", "Package name") + ":"),
				1, 1);
		table.add(name, 2, 1);
		// table.add(getSmallHeader(localize("localized_key", "Localized key") +
		// ":"), 1, 2);
		// table.add(localizedKey, 2, 2);
		table.add(save, 2, 2);

		form.add(new Break(2));
		form.add(getLocalizedHeader("created_packages", "Created packages"));
		form.add(new Break());

		Table packageTable = new Table();
		packageTable.setCellpadding(getCellpadding());
		packageTable.setCellspacing(getCellspacing());
		packageTable.setColumns(2);
		packageTable.setRowColor(1, getHeaderColor());
		int row = 1;

		packageTable.add(getLocalizedSmallHeader("course_package",
				"Course package"), 1, row++);
		// packageTable.add(getLocalizedSmallHeader("localized_key","Localized
		// key"), 2, row++);

		Collection packages = getBusiness().getCoursePackages();
		Iterator iter = packages.iterator();
		while (iter.hasNext()) {
			CoursePackage coursePackage = (CoursePackage) iter.next();

			packageTable.add(getSmallText(coursePackage.getName()), 1, row);
			// packageTable.add(getSmallText(coursePackage.getLocalizedKey() !=
			// null ? coursePackage.getLocalizedKey() : "-"), 2, row);

			if (!getBusiness().hasSchoolPackages(coursePackage)) {
				Link delete = new Link(getDeleteIcon(localize(
						"remove_course_package", "Remove course package")));
				delete.addParameter(PARAMETER_ACTION, ACTION_REMOVE);
				delete.addParameter(PARAMETER_COURSE_PACKAGE, coursePackage
						.getPrimaryKey().toString());
				packageTable.add(delete, 2, row);
			}

			row++;
		}
		form.add(packageTable);

		add(form);
	}

	private void storePackage(IWContext iwc) throws RemoteException {
		try {
			getBusiness().storePackage(iwc, iwc.getParameter(PARAMETER_NAME),
					"");
		} catch (CreateException ce) {
			ce.printStackTrace();
		}
	}

	private void removePackage(IWContext iwc) throws RemoteException {
		try {
			getBusiness().removePackage(
					iwc.getParameter(PARAMETER_COURSE_PACKAGE));
		} catch (RemoveException re) {
			re.printStackTrace();
		}
	}

	private int parseAction(IWContext iwc) {
		int action = ACTION_FORM;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return action;
	}
}