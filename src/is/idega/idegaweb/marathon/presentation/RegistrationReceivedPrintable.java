/*
 * Created on Aug 16, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.util.IWMarathonConstants;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import javax.ejb.FinderException;
import com.idega.block.text.business.TextFormatter;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.PrintButton;
import com.idega.presentation.ui.Window;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;


/**
 * @author birna
 *
 */
public class RegistrationReceivedPrintable extends Window {
	
	public RegistrationReceivedPrintable() {
		
	}
	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		String name = iwc.getParameter(IWMarathonConstants.PARAMETER_NAME);
		String run = iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN);
		String distance = iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
		String tshirt = iwc.getParameter(IWMarathonConstants.PARAMETER_TSHIRT);

		Table t = new Table();
		t.setStyleAttribute("border-style:solid;border-color:#e1e1e1;height:19px;border-width:1px;padding-left:6px;");
		String message = iwrb.getLocalizedString("registration_received", "Your registration has been received.");
		Group runGroup = null;
		Group distanceGroup = null;
		try {
			runGroup = getGroupBusiness(iwc).getGroupByGroupID(Integer.parseInt(run));
			distanceGroup = getGroupBusiness(iwc).getGroupByGroupID(Integer.parseInt(distance));
			Object[] args = { name, iwrb.getLocalizedString(runGroup.getName(),runGroup.getName()), iwrb.getLocalizedString(distanceGroup.getName(),distanceGroup.getName()), iwrb.getLocalizedString(tshirt, tshirt) };
			message = MessageFormat.format(iwrb.getLocalizedString("registration_received", "Your registration has been received."), args);
		}
		catch (RemoteException re) {
			log(re);
		}
		catch (FinderException fe) {
			log(fe);
		}
		t.add(TextFormatter.formatText(message), 1, 1);
		t.add(new PrintButton(iwrb.getLocalizedString("print","Print")));
		
		add(t);
	}
	private GroupBusiness getGroupBusiness(IWContext iwc) {
		try {
			return (GroupBusiness) IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}


	
}
