package se.idega.idegaweb.commune.childcare.presentation.admin;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplicationBMPBean;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplicationHome;
import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;
import se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
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
public class ApplicationEditor extends ChildCareBlock {

	private static String ACTION = "ae_a";
	private static String ACTION_EDIT = "ae_e";
	private static String ACTION_UPDATE = "ae_u";
	
	private static String PARAMETER_APPLICATION_ID = "ae_p_ai";
	private static String PARAMETER_APPLICATION_STATUS = "ae_p_as"; 
	
	User child;
	
	public void init(IWContext iwc) throws Exception {
		int childID = getSession().getChildID();
		if ( childID > 0 ) {
			try {
				UserHome uHome = (UserHome) IDOLookup.getHome(User.class);
				child = uHome.findByPrimaryKey(new Integer(childID));
			} catch (Exception e) {
				logError(e.getMessage());
			}
		}
	
		if (child != null) {
			String action = iwc.getParameter(ACTION);
			if (ACTION_EDIT.equals(action)) {
				displayEditForm(iwc);
			} else if (ACTION_UPDATE.equals(action)) {
				if (handleUpdate(iwc)) {
					add(getLocalizedSmallText("child_care.application_updated_success", "Application updated successfully"));
				} else {
					add(getLocalizedSmallText("child_care.application_updated_failed", "Application NOT updated"));
				}
				displayApplications(iwc);
			} else {
				displayApplications(iwc);
			}
		} else {
			add(super.getResourceBundle().getLocalizedString("child_care.no_user_selected", "No user selected"));
		}
		add(Text.getBreak());
		GenericButton back = (GenericButton) getStyledInterface(new GenericButton("back",localize("child_care.select_new_child","Select new child")));
		back.setPageToOpen(getBackPage());
		add(back);
	}
	
	private boolean handleUpdate(IWContext iwc) throws RemoteException {
		ChildCareApplication application = getBusiness().getApplication(Integer.parseInt(iwc.getParameter(PARAMETER_APPLICATION_ID)));
		
		String strStatus = iwc.getParameter(PARAMETER_APPLICATION_STATUS);
		try {
			char status = strStatus.charAt(0);
			boolean success = getBusiness().changeApplicationStatus(application, status, iwc.getCurrentUser());
			return success;
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return false;
	}
	
	private void displayEditForm(IWContext iwc) throws RemoteException{
		ChildCareApplication application = getBusiness().getApplication(Integer.parseInt(iwc.getParameter(PARAMETER_APPLICATION_ID)));
		
		Form form = new Form();
		Table table = new Table();
		form.add(table);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(2);
		
		int row = 1;
		
		table.mergeCells(1, row, 2, row);
		table.add(getSmallHeader(child.getName()+Text.NON_BREAKING_SPACE+"-"+Text.NON_BREAKING_SPACE+child.getPersonalID()), 1, row++);
		
		if (application != null) {
			table.mergeCells(1, row, 2, row);
			table.add(getSmallHeader(application.getProvider().getName()), 1, row++);
			
			table.add(new HiddenInput(PARAMETER_APPLICATION_ID, application.getPrimaryKey().toString()), 1, row);
			DropdownMenu menu = getDropdownForStatus(application.getApplicationStatus());
			
			
			table.add(getLocalizedSmallText("child_care.status", "Application Status"), 1, row);
			table.add(menu, 2, row++);
			table.add(new Link(getResourceBundle().getLocalizedImageButton("child_care.back", "Back")), 1, row);
			table.add(new SubmitButton(getResourceBundle().getLocalizedImageButton("child_care.update", "Update"), ACTION, ACTION_UPDATE), 2, row);
			table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
			
		} else {
			table.add(getLocalizedSmallText("child_care.no_application_selected", "No application selected"), 1, row++);
		}
		
		table.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
		table.setRowColor(1, "#FFFFFF");
		table.setRowColor(2, getHeaderColor());
		
		add(form);
	}

	private DropdownMenu getDropdownForStatus(char currentStatus) throws RemoteException {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_APPLICATION_STATUS));

		menu.addMenuElement(String.valueOf(currentStatus), this.getStatusString(currentStatus));
		
		switch (currentStatus) {
			case ChildCareBusiness.STATUS_ACCEPTED :
				break;
			case ChildCareBusiness.STATUS_CANCELLED :
				break;
			case ChildCareBusiness.STATUS_CONTRACT :
				menu.addMenuElement(String.valueOf(ChildCareBusiness.STATUS_REJECTED), this.getStatusString(ChildCareBusiness.STATUS_REJECTED));
				//menu.addMenuElement(String.valueOf(ChildCareBusiness.STATUS_DELETED), this.getStatusString(ChildCareBusiness.STATUS_DELETED));
				break;
			case ChildCareBusiness.STATUS_MOVED :
				break;
			case ChildCareBusiness.STATUS_NEW_CHOICE :
				break;
			case ChildCareBusiness.STATUS_NOT_ANSWERED :
				menu.addMenuElement(String.valueOf(ChildCareBusiness.STATUS_ACCEPTED), this.getStatusString(ChildCareBusiness.STATUS_ACCEPTED));
				menu.addMenuElement(String.valueOf(ChildCareBusiness.STATUS_SENT_IN), this.getStatusString(ChildCareBusiness.STATUS_SENT_IN));
				break;
			case ChildCareBusiness.STATUS_PARENTS_ACCEPT :
				menu.addMenuElement(String.valueOf(ChildCareBusiness.STATUS_REJECTED), this.getStatusString(ChildCareBusiness.STATUS_REJECTED));
				//menu.addMenuElement(String.valueOf(ChildCareBusiness.STATUS_DELETED), this.getStatusString(ChildCareBusiness.STATUS_DELETED));
				break;
			case ChildCareBusiness.STATUS_PRIORITY :
				break;
			case ChildCareBusiness.STATUS_READY :
				break;
			case ChildCareBusiness.STATUS_REJECTED :
				menu.addMenuElement(String.valueOf(ChildCareBusiness.STATUS_ACCEPTED), this.getStatusString(ChildCareBusiness.STATUS_ACCEPTED));
				menu.addMenuElement(String.valueOf(ChildCareBusiness.STATUS_SENT_IN), this.getStatusString(ChildCareBusiness.STATUS_SENT_IN));
				//menu.addMenuElement(String.valueOf(ChildCareBusiness.STATUS_DELETED), this.getStatusString(ChildCareBusiness.STATUS_DELETED));
				break;
			case ChildCareBusiness.STATUS_SENT_IN :
				break;
			case ChildCareBusiness.STATUS_DELETED :
				menu.addMenuElement(String.valueOf(ChildCareBusiness.STATUS_SENT_IN), this.getStatusString(ChildCareBusiness.STATUS_SENT_IN));
				//menu.addMenuElement(String.valueOf(ChildCareBusiness.STATUS_DELETED), this.getStatusString(ChildCareBusiness.STATUS_DELETED));
				break;
		}
		
		menu.addMenuElement(String.valueOf(ChildCareBusiness.STATUS_DELETED), this.getStatusString(ChildCareBusiness.STATUS_DELETED));
		menu.setSelectedElement(currentStatus);
		return menu;
	}

	private void displayApplications(IWContext iwc) throws RemoteException, FinderException {
		ChildCareApplicationHome ccHome = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplicationBMPBean.class);
		Collection applications = ccHome.findApplicationByChild(new Integer(child.getPrimaryKey().toString()).intValue()); 
		
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		int row = 1;
		int column = 1;
		
		table.mergeCells(1, row, 8, row);
		table.add(getSmallHeader(child.getName()+Text.NON_BREAKING_SPACE+"-"+Text.NON_BREAKING_SPACE+child.getPersonalID()), 1, row++);
		
		table.add(getLocalizedSmallHeader("child_care.provider","Provider"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.status","Status"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.placement_date","Placement date"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.rejection_date","Rejection date"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.care_time","Care time"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.queue_date","Queue date"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.choice_number","Choice number"), column++, row);
		table.add(getSmallHeader(""), column++, row);
		table.setRowColor(row, getHeaderColor());
		
		ChildCareApplication application;
		Link link;
		Link editLink;
		IWTimestamp rejectionDate = null;
		IWTimestamp placementDate = null;
		IWTimestamp queueDate = null;
		//String phone;
		if (applications != null && !applications.isEmpty()) {
			Iterator iter = applications.iterator();
			while (iter.hasNext()) {
				application = (ChildCareApplication) iter.next();
				column = 1;
				++row;

				if (application.getFromDate() != null) {
					placementDate = new IWTimestamp(application.getFromDate());
				} else {
					placementDate = null;
				}
				if (application.getRejectionDate() != null) {
					rejectionDate = new IWTimestamp(application.getRejectionDate());
				} else {
					placementDate = null;
				}
				if (application.getQueueDate() != null) {
					queueDate = new IWTimestamp(application.getQueueDate());
				} else {
					queueDate = null;
				}
				
				
				editLink = new Link(this.getEditIcon(localize("child_care.edit","Edit")));
				editLink.addParameter(ACTION, ACTION_EDIT);
				editLink.addParameter(PARAMETER_APPLICATION_ID, application.getPrimaryKey().toString());
				
				link = new Link(getSmallText(application.getProvider().getName()));
				link.setEventListener(ChildCareEventListener.class);
				link.addParameter(session.getParameterApplicationID(), application.getPrimaryKey().toString());
				if (getResponsePage() != null) {
					link.setPage(getResponsePage());
				}

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
				
				
				table.add(link, column++, row);
				table.add(getSmallText(getStatusString(application)), column++, row);
				if (placementDate != null) {
					table.add(getSmallText(placementDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
				} else {
					table.add(getSmallText("-"), column++, row);
				}

				if (rejectionDate != null) {
					table.add(getSmallText(rejectionDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
				} else {
					table.add(getSmallText("-"), column++, row);
				}
				
				if (application.getCareTime() > 0) {
					table.add(getSmallText(Integer.toString(application.getCareTime())), column++, row);
				} else {
					table.add(getSmallText("-"), column++, row);
				}
				
				if (queueDate != null) {
					table.add(getSmallText(queueDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
				} else {
					table.add(getSmallText("-"), column++, row);
				}
				
				if (application.getChoiceNumber() > 0) {
					table.add(getSmallText(Integer.toString(application.getChoiceNumber())), column++, row);
				} else {
					table.add(getSmallText("-"), column++, row);
				}
				
				table.add(editLink, column++, row);
			}
		}
		add(table);
	}
	
}