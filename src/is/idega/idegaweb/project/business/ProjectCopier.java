package is.idega.idegaweb.project.business;

import com.idega.data.IDOCopier;
import com.idega.data.GenericEntity;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class ProjectCopier extends IDOCopier {

  private static String DEFAULT_FROM_DATASOURCE="default";

  public ProjectCopier(){
    this(DEFAULT_FROM_DATASOURCE);
  }

  public ProjectCopier(String fromDatasource) {
    super();
    GenericEntity instance1 = (GenericEntity)((com.idega.core.builder.data.ICDomainHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.core.builder.data.ICDomain.class)).createLegacy();
    instance1.setDatasource(fromDatasource);
    GenericEntity instance2 = (GenericEntity)((com.idega.core.accesscontrol.data.LoginTableHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.core.accesscontrol.data.LoginTable.class)).createLegacy();
    instance2.setDatasource(fromDatasource);
    GenericEntity instance2b = (GenericEntity)((com.idega.core.accesscontrol.data.LoginInfoHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.core.accesscontrol.data.LoginInfo.class)).createLegacy();
    instance2b.setDatasource(fromDatasource);
    GenericEntity instance3 = (GenericEntity)((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.core.component.data.ICObjectInstance.class)).createLegacy();
    instance3.setDatasource(fromDatasource);
    GenericEntity instance4 = (GenericEntity)((com.idega.core.accesscontrol.data.ICPermissionHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.core.accesscontrol.data.ICPermission.class)).createLegacy();
    instance4.setDatasource(fromDatasource);
    GenericEntity instance5 = (GenericEntity)((com.idega.builder.dynamicpagetrigger.data.PageTriggerInfoHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.builder.dynamicpagetrigger.data.PageTriggerInfo.class)).createLegacy();
    instance5.setDatasource(fromDatasource);
    super.addEntityToCopy(instance1);
    super.addEntityToCopy(instance2);
    super.addEntityToCopy(instance3);
    super.addEntityToCopy(instance4);
    super.addEntityToCopy(instance5);
  }




}
