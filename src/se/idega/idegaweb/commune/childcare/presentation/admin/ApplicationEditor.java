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

import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.GenericButton;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 */
public class ApplicationEditor extends ChildCareBlock {

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
			displayApplications(iwc);
		} else {
			add(super.getResourceBundle().getLocalizedString("child_care.no_user_selected", "No user selected"));
		}
		GenericButton back = (GenericButton) getStyledInterface(new GenericButton("back",localize("child_care.select_new_child","Select new child")));
		back.setPageToOpen(getBackPage());
		add(back);
	}
	
	

	private void displayApplications(IWContext iwc) throws RemoteException, FinderException {
		ChildCareApplicationHome ccHome = (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplicationBMPBean.class);
		Collection applications = ccHome.findApplicationByChild(new Integer(child.getPrimaryKey().toString()).intValue()); 
		//Collection applications = getChildCareBusiness(iwc).getApplicationsForChild(child);
		
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		int row = 1;
		int column = 1;
		
		table.mergeCells(1, row, 5, row);
		table.add(getSmallHeader(child.getName()+Text.NON_BREAKING_SPACE+"-"+Text.NON_BREAKING_SPACE+child.getPersonalID()), 1, row++);
		
		table.add(getLocalizedSmallHeader("child_care.provider","Provider"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.status","Status"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.placement_date","Placement date"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.rejection_date","Rejection date"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.care_time","Care time"), column++, row);
		table.setRowColor(row, getHeaderColor());
		
		ChildCareApplication application;
		Link link;
		IWTimestamp rejectionDate = null;
		IWTimestamp placementDate = null;
		//String phone;
		if (applications != null && !applications.isEmpty()) {
			Iterator iter = applications.iterator();
			while (iter.hasNext()) {
				application = (ChildCareApplication) iter.next();
				column = 1;
				++row;

				if (application.getFromDate() != null) {
					placementDate = new IWTimestamp(application.getFromDate());
				}
				if (application.getRejectionDate() != null) {
					rejectionDate = new IWTimestamp(application.getRejectionDate());
				}
				//phone = getBusiness().getSchoolBusiness().getSchoolPhone(application.getProviderId());
				
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
				//if (phone != null) {
				//	table.add(getSmallText(phone), column, row);
				//}
				//column++;
				table.add(getSmallText(placementDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
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
				
			}
		}
		add(table);
	}
	
	protected ChildCareBusiness getChildCareBusiness(IWContext iwc) throws RemoteException {
		return (ChildCareBusiness) IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);
	}

}
