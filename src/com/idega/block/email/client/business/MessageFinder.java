package com.idega.block.email.client.business;

import com.idega.presentation.Block;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.search.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class MessageFinder extends Block {

  public MessageFinder() {
  }

  public static List getMessagesInfo(MailUserBean user){
		try{
      FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.DELETED), false);
      Folder f = user.getFolder();
      Message[] msgs = f.search(ft);
      List l = new ArrayList();
      MessageInfo mi;
      for (int i = 0; i < msgs.length; i++) {
        mi = new MessageInfo();
        mi.setMessage(msgs[i]);
        l.add(mi);
        System.err.println("msgnum " + mi.getNum());
      }
      return l;
    }
    catch(Exception ex){
      ex.printStackTrace();
    }

    return null;
  }

  public static Map getMappedMessagesInfo(MailUserBean user){
    return getMappedMessagesInfo(getMessagesInfo(user));

  }

  public static SortedMap getMappedMessagesInfo(List messagesInfo){
    TreeMap m = new TreeMap();
    if(messagesInfo!=null && messagesInfo.size()>0){
      Iterator iter = messagesInfo.iterator();
      while (iter.hasNext()) {
        MessageInfo mi = (MessageInfo) iter.next();
        m.put(new Integer(mi.getNum()),mi);
      }
    }
    return m;
  }
}
