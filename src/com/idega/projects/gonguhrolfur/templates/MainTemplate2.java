// idega - Gimmi & Eiki
package com.idega.projects.gonguhrolfur.templates;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.data.*;
import java.util.*;
import com.idega.projects.gonguhrolfur.templates.*;

public abstract class MainTemplate2 extends MainTemplate{

      public Table Content() {

        Table myTable = new Table(1,1);
          myTable.setVerticalAlignment(1,1,"top");
          myTable.setBorder(0);
          myTable.setWidth("100%");
          myTable.setHeight("100%");
          myTable.setCellpadding(6);
          myTable.setCellspacing(6);

          myTable.add(Middle(),1,1);

        return myTable;
      }


}
