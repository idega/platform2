package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.DownloadLink;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 *    
 *  
 * @author Dainis
 *
 */
public class ChildCareProviderDatesForChanges extends ChildCareBlock {
    // request parameters
	protected static final String PARAMETER_START_FROM = "cc_start_from";
	protected static final String PARAMETER_START_TO = "cc_start_to";	
	protected static final String PARAMETER_END_FROM = "cc_end_from";
	protected static final String PARAMETER_END_TO = "cc_end_to";
	
	private IWTimestamp startFromTimestamp;
	private IWTimestamp startToTimestamp;	
	private IWTimestamp endFromTimestamp;
	private IWTimestamp endToTimestamp;	
	
	
    /**
     * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
     */
    public void init(IWContext iwc) throws Exception {
        if (isCommuneAdministrator(iwc)) {
            parse(iwc);
            add(getGui(iwc));
        }
    }
    
    /**
     * Creates GUI
     * 
     * @param iwc
     * @return PresentationObjectContainer
     * @throws RemoteException 
     */
    private PresentationObjectContainer getGui(IWContext iwc) throws RemoteException {
        PresentationObjectContainer container = new PresentationObjectContainer();
        
        Form form = new Form();
        
        Table table = new Table(1,3);
        form.add(table);
        
        table.setCellpadding(0);
        table.setCellspacing(2);
        table.setBorder(0);
        
        int row = 1;
        
        table.add(getDateInputs(), 1, row++);
        
        SubmitButton submit = (SubmitButton) getStyledInterface(new SubmitButton("OK"));
        table.add(submit, 1, row++);
        
        table.add(getXLSLink(), 1, row++);
        
        container.getChildren().add(form);        
        return container;
    }
    
    /**
     * Create panel with all date inputs and their labels
     * 
     * @return
     */
    private Table getDateInputs() {
        Table table = new Table(3, 2);
        table.setBorder(0);
        table.setCellspacing(5);
        
        table.add(getSmallHeader(localize("child_care.select_start_date", "Select start date:")), 1, 1);
        table.add(getSmallHeader(localize("child_care.select_end_date", "End date:")), 1, 2);
        
        table.add(getSmallHeader(localize("child_care.from", "From") + ":"), 2, 1);
        table.add(new Break(), 2, 1);
        table.add(getSmallHeader(localize("child_care.from", "From") + ":"), 2, 2); 
        table.add(new Break(), 2, 2);
        
        DateInput registerDateFrom = (DateInput) getStyledInterface(new DateInput(PARAMETER_START_FROM, true));        
		if (getStartFromTimestamp() != null)
			registerDateFrom.setDate(getStartFromTimestamp().getDate()); 
        DateInput removeDateFrom = (DateInput) getStyledInterface(new DateInput(PARAMETER_END_FROM, true));
		if (getEndFromTimestamp() != null)
			removeDateFrom.setDate(getEndFromTimestamp().getDate());          
        
        table.add(registerDateFrom, 2, 1);
        table.add(removeDateFrom, 2, 2);   
        
        table.add(getSmallHeader(localize("child_care.to", "To") + ":"), 3, 1);
        table.add(new Break(), 3, 1);   
        table.add(getSmallHeader(localize("child_care.to", "To") + ":"), 3, 2);
        table.add(new Break(), 3, 2);   
        
        DateInput registerDateTo = (DateInput) getStyledInterface(new DateInput(PARAMETER_START_TO, true));
		if (getStartToTimestamp() != null)
			registerDateTo.setDate(getStartToTimestamp().getDate());         
        DateInput removeDateTo = (DateInput) getStyledInterface(new DateInput(PARAMETER_END_TO, true));   
		if (getEndToTimestamp() != null)
			removeDateTo.setDate(getEndToTimestamp().getDate());
		
        table.add(registerDateTo, 3, 1);
        table.add(removeDateTo, 3, 2);  
        
        return table;
    }
    
    /**
     * Parses request to find out request parameters
     * 
     * @param iwc
     */
    private void parse(IWContext iwc) {
    	if (iwc.isParameterSet(PARAMETER_START_FROM))
			this.setStartFromTimestamp(stringToIWTimestamp(iwc.getParameter(PARAMETER_START_FROM))); 
    	
    	if (iwc.isParameterSet(PARAMETER_START_TO))
			this.setStartToTimestamp(stringToIWTimestamp(iwc.getParameter(PARAMETER_START_TO))); 
    	
    	if (iwc.isParameterSet(PARAMETER_END_FROM))
			this.setEndFromTimestamp(stringToIWTimestamp(iwc.getParameter(PARAMETER_END_FROM))); 
    	
    	if (iwc.isParameterSet(PARAMETER_END_TO))
			this.setEndToTimestamp(stringToIWTimestamp(iwc.getParameter(PARAMETER_END_TO))); 
    	
    }
    
    /** 
     * Converts String to IWTimestamp
     * 
     * @param s
     * @return
     */
    private IWTimestamp stringToIWTimestamp(String s) {
    	if (s != null) {
			return new IWTimestamp(s);
		} else {
			return null;
		}
    }
    
    /**
     * By clicking on this link user can download results of the query
     * 
     * @return
     * @throws RemoteException
     */
	private Link getXLSLink() throws RemoteException {
		DownloadLink link = new DownloadLink(getBundle().getImage("shared/xls.gif"));
		link.setMediaWriterClass(ChildCareDatesForChangesWriter.class);
		link.addParameter(ChildCareDatesForChangesWriter.PARAMETER_PROVIDER_ID, getSession().getChildCareID());
		if (getStartFromTimestamp() != null)
			link.addParameter(PARAMETER_START_FROM, String.valueOf(getStartFromTimestamp().getDate()));
		if (getStartToTimestamp() != null)
			link.addParameter(PARAMETER_START_TO, String.valueOf(getStartToTimestamp().getDate()));
		if (getEndFromTimestamp() != null)
			link.addParameter(PARAMETER_END_FROM, String.valueOf(getEndFromTimestamp().getDate()));
		if (getEndToTimestamp() != null)
			link.addParameter(PARAMETER_END_TO, String.valueOf(getEndToTimestamp().getDate()));		

		return link;
	}    
    

	public IWTimestamp getEndFromTimestamp() {
		return endFromTimestamp;
	}

	public void setEndFromTimestamp(IWTimestamp endFromTimestamp) {
		this.endFromTimestamp = endFromTimestamp;
	}

	public IWTimestamp getEndToTimestamp() {
		return endToTimestamp;
	}

	public void setEndToTimestamp(IWTimestamp endToTimestamp) {
		this.endToTimestamp = endToTimestamp;
	}

	public IWTimestamp getStartFromTimestamp() {
		return startFromTimestamp;
	}

	public void setStartFromTimestamp(IWTimestamp startFromTimestamp) {
		this.startFromTimestamp = startFromTimestamp;
	}

	public IWTimestamp getStartToTimestamp() {
		return startToTimestamp;
	}

	public void setStartToTimestamp(IWTimestamp startToTimestamp) {
		this.startToTimestamp = startToTimestamp;
	}
     
    
}
