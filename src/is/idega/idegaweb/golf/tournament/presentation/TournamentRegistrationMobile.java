/*
 * Created on Jul 7, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.tournament.business.TournamentBusiness;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TournamentRegistrationMobile extends Block {
	
	public static final String PARAM_NAME_SEARCH_DAYS = "s_d";
	
	public static final String LOCALIZATION_KEY_SEARCH_TEXT = "mobile_search_text";
	public static final String LOCALIZATION_KEY_TOURNAMENT_SELECTION_TEXT = "mobile_selection_text";
	public static final String LOCALIZATION_KEY_TOURNAMENT_SELECTION_TEXT_AFTER = "mobile_selection_text_after";
	
	private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.golf";
	
	private static final int MAX_DAYS_FOR_SEARCH = 10;
	
	private ICPage _pageToSubmitTo = null;
	
	private IWResourceBundle _iwrb;
	private TournamentBusiness _biz = null;
	
	public void main(IWContext iwc) throws Exception {
		_iwrb = getResourceBundle(iwc);
		
		PresentationObject po;
		
		if(iwc.getParameter(PARAM_NAME_SEARCH_DAYS) == null ) {
			po = getTimeIntervalForm(iwc);
		} else {
			po = getTournamentSelectionForm(iwc);
		}
		
		add(po);
	}
	
	private PresentationObject getTournamentSelectionForm(IWContext iwc) {
		try {
			Paragraph cont = new Paragraph();
			
			String selectionLabel = _iwrb.getLocalizedString(LOCALIZATION_KEY_TOURNAMENT_SELECTION_TEXT, "Select tournament");
			cont.add(selectionLabel);
			
			int days = Integer.parseInt(iwc.getParameter(PARAM_NAME_SEARCH_DAYS));
			IWTimestamp begin = new IWTimestamp();
			begin.setHour(6);
			begin.setMinute(0);
			Tournament[] tournaments = getTournamentBusiness(iwc).getTournamentsWithRegistration(begin, days);
			for(int i=0; i<tournaments.length; i++) {
				Tournament t = tournaments[i];
				String day = (new IWTimestamp(t.getStartTime())).getDateString("EEE d MMM", iwc.getCurrentLocale());
				String name = t.getName();
				String place = t.getField().getName();
				String displayStr = day + " - " + name + " - " + place;
				cont.addBreak();
				Link link = new Link();
				link.addParameter(RegistrationForMembers.PRM_TOURNAMENT_ID, t.getPrimaryKey().toString());
				link.addParameter(RegistrationForMembers.PRM_ACTION, RegistrationForMembers.VAL_ACTION_OPEN);
				link.setText(displayStr);
				link.setPage(_pageToSubmitTo);
				cont.add(link);
			}
			
		 	return cont;
		} catch(Exception e) {
			e.printStackTrace();
			return new Text();
		}
	}
	
	public void setPageToSubmitTo(ICPage page) {
		_pageToSubmitTo = page;
	}
	
	private PresentationObject getTimeIntervalForm(IWContext iwc) {
		try {
			Form form = new Form();
			
			String menuLabel = _iwrb.getLocalizedString(LOCALIZATION_KEY_SEARCH_TEXT, "Find tournaments that start within");
			String menuLabelAfter = _iwrb.getLocalizedString(LOCALIZATION_KEY_TOURNAMENT_SELECTION_TEXT_AFTER, "days");
			DropdownMenu menu = new DropdownMenu(PARAM_NAME_SEARCH_DAYS);
			for(int i=1; i<=MAX_DAYS_FOR_SEARCH; i++) {
				String val = Integer.toString(i);
				menu.addMenuElement(val, val);
			}
			SubmitButton button = new SubmitButton();
			
			form.add(menuLabel);
			form.add(menu);
			form.add(menuLabelAfter);
			form.add(button);
			
			return form;
		} catch(Exception e) {
			e.printStackTrace();
			return new Text();
		}
	}
	
	public TournamentBusiness getTournamentBusiness(IWContext iwc) {
		if(_biz==null)
		try {
			_biz = (TournamentBusiness) IBOLookup.getServiceInstance(iwc, TournamentBusiness.class);
		} catch (IBOLookupException e) {
			e.printStackTrace();
		}
		return _biz;
	}
	
	public String getBundleIdentifier(){
		return IW_BUNDLE_IDENTIFIER;
    }
	
}
