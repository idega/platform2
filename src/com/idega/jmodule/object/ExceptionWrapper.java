//idega 2001 - Tryggvi Larusson
/*
*Copyright 2001 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class ExceptionWrapper extends ModuleObjectContainer{

private Exception ex;
private ModuleObject obj;

public ExceptionWrapper(){
}

public ExceptionWrapper(Exception ex,ModuleObject thrower){
    setException(ex);
    setThrower(thrower);
    add("Exception: in "+thrower.getClass().getName()+" <br> "+ex.getClass().getName()+"  "+ex.getMessage());
    ex.printStackTrace(System.err);
}


public void setException(Exception ex){
  this.ex=ex;
}

public void setThrower(ModuleObject obj){
  this.obj=obj;
}


}//End class
