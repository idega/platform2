package is.idega.idegaweb.golf.entity;

import javax.ejb.*;

public interface TournamentGroupRoundDay extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public java.lang.String getName();
 public is.idega.idegaweb.golf.entity.TournamentDay getTournamentDay();
 public is.idega.idegaweb.golf.entity.TournamentGroup getTournamentGroup();
 public is.idega.idegaweb.golf.entity.TournamentRound getTournamentRound();
}
