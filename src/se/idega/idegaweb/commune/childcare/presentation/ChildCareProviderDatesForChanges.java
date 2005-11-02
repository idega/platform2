package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.idega.block.school.data.School;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.DownloadWriter;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.DownloadLink;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
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

    protected final static String PARAMETER_PROVIDER_ID = "cc_provider_id";    
	protected static final String PARAMETER_START_FROM = "cc_start_from";
	protected static final String PARAMETER_START_TO = "cc_start_to";	
	protected static final String PARAMETER_END_FROM = "cc_end_from";
	protected static final String PARAMETER_END_TO = "cc_end_to";
    
    protected static final int ALL_PROVIDERS = -1;
	
    private int providerId = ALL_PROVIDERS;
	private IWTimestamp startFromTimestamp;
	private IWTimestamp startToTimestamp;	
	private IWTimestamp endFromTimestamp;
	private IWTimestamp endToTimestamp;	
    
    public final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune.childcare";
    
	private IWResourceBundle iwrb = null;
    
    /**
     * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
     */
    public void init(IWContext iwc) throws Exception {
        if (isCommuneAdministrator(iwc)) {
            
            setIwrb(iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc));
            
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
        form.setAction(iwc.getIWMainApplication().getMediaServletURI());
        form.setMethod("post");
        form.addParameter(DownloadWriter.PRM_WRITABLE_CLASS, IWMainApplication.getEncryptedClassName(ChildCareDatesForChangesWriter.class));
        form.setToShowLoadingOnSubmit(false);        
        
        Table table = new Table(1,3);
        form.add(table);
        
        table.setCellpadding(0);
        table.setCellspacing(2);
        table.setBorder(0);
        
        int row = 1;
        table.add(getSmallHeader(getLocalizedString("provider", "Provider") + ":"), 1, row);
        table.add(new Break(), 1, row);
        table.add(getProviderMenu(getProviderId()), 1, row++); 
        
        table.add(getDateInputs(), 1, row++);
        
        SubmitButton submit = (SubmitButton) getStyledInterface(new SubmitButton(getLocalizedString("ok","OK")));        
        table.add(submit, 1, row);
        table.setAlignment(1, row++, Table.HORIZONTAL_ALIGN_RIGHT);        
        
        //table.add(getXLSLink(), 1, row++);
        
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
        
        table.add(getSmallHeader(getLocalizedString("select_start_date", "Select start date") + ":"), 1, 1);
        table.add(getSmallHeader(getLocalizedString("end_date", "End date") + ":"), 1, 2);
        
        table.add(getSmallHeader(getLocalizedString("from", "From") + ":"), 2, 1);
        table.add(new Break(), 2, 1);
        table.add(getSmallHeader(getLocalizedString("to", "To") + ":"), 2, 2); 
        table.add(new Break(), 2, 2);
        
        IWTimestamp stamp = new IWTimestamp();
        
        DateInput registerDateFrom = (DateInput) getStyledInterface(new DateInput(PARAMETER_START_FROM, true));        
		if (getStartFromTimestamp() != null)
			registerDateFrom.setDate(getStartFromTimestamp().getDate());
        registerDateFrom.setYearRange(stamp.getYear() - 11, stamp.getYear()+3);
        registerDateFrom.setAsNotEmpty(getLocalizedString("please_fill_start_from", "Please fill in start date from field!"));
        
        DateInput removeDateFrom = (DateInput) getStyledInterface(new DateInput(PARAMETER_END_FROM, true));
		if (getEndFromTimestamp() != null)
			removeDateFrom.setDate(getEndFromTimestamp().getDate()); 
        removeDateFrom.setYearRange(stamp.getYear() - 11, stamp.getYear()+3);
        removeDateFrom.setAsNotEmpty(getLocalizedString("please_fill_start_to", "Please fill in start date to field!"));        
        
        table.add(registerDateFrom, 2, 1);
        table.add(removeDateFrom, 2, 2);   
        
        table.add(getSmallHeader(getLocalizedString("to", "To") + ":"), 3, 1);
        table.add(new Break(), 3, 1);   
        table.add(getSmallHeader(getLocalizedString("to", "To") + ":"), 3, 2);
        table.add(new Break(), 3, 2);   
        
        DateInput registerDateTo = (DateInput) getStyledInterface(new DateInput(PARAMETER_START_TO, true));
		if (getStartToTimestamp() != null)
			registerDateTo.setDate(getStartToTimestamp().getDate()); 
        registerDateTo.setYearRange(stamp.getYear() - 11, stamp.getYear()+3);
        registerDateTo.setAsNotEmpty(getLocalizedString("please_fill_end_from", "Please fill in end date from field!"));        
                
        DateInput removeDateTo = (DateInput) getStyledInterface(new DateInput(PARAMETER_END_TO, true));   
		if (getEndToTimestamp() != null)
			removeDateTo.setDate(getEndToTimestamp().getDate());
        removeDateTo.setYearRange(stamp.getYear() - 11, stamp.getYear()+3);
        removeDateTo.setAsNotEmpty(getLocalizedString("please_fill_end_to", "Please fill in end date to field!"));                
		
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
        if (iwc.isParameterSet(PARAMETER_PROVIDER_ID)) 
            this.setProviderId(Integer.parseInt(iwc.getParameter(PARAMETER_PROVIDER_ID)));
        
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
		link.addParameter(PARAMETER_PROVIDER_ID, this.getProviderId());
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
    
    
    private DropdownMenu getProviderMenu(int providerId) {
        DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_PROVIDER_ID));
        menu.addMenuElementFirst("-1", getLocalizedString("all_providers", "All providers"));  ///xxx add this localized string     
        menu.setSelectedElement(providerId); 
        
        Collection providers = null;
        try {
            providers = getBusiness().getSchoolBusiness().findAllSchoolsByType(getBusiness().getSchoolBusiness().findAllSchoolTypesForChildCare());
        } catch (RemoteException e) {            
            e.printStackTrace();
        }
        if (providers != null) {
            Iterator iter = providers.iterator();
            while (iter.hasNext()) {
                School element = (School) iter.next();
                menu.addMenuElement(element.getPrimaryKey().toString(), element.getSchoolName());
            }
        }
        
        return menu;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public IWResourceBundle getIwrb() {
        return iwrb;
    }

    public void setIwrb(IWResourceBundle iwrb) {
        this.iwrb = iwrb;
    }
    
    private String getLocalizedString(String key, String defaultValue) {      
        String simpleName = null;        
        StringTokenizer parser = new StringTokenizer(this.getClass().getName(), ".");
        while (parser.hasMoreTokens()) {
            simpleName = parser.nextToken();
        }        
        String s = getIwrb().getLocalizedString(simpleName + "." + key, defaultValue);        
        return s;
    }     
    
}
