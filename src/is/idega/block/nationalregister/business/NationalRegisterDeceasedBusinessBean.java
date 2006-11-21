/*
 * $Id: NationalRegisterDeceasedBusinessBean.java,v 1.1.2.1 2006/11/21 23:35:25 idegaweb Exp $
 * Created on Nov 21, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.nationalregister.business;

import is.idega.block.nationalregister.data.NationalRegisterDeceased;
import is.idega.block.nationalregister.data.NationalRegisterDeceasedHome;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;

public class NationalRegisterDeceasedBusinessBean extends IBOServiceBean implements NationalRegisterDeceasedBusiness{
	
	public NationalRegisterDeceased getEntryBySSN(String ssn) {
		try {
			Collection c = getNationalRegisterDeceasedHome().findAllBySSN(ssn);

			if (c != null) {
				Iterator it = c.iterator();
				if (it.hasNext()) {
					return (NationalRegisterDeceased) it.next();
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace(System.err);
		}
		catch (FinderException e) {
			e.printStackTrace(System.err);
		}

		return null;
	}
	
	public boolean updateEntry(
			String symbol,
			String ssn,
			String dateOfDeath,
			String name,
			String street,
			String commune,
			String gender,
			String maritialStatus,
			String spouseSSN) {
				
			try {
				NationalRegisterDeceased deceasedReg = getEntryBySSN(ssn);
				if (deceasedReg == null) {
					deceasedReg = getNationalRegisterDeceasedHome().create();
				} 

				deceasedReg.setSymbol(symbol);
				deceasedReg.setSSN(ssn);
				deceasedReg.setDateOfDeath(dateOfDeath);
				deceasedReg.setName(name);
				deceasedReg.setStreet(street);
				deceasedReg.setCommune(commune);
				deceasedReg.setGender(gender);
				deceasedReg.setMaritalStatus(maritialStatus);
				deceasedReg.setSpouseSSN(spouseSSN);
				
				deceasedReg.store();
			}
			catch (CreateException e) {
				e.printStackTrace();
				return false;
			}

			return true;
		}
	
	protected NationalRegisterDeceasedHome getNationalRegisterDeceasedHome() {
		NationalRegisterDeceasedHome home = null;
		try {
			home = (NationalRegisterDeceasedHome) IDOLookup.getHome(NationalRegisterDeceased.class);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		return home;
	}
}
