/*
 * Created on Apr 1, 2004
 *
 */
package is.idega.idegaweb.campus.business;

/**
 * CampusSettings to hold global settings for the campus application
 * @author aron 
 * @version 1.0
 */
public class CampusSettings {
	
	public final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.campus";
	protected final static String PROPERTY_TERM_OF_NOTICE = "CONTRACT_TERM_OF_NOTICE";
	protected final static String PROPERTY_TENANT_GROUP = "TENANT_GROUP";
	protected final static String PROPERTY_ADMIN_EMAIL = "ADMIN_EMAIL";
	protected final static String PROPERTY_SMTP_SERVER = "SMTP_SERVER";
	protected final static String PROPERTY_FINANCE_CATEGORY = "FINANCE_CATEGORY";
	protected final static String PROPERTY_SEND_EVENT_MAIL = "SEND_EVENT_MAIL";
	
	private Integer termOfNoticeDays = null;
	private Integer tenantGroupID = null;
	private String adminEmail = null;
	private String smtpServer = null;
	private Integer financeCategoryID = null;
	private Boolean sendEventMail = null;
	
	/**
	 * @return Returns the adminEmail.
	 */
	public String getAdminEmail() {
		return adminEmail;
	}
	/**
	 * @return Returns the financeCategoryID.
	 */
	public Integer getFinanceCategoryID() {
		return financeCategoryID;
	}
	/**
	 * @return Returns the smtpServer.
	 */
	public String getSmtpServer() {
		return smtpServer;
	}
	/**
	 * @return Returns the tenantGroupID.
	 */
	public Integer getTenantGroupID() {
		return tenantGroupID;
	}
	/**
	 * @return Returns the termOfNoticeDays.
	 */
	public Integer getTermOfNoticeDays() {
		return termOfNoticeDays;
	}
	/**
	 * @param adminEmail The adminEmail to set.
	 */
	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}
	/**
	 * @param financeCategoryID The financeCategoryID to set.
	 */
	public void setFinanceCategoryID(Integer financeCategoryID) {
		this.financeCategoryID = financeCategoryID;
	}
	/**
	 * @param smtpServer The smtpServer to set.
	 */
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}
	/**
	 * @param tenantGroupID The tenantGroupID to set.
	 */
	public void setTenantGroupID(Integer tenantGroupID) {
		this.tenantGroupID = tenantGroupID;
	}
	/**
	 * @param termOfNoticeDays The termOfNoticeDays to set.
	 */
	public void setTermOfNoticeDays(Integer termOfNoticeDays) {
		this.termOfNoticeDays = termOfNoticeDays;
	}
	/**
	 * @return flag for event mails
	 */
	public boolean getSendEventMail() {
		if(sendEventMail==null)
			return true;
		return sendEventMail.booleanValue();
	}
	
	
	/**
	 * @param sendEventMail The sendEventMail to set.
	 */
	public void setSendEventMail(boolean sendEventMail) {
		this.sendEventMail = Boolean.valueOf(sendEventMail);
	}
}
