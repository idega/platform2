package is.idega.idegaweb.golf.handicap.presentation;

import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.business.GolfCacher;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.handicap.business.Handicap;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * @author gimmi
 */
public class HandicapCard extends GolfBlock {

	private int memberId = -1;
	private IWResourceBundle iwrb;
	private IWBundle iwb;

	private String headerTextColor = "#000000";
	private String textColor = "#000000";
	private int numberOfDisplayedCards = 5;
	private String width;
	private boolean addPrintLink = false;
	private boolean addName = false;
	private boolean addClub = false;

	public HandicapCard(int memberId) {
		this.memberId = memberId;
	}

	public void main(IWContext modinfo) throws Exception {
		iwb = getBundle();
		iwrb = getResourceBundle();
		drawCard(modinfo);
	}

	private void drawCard(IWContext modinfo) throws SQLException, FinderException {
		Table table = new Table();
		if (width != null) {
			table.setWidth(width);
		}
		if (memberId > 0) {
			int row = 1;
			row = nameAndClub(table, row);
			row = lastRounds(table, row);
			row = footer(table, row);
			row = printLink(table, row);
		}
		add(table);
	}

	private int lastRounds(Table table, int row) throws SQLException, FinderException {
		Scorecard[] scoreCards = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where member_id='" + memberId + "' and scorecard_date is not null order by scorecard_date desc", (numberOfDisplayedCards+1)); // +1 to get one previous

		table.add(getHeaderText(iwrb.getLocalizedString("handicap.date", "Date")), 1, row);
		table.add(getHeaderText(iwrb.getLocalizedString("handicap.stableford_points", "Stableford points")), 2, row);
		table.add(getHeaderText(iwrb.getLocalizedString("handicap.new_handicap", "New handicap")), 3, row);
		table.setAlignment(1, row, "right");
		table.setAlignment(2, row, "center");
		table.setAlignment(3, row, "center");

		Member member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(memberId);
		IWTimestamp date;
		Scorecard prev = null;
		int subtracter = 1;
		if (scoreCards.length > numberOfDisplayedCards) {
			prev = scoreCards[numberOfDisplayedCards];
			subtracter = 2;
		}
		
		for (int i = (scoreCards.length - subtracter); i >= 0; i--) {
			++row;
			date = new IWTimestamp(scoreCards[i].getScorecardDate());
			table.add(getText(getDateString(date)), 1, row);
			table.add(getText(Integer.toString(getRealPoints(scoreCards[i], prev, member))), 2, row);
			//table.add(getText(Integer.toString(scoreCards[i].getTotalPoints())), 2, row);
			if (i == 0) {
				table.add(getHeaderText(TextSoap.singleDecimalFormat((double) scoreCards[i].getHandicapAfter())), 3, row);
			}
			else {
				table.add(getText(TextSoap.singleDecimalFormat((double) scoreCards[i].getHandicapAfter())), 3, row);
			}
			table.setAlignment(1, row, "right");
			table.setAlignment(2, row, "center");
			table.setAlignment(3, row, "center");
			prev = scoreCards[i];
		}

		return ++row;
	}

	private int footer(Table table, int row) {
		++row;
		table.mergeCells(1, row, 3, row);
		table.setAlignment(1, row, "center");
		table.add(getText(iwrb.getLocalizedString("handicap.handicap_card_certification", "This handicap card certifies that the holder has a current playing handicap as shown according to the EGA handicap system")), 1, row);
		++row;
		++row;
		IWTimestamp date = IWTimestamp.RightNow();
		table.mergeCells(1, row, 3, row);
		table.setAlignment(1, row, "center");
		table.add(getText(iwrb.getLocalizedString("handicap.date_of_issue", "Date of issue") + " : "), 1, row);
		table.add(getText(getDateString(date)), 1, row);
		++row;
		++row;
		table.mergeCells(1, row, 3, row);
		table.setAlignment(1, row, "center");
		table.add(getText(iwrb.getLocalizedString("handicap.handicap_committee", "Handicap Committee Golf Union of Iceland")), 1, row);
		return row;
	}

	private String getDateString(IWTimestamp date) {
		return date.getMonth() + "/" + date.getDay() + "/" + String.valueOf(date.getYear());
	}

	private int printLink(Table table, int row) {
		if (this.addPrintLink) {
			++row;
			++row;
			table.mergeCells(1, row, 3, row);
			table.setAlignment(1, row, "right");

			Link print = new Link(iwb.getImage("/shared/print.gif"));
			print.setToolTip(iwrb.getLocalizedString("handicap.printable_version", "Show printable version"));
			print.setWindowToOpen(HandicapCardWindow.class);
			print.addParameter(HandicapCardWindow.PARAMETER_MEMBER_ID, memberId);
			table.add(print, 1, row);
		}

		return row;
	}

	private int nameAndClub(Table table, int row) throws SQLException, FinderException {
		if (this.addName) {
			Member member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(memberId);
			table.mergeCells(1, row, 3, row);
			table.setAlignment(1, row, "center");
			table.add(getHeaderText(member.getName()), 1, row);
			if (this.addClub) {
				table.add(getHeaderText(" - " + GolfCacher.getCachedUnion(member.getMainUnionID()).getAbbrevation()), 1, row);
			}
			table.add(getHeaderText(" - " + iwrb.getLocalizedString("handicap.hcp", "Hpc") + ": " + member.getHandicap()), 1, row);
			++row;
			++row;
		}

		return row;
	}

	private Text getHeaderText(String content) {
		return getHeader(content);
	}

//	private Text getText(String content) {
//		Text text = new Text(content);
//		text.setFontSize(1);
//		text.setFontColor(textColor);
//		return text;
//	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setNumberOfDisplayedCards(int no) {
		this.numberOfDisplayedCards = no;
	}

	public void addPrintLink(boolean addPrintLink) {
		this.addPrintLink = addPrintLink;
	}

	public void addName(boolean addName) {
		this.addName = addName;
	}

	public void addClub(boolean addClub) {
		this.addClub = addClub;
	}
	
	private int getRealPoints(Scorecard scoreCard, Scorecard previousScorecard, Member member) throws SQLException, FinderException {
		float realHandicap = 0;

		double grunn = (double) scoreCard.getHandicapBefore();
		int heildarpunktar = scoreCard.getTotalPoints();
		float ny_grunn = scoreCard.getHandicapAfter();

		int leik_forgjof = 0;
		Field field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(scoreCard.getFieldID());
		double slope = (double) scoreCard.getSlope();
		double course_rating = (double) scoreCard.getCourseRating();
//		String cr = TextSoap.singleDecimalFormat(String.valueOf(course_rating));
		double field_par = (double) field.getFieldPar();

		Handicap leik = new Handicap(grunn);
		leik_forgjof = leik.getLeikHandicap(slope, course_rating, field_par);

		if (previousScorecard != null) {
			realHandicap = previousScorecard.getHandicapAfter();
		}
		else {
			MemberInfo memberInfo = member.getMemberInfo();
			realHandicap = memberInfo.getFirstHandicap();
		}

		Handicap realLeik = new Handicap((double) realHandicap);
		int realPlayHandicap = realLeik.getLeikHandicap(slope, course_rating, field_par);
		int realPoints = Handicap.getTotalPoints(scoreCard.getID(), realPlayHandicap);
		return realPoints;
	}
}
