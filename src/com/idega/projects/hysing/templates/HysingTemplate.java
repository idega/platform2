package com.idega.projects.hysing.templates;


import com.idega.projects.hysing.templates.*;
import com.idega.jmodule.*;
import com.idega.presentation.*;
import com.idega.idegaweb.template.*;


public class HysingTemplate extends JSPModule{



        public void initializePage(){
              setPage(new HysingPage());
        }

	public boolean isAdmin()throws Exception{
            return ((TemplatePage)getPage()).isAdministrator(getIWContext());
	}

        // ###########  Public - Föll
	public void add(PresentationObject objectToAdd){
            getPage().add(objectToAdd);
	}

	public void add2(PresentationObject objectToAdd){

			((TemplatePage)getPage()).add2(objectToAdd);

	}

	public void add3(PresentationObject objectToAdd){

			((TemplatePage)getPage()).add3(objectToAdd);

	}

	public void add4(PresentationObject objectToAdd){

			((TemplatePage)getPage()).add4(objectToAdd);

	}

}  // class HysingTemplate
