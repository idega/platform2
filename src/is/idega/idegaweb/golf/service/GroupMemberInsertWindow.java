package is.idega.idegaweb.golf.service;

import is.idega.idegaweb.golf.business.GolfCacher;
import is.idega.idegaweb.golf.entity.Group;
import is.idega.idegaweb.golf.entity.GroupHome;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Union;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HeaderTable;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */


public class GroupMemberInsertWindow extends com.idega.presentation.ui.Window{

  private Member member = null;
  private Union union = null;
  private SelectionBox selectGroups;
  private String headerText = "Setja í flokk";
  private String styleAttribute = "font-size: 8pt";

  private String[] selectGroupsValues = new String[0];

  private IWResourceBundle iwrb;

  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.golf";

  public String getBundleIdentifier(){
      return IW_BUNDLE_IDENTIFIER;
  }

  public GroupMemberInsertWindow() {

  }

  public GroupMemberInsertWindow(Member mem, Union uni, boolean isAdmin)throws java.sql.SQLException {
      construct(mem, uni, isAdmin);
      setTitle("Setja í flokk");
  }

  public SelectionBox getSelectionGroups() {
      return this.selectGroups;
  }

  public void oldmain(IWContext modinfo) {
      this.empty();
      add(getInputTable(modinfo));
  }

  public Form getInputTable(IWContext modinfo){
      Form form = new Form();
      try {

          form.setAction(modinfo.getRequest().getRequestURI()+"?cmd=save");
          HeaderTable hTable = new HeaderTable();
          hTable.setHeaderText(headerText);
          Table table = new Table(2, 2);
          table.mergeCells(1, 1, 2, 1);
          table.add(new SubmitButton("Vista"), 2, 2);
          table.add(new CloseButton("Loka"), 1, 2);
          hTable.add(table);
          table.add(selectGroups, 1, 1);
          if(modinfo.getRequest().getParameter("cmd") != null) {
              oldstore(modinfo);
              setParentToReload();
              close();
          }
          form.add(hTable);
          }
      catch(Exception e) {
          e.printStackTrace();
      }
      return form;
  }

  public void oldstore(IWContext modinfo)throws SQLException, IOException, FinderException {
      selectGroupsValues = modinfo.getRequest().getParameterValues(((Group) IDOLookup.instanciateEntity(Group.class)).getEntityName());

      selectGroupsValues = removeFrom(selectGroupsValues,member);
      for(int i = 0; i < selectGroupsValues.length; i++) {
          member.addTo(((GroupHome) IDOLookup.getHomeLegacy(Group.class)).findByPrimaryKey(Integer.parseInt(selectGroupsValues[i])));
      }
  }

  private void construct(Member mem, Union uni, boolean isAdmin) {
      try
      {
          member = mem;
          union = uni;
          Group group = (Group) IDOLookup.instanciateEntity(Group.class);
          Group[] groupArr = null;

          if(isAdmin)
              groupArr = (Group[]) group.findAll();
          else {
              //groupArr = (Group[]) group.findAll("select * from group_ where group_type = 'union_group' and group_.group_type not like 'accesscontrol'");
              //select * from group_, union_, union_group where union_.union_id = union_group.union_id and union_.union_id = 3 or union_.union_id = 81
              groupArr = getGroupArray(union.getUnionGroupsRecursive());
          }

          selectGroups = new SelectionBox(groupArr);
          selectGroups.setHeight(10);
          groupArr = member.getGroups();
          if(groupArr != null) {
            for(int i = 0; i < groupArr.length; i++) {
                selectGroups.setSelectedElement(String.valueOf(groupArr[i].getID()));
            }
          }
      }
      catch(Exception e ) {
          e.printStackTrace();
      }
  }

  public String[] removeFrom(String[] arr,Member member)throws SQLException, IOException {
      Vector vToArr = new Vector();
      Vector vCompare = new Vector();

      Group[] groupArr = member.getGroups();

      for (int i = 0; i < groupArr.length; i++) {
          vCompare.add(""+groupArr[i].getID());
      }

      for (int i = 0; i < arr.length; i++) {
          vToArr.add(arr[i]);
      }

      vToArr.removeAll(vCompare);

      return (String[]) vToArr.toArray(new String[0]);
  }

  public Group[] getGroupArray(List list) {
      Group[] groupArray = new Group[list.size()];
      for (int i = 0; i < groupArray.length; i++) {
          groupArray[i] = (Group) list.get(i);
      }
      return groupArray;
  }

  public void main(IWContext iwc) {
/*
    Enumeration enum = iwc.getParameterNames();
    while(enum.hasMoreElements()){
      String prm = (String) enum.nextElement();
      System.err.println("prm "+prm+"  val :"+iwc.getParameter(prm));
    }

*/
    if(iwc.isParameterSet("close")){
      setParentToReload();
      close();
    }
    else{
      iwrb = getResourceBundle(iwc);
      String sMemberId = iwc.getParameter("member_id");
      String sMainUnionId = iwc.getParameter("main_union");
      String sMemberUnionId = iwc.getParameter("member_union");

      Member member = null;
      try {

        int memberid = Integer.parseInt(sMemberId);
        int mainUnion = Integer.parseInt(sMainUnionId);
        int memberUnion = Integer.parseInt(sMemberUnionId);
        member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(memberid);

        if(iwc.isParameterSet("save")){
          store(iwc,member);
        }
        else if(iwc.isParameterSet("add_group")){
          int addUnion = Integer.parseInt(iwc.getParameter("add_union"));
          String name = iwc.getParameter("group_name");
          saveGroup(addUnion,name);
        }
        if(member!=null){
          add(getMemberGroupTable(mainUnion,memberUnion,member));
        }
      }
      catch (Exception ex) {add("Error"); }

    }
  }

  public void store(IWContext iwc,Member member)throws SQLException, IOException {
      String[] selectGroupsValues = iwc.getParameterValues("use_group");
      Hashtable H = null;
      if(selectGroupsValues!=null){
        H = new Hashtable(selectGroupsValues.length);
        for(int i = 0; i < selectGroupsValues.length; i++) {
          H.put(new Integer(selectGroupsValues[i]),selectGroupsValues[i]);
        }
      }


      List groups = getGroups(-1,member.getID());
      if(groups!=null){
        Iterator iter = groups.iterator();
        Group group;
        while(iter.hasNext()){
          group = (Group) iter.next();
          if(H!=null && H.containsKey(new Integer(group.getID()))){
            H.remove(new Integer(group.getID()));
          }
          else{
            member.removeFrom(group);
          }
        }
      }

      if(H!=null && !H.isEmpty()){
        Enumeration enumer = H.keys();
        while(enumer.hasMoreElements()){
          member.addTo(Group.class,((Integer) enumer.nextElement()).intValue());
        }
      }


  }

  public PresentationObject getMemberGroupTable(int MainUnion,int memberUnion,Member member){
    Table T = new Table(2,4);
    T.mergeCells(1,1,2,1);
    T.mergeCells(1,4,2,4);
    T.setAlignment(1,4,"right");
    T.setVerticalAlignment(1,2,"top");
    T.setVerticalAlignment(2,1,"top");
    T.add(member.getName(),1,1);
    T.add(getGroupTable(member,memberUnion,true,true,true),1,2);
    T.add(getGroupTable(member,MainUnion,true,false,true),2,2);
    Table T2 = new Table(5,1);
    T2.add(new SubmitButton(iwrb.getImage("buttons/close.gif"),"close"),2,1);
    T2.add(new SubmitButton(iwrb.getImage("buttons/save.gif"),"save"),4,1);
    T.add(T2,1,2);
    T.add(new HiddenInput("main_union",String.valueOf(MainUnion)));
    T.add(new HiddenInput("member_union",String.valueOf(memberUnion)));
    T.add(new HiddenInput("member_id",String.valueOf(member.getID())));
    Form F = new Form();
    F.add(T);
    return F;
  }

  public Table getGroupTable(Member member,int iUnionId,boolean admin,boolean newGroup,boolean showall){
    Table T = new Table();
    Union union = GolfCacher.getCachedUnion(iUnionId);
    int iMemberId = member!=null?member.getID():-1;
    List groups = getGroups(iUnionId,iMemberId);
    Map memberGroups = getMemberGroups(groups);
    if(showall)
      groups = getGroups(iUnionId,-1);
    boolean memberHasGroups = memberGroups!=null;
    int row = 1;
    int col = 1;

    T.add(union.getAbbrevation()+" "+iwrb.getLocalizedString("groups","Flokkar"),col,row++);
    if(groups!=null){
      java.util.Iterator iter = groups.iterator();
      Group group;
      CheckBox use;
      while(iter.hasNext()){
        group = (Group) iter.next();
        if(admin){
          use = new CheckBox("use_group",String.valueOf(group.getID()));
          if(memberHasGroups && memberGroups.containsKey(new Integer(group.getID())) ){
            use.setChecked(true);
          }
          T.add(use,col++,row);
        }
        T.add(getText(group.getName()),col,row);
        row++;
        col = 1;
      }
    }
    if(newGroup){
      TextInput GroupBox = new TextInput("group_name");
      GroupBox.setStyleAttribute(styleAttribute);
      SubmitButton addGroup = new SubmitButton(iwrb.getImage("buttons/add.gif"),"add_group");
      addGroup.setStyleAttribute(styleAttribute);
      Table nt = new Table(2,1);
      nt.add(GroupBox,1,1);
      nt.add(addGroup,2,1);
      nt.add(new HiddenInput("add_union",String.valueOf(iUnionId)));
      T.mergeCells(1,row,T.getColumns(),row);
      T.add(nt,1,row);
      //"#CEDFD0", MiddleColor = "#ADCAB1"
    }

    T.mergeCells(1,1,T.getColumns(),1);
    T.setHorizontalZebraColored("#ADCAB1","#CEDFD0");

    return T;
  }

  private Text getText(String text){
    Text t = new Text(text);
    t.setFontSize(1);
    return t;
  }

  private List getGroups(int unionId,int memberId){
    try{
      return com.idega.data.EntityFinder.findAll((Group) IDOLookup.instanciateEntity(Group.class),getUnionGroupSQL(unionId,memberId));
    }
    catch(SQLException ex){

    }
    return null;
  }

  private Map getMemberGroups(List L){
    if(L!=null){
      Hashtable H = new Hashtable();
      java.util.Iterator iter = L.iterator();
      Group group;
      while(iter.hasNext()){
        group = (Group) iter.next();
        H.put(new Integer(group.getID()),group);
      }
      return H;
    }
    return null;
  }

  private String getUnionGroupSQL(int unionId,int memberId){
    StringBuffer sql = new StringBuffer("select g.* from group_ g ");
    if(unionId > 0)
      sql.append(" ,union_group ug");
    if(memberId > 0)
      sql.append(",group_member mg ");
    sql.append(" where g.group_type = 'union_group' ");
    if(unionId > 0)
      sql.append(" and ug.group_id = g.group_id ");
    if(memberId > 0)
      sql.append("and mg.group_id = g.group_id  ");
    if(unionId > 0){
    sql.append(" and ug.union_id = ");
    sql.append(unionId);
    }
    if(memberId > 0){
      sql.append(" and mg.member_id = ");
      sql.append(memberId);
    }
    //System.err.println(sql.toString());
    return sql.toString();
  }

  private void saveGroup(int unionId,String name){
    //System.err.println("adding new group to union "+unionId);
    try  {
      Group group = (Group) IDOLookup.createLegacy(Group.class);
      group.setName(name);
      group.setGroupType("union_group");
      Union union = GolfCacher.getCachedUnion(unionId);
      group.insert();
      group.addTo(union);

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  public static Link getLink(PresentationObject object,int memberId,int memberUnionId,int mainUnionId){
    Link L = new Link(object);
    L.addParameter("main_union",mainUnionId);
    L.addParameter("member_union",memberUnionId);
    L.addParameter("member_id",memberId);
    return L;
  }

}