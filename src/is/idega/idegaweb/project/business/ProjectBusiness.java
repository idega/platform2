package is.idega.idegaweb.project.business;

import com.idega.data.EntityFinder;
import com.idega.builder.dynamicpagetrigger.data.PageLink;
import is.idega.idegaweb.project.data.IPCategory;
import is.idega.idegaweb.project.data.IPCategoryType;
import is.idega.idegaweb.project.data.IPProject;
import com.idega.core.data.ICObject;

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

  public List getProjectDPTPageLinks() throws Exception{
    return EntityFinder.findAllByColumn(PageLink.getStaticInstance(PageLink.class),PageLink._COLUMNNAME_REFERENCED_DATA_ID,ICObject.getICObject(IPProject.class.getName()).getID());
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

}