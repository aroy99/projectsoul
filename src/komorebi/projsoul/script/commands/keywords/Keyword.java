package komorebi.projsoul.script.commands.keywords;

import java.lang.reflect.Constructor;

import komorebi.projsoul.script.commands.abstracts.Command;

public class Keyword {

  private String keyword;
  private Constructor<? extends Command> constructor;
  private boolean canApplyToPlayer;
  
  public Keyword(String keyword, Constructor<? extends Command> constructor,
      boolean canApplyToPlayer)
  {
    this.keyword = keyword;
    this.constructor = constructor;
    this.canApplyToPlayer = canApplyToPlayer;
  }
  
  public boolean matches(String string)
  {
    return string.equals(keyword);
  }
  
  public Constructor<? extends Command> getConstructor()
  {
    return constructor;
  }
}
