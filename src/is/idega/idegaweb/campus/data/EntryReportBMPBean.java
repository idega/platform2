package is.idega.idegaweb.campus.data;

import javax.ejb.*;
import com.idega.data.*;
import java.sql.*;
import com.idega.util.database.ConnectionBroker;
import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class EntryReportBMPBean implements EntryReport{


  public static String getEntityTableName(){return "V_BUILDING_ACCOUNT_ENTRY";}
  public static String getColumnBuildingId(){return "BUILDING_ID";}
  public static String getColumnBuildingName(){return  "BUILDING_NAME";}
  public static String getColumnKeyId(){return "KEY_ID";}
  public static String getColumnKeyName(){return "KEY_NAME";}
  public static String getColumnKeyInfo(){return "KEY_INFO";}
  public static String getColumnTotal(){return "TOTAL";}
  public static String getColumnNumber(){return "NUMBER";}

  private Integer BuildingId;
  private String BuildingName;
  private Integer KeyId;
  private String KeyName;
  private String KeyInfo;
  private Float Total;
  private Integer Number;

  private EntityContext _entityContext;
  private EJBHome _ejbHome;
  private EJBLocalHome _ejbLocalHome;
  private Object _primaryKey;

  /*
  public void initializeAttributes() {
    addAttribute(getColumnBuildingId(),"Building id",true,true,Integer.class);
    addAttribute(getColumnBuildingName(),"Building id",true,true,String.class);
    addAttribute(getColumnKeyId(),"Key id",true,true,String.class);
    addAttribute(getColumnKeyName(),"Key name",true,true,String.class);
    addAttribute(getColumnKeyInfo(),"Key info",true,true,String.class);
    addAttribute(getColumnTotal(),"Total",true,true,Float.class);
    addAttribute(getColumnNumber(),"Number",true,true,Integer.class);
  }
  */
  public String getEntityName() {
    return(getEntityTableName());
  }
  public int getBuildingId(){
   return BuildingId.intValue();
  }
  public int getKeyId(){
   return KeyId.intValue();
  }
  public String getBuildingName(){
    return BuildingName;
  }
  public String getKeyName(){
    return KeyName;
  }
  public String getKeyInfo(){
    return KeyInfo;
  }
  public float getTotal(){
    return Total.floatValue();
  }
  public int getNumber(){
    return Number.intValue();
  }

  private void setBuildingId(int id){
    BuildingId = new Integer(id);
  }
  private void setKeyId(int id){
   KeyId = new Integer(id);
  }
  private void setBuildingName(String name){
    BuildingName = name;
  }
  private void setKeyName(String name){
    KeyName = name;
  }
  private void setKeyInfo(String info){
    KeyInfo = info;
  }
  private void setTotal(float total){
    Total = new Float(total);
  }
  private void setNumber(int number){
    Number = new Integer(number);
  }

  public Class getPrimaryKeyClass(){return Integer.class;}
  public void setEJBHome(EJBHome home){}
  public void setEJBLocalHome(EJBLocalHome home){}
  public Object ejbFindByPrimaryKey(Object primaryKey){return null;}
  public Object ejbCreate(){return null;}
  public void unsetEntityContext(){_entityContext = null;}
  public void setEntityContext(EntityContext context){_entityContext = context;}
  public void ejbStore(){}
  public void ejbPassivate(){}
  public void ejbRemove(){}
  public void ejbLoad(){}
  public void ejbActivate(){}

  public static List findAllBySearch(int iBuildingId,int iAccountKey,Timestamp from,Timestamp to)throws SQLException{
     Connection conn= null;
    Statement Stmt= null;
    ResultSetMetaData metaData;
    Vector vector=null;
    String sql = getFindSql(iBuildingId,iAccountKey,from,to);

    try{
      conn = ConnectionBroker.getConnection();
      Stmt = conn.createStatement();

      //System.err.println(sql);
      ResultSet RS = Stmt.executeQuery(sql);
      metaData = RS.getMetaData();
      int count = 1;
      while (RS.next() ){
        EntryReportBMPBean tempobj= new EntryReportBMPBean() ;

        if(tempobj != null){
          String columnName = null;
            tempobj.setBuildingId(RS.getInt(1));
            tempobj.setBuildingName(RS.getString(2));
            tempobj.setKeyId(RS.getInt(3));
            tempobj.setKeyName(RS.getString(4));
            tempobj.setKeyInfo(RS.getString(5));
            tempobj.setTotal(RS.getFloat(6));
            tempobj.setNumber(RS.getInt(7));

        }
        if(vector==null){
          vector=new Vector();
        }
        vector.addElement(tempobj);

      }
      RS.close();
    }
    catch(SQLException ex){
      throw new SQLException("SQL : "+sql);
    }
    finally{
      if(Stmt != null){
        Stmt.close();
      }
      if (conn != null){
        ConnectionBroker.freeConnection(conn);
      }
    }

    if (vector != null){
      vector.trimToSize();
      return vector;
    }
    else{
      return null;
    }
  }

  private static String getFindSql(int iBuildingId,int iAccountKey,java.sql.Timestamp from,java.sql.Timestamp to){
    StringBuffer sql = new StringBuffer(" select ");
    sql.append(" b.bu_building_id building_id, ");
    sql.append(" b.name building_name, ");
    sql.append(" k.fin_acc_key_id key_id, ");
    sql.append(" k.name key_name, ");
    sql.append(" k.info key_info, ");
    sql.append(" sum(e.total) total, ");
    sql.append(" count(acc.fin_account_id) number ");
    sql.append(" from ");
    sql.append(" bu_apartment a,bu_building b,bu_floor f, ");
    sql.append(" cam_contract c,fin_account acc,fin_acc_entry e,fin_acc_key k ");
    sql.append(" where b.bu_building_id = f.bu_building_id ");
    sql.append(" and f.bu_floor_id = a.bu_floor_id ");
    sql.append(" and a.bu_apartment_id = c.bu_apartment_id ");
    sql.append(" and c.ic_user_id = acc.ic_user_id ");
    sql.append(" and e.fin_account_id = acc.fin_account_id ");
    sql.append(" and k.fin_acc_key_id = e.fin_acc_key_id ");

    boolean and = false;

    if(iBuildingId > 0){
      sql.append(" and ");
      sql.append(" b.bu_building_id ");
      sql.append( " = ");
      sql.append(iBuildingId);
      and = true;
    }
    if(iAccountKey > 0){
      sql.append(" and ");
      sql.append(" k.fin_acc_key_id ");
      sql.append(" = ").append(iAccountKey);
    }
    if(from!=null){
      sql.append(" and e.payment_date >= '");
      sql.append(from.toString());
      sql.append("'");
    }
    if(to!=null){
      sql.append(" and e.payment_date <= '");
      sql.append(to.toString());
      sql.append("'");
    }

    sql.append(" group by b.bu_building_id,b.name,k.fin_acc_key_id,k.name,k.info ");
    sql.append(" order by b.bu_building_id ");

    return sql.toString();
  }

}