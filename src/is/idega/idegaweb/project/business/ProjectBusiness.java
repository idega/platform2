package is.idega.idegaweb.project.business;

import is.idega.idegaweb.project.data.IPCategory;
import is.idega.idegaweb.project.data.IPCategoryType;
import is.idega.idegaweb.project.data.IPParticipantGroup;
import is.idega.idegaweb.project.data.IPProject;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.idega.builder.business.BuilderConstants;
import com.idega.builder.dynamicpagetrigger.business.DPTCopySession;
import com.idega.builder.dynamicpagetrigger.business.DPTTriggerBusiness;
import com.idega.builder.dynamicpagetrigger.business.DPTTriggerBusinessBean;
import com.idega.builder.dynamicpagetrigger.data.PageLink;
import com.idega.builder.dynamicpagetrigger.data.PageTriggerInfo;
import com.idega.business.IBOLookup;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.core.accesscontrol.data.ICPermission;
import com.idega.core.builder.business.BuilderService;
import com.idega.core.builder.business.BuilderServiceFactory;
import com.idega.core.data.GenericGroup;
import com.idega.core.data.ICTreeNode;
import com.idega.data.EntityFinder;
import com.idega.data.IDOAddRelationshipException;
import com.idega.event.EventLogic;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;

/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Gudmundur Agust Saemundsson</a>
 * @version 1.0
 */

public class ProjectBusiness {

  public final static String _PRM_UPDATE = "ip_update";
  public final static String _PRM_DELETE = "ip_delete";
  public final static String _APPADDRESS_PROJECTPAGES = "IP_PAGE_MAP";
  public final static String IW_PROJECT_IDENTIFIER = "is.idega.idegaweb.project";

  public static int tmpHardcodedPageTriggerInfoId = 1;

  public ProjectBusiness() {
  }


  public static ProjectBusiness getInstance(){
    return new ProjectBusiness();
  }

  public List getCategoryTypes()throws SQLException{
    return EntityFinder.findAll(is.idega.idegaweb.project.data.IPCategoryTypeBMPBean.getStaticInstance(IPCategoryType.class));
  }

  public List getCategories(int catTypeId) throws SQLException {
    return EntityFinder.findAllByColumn(is.idega.idegaweb.project.data.IPCategoryBMPBean.getStaticInstance(IPCategory.class),is.idega.idegaweb.project.data.IPCategoryBMPBean._COLUMN_TYPE_ID,catTypeId);
  }

  public List getProjects() throws SQLException {
    return EntityFinder.findAll(is.idega.idegaweb.project.data.IPProjectBMPBean.getStaticInstance(IPProject.class));
  }
/*
  public List getProjectDPTPageLinks(int[] catTypes) throws Exception{
    PageLink staticPLink = com.idega.builder.dynamicpagetrigger.data.PageLinkBMPBean.getStaticInstance(PageLink.class);
    if(catTypes != null){
      return EntityFinder.findAll(staticPLink);
    }else{
      return EntityFinder.findAll(staticPLink,"select * from "+staticPLink.getEntityName()+" where ");
    }
    // wrong dpt_??ID not objectID return EntityFinder.findAllByColumn(com.idega.builder.dynamicpagetrigger.data.PageLinkBMPBean.getStaticInstance(PageLink.class),com.idega.builder.dynamicpagetrigger.data.PageLinkBMPBean._COLUMNNAME_REFERENCED_DATA_ID,com.idega.core.data.ICObjectBMPBean.getICObject(IPProject.class.getName()).getID());
  }
*/

  public List getProjectDPTPageLinks(int[] catTypes)throws SQLException{
    PageLink staticPLink = (PageLink)com.idega.builder.dynamicpagetrigger.data.PageLinkBMPBean.getStaticInstance(PageLink.class);
    IPProject staticIPProject = (IPProject)is.idega.idegaweb.project.data.IPProjectBMPBean.getStaticInstance(IPProject.class);
    IPCategory staticIPCategory = (IPCategory)is.idega.idegaweb.project.data.IPCategoryBMPBean.getStaticInstance(IPCategory.class);

    String tableToSelectFrom = staticIPProject.getNameOfMiddleTable(staticPLink,staticIPProject);
    String projectCategoryMiddleTable = staticIPProject.getNameOfMiddleTable(staticIPCategory,staticIPProject);
    StringBuffer buffer=new StringBuffer();
    buffer.append("select e.* from ");
    buffer.append(tableToSelectFrom);
    buffer.append(" middle, ");
    buffer.append(staticPLink.getEntityName());
    buffer.append(" e, ");
    buffer.append(staticIPProject.getEntityName());
    buffer.append(" p");
    buffer.append(" where ");
    buffer.append("middle.");
    buffer.append(staticIPProject.getIDColumnName());
    buffer.append(" = p.");
    buffer.append(staticIPProject.getIDColumnName());
    buffer.append(" and middle.");
    buffer.append(staticPLink.getIDColumnName());
    buffer.append(" = e.");
    buffer.append(staticPLink.getIDColumnName());
    buffer.append(" and middle.");
    buffer.append(staticIPProject.getIDColumnName());
    buffer.append(" in (");
    // get projectIDs
      buffer.append(" select p.");
      buffer.append(staticIPProject.getIDColumnName());
      buffer.append(" from ");
      buffer.append(staticIPProject.getEntityName());
      buffer.append(" p ");
      buffer.append("where p.");
      buffer.append(is.idega.idegaweb.project.data.IPProjectBMPBean._COLUMN_DELETED);
      buffer.append(" != 'Y' or p.");
      buffer.append(is.idega.idegaweb.project.data.IPProjectBMPBean._COLUMN_DELETED);
      buffer.append(" is null");
      if(catTypes != null){
        for (int i = 0; i < catTypes.length; i++) {
          buffer.append(" and p.");
          buffer.append(staticIPProject.getIDColumnName());
          buffer.append(" in ( ");
            buffer.append("select ");
            buffer.append(staticIPProject.getIDColumnName());
            buffer.append(" from ");
            buffer.append(projectCategoryMiddleTable);
            buffer.append(" where ");
            buffer.append(staticIPCategory.getIDColumnName());
            buffer.append(" = ");
            buffer.append(catTypes[i]);
          buffer.append(")");
        }
      }
    // get projectIDs ends
    buffer.append(")");


    String SQLString=buffer.toString();

/*
    System.err.print("getProjectDPTPageLinks( ");
    System.err.print(SQLString);
    System.err.println(" )");
*/
    return EntityFinder.findAll(staticPLink,SQLString);
  }


  public IPCategory getProjectCategory(int catTypeId, int projectId )throws SQLException{
    IPProject staticIPProject = (IPProject)is.idega.idegaweb.project.data.IPProjectBMPBean.getStaticInstance(IPProject.class);
    IPCategory staticIPCategory = (IPCategory)is.idega.idegaweb.project.data.IPCategoryBMPBean.getStaticInstance(IPCategory.class);

    String projectCategoryMiddleTable = staticIPProject.getNameOfMiddleTable(staticIPCategory,staticIPProject);
    StringBuffer buffer=new StringBuffer();
    buffer.append(" select c.* ");
    buffer.append(" from ");
    buffer.append(staticIPCategory.getEntityName());
    buffer.append(" c, ");
    buffer.append(projectCategoryMiddleTable);
    buffer.append(" pc ");
    buffer.append(" where pc.");
    buffer.append(staticIPCategory.getIDColumnName());
    buffer.append(" = c.");
    buffer.append(staticIPCategory.getIDColumnName());
    buffer.append(" and pc.");
    buffer.append(staticIPProject.getIDColumnName());
    buffer.append(" = ");
    buffer.append(projectId);
    buffer.append(" and c.");
    buffer.append(is.idega.idegaweb.project.data.IPCategoryBMPBean._COLUMN_TYPE_ID);
    buffer.append(" = ");
    buffer.append(catTypeId);

    String SQLString=buffer.toString();
/*
    System.err.print("getProjectCategory( ");
    System.err.print(SQLString);
    System.err.println(" )");
*/
    List l = EntityFinder.findAll(staticIPCategory,SQLString);

  //  System.err.println(" = "+l);

    if(l != null && l.size() > 0){
      return (IPCategory)l.get(0);
    } else {
      return null;
    }
  }


  public int createIPCategoryType(String name, String description) throws SQLException {
    IPCategoryType ipct = ((is.idega.idegaweb.project.data.IPCategoryTypeHome)com.idega.data.IDOLookup.getHomeLegacy(IPCategoryType.class)).createLegacy();

    ipct.setName(name);

    if(description != null){
      ipct.setDescription(description);
    }

    ipct.insert();

    return ipct.getID();
  }


  public int createIPCategory(int catTypeId, String name, String description) throws SQLException {
    IPCategory ipc = ((is.idega.idegaweb.project.data.IPCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(IPCategory.class)).createLegacy();

    ipc.setCategoryTypeId(catTypeId);

    ipc.setName(name);

    if(description != null){
      ipc.setDescription(description);
    }

    ipc.insert();

    return ipc.getID();
  }


  public void updateIPCategory(int catId, String name, String description) throws SQLException {
    IPCategory ipc = ((is.idega.idegaweb.project.data.IPCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(IPCategory.class)).findByPrimaryKeyLegacy(catId);

    if(name != null){
      ipc.setName(name);
    }

    if(description != null){
      ipc.setDescription(description);
    }

    ipc.update();
  }


  public void deleteIPCategory(int catId) throws SQLException {
    IPCategory cat = ((is.idega.idegaweb.project.data.IPCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(IPCategory.class)).findByPrimaryKeyLegacy(catId);
    cat.removeFrom(IPProject.class);
    cat.delete();
  }




  public int createIPProject(String name, String projectNumber, String description, Integer parentId, int[] categoryIds) throws SQLException {
    IPProject pr = ((is.idega.idegaweb.project.data.IPProjectHome)com.idega.data.IDOLookup.getHomeLegacy(IPProject.class)).createLegacy();

    pr.setName(name);

    if(projectNumber != null){
      pr.setProjectNumber(projectNumber);
    }

    if(description != null){
      pr.setDescription(description);
    }

    if(parentId != null){
      pr.setParentId(parentId.intValue());
    }

    pr.insert();


    if( categoryIds != null){
      for (int i = 0; i < categoryIds.length; i++) {
        if(categoryIds[i] > 0){
          pr.addTo(IPCategory.class,categoryIds[i]);
        }
      }
    }

    return pr.getID();
  }

  public void updateIPProject(int ProjectId, String name, String projectNumber, String description, Integer parentId, int[] categoryIds) throws SQLException {
    IPProject pr = ((is.idega.idegaweb.project.data.IPProjectHome)com.idega.data.IDOLookup.getHomeLegacy(IPProject.class)).findByPrimaryKeyLegacy(ProjectId);

    pr.setName(name);

    if(projectNumber != null){
      pr.setProjectNumber(projectNumber);
    } else {
      //pr.setColumnAsNull(is.idega.idegaweb.project.data.IPProjectBMPBean._COLUMN_PROJECT_NUMBER);
      pr.removeFromColumn(is.idega.idegaweb.project.data.IPProjectBMPBean._COLUMN_PROJECT_NUMBER);
    }

    if(description != null){
      pr.setDescription(description);
    } else {
      //pr.setColumnAsNull(is.idega.idegaweb.project.data.IPProjectBMPBean._COLUMN_DESCRIPTION);
      pr.removeFromColumn(is.idega.idegaweb.project.data.IPProjectBMPBean._COLUMN_DESCRIPTION);
    }

    if(parentId != null){
      pr.setParentId(parentId.intValue());
    } else {
      //pr.setColumnAsNull(is.idega.idegaweb.project.data.IPProjectBMPBean._COLUMN_PARENT_ID);
      pr.removeFromColumn(is.idega.idegaweb.project.data.IPProjectBMPBean._COLUMN_PARENT_ID);
    }

    pr.update();


    pr.removeFrom(IPCategory.class);

    if( categoryIds != null){
      for (int i = 0; i < categoryIds.length; i++) {
        if(categoryIds[i] > 0){
          pr.addTo(IPCategory.class,categoryIds[i]);
        }
      }
    }

  }


  public boolean changeNameOfPageLink(int projectId, String newName) throws SQLException {
    List l = EntityFinder.findRelated(((is.idega.idegaweb.project.data.IPProjectHome)com.idega.data.IDOLookup.getHomeLegacy(IPProject.class)).findByPrimaryKeyLegacy(projectId),com.idega.builder.dynamicpagetrigger.data.PageLinkBMPBean.getStaticInstance(PageLink.class));
    if(l != null && l.size() > 0){
      PageLink pl = (PageLink)l.get(0);
      pl.setDefaultLinkText(newName);
      pl.update();
      return true;
    } else {
      return false;
    }

  }


  public void createPageLink(IWContext iwc, int projectId, String name) throws Exception {

    IPProject project = ((is.idega.idegaweb.project.data.IPProjectHome)com.idega.data.IDOLookup.getHomeLegacy(IPProject.class)).findByPrimaryKeyLegacy(projectId);

    DPTTriggerBusiness business = DPTTriggerBusinessBean.getInstance(iwc);


    List l = EntityFinder.findAll(com.idega.builder.dynamicpagetrigger.data.PageTriggerInfoBMPBean.getStaticInstance(PageTriggerInfo.class));

    PageTriggerInfo info = (PageTriggerInfo)l.get(0);
    
    DPTCopySession cSession = ((DPTCopySession)IBOLookup.getSessionInstance(iwc,DPTCopySession.class));
    cSession.startCopySession();
    cSession.setToCopyInstancePermissions(true);
    cSession.setToCopyPagePermissions(true);
    PageLink pageLink = business.createPageLink(iwc,info,Integer.toString(projectId),name,null,null,null,null);
    cSession.endCopySession();
    
    if(pageLink != null){
      project.addTo(PageLink.class, pageLink.getID());


      // temp - only necessary to add newly created pages into this map
      iwc.removeApplicationAttribute(_APPADDRESS_PROJECTPAGES);

      // replicate permissions

      List participantGroups = DPTTriggerBusinessBean.getInstance(iwc).getDPTPermissionGroups(info);

      if(participantGroups != null && participantGroups.size() > 0 ){
		BuilderService bservice = BuilderServiceFactory.getBuilderService(iwc);
		
        ICTreeNode rootPage = bservice.getPageTree(pageLink.getPageId(),iwc.getCurrentUserId());
        Vector v = new Vector();
        //System.out.println("collecting subpages");
        this.collectSubpages(v,rootPage);

        Set s = new HashSet();
        Set pages = new HashSet();
        Iterator setIter = v.iterator();
        while (setIter.hasNext()) {
			ICTreeNode item = (ICTreeNode)setIter.next();
          pages.add(Integer.toString(item.getNodeID()));
          //System.out.println("----------------------------------");
          //System.out.println("getInstanceIdsOnPage("+item.getNodeID()+")");
          //BuilderLogic.getInstance().getIBXMLPage(item.getNodeID())
          Set set = EventLogic.getInstanceIdsOnPage(item.getNodeID());

          if(set != null){
            s.addAll(set);
          }
        }

        Iterator iter = participantGroups.iterator();
        while (iter.hasNext()) {
          GenericGroup oldGroup = (GenericGroup)iter.next();
          GenericGroup newGroup = this.getReplicatedParticipantGroup(oldGroup, project, "System group");

          //Pages
          List pagePermissions = AccessControl.getGroupsPermissionsForPages(oldGroup,pages);
          //System.err.println("pagePermissions: "+pagePermissions);
          if(pagePermissions != null){
            Iterator permissionIter = pagePermissions.iterator();
            while (permissionIter.hasNext()) {
              ICPermission item = (ICPermission)permissionIter.next();
              AccessControl.replicatePermissionForNewGroup(item, newGroup);
            }
          }

          //Instances
          List permissions = AccessControl.getGroupsPermissionsForInstances(oldGroup,s);
          if(permissions != null){
            Iterator permissionIter = permissions.iterator();
            while (permissionIter.hasNext()) {
              ICPermission item = (ICPermission)permissionIter.next();
              AccessControl.replicatePermissionForNewGroup(item, newGroup);
            }
          }

        }

      }

      // replicate permissions ends
    } else {
      // throw Exception;
    }
  }



//  public void createPageLink(IWContext iwc, int projectId, String name) throws SQLException {
//
//    IPProject project = ((is.idega.idegaweb.project.data.IPProjectHome)com.idega.data.IDOLookup.getHomeLegacy(IPProject.class)).findByPrimaryKeyLegacy(projectId);
//
//    DPTTriggerBusiness business = DPTTriggerBusiness.getInstance();
//
//
//    List l = EntityFinder.findAll(com.idega.builder.dynamicpagetrigger.data.PageTriggerInfoBMPBean.getStaticInstance(PageTriggerInfo.class));
//
//    PageTriggerInfo info = (PageTriggerInfo)l.get(0);
//
//    PageLink pageLink = business.createPageLink(iwc,info,Integer.toString(projectId),name,null,null,null,null);
//
//    if(pageLink != null){
//      project.addTo(PageLink.class, pageLink.getID());
//
//
//      // temp - only necessary to add newly created pages into this map
//      iwc.removeApplicationAttribute(_APPADDRESS_PROJECTPAGES);
//
//      // replicate permissions
//
//      List participantGroups = DPTTriggerBusiness.getDPTPermissionGroups(info);
//
//      if(participantGroups != null && participantGroups.size() > 0 ){
//
//        BuilderLogic logic = BuilderLogic.getInstance();
//
//        PageTreeNode rootPage = new PageTreeNode(pageLink.getPageId(),iwc);
//        Vector v = new Vector();
//        //System.out.println("collecting subpages");
//        this.collectSubpages(v,rootPage);
//
//        Set s = new HashSet();
//        Set pages = new HashSet();
//        Iterator setIter = v.iterator();
//        while (setIter.hasNext()) {
//          PageTreeNode item = (PageTreeNode)setIter.next();
//          pages.add(Integer.toString(item.getNodeID()));
//          //System.out.println("----------------------------------");
//          //System.out.println("getInstanceIdsOnPage("+item.getNodeID()+")");
//          //BuilderLogic.getInstance().getIBXMLPage(item.getNodeID())
//          Set set = logic.getInstanceIdsOnPage(item.getNodeID());
//
//          if(set != null){
//            s.addAll(set);
//          }
//        }
//
//        Iterator iter = participantGroups.iterator();
//        while (iter.hasNext()) {
//          GenericGroup oldGroup = (GenericGroup)iter.next();
//          GenericGroup newGroup = this.getReplicatedParticipantGroup(oldGroup, project, "System group");
//
//          //Pages
//          List pagePermissions = AccessControl.getGroupsPermissionsForPages(oldGroup,pages);
//          //System.err.println("pagePermissions: "+pagePermissions);
//          if(pagePermissions != null){
//            Iterator permissionIter = pagePermissions.iterator();
//            while (permissionIter.hasNext()) {
//              ICPermission item = (ICPermission)permissionIter.next();
//              AccessControl.replicatePermissionForNewGroup(item, newGroup);
//            }
//          }
//
//          //Instances
//          List permissions = AccessControl.getGroupsPermissionsForInstances(oldGroup,s);
//          if(permissions != null){
//            Iterator permissionIter = permissions.iterator();
//            while (permissionIter.hasNext()) {
//              ICPermission item = (ICPermission)permissionIter.next();
//              AccessControl.replicatePermissionForNewGroup(item, newGroup);
//            }
//          }
//
//        }
//
//      }
//
//      // replicate permissions ends
//    } else {
//      // throw Exception;
//    }
//  }




  private static void collectSubpages( List l, ICTreeNode node){
    if(node != null){
      l.add(node);
      Iterator tmp = node.getChildrenIterator();
      if(tmp != null){
        while (tmp.hasNext()) {
          collectSubpages(l,(ICTreeNode)tmp.next());
        }
      }
    }
  }


  private GenericGroup getReplicatedParticipantGroup(GenericGroup group, IPProject project, String newGroupName) throws SQLException{
    IPParticipantGroup newGroup = ((is.idega.idegaweb.project.data.IPParticipantGroupHome)com.idega.data.IDOLookup.getHomeLegacy(IPParticipantGroup.class)).createLegacy();

    if(newGroupName != null){
      newGroup.setName(newGroupName);
    }else{
      newGroup.setName(group.getName());
    }

    String desc = group.getDescription();
    if(desc != null){
      newGroup.setDescription(desc);
    }

    String extra = group.getExtraInfo();
    if(extra != null){
      newGroup.setExtraInfo(extra);
    }

    newGroup.insert();

    newGroup.addGroup(group);
    project.addTo(newGroup);

    return newGroup;
  }


  public void createParticipantGroup(PageTriggerInfo pti, String name, String description) throws IDOAddRelationshipException  {
    DPTTriggerBusinessBean.createDPTPermissionGroup(pti,name,description);
  }


  public static GenericGroup getProjectParticipantGroup(int dptPermissionGroupId, int projectId) throws SQLException {
    GenericGroup staticGenericGroup = (GenericGroup)com.idega.core.data.GenericGroupBMPBean.getStaticInstance(GenericGroup.class);
    IPProject staticIPProject = (IPProject)is.idega.idegaweb.project.data.IPProjectBMPBean.getStaticInstance(IPProject.class);

    String ipProjectICGroup = staticIPProject.getNameOfMiddleTable(staticIPProject,staticGenericGroup);
    String groupTreeTable = staticIPProject.getNameOfMiddleTable(staticGenericGroup,staticGenericGroup);

    StringBuffer buffer=new StringBuffer();
    buffer.append("select g.* from ");
    buffer.append(ipProjectICGroup);
    buffer.append(" pg, ");
    buffer.append(groupTreeTable);
    buffer.append(" gt, ");
    buffer.append(staticIPProject.getEntityName());
    buffer.append(" p, ");
    buffer.append(staticGenericGroup.getEntityName());
    buffer.append(" g");
    buffer.append(" where ");
    buffer.append("pg.");
    buffer.append(staticGenericGroup.getIDColumnName());
    buffer.append(" = g.");
    buffer.append(staticGenericGroup.getIDColumnName());
    buffer.append(" and pg.");
    buffer.append(staticIPProject.getIDColumnName());
    buffer.append(" = p.");
    buffer.append(staticIPProject.getIDColumnName());
    buffer.append(" and gt.");
    buffer.append(staticGenericGroup.getIDColumnName());
    buffer.append(" = g.");
    buffer.append(staticGenericGroup.getIDColumnName());
    buffer.append(" and gt.child_");
    buffer.append(staticGenericGroup.getIDColumnName());
    buffer.append(" = ");
    buffer.append(dptPermissionGroupId);
    buffer.append(" and pg.");
    buffer.append(staticIPProject.getIDColumnName());
    buffer.append(" = ");
    buffer.append(projectId);



    /**
     * select g.* from ip_project_ic_group pg, ic_group_tree gt, ic_group g, ip_project p
     * where pg.ic_group_id = g.ic_group_id
     * and pg.ip_project_id = p.ip_project_id
     * and gt.ic_group_id = g.ic_group_id
     * and gt.child_ic_group_id = 5
     * and pg.ip_project_id = 56
     */

    String SQLString=buffer.toString();
/*
    System.err.print("getProjectParticipantGroup( ");
    System.err.print(SQLString);
    System.err.println(" )");
*/


    List l = EntityFinder.findAll(staticGenericGroup,SQLString);

  //  System.err.println(" = "+l);

    if(l != null && l.size() > 0){
      return (GenericGroup)l.get(0);
    } else {
      return null;
    }
  }

  /**
   * temp wrong?
   */
  public static void cachPageToProjectRelationship(IWContext iwc, int pageId, int projectId ){
    Map pagePropertieIds = (Map)iwc.getApplicationAttribute(_APPADDRESS_PROJECTPAGES);
    if(pagePropertieIds == null){
      pagePropertieIds = new Hashtable();
      iwc.setApplicationAttribute(_APPADDRESS_PROJECTPAGES,pagePropertieIds);
    }
    pagePropertieIds.put(Integer.toString(pageId),Integer.toString(projectId));
  }

  public static synchronized int getCurrentProjectId(IWContext iwc)throws Exception{

    Map pagePropertieIds = (Map)iwc.getApplicationAttribute(_APPADDRESS_PROJECTPAGES);
    if(pagePropertieIds == null){
      pagePropertieIds = new Hashtable();
      iwc.setApplicationAttribute(_APPADDRESS_PROJECTPAGES,pagePropertieIds);
    }

    String pageId = iwc.getParameter(BuilderConstants.IB_PAGE_PARAMETER);

    if(pageId != null){
      String projectID = (String)pagePropertieIds.get(pageId);
      if(projectID != null){
        return Integer.parseInt(projectID);
      } else {
        List links = null;
        try {
          links = ProjectBusiness.getInstance().getProjectDPTPageLinks(null);
        }
        catch (Exception e) {
          e.printStackTrace();
        }

        if(links != null){
			BuilderService bservice = BuilderServiceFactory.getBuilderService(iwc);
          Iterator iter = links.iterator();
          while (iter.hasNext()) {
            PageLink item = (PageLink)iter.next();
            int pPageId = item.getPageId();
            String projectId = item.getReferencedDataId();

            if((pPageId !=-1) && (projectId != null) && (!projectId.equals(""))){
              if(pPageId > 0){
                pagePropertieIds.put(Integer.toString(pPageId),projectId);
              }
              Vector v = null;
              try {
				ICTreeNode rootPage = bservice.getPageTree(pPageId,iwc.getCurrentUserId());
                v = new Vector();
                collectSubpages(v,rootPage);
              }
              catch (Exception ex) {
                ex.printStackTrace();
              }

              if( v != null){
                Iterator iter2 = v.iterator();
                while (iter2.hasNext()) {
                  ICTreeNode item2 = (ICTreeNode)iter2.next();
                  int tmpPageID = item2.getNodeID();
                  if(tmpPageID > 0){
                    pagePropertieIds.put(Integer.toString(tmpPageID),projectId);
                  }
                }
              }
            }
          }
        }
        String s = (String)pagePropertieIds.get(pageId);
        if(s!= null){
          try {
            return Integer.parseInt(s);
          }
          catch (NumberFormatException ex) {
            return -1;
          }
        }else{
          return -1;
        }
      }
    } else {
      return -1;
    }
  }


  public boolean invalidateProject(IWContext iwc,int projectId, int userId){
    try {
      IPProject p = ((is.idega.idegaweb.project.data.IPProjectHome)com.idega.data.IDOLookup.getHomeLegacy(IPProject.class)).findByPrimaryKeyLegacy(projectId);

      List l = EntityFinder.findRelated(p,com.idega.builder.dynamicpagetrigger.data.PageLinkBMPBean.getStaticInstance(PageLink.class));
      if(l != null && l.size() > 0){
        boolean b = DPTTriggerBusinessBean.getInstance(iwc).invalidatePageLink( iwc, (PageLink)l.get(0),userId);
        if(!b){
          return false;
        }
      }
      p.setDeleted(true);
      p.setDeletedBy(userId);
      p.setDeletedWhen(IWTimestamp.getTimestampRightNow());
      p.update();

      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

} //



