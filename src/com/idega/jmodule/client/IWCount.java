

package com.idega.jmodule.client;

import java.lang.String;
import java.util.Date;
import java.io.Serializable;

/**
*@author <a href="mailto:aron@idega.is">Aron Birkir</a>
*@version 1.0
*
*
*/
public class IWCount implements Serializable {
  private int hitcount;
  private String lastIP;
  private String lastDate;
  private String ID;

  public IWCount(String strID){
    ID = strID;
    hitcount = 0;
    lastIP = null;
  }

  public String getID(){
    return ID;
  }

  public String getIP(){
    return lastIP;
  }

  public String getDate(){
    return lastDate;
  }

  public void increment(String strIP){
    if(lastIP != null && lastIP.compareTo( strIP ) != 0)
      hitcount ++;

    lastIP = strIP;
    lastDate = new Date().toString();
  }

  public void increment(){
    hitcount++;
    lastDate = new Date().toString();
  }

  public int getHit(){
    return hitcount;
  }


}
