//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.presentation.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class ModuleEvent{

HttpServletRequest request;
HttpServletResponse response;
IWContext iwc;


	public ModuleEvent(IWContext iwc){
                this.iwc = iwc;
		this.request=iwc.getRequest();
		this.response=iwc.getResponse();
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

        public IWContext getIWContext(){
          return this.iwc;
        }

}
