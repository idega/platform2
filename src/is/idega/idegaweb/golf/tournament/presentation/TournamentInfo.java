/*
 * Created on 28.4.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.block.login.business.AccessControl;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentController;
import is.idega.idegaweb.golf.tournament.business.TournamentSession;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.GenericButton;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;


/**
 * @author laddi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TournamentInfo extends GolfBlock {

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext modinfo) throws Exception {
		IWResourceBundle iwrb = getResourceBundle();
		
		Tournament tournament = getTournamentSession(modinfo).getTournament();
		if (tournament != null) {
			boolean ongoing = tournament.isTournamentOngoing();
			boolean finished = tournament.isTournamentFinished();
	
			Union union = tournament.getUnion();
			Field field = tournament.getField();
	
			
			Table table = new Table(2, 10);
			table.setWidth(getWidth());
			table.setCellpadding(0);
			table.setCellspacing(0);
			table.mergeCells(1, 1, 2, 1);
			table.mergeCells(1, 8, 2, 8);
			table.mergeCells(1, 9, 2, 9);
			table.setAlignment("center");
			table.setAlignment(2, 10, "right");
			table.setWidth(2, "90%");
			table.setRowStyleClass(1,getHeaderRowClass());
			table.setRowStyleClass(2,getLightRowClass());
			table.setRowStyleClass(3,getDarkRowClass());
			table.setRowStyleClass(4,getLightRowClass());
			table.setRowStyleClass(5,getDarkRowClass());
			table.setRowStyleClass(6,getLightRowClass());
			table.setRowStyleClass(7,getDarkRowClass());
			table.setRowStyleClass(8,getHeaderRowClass());
			table.setRowStyleClass(9,getLightRowClass());
			table.setRowStyleClass(10,getDarkRowClass());
	
	
			Text name = getSmallHeader(tournament.getName());
			table.add(name, 1, 1);
	
			int row = 1;
	
			Text extraInfoText = getSmallHeader(iwrb.getLocalizedString("tournament.information", "Information"));
			table.add(extraInfoText, 1, 8);
	
			Text startDateText = getHeader(iwrb.getLocalizedString("tournament.date", "Date"));
			table.add(startDateText, 1, 3);
	
			Text RegDateText = getHeader(iwrb.getLocalizedString("tournament.registration", "Registration"));
			table.add(RegDateText, 1, 6);
	
			Text tournamentTypeText = getHeader(iwrb.getLocalizedString("tournament.arrangement", "Arrangement"));
			table.add(tournamentTypeText, 1, 4);
	
			Text unionNameText = getHeader(iwrb.getLocalizedString("tournament.club", "Club"));
			table.add(unionNameText, 1, 2);
	
			Text fieldNameText = getHeader(iwrb.getLocalizedString("tournament.field", "Field"));
			table.add(fieldNameText, 1, 5);
	
			IWTimestamp startStamp = new IWTimestamp(tournament.getStartTime());
			Text startDate = getText(startStamp.getLocaleDate(modinfo.getCurrentLocale()));
			table.add(startDate, 2, 3);
	
			IWTimestamp firstRegStamp = new IWTimestamp(tournament.getFirstRegistrationDate());
			IWTimestamp lastRegStamp = new IWTimestamp(tournament.getLastRegistrationDate());
			Text RegDate = getText(firstRegStamp.getDay() + "/" + firstRegStamp.getMonth() + "/" + firstRegStamp.getYear() + " - " + lastRegStamp.getDay() + "/" + lastRegStamp.getMonth() + "/" + lastRegStamp.getYear());
			table.add(RegDate, 2, 6);
	
			Text tournamentType = getText(tournament.getTournamentType().getName());
			table.add(tournamentType, 2, 4);
	
			Text unionName = getText(union.getName() + " (" + union.getAbbrevation() + ")");
			table.add(unionName, 2, 2);
	
			Text fieldName = getText(field.getName());
			table.add(fieldName, 2, 5);
	
			Text extraInfo = getText("");
			if (tournament.getExtraText() != null) {
				String theExtraInfo = TextSoap.formatText(tournament.getExtraText());
				extraInfo.setText(theExtraInfo);
			}
	
			table.add(extraInfo, 1, 9);
	
			if (AccessControl.isAdmin(modinfo) || AccessControl.isClubAdmin(modinfo)) {
				if ((!ongoing) && (!finished)) {
					GenericButton deleteButton = getButton(new GenericButton(localize("tournament.delete_tournament","Delete Tournament")));
					deleteButton.setWindowToOpen(TournamentDeleteWindow.class);
					deleteButton.addParameter("tournament_id", tournament.getID());
					table.add(deleteButton, 2, 7);
				}
			}
	
			if (TournamentController.isOnlineRegistration(tournament)) {
				GenericButton register = getButton(new GenericButton(localize("tournament_register","Register")));
				register.setWindowToOpen(RegistrationForMembersWindow.class);
				register.addParameter("action", "open");
				register.addParameter("tournament_id", Integer.toString(tournament.getID()));
				table.add(register, 2, 7);
			}
			add(table);

		} else {
			Text noTournament = getSmallHeader(iwrb.getLocalizedString("tournament.no_tournament_selected", "No tournament is selected."));
			add(noTournament);

		}

	}

	private TournamentSession getTournamentSession(IWContext iwc) {
		try {
			return (TournamentSession) IBOLookup.getSessionInstance(iwc, TournamentSession.class);	
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}