package komorebi.projsoul.script;

/**
 * The EarthBound font, a placeholder for now
 *
 * @author Andrew Faulkenberry
 */
public class EarthboundFont extends Font {

  public static final int SCALE = 16;
  
  public EarthboundFont(int scale)
  {
    super(scale);
  }
  
  public int getFontPoint()
  {
    return 8;
  }
  
  public int getTexture()
  {
    return 5;
  }
  
  @Override
  public int getTexX(char c) {
    switch (c)
    {
      case' ':case',':case'8':case'D':case'P':case'h':case't':
        return 0;
      case'!':case'-':case'9':case'E':case'Q':case'i':case'u':
        return SCALE;
      case'"':case'.':case':':case'F':case'R':case'j':case'v':
        return SCALE*2;
      case'/':case';':case'G':case'S':case'k':case'w':
        return SCALE*3;
      case'$':case'0':case'H':case'T':case'l':case'x':
        return SCALE*4;
      case'%':case'1':case'=':case'I':case'U':case'a':case'm':case'y':
        return SCALE*5;
      case'2':case'J':case'V':case'b':case'n':case'z':
        return SCALE*6;
      case'\'':case'3':case'?':case'K':case'W':case'c':case'o':case'[':
        return SCALE*7;
      case'(':case'4':case'L':case'X':case'd':case'p':
        return SCALE*8;
      case')':case'5':case'A':case'M':case'Y':case'e':case'q':case']':
        return SCALE*9;
      case'*':case'6':case'B':case'N':case'Z':case'f':case'r':case'~':
        return SCALE*10;
      case'+':case'7':case'C':case'O':case'g':case's':
        return SCALE*11;
      default:
        return 0;
    }
  }

  @Override
  public int getTexY(char c) {
    switch(c)
    {
      case' ':case'!':case'"':case'$':case'%':case'\'':case'(':case')':case'*':
      case'+':
        return 0;
      case',':case'-':case'.':case'/':case'0':case'1':case'2':case'3':case'4':
      case'5':case'6':case'7':
        return SCALE;
      case'8':case'9':case':':case';':case'=':case'?':case'A':case'B':case'C':
        return SCALE*2;
      case'D':case'E':case'F':case'G':case'H':case'I':case'J':case'K':case'L':
      case'M':case'N':case'O':
        return SCALE*3;
      case'P':case'Q':case'R':case'S':case'T':case'U':case'V':case'W':case'X':
      case'Y':case'Z':
        return SCALE*4;
      case'a':case'b':case'c':case'd':case'e':case'f':case'g':
        return SCALE*5;
      case'h':case'i':case'j':case'k':case'l':case'm':case'n':case'o':case'p':
      case'q':case'r':case's':
        return SCALE*6;
      case'[':case']':case'~':case't':case'u':case'v':case'w':case'x':case'y':
      case'z':
        return SCALE*7;
      default:
        break;
    }

    return 0;
  }

  @Override
  public int getTexUnder(char c) {
    switch(c)
    {
      case 'g': case 'j': case 'p': case 'q': case 'y': case '[': case ']': 
        return 3;
      default:
        return 0;
    }
  }
  
  @Override
  public int getKerning(char c1, char c2) {
    if(c1 == ' ' || c2 == ' '){
      return 0;
    }
    
    switch(c1)
    {
      default:
        return 1;
    }
  }
  
  @Override
  public int getLength(char c) {
    switch (c)
    {
      case'!':case'.':case':':case'I':case'i':case'l':
        return 1;
      case'\'':case',':case'-':case';':case'j':case'[':case']':
      case' ':
        return 2;
      case'\"':case'(':case')':case'*':case'f':case'r':case't':
        return 3;
      case'/':case'0':case'1':case'2':case'3':case'5':case'6':case'7':case'8':
      case'9':case'?':case'E':case'F':case'J':case'L':case'Z':case'b':
      case'c':case'd':case'e':case'g':case'h':case'k':case'n':case'o':
      case'p':case'q':case's':case'u':case'x':case'y':case'z':
        return 4;
      case'$':case'+':case'4':case'=':case'B':case'C':case'D':case'G':
      case'H':case'K':case'N':case'O':case'P':case'Q':case'R':case'S':
      case'T':case'U':case'X':case'Y':case'a':case'v':
        return 5;
      case'A':case'V':case'~':
        return 6;
      case'M':case'W':case'm':case'w':
        return 7;
      case'%':
        return 9;
      default:
        return 0;
    }
  }

}
