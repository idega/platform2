//idega 2000 - Laddi

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.sql.*;

public class UnionText extends GolfEntity{

	public UnionText(){
		super();
	}

	public UnionText(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("union_id", "Klúbbur", true, true, "java.lang.Integer");
		addAttribute("text_id", "Texti", true, true, "java.lang.Integer");
	}

	public String getEntityName(){
		return "union_text";
	}

	public int getUnionId(){
		return getIntColumnValue("union_id");
	}

	public void setUnionId(int union_id){
		setColumn("union_id", union_id);
	}

	public int getTextId(){
		return getIntColumnValue("text_id");
	}

	public void setTextId(int text_id){
		setColumn("text_id", text_id);
	}

}
