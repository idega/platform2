/*
 * Created on 5.11.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection; 
import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationHome;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolHome;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;

/**
 * @author Roar
 *
 */
public class RegulationSearchPanel extends AccountingBlock {
	private static final String KEY_PROVIDER = "provider";
	private static final String KEY_PLACING = "placing";	
	private static final String KEY_VALID_DATE = "valid_date";	
	private static final String KEY_SEARCH = "search";	
	
	private static final String PAR_PROVIDER = KEY_PROVIDER; 
	private static final String PAR_PLACING = KEY_PLACING;	
	private static final String PAR_VALID_DATE = KEY_VALID_DATE; 
	private static final String PAR_ENTRY_PK = "PAR_ENTRY_PK";
	
	public RegulationSearchPanel(){
		super();
	}
	public RegulationSearchPanel(IWContext iwc){
		super(); 
		process(iwc);
	}
		
	
	private static final String ACTION_SEARCH_REGULATION = "ACTION_SEARCH_REGULATION";
	
	private Regulation _currentRegulation = null;
	private Collection _searchResult = null;
	private String _errorMessage = null;
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.presentation.AccountingBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		process(iwc);
				
		add(getSearchForm(iwc, _currentRegulation));
		
		if (_searchResult != null){
			add(getResultList(iwc, _searchResult)); 
		} 
	} 
	
	private boolean processed = false;
	public void process(IWContext iwc){
		if (! processed){
			boolean searchAction = iwc.getParameter(ACTION_SEARCH_REGULATION) != null;
	
			if (searchAction){
				_searchResult = doSearch(iwc);
			} 
			
			String regId = iwc.getParameter(PAR_ENTRY_PK);
			if (regId != null){
				_currentRegulation = getRegulation(regId);
			}

			processed = true;
		}
	}
	
	private List _maintainParameters = new ArrayList();
	public void maintainParameter(String par){
		_maintainParameters.add(par);
	}
	public void maintainParameter(String[] parameters){
		for(int i = 0; i < parameters.length; i++){
			_maintainParameters.add(parameters[i]);			
		}
	}	
	
	private boolean _maintainAllParameters = false;
	public void maintainAllParameters(){
		_maintainAllParameters = true;
	}	
	
	private List _setParameters = new ArrayList();	
	public void setParameter(String par, String value){
		_setParameters.add(new Parameter(par, value));
	}	
	
	
	private void maintainParameters(IWContext iwc, Link link){
		Iterator i = _maintainParameters.iterator();
		while(i.hasNext()){
			String par = (String) i.next();
			link.maintainParameter(par, iwc);
		}
	}
	
	private void setParameters(Link link){
		Iterator i = _setParameters.iterator();
		while(i.hasNext()){
			link.addParameter((Parameter) i.next());
		}
	}		

	
	private Table getResultList(IWContext iwc, Collection results){
		Table table = new Table(); 
		table.setCellspacing(10);
		int row = 1, col = 1;
		Iterator i = results.iterator();

		HiddenInput h = new HiddenInput(PAR_ENTRY_PK, "");
		table.add(h); 
						
		while(i.hasNext()){
			Regulation reg = (Regulation) i.next();
			Link link = new Link(reg.getName() + " ("+formatDate(reg.getPeriodFrom(), 4) + "-" + formatDate(reg.getPeriodTo(), 4)+")");
			link.addParameter(new Parameter(PAR_ENTRY_PK, reg.getPrimaryKey().toString()));
			maintainParameters(iwc, link);
			setParameters(link);
//			link.setOnClick("getElementById('"+ pkId +"').value='"+ reg.getPrimaryKey() +"'");			
//			link.setToFormSubmit(form);
					
			if (col > 3){
				col = 1;
				row++;
			} else{
				col++;
			}
			
			table.add(link, col, row);
		}
		return table;
	}

	
	private Collection doSearch(IWContext iwc){
		Collection matches = new ArrayList();
		String wcName = iwc.getParameter(PAR_PLACING);
		String vDate = iwc.getParameter(PAR_VALID_DATE);
		Date validDate = parseDate(vDate);		
		if (vDate != null && vDate.length() > 0 && validDate == null){
			_errorMessage = localize("regulation_search_panel.date_format_error", "Error i dateformat");
	
		} else {
			try{
				RegulationHome regHome = (RegulationHome) IDOLookup.getHome(Regulation.class);
				
				matches = validDate != null ? 
					regHome.findRegulationsByNameNoCaseAndDate("%"+wcName+"%", validDate)
					: regHome.findRegulationsByNameNoCase("%"+wcName+"%");
	//			System.out.println(matches);
	
			}catch(RemoteException ex){
				ex.printStackTrace();
				
			}catch(FinderException ex){
				ex.printStackTrace();			
			}
		}
		return matches;
	}	
	
	private Regulation getRegulation(String regId){
		Regulation reg = null;
		try{
			RegulationHome regHome = (RegulationHome) IDOLookup.getHome(Regulation.class);
			reg = regHome.findByPrimaryKey(regId);

		}catch(RemoteException ex){
			ex.printStackTrace();
			
		}catch(FinderException ex){
			ex.printStackTrace();			
		}		
		return reg;
	}
	
	public Regulation getRegulation(){
		return _currentRegulation;
	}
	
	private Table getSearchForm(IWContext iwc, Regulation reg){
		Collection providers = new ArrayList();		
		try{
			SchoolBusiness schoolBusiness = (SchoolBusiness) IBOLookup.getServiceInstance(iwc.getApplicationContext(), SchoolBusiness.class);
			String opField = getSession().getOperationalField();
			SchoolCategory sc = schoolBusiness.getSchoolCategoryHome().findByPrimaryKey(opField);
		
			SchoolHome home = (SchoolHome) IDOLookup.getHome(School.class);				
			providers = home.findAllByCategory(sc);
		}catch(RemoteException ex){
			ex.printStackTrace();
		}catch(FinderException ex){ 
			ex.printStackTrace();
		}

		Table table = new Table();
		int row = 1;
		
		String placing = reg != null ? reg.getName() : iwc.getParameter(PAR_PLACING);
		
		addDropDown(table, PAR_PROVIDER, KEY_PROVIDER, providers, iwc.getParameter(PAR_PROVIDER), "getSchoolName", 1, row++);
		addField(table, PAR_PLACING, KEY_PLACING, placing, 1, row);		
		if (_errorMessage != null){
			table.add(getErrorText(_errorMessage), 3, row);
			table.add(new Break());
		}
		addField(table, PAR_VALID_DATE, KEY_VALID_DATE, iwc.getParameter(PAR_VALID_DATE), 3, row);	
		
		table.add(getLocalizedButton(ACTION_SEARCH_REGULATION, KEY_SEARCH, "Search"), 5, row++);


		return table;
	
	}

//	private Form setToMaintain(Form form){
//		if (_maintainAllParameters){
//			form.maintainAllParameters();
//		} else {
//			form.maintainParameters(_maintainParameters);	
//		}
//		
//		Iterator i = _setParameters.iterator();
//		while (i.hasNext()){
//			Parameter par = (Parameter) i.next();
//			form.add(new HiddenInput(par.getName(), par.getValue()));	
//		}
//	
//		return form;		
//	}
	
	
	private Table addDropDown(Table table, String parameter, String key, Collection options, String selected, String method, int col, int row) {
		DropdownMenu dropDown = getDropdownMenu(parameter, options, method);
		dropDown.setSelectedElement(selected);
		return addWidget(table, key, dropDown, col, row);		
	}
	
	private Table addField(Table table, String parameter, String key, String value, int col, int row){
		return addWidget(table, key, getTextInput(parameter, value), col, row);
	}
	
	private Table addWidget(Table table, String key, PresentationObject widget, int col, int row){
		table.add(getLocalizedLabel(key, key), col, row);
		table.add(widget, col + 1, row);
		return table;
	
	}		
		
	

}
