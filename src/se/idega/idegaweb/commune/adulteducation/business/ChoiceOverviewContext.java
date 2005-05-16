/*
 * $Id: ChoiceOverviewContext.java,v 1.6 2005/05/16 16:05:29 laddi Exp $ Created
 * on 15.10.2004
 * 
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package se.idega.idegaweb.commune.adulteducation.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.adulteducation.AdultEducationConstants;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoice;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceReason;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourse;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationPersonalInfo;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.business.NoUserAddressException;
import com.idega.block.pdf.business.PrintingContextImpl;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLOutput;

/**
 * Last modified: $Date: 2005/05/16 16:05:29 $ by $Author: laddi $
 * 
 * @author <a href="mailto:aron@idega.com">aron </a>
 * @version $Revision: 1.6 $
 */
public class ChoiceOverviewContext extends PrintingContextImpl {

	protected IWBundle iwb;
	protected IWResourceBundle iwrb;

	public ChoiceOverviewContext(IWApplicationContext iwac, SchoolSeason season, User user, Locale locale) {
		try {
			init(iwac, season, user, locale);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private void init(IWApplicationContext iwac, SchoolSeason season, User user, Locale locale) throws RemoteException {
		Map props = new HashMap();

		props.put("iwb", getBundle(iwac));
		props.put("iwrb", getResourceBundle(iwac, locale));
		props.put("user", user);
		props.put("season", season);

		Collection reasons = null;
		String otherReason = null;
		StringBuffer courses = new StringBuffer();
		StringBuffer reasonsString = new StringBuffer();
		
		Collection choices = getBusiness(iwac).getChoices(user, season);
		Iterator iter = choices.iterator();
		while (iter.hasNext()) {
			AdultEducationChoice choice = (AdultEducationChoice) iter.next();
			AdultEducationCourse course = choice.getCourse();
			SchoolStudyPath path = course.getStudyPath();
			IWTimestamp start = new IWTimestamp(course.getStartDate());
			IWTimestamp end = new IWTimestamp(course.getEndDate());
			
			courses.append(path.getDescription()).append(", ").append(path.getPoints());
			courses.append(" (").append(start.getLocaleDate(locale, IWTimestamp.SHORT)).append(" - ").append(end.getLocaleDate(locale, IWTimestamp.SHORT)).append(")");
			
			try {
				reasons = choice.getReasons();
			}
			catch (IDORelationshipException ire) {
				ire.printStackTrace();
			}
			otherReason = choice.getOtherReason();

			if (reasons != null) {
				reasonsString.append(path.getDescription()).append(":\n");
				Iterator iterator = reasons.iterator();
				while (iterator.hasNext()) {
					AdultEducationChoiceReason reason = (AdultEducationChoiceReason) iterator.next();
					reasonsString.append(getResourceBundle(iwac, locale).getLocalizedString(reason.getLocalizedKey()));
					if (iterator.hasNext()) {
						reasonsString.append("\n");
					}
				}
			}
			if (otherReason != null) {
				if (reasons != null) {
					reasonsString.append("\n");
				}
				reasonsString.append(otherReason);
				if (iter.hasNext()) {
					reasonsString.append("\n\n");
				}
			}

			if (iter.hasNext()) {
				courses.append("\n");
			}
		}
		props.put("courses", courses.toString());
		props.put("reasons", reasonsString.toString());
		
		Address address = null;
		PostalCode code = null;
		try {
			CommuneUserBusiness userBuiz = getUserService(iwac);
			address = userBuiz.getPostalAddress(user);
			code = address.getPostalCode();
		}
		catch (NoUserAddressException e) {
			e.printStackTrace();
		}
		props.put("address", address != null ? address.getStreetAddress() : "");
		props.put("postalCode", code != null ? code.getPostalAddress() : "");
		
		Phone homePhone = null;
		try {
			homePhone = getUserService(iwac).getUsersHomePhone(user);
		}
		catch (NoPhoneFoundException npfe) {
			//No phone found...
		}
		props.put("homePhone", homePhone != null ? homePhone.getNumber() : "");
		
		Phone workPhone = null;
		try {
			workPhone = getUserService(iwac).getUsersWorkPhone(user);
		}
		catch (NoPhoneFoundException npfe) {
			//No phone found...
		}
		props.put("workPhone", workPhone != null ? workPhone.getNumber() : "");
		
		Phone mobilePhone = null;
		try {
			mobilePhone = getUserService(iwac).getUsersMobilePhone(user);
		}
		catch (NoPhoneFoundException npfe) {
			//No phone found...
		}
		props.put("mobilePhone", mobilePhone != null ? mobilePhone.getNumber() : "");
		
		Email email = null;
		try {
			email = getUserService(iwac).getUsersMainEmail(user);
		}
		catch (NoEmailFoundException npfe) {
			//No email found...
		}
		props.put("email", email != null ? email.getEmailAddress() : "");
		
		AdultEducationPersonalInfo info = null;
		try {
			info = getBusiness(iwac).getAdultEducationPersonalHome().findByUserId((Integer) user.getPrimaryKey());
		}
		catch (FinderException fe) {
			throw new IBORuntimeException("Student has no adult education information!!!");
		}
		
		props.put("swedish", info.getNativeThisCountry() ? "X" : "");
		props.put("otherNationality", !info.getNativeThisCountry() ? "X" : "");
		props.put("otherNation", info.getNativeCountryID() != -1 ? info.getNativeCountry().getName() : "");
		
		StringBuffer studies = new StringBuffer();
		boolean addComma = false;
		if (info.getEduA()) {
			studies.append(getResourceBundle(iwac, locale).getLocalizedString("persInfo.education_A"));
			addComma = true;
		}
		if (info.getEduB()) {
			if (addComma) {
				studies.append(", ");
			}
			studies.append(getResourceBundle(iwac, locale).getLocalizedString("persInfo.education_B"));
			addComma = true;
		}
		if (info.getEduC()) {
			if (addComma) {
				studies.append(", ");
			}
			studies.append(getResourceBundle(iwac, locale).getLocalizedString("persInfo.education_C"));
			addComma = true;
		}
		if (info.getEduD()) {
			if (addComma) {
				studies.append(", ");
			}
			studies.append(getResourceBundle(iwac, locale).getLocalizedString("persInfo.education_D"));
			addComma = true;
		}
		if (info.getEduE()) {
			if (addComma) {
				studies.append(", ");
			}
			studies.append(getResourceBundle(iwac, locale).getLocalizedString("persInfo.education_E"));
			addComma = true;
		}
		if (info.getEduF() != null) {
			if (addComma) {
				studies.append(", ");
			}
			studies.append(info.getEduF());
			addComma = true;
		}
		if (info.getEduGCountryID() != -1) {
			if (addComma) {
				studies.append("\n");
			}
			Country country = info.getEduGCountry();
			studies.append(info.getEduG()).append(" - ").append(country.getName()).append(" - ").append(getResourceBundle(iwac, locale).getLocalizedString("persInfo.education_G_Years")).append(info.getEduGYears());
		}
		props.put("studies", studies.toString());
		
		StringBuffer previousStudies = new StringBuffer();
		addComma = false;
		if (info.getEduHA()) {
			previousStudies.append(getResourceBundle(iwac, locale).getLocalizedString("persInfo.education_HA"));
			addComma = true;
		}
		if (info.getEduHB()) {
			if (addComma) {
				previousStudies.append(", ");
			}
			previousStudies.append(getResourceBundle(iwac, locale).getLocalizedString("persInfo.education_HB"));
			addComma = true;
		}
		if (info.getEduHC()) {
			if (addComma) {
				previousStudies.append(", ");
			}
			previousStudies.append(getResourceBundle(iwac, locale).getLocalizedString("persInfo.education_HC"));
			addComma = true;
		}
		if (info.getEduHCommune() != null) {
			previousStudies.append(" - ").append(info.getEduHCommune());
		}
		props.put("previousStudies", previousStudies.toString());
		
		String studying = "";
		if (info.getFulltime()) {
			studying = getResourceBundle(iwac, locale).getLocalizedString("persInfo.fulltime");
		}
		else {
			studying = getResourceBundle(iwac, locale).getLocalizedString("persInfo.parttime");
		}
		props.put("studying", studying);

		StringBuffer languages = new StringBuffer();
		addComma = false;
		if (info.getLangSFI()) {
			languages.append(getResourceBundle(iwac, locale).getLocalizedString("persInfo.langSFI"));
			addComma = true;
		}
		if (info.getLangSAS()) {
			if (addComma) {
				languages.append(", ");
			}
			languages.append(getResourceBundle(iwac, locale).getLocalizedString("persInfo.langSAS"));
			addComma = true;
		}
		if (info.getLangOTHER()) {
			if (addComma) {
				languages.append(", ");
			}
			languages.append(getResourceBundle(iwac, locale).getLocalizedString("persInfo.langOther"));
		}
		props.put("languages", languages.toString());
		
		String studySupport = "";
		if (info.getStudySupport()) {
			studySupport = getResourceBundle(iwac, locale).getLocalizedString("persInfo.study_support");
		}
		props.put("studySupport", studySupport);

		StringBuffer workSituation = new StringBuffer();
		addComma = false;
		if (info.getWorkUnEmploy()) {
			workSituation.append(getResourceBundle(iwac, locale).getLocalizedString("persInfo.work_unemployed"));
			addComma = true;
		}
		if (info.getWorkEmploy()) {
			if (addComma) {
				workSituation.append(", ");
			}
			workSituation.append(getResourceBundle(iwac, locale).getLocalizedString("persInfo.work_employed"));
			addComma = true;
		}
		if (info.getWorkKicked()) {
			if (addComma) {
				workSituation.append(", ");
			}
			workSituation.append(getResourceBundle(iwac, locale).getLocalizedString("persInfo.work_kicked"));
			addComma = true;
		}
		if (info.getWorkOther() != null) {
			if (addComma) {
				workSituation.append(", ");
			}
			workSituation.append(info.getWorkOther());
		}
		props.put("workSituation", workSituation.toString());
		
		addDocumentProperties(props);
		setResourceDirectory(new File(getResourcRealPath(getBundle(iwac), locale)));
		try {
			setTemplateStream(getTemplateUrlAsStream(getBundle(iwac), locale, "overview_template.xml", true));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected IWBundle getBundle(IWApplicationContext iwac) {
		if (iwb == null)
			iwb = iwac.getIWMainApplication().getBundle(getBundleIdentifier());
		return iwb;
	}

	protected IWResourceBundle getResourceBundle(IWApplicationContext iwac, Locale locale) {
		if (iwrb == null)
			iwrb = getBundle(iwac).getResourceBundle(locale);
		return iwrb;
	}

	protected String getTemplateUrl(IWBundle iwb, Locale locale, String name) {
		return getResourcRealPath(iwb, locale) + name;
	}

	protected String getResourceUrl(IWBundle iwb, Locale locale) {
		return getResourcRealPath(iwb, locale);
	}

	private String getResourcRealPath(IWBundle iwb, Locale locale) {
		if (locale != null)
			return iwb.getResourcesRealPath(locale) + "/print/";
		else
			return iwb.getResourcesRealPath() + "/print/";
	}

	protected FileInputStream getTemplateUrlAsStream(IWBundle iwb, Locale locale, String name, boolean createIfNotExists) throws IOException {
		File template = new File(getTemplateUrl(iwb, locale, name));
		if (!template.exists() && createIfNotExists)
			createTemplateFile(template);
		return new FileInputStream(template);
	}

	private void createTemplateFile(File file) throws IOException {
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);

		XMLOutput xmlOutput = new XMLOutput("  ", true);
		xmlOutput.setLineSeparator(System.getProperty("line.separator"));
		xmlOutput.setTextNormalize(true);
		xmlOutput.setEncoding("iso-8859-1");
		XMLDocument doc = getTemplateXMLDocument();
		xmlOutput.output(doc, fos);
		fos.close();

	}

	protected XMLDocument getTemplateXMLDocument() {
		XMLDocument doc = getBasicXMLDocument();
		XMLElement document = doc.getRootElement();
		XMLElement subject = new XMLElement("paragraph");
		subject.addContent("${msg.subject}");
		document.addContent(subject);
		XMLElement body = new XMLElement("paragraph");
		body.setAttribute("halign", "justified");
		body.addContent("${msg.body}");
		document.addContent(body);
		return doc;
	}

	protected XMLDocument getBasicXMLDocument() {
		XMLElement document = new XMLElement("document");
		document.setAttribute("size", "A4");
		document.setAttribute("margin-left", "25");
		document.setAttribute("margin-right", "25");
		document.setAttribute("margin-top", "25");
		document.setAttribute("margin-bottom", "25");
		XMLDocument doc = new XMLDocument(document);

		return doc;
	}

	protected String getBundleIdentifier() {
		return AdultEducationConstants.IW_BUNDLE_IDENTIFIER;
	}

	protected AdultEducationBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return (AdultEducationBusiness) IBOLookup.getServiceInstance(iwac, AdultEducationBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	protected CommuneUserBusiness getUserService(IWApplicationContext iwac) {
		try {
			return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwac, CommuneUserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}