// idega - Gimmi & Eiki
package com.idega.projects.vf.templates;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.data.*;
import com.idega.util.*;
import com.idega.projects.vf.entity.*;

public abstract class MainTemplate extends JSPModule implements JspPage{

public Table tafla;
public Table frame;

public String language = "IS";

	public void initializePage(){
        super.initializePage();

		Page jmodule = getPage();
		jmodule.setMarginHeight(0);
		jmodule.setMarginWidth(0);
		jmodule.setLeftMargin(0);
		jmodule.setTopMargin(0);
		jmodule.setBackgroundColor("FFFFFF");
    jmodule.setStyleSheetURL("/style/vf.css");
		jmodule.setTitle("Verkfræði og framkvæmdasvið");

		getJavaScript();

    setPage(jmodule);
    jmodule.add(template());

	}


	public Table template() {

		String language2 = getModuleInfo().getRequest().getParameter("language");
			if (language2==null) language2 = ( String ) getModuleInfo().getSession().getAttribute("language");
			if ( language2 != null) language = language2;

		getModuleInfo().setSpokenLanguage( language );

		getModuleInfo().getSession().setAttribute("language",language);

		 frame = new Table(1,1);
			frame.setWidth("100%");
			frame.setAlignment("center");
			frame.setHeight("100%");
			frame.setCellpadding(0);
			frame.setCellspacing(0);
			//frame.setColor("000000");
			frame.setColor(1,1,"FFFFFF");
			frame.setRowVerticalAlignment(1,"top");
			frame.setVerticalAlignment(1,1,"top");

		tafla = new Table(4,2);
			tafla.setCellpadding(0);
			tafla.setCellspacing(0);
			tafla.setAlignment(1,1,"left");
			tafla.setAlignment(2,1,"left");
			tafla.setAlignment(3,1,"center");
                        tafla.setWidth(1,1,"55");
			tafla.setWidth(2,1,"555");
			tafla.setWidth(3,1,"6");
			tafla.setWidth(4,1,"180");
			tafla.setWidth("796");
			tafla.setHeight("100%");
			tafla.setAlignment("top");
			tafla.setVerticalAlignment("top");
			tafla.setVerticalAlignment(1,1,"top");
			tafla.setVerticalAlignment(2,1,"top");
			tafla.setVerticalAlignment(4,1,"top");
			tafla.addBreak(4,1);
			tafla.setBackgroundImage(1,1,new Image("/pics/leftspacer.gif",""));
			tafla.setBackgroundImage(1,2,new Image("/pics/leftspacer.gif",""));
                        tafla.mergeCells(2,2,4,2);
                        tafla.setAlignment(2,2,"right");
                        tafla.add("<br>",2,1);

                        Image topImage = new Image("/pics/bakka.gif");
                        Link topLink = new Link(topImage,"#top");
                           topLink.setSessionId(false);
                        tafla.add(topLink,2,2);

		Table header = new Table(2,2);
			header.setWidth(1,"870");
			header.setHeight(1,"70");
			header.setHeight(2,"20");
			header.mergeCells(1,2,2,2);
			header.setCellpadding(0);
			header.setColor(1,1,"#FFFFFF");
			header.setCellspacing(0);
			header.setWidth("100%");
                        header.setVerticalAlignment(1,1,"top");
			header.setBackgroundImage(1,1,new Image("/pics/header.jpg","Verkfræði og framkvæmdasvið"));
			header.setBackgroundImage(2,1,new Image("/pics/bannert.gif",""));
			header.setBackgroundImage(1,2,new Image("/pics/bar.gif",""));
                        Link topAnchor = new Link("","");
                          topAnchor.setAttribute("name","top");
                        header.add(topAnchor,1,1);

		frame.add(header,1,1);
		frame.add(tafla, 1,1);

		return frame;

	}

	public boolean isAdmin() {
		if (getSession().getAttribute("member_access") != null) {
			if (getSession().getAttribute("member_access").equals("admin")) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	public void add(ModuleObject objectToAdd){
		tafla.add(objectToAdd,2,1);
	}

    public void addLeft(ModuleObject objectToAdd){
		tafla.add(objectToAdd,1,1);
	}

    public void addBreakLeft(){
		tafla.addBreak(1,1);
	}

    public void addRight(ModuleObject objectToAdd){
		tafla.add(objectToAdd,4,1);
	}

    public void addBreakRight(){
		tafla.addBreak(4,1);
	}

	public void merge(){
		tafla.mergeCells(2,1,3,1);
		tafla.setWidth(1,1,"55");
		tafla.setWidth(2,1,"731");
		tafla.setWidth(4,1,"10");
	}

	public void mergeAll(){
		tafla.mergeCells(2,1,3,1);
		tafla.setWidth(1,1,"10");
		tafla.setWidth(4,1,"10");
		tafla.setWidth(2,1,"776");
		tafla.setBackgroundImage(1,1,new Image("",""));
	}

	public void setLeftAlignment(String align) {
		tafla.setAlignment(1,1,align);
	}

	public void setRightAlignment(String align) {
		tafla.setAlignment(4,1,align);
	}

	public void setAlignment(String align) {
		tafla.setAlignment(2,1,align);
	}

	public void setLeftBackground(Image image) {
		tafla.setBackgroundImage(1,1,image);
		tafla.setBackgroundImage(1,2,image);
	}

  private void getJavaScript() {
    try {
      ModuleInfo modinfo = getModuleInfo();
      String projectStatusID = modinfo.getParameter("project_status_id");

      MenuBar menu = new MenuBar();
        menu.setSizes(1,2,1);
        menu.setColors("#000000", "#FFFFFF", "#598221", "#000000", "#FFFFFF", "#000000", "#F4F7EF", "#FFFFFF", "#598221");
        menu.setFonts("Arial", "Helvetica", "sans-serif", "normal", "normal", 8,"Arial", "Helvetica", "sans-serif", "normal", "normal", 8);
        menu.setPosition(0, 70);

      if ( projectStatusID == null ) {
        menu.addMenu("orderMenu",0,131);
          menu.addItem("orderMenu","&nbsp;&nbsp;Skipulag&nbsp;&nbsp;","/stefnumorkun.jsp");
          menu.addItem("orderMenu","&nbsp;Stefnumörkun","/stefnumorkun.jsp");
          menu.addItem("orderMenu","&nbsp;Skipulag","/text/index.jsp?text_id=4");
          menu.addItem("orderMenu","&nbsp;Starfsmenn","/staff.jsp");

        ProjectStatus[] projectStatus = (ProjectStatus[]) ProjectStatus.getStaticInstance("com.idega.projects.vf.entity.ProjectStatus").findAllOrdered("vf_project_status_id");
        for ( int a = 0; a < projectStatus.length; a++ ) {
          menu.addMenu("menu"+Integer.toString(a),0,156);
            menu.addItem("menu"+Integer.toString(a),"&nbsp;&nbsp;"+projectStatus[a].getName()+"&nbsp;&nbsp;","/project.jsp?project_status_id="+Integer.toString(projectStatus[a].getID()));
        }

        menu.addMenu("envMenu",0,125);
          menu.addItem("envMenu","&nbsp;&nbsp;Umhverfismál&nbsp;&nbsp;","/text/index.jsp?text_id=17");
          menu.addItem("envMenu","&nbsp;Lög og stefna","/text/index.jsp?text_id=17");
          menu.addItem("envMenu","&nbsp;Mat á umhverfisáhrifum","/text/index.jsp?text_id=8");
          menu.addItem("envMenu","&nbsp;Fiskirækt","/text/index.jsp?text_id=18");
          menu.addItem("envMenu","&nbsp;Uppgræðsla","/text/index.jsp?text_id=19");

        menu.addMenu("stuffMenu",0,125);
          menu.addItem("stuffMenu","&nbsp;&nbsp;Gagnasöfn&nbsp;&nbsp;","/text/index.jsp?text_id=17");
          menu.addItem("stuffMenu","&nbsp;Fréttasafn","/text/index.jsp?text_id=17");
          menu.addItem("stuffMenu","&nbsp;Myndasafn","/text/index.jsp?text_id=8");
          menu.addItem("stuffMenu","&nbsp;Skýrslusafnt","/text/index.jsp?text_id=18");
      }

      else {
        ProjectStatus projectStatus = new ProjectStatus(Integer.parseInt(projectStatusID));
        menu.addMenu("statusMenu",0,156);
          menu.addItem("statusMenu","&nbsp;&nbsp;"+projectStatus.getName()+"&nbsp;&nbsp;","/project.jsp?project_status_id="+Integer.toString(projectStatus.getID()));

        ProjectCategory[] projectCategory = (ProjectCategory[]) ProjectCategory.getStaticInstance("com.idega.projects.vf.entity.ProjectCategory").findAllOrdered("name");
        for ( int a = 0; a < projectCategory.length; a++ ) {
          menu.addMenu("categoryMenu"+Integer.toString(a),0,156);
            menu.addItem("categoryMenu"+Integer.toString(a),"&nbsp;&nbsp;"+projectCategory[a].getName()+"&nbsp;&nbsp;","/project.jsp?project_status_id="+Integer.toString(projectStatus.getID()));

          ProjectModule[] project = (ProjectModule[]) ProjectModule.getStaticInstance("com.idega.projects.vf.entity.ProjectModule").findAll("select * from vf_project where vf_project_status_id = "+projectStatusID+" and vf_project_category_id = "+Integer.toString(projectCategory[a].getID())+" order by name");
          for ( int b = 0; b < project.length; b++ ) {
            menu.addItem("categoryMenu"+Integer.toString(a),"&nbsp;"+project[b].getName(),"/project.jsp?project_id="+Integer.toString(project[b].getID())+"&project_status_id="+Integer.toString(projectStatus.getID()));
          }
        }
      }

      menu.addMenu("frontMenu",0,156);
        menu.addItem("frontMenu","&nbsp;&nbsp;Forsíða&nbsp;&nbsp;","/index.jsp");

      getPage().add(menu);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

}
