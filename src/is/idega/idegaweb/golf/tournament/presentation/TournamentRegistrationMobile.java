/*
 * Created on Jul 7, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.tournament.presentation;

import java.sql.SQLException;

import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.tournament.business.TournamentSession;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TournamentRegistrationMobile extends Block {
	
	private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.golf";
	
	private IWResourceBundle _iwrb;
	
	public void main(IWContext iwc) throws Exception {
		_iwrb = getResourceBundle(iwc);
		
//		PresentationObject tournamentSearchForm = getTournamentSearchPO(iwc);
//		add(tournamentSearchForm);
	}
	
	private PresentationObject getTournamentSearch(IWContext iwc) {
		try {
			Form tournamentSearchForm = new Form();
			
			String union_id = iwc.getParameter("union_id");
			
			String clubSelectionLabel = _iwrb.getLocalizedString("tournament.club", "Club") + ": ";
			DropdownMenu clubMenu = getClubMenu(iwc, union_id);
			
			IWTimestamp now = IWTimestamp.RightNow();
			IWCalendar dagatalid = new IWCalendar();
	
			String fromDateLabel = _iwrb.getLocalizedString("tournament.from", "From") + ": ";
			DateInput startDate = getDateMenu(getTournamentSession(iwc).getParameterNameStartDate(), now.getYear(), getStartStamp(iwc).getDate());
			
			String toDateLabel = _iwrb.getLocalizedString("tournament.to", "To") + ": ";
			DateInput endDate = getDateMenu(getTournamentSession(iwc).getParameterNameEndDate(), now.getYear(), getEndStamp(iwc).getDate());
			
			tournamentSearchForm.add(clubSelectionLabel);
			tournamentSearchForm.add(clubMenu);
			tournamentSearchForm.addBreak();
			tournamentSearchForm.add(fromDateLabel);
			tournamentSearchForm.add(startDate);
			tournamentSearchForm.addBreak();
			tournamentSearchForm.add(toDateLabel);
			tournamentSearchForm.add(endDate);
			
			SubmitButton button = new SubmitButton();
			
			tournamentSearchForm.add(button);
			return tournamentSearchForm;
		} catch(Exception e) {
			return new Text();
		}
	}
	
	private DateInput getDateMenu(String paramName, int year, java.sql.Date date) {
		DateInput dateInput = new DateInput(paramName);
		dateInput.setYear(year);
		dateInput.setDate(date);
		return dateInput;
	}
	
	public IWTimestamp getStartStamp(IWContext modinfo) {
		IWTimestamp stamp = null;
		try {
			stamp = new IWTimestamp(getTournamentSession(modinfo).getStartDate());
		}
		catch (Exception e) {
			stamp = IWTimestamp.RightNow();
			stamp.addDays(-7);
		}

		return stamp;
	}

	public IWTimestamp getEndStamp(IWContext modinfo) {
		IWTimestamp stamp = null;

		try {
			stamp = new IWTimestamp(getTournamentSession(modinfo).getEndDate());
		}
		catch (Exception e) {
			stamp = getStartStamp(modinfo);
			stamp.addDays(14);
		}

		return stamp;
	}
	
	private DropdownMenu getClubMenu(IWContext iwc, String selected) throws SQLException {
		Union[] unions = (Union[]) ((Union) IDOLookup.instanciateEntity(Union.class)).findAllOrdered("ABBREVATION");
		DropdownMenu clubMenu = new DropdownMenu("union_id");
		if (unions != null) {
			for (int i = 0; i < unions.length; i++) {
				if (unions[i].getID() != 1) {
					clubMenu.addMenuElement(unions[i].getID(), unions[i].getAbbrevation() + "&nbsp;&nbsp;" + unions[i].getName());
				}
			}
		}
		if (selected != null && selected.length()>0) {
			clubMenu.setSelectedElement(selected);
		}
		return clubMenu;
	}
	
	private TournamentSession getTournamentSession(IWContext iwc) {
		try {
			return (TournamentSession) IBOLookup.getSessionInstance(iwc, TournamentSession.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	public String getBundleIdentifier(){
		return IW_BUNDLE_IDENTIFIER;
    }
	
}
