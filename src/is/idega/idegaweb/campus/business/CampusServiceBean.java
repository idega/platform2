/*
 * Created on Dec 19, 2003
 *
 */
package is.idega.idegaweb.campus.business;

import com.idega.business.IBOServiceBean;
import com.idega.idegaweb.IWBundle;

/**
 * CampusServiceBean
 * @author aron 
 * @version 1.0
 */
public class CampusServiceBean extends IBOServiceBean implements CampusService {
	
	private final static String CAMPUS_SETTINGS = "CAMPUS_SETTINGS";
	
	public CampusSettings getCampusSettings(){
		Object settings = 	getIWApplicationContext().getApplicationAttribute(CAMPUS_SETTINGS);
		if(settings!=null){
			CampusSettings campusSettings =(CampusSettings) settings;
			return campusSettings;
		}
		else{
			return loadFromBundle();
		}
	}
	
	private CampusSettings loadFromBundle(){
		CampusSettings settings = new CampusSettings();
		IWBundle bundle = getIWApplicationContext().getIWMainApplication().getBundle(CampusSettings.IW_BUNDLE_IDENTIFIER);
		String property = bundle.getProperty(CampusSettings.PROPERTY_ADMIN_EMAIL);
		settings.setAdminEmail(property);
		property = bundle.getProperty(CampusSettings.PROPERTY_SMTP_SERVER);
		settings.setSmtpServer(property);
		property = bundle.getProperty(CampusSettings.PROPERTY_TERM_OF_NOTICE);
		try {
			Integer daysOfNotice = Integer.valueOf(property);
			settings.setTermOfNoticeDays(daysOfNotice);
		} catch (NumberFormatException e2) {
			
		}
		property = bundle.getProperty(CampusSettings.PROPERTY_TENANT_GROUP);
		try {
			Integer tenantGroupID = Integer.valueOf(property);
			settings.setTenantGroupID(tenantGroupID);
		} catch (NumberFormatException e1) {
			
		}
		property = bundle.getProperty(CampusSettings.PROPERTY_FINANCE_CATEGORY);
		try {
			Integer financeCategoryID = Integer.valueOf(property);
			settings.setFinanceCategoryID(financeCategoryID);
		} catch (NumberFormatException e) {
			
		}
		
		return settings;	
	}
	
	public void storeSettings(CampusSettings settings){
		IWBundle bundle = getIWApplicationContext().getIWMainApplication().getBundle(CampusSettings.IW_BUNDLE_IDENTIFIER);
		bundle.setProperty(CampusSettings.PROPERTY_ADMIN_EMAIL,settings.getAdminEmail());
		bundle.setProperty(CampusSettings.PROPERTY_SMTP_SERVER, settings.getSmtpServer());
		bundle.setProperty(CampusSettings.PROPERTY_TERM_OF_NOTICE,settings.getTermOfNoticeDays().toString());
		bundle.setProperty(CampusSettings.PROPERTY_TENANT_GROUP, settings.getTenantGroupID().toString());
		bundle.setProperty(CampusSettings.PROPERTY_FINANCE_CATEGORY,settings.getFinanceCategoryID().toString());
	}
}
