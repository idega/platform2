//package insert;
package com.idega.projects.golf.service.member;
import com.idega.projects.golf.entity.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.util.*;
import com.idega.util.text.*;
import java.util.*;
import java.sql.Date;
import java.sql.*;
import java.io.*;

import com.idega.jmodule.object.*;

import com.idega.jmodule.object.textObject.*;
import com.idega.projects.golf.*;
import com.idega.util.*;
import com.idega.data.*;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

  public class GolfMemberInsert {

      private EntityInsert[] entityInsert;
      private AddressInsert addrInsert;
      private AddressInsert addrInsert2;
      private MemberInsert memberInsert;
      private PhoneInsert homePhoneInsert;
      private PhoneInsert gsmInsert;
      private PhoneInsert workPhoneInsert;
      private UnionMemberInfoInsert unionMemberInsert;
      private CardInsert cardInsert;
      private GroupInsert groupInsert;
      private MemberInfoInsert memInfoInsert;
      private Form form = new Form();
      private ModuleInfo modinfo;
      private Image memberImg = new Image("/pics/member/x.gif");
      private Image uppi = new Image("/pics/member/uppi.gif");
      private Image info = new Image("/pics/member/upplysingar.gif");
      private Vector vError = new Vector();
      private String strSessionId = "error";
      private String strErrorPage = "membererror.jsp";
      private String strUnionId = null;
      private Member member;
      private String imageId;
      private boolean isUpdate;

      public GolfMemberInsert(ModuleInfo modinfo, int unionId)throws SQLException {
          this.modinfo = modinfo;
          isUpdate = false;
          //modinfo.getSession().removeAttribute("image_id");
          strUnionId = String.valueOf(unionId);
          addrInsert = new AddressInsert(modinfo, "heimili1", "heimililand1", "postnr1");
          addrInsert2 = new AddressInsert(modinfo, "heimili2", "heimililand2", "postnr2");
          memberInsert = new MemberInsert(modinfo);
          homePhoneInsert = new PhoneInsert(modinfo, "simi", "similand1", "homephonetype");
          gsmInsert = new PhoneInsert(modinfo, "gsm", "similand2", "gsmphonetype");
          workPhoneInsert = new PhoneInsert(modinfo, "vinnusimi", "similand3", "workphonetype");
          unionMemberInsert = new UnionMemberInfoInsert(modinfo);
          unionMemberInsert.setUnionId(unionId);
          cardInsert = new CardInsert(modinfo);
          memInfoInsert = new MemberInfoInsert(modinfo);
          setEntityInsert();
          homePhoneInsert.allowEmpty(true);
          gsmInsert.allowEmpty(true);
          workPhoneInsert.allowEmpty(true);
          addrInsert.allowEmpty(true);
          addrInsert2.allowEmpty(true);
          groupInsert = new GroupInsert(modinfo, false);

      }

      public GolfMemberInsert(ModuleInfo modinfo, int memberId, int unionId)throws SQLException {
          this.modinfo = modinfo;
          //modinfo.getSession().removeAttribute("image_id");
          isUpdate = true;
          strUnionId = String.valueOf(unionId);
          member = new Member(memberId);
          Address[] addressArr = member.getAddress();
          int addressLenght = addressArr.length;
          Phone[] phoneArr = member.getPhone();
          int phoneLength = phoneArr.length;
          int unionMemIfnId = -1;
          int memberInfId = -1;
          int cardId = -1;

          if(member.getUnionMemberInfo(String.valueOf(unionId), String.valueOf(memberId)) != null)
              unionMemIfnId = member.getUnionMemberInfo(String.valueOf(unionId), String.valueOf(memberId)).getID();
          if(member.getMemberInfo() != null)
              memberInfId  = member.getMemberInfo().getID();

          if(modinfo.getSessionAttribute("image_id") != null)
              memberImg = new Image(Integer.parseInt( ((String) modinfo.getSessionAttribute("image_id")) ));
          if(member.getImageId() != 1)
              memberImg = new Image(member.getImageId());


          memberInsert = new MemberInsert(modinfo, member.getID());
          if(addressLenght > 0) {
              addrInsert = new AddressInsert(modinfo, addressArr[0].getID(), "heimili1", "heimililand1", "postnr1");
          }
          else {
              addrInsert = new AddressInsert(modinfo, "heimili1", "heimililand1", "postnr1");
              //addrInsert = new AddressInsert(modinfo);
          }
          if(addressLenght > 1)
              addrInsert2 = new AddressInsert(modinfo, addressArr[1].getID(), "heimili2", "zip2", "addresscountry2");
          else
              addrInsert2 = new AddressInsert(modinfo, "heimili2", "zip2", "addresscountry2");

          if(phoneLength > 0)
              homePhoneInsert = new PhoneInsert(modinfo, phoneArr[0].getID(), "simi", "similand1", "homephonetype");
          else
              homePhoneInsert = new PhoneInsert(modinfo, "simi", "similand1", "homephonetype");
          if(phoneLength > 1)
              gsmInsert = new PhoneInsert(modinfo, phoneArr[1].getID(), "gsm", "similand2", "gsmphonetype");
          else
              gsmInsert = new PhoneInsert(modinfo, "gsm", "similand2", "gsmphonetype");
          if(phoneLength > 2)
              workPhoneInsert = new PhoneInsert(modinfo, phoneArr[2].getID(), "vinnusimi", "similand3", "workphonetype");
          else
              workPhoneInsert = new PhoneInsert(modinfo, "vinnusimi", "similand3", "workphonetype");
          if(unionMemIfnId != -1) {
              unionMemberInsert = new UnionMemberInfoInsert(modinfo, unionMemIfnId, memberId, unionId);
          }
          else {
              unionMemberInsert = new UnionMemberInfoInsert(modinfo);
          }
          if(memberInfId != -1) {
              memInfoInsert = new MemberInfoInsert(modinfo, memberInfId);
          }
          else {
              memInfoInsert = new MemberInfoInsert(modinfo);
          }

          cardId = unionMemberInsert.getUnionMemberInfo().getCardId();
          if(cardId != 1) {
              cardInsert = new CardInsert(modinfo, cardId);
          }
          else {
              cardInsert = new CardInsert(modinfo);
          }

          unionMemberInsert.setUnionId(unionId);
          unionMemberInsert.setMemberId(memberId);
          groupInsert = new GroupInsert(modinfo, member, false);
          setEntityInsert();
          homePhoneInsert.allowEmpty(true);
          gsmInsert.allowEmpty(true);
          workPhoneInsert.allowEmpty(true);
          addrInsert.allowEmpty(true);
          addrInsert2.allowEmpty(true);

      }


      public Form showInputForm()throws IOException, SQLException {


        if(modinfo.getRequest().getParameter("cmd") != null && modinfo.getRequest().getParameter("cmd").equalsIgnoreCase("submit"))
            handleError();


        imageId = (String) modinfo.getSession().getAttribute("image_id");


        if(imageId != null) {
            memberImg = new Image(Integer.parseInt( imageId));
            modinfo.getSession().removeAttribute("image_id");
        }
        else {
          if( member != null && member.getImageId() != 1)
              memberImg = new Image(member.getImageId());
          else
              memberImg = new Image("/pics/member/x.gif");
        }

        memberImg.setWidth(110);

        //debug

       // form.setMethod("get");

        if(member != null)
            form.setAction(modinfo.getRequest().getRequestURI()+"?cmd=submit&image_id="+imageId+"&member_id="+member.getID());
        else
            form.setAction(modinfo.getRequest().getRequestURI()+"?cmd=submit&image_id="+imageId);

        Window ImageWindow = new Window("imageWindow",500,400,"/image/insertimage.jsp?");
        Link imageChange = new Link(memberImg,ImageWindow);
        imageChange.addParameter("submit","new");
        imageChange.addParameter("member_id",modinfo.getRequest().getParameter("update_member_id"));

        Table table = new Table(4, 5);
        table.setAlignment("center");
        table.setCellpadding(0);
        table.setCellspacing(0);

        table.setVerticalAlignment(1,3,"top");
        table.setVerticalAlignment(2,3,"top");
        table.setVerticalAlignment(3,3,"top");
        table.setVerticalAlignment(3,4,"top");
        table.setVerticalAlignment(2,4,"top");
        table.setVerticalAlignment(4,3,"top");
        table.setVerticalAlignment(2,5,"top");

        HeaderTable memberTable = getMemberTable();
        HeaderTable addressTable = getAddressTable();
        HeaderTable phoneTable = getPhoneTable();
        HeaderTable cardTable = getCardTable();
        HeaderTable groupTable = getGroupTable();
        HeaderTable unionMemInfTable = getUnionMemberInfoTable();
        HeaderTable memberInfoTable = getMemberInfoTable();
        HeaderTable familyTable = getFamilyTable();

        unionMemInfTable.setLeftHeader(false);

        memberTable.setColor("#CEDFD0");
        groupTable.setColor("#CEDFD0");
        addressTable.setColor("#CEDFD0");
        phoneTable.setColor("#CEDFD0");
        cardTable.setColor("#CEDFD0");
        unionMemInfTable.setColor("#CEDFD0");
        memberInfoTable.setColor("#CEDFD0");

        memberTable.setWidth(200);
        addressTable.setWidth(200);
        phoneTable.setWidth(200);
        cardTable.setWidth(200);
        unionMemInfTable.setWidth(200);
        memberInfoTable.setWidth(200);

        memberTable.setHeight(200);
        addressTable.setHeight(200);
        phoneTable.setHeight(200);
        cardTable.setHeight(200);
        unionMemInfTable.setHeight(200);
        memberInfoTable.setHeight(200);



        phoneTable.setRightHeader(false);
        phoneTable.setLeftHeader(false);
        addressTable.setRightHeader(false);
        addressTable.setLeftHeader(false);
        memberTable.setRightHeader(false);

        //table.add(new Link(new Image("",);

        table.add(imageChange, 1, 3);

        table.add("&nbsp", 1, 3);

        table.add(memberInfoTable, 2, 4);
        table.add(memberTable, 2, 3);
        table.add(addressTable, 3, 3);
        table.add(familyTable, 3, 3);
        table.add(phoneTable, 2, 4);
        table.add(cardTable, 3, 4);
        table.add(unionMemInfTable, 4, 3);

        table.add(groupTable, 4, 4);


        table.add(new SubmitButton(new Image("/pics/form_takkar/vista.gif")), 4, 5);
        table.setColor("#FFFFFF");
        table.setCellspacing(0);
        form.add(table);
	//add(form);
        return form;
      }

      private void setEntityInsert() {
          entityInsert = new EntityInsert[9];
          entityInsert[0] = addrInsert;
          entityInsert[1] = addrInsert2;
          entityInsert[2] = memberInsert;
          entityInsert[3] = homePhoneInsert;
          entityInsert[4] = gsmInsert;
          entityInsert[5] = workPhoneInsert;
          entityInsert[6] = unionMemberInsert;
          entityInsert[7] = cardInsert;
          entityInsert[8] = memInfoInsert;
      }

      private void handleError()throws IOException, SQLException {
          for(int i = 0; i < entityInsert.length; i++) {
              vError.addAll(entityInsert[i].getNeetedEmptyFields());
          }
          if(vError.size() > 0) {
              modinfo.getSession().setAttribute(strSessionId, vError);
              modinfo.getResponse().sendRedirect(strErrorPage);
          }
          else insert();

      }

      private void insert()throws SQLException, IOException {

              Union union = new Union(Integer.parseInt(this.strUnionId));

              //modinfo.getWriter().print("before im id: "+memberInsert.getMember().getImageId());
              imageId = modinfo.getRequest().getParameter("image_id");
              if((imageId != null) && (! imageId.equals("null"))) {
                  memberInsert.getMember().setImageId(Integer.parseInt(imageId));
              }
             // memberInsert.getMember().setFamilyId(1);
              memberInsert.store();
              member = memberInsert.getMember();

              if(member.getFamilyId() == -1 && modinfo.getSession().getAttribute("family_id") == null) {
                  Family family = new Family();
                  //family.setUnionId(Integer.parseInt(strUnionId));
                  family.insert();
                  unionMemberInsert.getUnionMemberInfo().setFamily(family);
              }
              else if(modinfo.getSession().getAttribute("family_id") != null) {
                  String familyId = (String) modinfo.getSession().getAttribute("family_id");
                  unionMemberInsert.getUnionMemberInfo().setFamilyId(new Integer(familyId));
              }


              memInfoInsert.setMemberId(member.getID());
              memInfoInsert.store();
              homePhoneInsert.store(member);
              gsmInsert.store(member);
              workPhoneInsert.store(member);
              addrInsert.store(member);
              addrInsert2.store(member);
              cardInsert.store();
              unionMemberInsert.setMemberId(member.getID());
              unionMemberInsert.setCardId(cardInsert.getCard().getID());
              unionMemberInsert.store();
              groupInsert.store(member);


              if(member.isMemberInUnion())
                  member.addTo(union, "membership_type", "sub");
              else
                  member.setMainUnion(union);


              if(modinfo.getSession().getAttribute("image_id") != null) {
                  String imId = (String) modinfo.getSession().getAttribute("image_id");
                  member.setimage_id(Integer.parseInt(imId));
                  modinfo.getSession().removeAttribute("image_id");
              }

              modinfo.getResponse().sendRedirect(modinfo.getRequest().getRequestURI()+"?&member_id="+member.getID());

      }

      private void checkEmptyFields(String[] strArr) {
          for (int i = 0; i < strArr.length; i++) {
              vError.add(strArr[i]);
          }

      }

      public HeaderTable getAddressTable() {
          HeaderTable hTable = new HeaderTable();
          hTable.setHeaderText("Heimili");
          Table table = new Table(2, 6);
          hTable.add(table);

          table.add("Heimili", 1, 1);
          table.add("Póstnúmer", 1, 2);
          table.add("Land", 1, 3);

          table.add("Heimili 2", 1, 4);
          table.add("Póstnúmer", 1, 5);
          table.add("Land", 1, 6);

          table.add(addrInsert.getInputAddress(), 2, 1);
          table.add(addrInsert.getDropZipcode(), 2, 2);
          table.add(addrInsert.getDropCountry(), 2, 3);

          table.add(addrInsert2.getInputAddress(), 2, 4);
          table.add(addrInsert2.getDropZipcode(), 2, 5);
          table.add(addrInsert2.getDropCountry(), 2, 6);

          return hTable;
      }

      public HeaderTable getPhoneTable() {

          HeaderTable hTable = new HeaderTable();
          hTable.setHeaderText("Sími");
          Table table = new Table(2, 3);
          hTable.add(table);

         /*table.add("Tegund", 1, 1);
          table.add("Númer", 1, 2);

          table.add("Tegund", 1, 3);
          table.add("Númer", 1, 4);

          table.add("Tegund", 1, 5);
          table.add("Númer", 1, 6);*/

          homePhoneInsert.getInputPhoneNumber().setSize(11);
          workPhoneInsert.getInputPhoneNumber().setSize(11);
          gsmInsert.getInputPhoneNumber().setSize(11);

          table.add(homePhoneInsert.getDropType(), 1, 1);
          table.add(homePhoneInsert.getInputPhoneNumber(), 2, 1);

          table.add(workPhoneInsert.getDropType(), 1, 2);
          table.add(workPhoneInsert.getInputPhoneNumber(), 2, 2);

          table.add(gsmInsert.getDropType(), 1, 3);
          table.add(gsmInsert.getInputPhoneNumber(), 2, 3);

          table.setCellpadding(3);

          return hTable;
      }

      public HeaderTable getCardTable() {
          return cardInsert.getInputTable();
      }

      public HeaderTable getMemberTable() {
          return memberInsert.getInputTable();
      }

      public HeaderTable getUnionMemberInfoTable() {
          return unionMemberInsert.getInputTable();
      }

      public HeaderTable getMemberInfoTable() {
          return this.memInfoInsert.getInputTable();
      }

      public HeaderTable getGroupTable() {
          return this.groupInsert.getInputTable(false);
      }

      public HeaderTable getFamilyTable() throws SQLException{
          Window winFindFamily = new Window("Finna", 600, 600, "memberfamily.jsp?");
          Link linkFindFamily = new Link(new Image("/pics/flipar_takkar/form_takkar/finna.gif"),winFindFamily);


          Window winNewFamily = new Window("Nýskrá", 290, 127, "newfamily.jsp?");
          Link linkNewFamily = new Link(new Image("/pics/flipar_takkar/form_takkar/slitafra.gif"),winNewFamily);
          winNewFamily.setResizable(false);
          winNewFamily.setScrollbar(false);

          HeaderTable familyTable = new HeaderTable();
          familyTable.setHeaderText("Fjölskylda");
          UnionMemberInfo uniMem = null;
          if(member != null) {
              uniMem = member.getUnionMemberInfo(strUnionId);
              if(uniMem!=null)  {
                  UnionMemberInfo[] uniarr = ( UnionMemberInfo[])uniMem.findAll("select * from union_member_info where family_id="+uniMem.getFamilyId());
                  Vector vector = new Vector();
                  for( int k=0; k<uniarr.length ; k++){
                    if(member != null && uniarr[k].getMemberID() != member.getID())
                    vector.addElement(new Member(  uniarr[k].getMemberID() ));
                  }

                  Table familyInnerTable =  new Table(1, vector.size());
                  for (int i = 0; i < vector.size(); i++) {
                    familyInnerTable.add(new Link( ((Member)vector.elementAt(i)).getName(), modinfo.getRequestURI()+"?member_id="+( (Member)vector.elementAt(i) ).getID()),1,i+1);
                  }

                  familyTable.add(familyInnerTable);
              }
          }
          familyTable.add(linkNewFamily);
          familyTable.add(linkFindFamily);

          return familyTable;
        }

  }
