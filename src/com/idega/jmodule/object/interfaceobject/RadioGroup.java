//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object.interfaceobject;

import java.io.*;
import java.util.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.data.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class RadioGroup extends InterfaceObjectContainer{

RadioButton[] buttons;

public RadioGroup(GenericEntity[] entity){
	if (entity != null){
		Table myTable = new Table();
		int length=entity.length;
                buttons = new RadioButton[length];
                for (int i = 0; i < length;i++){
                        buttons[i] = new RadioButton(entity[i].getEntityName(),Integer.toString(entity[i].getID()));
			myTable.add(buttons[i],1,i+1);

			Text texti = new Text(entity[i].getName());
				texti.setFontSize(1);
			myTable.add(texti,2,i+1);
		}
		add(myTable);
	}
}

public void keepStatusOnAction(){
  if (buttons != null){
    for(int i=0;i<buttons.length;i++){
      buttons[i].keepStatusOnAction();
    }
  }
}




}

