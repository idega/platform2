package is.idega.idegaweb.campus.presentation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DataTable;
import com.idega.util.database.ConnectionBroker;

/**
 * Title
 * Description: Shows number of habitants in the campus system
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserve
 * Company:      ideg
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

 public class CampusHabitants extends CampusBlock {

  
  public CampusHabitants() {

  }

  public String getBundleIdentifier(){

    return IW_BUNDLE_IDENTIFIER;

  }

  private String getSQL(){
    StringBuffer sql = new StringBuffer();
    sql.append(" select x.name,b.name,count(t.child_app_applicant_id)");
    sql.append(" from  app_applicant_tree t,bu_complex x ,");
    sql.append(" cam_contract c,bu_building b,bu_floor f,bu_apartment p");
    sql.append(" where c.app_applicant_id = t.app_applicant_id");
    sql.append(" and c.bu_apartment_id = p.bu_apartment_id");
    sql.append(" and p.bu_floor_id = f.bu_floor_id");
    sql.append(" and f.bu_building_id = b.bu_building_id");
    sql.append(" and b.bu_complex_id = x.bu_complex_id");
    sql.append(" and c.rented = 'Y'");
    sql.append(" group by x.name,b.name");
    //sql.append(" order by b.bu_complex_id");
    return sql.toString();
  }

  public PresentationObject createResultTable(IWContext iwc)throws SQLException{
    DataTable T = new DataTable();
      T.addTitle(localize("tenant_count","Tenant count"));
      T.setTitlesHorizontal(true);
      T.setWidth("50%");
      int row = 1;
      int col = 1;
      T.add(getHeader(localize("campus","Campus")),col++,row);
      T.add(getHeader(localize("building","Building")),col++,row);
      T.add(getHeader(localize("habitants","Habitants")),col++,row);
      row++;

        Connection conn= null;
        Statement Stmt= null;
        String[] theReturn= null;
        try {
            conn= getConnection();
            Stmt= conn.createStatement();
            ResultSet RS= Stmt.executeQuery(getSQL());
            int total = 0;
            int subtotal = 0;
            while (RS.next()) {
              col = 1;
              T.add(getText(RS.getString(1)),col++,row);
              T.add(getText(RS.getString(2)),col++,row);
              subtotal = RS.getInt(3);
              T.add(getText(String.valueOf(subtotal)),col++,row);
              row++;
              total += subtotal;
            }
            col = 2;
            T.add(getText(localize("total","Total")),col++,row);
            T.add(getText(String.valueOf(total)),col,row);
            RS.close();

        }
        finally {
            if (Stmt != null) {
                Stmt.close();
            }
            if (conn != null) {
                freeConnection(conn);
            }
        }
      T.getContentTable().setColumnAlignment(3,"right");
    return T;
  }

  public void main(IWContext iwc)throws SQLException{
    add(createResultTable(iwc));
  }
  
  public Connection getConnection()
  {
	  return ConnectionBroker.getConnection();
  }
  public void freeConnection(Connection conn)
  {
	  ConnectionBroker.freeConnection(conn);
  }
}





