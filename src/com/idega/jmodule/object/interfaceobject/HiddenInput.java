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
public class HiddenInput extends Parameter{

public HiddenInput(){
	this("untitled");
}

public HiddenInput(String name){
	this(name,"unspecified");
}

public HiddenInput(String name,String value){
        super(name,value);
	System.err.println("HiddenInput created");
}




}

