package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.util.SelectorUtility;

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


	protected DropdownMenu getSchoolTypes(int typeID, int typeToIgnoreID) throws RemoteException {
		DropdownMenu menu = new DropdownMenu(getSession().getParameterSchoolTypeID());
		
		Collection types = getBusiness().getSchoolBusiness().findAllSchoolTypesForChildCare();
		Iterator iter = types .iterator();
		while (iter.hasNext()) {
			SchoolType element = (SchoolType) iter.next();
			if (((Integer)element.getPrimaryKey()).intValue() != typeToIgnoreID)
				menu.addMenuElement(element.getPrimaryKey().toString(), element.getSchoolTypeName());
		}
		if (typeID != -1)
			menu.setSelectedElement(typeID);
		
		return (DropdownMenu) getStyledInterface(menu);	
	}

	protected DropdownMenu getEmploymentTypes(String parameterName, int selectedType) throws RemoteException {
		SelectorUtility util = new SelectorUtility();
		DropdownMenu menu = (DropdownMenu) util.getSelectorFromIDOEntities(new DropdownMenu(parameterName), getBusiness().findAllEmploymentTypes(), "getLocalizationKey", getResourceBundle());
		menu.addMenuElementFirst("-1", "");
		if (selectedType != -1)
			menu.setSelectedElement(selectedType);
		return menu;
	}

	protected DropdownMenu getGroups(int groupID, int groupToIgnoreID) throws RemoteException {
		DropdownMenu menu = new DropdownMenu(getSession().getParameterGroupID());
		
		Collection groups = getBusiness().getSchoolBusiness().findChildcareClassesBySchool(getSession().getChildCareID());
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
		char status = application.getApplicationStatus();
		
		if (status == getBusiness().getStatusCancelled()) {
			return localize("child_care.status_cancelled","Cancelled");
		}
		else if (status == getBusiness().getStatusContract()) {
			return localize("child_care.status_contract","Contract");
		}
		else if (status == getBusiness().getStatusRejected()) {
			return localize("child_care.status_rejected","Rejected");
		}
		else if (status == getBusiness().getStatusAccepted()) {
			return localize("child_care.status_accepted","Accepted");
		}
		else if (status == getBusiness().getStatusMoved()) {
			return localize("child_care.status_moved","Moved");
		}
		else if (status == getBusiness().getStatusSentIn()) {
			return localize("child_care.status_open","Open");
		}
		else if (status == getBusiness().getStatusParentsAccept()) {
			return localize("child_care.status_parents_accept","Parents accept");
		}
		else if (status == getBusiness().getStatusReady()) {
			return localize("child_care.status_ready","Ready");
		}
		else if (status == getBusiness().getStatusNotAnswered()) {
			return localize("child_care.status_not_answered","Not answered");
		}
		else if (status == getBusiness().getStatusPriority()) {
			return localize("child_care.status_priority","Priority");
		}
		else if (status == getBusiness().getStatusNewChoice()) {
			return localize("child_care.status_new_choice","New Choice");
		}
		
		return "";
	}

	protected DropdownMenu getSeasons() throws RemoteException {
		SelectorUtility util = new SelectorUtility();
		Collection seasons = business.getSchoolBusiness().findAllSchoolSeasons();

		DropdownMenu menu = (DropdownMenu) util.getSelectorFromIDOEntities(new DropdownMenu(getSession().getParameterSeasonID()), seasons, "getSchoolSeasonName");
		menu.setToSubmit();
		
		if ( getSession().getSeasonID() != -1 )
			menu.setSelectedElement(getSession().getSeasonID());
		else {
			try {
				SchoolSeason currentSeason = getBusiness().getSchoolBusiness().getCurrentSchoolSeason();
				menu.setSelectedElement(currentSeason.getPrimaryKey().toString());
			}
			catch (FinderException e) {
				try {
					SchoolSeason currentSeason = getBusiness().getSchoolChoiceBusiness().getCurrentSeason();
					menu.setSelectedElement(currentSeason.getPrimaryKey().toString());
				}
				catch (FinderException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		return (DropdownMenu) getStyledInterface(menu);	
	}
	
	/**
	 * Returns a <code>DropdownMenu</code> that uses the given <code>Collection</code> of entities as options where the
	 * value is a localization key.
	 * @param name The form name for the returned <code>DropdownMenu</code>
	 * @param entities The entity beans to use as values.
	 * @param methodName The name of the method from which the values are retrieved.
	 * @param defaultValue The default value to set if method returns null
	 * @return
	 */
	protected DropdownMenu getDropdownMenuLocalized(String name, Collection entities, String methodName, String defaultValue) {
		SelectorUtility util = new SelectorUtility();
		DropdownMenu menu = (DropdownMenu) util.getSelectorFromIDOEntities(new DropdownMenu(name), entities, methodName, getResourceBundle(), defaultValue);
		
		return (DropdownMenu) getStyledInterface(menu);
	}
		
}