package is.idega.idegaweb.golf.tournament.business;

import java.util.Comparator;

import com.idega.util.IsCollator;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class ResultComparator implements Comparator {

  public static final int TOTALSTROKES = 1;
  public static final int TOTALSTROKESWITHHANDICAP = 2;
  public static final int TOTALPOINTS = 3;
  public static final int TOTALPOINTSWITHOUTHANDICAP = 8;
  public static final int NAME = 4;
  public static final int ABBREVATION = 5;
  public static final int TOURNAMENTROUND = 6;
  public static final int TOTAL_STROKES_V_2 = 7;

  private int sortBy;
  private int roundNumber_ = 0;

  public ResultComparator() {
    sortBy = TOTALSTROKES;
  }

  public ResultComparator(int toSortBy) {
      sortBy = toSortBy;
  }

  public void sortBy(int toSortBy) {
      sortBy = toSortBy;
  }

  public void sortByRound(int roundNumber) {
      roundNumber_ = roundNumber;
  }

  public int compare(Object o1, Object o2) {
    ResultsCollector r1 = (ResultsCollector)o1;
    ResultsCollector r2 = (ResultsCollector)o2;
    int result = 0;

    try {
      r1.calculateCompareInfo();
      r2.calculateCompareInfo();
    }
    catch(Exception e) {
      System.err.println("Something wrong here... "+r1.getMemberId()+"/"+r2.getMemberId());

    }

    switch (sortBy) {
      case TOTALSTROKES :
        result = totalStrokesCompare(r1,r2);
        if (result == 0)
          result = nameCompare(r1,r2);
      break;

      case TOTALSTROKESWITHHANDICAP :
        result = totalStrokesHandicapCompare(r1,r2);
        if (result == 0)
          result = nameCompare(r1,r2);
      break;

      case TOTALPOINTSWITHOUTHANDICAP :
      case TOTALPOINTS :
        result = totalPointsCompare(r1,r2);
        if (result == 0)
          result = nameCompare(r1,r2);
      break;

      case NAME :
        result = nameCompare(r1,r2);
      break;

      case ABBREVATION :
        result = abbrevationCompare(r1,r2);
        if (result == 0)
          result = nameCompare(r1,r2);
      break;

      case TOURNAMENTROUND :
        result = roundCompare(r1,r2);
        if (result == 0)
          result = nameCompare(r1,r2);
      break;

      case TOTAL_STROKES_V_2 :
        result = totalStrokesCompareV2(r1,r2);
        if (result == 0)
          result = nameCompare(r1,r2);
      break;
    }
    return(result);
  }

  public boolean equals(Object obj) {
    if (compare(this,obj) == 0)
      return(true);
    else
      return(false);
  }

  public int nameCompare(Object o1, Object o2) {
    ResultsCollector r1 = (ResultsCollector) o1;
    ResultsCollector r2 = (ResultsCollector) o2;
    int result = 0;

    String one = r1.getFirstName()!=null?r1.getFirstName():"";
    String two = r2.getFirstName()!=null?r2.getFirstName():"";
    result = IsCollator.getIsCollator().compare(one,two);

    if (result == 0){
      one = r1.getLastName()!=null?r1.getLastName():"";
      two = r2.getLastName()!=null?r2.getLastName():"";
      result = IsCollator.getIsCollator().compare(one,two);
    }
    if (result == 0){
      one = r1.getMiddleName()!=null?r1.getMiddleName():"";
      two = r2.getMiddleName()!=null?r2.getMiddleName():"";
      result = IsCollator.getIsCollator().compare(one,two);
    }

    return result;
  }

  public int abbrevationCompare(Object o1, Object o2) {
    ResultsCollector r1 = (ResultsCollector) o1;
    ResultsCollector r2 = (ResultsCollector) o2;
    int result = 0;

    String one = r1.getAbbrevation()!=null?r1.getAbbrevation():"";
    String two = r2.getAbbrevation()!=null?r2.getAbbrevation():"";
    result = IsCollator.getIsCollator().compare(one,two);

    return result;
  }

  public int totalStrokesCompare(Object o1, Object o2) {
    ResultsCollector r1 = (ResultsCollector) o1;
    ResultsCollector r2 = (ResultsCollector) o2;
    int result = 0;

    if ( r1.getDismissal() == 0 && r2.getDismissal() == 0 ) {
      if ( r1.getDifference() == r2.getDifference() ) {
        if ( r1.getTournamentPosition() == r2.getTournamentPosition() ) {
          if ( r1.getDate() != null && r2.getDate() != null ) {
            if ( r1.getDate().isLaterThan(r2.getDate()) ) {
              result = 1;
            }
            else if ( r2.getDate().isLaterThan(r1.getDate()) ) {
              result = -1;
            }
          }
          if ( result == 0 ) {
            if (r1.getLastNine() == r2.getLastNine()) {
              if (r1.getLastSix() == r2.getLastSix()) {
                if (r1.getLastThree() == r2.getLastThree()) {
                  if (r1.getLast() == r2.getLast())
                    result = 0;
                  else {
                    if (r1.getLast() < r2.getLast())
                      result = -1;
                    else
                      result = 1;
                  }
                }
                else {
                  if (r1.getLastThree() < r2.getLastThree())
                    result = -1;
                  else
                    result = 1;
                }
              }
              else {
                if (r1.getLastSix() < r2.getLastSix())
                  result = -1;
                else
                  result = 1;
              }
            }
            else {
              if (r1.getLastNine() < r2.getLastNine())
                result = -1;
              else
                result = 1;
            }
          }
        }
        else {
          if ( r1.getTournamentPosition() != -1 && r1.getTournamentPosition() != -1 ) {
            if ( r1.getTournamentPosition() < r2.getTournamentPosition() ) {
              result = -1;
            }
            else {
              result = 1;
            }
          }
          else {
            if ( r1.getTournamentPosition() == -1 && r2.getTournamentPosition() != -1 ) {
              result = 1;
            }
            else if ( r1.getTournamentPosition() != -1 && r2.getTournamentPosition() == -1 ) {
              result = -1;
            }
            else if ( r1.getTournamentPosition() == -1 && r2.getTournamentPosition() == -1 ) {
              result = 0;
            }
          }
        }
      }
      else {
        if ( r1.getDifference() < r2.getDifference() )
          result = -1;
        else
          result = 1;
      }

      if ( r1.getTotalScore() == 0 && r2.getTotalScore() > 0 )
        result = 1;
      if ( r1.getTotalScore() > 0 && r2.getTotalScore() == 0 )
        result = -1;
      if ( r1.getTotalScore() == 0 && r2.getTotalScore() == 0 )
        result = 0;
    }
    else {
      if ( r1.getDismissal() == 0 && r2.getDismissal() > 0 ) {
        result = -1;
      }
      else if ( r1.getDismissal() > 0 && r2.getDismissal() == 0 ) {
        result = 1;
      }
      else {
        result = totalStrokesCompareV2(r1,r2);
      }
    }

    return result;
  }

  public int totalStrokesCompareV2(Object o1, Object o2) {
    ResultsCollector r1 = (ResultsCollector) o1;
    ResultsCollector r2 = (ResultsCollector) o2;
    int result = 0;

    if ( r1.getDifference() == r2.getDifference() ) {
      if ( r1.getTournamentPosition() == r2.getTournamentPosition() ) {
        if ( r1.getDate() != null && r2.getDate() != null ) {
          if ( r1.getDate().isLaterThan(r2.getDate()) ) {
            result = 1;
          }
          else if ( r2.getDate().isLaterThan(r1.getDate()) ) {
            result = -1;
          }
          else {
            result = 0;
          }
        }
      }
      else {
        if ( r1.getTournamentPosition() != -1 && r1.getTournamentPosition() != -1 ) {
          if ( r1.getTournamentPosition() < r2.getTournamentPosition() ) {
            result = -1;
          }
          else {
            result = 1;
          }
        }
        else {
          if ( r1.getTournamentPosition() == -1 && r2.getTournamentPosition() != -1 ) {
            result = 1;
          }
          else if ( r1.getTournamentPosition() != -1 && r2.getTournamentPosition() == -1 ) {
            result = -1;
          }
          else if ( r1.getTournamentPosition() == -1 && r2.getTournamentPosition() == -1 ) {
            result = 0;
          }
        }
      }
    }
    else {
      if ( r1.getDifference() < r2.getDifference() )
        result = -1;
      else
        result = 1;
    }

    if ( r1.getTotalScore() == 0 && r2.getTotalScore() > 0 )
      result = 1;
    if ( r1.getTotalScore() > 0 && r2.getTotalScore() == 0 )
      result = -1;
    if ( r1.getTotalScore() == 0 && r2.getTotalScore() == 0 )
      result = 0;

    if ( r1.getDismissal() == 15 && r2.getDismissal() != 15 ) {
      result = -1;
    }
    if ( r1.getDismissal() != 15 && r2.getDismissal() == 15 ) {
      result = 1;
    }

    return result;
  }

  public int totalStrokesHandicapCompare(Object o1, Object o2) {
    ResultsCollector r1 = (ResultsCollector) o1;
    ResultsCollector r2 = (ResultsCollector) o2;
    int result = 0;

    if ( r1.getDismissal() == 0 && r2.getDismissal() == 0 ) {
      if (r1.getTotalStrokesWithHandicap() == r2.getTotalStrokesWithHandicap()) {
        if (r1.getLastNine() == r2.getLastNine()) {
          if (r1.getLastSix() == r2.getLastSix()) {
            if (r1.getLastThree() == r2.getLastThree()) {
              if (r1.getLast() == r2.getLast())
                result = 0;
              else {
                if (r1.getLast() < r2.getLast())
                  result = -1;
                else
                  result = 1;
              }
            }
            else {
              if (r1.getLastThree() < r2.getLastThree())
                result = -1;
              else
                result = 1;
            }
          }
          else {
            if (r1.getLastSix() < r2.getLastSix())
              result = -1;
            else
              result = 1;
          }
        }
        else {
          if (r1.getLastNine() < r2.getLastNine())
            result = -1;
          else
            result = 1;
        }
      }
      else {
        if (r1.getTotalStrokesWithHandicap() < r2.getTotalStrokesWithHandicap())
          result = -1;
        else
          result = 1;
      }

      if ( r1.getTotalScore() == 0 && r2.getTotalScore() > 0 )
        result = 1;
      if ( r1.getTotalScore() > 0 && r2.getTotalScore() == 0 )
        result = -1;
      if ( r1.getTotalScore() == 0 && r2.getTotalScore() == 0 )
        result = 0;
    }
    else {
      if ( r1.getDismissal() == 0 && r2.getDismissal() > 0 ) {
        result = -1;
      }
      else if ( r1.getDismissal() > 0 && r2.getDismissal() == 0 ) {
        result = 1;
      }
      else {
        return 0;
      }
    }

    return result;
  }

  public int totalPointsCompare(Object o1, Object o2) {
    ResultsCollector r1 = (ResultsCollector) o1;
    ResultsCollector r2 = (ResultsCollector) o2;
    int result = 0;

    if ( r1.getDismissal() == 0 && r2.getDismissal() == 0 ) {
      if (r1.getTotalPoints() == r2.getTotalPoints()) {
        if (r1.getLastNine() == r2.getLastNine()) {
          if (r1.getLastSix() == r2.getLastSix()) {
            if (r1.getLastThree() == r2.getLastThree()) {
              if (r1.getLast() == r2.getLast())
                result = 0;
              else {
                if (r1.getLast() < r2.getLast())
                  result = 1;
                else
                  result = -1;
              }
            }
            else {
              if (r1.getLastThree() < r2.getLastThree())
                result = 1;
              else
                result = -1;
            }
          }
          else {
            if (r1.getLastSix() < r2.getLastSix())
              result = 1;
            else
              result = -1;
          }
        }
        else {
          if (r1.getLastNine() < r2.getLastNine())
            result = 1;
          else
            result = -1;
        }
      }
      else {
        if (r1.getTotalPoints() < r2.getTotalPoints())
          result = 1;
        else
          result = -1;
      }
    }
    else {
      if ( r1.getDismissal() == 0 && r2.getDismissal() > 0 ) {
        result = -1;
      }
      else if ( r1.getDismissal() > 0 && r2.getDismissal() == 0 ) {
        result = 1;
      }
      else {
        return 0;
      }
    }

    return result;
  }

  public int roundCompare(Object o1, Object o2) {
    ResultsCollector r1 = (ResultsCollector) o1;
    ResultsCollector r2 = (ResultsCollector) o2;
    int result = 0;
    int tournamentType = r1.getGameType();

    if ( r1.getRoundScore(roundNumber_) == r2.getRoundScore(roundNumber_) ) {
      result = 0;
    }
    else {
      if ( r1.getRoundScore(roundNumber_) < r2.getRoundScore(roundNumber_) ) {
        if ( tournamentType == TOTALPOINTS )
          result = 1;
        else
          result = -1;
      }
      else {
        if ( tournamentType == TOTALPOINTS )
          result = -1;
        else
          result = 1;
      }
    }
    return result;
  }

}