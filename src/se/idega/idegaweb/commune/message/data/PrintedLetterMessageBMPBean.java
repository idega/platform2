package se.idega.idegaweb.commune.message.data;

import com.idega.core.data.ICFile;
import com.idega.data.*;
import com.idega.block.process.data.*;
import com.idega.user.data.User;

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

public class PrintedLetterMessageBMPBean extends AbstractCaseBMPBean implements PrintedLetterMessage,Message,Case{

  private static final String COLUMN_SUBJECT="SUBJECT";
  private static final String COLUMN_BODY="BODY";
  private static final String COLUMN_MESSAGE_TYPE="MESSAGE_TYPE";
  private static final String COLUMN_MESSAGE_DATA="MESSAGE_DATA";

  private static final String CASE_CODE_KEY="SYMEBRV";
  private static final String CASE_CODE_DESCRIPTION="Letter Message";

  public String getEntityName() {
    return "MSG_LETTER_MESSAGE";
  }

  public void initializeAttributes(){
//    this.addAttribute(this.getIDColumnName());
    addGeneralCaseRelation();
    this.addAttribute(COLUMN_SUBJECT,"Message subject",String.class);
    this.addAttribute(COLUMN_BODY,"Message body",String.class,1000);
    this.addAttribute(COLUMN_MESSAGE_TYPE,"Message type",String.class,20);
    this.addManyToOneRelationship(COLUMN_MESSAGE_DATA,"Message data",ICFile.class);

    //this.addAttribute(COLUMN_DATE,"Test data column",String.class);//temp
    //this.addAttribute(COLUMN_SENDER,"Test data column",String.class);//temp
	//this.addManyToManyRelationShip(SampleEntity.class);
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
      /*
	  UserMessageHome home = (UserMessageHome)com.idega.data.IDOLookup.getHome(UserMessage.class);
      User administrator = (User)com.idega.data.IDOLookup.findByPrimaryKey(User.class,1);

      Message msg = home.create();
      msg.setSubject("Välkommen till BUN24!");
      msg.setBody("Ditt medborgarkonto är nu redo.");
      //msg.setDateX("2002-06-02");
      //msg.setSenderNameX("BUN24 Administration");
      msg.setOwner(administrator);
      msg.store();

      msg = home.create();
      msg.setSubject("Barnomsorgscheck mottagen");
      msg.setBody("Barnomsorgschecken för ditt barn Henrik Mickelin har mottagits av anordnare Svanen.");
      //msg.setDateX("2002-06-03");
      //msg.setSenderNameX("Sonja Westerberg");
      msg.setOwner(administrator);
      msg.store();

      msg = home.create();
      msg.setSubject("Nyheter från BUN");
      msg.setBody("Skolorna profilerar sig för att tillgodose dina önskemål och du som förälder kan välja vilken skola du tycker är bäst och vill att ditt barn ska gå i. Mångfalden berikar, men det stora utbudet gör också valet svårare.");
      //msg.setDateX("2002-06-05");
      //msg.setSenderNameX("Lars Karlsson");
      msg.setOwner(administrator);
      msg.store();
	  */
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

  public String getMessageType()throws java.rmi.RemoteException{
    return this.getStringColumnValue(COLUMN_MESSAGE_TYPE);
  }

  public void setMessageType(String type)throws java.rmi.RemoteException{
    this.setColumn(COLUMN_MESSAGE_TYPE,type);
  }

  public ICFile getMessageData()throws java.rmi.RemoteException{
    return (ICFile)this.getColumnValue(COLUMN_MESSAGE_DATA);//Replace this later
  }

   public int getMessageDataFileID()throws java.rmi.RemoteException{
    return this.getIntColumnValue(COLUMN_MESSAGE_DATA);
  }

  public void setMessageData(ICFile file)throws java.rmi.RemoteException{ //Temp (test) method
    this.setColumn(COLUMN_MESSAGE_DATA,file);
  }

  public void setMessageData(int fileID)throws java.rmi.RemoteException{ //Temp (test) method
    this.setColumn(COLUMN_MESSAGE_DATA,fileID);
  }

  public String getSenderName(){
  	try
	{
		return getOwner().getName();
	}
	catch (RemoteException e)
	{
		return "";
	}
  }

  public String getDateString(){
  	/**
  	 * @todo: implement
  	 */
  	return "";
  }


  public Collection ejbFindMessages(User user)throws FinderException,java.rmi.RemoteException{
    return super.ejbFindAllCasesByUser(user);
  }

  public Collection ejbFindAllUnPrintedLetters()throws FinderException,java.rmi.RemoteException{
    return super.ejbFindAllCasesByStatus(super.getCaseStatusOpen());
  }

  public Collection ejbFindAllPrintedLetters()throws FinderException,java.rmi.RemoteException{
    return super.ejbFindAllCasesByStatus(super.getCaseStatusReady());
  }

  public int ejbHomeGetNumberOfPrintedLetters()throws java.rmi.RemoteException{
    return super.ejbHomeCountCasesWithStatus(getCaseStatusReady());
  }

  public int ejbHomeGetNumberOfUnPrintedLetters()throws java.rmi.RemoteException{
    return super.ejbHomeCountCasesWithStatus(getCaseStatusOpen());
  }

}
