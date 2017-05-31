package komorebi.projsoul.script.commands;

import komorebi.projsoul.entities.Person;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.script.commands.abstracts.CommandOnAnyPerson;
import komorebi.projsoul.script.commands.keywords.Keyword;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;

public class SayCommand extends CommandOnAnyPerson {

  private String quotation;
  
  @Override
  public void interpret(String data, int line) throws InvalidScriptSyntaxExceptionWithLine {
    quotation = data + "";
  }

  @Override
  public void execute(Person person) {
    
    String sayThis = quotation.replace("@me", 
        Map.getPlayer().getCharacter().getNameFormatted());
   
    person.say(sayThis);
    

  }

}
