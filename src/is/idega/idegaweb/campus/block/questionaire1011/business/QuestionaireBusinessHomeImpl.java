package is.idega.idegaweb.campus.block.questionaire1011.business;


public class QuestionaireBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements QuestionaireBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return QuestionaireBusiness.class;
 }


 public QuestionaireBusiness create() throws javax.ejb.CreateException{
  return (QuestionaireBusiness) super.createIBO();
 }



}