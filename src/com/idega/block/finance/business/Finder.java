package com.idega.block.finance.business;

import com.idega.block.finance.data.*;
import java.sql.SQLException;
import java.util.List;
import com.idega.util.idegaCalendar;
import com.idega.util.idegaTimestamp;
import com.idega.data.EntityFinder;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class Finder  {

 public static Tariff[] findTariffs(int month,int year){
   Tariff[] tariffs = new Tariff[0];
    try{
      com.idega.util.idegaCalendar ic = new com.idega.util.idegaCalendar();
      int endofmonth = ic.getLengthOfMonth(month,year);
      String from = new idegaTimestamp(1,month,year).getTimestamp().toString();
      String to = new idegaTimestamp(endofmonth,month,year).getTimestamp().toString();
      tariffs = (Tariff[]) (new Tariff().findAllByColumnOrdered("usefrom_date",from,"useto_date",to,"tariff_id"));
    }
    catch(SQLException e){}
    return tariffs;
  }

  public static Tariff[] findTariffs(){
   Tariff[] tariffs = new Tariff[0];
    try{

      tariffs = (Tariff[]) new Tariff().findAll();
    }
    catch(SQLException e){}
    return tariffs;
  }

  public static List listOfTariffs(){

    try{
      return EntityFinder.findAll(new Tariff());

    }
    catch(SQLException e){return null;}
  }

  public static TariffKey[] findTariffKeys(){
   TariffKey[] keys = new TariffKey[0];
    try{
    keys = (TariffKey[]) (new TariffKey()).findAllOrdered(TariffKey.getNameColumnName());
    }
    catch(SQLException e){}
    return keys;
  }

  public static List getAccountKeys(){
    List  L = null;
    try{
      L = EntityFinder.findAll(new AccountKey());
    }
    catch(SQLException e){

    }
    return L;
  }

  public static AccountKey[] findAccountKeys(){
   AccountKey[] keys = new AccountKey[0];
    try{
    keys = (AccountKey[]) (new AccountKey()).findAllOrdered(AccountKey.getNameColumnName());
    }
    catch(SQLException e){}
    return keys;
  }


}// class AccountKeyEditor