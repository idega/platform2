/*
 * Created on Nov 4, 2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package se.agura.applications.vacation.business;

import java.io.File;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.agura.applications.business.ApplicationsBusinessBean;
import se.agura.applications.vacation.data.VacationRequest;
import se.agura.applications.vacation.data.VacationRequestHome;
import se.agura.applications.vacation.data.VacationTime;
import se.agura.applications.vacation.data.VacationTimeHome;
import se.agura.applications.vacation.data.VacationType;
import se.agura.applications.vacation.data.VacationTypeHome;

import com.idega.block.process.data.Case;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Email;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author Anna
 */
public class VacationBusinessBean extends ApplicationsBusinessBean implements VacationBusiness {

  private static String DEFAULT_SMTP_MAILSERVER="mail.agurait.com";
	private static String PROP_SYSTEM_SMTP_MAILSERVER="messagebox_smtp_mailserver";
	private static String PROP_MESSAGEBOX_FROM_ADDRESS="messagebox_from_mailaddress";
	private static String DEFAULT_MESSAGEBOX_FROM_ADDRESS="messagebox@idega.com";
	
	public String getLocalizedCaseDescription(Case theCase, Locale locale) {
		VacationRequest request = getVacationApplicationInstance(theCase);
		VacationType type = request.getVacationType();
		Object[] arguments = { getLocalizedString(type.getLocalizedKey(), type.getTypeName()) };

		String desc = super.getLocalizedCaseDescription(theCase, locale);
		return MessageFormat.format(desc, arguments);
	}

	protected VacationRequest getVacationApplicationInstance(Case theCase) throws RuntimeException {
		String caseCode = "unreachable";
		try {
			caseCode = theCase.getCode();
			if (VacationConstants.CASE_CODE_KEY.equals(caseCode)) {
				return this.getVacationRequest(theCase.getPrimaryKey());
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		throw new ClassCastException("Case with casecode: " + caseCode + " cannot be converted to a vacation request");
	}

	/* (non-Javadoc)
	 * @see com.idega.block.process.business.CaseBusiness#getPrimaryKeyParameter()
	 */
	public String getPrimaryKeyParameter() {
		return VacationConstants.PARAMETER_PRIMARY_KEY;
	}
	
	protected UserBusiness getUserBusiness() {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
		}
		catch (IBOLookupException ible) {
			throw new IBORuntimeException(ible);
		}
	}

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

	public void storeApplication(User user, Date fromDate, Date toDate, int ordinaryWorkingHours, VacationType type, String[] workingHours, Collection extraInfo, String comment) throws CreateException {
		storeApplication(null, user, fromDate, toDate, ordinaryWorkingHours, type, workingHours, extraInfo, comment);
	}

	public void storeApplication(Object pk, User user, Date fromDate, Date toDate, int ordinaryWorkingHours, VacationType type, String[] workingHours, Collection extraInfo, String comment) throws CreateException {
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
		Group handlerGroup = getParentGroup(user);
		application.setOwner(user);
		application.setUser(user);
		if (handlerGroup != null) {
			application.setHandler(handlerGroup);
		}
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

		if (workingHours != null) {
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
	}

	public void approveApplication(VacationRequest vacation, User performer, String comment, boolean hasCompensation) {
		vacation.setSalaryCompensation(hasCompensation);
		changeCaseStatus(vacation, getCaseStatusGranted().getStatus(), comment, performer, null);
	}

	public void rejectApplication(VacationRequest vacation, User performer, String comment) {
		changeCaseStatus(vacation, getCaseStatusDenied().getStatus(), comment, performer, null);
	}

	public void closeApplication(VacationRequest vacation, User performer) {
		changeCaseStatus(vacation, getCaseStatusInactive().getStatus(), performer);
	}

	public void forwardApplication(VacationRequest vacation, User performer, Group handlerGroup, User handler, String comment, boolean hasCompensation) {
		vacation.setSalaryCompensation(hasCompensation);
		changeCaseStatus(vacation, getCaseStatusMoved().getStatus(), comment, performer, handlerGroup);

		try {
			Email email = getUserBusiness().getUsersMainEmail(handler);
			if (email != null) {
				sendMessage(email.getEmailAddress(), "", "", null);
			}
		}
		catch (NoEmailFoundException nefe) {
			// Nothing done...
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
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

	private void sendMessage(String email, String subject, String body, File attachment) {
		String receiver = email.trim();
		String mailServer = DEFAULT_SMTP_MAILSERVER;
		String fromAddress = DEFAULT_MESSAGEBOX_FROM_ADDRESS;
		try {
			IWBundle iwb = getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
			mailServer = iwb.getProperty(PROP_SYSTEM_SMTP_MAILSERVER, DEFAULT_SMTP_MAILSERVER);
			fromAddress = iwb.getProperty(PROP_MESSAGEBOX_FROM_ADDRESS, DEFAULT_MESSAGEBOX_FROM_ADDRESS);
		}
		catch (Exception e) {
			System.err.println("MessageBusinessBean: Error getting mail property from bundle");
			e.printStackTrace();
		}

		try {
			if (attachment == null) {
				com.idega.util.SendMail.send(fromAddress, receiver, "", "", mailServer, subject, body);
			}
			else {
				com.idega.util.SendMail.send(fromAddress, receiver, "", "", mailServer, subject, body, attachment);
			}
		}
		catch (javax.mail.MessagingException me) {
			System.err.println("Error sending mail to address: " + email + " Message was: " + me.getMessage());
		}
	}
	
	public Group getParentGroup(User user) {
		Group primaryGroup = user.getPrimaryGroup();
		if (primaryGroup != null) {
			Group parentGroup = (Group) primaryGroup.getParentNode();
			return parentGroup;
		}
		return null;
	}

	public Map getExtraVacationTypeInformation(VacationType type) {
		return type.getMetaDataAttributes();
	}

	public String getExtraInformationType(VacationType type, String key) {
		return (String) type.getMetaDataTypes().get(key);
	}
}