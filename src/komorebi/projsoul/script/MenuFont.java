/**
 * MenuFont.java     Jan 26, 2017, 9:28:01 AM
 */
package komorebi.projsoul.script;

/**
 * The font to be used with the Menus in-game
 *
 * @author Aaron Roy
 */
public class MenuFont extends Font {
  
  public static final int SCALE = 16;

  /**
   * Creates a MenuFont
   * 
   * @param scale The scaling of the font
   */
  public MenuFont(int scale) {
    super(scale);
  }

  @Override
  public int getTexX(char c) {
    switch(c){
      case 'A': case 'Q': case 'a': case 'q': case '0':
        return 0;                  
      case 'B': case 'R': case 'b': case 'r': case '1':
        return SCALE;                  
      case 'C': case 'S': case 'c': case 's': case '2':
        return SCALE*2;                  
      case 'D': case 'T': case 'd': case 't': case '3':
        return SCALE*3;                  
      case 'E': case 'U': case 'e': case 'u': case '4':
        return SCALE*4;                
      case 'F': case 'V': case 'f': case 'v': case '5':
        return SCALE*5;                
      case 'G': case 'W': case 'g': case 'w': case '6':
        return SCALE*6;           
      case 'H': case 'X': case 'h': case 'x': case '7':
        return SCALE*7;           
      case 'I': case 'Y': case 'i': case 'y': case '8':
        return SCALE*8;           
      case 'J': case 'Z': case 'j': case 'z': case '9':
        return SCALE*9;                 
      case 'K':           case 'k':           
        return SCALE*10;                 
      case 'L':           case 'l':           
        return SCALE*11;                 
      case 'M':           case 'm':           
        return SCALE*12;                   
      case 'N':           case 'n':           
        return SCALE*13;                 
      case 'O':           case 'o':           
        return SCALE*14;                 
      case 'P':           case 'p':           
        return SCALE*15;                 
      default: return 0;
    }
  }

  @Override
  public int getTexY(char c) {
    switch(c){
      case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': 
      case 'H': case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': 
      case 'O': case 'P':
        return 0;
      case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W':
      case 'X': case 'Y': case 'Z':
        return SCALE;
      case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': 
      case 'h': case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': 
      case 'o': case 'p':
        return SCALE*2;
      case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w':
      case 'x': case 'y': case 'z':
        return SCALE*3;
      case '0': case '1': case '2': case '3': case '4': case '5': case '6':
      case '7': case '8': case '9':
        return SCALE*4;
      default: return SCALE*5;
    }
  }

  @Override
  public int getLength(char c) {
    switch(c)
    {
      case 'i': case 'l':
        return 3;
      case 't':
        return 4;
      case 'k': case 's': case 'v': case ' ':
        return 5;
      case 'b': case 'c':  case 'e': case 'f': case 'h': case 'n': case 'o':
      case 'q': case 'r': case 'u': case 'y': case 'z':
        return 6;
      case 'a': case 'd': case 'g': case 'j': case 'w': case 'x':
        return 7;
      case 'C':case 'E': case 'G': case 'J': case 'T': case 'U': case 'm':
      case 'p':
        return 8;
      case 'A': case 'B': case 'H': case 'L': case 'O': case 'S': case 'X': 
      case '0': case '1': case '2': case '3': case '4': case '5': case '6': 
      case '7': case '8': case '9': 
        return 9;
      case 'F':case 'I': case 'K': case 'P': case 'Q': case 'R': case 'V': 
      case 'Y': case 'Z': 
        return 10;
      case 'D':
        return 11;
      case 'M':case 'N': 
        return 12;
      case 'W':
        return 13;
      default:
        return 0;
    }
  }
  
  @Override
  public int getTexUnder(char c) {
    switch(c)
    {
      case 'D': 
        return 1;
      case 'G': 
        return 2;
      case 'g': case 'j': case 'p': case 'q': case 'y':
        return 4;
      default:
        return 0;
    }
  }

  //TODO Implement a system of reading this from a file, then putting it into a hashmap
  @Override
  public int getKerning(char c1, char c2) {
    if(c1 == ' ' || c2 == ' '){
      return 0;
    }
    
    switch(c1){
      case 'f':
        switch(c2){
          case 'o': return -2;
          default:  return 1;
        }

      case 'i':
        switch(c2){
          case 'c': return 0;
          default:  return 1;
        }

      case 'o':
        switch(c2){
          case 'x': return 0;
          default:  return 1;
        }
        
      case 'm':
        switch(c2){
          case 'p': return -1;
          default:  return 1;
        }
        
      case 'v':
        switch(c2){
          case 'e': return 0;
          default:  return 1;
        }

      default:
        return 1;
    }
  }
  
  
  
  @Override
  public int getFontPoint() {
    return 11;
  }

  @Override
  public int getTexture() {
    return 14;
  }

}
