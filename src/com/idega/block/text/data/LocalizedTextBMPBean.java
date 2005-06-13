//idega 2000 - Laddi
package com.idega.block.text.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDORelationshipException;
import com.idega.util.text.TextSoap;

public class LocalizedTextBMPBean extends com.idega.data.GenericEntity implements com.idega.block.text.data.LocalizedText {

  public LocalizedTextBMPBean(){
    super();
    setBody("");
  }

  public LocalizedTextBMPBean(int id)throws SQLException{
    super(id);
    setBody("");
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameLocaleId(), "Locale", true, true, java.lang.Integer.class,"many_to_one",com.idega.core.localisation.data.ICLocale.class);
    addAttribute(getColumnNameHeadline(), "Headline", true, true, java.lang.String.class);
    addAttribute(getColumnNameTitle(), "Title", true, true, java.lang.String.class);
    addAttribute(getColumnNameBody(), "Body", true, true, java.lang.String.class,30000);
    addAttribute(getColumnNameCreated(), "Created", true, true, java.sql.Timestamp.class);
    addAttribute(getColumnNameUpdated(), "Updated", true, true, java.sql.Timestamp.class);
    addAttribute(getColumnNameMarkupLanguage(), "The markup language of the text", true, true, java.lang.String.class);

    addIndex("IDX_TX_LOCALIZED_TEXT_1", getColumnNameLocaleId());
  }

  public static String getEntityTableName(){ return "TX_LOCALIZED_TEXT";}
  public static String getColumnNameLocaleId(){ return "IC_LOCALE_ID";}
  public static String getColumnNameHeadline(){ return "HEADLINE";}
  public static String getColumnNameTitle(){ return "TITLE";}
  public static String getColumnNameBody(){ return "BODY";}
  public static String getColumnNameCreated(){ return "CREATED";}
  public static String getColumnNameUpdated(){ return "UPDATED";}
  public static String getColumnNameMarkupLanguage(){ return "MARKUP_LANGUAGE";}

  public String getEntityName(){
    return getEntityTableName();
  }
  public int getLocaleId(){
    return getIntColumnValue(getColumnNameLocaleId());
  }
  public void setLocaleId(int id){
    setColumn(getColumnNameLocaleId(),id);
  }
  public void setLocaleId(Integer id){
    setColumn(getColumnNameLocaleId(),id);
  }
  public String getHeadline(){
    return getStringColumnValue(getColumnNameHeadline());
  }
  public void setHeadline(String headline){
    setColumn(getColumnNameHeadline(), headline);
  }
  public String getTitle(){
    return getStringColumnValue(getColumnNameTitle());
  }
  public void setTitle(String title){
    setColumn(getColumnNameTitle(), title);
  }
  public String getBody(){
    return getStringColumnValue(getColumnNameBody());
  }
  public void setBody(String body){
    setColumn(getColumnNameBody(), addBreaks(body));
  }
  public String getMarkupLanguage(){
    return getStringColumnValue(getColumnNameMarkupLanguage());
  }
  public void setMarkupLanguage(String markup){
    setColumn(getColumnNameMarkupLanguage(), markup);
  }
  public java.sql.Timestamp getCreated(){
    return (java.sql.Timestamp) getColumnValue(getColumnNameCreated());
  }
  public void setCreated(java.sql.Timestamp stamp){
    setColumn(getColumnNameCreated(), stamp);
  }
  public java.sql.Timestamp getUpdated(){
    return (java.sql.Timestamp) getColumnValue(getColumnNameUpdated());
  }
  public void setUpdated(java.sql.Timestamp stamp){
    setColumn(getColumnNameUpdated(), stamp);
  }

  private String addBreaks(String text){
    //replace with local bean method? and a none html specific xml
    return TextSoap.findAndReplaceOnPrefixCondition(text, "\r\n", ">","<br/>",true);
  }
  public Collection ejbFindRelatedEntities(IDOEntity entity) throws IDORelationshipException{
  	return idoGetRelatedEntities(entity);
  }
  /*
   *  (non-Javadoc)
   * @see com.idega.data.GenericEntity#idoAddTo(com.idega.data.IDOEntity)
   */
  public void idoAddTo(IDOEntity entity) throws IDOAddRelationshipException {

  	try {
  		idoAddTo(getNameOfMiddleTable(entity, this), entity.getEntityDefinition().getPrimaryKeyDefinition().getField().getSQLFieldName(), entity.getPrimaryKey());
  	} catch (Exception e) {
  		throw new IDOAddRelationshipException(e, this);
  	}
  }
  /*
   * copy and paste from GenericEntity
   */
  public void idoAddTo(String middleTableName, String sqlFieldName, Object primaryKey) throws IDOAddRelationshipException {
  	/**
  	 * @todo Change implementation
  	 */
  	try {
  		Connection conn = null;
  		Statement Stmt = null;
  		try {
  			conn = getConnection(getDatasource());
  			Stmt = conn.createStatement();
  			//String sql = "insert into "+getNameOfMiddleTable(entityToAddTo,this)+"("+getIDColumnName()+","+entityToAddTo.getIDColumnName()+") values("+getID()+","+entityToAddTo.getID()+")";
  			String sql = null;
  			//try
  			//{
  			sql = "insert into " + middleTableName + "(" + getIDColumnName() + "," + sqlFieldName + ") values(" + getPrimaryKeyValueSQLString() + "," + getKeyValueSQLString(primaryKey) + ")";
  			/*}
  			 catch (RemoteException rme)
  			 {
  			 throw new SQLException("RemoteException in addTo, message: " + rme.getMessage());
  			 }*/

  			//debug("statement: "+sql);

  			Stmt.executeUpdate(sql);
  		} finally {
  			if (Stmt != null) {
  				Stmt.close();
  			}
  			if (conn != null) {
  				freeConnection(getDatasource(), conn);
  			}
  		}

  	} catch (Exception ex) {
  		//ex.printStackTrace();
  		throw new IDOAddRelationshipException(ex, this);
  	}
  }

}
