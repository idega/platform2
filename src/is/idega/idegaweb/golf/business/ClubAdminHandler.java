package is.idega.idegaweb.golf.business;

import is.idega.idegaweb.golf.access.LoginTable;
import is.idega.idegaweb.golf.entity.Address;
import is.idega.idegaweb.golf.entity.Group;
import is.idega.idegaweb.golf.entity.GroupHome;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.Phone;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SelectionBox;

/**
 * Title:        idegaUtil
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega margmiðlun hf.
 * @author
 * @version 1.0
 */

public class ClubAdminHandler extends Block {

  IWContext modinfo;
  int begin;
  int end;


  public ClubAdminHandler(int beginUnionID, int endUnionID) {
    begin = beginUnionID;
    end = endUnionID;
  }

  private void insertClubAdminMembers(int beginUnionID, int endUnionID)throws SQLException, IOException, FinderException {
    PrintWriter ut = modinfo.getResponse().getWriter();
    ut.print("inni");
    Group group = ((GroupHome) IDOLookup.getHomeLegacy(Group.class)).findByPrimaryKey(14);
    Group[] groups = new Group[1];
    groups[0] = group;
    Member member = null;
    Union union = null;
    Address[] address =null;

    String login;
    String password;

    ut.print("forlykkja naest : ");

    for (int i = beginUnionID; i <= endUnionID; i++) {
      try{
        member = getMember();
        union = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(i);
        if(union==null){
          ut.println("union " + i + " ==null");
        }
        member.setFirstName(union.getAbbrevation());

        //member.update();
        //address = getAddress();
        //address[0].setMember(member);
        //address[0].update();

        //MemberInsertion ins = new MemberInsertion(member, address, union, getPhone(), getMemberInfo(), groups);
        //ins.setMemberStatus("I");
        //ins.insert();

        member.setimage_id(1);

        //Family family = ((FamilyHome) IDOLookup.getHomeLegacy(Family.class)).create();
        //family.insert();
       //debug this is in union_member_info
       //member.setFamily(family);


        //member.setCardId(1);

        member.insert();
        member.addTo(group);
        member.addTo(union);



        //ut.println("<H1>MemberID:"+member.getID()+"</H1>");

        login = union.getAbbrevation();
        password = com.idega.util.StringHandler.getRandomString(8);

        LoginTable loggi = (LoginTable) IDOLookup.createLegacy(LoginTable.class);
        loggi.setMemberId(member.getID());
        loggi.setUserLogin(login);
        ut.print( member.getName() + " \t-> Notandi : " + login );
        loggi.setUserPassword(password);
        ut.println(" , Lykilord : " + password );
        loggi.insert();

      } catch (Exception e){
        ut.println("Mistokst, Union_id : " + i );
        ut.println("Union_id : " + i + "<br>");
        ut.println(e.getMessage() + "<br>");
        //e.printStackTrace(ut);
      }
    }
  }

  private static Member getMember()throws SQLException, CreateException{

    Member member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).create();

   //member.setFirstInstallmentDate(new idegaTimestamp().getSQLDate());
    member.setFirstName("");
    member.setGender("");
    member.setSocialSecurityNumber("1010101010");
    //member.setVisible(false);
    //member.setMemberNumber(-1);
    member.setLastName("- admin");
    //member.insert();

    return member;
  }

  private static Address[] getAddress()throws SQLException, CreateException{
    Address[] addressArr = new Address[1];

    Address addr = (Address) IDOLookup.createLegacy(Address.class);
    addr.setAddressType("");
    addr.setCountryId(1);
    addr.setExtraInfo("");
    addr.setSeason("");
    addr.setStreetNumber(-1);
    addr.setStreet("");
    addr.setZipcodeId(2);
    addr.insert();

    addressArr[0] = addr;

    return addressArr;
  }

  private static Phone[] getPhone()throws SQLException{
    Phone[] myPhone = new Phone[1];

    Phone phone = (Phone) IDOLookup.createLegacy(Phone.class);
    phone.setCountryId(new Integer(1));
    phone.setNumber("1");
    phone.setPhoneType("");
    //phone.insert();

    myPhone[0] = phone;

    return myPhone;
  }

  private static MemberInfo getMemberInfo()throws SQLException{
    MemberInfo info = (MemberInfo) IDOLookup.createLegacy(MemberInfo.class);
    info.setFirstHandicap(36.f);
    info.setHandicap(36.f);
    //info.insert();

    return info;
  }


  public void main(IWContext modinfo2) throws Exception{
    try{
      this.modinfo = modinfo2;
      insertClubAdminMembers(begin, end);
    }
    catch(Exception ex){
      ex.printStackTrace(modinfo2.getResponse().getWriter());
    }
  }




  public void dummyFunction(){

    Form myForm = new Form("myform");

    SelectionBox box = new SelectionBox();

    SelectionBox selections = new SelectionBox();
      selections.addElement("","");
      selections.addElement("","");
      selections.addElement("","");

//    box.addBox(selections, "west");

    myForm.add(box);


  }









}