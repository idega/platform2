package is.idega.idegaweb.campus.presentation;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.nortek.business.NortekBusiness;
import is.idega.idegaweb.campus.nortek.data.Card;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.building.data.Apartment;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.user.data.User;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author <br>
 *         <a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class PersonalNumberResult extends CampusBlock implements Campus {
	
	protected final static String LABEL_CARD_DECODED = "nt_card_decoded";

	protected final static String LABEL_CARD = "nt_card";

	protected final static String LABEL_USER = "nt_user";

	protected final static String LABEL_VALID = "nt_valid";
	
	private static final String APPLICANT_INFO = "appl_info";

	private static final String COMMENT = "comment";

	private final static String SAVE = "save";

	private String SSN = null;

	private Integer applicantID = null;

	private DateFormat df;
	
	private User user = null;
		
	private String comment = null;
	
	private ICPage page = null;

	public PersonalNumberResult() {
	}

	public void control(IWContext iwc) {
		parse(iwc);
		add(getSSNResult(iwc));
	}

	public void parse(IWContext iwc) {
		if (iwc.isParameterSet(PersonalNumberSearch.PERSONAL_NUMBER)) {
			if (iwc.isParameterSet(SAVE)) {
				this.comment = iwc.getParameter(COMMENT);
			}
			SSN = iwc.getParameter(PersonalNumberSearch.PERSONAL_NUMBER);
			try {
				this.user = this.getUserService(iwc).getUser(SSN);
			} catch (RemoteException e) {
			} catch (FinderException e) {
			}
		}
		else if (iwc.isParameterSet(APPLICANT_INFO)) {
			applicantID = Integer.valueOf(iwc.getParameter(APPLICANT_INFO));
		}
	}

	private PresentationObject getSSNResult(IWContext iwc) {
		Table T = new Table();
		int col = 1, row = 1;

		if (SSN != null) {
			try {
				Collection applicants = getContractService(iwc)
						.getContractHome().getUnsignedApplicants(SSN);
				if (applicants != null && !applicants.isEmpty()) {
					T.add(getNonContractApplicantInfo(applicants), col, row++);
				}
				Collection contracts = getContractService(iwc)
						.getContractHome().findByPersonalID(SSN);
				if (contracts != null && !contracts.isEmpty()) {
					T.add(getContractInfo(iwc, contracts), col, row++);
				}
				if (user != null) {
					T.add(getUserComment(iwc), col, row++);
					PresentationObject card = getLaundryCardForUser(iwc);
					if (card != null) {
						T.add(card, col, row++);						
					}
				}
			} catch (com.idega.data.IDOFinderException ex) {
				ex.printStackTrace();
				T.add(getErrorText(localize("error_finding_from_ssn",
						"Error in ssn search")), col, row);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (applicantID != null) {
			try {
				Applicant applicant = ((ApplicantHome) IDOLookup
						.getHome(Applicant.class))
						.findByPrimaryKey(applicantID);
				T.add(getApplicantInfo(applicant), col, row);
			} catch (IDOLookupException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
				T.add(getErrorText(localize("error_finding_from_id",
						"Error in id search")), col, row);
			}
		} else
			T.add(getErrorText(localize("warning_no_ssn_provided",
					"No ssn provided")), col, row);

		return T;
	}

	private PresentationObject getUserComment(IWContext iwc) {
		Form form = new Form();
		form.maintainAllParameters();

		DataTable T = new DataTable();

		T.setUseBottom(false);
		T.setUseTop(false);
		T.addTitle(localize("user_comment",
				"User comment"));
		T.setTitlesHorizontal(true);
		int col = 1;
		int row = 1;
		T.add(getHeader(localize(COMMENT, "Comment")), col++, row++);

		TextArea input = new TextArea(COMMENT,40,7);
		input.setMaximumCharacters(255);
		if (this.comment != null) {
			if (this.comment.length() > 255) {
				this.comment = this.comment.substring(0, 255);
			}
			user.setDescription(this.comment);
			user.store();
		} else {
			this.comment = user.getDescription();
		}
		if (this.comment != null && !"".equals(this.comment)) {
			input.setContent(this.comment);
		}

		T.add(input, 1, row++);
		SubmitButton save = new SubmitButton(SAVE, SAVE);
		T.add(save, 1, row++);

		form.add(T);
		
		return form;
	}

	private PresentationObject getLaundryCardForUser(IWContext iwc) {
		Card card = null;
		try {
			card = getNortekBusiness(iwc).getCard(this.user);
		} catch (IBOLookupException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		System.out.println("card = " + card);
		
		if (card == null) {
			return null;
		}
		
		DataTable T = new DataTable();
		T.setUseBottom(false);
		T.setUseTop(false);
		T.addTitle(localize("laundry_card", "Laundry card"));
		T.setTitlesHorizontal(true);

		int row = 1;
		int column = 1;
		T.add(getHeader(this.iwrb.getLocalizedString(LABEL_CARD_DECODED, "Card decoded")), column++, row);
		T.add(getHeader(this.iwrb.getLocalizedString(LABEL_CARD, "Card")), column++, row);
		T.add(getHeader(this.iwrb.getLocalizedString(LABEL_USER, "User")), column++, row);
		T.add(getHeader(this.iwrb.getLocalizedString(LABEL_VALID, "Valid")), column++, row++);

		column = 1;
		String text = "x";
		if (card.getDecodedCardSerialNumber() != null) {
			text = card.getDecodedCardSerialNumber();
		}
		if (this.page != null) {
			Link link = new Link(new Text(text));
			link.addParameter(LABEL_CARD, card.getCardSerialNumber());
			link.setPage(this.page);
			T.add(link, column++, row);			
		} else {
			T.add(new Text(text), column++, row);
		}
		T.add(new Text(card.getCardSerialNumber()), column++, row);
		if (card.getUser() != null) {
			T.add(new Text(card.getUser().getName() + " (" + card.getUser().getPersonalID() + ")"), column++, row);
		} else {
			T.add(new Text(""), column++, row);
		}
		CheckBox check = new CheckBox();
		check.setDisabled(true);
		if (card.getIsValid()) {
			check.setChecked(true);
		} else {
			check.setChecked(false);
		}
		T.add(check, column++, row++);
		
		return T;
	}
	
	private PresentationObject getNonContractApplicantInfo(Collection applicants) {
		DataTable T = new DataTable();

		T.setUseBottom(false);
		T.setUseTop(false);
		T.addTitle(localize("applicants_without_contracts",
				"Applicants without contracts"));
		T.setTitlesHorizontal(true);
		int col = 1;
		int row = 1;
		T.add(getHeader(localize("id", "ID")), col++, row);
		T.add(getHeader(localize("ssn", "SSN")), col++, row);
		T.add(getHeader(localize("name", "Name")), col++, row);

		col += 4;
		T.add(getHeader(localize("mobile", "Mobile")), col++, row);
		T.add(getHeader(localize("status", "Status")), col++, row);

		Iterator iter = applicants.iterator();
		while (iter.hasNext()) {
			col = 1;
			row++;
			Applicant applicant = (Applicant) iter.next();

			T.add(getApplicantInfoLink(getText(applicant.getPrimaryKey()
					.toString()), (Integer) applicant.getPrimaryKey()), col++,
					row);
			T.add(getText(applicant.getSSN()), col++, row);
			T.add(getText(applicant.getFullName()), col++, row);
			T.add(getText(applicant.getLegalResidence()), col++, row);
			T.add(getText(applicant.getResidence()), col++, row);
			T.add(getText(applicant.getPO()), col++, row);
			T.add(getText(applicant.getResidencePhone()), col++, row);
			T.add(getText(applicant.getMobilePhone()), col++, row);
			T.add(getText(applicant.getStatus()), col++, row);
		}
		return T;
	}

	private PresentationObject getContractInfo(IWContext iwc,
			Collection contracts) throws FinderException, RemoteException {
		DataTable T = new DataTable();
		T.setUseBottom(false);
		T.setUseTop(false);
		T.addTitle(localize("applicants_with_contracts",
				"Applicants with contracts"));
		T.setTitlesHorizontal(true);
		int col = 1;
		int row = 1;
		T.add(getHeader(localize("id", "ID")), col++, row);
		T.add(getHeader(localize("ssn", "SSN")), col++, row);
		T.add(getHeader(localize("name", "Name")), col++, row);
		T.add(getHeader(localize("mobile", "Mobile")), col++, row);
		T.add(getHeader(localize("apartment", "Apartment")), col++, row);
		T.add(getHeader(localize("from", "From")), col++, row);
		T.add(getHeader(localize("to", "To")), col++, row);
		T.add(getHeader(localize("status", "Status")), col++, row);

		Iterator iter = contracts.iterator();
		while (iter.hasNext()) {
			col = 1;
			row++;
			Contract contract = (Contract) iter.next();

			Applicant applicant = contract.getApplicant();
			Apartment apartment = contract.getApartment();
			T.add(getApplicantInfoLink(getText(applicant.getPrimaryKey()
					.toString()), (Integer) applicant.getPrimaryKey()), col++,
					row);
			T.add(getText(applicant.getSSN()), col++, row);
			T.add(getText(applicant.getFullName()), col++, row);
			T.add(getText(applicant.getMobilePhone()), col++, row);
			T
					.add(getText(apartment.getName() + " "
							+ apartment.getFloor().getBuilding().getName()),
							col++, row);
			T.add(getText(df.format(contract.getValidFrom())), col++, row);
			T.add(getText(df.format(contract.getValidTo())), col++, row);
			T.add(getText(getContractService(iwc).getLocalizedStatus(
					getResourceBundle(), contract.getStatus())), col++, row);
		}
		
		return T;
	}

	public PresentationObject getApplicantInfo(Applicant applicant) {
		DataTable T = new DataTable();
		T.setUseBottom(false);
		T.setUseTop(false);
		T.addTitle(localize("applicant_info", "Applicant info"));
		T.setTitlesHorizontal(false);
		int col = 1;
		int row = 1;
		T.add(getHeader(localize("id", "ID")), col, row++);
		T.add(getHeader(localize("ssn", "SSN")), col, row++);
		T.add(getHeader(localize("name", "Name")), col, row++);
		T.add(getHeader(localize("legal_residence", "Legal residence")), col,
				row++);
		T.add(getHeader(localize("residence", "Residence")), col, row++);
		T.add(getHeader(localize("zip", "Zip")), col, row++);
		T.add(getHeader(localize("phone", "Phone")), col, row++);
		T.add(getHeader(localize("mobile", "Mobile")), col, row++);

		if (applicant != null) {
			row = 1;
			col++;

			T.add(
					getText(String
							.valueOf(applicant.getPrimaryKey().toString())),
					col, row++);
			T.add(getSSNLink(getText(applicant.getSSN()), applicant.getSSN()),
					col, row++);
			T.add(getText(applicant.getFullName()), col, row++);
			T.add(getText(applicant.getLegalResidence()), col, row++);
			T.add(getText(applicant.getResidence()), col, row++);
			T.add(getText(applicant.getPO()), col, row++);
			T.add(getText(applicant.getResidencePhone()), col, row++);
			T.add(getText(applicant.getMobilePhone()), col, row++);

		}
		
		return T;
	}

	public Link getApplicantInfoLink(Text text, Integer applicantID) {
		Link L = new Link(text);
		L.addParameter(APPLICANT_INFO, applicantID.toString());
		
		return L;
	}

	public Link getSSNLink(Text text, String ssn) {
		Link L = new Link(text);
		L.addParameter(PersonalNumberSearch.PERSONAL_NUMBER, ssn);
		
		return L;
	}

	public void main(IWContext iwc) {
		df = DateFormat.getDateInstance(DateFormat.SHORT, iwc
				.getCurrentLocale());
		control(iwc);
	}
	
	public void setLaundryCardPage(ICPage page) {
		this.page = page;
	}
	
	public ICPage getLaundryCardPage() {
		return this.page;
	}
	
	private NortekBusiness getNortekBusiness(IWContext iwc) throws IBOLookupException {
		NortekBusiness bus1 = (NortekBusiness) IBOLookup.getServiceInstance(
				iwc, NortekBusiness.class);

		return bus1;
	}
}