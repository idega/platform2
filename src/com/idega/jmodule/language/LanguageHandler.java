package com.idega.jmodule.language;

import java.sql.*;

public  class LanguageHandler extends Object{

private Connection conn;
private String language;

/*public LanguageHandler(){
}

public LanguageHandler(Connection conn){
	setConnection(conn);
}	
*/

public LanguageHandler(Connection conn,String language){
	setConnection(conn);
	setLanguage(language);
}

public void setLanguage(String language){
	this.language=language;
}

public void setConnection(Connection conn){
	this.conn=conn;
}

public String getString(String StringName)throws SQLException{
	Statement Stmt=conn.createStatement();
	ResultSet RS = Stmt.executeQuery("select string_value from language_string_value where language_string_value.string_id=language_string.string_id and langage_string.string_name='"+StringName+"' and language_string_value.language='"+language+"'");
	return RS.getString("string_value");
}

public String getString(int StringID)throws SQLException{
	String returnString;
	Statement Stmt=conn.createStatement();
	ResultSet RS = Stmt.executeQuery("select string_value from language_string_value where language='"+language+"' and string_id='"+StringID+"'");
	returnString = RS.getString("string_value");
	RS.close();
	Stmt.close();
	return returnString;
}

}
