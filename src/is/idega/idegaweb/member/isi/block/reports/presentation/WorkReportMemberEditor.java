/*
 * Created on 26.6.2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.data.WorkReportClubMember;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.core.data.Address;
import com.idega.data.IDOException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * @author laddi
 */
public class WorkReportMemberEditor extends WorkReportSelector {

	private static final String PARAMETER_SAVE = "param_save";

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		if (this.getWorkReportId() == -1) {
			add("Nothing found...");
		}
		else {
			parseAction(iwc);
			printForm(iwc);
		}
	}
	
	private void printForm(IWContext iwc) {
		try {
			Collection members = getWorkReportBusiness(iwc).getWorkReportClubMemberHome().findAllClubMembersByWorkReportIdOrderedByMemberName(getWorkReportId());
			Collection leagues = getWorkReportBusiness(iwc).getAllLeagueGroups();
			
			Form form = new Form();
			form.maintainParameters(getParametersToMaintain());
			
			Table table = new Table();
			form.add(table);
			
			int row = 1;
			int column = 1;
			
			table.add("Name", column++, row);
			table.add("Personal ID", column++, row);
			table.add("Address", column++, row);
			table.add("Zip code", column++, row);
			
			Iterator leagueIter = leagues.iterator();
			while (leagueIter.hasNext()) {
				WorkReportGroup group = (WorkReportGroup) leagueIter.next();
				table.add(group.getName(), column++, row);
			}
			row++;
			
			Iterator iter = members.iterator();
			while (iter.hasNext()) {
				column = 1;
				
				WorkReportClubMember user = (WorkReportClubMember) iter.next();
				table.add(user.getName(), column++, row);
				table.add(user.getPersonalId(), column++, row);

				Address address = getWorkReportBusiness(iwc).getUsersMainAddress(user.getUserId());
				if (address != null) {
					table.add(address.getStreetAddress(), column++, row);
					table.add(address.getPostalAddress(), column++, row);
				}
				else
					column = column + 2;
					
				Collection memberLeagues = null;
				try {
					memberLeagues = user.getLeaguesForMember();
				}
				catch (IDOException ie) {
					memberLeagues = null;
				}
				
				Iterator iterator = leagues.iterator();
				while (iterator.hasNext()) {
					WorkReportGroup group = (WorkReportGroup) iterator.next();
					CheckBox box = new CheckBox(group.getGroupId().toString());
					if (memberLeagues != null && memberLeagues.contains(group))
						box.setChecked(true);
						
					table.add(box, column++, row);
				}
				row++;
			}
			
			column = 1;
			TextInput ssn = new TextInput("ssn");
			ssn.setAsIcelandicSSNumber("Stupid git, not a valid social security number!");
			
			table.add("Add new member", column++, row);
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

			add(table);
		}
		catch (FinderException fe) {
		}
		catch (RemoteException re) {
		}
	}
	
	private void parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_SAVE)) {
			if (iwc.isParameterSet("ssn")) {
				try {
					getWorkReportBusiness(iwc).createEntry(getWorkReportId(), iwc.getParameter("ssn"));
				}
				catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}
}