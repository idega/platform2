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
	private static final String CLOSE_OFFER = "close_offer";
	private static final String SEND_REMINDER = "send_reminder";
	private static final String SEND_OFFER = "send_offer";
	private static final String SEND_CONTRACT = "send_contract";

	protected boolean isAdmin = false;
	private static final String SAVE = "save";

	private Integer global_status = new Integer(-1);
	private Integer global_complex = new Integer(-1);
	private Integer global_building = new Integer(-1);
	
	public void main(IWContext iwc) {
		isAdmin = iwc.hasEditPermission(this);
		control(iwc);
	}

	
	protected void control(IWContext iwc) {
		initFilter(iwc);
		if (isAdmin) {
			if (iwc.isParameterSet(SEND_OFFER)) {
				sendOffer(iwc);
			}
			else if (iwc.isParameterSet(SEND_REMINDER)) {
				sendReminder(iwc);
			}
			else if (iwc.isParameterSet(CLOSE_OFFER)) {
				closeOffer(iwc);
			}
			else if (iwc.isParameterSet(SEND_CONTRACT)) {
				sendContract(iwc);
			} else if (iwc.isParameterSet(SAVE)) {
				System.out.println("Save sent, calling saveInfo");
				saveInfo(iwc);
			}
			add(actionForm());
			add(statusForm());
			add(getContractTable(iwc));
		} else {
			add(getNoAccessObject(iwc));
		}
	}

	private void saveInfo(IWContext iwc) {
		String ids[] = iwc.getParameterValues("renew_contracts");
		if (ids != null) {
			System.out.println("Got renew id's");
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				System.out.println("id = " + id);
				try {
					getContractRenewalService(iwc).setRenewalGranted(id, "Y");
				} catch (RemoteException e) {
				}
			}
		} else {
			System.out.println("nothing selected");
		}

		ids = iwc.getParameterValues("decline_contract");
		if (ids != null) {
			System.out.println("Got decline id's");
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				System.out.println("id = " + id);
				try {
					getContractRenewalService(iwc).setRenewalGranted(id, "N");
				} catch (RemoteException e) {
				}
			}
		} else {
			System.out.println("nothing selected");
		}
	}
	
	private void sendOffer(IWContext iwc) {
		try {
			getContractRenewalService(iwc).sendOffer(iwc.getCurrentLocale());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void sendContract(IWContext iwc) {
		try {
			getContractRenewalService(iwc).sendContract(iwc);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void sendReminder(IWContext iwc) {
		try {
			getContractRenewalService(iwc).sendReminder(iwc.getCurrentLocale());
		} catch (RemoteException e) {
			e.printStackTrace();
		}		
	}

	private void closeOffer(IWContext iwc) {
		try {
			getContractRenewalService(iwc).closeOffer(iwc.getCurrentLocale());
		} catch (RemoteException e) {
			e.printStackTrace();
		}				
	}

	private void initFilter(IWContext iwc) {
		if (iwc.isParameterSet("status_filter")) {
			this.global_status = Integer.valueOf(iwc.getParameter("status_filter"));
			iwc.setSessionAttribute("status_filter", this.global_status);
		} else if (iwc.getSessionAttribute("status_filter") != null) {
			this.global_status = ((Integer) iwc.getSessionAttribute("status_filter"));
		}

		if (iwc.isParameterSet("complex_filter")) {
			this.global_complex = Integer.valueOf(iwc.getParameter("complex_filter"));
			iwc.setSessionAttribute("complex_filter", this.global_complex);
		} else if (iwc.getSessionAttribute("complex_filter") != null) {
			this.global_complex = ((Integer) iwc.getSessionAttribute("complex_filter"));
		}
		
		if (iwc.isParameterSet("building_filter")) {
			this.global_building = Integer.valueOf(iwc.getParameter("building_filter"));
			iwc.setSessionAttribute("building_filter", this.global_building);
		} else if (iwc.getSessionAttribute("building_filter") != null) {
			this.global_building = ((Integer) iwc.getSessionAttribute("building_filter"));
		}
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
		T.add(getHeader(localize("renewal.action_send_contract", "Send contract")), 4, 1);
		T.add(new SubmitButton(SEND_OFFER, SEND_OFFER), 1, 2);
		T.add(new SubmitButton(SEND_REMINDER, SEND_REMINDER), 2, 2);
		T.add(new SubmitButton(CLOSE_OFFER, CLOSE_OFFER), 3, 2);
		T.add(new SubmitButton(SEND_CONTRACT, SEND_CONTRACT), 4, 2);

		form.add(T);

		return (form);
	}

	private PresentationObject statusForm() {
		Form form = new Form();
		DropdownMenu status = statusDrop("status_filter", "");
		DropdownMenu complex = drpLodgings(Complex.class, "complex_filter", "--", this.global_complex);
		DropdownMenu building = drpLodgings(Building.class, "building_filter", "--", this.global_building);

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
			offers = getContractRenewalService(iwc).getContractRenewalOffers(this.global_status, this.global_complex, this.global_building);
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
					if (offer.getRenewalGranted() != null && "Y".equals(offer.getRenewalGranted())) {
						renew.setChecked(true);
					}
					T.add(renew, col++, row);

					CheckBox decline = new CheckBox(
							"decline_contract", offer.getUniqueId());
					if (offer.getRenewalGranted() != null && "N".equals(offer.getRenewalGranted())) {
						decline.setChecked(true);
					}
					T.add(decline, col++, row++);
					if (col > maxCol) {
						maxCol = col -1;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
			SubmitButton save = new SubmitButton(SAVE, SAVE);
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
		drp.addMenuElement(-1, localize("renewal.status_all", "All"));
		drp.addMenuElement(0, localize("renewal.status_answered", "Answered"));
		drp.addMenuElement(1, localize("renewal.status_unanswered", "Unanswered"));
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