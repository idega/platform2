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
import com.idega.block.process.data.CaseLog;
import com.idega.block.process.data.CaseStatus;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Email;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.FileUtil;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * @author Anna
 */
public class VacationBusinessBean extends ApplicationsBusinessBean implements VacationBusiness {

	protected static final String IW_VACATION_BUNDLE_IDENTIFIER = "se.agura.applications.vacation";
	private static String PROP_SALARY_DEPARTMENT_EMAIL = "salary_department_mailaddress";
	private static String PROP_SALARY_DEPARTMENT_EMAIL_CC = "salary_department_mailaddress_cc";

	protected String getBundleIdentifier() {
		return IW_VACATION_BUNDLE_IDENTIFIER;
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.block.process.business.CaseBusiness#getPrimaryKeyParameter()
	 */
	public String getPrimaryKeyParameter() {
		return VacationConstants.PARAMETER_PRIMARY_KEY;
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

	public void storeApplication(User user, Date fromDate, Date toDate, int ordinaryWorkingHours, VacationType type,
			String[] workingHours, Collection extraInfo, String comment, Locale locale) throws CreateException {
		storeApplication(null, user, fromDate, toDate, ordinaryWorkingHours, type, workingHours, extraInfo, comment, locale);
	}

	public void storeApplication(Object pk, User user, Date fromDate, Date toDate, int ordinaryWorkingHours,
			VacationType type, String[] workingHours, Collection extraInfo, String comment, Locale locale)
			throws CreateException {
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
			stamp = new IWTimestamp(locale, fromDate);
			VacationTime time = null;
			int week = -1;
			for (int i = 0; i < workingHours.length; i++) {
				int day = stamp.getDayOfWeek();
				if (week != stamp.getWeekOfYear()) {
					week = stamp.getWeekOfYear();
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
		IWBundle iwb = getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		String email = iwb.getProperty(PROP_SALARY_DEPARTMENT_EMAIL, "helen.overgaard@svenskakyrkan.se");
		String cc = iwb.getProperty(PROP_SALARY_DEPARTMENT_EMAIL_CC, "ylva.jacobsson@svenskakyrkan.se");
		if (email != null) {
			VacationType type = vacation.getVacationType();
			User user = vacation.getUser();
			Locale locale = getIWApplicationContext().getApplicationSettings().getDefaultLocale();
			IWTimestamp from = new IWTimestamp(vacation.getFromDate());
			IWTimestamp to = new IWTimestamp(vacation.getToDate());
			IWTimestamp created = new IWTimestamp(vacation.getCreatedDate());
			String vacationComment = vacation.getComment();
			Group parish = getUserParish(user);
			Map extraInfo = getExtraVacationTypeInformation(type);
			Collection times = getVacationTimes(vacation);
			File attachment = null;
			
			StringBuffer metadata = new StringBuffer();
			if (extraInfo != null && extraInfo.size() > 0) {
				Iterator iter = extraInfo.keySet().iterator();
				while (iter.hasNext()) {
					String key = (String) iter.next();
					String metaType = getExtraInformationType(type, key);
					String value = vacation.getExtraTypeInformation(key);
					if (value != null) {
						if (!metaType.equals("com.idega.block.media.presentation.FileChooser")) {
							metadata.append(getLocalizedString("vacation_type_metadata." + key, key)).append(": ");
						}
						if (metaType.equals("com.idega.presentation.ui.TextArea")
								|| metaType.equals("com.idega.presentation.ui.TextInput")) {
							metadata.append(value);
						}
						else if (metaType.equals("com.idega.presentation.ui.RadioButton")) {
							metadata.append(getLocalizedString("vacation_type_metadata_boolean." + value, value));
						}
						else if (metaType.equals("com.idega.block.media.presentation.FileChooser")) {
							try {
								ICFile file = ((ICFileHome) IDOLookup.getHome(ICFile.class)).findByPrimaryKey(new Integer(value));
								attachment = FileUtil.streamToFile(file.getFileValue(), getBundle().getResourcesRealPath(), file.getName());
							}
							catch (IDOLookupException ile) {
								throw new IBORuntimeException(ile);
							}
							catch (FinderException fe) {
								fe.printStackTrace(System.err);
							}
						}
						if (iter.hasNext()) {
							metadata.append("\n");
						}
					}
				}
			}
			
			StringBuffer hoursAndDays = new StringBuffer();
			if (times.size() > 0) {
				hoursAndDays.append(getLocalizedString("vacation.time.week", "Week")).append("\t");
				hoursAndDays.append(getLocalizedString("vacation.time.monday", "Mo")).append("\t");
				hoursAndDays.append(getLocalizedString("vacation.time.tuesday", "Tu")).append("\t");
				hoursAndDays.append(getLocalizedString("vacation.time.wednesday", "We")).append("\t");
				hoursAndDays.append(getLocalizedString("vacation.time.thursday", "th")).append("\t");
				hoursAndDays.append(getLocalizedString("vacation.time.friday", "Fr")).append("\t");
				hoursAndDays.append(getLocalizedString("vacation.time.saturday", "Sa")).append("\t");
				hoursAndDays.append(getLocalizedString("vacation.time.sunday", "Su")).append("\n");
				Iterator iter = times.iterator();
				while (iter.hasNext()) {
					VacationTime time = (VacationTime) iter.next();
					hoursAndDays.append(time.getWeekNumber()).append("\t");
					hoursAndDays.append(time.getMonday() > 0 ? time.getMonday() : 0).append("\t");
					hoursAndDays.append(time.getTuesday() > 0 ? time.getTuesday() : 0).append("\t");
					hoursAndDays.append(time.getWednesday() > 0 ? time.getWednesday() : 0).append("\t");
					hoursAndDays.append(time.getThursday() > 0 ? time.getThursday() : 0).append("\t");
					hoursAndDays.append(time.getFriday() > 0 ? time.getFriday() : 0).append("\t");
					hoursAndDays.append(time.getSaturday() > 0 ? time.getSaturday() : 0).append("\t");
					hoursAndDays.append(time.getSunday() > 0 ? time.getSunday() : 0).append("\t");
					hoursAndDays.append("\n");
				}
			}
			
			StringBuffer logBuffer = new StringBuffer();
			hoursAndDays.append(getLocalizedString("vacation.message_to_employee", "Messages to employee")).append(":\n");
			Collection logs = getLogs(vacation);
			if (logs != null) {
				Iterator iter = logs.iterator();
				while (iter.hasNext()) {
					CaseLog log = (CaseLog) iter.next();
					User commenter = log.getPerformer();
					String logComment = log.getComment();
					
					logBuffer.append(logComment).append("\n").append("- ").append(commenter.getName());
					if (iter.hasNext()) {
						logBuffer.append("\n\n");
					}
				}
			}

			Object[] arguments = { user.getName(), PersonalIDFormatter.format(user.getPersonalID(), locale),
					getLocalizedString(type.getLocalizedKey(), type.getTypeName()),
					from.getLocaleDate(locale, IWTimestamp.SHORT), to.getLocaleDate(locale, IWTimestamp.SHORT),
					parish != null ? parish.getName() : "xxx", hoursAndDays.toString(), 
					vacationComment, created.getLocaleDate(locale, IWTimestamp.SHORT), metadata.toString(), logBuffer.toString() };
			sendMessage(
					email,
					cc,
					getLocalizedString("vacation_application.accepted_subject", "Vacation application accepted"),
					MessageFormat.format(
							getLocalizedString(
									"vacation_application.accepted_body",
									"A vacation application has been accepted for:\nName:\t {0},\nPersonal number:\t {1},\nParish:\t {5}.\n\nThe vacation period is:\nfrom\t {3}\n to\t{4}\n\n{6}\n\nVacation type:\t{2}\n{9}\nMotivation:\t{7}\nRequested vacation date:\t{8}\nComment to employee:\t{10}"),
							arguments), attachment);
		}
	}	public void rejectApplication(VacationRequest vacation, User performer, String comment) {
		changeCaseStatus(vacation, getCaseStatusDenied().getStatus(), comment, performer, null);
	}

	public void closeApplication(VacationRequest vacation, User performer) {
		changeCaseStatus(vacation, getCaseStatusInactive().getStatus(), performer);
	}

	public void forwardApplication(VacationRequest vacation, User performer, Group handlerGroup, User handler,
			String comment, boolean hasCompensation) {
		vacation.setSalaryCompensation(hasCompensation);
		changeCaseStatus(vacation, getCaseStatusMoved().getStatus(), comment, performer, handlerGroup);
		try {
			Email email = getUserBusiness().getUsersMainEmail(handler);
			if (email != null) {
				sendMessage(email.getEmailAddress(), null, getLocalizedString("vacation_application.forwarded_subject",
						"Acceptance of a vacation application forwarded to you"), getLocalizedString(
						"vacation_application.forward_body", "You have to handle it on 'My page' on the intranet."), null);
			}
		}
		catch (NoEmailFoundException nefe) {
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

	public boolean canDeleteCase(Case theCase) {
		CaseStatus status = theCase.getCaseStatus();
		if (status.getStatus().equals(getCaseStatusOpenString())
				|| status.getStatus().equals(getCaseStatusMoved().getStatus())) {
			return true;
		}
		return false;
	}
}