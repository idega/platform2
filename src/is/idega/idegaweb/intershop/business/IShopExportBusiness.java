/*
 *  $Id: IShopExportBusiness.java,v 1.2 2002/03/19 09:41:09 palli Exp $
 *
 *  Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 *
 */
package is.idega.idegaweb.intershop.business;

import com.idega.idegaweb.IWApplicationContext;

import is.idega.idegaweb.intershop.data.IShopTemplate;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Clob;
import java.util.Properties;
import java.util.Enumeration;

import com.idega.util.FileUtil;
import com.strengur.idegaweb.intershop.business.IShopXMLDesc;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */

public class IShopExportBusiness {
  private static IShopExportBusiness _instance = null;

  private IShopExportBusiness() {
  }

  public static IShopExportBusiness getInstance() {
    if (_instance == null)
      _instance = new IShopExportBusiness();

    return(_instance);
  }

  public boolean exportPage(IShopTemplate page, Properties dbProps, String html, IWApplicationContext iwac) {
    try {
      IShopXMLDesc desc = new IShopXMLDesc(iwac);//.getApplication().getBundle("com.strengur.idegaweb.intershop"));
//      System.out.println("OS create = " + desc.hasCreatePermissions("OS"));
//      System.out.println("OS update = " + desc.hasModifyPermissions("OS"));
//      System.out.println("OS delete = " + desc.hasDeletePermissions("OS"));

      Enumeration e = desc.getAvailableTemplateClasses();
      while (e.hasMoreElements()) {
        String key = (String)e.nextElement();
        System.out.println("key = " + key);
//        TemplateProps template = (TemplateProps)t.get(key);
//        System.out.println(key + " = " + template);
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }

    return(true);
  }

/*    Class.forName("com.sybase.jdbc2.jdbc.SybDriver").newInstance();
      Connection conn = DriverManager.getConnection("jdbc:sybase:Tds:" + ipad + ":5000?ServiceName=" + dbName, "sa", "intershop");

      StringBuffer sql = new StringBuffer("SELECT * FROM ");
      sql.append(dbName);
      sql.append("..");
      sql.append(IShopTemplate.ISHOP_TABLE_NAME);
      sql.append(" where id = 'pallitest'");

      Statement stmt = conn.createStatement();
      ResultSet res = stmt.executeQuery(sql.toString());

      res.close();
      stmt.close();

      /*
       *  String sql = "update trainingstoredb..templates set content = 'Þetta er smá test' where id = 'pallitest'";
       *  Statement stmt = conn.createStatement();
       *  int res = stmt.executeUpdate(sql);
       *  System.out.println("res = " + res);
       *  stmt.close();
       *  conn.close();
       */
/*    }
    catch (Exception e) {
      System.out.println("error: " + e.getMessage());
      e.printStackTrace();
    }*/
}