/*
 * $Id: IShopTemplate.java,v 1.1 2002/03/11 22:59:27 palli Exp $
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
  /**
   *
   */
  public String getId();

  /**
   *
   */
  public void setId(String id);

  /**
   *
   */
  public String getClassId();

  /**
   *
   */
  public void setClassId(String classId);

  /**
   *
   */
  public int getLanguageNr();

  /**
   *
   */
  public void setLanguageNr(int nr);

  /**
   *
   */
  public String getContent();

  /**
   *
   */
  public void setContent(String content);

  /**
   *
   */
  public String getByteCode();

  /**
   *
   */
  public void setByteCode(String byteCode);
}