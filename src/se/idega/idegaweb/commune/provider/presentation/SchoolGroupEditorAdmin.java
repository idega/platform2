/*
 * Created on 4.11.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.provider.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.business.AccountingSession;
import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;
import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;
import se.idega.idegaweb.commune.school.business.SchoolCommuneSession;
import se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.UnavailableIWContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.RadioButton;


/**
 * @author Roar
 *
 * Block for creating and editing a school group by a centralized administrator (BUN)
 */
public class SchoolGroupEditorAdmin extends SchoolGroupEditor {
	
	public final static String PARAM_BUNADM = "PARAM_BUNADM";		
	private boolean _centralAdmin = false;
	protected IWContext _iwc = null;
	
	
	public void init(IWContext iwc) throws Exception {
		_iwc = iwc;
		_centralAdmin = iwc.getParameter(PARAM_BUNADM) != null && iwc.getParameter(PARAM_BUNADM).equals("true");
		super.init(iwc); 
	}	
	
	/**
	 * Returns the currently selected provider or null if none selected.
	 */
	protected School getProvider() throws RemoteException, FinderException{
		SchoolHome home = (SchoolHome) IDOLookup.getHome(School.class);
		String id = _iwc.getParameter(SchoolCommuneSessionBean.PARAMETER_SCHOOL_ID);
		if (id != null && ! id.equals("-1")) {
			return home.findByPrimaryKey(id);			
		}
		return null;

	}
	
	/**
	 * Returns the currently selected providers id or -1 if none selected.
	 */
	public int getProviderID() throws RemoteException{
		try{
			School provider = getProvider();	
			if (provider != null){
				return new Integer(""+getProvider().getPrimaryKey()).intValue();
			}
		}catch(FinderException ex){
			ex.printStackTrace();
		}
		
		return super.getProviderID();
	}	
	
	/**
	 * Returns a Parameter containing the current selected provider
	 */
	Parameter getProviderAsParameter(){
		if (_iwc.getParameter(SchoolCommuneSessionBean.PARAMETER_SCHOOL_ID) != null){
			return new Parameter(SchoolCommuneSessionBean.PARAMETER_SCHOOL_ID,  _iwc.getParameter(SchoolCommuneSessionBean.PARAMETER_SCHOOL_ID));
		} else {
			return new Parameter ("", "");
		}
	}	
		
	/**
	 * Returns the navigation form from SchoolGroupEditor, appended to a navigation
	 * form for centralized administrators (BUN)
	 */
	
	protected Form getNavigationForm(boolean showStudyPaths) throws RemoteException {
		Form form = new Form();
		form.add(getNavigationTable(_iwc, true, true));
		
		try{
			Form form2 = super.getNavigationForm(false);
			form2.maintainParameter(SchoolCommuneSessionBean.PARAMETER_SCHOOL_ID);
			form2.maintainParameter(PARAMETER_TYPE_ID);
			form.add(form2);
		}catch(NullPointerException ex){
			//ex.printStackTrace();
		}
		return form;
	}
	
	/**
	 * Returns a table with fields for centralized administrator: 
	 * Drop down for selecting operational field, radio buttons for including
	 * providers not centrally administrated and drop down for selecting provider.
	 * @param iwc
	 * @param multipleSchools
	 * @param centralizedAdminChoice
	 * @return Table 
	 * @throws RemoteException
	 */
	protected Table getNavigationTable(IWContext iwc, boolean multipleSchools, boolean centralizedAdminChoice) throws RemoteException {
		String category = getAccountingSession().getOperationalField();
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);

		int row = 1;

		if (multipleSchools) {
			table.add(getSmallHeader(localize("school.opfield", "Operationalfield: ")), 1, row);
			table.mergeCells(2, row, 3, row);
			table.add(new OperationalFieldsMenu(), 2, row++);
			table.setHeight(row++, 15);			
			
			if (centralizedAdminChoice){
				table.add(getSmallHeader(localize("school.bun_adm","Show only BUN administrated schools")+":"+Text.NON_BREAKING_SPACE),1,row);
		
				RadioButton rb1 = new RadioButton(PARAM_BUNADM, ""+true);
				RadioButton rb2 = new RadioButton(PARAM_BUNADM, ""+false);		
						
				if (_centralAdmin){
					rb1.setSelected();
				} else{
					rb2.setSelected();
				}

				rb1.setToSubmit();
				rb2.setToSubmit();
				table.add(rb1,2,row);
				table.add(getSmallHeader(localize("school.yes","Yes")+Text.NON_BREAKING_SPACE),2,row);

				table.add(rb2,3,row);
				table.add(getSmallHeader(localize("school.no","No")+Text.NON_BREAKING_SPACE),3,row);

				row++;			
				table.setHeight(row++, 2);		
			}
						
			table.add(getSmallHeader(localize("school.school_list","School")+":"+Text.NON_BREAKING_SPACE),1,row);
			table.mergeCells(2, row, 8, row);
			table.add(getSchools(iwc, _centralAdmin, category), 2, row);
			table.setHeight(row, 15);
		}

		table.setHeight(row + 1, 20);	
		
		return table;
	}	

/**
 * Returns the drop down with providers
 * @param iwc
 * @param onlyCentralizedAdministrated
 * @param category
 * @return
 * @throws RemoteException
 */
	protected DropdownMenu getSchools(IWContext iwc, boolean onlyCentralizedAdministrated, String category) throws RemoteException {
		
		SchoolCommuneSession session = (SchoolCommuneSession) IBOLookup.getSessionInstance(iwc, SchoolCommuneSession.class);
		SchoolCommuneBusiness business = (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);
		SchoolBusiness sBusiness = (SchoolBusiness) IBOLookup.getServiceInstance(iwc.getApplicationContext(), SchoolBusiness.class);	
				
		DropdownMenu menu = new DropdownMenu(session.getParameterSchoolID());
		menu.setToSubmit();
		Collection schools = null;
		Collection schoolTypeIds = sBusiness.findAllSchoolTypesInCategory(category);
		if (schoolTypeIds == null){
			schoolTypeIds = new java.util.HashSet();
		}
				
		if (onlyCentralizedAdministrated) {
			schools = business.getSchoolBusiness().findAllCentralizedAdministratedByType(schoolTypeIds);			
		} else {
			schools = business.getSchoolBusiness().findAllSchoolsByType(schoolTypeIds);
		}
		Iterator iter = schools.iterator();
		while (iter.hasNext()) {
			School sCool = (School) iter.next();	
			menu.addMenuElement(sCool.getPrimaryKey().toString(), sCool.getName());
		}
		menu.addMenuElementFirst("-1", localize("school.all_schools", "All schools"));
		
		try{
			int schoolId = new Integer(iwc.getParameter(session.getParameterSchoolID())).intValue();
			menu.setSelectedElement(schoolId);	
		}catch(NumberFormatException ex){				
		}catch(NullPointerException ex){
		}

		
		return (DropdownMenu) getStyledInterface(menu);	
	}
	
	
	public AccountingSession getAccountingSession() {
			try
			{
				return (AccountingSession)IBOLookup.getSessionInstance(IWContext.getInstance(),AccountingSession.class);
			}
			catch (IBOLookupException e)
			{
				System.err.print("AccountingBlock.getSession(): Error looking up AccountingSession");
				e.printStackTrace();
			}
			catch (UnavailableIWContext e)
			{
				System.err.print("AccountingBlock.getSession(): Error getting IWContext");
				e.printStackTrace();
			}
		return null;			
	}

		
	
}
