package is.idega.idegaweb.campus.presentation;

import is.idega.idegaweb.campus.data.ContractRenewalOffer;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

public class ContractRenewalOfferForm extends CampusBlock {
	private static final String RENEWAL_DECLINE = "renewal.decline";
	private static final String RENEWAL_ACCEPT = "renewal.accept";
	private String offerUUID;
	private ContractRenewalOffer offer = null;

	public ContractRenewalOfferForm() {
		super();
	}

	protected void control(IWContext iwc) throws RemoteException {
		offerUUID = iwc.getParameter(ContractRenewalOfferInput.OFFER_UUID);
		try {
			offer = getContractRenewalService(iwc)
					.getContractRenewalOfferHome().findByUUID(offerUUID);
		} catch (RemoteException e) {
		} catch (FinderException e) {
		}

		if (iwc.isParameterSet(RENEWAL_ACCEPT)) {
			acceptRenewal(iwc);
		} else if (iwc.isParameterSet(RENEWAL_DECLINE)) {
			declineRenewal(iwc);
		}
		addOfferLookup(iwc);
	}

	private void acceptRenewal(IWContext iwc) {
		if (offer != null) {
			offer.setAnswer(true);
			offer.setOfferAnsweredDate(IWTimestamp.getTimestampRightNow());
			offer.store();
		}
	}

	private void declineRenewal(IWContext iwc) {
		if (offer != null) {
			offer.setAnswer(false);
			offer.setOfferAnsweredDate(IWTimestamp.getTimestampRightNow());
			offer.store();
		}
	}

	private void addOfferLookup(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(ContractRenewalOfferInput.OFFER_UUID);

		Table table = new Table();
		table.setCellpadding(2);
		table.setCellspacing(2);

		form.add(table);
		add(form);

		int row = 1;

		if (offer == null) {
			table.add(
					getErrorText(localize("renewal.no_offer_found_for_uuid",
							"No offer found for this uuid")), 1, row++);
		} else {
			if (offer.getOfferAnsweredDate() == null) {
				table.add(
						getHeader(localize("renewal.offer_info",
								"Offer info")), 1, row);
				table.mergeCells(1, row, 2, row);
				row++;
				table.add(
						getHeader(localize(
								"renewal.offered",
								"You have been offered the following.... please select accept/decline")), 1, row);
				table.mergeCells(1, row, 2, row);
				row++;
				table.add(new SubmitButton(RENEWAL_ACCEPT, localize(RENEWAL_ACCEPT, "Accept")), 1, row);
				table.add(new SubmitButton(RENEWAL_DECLINE, localize(RENEWAL_DECLINE, "Decline")), 2, row);
			} else {
				table.add(
						getHeader(localize("renewal.offer_info",
								"Offer info")), 1, row);
				table.mergeCells(1, row, 2, row);
				row++;
				table.add(
						getHeader(localize(
								"renewal.already_answered",
								"You have already responded to your renewal offer")), 1, row);
				table.mergeCells(1, row, 2, row);				
			}
		}
	}

	public void main(IWContext iwc) {
		try {
			control(iwc);
		} catch (RemoteException e) {
			add("Service is unavailable please come back later");
		}
	}

}
