package is.idega.idegaweb.golf.service.member;

import is.idega.idegaweb.golf.block.image.presentation.GolfImage;
import is.idega.idegaweb.golf.entity.Address;
import is.idega.idegaweb.golf.entity.Card;
import is.idega.idegaweb.golf.entity.CardHome;
import is.idega.idegaweb.golf.entity.Family;
import is.idega.idegaweb.golf.entity.Group;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.Phone;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.entity.UnionMemberInfo;
import is.idega.idegaweb.golf.service.FamilyDisconnecterWindow;
import is.idega.idegaweb.golf.service.FamilyInsertWindow;
import is.idega.idegaweb.golf.service.GroupMemberInsertWindow;
import is.idega.idegaweb.golf.service.GroupMemberRemoveWindow;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.media.presentation.ImageInserter;
import com.idega.data.IDOLookup;
import com.idega.presentation.Editor;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BorderTable;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

 public class GolfMemberInsert extends Editor{

  private AddressInsert addressInsert,addressInsert2;
  private MemberInsert memberInsert;
  private PhoneInsert phoneInsert1, phoneInsert2, phoneInsert3;
  private UnionMemberInfoInsert unionMemberInfoInsert;
  private CardInsert cardInsert;
  private MemberInfoInsert memberInfoInsert;
  private String sMemberImageURL = "/pics/member/x.gif";
  private Image memberImg = null;
  private Member eMember = null;
  private Union eUnion = null;
  private UnionMemberInfo eUMI = null;
  private MemberInfo eMemberInfo = null;
  private Card eCard = null;
  private Address[] eAddresses = null;
  private Phone[] ePhones = null;
  private String imageId = null;
  private boolean bUpdate;
  private int iMemberId = -1;
  private int iUnionId = -1;
  private String sAction = "member.file.action";

  public GolfMemberInsert( int iUnionId)throws SQLException {
    this.iUnionId = iUnionId;
    this.bUpdate = false;
  }

  public GolfMemberInsert( int iMemberId, int iUnionId)throws SQLException {
    this.iMemberId = iMemberId;
    this.iUnionId = iUnionId;
    this.bUpdate = true;
  }

  protected  void control(IWContext modinfo){
    try{
      entitySearch();
      init(modinfo);

      this.makeView();
      String cmd = modinfo.getParameter("cmd");
      if("submit".equalsIgnoreCase(cmd))
        insert(modinfo);

        addMain(this.showInputForm(modinfo));
          }
    catch(Exception ex){
      ex.printStackTrace();
    }

  }
  protected PresentationObject makeLinkTable(int menuNr){
    return new Text("");
  }

  private void entitySearch(){
    try{
      if(iUnionId != -1){
        eUnion = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(iUnionId);
        if(iMemberId != -1)
          try{
            eMember = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(iMemberId);
            eUMI = eMember.getUnionMemberInfo(iUnionId);
            eMemberInfo = eMember.getMemberInfo();
            eAddresses = eMember.getAddress();
            ePhones = eMember.getPhone();
            eCard = ((CardHome) IDOLookup.getHomeLegacy(Card.class)).findByPrimaryKey(eUMI.getCardId());
          }
          catch(SQLException sql){
            sql.printStackTrace();
          }
      }
    }
    catch(FinderException fe) {
    		fe.printStackTrace();
    }
  }

  private void init(IWContext modinfo)throws Exception{

    if(this.bUpdate){

      new Image(this.sMemberImageURL);
      int addressLength = this.eAddresses.length;
      int phoneLength = this.ePhones.length;

      this.memberInsert = new MemberInsert(this.eMember);

      addressInsert = addressLength > 0 ? new AddressInsert(eAddresses[0],"street1","country1","zip1"):new AddressInsert("street1","country1","zip1");
      addressInsert2 = addressLength > 1? new AddressInsert(eAddresses[1],"street2","country2","zip2"):new AddressInsert("street3","country3","zip3");

      phoneInsert1 = phoneLength > 0 ? new PhoneInsert(ePhones[0],"phone1","country1","phonetype1"): new PhoneInsert(  "phone1", "country1", "phonetype1");
      phoneInsert2 = phoneLength > 1? new PhoneInsert( ePhones[1],"phone2","country2","phonetype2"): new PhoneInsert( "phone2", "country2", "phonetype2");
      phoneInsert3 = phoneLength > 2? new PhoneInsert( ePhones[2],"phone3","country3","phonetype3"): new PhoneInsert( "phone3", "country3", "phonetype3");

      unionMemberInfoInsert = eUMI != null? new UnionMemberInfoInsert( eUMI): new UnionMemberInfoInsert(this.iUnionId);
      memberInfoInsert = this.eMemberInfo != null? new MemberInfoInsert( eMemberInfo): new MemberInfoInsert();

      if(this.eCard != null){
        int id = eCard.getID();
        cardInsert = ((id != -1) && (id!= 1) )? new CardInsert(eCard): new CardInsert();
      }
    }
    else{
      addressInsert = new AddressInsert( "heimili1", "heimililand1", "postnr1");
      addressInsert2 = new AddressInsert( "heimili2", "heimililand2", "postnr2");
      memberInsert = new MemberInsert();
      phoneInsert1 = new PhoneInsert( "phone1", "country1", "phonetype1");
      phoneInsert2 = new PhoneInsert("phone2", "country2", "phonetype2");
      phoneInsert3 = new PhoneInsert( "phone3", "country3", "phonetype3");
      unionMemberInfoInsert = new UnionMemberInfoInsert(this.iUnionId);
      cardInsert = new CardInsert();
      memberInfoInsert = new MemberInfoInsert();
    }
  }

  public PresentationObject showInputForm(IWContext modinfo)throws IOException, SQLException, FinderException {

    Form form = new Form();
    boolean isUpdating = false;
    if(modinfo.getRequest().getParameter("cmd") != null && modinfo.getRequest().getParameter("cmd").equalsIgnoreCase("submit")) {
        isUpdating = true;
    }
    imageId = (String) modinfo.getSession().getAttribute("image_id");

    if(imageId != null) {
        memberImg = new GolfImage(Integer.parseInt( imageId));
        modinfo.getSession().removeAttribute("image_id");
    }
    else {
      if( eMember != null && eMember.getImageId() != 1)
          memberImg = new GolfImage(eMember.getImageId());
      else
          memberImg = new Image("/pics/member/x.gif");
    }

    memberImg.setWidth(110);

    if(eMember != null)
        form.setAction(modinfo.getRequest().getRequestURI()+"?cmd=submit&image_id="+imageId+"&member_id="+eMember.getID());
    else
        form.setAction(modinfo.getRequest().getRequestURI()+"?cmd=submit&image_id="+imageId);


    ImageInserter imageInsert = new ImageInserter("image_id");
      imageInsert.setHasUseBox(false);
      imageInsert.setMaxImageWidth(110);
      //imageInsert.setDefaultImageURL("/pics/member/x.gif");


    Table table = new Table(7, 5);
    table.setBorder( 1);
    int firstrow = 1;
    int secondrow = 3;
    if(isUpdating) {
      Text text = new Text("Er að vista", true, true, true);
      text.setFontColor("red");
      text.setFontSize("4");
      //table.add(text, 3, 2);
    }
    //table.setAlignment("center");
    table.setCellpadding(0);
    table.setCellspacing(0);

    table.setHeight(2,"10");
    table.setHeight(4,"10");
    table.setWidth(2,"10");
    table.setWidth(4,"10");
    table.setWidth(6,"10");

    table.setRowVerticalAlignment(1,"top");
    table.setRowVerticalAlignment(3,"top");
    //table.setColumnAlignment(1,"center");
    //table.setVerticalAlignment(1,3,"bottom");

    BorderTable memberTable = getMemberTable();
    BorderTable addressTable = getAddressTable();
    BorderTable phoneTable = getPhoneTable();
    BorderTable cardTable = getCardTable();
    BorderTable groupTable = getGroupTable();
    BorderTable unionMemInfTable = getUnionMemberInfoTable();
    BorderTable memberInfoTable = getMemberInfoTable();
    BorderTable familyTable = getFamilyTable(modinfo);

    memberTable.setColor(LightColor);
    addressTable.setColor(LightColor);
    phoneTable.setColor(LightColor);
    cardTable.setColor(LightColor);
    unionMemInfTable.setColor(LightColor);
    memberInfoTable.setColor(LightColor);
    familyTable.setColor(LightColor);
    groupTable.setColor(LightColor);

    memberTable.setBorderColor(DarkColor);
    addressTable.setBorderColor(DarkColor);
    phoneTable.setBorderColor(DarkColor);
    memberInfoTable.setBorderColor(DarkColor);
    unionMemInfTable.setBorderColor(DarkColor);
    cardTable.setBorderColor(DarkColor);
    familyTable.setBorderColor(DarkColor);
    groupTable.setBorderColor(DarkColor);
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
    //memberInfoTable.setWidth(width);

    memberTable.setHeight(height);
    addressTable.setHeight(height);
    phoneTable.setHeight(height);
    cardTable.setHeight(height);
    unionMemInfTable.setHeight(height);
    //memberInfoTable.setHeight(height);

    //table.add(imageChange, 1, 3);
    table.add(imageInsert, 1, firstrow);

    table.add(formatText("Forgjöf"),1,firstrow);
    table.add(memberInfoTable, 1, firstrow);

    table.add(formatText("Félagi"),3,firstrow);
    table.add(memberTable,3,firstrow);

    table.add(formatText("Heimili"),5,firstrow);
    table.add(addressTable,5,firstrow);

    table.add(formatText("Símar"),7,firstrow);
    table.add(phoneTable,7,firstrow);

    table.add(formatText("Félagið"),5,secondrow);
    table.add(unionMemInfTable, 5,secondrow);

    table.add(formatText("Kortið"),7,secondrow);
    table.add(cardTable, 7, secondrow);

    if(iMemberId != -1) {

      table.add(formatText("Fjölskylda"),3,secondrow);
      table.add(familyTable, 3, secondrow);
      table.add(getFamilyLinkTable(),3,secondrow);
      table.add(formatText("Hópar"),3,secondrow);
      table.add(groupTable, 3, secondrow);
      table.add(getGroupLinkTable(),3,secondrow);
    }

    table.add(Text.getBreak(),1,secondrow);
    table.add(new SubmitButton(new Image("/pics/formtakks/vista.gif")), 1, secondrow);
    table.setColor("#FFFFFF");
    table.setCellspacing(0);
    form.add(table);
    return form;
  }

  private void insert(IWContext modinfo)throws SQLException, IOException {

    Union union = this.eUnion;

    if(eMember == null) {
        Family family = (Family) IDOLookup.createLegacy(Family.class);
        family.insert();
        unionMemberInfoInsert.getUnionMemberInfo().setFamily(family);
    }
    imageId = modinfo.getRequest().getParameter("image_id");
    if((imageId != null) && (! imageId.equals("null"))) {
        memberInsert.getMember().setImageId(Integer.parseInt(imageId));
    }

    memberInsert.store(modinfo);
    eMember = memberInsert.getMember();

    memberInfoInsert.setMemberId(eMember.getID());
    memberInfoInsert.store(modinfo);
    phoneInsert1.store(modinfo,eMember);
    phoneInsert2.store(modinfo,eMember);
    phoneInsert3.store(modinfo,eMember);
    addressInsert.store(modinfo,eMember);
    addressInsert2.store(modinfo,eMember);
    cardInsert.store(modinfo);
    unionMemberInfoInsert.setMemberId(eMember.getID());
    int cardID = cardInsert.getCard().getID();
    if(cardID == -1)
        unionMemberInfoInsert.setCardId(1);
    else
        unionMemberInfoInsert.setCardId(cardID);

    if(eMember.isMemberInUnion()){
    //ef ekki í þessu unioni
      if(! eMember.isMemberInUnion(union) ) {
        eMember.addTo(union);
        unionMemberInfoInsert.setMembershipType("sub");
        unionMemberInfoInsert.setMemberStatus("A");
      }
    }
    else{
      eMember.addTo(union);
      unionMemberInfoInsert.setMembershipType("main");
      unionMemberInfoInsert.setMemberStatus("A");
    }

    unionMemberInfoInsert.store(modinfo);

    if(modinfo.getSession().getAttribute("image_id") != null) {
      String imId = (String) modinfo.getSession().getAttribute("image_id");
      eMember.setimage_id(Integer.parseInt(imId));
      modinfo.getSession().removeAttribute("image_id");
    }

   //modinfo.getResponse().sendRedirect(modinfo.getRequest().getRequestURI()+"?&member_id="+eMember.getID());
    //getPage().setToRedirect(modinfo.getRequest().getRequestURI()+"?&member_id="+member.getID());
  }

  public BorderTable getAddressTable() {
    BorderTable hTable = new BorderTable();
    Table table = new Table(2, 6);
    hTable.add(table);

    table.add(addressInsert.formatText("Heimili"), 1, 1);
    table.add(addressInsert.formatText("Póstnr"), 1, 2);
    table.add(addressInsert.formatText("Land"), 1, 3);

    table.add(addressInsert.formatText("Heimili 2"), 1, 4);
    table.add(addressInsert.formatText("Póstnr"), 1, 5);
    table.add(addressInsert.formatText("Land"), 1, 6);

    table.add(addressInsert.getInputAddress(), 2, 1);
    table.add(addressInsert.getDropZipcode(), 2, 2);
    table.add(addressInsert.getDropCountry(), 2, 3);

    table.add(addressInsert2.getInputAddress(), 2, 4);
    table.add(addressInsert2.getDropZipcode(), 2, 5);
    table.add(addressInsert2.getDropCountry(), 2, 6);

    return hTable;
  }

  public BorderTable getPhoneTable() {

    BorderTable hTable = new BorderTable();
    Table table = new Table(2, 3);
    hTable.add(table);

    phoneInsert1.getInputPhoneNumber().setSize(11);
    phoneInsert2.getInputPhoneNumber().setSize(11);
    phoneInsert3.getInputPhoneNumber().setSize(11);

    table.add(phoneInsert1.getDropType(), 1, 1);
    table.add(phoneInsert1.getInputPhoneNumber(), 2, 1);

    table.add(phoneInsert2.getDropType(), 1, 2);
    table.add(phoneInsert2.getInputPhoneNumber(), 2, 2);

    table.add(phoneInsert3.getDropType(), 1, 3);
    table.add(phoneInsert3.getInputPhoneNumber(), 2, 3);

    table.setCellpadding(3);

    return hTable;
  }

  public BorderTable getCardTable() {
      return cardInsert.getInputTable();
  }

  public BorderTable getMemberTable() {
      return memberInsert.getInputTable();
  }

  public BorderTable getUnionMemberInfoTable() {
      return unionMemberInfoInsert.getInputTable();
  }

  public BorderTable getMemberInfoTable() {
      return this.memberInfoInsert.getInputTable();
  }

  /*public HeaderTable getGroupTable() {
      return this.groupInsert.getInputTable(false);
  }*/

  public BorderTable getGroupTable() {
    BorderTable hTable = new BorderTable();
    //hTable.setHeaderText("Flokkar");
    try {

    if(eMember!=null){//debug eiki
        Union uni = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(iUnionId);
        Group[] groupArr = null;
        Group[] grArr = null;

        Group theGroup = (Group) IDOLookup.instanciateEntity(Group.class);
        grArr = (Group[]) theGroup.findAll("select group_.* from group_, union_group, group_member where member_id = '"+eMember.getID()+"' and group_.group_id = group_member.group_id and union_group.group_id = group_member.group_id and union_group.union_id = 3");
        groupArr = (Group[]) theGroup.findAll("select group_.* from group_, union_group, group_member where member_id = '"+eMember.getID()+"' and group_.group_id = group_member.group_id and union_group.group_id = group_member.group_id and union_group.union_id = "+uni.getID());
        groupArr = joinArrays(grArr, groupArr);

        Table table = new Table(1, groupArr.length);
        table.setAlignment("center");
        int i = 0;

        for (; i < groupArr.length; i++) {
            Text t = new Text(groupArr[i].getName(), true, false, false);
            t.setFontFace("Arial");
            t.setFontSize(2);
            table.add(t, 1, i+1);
        }
        hTable.add(table);
      }
      else {
        Table empty = new Table(1, 1);
        empty.add("Það þarf að vista áður<br>en flokkur er valinn");
        hTable.add(empty);
      }
    }
    catch(Exception e) {
        e.printStackTrace();
    }
    return hTable;
  }

  public PresentationObject getGroupLinkTable() throws SQLException, FinderException{
    Union uni = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(iUnionId);
    GroupMemberInsertWindow group = new GroupMemberInsertWindow(eMember, uni, false);
    Link linkInsertGroup = new Link(new Image("/pics/formtakks/finna.gif"), group);
    GroupMemberRemoveWindow groupRem = new GroupMemberRemoveWindow(eMember, uni, false);
    Link linkRemoveGroup = new Link(new Image("/pics/formtakks/slitafra.gif"),groupRem);
    Table T = new Table(2, 1);
    T.add(linkInsertGroup, 1, 1);
    T.add(linkRemoveGroup, 2, 1);
    return T;
  }

  public PresentationObject getFamilyLinkTable() throws SQLException, FinderException{
    Table T = new Table(2,1);
    FamilyInsertWindow findFam = new FamilyInsertWindow(iMemberId,iUnionId);
    Link linkFindFamily = new Link(new Image("/pics/formtakks/finna.gif"),findFam);
    FamilyDisconnecterWindow winNewFamily = new FamilyDisconnecterWindow(iMemberId, iUnionId);
    Link linkNewFamily = new Link(new Image("/pics/formtakks/slitafra.gif"),winNewFamily);
    T.add(linkNewFamily,1,1);
    T.add(linkFindFamily,2,1);
    return T;
  }

  public BorderTable getFamilyTable(IWContext modinfo) throws SQLException, FinderException{

    BorderTable familyTable = new BorderTable();
    UnionMemberInfo uniMem = null;
    if(eMember != null) {
      uniMem = eMember.getUnionMemberInfo(iUnionId);
      if(uniMem!=null)  {
      //debug eiki added union_id
          UnionMemberInfo[] uniarr = ( UnionMemberInfo[])uniMem.findAll("select * from union_member_info where family_id="+uniMem.getFamilyId()+" and union_id="+this.iUnionId);
          Vector vector = new Vector();
          for( int k=0; k<uniarr.length ; k++){
            if(eMember != null && uniarr[k].getMemberID() != eMember.getID())
            vector.addElement(((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(  uniarr[k].getMemberID() ));
          }

          Table familyInnerTable =  new Table(1, vector.size());
          for (int i = 0; i < vector.size(); i++) {
            familyInnerTable.add(new Link( ((Member)vector.elementAt(i)).getName(), modinfo.getRequestURI()+"?member_id="+( (Member)vector.elementAt(i) ).getID()),1,i+1);
          }

          familyTable.add(familyInnerTable);
      }
    }
    return familyTable;
  }
  public Text formatText(String s){
    Text T= new Text();
    if(s!=null){
      T= new Text(s);
      T.setFontColor(this.DarkColor);
      T.setFontSize(this.fontSize);
      T.setBold();
    }
    return T;
  }
  public Text formatText(int i){
    return formatText(String.valueOf(i));
  }

  private Group[] joinArrays(Group[] p1, Group[] p2) {
    Vector v = new Vector();

    for(int i = 0; i < p1.length; i++) {
        v.add(p1[i]);
    }
    for(int i = 0; i < p2.length; i++) {
        v.add(p2[i]);
    }

    Group[] returnEntity = new Group[v.size()];

    for(int i = 0; i < v.size(); i++) {
        returnEntity[i] = (Group) v.get(i);
    }

    return  returnEntity;
  }

  public void setMemberImageURL(String url){
    this.sMemberImageURL = url;
  }
}