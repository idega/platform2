package is.idega.idegaweb.golf.business;

import is.idega.idegaweb.golf.entity.Union;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.presentation.ui.DropdownMenu;

/**
 * Title:        Golf
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class GolfCacher {

  private static List Unions;
  private static Hashtable UnionsHashtable;

  public GolfCacher() {
  }

  private static void initializeUnions(){
    try{
    Union union = (Union) IDOLookup.instanciateEntity(Union.class);
    Unions = EntityFinder.findAll(union,"Select * from " +union.getEntityName() + " order by abbrevation");
    }catch(SQLException e){
      e.printStackTrace();
    }
    UnionsHashtable = new Hashtable();
    Iterator iter = Unions.iterator();
    while (iter.hasNext()) {
      Union union = (Union)iter.next();
      UnionsHashtable.put(Integer.toString(union.getID()),union);
    }
  }

  public static void refetchUnions(){
    initializeUnions();
  }

  public static DropdownMenu getUnionAbbreviationDropdown(String name){
    DropdownMenu mydropdown = new DropdownMenu(name);

    if(Unions == null){
      initializeUnions();
    }

    List myUnions = (List)((Vector)Unions).clone();

    // order myUnions here
    Union union;
    for(int i = 0; i < myUnions.size(); i++){
      union = (Union)myUnions.get(i);
      mydropdown.addMenuElement(union.getID(), union.getAbbrevation());
    }
    return mydropdown;
  }

  public static List getUnions(){
    return Unions;
  }

  public static Union getCachedUnion(int union_id){
    if(UnionsHashtable == null){
      initializeUnions();
    }
    return (Union)UnionsHashtable.get(Integer.toString(union_id));
  }

  public static Union getCachedUnion(String union_id){
    if(UnionsHashtable == null){
      initializeUnions();
    }
    return (Union)UnionsHashtable.get(union_id);
  }
} // Class end