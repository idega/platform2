package is.idega.idegaweb.project.business;

import com.idega.data.EntityFinder;
import com.idega.builder.dynamicpagetrigger.data.PageLink;
import is.idega.idegaweb.project.data.IPCategory;
import is.idega.idegaweb.project.data.IPCategoryType;
import is.idega.idegaweb.project.data.IPProject;
import com.idega.core.data.ICObject;
import com.idega.builder.dynamicpagetrigger.business.DPTriggerBusiness;
import com.idega.builder.dynamicpagetrigger.data.PageLink;
import com.idega.builder.dynamicpagetrigger.data.PageTriggerInfo;
import com.idega.data.EntityFinder;
import com.idega.presentation.IWContext;

import java.util.List;
import java.util.Vector;
import java.util.Iterator;

import java.sql.SQLException;

/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class ProjectBusiness {

  public final static String _PRM_UPDATE = "ip_update";

  public ProjectBusiness() {
  }


  public static ProjectBusiness getInstance(){
    return new ProjectBusiness();
  }

  public List getCategoryTypes()throws SQLException{
    return EntityFinder.findAll(IPCategoryType.getStaticInstance(IPCategoryType.class));
  }

  public List getCategories(int catTypeId) throws SQLException {
    return EntityFinder.findAllByColumn(IPCategory.getStaticInstance(IPCategory.class),IPCategory._COLUMN_TYPE_ID,catTypeId);
  }

  public List getProjects() throws SQLException {
    return EntityFinder.findAll(IPProject.getStaticInstance(IPProject.class));
  }
/*
  public List getProjectDPTPageLinks(int[] catTypes) throws Exception{
    PageLink staticPLink = PageLink.getStaticInstance(PageLink.class);
    if(catTypes != null){
      return EntityFinder.findAll(staticPLink);
    }else{
      return EntityFinder.findAll(staticPLink,"select * from "+staticPLink.getEntityName()+" where ");
    }
    // wrong dpt_??ID not objectID return EntityFinder.findAllByColumn(PageLink.getStaticInstance(PageLink.class),PageLink._COLUMNNAME_REFERENCED_DATA_ID,ICObject.getICObject(IPProject.class.getName()).getID());
  }
*/

  public List getProjectDPTPageLinks(int[] catTypes)throws SQLException{
    PageLink staticPLink = (PageLink)PageLink.getStaticInstance(PageLink.class);
    IPProject staticIPProject = (IPProject)IPProject.getStaticInstance(IPProject.class);
    IPCategory staticIPCategory = (IPCategory)IPCategory.getStaticInstance(IPCategory.class);

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
      if(catTypes != null){
        for (int i = 0; i < catTypes.length; i++) {
          if(i == 0){
            buffer.append(" where p.");
          }else {
            buffer.append(" and p.");
          }
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




  public int createIPCategoryType(String name, String description) throws SQLException {
    IPCategoryType ipct = new IPCategoryType();

    ipct.setName(name);

    if(description != null){
      ipct.setDescription(description);
    }

    ipct.insert();

    return ipct.getID();
  }


  public int createIPCategory(int catTypeId, String name, String description) throws SQLException {
    IPCategory ipc = new IPCategory();

    ipc.setCategoryTypeId(catTypeId);

    ipc.setName(name);

    if(description != null){
      ipc.setDescription(description);
    }

    ipc.insert();

    return ipc.getID();
  }


  public int createIPProject(String name, String projectNumber, String description, Integer parentId, int[] categoryIds) throws SQLException {
    IPProject pr = new IPProject();

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




  public void createPageLink(IWContext iwc, int icObjectInstanceId, int projectId, String name) throws SQLException {

    IPProject project = new IPProject(projectId);

    DPTriggerBusiness business = DPTriggerBusiness.getInstance();


    List l = EntityFinder.findAll(PageTriggerInfo.getStaticInstance(PageTriggerInfo.class));

    PageTriggerInfo info = (PageTriggerInfo)l.get(0);

    int pageLinkID = business.createPageLink(iwc,info,Integer.toString(projectId),name,null,null,null,null);

    project.addTo(PageLink.class, pageLinkID);
  }




}