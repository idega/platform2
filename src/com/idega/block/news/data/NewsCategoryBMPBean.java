package com.idega.block.news.data;



import java.sql.*;

import com.idega.data.*;

import com.idega.util.IWTimestamp;

import com.idega.core.data.ICCategory;



public class NewsCategoryBMPBean extends com.idega.core.data.ICCategoryBMPBean implements com.idega.block.news.data.NewsCategory {



  public NewsCategoryBMPBean(){

    super();

  }

  public NewsCategoryBMPBean(int id)throws SQLException{

    super(id);

  }



  public String getCategoryType(){

    return "news";

  }



}

