package se.cubecon.bun24.viewpoint.data;

import com.idega.data.*;
import java.util.*;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import com.idega.user.data.*;

/**
 * Last modified: $Date: 2002/12/30 11:33:48 $ by $Author: tryggvil $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.11 $
 */
public class SubCategoryBMPBean extends GenericEntity implements SubCategory {

    private static final String ENTITY_NAME = "vp_subcategory";
    private static final String COLUMN_ID = ENTITY_NAME + "_id";
	private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TOPCATEGORY_ID = "topcategory_id";
    private static final String COLUMN_HANDLERGROUP_ID = "handlergroup_id";

	public String getEntityName() {
		return ENTITY_NAME;
	}

    public void insertStartData () throws Exception {
        super.insertStartData ();
        System.out.println ("¤¤¤ Invoked " + ENTITY_NAME + ".insertStartData ()");

        final String [][] startData = {
            { "Anordnare", "Barnomsorg", "Myndighetsgruppen" },
            { "Betalningar", "Barnomsorg", "Ekonomi" },
            { "Kötid", "Barnomsorg", "Kundvalsgruppen" },
            { "Regelverk", "Barnomsorg", "Kundvalsgruppen" },
            { "Taxan", "Barnomsorg", "Kundvalsgruppen" },
            { "Övrigt", "Barnomsorg", "Kundvalsgruppen" },
            { "Fritids", "Skola", "Kundvalsgruppen" },
            { "Förskoleklass", "Skola", "Kundvalsgruppen" },
            { "Likvärdighetsgaranti", "Skola", "Myndighetsgruppen" },
            { "Modersmål", "Skola", "Myndighetsgruppen" },
            { "Regelverk", "Skola", "Kundvalsgruppen" },
            { "Skolskjuts", "Skola", "Myndighetsgruppen" },
            { "Skolval", "Skola", "Kundvalsgruppen" },
            { "Övrigt", "Skola", "Kundvalsgruppen" },
            { "Elevvård", "Gymnasieskola", "Myndighetsgruppen" },
            { "Inackorderingsbidrag", "Gymnasieskola", "Myndighetsgruppen" },
            { "Intagning", "Gymnasieskola", "Intagningsgruppen" },
            { "Programval", "Gymnasieskola", "Kundvalsgruppen" },
            { "Skolhälsovård", "Gymnasieskola", "Myndighetsgruppen" },
            { "Studiebidrag", "Gymnasieskola", "Myndighetsgruppen" },
            { "Övrigt", "Gymnasieskola", "Kundvalsgruppen" },
            { "Anordnare", "Komvux", "Myndighetsgruppen" },
            { "Kurser/utbud", "Komvux", "Kundvalsgruppen" },
            { "SFI", "Komvux", "Kundvalsgruppen" },
            { "Studiebidrag", "Komvux", "Myndighetsgruppen" },
            { "Studievägledning", "Komvux", "Kundvalsgruppen" },
            { "Övrigt", "Komvux", "Kundvalsgruppen" },
            { "Beslut i nämnden", "Politiker", "Namndsekreterare" },
            { "Enskilt ärende", "Politiker", "Myndighetsgruppen" },
            { "Förslag", "Politiker", "Namndsekreterare" },
            { "Kundvalet", "Politiker", "Kundvalsgruppen" },
            { "Övrigt", "Politiker", "Namndsekreterare" },
            { "Nacka24", "Myndigheten", "Kundvalsgruppen" },
            { "Barnomsorgscheck", "Myndigheten", "Finansgruppen" },
            { "Handikapp", "Myndigheten", "Myndighetsgruppen" },
            { "Likvärdighetsgaranti", "Myndigheten", "Myndighetsgruppen" },
            { "Service", "Myndigheten", "Kundvalsgruppen" },
            { "Skolpeng", "Myndigheten", "Finansgruppen" },
            { "Övrigt", "Myndigheten", "Myndighetsgruppen" },
        };
       final TopCategoryHome topCategoryHome
               = (TopCategoryHome) IDOLookup.getHome (TopCategory.class);
       final TopCategory [] topCategories = topCategoryHome.findAll ();
       final Map topCategoriesMap = new HashMap ();
       for (int i = 0; i < topCategories.length; i++) {
           topCategoriesMap.put (topCategories [i].getName (),
                                 topCategories [i].getPrimaryKey ());
       }
       final Map groupsMap = new HashMap ();
       SubCategoryHome subCategoryHome
               = (SubCategoryHome) IDOLookup.getHome(SubCategory.class);
       for (int i = 0; i < startData.length; i++) {
           final String subCategoryName = startData [i][0];
           final String topCategoryName = startData [i][1];
           final String groupName = startData [i][2];
           final Integer topCategoryId
                   = (Integer) topCategoriesMap.get (topCategoryName);
           if (!groupsMap.containsKey (groupName)) {
               addGroupToDatabaseAndMap (groupName, groupsMap);
           }
           final Integer handlerGroupId = (Integer) groupsMap.get (groupName);
           if (topCategoryId != null && handlerGroupId != null) {
               final SubCategory subCategory = subCategoryHome.create ();
               subCategory.setName (subCategoryName);
               subCategory.setHandlerGroupId (handlerGroupId.intValue ());
               subCategory.setTopCategoryId (topCategoryId.intValue ());
               subCategory.store ();
           } else {
               System.err.println ("Couldn't store sub category ("
                                   + subCategoryName + ", " + topCategoryName
                                   + ", " + groupName + ") with ids (" +
                                   topCategoryId + ", " + handlerGroupId + ")");
           }
       }
    }

    public void initializeAttributes () {
        addAttribute(COLUMN_ID, "Id", Integer.class);
        setAsPrimaryKey (COLUMN_ID, true);
		addAttribute (COLUMN_NAME, "Name", String.class);
        addManyToOneRelationship (COLUMN_TOPCATEGORY_ID, TopCategory.class);
        addManyToOneRelationship (COLUMN_HANDLERGROUP_ID, Group.class);
    }

    public String getName () {
		return getStringColumnValue (COLUMN_NAME);
    }

    public Group getHandlerGroup () {
        return (Group) getColumnValue (COLUMN_HANDLERGROUP_ID);
    }

    public TopCategory getTopCategory () {
        return (TopCategory) getColumnValue (COLUMN_TOPCATEGORY_ID);
    }

    public void setName (final String name) {
		setColumn (COLUMN_NAME, name);
    }

    public void setTopCategoryId (final int id) {
		setColumn (COLUMN_TOPCATEGORY_ID, id);
    }

    public void setHandlerGroupId (final int id) {
		setColumn (COLUMN_HANDLERGROUP_ID, id);
    }

    public Collection ejbFindSubCategories (final int topCategoryId)
        throws FinderException, RemoteException {
        final String sql = "select * from " + ENTITY_NAME + " where "
                + COLUMN_TOPCATEGORY_ID + " = '" + topCategoryId + "'";
        return idoFindIDsBySQL (sql);
    }
    
    public Integer ejbFindSubCategoryByName(String name) throws FinderException {
    	IDOQuery query = idoQuery();
    	query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_NAME, name);
    	return (Integer) super.idoFindOnePKByQuery(query);
    }

    private void addGroupToDatabaseAndMap (final String groupName,
                                           final Map groupsMap) {
        try {
            final GroupHome home = (GroupHome) IDOLookup.getHome(Group.class);
            final Group group = home.create ();
            group.setName (groupName);
            group.store ();
            groupsMap.put (groupName, group.getPrimaryKey ());
        } catch (final Exception e) {
            e.printStackTrace ();
        }
    }
}
