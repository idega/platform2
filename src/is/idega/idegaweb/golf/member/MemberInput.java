package is.idega.idegaweb.golf.member;



import is.idega.idegaweb.golf.member.Service;

import com.idega.presentation.*;

import com.idega.presentation.ui.*;

import com.idega.presentation.text.*;

import is.idega.idegaweb.golf.entity.*;

import is.idega.idegaweb.golf.service.*;

import com.idega.jmodule.image.presentation.ImageInserter;

import java.sql.SQLException;



public class MemberInput extends Input{



  public static PresentationObject getMemberTable(GolfMemberProfile profile) {

    Member eMember = null;

    boolean m = false;

    if(profile.bHasMember){

      eMember = profile.getMember();

      m = true;

    }

    String sname = m?eMember.getName():"";

    TextInput name = new TextInput("name",sname);

    HiddenInput hname = new HiddenInput("hname",sname);



    String sSsn = m?eMember.getSocialSecurityNumber():"";

    TextInput ssn = new TextInput("ssn",sSsn);

    ssn.setMaxlength(10);

    HiddenInput hssn = new HiddenInput("hssn",sSsn);



    String sGender = m?eMember.getGender():"M";

    DropdownMenu gender = drpGender("gender",sGender);

    HiddenInput hgender = new HiddenInput("hgender",sGender);



    String sEmail = m?eMember.getEmail():"";

    TextInput email = new TextInput("email",sEmail);

    HiddenInput hemail = new HiddenInput("hemail",sEmail);



    String sJob = m?eMember.getEmail():"";

    TextInput job = new TextInput("job",sJob);

    HiddenInput hjob = new HiddenInput("hjob",sJob);



    String sWork = m?eMember.getWorkPlace():"";

    TextInput work = new TextInput("work",sWork);

    HiddenInput hwork = new HiddenInput("hwork",sWork);



    Table table = new Table(2, 6);



    setStyle(name);

    setStyle(ssn);

    setStyle(gender);

    setStyle(email);

    setStyle(job);

    setStyle(work);



    table.add(bodyText("Nafn"), 1, 1);

    table.add(name,2,1);

    table.add(hname,2,1);

    table.add(bodyText("Kennitala"), 1, 2);

    table.add(ssn,2,2);

    table.add(hssn,2,2);

    table.add(bodyText("Kyn"), 1, 3);

    table.add(gender,2,3);

    table.add(hgender,2,3);

    table.add(bodyText("Netfang"), 1, 4);

    table.add(email,2,4);

    table.add(hemail,2,4);

    table.add(bodyText("Starf"), 1, 5);

    table.add(job,2,5);

    table.add(hjob,2,5);

    table.add(bodyText("Vinna"), 1, 6);

    table.add(work,2,6);

    table.add(hwork,2,6);



    return table;

  }



  public void MemberUpdate(IWContext iwc,GolfMemberProfile profile){

    Member eMember = null;

    boolean m = false;

    if(profile.bHasMember){

      eMember = profile.getMember();

      m = true;

    }

    String sName = iwc.getParameter("name").trim();

    String hName = iwc.getParameter("hname").trim();

    boolean bName = sName.equalsIgnoreCase(hName)?false:true;

    String sSsn = iwc.getParameter("ssn").trim();

    String hSsn = iwc.getParameter("hssn").trim();

    boolean bSsn = sSsn.equalsIgnoreCase(hSsn)?false:true;

    String sGender = iwc.getParameter("gender").trim();

    String hGender = iwc.getParameter("hgender").trim();

    boolean bGender = sGender.equalsIgnoreCase(hGender)?false:true;

    String sEmail = iwc.getParameter("email").trim();

    String hEmail = iwc.getParameter("hemail").trim();

    boolean bEmail = sEmail.equalsIgnoreCase(hEmail)?false:true;

    String sJob = iwc.getParameter("job").trim();

    String hJob = iwc.getParameter("hjob").trim();

    boolean bJob = sJob.equalsIgnoreCase(hJob)?false:true;

    String sWork = iwc.getParameter("work").trim();

    String hWork = iwc.getParameter("hwork").trim();

    boolean bWork = sWork.equalsIgnoreCase(hWork)?false:true;



}

 public static DropdownMenu drpGender(String name, String selected) {

    DropdownMenu drp = new DropdownMenu(name);

    drp.addMenuElement("M", "KK");

    drp.addMenuElement("F", "KVK");

    drp.setSelectedElement(selected);

    return drp;

  }

}
