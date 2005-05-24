/*
 * Created on Jul 13, 2004
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * 
 * @author birna
 */
public class CreateYearWindow extends StyledIWAdminWindow {

	public CreateYearWindow() {
		super();
		setWidth(700);
		setHeight(500);
	}
	
	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		String runID = iwc.getParameter("ic_group_id");

		Group run = null;
		if (runID != null && !runID.equals("")) {
			int id = Integer.parseInt(runID);
			run = getGroupBiz(iwc).getGroupByGroupID(id);
		}

		Form form = new Form();
		form.maintainParameter("ic_group_id");

		Table table = new Table();
		table.setCellpadding(3);
		table.setColumns(7);
		int row = 1;
		
		table.add(new Text("Year: "), 1, row);
		table.mergeCells(2, 1, 7, 1);
		table.add(new TextInput("year"), 2, row++);
		table.setHeight(row++, 12);
		
		table.add(new Text("Price (ISK)"), 2, row);
		table.add(new Text("Price (EUR)"), 3, row);
		table.add(new Text("Child Price (ISK)"), 4, row);
		table.add(new Text("Child Price (EUR)"), 5, row);
		table.add(new Text("Use chip"), 6, row);
		table.add(new Text("Family discount"), 7, row++);
		String[] distances = getRunBiz(iwc).getDistancesForRun(run);
		if (distances != null) {
			for (int i = 0; i < distances.length; i++) {
				String distance = distances[i];
				table.add(new Text(iwrb.getLocalizedString("distance." + distance, distance)), 1, row);
				
				TextInput text = new TextInput("price_isk");
				text.setLength(12);
				table.add(text, 2, row);
				
				text = new TextInput("price_eur");
				text.setLength(12);
				table.add(text, 3, row);
				
				text = new TextInput("price_children_isk");
				text.setLength(12);
				table.add(text, 4, row);
				
				text = new TextInput("price_children_eur");
				text.setLength(12);
				table.add(text, 5, row);
				
				DropdownMenu menu = new DropdownMenu("use_chip");
				menu.addMenuElement(Boolean.TRUE.toString(), "Yes");
				menu.addMenuElement(Boolean.FALSE.toString(), "No");
				table.add(menu, 6, row);

				menu = new DropdownMenu("family_discount");
				menu.addMenuElement(Boolean.TRUE.toString(), "Yes");
				menu.addMenuElement(Boolean.FALSE.toString(), "No");
				menu.setSelectedElement(Boolean.FALSE.toString());
				table.add(menu, 7, row++);
			}
		}
		form.add(table);

		SubmitButton create = new SubmitButton(iwrb.getLocalizedString("run_reg.submit_step_one", "Next step"), "action", String.valueOf(1));
		table.setHeight(row++, 12);
		table.add(create, 1, row);

		add(form, iwc);

		switch (parseAction(iwc)) {
			case 1:
				getRunBiz(iwc).createNewGroupYear(iwc, run, iwc.getParameter("year"), iwc.getParameterValues("price_isk"), iwc.getParameterValues("price_eur"), iwc.getParameterValues("use_chip"), iwc.getParameterValues("price_children_isk"), iwc.getParameterValues("price_children_eur"), iwc.getParameterValues("family_discount"));
				close();
		}
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet("action")) {
			return Integer.parseInt(iwc.getParameter("action"));
		} else {
			return 0;
		}
	}

	private RunBusiness getRunBiz(IWContext iwc) {
		RunBusiness business = null;
		try {
			business = (RunBusiness) IBOLookup.getServiceInstance(iwc, RunBusiness.class);
		} catch (IBOLookupException e) {
			business = null;
		}
		return business;
	}

	private GroupBusiness getGroupBiz(IWContext iwc) throws IBOLookupException {
		GroupBusiness business = (GroupBusiness) IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
		return business;
	}

	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}
}