package com.idega.block.messenger.data;

import java.io.Serializable;

public class Property implements Serializable{
  private String key;
  private String value;

  public Property (String key, String value){
    this.key = key;
    this.value = value;
  }

  public void setProperty(String key, String value){
    this.key = key;
    this.value = value;
  }

  public String getKey(){
   return this.key;
  }

  public String getValue(){
   return this.value;
  }


}