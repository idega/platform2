package com.idega.projects.golf;

/**
 * Title:
 * Description:
 * Copyright:    idega Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.util.*;
import java.math.*;
import java.io.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.util.*;
import com.idega.util.text.TextSoap;
import com.idega.projects.golf.*;
import com.idega.projects.golf.entity.*;
import com.idega.projects.golf.business.*;

public class MemberDevelopment extends JModuleObject {

private int unionID = -1;
private idegaTimestamp dateBefore;
private idegaTimestamp dateAfter;

  public MemberDevelopment() {
    this.unionID = 3;
    dateBefore = new idegaTimestamp(1,1,2000);
    dateAfter = new idegaTimestamp();
  }

  public MemberDevelopment(int unionID) {
    this.unionID = unionID;
    dateBefore = new idegaTimestamp(1,1,2000);
    dateAfter = new idegaTimestamp();
  }

  public MemberDevelopment(idegaTimestamp dateBefore) {
    this.unionID = 3;
    this.dateBefore = dateBefore;
    dateAfter = new idegaTimestamp();
  }

  public MemberDevelopment(idegaTimestamp dateBefore, idegaTimestamp dateAfter) {
    this.unionID = 3;
    this.dateBefore = dateBefore;
    this.dateAfter = dateAfter;
  }

  public MemberDevelopment(int unionID, idegaTimestamp dateBefore) {
    this.unionID = unionID;
    this.dateBefore = dateBefore;
    dateAfter = new idegaTimestamp();
  }

  public MemberDevelopment(int unionID, idegaTimestamp dateBefore, idegaTimestamp dateAfter) {
    this.unionID = unionID;
    this.dateBefore = dateBefore;
    this.dateAfter = dateAfter;
  }

  public void main(ModuleInfo modinfo) throws Exception {

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
      data.append("Forgjöf fyrir"); data.append("\t");
      data.append("Forgjöf eftir");data.append("\t");
      data.append("Mismunur"); data.append("\t");
      data.append("Símar"); data.append("\t");
      data.append("\n");
      out.write(data.toString().toCharArray());

      System.out.println("Starting: "+new idegaTimestamp().getTimestampRightNow().toString());
      Union union = new Union(unionID);
      List memberList = null;
      if ( unionID == 3 ) {
        memberList = union.getAllActiveMembers();
      }
      else {
        memberList = union.getMembersInUnion();
      }

      Collections.sort(memberList,new GenericMemberComparator(GenericMemberComparator.FIRSTLASTMIDDLE));

      if ( memberList != null ) {
        for ( int a = 0; a < memberList.size(); a++ ) {
          Member member = (Member) memberList.get(a);

          float handicapBefore = 0;
          float handicapAfter = 0;
          float difference = 0;

          Scorecard[] scorecardBefore = (Scorecard[]) Scorecard.getStaticInstance("com.idega.projects.golf.entity.Scorecard").findAll("select * from scorecard where scorecard_date < '"+dateBefore.toSQLDateString()+"' and scorecard_date is not null and member_id = "+Integer.toString(member.getID())+" order by scorecard_date desc");
          Scorecard[] scorecardAfter = (Scorecard[]) Scorecard.getStaticInstance("com.idega.projects.golf.entity.Scorecard").findAll("select * from scorecard where scorecard_date < '"+dateAfter.toSQLDateString()+"' and scorecard_date > '"+dateBefore.toSQLDateString()+"' and scorecard_date is not null and member_id = "+Integer.toString(member.getID())+" order by scorecard_date desc");

          if ( scorecardBefore.length > 0 ) {
            handicapBefore = scorecardBefore[0].getHandicapAfter();
          }
          else {
            MemberInfo memberInfo = new MemberInfo(member.getID());
            handicapBefore = memberInfo.getFirstHandicap();

            if ( handicapBefore > 40 ) {
              if ( scorecardAfter.length > 0 ) {
                if ( scorecardAfter[scorecardAfter.length-1].getHandicapCorrectionBoolean() ) {
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
            data.append("\t");
          }
          catch ( Exception e ) {
            e.printStackTrace(System.err);
          }

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

          c = data.toString().toCharArray();
          out.write(c);

        }

      System.out.println("Done: "+new idegaTimestamp().getTimestampRightNow().toString());

        if(c!=null)
          out.write(c);

        out.close();

        this.getParentPage().setToRedirect("/servlet/Excel?&dir="+file,1);

      }
    }
    catch(IOException io) {
      io.printStackTrace(System.err);
    }
    catch(SQLException sql) {
      sql.printStackTrace();
    }

  }

}