/*
 * Created on 5.3.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author gimmi
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ChangePosition extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
	  IWResourceBundle iwrb = getResourceBundle();
	
		//getPage().setTitle(iwrb.getLocalizedString("tournament.change_position","Change position"));
	
		String mode = modinfo.getParameter("mode");
			if ( mode == null ) { mode = ""; }
	  String member_id = modinfo.getParameter("member_id");
	  String tournament_id = modinfo.getParameter("tournament_id");
	  Tournament tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(tournament_id));
	
		if ( mode.equals("") ) {
	
	    Form myForm = new Form();
	      myForm.add(new HiddenInput("mode","submit"));
	      myForm.add(new HiddenInput("member_id",member_id));
	      myForm.add(new HiddenInput("tournament_id",tournament_id));
	
			Table myTable = new Table(1,3);
				myTable.setCellpadding(3);
				myTable.setCellspacing(3);
				myTable.setAlignment(1,3,"right");
				myTable.setAlignment("center");
	      myTable.setVerticalAlignment("middle");
	
	    int tournamentPosition = tournament.getPosition(Integer.parseInt(member_id));
	
			TextInput position = new TextInput("position");
	    if ( tournamentPosition != -1 ) {
	      position.setContent(Integer.toString(tournamentPosition));
	    }
	
				myTable.addText(iwrb.getLocalizedString("tournament.enter_position","Enter position")+":",1,1);
				myTable.add(position,1,2);
				myTable.add(getButton(new SubmitButton(localize("tournament.confirm","Confirm"))),1,3);
	
			myForm.add(myTable);
	
			try {
	      add(new Text().getBreak());
	      add(myForm);
			}
	    catch (Exception e) {
	      e.printStackTrace(System.err);
	    }
		}
	
		else if ( mode.equals("submit") ) {
	
			String position = modinfo.getParameter("position");
	    int tournamentPosition = -1;
	
	    if ( position != null && position.length() > 0 ) {
	      tournamentPosition = Integer.parseInt(position);
	      if ( tournamentPosition != -1 ) {
	        tournament.setPosition(Integer.parseInt(member_id),tournamentPosition);
	      }
	    }
	
			getParentPage().close();
	
		}
	}
}
