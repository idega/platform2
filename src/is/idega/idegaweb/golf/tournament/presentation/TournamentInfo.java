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
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
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
		boolean ongoing = tournament.isTournamentOngoing();
		boolean finished = tournament.isTournamentFinished();

		Union union = tournament.getUnion();
		Field field = tournament.getField();

		Table table = new Table(2, 10);
		table.setColor("#CEDFD0");
		table.setCellpadding(5);
		table.setCellspacing(1);
		table.mergeCells(1, 1, 2, 1);
		table.mergeCells(1, 8, 2, 8);
		table.mergeCells(1, 9, 2, 9);
		table.setWidth("690");
		table.setAlignment("center");
		table.setAlignment(2, 10, "right");
		table.setHorizontalZebraColored("#DCEFDE", "#EAFAEC");
		table.setRowColor(1, "#2C4E3B");
		table.setRowColor(8, "#2C4E3B");
		table.setRowColor(7, "#CEDFD0");
		table.setRowColor(10, "#CEDFD0");
		table.setWidth(2, "90%");

		Text name = new Text(tournament.getName());
		name.setFontFace(Text.FONT_FACE_VERDANA);
		name.setFontSize(Text.FONT_SIZE_10_HTML_2);
		name.setFontColor("#FFFFFF");
		name.setBold();
		table.add(name, 1, 1);

		int row = 1;

		Text extraInfoText = new Text(iwrb.getLocalizedString("tournament.information", "Information"), true, false, false);
		extraInfoText.setFontFace(Text.FONT_FACE_VERDANA);
		extraInfoText.setFontSize(Text.FONT_SIZE_10_HTML_2);
		extraInfoText.setFontColor("#FFFFFF");
		extraInfoText.setBold();
		table.add(extraInfoText, 1, 8);

		Text startDateText = new Text(iwrb.getLocalizedString("tournament.date", "Date"), true, false, false);
		startDateText.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(startDateText, 1, 3);

		Text RegDateText = new Text(iwrb.getLocalizedString("tournament.registration", "Registration"), true, false, false);
		RegDateText.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(RegDateText, 1, 6);

		Text tournamentTypeText = new Text(iwrb.getLocalizedString("tournament.arrangement", "Arrangement"), true, false, false);
		tournamentTypeText.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(tournamentTypeText, 1, 4);

		Text unionNameText = new Text(iwrb.getLocalizedString("tournament.club", "Club"), true, false, false);
		unionNameText.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(unionNameText, 1, 2);

		Text fieldNameText = new Text(iwrb.getLocalizedString("tournament.field", "Field"), true, false, false);
		fieldNameText.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(fieldNameText, 1, 5);

		IWTimestamp startStamp = new IWTimestamp(tournament.getStartTime());
		Text startDate = new Text(startStamp.getLocaleDate(modinfo.getCurrentLocale()));
		startDate.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(startDate, 2, 3);

		IWTimestamp firstRegStamp = new IWTimestamp(tournament.getFirstRegistrationDate());
		IWTimestamp lastRegStamp = new IWTimestamp(tournament.getLastRegistrationDate());
		Text RegDate = new Text(firstRegStamp.getDate() + "/" + firstRegStamp.getMonth() + "/" + firstRegStamp.getYear() + " - " + lastRegStamp.getDate() + "/" + lastRegStamp.getMonth() + "/" + lastRegStamp.getYear());
		RegDate.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(RegDate, 2, 6);

		Text tournamentType = new Text(tournament.getTournamentType().getName());
		tournamentType.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(tournamentType, 2, 4);

		Text unionName = new Text(union.getName() + " (" + union.getAbbrevation() + ")");
		unionName.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(unionName, 2, 2);

		Text fieldName = new Text(field.getName());
		fieldName.setFontSize(Text.FONT_SIZE_10_HTML_2);
		table.add(fieldName, 2, 5);

		Text extraInfo = new Text("");
		if (tournament.getExtraText() != null) {
			String theExtraInfo = TextSoap.formatText(tournament.getExtraText());
			extraInfo.setText(theExtraInfo);
		}
		extraInfo.setFontSize(Text.FONT_SIZE_10_HTML_2);

		table.add(extraInfo, 1, 9);

		if (AccessControl.isAdmin(modinfo) || AccessControl.isClubAdmin(modinfo)) {
			if ((!ongoing) && (!finished)) {
				Form form4 = new Form();
				form4.setWindowToOpen(TournamentDeleteWindow.class);
				form4.add(new Parameter("tournament_id", tournament.getID() + ""));
				form4.add(new SubmitButton(getResourceBundle().getImage("buttons/delete_tournament.gif")));
				table.add(form4, 1, row);
			}
		}
		table.setAlignment(1, row, "left");

		if (TournamentController.isOnlineRegistration(tournament)) {
			Image registerImage = getResourceBundle().getImage("shared/tournament/register.gif");
			registerImage.setName(iwrb.getLocalizedString("tournament.register_me", "Register me"));
			Link register = getLocalizedLink("tournament_register","Register");
			register.setWindowToOpen(RegistrationForMembersWindow.class);
			register.addParameter("action", "open");
			register.addParameter("tournament_id", Integer.toString(tournament.getID()));
			register.setFontSize(1);
			table.add(register, 2, 3);
		}

		add(table);
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