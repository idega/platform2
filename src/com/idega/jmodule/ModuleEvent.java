//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.jmodule.object.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class ModuleEvent{

HttpServletRequest request;
HttpServletResponse response;
ModuleInfo modinfo;


	public ModuleEvent(ModuleInfo modinfo){
                this.modinfo = modinfo;
		this.request=modinfo.getRequest();
		this.response=modinfo.getResponse();
	}

	public ModuleEvent(HttpServletRequest request,HttpServletResponse response){
		this.request=request;
		this.response=response;
	}

	public EventSource getSource(){
		return new EventSource(request,response);
	}

	public HttpServletRequest getRequest(){
		return request;
	}

	public HttpServletResponse getResponse(){
		return response;
	}

        public ModuleInfo getModuleInfo(){
          return this.modinfo;
        }

}
