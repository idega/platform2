/*
 *  $Id: IShopTemplateHome.java,v 1.1 2002/03/19 09:41:10 palli Exp $
 *
 *  Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 *
 */
package is.idega.idegaweb.intershop.business;

import com.idega.data.GenericEntity;
import com.idega.data.EntityFinder;
import com.idega.data.SimpleQuerier;
import is.idega.idegaweb.intershop.data.IShopTemplate;
import is.idega.idegaweb.intershop.data.IShopTemplateBean;

/**
 * @author    <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version   1.0
 */
public class IShopTemplateHome {
  private static IShopTemplateHome _instance = null;

  private IShopTemplateHome() { }

  public static IShopTemplateHome getInstance() {
    if (_instance == null) {
      _instance = new IShopTemplateHome();
    }

    return _instance;
  }

  public IShopTemplate getNewElement() {
    IShopTemplateBean bean = new IShopTemplateBean();

    return bean;
  }

  public IShopTemplate getElement(int id) {
    IShopTemplateBean bean = null;
    try {
      bean = new IShopTemplateBean(id);
    }
    catch(Exception e) {
      return null;
    }

    return bean;
  }

  public boolean insert(IShopTemplate entry) {
    if (entry instanceof IShopTemplateBean) {
      IShopTemplateBean bean = (IShopTemplateBean)entry;
      try {
        bean.insert();
      }
      catch (Exception e) {
        return false;
      }

      return true;
    }

    return false;
  }

  public boolean update(IShopTemplate entry) {
    if (entry instanceof IShopTemplateBean) {
      IShopTemplateBean bean = (IShopTemplateBean)entry;
      try {
        bean.update();
      }
      catch (Exception e) {
        return false;
      }

      return true;
    }

    return false;
  }

  public boolean delete(IShopTemplate entry) {
    if (entry instanceof IShopTemplateBean) {
      IShopTemplateBean bean = (IShopTemplateBean)entry;
      try {
        bean.delete();
      }
      catch (Exception e) {
        return false;
      }

      return true;
    }

    return false;
  }

  public int count() {
    StringBuffer sql = new StringBuffer("select count(*) from ");
    sql.append(IShopTemplateBean.getEntityTableName());
    String res[] = null;
    try {
      res = SimpleQuerier.executeStringQuery(sql.toString());
    }
    catch(Exception e) {
      return(0);
    }

    if (res == null)
      return(0);

    String c = res[0];
//    try {
      int ret = Integer.parseInt(c);
//    }
      return(ret);
  }
}