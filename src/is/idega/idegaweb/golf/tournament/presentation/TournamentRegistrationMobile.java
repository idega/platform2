/*
 * Created on Jul 7, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentBusiness;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Strong;
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
public class TournamentRegistrationMobile extends GolfBlock {
	
	public static final String PARAM_NAME_SEARCH_DAYS = "s_d";
	public static final String PARAM_NAME_CHUNK = "s_c";
	
	public static final int MAX_LINKS_IN_CHUNK = 10;
	
	public static final String LOCALIZATION_KEY_SEARCH_TEXT = "mobile_search_text";
	public static final String LOCALIZATION_KEY_TOURNAMENT_SELECTION_TEXT = "mobile_selection_text";
	public static final String LOCALIZATION_KEY_TOURNAMENT_SELECTION_TEXT_AFTER = "mobile_selection_text_after";
	public static final String LOCALIZATION_KEY_PREV_LINK_TEXT = "mobile.prev";
	public static final String LOCALIZATION_KEY_NEXT_LINK_TEXT = "mobile.next";
	
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
			cont.addBreak();
			
			int days = Integer.parseInt(iwc.getParameter(PARAM_NAME_SEARCH_DAYS));
			IWTimestamp begin = new IWTimestamp();
			begin.setHour(6);
			begin.setMinute(0);
			Tournament[] allTournaments = getTournamentBusiness(iwc).getTournamentsWithRegistration(begin, days);
			int tournamentCount = allTournaments.length;
			Tournament[] tournaments;
			int currentChunk = -1; // current chunk, -1 means content not broken into chunks (0 is first chunk)
			boolean showNext = false;
			boolean showPrev = false;
			if(tournamentCount>MAX_LINKS_IN_CHUNK) {
				String strChunk = iwc.getParameter(PARAM_NAME_CHUNK);
				currentChunk = 0;
				if(strChunk!=null && strChunk.length()>0) {
					try {
						currentChunk = Integer.parseInt(strChunk);
					} catch(Exception e) {
						System.out.println("Could not parse \"" + strChunk + "\" to integer");
						// ok, just show first chunk
					}
				}
				showPrev = currentChunk>0;
				int firstIndexInChunk = currentChunk*MAX_LINKS_IN_CHUNK;
				int lastIndexInChunk = Math.min((currentChunk+1)*MAX_LINKS_IN_CHUNK, tournamentCount); // actually the index after the last index in chunk
				showNext = lastIndexInChunk<tournamentCount;
				tournaments = new Tournament[lastIndexInChunk-firstIndexInChunk];
				for(int i = firstIndexInChunk; i<lastIndexInChunk; i++) {
					tournaments[i-firstIndexInChunk] = allTournaments[i];
				}
			} else {
				tournaments = allTournaments;
			}
			
			String lastDay = null;
			for(int i=0; i<tournaments.length; i++) {
				Tournament t = tournaments[i];
				String day = (new IWTimestamp(t.getStartTime())).getDateString("EEE d MMM", iwc.getCurrentLocale());
				if(!day.equals(lastDay)) {
					lastDay = day;
					Text dayLabel = new Text();
					dayLabel.setText(day);
					Strong strong = new Strong();
					strong.add(dayLabel);
					cont.add(strong);
					cont.addBreak();
				}
				String name = t.getName();
				String place = t.getField().getName();
				String displayStr = name + " - " + place;
				cont.addBreak();
				Link link = new Link();
				link.addParameter(RegistrationForMembers.PRM_TOURNAMENT_ID, t.getPrimaryKey().toString());
				link.addParameter(RegistrationForMembers.PRM_ACTION, RegistrationForMembers.VAL_ACTION_OPEN);
				link.setText(displayStr);
				link.setPage(_pageToSubmitTo);
				cont.add(link);
				cont.addBreak();
			}
			
			if(showPrev || showNext) {
				cont.addBreak();
				if(showPrev) {
					Link link = new Link();
					link.addParameter(PARAM_NAME_SEARCH_DAYS, iwc.getParameter(PARAM_NAME_SEARCH_DAYS));
					link.addParameter(PARAM_NAME_CHUNK, Integer.toString(currentChunk-1));
					link.setText(_iwrb.getLocalizedString(LOCALIZATION_KEY_PREV_LINK_TEXT, "Prev"));
					cont.add(link);
				}
				if(showNext) {
					Link link = new Link();
					link.addParameter(PARAM_NAME_SEARCH_DAYS, iwc.getParameter(PARAM_NAME_SEARCH_DAYS));
					link.addParameter(PARAM_NAME_CHUNK, Integer.toString(currentChunk+1));
					link.setText(_iwrb.getLocalizedString(LOCALIZATION_KEY_NEXT_LINK_TEXT, "Next"));
					cont.add(link);
				}
				cont.addBreak();
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
			SubmitButton button = new SubmitButton(localize("tournament.search","Search"));
			
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
