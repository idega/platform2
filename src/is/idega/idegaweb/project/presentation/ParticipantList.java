package is.idega.idegaweb.project.presentation;

import is.idega.idegaweb.project.business.ProjectBusiness;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.idega.block.staff.business.StaffFinder;
import com.idega.block.staff.business.StaffHolder;
import com.idega.block.staff.presentation.StaffPropertyWindow;
import com.idega.core.data.GenericGroup;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.business.UserGroupBusiness;
import com.idega.core.user.data.User;
import com.idega.data.IDOLegacyEntityComparator;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SelectionDoubleBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;


/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class ParticipantList extends AbstractContentList {

  private int groupId = -1;
  private boolean _editPermission = false;

  public ParticipantList() {
    super();
  }


  public String getBundleIdentifier(){
    return ProjectBusiness.IW_PROJECT_IDENTIFIER;
  }


  public synchronized Object clone(){
    ParticipantList obj = (ParticipantList)super.clone();

    obj.groupId = this.groupId;

    return obj;
  }


  public String getGroupName(){
    if(groupId != -1){
      try {
        GenericGroup gr = ((com.idega.core.data.GenericGroupHome)com.idega.data.IDOLookup.getHomeLegacy(GenericGroup.class)).findByPrimaryKeyLegacy(groupId);
        return gr.getName();
      }
      catch (Exception ex) {
        return null;
      }
    }
    return null;
  }


  public void setParticipantGroupId(int id){
    groupId = id;
  }

  private Link getAddAndRemoveGroupLink(IWContext iwc, IWResourceBundle iwrb) throws Exception{
    Link addLink = new Link(iwrb.getLocalizedString("add_remove_group","Add/Remove group"));
    addLink.setFontFace(Text.FONT_FACE_ARIAL);
    addLink.setFontSize(Text.FONT_SIZE_7_HTML_1);
    addLink.setBold();
    addLink.setWindowToOpen(ParticipantList.GroupGroupSetter.class);
    addLink.addParameter(GroupGroupSetter.PARAMETER_GROUP_ID,this.getGroupId(iwc));
    return addLink;
  }

  private Link getAddAndRemoveUserLink(IWContext iwc, IWResourceBundle iwrb) throws Exception{
    Link addLink = new Link(iwrb.getLocalizedString("add_remove_user","Add/Remove user"));
    addLink.setFontFace(Text.FONT_FACE_ARIAL);
    addLink.setFontSize(Text.FONT_SIZE_7_HTML_1);
    addLink.setBold();
    addLink.setWindowToOpen(ParticipantList.UserGroupSetter.class);
    addLink.addParameter(GroupGroupSetter.PARAMETER_GROUP_ID,this.getGroupId(iwc));
    return addLink;
  }

  private Link getAddAndRemoveGroupLinkIcon(IWContext iwc, IWBundle iwb) throws Exception{
    Image icon = iwb.getImage("add_remove_group.gif");
    Link addLink = null;
    if(icon != null){
      addLink = new Link(icon);
      addLink.setWindowToOpen(ParticipantList.GroupGroupSetter.class);
      addLink.addParameter(GroupGroupSetter.PARAMETER_GROUP_ID,this.getGroupId(iwc));
    }
    return addLink;
  }

  private Link getAddAndRemoveUserLinkIcon(IWContext iwc, IWBundle iwb) throws Exception{
    Image icon = iwb.getImage("add_remove_user.gif");
    Link addLink = null;
    if(icon != null){
      addLink = new Link(icon);
      addLink.setWindowToOpen(ParticipantList.UserGroupSetter.class);
      addLink.addParameter(GroupGroupSetter.PARAMETER_GROUP_ID,this.getGroupId(iwc));
    }
    return addLink;
  }


  public void main(IWContext iwc) throws Exception {
    _editPermission = (iwc.hasEditPermission(this) || this.isOwnerOfProject(iwc));
    Text tBreak = (Text)Text.getBreak().clone();
    tBreak.setFontSize(Text.FONT_SIZE_7_HTML_1);
    this.addAtBeginning(tBreak);

    IWBundle iwb = this.getBundle(iwc);
    IWResourceBundle iwrb = iwb.getResourceBundle(iwc);

    //this.addAtBeginning(new Text(this.getGroupName(),true,false,true));
    super.main(iwc);
    if(_editPermission){
      Table table = new Table(8,1);
      table.setHorizontalAlignment("left");
      table.setCellpadding(0);
      table.setCellpadding(0);

      table.setWidth(1,"6");
      table.setWidth(3,"6");
      table.setWidth(5,"12");
      table.setWidth(7,"6");

//      table.add(getAddAndRemoveGroupLinkIcon(iwc, iwb),2,1);
//      table.add(getAddAndRemoveGroupLink(iwc, iwrb),4,1);
//
//      table.add(getAddAndRemoveUserLinkIcon(iwc, iwb),6,1);
//      table.add(getAddAndRemoveUserLink(iwc, iwrb),8,1);

      table.add(getAddAndRemoveUserLinkIcon(iwc, iwb),2,1);
      table.add(getAddAndRemoveUserLink(iwc, iwrb),4,1);

      this.add(table);
    }
  }

  /**
   * @todo reimplement
   */
  public boolean isOwnerOfProject(IWContext iwc){
    Page p = this.getParentPage();
    if(p != null){
      try {
        return iwc.getAccessController().isOwner(p,iwc);
      }
      catch (Exception ex) {
        System.err.println(ex.getMessage());
        return false;
      }
    } else {
      return false;
    }
  }


  public int getGroupId(IWContext iwc) throws Exception{
    /**
     * @todo cach
     */
    if(groupId != -1){
      GenericGroup gr = ProjectBusiness.getProjectParticipantGroup(groupId,ProjectBusiness.getCurrentProjectId(iwc));
      if(gr != null){
        return gr.getID();
      }
    }
    return -1;
  }

  public List getEntityList(IWContext iwc) throws Exception {
    List l = null;
    int gID = getGroupId(iwc);
    if(gID != -1){
      l = UserBusiness.getUsersInGroup(gID);
    }
  //    l = UserBusiness.getUsers();



    if(l != null){
      String[] names = new String[3];
      names[0] = com.idega.core.user.data.UserBMPBean.getColumnNameFirstName();
      names[1] = com.idega.core.user.data.UserBMPBean.getColumnNameMiddleName();
      names[2] = com.idega.core.user.data.UserBMPBean.getColumnNameLastName();
      IDOLegacyEntityComparator c = new IDOLegacyEntityComparator(names);
      Collections.sort(l,c);

      l = StaffFinder.getStaffHolders(l,iwc);
    }

    return l;

  }

  public void initColumns(IWContext iwc) throws java.lang.Exception {
    this.setColumns(8);
    this.setWidth("688");
    this.setExtraRowsAtBeginning(1);
    this.setMinimumNumberOfRows(6);

    this.setRowColor(1,"#CCCC66");
    this.setLinesBetween(false);
    this.setSebraColor("#F4F4DB","#FFFFFF");

    this.setColumnWidth(1,"4");
    this.setColumnWidth(2,"200");
    this.setColumnWidth(3,"4");
    this.setColumnWidth(4,"187");
    this.setColumnWidth(5,"4");
    this.setColumnWidth(6,"75");
    this.setColumnWidth(7,"4");
    this.setColumnWidth(8,"210");

  }

  public PresentationObject getObjectToAddToColumn(int colIndex, int rowIndex, Object item, IWContext iwc, boolean beforeEntities)throws Exception{
    if(item == null){
      if(beforeEntities && (rowIndex == 1)){
        Text text = new Text();
        text.setFontFace(Text.FONT_FACE_ARIAL);
        text.setFontColor("#FFFFFF");
        text.setFontSize(Text.FONT_SIZE_7_HTML_1);
        text.setBold();

        switch (colIndex) {
          case 2:
            text.setText("Name");
            break;
          case 4:
            text.setText("Title");
            break;
          case 6:
            text.setText("Phone");
            break;
          case 8:
            text.setText("e-mail");
            break;
          default:
            return null;
        }
        return text;

      }
    } else {
        Text text = new Text("");

        StaffHolder staffHolder = (StaffHolder)item;

        switch (colIndex) {
          case 2:
            /**
             * @todo change accesscontrol
             */
            boolean staffHolderIsSuperAdmin = iwc.getAccessController().getAdministratorUser().getID() == staffHolder.getUserID();
            if(iwc.isSuperAdmin() && !staffHolderIsSuperAdmin){  //tmp
              text = new Link(staffHolder.getName());
//              ((Link)text).setWindowToOpen(StaffEditor.class);
//              ((Link)text).addParameter(StaffBusiness.PARAMETER_USER_ID,staffHolder.getUserID());
              ((Link)text).setWindowToOpen(StaffPropertyWindow.class);
              ((Link)text).addParameter(StaffPropertyWindow.PARAMETERSTRING_USER_ID, staffHolder.getUserID());
            } else {
              text.setText(staffHolder.getName());
            }
            break;
          case 4:
            if(staffHolder.getTitle() != null){
              text.setText(staffHolder.getTitle());
            }
            break;
          case 6:
            if(staffHolder.getWorkPhone() != null){
              text.setText(staffHolder.getWorkPhone());
            }
            break;
          case 8:
            if(staffHolder.getEmail() != null){
              text.setText(staffHolder.getEmail());
            }
            break;
          default:
            return null;
        }
        text.setFontSize(Text.FONT_SIZE_7_HTML_1);
        text.setFontFace(Text.FONT_FACE_ARIAL);
        return text;
    }
    return null;
  }





  public static class GroupGroupSetter extends Window {

    private static final String FIELDNAME_SELECTION_DOUBLE_BOX = "related_groups";
    private static final String PARAMETER_GROUP_ID = "ic_group_id";

    public GroupGroupSetter(){
      super("add/remove groups");
      this.setAllMargins(0);
      this.setWidth(400);
      this.setHeight(300);
      this.setBackgroundColor("#d4d0c8");
    }


    private void LineUpElements(IWContext iwc){

      Form form = new Form();

      Table frameTable = new Table(3,3);
      frameTable.setWidth("100%");
      frameTable.setHeight("100%");
      //frameTable.setBorder(1);


      SelectionDoubleBox sdb = new SelectionDoubleBox(GroupGroupSetter.FIELDNAME_SELECTION_DOUBLE_BOX,"Not in","In");

      SelectionBox left = sdb.getLeftBox();
      left.setHeight(8);
      left.selectAllOnSubmit();


      SelectionBox right = sdb.getRightBox();
      right.setHeight(8);
      right.selectAllOnSubmit();



      String stringGroupId = iwc.getParameter(GroupGroupSetter.PARAMETER_GROUP_ID);
      int groupId = Integer.parseInt(stringGroupId);
      form.addParameter(GroupGroupSetter.PARAMETER_GROUP_ID,stringGroupId);

      List directGroups = UserGroupBusiness.getGroupsContainingDirectlyRelated(groupId);

      Iterator iter = null;
      if(directGroups != null){
        iter = directGroups.iterator();
        while (iter.hasNext()) {
          Object item = iter.next();
          right.addElement(Integer.toString(((GenericGroup)item).getID()),((GenericGroup)item).getName());
        }
      }
      List notDirectGroups = UserGroupBusiness.getRegisteredGroupsNotDirectlyRelated(groupId,iwc);
      if(notDirectGroups != null){
        iter = notDirectGroups.iterator();
        while (iter.hasNext()) {
          Object item = iter.next();
          left.addElement(Integer.toString(((GenericGroup)item).getID()),((GenericGroup)item).getName());
        }
      }

      //left.addSeparator();
      //right.addSeparator();

      frameTable.setAlignment(2,2,"center");
      frameTable.add("GroupId: "+groupId,2,1);
      frameTable.add(sdb,2,2);
      frameTable.add(new SubmitButton("  Save  ","save","true"),2,3);
      frameTable.add(new CloseButton("  Cancel  "),2,3);
      frameTable.setAlignment(2,3,"right");
      form.add(frameTable);
      this.add(form);
    }

    public void main(IWContext iwc) throws Exception {


      String save = iwc.getParameter("save");
      if(save != null){
        String stringGroupId = iwc.getParameter(GroupGroupSetter.PARAMETER_GROUP_ID);
        int groupId = Integer.parseInt(stringGroupId);

        String[] related = iwc.getParameterValues(GroupGroupSetter.FIELDNAME_SELECTION_DOUBLE_BOX);

        GenericGroup group = ((com.idega.core.data.GenericGroupHome)com.idega.data.IDOLookup.getHomeLegacy(GenericGroup.class)).findByPrimaryKeyLegacy(groupId);
        List currentRelationShip = group.getParentGroups();


        if(related != null){

          if(currentRelationShip != null){
            for (int i = 0; i < related.length; i++) {
              int id = Integer.parseInt(related[i]);
              GenericGroup gr = ((com.idega.core.data.GenericGroupHome)com.idega.data.IDOLookup.getHomeLegacy(GenericGroup.class)).findByPrimaryKeyLegacy(id);
              if(!currentRelationShip.remove(gr)){
                gr.addGroup(group);
              }
            }

            Iterator iter = currentRelationShip.iterator();
            while (iter.hasNext()) {
              Object item = iter.next();
              ((GenericGroup)item).removeGroup(group);
            }

          } else{
            for (int i = 0; i < related.length; i++) {
              ((com.idega.core.data.GenericGroupHome)com.idega.data.IDOLookup.getHomeLegacy(GenericGroup.class)).findByPrimaryKeyLegacy(Integer.parseInt(related[i])).addGroup(group);
            }
          }

        }else if (currentRelationShip != null){
            Iterator iter = currentRelationShip.iterator();
            while (iter.hasNext()) {
              Object item = iter.next();
              ((GenericGroup)item).removeGroup(group);
            }
          }

        this.close();
        this.setParentToReload();
      } else {
        LineUpElements(iwc);
      }

/*
      Enumeration enum = iwc.getParameterNames();
       System.err.println("--------------------------------------------------");
      if(enum != null){
        while (enum.hasMoreElements()) {
          Object item = enum.nextElement();
          if(item.equals("save")){
            this.close();
          }
          String val[] = iwc.getParameterValues((String)item);
          System.err.print(item+" = ");
          if(val != null){
            for (int i = 0; i < val.length; i++) {
              System.err.print(val[i]+", ");
            }
          }
          System.err.println();
        }
      }
*/
    }

  } // InnerClass


  public static class UserGroupSetter extends Window{
    private static final String _PARAMETER_GROUP_ID = "ic_group_id";
    private static final String _USERS_RELATED = "related_user_ids";

    public UserGroupSetter(){
      super("add/remove users");
      this.setAllMargins(0);
      this.setWidth("744");
      this.setHeight(400);
      this.setScrollbar(true);
      //this.setBackgroundColor("#d4d0c8");
    }


    public void main(IWContext iwc) throws Exception {

      Form myForm = new Form();
      myForm.maintainParameter(_PARAMETER_GROUP_ID);

      int groupId = -1;
      try {
        groupId = Integer.parseInt(iwc.getParameter(_PARAMETER_GROUP_ID));
      }
      catch (Exception ex) {
        // do Nothing
      }


      if(iwc.getParameter("commit") != null){

        // Save

        //System.out.println("----------------------------");
        //System.out.println("users: "+iwc.getParameterValues(_USERS_RELATED));

        UserGroupBusiness.updateUsersInGroup(groupId,iwc.getParameterValues(_USERS_RELATED));

        this.setParentToReload();
        this.close();

      } else {
        UserList uList = new UserList(_USERS_RELATED);

        List lDirect = com.idega.core.user.business.UserGroupBusiness.getUsersContainedDirectlyRelated(groupId);
        Set direct = new HashSet();
        if(lDirect != null){
          Iterator iter = lDirect.iterator();
          while (iter.hasNext()) {
            User item = (User)iter.next();
            direct.add(Integer.toString(item.getGroupID()));
          }
        }
        uList.setDirectlyRelatedUserIds(direct);

        List lNotDirect = com.idega.core.user.business.UserGroupBusiness.getUsersContainedNotDirectlyRelated(groupId);
        Set notDirect = new HashSet();
        if(lNotDirect != null){
          Iterator iter2 = lNotDirect.iterator();
          while (iter2.hasNext()) {
            User item = (User)iter2.next();
            notDirect.add(Integer.toString(item.getGroupID()));
          }
        }
        uList.setRelatedUserIdsNotDirectly(notDirect);


        myForm.add(uList);

        myForm.add(new SubmitButton("commit","    OK   "));
        myForm.add(new CloseButton("  Cancel  "));
        this.add(myForm);
      }


    }


  }



  public static class UserList extends AbstractContentList {

    private String _name = null;
    private Set directlyRelatedUsers = null;
    private Set RelatedUsersNotDirectly = null;

    private UserList() {
      super();
    }

    private UserList(String name) {
      this();
      _name = name;
    }

    public synchronized Object clone(){
      UserGroupSetter obj = (UserGroupSetter)super.clone();

      return obj;
    }


    public List getEntityList(IWContext iwc) throws Exception {
      List l = null;
      /*int gID = getGroupId(iwc);
      if(gID != -1){
        l = UserBusiness.getUsersInGroup(gID);
      }*/

      l = UserBusiness.getUsers();

      if(l != null){
        String[] names = new String[3];
        names[0] = com.idega.core.user.data.UserBMPBean.getColumnNameFirstName();
        names[1] = com.idega.core.user.data.UserBMPBean.getColumnNameMiddleName();
        names[2] = com.idega.core.user.data.UserBMPBean.getColumnNameLastName();
        IDOLegacyEntityComparator c = new IDOLegacyEntityComparator(names);
        Collections.sort(l,c);

        l = StaffFinder.getStaffHolders(l,iwc);
      }

      return l;

    }

//    public void initColumns(IWContext iwc) throws java.lang.Exception {
//      this.setColumns(6);
//      this.setWidth("567");
//      this.setExtraRowsAtBeginning(1);
//
//
//      this.setColumnWidth(1,"40");
//      this.setColumnWidth(2,"200");
//      this.setColumnWidth(3,"47");
//      this.setColumnWidth(4,"50");
//      this.setColumnWidth(5,"50");
//      this.setColumnWidth(6,"180");
//
//    }


    public void initColumns(IWContext iwc) throws java.lang.Exception {
      this.setColumns(8);
      this.setWidth("728");
      this.setExtraRowsAtBeginning(1);
      this.setMinimumNumberOfRows(8);

      this.setRowColor(1,"#CCCC66");
      this.setLinesBetween(false);
      this.setSebraColor("#F4F4DB","#FFFFFF");

      this.setColumnWidth(1,"40");
      this.setColumnWidth(2,"4");
      this.setColumnWidth(3,"200");
      this.setColumnWidth(4,"4");
      this.setColumnWidth(5,"187");
      this.setColumnWidth(6,"4");
      this.setColumnWidth(7,"75");
      this.setColumnWidth(8,"4");
      this.setColumnWidth(9,"210");

    }



//    public PresentationObject getObjectToAddToColumn(int colIndex, int rowIndex, Object item, IWContext iwc, boolean beforeEntities)throws Exception{
//      if(item == null){
//        if(beforeEntities && (rowIndex == 1)){
//          Text text = new Text();
//          text.setBold();
//
//          switch (colIndex) {
//            case 1:
//              text.setText("Velja");
//              break;
//            case 2:
//              text.setText("Nafn");
//              break;
//            case 3:
//              text.setText("");
//              break;
//            case 4:
//              text.setText("Sími");
//              break;
//            case 5:
//              //text.setText("Fax");
//              break;
//            case 6:
//              text.setText("Tölvupóstur");
//              break;
//          }
//          return text;
//
//        }
//      } else {
//          Text text = new Text("");
//          text.setFontSize(Text.FONT_SIZE_7_HTML_1);
//          text.setFontFace(Text.FONT_FACE_ARIAL);
//
//          StaffHolder staffHolder = (StaffHolder)item;
//
//          switch (colIndex) {
//            case 1:
//              return getCheckBox(staffHolder.getGroupID());
//              //break;
//            case 2:
//              text.setText(staffHolder.getName());
//              break;
//            case 3:
//              text.setText("");
//              break;
//            case 4:
//              if(staffHolder.getWorkPhone() != null){
//                text.setText(staffHolder.getWorkPhone());
//              }
//              break;
//            case 5:
//              //text.setText("fax");
//              break;
//            case 6:
//              if(staffHolder.getEmail() != null){
//              text.setText(staffHolder.getEmail());
//              }
//              break;
//          }
//          return text;
//      }
//      return null;
//    }


    public PresentationObject getObjectToAddToColumn(int colIndex, int rowIndex, Object item, IWContext iwc, boolean beforeEntities)throws Exception{
      if(item == null){
        if(beforeEntities && (rowIndex == 1)){
          Text text = new Text();
          text.setFontFace(Text.FONT_FACE_ARIAL);
          text.setFontColor("#FFFFFF");
          text.setFontSize(Text.FONT_SIZE_7_HTML_1);
          text.setBold();

          switch (colIndex) {
            case 1:
              text.setText("Choose");
              break;
            case 3:
              text.setText("Name");
              break;
            case 5:
              text.setText("Title");
              break;
            case 7:
              text.setText("Phone");
              break;
            case 9:
              text.setText("e-mail");
              break;
            default:
              return null;
          }
          return text;

        }
      } else {
          Text text = new Text("");

          StaffHolder staffHolder = (StaffHolder)item;

          switch (colIndex) {
            case 1:
              return getCheckBox(staffHolder.getGroupID());
              //break;
            case 3:
              /**
               * @todo change accesscontrol
               */
//              boolean staffHolderIsSuperAdmin = iwc.getAccessController().getAdministratorUser().getID() == staffHolder.getUserID();
//              if(iwc.isSuperAdmin() && !staffHolderIsSuperAdmin){  //tmp
//                text = new Link(staffHolder.getName());
//  //              ((Link)text).setWindowToOpen(StaffEditor.class);
//  //              ((Link)text).addParameter(StaffBusiness.PARAMETER_USER_ID,staffHolder.getUserID());
//                ((Link)text).setWindowToOpen(StaffPropertyWindow.class);
//                ((Link)text).addParameter(StaffPropertyWindow.PARAMETERSTRING_USER_ID, staffHolder.getUserID());
//              } else {
                text.setText(staffHolder.getName());
//              }
              break;
            case 5:
              if(staffHolder.getTitle() != null){
                text.setText(staffHolder.getTitle());
              }
              break;
            case 7:
              if(staffHolder.getWorkPhone() != null){
                text.setText(staffHolder.getWorkPhone());
              }
              break;
            case 9:
              if(staffHolder.getEmail() != null){
                text.setText(staffHolder.getEmail());
              }
              break;
            default:
              return null;
          }
          text.setFontSize(Text.FONT_SIZE_7_HTML_1);
          text.setFontFace(Text.FONT_FACE_ARIAL);
          return text;
      }
      return null;
    }



    public CheckBox getCheckBox(int userId){
      if(RelatedUsersNotDirectly != null){
        if( RelatedUsersNotDirectly.contains(Integer.toString(userId)) || RelatedUsersNotDirectly.contains(new Integer(userId)) ){
          CheckBox box = new CheckBox(_name+"_extended");
          box.setChecked(true);
          box.setDisabled(true);
          box.setValue(userId);
          return box;
        }
      }
      CheckBox box = new CheckBox(_name);

      if(directlyRelatedUsers != null){
        if( directlyRelatedUsers.contains(Integer.toString(userId)) || directlyRelatedUsers.contains(new Integer(userId)) ){
          box.setChecked(true);
        }
      }

      box.setValue(userId);
      return box;

    }

    public void setDirectlyRelatedUserIds( Set set ){
      directlyRelatedUsers = set;
    }

    public void setRelatedUserIdsNotDirectly( Set set  ){
      RelatedUsersNotDirectly = set;
    }



  } // InnerClass UserList



}
