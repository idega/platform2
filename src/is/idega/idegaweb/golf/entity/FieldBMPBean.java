//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package is.idega.idegaweb.golf.entity;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class FieldBMPBean extends GenericEntity implements Field{

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("online_startingtime","Leyfa rástímaskráningu á netinu",true,true,"java.lang.Boolean");
		addAttribute("field_type","Vallargerð",false,false,"java.lang.String");
		addAttribute("number_of_holes","Fjöldi hola",true,true,"java.lang.Integer");
		addAttribute("name","Nafn",true,true,"java.lang.String");
		addAttribute("union_id","Eigandi Samband/Klúbbur",true,true,"java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.Union");
		addAttribute("field_par","Par",true,true,"java.lang.Integer");
	}

	public String getEntityName(){
		return "field";
	}

	public boolean getOnlineStartingtime(){
		return getBooleanColumnValue("online_startingtime");
	}
	public void setOnlineStartingtime(boolean inUse){
		setColumn("online_startingtime",new Boolean(inUse));
	}

	public String getFieldType(){
		return getStringColumnValue("field_type");
	}
	public void setFieldType(String fieldType) {
		setColumn("field_type",fieldType);
	}

	public int getNumberOfHoles(){
		return getIntColumnValue("number_of_holes");
	}
	public void setNumberOfHoles(int numberOfHoles) {
		setColumn("number_of_holes",numberOfHoles);
	}
	public void setNumberOfHoles(String numberOfHoles) {
		setColumn("number_of_holes",Integer.parseInt(numberOfHoles));
	}

	public String getName(){
		return getStringColumnValue("name");
	}
	public void setName(String name) {
		setColumn("name",name);
	}

	public int getUnionID(){
		return getIntColumnValue("union_id");
	}
	public void setUnionID(int unionID) {
		setColumn("union_id",unionID);
	}
	public void setUnionID(String unionID) {
		setColumn("union_id",Integer.parseInt(unionID));
	}

	public int getFieldPar(){
		return getIntColumnValue("field_par");
	}
	public void setFieldPar(int fieldPar) {
		setColumn("field_par",fieldPar);
	}
	public void setFieldPar(String fieldPar) {
		setColumn("field_par",Integer.parseInt(fieldPar));
	}
	
	public Collection ejbFindByUnion(Union union) throws FinderException {
		return idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals("union_id",union));
	}

}
