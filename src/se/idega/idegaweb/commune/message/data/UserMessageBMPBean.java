package se.idega.idegaweb.commune.message.data;

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

public class UserMessageBMPBean extends AbstractCaseBMPBean implements UserMessage,Message,Case{

  private static final String COLUMN_SUBJECT="SUBJECT";
  private static final String COLUMN_BODY="BODY";
  private static final String COLUMN_TEMP_SENDER="TEMP_SENDER";
  private static final String COLUMN_DATE="TEMP_DATE";
	private static final String COLUMN_SENDER="SENDER";

  private static final String CASE_CODE_KEY="SYMEDAN";
  private static final String CASE_CODE_DESCRIPTION="User Message";

  public String getEntityName() {
    return "MSG_USER_MESSAGE";
  }

  public void initializeAttributes(){
    addGeneralCaseRelation();
    this.addAttribute(COLUMN_SUBJECT,"Message subject",String.class);
    this.addAttribute(COLUMN_BODY,"Message body",String.class,1000);
    //this.addAttribute(COLUMN_SENDER,"Message sender",Integer.class);//temp
    this.addAttribute(COLUMN_DATE,"Message sender",String.class);//temp
    this.addAttribute(COLUMN_TEMP_SENDER,"Message sender",String.class);//temp
    this.addManyToOneRelationship(COLUMN_SENDER, User.class);
    //this.setNullable(COLUMN_SENDER, true);
  }

  public String getCaseCodeKey(){
    return CASE_CODE_KEY;
  }

  public String getCaseCodeDescription(){
    return CASE_CODE_DESCRIPTION;
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

  public int getSender(){
    return this.getIntColumnValue(COLUMN_SENDER);
  }

  public void setSender(int userID){
    this.setColumn(COLUMN_SENDER,userID);
  }

  public Collection ejbFindMessages(User user)throws FinderException,java.rmi.RemoteException{
    return super.ejbFindAllCasesByUser(user);
  }
 
	public Collection ejbFindMessagesByStatus(User user, String[] status)throws FinderException,java.rmi.RemoteException{
		return super.ejbFindAllCasesByUserAndStatusArray(user, status);
	}
 
}
