/*
 *  $Id: IShopTemplateBean.java,v 1.1 2002/03/11 22:59:27 palli Exp $
 *
 *  Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 *
 */
package is.idega.idegaweb.intershop.data;

import com.idega.data.GenericEntity;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Clob;

import com.idega.util.FileUtil;

/**
 * @author    <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version   1.0
 */
public class IShopTemplateBean implements IShopTemplate {
  private final static String ENTITY_NAME = "templates";
  private final static String ID = "id";
  private final static String CLASS = "class";
  private final static String LANGUAGE_NR = "langnr";
  private final static String CONTENT = "content";
  private final static String BYTE_CODE = "bytecode";

  /**
   * Constructor for the IShopTemplateBean object
   */
  public IShopTemplateBean() { }

  /**
   * Description of the Method
   *
   * @param argv  Description of the Parameter
   */
  public static void main(String argv[]) {
//    test(argv);
    String html = FileUtil.getStringFromURL("http://www.idega.is/index.html");
    System.out.println(html);
  }

  /**
   * A unit test for JUnit
   *
   * @param argv  Description of the Parameter
   */
  public static void test(String argv[]) {
    try {
      //setting the connection driver.
      System.out.println("Connecting...");
      String databaseName = "";

      String ipAddress = "";
      Class.forName("com.sybase.jdbc2.jdbc.SybDriver").newInstance();

      if (argv.length < 2) {
        ipAddress = "localhost";
        databaseName = argv[0];
        //display ip address.
        System.out.println("IP used : " + ipAddress);
      }
      else {
        ipAddress = argv[0];
        databaseName = argv[1];

        //display ip address.
        System.out.println("IP used : " + ipAddress);
      }

      testConnection(databaseName, ipAddress);
      //System.out.println("Connected Successfully");
    }
    catch (Exception e) {
      System.out.println("Error");
      e.printStackTrace();

      System.out.println(e.getMessage());
      //printing out an error if so.
    }
  }


  /**
   * A unit test for JUnit
   *
   * @param dbName            Description of the Parameter
   * @param ipad              Description of the Parameter
   * @exception SQLException  Description of the Exception
   */
  public static void testConnection(String dbName, String ipad) throws SQLException {
    try {
      //connecting to the database using port 2638; Database name = Lab1
      Connection conn = DriverManager.getConnection("jdbc:sybase:Tds:" + ipad + ":5000?ServiceName=" + dbName, "sa", "intershop");
      System.out.println("Connected successfully to '" + dbName + "' database using JConnect");

      StringBuffer sql = new StringBuffer("SELECT * FROM ");
      sql.append(dbName);
      sql.append("..");
      sql.append(ENTITY_NAME);
      sql.append(" where id = 'pallitest'");

      Statement stmt = conn.createStatement();
      ResultSet res = stmt.executeQuery(sql.toString());

      if (res != null) {
        if (res.next()) {
          String test = res.getString("content");
          System.out.println("Content = " + test);
        }
      }

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
    }
    catch (Exception e) {
      System.out.println("error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Gets the id attribute of the IShopTemplateBean object
   *
   * @return   The id value
   */
  public String getId() {
    return (null);
  }

  /**
   * Sets the id attribute of the IShopTemplateBean object
   *
   * @param id  The new id value
   */
  public void setId(String id) {
  }

  /**
   * Gets the classId attribute of the IShopTemplateBean object
   *
   * @return   The classId value
   */
  public String getClassId() {
    return (null);
  }

  /**
   * Sets the classId attribute of the IShopTemplateBean object
   *
   * @param classId  The new classId value
   */
  public void setClassId(String classId) {
  }

  /**
   * Gets the languageNr attribute of the IShopTemplateBean object
   *
   * @return   The languageNr value
   */
  public int getLanguageNr() {
    return (-1);
  }

  /**
   * Sets the languageNr attribute of the IShopTemplateBean object
   *
   * @param nr  The new languageNr value
   */
  public void setLanguageNr(int nr) {
  }

  /**
   * Gets the content attribute of the IShopTemplateBean object
   *
   * @return   The content value
   */
  public String getContent() {
    return (null);
  }

  /**
   * Sets the content attribute of the IShopTemplateBean object
   *
   * @param content  The new content value
   */
  public void setContent(String content) {
  }

  /**
   * Gets the byteCode attribute of the IShopTemplateBean object
   *
   * @return   The byteCode value
   */
  public String getByteCode() {
    return (null);
  }

  /**
   * Sets the byteCode attribute of the IShopTemplateBean object
   *
   * @param byteCode  The new byteCode value
   */
  public void setByteCode(String byteCode) {
  }
}
