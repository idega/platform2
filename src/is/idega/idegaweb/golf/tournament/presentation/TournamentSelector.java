/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.access.AccessControl;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.SubmitButton;

/**
 * @author gimmi
 */
public class TournamentSelector extends TournamentBlock {

	public static String PARAMETER_TOURNAMENT = "tournament";
	
	protected boolean tournamentMustBeSet() {
		return false;
	}

	public void _main(IWContext modinfo) throws Exception {
		String tournament_id=modinfo.getParameter(PARAMETER_TOURNAMENT);
		if (tournament_id != null){
			super.setTournamentID(modinfo, tournament_id);
			String clsName = modinfo.getParameter(PARAMETER_CLASS_NAME);
			if (clsName == null || TournamentAdministratorWindow.class.getName().equals(clsName)) {
				getParentPage().setToRedirect(modinfo.getIWMainApplication().getObjectInstanciatorURI(TournamentRegistrationWindow.class)+"&tournament_admin_view="+TournamentAdministratorWindow.ADMIN_VIEW_REGISTER_MEMBER);
			}else {
	      getParentPage().setToRedirect(modinfo.getIWMainApplication().getObjectInstanciatorURI(Class.forName(clsName)));
			}
		} else {
			super._main(modinfo);
		}
	}
	
	public void main(IWContext modinfo) throws Exception {
		    Form form = new Form();
		    Table table = new Table(2, 4);
		    	table.setBorder(0);
		    	table.mergeCells(1, 1, 2, 1);
		    	table.mergeCells(1, 2, 2, 2);
		    	table.mergeCells(1, 4, 2, 4);
		        table.setAlignment(1,1,"center");
		        table.setAlignment(2,3,"right");
		        table.setAlignment(1,4,"center");
		    form.add(table);
		    form.maintainParameter(PARAMETER_CLASS_NAME);

		    super.setAdminView(TournamentAdministratorWindow.ADMIN_VIEW_SELECT_TOURNAMENT);
		    add(form);
		
			if (isAdmin() || isClubAdmin()) {
		
		    try {
				
				int stour_id = getTournamentID(modinfo);
				if (stour_id > 0) {
					Tournament tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(stour_id);
			    	table.add(getResourceBundle().getLocalizedString("tournament.choose_new_tournament","Choose a new tournament"),1,1);
			    	table.addBreak(1, 1);
			    	table.add(getResourceBundle().getLocalizedString("tournament.selected_tournament","Selected tournament")+" : "+tournament.getName(), 1, 1);
			    	table.setAlignment(1, 1, "left");
			    }else {
				    table.add(getResourceBundle().getLocalizedString("tournament.choose_tournament","Choose a tournament"),1,1);
			    }
			
			
			    //Tournament tournament = new Tournament();
			    DropdownMenu Dropdown2 = new DropdownMenu("repps");
			    int iYear = com.idega.util.IWTimestamp.RightNow().getYear();
			    String sYear = modinfo.getParameter("view_year");
			    if (sYear != null) {
			    	iYear = Integer.parseInt(sYear);
			    }
			    
			    if (isClubAdmin()) {
			        Member member = AccessControl.getMember(modinfo);
			        int member_id = member.getID();
			        Member golfMember = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(member_id);
			        int main_union_id = golfMember.getMainUnionID();
			        Dropdown2 = getTournamentBusiness(modinfo).getDropdownOrderedByUnion(new DropdownMenu(PARAMETER_TOURNAMENT),modinfo, iYear);
			
			    }
			    else if (isAdmin()) {
			        Dropdown2 = getTournamentBusiness(modinfo).getDropdownOrderedByUnion(new DropdownMenu(PARAMETER_TOURNAMENT),modinfo, iYear);
			    }
		        Dropdown2.setMarkupAttribute("size","10");
			    table.add(Dropdown2,1,2);
			
				DropdownMenu year = new DropdownMenu("view_year");
				for (int i = 2001 ; i <= com.idega.util.IWTimestamp.RightNow().getYear(); i++) {
					year.addMenuElement(Integer.toString(i), Integer.toString(i));
				}
				year.setSelectedElement(Integer.toString(iYear));
				year.setToSubmit();
			
			    GenericButton aframButton = getButton(new SubmitButton(localize("tournament.continue","Continue")));
				table.add(year, 1, 3);
			    table.add(aframButton,2,3);
			    
			    add(getResourceBundle().getLocalizedString("tournament.select_tournament_warning","WARNING! It is not possible to work with more than one tournament at a time per computer, even though many Tournament Manager windows are opened.")); 
			    
			    }
			    catch (Exception ex) {
			        ex.printStackTrace(modinfo.getWriter());
			    }
			}else {
				table.add(getResourceBundle().getLocalizedString("tournament.no_permission","No permission"), 1, 1);		
			}
		

		}

}
