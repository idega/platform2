/*
 * $Id: CitizenAccountAdmin.java,v 1.26 2004/06/09 07:01:27 malin Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.account.citizen.presentation;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusiness;
import se.idega.idegaweb.commune.account.citizen.data.AdminListOfApplications;
import se.idega.idegaweb.commune.account.citizen.data.CitizenAccount;
import se.idega.idegaweb.commune.account.citizen.data.CitizenApplicantMovingTo;
import se.idega.idegaweb.commune.account.citizen.data.CitizenApplicantPutChildren;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.business.IBOLookup;
import com.idega.core.accesscontrol.business.UserHasLoginException;
import com.idega.core.location.data.Commune;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.util.PersonalIDFormatter;

/**
 * CitizenAccountAdmin is an IdegaWeb block that displays the admintstation user
 * interface for accpting and rejecting citizen account applications. It is
 * based on session ejb classes in
 * {@link se.idega.idegaweb.commune.account.citizen.business} and entity ejb
 * classes in {@link se.idega.idegaweb.commune.account.citizen.business.data}.
 * <p>
 * Last modified: $Date: 2004/06/09 07:01:27 $ by $Author: malin $
 *
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.26 $
 */
public class CitizenAccountAdmin extends CommuneBlock {
	private final static int ACTION_VIEW_LIST = 0;
	private final static int ACTION_VIEW_DETAILS = 1;
	private final static int ACTION_APPROVE = 2;
	private final static int ACTION_REJECT = 3;
	private final static int ACTION_REMOVE = 4;

	private final static String ADDRESS_DEFAULT = "Address";
	private final static String ADDRESS_KEY = "caa_adm_address";
	private final static String MESSAGE_DEFAULT = "Meddelande";
	private final static String MESSAGE_KEY = "caa_adm_message";
	private final static String NAME_DEFAULT = "Namn";
	private final static String NAME_KEY = "caa_adm_name";

	private final static String PARAM_FORM_APPROVE = "caa_adm_approve";
	private final static String PARAM_FORM_REJECT = "caa_adm_reject";
	private final static String PARAM_FORM_DETAILS = "caa_adm_details";
	private final static String PARAM_FORM_REMOVE = "caa_adm_remove";
	private final static String PARAM_FORM_CANCEL = "caa_adm_cancel";
	private final static String PARAM_FORM_LIST = "caa_adm_list";

    private final static String USER_ALLREADY_HAS_A_LOGIN_KEY = "caa_adm_user_has_login";
    private final static String USER_ALLREADY_HAS_A_LOGIN_DEFAULT = "Användaren har redan ett konto";

	public void main(IWContext iwc) {
		setResourceBundle(getResourceBundle(iwc));

		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_VIEW_LIST :
					viewList(iwc);
					break;
				case ACTION_VIEW_DETAILS :
					viewDetails(iwc);
					break;
				case ACTION_APPROVE :
					approve(iwc);
					break;
				case ACTION_REJECT :
					reject(iwc);
					break;
				case ACTION_REMOVE :
					remove(iwc);
					break;
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAM_FORM_APPROVE)) {
			String value = iwc.getParameter(PARAM_FORM_APPROVE);
			if (value != null && !value.equals(""))
				return ACTION_APPROVE;
		}

		if (iwc.isParameterSet(PARAM_FORM_REJECT)) {
			String value = iwc.getParameter(PARAM_FORM_REJECT);
			if (value != null && !value.equals(""))
				return ACTION_REJECT;
		}

		if (iwc.isParameterSet(PARAM_FORM_REMOVE)) {
			String value = iwc.getParameter(PARAM_FORM_REMOVE);
			if (value != null && !value.equals(""))
				return ACTION_REMOVE;
		}

		if (iwc.isParameterSet(PARAM_FORM_DETAILS)) {
			String value = iwc.getParameter(PARAM_FORM_DETAILS);
			if (value != null && !value.equals(""))
				return ACTION_VIEW_DETAILS;
		}

		return ACTION_VIEW_LIST;
	}

	private void viewList(IWContext iwc) {
		Form form = new Form();
		Table table = new Table();
		table.setColumns(4);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setWidth(getWidth());
		table.setWidth(4, "12");

		int row = 1;
		int col = 1;
		table.setRowColor(row, getHeaderColor());
		table.add(getSmallHeader(localize(NAME_KEY, NAME_DEFAULT)), col++, row);
		table.add(getSmallHeader(localize(CitizenAccountApplication.SSN_KEY, CitizenAccountApplication.SSN_DEFAULT)), col++, row);
		table.add(getSmallHeader(localize(ADDRESS_KEY, ADDRESS_DEFAULT)), col, row++);

		List applications = null;
		try {
			CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			applications = business.getListOfUnapprovedApplications();
		}
		catch (RemoteException e) {
            e.printStackTrace ();
		}

		if (applications != null && !applications.isEmpty()) {
			Iterator it = applications.iterator();
			while (it.hasNext()) {
				col = 1;
				AdminListOfApplications list = (AdminListOfApplications) it.next();
				if (list.getName() != null){
					table.add(getSmallText(list.getName()), col++, row);	
				}
				else {
					col++;
				}
				String personalID = PersonalIDFormatter.format(list.getPID(), iwc.getIWMainApplication().getSettings().getApplicationLocale());
				if (personalID != null) {
					table.add(getSmallText(personalID), col++, row);	
				}
				else{
					col++;
				}
				if (list.getAddress().indexOf("null", 0) == -1){
					table.add(getSmallText(list.getAddress()), col++, row);	
				}
				else {
					col++;
				}
	
				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());
	
				SubmitButton details = new SubmitButton(getVariousIcon(""), PARAM_FORM_DETAILS, list.getId());
				details.setDescription(localize(PARAM_FORM_DETAILS, "Administrate"));
				table.add(details, col, row++);
			}
		}

		form.add(table);
		add(form);
	}

	private void viewDetails(IWContext iwc) {
		Form form = new Form();
		Table table = new Table();
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(0);
		table.setColumns(3);
		table.setWidth(1, "30%");
		int row = 1;

		try {
			final CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			final String idAsString = iwc.getParameter(PARAM_FORM_DETAILS);
			final int id = new Integer(idAsString).intValue();
			final CitizenAccount applicant = business.getAccount(id);

			table.add(getSmallHeader(localize(NAME_KEY, NAME_DEFAULT)), 1, row);
			if (applicant.getApplicantName() != null){
				table.add(getSmallText(applicant.getApplicantName()), 3, row++);	
			}
			else{
				row++;
			}
			
			table.add(getSmallHeader(localize(CitizenAccountApplication.SSN_KEY, CitizenAccountApplication.SSN_DEFAULT)), 1, row);
			final String pid = PersonalIDFormatter.format(applicant.getSsn(), iwc.getIWMainApplication().getSettings().getApplicationLocale());
			if (pid != null){
				table.add(getSmallText(pid), 3, row++);
			}
			else{
				row++;
			}
				
			table.add(getSmallHeader(localize(CitizenAccountApplication.EMAIL_KEY, CitizenAccountApplication.EMAIL_DEFAULT)), 1, row);
			final Link email = getSmallLink(applicant.getEmail());
			
			if (applicant.getEmail() != null){
				email.setURL("mailto:" + applicant.getEmail());
				table.add(email, 3, row++);
			}
			else {
				row++;
			}
			
			table.add(getSmallHeader(localize(CitizenAccountApplication.PHONE_HOME_KEY, CitizenAccountApplication.PHONE_HOME_DEFAULT)), 1, row);
			if (applicant.getPhoneHome() != null){
				table.add(getSmallText(applicant.getPhoneHome()), 3, row++);
			}
			else {
				row++;
			}
			table.add(getSmallHeader(localize(CitizenAccountApplication.PHONE_WORK_KEY, CitizenAccountApplication.PHONE_WORK_DEFAULT)), 1, row);
			if (applicant.getPhoneWork() != null){
				table.add(getSmallText(applicant.getPhoneWork()), 3, row++);
			}
			else {
				row++;
			}
			final String careOf = applicant.getCareOf ();
			final String address = (careOf != null ? "c/o " + careOf + ' ' : "") + applicant.getStreet() + "; " + applicant.getZipCode() + " " + applicant.getCity();
			if (applicant.getStreet() != null && applicant.getZipCode() != null && applicant.getCity() != null) {
				table.add(getSmallHeader(localize(ADDRESS_KEY, ADDRESS_DEFAULT)), 1, row);
				table.add(getSmallText(address), 3, row++);
			}

			if (applicant.getCivilStatus() != null) {
				table.add(getSmallHeader(localize(CitizenAccountApplication.CIVIL_STATUS_KEY, CitizenAccountApplication.CIVIL_STATUS_DEFAULT)), 1, row);
				table.add(getSmallText(applicant.getCivilStatus()), 3, row++);
			}

			table.add(getSmallHeader(localize(CitizenAccountApplication.COHABITANT_KEY, CitizenAccountApplication.COHABITANT_DEFAULT)), 1, row);
			final String hasCohabitant = applicant.hasCohabitant() ? localize(CitizenAccountApplication.YES_KEY, CitizenAccountApplication.YES_DEFAULT) : localize(CitizenAccountApplication.NO_KEY, CitizenAccountApplication.NO_DEFAULT);
			table.add(getSmallText(hasCohabitant), 3, row++);
			
			table.add(getSmallHeader(localize(CitizenAccountApplication.CHILDREN_COUNT_KEY, CitizenAccountApplication.CHILDREN_COUNT_DEFAULT)), 1, row);
			table.add(getSmallText(String.valueOf(applicant.getChildrenCount())), 3, row++);
			
			if (applicant.getApplicationReason() != null) {
				table.add(getSmallHeader(localize(CitizenAccountApplication.APPLICATION_REASON_KEY, CitizenAccountApplication.APPLICATION_REASON_DEFAULT)), 1, row);
				final String applicationReason = localize(applicant.getApplicationReason(), "?");
				table.add(getSmallText(applicationReason), 3, row++);

                if (applicant.getApplicationReason().equals
                    (CitizenAccount.PUT_CHILDREN_IN_NACKA_SCHOOL_KEY)
                	|| applicant.getApplicationReason().equals
                	(CitizenAccount.PUT_CHILDREN_IN_NACKA_CHILDCARE_KEY)
					|| applicant.getApplicationReason().equals
					(CitizenAccount.MOVING_TO_NACKA_KEY)) {
                    try {
                        final CitizenApplicantPutChildren capc = business.findCitizenApplicantPutChildren (id);
                        Commune commune = business.findCommuneByCommunePK(new Integer(capc.getCurrentCommuneId()));
                        table.add(getSmallHeader(localize(CitizenAccountApplication.CURRENT_KOMMUN_KEY, CitizenAccountApplication.CURRENT_KOMMUN_DEFAULT)), 1, row);
                        if (commune != null) {
                        	table.add(getSmallText(commune.getCommuneName()), 3, row++);
                        }
                    } catch (FinderException e) {
                        System.err.println (e.getMessage ());
                    }
                } 
                if (applicant.getApplicationReason().equals
                           (CitizenAccount.MOVING_TO_NACKA_KEY)) {
                    try {
                        final CitizenApplicantMovingTo camt
                                = business.findCitizenApplicantMovingTo (id);
                        table.add(getSmallHeader(localize(CitizenAccountApplication.MOVING_IN_ADDRESS_KEY, CitizenAccountApplication.MOVING_IN_ADDRESS_DEFAULT)), 1, row);
                        table.add(getSmallText(camt.getAddress ()), 3, row++);
                        table.add(getSmallHeader(localize(CitizenAccountApplication.MOVING_IN_DATE_KEY, CitizenAccountApplication.MOVING_IN_DATE_DEFAULT)), 1, row);
                        table.add(getSmallText(camt.getMovingInDate ()), 3, row++);
                        if (camt.getHousingType ().equals (CitizenAccountApplication.TENANCY_AGREEMENT_KEY)) {
                            table.add(getSmallHeader(localize(CitizenAccountApplication.LANDLORD_KEY, CitizenAccountApplication.LANDLORD_DEFAULT)), 1, row);
                            table.add(getSmallText(camt.getLandlord ()), 3, row++);
                        } else if (camt.getHousingType ().equals (CitizenAccountApplication.DETACHED_HOUSE_KEY)) {
                            table.add(getSmallHeader(localize(CitizenAccountApplication.PROPERTY_TYPE_KEY, CitizenAccountApplication.PROPERTY_TYPE_DEFAULT)), 1, row);
                            table.add(getSmallText(camt.getPropertyType ()), 3, row++);
                        }
                    } catch (FinderException e) {
                        System.err.println (e.getMessage ());
                    }
                }
			}
			
			table.setHeight(row++, 6);
			table.mergeCells(1, row, 3, row);
			table.setWidth(row, Table.HUNDRED_PERCENT);
			table.add(getSmallHeader(localize(MESSAGE_KEY, MESSAGE_DEFAULT)), 1, row);
			table.add(new Break(), 1, row);
			TextArea area = new TextArea(MESSAGE_KEY);
			area.setRows(7);
			area.setColumns(40);
			table.add(area, 1, row++);

			SubmitButton approve = (SubmitButton) getButton(new SubmitButton(localize(PARAM_FORM_APPROVE, "Godkänn"), PARAM_FORM_APPROVE, idAsString));
			SubmitButton reject = (SubmitButton) getButton(new SubmitButton(localize(PARAM_FORM_REJECT, "Avslå"), PARAM_FORM_REJECT, idAsString));
			SubmitButton remove = (SubmitButton) getButton(new SubmitButton(localize(PARAM_FORM_REMOVE, "Ta bort"), PARAM_FORM_REMOVE, idAsString));
			SubmitButton cancel = (SubmitButton) getButton(new SubmitButton(localize(PARAM_FORM_CANCEL, "Avbryt"), PARAM_FORM_CANCEL, idAsString));

			table.setHeight(row++, 12);
			table.mergeCells(1, row, table.getColumns(), row);

			table.add(approve, 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
			table.add(reject, 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
			table.add(remove, 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
			table.add(cancel, 1, row);
		}
		catch (final Exception e) {
			e.printStackTrace();
		}

		form.add(table);
		add(form);
	}

	private void approve(IWContext iwc) {
		final Form form = new Form();
		final String id = iwc.getParameter(PARAM_FORM_APPROVE);

		try {
			final CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			business.acceptApplication(new Integer(id).intValue(), iwc.getCurrentUser());

			form.add(getText(localize("caa_acc_application", "Godkänd ansökan: ") + id));
		} catch (UserHasLoginException uhle) {
			form.add (getText (localize(USER_ALLREADY_HAS_A_LOGIN_KEY,
                                       USER_ALLREADY_HAS_A_LOGIN_DEFAULT)));
		} catch (Exception e) {
			e.printStackTrace();
			form.add(getText(localize("caa_acc_application_failed", "Ett fel inträffade vid godkännade av ansökan: ") + id));
		}

		final SubmitButton list = (SubmitButton) getButton(new SubmitButton(localize(PARAM_FORM_LIST, "Lista"), PARAM_FORM_LIST, ""));
		form.add(Text.BREAK);
		form.add(Text.BREAK);
		form.add(list);
		add(form);
	}

	private void reject(IWContext iwc) {
		Form form = new Form();
		String id = iwc.getParameter(PARAM_FORM_REJECT);

		try {
			CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
			if (iwc.isParameterSet(MESSAGE_KEY)) {
				business.rejectApplication(new Integer(id).intValue(), iwc.getCurrentUser(), iwc.getParameter(MESSAGE_KEY));
			form.add(getText(localize("caa_rej_application", "Rejected application number : ") + id));
			} else {
                form.add (getText(localize("caa_rej_no_message", "Du måste fylla i fältet 'Meddelande/Orsak'")));
            }
		} catch (Exception e) {
			e.printStackTrace();
			form.add(getText(localize("caa_rej_application_failed", "There was an error rejecting application number : ") + id));
		}

		SubmitButton list = (SubmitButton) getButton(new SubmitButton(localize(PARAM_FORM_LIST, "Lista"), PARAM_FORM_LIST, ""));
		form.add(Text.BREAK);
		form.add(Text.BREAK);
		form.add(list);
		add(form);
	}

	private void remove(IWContext iwc) {
		Form form = new Form();
		String id = iwc.getParameter(PARAM_FORM_REMOVE);

		try {
			CitizenAccountBusiness business = (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwc, CitizenAccountBusiness.class);
            business.removeApplication(new Integer(id).intValue(), iwc.getCurrentUser());
			form.add(getText(localize("caa_rem_application", "Tog bort ansökan nummer: ") + id));
		} catch (Exception e) {
			e.printStackTrace();
			form.add(getText(localize("caa_rem_application_failed", "Ett fel inträffade vid borttagning av ansökan nummer: ") + id));
		}

		SubmitButton list = (SubmitButton) getButton(new SubmitButton(localize(PARAM_FORM_LIST, "Lista"), PARAM_FORM_LIST, ""));
		form.add(Text.BREAK);
		form.add(Text.BREAK);
		form.add(list);
		add(form);
	}
}
