//idega 2000 - Tryggvi Larusson

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import com.idega.data.GenericEntity;

public class StrokeBMPBean extends GenericEntity implements Stroke{

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("scorecard_id", "Skorkort", true, true, "java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.Scorecard");
		addAttribute("point_count", "Punktafjöldi", true, true, "java.lang.Integer");
		addAttribute("tee_id", "Teigur", true, true, "java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.Tee");
		addAttribute("stroke_count", "Höggafjöldi", true, true, "java.lang.Integer");
		addAttribute("hole_par", "Par holu", true, true, "java.lang.Integer");
		addAttribute("hole_handicap", "Forgjöf holu", true, true, "java.lang.Integer");

	}

	public String getEntityName(){
		return "stroke";
	}

	public int getScorecardID(){
		return getIntColumnValue("scorecard_id");
	}

	public void setScorecardID(int scorecard_id) {
		setColumn("scorecard_id",scorecard_id);
	}

	public int getPointCount(){
		return getIntColumnValue("point_count");
	}

	public void setPointCount(int point_count) {
		setColumn("point_count",point_count);
	}

	public int getTeeID() {
		return getIntColumnValue("tee_id");
	}

	public void setTeeID(int tee_id) {
		setColumn("tee_id",tee_id);
	}

	public int getStrokeCount(){
		return getIntColumnValue("stroke_count");
	}

	public void setStrokeCount(int stroke_count) {
		setColumn("stroke_count",stroke_count);
	}

	public int getHolePar(){
		return getIntColumnValue("hole_par");
	}

	public void setHolePar(int hole_par) {
		setColumn("hole_par",hole_par);
	}

	public int getHoleHandicap(){
		return getIntColumnValue("hole_handicap");
	}

	public void setHoleHandicap(int hole_handicap) {
		setColumn("hole_handicap",hole_handicap);
	}

}
