package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentController;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;

/**
 * @author gimmi
 */
public class TournamentDelete extends GolfBlock {
	public void main(IWContext modinfo)throws Exception{
		IWResourceBundle iwrb = getResourceBundle();
		String tournament_id;
	        String action = modinfo.getParameter("action");
	        Table table = new Table(2,3);
	        add(table);
	        Member member = getMember();

	        tournament_id=modinfo.getParameter("tournament_id");
	        String OK = modinfo.getParameter("OK");

	        if (tournament_id != null){

	            Tournament tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(tournament_id));

	            if(OK==null){
	                boolean permission=false;
	                if(isAdmin()){
	                  permission=true;
	                }
	                else if(isClubAdmin()){

	                    int union_id = member.getMainUnionID();
	                    int tourn_union_id=tournament.getUnionId();

	                    if(union_id==tourn_union_id){
	                      permission=true;
	                    }
	                    else{
	                      permission=false;
	                    }
	                  }

	                if(permission){

	                  table.add(iwrb.getLocalizedString("tournament.are_you_sure_you_want_to_del","Are you sure you want to delete this tournament")+" "+tournament.getName()+"?");
	                  table.addBreak();
	                  table.add(iwrb.getLocalizedString("tournament.if_the_tournament_is_del_all_will_del","NB! If the tournament is deleted all members will be unregistered from it"));
	                  Form form = new Form();
	                  table.add(form);
	                  GenericButton button = getButton(new SubmitButton(iwrb.getLocalizedString("yes","Yes") ,iwrb.getLocalizedString("yes","Yes")));
	                  form.add(button);
	                  form.add(new Parameter("tournament_id",tournament_id));
	                  form.add(new Parameter("OK","OK"));
	                }
	                else{
	                  table.add(iwrb.getLocalizedString("tournament.no_permission_to_delete","you do not have permission to delete this tournament")+" "+tournament.getName()+".",1,1);
	                  table.add(new CloseButton(iwrb.getLocalizedString("tournament.close","close")),1,2);
	                }

	            }
	            else{
	                tournament.delete();
	                table.add(iwrb.getLocalizedString("tournament.tournament","tournament")+" "+tournament.getName()+iwrb.getLocalizedString("tournament.was_deleted","was deleted"),1,1);
	                table.add(new CloseButton(iwrb.getLocalizedString("tournament.close","close")),1,2);
	                TournamentController.removeTournamentTableApplicationAttribute(modinfo);
	                getParentPage().setParentToReload();
	            }
	        }
	        else {
	            table.add(iwrb.getLocalizedString("tournament.no_tournament_selected","No tournament selected"),1,1);
	        }
	}

}
