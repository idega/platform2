package is.idega.idegaweb.golf.presentation;

import java.sql.SQLException;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.HeaderTable;

/**
 * Title:        idegaWeb Golf classes
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company: idega software
 * @author <a href="eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.0
 */

public class ForumBox extends Block {

  protected final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.golf";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  private boolean left = false;
  private boolean right = false;


  public ForumBox() {
    setCacheable("ChatBox",3600000);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void setLeft(boolean left){
    this.left = left;
  }

  public void setRight(boolean right){
    this.right = right;
  }

  public void main(IWContext modinfo) throws SQLException{
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);

    HeaderTable table = new HeaderTable();
    table.setBorderColor("#8ab490");
    table.setHeadlineSize(1);
    table.setHeadlineColor("#FFFFFF");

    if( left ){
      table.setRightHeader(false);
    }
    else if( right ){
      table.setLeftHeader(false);
    }




    table.setHeadlineAlign("left");
    table.setWidth(148);
	    table.setHeaderText(iwrb.getLocalizedString("Chat","Chat"));

	    //FIXME Commented out but needs to be fixed...
    /*ForumThread[] forum = (ForumThread[]) (new ForumThread()).findAllByColumnOrdered("parent_thread_id","-1","thread_date desc");

    int links = 4;
    Table myTable = new Table();
    myTable.setWidth("100%");
    myTable.setCellpadding(2);
    myTable.setCellspacing(2);

    if ( forum.length < links ) {
      links = forum.length;
    }

    for (int a = 0; a < links; a++) {
      IWTimestamp stampur = new IWTimestamp(forum[a].getThreadDate());
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

      Link chatLink = new Link(forum[a].getThreadSubject(),"/forum/index.jsp");
      chatLink.setFontSize(1);
      chatLink.addParameter("forum_thread_id",forum[a].getID()+"");
      chatLink.addParameter("forum_id",forum[a].getForumID()+"");
      chatLink.addParameter("state","3");
      chatLink.addParameter("FTopen",forum[a].getID()+"");

      myTable.add(userText,1,a+1);
      myTable.add(chatDate,1,a+1);
      myTable.addBreak(1,a+1);
      myTable.add(chatLink,1,a+1);
    }
    table.add(myTable);*/

    add(table);
  }
}