//idega 2001 - Tryggvi Larusson
/*
*Copyright 2001 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object.interfaceobject;

import java.io.*;
import java.util.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*UNIMPLEMENTED
*/

public class Panel extends InterfaceObjectContainer{

private Table table;
private static String imagePrefix = "/common/pics/interfaceobject/FramePane/";

public Panel(){
}

public void add(ModuleObject obj){
  table.add(obj,2,2);
}


public void setWidth(int width){
  table.setWidth(width);
}

public void setHeight(int height){
  table.setHeight(height);
}

}