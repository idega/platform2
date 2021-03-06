package com.idega.block.importer.data;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOQuery;

/**
 * Title: com.idega.block.importer.data.ImportHandlerBMPBean Description: A
 * table of available Import handlers Copyright: Idega Software (c) 2002
 * Company: Idega Software http://www.idega.com
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class ImportHandlerBMPBean extends com.idega.data.GenericEntity
		implements ImportHandler {

	public ImportHandlerBMPBean() {
		super();
	}

	public ImportHandlerBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		this.addAttribute(this.getIDColumnName());
		this.addAttribute(getNameColumnName(), "Name", true, true,
				"java.lang.String");
		this.addAttribute(getClassColumnName(), "Class name", true, true,
				"java.lang.String", 500);
		this.addAttribute(getDescriptionColumnName(), "Description", true,
				true, "java.lang.String", 500);
		this.addAttribute(getAutoImpFolderColumnName(),
				"Automatic import folder", true, true, "java.lang.String", 500);
		this.addAttribute(getAutoImpFileTypeColumnName(),
				"Automatic import file type", true, true, "java.lang.String",
				500);
	}

	public String getEntityName() {
		return "im_handler";
	}

	public static String getNameColumnName() {
		return "name";
	}

	public static String getClassColumnName() {
		return "class_name";
	}

	public static String getDescriptionColumnName() {
		return "description";
	}

	public static String getAutoImpFolderColumnName() {
		return "auto_imp_folder";
	}

	public static String getAutoImpFileTypeColumnName() {
		return "auto_imp_file_type";
	}

	public void setName(String name) {
		this.setColumn(getNameColumnName(), name);
	}

	public void setDescription(String description) {
		this.setColumn(getDescriptionColumnName(), description);
	}

	public void setAutoImpFolder(String autoImpFolder) {
		this.setColumn(getAutoImpFolderColumnName(), autoImpFolder);
	}

	public void setAutoImpFileType(String autoImpFileType) {
		this.setColumn(getAutoImpFileTypeColumnName(), autoImpFileType);
	}

	public String getName() {
		return this.getStringColumnValue(getNameColumnName());
	}

	public String getDescription() {
		return this.getStringColumnValue(getDescriptionColumnName());
	}

	public String getAutoImpFolder() {
		return this.getStringColumnValue(getAutoImpFolderColumnName());
	}

	public String getAutoImpFileType() {
		return this.getStringColumnValue(getAutoImpFileTypeColumnName());
	}

	public void setClassName(String className) {
		this.setColumn(getClassColumnName(), className);
	}

	public String getClassName() {
		return this.getStringColumnValue(getClassColumnName());
	}

	public void insertStartData() throws SQLException {
	}

	public Collection ejbFindAllImportHandlers() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}

	public Collection ejbFindAllAutomaticUpdates() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhere();
		query.append(getAutoImpFolderColumnName());
		query.appendIsNotNull();
		return idoFindPKsByQuery(query);
	}

	public Integer ejbFindByClassName(String className) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(
				getClassColumnName(), className);
		return (Integer) idoFindOnePKByQuery(query);
	}

}
