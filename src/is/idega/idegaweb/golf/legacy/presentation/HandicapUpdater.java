package is.idega.idegaweb.golf.legacy.presentation;

import is.idega.idegaweb.golf.UpdateHandicap;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public class HandicapUpdater extends GolfBlock {

	private static final String PARAMETER_DATE = "prm_date";
	
	public void main(IWContext iwc) throws Exception {
		
		if (iwc.isParameterSet(PARAMETER_DATE)) {
			updateHandicap(iwc);
		} 
		else {
			dateForm(iwc);
		}
		
	}
	
	private void updateHandicap(IWContext iwc) throws IDOLookupException, FinderException {
		MemberHome memberHome = (MemberHome) IDOLookup.getHome(Member.class);
		System.out.println("HandicapUpdater : starting updating : "+IWTimestamp.RightNow());
		Collection allMembers = memberHome.findAll();
		IWTimestamp stamp = new IWTimestamp(iwc.getParameter(PARAMETER_DATE));
		int counter = 0;
		System.out.println("HandicapUpdater : member collection collected : "+IWTimestamp.RightNow());
		if (allMembers != null) {
			Iterator iter = allMembers.iterator();
			Member member;
			while (iter.hasNext()) {
				++counter;
				UpdateHandicap.update((Member) iter.next(), stamp);
				if (counter % 100 == 0) {
					System.out.println("HandicapUpdater : finished updating "+counter+" members : "+IWTimestamp.RightNow());
				}
			}
		}
		System.out.println("HandicapUpdater : finished updating : "+IWTimestamp.RightNow());

	}

	private void dateForm(IWContext iwc) {
		Form form = new Form();
		Table table = new Table();
		form.add(table);
		
		table.add(getSmallText(localize("update_from_date","Update from")+" :"), 1, 1);
		table.add((DateInput) getStyledInterface(new DateInput(PARAMETER_DATE)), 2, 1);

		table.add((SubmitButton) getStyledInterface(new SubmitButton(localize("update", "Update"))), 2, 2);

		add(form);
	}
	
}
