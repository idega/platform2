package com.idega.block.news.data;



import java.sql.SQLException;



public class NewsCategoryBMPBean extends com.idega.block.category.data.ICCategoryBMPBean implements com.idega.block.news.data.NewsCategory {



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

