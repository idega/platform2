/*
 * $Id: IShopTemplate.java,v 1.4 2002/04/06 19:11:21 tryggvil Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.intershop.data;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public interface IShopTemplate {
  public final static String ISHOP_TABLE_TEMPLATES = "templates";
  public final static String ISHOP_TABLE_TEMPLATEDESC = "templatedescr";
  public final static String SUBTYPE_NAME = "intershop";

  public String getIShopID();

  public void setIShopID(String id);

  public String getIShopClass();

  public void setIShopClass(String cls);

  public int getIShopLanguageNr();

  public void setIShopLanguageNr(int nr);

  public void setIShopLanguageNr(Integer nr);

  public int getPageID();

  public void setPageID(int id);

  public void setPageID(Integer id);

  public String getIShopName();

  public void setIShopName(String name);

  public String getIShopDescription();

  public void setIShopDescription(String desc);
}
