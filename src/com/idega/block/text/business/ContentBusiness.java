package com.idega.block.text.business;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.text.data.Content;
import com.idega.block.text.data.LocalizedText;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.util.IWTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class ContentBusiness {

	public static Content getContent(int iContentId) {
		return ContentFinder.getContent(iContentId);
	}

	public static boolean addFileToContent(int iContentId, int iICFileId) {
		try {
			if (iContentId > 0 && iICFileId > 0) {
				Content content = ((com.idega.block.text.data.ContentHome) com.idega.data.IDOLookup.getHomeLegacy(Content.class)).findByPrimaryKeyLegacy(iContentId);
				content.addFileToContent((ICFile)((com.idega.core.file.data.ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class)).findByPrimaryKey( new Integer(iICFileId)));
				return true;
			}
			else {
				return false;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		} catch (IDOAddRelationshipException e) {
			e.printStackTrace();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean removeFileFromContent(int iContentId, int iICFileId) {
		try {
			Content content = ((com.idega.block.text.data.ContentHome) com.idega.data.IDOLookup.getHomeLegacy(Content.class)).findByPrimaryKeyLegacy(iContentId);
			content.removeFileFromContent(((com.idega.core.file.data.ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class)).findByPrimaryKey(new Integer(iICFileId)));
			return true;
		} catch (IDORemoveRelationshipException e) {
			e.printStackTrace();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean deleteContent(int iContentId) {
		javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
		try {
			t.begin();
			//  List O = TextFinder.listOfObjectInstanceTexts();
			Content eContent = ((com.idega.block.text.data.ContentHome) com.idega.data.IDOLookup.getHomeLegacy(Content.class)).findByPrimaryKeyLegacy(iContentId);
			List L = ContentFinder.listOfLocalizedText(eContent);
			if (L != null) {
				LocalizedText lt;
				for (int i = 0; i < L.size(); i++) {
					lt = (LocalizedText) L.get(i);
					lt.removeFrom(eContent);
					lt.delete();
				}
			}
			L = ContentFinder.listOfContentFiles(eContent);
			if (L != null) {
				ICFile file;
				for (int i = 0; i < L.size(); i++) {
					file = (ICFile) L.get(i);
					eContent.removeFileFromContent(file);
					//file.removeFrom(eContent);
					//file.delete();
				}
			}
			eContent.delete();

			t.commit();
			return true;
		}
		catch (Exception e) {
			try {
				t.rollback();
			}
			catch (javax.transaction.SystemException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
		return false;
	}

	public static Content saveContent(int iContentId, int iLocalizedTextId, int iLocaleId, int iUserId, Timestamp tsPublishFrom, Timestamp tsPublishTo, String sHeadline, String sBody, String sTitle, List listOfFiles) {
		return saveContent(iContentId, iLocalizedTextId, iLocaleId, iUserId, tsPublishFrom, tsPublishTo, sHeadline, sBody, sTitle, listOfFiles, null);
	}
	
	public static Content saveContent(int iContentId, int iLocalizedTextId, int iLocaleId, int iUserId, Timestamp tsPublishFrom, Timestamp tsPublishTo, String sHeadline, String sBody, String sTitle, List listOfFiles, Timestamp dateOfContent) {

		javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
		try {
			t.begin();
			boolean ctUpdate = false;
			boolean locUpdate = false;
			Content eContent = null;
			LocalizedText locText = null;
			if (iContentId > 0) {
				ctUpdate = true;
				eContent = ((com.idega.block.text.data.ContentHome) com.idega.data.IDOLookup.getHomeLegacy(Content.class)).findByPrimaryKeyLegacy(iContentId);
				if (iLocalizedTextId > 0) {
					locUpdate = true;
					locText = ((com.idega.block.text.data.LocalizedTextHome) com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).findByPrimaryKeyLegacy(iLocalizedTextId);
				}
				else {
					locUpdate = false;
					locText = ((com.idega.block.text.data.LocalizedTextHome) com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
				}
			}
			else {
				ctUpdate = false;
				locUpdate = false;
				eContent = ((com.idega.block.text.data.ContentHome) com.idega.data.IDOLookup.getHomeLegacy(Content.class)).createLegacy();
				if (dateOfContent == null)
					eContent.setCreated(IWTimestamp.getTimestampRightNow());
				locText = ((com.idega.block.text.data.LocalizedTextHome) com.idega.data.IDOLookup.getHomeLegacy(LocalizedText.class)).createLegacy();
			}

			locText.setHeadline(sHeadline);
			locText.setBody(sBody);
			locText.setLocaleId(iLocaleId);
			locText.setTitle(sTitle);
			locText.setUpdated(IWTimestamp.getTimestampRightNow());

			if (tsPublishFrom != null)
				eContent.setPublishFrom(tsPublishFrom);
			if (tsPublishTo != null)
				eContent.setPublishTo(tsPublishTo);
			
			if (dateOfContent == null)
				eContent.setLastUpdated(IWTimestamp.getTimestampRightNow());
			else {
				eContent.setLastUpdated(dateOfContent);
				eContent.setCreated(dateOfContent);
			}

			if (ctUpdate) {
				eContent.update();
				if (locUpdate) {
					locText.update();
				}
				else if (!locUpdate) {
					locText.setCreated(IWTimestamp.getTimestampRightNow());
					locText.insert();
				}
			}
			else if (!ctUpdate) {
				eContent.setCreated(IWTimestamp.getTimestampRightNow());
				if (iUserId > 0)
					eContent.setUserId(iUserId);
				eContent.insert();
				locText.setCreated(IWTimestamp.getTimestampRightNow());
				locText.insert();
			}

			t.commit();

			/*try {
				locText.addTo(eContent);
			}catch (Exception sql) {
				sql.printStackTrace(System.err);
			}*/
			if (ctUpdate) {
				if (!locUpdate) {
					locText.addTo(eContent);
				}
			}
			else if (!ctUpdate) {
				locText.addTo(eContent);
			}

			if (listOfFiles != null) {
				Iterator I = listOfFiles.iterator();
				while (I.hasNext()) {
					ICFile file = (ICFile) I.next();
					try {
						eContent.addFileToContent(file);
					} catch (IDOAddRelationshipException e) {
						// ICFile is already connected to Content
					}
				}
			}

			return eContent;
		}
		catch (Exception e) {
			try {
				t.rollback();
			}
			catch (javax.transaction.SystemException ex) {
				ex.printStackTrace();
			}

			e.printStackTrace();
		}

		return null;
	}
}
