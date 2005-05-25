/*
 * $Id: AdultEducationSessionBean.java,v 1.1 2005/05/25 13:06:37 laddi Exp $
 * Created on May 24, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.business;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoice;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOSessionBean;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2005/05/25 13:06:37 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class AdultEducationSessionBean extends IBOSessionBean  implements AdultEducationSession{
	
	private Object iSeasonPK = null;
	private SchoolSeason iSeason = null;
	
	private Object iChoicePK = null;
	private AdultEducationChoice iChoice = null;

	public SchoolSeason getSchoolSeason() {
		if (iSeason == null && iSeasonPK != null) {
			try {
				iSeason = getSchoolBusiness().getSchoolSeason(new Integer(iSeasonPK.toString()));
			}
			catch (RemoteException re) {
				iSeason = null;
			}
		}
		else if (iSeason == null) {
			try {
				iSeason = getSchoolBusiness().getSchoolSeasonHome().findNextSeason(getSchoolBusiness().getCategoryAdultEducation(), new IWTimestamp().getDate());
			}
			catch (FinderException fe) {
				//Nothing found...
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		return iSeason;
	}

	public void setSeason(Object seasonPK) {
		iSeasonPK = seasonPK;
		iSeason = null;
	}
	
	public AdultEducationChoice getChoice() {
		if (iChoice == null && iChoicePK != null) {
			try {
				iChoice = getAdultEducationBusiness().getChoice(new Integer(iChoicePK.toString()));
			}
			catch (FinderException fe) {
				fe.printStackTrace();
				iChoice = null;
			}
			catch (RemoteException re) {
				iChoice = null;
			}
		}
		return iChoice;
	}

	public void setChoice(Object choicePK) {
		iChoicePK = choicePK;
		iChoice = null;
	}

	private SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), SchoolBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private AdultEducationBusiness getAdultEducationBusiness() {
		try {
			return (AdultEducationBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), AdultEducationBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

}