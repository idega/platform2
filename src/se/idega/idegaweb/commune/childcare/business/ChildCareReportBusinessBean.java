/*
 * Created on 22.3.2004
 */
package se.idega.idegaweb.commune.childcare.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.care.business.CareConstants;
import se.idega.idegaweb.commune.care.data.ApplicationPriority;
import se.idega.idegaweb.commune.care.data.ApplicationPriorityHome;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableData;
import com.idega.block.datareport.util.ReportableField;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOSessionBean;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;

/**
 * @author laddi
 */
public class ChildCareReportBusinessBean extends IBOSessionBean implements ChildCareReportBusiness {

	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";

	private IWBundle _iwb;
	private IWResourceBundle _iwrb;

	private void initializeBundlesIfNeeded() {
		if (_iwb == null) {
			_iwb = this.getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		}
		_iwrb = _iwb.getResourceBundle(this.getUserContext().getCurrentLocale());
	}

	public ReportableCollection getChildCareReport(Integer numberOfWeeks, Integer numberOfMonths, Object areaID, Boolean firstHandOnly) {
		initializeBundlesIfNeeded();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		List childrenList = new ArrayList();
		
		ReportableCollection reportCollection = new ReportableCollection();

		ReportableField personalID = new ReportableField(FIELD_PERSONAL_ID, String.class);
		personalID.setLocalizedName(getLocalizedString(FIELD_PERSONAL_ID, "Personal ID"), currentLocale);
		reportCollection.addField(personalID);

		ReportableField name = new ReportableField(FIELD_NAME, String.class);
		name.setLocalizedName(getLocalizedString(FIELD_NAME, "Name"), currentLocale);
		reportCollection.addField(name);

		ReportableField address = new ReportableField(FIELD_ADDRESS, String.class);
		address.setLocalizedName(getLocalizedString(FIELD_ADDRESS, "Address"), currentLocale);
		reportCollection.addField(address);

		ReportableField zipCode = new ReportableField(FIELD_ZIP_CODE, String.class);
		zipCode.setLocalizedName(getLocalizedString(FIELD_ZIP_CODE, "Zip code"), currentLocale);
		reportCollection.addField(zipCode);

		ReportableField area = new ReportableField(FIELD_AREA, String.class);
		area.setLocalizedName(getLocalizedString(FIELD_AREA, "Area"), currentLocale);
		reportCollection.addField(area);

		ReportableField email = new ReportableField(FIELD_EMAIL, String.class);
		email.setLocalizedName(getLocalizedString(FIELD_EMAIL, "E-mail"), currentLocale);
		reportCollection.addField(email);

		ReportableField phone = new ReportableField(FIELD_PHONE, String.class);
		phone.setLocalizedName(getLocalizedString(FIELD_PHONE, "Phone"), currentLocale);
		reportCollection.addField(phone);

		ReportableField providers = new ReportableField(FIELD_PROVIDER, String.class);
		providers.setLocalizedName(getLocalizedString(FIELD_PROVIDER, "Provider"), currentLocale);
		reportCollection.addField(providers);

		ReportableField status = new ReportableField(FIELD_STATUS, String.class);
		status.setLocalizedName(getLocalizedString(FIELD_STATUS, "Status"), currentLocale);
		reportCollection.addField(status);

		ReportableField queueDate = new ReportableField(FIELD_QUEUE_DATE, String.class);
		queueDate.setLocalizedName(getLocalizedString(FIELD_QUEUE_DATE, "Queue Date"), currentLocale);
		reportCollection.addField(queueDate);

		ReportableField placementDate = new ReportableField(FIELD_PLACEMENT_DATE, String.class);
		placementDate.setLocalizedName(getLocalizedString(FIELD_PLACEMENT_DATE, "Placement date"), currentLocale);
		reportCollection.addField(placementDate);

		ReportableField removedDate = new ReportableField(FIELD_REMOVED_DATE, String.class);
		removedDate.setLocalizedName(getLocalizedString(FIELD_REMOVED_DATE, "Removed date"), currentLocale);
		reportCollection.addField(removedDate);

		int numberOfChoices = 0;
		try {
			Collection children = getChildCareBusiness().findSentInAndRejectedApplicationsByArea(areaID, numberOfMonths.intValue(), numberOfWeeks.intValue(), firstHandOnly.booleanValue(), CareConstants.CASE_CODE_KEY);
			if (children != null) {
				Iterator iter = children.iterator();
				while (iter.hasNext()) {
					ChildCareApplication application = (ChildCareApplication) iter.next();
					numberOfChoices++;
					/*if (getChildCareBusiness().hasActiveApplications(application.getChildId(), getChildCareBusiness().getChildCareCaseCode(), new IWTimestamp().getDate())) {
						continue;
					}*/
					School provider = application.getProvider();
					IWTimestamp queue = new IWTimestamp(application.getQueueDate());
					IWTimestamp placement = new IWTimestamp(application.getFromDate());
					boolean addRejected = false;

					ReportableData data = new ReportableData();
					if (!childrenList.contains(new Integer(application.getChildId()))) {
						addRejected = true;
						User user = application.getChild();
						Address homeAddress = getUserBusiness().getUsersMainAddress(user);
						Phone homePhone = getUserBusiness().getChildHomePhone(user);
						User parent = getUserBusiness().getCustodianForChild(user);
						Email mail = null;
						if (parent != null) {
							mail = getUserBusiness().getEmail(parent);
						}

						data.addData(personalID, PersonalIDFormatter.format(user.getPersonalID(), currentLocale));
						Name userName = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
						data.addData(name, userName.getName(getIWMainApplication().getSettings().getDefaultLocale(), true));

						if (homeAddress != null) {
							data.addData(address, homeAddress.getStreetAddress());
							PostalCode code = homeAddress.getPostalCode();
							
							if (code != null) {
								data.addData(zipCode, code.getPostalCode());
								data.addData(area, code.getName());
							}
						}
						if (mail != null) {
								data.addData(email, mail.getEmailAddress());
						}
						if (homePhone != null) {
								data.addData(phone, homePhone.getNumber());
						}
						childrenList.add(new Integer(application.getChildId()));
					}
					else {
						addRejected = false;
						data.addData(personalID, "");
						data.addData(name, "");
						data.addData(address, "");
						data.addData(zipCode, "");
						data.addData(area, "");
						data.addData(email, "");
						data.addData(phone, "");
					}
					
					data.addData(providers, provider.getSchoolName());
					data.addData(status, getChildCareBusiness().getStatusString(application.getApplicationStatus()));
					data.addData(queueDate, queue.getLocaleDate(currentLocale, IWTimestamp.SHORT));
					data.addData(placementDate, placement.getLocaleDate(currentLocale, IWTimestamp.SHORT));
					reportCollection.add(data);
					
					if (addRejected) {
						try {
							Collection rejected = getChildCareBusiness().findRejectedApplicationsByChild(application.getChildId());
							Iterator iterator = rejected.iterator();
							while (iterator.hasNext()) {
								ChildCareApplication rejectedApplication = (ChildCareApplication) iterator.next();
								if (getChildCareBusiness().wasRejectedByParent(rejectedApplication)) {
									provider = rejectedApplication.getProvider();
									queue = new IWTimestamp(rejectedApplication.getQueueDate());
									placement = new IWTimestamp(rejectedApplication.getFromDate());
									IWTimestamp rejectedDate = new IWTimestamp(rejectedApplication.getRejectionDate());
	
									data = new ReportableData();
									data.addData(providers, provider.getSchoolName());
									data.addData(status, getChildCareBusiness().getStatusString(rejectedApplication.getApplicationStatus()));
									data.addData(queueDate, queue.getLocaleDate(currentLocale, IWTimestamp.SHORT));
									data.addData(placementDate, placement.getLocaleDate(currentLocale, IWTimestamp.SHORT));
									data.addData(removedDate, rejectedDate.getLocaleDate(currentLocale, IWTimestamp.SHORT));
									reportCollection.add(data);
								}
							}
						}
						catch (FinderException fex) {
							//Nothing found...
						}
					}
				}
			}
		}
		catch (FinderException fe) {
			log(fe);
		}
		catch (RemoteException re) {
			log(re);
		}

		ReportableData count = new ReportableData();
		count.addData(personalID, getLocalizedString("number_of_choices", "Number of choices:") + " " +  String.valueOf(numberOfChoices));
		reportCollection.add(count);

		return reportCollection;
	}
	
	public ReportableCollection getRemovedReport(String fromDateOfBirth, String toDateOfBirth, Integer providerID, String fromDate, String toDate) {
		initializeBundlesIfNeeded();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		List childrenList = new ArrayList();
		
		ReportableCollection reportCollection = new ReportableCollection();

		ReportableField personalID = new ReportableField(FIELD_PERSONAL_ID, String.class);
		personalID.setLocalizedName(getLocalizedString(FIELD_PERSONAL_ID, "Personal ID"), currentLocale);
		reportCollection.addField(personalID);

		ReportableField name = new ReportableField(FIELD_NAME, String.class);
		name.setLocalizedName(getLocalizedString(FIELD_NAME, "Name"), currentLocale);
		reportCollection.addField(name);

		ReportableField address = new ReportableField(FIELD_ADDRESS, String.class);
		address.setLocalizedName(getLocalizedString(FIELD_ADDRESS, "Address"), currentLocale);
		reportCollection.addField(address);

		ReportableField zipCode = new ReportableField(FIELD_ZIP_CODE, String.class);
		zipCode.setLocalizedName(getLocalizedString(FIELD_ZIP_CODE, "Zip code"), currentLocale);
		reportCollection.addField(zipCode);

		ReportableField area = new ReportableField(FIELD_AREA, String.class);
		area.setLocalizedName(getLocalizedString(FIELD_AREA, "Area"), currentLocale);
		reportCollection.addField(area);

		ReportableField email = new ReportableField(FIELD_EMAIL, String.class);
		email.setLocalizedName(getLocalizedString(FIELD_EMAIL, "E-mail"), currentLocale);
		reportCollection.addField(email);

		ReportableField phone = new ReportableField(FIELD_PHONE, String.class);
		phone.setLocalizedName(getLocalizedString(FIELD_PHONE, "Phone"), currentLocale);
		reportCollection.addField(phone);

		ReportableField providers = new ReportableField(FIELD_PROVIDER, String.class);
		providers.setLocalizedName(getLocalizedString(FIELD_PROVIDER, "Provider"), currentLocale);
		reportCollection.addField(providers);

		ReportableField status = new ReportableField(FIELD_STATUS, String.class);
		status.setLocalizedName(getLocalizedString(FIELD_STATUS, "Status"), currentLocale);
		reportCollection.addField(status);

		ReportableField queueDate = new ReportableField(FIELD_QUEUE_DATE, String.class);
		queueDate.setLocalizedName(getLocalizedString(FIELD_QUEUE_DATE, "Queue Date"), currentLocale);
		reportCollection.addField(queueDate);

		ReportableField placementDate = new ReportableField(FIELD_PLACEMENT_DATE, String.class);
		placementDate.setLocalizedName(getLocalizedString(FIELD_PLACEMENT_DATE, "Placement date"), currentLocale);
		reportCollection.addField(placementDate);

		ReportableField removedDate = new ReportableField(FIELD_REMOVED_DATE, String.class);
		removedDate.setLocalizedName(getLocalizedString(FIELD_REMOVED_DATE, "Removed date"), currentLocale);
		reportCollection.addField(removedDate);

		int numberOfChoices = 0;
		try {
			Collection children = getChildCareBusiness().getRejectedApplicationsByProvider(providerID, fromDateOfBirth, toDateOfBirth, fromDate, toDate);
			if (children != null) {
				Iterator iter = children.iterator();
				while (iter.hasNext()) {
					ChildCareApplication application = (ChildCareApplication) iter.next();
					if (getChildCareBusiness().wasRejectedByParent(application)) {
						continue;
					}
					numberOfChoices++;
					School provider = application.getProvider();
					IWTimestamp queue = new IWTimestamp(application.getQueueDate());
					IWTimestamp placement = new IWTimestamp(application.getFromDate());
					if (application.getRejectionDate() == null) {
						continue;
					}
					IWTimestamp removed = new IWTimestamp(application.getRejectionDate());

					ReportableData data = new ReportableData();
					if (!childrenList.contains(new Integer(application.getChildId()))) {
						User user = application.getChild();
						Address homeAddress = getUserBusiness().getUsersMainAddress(user);
						Phone homePhone = getUserBusiness().getChildHomePhone(user);
						User parent = getUserBusiness().getCustodianForChild(user);
						Email mail = null;
						if (parent != null) {
							mail = getUserBusiness().getEmail(parent);
						}

						data.addData(personalID, PersonalIDFormatter.format(user.getPersonalID(), currentLocale));
						Name userName = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
						data.addData(name, userName.getName(getIWMainApplication().getSettings().getDefaultLocale(), true));

						if (homeAddress != null) {
							data.addData(address, homeAddress.getStreetAddress());
							PostalCode code = homeAddress.getPostalCode();
							
							if (code != null) {
								data.addData(zipCode, code.getPostalCode());
								data.addData(area, code.getName());
							}
						}
						if (mail != null) {
								data.addData(email, mail.getEmailAddress());
						}
						if (homePhone != null) {
								data.addData(phone, homePhone.getNumber());
						}
						childrenList.add(new Integer(application.getChildId()));
					}
					else {
						data.addData(personalID, "");
						data.addData(name, "");
						data.addData(address, "");
						data.addData(zipCode, "");
						data.addData(area, "");
						data.addData(email, "");
						data.addData(phone, "");
					}
					
					data.addData(providers, provider.getSchoolName());
					data.addData(status, getChildCareBusiness().getStatusString(application.getApplicationStatus()));
					data.addData(queueDate, queue.getLocaleDate(currentLocale, IWTimestamp.SHORT));
					data.addData(placementDate, placement.getLocaleDate(currentLocale, IWTimestamp.SHORT));
					data.addData(removedDate, removed.getLocaleDate(currentLocale, IWTimestamp.SHORT));
					reportCollection.add(data);
				}
			}
		}
		catch (FinderException fe) {
			log(fe);
		}
		catch (RemoteException re) {
			log(re);
		}

		ReportableData count = new ReportableData();
		count.addData(personalID, getLocalizedString("number_of_choices", "Number of choices:") + " " +  String.valueOf(numberOfChoices));
		reportCollection.add(count);

		return reportCollection;
	}

	public ReportableCollection getPriorityReport(Integer providerID, String fromDate, String toDate) {
		initializeBundlesIfNeeded();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		ReportableCollection reportCollection = new ReportableCollection();

		ReportableField priorityDate = new ReportableField(FIELD_PRIORITY_DATE, String.class);
		priorityDate.setLocalizedName(getLocalizedString(FIELD_PRIORITY_DATE, "Priority Date"), currentLocale);
		reportCollection.addField(priorityDate);

		ReportableField providerName = new ReportableField(FIELD_PROVIDER_NAME, String.class);
		providerName.setLocalizedName(getLocalizedString(FIELD_PROVIDER_NAME, "Provider Name"), currentLocale);
		reportCollection.addField(providerName);

		ReportableField childName = new ReportableField(FIELD_CHILD_NAME, String.class);
		childName.setLocalizedName(getLocalizedString(FIELD_CHILD_NAME, "Child"), currentLocale);
		reportCollection.addField(childName);

		ReportableField personalId = new ReportableField(FIELD_PERSONAL_ID, String.class);
		personalId.setLocalizedName(getLocalizedString(FIELD_PERSONAL_ID, "Personal ID"), currentLocale);
		reportCollection.addField(personalId);

		ReportableField message = new ReportableField(FIELD_MESSAGE, String.class);
		personalId.setLocalizedName(getLocalizedString(FIELD_MESSAGE, "Message"), currentLocale);
		reportCollection.addField(personalId);

		int numberOfApplications = 0;
		try {
			ApplicationPriorityHome home = (ApplicationPriorityHome) this.getIDOHome(ApplicationPriority.class);
			Date from = fromDate != null && fromDate.length() > 0 ? new IWTimestamp(fromDate).getDate() : null; 
			Date to = toDate != null && toDate.length() > 0 ? new IWTimestamp(toDate).getDate() : null;
			if (providerID == null) {
				providerID = new Integer(-1);
			}
			Collection priorities = home.findByPeriodAndProvider(from, to, providerID.intValue());
			if (priorities != null) {
				Iterator iter = priorities.iterator();
				while (iter.hasNext()) {
					ApplicationPriority ap = (ApplicationPriority) iter.next();
					ChildCareApplication application = ap.getApplication();
					if (application == null) {
						continue;
					}
					School provider = application.getProvider();
					if (provider == null) {
						continue;
					}
					User child = application.getChild();
					if (child == null) {
						continue;
					}

					ReportableData data = new ReportableData();

					data.addData(priorityDate, (new IWTimestamp(ap.getPriorityDate())).getLocaleDate(currentLocale, IWTimestamp.SHORT));
					data.addData(providerName, provider.getName());
					data.addData(childName, child.getLastName() + ", " + child.getFirstName());
					data.addData(personalId, PersonalIDFormatter.format(child.getPersonalID(), currentLocale));
					data.addData(message, ap.getMessage());

					numberOfApplications++;

					reportCollection.add(data);
				}
			}
		}
		catch (FinderException fe) {
			log(fe);
		}
		catch (RemoteException re) {
			log(re);
		}

		ReportableData count = new ReportableData();
		count.addData(priorityDate, getLocalizedString("number_of_applications", "Number of applications:") + " " +  String.valueOf(numberOfApplications));
		reportCollection.add(count);

		return reportCollection;
	}

	private ChildCareBusiness getChildCareBusiness() {
		try {
			return (ChildCareBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), ChildCareBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
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

	private String getLocalizedString(String key, String defaultValue) {
		return _iwrb.getLocalizedString(PREFIX + key, defaultValue);
	}
}