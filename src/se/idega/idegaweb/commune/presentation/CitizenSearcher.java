/*
 * $Id: CitizenSearcher.java,v 1.7 2005/04/19 11:10:40 laddi Exp $
 * Created on 12.4.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.presentation;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.util.PIDChecker;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.user.presentation.UserSearcher;


/**
 * <p>
 * TODO anna Describe Type CitizenSearcher
 * </p>
 *  Last modified: $Date: 2005/04/19 11:10:40 $ by $Author: laddi $
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.7 $
 */
public class CitizenSearcher extends UserSearcher {

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
	}
	
	public void process(IWContext iwc, boolean save) throws FinderException, RemoteException {
		super.process(iwc, save);
	}	
	
	protected Integer processSave(IWContext iwc, String firstName, String middleName, String lastName, String personalID) throws CreateException {
		if (personalID.length() != 12 || !PIDChecker.getInstance().isValid(personalID, true)) {
			throw new CreateException(getLocalizedString("not_a_valid_personal_id", "The personal ID is invalid or too short", iwc));
		}
		
		try {
			User user = getCommuneUserBusiness(iwc).createCitizen(firstName, middleName, lastName, personalID);
			return (Integer) user.getPrimaryKey();
		}
		catch(RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private CommuneUserBusiness getCommuneUserBusiness(IWApplicationContext iwac) {
		try {
			return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwac, CommuneUserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}