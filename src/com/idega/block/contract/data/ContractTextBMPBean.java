/*

 * $Id: ContractTextBMPBean.java,v 1.2 2003/04/03 07:24:44 laddi Exp $

 *

 * Copyright (C) 2001 Idega hf. All Rights Reserved.

 *

 * This software is the proprietary information of Idega hf.

 * Use is subject to license terms.

 *

 */

package com.idega.block.contract.data;





import java.sql.SQLException;



/**

 *

 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>

 * @version 1.0

 */

public class ContractTextBMPBean extends com.idega.data.GenericEntity implements com.idega.block.contract.data.ContractText {



  public static String getEntityTableName(){return "CON_TEXT";}

	public static String getColumnNameCategoryId(){return "CON_CATEGORY_ID";}

  public static String getNameColumnName(){return "NAME";}

  public static String getTextColumnName(){return "TEXT";}

  public static String getOrdinalColumnName(){return "ORDINAL";}

  public static String getLanguageColumnName(){return "LANGUAGE";}

  public static String getUseTagsColumnName(){return "USETAGS";}





  public ContractTextBMPBean() {

  }

  public ContractTextBMPBean(int id) throws SQLException {

    super(id);

  }



  public void initializeAttributes() {

    addAttribute(getIDColumnName());

		addAttribute(getColumnNameCategoryId(),"Category",true,true,java.lang.Integer.class,"many-to-one",com.idega.block.contract.data.ContractCategory.class);

    addAttribute(getNameColumnName(),"Name",true,true,"java.lang.String",300);

    addAttribute(getTextColumnName(),"Text",true,true,"java.lang.String",4000);

    addAttribute(getOrdinalColumnName(),"Ordinal",true,true,"java.lang.Integer");

    addAttribute(getLanguageColumnName(),"Language",true,true,"java.lang.String");

    addAttribute(getUseTagsColumnName(),"Tagcheck",true,true,"java.lang.Boolean");

  }



  public String getEntityName() {

    return(getEntityTableName());

  }

  public void setName(String name) {

    setColumn(getNameColumnName(),name);

  }

  public String getName() {

    return(getStringColumnValue(getNameColumnName()));

  }

  public void setText(String text) {

    setColumn(getTextColumnName(),text);

  }

  public String getText() {

    return(getStringColumnValue(getTextColumnName()));

  }

  public void setLanguage(String lang) {

    setColumn(getLanguageColumnName(),lang);

  }

  public String getLanguage() {

    return(getStringColumnValue(getLanguageColumnName()));

  }

  public void setOrdinal(Integer ordinal){

    setColumn(getOrdinalColumnName(),ordinal);

  }

  public void setOrdinal(int ordinal){

    setColumn(getOrdinalColumnName(),ordinal);

  }

  public int getOrdinal(){

    return getIntColumnValue(getOrdinalColumnName());

  }

  public boolean getUseTags(){

    return getBooleanColumnValue(getUseTagsColumnName());

  }

  public void setUseTags(boolean usetags){

    setColumn(getUseTagsColumnName(), usetags);

  }

	public void setCategoryId(int id){

	  setColumn(getColumnNameCategoryId(),id);

	}

	public void setCategoryId(Integer id){

	  setColumn(getColumnNameCategoryId(),id);

	}

	public Integer getCategoryId(){

	  return getIntegerColumnValue(getColumnNameCategoryId());

	}



}

