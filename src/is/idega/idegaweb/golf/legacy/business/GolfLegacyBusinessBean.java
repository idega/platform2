/*
 * Created on 1.5.2004
 */
package is.idega.idegaweb.golf.legacy.business;

import is.idega.idegaweb.golf.block.image.data.ImageEntity;
import is.idega.idegaweb.golf.block.text.data.TextModule;
import is.idega.idegaweb.golf.entity.FieldImage;
import is.idega.idegaweb.golf.entity.FieldImageHome;
import is.idega.idegaweb.golf.entity.HoleText;
import is.idega.idegaweb.golf.entity.HoleTextHome;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.TeeImage;
import is.idega.idegaweb.golf.entity.TeeImageHome;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.media.business.MediaBusiness;
import com.idega.block.text.business.TextService;
import com.idega.block.text.data.TxText;
import com.idega.block.text.data.TxTextHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.LoginTableHome;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.core.file.data.ICMimeTypeBMPBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.util.Converter;


/**
 * Title: GolfLegacyBusinessBean
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class GolfLegacyBusinessBean extends IBOServiceBean implements GolfLegacyBusiness{

	public static final String UNION_TYPE_UNION = "golf_union";
	public static final String GROUP_TYPE_UNION = IWMemberConstants.GROUP_TYPE_LEAGUE;
	public static final String UNION_TYPE_CLUB = "golf_club";
	public static final String GROUP_TYPE_CLUB = IWMemberConstants.GROUP_TYPE_CLUB;
	public static final String UNION_TYPE_EXTRA_CLUB = "extra_club";
	
	public static final String UNION_TYPE_NONE = "none";
	
	
	
	public GolfLegacyBusinessBean() {
		super();
	}	
	
	
	public Collection getLogin(is.idega.idegaweb.golf.block.login.data.LoginTable golfLogin) throws IDOLookupException, FinderException{
		return ((LoginTableHome)IDOLookup.getHome(LoginTable.class)).findLoginsForUser(golfLogin.getMember().getICUser());
	}
	
	public is.idega.idegaweb.golf.block.login.data.LoginTable getGolfLogin(LoginTable login) throws EJBException, IDOLookupException, FinderException {
		Member member = ((MemberHome)IDOLookup.getHome(Member.class)).findMemberByIWMemberSystemUser(Converter.convertToNewUser(login.getUser()));
		return ((is.idega.idegaweb.golf.block.login.data.LoginTableHome)IDOLookup.getHomeLegacy(is.idega.idegaweb.golf.block.login.data.LoginTable.class)).findByMember(member);
	}
	
//	public Group getGroup(is.idega.idegaweb.golf.entity.Group golfGroup) {
//		return null;
//	}
//	
//	public is.idega.idegaweb.golf.entity.Group getGolfGroup(Group group){
//		return null;
//	}
	
	
	public void copyAllFromUnionToGroup() {
		System.out.println("[GOLF] Start: Copy all unions to Group...");
		System.out.println("[GOLF] finding all unions");
		
		try {

			Collection unions = ((UnionHome)IDOLookup.getHomeLegacy(Union.class)).findAllUnions();
			System.out.println("[GOLF] Start: create group for unions");
			GroupHome groupHome = (GroupHome)IDOLookup.getHome(Group.class);
			GroupBusiness business = (GroupBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(),GroupBusiness.class);
			
			for (Iterator unionIter = unions.iterator(); unionIter.hasNext();) {
				System.out.print("[GOLF] next union > ");
				Union union = (Union) unionIter.next();
				System.out.println(union.getPrimaryKey());
				
				System.out.println("[GOLF] get group for union");
				
				
				
				Group group = union.getUnionFromIWMemberSystem();
				
				String unionType = union.getUnionType();
				String groupType = null;
				
				if(UNION_TYPE_CLUB.equals(unionType)) {
					groupType=GROUP_TYPE_CLUB;
				} else if(UNION_TYPE_EXTRA_CLUB.equals(unionType)) {
					groupType=GROUP_TYPE_CLUB;
				} else if(UNION_TYPE_UNION.equals(unionType)) {
					groupType=GROUP_TYPE_UNION;
				} else {
					continue;
				}
				
				if(group == null) {
					System.out.println("[GOLF] create new group for union");
					try {
						group = business.createGroup(union.getName(),"",groupType);
					} catch (RemoteException e1) {
						e1.printStackTrace();
						continue;
					}
				}
				
				group.setName(union.getName());
				group.setAbbrevation(union.getAbbrevation());
				group.setShortName(union.getAbbrevation());
				
				
				group.store();
				
				union.setICGroup(group);
				union.store();
				
				
				
				
				//System.out.println("[GOLF] create group.address for union.address");
				//address
				
				
				//System.out.println("[GOLF] create group.phone for union.phone");
				//Phone
				
				
				//System.out.println("[GOLF] create group.group for union.group");
				//Group
				
				
				
			}
			
			
			System.out.println("[GOLF] ... No exceptions");
		} catch (FinderException e) {
				e.printStackTrace();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (IBOLookupException e) {
			e.printStackTrace();
		}
		
		System.out.println("[GOLF] Finish: Copy all unions to Group");
		
	}
	
	
	public void copyHoleTextForFieldOverview(IWContext iwc) {
		System.out.println("[GOLF] Start: Copy HoldeText...");
		try {
			HoleTextHome hth = (HoleTextHome)IDOLookup.getHome(HoleText.class);
			TxTextHome txth = (TxTextHome)IDOLookup.getHome(TxText.class);
			TextService ts = (TextService)IBOLookup.getServiceInstance(iwc,TextService.class);
			
			Collection hTexts = hth.findAll();
			for (Iterator iter = hTexts.iterator(); iter.hasNext();) {
				System.out.println("iterating");
				HoleText ht = (HoleText) iter.next();
				TextModule oldText = ht.getOldText();
				if(ht.getTextID()<1 && oldText != null) {
					try {
						TxText newText = ts.storeText(null,null,new Integer(iwc.getCurrentLocaleId()),new Integer(iwc.getUserId()),oldText.getTextHeadline(),null,oldText.getTextBody());
						ht.setTextID(newText.getID());
						ht.store();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			}
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (IBOLookupException e) {
			e.printStackTrace();
		}
		System.out.println("[GOLF] Finish: Copy HoldeText...");
	}
	
	public void copyFieldImagesForFieldOverview(IWContext iwc) {
		System.out.println("[GOLF] Start: Copy FieldImages...");
		try {
			FieldImageHome fih = (FieldImageHome)IDOLookup.getHome(FieldImage.class);
			ICFileHome fh = (ICFileHome)IDOLookup.getHome(ICFile.class);
			
			
			ICFile folder = fh.create();
			folder.setName("Vallarmyndir");
			folder.setMimeType(com.idega.core.file.data.ICMimeTypeBMPBean.IC_MIME_TYPE_FOLDER);
			folder.setDescription("This is folder for imported images from old golf ImageEntity");
			folder.store();

			MediaBusiness.saveMediaToDB(folder,-1,iwc);
			
			int folderID = ((Integer)folder.getPrimaryKey()).intValue();

			
			Collection fImages = fih.findAll();
			for (Iterator iter = fImages.iterator(); iter.hasNext();) {
				System.out.println("iterating");
				FieldImage fi = (FieldImage) iter.next();
				ImageEntity oldImage = fi.getOldImage();
				if(fi.getImageID()<1 && oldImage != null) {
					try {
						ICFile newImage = fh.create();
						newImage.setFileValue(oldImage.getImageValue());
						String name = oldImage.getName();
						if(name != null) {
							newImage.setName(name);
							newImage.setMimeType((name.indexOf(".gif")>-1)?"image/gif":"image/jpeg");
						}
						newImage.setDescription("This is imported from old golf ImageEntity");
						newImage.store();
						
						MediaBusiness.saveMediaToDB(newImage,folderID,iwc);
						
						fi.setImageID((Integer)newImage.getPrimaryKey());
						fi.store();
						
					} catch (CreateException e1) {
						e1.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
		System.out.println("[GOLF] Finish: Copy FieldImages...");
	}
	
	public void copyTeeImagesForFieldOverview(IWContext iwc) {
		System.out.println("[GOLF] Start: Copy TeeImages...");
		try {
			TeeImageHome fih = (TeeImageHome)IDOLookup.getHome(TeeImage.class);
			ICFileHome fh = (ICFileHome)IDOLookup.getHome(ICFile.class);
			
			ICFile folder = fh.create();
			folder.setName("Brautarmyndir");
			folder.setMimeType(com.idega.core.file.data.ICMimeTypeBMPBean.IC_MIME_TYPE_FOLDER);
			folder.setDescription("This is folder for imported images from old golf ImageEntity");
			folder.store();
			
			MediaBusiness.saveMediaToDB(folder,-1,iwc);
			
			int folderID = ((Integer)folder.getPrimaryKey()).intValue();
			
			Collection fImages = fih.findAll();
			for (Iterator iter = fImages.iterator(); iter.hasNext();) {
				System.out.println("iterating");
				TeeImage fi = (TeeImage) iter.next();
				ImageEntity oldImage = fi.getOldImage();
				if(fi.getImageID()<1 && oldImage != null) {
					try {
						ICFile newImage = fh.create();
						newImage.setFileValue(oldImage.getImageValue());
						String name = oldImage.getName();
						if(name != null) {
							newImage.setName(name);
							newImage.setMimeType((name.indexOf(".gif")>-1)?"image/gif":"image/jpeg");
						}
						newImage.setDescription("This is imported from old golf ImageEntity");
						newImage.store();
						
						MediaBusiness.saveMediaToDB(newImage,folderID,iwc);
						
						fi.setImageID((Integer)newImage.getPrimaryKey());
						fi.store();
						
					} catch (CreateException e1) {
						e1.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
		System.out.println("[GOLF] Finish: Copy TeeImages...");
	}
	
	

}
