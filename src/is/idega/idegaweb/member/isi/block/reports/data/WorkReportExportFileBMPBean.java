/*
 * Created on Jul 3, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.core.file.data.ICFile;
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
	
	private static final String UNION_ID = "union_id";
	private static final String CLUB_ID = "club_id";
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
		addManyToOneRelationship(UNION_ID,Group.class);
		addManyToOneRelationship(CLUB_ID,Group.class);
		addManyToOneRelationship(FILE_ID,ICFile.class);
	}
	
	//Get and set methods
	public void setYear(int year) {
		setColumn(YEAR,year);
	}
	
	public int getYear() {
		return getIntColumnValue(YEAR);
	}
	
	public void setUnionId(int unionId) {
		setColumn(UNION_ID,unionId);
	}
	
	public int getUnionId() {
		return getIntColumnValue(UNION_ID);
	}
	
	public void setUnion(Group union) {
		setColumn(UNION_ID,union);
	}
	
	public Group getUnion() {
		return (Group)getColumnValue(UNION_ID);
	}

	public void setClubId(int clubId) {
		setColumn(CLUB_ID,clubId);
	}
	
	public int getClubId() {
		return getIntColumnValue(CLUB_ID);
	}
	
	public void setClub(Group club) {
		setColumn(CLUB_ID,club);
	}
	
	public Group getClub() {
		return (Group)getColumnValue(CLUB_ID);
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
	public Collection ejbFindWorkReportExportFileByUnionIdAndYear(int unionId, int year) throws FinderException{
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getEntityName());
		sql.appendWhereEquals(UNION_ID,unionId);
		sql.appendAndEquals(YEAR,year);

		return this.idoFindIDsBySQL(sql.toString());		
	}	
	
	public Integer ejbFindWorkReportExportFileByClubIdAndYear(int clubId,int year) throws FinderException{
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getEntityName());
		sql.appendWhereEquals(CLUB_ID,clubId);
		sql.appendAndEquals(YEAR,year);

		return (Integer) this.idoFindOnePKBySQL(sql.toString());	
	}	
}