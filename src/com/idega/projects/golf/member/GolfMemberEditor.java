package com.idega.projects.golf.member;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.projects.golf.entity.*;
import com.idega.projects.golf.service.*;
import com.idega.jmodule.image.presentation.ImageInserter;
import java.sql.SQLException;



public class GolfMemberEditor extends Editor{

  private GolfMemberProfile profile = null;
  private Member eMember;
  private int iUnionId;
  private final String sAction = "mb.ed.action";
  private String sActPrm = "0";
  private int iAction = 0;
  private String prefix = "mb.ed.";
  private int iImageId = -1;
  private String styleAttribute = "font-size: 8pt";
  private String sMemberImageURL = "/pics/member/x.gif";
  private int bodyFontSize = 1;
  private int fontSize = 2;

  public GolfMemberEditor(){

  }

  public GolfMemberEditor(Member eMember,int iUnionId){
    this.eMember = eMember;
    this.iUnionId = iUnionId;
    profile = new GolfMemberProfile(eMember,iUnionId);
  }

  protected void control(ModuleInfo modinfo){
    if(modinfo.getParameter(sAction) != null)
      sActPrm = modinfo.getParameter(sAction);
    else
      sActPrm = "0";
    try{
      iAction = Integer.parseInt(sActPrm);
      switch(iAction){
        case ACT1:    break;
        case ACT2: break;
        case ACT3:  break;
        case ACT4: doUpdate(modinfo); break;
      }
      doMain(modinfo);
    }
    catch(Exception e){
      e.printStackTrace();
    }


  }
  private void doMain(ModuleInfo modinfo)throws SQLException{
    add("hallo");
    add(drawTable(modinfo));
  }
  private ModuleObject drawTable(ModuleInfo modinfo) throws SQLException{
    Form form = new Form();
    ModuleObject imageObject = null;
    Image memberImg;
    if(iImageId >0) {
      memberImg = new Image(iImageId);
      memberImg.setWidth(110);
      modinfo.getSession().removeAttribute("image_id");
      imageObject = memberImg;
    }
    else if( eMember != null && eMember.getImageId() != 1){
      memberImg = new Image(eMember.getImageId());
      memberImg.setWidth(110);
      imageObject = memberImg;
    }
    else{
      ImageInserter imageInsert = new ImageInserter("image_id");
      imageInsert.setHasUseBox(false);
      imageInsert.setMaxImageWidth(110);
      imageInsert.setDefaultImageURL(sMemberImageURL);
      imageObject = imageInsert;
    }

    Table table = new Table(7, 5);
    //table.setBorder( 1);
    int firstrow = 1,secondrow = 3, thirdrow = 5;
    int firstcol = 1,secondcol = 3,thirdcol = 5, fourthcol = 7;

    table.setAlignment("center");
    table.setVerticalAlignment("top");
    table.setCellpadding(0);
    table.setCellspacing(0);

    table.setHeight(2,"10");
    table.setHeight(4,"10");
    table.setWidth(2,"10");
    table.setWidth(4,"10");
    table.setWidth(6,"10");

    table.setRowVerticalAlignment(1,"top");
    table.setRowVerticalAlignment(3,"top");
    table.setRowVerticalAlignment(5,"top");
    //table.setColumnAlignment(1,"center");
    //table.setVerticalAlignment(1,3,"bottom");

    //BorderTable memberTable = getMemberTable();
    AddressInput AI = new AddressInput();
    BorderTable addressTable = AI.getAddressTable(this.profile);
    System.err.println("hallo");
    form.add(addressTable);
    /*
    BorderTable phoneTable = getPhoneTable();
    BorderTable cardTable = getCardTable();
    BorderTable groupTable = getGroupTable();
    BorderTable unionMemInfTable = getUnionMemberInfoTable();
    BorderTable memberInfoTable = getMemberInfoTable();
    BorderTable familyTable = getFamilyTable();

    String color = LightColor;
    memberTable.setColor(color);
    addressTable.setColor(color);
    phoneTable.setColor(color);
    cardTable.setColor(color);
    unionMemInfTable.setColor(color);
    memberInfoTable.setColor(color);
    familyTable.setColor(color);
    groupTable.setColor(color);

    color = DarkColor;
    memberTable.setBorderColor(color);
    addressTable.setBorderColor(color);
    phoneTable.setBorderColor(color);
    memberInfoTable.setBorderColor(color);
    unionMemInfTable.setBorderColor(color);
    cardTable.setBorderColor(color);
    familyTable.setBorderColor(color);
    groupTable.setBorderColor(color);
    int b = 1;
    memberTable.setBorder(b);
    addressTable.setBorder(b);
    phoneTable.setBorder(b);
    memberInfoTable.setBorder(b);
    unionMemInfTable.setBorder(b);
    cardTable.setBorder(b);
    familyTable.setBorder(b);
    groupTable.setBorder(b);

    int width = 210;
    int height = 210;
    memberTable.setWidth(width);
    addressTable.setWidth(width);
    phoneTable.setWidth(width);
    cardTable.setWidth(width);
    unionMemInfTable.setWidth(width);
    familyTable.setWidth(width);
    groupTable.setWidth(width);
    memberInfoTable.setWidth("100%");

    memberTable.setHeight(height);
    addressTable.setHeight(height);
    phoneTable.setHeight(height);
    cardTable.setHeight(height);
    unionMemInfTable.setHeight(height);
    familyTable.setHeight(90);
    groupTable.setHeight(90);
    memberInfoTable.setHeight(50);

    Table T = new Table(1,4);
    T.add(headerText("Mynd"),1,1);
    T.add(imageObject,1,2);
    T.add(headerText("Forgjöf"),1,3);
    T.add(memberInfoTable,1,4);

    table.add(T, firstcol, firstrow);

    table.add(this.getUnionsTable(),firstcol,secondrow);

    table.add(headerText("Félagi"),secondcol,firstrow);
    table.add(memberTable,secondcol,firstrow);
    table.add(headerText("Heimili"),secondcol,secondrow);
    table.add(addressTable,secondcol,secondrow);
    table.add(headerText("Símar"),thirdcol,secondrow);
    table.add(phoneTable,thirdcol,secondrow);
    table.add(headerText("Félagið"),thirdcol,firstrow);
    table.add(unionMemInfTable, thirdcol,firstrow);
    table.add(headerText("Kortið"),thirdcol,thirdrow);
    table.add(cardTable, thirdcol, thirdrow);
    table.add(headerText("Fjölskylda"),secondcol,thirdrow);
    table.add(familyTable, secondcol, thirdrow);
    table.add(getFamilyLinkTable(),secondcol,thirdrow);
    table.add(headerText("Hópar"),secondcol,thirdrow);
    table.add(groupTable, secondcol, thirdrow);
    table.add(getGroupLinkTable(),secondcol,thirdrow);


    table.add(Text.getBreak(),firstcol,secondrow);
    table.add(new SubmitButton(new Image("/pics/formtakks/vista.gif")), firstcol, secondrow);
    table.setAlignment(firstcol,secondrow,"center");
    table.setColor("#FFFFFF");
    table.setCellspacing(0);
    form.add(table);*/
    return form;

  }
  private void doUpdate(ModuleInfo modinfo){

  }

  public Text headerText(String s){
    Text T= new Text();
    if(s!=null){
      T= new Text(s);
      T.setFontColor(this.DarkColor);
      T.setFontSize(this.fontSize);
      T.setBold();
    }
    return T;
  }
  public Text headerText(int i){
    return headerText(String.valueOf(i));
  }



}