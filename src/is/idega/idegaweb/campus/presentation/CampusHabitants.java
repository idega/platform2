package is.idega.idegaweb.campus.presentation;

import is.idega.idegaweb.campus.block.finance.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.util.database.ConnectionBroker;
import com.idega.data.SimpleQuerier;
import com.idega.util.text.TextFormat;
import java.sql.*;

/**
 * Title
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserve
 * Company:      ideg
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

 public class CampusHabitants extends Block {

  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";
  public final static String FRAME_NAME = "fin_rightFrame";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private TextFormat tf;


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
    sql.append(" order by b.bu_complex_id");
    return sql.toString();
  }

  public PresentationObject createResultTable(IWContext iwc)throws SQLException{
    DataTable T = new DataTable();
      T.addTitle(iwrb.getLocalizedString("tenant_count","Tenant count"));
      T.setTitlesHorizontal(true);
      T.setWidth("50%");
      int row = 1;
      int col = 1;
      T.add(tf.format(iwrb.getLocalizedString("campus","Campus"),tf.HEADER),col++,row);
      T.add(tf.format(iwrb.getLocalizedString("building","Building"),tf.HEADER),col++,row);
      T.add(tf.format(iwrb.getLocalizedString("habitants","Habitants"),tf.HEADER),col++,row);
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
              T.add(tf.format(RS.getString(1)),col++,row);
              T.add(tf.format(RS.getString(2)),col++,row);
              subtotal = RS.getInt(3);
              T.add(tf.format(subtotal),col++,row);
              row++;
              total += subtotal;
            }
            col = 2;
            T.add(tf.format(iwrb.getLocalizedString("total","Total"),tf.HEADER),col++,row);
            T.add(tf.format(String.valueOf(total),tf.HEADER),col,row);
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
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    tf = TextFormat.getInstance();
    add(createResultTable(iwc));
  }
}





