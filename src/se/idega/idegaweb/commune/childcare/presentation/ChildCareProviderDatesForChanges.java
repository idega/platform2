package se.idega.idegaweb.commune.childcare.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

/**
 *    
 *  
 * @author Dainis
 *
 */
public class ChildCareProviderDatesForChanges extends ChildCareBlock {
    // request parameters
    
    /**
     * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
     */
    public void init(IWContext iwc) throws Exception {
        if (isCommuneAdministrator(iwc) || getSession().hasPrognosis()) {
            parse(iwc);
            add(getGui(iwc));
        }
    }
    
    /**
     * Creates GUI
     * 
     * @param iwc
     * @return PresentationObjectContainer
     */
    private PresentationObjectContainer getGui(IWContext iwc) {
        PresentationObjectContainer container = new PresentationObjectContainer();
        
        Form form = new Form();
        
        Table table = new Table(1,2);
        form.add(table);
        
        table.setCellpadding(0);
        table.setCellspacing(0);
        table.setBorder(1);
        
        int row = 1;
        
        table.add(getDateInputs(), 1, row++);
        
        SubmitButton submit = (SubmitButton) getStyledInterface(new SubmitButton("OK"));
        table.add(submit, 1, row++);
        
        container.getChildren().add(table);        
        return container;
    }
    
    /**
     * Create panel with all date inputs and their labels
     * 
     * @return
     */
    private Table getDateInputs() {
        Table table = new Table(5, 2);
        table.setBorder(1);
        
        table.add(new Text("Select start date:"), 1, 1);
        table.add(new Text("End date:"), 1, 2);
        
        table.add(getSmallHeader(localize("child_care.from", "From") + ":"), 2, 1);
        table.add(getSmallHeader(localize("child_care.from", "From") + ":"), 2, 2);
        
        DateInput registerDateFrom = (DateInput) getStyledInterface(new DateInput());
        DateInput removeDateFrom = (DateInput) getStyledInterface(new DateInput());
        table.add(registerDateFrom, 3, 1);
        table.add(removeDateFrom, 3, 2);   
        
        table.add(getSmallHeader(localize("child_care.to", "To") + ":"), 4, 1);
        table.add(getSmallHeader(localize("child_care.to", "To") + ":"), 4, 2);

        DateInput registerDateTo = (DateInput) getStyledInterface(new DateInput());
        DateInput removeDateTo = (DateInput) getStyledInterface(new DateInput());        
        table.add(registerDateTo, 5, 1);
        table.add(removeDateTo, 5, 2);  
        
        return table;
    }
    
    /**
     * Parses request to find out request parameters
     * 
     * @param iwc
     */
    private void parse(IWContext iwc) {
        
    }
     
    
}
