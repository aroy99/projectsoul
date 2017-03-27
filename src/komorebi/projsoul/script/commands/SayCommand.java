package komorebi.projsoul.script.commands;

import komorebi.projsoul.entities.Person;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.script.commands.abstracts.CommandOnAnyPerson;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;

public class SayCommand extends CommandOnAnyPerson {

  private String quotation;
  
  public static String keyword()
  {
    return "say";
  }
  
  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
    quotation = data + "";
  }

  @Override
  public void execute(Person person) {
    
    String sayThis = quotation.replace("@me", 
        Map.getPlayer().getCharacter().getName());
   
    person.say(sayThis, lock);
    

  }

}
