package is.idega.idegaweb.golf;

/**
 * Title:
 * Description:
 * Copyright:    idega Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.MemberInfoHome;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.entity.UnionMemberInfo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.idega.data.IDOLookup;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

public class MemberDevelopment extends Block {

private int unionID = -1;
private IWTimestamp dateBefore;
private IWTimestamp dateAfter;
private boolean onlyActive = false;
private boolean onlyInActive = false;
private boolean phones = false;

  public MemberDevelopment() {
    this.unionID = 3;
    dateBefore = new IWTimestamp(1,1,2000);
    dateAfter = new IWTimestamp();
  }

  public MemberDevelopment(int unionID) {
    this.unionID = unionID;
    dateBefore = new IWTimestamp(1,1,2000);
    dateAfter = new IWTimestamp();
  }

  public MemberDevelopment(IWTimestamp dateBefore) {
    this.unionID = 3;
    this.dateBefore = dateBefore;
    dateAfter = new IWTimestamp();
  }

  public MemberDevelopment(IWTimestamp dateBefore, IWTimestamp dateAfter) {
    this.unionID = 3;
    this.dateBefore = dateBefore;
    this.dateAfter = dateAfter;
  }

  public MemberDevelopment(int unionID, IWTimestamp dateBefore) {
    this.unionID = unionID;
    this.dateBefore = dateBefore;
    dateAfter = new IWTimestamp();
  }

  public MemberDevelopment(int unionID, IWTimestamp dateBefore, IWTimestamp dateAfter) {
    this.unionID = unionID;
    this.dateBefore = dateBefore;
    this.dateAfter = dateAfter;
  }

  public void main(IWContext modinfo) throws Exception {

    String fileSeperator = System.getProperty("file.separator");
    String filepath = modinfo.getServletContext().getRealPath(fileSeperator+"files"+fileSeperator);
    String fileName = "handicap_report.xls";

    try{
      String file = filepath+fileName;
      FileWriter out = new FileWriter(file);

      char[] c  = null;

      StringBuffer data = new StringBuffer();
      data.append("Kylfingur"); data.append("\t");
      data.append("Kennitala"); data.append("\t");
      data.append("Fæðingardagur"); data.append("\t");
      data.append("Kyn"); data.append("\t");
      data.append("Virkur/Óvirkur"); data.append("\t");
      data.append("Skráður"); data.append("\t");
      data.append("Forgjöf fyrir"); data.append("\t");
      data.append("Forgjöf eftir");data.append("\t");
      data.append("Mismunur"); data.append("\t");
      data.append("\n");
      out.write(data.toString().toCharArray());

      System.out.println("Starting: "+new IWTimestamp().getTimestampRightNow().toString());
      Union union = ((UnionHome)IDOLookup.getHome(Union.class)).findByPrimaryKey(unionID);
      List memberList = null;
      if ( unionID == 3 ) {
        memberList = union.getAllActiveMembers();
      }
      else {
        if ( this.onlyActive )
          memberList = union.getActiveMembers();
        else if ( this.onlyInActive )
          memberList = union.getInActiveMembers();
        else
          memberList = union.getAllMembersInUnion();
      }

      //Collections.sort(memberList,new GenericMemberComparator(GenericMemberComparator.FIRSTLASTMIDDLE));

      if ( memberList != null ) {
        for ( int a = 0; a < memberList.size(); a++ ) {
          try {
            Member member = (Member) memberList.get(a);

            float handicapBefore = 0;
            float handicapAfter = 0;
            float difference = 0;

            Scorecard[] scorecardBefore = (Scorecard[]) ((Scorecard)IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where scorecard_date < '"+dateBefore.toSQLDateString()+"' and scorecard_date is not null and member_id = "+Integer.toString(member.getID())+" order by scorecard_date desc");
            Scorecard[] scorecardAfter = (Scorecard[]) ((Scorecard)IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where scorecard_date < '"+dateAfter.toSQLDateString()+"' and scorecard_date > '"+dateBefore.toSQLDateString()+"' and scorecard_date is not null and member_id = "+Integer.toString(member.getID())+" order by scorecard_date desc");

            if ( scorecardBefore.length > 0 ) {
              handicapBefore = scorecardBefore[0].getHandicapAfter();
            }
            else {
              MemberInfo memberInfo = ((MemberInfoHome)IDOLookup.getHome(MemberInfo.class)).findByPrimaryKey(member.getID());
              handicapBefore = memberInfo.getFirstHandicap();

              if ( handicapBefore > 40 ) {
                if ( scorecardAfter.length > 0 ) {
                  if ( scorecardAfter[scorecardAfter.length-1].getHandicapCorrection() ) {
                    handicapBefore = scorecardAfter[scorecardAfter.length-1].getHandicapAfter();
                  }
                }
              }
            }

            if ( scorecardAfter.length > 0 ) {
              handicapAfter = scorecardAfter[0].getHandicapAfter();
            }
            else {
              handicapAfter = handicapBefore;
            }

            if ( handicapBefore > 40 ) {
              if ( member.getGender().equalsIgnoreCase("m") ) {
                handicapBefore = 36;
              }
              if ( member.getGender().equalsIgnoreCase("f") ) {
                handicapBefore = 40;
              }
            }

            if ( handicapAfter > 40 ) {
              if ( member.getGender().equalsIgnoreCase("m") ) {
                handicapAfter = 36;
              }
              if ( member.getGender().equalsIgnoreCase("f") ) {
                handicapAfter = 40;
              }
            }

            difference = handicapBefore - handicapAfter;
            data = new StringBuffer();
            String s = "";

            try {
              s = member.getName(); // Name
              data.append(s);
              data.append("\t");
            }
            catch ( Exception e ) {
              e.printStackTrace(System.err);
            }

            try {
              s = member.getSocialSecurityNumber(); // SocialSecurityNumber
              if ( s.length() == 10 ) {
                s = s.substring(0,6)+"-"+s.substring(6,s.length());
              }
              data.append(s);
              data.append("\t");
            }
            catch ( Exception e ) {
              e.printStackTrace(System.err);
            }

            try {
              if ( member.getDateOfBirth() != null ) {
                s = member.getDateOfBirth().toString();
                data.append(s);
              }
              data.append("\t");
            }
            catch ( Exception e ) {
              e.printStackTrace(System.err);
            }

            try {
              if ( member.getGender() != null ) {
                s = member.getGender();
                data.append(s);
              }
              data.append("\t");
            }
            catch ( Exception e ) {
              e.printStackTrace(System.err);
            }

            try {
              UnionMemberInfo umi = member.getUnionMemberInfo(unionID);
              if ( umi != null ) {
                s = umi.getMemberStatus();
                if ( s.equalsIgnoreCase("A") ) {
                  s = "Virkur";
                }
                else {
                  s = "Óvirkur";
                }
                data.append(s);
              }
              data.append("\t");

              if ( umi != null ) {
                if ( umi.getRegistrationDate() != null )
                  s = (new IWTimestamp(umi.getRegistrationDate())).toSQLDateString();
                else
                  s = "";
                data.append(s);
              }
              data.append("\t");
            }
            catch ( Exception e ) {
              e.printStackTrace(System.err);
            }

            try {
              s = TextSoap.singleDecimalFormat((double)handicapBefore); // HandicapBefore
              s = s.replace('.',',');
              data.append(s);
              data.append("\t");
            }
            catch ( Exception e ) {
              e.printStackTrace(System.err);
            }

            try {
              s = TextSoap.singleDecimalFormat((double)handicapAfter); // HandicapAfter
              s = s.replace('.',',');
              data.append(s);
              data.append("\t");
            }
            catch ( Exception e ) {
              e.printStackTrace(System.err);
            }

            try {
              s = TextSoap.singleDecimalFormat((double)difference); // Difference
              s = s.replace('.',',');
              data.append(s);
              data.append("\n");
            }
            catch ( Exception e ) {
              e.printStackTrace(System.err);
            }

            /*if ( phones ) {
              try {
                Phone[] phones = member.getPhone();
                for ( int z = 0; z < phones.length; z++ ) {
                  s = phones[z].getNumber();
                  data.append(s);
                  if ( z + 1 < phones.length ) {
                     data.append("\t");
                  }
                  else {
                    data.append("\n");
                  }
                }
                if ( phones.length == 0 ) {
                  data.append("\n");
                }
              }
              catch ( Exception e ) {
                e.printStackTrace(System.err);
              }
            }*/

            c = data.toString().toCharArray();
            out.write(c);
          }
          catch (Exception e) {
            e.printStackTrace(System.err);
          }
        }

      System.out.println("Done: "+new IWTimestamp().getTimestampRightNow().toString());

        if(c!=null)
          out.write(c);

        out.close();

        this.getParentPage().setToRedirect("/servlet/Excel?&dir="+file,1);

      }
    }
    catch(IOException io) {
      io.printStackTrace(System.err);
    }

  }

  public void setOnlyActive() {
    this.onlyActive=true;
  }

  public void setOnlyInActive() {
    this.onlyActive=true;
  }

  public void setPhones() {
    this.phones=true;
  }

}