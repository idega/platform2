/*
 * Created on 5.3.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author gimmi
 */
public class BlockStartingtime extends GolfBlock {


  public void main(IWContext modinfo)throws Exception{
  	IWResourceBundle iwrb = getResourceBundle();
    String action = modinfo.getParameter("action");
    if (action == null) action = "";

    if (action.equals("") || action.equals("getForm") ) {
      getForm(modinfo,iwrb);
    }
    else if (action.equals("block")) {
      block(modinfo,iwrb,true);
    }
    else if (action.equals("unblock")) {
      block(modinfo,iwrb,false);
    }
  }

  public void getForm(IWContext modinfo,IWResourceBundle iwrb) throws RemoteException, SQLException{
    Form form = new Form();
    Table table = new Table();
    table.setAlignment("center");

    String tournamentRoundID = modinfo.getParameter("tournament_round_id");
    form.add(new HiddenInput("tournament_round_id",tournamentRoundID));

    TournamentRound tournamentRound = null;
    try {
    		tournamentRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournamentRoundID));
    }
    catch (FinderException fe) {
    		throw new SQLException(fe.getMessage());
    }
    Tournament tournament = tournamentRound.getTournament();

    TextInput text = new TextInput("name");
    table.add(iwrb.getLocalizedString("tournament.text","Texti")+":",1,1);
    table.add(text,2,1);

    DropdownMenu start = getTournamentBusiness(modinfo).getAvailableGrupNums("start",tournamentRound.getTournament(),tournamentRound);
    DropdownMenu stop = getTournamentBusiness(modinfo).getAvailableGrupNums("stop",tournamentRound.getTournament(),tournamentRound);
    table.add(iwrb.getLocalizedString("tournament.from","Frá")+":",1,2);
    table.add(iwrb.getLocalizedString("tournament.to","Til")+":",1,3);
    table.add(start,2,2);
    table.add(stop,2,3);

    SubmitButton block = new SubmitButton("Blokka","action","block");
    SubmitButton unblock = new SubmitButton("Afblokka","action","unblock");
    table.add(block,1,4);
    table.add(unblock,2,4);

    form.add(table);
    add("<br><center>");
    add(new Text(tournament.getName()+" - Hringur "+tournamentRound.getRoundNumber()));
    add("</center><br>");
    add(form);
  }

  public void block(IWContext modinfo,IWResourceBundle iwrb,boolean block) throws NumberFormatException, RemoteException, SQLException{
    Form form = new Form();
    Table table = new Table();
    table.setAlignment("center");

    String tournamentRoundID = modinfo.getParameter("tournament_round_id");
    form.add(new HiddenInput("tournament_round_id",tournamentRoundID));

    String name = modinfo.getParameter("name");
    if ( name.length() == 0 ) name = "Frátekið";
    String start = modinfo.getParameter("start");
    String stop = modinfo.getParameter("stop");

    if ( block )
      getTournamentBusiness(modinfo).blockStartingtime(modinfo, name,Integer.parseInt(tournamentRoundID),Integer.parseInt(start),Integer.parseInt(stop));
    else
      getTournamentBusiness(modinfo).unblockStartingtime(modinfo, Integer.parseInt(tournamentRoundID),Integer.parseInt(start),Integer.parseInt(stop));

    SubmitButton back = new SubmitButton("Til baka","action","getForm");
    table.add(back);

    form.add(table);
    add("<br><br>");
    add(form);
  }


}
