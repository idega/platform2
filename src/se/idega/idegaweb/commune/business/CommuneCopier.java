package se.idega.idegaweb.commune.business;
import java.util.ArrayList;
import java.util.List;

import com.idega.block.school.data.SchoolClassMember;
import com.idega.core.accesscontrol.data.PermissionGroup;
import com.idega.core.accesscontrol.data.PermissionGroupBMPBean;
//import com.idega.core.data.GenericGroup;
//import com.idega.core.data.GenericGroupBMPBean;
import com.idega.core.user.data.PrimaryUserGroup;
import com.idega.core.user.data.PrimaryUserGroupBMPBean;
import com.idega.data.IDOCopier;
import com.idega.data.IDOLegacyEntity;
import com.idega.user.data.Group;
import com.idega.user.data.GroupBMPBean;
/**
 * Title:        A class to copy the setup of the Commune application from one datasource to another.
 * Description:
 * Copyright:    Copyright (c) 2002 idega software
 * Company:     idega software
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class CommuneCopier extends IDOCopier {
	private static String DEFAULT_FROM_DATASOURCE = "default";
	private static List groupClassesList;
	
	public CommuneCopier() {
		this(DEFAULT_FROM_DATASOURCE);
	}
	public CommuneCopier(String fromDatasource) {
		super();
		IDOLegacyEntity instance1 = createEntityInstance(com.idega.builder.data.IBDomain.class);
		//IDOLegacyEntity instance1 = ((com.idega.builder.data.IBDomainHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.builder.data.IBDomain.class)).createLegacy();
		instance1.setDatasource(fromDatasource);
		IDOLegacyEntity instance2 = createEntityInstance(com.idega.core.accesscontrol.data.LoginTable.class);
		//IDOLegacyEntity instance2 = ((com.idega.core.accesscontrol.data.LoginTableHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.core.accesscontrol.data.LoginTable.class)).createLegacy();
		instance2.setDatasource(fromDatasource);
		IDOLegacyEntity instance2b = createEntityInstance(com.idega.core.accesscontrol.data.LoginInfo.class);
		//IDOLegacyEntity instance2b = ((com.idega.core.accesscontrol.data.LoginInfoHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.core.accesscontrol.data.LoginInfo.class)).createLegacy();
		instance2b.setDatasource(fromDatasource);
		IDOLegacyEntity instance3 = createEntityInstance(com.idega.core.data.ICObjectInstance.class);
		instance3.setDatasource(fromDatasource);
		IDOLegacyEntity instance4 = createEntityInstance(com.idega.core.accesscontrol.data.ICPermission.class);
		//IDOLegacyEntity instance4 = ((com.idega.core.accesscontrol.data.ICPermissionHome)com.idega.data.IDOLookup.getHomeLegacy(com.idega.core.accesscontrol.data.ICPermission.class)).createLegacy();
		instance4.setDatasource(fromDatasource);
		IDOLegacyEntity instance5 = createEntityInstance(com.idega.builder.dynamicpagetrigger.data.PageTriggerInfo.class);
		instance5.setDatasource(fromDatasource);
		IDOLegacyEntity instance6 = createEntityInstance(com.idega.user.data.GroupRelation.class);
		instance6.setDatasource(fromDatasource);
		IDOLegacyEntity instance7 = createEntityInstance(SchoolClassMember.class);
		instance7.setDatasource(fromDatasource);
		IDOLegacyEntity instance8 = createEntityInstance(com.idega.block.school.data.School.class);
		instance8.setDatasource(fromDatasource);
		super.addEntityToCopy(instance1);
		super.addEntityToCopy(instance2);
		super.addEntityToCopy(instance3);
		super.addEntityToCopy(instance4);
		super.addEntityToCopy(instance5);
		super.addEntityToCopy(instance6);
		super.addEntityToCopy(instance7);
		super.addEntityToCopy(instance8);
	}
	protected IDOEntityCopyInfo getIDOEntityCopyInfo(Class entityClass, String tableName) {
		return new CommuneIDOEntityCopyInfo(entityClass, tableName);
	}
	protected class CommuneIDOEntityCopyInfo extends IDOEntityCopyInfo {
		protected CommuneIDOEntityCopyInfo(Class entityClass, String tableName) {
			super(entityClass, tableName);
		}
		public boolean equals(Object o) {
			if (o != null) {
				if (o instanceof IDOEntityCopyInfo) {
					IDOEntityCopyInfo ci = (IDOEntityCopyInfo)o;
					return isAGroupEntity(this)&&isAGroupEntity(ci);
				}
			}
			return false;
		}

	}


	private List getGroupClassesList() {
		if (groupClassesList == null) {
			groupClassesList = new ArrayList();
			//groupClassesList.add(GenericGroup.class);
			//groupClassesList.add(GenericGroupBMPBean.class);
			groupClassesList.add(PermissionGroup.class);
			groupClassesList.add(PermissionGroupBMPBean.class);
			groupClassesList.add(Group.class);
			groupClassesList.add(GroupBMPBean.class);
			groupClassesList.add(PrimaryUserGroup.class);
			groupClassesList.add(PrimaryUserGroupBMPBean.class);

			return groupClassesList;
		}
		else {
			return groupClassesList;
		}
	}
	
	protected boolean isAGroupEntity(IDOEntityCopyInfo ci){
		return getGroupClassesList().contains(ci.entityClass);	
	}

}
