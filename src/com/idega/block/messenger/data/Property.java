package com.idega.block.messenger.data;



import java.io.Serializable;



public class Property implements Serializable{

  private String key;

  private Object value;



  public Property (String key, Object value){

    this.key = key;

    this.value = value;

  }



  public void setProperty(String key, Object value){

    this.key = key;

    this.value = value;

  }



  public String getKey(){

   return this.key;

  }



  public Object getValue(){

   return this.value;

  }





}
