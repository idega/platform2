/*
 * Created on Nov 4, 2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package se.agura.applications.vacation.business;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import se.agura.applications.vacation.data.VacationRequest;
import se.agura.applications.vacation.data.VacationRequestHome;
import se.agura.applications.vacation.data.VacationTime;
import se.agura.applications.vacation.data.VacationTimeHome;
import se.agura.applications.vacation.data.VacationType;
import se.agura.applications.vacation.data.VacationTypeHome;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author Anna
 */
public class VacationBusinessBean extends CaseBusinessBean  implements VacationBusiness{

	private VacationRequestHome getVacationRequestHome() {
		try {
			return (VacationRequestHome) IDOLookup.getHome(VacationRequest.class);
		}
		catch (IDOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	private VacationTimeHome getVacationTimeHome() {
		try {
			return (VacationTimeHome) IDOLookup.getHome(VacationTime.class);
		}
		catch (IDOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	private VacationTypeHome getVacationTypeHome() {
		try {
			return (VacationTypeHome) IDOLookup.getHome(VacationType.class);
		}
		catch (IDOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	public VacationRequest getVacationRequest(Object primaryKey) throws FinderException {
		return getVacationRequestHome().findByPrimaryKey(new Integer(primaryKey.toString()));
	}
	
	public VacationType getVacationType(Object primaryKey) throws FinderException {
		return getVacationTypeHome().findByPrimaryKey(new Integer(primaryKey.toString()));
	}
	
	public Collection getVacationTimes(VacationRequest vacation) {
		try {
			return getVacationTimeHome().findAllByVacationRequest(vacation);
		}
		catch (FinderException fe) {
			return new ArrayList();
		}
	}
	
	
	public void storeApplication(User user, Date fromDate, Date toDate, int ordinaryWorkingHours, VacationType type,
			String[] workingHours, Collection extraInfo, String comment) throws CreateException {
		storeApplication(null, user, fromDate, toDate, ordinaryWorkingHours, type, workingHours, extraInfo, comment);
	}

	public void storeApplication(Object pk, User user, Date fromDate, Date toDate, int ordinaryWorkingHours,
			VacationType type, String[] workingHours, Collection extraInfo, String comment) throws CreateException {
		VacationRequest application = null;
		if (pk != null) {
			try {
				application = getVacationRequestHome().findByPrimaryKey(pk);
				application.removeAllExtraTypeInformation();
				
				Collection times = getVacationTimeHome().findAllByVacationRequest(application);
				Iterator iter = times.iterator();
				while (iter.hasNext()) {
					VacationTime time = (VacationTime) iter.next();
					time.remove();
				}
			}
			catch (FinderException fe) {
				log(fe);
			}
			catch (EJBException e) {
				log(e);
			}
			catch (RemoveException e) {
				log(e);
			}
		}
		if (application == null) {
			application = getVacationRequestHome().create();
		}
		IWTimestamp stamp = new IWTimestamp();
		application.setUser(user);
		application.setFromDate(fromDate);
		application.setToDate(toDate);
		application.setCreatedDate(stamp.getDate());
		application.setOrdinaryWorkingHours(new Integer(ordinaryWorkingHours));
		application.setVacationType(type);
		application.setComment(comment);
		
		if (extraInfo != null) {
			Iterator iter = extraInfo.iterator();
			while (iter.hasNext()) {
				ExtraInformation info = (ExtraInformation) iter.next();
				application.setExtraTypeInformation(info.getKey(), info.getValue(), info.getType());
			}
		}
		application.store();

		stamp = new IWTimestamp(fromDate);
		VacationTime time = null;
		int week = -1;
		for (int i = 0; i < workingHours.length; i++) {
			int day = stamp.getDayOfWeek();
			if (week != stamp.getWeekOfYear() && !(stamp.getDayOfWeek() == Calendar.SUNDAY)) {
				week = stamp.getWeekOfYear();
				if (time != null) {
					time.store();
				}
				time = getVacationTimeHome().create();
			}
			if (week == -1 && (stamp.getDayOfWeek() == Calendar.SUNDAY)) {
				if (time != null) {
					time.store();
				}
				time = getVacationTimeHome().create();
			}
			int hours = Integer.parseInt(workingHours[i]);
			switch (day) {
				case 1:
					time.setSunday(hours);
					break;
				case 2:
					time.setMonday(hours);
					break;
				case 3:
					time.setTuesday(hours);
					break;
				case 4:
					time.setWednesday(hours);
					break;
				case 5:
					time.setThursday(hours);
					break;
				case 6:
					time.setFriday(hours);
					break;
				case 7:
					time.setSaturday(hours);
					break;
				case 8:
					time.setSunday(hours);
					break;
			}
			time.setVacationRequest(application);
			time.setYear(stamp.getYear());
			time.setWeekNumber(stamp.getWeekOfYear());
			stamp.addDays(1);
		}
		if (time != null) {
			time.store();
		}
	}
	
	public void approveApplication(VacationRequest vacation, User performer, String comment) {
		changeCaseStatus(vacation, getCaseStatusGranted().getStatus(), comment, performer, null);
	}
	
	public void rejectApplication(VacationRequest vacation, User performer, String comment) {
		changeCaseStatus(vacation, getCaseStatusDenied().getStatus(), comment, performer, null);
	}
	
	public void forwardApplication(VacationRequest vacation, User performer, Group handler, String comment) {
		changeCaseStatus(vacation, getCaseStatusMoved().getStatus(), comment, performer, handler);
	}
	
	public Collection getVacationTypes() {
		try {
			return getVacationTypeHome().findAll();
		}
		catch (FinderException fe) {
			return new ArrayList();
		}
	}
	
	public Collection getLogs(VacationRequest vacation) {
		try {
			return getCaseLogsByCase(vacation);
		}
		catch (FinderException fe) {
			return new ArrayList();
		}
	}
	
	public Map getExtraVacationTypeInformation(VacationType type) {
		return type.getMetaDataAttributes();
	}
	
	public String getExtraInformationType(VacationType type, String key) {
		return (String) type.getMetaDataTypes().get(key);
	}
}