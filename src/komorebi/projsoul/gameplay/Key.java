/**
 * Key.java Jul 6, 2016, 12:32:17 PM
 */
package komorebi.projsoul.gameplay;

import org.lwjgl.input.Keyboard;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public enum Key {
  ESC, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, INS, DEL, PGUP, PGDN,
  HOME, END, GRAVE, ROW1, ROW2, ROW3, ROW4, ROW5, ROW6, ROW7, ROW8, ROW9, ROW0,
  DASH, EQUALS, BACKSPACE, NUMLOCK, SLASH, ASTERISK, MINUS, PLUS, ENTER, PERIOD,
  PAD1, PAD2, PAD3, PAD4, PAD5, PAD6, PAD7, PAD8, PAD9, PAD0, DECIMAL,
  TAB, Q,W,E,R,T,Y,U,I,O,P, OPEN_BRACKET, CLOSE_BRACKET, BACKSLASH,
  CAPS, A,S,D,F,G,H,J,K,L,SEMICOLON, APOSTROPHE, RSHIFT,
  LSHIFT,Z,X,C,V,B,N,M,COMMA,LCTRL,RCTRL,LALT,RALT,SPACE,LEFT,RIGHT,UP,DOWN,
  CTRL, SHIFT, 
  LBUTTON, RBUTTON, MBUTTON;
  
  /**
   * Returns the Open GL library's corresponding integer index for the specified
   * key
   * @return The Keyboard class index
   */
  public int getGLKey()
  {
    switch (this)
    {
      case A:
        return Keyboard.KEY_A;
      case APOSTROPHE:
        return Keyboard.KEY_APOSTROPHE;
      case ASTERISK:
        return Keyboard.KEY_MULTIPLY;
      case B:
        return Keyboard.KEY_B;
      case BACKSLASH:
        return Keyboard.KEY_BACKSLASH;
      case BACKSPACE:
        return Keyboard.KEY_BACK;
      case C:
        return Keyboard.KEY_C;
      case CAPS:
        return Keyboard.KEY_CAPITAL;
      case CLOSE_BRACKET:
        return Keyboard.KEY_RBRACKET;
      case COMMA:
        return Keyboard.KEY_COMMA;
      case D:
        return Keyboard.KEY_D;
      case DASH:
        return Keyboard.KEY_MINUS;
      case DECIMAL:
        return Keyboard.KEY_DECIMAL;
      case DEL:
        return Keyboard.KEY_DELETE;
      case DOWN:
        return Keyboard.KEY_DOWN;
      case E:
        return Keyboard.KEY_E;
      case END:
        return Keyboard.KEY_END;
      case ENTER:
        return Keyboard.KEY_RETURN;
      case EQUALS:
        return Keyboard.KEY_EQUALS;
      case ESC:
        return Keyboard.KEY_ESCAPE;
      case F:
        return Keyboard.KEY_F;
      case F1:
        return Keyboard.KEY_F1;
      case F10:
        return Keyboard.KEY_F10;
      case F11:
        return Keyboard.KEY_F11;
      case F12:
        return Keyboard.KEY_F12;
      case F2:
        return Keyboard.KEY_F2;
      case F3:
        return Keyboard.KEY_F3;
      case F4:
        return Keyboard.KEY_F4;
      case F5:
        return Keyboard.KEY_F5;
      case F6:
        return Keyboard.KEY_F6;
      case F7:
        return Keyboard.KEY_F7;
      case F8:
        return Keyboard.KEY_F8;
      case F9:
        return Keyboard.KEY_F9;
      case G:
        return Keyboard.KEY_G;
      case GRAVE:
        return Keyboard.KEY_GRAVE;
      case H:
        return Keyboard.KEY_H;
      case HOME:
        return Keyboard.KEY_HOME;
      case I:
        return Keyboard.KEY_I;
      case INS:
        return Keyboard.KEY_INSERT;
      case J:
        return Keyboard.KEY_J;
      case K:
        return Keyboard.KEY_K;
      case L:
        return Keyboard.KEY_L;
      case LALT:
        return Keyboard.KEY_LMETA;
      case LCTRL:
        return Keyboard.KEY_LCONTROL;
      case LEFT:
        return Keyboard.KEY_LEFT;
      case M:
        return Keyboard.KEY_M;
      case MINUS:
        return Keyboard.KEY_SUBTRACT;
      case N:
        return Keyboard.KEY_N;
      case NUMLOCK:
        return Keyboard.KEY_NUMLOCK;
      case O:
        return Keyboard.KEY_O;
      case OPEN_BRACKET:
        return Keyboard.KEY_LBRACKET;
      case P:
        return Keyboard.KEY_P;
      case PAD0:
        return Keyboard.KEY_NUMPAD0;
      case PAD1:
        return Keyboard.KEY_NUMPAD1;
      case PAD2:
        return Keyboard.KEY_NUMPAD2;
      case PAD3:
        return Keyboard.KEY_NUMPAD3;
      case PAD4:
        return Keyboard.KEY_NUMPAD4;
      case PAD5:
        return Keyboard.KEY_NUMPAD5;
      case PAD6:
        return Keyboard.KEY_NUMPAD6;
      case PAD7:
        return Keyboard.KEY_NUMPAD7;
      case PAD8:
        return Keyboard.KEY_NUMPAD8;
      case PAD9:
        return Keyboard.KEY_NUMPAD9;
      case PERIOD:
        return Keyboard.KEY_PERIOD;
      case PGDN:
        return Keyboard.KEY_NEXT;
      case PGUP:
        return Keyboard.KEY_PRIOR;
      case PLUS:
        return Keyboard.KEY_ADD;
      case Q:
        return Keyboard.KEY_Q;
      case R:
        return Keyboard.KEY_R;
      case RALT:
        return Keyboard.KEY_RMETA;
      case RCTRL:
        return Keyboard.KEY_RCONTROL;
      case RIGHT:
        return Keyboard.KEY_RIGHT;
      case ROW0:
        return Keyboard.KEY_0;
      case ROW1:
        return Keyboard.KEY_1;
      case ROW2:
        return Keyboard.KEY_2;
      case ROW3:
        return Keyboard.KEY_3;
      case ROW4:
        return Keyboard.KEY_4;
      case ROW5:
        return Keyboard.KEY_5;
      case ROW6:
        return Keyboard.KEY_6;
      case ROW7:
        return Keyboard.KEY_7;
      case ROW8:
        return Keyboard.KEY_8;
      case ROW9:
        return Keyboard.KEY_9;
      case S:
        return Keyboard.KEY_S;
      case SEMICOLON:
        return Keyboard.KEY_SEMICOLON;
      case LSHIFT:
        return Keyboard.KEY_LSHIFT;
      case RSHIFT:
        return Keyboard.KEY_RSHIFT;
      case SLASH:
        return Keyboard.KEY_SLASH;
      case SPACE:
        return Keyboard.KEY_SPACE;
      case T:
        return Keyboard.KEY_T;
      case TAB:
        return Keyboard.KEY_TAB;
      case U:
        return Keyboard.KEY_U;
      case UP:
        return Keyboard.KEY_UP;
      case V:
        return Keyboard.KEY_V;
      case W:
        return Keyboard.KEY_W;
      case X:
        return Keyboard.KEY_X;
      case Y:
        return Keyboard.KEY_Y;
      case Z:
        return Keyboard.KEY_Z;
      case LBUTTON:
        return Keyboard.KEYBOARD_SIZE;
      case RBUTTON:
        return Keyboard.KEYBOARD_SIZE+1;
      case MBUTTON:
        return Keyboard.KEYBOARD_SIZE+2;
      default:
        return 0;
      
    }
  }
}
