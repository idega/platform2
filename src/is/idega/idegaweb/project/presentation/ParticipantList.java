package is.idega.idegaweb.project.presentation;

import java.util.List;
import com.idega.core.business.UserGroupBusiness;
import com.idega.core.user.business.UserBusiness;
import com.idega.business.GenericEntityComparator;
import java.util.Collections;
import java.util.Vector;
import com.idega.core.user.data.User;
import com.idega.presentation.IWContext;
import com.idega.data.GenericEntity;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Text;
import com.idega.core.user.data.User;
import com.idega.block.staff.business.StaffFinder;
import com.idega.block.staff.business.StaffHolder;
import is.idega.idegaweb.project.business.ProjectBusiness;
import com.idega.core.data.GenericGroup;

/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class ParticipantList extends AbstractContentList {

  int groupId = -1;


  public ParticipantList() {
    super();
    groupId = 5;
  }



  public synchronized Object clone(){
    ParticipantList obj = (ParticipantList)super.clone();

    obj.groupId = this.groupId;

    return obj;
  }


  public String getGroupName(){
    if(groupId != -1){
      try {
        GenericGroup gr = new GenericGroup(groupId);
        return gr.getName();
      }
      catch (Exception ex) {
        return null;
      }
    }
    return null;
  }


  public int getGroupId(IWContext iwc) throws Exception{
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
      names[0] = User.getColumnNameFirstName();
      names[1] = User.getColumnNameMiddleName();
      names[2] = User.getColumnNameLastName();
      GenericEntityComparator c = new GenericEntityComparator(names);
      Collections.sort(l,c);

      l = StaffFinder.getStaffHolders(l,iwc);
    }

    return l;

  }

  public void initColumns(IWContext iwc) throws java.lang.Exception {
    this.setColumns(5);
    this.setWidth("527");
    this.setExtraRowsAtBeginning(1);


    this.setColumnWidth(1,"200");
    this.setColumnWidth(2,"47");
    this.setColumnWidth(3,"50");
    this.setColumnWidth(4,"50");
    this.setColumnWidth(5,"180");

  }

  public PresentationObject getObjectToAddToColumn(int colIndex, int rowIndex, Object item, IWContext iwc, boolean beforeEntities)throws Exception{
    if(item == null){
      if(beforeEntities && (rowIndex == 1)){
        Text text = new Text();
        text.setBold();

        switch (colIndex) {
          case 1:
            text.setText("Nafn");
            break;
          case 2:
            text.setText("");
            break;
          case 3:
            text.setText("Sími");
            break;
          case 4:
            text.setText("Fax");
            break;
          case 5:
            text.setText("Tölvupóstur");
            break;
        }
        return text;

      }
    } else {
        Text text = new Text("");
        text.setFontSize(Text.FONT_SIZE_7_HTML_1);
        text.setFontFace(Text.FONT_FACE_ARIAL);

        StaffHolder staffHolder = (StaffHolder)item;

        switch (colIndex) {
          case 1:
            text.setText(staffHolder.getName());
            break;
          case 2:
            text.setText("");
            break;
          case 3:
            if(staffHolder.getWorkPhone() != null){
              text.setText(staffHolder.getWorkPhone());
            }
            break;
          case 4:
            text.setText("fax");
            break;
          case 5:
            if(staffHolder.getEmail() != null){
            text.setText(staffHolder.getEmail());
            }
            break;
        }
        return text;
    }
    return null;
  }

}