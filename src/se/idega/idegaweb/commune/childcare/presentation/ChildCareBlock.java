package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;

import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;

import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.business.ChildCareSession;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

/**
 * @author laddi
 */
public abstract class ChildCareBlock extends CommuneBlock {

	private ChildCareBusiness business;
	protected ChildCareSession session;
	private int _childCareID = -1;
	
	public static final String ACCEPTED_COLOR = "#FFEAEA";
	public static final String PARENTS_ACCEPTED_COLOR = "#EAFFEE";
	public static final String CONTRACT_COLOR = "#EAF1FF";
	
	public void main(IWContext iwc) throws Exception{
		setResourceBundle(getResourceBundle(iwc));
		business = getSchoolCommuneBusiness(iwc);
		session = getSchoolCommuneSession(iwc);
		initialize(iwc);

		init(iwc);
	}
	
	private void initialize(IWContext iwc) throws RemoteException {
		_childCareID = session.getChildCareID();	
	}
	
	public abstract void init(IWContext iwc) throws Exception;
	
	private ChildCareBusiness getSchoolCommuneBusiness(IWContext iwc) throws RemoteException {
		return (ChildCareBusiness) IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);	
	}
	
	private ChildCareSession getSchoolCommuneSession(IWContext iwc) throws RemoteException {
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
}