package com.idega.block.news.data;

import java.sql.*;
import com.idega.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.core.data.ICCategory;

public class NewsCategory extends ICCategory{

  public NewsCategory(){
    super();
  }
  public NewsCategory(int id)throws SQLException{
    super(id);
  }

  public String getCategoryType(){
    return "news";
  }

}
