package com.idega.block.survey.data;


public interface SurveyEntity extends com.idega.data.IDOEntity
{
 public void addQuestion(com.idega.block.survey.data.SurveyQuestion p0)throws com.idega.data.IDOAddRelationshipException;
 public int getCategoryID();
 public com.idega.core.localisation.data.ICLocale getCreationLocale();
 public java.lang.String getDescription();
 public java.sql.Timestamp getEndTime();
 public int getFolderID();
 public java.lang.String getName();
 public java.sql.Timestamp getStartTime();
 public java.util.Collection getSurveyQuestions()throws com.idega.data.IDORelationshipException;
 public void removeQuestion(com.idega.block.survey.data.SurveyQuestion p0)throws com.idega.data.IDORemoveRelationshipException;
 public void setCategoryID(com.idega.block.category.data.ICInformationCategory p0);
 public void setCreationLocale(com.idega.core.localisation.data.ICLocale p0);
 public void setDescription(java.lang.String p0);
 public void setEndTime(java.sql.Timestamp p0);
 public void setFolder(com.idega.block.category.data.ICInformationFolder p0);
 public void setName(java.lang.String p0);
 public void setRemoved(com.idega.user.data.User p0);
 public void setStartTime(java.sql.Timestamp p0);
 public void store();
}
