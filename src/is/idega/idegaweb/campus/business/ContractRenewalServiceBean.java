package is.idega.idegaweb.campus.business;

import is.idega.idegaweb.campus.block.allocation.business.ContractService;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean;
import is.idega.idegaweb.campus.data.ContractRenewalOffer;
import is.idega.idegaweb.campus.data.ContractRenewalOfferHome;
import is.idega.idegaweb.campus.presentation.CampusBlock;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.mail.MessagingException;

import com.idega.business.IBOServiceBean;
import com.idega.core.contact.data.Email;
import com.idega.core.idgenerator.business.UUIDGenerator;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.IWTimestamp;
import com.idega.util.SendMail;

public class ContractRenewalServiceBean extends IBOServiceBean implements
		ContractRenewalService {

	public Collection getContractRenewalOffers() {
		try {
			return getContractRenewalOfferHome().findAll();
		} catch (RemoteException e) {
		} catch (FinderException e) {
		}
		
		return null;
	}
	
	public ContractRenewalOfferHome getContractRenewalOfferHome() throws RemoteException {
		return (ContractRenewalOfferHome) getIDOHome(ContractRenewalOffer.class);
	}
	
	public void sendOffer(Locale locale) {
		try {
			Collection contracts = getContractService().getContractHome().findByStatus(ContractBMPBean.STATUS_SIGNED);
			if (contracts != null && !contracts.isEmpty()) {
				IWResourceBundle iwrb = this.getIWMainApplication().getBundle(
						CampusBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);

				Iterator it = contracts.iterator();
				while (it.hasNext()) {
					Contract contract = (Contract) it.next();
					ContractRenewalOffer offer = getContractRenewalOfferHome().create();
					offer.setContract(contract);
					offer.setUser(contract.getUser());
					offer.setIsOfferClosed(false);
					offer.setOfferSentDate(IWTimestamp.getTimestampRightNow());
					offer.setUniqueId(UUIDGenerator.getInstance().generateUUID());
					offer.store();
					
					String subject = iwrb.getLocalizedString("RENEWAL_MAIL_SUBJECT",
							"Renewal mail subject");
					String body = iwrb.getLocalizedString("RENEWAL_MAIL_BODY",
							"Renewal mail body [ref_num]");

					sendEmail(offer, subject, body);
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
	}

	public void sendReminder(Locale locale) {
		try {
			Collection offers = getContractRenewalOfferHome().findAllUnanswered();
			if (offers != null && !offers.isEmpty()) {
				IWResourceBundle iwrb = this.getIWMainApplication().getBundle(
						CampusBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);

				Iterator it = offers.iterator();
				while (it.hasNext()) {
					ContractRenewalOffer offer = (ContractRenewalOffer) it.next();
					
					String subject = iwrb.getLocalizedString("RENEWAL_REMIND_MAIL_SUBJECT",
							"Renewal remind mail subject");
					String body = iwrb.getLocalizedString("RENEWAL_REMIND_MAIL_BODY",
							"Renewal remind mail body [ref_num]");

					sendEmail(offer, subject, body);
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} 
	}

	public void closeOffer(Locale locale) {
		try {
			Collection offers = getContractRenewalOfferHome().findAllOpen();
			if (offers != null && !offers.isEmpty()) {
				IWResourceBundle iwrb = this.getIWMainApplication().getBundle(
						CampusBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
				
				Iterator it = offers.iterator();
				while (it.hasNext()) {
					ContractRenewalOffer offer = (ContractRenewalOffer) it.next();
					offer.setIsOfferClosed(true);
					offer.store();

					String subject = iwrb.getLocalizedString("RENEWAL_CLOSE_SUBJECT",
							"Renewal close subject");
					String body = iwrb.getLocalizedString("RENEWAL_CLOSE_BODY",
							"Renewal close [ref_num]");

					sendEmail(offer, subject, body);
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} 
	}

	
	private void sendEmail(ContractRenewalOffer offer, String subject, String body) {

		CampusSettings settings = null;
		try {
			settings = getCampusService().getCampusSettings();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}

		if (settings != null && settings.getSendEventMail()) {
			StringBuffer finalText = new StringBuffer();
			StringTokenizer st = new StringTokenizer(body, "[]");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if (token.equals("renewal_code")) {
					finalText.append(offer.getUniqueId());
				} else {
					finalText.append(token);
				}
			}

			Email email = null;
			
			try {
				email = getCampusService().getUserService().getUserMail(offer.getUser());
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			
			String sendTo = null;
			
			if (email == null) {
				sendTo = "bjork@fs.is";
			} else {
				sendTo = email.getEmailAddress();
			}
			
			try {
				SendMail.send(settings.getAdminEmail(), sendTo, null,
						"palli@idega.com", settings.getSmtpServer(),
						subject, finalText.toString());
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	
	public ContractRenewalOffer getOfferByUUID(String uuid) {
		try {
			return getContractRenewalOfferHome().findByUUID(uuid);
		} catch (RemoteException e) {
		} catch (FinderException e) {
		}
		
		return null;
	}
	
	public CampusService getCampusService() throws RemoteException {
		return (CampusService) getServiceInstance(CampusService.class);
	}

	public ContractService getContractService() throws RemoteException {
		return (ContractService) getServiceInstance(ContractService.class);
	}
}
