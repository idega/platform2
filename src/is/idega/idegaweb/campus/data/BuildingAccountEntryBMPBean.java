/*
 * $Id: BuildingAccountEntryBMPBean.java,v 1.4 2004/05/24 14:21:41 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.data;



import java.sql.SQLException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */
public class BuildingAccountEntryBMPBean extends com.idega.data.GenericEntity implements BuildingAccountEntry  {

/*
create view V_BUILDING_ACCOUNT_ENTRY(
    BUILDING_ID,
    BUILDING_NAME,
    Key_ID,
    KEY_NAME,
    KEY_INFO,
    TOTAL,
    NUMBER
)
as
select
b.bu_building_id building_id,
b.name building_name,
k.fin_acc_key_id key_id,
k.name key_name,
k.info key_info,
sum(e.total) total,
count(acc.fin_account_id) number

from
bu_apartment a,bu_building b,bu_floor f,
cam_contract c,fin_account acc,fin_acc_entry e,fin_acc_key k
where b.bu_building_id = f.bu_building_id
and f.bu_floor_id = a.bu_floor_id
and a.bu_apartment_id = c.bu_apartment_id
and c.ic_user_id = acc.ic_user_id
and e.fin_account_id = acc.fin_account_id
and k.fin_acc_key_id = e.fin_acc_key_id
group by b.bu_building_id,b.name,k.fin_acc_key_id, k.name,k.info
*/

  public static String getEntityTableName(){return "V_BUILDING_ACCOUNT_ENTRY";}
  public static String getColumnBuildingId(){return "BUILDING_ID";}
  public static String getColumnBuildingName(){return  "BUILDING_NAME";}
  public static String getColumnKeyId(){return "KEY_ID";}
  public static String getColumnKeyName(){return "KEY_NAME";}
  public static String getColumnKeyInfo(){return "KEY_INFO";}
  public static String getColumnTotal(){return "TOTAL";}
  public static String getColumnNumber(){return "NUMBER";}

  public void initializeAttributes() {
    addAttribute(getColumnBuildingId(),"Building id",true,true,Integer.class);
    addAttribute(getColumnBuildingName(),"Building id",true,true,String.class);
    addAttribute(getColumnKeyId(),"Key id",true,true,String.class);
    addAttribute(getColumnKeyName(),"Key name",true,true,String.class);
    addAttribute(getColumnKeyInfo(),"Key info",true,true,String.class);
    addAttribute(getColumnTotal(),"Total",true,true,Float.class);
    addAttribute(getColumnNumber(),"Number",true,true,Integer.class);
  }
  public String getEntityName() {
    return(getEntityTableName());
  }
  public int getBuildingId(){
   return getIntColumnValue(getColumnBuildingId());
  }
  public int getKeyId(){
   return getIntColumnValue(getColumnKeyId());
  }
  public String getBuildingName(){
    return getStringColumnValue(getColumnBuildingName());
  }
  public String getKeyName(){
    return getStringColumnValue(getColumnKeyName());
  }
  public String getKeyInfo(){
    return getStringColumnValue(getColumnKeyInfo());
  }
  public float getTotal(){
    return getFloatColumnValue(getColumnTotal());
  }
  public int getNumber(){
    return getIntColumnValue(getColumnNumber());
  }

  public void insert()throws SQLException{

  }
  public void delete()throws SQLException{

  }
}

