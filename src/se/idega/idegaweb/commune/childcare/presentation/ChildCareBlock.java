package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.school.data.SchoolClass;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;

import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.business.ChildCareSession;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

/**
 * @author laddi
 */
public abstract class ChildCareBlock extends CommuneBlock {

	private ChildCareBusiness business;
	protected ChildCareSession session;
	private int _childCareID = -1;
	
	//public static final String ACCEPTED_COLOR = "#FFEAEA";
	//public static final String PARENTS_ACCEPTED_COLOR = "#EAFFEE";
	//public static final String CONTRACT_COLOR = "#EAF1FF";
	public static final String ACCEPTED_COLOR = "#FFE0E0";
	public static final String PARENTS_ACCEPTED_COLOR = "#E0FFE0";
	public static final String CONTRACT_COLOR = "#E0E0FD";
	
	public void main(IWContext iwc) throws Exception{
		setResourceBundle(getResourceBundle(iwc));
		business = getChildCareBusiness(iwc);
		session = getChildCareSession(iwc);
		initialize();

		init(iwc);
	}
	
	private void initialize() throws RemoteException {
		_childCareID = session.getChildCareID();	
	}
	
	public abstract void init(IWContext iwc) throws Exception;
	
	private ChildCareBusiness getChildCareBusiness(IWContext iwc) throws RemoteException {
		return (ChildCareBusiness) IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);	
	}
	
	private ChildCareSession getChildCareSession(IWContext iwc) throws RemoteException {
		return (ChildCareSession) IBOLookup.getSessionInstance(iwc, ChildCareSession.class);	
	}
	
	/**
	 * @return ChildCareBusiness
	 */
	public ChildCareBusiness getBusiness() {
		return business;
	}

	/**
	 * @return ChildCareSession
	 */
	public ChildCareSession getSession() {
		return session;
	}

	/**
	 * @return int
	 */
	public int getChildcareID() {
		return _childCareID;
	}

	protected Table getLegendTable() {
		Table table = new Table(8,1);
		table.setHeight(1, 12);
		table.setWidth(1, "12");
		table.setWidth(3, "12");
		table.setWidth(4, "12");
		table.setWidth(6, "12");
		table.setWidth(7, "12");
		
		table.add(getColorTable(ACCEPTED_COLOR), 1, 1);
		table.add(getColorTable(PARENTS_ACCEPTED_COLOR), 4, 1);
		table.add(getColorTable(CONTRACT_COLOR), 7, 1);
		
		table.add(getSmallHeader(localize("child_care.application_status_accepted","Accepted")), 2, 1);
		table.add(getSmallHeader(localize("child_care.application_status_parents_accepted","Parents accepted")), 5, 1);
		table.add(getSmallHeader(localize("child_care.application_status_contract","Contract")), 8, 1);
		
		return table;
	}
	
	private Table getColorTable(String color) {
		Table colorTable = new Table(1, 1);
		colorTable.setHeight(1, 1, "12");
		colorTable.setWidth(1, 1, "12");
		colorTable.setColor("#000000");
		colorTable.setColor(1, 1, color);
		colorTable.setCellpadding(0);
		colorTable.setCellspacing(1);
		
		return colorTable;		
	}

	protected DropdownMenu getGroups(int groupID, int groupToIgnoreID) throws RemoteException {
		DropdownMenu menu = new DropdownMenu(getSession().getParameterGroupID());
		
		Collection groups = getBusiness().getSchoolBusiness().findSchoolClassesBySchool(getSession().getChildCareID());
		Iterator iter = groups.iterator();
		while (iter.hasNext()) {
			SchoolClass element = (SchoolClass) iter.next();
			if (((Integer)element.getPrimaryKey()).intValue() != groupToIgnoreID)
				menu.addMenuElement(element.getPrimaryKey().toString(), element.getSchoolClassName());
		}
		if (groupID != -1)
			menu.setSelectedElement(groupID);
		
		return (DropdownMenu) getStyledInterface(menu);	
	}
	
	protected String getStatusString(ChildCareApplication application) throws RemoteException {
		String status = application.getStatus();
		
		if (status.equals(getBusiness().getCaseStatusCancelled().getStatus())) {
			return localize("child_care.status_cancelled","Cancelled");
		}
		else if (status.equals(getBusiness().getCaseStatusContract().getStatus())) {
			return localize("child_care.status_contract","Contract");
		}
		else if (status.equals(getBusiness().getCaseStatusDenied().getStatus())) {
			return localize("child_care.status_rejected","Rejected");
		}
		else if (status.equals(getBusiness().getCaseStatusGranted().getStatus())) {
			return localize("child_care.status_accepted","Accepted");
		}
		else if (status.equals(getBusiness().getCaseStatusInactive().getStatus())) {
			return localize("child_care.status_inactive","Inactive");
		}
		else if (status.equals(getBusiness().getCaseStatusOpen().getStatus())) {
			return localize("child_care.status_open","Open");
		}
		else if (status.equals(getBusiness().getCaseStatusPreliminary().getStatus())) {
			return localize("child_care.status_parents_accept","Parents accept");
		}
		else if (status.equals(getBusiness().getCaseStatusReady().getStatus())) {
			return localize("child_care.status_ready","Ready");
		}
		
		return "";
	}
}