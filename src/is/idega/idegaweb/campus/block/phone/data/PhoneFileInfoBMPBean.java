package is.idega.idegaweb.campus.block.phone.data;



import java.sql.SQLException;





/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved

 * Company:      idega

  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>

 * @version 1.1

 */



public class PhoneFileInfoBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.campus.block.phone.data.PhoneFileInfo {



  public PhoneFileInfoBMPBean() {

  }

  public PhoneFileInfoBMPBean(int id) throws SQLException{

    super(id);

  }

  public void initializeAttributes() {

    addAttribute(getIDColumnName());

    addAttribute(getColumnNameFileName(),"File Name",true,true,String.class);

    addAttribute(getColumnNameTimeRead(),"Timetamp read",true,true,java.sql.Timestamp.class);

    addAttribute(getColumnNameLineCount(),"Line count",true,true,Integer.class);

    addAttribute(getColumnNameNumberCount(),"Number count",true,true,Integer.class);

    addAttribute(getColumnNameTotalAmount(),"Total amount",true,true,Float.class);

    addAttribute(getColumnNameErrorCount(),"Error count",true,true,Integer.class);

    addAttribute(getColumnNameNoAccountCount(),"No Accounts",true,true,Integer.class);

  }

  public String getEntityName() {

    return getEntityTableName();

  }



  public static String getEntityTableName(){return "CAM_PH_FILE_INFO";}

  public static String getColumnNameFileName(){return "FILE_NAME";}

  public static String getColumnNameTimeRead(){return "READ_DATE";}

  public static String getColumnNameLineCount(){return "LINE_COUNT";}

  public static String getColumnNameNumberCount(){return "NUMBER_COUNT";}

  public static String getColumnNameTotalAmount(){return "TOTAL_AMOUNT";}

  public static String getColumnNameErrorCount(){return "ERROR_COUNT";}

  public static String getColumnNameNoAccountCount(){return "NO_ACCOUNT_COUNT";}





  public void setFileName(String fileName){

    setColumn(getColumnNameFileName(),fileName);

  }

  public String getFileName(){

    return getStringColumnValue(getColumnNameFileName());

  }



  public String getName(){ return getFileName();}



  public int getLineCount(){

    return getIntColumnValue(getColumnNameLineCount());

  }

  public void setLineCount(int count){

    setColumn(getColumnNameLineCount(),count);

  }

  public int getNumberCount(){

    return getIntColumnValue(getColumnNameNumberCount());

  }

  public void setNumberCount(int count){

    setColumn(getColumnNameNumberCount(),count);

  }



  public void setTotalAmount(float amount){

    setColumn(getColumnNameTotalAmount(),amount);

  }

  public float getTotalAmount(){

    return getFloatColumnValue(getColumnNameTotalAmount());

  }

  public void setDateRead(java.sql.Timestamp date){

    setColumn(getColumnNameTimeRead(),date);

  }

  public java.sql.Timestamp getReadTime(){

    return((java.sql.Timestamp)getColumnValue(getColumnNameTimeRead()));

  }

  public int getErrorCount(){

    return getIntColumnValue(getColumnNameErrorCount() );

  }

  public void setErrorCount(int count){

    setColumn(getColumnNameErrorCount(),count);

  }

  public int getNoAccountCount(){

    return getIntColumnValue(getColumnNameNoAccountCount()  );

  }

  public void setNoAccountCount(int count){

    setColumn(getColumnNameNoAccountCount(),count);

  }



}

