package com.idega.projects.hysing.templates;


import com.idega.projects.hysing.templates.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.idegaweb.template.*;


public class HysingTemplate extends JSPModule{



        public void initializePage(){
              setPage(new HysingPage());
        }

	public boolean isAdmin()throws Exception{
            return ((TemplatePage)getPage()).isAdministrator(getModuleInfo());
	}

        // ###########  Public - Föll
	public void add(ModuleObject objectToAdd){
            getPage().add(objectToAdd);
	}

	public void add2(ModuleObject objectToAdd){

			((TemplatePage)getPage()).add2(objectToAdd);

	}

	public void add3(ModuleObject objectToAdd){

			((TemplatePage)getPage()).add3(objectToAdd);

	}

	public void add4(ModuleObject objectToAdd){

			((TemplatePage)getPage()).add4(objectToAdd);

	}

}  // class HysingTemplate
