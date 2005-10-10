/*
 * Created on 11.4.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.care.check.data.GrantedCheck;
import se.idega.idegaweb.commune.childcare.check.business.CheckBusiness;
import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;
import se.idega.util.PIDChecker;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.user.data.User;
import com.idega.util.Age;

/**
 * @author laddi
 */
public class ChildCareChildren extends ChildCareBlock {

	private final static String ERROR_NO_CHECKS = "cca_no_checks";
	private final static String ERROR_NO_RESPONSE_PAGE = "cca_no_response_page";
	private final static String SELECT_CHILD = "cca_select_child";

	private int fromAge = -1;
	private int toAge = 100;
	private boolean _isCheckRequired= true;
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
					Age age = null;
					if (child.getDateOfBirth() != null)
						age = new Age(child.getDateOfBirth());
					else if (child.getPersonalID() != null)
						age = new Age(PIDChecker.getInstance().getDateFromPersonalID(child.getPersonalID()));
					
					if (age != null) {
						if(age.getYears() > toAge || age.getYears() < fromAge){
							continue;
						}
					}
					
					boolean createLink = false;
					GrantedCheck check = null;
					//if (this.isCheckRequired()) {
					if (_isCheckRequired || this.isCheckRequired()){
						try {
							check = getCheckBusiness(iwc).getGrantedCheckByChild(child);
							if (check != null) {
								createLink = true;
							}
						}
						catch (RemoteException e) {
						}
					}
					else {
						createLink = true;
					}
	
					Link link = null;
					if (createLink) {
						link = getLink(child.getName());
						if (check != null) {
							link.addParameter(getSession().getParameterCheckID(), ((Integer) check.getPrimaryKey()).intValue());
						}
						//link.addParameter(getSession().getParameterUserID(), ((Integer)child.getPrimaryKey()).intValue());
						if (child.getUniqueId() != null)
							link.addParameter(getSession().getParameterUniqueID(), child.getUniqueId());
						else
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

	/**
	 * Sets if the component is to set children age range.
	 * <br>This defaults to the range -1 to 1000
	 **/
	public void setAgeRange(int from,int to){
		this.fromAge = from;
		this.toAge = to;
	}
	
	/**
	 * Sets if the component is to set children age range.
	 * <br>This defaults to the range -1 to 1000
	 **/
	public void setIsCheckRequired(boolean isRequired){
		_isCheckRequired = isRequired;
	}
	
	private CheckBusiness getCheckBusiness(IWApplicationContext iwac) throws RemoteException {
		return (CheckBusiness) IBOLookup.getServiceInstance(iwac, CheckBusiness.class);
	}
}