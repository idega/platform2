/*
 * $Id: WorkReportExportFile.java,v 1.4 2004/11/27 19:42:01 eiki Exp $
 * Created on Nov 27, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import com.idega.core.file.data.ICFile;
import com.idega.data.IDOEntity;
import com.idega.user.data.Group;


/**
 * 
 *  Last modified: $Date: 2004/11/27 19:42:01 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.4 $
 */
public interface WorkReportExportFile extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileBMPBean#setYear
	 */
	public void setYear(int year);

	/**
	 * @see is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileBMPBean#getYear
	 */
	public int getYear();

	/**
	 * @see is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileBMPBean#setUnionId
	 */
	public void setUnionId(int unionId);

	/**
	 * @see is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileBMPBean#getUnionId
	 */
	public int getUnionId();

	/**
	 * @see is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileBMPBean#setUnion
	 */
	public void setUnion(Group union);

	/**
	 * @see is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileBMPBean#getUnion
	 */
	public Group getUnion();

	/**
	 * @see is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileBMPBean#setClubId
	 */
	public void setClubId(int clubId);

	/**
	 * @see is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileBMPBean#getClubId
	 */
	public int getClubId();

	/**
	 * @see is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileBMPBean#setClub
	 */
	public void setClub(Group club);

	/**
	 * @see is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileBMPBean#getClub
	 */
	public Group getClub();

	/**
	 * @see is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileBMPBean#setFileId
	 */
	public void setFileId(int fileId);

	/**
	 * @see is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileBMPBean#getFileId
	 */
	public int getFileId();

	/**
	 * @see is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileBMPBean#setFile
	 */
	public void setFile(ICFile file);

	/**
	 * @see is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileBMPBean#getFile
	 */
	public ICFile getFile();
}
