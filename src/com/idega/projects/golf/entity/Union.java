//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.projects.golf.entity;

import java.sql.*;
import java.util.*;
import com.idega.data.*;
import com.idega.util.datastructures.idegaTreeNode;
import com.idega.core.ICTreeNode;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class Union extends GenericEntity implements idegaTreeNode,ICTreeNode{

        public static String sClassName = "com.idega.projects.golf.entity.Union";


	public Union(){
		super();
	}

	public Union(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("name", "Félag", true, true, "java.lang.String");
		addAttribute("abbrevation", "Skammstöfun", true, true, "java.lang.String");
		addAttribute("number", "Númer", true, true, "java.lang.Integer");
		addAttribute("union_type", "Tegund", true, true, "java.lang.String");

                      addManyToManyRelationShip("com.idega.projects.golf.entity.Address","union_address");
                      addManyToManyRelationShip("com.idega.projects.golf.entity.Phone","union_phone");
                      addManyToManyRelationShip("com.idega.projects.golf.entity.Group","union_group");

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
              Field field = new Field();
              return EntityFinder.findAll(field,"select * from "+field.getEntityName()+" where "+this.getIDColumnName()+"='"+this.getID()+"'");
	}

	public void delete()throws SQLException{
		Connection conn= null;
		Statement Stmt= null;
		try{
			conn = getConnection();

			Address address = new Address();
			Address[] addresses = (Address[])findReverseRelated(address);

			Stmt = conn.createStatement();
			Stmt.executeUpdate("delete from "+getNameOfMiddleTable(this,address)+" where "+getIDColumnName()+"='"+getID()+"'");

			for(int i = 0; i < addresses.length; i++){
				addresses[i].delete();
			}


			Phone phone = new Phone();
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
      System.err.println("There was an error in Union.getChildren() "+e.getMessage());
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
      System.err.println("There was an error in Union.children() "+e.getMessage());
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
    return new Union(childIndex);
  }
  catch(Exception e){
      System.err.println("There was an error in Union.getChildAt() "+e.getMessage());
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
      return new Union(parent_id);
    }
    else{
      return null;
    }
  }
  catch(Exception e){
      System.err.println("There was an error in Union.getParent() "+e.getMessage());
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
    theReturn = EntityFinder.findAll(new Member(),"select member.* from member m, union_member_info umi where m.member_id = umi.member_id and umi.member_status = 'A' and umi.membership_type = 'main' and umi.union_id = "+this.getID());
  }
  catch(Exception ex){
    ex.printStackTrace(System.err);
  }
  return theReturn;
}

public List getAllActiveMembers(){
  List theReturn=null;
  try{
    theReturn = EntityFinder.findAll(new Member(),"select member.* from member m, union_member_info umi where m.member_id = umi.member_id and umi.member_status = 'A' and umi.membership_type = 'main'");
  }
  catch(Exception ex){
    ex.printStackTrace(System.err);
  }
  return theReturn;
}

public List getGroups(String group_type){
  List theReturn=null;
  try{
    theReturn = EntityFinder.findAll(new Group(),"select group_.* from group_, union_, union_group where group_.group_id = union_group.group_id and union_.union_id = union_group.union_id and group_.group_type='"+group_type+"' and union_group.union_id = "+this.getID());
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
      TournamentGroup group = new TournamentGroup();

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
    theReturn = EntityFinder.findAllByColumn(new TournamentGroup(),"union_id",this.getID());
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
      theReturn = EntityFinder.findAll(new TournamentGroup());
    }
    /**
     * Else the Union returns its own TournamentGroups and all others;
     */
    else{
      idegaTreeNode currentUnion = this;
      TournamentGroup group = new TournamentGroup();

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
                                vector.addElement(new Union(RS.getInt("union_id")));
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
                                vector.addElement(new Union(RS.getInt("union_id")));
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
                                    vector.addElement(new Union(RS.getInt("union_id")));
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
                                    vector.addElement(new Union(RS.getInt("union_id")));
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
                                    vector.addElement(new Union(RS.getInt("union_id")));
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
      return (Union)Union.getStaticInstance(sClassName);
    }


    public void insertStartData()throws Exception{
      Union union = new Union();
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

}
