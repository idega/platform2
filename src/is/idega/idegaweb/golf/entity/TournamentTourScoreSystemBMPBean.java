package is.idega.idegaweb.golf.entity;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.ibm.icu.util.StringTokenizer;
import com.idega.data.GenericEntity;
import com.idega.data.IDOLookupException;
import com.idega.data.query.Column;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;

public class TournamentTourScoreSystemBMPBean extends GenericEntity implements TournamentTourScoreSystem {

	private static final String ENTITY_NAME = "TOURNAMENT_TOUR_SCORESYSTEM";
	private static final String COLUMN_NAME = "NAME";
	private static final String COLUMN_DEFAULT_POINTS = "DEFAULT_POINTS";
	private static final String COLUMN_POINTS_DIVISION = "POINT_DIVISION";
	
	public String getEntityName() {
		return TournamentTourScoreSystemBMPBean.ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "Name", String.class);
		addAttribute(COLUMN_DEFAULT_POINTS, "Default points per tournament", Integer.class);
		addAttribute(COLUMN_POINTS_DIVISION, "points divisions", String.class, 2000);
	}
	public void insertStartData() throws Exception {
		super.insertStartData();
		
		try {
			TournamentTourScoreSystemHome home = (TournamentTourScoreSystemHome) super.getIDOHome(TournamentTourScoreSystem.class);
			TournamentTourScoreSystem defaultSystem = home.create();
			defaultSystem.setDefaultPoints(1000);
			defaultSystem.setPointsDivision(new float[]{
					(float) 16.67, (float) 11.11, (float) 6.26, (float) 5,  (float) 4.24, (float) 3.5,  (float) 3,    (float) 2.5,  (float) 2.24, (float) 2,
					(float) 1.84, (float) 1.72, (float) 1.61, (float) 1.53, (float) 1.47, (float) 1.41, (float) 1.35, (float) 1.29, (float) 1.24, (float) 1.20,
					(float) 1.16, (float) 1.13, (float) 1.10, (float) 1.07, (float) 1.04, (float) 1.01, (float) 0.98, (float) 0.95, (float) 0.92, (float) 0.89,
					(float) 0.86, (float) 0.85, (float) 0.80, (float) 0.77, (float) 0.75, (float) 0.73, (float) 0.71, (float) 0.69, (float) 0.67, (float) 0.65,
					(float) 0.63, (float) 0.61, (float) 0.59, (float) 0.57, (float) 0.55, (float) 0.53, (float) 0.51, (float) 0.49, (float) 0.47, (float) 0.45,
					(float) 0.43, (float) 0.41, (float) 0.39, (float) 0.37, (float) 0.35, (float) 0.33, (float) 0.31, (float) 0.30, (float) 0.29, (float) 0.28,
					(float) 0.27, (float) 0.26, (float) 0.25, (float) 0.24, (float) 0.23, (float) 0.22, (float) 0.21, (float) 0.20, (float) 0.19, (float) 0.18
					}
					);
			defaultSystem.setName("Default");
			defaultSystem.store();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	public void setDefaultPoints(int points) {
		setColumn(COLUMN_DEFAULT_POINTS, points);
	}

	public int getDefaultPoints() {
		return getIntColumnValue(COLUMN_DEFAULT_POINTS);
	}
	
	/**
	 * Set the dividing of the points... 
	 * pointsDivision[0] = percentage of total score for 1st place
	 * pointsDivision[1] = percentage of total score for 2st place, and so on
	 * @param pointsDivision
	 */
	public void setPointsDivision(float[] pointsDivision) {
		StringBuffer points = null;
		if (pointsDivision != null) {
			points = new StringBuffer("");
			for (int i = 0; i < pointsDivision.length; i++) {
				if (i != 0) {
					points.append(",");
				}
				points.append(pointsDivision[i]);
			}
			setColumn(COLUMN_POINTS_DIVISION, points.toString());
		}
		
	}
	
	public float[] getPointsDivision() {
		String points = getStringColumnValue(COLUMN_POINTS_DIVISION);
		if (points != null) {
			StringTokenizer ST = new StringTokenizer(points, ",");
			int theSize = ST.countTokens();
			float[] pointsDivision = new float[theSize];
			for (int i = 0; i < theSize; i++) {
				pointsDivision[i] = Float.parseFloat(ST.nextToken());
			}
			return pointsDivision;
		} else {
			return new float[]{};
		}
	}
	
	public Collection ejbFindAll() throws FinderException {
		Table table = new Table(this);
		Column col = new Column(table, getIDColumnName());
		SelectQuery query = new SelectQuery(table);
		query.addColumn(col);
		return this.idoFindPKsByQuery(query);
	}
}
