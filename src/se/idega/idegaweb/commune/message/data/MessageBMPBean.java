package se.idega.idegaweb.commune.message.data;

import com.idega.data.*;
import com.idega.block.process.data.*;
import com.idega.core.user.data.User;

import javax.ejb.*;

import java.util.Collection;
import java.util.Iterator;
import java.rmi.RemoteException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */

public class MessageBMPBean extends AbstractCaseBMPBean implements Message,Case{

  private static final String COLUMN_SUBJECT="SUBJECT";
  private static final String COLUMN_BODY="BODY";
  private static final String COLUMN_DATE="TEMP_DATE"; //Temp (test) data
  private static final String COLUMN_SENDER="TEMP_SENDER";//Temp (test) data

  private static final String CASE_CODE_KEY="SYMEDAN";
  private static final String CASE_CODE_DESCRIPTION="Message";

  public MessageBMPBean() {
  }

  public String getEntityName() {
    return "MSG_MESSAGE";
  }

  public void initializeAttributes(){
//    this.addAttribute(this.getIDColumnName());
    addGeneralCaseRelation();
    this.addAttribute(COLUMN_SUBJECT,"Message subject",String.class);
    this.addAttribute(COLUMN_BODY,"Message body",String.class,1000);
    this.addAttribute(COLUMN_DATE,"Test data column",String.class);//temp
    this.addAttribute(COLUMN_SENDER,"Test data column",String.class);//temp
//    this.addManyToManyRelationShip(SampleEntity.class);
  }

  public String getCaseCodeKey(){
    return CASE_CODE_KEY;
  }

  public String getCaseCodeDescription(){
    return CASE_CODE_DESCRIPTION;
  }

  public void insertStartData(){
    try{
      super.insertStartData();
      MessageHome home = (MessageHome)com.idega.data.IDOLookup.getHome(Message.class);
      User administrator = (User)com.idega.data.IDOLookup.findByPrimaryKey(User.class,1);

      Message msg = home.create();
      msg.setSubject("Välkommen till BUN24!");
      msg.setBody("Ditt medborgarkonto är nu redo.");
      msg.setDateX("2002-06-02");
      msg.setSenderNameX("BUN24 Administration");
      msg.setOwner(administrator);
      msg.store();

      msg = home.create();
      msg.setSubject("Barnomsorgscheck mottagen");
      msg.setBody("Barnomsorgschecken för ditt barn Henrik Mickelin har mottagits av anordnare Svanen.");
      msg.setDateX("2002-06-03");
      msg.setSenderNameX("Sonja Westerberg");
      msg.setOwner(administrator);
      msg.store();

      msg = home.create();
      msg.setSubject("Nyheter från BUN");
      msg.setBody("Skolorna profilerar sig för att tillgodose dina önskemål och du som förälder kan välja vilken skola du tycker är bäst och vill att ditt barn ska gå i. Mångfalden berikar, men det stora utbudet gör också valet svårare.");
      msg.setDateX("2002-06-05");
      msg.setSenderNameX("Lars Karlsson");
      msg.setOwner(administrator);
      msg.store();
    }
    catch(Exception e){
      e.printStackTrace(System.out);
    }
  }

  public void setSubject(String subject)throws java.rmi.RemoteException{
    this.setColumn(COLUMN_SUBJECT,subject);
  }

  public String getSubject()throws java.rmi.RemoteException{
    return this.getStringColumnValue(COLUMN_SUBJECT);
  }

  public void setBody(String body)throws java.rmi.RemoteException{
    this.setColumn(COLUMN_BODY,body);
  }

  public String getBody()throws java.rmi.RemoteException{
    return this.getStringColumnValue(COLUMN_BODY);
  }

  public String getDateString()throws java.rmi.RemoteException{
    return this.getStringColumnValue(COLUMN_DATE);//Replace this later
  }

  public void setDateX(String date)throws java.rmi.RemoteException{ //Temp (test) method
    this.setColumn(COLUMN_DATE,date);
  }

  public String getSenderName()throws java.rmi.RemoteException{
    return this.getStringColumnValue(COLUMN_SENDER);//Replace this later
  }

  public void setSenderNameX(String name)throws java.rmi.RemoteException{ //Temp (test) method
    this.setColumn(COLUMN_SENDER,name);
  }

  public Collection ejbFindMessages(int userId)throws FinderException{
    return this.idoFindPKsBySQL("select * from "+this.getEntityName());
  }
}
