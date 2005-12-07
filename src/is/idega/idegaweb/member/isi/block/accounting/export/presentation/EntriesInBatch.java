package is.idega.idegaweb.member.isi.block.accounting.export.presentation;

import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.export.business.ExportBusiness;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;

public class EntriesInBatch extends StyledIWAdminWindow {
	private String backTableStyle = "back";

	private String borderTableStyle = "borderAll";

	protected final static String LABEL_USER = "eib_user";

	protected final static String LABEL_CLUB = "eib_club";

	protected final static String LABEL_DIVISION = "eib_division";

	protected final static String LABEL_GROUP = "eib_group";

	protected final static String LABEL_AMOUNT = "eib_amount";

	protected final static String LABEL_DATE = "eib_date";

	public EntriesInBatch() {
		setHeight(600);
		setWidth(400);
		setResizable(true);
		setScrollbar(true);
	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setTitle(iwrb.getLocalizedString("isi_acc_ass_list_window",
				"Assessment list window"));
		addTitle(iwrb.getLocalizedString("isi_acc_ass_list_window",
				"Assessment list window"), TITLE_STYLECLASS);

		String batchID = iwc.getParameter(SendFiles.BATCH_ID);
		if (batchID != null && !"".equals(batchID)) {
			showList(batchID, iwc);
		}
	}

	private void showList(String id, IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);

		Table t = new Table();
		t.setCellpadding(5);

		int row = 1;
		Text labelUser = new Text(iwrb.getLocalizedString(LABEL_USER, "User"));
		labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelClub = new Text(iwrb.getLocalizedString(LABEL_CLUB, "Club"));
		labelClub.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelDivision = new Text(iwrb.getLocalizedString(LABEL_DIVISION,
				"Division"));
		labelDivision.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelGroup = new Text(iwrb
				.getLocalizedString(LABEL_GROUP, "Group"));
		labelGroup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelAmount = new Text(iwrb.getLocalizedString(LABEL_AMOUNT,
				"Amount"));
		labelAmount.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelDate = new Text(iwrb.getLocalizedString(LABEL_DATE, "Date"));
		labelDate.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

		Collection entries = null;
		try {
			entries = getExportBusiness(iwc).findAllEntriesInBatch(Integer.parseInt(id));
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		row = 1;
		t.add(labelUser, 1, row);
		t.add(labelClub, 2, row);
		t.add(labelDivision, 3, row);
		t.add(labelGroup, 4, row);
		t.add(labelAmount, 5, row);
		t.add(labelDate, 6, row++);

		NumberFormat nf = NumberFormat.getInstance(iwc.getCurrentLocale());
		nf.setMaximumFractionDigits(0);

		if (entries != null) {
			Iterator it = entries.iterator();
			while (it.hasNext()) {
				FinanceEntry entry = (FinanceEntry) it.next();
				if (entry.getUser() != null) {
					t.add(entry.getUser().getName(), 1, row);
				}
				if (entry.getClub() != null) {
					t.add(entry.getClub().getName(), 2, row);
				}

				if (entry.getDivision() != null) {
					t.add(entry.getDivision().getName(), 3, row);
				}

				if (entry.getGroup() != null) {
					t.add(entry.getGroup().getName(), 4, row);
				}

				t.add(nf.format(entry.getAmount()), 5, row);

				if (entry.getDateOfEntry() != null) {
					IWTimestamp date = new IWTimestamp(entry.getDateOfEntry());
					t.add(date.getDateString("dd.MM.yyyy HH:mm:ss"), 6, row);
				}

				row++;
			}
		}

		add(t);
	}

	protected ExportBusiness getExportBusiness(IWApplicationContext iwc) {
		try {
			return (ExportBusiness) IBOLookup.getServiceInstance(iwc,
					ExportBusiness.class);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return null;
	}
}
