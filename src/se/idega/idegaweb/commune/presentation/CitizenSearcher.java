/*
 * $Id: CitizenSearcher.java,v 1.3 2005/04/13 09:09:40 anna Exp $
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
import se.idega.util.PIDChecker;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.User;
import com.idega.user.presentation.UserSearcher;
import com.idega.util.IWTimestamp;


/**
 * <p>
 * TODO anna Describe Type CitizenSearcher
 * </p>
 *  Last modified: $Date: 2005/04/13 09:09:40 $ by $Author: anna $
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.3 $
 */
public class CitizenSearcher extends UserSearcher {

	protected Integer processSave(IWContext iwc, String firstName, String middleName, String lastName, String personalID) throws CreateException {
		if (personalID.length() != 12 || !PIDChecker.getInstance().isValid(personalID, true)) {
			throw new CreateException(getLocalizedString("not_a_valid_personal_id", "The personal ID is invalid or too short", iwc));
		}
		
		GenderHome home = null;
		try {
			home = (GenderHome) IDOLookup.getHome(Gender.class);
		}
		catch (IDOLookupException e) {
			//throw new FinderException(e.getMessage());
		}
		
		
		Gender gender = null;
		if (PIDChecker.getInstance().isFemale(personalID)) {
			try {
				gender = home.getFemaleGender();
			}
			catch(FinderException fe) {
				
			}
			
		}
		else {
			try {
				gender = home.getMaleGender();
			}
				catch(FinderException fe) {
				
			}
		}
		
		//Date dateOfBirth = PIDChecker.getInstance().getDateFromPersonalID(newUserPersonalID);
		IWTimestamp dateOfBirth = new IWTimestamp(PIDChecker.getInstance().getDateFromPersonalID(personalID));
		
		try {
			User user = getUserBusiness(iwc).createUser(firstName, middleName, lastName, personalID, gender, dateOfBirth);
			return (Integer) user.getPrimaryKey();
		}
		catch(RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
}