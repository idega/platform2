package se.idega.idegaweb.commune.accounting.update.presentation;

import java.util.List;
import java.util.Collection;
import java.util.Iterator;

import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.CalendarMonth;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationHome;
import se.idega.idegaweb.commune.accounting.regulations.data.AgeRegulation;
import se.idega.idegaweb.commune.accounting.regulations.data.AgeRegulationHome;
import se.idega.idegaweb.commune.accounting.regulations.data.VATRegulation;
import se.idega.idegaweb.commune.accounting.regulations.data.VATRegulationHome;
import se.idega.idegaweb.commune.accounting.posting.data.PostingParameters;
import se.idega.idegaweb.commune.accounting.posting.data.PostingParametersHome;

/**
 * Temporary UI to set the "to"-date to last day in that month for Regulation, AgeRegulation, VATRegulation, PostingParameters 
 * This should be run once. Can be deleted afterwards. Please dont mind the direct accesses to the beans.
 * If it is run twice or more. No worries. It works anyway and never affects the dates.
 * @author Kelly
 */
public class SetToDateAsLastInMonthInRegulationsAndPosting extends AccountingBlock {
	List list;
	
	public void init(IWContext iwc){
	
		if (handleAction(iwc)) {
			add("Press Save to set the To-Date to last day in month for Regulation, AgeRegulation, VATRegulation, PostingParameters ");
		}
		
		Form form = new Form();
		add(form);
		GenericButton saveButton = this.getSaveButton();
		form.add(saveButton);

		
	}
	
	/**
	 * @param iwc
	 */
	private boolean handleAction(IWContext iwc) {
		if(iwc.isParameterSet(PARAM_SAVE)){
			handleSave();
			add("Done!");
			return false;
		}
		return true;
	}
	
	/**
	 * @param iwc
	 */
	private void handleSave() {
		try {
			Iterator iterator;
			Collection regulations = getRegulationHome().findAllRegulations();
			Collection ageRegulations = getAgeRegulationHome().findAll();
			Collection vatRegulations = getVATRegulationHome().findAll();
			Collection postingParameters = getPostingParametersHome().findAllPostingParameters();
			
			iterator = regulations.iterator();
			while (iterator.hasNext()) {
				Regulation r = (Regulation) iterator.next();
				CalendarMonth fixedDate = new CalendarMonth(r.getPeriodTo());
				r.setPeriodTo(fixedDate.getLastDateOfMonth());
				r.store();
			}

			iterator = ageRegulations.iterator();
			while (iterator.hasNext()) {
				AgeRegulation ar = (AgeRegulation) iterator.next();
				CalendarMonth fixedDate = new CalendarMonth(ar.getPeriodTo());
				ar.setPeriodTo(fixedDate.getLastDateOfMonth());
				ar.store();
			}

			iterator = vatRegulations.iterator();
			while (iterator.hasNext()) {
				VATRegulation vr = (VATRegulation) iterator.next();
				CalendarMonth fixedDate = new CalendarMonth(vr.getPeriodTo());
				vr.setPeriodTo(fixedDate.getLastDateOfMonth());
				vr.store();
			}

			iterator = postingParameters.iterator();
			while (iterator.hasNext()) {
				PostingParameters pp = (PostingParameters) iterator.next();
				CalendarMonth fixedDate = new CalendarMonth(pp.getPeriodTo());
				pp.setPeriodTo(fixedDate.getLastDateOfMonth());
				pp.store();
			}

			
		} catch (Exception e) {
			add(new ExceptionWrapper(e));
		}
	}

	private RegulationHome getRegulationHome() {
		try {
			return (RegulationHome) IDOLookup.getHome(Regulation.class);
		}
		catch (IDOLookupException e) {}
		return null;
	}
	
	private AgeRegulationHome getAgeRegulationHome() {
		try {
			return (AgeRegulationHome) IDOLookup.getHome(AgeRegulation.class);
		}
		catch (IDOLookupException e) {}
		return null;
	}

	private VATRegulationHome getVATRegulationHome() {
		try {
			return (VATRegulationHome) IDOLookup.getHome(VATRegulation.class);
		}
		catch (IDOLookupException e) {}
		return null;
	}

	private PostingParametersHome getPostingParametersHome() {
		try {
			return (PostingParametersHome) IDOLookup.getHome(PostingParameters.class);
		}
		catch (IDOLookupException e) {}
		return null;
	}

}
