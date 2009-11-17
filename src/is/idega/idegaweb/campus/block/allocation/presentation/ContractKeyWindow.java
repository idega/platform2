package is.idega.idegaweb.campus.block.allocation.presentation;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.nortek.business.NortekBusiness;
import is.idega.idegaweb.campus.nortek.data.Card;
import is.idega.idegaweb.campus.presentation.CampusWindow;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Title: idegaclasses Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class ContractKeyWindow extends CampusWindow {

	private static final String PARAM_DELIVERED_DATE = "deliveredDate";
	private static final String PARAM_TYPE = "val";
	private static final String TYPE_DELIVER = "deliver";
	private static final String TYPE_RETURN = "return";
	private static final String PARAM_SAVE_X = "save.x";
	private static final String PARAM_SAVE = "save";
	private static final String PARAM_INVALIDATE_LAUNDRY_CARD = "invalidate_card";
	public static String PARAM_CONTARACT = "contract_id";
	
	public ContractKeyWindow() {
		setWidth(300);
		setHeight(250);
		setResizable(true);
	}

	protected void control(IWContext iwc) throws RemoteException {

		if (iwc.isLoggedOn()) {
			if (iwc.isParameterSet(PARAM_SAVE) || iwc.isParameterSet(PARAM_SAVE_X)) {
				doContract(iwc);
				setParentToReload();
			}
			add(getSignatureTable(iwc));
		} else {
			add(getText(localize("access_denied", "Access denied")));
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public PresentationObject makeLinkTable(int menuNr) {
		Table LinkTable = new Table(6, 1);

		return LinkTable;
	}

	private PresentationObject getSignatureTable(IWContext iwc) {
		int iContractId = Integer.parseInt(iwc.getParameter(PARAM_CONTARACT));
		try {
			ContractHome contractHome = (ContractHome) IDOLookup
					.getHome(Contract.class);
			Contract eContract = contractHome.findByPrimaryKey(new Integer(
					iContractId));
			User eUser = eContract.getUser();
			Apartment apartment = eContract.getApartment();
			IWTimestamp from = new IWTimestamp(eContract.getValidFrom());
			IWTimestamp to = new IWTimestamp(eContract.getValidTo());
			Applicant eApplicant = ((ApplicantHome) com.idega.data.IDOLookup
					.getHome(Applicant.class)).findByPrimaryKey(eContract
					.getApplicantId());
			boolean apartmentReturn = eContract.getIsRented();
			DataTable T = new DataTable();
			T.setWidth("100%");
			T.addButton(new CloseButton(getResourceBundle().getImage(
					"close.gif")));
			String val = "";
			SubmitButton save = new SubmitButton(getResourceBundle().getImage(
					"save.gif"), PARAM_SAVE);
			boolean canSave = false;
			if (apartmentReturn) {
				T.addTitle(getResourceBundle().getLocalizedString(
						"apartment_return", "Apartment return"));
				val = TYPE_RETURN;
				if (eContract
						.getStatus()
						.equals(
								is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean.STATUS_ENDED)
						|| eContract
								.getStatus()
								.equals(
										is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean.STATUS_RESIGNED)) {
					T.addButton(save);
					canSave = true;
				}
			} else {
				T.addTitle(localize("apartment_deliver", "Apartment deliver"));
				val = TYPE_DELIVER;
				if (eContract
						.getStatus()
						.equals(
								is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean.STATUS_SIGNED)) {
					T.addButton(save);
					canSave = true;
				}
			}
			T.add(new HiddenInput(PARAM_TYPE, val), 1, 1);

			int row = 1;
			HiddenInput HI = new HiddenInput(PARAM_CONTARACT, eContract
					.getPrimaryKey().toString());
			T.add(HI, 1, row);
			T.add(getHeader(localize("name", "Name")), 1, row);
			T.add(getText(eApplicant.getFullName()), 2, row);
			row++;
			T.add(getHeader(localize("ssn", "SocialNumber")), 1, row);
			T.add(getText(eApplicant.getSSN()), 2, row);
			row++;

			T.add(getHeader(localize("apartment", "Apartment")), 1, row);
			T.add(getText(getApartmentString(apartment)), 2, row);

			row++;
			T.add(getHeader(localize("valid_from", "Valid from")), 1, row);
			T.add(getText(from.getLocaleDate(iwc.getCurrentLocale())), 2, row);
			row++;
			T.add(getHeader(localize("valid_to", "Valid to")), 1, row);
			T.add(getText(to.getLocaleDate(iwc.getCurrentLocale())), 2, row);
			row++;
			T.add(getHeader(localize("status", "Status")), 1, row);
			T.add(getText(getStatus(eContract.getStatus())), 2, row);
			row++;
			T.add(getHeader(localize("returned", "Returned")), 1, row);
			java.sql.Timestamp retstamp = eContract.getReturnTime();
			if (retstamp != null) {
				IWTimestamp ret = new IWTimestamp(retstamp);
				T.add(getText(ret.getLocaleDate(iwc.getCurrentLocale())), 2,
						row);
			}
			row++;
			T.add(getHeader(localize("delivered", "Delivered")), 1, row);
			java.sql.Timestamp delstamp = eContract.getDeliverTime();
			if (delstamp != null) {
				IWTimestamp del = new IWTimestamp(delstamp);
				T.add(getText(del.getLocaleDate(iwc.getCurrentLocale())), 2,
						row);
			} else {
				if (canSave) {
					IWTimestamp del = new IWTimestamp();
					DateInput input = new DateInput(PARAM_DELIVERED_DATE);
					input.setDate(del.getDate());

					T.add(input, 2, row);
				}
			}

			row++;

			if (apartmentReturn) {
				Card card = getNortekBusiness(iwc).getCard(eUser);
				if (card != null && card.getIsValid()) {
					T.add(getHeader(localize("laundry_card", "Invalidate laundry card")), 1, row);
					CheckBox invalidateCard = new CheckBox(PARAM_INVALIDATE_LAUNDRY_CARD, "checked");
					invalidateCard.setChecked(false);
					T.add(invalidateCard, 2, row);
				}
			}
			
			Form F = new Form();
			F.add(T);
			return F;
		} catch (Exception ex) {
			return new Text("");
		}
	}

	private void doContract(IWContext iwc) throws RemoteException {
		Integer id = Integer.valueOf(iwc.getParameter(PARAM_CONTARACT));
		String invalidateCard = iwc.getParameter(PARAM_INVALIDATE_LAUNDRY_CARD);
		if (iwc.isParameterSet(PARAM_TYPE)) {
			String val = iwc.getParameter(PARAM_TYPE);
			if (val.equals(TYPE_RETURN)) {
				getContractService(iwc).returnKey(id, iwc.getCurrentUser());
				if (invalidateCard != null && "checked".equals(invalidateCard)) {
					try {
						Contract contract = getContractService(iwc).getContractHome().findByPrimaryKey(id);
						Card card = getNortekBusiness(iwc).getCard(contract.getUser());
						card.setIsValid(false);
						card.store();
					} catch (FinderException e) {
					}
				}
			} else if (val.equals(TYPE_DELIVER)) {
				String from = iwc.getParameter(PARAM_DELIVERED_DATE);
				IWTimestamp fromStamp = new IWTimestamp(from);
				String addKeyCharge = iwc.getApplicationSettings().getProperty("AUTO_ADD_KEY_CHARGE", String.valueOf(false));
				String accountKeyID = iwc.getApplicationSettings().getProperty("AUTO_ADD_KEY_CHARGE_ACCOUNT_KEY_ID", "-1");
				String tariffGroupID = iwc.getApplicationSettings().getProperty("AUTO_ADD_KEY_CHARGE_TARIFF_GROUP_ID", "-1");
				String finanaceCategoryID = iwc.getApplicationSettings().getProperty("AUTO_ADD_KEY_CHARGE_FINANCE_CATEGORY_ID", "-1");
				String amount = iwc.getApplicationSettings().getProperty("AUTO_ADD_KEY_CHARGE_AMOUNT", "-1");
				getContractService(iwc)
						.deliverKey(id, fromStamp.getTimestamp(), Boolean.valueOf(addKeyCharge).booleanValue(), Integer.valueOf(accountKeyID), Integer.valueOf(tariffGroupID), Integer.valueOf(finanaceCategoryID), Double.valueOf(amount).doubleValue());
			}
		}
	}

	private String getApartmentString(Apartment A) {
		StringBuffer S = new StringBuffer();
		Floor F = A.getFloor();
		Building B = F.getBuilding();
		Complex C = B.getComplex();
		S.append(A.getName());
		S.append(" ");
		S.append(F.getName());
		S.append(" ");
		S.append(B.getName());
		S.append(" ");
		S.append(C.getName());
		return S.toString();

	}

	private String getStatus(String status) {
		String r = "";
		char c = status.charAt(0);
		switch (c) {
		case 'C':
			r = localize("created", "Created");
			break;
		case 'P':
			r = localize("printed", "Printed");
			break;
		case 'S':
			r = localize("signed", "Signed");
			break;
		case 'R':
			r = localize("rejected", "Rejected");
			break;
		case 'T':
			r = localize("terminated", "Terminated");
			break;
		case 'E':
			r = localize("ended", "Ended");
			break;
		}
		return r;
	}

	public void main(IWContext iwc) throws RemoteException {
		control(iwc);
	}
	
	private NortekBusiness getNortekBusiness(IWContext iwc) throws IBOLookupException {
		NortekBusiness bus1 = (NortekBusiness) IBOLookup.getServiceInstance(
				iwc, NortekBusiness.class);

		return bus1;
	}
}