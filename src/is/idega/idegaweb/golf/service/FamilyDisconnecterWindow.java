package is.idega.idegaweb.golf.service;

import is.idega.idegaweb.golf.entity.Family;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.UnionMemberInfo;

import java.io.IOException;
import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HeaderTable;
import com.idega.presentation.ui.SubmitButton;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author       Ægir
 * @version 1.0
 */

public class FamilyDisconnecterWindow extends com.idega.presentation.ui.Window{

  private final String STORE_NAME = "STOREFAMILY";
  private UnionMemberInfo uniMemInfo = null;
  private String headerText = "Slíta fjölskyldutengsl";
  private int unionId;


  public FamilyDisconnecterWindow(int memberId, int unionId)throws java.sql.SQLException, FinderException {

      setTitle("Finna fjölskyldu");
      this.unionId = unionId;
      uniMemInfo  = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(memberId).getUnionMemberInfo(unionId);
  }

  public void main(IWContext modinfo) {
      this.empty();
      add(getInputTable(modinfo));
  }

  public Form getInputTable(IWContext modinfo){
      Form form = new Form();
      form.setMethod("get");
      try {


          String strStore = modinfo.getRequest().getParameter(STORE_NAME);

          HeaderTable hTable = new HeaderTable();
          hTable.setHeaderText(headerText);

          Table table = new Table(1, 3);

          Text t = new Text("Ertu viss ?", true, false , false);
          t.setFontColor("red");
          table.setRowAlignment(1, "center");
          table.add(t, 1, 1);

          Table buttonTable = new Table(2,1);

          buttonTable.add(new SubmitButton(new Image("/pics/formtakks/skra.gif"), STORE_NAME, "storeval"), 1, 1);
          buttonTable.add(new CloseButton(new Image("/pics/formtakks/loka.gif")), 2, 1);

          table.add(buttonTable, 1, 3);
          if(strStore != null) {

              store(modinfo);
              close();
              setParentToReload();
          }
          hTable.add(table);
          form.add(hTable);
      }
      catch(Exception e) {
          System.err.println("\n\nVilla i getInputTable\n\n");
          e.printStackTrace();
      }
      return form;
  }

  private void store(IWContext modinfo)throws SQLException, IOException {

      Family family = (Family) IDOLookup.createLegacy(Family.class);
      family.insert();

      System.err.println("\n\nNý fjölsk. "+ family.getID());
      System.err.println("\n\nGamla fjölsk. "+ uniMemInfo.getFamilyId());

      uniMemInfo.setFamily(family);
      uniMemInfo.update();

      System.err.println("\n\nEftir breytingu. "+ uniMemInfo.getFamilyId());
  }

}