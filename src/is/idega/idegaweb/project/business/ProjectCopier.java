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
    GenericEntity instance1 = new com.idega.builder.data.IBDomain();
    instance1.setDatasource(fromDatasource);
    GenericEntity instance2 = new com.idega.core.accesscontrol.data.LoginTable();
    instance2.setDatasource(fromDatasource);
    GenericEntity instance3 = new com.idega.core.data.ICObjectInstance();
    instance3.setDatasource(fromDatasource);
    GenericEntity instance4 = new com.idega.core.accesscontrol.data.ICPermission();
    instance4.setDatasource(fromDatasource);
    super.addEntityToCopy(instance1);
    super.addEntityToCopy(instance2);
    super.addEntityToCopy(instance3);
    super.addEntityToCopy(instance4);
  }




}