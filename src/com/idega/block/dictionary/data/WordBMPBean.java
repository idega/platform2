package com.idega.block.dictionary.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.category.data.ICCategory;
import com.idega.core.file.data.ICFile;
import com.idega.data.GenericEntity;

/**
 * Title:        Book bean
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author <a href="mailto:laddi@idega.is">Þórhallur Helgason</a>
 * @version 1.0
 */

public class WordBMPBean extends GenericEntity implements Word {

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnCategoryID(), "Category", true, true, Integer.class,"many-to-one",ICCategory.class);
    addAttribute(getColumnWord(), "Name", true, true, String.class);
    addAttribute(getColumnDescription(), "Description", true, true, String.class,10000);
    addAttribute(getColumnImage(), "Image", true, true, Integer.class,"many-to-one",ICFile.class);
    setNullable(getColumnImage(),true);
  }

  public String getIDColumnName(){ return "DI_WORD_ID";}

  protected static String getEntityTableName(){ return "DI_WORD";}

  protected static String getColumnCategoryID(){ return "WORD_CATEGORY_ID";}
  protected static String getColumnWord(){ return "WORD";}
  protected static String getColumnDescription(){ return "DESCRIPTION";}
  protected static String getColumnImage(){ return "IC_FILE_ID";}


  public String getEntityName(){
    return getEntityTableName();
  }

  public int getCategoryID(){
    return getIntColumnValue(getColumnCategoryID());
  }

  public void setCategoryID(int categoryID){
    setColumn(getColumnCategoryID(), categoryID);
  }

  public String getWord(){
    return getStringColumnValue(getColumnWord());
  }

  public void setWord(String word){
    setColumn(getColumnWord(), word);
  }

  public String getDescription(){
    return getStringColumnValue(getColumnDescription());
  }

  public void setDescription(String description){
    setColumn(getColumnDescription(), description);
  }

  public int getImageID(){
    return getIntColumnValue(getColumnImage());
  }

  public void setImageID(int imageID){
    setColumn(getColumnImage(), imageID);
  }

  public Collection ejbFindAllWordsInCategories(int[] categories) throws FinderException {
    StringBuffer sql = new StringBuffer();
    sql.append("select * from ");
    sql.append(getEntityTableName());
    sql.append(" where ");
    sql.append(getColumnCategoryID());
    sql.append(" in (");
    for ( int a = 0; a < categories.length; a++ ) {
      if ( a > 0 )
	sql.append(",");
      sql.append(categories[a]);
    }
    sql.append(")");
    return super.idoFindIDsBySQL(sql.toString());
  }

  public Collection ejbFindAllWordsByCategory(int categoryID) throws FinderException {
    return super.idoFindIDsBySQL("select * from "+getEntityTableName()+" where "+getColumnCategoryID()+" = "+String.valueOf(categoryID));
  }

  public Collection ejbFindAllWordsContaining(String word) throws FinderException {
    return super.idoFindIDsBySQL("select * from "+this.getEntityTableName()+" where "+getColumnWord()+" like '%"+word+"%'");
  }

}