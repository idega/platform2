/*
 * Created on Aug 17, 2004
 *
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.presentation.UserTab;


/**
 * @author birna
 *
 */
public class UserRunTab extends UserTab{
	
	private Text yearText;
	private Text runText;
	
	private Collection runs = null;

	
	
	public UserRunTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString("run_tab.name", "Users run info"));
	}

	
	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
		return true;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initFieldContents()
	 */
	public void initFieldContents() {
		updateFieldsDisplayStatus();
	}
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFieldNames()
	 */
	public void initializeFieldNames() {
	}
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFields()
	 */
	public void initializeFields() {
	}
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
	}
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeTexts()
	 */
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		yearText = new Text(iwrb.getLocalizedString("run_tab.year", "Year"));
		runText = new Text(iwrb.getLocalizedString("run_tab.run", "Run"));
	}
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#lineUpFields()
	 */
	public void lineUpFields() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		RunBusiness runBiz = getRunBiz(iwc);
		String userID = iwc.getParameter("ic_user_id");
		User user = null;
		if(userID != null) {
			try {
				user = getUserBusiness(iwc).getUser(Integer.parseInt(userID));
			}
			catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
		
		try {
			runs = runBiz.getRunsForUser(user);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		

		Table t = new Table();
		int row = 1;
		t.setCellpadding(0);
		t.setCellspacing(0);
		t.add(runText,1,row++);
		if(runs != null) {
			System.out.println("runs in userRunTab: " + runs.toString());
			Iterator i = runs.iterator();
			while(i.hasNext()) {
				Group run = (Group) i.next();
				Link l = new Link(iwrb.getLocalizedString(run.getName(), run.getName()));
				l.setStyleClass("styledLink");
				l.addParameter(IWMarathonConstants.GROUP_TYPE_RUN,run.getPrimaryKey().toString());
				l.addParameter("ic_user_id",Integer.parseInt(userID));
				l.setWindowToOpen(UpdateRunInfoWindow.class);
				if(l != null) {
					t.add(l,1,row++);
				}
			}
		}

		this.add(t,1,1);
	}
	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#store(com.idega.presentation.IWContext)
	 */
	public boolean store(IWContext iwc) {
		return true;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {

	}
	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}
	private RunBusiness getRunBiz(IWContext iwc) {
		RunBusiness business = null;
		try {
			business = (RunBusiness) IBOLookup.getServiceInstance(iwc, RunBusiness.class);
		}
		catch (IBOLookupException e) {
			business = null;
		}
		return business;
	}
}
