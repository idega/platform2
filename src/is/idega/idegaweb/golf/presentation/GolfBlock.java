/*
 * Created on 5.10.2003
 */
package is.idega.idegaweb.golf.presentation;

import java.rmi.RemoteException;

import is.idega.idegaweb.golf.handicap.business.HandicapBusiness;

import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.Block;

/**
 * @author laddi
 */
public class GolfBlock extends Block {

	protected final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.golf";

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	protected HandicapBusiness getHandicapBusiness(IWApplicationContext iwac) {
		try {
			return (HandicapBusiness) IBOLookup.getServiceInstance(iwac, HandicapBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}
}