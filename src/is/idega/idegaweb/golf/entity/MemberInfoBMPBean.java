//idega 2000 - Ægir

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import javax.ejb.FinderException;

import com.idega.data.GenericEntity;


public class MemberInfoBMPBean extends GenericEntity implements MemberInfo{

	public void initializeAttributes(){
    addAttribute("member_id","Meðlimur",true,true,"java.lang.Integer","one-to-one","is.idega.idegaweb.golf.entity.Member");
    addAttribute("handicap","Forgjöf",true,true,"java.lang.Float");
    addAttribute("handicap_first","Upphafsforgjöf",true,true,"java.lang.Float");
    addAttribute("history", "Saga", true, true, "java.lang.String",20000);
    
    addIndex("IDX_MINFO_1", "member_id");
  }

	public String getEntityName(){
		return "member_info";
	}

	public int getMemberId(){
		return getIntColumnValue("member_id");
	}

	public void setMemberId(int member_id){
		setColumn("member_id",member_id);
	}

	public String getIDColumnName(){
		return "member_id";
	}

	public float getHandicap(){
		return getFloatColumnValue("handicap");
	}
	public void setFirstHandicap(float handicap) {
		setColumn("handicap_first", handicap);
	}

      	public void setFirstHandicap(Float handicap) {
		setColumn("handicap_first", handicap);
	}


	public float getFirstHandicap() {
		return getFloatColumnValue("handicap_first");
	}

	public void setHandicap(Float handicap){
		setColumn("handicap", handicap);
	}

	public void setHandicap(float handicap){
		setColumn("handicap", handicap);
	}


        public String getHistory() {
          return getStringColumnValue("history");
	}

	public void setHistory(String history) {
          setColumn("history",history);
	}

	
	public Object ejbFindByMember(Member member) throws FinderException {
		return idoFindOnePKByQuery(idoQueryGetSelect().appendWhereEquals("member_id",member));
	}



/*

	public void insert()throws SQLException{
		Connection conn= null;
		Statement Stmt= null;
		//PreparedStatement Stmt = null;
		try{
			conn = getConnection();
			/*String query = "insert into "+getEntityName()+" ("+getCommaDelimitedColumnNames()+") values("+getQuestionmarksForColumns()+")";
			System.out.println(query);
			Stmt = conn.prepareStatement(query);
			for (int i = 0; i < getColumnNames().length; i++){
				if (!isNull(getColumnNames()[i])){
					insertIntoPreparedStatement(getColumnNames()[i],Stmt,i+1);
				}
			}
			Stmt.executeUpdate();*/
/*			Stmt = conn.createStatement();
			int i = Stmt.executeUpdate("insert into "+getEntityName()+"("+getCommaDelimitedColumnNames()+") values ("+getCommaDelimitedColumnValues()+")");
		}
		finally{
			if(Stmt != null){
				Stmt.close();
			}
			if (conn != null){
				freeConnection(conn);
			}
		}
	}
*/
}
