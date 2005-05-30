/*
 * $Id: AdultEducationSessionBean.java,v 1.5 2005/05/30 11:07:02 laddi Exp $
 * Created on May 24, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.business;

import java.rmi.RemoteException;
import java.sql.Date;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoice;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2005/05/30 11:07:02 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.5 $
 */
public class AdultEducationSessionBean extends IBOSessionBean  implements AdultEducationSession{
	
	private Object iSeasonPK = null;
	private SchoolSeason iSeason = null;
	
	private Object iChoicePK = null;
	private AdultEducationChoice iChoice = null;
	
	private String iUserUniqueID = null;
	private User iUser = null;
	private Object iUserPK = null;
	
	private Object iSchoolTypePK = null;
	private SchoolType iSchoolType = null;
	
	private Date iDate = null;
	
	private int iSort = -1;

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
	
	public SchoolType getSchoolType() {
		if (iSchoolType == null && iSchoolTypePK != null) {
			try {
				iSchoolType = getSchoolBusiness().getSchoolType(new Integer(iSchoolTypePK.toString()));
			}
			catch (RemoteException re) {
				iSchoolType = null;
			}
		}
		return iSchoolType;
	}

	public void setSchoolType(Object schoolTypePK) {
		iSchoolTypePK = schoolTypePK;
		iSchoolType= null;
	}
	
	public Date getDate() {
		if (iDate == null) {
			iDate = new IWTimestamp().getDate();
		}
		return iDate;
	}
	
	public void setDate(Date date) {
		iDate = date;
	}
	
	public int getSort() {
		return iSort;
	}
	
	public void setSort(int sort) {
		iSort = sort;
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

	public User getStudent() {
		if (iUser == null && iUserPK != null) {
			try {
				iUser = getUserBusiness().getUser(new Integer(iUserPK.toString()));
			}
			catch (RemoteException re) {
				iUser = null;
			}
		}
		return iUser;
	}

	public void setStudent(String userPK) {
		iUserPK = userPK;
		iUser = null;
	}

	public void setStudentUniqueID(String userUniqueID) {
		iUserUniqueID = userUniqueID;
		try {
			iUser = getUserBusiness().getUserByUniqueId(iUserUniqueID);
			iUserPK = iUser.getPrimaryKey();
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			iUser = null;
			iUserPK = null;
		}
		catch (RemoteException re) {
			iUser = null;
			iUserPK = null;
		}
	}

	private CommuneUserBusiness getUserBusiness() {
		try {
			return (CommuneUserBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), CommuneUserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
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