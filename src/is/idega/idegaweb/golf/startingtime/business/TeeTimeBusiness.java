package is.idega.idegaweb.golf.startingtime.business;



import java.sql.*;

import com.idega.data.*;

import is.idega.idegaweb.golf.entity.*;

import com.idega.util.*;

import com.idega.presentation.ui.*;

import java.io.*;

import com.idega.util.text.TextSoap;

import com.idega.data.EntityFinder;

import java.util.List;

import is.idega.idegaweb.golf.startingtime.data.TeeTime;



/**

 * Title:        Golf<p>

 * Description:  <p>

 * Copyright:    Copyright (c) idega 2000 <p>

 * Company:      idega margmiðlun<p>

 * @author idega 2000 - idega team - gummi

 * @version 1.0

 */





public class TeeTimeBusiness{



  private TeeTime startTime;                 //notaður sem almennur hlutur til að nalgast almenn föll ohað id

  private StartingtimeFieldConfig fieldConfig;    //notaður sem almennur hlutur til að nalgast almenn föll ohað id

  private Field field;		//notaður sem almennur hlutur til að nalgast almenn föll ohað id

  private Union union;		//notaður sem almennur hlutur til að nalgast almenn föll ohað id



//  private Connection conn;





  //  ####  smiðir  #####



  public TeeTimeBusiness(){              //throws SQLException{

  super();

  startTime = ((is.idega.idegaweb.golf.startingtime.data.TeeTimeHome)com.idega.data.IDOLookup.getHomeLegacy(TeeTime.class)).createLegacy();

  fieldConfig = ((is.idega.idegaweb.golf.entity.StartingtimeFieldConfigHome)com.idega.data.IDOLookup.getHomeLegacy(StartingtimeFieldConfig.class)).createLegacy();

  field = ((is.idega.idegaweb.golf.entity.FieldHome)com.idega.data.IDOLookup.getHomeLegacy(Field.class)).createLegacy();

  union = ((is.idega.idegaweb.golf.entity.UnionHome)com.idega.data.IDOLookup.getHomeLegacy(Union.class)).createLegacy();





//  conn = getConnection();

  }





  // #### Private - Föll  ####









  // #### Public - Föll  ####





    // Start.jsp



/*  public int getFirstField(int union_id)throws SQLException{

    return ((Field[])field.findAll("SELECT * FROM " + field.getEntityName() + " WHERE union_id = " + union_id ))[0].getID();

  }*/

  public synchronized int getFirstField(String union_id)throws SQLException{

    try{

      return ((Field[])field.findAll("SELECT * FROM " + field.getEntityName() + " WHERE union_id = " + union_id + " AND  ONLINE_STARTINGTIME='Y' " ))[0].getID();

    }catch(ArrayIndexOutOfBoundsException e){

      return -1;

    }

  }



  // ##%%##%%##%%#%%## var að breyta DESC til að fa fyrstu menn fyrst, kanna hvort virkar eftir að Innskraning virkar

  public synchronized TeeTime[] getTableEntries(String date, int first_group, int last_group, int field_id )throws SQLException{

    return (TeeTime[])startTime.findAll("SELECT * FROM " + startTime.getEntityName() + " WHERE startingtime_date = '" + date + "' and grup_num >= " + first_group + " and grup_num < " + last_group + " and field_id = " + field_id + " order by grup_num DESC, "+startTime.getIDColumnName());

//    return (TeeTime[])startTime.findAll("SELECT * FROM " + startTime.getEntityName() + " WHERE startingtime_date = '" + date + " 00:00:00.0' and grup_num >= " + first_group + " and grup_num < " + last_group + " and field_id = " + field_id + " order by grup_num DESC");

  }



  public synchronized List getStartingtimeTableEntries(IWTimeStamp date, String field_id, int firstGroup, int lastGroup )throws SQLException{

    date.setHour(0);

    date.setMinute(0);

    date.setSecond(0);

    return EntityFinder.findAll(startTime,"SELECT * FROM " + startTime.getEntityName() + " WHERE startingtime_date = '" + date.toSQLDateString() + "' and field_id = " + field_id + " and grup_num >= " + firstGroup + " and grup_num <= " + lastGroup + " order by grup_num DESC, "+startTime.getIDColumnName());

  }



  public synchronized List getStartingtimeTableEntries(IWTimeStamp date, String field_id)throws SQLException{

    date.setHour(0);

    date.setMinute(0);

    date.setSecond(0);

    return EntityFinder.findAll(startTime,"SELECT * FROM " + startTime.getEntityName() + " WHERE startingtime_date = '" + date.toSQLDateString() + "' and field_id = " + field_id + " order by grup_num DESC, "+startTime.getIDColumnName());

  }







  public synchronized Field[] getFields(String union_id)throws SQLException{

    return (Field[])field.findAll("SELECT * FROM " + field.getEntityName() + " WHERE union_id = " + union_id + " AND  ONLINE_STARTINGTIME='Y' ORDER BY " + field.getIDColumnName());

  }





  public synchronized StartingtimeFieldConfig getFieldConfig(int field_id, String date)throws SQLException{

    StartingtimeFieldConfig[] temp = (StartingtimeFieldConfig[])fieldConfig.findAll("SELECT * FROM " + fieldConfig.getEntityName() + " WHERE begin_date <= '"+ date +" 23:59:59.0' and field_id = " + field_id + " ORDER BY begin_date DESC");

    try{

      return temp[0];

    }catch(ArrayIndexOutOfBoundsException e){

      return null;

    }

  }



  public synchronized StartingtimeFieldConfig getFieldConfig(int field_id, IWTimeStamp date)throws SQLException{

    date.setHour(23);

    date.setMinute(59);

    date.setSecond(59);

    StartingtimeFieldConfig[] temp = (StartingtimeFieldConfig[])fieldConfig.findAll("SELECT * FROM " + fieldConfig.getEntityName() + " WHERE begin_date <= '"+ date.toSQLString() +"' and field_id = " + field_id + " ORDER BY begin_date DESC");

    return temp[0];

  }





  // innskraning1.jsp





  public synchronized void preSetStartingtime(int group_num, String date, String field_id)throws SQLException{

    TeeTime insert = ((is.idega.idegaweb.golf.startingtime.data.TeeTimeHome)com.idega.data.IDOLookup.getHomeLegacy(TeeTime.class)).createLegacy();



    insert.setGroupNum(new Integer(group_num));

    insert.setStartingtimeDate( new IWTimeStamp(date).getSQLDate() );

    insert.setFieldID( new Integer(field_id) );



    insert.insert();

  }





  public synchronized void setStartingtime(int group_num, IWTimeStamp date, String field_id, String member_id, String owner_id, String player_name, String handicap, String union, String card, String card_no )throws SQLException{

    TeeTime insert = ((is.idega.idegaweb.golf.startingtime.data.TeeTimeHome)com.idega.data.IDOLookup.getHomeLegacy(TeeTime.class)).createLegacy();



    if(card != null){

      insert.setCardName(card);

    }

    if(card_no != null){

      insert.setCardNum(card_no);

    }



    if(union != null){

      insert.setClubName(union);

    }else{

      insert.setClubName("-");

    }



//    if(field_id != null){

      insert.setFieldID( new Integer(field_id) );

//    }



    insert.setGroupNum(new Integer(group_num));



    try{

      insert.setHandicap(Float.parseFloat(TextSoap.findAndReplace(handicap,",",".")));

    }catch(NumberFormatException e){

      //System.err.println("forgjöf röng : "  );

    }catch(NullPointerException  e){

      //System.err.println("forgjöf null");

    }



    if(member_id != null){

      insert.setMemberID(new Integer(member_id));

    }



    if(member_id != null){

      insert.setOwnerID(new Integer(owner_id));

    }

//    if(date != null){

      insert.setPlayerName(player_name);

//    }

//    if(date != null){

      insert.setStartingtimeDate( date.getSQLDate() );

//    }



    insert.insert();

  }



  public synchronized void setStartingtime(int group_num, IWTimeStamp date, String field_id, String member_id, String player_name, String handicap, String union, String card, String card_no )throws SQLException{

     setStartingtime(group_num, date, field_id, member_id, null, player_name, handicap, union, card, card_no );

  }



  public synchronized void setStartingtime(int group_num, String date, String field_id, String member_id, String player_name, String handicap, String union, String card, String card_no )throws SQLException{

     setStartingtime(group_num, new IWTimeStamp(date), field_id, member_id, player_name, handicap, union, card, card_no );

  }







  public int countEntriesInGroup( int group_num, String field_id, IWTimeStamp date )throws SQLException{

    return this.startTime.getNumberOfRecords("SELECT count(*) FROM " + startTime.getEntityName() + " WHERE grup_num = '" + group_num + "' AND field_id = '" + field_id + "' AND startingtime_date = '" + date.toSQLDateString() + "'");

  }



  public int entriesInGroup( int group_num, String field_id, String date )throws SQLException{

    return countEntriesInGroup( group_num, field_id, new IWTimeStamp(date) );

  }



  public int countOwnersEntries( int owner_id, String field_id, IWTimeStamp date )throws SQLException{

    return this.startTime.getNumberOfRecords("SELECT count(*) FROM " + startTime.getEntityName() + " WHERE owner_id = '" + owner_id + "' AND field_id = '" + field_id + "' AND startingtime_date = '" + date.toSQLDateString() + "'");

  }



  public int countMembersEntries( int member_id, String field_id, IWTimeStamp date )throws SQLException{

    return this.startTime.getNumberOfRecords("SELECT count(*) FROM " + startTime.getEntityName() + " WHERE member_id = '" + member_id + "' AND field_id = '" + field_id + "' AND startingtime_date = '" + date.toSQLDateString() + "'");

  }



  public TeeTime getStartingtime(int member_id, IWTimeStamp date )throws SQLException{

    IDOLegacyEntity[] time = this.startTime.findAllByColumn("member_id",Integer.toString(member_id),"startingtime_date",date.toSQLDateString());

    if(time != null && time.length > 0){

      return (TeeTime)time[0];

    }else{

      return null;

    }

  }





   //  search.jsp



    // nota getTableEntries(String date, int first_group, int last_group, int field_id ) fra start.jsp







  public synchronized Union[] getStartingEntryUnion()throws SQLException{

    return (Union[])union.findAll("SELECT distinct union_.name, union_.union_id FROM union_,field where field.ONLINE_STARTINGTIME='Y' and union_.union_id=field.union_id");

  }





  public synchronized Field[] getStartingEntryField()throws SQLException{

    return (Field[])field.findAll("SELECT * FROM " + field.getEntityName() + " WHERE ONLINE_STARTINGTIME = 'Y' ORDER BY name");

  }



  public synchronized String getFieldName(int field_id)throws SQLException{

    return ((is.idega.idegaweb.golf.entity.FieldHome)com.idega.data.IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKeyLegacy(field_id).getName();

  }



  public synchronized IWTimeStamp getFirstOpentime()throws SQLException{

    return new IWTimeStamp(((StartingtimeFieldConfig[])fieldConfig.findAll("SELECT * FROM " + fieldConfig.getEntityName() + " ORDER BY open_time" ))[0].getOpenTime() );

  }



  public synchronized int getMax_days_shown()throws SQLException{

    return ((StartingtimeFieldConfig[])fieldConfig.findAll("SELECT * FROM " + fieldConfig.getEntityName() + " ORDER BY days_shown" ))[0].getDaysShown();

  }



  public synchronized IWTimeStamp getLastClosetime()throws SQLException{

    return new IWTimeStamp(((StartingtimeFieldConfig[])fieldConfig.findAll("SELECT * FROM " + fieldConfig.getEntityName() + " ORDER BY close_time" ))[0].getCloseTime() );

  }



  public synchronized int get_field_union( int field_id )throws SQLException{

    return ((is.idega.idegaweb.golf.entity.FieldHome)com.idega.data.IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKeyLegacy(field_id).getUnionID();

  }



public TeeTime[] findAllPlayersInFieldOrdered(String field_id, String orderby_clause)throws IOException, SQLException{



	TeeTime stime = ((is.idega.idegaweb.golf.startingtime.data.TeeTimeHome)com.idega.data.IDOLookup.getHomeLegacy(TeeTime.class)).createLegacy();

	TeeTime[] startingtimeimeArray = null;

	try

	{

		startingtimeimeArray = (TeeTime[]) stime.findAll("select * from startingtime where field_id = "+field_id+" and startingtime_date >= '"+IWTimeStamp.RightNow().toSQLDateString()+"' order by "+orderby_clause);

	}

	catch (SQLException E) {

		E.printStackTrace();

	}

	return startingtimeimeArray;

}



public TeeTime[] findAllPlayersByMemberOrdered(String field_id, String member_id, String orderby_clause)throws IOException, SQLException{



	TeeTime stime = ((is.idega.idegaweb.golf.startingtime.data.TeeTimeHome)com.idega.data.IDOLookup.getHomeLegacy(TeeTime.class)).createLegacy();

	TeeTime[] startingtimeimeArray = null;

	try

	{

		startingtimeimeArray = (TeeTime[]) stime.findAll("select * from startingtime where field_id = "+field_id+" and member_id = "+member_id+" and startingtime_date >= '"+IWTimeStamp.RightNow().toSQLDateString()+"' order by "+orderby_clause);

	}

	catch (SQLException E) {

		E.printStackTrace();

	}

	return startingtimeimeArray;

}



public TeeTime[] getPlayersStartingToDay(String columnName, String toFind)throws SQLException

{

	TeeTime stime = ((is.idega.idegaweb.golf.startingtime.data.TeeTimeHome)com.idega.data.IDOLookup.getHomeLegacy(TeeTime.class)).createLegacy();

	TeeTime[] startArray = null;



	try

	{

		startArray = (TeeTime[]) stime.findAll("select * from "+stime.getEntityName()+" where "+columnName+" like '"+toFind+"' and startingtime_date >= '"+IWTimeStamp.RightNow().toSQLDateString()+"'");

	}

	catch (SQLException E) {

		E.printStackTrace();

    }

	return startArray;

}



public TeeTime[] getPlayersStartingToDay(String column1, String toFind1, String column2, String toFind2)throws SQLException

{

	TeeTime stime = ((is.idega.idegaweb.golf.startingtime.data.TeeTimeHome)com.idega.data.IDOLookup.getHomeLegacy(TeeTime.class)).createLegacy();

	TeeTime[] startArray = null;



	try

	{

		startArray = (TeeTime[]) stime.findAll("select * from "+stime.getEntityName()+" where "+column1+" like '"+toFind1+"' and "+column2+" like '"+toFind2+"' and startingtime_date >= '"+IWTimeStamp.RightNow().toSQLDateString()+"'");

	}

	catch (SQLException E) {

		E.printStackTrace();

    }

	return startArray;

}



}   // class StartService



















