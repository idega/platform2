/*
 *  $Id: IShopBundleStarter.java,v 1.9 2004/05/24 14:56:57 palli Exp $
 *
 *  Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 *
 */
package is.idega.idegaweb.intershop.business;

import is.idega.idegaweb.intershop.data.IShopTemplate;
import is.idega.idegaweb.intershop.presentation.IShopToolbarButton;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.idega.builder.app.IBApplication;
import com.idega.builder.business.IBPageHelper;
import com.idega.core.builder.data.ICDomain;
import com.idega.data.EntityFinder;
import com.idega.data.IDOFinderException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.util.FileUtil;

/**
 * @author    <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version   1.0
 */
public class IShopBundleStarter implements IWBundleStartable {
  /**
   * Constructor for the IShopBundleStarter object
   */
  public IShopBundleStarter() { }

  /**
   * Description of the Method
   *
   * @param starterBundle  Description of the Parameter
   */
  public void start(IWBundle starterBundle) {
    System.out.println("Starting Intershop initialization");
    IShopToolbarButton separator = new IShopToolbarButton(starterBundle, IShopToolbarButton.BUTTON_SEPERATOR);
    IShopToolbarButton button = new IShopToolbarButton(starterBundle, IShopToolbarButton.BUTTON_EXPORT);
    IShopToolbarButton button2 = new IShopToolbarButton(starterBundle, IShopToolbarButton.BUTTON_NEW_PAGE);

    List l = (List)starterBundle.getApplication().getAttribute(IBApplication.TOOLBAR_ITEMS);
    if (l == null) {
      l = new Vector();
      starterBundle.getApplication().setAttribute(IBApplication.TOOLBAR_ITEMS, l);
    }

    l.add(separator);
    l.add(button);
    l.add(button2);

    StringBuffer path = new StringBuffer(starterBundle.getPropertiesRealPath());
    if (!path.toString().endsWith(FileUtil.getFileSeparator()))
      path.append(FileUtil.getFileSeparator());

    path.append(starterBundle.getProperty("sybaseproperties","sybasedb.properties"));

    int count = IShopTemplateHome.getInstance().count();
    if (count == 0) {
      insertInitialData(path.toString(),starterBundle);
    }
  }

  /**
   *
   */
  private void insertInitialData(String pathToPropertiesFile, IWBundle starterBundle) {
    List l = null;
    try {
      l = EntityFinder.getInstance().findAll(ICDomain.class);
    }
    catch(IDOFinderException e) {
      e.printStackTrace();
      return;
    }

    ICDomain main = null;
    if (l.size() > 0)
      main = (ICDomain)l.get(0);
    else
      return;

    Properties props = new Properties();
    try {
      props.load(new FileInputStream(pathToPropertiesFile));
    }
    catch(FileNotFoundException e) {
      e.printStackTrace();
      return;
    }
    catch(IOException e) {
      e.printStackTrace();
      return;
    }

    String parent = Integer.toString(main.getStartPageID());
    String folder = starterBundle.getProperty("INTERSHOP_FOLDER");
    int folderId = 0;
    if (folder == null) {
      folderId = IBPageHelper.getInstance().createNewPage(parent,"Intershop folder",com.idega.builder.data.IBPageBMPBean.PAGE,"",null);
      starterBundle.setProperty("INTERSHOP_FOLDER",Integer.toString(folderId));
    }
    else {
      folderId = Integer.parseInt(folder);
    }

    String driver = props.getProperty("drivers");
    String url = props.getProperty("default.url");
    String dbname = props.getProperty("default.dbname");
    String user = props.getProperty("default.user");
    String passwd = props.getProperty("default.password");

    if (driver == null || url == null || dbname == null || user == null || passwd == null) {
      return;
    }

    Vector result = new Vector();
    try {
      Class.forName(driver).newInstance();
      Connection conn = DriverManager.getConnection(url,user,passwd);

      StringBuffer sql = new StringBuffer("select a.");
      sql.append(is.idega.idegaweb.intershop.data.IShopTemplateBeanBMPBean.getIShopClassColumnName());
      sql.append(", a.");
      sql.append(is.idega.idegaweb.intershop.data.IShopTemplateBeanBMPBean.getIShopIDColumnName());
      sql.append(", ");
      sql.append(is.idega.idegaweb.intershop.data.IShopTemplateBeanBMPBean.getIShopLanguageNrColumnName());
      sql.append(", ");
      sql.append(is.idega.idegaweb.intershop.data.IShopTemplateBeanBMPBean.getIShopDescriptionColumnName());
      sql.append(", ");
      sql.append(is.idega.idegaweb.intershop.data.IShopTemplateBeanBMPBean.getIShopNameColumnName());
      sql.append(" from ");
      sql.append(dbname);
      sql.append("..");
      sql.append(IShopTemplate.ISHOP_TABLE_TEMPLATES);
      sql.append(" a, ");
      sql.append(dbname);
      sql.append("..");
      sql.append(IShopTemplate.ISHOP_TABLE_TEMPLATEDESC);
      sql.append(" b ");
      sql.append(" where a.");
      sql.append(is.idega.idegaweb.intershop.data.IShopTemplateBeanBMPBean.getIShopClassColumnName());
      sql.append(" = b.");
      sql.append(is.idega.idegaweb.intershop.data.IShopTemplateBeanBMPBean.getIShopClassColumnName());
      sql.append(" and a.");
      sql.append(is.idega.idegaweb.intershop.data.IShopTemplateBeanBMPBean.getIShopIDColumnName());
      sql.append(" = b.");
      sql.append(is.idega.idegaweb.intershop.data.IShopTemplateBeanBMPBean.getIShopIDColumnName());

      Statement stmt = conn.createStatement();
      ResultSet set = stmt.executeQuery(sql.toString());

      while (set.next()) {
        String className = set.getString(is.idega.idegaweb.intershop.data.IShopTemplateBeanBMPBean.getIShopClassColumnName());
        String id = set.getString(is.idega.idegaweb.intershop.data.IShopTemplateBeanBMPBean.getIShopIDColumnName());
        String langnr = set.getString(is.idega.idegaweb.intershop.data.IShopTemplateBeanBMPBean.getIShopLanguageNrColumnName());
        String name = set.getString(is.idega.idegaweb.intershop.data.IShopTemplateBeanBMPBean.getIShopNameColumnName());
        String desc = set.getString(is.idega.idegaweb.intershop.data.IShopTemplateBeanBMPBean.getIShopDescriptionColumnName());
        result.add(className);
        result.add(id);
        result.add(langnr);
        result.add(name);
        result.add(desc);
      }

      set.close();
      stmt.close();
      conn.close();
    }
    catch(Exception e) {
      e.printStackTrace();
      return;
    }

    String top = Integer.toString(folderId);
    Hashtable parents = new Hashtable();

    try {
      Iterator it = result.iterator();
      while (it.hasNext()) {
        String className = (String)it.next();
        String id = (String)it.next();
        String langnr = (String)it.next();
        String name = (String)it.next();
        String desc = (String)it.next();

        String parentId = (String)parents.get(className);
        if (parentId == null) {
          int tmp = IBPageHelper.getInstance().createNewPage(top,className,com.idega.builder.data.IBPageBMPBean.PAGE,"",null);
          parentId = Integer.toString(tmp);
          parents.put(className,parentId);
          starterBundle.setProperty(className + "_FOLDER",parentId);
        }

        int pageId = IBPageHelper.getInstance().createNewPage(parentId,name,com.idega.builder.data.IBPageBMPBean.PAGE,"",null,null,IShopTemplate.SUBTYPE_NAME);

        IShopTemplate temp = IShopTemplateHome.getInstance().getNewElement();
        temp.setIShopClass(className);
        temp.setIShopDescription(desc);
        temp.setIShopID(id);
        temp.setIShopLanguageNr(Integer.parseInt(langnr));
        temp.setIShopName(name);
        temp.setPageID(pageId);
        IShopTemplateHome.getInstance().insert(temp);
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

	/**
	 * @see com.idega.idegaweb.IWBundleStartable#stop(IWBundle)
	 */
	public void stop(IWBundle starterBundle) {
		//does nothing...
	}
}