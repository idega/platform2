package se.idega.idegaweb.commune.business;

import com.idega.data.IDOCopier;
import com.idega.data.IDOLegacyEntity;

/**
 * Title:        A class to copy the setup of the Commune application from one datasource to another.
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class CommuneCopier extends IDOCopier {

  private static String DEFAULT_FROM_DATASOURCE="default";

  public CommuneCopier(){
    this(DEFAULT_FROM_DATASOURCE);
  }

  public CommuneCopier(String fromDatasource) {
    super();
    IDOLegacyEntity instance1 = ((com.idega.builder.data.IBDomainHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.builder.data.IBDomain.class)).createLegacy();
    instance1.setDatasource(fromDatasource);
    IDOLegacyEntity instance2 = ((com.idega.core.accesscontrol.data.LoginTableHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.core.accesscontrol.data.LoginTable.class)).createLegacy();
    instance2.setDatasource(fromDatasource);
    IDOLegacyEntity instance2b = ((com.idega.core.accesscontrol.data.LoginInfoHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.core.accesscontrol.data.LoginInfo.class)).createLegacy();
    instance2b.setDatasource(fromDatasource);
    IDOLegacyEntity instance3 = ((com.idega.core.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.core.data.ICObjectInstance.class)).createLegacy();
    instance3.setDatasource(fromDatasource);
    IDOLegacyEntity instance4 = ((com.idega.core.accesscontrol.data.ICPermissionHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.core.accesscontrol.data.ICPermission.class)).createLegacy();
    instance4.setDatasource(fromDatasource);
    IDOLegacyEntity instance5 = ((com.idega.builder.dynamicpagetrigger.data.PageTriggerInfoHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.builder.dynamicpagetrigger.data.PageTriggerInfo.class)).createLegacy();
    instance5.setDatasource(fromDatasource);
    super.addEntityToCopy(instance1);
    super.addEntityToCopy(instance2);
    super.addEntityToCopy(instance3);
    super.addEntityToCopy(instance4);
    super.addEntityToCopy(instance5);
  }




}
