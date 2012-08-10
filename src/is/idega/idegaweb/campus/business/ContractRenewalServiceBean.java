package is.idega.idegaweb.campus.business;

import is.idega.idegaweb.campus.block.allocation.business.ContractService;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean;
import is.idega.idegaweb.campus.data.ContractRenewalOffer;
import is.idega.idegaweb.campus.data.ContractRenewalOfferHome;
import is.idega.idegaweb.campus.presentation.CampusBlock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.mail.MessagingException;

import com.idega.block.application.data.Applicant;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.business.IBOServiceBean;
import com.idega.core.contact.data.Email;
import com.idega.core.idgenerator.business.UUIDGenerator;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.idega.util.SendMail;
import com.lowagie.text.Chapter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.PdfWriter;

public class ContractRenewalServiceBean extends IBOServiceBean implements
		ContractRenewalService {

	public final static String tenant_name = "tenant_name";

	public final static String tenant_personal_id = "tenant_personal_id";

	public final static String tenant_co_name = "tenant_co_name";

	public final static String tenant_co_personal_id = "tenant_co_personal_id";

	public final static String apartment_name = "apartment_name";

	public final static String apartment_number = "apartment_number";

	public final static String orig_contract_date = "orig_contract_date";

	public final static String orig_date_from = "orig_date_from";

	public final static String orig_date_to = "orig_date_to";

	public final static String new_date_from = "new_date_from";

	public final static String new_date_to = "new_date_to";

	public static String[] TAGS = { tenant_name, tenant_personal_id,
			tenant_co_name, tenant_co_personal_id, apartment_name,
			apartment_number, orig_contract_date, orig_date_from, orig_date_to,
			new_date_from, new_date_to };

	public Collection getContractRenewalOffers() {
		try {
			return getContractRenewalOfferHome().findAll();
		} catch (RemoteException e) {
		} catch (FinderException e) {
		}

		return null;
	}

	public Collection getContractRenewalOffers(Integer status, Integer complex,
			Integer building) {
		try {
			Collection col = getContractRenewalOfferHome().findAll();
			if (status.intValue() == -1 && complex.intValue() == -1
					&& building.intValue() == -1) {
				return col;
			}

			Collection ret = new ArrayList();

			Iterator it = col.iterator();
			while (it.hasNext()) {
				boolean add = true;
				ContractRenewalOffer offer = (ContractRenewalOffer) it.next();
				if (status.intValue() != -1) {
					if (status.intValue() == 0 && !offer.getAnswer()) {
						add = false;
					}
					if (status.intValue() == 1 && offer.getAnswer()) {
						add = false;
					}
				}

				if (complex.intValue() != -1) {
					if (offer.getContract().getApartment().getFloor()
							.getBuilding().getComplexId() != complex.intValue()) {
						add = false;
					}
				}

				if (building.intValue() != -1) {
					if (offer.getContract().getApartment().getFloor()
							.getBuildingId() != building.intValue()) {
						add = false;
					}
				}

				if (add) {
					ret.add(offer);
				}
			}

			return ret;
		} catch (RemoteException e) {
		} catch (FinderException e) {
		}

		return null;
	}

	public void setRenewalGranted(String uuid, String status) {
		ContractRenewalOffer offer = getOfferByUUID(uuid);
		offer.setRenewalGranted(status);
		offer.store();
	}

	public ContractRenewalOfferHome getContractRenewalOfferHome()
			throws RemoteException {
		return (ContractRenewalOfferHome) getIDOHome(ContractRenewalOffer.class);
	}

	public void sendOffer(Locale locale) {
		try {
			Collection contracts = getContractService().getContractHome()
					.findByStatus(ContractBMPBean.STATUS_SIGNED);
			
			System.out.println("number of contracts = " + contracts.size());
			if (contracts != null && !contracts.isEmpty()) {
				IWResourceBundle iwrb = this.getIWMainApplication()
						.getBundle(CampusBlock.IW_BUNDLE_IDENTIFIER)
						.getResourceBundle(locale);

				Iterator it = contracts.iterator();
				while (it.hasNext()) {
					Contract contract = (Contract) it.next();

					IWTimestamp validTo = new IWTimestamp(contract.getValidTo());
					if (validTo.getYear() <= IWTimestamp.RightNow().getYear()) {
						ContractRenewalOffer offer = null;
						try {
							offer = getContractRenewalOfferHome().findByContract(contract);							
						} catch (FinderException e) {
							offer = null;
						}
						
						if (offer == null) {
							System.out.println("found no offer for contract " + contract.getPrimaryKey().toString());
							offer = getContractRenewalOfferHome().create();
							offer.setContract(contract);
							offer.setUser(contract.getUser());
							offer.setIsOfferClosed(false);
							offer.setOfferSentDate(IWTimestamp.getTimestampRightNow());
							offer.setUniqueId(UUIDGenerator.getInstance()
									.generateUUID());
							offer.store();						
						} else {
							System.out.println("found offer for contract " + contract.getPrimaryKey().toString());
						}
					}

					/*String subject = iwrb.getLocalizedString(
							"RENEWAL_MAIL_SUBJECT", "Renewal mail subject");
					String body = iwrb.getLocalizedString("RENEWAL_MAIL_BODY",
							"Renewal mail body [ref_num]");

					sendEmail(offer, subject, body);*/
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
			Collection offers = getContractRenewalOfferHome()
					.findAllUnanswered();
			if (offers != null && !offers.isEmpty()) {
				IWResourceBundle iwrb = this.getIWMainApplication()
						.getBundle(CampusBlock.IW_BUNDLE_IDENTIFIER)
						.getResourceBundle(locale);

				Iterator it = offers.iterator();
				while (it.hasNext()) {
					ContractRenewalOffer offer = (ContractRenewalOffer) it
							.next();

					String subject = iwrb.getLocalizedString(
							"RENEWAL_REMIND_MAIL_SUBJECT",
							"Renewal remind mail subject");
					String body = iwrb.getLocalizedString(
							"RENEWAL_REMIND_MAIL_BODY",
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

	public void sendContract(IWContext iwc) {
		try {
			Collection offers = getContractRenewalOfferHome()
					.findAllUnsentContracts();
			if (offers != null && !offers.isEmpty()) {
				IWResourceBundle iwrb = this.getIWMainApplication()
						.getBundle(CampusBlock.IW_BUNDLE_IDENTIFIER)
						.getResourceBundle(LocaleUtil.getIcelandicLocale());

				IWResourceBundle iwrb_en = this.getIWMainApplication()
						.getBundle(CampusBlock.IW_BUNDLE_IDENTIFIER)
						.getResourceBundle(Locale.ENGLISH);

				Iterator it = offers.iterator();
				while (it.hasNext()) {
					ContractRenewalOffer offer = (ContractRenewalOffer) it
							.next();

					String subject = iwrb.getLocalizedString(
							"RENEWAL_CONTRACT_TITLE", "RENEWAL_CONTRACT_TITLE");
					String body = iwrb.getLocalizedString(
							"RENEWAL_CONTRACT_TEXT", "RENEWAL_CONTRACT_TEXT");

					String subject_en = iwrb_en.getLocalizedString(
							"RENEWAL_CONTRACT_TITLE", "RENEWAL_CONTRACT_TITLE");
					String body_en = iwrb_en.getLocalizedString(
							"RENEWAL_CONTRACT_TEXT", "RENEWAL_CONTRACT_TEXT");

					sendContractEmail(iwc, offer, subject, body, LocaleUtil.getIcelandicLocale());
					sendContractEmail(iwc, offer, subject_en, body_en, Locale.ENGLISH);
					
					offer.setIsContractSent(true);
					offer.store();
					
					Contract eContract = offer.getContract();
					Apartment eApartment = eContract.getApartment();
					ApartmentType eApartmentType = eApartment.getApartmentType();
					
					//IWTimestamp newDateFrom = new IWTimestamp(getNewDateFrom(eApartmentType));
					IWTimestamp newDateTo = new IWTimestamp(getNewDateTo(eApartmentType));
					
					eContract.setValidTo(newDateTo.getDate());
					eContract.store();
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
				IWResourceBundle iwrb = this.getIWMainApplication()
						.getBundle(CampusBlock.IW_BUNDLE_IDENTIFIER)
						.getResourceBundle(locale);

				Iterator it = offers.iterator();
				while (it.hasNext()) {
					ContractRenewalOffer offer = (ContractRenewalOffer) it
							.next();
					offer.setIsOfferClosed(true);
					offer.store();

					String subject = iwrb.getLocalizedString(
							"RENEWAL_CLOSE_SUBJECT", "Renewal close subject");
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

	private void sendContractEmail(IWContext iwc, ContractRenewalOffer offer, String subject,
			String body, Locale locale) {
		CampusSettings settings = null;
		try {
			settings = getCampusService().getCampusSettings();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}

		if (settings != null && settings.getSendEventMail()) {
			Email email = null;

			try {
				email = getCampusService().getUserService().getUserMail(
						offer.getUser());
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}

			String sendTo = null;

			if (email == null) {
				sendTo = "anna@fs.is";
			} else {
				sendTo = email.getEmailAddress();
			}

			//sendTo = "palli@idega.com";

			String identifier = "is.idega.idegaweb.campus";
			IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(identifier).getResourceBundle(locale);
			Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
			Font paraFont = new Font(Font.HELVETICA, 10, Font.BOLD);
			Font nameFont = new Font(Font.HELVETICA, 12, Font.BOLDITALIC);
			Font tagFont = new Font(Font.HELVETICA, 10, Font.BOLDITALIC);
			Font textFont = new Font(Font.HELVETICA, 8, Font.NORMAL);

			File attachment = writePDF(offer, iwrb, nameFont, titleFont, paraFont, tagFont, textFont, subject, body, locale.equals(LocaleUtil.getIcelandicLocale()) ? "_is.pdf" : "_en.pdf");

			try {
				SendMail.send(settings.getAdminEmail(), sendTo, null,
						"palli@idega.com", settings.getSmtpServer(), subject,
						"", attachment);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	private File writePDF(ContractRenewalOffer offer,
			IWResourceBundle iwrb, Font nameFont, Font titleFont,
			Font paragraphFont, Font tagFont, Font textFont, String title,
			String text, String suffix) {
		try {
			MemoryFileBuffer buffer = new MemoryFileBuffer();
			MemoryOutputStream mos = new MemoryOutputStream(buffer);
			MemoryInputStream mis = new MemoryInputStream(buffer);
			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			PdfWriter.getInstance(document, mos);
			document.addAuthor("Idegaweb Campus");
			document.addSubject("");
			document.open();
			
			//Image logo = Image.getInstance(new URL("http://www.studentagardar.is/idegaweb/bundles/is.idega.idegaweb.campus.bundle/resources/fs_undir_studentagardar_2.jpg"));
			//document.add(logo);
			
			HeaderFooter footer = new HeaderFooter(new Phrase("", textFont),
					true);
			footer.setBorder(0);
			footer.setAlignment(Element.ALIGN_CENTER);
			document.setHeader(footer);
			title = title + " \n\n";
			Paragraph cTitle = new Paragraph(title, titleFont);
			cTitle.setAlignment(Element.ALIGN_CENTER);
			document.setPageCount(1);
			Chapter chapter = new Chapter(cTitle, 1);
			chapter.setNumberDepth(0);
			Paragraph P, P2;
			Section subSection;
			Phrase phrase;
			// System.err.println("inside chapter : "+ids[j]);
			Map map = getTagMap(offer, iwrb, nameFont, tagFont, textFont);

			P = new Paragraph(new Phrase("", paragraphFont));
			subSection = chapter.addSection(P, 0);
			phrase = detagParagraph(map, text);
			P2 = new Paragraph(phrase);
			subSection.add(P2);

			document.add(chapter);
			document.close();

			try {
				File file = File.createTempFile(offer.getUniqueId(), suffix);
				file.deleteOnExit();
				
				OutputStream out = new FileOutputStream(file);
				int read = 0;
				byte[] bytes = new byte[1024];
			 
				while ((read = mis.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}				

				out.close();
				
				return file;
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				mis.close();
				mos.close();				
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	private static Phrase detagParagraph(Map map, String sParagraph) {
		Phrase phrase = new Phrase();
		StringTokenizer ST = new StringTokenizer(sParagraph, "[]");
		while (ST.hasMoreTokens()) {
			String token = ST.nextToken();
			if (map.containsKey(token)) {
				phrase.add(map.get(token));
			} else {
				phrase.add(new Chunk(token, new Font(Font.HELVETICA, 8,
						Font.NORMAL)));
			}
		}
		return phrase;
	}

	private Map getTagMap(ContractRenewalOffer offer,
			IWResourceBundle iwrb, Font nameFont, Font tagFont, Font textFont) {
		if (offer != null) {
			try {
				IWBundle iwb = iwrb.getIWBundleParent();
				Contract eContract = offer.getContract();
				Applicant eApplicant = eContract.getApplicant();
				Apartment eApartment = eContract.getApartment();
				ApartmentType eApartmentType = eApartment.getApartmentType();

				String aprtTypeName = eApartmentType.getName();

				Floor eFloor = eApartment.getFloor();
				Building eBuilding = eFloor.getBuilding();
				Complex eComplex = eBuilding.getComplex();

				// new stuff 24.1.2006
				Applicant coHabitant = null;
				if (eApplicant.getChildCount() > 0) {
					Iterator it = eApplicant.getChildrenIterator();
					while (it.hasNext()) {
						Applicant tmp = (Applicant) it.next();
						if (tmp.getStatus().equals("P")) {
							coHabitant = tmp;
						}
					}
				}

				DateFormat dfLong = DateFormat.getDateInstance(DateFormat.LONG,
						iwrb.getLocale());

				Hashtable H = new Hashtable(TAGS.length);

				H.put(tenant_name,
						new Chunk(eApplicant.getFullName(), tagFont));
				H.put(tenant_personal_id, new Chunk(eApplicant.getSSN(),
						nameFont));
				if (coHabitant != null) {
					H.put(tenant_co_name, new Chunk(coHabitant.getFullName(),
							tagFont));
					if (coHabitant.getSSN() != null) {
						H.put(tenant_co_personal_id, new Chunk(coHabitant.getSSN(),
								tagFont));						
					} else {
						H.put(tenant_co_personal_id, new Chunk("",
								tagFont));
					}
				} else {
					H.put(tenant_co_name, new Chunk("", tagFont));
					H.put(tenant_co_personal_id, new Chunk("", tagFont));
				}
				String aname = iwrb
						.getLocalizedString("apartment", "Apartment")
						+ " " + eApartment.getName();
				H.put(apartment_number, new Chunk(aname, tagFont));
				H.put(apartment_name, new Chunk(eBuilding.getName(), tagFont));
				
				H.put(orig_contract_date, new Chunk(dfLong.format(eContract.getStatusDate()), tagFont));
				H.put(orig_date_from, new Chunk(dfLong.format(eContract.getValidFrom()), tagFont));
				H.put(orig_date_to, new Chunk(dfLong.format(eContract.getValidTo()), tagFont));
				
				H.put(new_date_from, new Chunk(dfLong.format(getNewDateFrom(eApartmentType)), tagFont));
				H.put(new_date_to, new Chunk(dfLong.format(getNewDateTo(eApartmentType)), tagFont));

				
				return H;
			}

			catch (Exception e) {
				e.printStackTrace();
				return new Hashtable();
			}
		} else
			return new Hashtable();
	}

	private Date getNewDateFrom(ApartmentType type) {
		IWTimestamp ret = new IWTimestamp();

		String abbr = type.getAbbreviation();
		
		if (abbr == null || abbr.equals("1-2") || abbr.equals("1-4")) {//Gamli gardur 
			ret.setDay(28);
			ret.setMonth(8);
			ret.setYear(2012);
		} else if (abbr.equals("5-2") || abbr.equals("5-7") || abbr.equals("4-1") || abbr.equals("4-2") || abbr.equals("4-3") || abbr.equals("5-1") || abbr.equals("5-3") || abbr.equals("5-5") 
				|| abbr.equals("2-6") || abbr.equals("2-5") || abbr.equals("1-6") || abbr.equals("3-8") || abbr.equals("3-10") || abbr.equals("3-11") || abbr.equals("3-9") || abbr.equals("3-12")) {
			ret.setDay(1);
			ret.setMonth(9);
			ret.setYear(2012);			
		} else if (abbr.equals("7-1") || abbr.equals("7-2") || abbr.equals("7-3")) {
			ret.setDay(16);
			ret.setMonth(8);
			ret.setYear(2012);
		} else if (abbr.equals("6-1") || abbr.equals("6-9") || abbr.equals("6-8") || abbr.equals("6-6") || abbr.equals("6-7")) {
			ret.setDay(21);
			ret.setMonth(8);
			ret.setYear(2012);
		}
		
		return ret.getDate();
	}

	private Date getNewDateTo(ApartmentType type) {
		IWTimestamp ret = new IWTimestamp();

		String abbr = type.getAbbreviation();
		
		if (abbr == null || abbr.equals("1-2") || abbr.equals("1-4")) {//Gamli gardur 
			ret.setDay(26);
			ret.setMonth(5);
			ret.setYear(2013);
		} else if (abbr.equals("5-2") || abbr.equals("5-7") || abbr.equals("4-1") || abbr.equals("4-2") || abbr.equals("4-3") || abbr.equals("5-1") || abbr.equals("5-3") || abbr.equals("5-5") 
				|| abbr.equals("2-6") || abbr.equals("2-5") || abbr.equals("1-6") || abbr.equals("3-8") || abbr.equals("3-10") || abbr.equals("3-11") || abbr.equals("3-9") || abbr.equals("3-12")) {
			ret.setDay(31);
			ret.setMonth(8);
			ret.setYear(2013);			
		} else if (abbr.equals("7-1") || abbr.equals("7-2") || abbr.equals("7-3")) {
			ret.setDay(15);
			ret.setMonth(8);
			ret.setYear(2013);
		} else if (abbr.equals("6-1") || abbr.equals("6-9") || abbr.equals("6-8") || abbr.equals("6-6") || abbr.equals("6-7")) {
			ret.setDay(19);
			ret.setMonth(8);
			ret.setYear(2013);
		}
		
		return ret.getDate();
	}

	private void sendEmail(ContractRenewalOffer offer, String subject,
			String body) {

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
				email = getCampusService().getUserService().getUserMail(
						offer.getUser());
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}

			String sendTo = null;

			if (email == null) {
				sendTo = "anna@fs.is";
			} else {
				sendTo = email.getEmailAddress();
			}

			try {
				SendMail.send(settings.getAdminEmail(), sendTo, null,
						"palli@idega.com", settings.getSmtpServer(), subject,
						finalText.toString());
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	public ContractRenewalOffer getOfferByUUID(String uuid) {
		try {
			return getContractRenewalOfferHome().findByUUID(uuid, true);
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
