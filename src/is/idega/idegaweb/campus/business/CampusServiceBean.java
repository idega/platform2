/*
 * Created on Dec 19, 2003
 *
 */
package is.idega.idegaweb.campus.business;

import is.idega.idegaweb.campus.block.allocation.business.ContractService;
import is.idega.idegaweb.campus.block.application.business.ApplicationService;

import java.rmi.RemoteException;
import java.util.Locale;

import com.idega.block.building.business.BuildingService;
import com.idega.block.finance.business.FinanceService;
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
		property = bundle.getProperty(CampusSettings.PROPERTY_TRANSFER_BGR_COLOR);
		settings.setTransferBackgroundColor(property);
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
		property = bundle.getProperty(CampusSettings.PROPERTY_SEND_EVENT_MAIL,Boolean.toString(true));
		try {
			Boolean sendEventMail = Boolean.valueOf(property);
			settings.setSendEventMail(sendEventMail.booleanValue());
		} catch (NumberFormatException e) {
			
		}
		property = bundle.getProperty(CampusSettings.PROPERTY_SYSTEM_LOCALE,getIWApplicationContext().getApplicationSettings().getDefaultLocale().toString());
		try{
			Locale locale = new Locale(property);
			settings.setSystemLocale(locale);
		}
		catch(Exception e){
			settings.setSystemLocale(getIWApplicationContext().getApplicationSettings().getDefaultLocale());
		}
		
		return settings;	
	}
	
	public void storeSettings(CampusSettings settings){
		IWBundle bundle = getIWApplicationContext().getIWMainApplication().getBundle(CampusSettings.IW_BUNDLE_IDENTIFIER);
		setBundleProperty(bundle,CampusSettings.PROPERTY_ADMIN_EMAIL,settings.getAdminEmail());
		setBundleProperty(bundle,CampusSettings.PROPERTY_SMTP_SERVER, settings.getSmtpServer());
		if(settings.getTermOfNoticeDays()!=null)
			setBundleProperty(bundle,CampusSettings.PROPERTY_TERM_OF_NOTICE,settings.getTermOfNoticeDays().toString());
		if(settings.getTenantGroupID()!=null)
			setBundleProperty(bundle,CampusSettings.PROPERTY_TENANT_GROUP, settings.getTenantGroupID().toString());
		if(settings.getFinanceCategoryID()!=null)
			setBundleProperty(bundle,CampusSettings.PROPERTY_FINANCE_CATEGORY,settings.getFinanceCategoryID().toString());
		setBundleProperty(bundle,CampusSettings.PROPERTY_SEND_EVENT_MAIL,Boolean.toString(settings.getSendEventMail()));
		setBundleProperty(bundle,CampusSettings.PROPERTY_TRANSFER_BGR_COLOR, settings.getTransferBackgroundColor());
		setBundleProperty(bundle,CampusSettings.PROPERTY_SYSTEM_LOCALE, settings.getSystemLocale().toString());
	}
	
	private  void setBundleProperty(IWBundle bundle,String propertyKey,String propertyValue){
		if(propertyValue!=null)
			bundle.setProperty(propertyKey,propertyValue);
		
	}
	
	public ContractService getContractService()throws RemoteException{
		return (ContractService) getServiceInstance(ContractService.class);
	}
	public ApplicationService getApplicationService()throws RemoteException{
		return (ApplicationService) getServiceInstance(ApplicationService.class);
	}
	
	public BuildingService getBuildingService()throws RemoteException{
		return (BuildingService) getServiceInstance(BuildingService.class);
	}
	
	public FinanceService getFinanceService()throws RemoteException{
		return (FinanceService) getServiceInstance(FinanceService.class);
	}
	
	public CampusUserService getUserService()throws RemoteException{
		return (CampusUserService) getServiceInstance(CampusUserService.class);
	}
	
}
