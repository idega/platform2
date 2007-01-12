/*
 * Created on Dec 16, 2003
 *
 */
package com.idega.block.text.business;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.text.data.Content;
import com.idega.block.text.data.ContentHome;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.data.LocalizedTextHome;
import com.idega.block.text.data.TxText;
import com.idega.block.text.data.TxTextHome;
import com.idega.business.IBOServiceBean;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.core.component.data.ICObjectInstanceHome;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.util.IWTimestamp;

/**
 * TextServiceBean
 * @author aron 
 * @version 1.0
 */
public class TextServiceBean extends IBOServiceBean implements TextService{
	
	public  Content storeContent(Integer contentId, Integer localizedTextId, Integer localeId, Integer userId, Timestamp tsPublishFrom, Timestamp tsPublishTo, String sHeadline, String sBody, String sTitle, List listOfFiles) {
		return storeContent(contentId, localizedTextId, localeId, userId, tsPublishFrom, tsPublishTo, sHeadline, sBody, sTitle, listOfFiles, null);
	}
	public  Content storeContent(Integer contentId, Integer localizedTextId, Integer localeId, Integer userId, Timestamp tsPublishFrom, Timestamp tsPublishTo, String sHeadline, String sBody, String sTitle, List listOfFiles, Timestamp dateOfContent) {

		javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
		try {
			t.begin();
			boolean ctUpdate = false;
			boolean locUpdate = false;
			Content eContent = null;
			LocalizedText locText = null;
			if (contentId!=null && contentId.intValue() > 0) {
				ctUpdate = true;
				eContent = ((ContentHome) getIDOHome(Content.class)).findByPrimaryKey(contentId);
				if (localizedTextId!=null && localizedTextId.intValue() > 0) {
					locUpdate = true;
					locText = ((LocalizedTextHome) getIDOHome(LocalizedText.class)).findByPrimaryKey(localizedTextId);
				}
				else {
					locUpdate = false;
					locText = ((LocalizedTextHome) getIDOHome(LocalizedText.class)).create();
				}
			}
			else {
				ctUpdate = false;
				locUpdate = false;
				eContent = ((ContentHome) getIDOHome(Content.class)).create();
				if (dateOfContent == null) {
					eContent.setCreated(IWTimestamp.getTimestampRightNow());
				}
				locText = ((LocalizedTextHome) getIDOHome(LocalizedText.class)).create();
			}

			locText.setHeadline(sHeadline);
			locText.setBody(sBody);
			locText.setLocaleId(localeId);
			locText.setTitle(sTitle);
			locText.setUpdated(IWTimestamp.getTimestampRightNow());

			if (tsPublishFrom != null) {
				eContent.setPublishFrom(tsPublishFrom);
			}
			if (tsPublishTo != null) {
				eContent.setPublishTo(tsPublishTo);
			}
			
			if (dateOfContent == null) {
				eContent.setLastUpdated(IWTimestamp.getTimestampRightNow());
			}
			else {
				eContent.setLastUpdated(dateOfContent);
				eContent.setCreated(dateOfContent);
			}

			if (ctUpdate) {
				eContent.store();
				if (locUpdate) {
					locText.store();
				}
				else if (!locUpdate) {
					locText.setCreated(IWTimestamp.getTimestampRightNow());
					locText.store();
				}
			}
			else if (!ctUpdate) {
				eContent.setCreated(IWTimestamp.getTimestampRightNow());
				if (userId!=null && userId.intValue() > 0) {
					eContent.setUserId(userId);
				}
				eContent.store();
				locText.setCreated(IWTimestamp.getTimestampRightNow());
				locText.store();
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
					eContent.addFileToContent(file);
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
	
	public  boolean removeContent(Integer contentId) {
		javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
		try {
			t.begin();
			//  List O = TextFinder.listOfObjectInstanceTexts();
			Content eContent = ((ContentHome) getIDOHome(Content.class)).findByPrimaryKey(contentId);
			List L = ContentFinder.listOfLocalizedText(eContent);
			if (L != null) {
				LocalizedText lt;
				for (int i = 0; i < L.size(); i++) {
					lt = (LocalizedText) L.get(i);
					lt.removeFrom(eContent);
					lt.remove();
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
			eContent.remove();

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
	
	public boolean removeFileFromContent(Integer contentId, Integer fileId) {
		
			try {
				Content content = ((ContentHome) getIDOHome(Content.class)).findByPrimaryKey(contentId);
				content.removeFileFromContent(((ICFileHome) getIDOHome(ICFile.class)).findByPrimaryKey((fileId)));
				return true;
			}
			catch (IDORemoveRelationshipException e) {
				e.printStackTrace();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		
		return false;
	}
	
	public  boolean addFileToContent(Integer contentId, Integer fileId) {
		
			try {
				Content content = ((ContentHome) getIDOHome(Content.class)).findByPrimaryKey(contentId);
				content.addFileToContent(((ICFileHome)getIDOHome(ICFile.class)).findByPrimaryKey( (fileId)));
				return true;
			}
			catch (IDOAddRelationshipException e) {
				e.printStackTrace();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		
		
		return false;
	}
	
	public TxText storeText(Integer textID,Integer localizedTextID,Integer localeID,Integer userID,String headline,String title,String body){
		return storeText(textID,localizedTextID,localeID,userID,null,null,null,headline,title,body,null,null);
	}
	
	public  TxText storeText(Integer textId,
								  Integer localizedTextId,
								  Integer localeId ,
								  Integer userId,
								  Integer instanceId,
								  Timestamp tsPubFrom,
								  Timestamp tsPubTo,
								  String sHeadline,
								  String sTitle,
								  String sBody,
								  String sAttribute,
								  List listOfFiles){


		try {
			boolean update = false;
			TxText eTxText = ((TxTextHome)getIDOHome(TxText.class)).createLegacy();
			if(textId!=null && textId.intValue() > 0){
				eTxText = ((TxTextHome)getIDOHome(TxText.class)).findByPrimaryKey(textId);
				update = true;
			}
			Content eContent = storeContent(new Integer(eTxText.getContentId()),localizedTextId,localeId,userId,tsPubFrom,tsPubTo,sHeadline,sBody,sTitle,listOfFiles);
			if(eContent != null){
				if(sAttribute != null){
					eTxText.setAttribute(sAttribute);
				}
				if(eContent.getID() > 0) {
					eTxText.setContentId(eContent.getID());
				}
				if(update) {
					eTxText.update();
				}
				else {
					eTxText.insert();
				}
				if(instanceId!=null && instanceId.intValue() > 0 && !update){
					//System.err.println("instance er til");
					ICObjectInstance objIns = ((ICObjectInstanceHome)getIDOHome(ICObjectInstance.class)).findByPrimaryKey(instanceId);
					//System.err.println(" object instance "+objIns.getID() + objIns.getName());
					//objIns.removeFrom(new ICCategory());
					eTxText.addTo(objIns);
				}
				return eTxText;
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
