/*

 *  $Id: IShopTemplateBeanBMPBean.java,v 1.3 2004/05/24 14:56:56 palli Exp $

 *

 *  Copyright (C) 2002 Idega hf. All Rights Reserved.

 *

 *  This software is the proprietary information of Idega hf.

 *  Use is subject to license terms.

 *

 */

package is.idega.idegaweb.intershop.data;



import java.sql.SQLException;

import com.idega.core.builder.data.ICPage;



/**

 * @author    <a href="mail:palli@idega.is">Pall Helgason</a>

 * @version   1.0

 */

public class IShopTemplateBeanBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.intershop.data.IShopTemplateBean,is.idega.idegaweb.intershop.data.IShopTemplate {

  private final static String ENTITY_NAME = "is_template";

  private final static String ISHOP_ID = "id";

  private final static String ISHOP_CLASS = "class";

  private final static String ISHOP_LANGUAGE_NR = "langnr";

  private final static String ISHOP_CONTENT = "content";

  private final static String ISHOP_BYTE_CODE = "bytecode";

  private final static String ISHOP_NAME = "name";

  private final static String ISHOP_DESCRIPTION = "description";

  private final static String PAGE_ID = "ib_page_id";



  /**

   * Constructor for the IShopTemplateBean object

   */

  public IShopTemplateBeanBMPBean() {

    super();

  }



  public IShopTemplateBeanBMPBean(int id) throws SQLException {

    super(id);

  }



  public void initializeAttributes() {

    addAttribute(getIDColumnName());

    addAttribute(getIShopClassColumnName(),"",true,true,String.class,2);

    addAttribute(getIShopIDColumnName(),"",true,true,String.class,30);

    addAttribute(getIShopLanguageNrColumnName(),"",true,true,Integer.class);

    addAttribute(getIShopNameColumnName(),"",true,true,String.class,255);

    addAttribute(getIShopDescriptionColumnName(),"",true,true,String.class,255);

    addAttribute(getPageIdColumnName(),"",true,true,Integer.class,com.idega.data.GenericEntity.ONE_TO_ONE,ICPage.class);

  }



  public String getEntityName() {

    return ENTITY_NAME;

  }



  public static String getEntityTableName() {

    return ENTITY_NAME;

  }



  public static String getIShopIDColumnName() {

    return ISHOP_ID;

  }



  public static String getIShopClassColumnName() {

    return ISHOP_CLASS;

  }



  public static String getIShopLanguageNrColumnName() {

    return ISHOP_LANGUAGE_NR;

  }



  public static String getIShopContentColumnName() {

    return ISHOP_CONTENT;

  }



  public static String getIShopByteCodeColumnName() {

    return ISHOP_BYTE_CODE;

  }



  public static String getIShopNameColumnName() {

    return ISHOP_NAME;

  }



  public static String getIShopDescriptionColumnName() {

    return ISHOP_DESCRIPTION;

  }



  public static String getPageIdColumnName() {

    return PAGE_ID;

  }



  public String getIShopID() {

    return getStringColumnValue(getIShopIDColumnName());

  }



  public void setIShopID(String id) {

    setColumn(getIShopIDColumnName(),id);

  }



  public String getIShopClass() {

    return getStringColumnValue(getIShopClassColumnName());

  }



  public void setIShopClass(String classId) {

    setColumn(getIShopClassColumnName(),classId);

  }



  public int getIShopLanguageNr() {

    return getIntColumnValue(getIShopLanguageNrColumnName());

  }



  public void setIShopLanguageNr(int nr) {

    setColumn(getIShopLanguageNrColumnName(),nr);

  }



  public void setIShopLanguageNr(Integer nr) {

    setColumn(getIShopLanguageNrColumnName(),nr);

  }



  public String getIShopName() {

    return getStringColumnValue(getIShopNameColumnName());

  }



  public void setIShopName(String name) {

    setColumn(getIShopNameColumnName(),name);

  }



  public String getIShopDescription() {

    return getStringColumnValue(getIShopDescriptionColumnName());

  }



  public void setIShopDescription(String desc) {

    setColumn(getIShopDescriptionColumnName(),desc);

  }



  public int getPageID() {

    return getIntColumnValue(getPageIdColumnName());

  }



  public void setPageID(int id) {

    setColumn(getPageIdColumnName(),id);

  }



  public void setPageID(Integer id) {

    setColumn(getPageIdColumnName(),id);

  }

}
