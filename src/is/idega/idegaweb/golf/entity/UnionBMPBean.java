//idega 2000 - Tryggvi Larusson

/*

*Copyright 2000 idega.is All Rights Reserved.

*/



package is.idega.idegaweb.golf.entity;



import java.sql.*;

import java.util.*;

import com.idega.data.*;

import com.idega.util.datastructures.idegaTreeNode;

import com.idega.core.data.ICTreeNode;



/**

*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>

*@version 1.2

*/

public class UnionBMPBean extends com.idega.data.TreeableEntityBMPBean implements is.idega.idegaweb.golf.entity.Union,com.idega.util.datastructures.idegaTreeNode,com.idega.core.data.ICTreeNode {



        public static String sClassName = "is.idega.idegaweb.golf.entity.Union";





	public UnionBMPBean(){

		super();

	}



	public UnionBMPBean(int id)throws SQLException{

		super(id);

	}



	public void initializeAttributes(){

		addAttribute(getIDColumnName());

		addAttribute("name", "Félag", true, true, "java.lang.String");

		addAttribute("abbrevation", "Skammstöfun", true, true, "java.lang.String");

		addAttribute("number", "Númer", true, true, "java.lang.Integer");

		addAttribute("union_type", "Tegund", true, true, "java.lang.String");



                      addManyToManyRelationShip("is.idega.idegaweb.golf.entity.Address","union_address");

                      addManyToManyRelationShip("is.idega.idegaweb.golf.entity.Phone","union_phone");

                      addManyToManyRelationShip("is.idega.idegaweb.golf.entity.Group","union_group");



	}



	public String getEntityName(){

		return "union_";

	}



	public String getIDColumnName(){

		return "union_id";

	}



	public String getName(){

		return getStringColumnValue("name");

	}



	public void setName(String name){

		setColumn("name",name);

	}



	public void setAbbrevation(String abbrevation){

		setColumn("abbrevation", abbrevation);

	}



	public String getAbbrevation(){

		return getStringColumnValue("abbrevation");

	}



	public void setNumber(int number){

		setColumn("number", number);

	}



	public void setNumber(Integer number){

		setColumn("number", number);

	}



	public int getNumber(){

		return getIntColumnValue("number");

	}



	public void setUnionType(String type){

		setColumn("union_type", type);

	}



	public String getUnionType(){

		return getStringColumnValue("union_type");

	}



	public Union[] findAllGolfClubs()throws SQLException{

		return (Union[]) findAll("select * from "+this.getEntityName()+" where union_type='golf_club'");

	}



        /**

         * Same as findAllGolfClubs but uses EntityFinder and returns List

         */

	public List getAllGolfClubs()throws SQLException{

		return EntityFinder.findAll(this,"select * from "+this.getEntityName()+" where union_type='golf_club'");

	}





        public List getOwningFields()throws SQLException{

              Field field = ((is.idega.idegaweb.golf.entity.FieldHome)com.idega.data.IDOLookup.getHomeLegacy(Field.class)).createLegacy();

              return EntityFinder.findAll(field,"select * from "+field.getEntityName()+" where "+this.getIDColumnName()+"='"+this.getID()+"'");

	}



	public void delete()throws SQLException{

		Connection conn= null;

		Statement Stmt= null;

		try{

			conn = getConnection();



			Address address = ((is.idega.idegaweb.golf.entity.AddressHome)com.idega.data.IDOLookup.getHomeLegacy(Address.class)).createLegacy();

			Address[] addresses = (Address[])findReverseRelated(address);



			Stmt = conn.createStatement();

			Stmt.executeUpdate("delete from "+getNameOfMiddleTable(this,address)+" where "+getIDColumnName()+"='"+getID()+"'");



			for(int i = 0; i < addresses.length; i++){

				addresses[i].delete();

			}





			Phone phone = ((is.idega.idegaweb.golf.entity.PhoneHome)com.idega.data.IDOLookup.getHomeLegacy(Phone.class)).createLegacy();

			Phone[] phones = (Phone[])findReverseRelated(phone);

			Stmt = conn.createStatement();

			Stmt.executeUpdate("delete from "+getNameOfMiddleTable(this,phone)+" where "+getIDColumnName()+"='"+getID()+"'");



			for(int i = 0; i < phones.length; i++){

				phones[i].delete();

			}





			super.delete();

		}

		finally{

			if (Stmt != null){

				Stmt.close();

			}

			if (conn != null){

				freeConnection(conn);

			}

		}



	}





/**

 * Returns the children of the reciever as an Iterator. Returns null if no children found

 */

public Iterator getChildren(){

  try{

  List list = EntityFinder.findAll(this,"select * from union_,union_tree where union_.union_id=union_tree.child_union_id and union_tree.union_id='"+this.getID()+"'");

  if(list != null){

    return list.iterator();

  }

  else{

    return null;

  }

  }

  catch(Exception e){

      System.err.println("There was an error in is.idega.idegaweb.golf.entity.UnionBMPBean.getChildren() "+e.getMessage());

      e.printStackTrace(System.err);

      return null;

  }

}



/**

 * Returns the children of the reciever as an Enumeration. Returns null if no children found

 */

public Enumeration children(){

  try{

  List list = EntityFinder.findAll(this,"select * from union_,union_tree where union_.union_id=union_tree.child_union_id and union_tree.union_id='"+this.getID()+"'");

  if(list != null){

    Vector vector = new Vector(list);

    return vector.elements();

  }

  else{

    return null;

  }

  }

  catch(Exception e){

      System.err.println("There was an error in is.idega.idegaweb.golf.entity.UnionBMPBean.children() "+e.getMessage());

      e.printStackTrace(System.err);

      return null;

  }

}

/**

 *  Returns true if the receiver allows children.

 */

public boolean getAllowsChildren(){

  return true;

}



/**

 *  Returns the child TreeNode at index childIndex.

 */

public idegaTreeNode getChildAt(int childIndex){

  try{

    return ((is.idega.idegaweb.golf.entity.UnionHome)com.idega.data.IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKeyLegacy(childIndex);

  }

  catch(Exception e){

      System.err.println("There was an error in is.idega.idegaweb.golf.entity.UnionBMPBean.getChildAt() "+e.getMessage());

      e.printStackTrace(System.err);

      return null;

  }

}



/**

 *    Returns the number of children TreeNodes the receiver contains.

 */

public int getChildCount(){

    return EntityControl.returnSingleSQLQuery(this,"select count(*) from union_tree where union_id='"+this.getID()+"'");

}



/**

 * Returns the index of node in the receivers children.

 */

public int getIndex(idegaTreeNode node){

  return this.getID();

}



/**

 *  Returns the parent TreeNode of the receiver. Return null if none

 */

public idegaTreeNode getParent(){

  try{

    int parent_id = EntityControl.returnSingleSQLQuery(this,"select union_id from union_tree where child_union_id='"+this.getID()+"'");

    if(parent_id!=-1){

      return ((is.idega.idegaweb.golf.entity.UnionHome)com.idega.data.IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKeyLegacy(parent_id);

    }

    else{

      return null;

    }

  }

  catch(Exception e){

      System.err.println("There was an error in is.idega.idegaweb.golf.entity.UnionBMPBean.getParent() "+e.getMessage());

      e.printStackTrace(System.err);

      return null;

  }

}



/**

 *  Returns true if the receiver is a leaf.

 */

public boolean isLeaf(){

  int children = getChildCount();

  if (children > 0){

    return false;

  }

  else{

    return true;

  }

}





public String getNodeName(){

  return getName();

}



public List getMembersInUnion(){

  List theReturn=null;

  try{

    theReturn = EntityFinder.findAll(((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(Member.class)).createLegacy(),"select member.* from member m, union_member_info umi where m.member_id = umi.member_id and umi.member_status = 'A' and umi.membership_type = 'main' and umi.union_id = "+this.getID());

  }

  catch(Exception ex){

    ex.printStackTrace(System.err);

  }

  return theReturn;

}



public List getAllMembersInUnion(){

  List theReturn=null;

  try{

    theReturn = EntityFinder.findAll(((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(Member.class)).createLegacy(),"select member.* from member m, union_member_info umi where m.member_id = umi.member_id and umi.membership_type = 'main' and umi.union_id = "+this.getID());

  }

  catch(Exception ex){

    ex.printStackTrace(System.err);

  }

  return theReturn;

}



public List getAllActiveMembers(){

  List theReturn=null;

  try{

    theReturn = EntityFinder.findAll(((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(Member.class)).createLegacy(),"select member.* from member m, union_member_info umi where m.member_id = umi.member_id and umi.member_status = 'A' and umi.membership_type = 'main'");

  }

  catch(Exception ex){

    ex.printStackTrace(System.err);

  }

  return theReturn;

}



public List getAllInActiveMembers(){

  List theReturn=null;

  try{

    theReturn = EntityFinder.findAll(((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(Member.class)).createLegacy(),"select member.* from member m, union_member_info umi where m.member_id = umi.member_id and umi.member_status = 'I' and umi.membership_type = 'main'");

  }

  catch(Exception ex){

    ex.printStackTrace(System.err);

  }

  return theReturn;

}



public List getActiveMembers(){

  List theReturn=null;

  try{

    theReturn = EntityFinder.findAll(((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(Member.class)).createLegacy(),"select member.* from member m, union_member_info umi where m.member_id = umi.member_id and umi.member_status = 'A' and umi.membership_type = 'main' and umi.union_id = "+this.getID());

  }

  catch(Exception ex){

    ex.printStackTrace(System.err);

  }

  return theReturn;

}



public List getInActiveMembers(){

  List theReturn=null;

  try{

    theReturn = EntityFinder.findAll(((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(Member.class)).createLegacy(),"select member.* from member m, union_member_info umi where m.member_id = umi.member_id and umi.member_status = 'I' and umi.membership_type = 'main' and umi.union_id = "+this.getID());

  }

  catch(Exception ex){

    ex.printStackTrace(System.err);

  }

  return theReturn;

}



public List getGroups(String group_type){

  List theReturn=null;

  try{

    theReturn = EntityFinder.findAll(((is.idega.idegaweb.golf.entity.GroupHome)com.idega.data.IDOLookup.getHomeLegacy(Group.class)).createLegacy(),"select group_.* from group_, union_, union_group where group_.group_id = union_group.group_id and union_.union_id = union_group.union_id and group_.group_type='"+group_type+"' and union_group.union_id = "+this.getID());

  }

  catch(Exception ex){

    ex.printStackTrace(System.err);

  }

  return theReturn;

}





/**

 * Returns a list of UnionGroups which only this Union owns - returns null if no match

 * */

public List getUnionGroups(){

  return getGroups("union_group");

}





/**

 * Returns a list of UnionGroups recursive up the Union tree - returns null if no match

 * */

public List getUnionGroupsRecursive(){

  List theReturn=null;

  try{

    /**

     * If the Union has no parents it returns all the TournamentGroups

     */



      idegaTreeNode currentUnion = this;

      TournamentGroup group = ((is.idega.idegaweb.golf.entity.TournamentGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentGroup.class)).createLegacy();



      while (currentUnion !=null){



        if(theReturn==null){

          theReturn = ((Union)currentUnion).getUnionGroups();

        }

        else{

          theReturn.addAll(((Union)currentUnion).getUnionGroups());

        }

        currentUnion=currentUnion.getParent();

      }

  }

  catch(Exception ex){

    ex.printStackTrace(System.err);

  }

  return theReturn;

}









/**

 * Returns a list of tournamentGroups which only this Union owns - returns null if no match

 * */

public List getTournamentGroups(){

  List theReturn=null;

  try{

    theReturn = EntityFinder.findAllByColumn(((is.idega.idegaweb.golf.entity.TournamentGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentGroup.class)).createLegacy(),"union_id",this.getID());

  }

  catch(Exception ex){

    ex.printStackTrace(System.err);

  }

  return theReturn;

}





/**

 * Returns a list of tournamentGroups recursive up the Union tree - returns null if no match

 * */

public List getTournamentGroupsRecursive(){

  List theReturn=null;

  try{

    /**

     * If the Union has no parents it returns all the TournamentGroups

     */

    if (getParent()==null){

      theReturn = EntityFinder.findAll(((is.idega.idegaweb.golf.entity.TournamentGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentGroup.class)).createLegacy());

    }

    /**

     * Else the Union returns its own TournamentGroups and all others;

     */

    else{

      idegaTreeNode currentUnion = this;

      TournamentGroup group = ((is.idega.idegaweb.golf.entity.TournamentGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentGroup.class)).createLegacy();



      while (currentUnion !=null){



        if(theReturn==null){

          theReturn = ((Union)currentUnion).getTournamentGroups();

        }

        else{

          theReturn.addAll(((Union)currentUnion).getTournamentGroups());

        }

        currentUnion=((Union)currentUnion).getParent();

      }

    }

  }

  catch(Exception ex){

    ex.printStackTrace(System.err);

  }

  return theReturn;

}





    public List getUnionsBetweenZipcodes(Union entity, int zipcodeFrom, int zipcodeTo) throws SQLException {

		Connection conn= null;

		Statement Stmt= null;

                String SQLString = null;

		ResultSetMetaData metaData;

                int code = 0;

                Vector vector=null;

		try{

                        SQLString = "Select union_.union_id,zipcode.code from union_,union_address,address,zipcode WHERE (union_.union_id = union_address.union_id) AND (address.address_id = union_address.address_id) AND (zipcode.zipcode_id = address.zipcode_id) order by name";

			conn = entity.getConnection();

			Stmt = conn.createStatement();

			ResultSet RS = Stmt.executeQuery(SQLString);

			metaData = RS.getMetaData();

			while (RS.next()){

                            code = 0;

                            try {

                                code = Integer.parseInt(RS.getString("code"));

                            }

                            catch (NumberFormatException n) {}

                            if ( (code >= zipcodeFrom) && (code <= zipcodeTo)) {

                                if(vector==null){

                                  vector=new Vector();

                                }

                                vector.addElement(((is.idega.idegaweb.golf.entity.UnionHome)com.idega.data.IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKeyLegacy(RS.getInt("union_id")));

                            }

			}

			RS.close();



		}

		finally{

			if(Stmt != null){

				Stmt.close();

			}

			if (conn != null){

				entity.freeConnection(conn);

			}

		}

		if (vector != null){

			vector.trimToSize();

                        return vector;

		}

		else{

			return null;

		}

	}





    public List getUnionsBetweenZipcodes(Union entity, int zipcodeFrom1, int zipcodeTo1, int zipcodeFrom2, int zipcodeTo2) throws SQLException {

		Connection conn= null;

		Statement Stmt= null;

                String SQLString = null;

		ResultSetMetaData metaData;

                int code = 0;

                Vector vector=null;

		try{

                        SQLString = "Select union_.union_id,zipcode.code from union_,union_address,address,zipcode WHERE (union_.union_id = union_address.union_id) AND (address.address_id = union_address.address_id) AND (zipcode.zipcode_id = address.zipcode_id) order by name";

			conn = entity.getConnection();

			Stmt = conn.createStatement();

			ResultSet RS = Stmt.executeQuery(SQLString);

			metaData = RS.getMetaData();

			while (RS.next()){

                            code = 0;

                            try {

                                code = Integer.parseInt(RS.getString("code"));

                            }

                            catch (NumberFormatException n) {}



                            if ( ((code >= zipcodeFrom1) && (code <= zipcodeTo1)) ||  ((code >= zipcodeFrom2) && (code <= zipcodeTo2))) {

                                if(vector==null){

                                  vector=new Vector();

                                }

                                vector.addElement(((is.idega.idegaweb.golf.entity.UnionHome)com.idega.data.IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKeyLegacy(RS.getInt("union_id")));

                            }

			}

			RS.close();



		}

		finally{

			if(Stmt != null){

				Stmt.close();

			}

			if (conn != null){

				entity.freeConnection(conn);

			}

		}

		if (vector != null){

			vector.trimToSize();

                        return vector;

		}

		else{

			return null;

		}

	}





    public List getUnionsBetweenZipcodes(Union entity, int zipcodeFrom1, int zipcodeTo1, int zipcodeFrom2, int zipcodeTo2,int zipcodeIsNot) throws SQLException {

		Connection conn= null;

		Statement Stmt= null;

                String SQLString = null;

		ResultSetMetaData metaData;

                int code = 0;

                Vector vector=null;

		try{

                        SQLString = "Select union_.union_id,zipcode.code from union_,union_address,address,zipcode WHERE (union_.union_id = union_address.union_id) AND (address.address_id = union_address.address_id) AND (zipcode.zipcode_id = address.zipcode_id) order by name";

			conn = entity.getConnection();

			Stmt = conn.createStatement();

			ResultSet RS = Stmt.executeQuery(SQLString);

			metaData = RS.getMetaData();

			while (RS.next()){

                            code = 0;

                            try {

                                code = Integer.parseInt(RS.getString("code"));

                            }

                            catch (NumberFormatException n) {}

                            if ( ((code >= zipcodeFrom1) && (code <= zipcodeTo1)) ||  ((code >= zipcodeFrom2) && (code <= zipcodeTo2))) {

                                if (code != zipcodeIsNot) {

                                    if(vector==null){

                                      vector=new Vector();

                                    }

                                    vector.addElement(((is.idega.idegaweb.golf.entity.UnionHome)com.idega.data.IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKeyLegacy(RS.getInt("union_id")));

                                }

                            }

			}

			RS.close();



		}

		finally{

			if(Stmt != null){

				Stmt.close();

			}

			if (conn != null){

				entity.freeConnection(conn);

			}

		}

		if (vector != null){

			vector.trimToSize();

                        return vector;

		}

		else{

			return null;

		}

	}





    public List getUnionsBetweenZipcodes(Union entity, int zipcodeFrom1, int zipcodeTo1, int zipcodeFrom2, int zipcodeTo2,int zipcodeIsNot1,  int zipcodeIsNot2) throws SQLException {

		Connection conn= null;

		Statement Stmt= null;

                String SQLString = null;

		ResultSetMetaData metaData;

                int code = 0;

                Vector vector=null;

		try{

                        SQLString = "Select union_.union_id,zipcode.code from union_,union_address,address,zipcode WHERE (union_.union_id = union_address.union_id) AND (address.address_id = union_address.address_id) AND (zipcode.zipcode_id = address.zipcode_id) order by name";

                        System.out.println(zipcodeFrom1 + " " + zipcodeTo1 + " " + zipcodeFrom2 + " " + zipcodeTo2 + " " + zipcodeIsNot1 + " " + zipcodeIsNot2);



			conn = entity.getConnection();

			Stmt = conn.createStatement();

			ResultSet RS = Stmt.executeQuery(SQLString);

			metaData = RS.getMetaData();

			while (RS.next()){

                            code = 0;

                            try {

                                code = Integer.parseInt(RS.getString("code"));

                            }

                            catch (NumberFormatException n) {}

                            if ( ((code >= zipcodeFrom1) && (code <= zipcodeTo1)) ||  ((code >= zipcodeFrom2) && (code <= zipcodeTo2))) {

                                if ((code != zipcodeIsNot1) && (code != zipcodeIsNot2)) {

                                    if(vector==null){

                                      vector=new Vector();

                                    }

                                    vector.addElement(((is.idega.idegaweb.golf.entity.UnionHome)com.idega.data.IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKeyLegacy(RS.getInt("union_id")));

                                }

                            }

			}

			RS.close();



		}

		finally{

			if(Stmt != null){

				Stmt.close();

			}

			if (conn != null){

				entity.freeConnection(conn);

			}

		}

		if (vector != null){

			vector.trimToSize();

                        return vector;

		}

		else{

			return null;

		}

	}





    public List getUnionsBetweenZipcodesOrderBy(Union entity, int zipcodeFrom1, int zipcodeTo1, int zipcodeFrom2, int zipcodeTo2,int zipcodeIsNot1,  int zipcodeIsNot2, String orderBy) throws SQLException {

		Connection conn= null;

		Statement Stmt= null;

                String SQLString = null;

		ResultSetMetaData metaData;

                int code = 0;

                Vector vector=null;

		try{

                        SQLString = "Select union_.union_id,zipcode.code from union_,union_address,address,zipcode WHERE (union_.union_id = union_address.union_id) AND (address.address_id = union_address.address_id) AND (zipcode.zipcode_id = address.zipcode_id) order by "+orderBy;

                        System.out.println(zipcodeFrom1 + " " + zipcodeTo1 + " " + zipcodeFrom2 + " " + zipcodeTo2 + " " + zipcodeIsNot1 + " " + zipcodeIsNot2);



			conn = entity.getConnection();

			Stmt = conn.createStatement();

			ResultSet RS = Stmt.executeQuery(SQLString);

			metaData = RS.getMetaData();

			while (RS.next()){

                            code = 0;

                            try {

                                code = Integer.parseInt(RS.getString("code"));

                            }

                            catch (NumberFormatException n) {}

                            if ( ((code >= zipcodeFrom1) && (code <= zipcodeTo1)) ||  ((code >= zipcodeFrom2) && (code <= zipcodeTo2))) {

                                if ((code != zipcodeIsNot1) && (code != zipcodeIsNot2)) {

                                    if(vector==null){

                                      vector=new Vector();

                                    }

                                    vector.addElement(((is.idega.idegaweb.golf.entity.UnionHome)com.idega.data.IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKeyLegacy(RS.getInt("union_id")));

                                }

                            }

			}

			RS.close();



		}

		finally{

			if(Stmt != null){

				Stmt.close();

			}

			if (conn != null){

				entity.freeConnection(conn);

			}

		}

		if (vector != null){

			vector.trimToSize();

                        return vector;

		}

		else{

			return null;

		}

	}







    public static Union getStaticInstance(){

      return (Union)is.idega.idegaweb.golf.entity.UnionBMPBean.getStaticInstance(sClassName);

    }





    public void insertStartData()throws Exception{

      Union union = ((is.idega.idegaweb.golf.entity.UnionHome)com.idega.data.IDOLookup.getHomeLegacy(Union.class)).createLegacy();

      union.setName("No Union");

      union.insert();

    }





    public int getNodeID(){

      return getID();

    }



    public ICTreeNode getParentNode(){

      return (ICTreeNode)getParent();

    }



    /**

     * Returns the index of node in the receivers children.

     */

    public int getIndex(ICTreeNode node){

      return node.getNodeID();

    }



    public ICTreeNode getChildAtIndex(int index){

      return (ICTreeNode)getChildAt(index);

    }



    /**

     * @todo: Implement

     */

    public int getSiblingCount(){

      return 0;

    }





}

