/*
 * Created on 11.4.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.check.business.CheckBusiness;
import se.idega.idegaweb.commune.childcare.check.data.GrantedCheck;
import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.user.data.User;

/**
 * @author laddi
 */
public class ChildCareChildren extends ChildCareBlock {

	private final static String ERROR_NO_CHECKS = "cca_no_checks";
	private final static String ERROR_NO_RESPONSE_PAGE = "cca_no_response_page";
	private final static String SELECT_CHILD = "cca_select_child";

	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		if (getResponsePage() != null) {
			Table table = new Table();
			table.setCellpadding(0);
			table.setCellspacing(0);
			
			int row = 1;
			
			Collection children = null;
			try {
				children = getUserBusiness(iwc).getChildrenForUser(iwc.getCurrentUser());
			}
			
			catch (RemoteException e) {
				e.printStackTrace();
			}
	
			if (children != null && !children.isEmpty()) {
				table.add(getSmallHeader(localize(SELECT_CHILD,"Select the appropriate child") + ":"),1,row++);
				table.setHeight(row++,12);
							
				Iterator it = children.iterator();
				while (it.hasNext()) {
					User child = (User) it.next();
					GrantedCheck check = null;
					try {
						check = getCheckBusiness(iwc).getGrantedCheckByChild(child);
					}
					catch (RemoteException e) {
					}
	
					Link link = null;
					if (check != null) {
						link = getLink(child.getName());
						link.addParameter(getSession().getParameterCheckID(), ((Integer) check.getPrimaryKey()).intValue());
						link.addParameter(getSession().getParameterUserID(), ((Integer)child.getPrimaryKey()).intValue());
						link.setEventListener(ChildCareEventListener.class);
						link.setPage(getResponsePage());
					}
	
					if (link != null) {
						table.add(link, 1, row++);
						table.setHeight(row++,2);
					}
				}
			}
			else {
				add(getErrorText(localize(ERROR_NO_CHECKS, "This user has no checks")));
			}
			add(table);
		}
		else {
			add(getErrorText(localize(ERROR_NO_RESPONSE_PAGE, "The response page has not been set.")));
		}
	}

	private CheckBusiness getCheckBusiness(IWApplicationContext iwac) throws RemoteException {
		return (CheckBusiness) IBOLookup.getServiceInstance(iwac, CheckBusiness.class);
	}
}