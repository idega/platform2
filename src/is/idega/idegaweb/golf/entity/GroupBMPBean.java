//idega 2000 - Tryggvi Larusson

/*

*Copyright 2000 idega.is All Rights Reserved.

*/



package is.idega.idegaweb.golf.entity;



//import java.util.*;

import java.sql.*;



/**

*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>

*@version 1.2

*/

public class GroupBMPBean extends com.idega.data.genericentity.GroupBMPBean implements Group {



	public GroupBMPBean(){

		super();

	}



	public GroupBMPBean(int id)throws SQLException{

		super(id);

	}



      public void initializeAttributes(){

          super.initializeAttributes();

          addManyToManyRelationShip("is.idega.idegaweb.golf.entity.Union","union_group");

      }



        public static String getClassName(){

          return "com.idega.project.golf.entity.Group";

        }











/**

 * does not extend GolfEntity anymore

 */



}

