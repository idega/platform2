/*
 *  $Id: IShopTemplateHome.java,v 1.4 2004/05/24 14:56:57 palli Exp $
 *
 *  Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 *
 */
package is.idega.idegaweb.intershop.business;

import is.idega.idegaweb.intershop.data.IShopTemplate;
import is.idega.idegaweb.intershop.data.IShopTemplateBean;

import java.util.List;

import com.idega.data.EntityFinder;
import com.idega.data.IDOFinderException;
import com.idega.data.SimpleQuerier;

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
    IShopTemplateBean bean = ((is.idega.idegaweb.intershop.data.IShopTemplateBeanHome)com.idega.data.IDOLookup.getHomeLegacy(IShopTemplateBean.class)).createLegacy();

    return bean;
  }

  public IShopTemplate getElement(int id) {
    IShopTemplateBean bean = null;
    try {
      bean = ((is.idega.idegaweb.intershop.data.IShopTemplateBeanHome)com.idega.data.IDOLookup.getHomeLegacy(IShopTemplateBean.class)).findByPrimaryKeyLegacy(id);
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
    sql.append(is.idega.idegaweb.intershop.data.IShopTemplateBeanBMPBean.getEntityTableName());
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
    int ret = Integer.parseInt(c);

    return ret;
  }

  public List findAll() {
    List l = null;
    try {
      l = EntityFinder.getInstance().findAll(IShopTemplateBean.class);
    }
    catch(IDOFinderException e) {
    }

    return l;
  }

  public IShopTemplate findByPageId(int pageId) {
    List l = null;
    try {
      l = EntityFinder.getInstance().findAllByColumn(IShopTemplateBean.class,is.idega.idegaweb.intershop.data.IShopTemplateBeanBMPBean.getPageIdColumnName(),pageId);
    }
    catch(IDOFinderException e) {

    }

    if (l == null)
      return null;

    if (l.size() == 1)
      return (IShopTemplate)l.get(0);

    return null;
  }
}
