package se.idega.idegaweb.commune.childcare.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ejb.CreateException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.business.Constants;
import se.idega.idegaweb.commune.business.NoUserAddressException;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;

import com.idega.block.pdf.business.PrintingContextImpl;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.data.User;
import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLOutput;

/**
 * Used to create 
 * 
 * TODO: this and CancelFormContext should be refactored so both would use one parent class which both would than extend/implement
 * 
 * @author dainis
 */
public class ChildCareContractFormContext extends PrintingContextImpl {

	protected IWBundle iwb;

	protected IWResourceBundle iwrb;

	public ChildCareContractFormContext(IWApplicationContext iwac,
			ChildCareApplication application, Locale locale) {
		init(iwac, application, locale);
	}

	private void init(IWApplicationContext iwac,
			ChildCareApplication application, Locale locale) {
		Map props = new HashMap();

		props.put("iwb", getBundle(iwac));
		props.put("iwrb", getResourceBundle(iwac, locale));

		School provider = application.getProvider();
		User child = application.getChild();
		User user = application.getOwner();

		props.put("user", user);
		props.put("child", child);
		props.put("provider", provider);
		Address address = null;
		PostalCode code = null;
		try {
			CommuneUserBusiness userBuiz = getUserService(iwac);
			address = userBuiz.getPostalAddress(user);
			code = address.getPostalCode();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NoUserAddressException e) {
			try {
				CommuneUserBusiness userBuiz = getUserService(iwac);
				address = userBuiz.getAddressHome().create();
				address.setStreetName("");
				address.setStreetNumber("");

				code = userBuiz.getAddressBusiness().getPostalCodeHome()
						.create();
				code.setName("");
				code.setPostalCode("");
			} catch (CreateException ce) {
				ce.printStackTrace();
			} catch (RemoteException re) {
				re.printStackTrace();
			}
		}
		props.put("address", address);
		props.put("postalCode", code);
		props.put("application", application);

		addDocumentProperties(props);
		setResourceDirectory(new File(getResourcRealPath(getBundle(iwac),
				locale)));
		try {
			setTemplateStream(getTemplateUrlAsStream(getBundle(iwac), locale,
					"child_care_contract_form_template.xml", true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected IWBundle getBundle(IWApplicationContext iwac) {
		if (iwb == null)
			iwb = iwac.getIWMainApplication().getBundle(getBundleIdentifier());
		return iwb;
	}

	protected IWResourceBundle getResourceBundle(IWApplicationContext iwac,
			Locale locale) {
		if (iwrb == null)
			getBundle(iwac).getResourceBundle(locale);
		return iwrb;
	}

	protected String getTemplateUrl(IWBundle iwb, Locale locale, String name) {
		return getResourcRealPath(iwb, locale) + name;
	}

	protected String getResourceUrl(IWBundle iwb, Locale locale) {
		return getResourcRealPath(iwb, locale);
	}

	protected String getResourcRealPath(IWBundle iwb, Locale locale) {
		if (locale != null)
			return iwb.getResourcesRealPath(locale) + "/print/";
		else
			return iwb.getResourcesRealPath() + "/print/";
	}

	protected FileInputStream getTemplateUrlAsStream(IWBundle iwb,
			Locale locale, String name, boolean createIfNotExists)
			throws IOException {
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
		return Constants.IW_BUNDLE_IDENTIFIER;
	}

	protected ChildCareBusiness getChildCareService(IWApplicationContext iwac)
			throws IBOLookupException {
		return (ChildCareBusiness) IBOLookup.getServiceInstance(iwac,
				ChildCareBusiness.class);
	}

	protected CommuneUserBusiness getUserService(IWApplicationContext iwac)
			throws IBOLookupException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwac,
				CommuneUserBusiness.class);
	}

}