/*
 * Created on Jun 3, 2004
 */
package is.idega.idegaweb.member.isi.block.members.presentation;


import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound;
import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundHome;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryHome;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.PrintButton;
import com.idega.util.IWTimestamp;

/**
 * @author Sigtryggur
 */

public class MemberFinanceEntryDetailWindow extends StyledIWAdminWindow {

	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	private String backTableStyle ="back";
	private String borderTableStyle ="borderAll";
	
	protected static final String LABEL_NAME = "member_fin_entry_detail_name";
	protected static final String LABEL_PERSONAL_ID = "member_fin_entry_detail_personal_id";
	protected static final String LABEL_CLUB = "member_fin_entry_detail_club";
	protected static final String LABEL_DIVISION = "member_fin_entry_detail_division";
	protected static final String LABEL_GROUP = "member_fin_entry_detail_group";
	protected static final String LABEL_DATE = "member_fin_entry_detail_entry_date";
	protected static final String LABEL_AMOUNT = "member_fin_entry_detail_amount";
	protected static final String LABEL_TARIFF_TYPE = "member_fin_entry_detail_tariff_type";
	protected static final String LABEL_INFO = "member_fin_entry_detail_info";
	
	

	public MemberFinanceEntryDetailWindow() {
		setHeight(200);
		setWidth(700);
		setResizable(true);
		setScrollbar(true);
	}
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		IWResourceBundle iwrb = getResourceBundle(iwc);
		setTitle("Finance entry detail window");
		addTitle(iwrb.getLocalizedString("member_fin_entry_detail_window", "Finance entry detail window"), IWConstants.BUILDER_FONT_STYLE_TITLE);
		
		String finEntryID = iwc.getParameter(MemberOverview.PARAM_NAME_FINANCE_ENTRY_ID);
		if (finEntryID != null && !"".equals(finEntryID)) {
			showList(finEntryID, iwc);
		}
	}
	
	private void showList(String id, IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		Table backTable = new Table(3,3);
		backTable.setStyleClass(backTableStyle);
		backTable.setWidth(Table.HUNDRED_PERCENT);
		backTable.setHeight(1,1,"6");
		backTable.setWidth(1,2,"6");
		backTable.setWidth(3,2,"6");
		Table heading = new Table();
		heading.setColor("#ffffff");
		heading.setStyleClass(borderTableStyle);
		heading.setWidth(Table.HUNDRED_PERCENT);
		Table detail = new Table();
		heading .setCellpadding(5);
		detail.setWidth(Table.HUNDRED_PERCENT);
		detail.setCellpadding(5);
		detail.setColor("#ffffff");
		detail.setStyleClass(borderTableStyle);
		
		int row = 1;
		Text labelName = new Text(iwrb.getLocalizedString(LABEL_NAME, "Name"));
		labelName.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelPersonalID = new Text(iwrb.getLocalizedString(LABEL_PERSONAL_ID, "Personal ID"));
		labelPersonalID.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelClub = new Text(iwrb.getLocalizedString(LABEL_CLUB, "Club"));
		labelClub.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelDiv = new Text(iwrb.getLocalizedString(LABEL_DIVISION, "Division"));
		labelDiv.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelGroup = new Text(iwrb.getLocalizedString(LABEL_GROUP, "Group"));
		labelGroup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelDate = new Text(iwrb.getLocalizedString(LABEL_DATE, "Date"));
		labelDate.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelAmount = new Text(iwrb.getLocalizedString(LABEL_AMOUNT, "Amount"));
		labelAmount.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelTariffType = new Text(iwrb.getLocalizedString(LABEL_TARIFF_TYPE, "Tariff type"));
		labelTariffType.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelInfo = new Text(iwrb.getLocalizedString(LABEL_INFO, "Info"));
		labelInfo.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		
		detail.add(labelClub, 1, row);		
		detail.add(labelDiv, 2, row);
		detail.add(labelGroup, 3, row);
		detail.add(labelAmount, 4, row);
		detail.add(labelDate, 5, row);
		detail.add(labelTariffType, 6, row);
		detail.add(labelInfo, 7, row);
		detail.setAlignment(4, row, "RIGHT");
		row++;
		
		NumberFormat nf = NumberFormat.getInstance(iwc.getCurrentLocale());
		nf.setMaximumFractionDigits(0);
		
		try {
			FinanceEntry entry = getFinanceEntryHome().findByPrimaryKey(id);
			
			int headingRow = 1;
			heading.add(labelName, 1, headingRow);
			heading.add(labelPersonalID, 2, headingRow++);
			heading.add(entry.getUser().getName(), 1, headingRow);
			heading.add(entry.getUser().getPersonalID(), 2, headingRow);
			if (entry.getClub() != null)
				detail.add(entry.getClub().getName(), 1, row);
			if (entry.getDivision() != null)
				detail.add(entry.getDivision().getName(), 2, row);
			if (entry.getGroup() != null)
				detail.add(entry.getGroup().getName(), 3, row);
			detail.add(nf.format(entry.getAmount()), 4, row);				
			if (entry.getDateOfEntry() != null)
				detail.add(new IWTimestamp(entry.getDateOfEntry()).getDateString("dd.MM.yyyy"), 5, row);
			if (entry.getTariffType() != null)
				detail.add(entry.getTariffType().getName(), 6, row);
			if (entry.getInfo() != null)
				detail.add(entry.getInfo(), 7, row);
			detail.setAlignment(4, row, "RIGHT");
			row++;		
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		backTable.add(heading,2,2);
		backTable.add(detail,2,3);
		add(backTable);
		add(new PrintButton("Prenta"));
	}
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	private FinanceEntryHome getFinanceEntryHome() {
		try {
			return (FinanceEntryHome) IDOLookup.getHome(FinanceEntry.class);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		return null;
	}
}