/*
 * Created on 26.6.2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;


/**
 * @author laddi
 */
public class WorkReportMemberEditor extends WorkReportSelector {

	private static final String PARAMETER_SAVE = "param_save";
	private static final String STEP_NAME_LOCALIZATION_KEY = "workreportmembereditor.step_name";
	/**
	 * 
	 */
	public WorkReportMemberEditor() {
		super();
		setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
	}

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		if (this.getWorkReportId() != -1){
			//sets this step as bold, if another class calls it this will be overridden
			setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
			parseAction(iwc);
			printForm(iwc);
		}
	}
	
	
	private void printForm(IWContext iwc) {
		try {
			WorkReportBusiness wBiz = getWorkReportBusiness(iwc);
			Collection members = wBiz.getAllWorkReportMembersForWorkReportId(getWorkReportId());
			Collection leagues = wBiz.getAllWorkReportGroupsForYearAndType( wBiz.getWorkReportById(getWorkReportId()).getYearOfReport().intValue(),IWMemberConstants.GROUP_TYPE_LEAGUE);
			
			Form form = new Form();
			form.maintainParameters(getParametersToMaintain());
			
			Table table = new Table();
			form.add(table);
			
			int row = 1;
			int column = 1;
			
			table.add(new Text(iwrb.getLocalizedString("WorkReportMemberEditor.column_name","Name"),true,false,false) , column++, row);
			table.add(new Text(iwrb.getLocalizedString("WorkReportMemberEditor.column_personal_id","Personal ID"),true,false,false), column++, row);
			table.add(new Text(iwrb.getLocalizedString("WorkReportMemberEditor.column_address","Address"),true,false,false), column++, row);
			table.add(new Text(iwrb.getLocalizedString("WorkReportMemberEditor.column_postal_code","Postal code"),true,false,false), column++, row);
			
			Iterator leagueIter = leagues.iterator();
			while (leagueIter.hasNext()) {
				WorkReportGroup group = (WorkReportGroup) leagueIter.next();
				table.add(new Text(group.getName(),true,false,false), column++, row);
			}
			row++;
			
			Iterator iter = members.iterator();
			while (iter.hasNext()) {
				column = 1;
				
				WorkReportMember user = (WorkReportMember) iter.next();
				table.add(user.getName(), column++, row);
				table.add(user.getPersonalId(), column++, row);

				if(user.getStreetName()!=null ){
					table.add(user.getStreetName(), column++, row);
				
					if(user.getPostalCodeID()!=-1){
						try {
							table.add(user.getPostalCode().getPostalCode(), column, row);
						}
						catch (SQLException e) {
						//do nothing
						}
					}
					column++;
				}
				else{
					column = column + 2;
				}
				
				Collection memberLeagues = null;
				try {
					memberLeagues = user.getLeaguesForMember();
				}
				catch (IDOException ie) {
					ie.printStackTrace();
				}
				
				Iterator iterator = leagues.iterator();
				while (iterator.hasNext()) {
					WorkReportGroup group = (WorkReportGroup) iterator.next();
					CheckBox box = new CheckBox(group.getPrimaryKey().toString());
					if (memberLeagues != null && memberLeagues.contains(group))
						box.setChecked(true);
						
					table.add(box, column++, row);
				}
				row++;
			}
			
			column = 1;
			TextInput ssn = new TextInput("ssn");
			ssn.setAsIcelandicSSNumber(iwrb.getLocalizedString("WorkReportMemberEditor.ssn_not_valid","The personal id you entered is invalid"));
			
			table.add(new Text(iwrb.getLocalizedString("WorkReportMemberEditor.add_member","Enter personal id"),true,false,false), column++, row);
			table.add(ssn, column++, row);
			column += 2;
			
			Iterator iterator = leagues.iterator();
			while (iterator.hasNext()) {
				WorkReportGroup group = (WorkReportGroup) iterator.next();
				CheckBox box = new CheckBox(group.getGroupId().toString());
				table.add(box, column++, row);
			}
			row++;
			
			column = 1;
			table.add(new SubmitButton("Submit", PARAMETER_SAVE, "true"), column, row);

			add(form);
			add(iwrb.getLocalizedString("WorkReportMemberEditor.number_of_members","Number of members : ")+(Math.max(row-3,0)));
			
		}
		catch (RemoteException re) {
		}
	}
	
	private void parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_SAVE)) {
			if (iwc.isParameterSet("ssn")) {
				try {
					getWorkReportBusiness(iwc).createWorkReportMember(getWorkReportId(), iwc.getParameter("ssn"));
				}
				catch (RemoteException e) {
					e.printStackTrace();
				} catch (CreateException e) {
					add(new ExceptionWrapper(e));
					e.printStackTrace();
					
				}
			}
		}
	}
}