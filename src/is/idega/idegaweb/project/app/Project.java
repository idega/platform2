package is.idega.idegaweb.project.app;

import is.idega.idegaweb.project.presentation.IPCategoryCreator;
import is.idega.idegaweb.project.presentation.IPCategoryTypeCreator;
import is.idega.idegaweb.project.presentation.IPProjectCreator;
import is.idega.idegaweb.project.presentation.IPProjectParticipantGroupCreator;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWConstants;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Page;
import com.idega.presentation.Table;
import com.idega.presentation.app.IWApplication;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;


/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class Project extends IWApplication {

  public static String _FRAME_NAME_HEADER = "accessapp_header";
  public static String _FRAME_NAME_MAIN = "accessapp_main";
  public static String _FRAME_NAME_FOOTER = "accessapp_footer";

  public Project() {
    super("idegaWeb Project",550,370);
    super.add(AppHeader.class);
    super.add(AppMainFrame.class);
    super.add(AppFooter.class);
    super.setFrameName(1,_FRAME_NAME_HEADER);
    super.setFrameName(2,_FRAME_NAME_MAIN);
    super.setFrameName(2,_FRAME_NAME_FOOTER);
    this.setSpanPixels(1,25);
    this.setSpanPixels(3,35);
    this.setScrollbar(false);
    this.setScrolling(1,false);
    this.setScrolling(2,false);
    this.setScrolling(3,false);
  }


  public static class AppHeader extends Page{
    private final static String IW_BUNDLE_IDENTIFIER="com.idega.core";

    public AppHeader(){
      super();
      this.setBackgroundColor("#0E2456");
      this.setAllMargins(0);
    }

    public void main(IWContext iwc) throws Exception {
      IWBundle iwbCore = iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);

      Table headerTable = new Table();
      headerTable.setCellpadding(0);
      headerTable.setCellspacing(0);
      headerTable.setVerticalAlignment("middle");
      headerTable.setWidth("100%");
      headerTable.setAlignment(2,1,"right");
      Image idegaweb = iwbCore.getImage("/editorwindow/idegaweb.gif","idegaWeb");
      headerTable.add(idegaweb,1,1);

      this.add(headerTable);
    }

  }

  public static class AppFooter extends Page{
    public AppFooter(){
      super();
      this.setBackgroundColor(IWConstants.DEFAULT_LIGHT_INTERFACE_COLOR);
      this.setAllMargins(0);
    }

    public void main(IWContext iwc) throws Exception {
      if(iwc.getParameter("close") != null){
        //
      }else{
        Table footerTable = new Table(2,1);
        footerTable.setCellpadding(0);
        footerTable.setCellspacing(0);
        footerTable.setWidth(2,"20");
        footerTable.setAlignment("right");
        footerTable.setHeight("100%");
        footerTable.setVerticalAlignment(1,1,"middle");
        /*SubmitButton close = new SubmitButton(" Close ");
        close.setAttribute("OnClick","parent.close()");*/

        CloseButton close = new CloseButton();

        footerTable.add(close,1,1);
/*
        Form myForm = new Form();
        myForm.add(footerTable);
        this.add(myForm);
  */
        this.add(footerTable);
      }
    }
  }

  public static class AppMainFrame extends Page{
/*
    public static String _FRAME_NAME_1 = "accessapp_main_frame1";
    public static String _FRAME_NAME_2 = "accessapp_main_frame2";
*/
    public AppMainFrame(){
      super();
      Link l = new Link("create project category type");
      l.setWindowToOpen(IPCategoryTypeCreator.class);
      this.add(l);

      this.add(Text.getBreak());

      Link li = new Link("create project category");
      li.setWindowToOpen(IPCategoryCreator.class);
      this.add(li);

      this.add(Text.getBreak());

      Link lin = new Link("create project");
      lin.setWindowToOpen(IPProjectCreator.class);
      this.add(lin);

      this.add(Text.getBreak());

      Link link = new Link("create project participants group");
      link.setWindowToOpen(IPProjectParticipantGroupCreator.class);
      this.add(link);

      /*this.add(ICObjectLinkList.class);
      this.add(IBPermissionFrame.class);
      this.setFrameName(1,_FRAME_NAME_1);
      this.setFrameName(2,_FRAME_NAME_2);
      this.setSpanPixels(1,220);
      this.setHorizontal();
      this.setScrollbar(false);
      this.setScrolling(2,false);*/
    }





  }


}
