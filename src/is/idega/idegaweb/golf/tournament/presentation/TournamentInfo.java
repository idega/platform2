package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.business.GolfCacher;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentController;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.jmodule.login.business.AccessControl;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;
import com.idega.util.GenericMemberComparator;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * @author gimmi
 */
public class TournamentInfo extends GolfBlock {

	public void main(IWContext modinfo)throws Exception{
		IWResourceBundle iwrb = getResourceBundle();
		IWBundle bundle = getBundle();
    String tournament_id;
    String action = modinfo.getParameter("action");
    if (action == null) {
      action = "";
    }

    tournament_id = modinfo.getParameter("tournament_id");

    if (tournament_id != null){
      Tournament tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(tournament_id));
      boolean ongoing=tournament.isTournamentOngoing();
      boolean finished=tournament.isTournamentFinished();

      Table myTable = new Table(1,2);
        myTable.setWidth("100%");
        myTable.setHeight("100%");
        myTable.setHeight(2,"100%");
        myTable.setCellpadding(0);
        myTable.setCellspacing(0);
        myTable.setAlignment(1,1,"right");
        myTable.setVerticalAlignment(1,1,"bottom");
        myTable.setVerticalAlignment(1,2,"top");
        myTable.setAlignment(1,2,"center");
        myTable.setColor(1,2,"#CEDFD0");
        myTable.addBreak(1,2);

      Text header = new Text(tournament.getName());
      header.addBreak();
      header.setFontSize(3);
      header.setBold();
      add(header);

      Image participantsImage = iwrb.getImage("tabs/participants1.gif");
      Image startingtimeImage = iwrb.getImage("tabs/teetimes1.gif");
      Image informationImage = iwrb.getImage("tabs/information1.gif");
      Image iOngoing = iwrb.getImage("tabs/scoreoverview1.gif");
      Image iFinished = iwrb.getImage("tabs/results1.gif");


        if ( (action.equalsIgnoreCase("")) || (action.equals("information")) ) {
            action = "information";
            informationImage = iwrb.getImage("tabs/information.gif");
        }
        else if  (action.equals("member_list")) {
            participantsImage = iwrb.getImage("tabs/participants.gif");
        }
        else if ( action.equalsIgnoreCase("startingtime") ) {
            startingtimeImage = iwrb.getImage("tabs/teetimes.gif");
        }
        else if ( action.equals("viewCurrentScore") ) {
          iOngoing = iwrb.getImage("tabs/scoreoverview.gif");
        }
        else if ( action.equals("viewFinalScore") ) {
          iFinished = iwrb.getImage("tabs/results.gif");
        }

      Link infoLink = new Link(informationImage);
          infoLink.addParameter("action","information");
          infoLink.addParameter("tournament_id",tournament.getID()+"");
      myTable.add(infoLink,1,1);

      Link link2 = new Link(participantsImage);
          link2.addParameter("action","member_list");
          link2.addParameter("tournament_id",tournament.getID()+"");
      myTable.add(link2,1,1);

      Link link = new Link(startingtimeImage);
          link.addParameter("action","startingtime");
          link.addParameter("tournament_id",tournament.getID()+"");
      myTable.add(link,1,1);


    Link ongoingLink = new Link(iOngoing);
        ongoingLink.addParameter("action","viewCurrentScore");
        ongoingLink.addParameter("tournament_id",tournament.getID()+"");

    if(finished){
        ongoingLink = new Link(iFinished);
        ongoingLink.addParameter("action","viewFinalScore");
        ongoingLink.addParameter("tournament_id",tournament.getID()+"");
    }

    myTable.add(ongoingLink,1,1);





        int union_id = -1;
        Union union;
        String abbrevation = "-";

        float handicap = 0;
        Text tName;
        Text abb;
        Text tHandicap;
        Text tTHandicap;

        Table table2 = new Table();
            table2.setCellpadding(1);
            table2.setCellspacing(1);
            table2.setWidth("98%");
            //table2.setHeight("100%");

        String headerColor = "#2C4E3B";
        String darkColor = "#DCEFDE";
        String lightColor = "#EAFAEC";


        if (action.equals("member_list"))  {

	      List members = TournamentController.getMembersInTournamentList(tournament);
	      Member member;

            if (members != null) {

                Text textLook = new Text("");
                  textLook.setFontSize(Text.FONT_SIZE_7_HTML_1);
                Text headerLook = new Text("");
                  headerLook.setFontColor("#FFFFFF");
                  headerLook.setFontFace(Text.FONT_FACE_VERDANA);
                  headerLook.setFontSize(Text.FONT_SIZE_7_HTML_1);
                  headerLook.setBold();

                Collections.sort(members,new GenericMemberComparator(GenericMemberComparator.FIRSTLASTMIDDLE));
                Text nameString = (Text) headerLook.clone();
                  nameString.setText(iwrb.getLocalizedString("tournament.name","Name"));
                Text handicapString = (Text) headerLook.clone();
                  handicapString.setText(iwrb.getLocalizedString("tournament.handicap","Handicap"));
                Text abbString = (Text) headerLook.clone();
                  abbString.setText(iwrb.getLocalizedString("tournament.club","Club"));

                    table2.add(nameString,1,1);
                    table2.add(abbString,2,1);
                    table2.add(handicapString,3,1);
                    table2.add(nameString,4,1);
                    table2.add(abbString,5,1);
                    table2.add(handicapString,6,1);
                    table2.setAlignment(1,1, "center");
                    table2.setAlignment(2,1, "center");
                    table2.setAlignment(3,1, "center");
                    table2.setAlignment(4,1, "center");
                    table2.setAlignment(5,1, "center");
                    table2.setAlignment(6,1, "center");
                table2.setHeight(1,"15");

                int half = (int ) (members.size()+1) / 2;
                int column = 1;
                int row = 1;

                for (int i = 0; i < members.size();  i++) {
                    if (i == half) {
                        column = 4;
                        row = 2;
                    }
                    else {++row;}
                    table2.setHeight(row,"20");
                    member = (Member) members.get(i);
//                    member = new TournamentParticipants().getTournamentParticipants(((Member) members.get(i)).getID(),tournament.getID() );

                    handicap = member.getHandicap();
                    tName = new Text(member.getName());
                      tName.setFontSize(1);

                    //union_id = member.getMainUnionID();
                    union_id = tournament.getTournamentMemberUnionId(member);
                    abbrevation = union_id + "";

                    if (union_id != -1) {
                      union = GolfCacher.getCachedUnion(union_id);
                      //union = new Union(union_id);
                      abbrevation = union.getAbbrevation();
                    }else {
                      abbrevation = "-";
                    }

                    abb = (Text) textLook.clone();
                      abb.setText(abbrevation);

                    tHandicap = (Text) textLook.clone();
                      tHandicap.setText(TextSoap.singleDecimalFormat(handicap));

                    table2.add(tName,column,row);
                    table2.add(abb,column+1,row);
                    table2.add(tHandicap,column +2,row);

                    if (member.getGender().equalsIgnoreCase("M")) {
                        if (handicap > tournament.getMaxHandicap() ) {
                            tTHandicap = (Text) textLook.clone();
                              tTHandicap.setText(" ("+tournament.getMaxHandicap()+")");
                            table2.add(tTHandicap,column +2,row);
                        }
                    }
                    else {
                        if (handicap > tournament.getFemaleMaxHandicap() ) {
                            tTHandicap = (Text) textLook.clone();
                              tTHandicap.setText(" ("+tournament.getFemaleMaxHandicap()+")");
                            table2.add(tTHandicap,column +2,row);
                        }
                    }


                    table2.setAlignment(column,row, "left");
                    table2.setAlignment(column+1,row, "center");
                    table2.setAlignment(column+2,row, "center");

                } // for ends


                half += 3;

                table2.mergeCells(1,half,7,half);
                table2.setAlignment(1,half, "left");
                Text notice = new Text(iwrb.getLocalizedString("tournament.handicap_comment",""));
                    notice.setFontSize(1);
                table2.add(notice,1,half);


                table2.setHorizontalZebraColored(darkColor,lightColor);
                table2.setRowColor(1,headerColor);


            }
            else {
                table2.add(iwrb.getLocalizedString("tournament.nobody_regstered","Nobody is registered in the tournament"));
            }
        }

        else if (action.equalsIgnoreCase("startingtime") ) {

            String tournament_round = modinfo.getParameter("tournament_round");


            Form form = TournamentController.getStartingtimeTable(modinfo, tournament,tournament_round,true,false,iwrb);
                form.maintainParameter("action");
                form.maintainParameter("tournament_id");
            table2.add(form);
            table2.setAlignment(1,1,"center");

        }
        else if (action.equals("information")) {
            table2.add(getInfo(modinfo, tournament,iwrb,bundle));
        }
        else if (action.equals("viewCurrentScore")) {
            table2.setAlignment(1,1,"left");
            try {
		String gender = modinfo.getParameter("gender");
		String t_g_id = modinfo.getParameter("tournament_group_id");
		String t_r_id = modinfo.getParameter("tournament_round_id");
		String sort = modinfo.getParameter("sort");
		String order = modinfo.getParameter("order");



              ResultsViewer result = new ResultsViewer(Integer.parseInt(tournament_id));
                result.addHiddenInput("action",action);
                result.addHiddenInput("tournament_id",tournament_id);
	result.setCacheable("ResView1_"+action+"_"+tournament_id+"_"+gender+"_"+t_g_id+"_"+t_r_id+"_"+sort+"_"+order,1800000);
              table2.add(result,1,1);
            }
            catch (Exception e) {
              e.printStackTrace(System.err);
            }
        }
        else if (action.equals("viewFinalScore")) {
            table2.setAlignment(1,1,"left");
            try {
		String gender = modinfo.getParameter("gender");
		String t_g_id = modinfo.getParameter("tournament_group_id");
		String t_r_id = modinfo.getParameter("tournament_round_id");
		String sort = modinfo.getParameter("sort");
		String order = modinfo.getParameter("order");

              ResultsViewer result = new ResultsViewer(Integer.parseInt(tournament_id));
                result.addHiddenInput("action",action);
                result.addHiddenInput("tournament_id",tournament_id);
		result.setCacheable("ResView_"+action+"_"+tournament_id+"_"+gender+"_"+t_g_id+"_"+t_r_id+"_"+sort+"_"+order,1800000);
              table2.add(result,1,1);
            }
            catch (Exception e) {
              e.printStackTrace(System.err);
            }
        }

        myTable.add(table2,1,2);
        add(myTable);

    }
    else {
        add(iwrb.getLocalizedString("tournament.no_tournament_selected","No tournament selected"));
    }
}

    public String scale_decimals(String nyForgjof,int scale) throws IOException {

            BigDecimal test2 = new BigDecimal(nyForgjof);

            String nyForgjof2 = test2.setScale(scale,5).toString();

            return nyForgjof2;

    }



	public Table getInfo(IWContext modinfo, Tournament tournament,IWResourceBundle iwrb,IWBundle iwb) throws SQLException{
	    boolean ongoing=tournament.isTournamentOngoing();
	    boolean finished=tournament.isTournamentFinished();
	
	    Union union = tournament.getUnion();
	    Field field = tournament.getField();
	
	    Table table = new Table(2,10);
	        table.setColor("#CEDFD0");
	        table.setCellpadding(5);
	        table.setCellspacing(1);
	        table.mergeCells(1,1,2,1);
	        table.mergeCells(1,8,2,8);
	        table.mergeCells(1,9,2,9);
	        table.setWidth("690");
	        table.setAlignment("center");
	        table.setAlignment(2,10,"right");
	        table.setHorizontalZebraColored("#DCEFDE","#EAFAEC");
	        table.setRowColor(1,"#2C4E3B");
	        table.setRowColor(8,"#2C4E3B");
	        table.setRowColor(7,"#CEDFD0");
	        table.setRowColor(10,"#CEDFD0");
	        table.setWidth(2,"90%");
	
	    Text name = new Text(tournament.getName());
	      name.setFontFace(Text.FONT_FACE_VERDANA);
	      name.setFontSize(Text.FONT_SIZE_10_HTML_2);
	      name.setFontColor("#FFFFFF");
	      name.setBold();
	      table.add(name,1,1);
	
	    int row = 1;
	
	    Text extraInfoText = new Text(iwrb.getLocalizedString("tournament.information","Information"),true,false,false);
	      extraInfoText.setFontFace(Text.FONT_FACE_VERDANA);
	      extraInfoText.setFontSize(Text.FONT_SIZE_10_HTML_2);
	      extraInfoText.setFontColor("#FFFFFF");
	      extraInfoText.setBold();
	      table.add(extraInfoText,1,8);
	
	    Text startDateText = new Text(iwrb.getLocalizedString("tournament.date","Date"),true,false,false);
	      startDateText.setFontSize(Text.FONT_SIZE_10_HTML_2);
	      table.add(startDateText,1,3);
	
	    Text RegDateText = new Text(iwrb.getLocalizedString("tournament.registration","Registration"),true,false,false);
	      RegDateText.setFontSize(Text.FONT_SIZE_10_HTML_2);
	      table.add(RegDateText,1,6);
	
	    Text tournamentTypeText = new Text(iwrb.getLocalizedString("tournament.arrangement","Arrangement"),true,false,false);
	      tournamentTypeText.setFontSize(Text.FONT_SIZE_10_HTML_2);
	      table.add(tournamentTypeText,1,4);
	
	    Text unionNameText = new Text(iwrb.getLocalizedString("tournament.club","Club"),true,false,false);
	      unionNameText.setFontSize(Text.FONT_SIZE_10_HTML_2);
	      table.add(unionNameText,1,2);
	
	    Text fieldNameText = new Text(iwrb.getLocalizedString("tournament.field","Field"),true,false,false);
	      fieldNameText.setFontSize(Text.FONT_SIZE_10_HTML_2);
	      table.add(fieldNameText,1,5);
	
	    IWTimestamp startStamp = new IWTimestamp(tournament.getStartTime());
	    Text startDate = new Text(startStamp.getLocaleDate(modinfo.getCurrentLocale()));
	      startDate.setFontSize(Text.FONT_SIZE_10_HTML_2);
	      table.add(startDate,2,3);
	
	    IWTimestamp firstRegStamp = new IWTimestamp(tournament.getFirstRegistrationDate());
	    IWTimestamp lastRegStamp = new IWTimestamp(tournament.getLastRegistrationDate());
	    Text RegDate = new Text(firstRegStamp.getDate()+"/"+firstRegStamp.getMonth()+"/"+firstRegStamp.getYear()+" - "+lastRegStamp.getDate()+"/"+lastRegStamp.getMonth()+"/"+lastRegStamp.getYear());
	      RegDate.setFontSize(Text.FONT_SIZE_10_HTML_2);
	      table.add(RegDate,2,6);
	
	    Text tournamentType = new Text(tournament.getTournamentType().getName());
	      tournamentType.setFontSize(Text.FONT_SIZE_10_HTML_2);
	      table.add(tournamentType,2,4);
	
	    Text unionName = new Text(union.getName() + " ("+union.getAbbrevation()+")");
	      unionName.setFontSize(Text.FONT_SIZE_10_HTML_2);
	      table.add(unionName,2,2);
	
	    Text fieldName = new Text(field.getName());
	      fieldName.setFontSize(Text.FONT_SIZE_10_HTML_2);
	      table.add(fieldName,2,5);
	
	    Text extraInfo = new Text("");
	        if (tournament.getExtraText() != null) {
	            String theExtraInfo = TextSoap.formatText(tournament.getExtraText());
	            extraInfo.setText(theExtraInfo);
	        }
	        extraInfo.setFontSize(Text.FONT_SIZE_10_HTML_2);
	
	        table.add(extraInfo,1,9);
	
	    if(AccessControl.isAdmin(modinfo) || AccessControl.isClubAdmin(modinfo)){
	        if ( (!ongoing) && (!finished) ) {
	            Form form4 = new Form(new Window(iwrb.getLocalizedString("tournament.tournament_editor","Tournament editor"),"/tournament/deletetournament.jsp"));
	            form4.add(new Parameter("tournament_id",tournament.getID()+""));
	            form4.add(new SubmitButton(getResourceBundle().getImage("buttons/delete_tournament.gif")));
	            table.add(form4,1,row);
	        }
	        /*
	        Form modifyTournamentF1 = new Form(new Window(iwrb.getLocalizedString("tournament.tournament_editor","Tournament editor"),"modifytournament.jsp"));
	        modifyTournamentF1.add(new Parameter("tournament",tournament.getID()+"" ));
	        // vantar submittakka
	        table.add(modifyTournamentF1,1,3);
	        */
	    }
	    table.setAlignment(1,row,"left");
	
	    if (TournamentController.isOnlineRegistration(tournament)) {
	          Image registerImage = getResourceBundle().getImage("buttons/skramigimotid.gif");
	          registerImage.setName(iwrb.getLocalizedString("tournament.register_me","Register me"));
	          Window window  = new Window("Skraning",700,600,"registrationForMembers.jsp");
	              window.setResizable(true);
	          Link register = new Link(registerImage,window);
	          register.addParameter("action","open");
	          register.addParameter("tournament_id",Integer.toString(tournament.getID()));
	          register.setFontSize(1);
	          table.add(register,2,3);
	    }
	
	
	
	
	
	    return table;
	
	
	}
}
