/*
 * Created on Jul 3, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import javax.ejb.FinderException;

import com.idega.core.data.ICFile;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.Group;

/**
 * @author palli
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WorkReportExportFileBMPBean extends GenericEntity implements WorkReportExportFile {
	private static final String ENTITY_NAME = "isi_wr_export";
	
	private static final String GROUP_ID = "ic_group_id";
	private static final String YEAR = "year_of_wr";
	private static final String FILE_ID = "ic_file_id";

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(YEAR,"Year of export",true,true,Integer.class);
		addManyToOneRelationship(GROUP_ID,Group.class);
		addManyToOneRelationship(FILE_ID,ICFile.class);
	}
	
	//Get and set methods
	public void setYear(int year) {
		setColumn(YEAR,year);
	}
	
	public int getYear() {
		return getIntColumnValue(YEAR);
	}
	
	public void setGroupId(int groupId) {
		setColumn(GROUP_ID,groupId);
	}
	
	public int getGroupId() {
		return getIntColumnValue(GROUP_ID);
	}
	
	public void setGroup(Group group) {
		setColumn(GROUP_ID,group);
	}
	
	public Group getGroup() {
		return (Group)getColumnValue(GROUP_ID);
	}
	
	public void setFileId(int fileId) {
		setColumn(FILE_ID,fileId);
	} 
	
	public int getFileId() {
		return getIntColumnValue(FILE_ID);
	}
	
	public void setFile(ICFile file) {
		setColumn(FILE_ID,file);
	}
	
	public ICFile getFile() {
		return (ICFile)getColumnValue(FILE_ID);
	}
	
	//Find functions
	public Integer ejbFindWorkReportExportFileByGroupIdAndYear(int groupId, int year) throws FinderException{
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getEntityName());
		sql.appendWhereEquals(GROUP_ID,groupId);
		sql.appendAndEquals(YEAR,year);
		
		return (Integer) this.idoFindOnePKByQuery(sql);
	}	
}