/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound;
import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundHome;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryHome;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.user.presentation.StyledIWAdminWindow;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class AssessmentListWindow extends StyledIWAdminWindow {
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi.block.accounting";
	
//	protected static final String LABEL_CLUB = "isi_acc_alw_club";
//	protected static final String LABEL_DIVISION = "isi_acc_alw_div";
	protected static final String LABEL_GROUP = "isi_acc_alw_group";
	protected static final String LABEL_USER = "isi_acc_alw_user";
	protected static final String LABEL_DATE = "isi_acc_alw_date";
	protected static final String LABEL_AMOUNT = "isi_acc_alw_amount";
//	protected static final String LABEL_STATUS = "isi_acc_alw_status";
	protected static final String LABEL_ROUND_NAME = "isi_acc_alw_round_name";
	protected static final String LABEL_EXECUTED_BY = "isi_acc_alw_executed_by";
	protected static final String LABEL_SUM = "isi_acc_alw_sum";
	
	public AssessmentListWindow() {
		setHeight(600);
		setWidth(400);
		setResizable(true);
		setScrollbar(true);
	}
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setTitle("Assessmentlist window");
		addTitle(iwrb.getLocalizedString("isi_acc_ass_list_window", "Assessment list window"), IWConstants.BUILDER_FONT_STYLE_TITLE);
		
		String assID = iwc.getParameter(AutomaticAssessment.ASSESSMENT_ID);
		if (assID != null && !"".equals(assID)) {
			showList(assID, iwc);
		}
	}
	
	private void showList(String id, IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		Table heading = new Table();
		Table t = new Table();
		heading .setCellpadding(5);
		t.setCellpadding(5);
		
		int row = 1;
		Text labelGroup = new Text(iwrb.getLocalizedString(LABEL_GROUP, "Group"));
		labelGroup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelUser = new Text(iwrb.getLocalizedString(LABEL_USER, "User"));
		labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelDate = new Text(iwrb.getLocalizedString(LABEL_DATE, "Date"));
		labelDate.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelAmount = new Text(iwrb.getLocalizedString(LABEL_AMOUNT, "Amount"));
		labelAmount.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelExecutedBy = new Text(iwrb.getLocalizedString(LABEL_EXECUTED_BY, "Executed by"));
		labelExecutedBy.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelRoundName = new Text(iwrb.getLocalizedString(LABEL_ROUND_NAME, "Round name"));
		labelRoundName.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelSum = new Text(iwrb.getLocalizedString(LABEL_SUM, "Sum"));
		labelSum.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		
//		Text labelStatus = new Text(iwrb.getLocalizedString(LABEL_STATUS, "Status"));
//		labelStatus.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		
		t.add(labelGroup, 1, row);
		t.add(labelUser, 2, row);
//		t.add(labelDate, 3, row);
		t.add(labelAmount, 3, row);
//		t.add(labelStatus, 5, row++);
		row++;
		
		try {
			AssessmentRound round = getAssessmentRoundHome().findByPrimaryKey(new Integer(id));
			Collection col = getFinanceEntryHome().findAllByAssessmentRound(round);
			
			int headingRow = 1;
			heading.add(labelRoundName, 1, headingRow);
			heading.add(labelExecutedBy, 2, headingRow);
			heading.add(labelDate, 3, headingRow++);
			heading.add(round.getName(), 1, headingRow);
			heading.add(round.getExecutedBy().getName(), 2, headingRow);
			IWTimestamp ex = new IWTimestamp(round.getStartTime());
			heading.add(ex.getDateString("dd-MMM-yyyy"), 3, headingRow);
			
			if (col != null && !col.isEmpty()) {
				Iterator it = col.iterator();
				double sum = 0;
				while (it.hasNext()) {
					FinanceEntry entry = (FinanceEntry) it.next();
					t.add(entry.getGroup().getName(), 1, row);
					t.add(entry.getUser().getName(), 2, row);
//					t.add(entry.getDateOfEntry().toString(), 3, row);
					t.add(Double.toString(entry.getAmount()), 3, row);
//					t.add(entry.get)
					row++;
				}
				t.add(labelSum, 2, row);
				t.add(Double.toString(sum), 3, row);
			}
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		add(heading);
		add(t);
	}
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
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
	
	private AssessmentRoundHome getAssessmentRoundHome() {
		try {
			return (AssessmentRoundHome) IDOLookup.getHome(AssessmentRound.class);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
	}
}