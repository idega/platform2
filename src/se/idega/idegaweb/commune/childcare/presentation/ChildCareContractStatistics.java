/*
 * Created on 24.6.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.text.DecimalFormat;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.business.ChildCareStatisticsWriter;

import com.idega.core.data.ICFile;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class ChildCareContractStatistics extends ChildCareBlock {

	private static final String PARAMETER_CREATE_REPORT = "create_report";
	
	private ICFile statisticsFolder;
	private boolean createReport = false;
	private boolean reportCreated = false;
	
	/**
	 * @return
	 */
	public ICFile getStatisticsFolder() {
		return statisticsFolder;
	}

	/**
	 * @param statisticsFolder
	 */
	public void setStatisticsFolder(ICFile statisticsFolder) {
		this.statisticsFolder = statisticsFolder;
	}

	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		if (getStatisticsFolder() != null) {
			parseAction(iwc);
			
			Table table = new Table(1, 3);
			table.setCellpadding(0);
			table.setCellspacing(0);
			table.setHeight(2, 12);
			table.setWidth(getWidth());
			
			table.add(getCreateForm(), 1, 1);
			table.add(getFileList(iwc), 1, 3);
			add(table);
		}
		else {
			add(getSmallErrorText("The statistics folder has not been set..."));
		}
	}
	
	private Form getCreateForm() {
		Form form = new Form();
		form.addParameter(PARAMETER_CREATE_REPORT, "true");
		
		SubmitButton button = (SubmitButton) getButton(new SubmitButton(localize("child_care.create_report", "Create report")));
		form.add(button);
		
		if (createReport && !reportCreated) {
			form.add(Text.getNonBrakingSpace());
			form.add(getSmallErrorText(localize("child_care.create_report_failed", "Failed to create report. Possibly no changes found.")));
		}
		
		return form;
	}
	
	private Table getFileList(IWContext iwc) {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(4);
		table.setRowColor(1, getHeaderColor());
		table.setWidth(1, Table.HUNDRED_PERCENT);
		int row = 1;
		int column = 1;
			
		table.add(getLocalizedSmallHeader("child_care.file_name","File name"), column++, row);
		table.setNoWrap(column, row);
		table.add(getLocalizedSmallHeader("child_care.file_size","File size"), column++, row);
		table.setNoWrap(column, row);
		table.add(getLocalizedSmallHeader("child_care.created_date","Created date"), column++, row);
		table.setNoWrap(column, row);
		table.add(getLocalizedSmallHeader("child_care.mimetype","Mimetype"), column++, row++);
		
		Iterator iter = statisticsFolder.getChildren();
		if (iter != null) {
			while (iter.hasNext()) {
				column = 1;
				ICFile file = (ICFile) iter.next();
				
				double size = (double) file.getFileSize().intValue() / (double) 1024;
				DecimalFormat format = new DecimalFormat("0.0 KB");

				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());
	
				Link link = getSmallLink(file.getName());
				link.setFile(file);
				
				table.add(link, column++, row);
				table.setNoWrap(column, row);
				table.add(getSmallText(format.format(size)), column++, row);
				table.setNoWrap(column, row);
				table.add(getSmallText(new IWTimestamp(file.getCreationDate()).getLocaleDateAndTime(iwc.getCurrentLocale(), IWTimestamp.SHORT, IWTimestamp.SHORT)), column++, row);
				table.setNoWrap(column, row);
				table.add(getSmallText(file.getMimeType()), column++, row++);
			}
		}
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		
		return table;
	}

	private void parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_CREATE_REPORT)) {
			createReport = true;
			ChildCareStatisticsWriter statistics = new ChildCareStatisticsWriter();
			reportCreated = statistics.createReport(iwc, statisticsFolder, iwc.getCurrentLocale());
		}
	}
}
