//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object.interfaceobject;

import java.io.*;
import java.util.*;
import com.idega.jmodule.object.*;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class PasswordInput extends TextInput{

  public PasswordInput(){
          this("untitled");
  }

  public PasswordInput(String name){
          super(name);
          super.setAsPasswordInput();
  }

  public PasswordInput(String name,String content){
          super(name,content);
          super.setAsPasswordInput();
  }
}

