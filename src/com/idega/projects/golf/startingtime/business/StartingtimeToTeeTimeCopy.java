package com.idega.projects.golf.startingtime.business;

import com.idega.projects.golf.entity.Startingtime;
import com.idega.projects.golf.startingtime.data.TeeTime;
import com.idega.data.EntityFinder;
import com.idega.jmodule.object.ModuleInfo;
import java.util.List;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Title:        Golf
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class StartingtimeToTeeTimeCopy {

  public StartingtimeToTeeTimeCopy() {
  }

  public static void putListsInSession(ModuleInfo modinfo) throws SQLException{
    Startingtime stTime = new Startingtime();
    List from = EntityFinder.findAll(stTime,"select * from "+stTime.getEntityName());
    List notFrom = EntityFinder.findAll(stTime,"select * from tournament_round_startingtime trs, startingtime st where trs.startingtime_id = st.startingtime_id");

    modinfo.setSessionAttribute("from",from);
    modinfo.setSessionAttribute("notFrom",from);
  }

  public static void sortLists(ModuleInfo modinfo){
    List from = (List)modinfo.getSessionAttribute("from");
    List notFrom = (List)modinfo.getSessionAttribute("notFrom");

    Vector vector = new Vector();

    for (int i = 0; i < from.size(); i++) {
      if(!notFrom.contains(from.get(i))){
        vector.add(from.get(i));
      }
    }
    System.err.println(vector.size());
    modinfo.setSessionAttribute("vector",vector);


  }


  public static void copy(ModuleInfo modinfo)throws SQLException{
    List toCopy = (List)modinfo.getSessionAttribute("vector");
    TeeTime t = new TeeTime();
    Startingtime s = null;
    System.err.println(toCopy.size());
    for (int i = 0; i < toCopy.size(); i++) {
      s = (Startingtime)toCopy.get(i);

      t.setID(s.getID());
      t.setCardName(s.getCardName());
      t.setCardNum(s.getCardNum());
      t.setClubName(s.getClubName());
      t.setFieldID(s.getFieldID());
      t.setGroupNum(s.getGroupNum());
      t.setHandicap(s.getHandicap());
      t.setMemberID(s.getMemberID());
      t.setOwnerID(s.getOwnerID());
      t.setPlayerName(s.getPlayerName());
      t.setStartingtimeDate(s.getStartingtimeDate());

      t.insert();
    }

    System.err.println();
  }


  public static void remove(ModuleInfo modinfo){
    modinfo.removeSessionAttribute("from");
    modinfo.removeSessionAttribute("notFrom");
    modinfo.removeSessionAttribute("vector");
  }
}