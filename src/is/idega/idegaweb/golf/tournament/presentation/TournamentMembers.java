package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.MemberInfoHome;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentGroup;
import is.idega.idegaweb.golf.entity.TournamentGroupHome;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.member.GenericMemberComparator;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import java.util.Collections;
import java.util.List;

import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.data.SimpleQuerier;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * @author gimmi
 */
public class TournamentMembers extends GolfBlock {

	private IWResourceBundle iwrb;

  public void main(IWContext modinfo) throws Exception {
    try {
      iwrb = getResourceBundle();

      String tournamentID = modinfo.getParameter("tournament_id");
      String tournamentGroupID = modinfo.getParameter("tournament_group_id");

      Tournament tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(tournamentID));
      IWTimestamp tournStart = new IWTimestamp(tournament.getStartTime());

      Form myForm = new Form();
        myForm.add(new HiddenInput("tournament_id",tournamentID));

      Table myTable = new Table(1,2);
        myTable.setAlignment(1,1,"right");
        myTable.setWidth("100%");

      TournamentGroup[] group = tournament.getTournamentGroups();

      DropdownMenu dropdown = new DropdownMenu("tournament_group_id");
      for ( int a = 0; a < group.length; a++ ) {
        dropdown.addMenuElement(group[a].getID(),group[a].getName());
      }

      if ( tournamentGroupID != null ) {
        dropdown.setSelectedElement(tournamentGroupID);
      }
      else {
        if ( group.length > 0 ) {
          tournamentGroupID = Integer.toString(group[0].getID());
          dropdown.setSelectedElement(tournamentGroupID);
        }
      }

      if ( tournamentGroupID != null ) {
        TournamentGroup tournamentGroup = ((TournamentGroupHome) IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKey(Integer.parseInt(tournamentGroupID));

        SubmitButton submit = new SubmitButton("Sækja");

        myTable.add(dropdown,1,1);
        myTable.add(submit,1,1);

        List memberList = EntityFinder.findAll((Member) IDOLookup.instanciateEntity(Member.class),"select m.* from member m, tournament_member tm where m.member_id = tm.member_id and tm.tournament_id = "+tournamentID+" and tournament_group_id = "+tournamentGroupID);

        Table memberTable = new Table();
          memberTable.setWidth("100%");

        if ( memberList != null ) {
          Collections.sort(memberList,new GenericMemberComparator(GenericMemberComparator.FIRSTLASTMIDDLE));
          int rows = memberList.size();
          memberTable.setColumns(4);
          memberTable.setRows(rows+3);
          memberTable.mergeCells(1,1,4,1);
          memberTable.mergeCells(1,rows+3,4,rows+3);
          memberTable.add("<hr size=\"1\" noshade align=\"left\" width=\"150\">"+iwrb.getLocalizedString("tournament.number_of_contestants","Number of contestants")+": "+rows,1,rows+3);

          Text groupText = new Text(tournamentGroup.getName());
            groupText.setBold();
            groupText.setFontSize(3);
          Text memberText = new Text(iwrb.getLocalizedString("tournament.golfer","Golfer"));
            memberText.setBold();
            memberText.setFontSize(2);
          Text sscText = new Text(iwrb.getLocalizedString("tournament.social_security_number","Social-security"));
            sscText.setBold();
            sscText.setFontSize(2);
          Text handicapText = new Text(iwrb.getLocalizedString("tournament.handicap","Handicap"));
            handicapText.setBold();
            handicapText.setFontSize(2);

          memberTable.add(groupText,1,1);
          memberTable.add(memberText,2,2);
          memberTable.add(sscText,3,2);
          memberTable.add(handicapText,4,2);

          for ( int a = 0; a < memberList.size(); a++ ) {
            Member member = (Member) memberList.get(a);

            Text numberText = new Text(Integer.toString(a+1));
              numberText.setFontSize(2);
            Text nameText = new Text(member.getName());
              nameText.setFontSize(2);

            memberTable.add(numberText,1,a+3);
            memberTable.add(nameText,2,a+3);

            String ssc = member.getSocialSecurityNumber();
            if ( ssc.length() == 10 ) {
              ssc = ssc.substring(0,6)+"-"+ssc.substring(6,ssc.length());
            }

            Text securityText = new Text(ssc);
              securityText.setFontSize(2);

            memberTable.add(securityText,3,a+3);

            String sql = "select handicap_after from scorecard where member_id = "+Integer.toString(member.getID())+" and scorecard_date < '"+tournStart.toSQLDateString()+"' order by scorecard_date desc";

            String[] handicap = SimpleQuerier.executeStringQuery(sql);

            Text hcpText = new Text("-");
              hcpText.setFontSize(2);
              if ( handicap.length > 0 ) {
                hcpText.setText(TextSoap.singleDecimalFormat(handicap[0]));
              }
              else {
                MemberInfo memberInfo = ((MemberInfoHome) IDOLookup.getHomeLegacy(MemberInfo.class)).findByPrimaryKey(member.getID());
                hcpText.setText(TextSoap.singleDecimalFormat(Float.toString(memberInfo.getFirstHandicap())));
              }

            memberTable.add(hcpText,4,a+3);
          }

          memberTable.setColumnAlignment(1,"center");
          memberTable.setColumnAlignment(3,"center");
          memberTable.setColumnAlignment(4,"center");
          memberTable.setAlignment(1,rows+3,"left");
          memberTable.setAlignment(1,1,"left");
        }

        myTable.add(memberTable,1,2);
      }
      else {
        getParentPage().close();
      }
      myForm.add(myTable);
      add(myForm);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

}
