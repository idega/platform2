package se.idega.idegaweb.commune.childcare.presentation.admin;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 */
public class ApplicationQueueAdmin extends ChildCareBlock{

	private static final String PARAMETER_HAS_QUEUE_PRIORITY = "prm_hqp_";
	private static final String PARAMETER_APPLICATION_ID = "prm_ai";
	
	private static final String ACTION = "aqa_a";
	private static final String ACTION_UPDATE = "aqa_au";
	
	public void init(IWContext iwc) throws Exception {
		int childID = getSession().getChildID();
		if ( childID > 0 ) {
			UserHome uHome = (UserHome) IDOLookup.getHome(User.class);
			User child = uHome.findByPrimaryKey(new Integer(childID)); 
			
			String action = iwc.getParameter(ACTION);
			if (action != null && action.equals(ACTION_UPDATE)) {
				handleUpdate(iwc);
			}
			drawForm(iwc, child);
			
			
			
			
		} else {
			add(super.getResourceBundle().getLocalizedString("child_care.no_user_selected", "No user selected"));
			GenericButton back = (GenericButton) getStyledInterface(new GenericButton("back",localize("child_care.select_new_child","Select new child")));
			back.setPageToOpen(getResponsePage());
			add(back);
		}
	}
	
	private void drawForm(IWContext iwc, User child) throws RemoteException {
		Collection applications = getBusiness().getApplicationsForChild(child);
		
		Form form = new Form();
		Table table = new Table();
		form.add(table);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		int row = 1;
		int column = 1;
	
		table.mergeCells(1, row, 6, row);
		table.add(getSmallHeader(child.getName()+Text.NON_BREAKING_SPACE+"-"+Text.NON_BREAKING_SPACE+child.getPersonalID()), 1, row++);
		
		table.add(getLocalizedSmallHeader("child_care.provider","Provider"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.status","Status"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.phone","Phone"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.queue_date","Queue date"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.placement_date","Placement date"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.queue_priority","Queue priority"), column, row);
		
		
		IWTimestamp queueDate;
		IWTimestamp placementDate;
		String phone;
		CheckBox queuePriority;
		ChildCareApplication application;
		Iterator iter = applications.iterator();
		while (iter.hasNext()) {
			++row;
			column = 1;
			application = (ChildCareApplication) iter.next();
			queueDate = new IWTimestamp(application.getQueueDate());
			placementDate = new IWTimestamp(application.getFromDate());
			phone = getBusiness().getSchoolBusiness().getSchoolPhone(application.getProviderId());
			
			queuePriority = new CheckBox(PARAMETER_HAS_QUEUE_PRIORITY+application.getPrimaryKey().toString());
			queuePriority.setChecked(application.getHasQueuePriority());
			
			if (application.getApplicationStatus() == getBusiness().getStatusAccepted()) {
				table.setRowColor(row, ACCEPTED_COLOR);
			}
			else if (application.getApplicationStatus() == getBusiness().getStatusParentsAccept()) {
				table.setRowColor(row, PARENTS_ACCEPTED_COLOR);
			}
			else if (application.getApplicationStatus() == getBusiness().getStatusContract()) {
				table.setRowColor(row, CONTRACT_COLOR);
			}
			else {
				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());
			}
			
			
			table.add(new HiddenInput(PARAMETER_APPLICATION_ID, application.getPrimaryKey().toString()));
			table.add(getSmallText(application.getProvider().getName()), column++, row);
			table.add(getSmallText(getStatusString(application)), column++, row);
			if (phone != null) {
				table.add(getSmallText(phone), column, row);
			}
			column++;
			table.add(getSmallText(queueDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			table.add(getSmallText(placementDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			table.add(queuePriority, column++, row);
		}

		//table.setRowColor(1, getHeaderColor());
		table.setRowColor(2, getHeaderColor());
		table.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);
		
		form.add(getLegendTable());
		SubmitButton update = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.update", "Update"), ACTION, ACTION_UPDATE));
		GenericButton back = (GenericButton) getStyledInterface(new GenericButton("back",localize("child_care.select_new_child","Select new child")));
		back.setPageToOpen(getResponsePage());
		form.add(Text.BREAK);
		form.add(back);
		form.add(Text.NON_BREAKING_SPACE+ Text.NON_BREAKING_SPACE);
		form.add(update);
		add(form);
	}
	
	private void handleUpdate(IWContext iwc) throws RemoteException{
		String[] applicationIDs = iwc.getParameterValues(PARAMETER_APPLICATION_ID);
		
		ChildCareApplication application;
		boolean queuePriority;
		String qP;
		if (applicationIDs != null) {
			for (int i = 0; i < applicationIDs.length; i++) {
				try {
					application = getBusiness().getApplication(Integer.parseInt(applicationIDs[i]));
					queuePriority = application.getHasQueuePriority();
					qP = iwc.getParameter(PARAMETER_HAS_QUEUE_PRIORITY+applicationIDs[i]);
					
					if ( (queuePriority && qP == null) || (!queuePriority && qP != null)) {
						application.setHasQueuePriority(!queuePriority);
						application.store();
					} 
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			} 

			
		}
	}
	
	
}
