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
public class EventSource{

HttpServletRequest request;
HttpServletResponse response;

	
	public EventSource(HttpServletRequest request,HttpServletResponse response){
		this.request=request;
		this.response=response;
	}
	
	public boolean equals(PresentationObject modobj){
		if (modobj != null){
			if(request.getParameter(modobj.getName()) != null){
				return true;
			}
			else{
			   if ((request.getParameter(modobj.getName()+".x") != null) ||  (request.getParameter(modobj.getName()+".y") != null)){
               return true;
            }
            else{
             	return false;
	         }
   		}

		}
		else{
			return false;
		}
	}
	

}
