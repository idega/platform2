/*
 * Created on 5.3.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Dismissal;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;

/**
 * @author gimmi
 */
public class Dismiss extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
	  IWResourceBundle iwrb = getResourceBundle();

		getParentPage().setTitle(iwrb.getLocalizedString("tournament.dismiss","Dismiss"));

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

	    int tournamentDismissal = tournament.getDismissal(Integer.parseInt(member_id));

	    Dismissal[] dismiss = (Dismissal[]) ((Dismissal) IDOLookup.instanciateEntity(Dismissal.class)).findAllOrdered("dismissal_id");

			DropdownMenu dismissal = new DropdownMenu("dismissal");
	    for ( int a = 0; a < dismiss.length; a++ ) {
	      dismissal.addMenuElement(dismiss[a].getID(),dismiss[a].getName());
	    }
	    if ( tournamentDismissal != -1 ) {
	      dismissal.setSelectedElement(Integer.toString(tournamentDismissal));
	    }

				myTable.addText(iwrb.getLocalizedString("tournament.reason","Reason")+":",1,1);
				myTable.add(dismissal,1,2);
				Table buttonTable = new Table(2,1);
				buttonTable.setCellpaddingAndCellspacing(0);
				buttonTable.setCellpaddingLeft(2,1,10);
				
				
				buttonTable.add(getButton(new CloseButton(localize("tournament.cancel","Cancel"))),1,1);
				buttonTable.add(getButton(new SubmitButton(localize("tournament.confirm","Confirm"))),2,1);
				
				myTable.add(buttonTable,1,3);

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

			String dismissal = modinfo.getParameter("dismissal");
	    int tournamentDismissal = 0;

	    if ( dismissal != null && dismissal.length() > 0 ) {
	      tournamentDismissal = Integer.parseInt(dismissal);
	      if ( tournamentDismissal >= 0 ) {
	        tournament.setDismissal(Integer.parseInt(member_id),tournamentDismissal);
	      }
	    }

			getParentPage().close();

		}
	}
}
