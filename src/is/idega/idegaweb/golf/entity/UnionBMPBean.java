//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package is.idega.idegaweb.golf.entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.core.data.ICTreeNode;
import com.idega.data.EntityControl;
import com.idega.data.EntityFinder;
import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.TreeableEntity;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.util.datastructures.idegaTreeNode;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class UnionBMPBean extends GenericEntity implements Union,idegaTreeNode,ICTreeNode{

        public static String sClassName = "is.idega.idegaweb.golf.entity.Union";
        
        private static final String COLUMNNAME_IC_GROUP_ID = "IC_GROUP_ID";

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("name", "Félag", true, true, "java.lang.String");
		addAttribute("abbrevation", "Skammstöfun", true, true, "java.lang.String");
		addAttribute("number", "Númer", true, true, "java.lang.Integer");
		addAttribute("union_type", "Tegund", true, true, "java.lang.String");

	      addManyToManyRelationShip(is.idega.idegaweb.golf.entity.Address.class,"union_address");
	      addManyToManyRelationShip(is.idega.idegaweb.golf.entity.Phone.class,"union_phone");
	      addManyToManyRelationShip(is.idega.idegaweb.golf.entity.Group.class,"union_group");
	      addOneToOneRelationship(COLUMNNAME_IC_GROUP_ID,com.idega.user.data.Group.class);
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
	
	public Collection ejbFindAllUnions() throws FinderException {
		return idoFindAllIDsBySQL();
	}

        /**
         * Same as findAllGolfClubs but uses EntityFinder and returns List
         */
	public List getAllGolfClubs()throws SQLException{
		return EntityFinder.findAll(this,"select * from "+this.getEntityName()+" where union_type='golf_club'");
	}


        public List getOwningFields()throws SQLException{
              Field field = (Field) IDOLookup.instanciateEntity(Field.class);
              return EntityFinder.findAll(field,"select * from "+field.getEntityName()+" where "+this.getIDColumnName()+"='"+this.getID()+"'");
	}

	public void delete()throws SQLException{
		Connection conn= null;
		Statement Stmt= null;
		try{
			conn = getConnection();

			Address address = (Address) IDOLookup.instanciateEntity(Address.class);
			Address[] addresses = (Address[])findReverseRelated(address);

			Stmt = conn.createStatement();
			Stmt.executeUpdate("delete from "+getNameOfMiddleTable(this,address)+" where "+getIDColumnName()+"='"+getID()+"'");

			for(int i = 0; i < addresses.length; i++){
				addresses[i].delete();
			}


			Phone phone = (Phone) IDOLookup.instanciateEntity(Phone.class);
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
public Iterator getChildrenIterator(){
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
    return ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(childIndex);
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
      return ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(parent_id);
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
    theReturn = EntityFinder.findAll((Member) IDOLookup.instanciateEntity(Member.class),"select m.* from member m, union_member_info umi where m.member_id = umi.member_id and umi.member_status = 'A' and umi.membership_type = 'main' and umi.union_id = "+this.getID());
  }
  catch(Exception ex){
    ex.printStackTrace(System.err);
  }
  return theReturn;
}

public List getAllMembersInUnion(){
  List theReturn=null;
  try{
    theReturn = EntityFinder.findAll((Member) IDOLookup.instanciateEntity(Member.class),"select m.* from member m, union_member_info umi where m.member_id = umi.member_id and umi.membership_type = 'main' and umi.union_id = "+this.getID());
  }
  catch(Exception ex){
    ex.printStackTrace(System.err);
  }
  return theReturn;
}

public List getAllActiveMembers(){
  List theReturn=null;
  try{
    theReturn = EntityFinder.findAll((Member) IDOLookup.instanciateEntity(Member.class),"select m.* from member m, union_member_info umi where m.member_id = umi.member_id and umi.member_status = 'A' and umi.membership_type = 'main'");
  }
  catch(Exception ex){
    ex.printStackTrace(System.err);
  }
  return theReturn;
}

public List getAllInActiveMembers(){
  List theReturn=null;
  try{
    theReturn = EntityFinder.findAll((Member) IDOLookup.instanciateEntity(Member.class),"select member.* from member m, union_member_info umi where m.member_id = umi.member_id and umi.member_status = 'I' and umi.membership_type = 'main'");
  }
  catch(Exception ex){
    ex.printStackTrace(System.err);
  }
  return theReturn;
}

public List getActiveMembers(){
  List theReturn=null;
  try{
    theReturn = EntityFinder.findAll((Member) IDOLookup.instanciateEntity(Member.class),"select m.* from member m, union_member_info umi where m.member_id = umi.member_id and umi.member_status = 'A' and umi.membership_type = 'main' and umi.union_id = "+this.getID());
  }
  catch(Exception ex){
    ex.printStackTrace(System.err);
  }
  return theReturn;
}

public List getInActiveMembers(){
  List theReturn=null;
  try{
    theReturn = EntityFinder.findAll((Member) IDOLookup.instanciateEntity(Member.class),"select m.* from member m, union_member_info umi where m.member_id = umi.member_id and umi.member_status = 'I' and umi.membership_type = 'main' and umi.union_id = "+this.getID());
  }
  catch(Exception ex){
    ex.printStackTrace(System.err);
  }
  return theReturn;
}

public List getGroups(String group_type){
  List theReturn=null;
  try{
    theReturn = EntityFinder.findAll((Group) IDOLookup.instanciateEntity(Group.class),"select group_.* from group_, union_, union_group where group_.group_id = union_group.group_id and union_.union_id = union_group.union_id and group_.group_type='"+group_type+"' and union_group.union_id = "+this.getID());
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
      TournamentGroup group = (TournamentGroup) IDOLookup.instanciateEntity(TournamentGroup.class);

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
    theReturn = EntityFinder.findAllByColumn((TournamentGroup) IDOLookup.instanciateEntity(TournamentGroup.class),"union_id",this.getID());
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
      theReturn = EntityFinder.findAll((TournamentGroup) IDOLookup.instanciateEntity(TournamentGroup.class));
    }
    /**
     * Else the Union returns its own TournamentGroups and all others;
     */
    else{
      idegaTreeNode currentUnion = this;
      TournamentGroup group = (TournamentGroup) IDOLookup.instanciateEntity(TournamentGroup.class);

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
                                try {
                                		vector.addElement(((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(RS.getInt("union_id")));
                                }
                                catch (FinderException fe) {
                                		//
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
                                try {
                                		vector.addElement(((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(RS.getInt("union_id")));
                                }
                                catch (FinderException fe) {
                                		//Nothing...
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
                                    try {
                                    		vector.addElement(((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(RS.getInt("union_id")));
                                    }
                                    catch (FinderException fe) {
                                    		//Nothing...
                                    }
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
                                    try {
                                  		vector.addElement(((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(RS.getInt("union_id")));
                                  }
                                  catch (FinderException fe) {
                                  		//Nothing...
                                  }
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
                        SQLString = "select distinct union_.union_id,zipcode.code,"+orderBy+" from union_,union_address,address,zipcode WHERE (union_.union_id = union_address.union_id) AND (address.address_id = union_address.address_id) AND (zipcode.zipcode_id = address.zipcode_id) order by "+orderBy;

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
                                    try {
                                  		vector.addElement(((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(RS.getInt("union_id")));
                                  }
                                  catch (FinderException fe) {
                                  		//Nothing...
                                  }
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
      return (Union) IDOLookup.instanciateEntity(Union.class);
    }


    public void insertStartData()throws Exception{
      Union union = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).create();
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

	/* (non-Javadoc)
	 * @see com.idega.core.data.ICTreeNode#getSiblingCount()
	 */
	public int getSiblingCount() {
		return 0;
	}
	/* (non-Javadoc)
	 * @see com.idega.core.data.ICTreeNode#getNodeName(java.util.Locale)
	 */
	public String getNodeName(Locale locale) {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.data.TreeableEntity#addChild(com.idega.data.TreeableEntity)
	 */
	public void addChild(TreeableEntity p0) throws SQLException {
	}
	/* (non-Javadoc)
	 * @see com.idega.data.TreeableEntity#getChildren(java.lang.String)
	 */
	public Iterator getChildren(String p0) {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.data.TreeableEntity#getParentEntity()
	 */
	public TreeableEntity getParentEntity() {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.data.TreeableEntity#getTreeRelationshipChildColumnName(com.idega.data.TreeableEntity)
	 */
	public String getTreeRelationshipChildColumnName(TreeableEntity p0) {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.data.TreeableEntity#getTreeRelationshipTableName(com.idega.data.TreeableEntity)
	 */
	public String getTreeRelationshipTableName(TreeableEntity p0) {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.data.TreeableEntity#moveChildrenFrom(com.idega.data.TreeableEntity)
	 */
	public void moveChildrenFrom(TreeableEntity p0) throws SQLException {
	}
	/* (non-Javadoc)
	 * @see com.idega.data.TreeableEntity#removeChild(com.idega.data.TreeableEntity)
	 */
	public void removeChild(TreeableEntity p0) throws SQLException {
	}
	/* (non-Javadoc)
	 * @see com.idega.core.data.ICTreeNode#getNodeName(java.util.Locale, com.idega.idegaweb.IWApplicationContext)
	 */
	public String getNodeName(Locale locale, IWApplicationContext iwac) {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.data.TreeableEntity#leafsFirst()
	 */
	public boolean leafsFirst() {
		return false;
	}
	/* (non-Javadoc)
	 * @see com.idega.data.TreeableEntity#setLeafsFirst(boolean)
	 */
	public void setLeafsFirst(boolean b) {
	}
	/* (non-Javadoc)
	 * @see com.idega.data.TreeableEntity#setToSortLeafs(boolean)
	 */
	public void setToSortLeafs(boolean b) {
	}
	/* (non-Javadoc)
	 * @see com.idega.data.TreeableEntity#sortLeafs()
	 */
	public boolean sortLeafs() {
		return false;
	}
	
	public void setICGroup(com.idega.user.data.Group group) {
		setColumn(COLUMNNAME_IC_GROUP_ID,group);
	}
	
	public com.idega.user.data.Group getICGroup() {
		return (com.idega.user.data.Group)getColumnValue(COLUMNNAME_IC_GROUP_ID);
	}
	
	public com.idega.user.data.Group getUnionFromIWMemberSystem() {
		return (com.idega.user.data.Group)getColumnValue(COLUMNNAME_IC_GROUP_ID);
	}
	
	public Object ejbFindUnionByIWMemberSystemGroup(com.idega.user.data.Group union) throws FinderException {
		return idoFindOnePKByQuery(idoQueryGetSelect().appendWhereEquals(COLUMNNAME_IC_GROUP_ID,union));
	}
	
	public Object ejbFindByAbbreviation(String abbr) throws FinderException {
		return idoFindOnePKByColumnBySQL("abbrevation", abbr);
	}
	
}
