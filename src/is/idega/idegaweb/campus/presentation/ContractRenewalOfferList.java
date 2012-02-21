package is.idega.idegaweb.campus.presentation;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.data.ContractRenewalOffer;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.application.data.Applicant;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.data.EntityFinder;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

public class ContractRenewalOfferList extends CampusBlock {
	protected boolean isAdmin = false;
	private static final String SAVE = "save";

	
	public void main(IWContext iwc) {
		isAdmin = iwc.hasEditPermission(this);
		control(iwc);
	}

	
	protected void control(IWContext iwc) {
		initFilter(iwc);
		if (isAdmin) {
			if (iwc.isParameterSet("send_offer")) {
				sendOffer(iwc);
			}
			else if (iwc.isParameterSet("send_reminder")) {
				sendReminder(iwc);
			}
			else if (iwc.isParameterSet("close_offer")) {
				closeOffer(iwc);
			}
			add(actionForm());
			add(statusForm());
			add(getContractTable(iwc));
		} else {
			add(getNoAccessObject(iwc));
		}
	}

	private void sendOffer(IWContext iwc) {
		try {
			getContractRenewalService(iwc).sendOffer();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void sendReminder(IWContext iwc) {
		
	}

	private void closeOffer(IWContext iwc) {
		
	}

	private void initFilter(IWContext iwc) {
		// complex check
	}

	private PresentationObject actionForm() {
		Form form = new Form();

		DataTable T = new DataTable();
		T.addTitle(localize("renewal.actions", "Actions"));
		T.setTitlesHorizontal(true);
		T.setBottomHeight(3);
		T.add(getHeader(localize("renewal.action_send_offer", "Send offer")), 1, 1);
		T.add(getHeader(localize("renewal.action_send_reminder", "Send reminder")), 2, 1);
		T.add(getHeader(localize("renewal.action_close_offers", "Close offers")), 3, 1);
		T.add(new SubmitButton("send_offer", "send_offer"), 1, 2);
		T.add(new SubmitButton("send_reminder", "send_reminder"), 2, 2);
		T.add(new SubmitButton("close_offer", "close_offer"), 3, 2);

		form.add(T);

		return (form);
	}

	private PresentationObject statusForm() {
		Form form = new Form();
		DropdownMenu status = statusDrop("", "");
		DropdownMenu complex = drpLodgings(Complex.class, "", "--", new Integer(-1));
		DropdownMenu building = drpLodgings(Building.class, "", "--", new Integer(-1));

		DataTable T = new DataTable();
		T.addTitle(localize("filter", "Filter"));
		T.setTitlesHorizontal(true);
		T.setBottomHeight(3);
		T.add(getHeader(localize("status", "Status")), 1, 1);
		T.add(getHeader(localize("complex", "Complex")), 2, 1);
		T.add(getHeader(localize("building", "Building")), 3, 1);
		T.add(status, 1, 2);
		T.add(complex, 2, 2);
		T.add(building, 3, 2);
		SubmitButton get = (SubmitButton) getSubmitButton("conget", null,
				"Get", "get");
		T.add(get, 4, 2);

		form.add(T);

		return (form);
	}

	private PresentationObject getContractTable(IWContext iwc) {
		Collection offers = null;
		try {
			offers = getContractRenewalService(iwc).getContractRenewalOffers();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		Contract contract = null;
		Applicant applicant = null;
		Apartment apartment = null;

		int row = 1;
		int col = 1;
		int maxCol = 1;
		Form f = new Form();
		DataTable T = getDataTable();
		f.add(T);
		T.setTitlesHorizontal(true);
		T.setWidth("100%");
		T.add(getHeader(localize("renewal.offer_id", "Offer id")), col++, row);
		
		T.add(getHeader(localize("name", "Name")), col++, row);
		T.add(getHeader(localize("ssn", "Socialnumber")), col++, row);
		T.add(getHeader(localize("apartment", "Apartment")), col++, row);
		T.add(getHeader(localize("answer", "Answer")), col++, row);
		T.add(getHeader(localize("renew", "Renew")), col++, row);
		T.add(getHeader(localize("decline", "Decline")), col++, row++);

		StringBuffer listOfUsers = new StringBuffer();

		if (offers != null) {
			StringBuffer sbIDs = new StringBuffer();
			for (Iterator iter = offers.iterator(); iter.hasNext();) {
				col = 1;
				try {
					ContractRenewalOffer offer = (ContractRenewalOffer) iter.next();
					contract = offer.getContract();
					String status = contract.getStatus();

					applicant = contract.getApplicant();
					apartment = contract.getApartment();
					T.add(getText(offer.getUniqueId()), col++, row);
					T.add(getText(applicant.getFullName()), col++, row);
					T.add(getText(applicant.getSSN()), col++, row);
					T.add((getApartmentTable(apartment)), col++, row);
					if (offer.getOfferAnsweredDate() != null) {
						if (offer.getAnswer()) {
							T.add(getText(localize("renewal.offer_accepted","Accepted")), col++, row);						
						} else {
							T.add(getText(localize("renewal.offer_declined","Declined")), col++, row);						
						}
					} else {
						T.add(getText(""), col++, row);												
					}
					CheckBox renew = new CheckBox(
							"renew_contracts", offer.getUniqueId());
					T.add(renew, col++, row);

					CheckBox decline = new CheckBox(
							"decline_contract", offer.getUniqueId());
					T.add(decline, col++, row++);
					if (col > maxCol) {
						maxCol = col -1;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
			SubmitButton save = new SubmitButton(SAVE, SAVE, listOfUsers
					.toString());
			T.add(save, maxCol, row);
		}

		Table T2 = new Table();
		T2.setCellpadding(0);
		T2.setCellspacing(0);
		T2.add(f, 1, 1);
		return T2;
	}
	
	private PresentationObject getApartmentTable(Apartment A) {
		Table T = new Table();
		ApartmentType type = A.getApartmentType();
		Floor F = A.getFloor();
		Building B = F.getBuilding();
		Complex C = B.getComplex();
		T.add(getText(A.getName()), 1, 1);
		T.add(getText(F.getName()), 2, 1);
		T.add(getText(B.getName()), 3, 1);
		T.add(getText(C.getName()), 4, 1);
		if (type.getAbbreviation() != null)
			T.add(" (" + type.getAbbreviation() + ")", 5, 1);
		return T;
	}
	
	private DropdownMenu statusDrop(String name, String selected) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement("all", localize("renewal.status_all", "All"));
		drp.addMenuElement("answered", localize("renewal.status_answered", "Answered"));
		drp.addMenuElement("unanswered", localize("renewal.status_unanswered", "Unanswered"));
		drp.setSelectedElement(selected);
		return drp;
	}
	
	private DropdownMenu drpLodgings(Class lodgings, String name,
			String display, Integer selected) {
		Collection lods = null;
		try {
			lods = EntityFinder.getInstance().findAll(lodgings);
		} catch (Exception e) {
		}
		DropdownMenu drp = new DropdownMenu(lods, name);
		if (!"".equals(display))
			drp.addMenuElementFirst("-1", display);
		drp.setSelectedElement(selected.toString());

		return drp;
	}
}