package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;

import com.idega.business.IBOLookup;
import com.idega.business.InputHandler;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.user.business.GroupBusiness;
import com.idega.util.IWTimestamp;
/**
 * A presentation object for dynamic reports to choose groups. By default it
 * creates a selectionbox with all groups but subclassing it or using the
 * setGroupType method can filter the list to only show a desired type.
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportYearDropDownMenu extends Block implements InputHandler {

	protected GroupBusiness groupBiz = null;

	private int year = IWTimestamp.getTimestampRightNow().getYear();

	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	private WorkReportBusiness workBiz = null;
	
	public WorkReportYearDropDownMenu() {
		super();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.business.InputHandler#getHandlerObject(java.lang.String,
	 *      java.lang.String, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, String value, IWContext iwc) {
		this.setName(name);
		DropdownMenu yearInput=null;
		try {
			yearInput = getWorkReportBusiness(iwc).getYearDropdownMenu(year);
			
			yearInput.setName(name);
			if(value!=null){
				yearInput.setSelectedElement(value);
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return yearInput;
	}

	/**
	 * @return the year, Integer
	 *  
	 */
	public Object getResultingObject(String[] values, IWContext iwc) throws Exception {
		if(values!=null && values.length>0){
			return new Integer(values[0]);
		}
		else return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.String,
	 *      com.idega.presentation.IWContext)
	 */
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		if(value!=null){
			return value.toString();
		}
		else return "";
	}
	
	private WorkReportBusiness getWorkReportBusiness(IWApplicationContext iwac) throws RemoteException {
		if (workBiz == null) {
			workBiz = (WorkReportBusiness) IBOLookup.getServiceInstance(iwac, WorkReportBusiness.class);
		}

		return workBiz;
	}
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}


	public PresentationObject getHandlerObject(String name, Collection values, IWContext iwc) {
		String value = (String) Collections.min(values);
		return getHandlerObject(name, value, iwc);
	}


	public Object convertSingleResultingObjectToType(Object value, String className) {
		return value;
	}
}
