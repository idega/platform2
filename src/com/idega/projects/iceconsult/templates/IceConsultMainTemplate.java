// idega - Gimmi & Eiki
package com.idega.projects.iceconsult.templates;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.projects.iceconsult.moduleobject.IceConsultPage;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.data.*;

public abstract class IceConsultMainTemplate extends JSPModule implements JspPage{

public String language = "EN";

	public void initializePage(){
          super.initializePage();
                setPage(new IceConsultPage());
		Page jmodule = getPage();
		jmodule.setMarginHeight(0);
		jmodule.setMarginWidth(0);
		jmodule.setLeftMargin(0);
		jmodule.setTopMargin(0);
		jmodule.setAlinkColor("black");
		jmodule.setVlinkColor("black");
		jmodule.setLinkColor("black");


              String language2 = getModuleInfo().getRequest().getParameter("language");
              if (language2==null) language2 = ( String ) getModuleInfo().getSession().getAttribute("language");
              if ( language2 != null) language = language2;

              getModuleInfo().setSpokenLanguage( language );

              getModuleInfo().getSession().setAttribute("language",language);

                String title = (language.equalsIgnoreCase("IS")) ? "ICEconsult" : "ICEconsult";
                jmodule.setTitle(title);



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


        public void setWidthLeftsidePercent(int width_leftside_percent){
         ((IceConsultPage)getPage()).setWidthLeftsidePercent(width_leftside_percent);
        }


        public void setLeftImage(com.idega.jmodule.object.Image mynd) {
        	((IceConsultPage)getPage()).setLeftImage(mynd);
        }

        public void setRightImage(com.idega.jmodule.object.Image mynd) {
        	((IceConsultPage)getPage()).setRightImage(mynd);
        }

        public void setLeftText(Text text) {
        	((IceConsultPage)getPage()).setLeftText(text);
        }

        public void setRightText(Text text) {
        	((IceConsultPage)getPage()).setRightText(text);
        }

        public void setSpace(int space) {
            ((IceConsultPage)getPage()).setSpace(space);
       }
       	public void setLocation(String loc) {
            ((IceConsultPage)getPage()).setLocation(loc);
        }



  }
