//package insert;
package is.idega.idegaweb.golf.service.member;

import is.idega.idegaweb.golf.entity.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.Table;
import com.idega.presentation.IWContext;
import java.util.Vector;
import java.sql.SQLException;
import java.io.IOException;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */


public class GroupInsert extends EntityInsert {


  private final String selectGroupsName = "group_";
  private Member eMember = null;
  private SelectionBox selectGroups;
  private String headerText = "Flokkar";

  private String[] selectGroupsValues = new String[0];

  public GroupInsert( boolean isAdmin) {
    bUpdate = false;
    try {
        Group group = new Group();
        Group[] groupArr = null;

        if(isAdmin)
            groupArr = (Group[]) group.findAll();
        else
            groupArr = (Group[]) group.findAll("select * from group_ where group_type = 'union_group'");
        selectGroups = new SelectionBox(groupArr);
        selectGroups.setHeight(10);
    }
    catch(SQLException e) {
        System.out.println("Villa i GroupInsert(IWContext iwc) "+e.getMessage());
        e.printStackTrace();
    }
  }

  public GroupInsert( Member mem, boolean isAdmin)throws java.sql.SQLException {
    bUpdate = true;
    eMember = mem;
    Group group = new Group();
    Group[] groupArr = null;

    if(isAdmin)
        groupArr = (Group[]) group.findAll();
    else
        groupArr = (Group[]) group.findAll("select * from group_ where group_type = 'union_group'");
    selectGroups = new SelectionBox(groupArr);
    selectGroups.setHeight(10);
    groupArr = eMember.getGroups();
    if(groupArr != null) {
      for(int i = 0; i < groupArr.length; i++) {
          selectGroups.setSelectedElement(String.valueOf(groupArr[i].getID()));
      }
    }
  }

  public boolean areNeededFieldsEmpty(IWContext iwc) {
      return false;
  }

  public Vector getNeededEmptyFields(IWContext iwc) {
      return this.getEmptyFields();
  }

  public SelectionBox getSelectionGroups() {
      return this.selectGroups;
  }

  public boolean areAllFieldsEmpty() {
      return false;
  }

  public boolean areSomeFieldsEmpty(IWContext iwc) {
      return false;
  }

  public Vector getEmptyFields() {
      return new Vector();
  }

  public BorderTable getInputTable(boolean submitButton) {

    Table table = null;
    BorderTable hTable = new BorderTable();
    if(submitButton) {
      table = new Table(1, 2);
      table.add(new SubmitButton("Vista"), 1, 2);
    }
    else
      table = new Table(1, 1);
    hTable.add(table);
    table.add(selectGroups, 1, 1);
    return hTable;
  }

  public void store(IWContext iwc,Member mem)throws SQLException, IOException {
    setVariables(iwc);
    eMember = mem;
    if(selectGroupsValues == null)
        return;
    for(int i = 0; i < selectGroupsValues.length; i++) {
        eMember.addTo(new Group(Integer.parseInt(selectGroupsValues[i])));
    }
  }

  public void store(IWContext iwc)throws SQLException, IOException {
      setVariables(iwc);
      if(selectGroupsValues == null)
          return;

      selectGroupsValues = removeGroupsMemberHas(selectGroupsValues);
      for(int i = 0; i < selectGroupsValues.length; i++) {
          iwc.getResponse().getWriter().print("<br>"+Integer.parseInt(selectGroupsValues[i]));
          eMember.addTo(new Group(Integer.parseInt(selectGroupsValues[i])));
      }
  }

  public void setVariables(IWContext iwc) {
      selectGroupsValues = iwc.getRequest().getParameterValues(selectGroupsName);
  }

  private String[] removeGroupsMemberHas(String[] values)throws IOException {
      if(eMember.getID() == -1)
          return values;

      Vector v = new Vector();
      Vector toReturn = new Vector();
      Group[] groupArr = null;

      try {
          groupArr = eMember.getGroups();
      }
      catch(Exception e) {
          System.out.print("Villa i GroupInsert.removeGroupsMemberHas(String values)"+e.getMessage());
          e.printStackTrace();
      }
      for (int i = 0; i < groupArr.length; i++)
          v.add(String.valueOf(groupArr[i].getID()));

      for (int i = 0; i < values.length; i++)
      {
          if(! v.contains(values[i])) {
              toReturn.add(values[i]);
          }
      }

      return (String[]) toReturn.toArray(new String[0]);
  }


}