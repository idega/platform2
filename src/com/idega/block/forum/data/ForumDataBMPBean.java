package com.idega.block.forum.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.category.data.ICCategory;
import com.idega.core.user.data.User;
import com.idega.core.user.data.UserBMPBean;
import com.idega.data.TreeableEntityBMPBean;

/**
 * Title:        Forums<p>
 * Description:  <p>
 * Copyright:    Copyright (c) idega margmiðlun hf.<p>
 * Company:      idega margmiðlun hf.<p>
 * @author idega 2001 - idega team - <a href="mailto:laddi@idega.is">laddi@idega.is</a>
 * @version 2.0
 */

public class ForumDataBMPBean extends TreeableEntityBMPBean implements ForumData {

  public ForumDataBMPBean() {
    super();
  }

  public ForumDataBMPBean(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameParentThreadID(),"Parent thread",true,true,Integer.class);
    addAttribute(getColumnNameTopicID(),"Topic",true,true,Integer.class,"many-to-one",ICCategory.class);
    addAttribute(getColumnNameThreadSubject(),"Thread subject",true,true,String.class);
    addAttribute(getColumnNameThreadBody(),"Thread body",true,true,String.class,15000);
    addAttribute(getColumnNameUserID(),"User",true,true,Integer.class,"many-to-one",User.class);
    addAttribute(getColumnNameUserName(),"User name",true,true,String.class);
    addAttribute(getColumnNameUserEMail(),"User e-mail",true,true,String.class);
    addAttribute(getColumnNameThreadDate(),"Date",true,true,Timestamp.class);
    addAttribute(getColumnNameNumberOfResponses(),"Responses",true,true,Integer.class);
    addAttribute(getColumnNameValid(),"Valid",true,true,String.class);
    setNullable(getColumnNameUserID(),true);
  }

  public static String getEntityTableName() { return "FO_THREAD"; }

  public static String getColumnNameForumThreadID() { return "FO_THREAD_ID"; }
  public static String getColumnNameParentThreadID() { return "PARENT_THREAD_ID";}
  public static String getColumnNameTopicID() { return "IC_CATEGORY_ID"; }
  public static String getColumnNameThreadSubject() { return "THREAD_SUBJECT"; }
  public static String getColumnNameThreadBody() { return "THREAD_BODY"; }
  public static String getColumnNameUserID() { return UserBMPBean.getColumnNameUserID(); }
  public static String getColumnNameUserName() { return "USER_NAME"; }
  public static String getColumnNameUserEMail() { return "USER_EMAIL"; }
  public static String getColumnNameThreadDate() { return "THREAD_DATE"; }
  public static String getColumnNameNumberOfResponses() { return "NUMBER_OF_RESPONSES"; }
  public static String getColumnNameValid() { return "VALID"; }

  public String getEntityName(){
    return getEntityTableName();
  }


  //Get
  public int getParentThreadID(){
    return getIntColumnValue(getColumnNameParentThreadID());
  }

  public int getTopicID(){
    return getIntColumnValue(getColumnNameTopicID());
  }

  public String getThreadSubject(){
    return (String)getColumnValue(getColumnNameThreadSubject());
  }

  public String getThreadBody(){
    return (String)getColumnValue(getColumnNameThreadBody());
  }

  public int getUserID(){
    return getIntColumnValue(getColumnNameUserID());
  }

  public String getUserName(){
    return (String)getColumnValue(getColumnNameUserName());
  }

  public String getUserEMail(){
    return (String)getColumnValue(getColumnNameUserEMail());
  }

  public Timestamp getThreadDate(){
    return (Timestamp)getColumnValue(getColumnNameThreadDate());
  }

  public int getNumberOfResponses(){
    return getIntColumnValue(getColumnNameNumberOfResponses());
  }

  public boolean isValid(){
    if (((String)getColumnValue(getColumnNameValid())).equals("Y") )
      return true;
    else
      return false;
  }


  //Set
  public void setParentThreadID(int parentThreadID){
    setColumn(getColumnNameParentThreadID(),parentThreadID);
  }

  public void setTopicID(int topicID){
    setColumn(getColumnNameTopicID(),topicID);
  }

  public void setThreadSubject(String threadSubject){
    setColumn(getColumnNameThreadSubject(),threadSubject);
  }

  public void setThreadBody(String threadBody){
    setColumn(getColumnNameThreadBody(),threadBody);
  }

  public void setUserID(int userID){
    setColumn(getColumnNameUserID(),userID);
  }

  public void setUserName(String userName){
    setColumn(getColumnNameUserName(),userName);
  }

  public void setUserEMail(String email){
    setColumn(getColumnNameUserEMail(),email);
  }

  public void setThreadDate(Timestamp threadDate){
    setColumn(getColumnNameThreadDate(),threadDate);
  }

  public void setNumberOfResponses(int numberOfResponses){
    setColumn(getColumnNameNumberOfResponses(),numberOfResponses);
  }

  public void setValid(boolean valid){
    if (valid)
      setColumn(getColumnNameValid(),"Y");
    else
      setColumn(getColumnNameValid(),"N");
  }

  public Iterator getChildrenIterator(){
    Iterator iter = super.getChildrenIterator(getColumnNameThreadDate());
    return iter;
  }

  public Collection ejbFindAllThreads(ICCategory category) throws FinderException {
    String sql = "select * from "+getEntityTableName()+" where "+this.getColumnNameTopicID()+" = "+Integer.toString(category.getID())+" and "+this.getColumnNameParentThreadID()+" = -1 and "+this.getColumnNameValid()+" = 'Y' order by "+this.getColumnNameThreadDate()+" desc";
    return super.idoFindIDsBySQL(sql);
  }

  public Collection ejbFindAllThreads(ICCategory category,int numberOfReturns) throws FinderException {
    String sql = "select * from "+getEntityTableName()+" where "+this.getColumnNameTopicID()+" = "+Integer.toString(category.getID())+" and "+this.getColumnNameParentThreadID()+" = -1 and "+this.getColumnNameValid()+" = 'Y' order by "+this.getColumnNameThreadDate()+" desc";
    return super.idoFindIDsBySQL(sql,numberOfReturns);
  }

  public int ejbHomeGetNumberOfThreads(ICCategory category) throws EJBException {
    try{
      String sql = "select count(*) from "+getEntityTableName()+" where "+this.getColumnNameTopicID()+" = "+Integer.toString(category.getID())+" and "+this.getColumnNameParentThreadID()+" = -1 and "+this.getColumnNameValid()+" = 'Y'";
      return super.idoGetNumberOfRecords(sql);
    }
    catch(com.idega.data.IDOException idoe){
      throw new EJBException(idoe);
    }
  }

  public Collection ejbFindNewestThread(ICCategory category) throws FinderException {
    String sql = "select * from "+getEntityTableName()+" where "+this.getColumnNameTopicID()+" = "+Integer.toString(category.getID())+" and "+this.getColumnNameParentThreadID()+" = -1 and "+this.getColumnNameValid()+" = 'Y' order by "+this.getColumnNameThreadDate()+" desc";
    return super.idoFindIDsBySQL(sql,1);
  }

  public Collection ejbFindThreadsInCategories(Collection categories,int numberOfReturns) throws FinderException {
    StringBuffer sql = new StringBuffer();
    sql.append("select * from ");
    sql.append(getEntityTableName());
    sql.append(" where ");
    sql.append(this.getColumnNameParentThreadID());
    sql.append(" = -1 and ");
    sql.append(this.getColumnNameValid());
    sql.append(" = 'Y' and (");
    Iterator iter = categories.iterator();
    while (iter.hasNext()) {
      ICCategory item = (ICCategory) iter.next();
      sql.append(getColumnNameTopicID());
      sql.append(" = ");
      sql.append(String.valueOf(item.getID()));
      if ( iter.hasNext() )
	sql.append(" or ");
    }
    sql.append(") order by ");
    sql.append(this.getColumnNameThreadDate());
    sql.append(" desc");
    return super.idoFindIDsBySQL(sql.toString(),numberOfReturns);
  }

} // class ForumThread