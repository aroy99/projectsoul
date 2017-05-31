package komorebi.projsoul.script.commands.keywords;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import komorebi.projsoul.script.commands.AlignCommand;
import komorebi.projsoul.script.commands.AskCommand;
import komorebi.projsoul.script.commands.BlockCommand;
import komorebi.projsoul.script.commands.CallBranchCommand;
import komorebi.projsoul.script.commands.ChangeSpriteCommand;
import komorebi.projsoul.script.commands.FadeInCommand;
import komorebi.projsoul.script.commands.FadeoutCommand;
import komorebi.projsoul.script.commands.FlagBooleanCommand;
import komorebi.projsoul.script.commands.FreezeCommand;
import komorebi.projsoul.script.commands.GoToCommand;
import komorebi.projsoul.script.commands.IfElseCommand;
import komorebi.projsoul.script.commands.JogCommand;
import komorebi.projsoul.script.commands.LockPlayerCommand;
import komorebi.projsoul.script.commands.PauseCommand;
import komorebi.projsoul.script.commands.PlayMusicCommand;
import komorebi.projsoul.script.commands.RetileCommand;
import komorebi.projsoul.script.commands.SayCommand;
import komorebi.projsoul.script.commands.StopMusicCommand;
import komorebi.projsoul.script.commands.TurnCommand;
import komorebi.projsoul.script.commands.UnblockCommand;
import komorebi.projsoul.script.commands.UnlockPlayerCommand;
import komorebi.projsoul.script.commands.WalkCommand;
import komorebi.projsoul.script.commands.WarpCommand;
import komorebi.projsoul.script.commands.WarpTogetherCommand;
import komorebi.projsoul.script.commands.abstracts.Command;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;

public class Keywords {

  private static ArrayList<Keyword> keywords;
  
  public static void loadKeywords()
  {
    keywords = new ArrayList<Keyword>();
    
    addKeyword("align", AlignCommand.class, false);
    addKeyword("ask", AskCommand.class, false);
    addKeyword("block", BlockCommand.class, false);
    addKeyword("call", CallBranchCommand.class, false);
    addKeyword("fadein", FadeInCommand.class, false);
    addKeyword("fadeout", FadeoutCommand.class, false);
    addKeyword("flag", FlagBooleanCommand.class, false);
    addKeyword("freeze", FreezeCommand.class, false);
    addKeyword("if", IfElseCommand.class, false);
    addKeyword("lock", LockPlayerCommand.class, false);
    addKeyword("pause", PauseCommand.class, false);
    addKeyword("play", PlayMusicCommand.class, false);
    addKeyword("retile", RetileCommand.class, false);
    addKeyword("stopmusic", StopMusicCommand.class, false);
    addKeyword("unblock", UnblockCommand.class, false);
    addKeyword("unlock", UnlockPlayerCommand.class, false);
    addKeyword("warp", WarpCommand.class, false);
    addKeyword("warpwith", WarpTogetherCommand.class, false);
    
    addKeyword("change", ChangeSpriteCommand.class, true);
    addKeyword("goto", GoToCommand.class, true);
    addKeyword("jog", JogCommand.class, true);
    addKeyword("say", SayCommand.class, true);
    addKeyword("turn", TurnCommand.class, true);
    addKeyword("walk", WalkCommand.class, true);
  }
  
  
  private static void addKeyword(String keyword, Class<? extends Command> classFile,
      boolean canApplyToPlayer)
  {
    try {
      keywords.add(new Keyword(keyword, classFile.getConstructor(), false));
    } catch (NoSuchMethodException e) {
      throw new UndefinedConstructorException(classFile.getSimpleName());
    }
  }
  
  public static boolean keywordExists(String keyword)
  {
    for (Keyword k: keywords)
    {
      if (k.matches(keyword))
        return true;
    }
    
    return false;
  }
  
  public static Command createNewInstanceWithKeyword(String keyword)
  {
    
    Keyword match = getKeyword(keyword);
    
    Constructor<?> constructor = match.getConstructor();
    try {
      return (Command) constructor.newInstance();
    } catch (InstantiationException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException e) {
      e.printStackTrace();
    }
    
    throw new RuntimeException("Invalid keyword");
  }

  
  private static Keyword getKeyword(String keyword)
  {
    for (Keyword k: keywords)
    {
      if (k.matches(keyword))
        return k;
    }
    
    throw new RuntimeException("Unmet pre-condition: no such keyword as " + keyword);
  }

}
