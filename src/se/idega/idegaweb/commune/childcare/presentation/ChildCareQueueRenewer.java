/*
 * Created on 8.3.2004
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;

import com.idega.block.navigation.presentation.UserHomeLink;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * @author laddi
 */
public class ChildCareQueueRenewer extends ChildCareBlock {

	public static final String PARAMETER_APPLICATION_ID = "cc_application_id";
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		parse(iwc);

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		table.setHeight(2, 12);
		
		if (getResponsePage() == null) {
			throw new RemoteException("Response page must be set...");
		}
		
		if (getSession().getChildID() != -1) {
			Text informationText = getSmallHeader(localize("child_care.renew_queue_information", "Some of your applications have timed out.  Please select the ones you want to keep and discard the others."));
			table.add(informationText, 1, 1);
			
			table.add(getApplicationTable(iwc), 1, 3);
		}
		else {
			table.add(this.getLocalizedHeader("child_care.no_child_or_application_found","No child or application found."), 1, 1);
			table.add(new UserHomeLink(), 1, 3);
		}
		
		add(table);
	}

	protected Form getApplicationTable(IWContext iwc) throws RemoteException {
		Form form = new Form();
		
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(6);
		table.setRowColor(1, getHeaderColor());
		int row = 1;
		int column = 1;
			
		table.add(getLocalizedSmallHeader("child_care.provider","Provider"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.status","Status"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.placement_date","Placement date"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.last_reply_date","Last reply date"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.keep_application","Keep"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.discard_application","Discard"), column, row++);
			
		ChildCareApplication application;
		IWTimestamp placementDate;
		IWTimestamp lastReplyDate;
		RadioButton keep;
		RadioButton discard;

		Collection applications = getBusiness().getPendingApplications(getSession().getChildID());
		Iterator iter = applications.iterator();
		while (iter.hasNext()) {
			column = 1;
			application = (ChildCareApplication) iter.next();
			form.addParameter(PARAMETER_APPLICATION_ID, application.getPrimaryKey().toString());
			
			placementDate = new IWTimestamp(application.getFromDate());
			lastReplyDate = new IWTimestamp(application.getLastReplyDate());
			keep = getRadioButton(PARAMETER_APPLICATION_ID + "_" + application.getPrimaryKey().toString(), Boolean.TRUE.toString());
			keep.setMustBeSelected(localize("child_care.queue_renew_must_select", "You must select to renew/cancel application for: " + application.getProvider().getSchoolName()));
			discard = getRadioButton(PARAMETER_APPLICATION_ID + "_" + application.getPrimaryKey().toString(), Boolean.FALSE.toString());
				
			if (row % 2 == 0)
				table.setRowColor(row, getZebraColor1());
			else
				table.setRowColor(row, getZebraColor2());

			table.add(getSmallText(application.getProvider().getSchoolName()), column++, row);
			table.add(getSmallText(getStatusString(application)), column++, row);
			table.add(getSmallText(placementDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			table.add(getSmallText(lastReplyDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			table.add(keep, column++, row);
			table.add(discard, column++, row++);
		}
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);
		
		SubmitButton button = (SubmitButton) getButton(new SubmitButton(localize("child_care.renew_queue", "Renew queue")));
		button.setSingleSubmitConfirm(localize("child_care.renew_queue_confirmation", "Are you sure you want to renew your queue options with these values?"));
		table.setHeight(row++, 8);
		table.add(button, 1, row);
		
		form.add(table);
		return form;
	}
	
	private void parse(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_APPLICATION_ID)) {
			try {
				User performer = iwc.getCurrentUser();
				String[] applications = iwc.getParameterValues(PARAMETER_APPLICATION_ID);
				for (int i = 0; i < applications.length; i++) {
					Boolean keep = new Boolean(iwc.getParameter(PARAMETER_APPLICATION_ID + "_" + applications[i]));
					if (keep.booleanValue()) {
						getBusiness().renewApplication(Integer.parseInt(applications[i]), performer);
					}
					else {
						getBusiness().removeFromQueue(Integer.parseInt(applications[i]), performer);
					}
				}
				iwc.forwardToIBPage(getParentPage(), getResponsePage());
			}
			catch (RemoteException re) {
				re.printStackTrace();
			}
		}
	}
}