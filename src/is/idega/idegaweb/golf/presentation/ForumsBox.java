package is.idega.idegaweb.golf.presentation;

import is.idega.idegaweb.golf.entity.Union;

import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.HeaderTable;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.util.idegaTimestamp;

import com.idega.jmodule.forum.data.ForumThread;

/**
 * Title:        idegaWeb Golf
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 2.0
 */

public class ForumsBox extends Block {


  private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";

  public ForumsBox() {
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc)throws Exception{
      setCacheable("GolfForums",3600000);//60*60*1000 1 hour
      add(getChat(iwc));
  }

  protected HeaderTable getChat(IWContext iwc) throws Exception {
    IWResourceBundle iwrb = getBundle(iwc).getResourceBundle(iwc);
    HeaderTable table = new HeaderTable();
    table.setBorderColor("#8ab490");
    table.setHeadlineSize(1);
    table.setHeadlineColor("#FFFFFF");
    table.setRightHeader(false);
    table.setHeadlineAlign("left");
    table.setWidth(148);
    table.setHeaderText(iwrb.getLocalizedString("Chat","Chat"));

    ForumThread[] forum = (ForumThread[]) (new ForumThread()).findAllByColumnOrdered("parent_thread_id","-1","thread_date desc");

    int links = 4;
    Table myTable = new Table();
    myTable.setWidth("100%");
    myTable.setCellpadding(2);
    myTable.setCellspacing(2);

    if ( forum.length < links ) {
      links = forum.length;
    }

    for (int a = 0; a < links; a++) {
      idegaTimestamp stampur = new idegaTimestamp(forum[a].getThreadDate());
      String minutes = stampur.getMinute()+"";
      if ( stampur.getMinute() < 10 ) {
        minutes = "0" + stampur.getMinute();
      }

      Text userText = new Text(forum[a].getUserName()+" - ");
      userText.setFontSize(1);
      userText.setFontColor("#666666");

      Text chatDate = new Text(stampur.getDate()+"/"+stampur.getMonth()+"/"+stampur.getYear()+" "+stampur.getHour()+":"+minutes);
      chatDate.setFontSize(1);
      chatDate.setFontColor("#666666");

      Text text = new Text(forum[a].getThreadSubject());
      text.setFontSize(1);
      Link chatLink = new Link(text,"/forum/");
      chatLink.addParameter("forum_thread_id",forum[a].getID()+"");
      chatLink.addParameter("forum_id",forum[a].getForumID()+"");
      chatLink.addParameter("state","3");
      chatLink.addParameter("FTopen",forum[a].getID()+"");

      myTable.add(userText,1,a+1);
      myTable.add(chatDate,1,a+1);
      myTable.addBreak(1,a+1);
      myTable.add(chatLink,1,a+1);
    }
    table.add(myTable);

    return table;
  }
}
