package is.idega.idegaweb.golf.tournament.business;

import java.util.*;
import com.idega.util.IWTimestamp;

public class ResultsCollector {
  public static boolean strokeGameType = true;
  public static boolean pointsGameType = false;

  public static final int TOTALSTROKES = 1;
  public static final int TOTALSTROKESWITHHANDICAP = 2;
  public static final int TOTALPOINTS = 3;

  private Vector strokes_ = null;
  private Vector holeNumber_ = null;
  private Vector points_ = null;
  private Vector par_ = null;
  private Vector roundScore_ = null;
  private Vector roundNumber_ = null;

  private boolean strokeGame_ = true;
  private double handicap_ = 0;
  private double lastNine_ = 0;
  private double lastSix_ = 0;
  private double lastThree_ = 0;
  private double last_ = 0;
  private int realLastNine_ = 0;
  private IWTimestamp scorecardDate = null;

  private boolean sortByRound = false;
  private int resultType_ = 0;

  private int memberId_ = 0;
  private int tournamentPosition_ = 0;
  private int dismissal_ = 0;
  private int totalPoints_ = 0;
  private int totalStrokes_ = 0;
  private int totalStrokesWithHandicap_ = 0;
  private int totalPar_ = 0;
  private int difference_ = 0;
  private String firstName_ = null;
  private String middleName_ = null;
  private String lastName_ = null;
  private int tournamentGroupId_ = 0;
  private String abbrevation_ = null;
  private int holes_ = 0;
  private int hole_ = 0;
  private int numberOfRounds_ = 0;
  private int fieldPar_ = 0;

  public ResultsCollector() {
    resultType_ = TOTALSTROKESWITHHANDICAP;
  }

  public ResultsCollector(int resultType) {
    resultType_ = resultType;
  }

  public void setStrokes(Vector holeNumber, Vector strokes) {
    holeNumber_ = holeNumber;
  	strokes_ = strokes;
  }

  public void setPoints(Vector points) {
    points_ = points;
  }

  public void addRoundNumber(int roundNumber) {
    if (roundNumber_ == null)
      roundNumber_ = new Vector();

    roundNumber_.add(new Integer(roundNumber));
  }

  public int getRoundNumber(int roundNumber) {
    int position = -1;

    if (roundNumber_ != null) {
      position = roundNumber_.indexOf(new Integer(roundNumber)) + 1;
    }

    return position;
  }

  public int getRound(int roundNumber) {
    int position = -1;

    if (roundNumber_ != null) {
      position = roundNumber_.indexOf(new Integer(roundNumber));
      if ( position != -1 ) {
	position = ((Integer) roundNumber_.elementAt(position)).intValue();
      }
    }

    return position;
  }

  public void addStroke(int holeNumber, double stroke) {
  	if(holeNumber_==null)
  	  holeNumber_ = new Vector();
  	if (strokes_ == null)
      strokes_ = new Vector();
    
    holeNumber_.add(new Integer(holeNumber));
    strokes_.add(new Double(stroke));
  }

  public void addPoints(double points) {
    if (points_ == null)
      points_ = new Vector();

    points_.add(new Double(points));
  }

  public void addPar(int par) {
    if (par_ == null)
      par_ = new Vector();

    par_.add(new Integer(par));
  }

  public void setDate(IWTimestamp date) {
    scorecardDate = date;
  }

  public void setMemberId(int memberId) {
    memberId_ = memberId;
  }

  public void setTournamentPosition(int tournamentPosition) {
    tournamentPosition_ = tournamentPosition;
  }

  public void setDismissal(int dismissal) {
    dismissal_ = dismissal;
  }

  public void setFirstName(String firstName) {
    firstName_ = firstName;
  }

  public void setMiddleName(String middleName) {
    middleName_ = middleName;
  }

  public void setLastName(String lastName) {
    lastName_ = lastName;
  }

  public void setAbbrevation(String abbrevation) {
    abbrevation_ = abbrevation;
  }

  public void setTournamentGroupId(int tournamentGroupId) {
    tournamentGroupId_ = tournamentGroupId;
  }

  public void setTotalPoints(int totalPoints) {
    totalPoints_ = totalPoints;
  }

  public void setTotalStrokes(int totalStrokes) {
    totalStrokes_ = totalStrokes;
  }

  public void setHandicap(double handicap) {
    handicap_ = handicap;
  }

  public void setGameType(int gameType) {
    resultType_ = gameType;
  }

  public void setHoles(int holes) {
    holes_ = holes;
  }

  public void setNumberOfRounds(int numberOfRounds) {
    this.numberOfRounds_ = numberOfRounds;
  }

  public void calculateCompareInfo() {
    lastNine_ = 0.0;
    lastSix_ = 0.0;
    lastThree_ = 0.0;
    last_ = 0.0;
    totalPoints_ = 0;
    totalStrokes_ = 0;
    totalStrokesWithHandicap_ = 0;
    totalPar_ = 0;
    roundScore_ = new Vector();
    hole_ = 0;

    switch (resultType_) {
      case TOTALSTROKES :
	getStrokesInfo();
      break;

      case TOTALSTROKESWITHHANDICAP :
	getStrokesInfo();
      break;

      case TOTALPOINTS :
	getPointsInfo();
      break;
    }

  }

  public void getStrokesInfo() {
    int size = 0;
    int roundTotal = 0;
    fieldPar_ = 0;

    if ( strokes_ != null ) {
      size = strokes_.size();
      hole_ = size;

      if ( size < 9 ) {
	for (int i = 0; i < size; i++) {
	  totalStrokes_ += ((Double)strokes_.elementAt(i)).intValue();
	  roundTotal += ((Double)strokes_.elementAt(i)).intValue();
	  totalPar_ += ((Integer)par_.elementAt(i)).intValue();
	  fieldPar_ += ((Integer)par_.elementAt(i)).intValue();
	}

	if ( resultType_ == TOTALSTROKESWITHHANDICAP ) {
	  roundTotal -= (int) handicap_;
	}

	roundScore_.add(new Integer(roundTotal));
	int multiplier = size / holes_;
	totalStrokesWithHandicap_ = (int) ((double)totalStrokes_ - ((double)multiplier * handicap_));
	difference_ = totalStrokes_ - totalPar_;

      }
      else {
	for (int i = 0; i < size; i++) {
	  totalStrokes_ += ((Double)strokes_.elementAt(i)).intValue();
	  roundTotal += ((Double)strokes_.elementAt(i)).intValue();
	  totalPar_ += ((Integer)par_.elementAt(i)).intValue();
	  fieldPar_ += ((Integer)par_.elementAt(i)).intValue();
	  if ( i >= size - 9 ) {
	    lastNine_ += ((Double)strokes_.elementAt(i)).doubleValue();
	    if ( i >= size - 6 ) {
	      lastSix_ += ((Double)strokes_.elementAt(i)).doubleValue();
	      if ( i >= size - 3 ) {
		lastThree_ += ((Double)strokes_.elementAt(i)).doubleValue();
	      }
	    }
	  }
	  if ( (i+1) % holes_ == 0 ) {
	    if ( resultType_ == TOTALSTROKESWITHHANDICAP )
	      roundTotal -= (int) handicap_;
	    roundScore_.add(new Integer(roundTotal));
	    roundTotal = 0;
	    fieldPar_ = 0;
	  }
	}

	/*for (int i = size - 3; i < size; i++) {
	  lastNine_ += ((Double)strokes_.elementAt(i)).doubleValue();
	  lastSix_ += ((Double)strokes_.elementAt(i)).doubleValue();
	  lastThree_ += ((Double)strokes_.elementAt(i)).doubleValue();
	  totalStrokes_ += ((Double)strokes_.elementAt(i)).intValue();
	  roundTotal += ((Double)strokes_.elementAt(i)).intValue();
	  totalPar_ += ((Integer)par_.elementAt(i)).intValue();
	  fieldPar_ += ((Integer)par_.elementAt(i)).intValue();
	  if ( (i+1) % holes_ == 0 ) {
	    if ( resultType_ == TOTALSTROKESWITHHANDICAP )
	      roundTotal -= (int) handicap_;
	    roundScore_.add(new Integer(roundTotal));
	    roundTotal = 0;
	    fieldPar_ = 0;
	  }
	}

	for (int i = size - 6; i < size - 3; i++) {
	  lastNine_ += ((Double)strokes_.elementAt(i)).doubleValue();
	  lastSix_ += ((Double)strokes_.elementAt(i)).doubleValue();
	  totalStrokes_ += ((Double)strokes_.elementAt(i)).intValue();
	  roundTotal += ((Double)strokes_.elementAt(i)).intValue();
	  totalPar_ += ((Integer)par_.elementAt(i)).intValue();
	  fieldPar_ += ((Integer)par_.elementAt(i)).intValue();
	  if ( (i+1) % holes_ == 0 ) {
	    if ( resultType_ == TOTALSTROKESWITHHANDICAP )
	      roundTotal -= (int) handicap_;
	    roundScore_.add(new Integer(roundTotal));
	    roundTotal = 0;
	    fieldPar_ = 0;
	  }
	}

	for (int i = size - 9; i < size - 6; i++) {
	  lastNine_ += ((Double)strokes_.elementAt(i)).doubleValue();
	  totalStrokes_ += ((Double)strokes_.elementAt(i)).intValue();
	  roundTotal += ((Double)strokes_.elementAt(i)).intValue();
	  totalPar_ += ((Integer)par_.elementAt(i)).intValue();
	  fieldPar_ += ((Integer)par_.elementAt(i)).intValue();
	  if ( (i+1) % holes_ == 0 ) {
	    if ( resultType_ == TOTALSTROKESWITHHANDICAP )
	      roundTotal -= (int) handicap_;
	    roundScore_.add(new Integer(roundTotal));
	    roundTotal = 0;
	    fieldPar_ = 0;
	  }
	}*/

	if ( resultType_ == TOTALSTROKESWITHHANDICAP ) {
	  roundTotal -= (int) handicap_;
	}

	if ( roundTotal != 0 )
	  roundScore_.add(new Integer(roundTotal));
	int multiplier = size / holes_;
	totalStrokesWithHandicap_ = (int) ((double)totalStrokes_ - ((double)multiplier * handicap_));
	difference_ = totalStrokes_ - totalPar_;

	last_ = ((Double)strokes_.elementAt(size-1)).doubleValue();
	realLastNine_ = (int) lastNine_;
	if ( resultType_ == TOTALSTROKESWITHHANDICAP ) {
	  lastNine_ = lastNine_ - handicap_ / 2;
	  lastSix_ = lastSix_ - handicap_ / 3;
	  lastThree_ = lastThree_ - handicap_ / 6;
	}
      }
    }
  }

  public void getPointsInfo() {
    int size = 0;
    int roundTotal = 0;

    if ( points_ != null ) {
      size = points_.size();
      hole_ = size;
    }

    if ( size < 9 ) {
      for (int i = 0; i < size; i++) {
	totalPoints_ += ((Double)points_.elementAt(i)).intValue();
      }

    }
    else {
      for (int i = 0; i < size - 9; i++) {
	totalPoints_ += ((Double)points_.elementAt(i)).intValue();
	roundTotal += ((Double)points_.elementAt(i)).intValue();
	if ( (i+1) % holes_ == 0 ) {
	  roundScore_.add(new Integer(roundTotal));
	  roundTotal = 0;
	}
      }

      for (int i = size - 3; i < size; i++) {
	lastNine_ += ((Double)points_.elementAt(i)).doubleValue();
	lastSix_ += ((Double)points_.elementAt(i)).doubleValue();
	lastThree_ += ((Double)points_.elementAt(i)).doubleValue();
	totalPoints_ += ((Double)points_.elementAt(i)).intValue();
	roundTotal += ((Double)points_.elementAt(i)).intValue();
      }

      for (int i = size - 6; i < size - 3; i++) {
	lastNine_ += ((Double)points_.elementAt(i)).doubleValue();
	lastSix_ += ((Double)points_.elementAt(i)).doubleValue();
	totalPoints_ += ((Double)points_.elementAt(i)).intValue();
	roundTotal += ((Double)points_.elementAt(i)).intValue();
      }

      for (int i = size - 9; i < size - 6; i++) {
	lastNine_ += ((Double)points_.elementAt(i)).doubleValue();
	totalPoints_ += ((Double)points_.elementAt(i)).intValue();
	roundTotal += ((Double)points_.elementAt(i)).intValue();
      }

      roundScore_.add(new Integer(roundTotal));
      last_ = ((Double)points_.elementAt(size-1)).doubleValue();
      realLastNine_ = (int) lastNine_;
    }
  }

  public String getName() {
    StringBuffer name = new StringBuffer();
    if ( firstName_ != null )
      name.append(firstName_);
    if ( middleName_ != null && "" != middleName_ )
      name.append(" "+middleName_);
    if ( lastName_ != null )
      name.append(" "+lastName_);

    return name.toString();
  }

  public String getFirstName() {
    return firstName_;
  }

  public String getMiddleName() {
    return middleName_;
  }

  public String getLastName() {
    return lastName_;
  }

  public IWTimestamp getDate() {
    return scorecardDate;
  }

  public int getMemberId() {
    return(memberId_);
  }

  public int getDismissal() {
    return(dismissal_);
  }

  public int getTournamentPosition() {
    return(tournamentPosition_);
  }

  public String getAbbrevation() {
    return(abbrevation_);
  }

  public int getHandicap() {
    return((int) handicap_);
  }

  public int getDifference() {
    return(difference_);
  }

  public int getTournamentGroupId() {
    return(tournamentGroupId_);
  }

  public int getTotalPoints() {
    return(totalPoints_);
  }

  public int getTotalScore() {
    int totalScore = 0;
    if ( resultType_ == TOTALPOINTS )
      totalScore = totalPoints_;
    else if ( resultType_ == TOTALSTROKES )
      totalScore = totalStrokes_;
    else if ( resultType_ == TOTALSTROKESWITHHANDICAP )
      totalScore = totalStrokesWithHandicap_;

    return totalScore;
  }

  public int getTotalStrokes() {
    return(totalStrokes_);
  }

  public int getTotalStrokesWithHandicap() {
    return(totalStrokesWithHandicap_);
  }

  public double getScore() {
    return(0);
  }

  public Vector getStrokes() {
    return strokes_;
  }
  
  public Vector getHoleNumber(){
  	return holeNumber_;
  }

  public Vector getPoints() {
    return points_;
  }

  public Vector getPar() {
    return par_;
  }

  public double getRealLastNine() {
    return(realLastNine_);
  }

  public double getLastNine() {
    return(lastNine_);
  }

  public double getLastSix() {
    return(lastSix_);
  }

  public double getLastThree() {
    return(lastThree_);
  }

  public double getLast() {
    return(last_);
  }

  public int getGameType() {
    return(resultType_);
  }

  public int getHoles() {
    return(holes_);
  }

  public int getFieldPar() {
    return(fieldPar_);
  }

  public String getHole() {
    String holeString = "";

    if ( hole_ == 0 ) {
      holeString = Integer.toString(hole_);
    }
    else {
      if ( hole_ % holes_ == 0 ) {
	if ( hole_ == holes_ * numberOfRounds_ ) {
	  holeString = "F";
	}
	else {
	  holeString = "F";
	}
      }
      else {
	if ( ((holes_ * numberOfRounds_) - hole_) > 0 ) {
	  holeString = Integer.toString(hole_ % holes_);
	}
	else {
	  holeString = Integer.toString(hole_ % holes_);
	}
      }

    }

    return(holeString);
  }

  public Vector getRoundScore() {
    return(roundScore_);
  }

  public int getRoundScore(int roundNumber) {
    int roundScore = 0;
    if ( roundScore_ != null ) {
      if ( roundScore_.size() >= roundNumber ) {
	roundScore = ((Integer)roundScore_.elementAt(roundNumber-1)).intValue();
      }
    }

    return(roundScore);
  }

}
