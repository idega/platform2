/*
 * Created on 24.6.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.text.DecimalFormat;
import java.util.Iterator;
import se.idega.idegaweb.commune.childcare.business.ChildCareStatisticsWriter;
import com.idega.core.file.data.ICFile;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
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
	
	private boolean useAsContractAndPlacementChangesExport = false;
	
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
		if (!isUseAsContractAndPlacementChangesExport()) {
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
		} else {
			setIwc(iwc); 
			
			parseAction();			
			createGui();
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
		
		Iterator iter = statisticsFolder.getChildrenIterator();
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

	
	public boolean isUseAsContractAndPlacementChangesExport() {
		return useAsContractAndPlacementChangesExport;
	}

	
	public void setUseAsContractAndPlacementChangesExport(boolean useAsContractAndPlacementChangesExport) {
		this.useAsContractAndPlacementChangesExport = useAsContractAndPlacementChangesExport;
	}
	
	/*
	 * Contract and placement changes
	 * This is copy from ContractAndPlacementChangesExport	  	 
	 */
	
	//private static final String PARAMETER_CREATE_REPORT = "create_report";	

	private ICFile exportFolder;
	IWContext iwc = null;
	
	private boolean createExport = false;
	private boolean exportCreated = false;		
	
	private void parseAction() {		
		if (iwc.isParameterSet(PARAMETER_CREATE_REPORT)) {
			this.createExport = true;
			
//			ContractAndPlacementChangesExportWriter writer = 
//					new ContractAndPlacementChangesExportWriter();

			ChildCareStatisticsWriter writer = 
				new ChildCareStatisticsWriter();
			
			this.exportCreated = writer.createExportFile(this.getIwc(), this.getExportFolder());			
		}		
	}
	
	private void createGui() {
		PresentationObjectContainer poc = new PresentationObjectContainer();
		
		if (exportFolder != null ) {
			Table table = new Table(1, 3);
			table.setCellpadding(0);
			table.setCellspacing(0);
			table.setHeight(2, 12);
			table.setWidth(getWidth());
			
			table.add(getCreateContractAndPlacementChangesForm(), 1, 1);
			table.add(getFileList(), 1, 3);		
			
			poc.add(table);
		} 
		else {
			poc.add(getSmallErrorText("The statistics folder has not been set..."));
		}
		
		add(poc);
		
	}
	
	private Table getFileList() {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(3);
		table.setRowColor(1, getHeaderColor());
		table.setWidth(1, Table.HUNDRED_PERCENT);
		int row = 1;
		int column = 1;
			
		table.add(getLocalizedSmallHeader("child_care.file_name", "File name"), column++, row);
		table.setNoWrap(column, row);
		table.add(getLocalizedSmallHeader("child_care.file_size", "File size"), column++, row);
		table.setNoWrap(column, row);
		table.add(getLocalizedSmallHeader("child_care.created_date", "Created date"), column++, row);
		//table.setNoWrap(column, row);
		//table.add(getLocalizedSmallHeader("child_care.mimetype", "Mimetype"), column++, row++);
		row++;
		
		Iterator iter = getExportFolder().getChildrenIterator();
		
		if (iter != null) {
			while (iter.hasNext()) {
				column = 1;
				ICFile file = (ICFile) iter.next();
				
				double size = 0;
				if (!file.isFolder()) {
					size = (double) file.getFileSize().intValue() / (double) 1024;					
				}
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
				//table.setNoWrap(column, row);
				//table.add(getSmallText(file.getMimeType()), column++, mos.write(new String("hello, world").getBytes()););
				row++;	
			}
		}
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		
		return table;
	}
	
	private Form getCreateContractAndPlacementChangesForm() {
		Form form = new Form();
		form.addParameter(PARAMETER_CREATE_REPORT, "true");
		
		SubmitButton button = (SubmitButton) getButton(new SubmitButton(localize("child_care.create_report", "Create report")));
		form.add(button);
		
		if (createExport && !exportCreated) {
			form.add(Text.getNonBrakingSpace());
			form.add(getSmallErrorText(localize("child_care.create_report_failed", "Failed to create report. Possibly no changes found.")));
		}
		
		return form;
	}	
	

	public ICFile getExportFolder() {
		return exportFolder;
	}

	public void setExportFolder(ICFile statisticsFolder) {
		this.exportFolder = statisticsFolder;
	}
	
	public IWContext getIwc() {
		return iwc;
	}
	
	public void setIwc(IWContext iwc) {
		this.iwc = iwc;
	}	

	// end of Contract and placement changes	
	
}
