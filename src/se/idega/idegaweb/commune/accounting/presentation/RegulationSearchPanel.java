/*
 * Created on 5.11.2003
 *
 */
package se.idega.idegaweb.commune.accounting.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.business.PostingException;
import se.idega.idegaweb.commune.accounting.regulations.business.PaymentFlowConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationHome;
import se.idega.idegaweb.commune.accounting.school.data.Provider;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;

/**
 * @author Roar
 * 
 * Search panel for searchin in regulations and postings. 
 * The panel shows a dropdown with providers, a field for regulation name and a field
 * for date.
 */
public class RegulationSearchPanel extends AccountingBlock {
	private static final String KEY_PROVIDER = "provider";
	private static final String KEY_PLACING = "placing";	
	private static final String KEY_VALID_DATE = "valid_date";	
	private static final String KEY_SEARCH = "search";		
	
	public static String PAR_PROVIDER = KEY_PROVIDER; 
	public static final String PAR_PLACING = KEY_PLACING;	
	public static final String PAR_VALID_DATE = KEY_VALID_DATE; 
	private static final String PAR_ENTRY_PK = "PAR_ENTRY_PK";

	
	public static final String SEARCH_REGULATION = "ACTION_SEARCH_REGULATION";
	
	private Regulation _currentRegulation = null;
	private Collection _searchResult = null;
	private SchoolCategory _currentSchoolCategory = null;
	private SchoolType _currentSchoolType = null;
	private String[] _currentPosting = null;
	private School _currentSchool = null;
	private Date _validDate = null;
	private String _currentPlacing = null;
	private String _placingErrorMessage = null;
	private String _dateFormatErrorMessage = null;
	private PostingException _postingException = null;
	private boolean _outFlowOnly = false;
	
		
	//Force the request to be processed at once.
	private RegulationSearchPanel(){
		super();
	}
	
	public RegulationSearchPanel(IWContext iwc){
		super(); 
		process(iwc);
	}
	
	public RegulationSearchPanel(IWContext iwc, String providerKey){
		super(); 
		PAR_PROVIDER = providerKey;
		process(iwc);
	}	
		
	



	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.accounting.presentation.AccountingBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
				
		process(iwc); //This should not be necessary, as the request will always be processed by now...

		maintainParameter(PAR_PLACING);
		maintainParameter(PAR_PROVIDER);
		maintainParameter(PAR_VALID_DATE);
				
		Table t = new Table();
		t.add(getSearchForm(iwc), 1, 1);
		
		if (_searchResult != null){
			t.add(getResultList(iwc, _searchResult), 1, 2); 
		} 
		add(t);
	} 
	
	private boolean processed = false;
	/**
	 * Processes the request: does the actual search or lookup so that the
	 * enclosing block can use the result to populate fields.
	 * @param iwc
	 */
	public void process(IWContext iwc){
		if (! processed){
			boolean searchAction = iwc.getParameter(SEARCH_REGULATION) != null;
			
			//Find selected category, date and provider
			String vDate = iwc.getParameter(PAR_VALID_DATE);
			_validDate = parseDate(vDate);		
			if (vDate != null && vDate.length() > 0 && _validDate == null){
				_dateFormatErrorMessage = localize("regulation_search_panel.date_format_error", "Error i dateformat");
			} else {
				
				_currentSchool = null;
				//First time on this page: PAR_PROVIDER parameter not set
				if (iwc.getParameter(PAR_PROVIDER) != null){
					try{
						SchoolHome schoolHome = (SchoolHome) IDOLookup.getHome(School.class);	
						int currentSchoolId = new Integer(iwc.getParameter(PAR_PROVIDER)).intValue();
						_currentSchool = schoolHome.findByPrimaryKey("" + currentSchoolId);
					}catch(RemoteException ex){
						ex.printStackTrace();
					}catch(FinderException ex){ 
						ex.printStackTrace();
					}
				}
				
					
				//Search regulations
				if (searchAction){
					_searchResult = doSearch(iwc);
				} 
						
				//Lookup regulation and postings
				String regId = iwc.getParameter(PAR_ENTRY_PK);
				//regId and _currentRegulation will get a value only after choosing a regulation (by clicking a link)
				if (regId != null){
					_currentRegulation = getRegulation(regId);
					try{
						RegulationsBusiness regBiz = (RegulationsBusiness) IBOLookup.getServiceInstance(iwc, RegulationsBusiness.class);
						PostingBusiness postingBiz = (PostingBusiness) IBOLookup.getServiceInstance(iwc, PostingBusiness.class);
						if (_currentRegulation != null){
							_currentSchoolType = regBiz.getSchoolType(_currentRegulation);
							_currentPosting = postingBiz.getPostingStrings(getCurrentSchoolCategory(iwc), _currentSchoolType, ((Integer) _currentRegulation.getRegSpecType().getPrimaryKey()).intValue(), new Provider(_currentSchool), _validDate);
						}
					}catch (RemoteException ex){
						ex.printStackTrace();
					}catch (PostingException ex){
						_postingException = ex;
					}
									
				}
				if (_currentRegulation!= null){
					_currentPlacing = _currentRegulation.getName();
				} else if (iwc.getParameter(PAR_PLACING) != null){
					_currentPlacing = iwc.getParameter(PAR_PLACING);
				}
								
			}
			processed = true;
		}
	}
	
	public SchoolCategory getCurrentSchoolCategory(IWContext iwc){
		if (_currentSchoolCategory == null){
		
			try {
				SchoolBusiness schoolBusiness = (SchoolBusiness) IBOLookup.getServiceInstance(iwc.getApplicationContext(),	SchoolBusiness.class);
				String opField = getSession().getOperationalField();
				_currentSchoolCategory = schoolBusiness.getSchoolCategoryHome().findByPrimaryKey(opField);					
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}	
		}
		
		return _currentSchoolCategory;	
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

	/**
	 * Formats the search results and returns a Table that displays it
	 * as links, or an approperiate text if none where found
	 * @param iwc
	 * @param results
	 * @return
	 */
	private Table getResultList(IWContext iwc, Collection results){
		Table table = new Table(); 
		table.setCellspacing(10);
		int row = 1, col = 0;
		
		if (results.size() == 0){
			table.add(getErrorText(localize("regulation_search_panel.no_regulations_found", "No regulations found")));
			
		} else {
			Iterator i = results.iterator();
	
			HiddenInput h = new HiddenInput(PAR_ENTRY_PK, "");
			table.add(h); 
							
			while(i.hasNext()){
				Regulation reg = (Regulation) i.next();
				if (_outFlowOnly && !reg.getPaymentFlowType().getLocalizationKey().equals(PaymentFlowConstant.OUT)) {
					continue;
				}else{
					Link link = new Link(reg.getName() /* + " ("+formatDate(reg.getPeriodFrom(), 4) + "-" + formatDate(reg.getPeriodTo(), 4)+")"*/ );
					link.addParameter(new Parameter(PAR_ENTRY_PK, reg.getPrimaryKey().toString()));
					maintainParameters(iwc, link);
					setParameters(link);
					
					if (col >= 3){
						col = 1;
						row++;
					} else{
						col++;
					}
					
					table.add(link, col, row);
				}
			}
		}
		return table;
	}


	/**
	 *	Does the search in the regulations and return them as a Collection
	 */
	private Collection doSearch(IWContext iwc){
		Collection matches = new ArrayList();
		String wcName = "%"+iwc.getParameter(PAR_PLACING)+"%";
		try{
			RegulationHome regHome = (RegulationHome) IDOLookup.getHome(Regulation.class);
			
			String catId = getCurrentSchoolCategoryId(iwc);
			
			matches = _validDate != null ? 
				regHome.findRegulationsByNameNoCaseDateAndCategory(wcName, _validDate, catId)
				: regHome.findRegulationsByNameNoCaseAndCategory(wcName, catId);

			
			
		}catch(RemoteException ex){
			ex.printStackTrace();
			
		}catch(FinderException ex){
			ex.printStackTrace();			
		}
		
		return matches;
	}	
	
	/**
	 * Does a lookup to find a regulation.
	 * @param regId
	 * @return the Regulation
	 */
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
	
	/**
	 * 
	 * @return the currently chosen Regulation
	 */
	public Regulation getRegulation(){
		return _currentRegulation;
	}
	

			
	
	/**
	 * 
	 * @return the currently chosen posting strings as String[]
	 */
	public String[] getPosting() throws PostingException{
		if (_postingException != null){
			throw _postingException;
		}
		return _currentPosting;
	}	

	/**
	 * Returns the search form as an Table
	 * @param iwc
	 * @return
	 */
	private Table getSearchForm(IWContext iwc) {
		
		Collection providers = new ArrayList();		
		try{
			SchoolHome home = (SchoolHome) IDOLookup.getHome(School.class);	
			SchoolCategory category = getCurrentSchoolCategory(iwc);
			if (category != null){			
				providers = home.findAllByCategory(category);
			}
		}catch(RemoteException ex){
			ex.printStackTrace();
		}catch(FinderException ex){ 
			ex.printStackTrace();
		}

		Table table = new Table();
		int row = 1;
		
		String currentSchoolId = _currentSchool != null ? "" + _currentSchool.getPrimaryKey() : "0";
		addDropDown(table, PAR_PROVIDER, KEY_PROVIDER, providers, currentSchoolId, "getSchoolName", true, 1, row++);
		
//		Collection types = null;
//		try{
//			types = _currentSchool != null ? _currentSchool.getSchoolTypes() : new ArrayList();
//		}catch(IDORelationshipException ex ){
//			ex.printStackTrace();
//		}

//		if (! providers.isEmpty()){
//			String currentTypeId = iwc.getParameter(PAR_TYPE) != null ? iwc.getParameter(PAR_TYPE) : "0";
//			addDropDown(table, PAR_TYPE, KEY_TYPE, types, currentTypeId, "getName", false, 1, row++);
//		}
		
		if (_placingErrorMessage != null){
			table.add(getErrorText(_placingErrorMessage), 2, row);
		}		
		if (_dateFormatErrorMessage != null){
			table.add(getErrorText(_dateFormatErrorMessage), 4, row);
		}		

		if (_dateFormatErrorMessage != null || _placingErrorMessage != null){
			row++;
		}
		

		
		addField(table, PAR_PLACING, KEY_PLACING, _currentPlacing, 1, row, 300);		
		String date = iwc.getParameter(PAR_VALID_DATE) != null ? iwc.getParameter(PAR_VALID_DATE) :
			formatDate(new Date(System.currentTimeMillis()), 4); 
		addField(table, PAR_VALID_DATE, KEY_VALID_DATE, date, 3, row, 35);	
		table.add(getLocalizedButton(SEARCH_REGULATION, KEY_SEARCH, "Search"), 5, row++);

		table.setColumnWidth(1, "" + _leftColMinWidth);
		return table;
	
	}

	private int _leftColMinWidth = 0;
	public void setLeftColumnMinWidth(int minWidth){
		_leftColMinWidth = minWidth;
	}
	
	
	private Table addDropDown(Table table, String parameter, String key, Collection options, String selected, String method, boolean setToSubmit, int col, int row) {
		DropdownMenu dropDown = getDropdownMenu(parameter, options, method);
		dropDown.setToSubmit(setToSubmit);
		dropDown.setSelectedElement(selected);
		return addWidget(table, key, dropDown, col, row);		
	}	
	
	private Table addField(Table table, String parameter, String key, String value, int col, int row, int width){
		return addWidget(table, key, getTextInput(parameter, value, width), col, row);
	}
	
	private Table addWidget(Table table, String key, PresentationObject widget, int col, int row){
		table.add(getLocalizedLabel(key, key), col, row);
		table.add(widget, col + 1, row);
		return table;
	
	}
	
	public void setOutFlowOnly(boolean b){
		_outFlowOnly = b;
	}

	public void setPlacingIfNull(String placing) {
		if (_currentPlacing == null){
			_currentPlacing = placing;
		}
	}		
	
	public void setSchoolIfNull(School school) {
		if (_currentSchool == null){
			_currentSchool = school;
		}
	}		
		


	public void setError(String placingErrorMessage){
		_placingErrorMessage = placingErrorMessage;
	}
	
	public School getSchool(){
		return _currentSchool;
	}
	
	public String getCurrentSchoolCategoryId(IWContext iwc) throws RemoteException, FinderException{
		SchoolBusiness schoolBusiness = (SchoolBusiness) IBOLookup.getServiceInstance(iwc.getApplicationContext(),	SchoolBusiness.class);
		String opField = getSession().getOperationalField();
		return schoolBusiness.getSchoolCategoryHome().findByPrimaryKey(opField).getPrimaryKey().toString();					
	}

	/**
	 * @return
	 */
	public SchoolType getCurrentSchoolType() {
		return _currentSchoolType;
	}
		
	

}
