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

	public HandicapCard() {
		this(-1);
	}
	
	public HandicapCard(int memberId) {
		this.memberId = memberId;
	}

	public void main(IWContext iwc) throws Exception {
		iwb = getBundle();
		iwrb = getResourceBundle();
		
		String iMemberID = iwc.getRequest().getParameter("member_id");
		if (iMemberID == null) {
			iMemberID = (String) iwc.getSession().getAttribute("member_id");
		}
		if (iMemberID == null) {
			Member member = (Member) iwc.getSession().getAttribute("member_login");
			if (member != null) {
				iMemberID = String.valueOf(member.getID());
				if (iMemberID == null) {
					iMemberID = "1";
				}
			}
			else {
				iMemberID = "1";
			}
		}

		if (memberId == -1) {
			memberId = Integer.parseInt(iMemberID);
		}
		
		drawCard(iwc);
	}

	private void drawCard(IWContext modinfo) throws SQLException, FinderException {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		if (width != null) {
			table.setWidth(width);
		}
		if (memberId > 0) {
			int row = 1;
			row = nameAndClub(table, row);
			row = lastRounds(table, row);
			row = footer(table, row);
		}
		add(table);
	}

	private int lastRounds(Table table, int row) throws SQLException, FinderException {
		Scorecard[] scoreCards = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where member_id='" + memberId + "' and scorecard_date is not null order by scorecard_date desc", (numberOfDisplayedCards+1)); // +1 to get one previous

		table.add(getSmallHeader(iwrb.getLocalizedString("handicap.date", "Date")), 1, row);
		table.add(getSmallHeader(iwrb.getLocalizedString("handicap.stableford", "Stableford")), 2, row);
		table.add(Text.getBreak(), 2, row);
		table.add(getSmallHeader(iwrb.getLocalizedString("handicap.points_lowercase", "points")), 2, row);
		table.add(getSmallHeader(iwrb.getLocalizedString("handicap.new", "New")), 3, row);
		table.add(Text.getBreak(), 3, row);
		table.add(getSmallHeader(iwrb.getLocalizedString("handicap.handicap_lowercase", "handicap")), 3, row);
		table.setAlignment(2, row, "center");
		table.setAlignment(3, row, "center");
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);
		table.setRowStyleClass(row++, getHeaderRowClass());

		Member member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(memberId);
		IWTimestamp date;
		Scorecard prev = null;
		int subtracter = 1;
		if (scoreCards.length > numberOfDisplayedCards) {
			prev = scoreCards[numberOfDisplayedCards];
			subtracter = 2;
		}
		
		int sRow = 1;
		for (int i = (scoreCards.length - subtracter); i >= 0; i--) {
			date = new IWTimestamp(scoreCards[i].getScorecardDate());
			table.add(getSmallText(getDateString(date)), 1, row);
			table.add(getSmallText(Integer.toString(getRealPoints(scoreCards[i], prev, member))), 2, row);

			if (i == 0) {
				table.add(getHeader(TextSoap.singleDecimalFormat((double) scoreCards[i].getHandicapAfter())), 3, row);
			}
			else {
				table.add(getSmallText(TextSoap.singleDecimalFormat((double) scoreCards[i].getHandicapAfter())), 3, row);
			}

			table.setAlignment(2, row, "center");
			table.setAlignment(3, row, "center");
			prev = scoreCards[i];

			if (sRow % 2 == 0) {
				table.setRowStyleClass(row++, getDarkRowClass());
			}
			else {
				table.setRowStyleClass(row++, getLightRowClass());
			}
			sRow++;
		}

		return ++row;
	}

	private int footer(Table table, int row) {
		table.setHeight(row++, 4);

		table.mergeCells(1, row, 3, row);
		table.setCellpadding(1, row, 4);
		table.add(getText(iwrb.getLocalizedString("handicap.handicap_card_certification", "This handicap card certifies that the holder has a current playing handicap as shown according to the EGA handicap system")), 1, row++);
		table.setHeight(row++, 4);
		
		IWTimestamp date = IWTimestamp.RightNow();
		table.mergeCells(1, row, 3, row);
		table.setCellpadding(1, row, 4);
		table.add(getHeader(iwrb.getLocalizedString("handicap.date_of_issue", "Date of issue") + " : "), 1, row);
		table.add(getHeader(getDateString(date)), 1, row++);
		table.setHeight(row++, 4);
		
		table.mergeCells(1, row, 3, row);
		table.setCellpadding(1, row, 4);
		Table bottomTable = new Table(2, 1);
		bottomTable.setWidth(Table.HUNDRED_PERCENT);
		bottomTable.setCellpadding(0);
		bottomTable.setCellspacing(0);
		bottomTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(bottomTable, 1, row);
		
		bottomTable.add(getText(iwrb.getLocalizedString("handicap.handicap_committee_text", "Handicap Committee")), 1, 1);
		bottomTable.add(Text.getBreak(), 1, 1);
		bottomTable.add(getText(iwrb.getLocalizedString("handicap.golf_union_of_iceland", "Golf Union of Iceland")), 1, 1);
		if (this.addPrintLink) {
			bottomTable.add(getPrintLink(), 2, 1);
		}
		bottomTable.setRowVerticalAlignment(1, Table.VERTICAL_ALIGN_BOTTOM);
		
		return row;
	}

	private String getDateString(IWTimestamp date) {
		return date.getMonth() + "/" + date.getDay() + "/" + String.valueOf(date.getYear());
	}

	private Table getPrintLink() {
		Table table = new Table(2, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setCellpaddingRight(1, 1, 5);
		
		Link print = new Link(iwb.getImage("/shared/print.gif"));
		print.setToolTip(iwrb.getLocalizedString("handicap.printable_version", "Show printable version"));
		print.setWindowToOpen(HandicapCardWindow.class);
		print.addParameter(HandicapCardWindow.PARAMETER_MEMBER_ID, memberId);
		table.add(print, 1, 1);

		Link printText = getSmallLink(iwrb.getLocalizedString("print", "Print"));
		printText.setToolTip(iwrb.getLocalizedString("handicap.printable_version", "Show printable version"));
		printText.setWindowToOpen(HandicapCardWindow.class);
		printText.addParameter(HandicapCardWindow.PARAMETER_MEMBER_ID, memberId);
		table.add(printText, 2, 1);

		return table;
	}

	private int nameAndClub(Table table, int row) throws SQLException, FinderException {
		if (this.addName) {
			Member member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(memberId);
			table.mergeCells(1, row, 3, row);
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);
			table.add(getHeaderText(member.getName()), 1, row);
			if (this.addClub) {
				table.add(getHeaderText(" - " + GolfCacher.getCachedUnion(member.getMainUnionID()).getAbbrevation()), 1, row);
			}
			table.add(getHeaderText(" - " + iwrb.getLocalizedString("handicap.hcp", "Hpc") + ": " + member.getHandicap()), 1, row++);
			table.setHeight(row++, 8);
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
