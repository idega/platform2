/*
 * Created on 12.7.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.school.data;

/**
 * @author Roar
 *
 */
public interface SchoolSubArea extends com.idega.data.IDOEntity {
	public java.lang.String getSchoolSubAreaName();
	public java.lang.String getName();
	public void initializeAttributes();
	public void setSchoolSubAreaName(java.lang.String p0);	
	public void setSchoolAreaId(int p0);
	public int getSchoolAreaId();	
	public String getSchoolAreaName();	

}
