/*
 * $Id: CitizenSearcher.java,v 1.1 2005/04/12 09:16:20 anna Exp $
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
 *  Last modified: $Date: 2005/04/12 09:16:20 $ by $Author: anna $
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public class CitizenSearcher extends UserSearcher {
	protected Integer processSave(IWContext iwc) {
		//UserBusiness business = getUserBusiness(iwc);
		String newUserFirstName = iwc.getParameter(NEW_USER_FIRST_NAME);
		String newUserMiddleName = iwc.getParameter(NEW_USER_MIDDLE_NAME);
		String newUserLastName = iwc.getParameter(NEW_USER_LAST_NAME);
		String newUserPersonalID = iwc.getParameter(NEW_USER_PERSONAL_ID);
		
		GenderHome home = null;
		try {
			home = (GenderHome) IDOLookup.getHome(Gender.class);
		}
		catch (IDOLookupException e) {
			//throw new FinderException(e.getMessage());
		}
		
		
		Gender gender = null;
		if (PIDChecker.getInstance().isFemale(newUserPersonalID)) {
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
		IWTimestamp dateOfBirth = new IWTimestamp(PIDChecker.getInstance().getDateFromPersonalID(newUserPersonalID));
		
		try {
			User user = getUserBusiness(iwc).createUser(newUserFirstName, newUserMiddleName, newUserLastName, newUserPersonalID, gender, dateOfBirth);
			return (Integer) user.getPrimaryKey();
		}
		catch(RemoteException re) {
			throw new IBORuntimeException(re);
		}
		catch(CreateException ce) {
			System.out.print("This user could not be created!" + ce.getMessage() );
			return null;
		}
	}

}
