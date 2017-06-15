/**
 * MoveMode.java		Aug 4, 2016, 6:56:08 PM
 */
package komorebi.projsoul.editor.modes;

import komorebi.projsoul.editor.Editor;
import komorebi.projsoul.editor.controls.RadioButton;
import komorebi.projsoul.editor.history.MovementPermissionRevision;
import komorebi.projsoul.editor.history.PermissionArrangement;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.MainE;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.script.text.EarthboundFont;
import komorebi.projsoul.script.text.TextHandler;

/**
 * Movement Permissions Editor
 * 
 * @author Aaron Roy
 * @version 
 */
@SuppressWarnings("unused")
public class MoveMode extends Mode{

  public static final int NUM_MOVEMENT_LAYERS = 3;

  private static final int LEFT_BOUND = 42, UP_BOUND = 31,
      LOW_BOUND = 26, RIGHT_BOUND_IF_TOP_ROW = 47,
      TOP_ROW_1 = 30, TOP_ROW_2 = 31;

  public enum Permission {
    TRUE('0', 0, 0),   FALSE('1',0,1),    NEUTRAL('2',0,2),
    DIAG_DOWN_TOP_TRUE('3',1,0),    DIAG_UP_TOP_TRUE('4',1,1),
    HORIZ_TOP_FALSE('5',1,2),       VERT_LEFT_TRUE('6',1,3),
    DIAG_DOWN_TOP_FALSE('7',2,0),   DIAG_UP_TOP_FALSE('8',2,1),
    HORIZ_TOP_TRUE('9',2,2),        VERT_LEFT_FALSE('a',2,3);

    private char c;
    private int row, col;
    private Permission(char c, int row, int col)
    {
      this.c = c;
      this.row = row;
      this.col = col;
    }

    public int getX(){
      return col;
    }

    public int getY(){
      return row;
    }

    public int getTexX()
    {
      return 128 + 16*col;
    }

    public int getTexY()
    {
      return 16*row;
    }

    public char asChar()
    {
      return c;
    }
    
    private static final int MAX_ROW = 2;

    public static Permission get(int x, int y)
    {
      for (Permission p: values())
      {
        if (p.getX()==x-LEFT_BOUND/2 &&
            MAX_ROW - p.getY()==y-LOW_BOUND/2)
          return p;
      }

      return TRUE;
    }
    
    public static Permission interpret(char c)
    {
      for (Permission p: values())
      {
        if (p.asChar() == c)
        {
          return p;
        }
      }
      
      return TRUE;
    }
  }

  private RadioButton[] buttons;

  private Permission perm;
  private Animation selection;
  private int selX, selY;

  private int currLayer;
  boolean applyAll;

  private TextHandler text;
  
  private static PermissionArrangement[] prevPermissions;
  private static PermissionArrangement[] currPermissions;

  private static int minx, maxx, miny, maxy;
  private static boolean changed;

  /**
   * @param col The collision data for the map
   */
  public MoveMode(Permission[][][] col) {
    
    prevPermissions = new PermissionArrangement[3];
    currPermissions = new PermissionArrangement[3];
    
    for (int layer = 0; layer < currPermissions.length; layer++)
    {
      prevPermissions[layer] = new PermissionArrangement(col[layer],
          0, 0, layer);
      currPermissions[layer] = new PermissionArrangement(col[layer],
          0, 0, layer);
    }

    
    selection = new Animation(8, 8, 16, 16, 2, false, 2);
    for(int i=3; i >= 0; i--){
      selection.add(0 , 0 , i);
      selection.add(16, 0 , i);
    }

    selX = LEFT_BOUND;
    selY = UP_BOUND-1;

    

    buttons = new RadioButton[3];
    
    changed = false;

    for (int i = 0; i < buttons.length; i++)
    {
      buttons[i] = new RadioButton(MainE.WIDTH - 32*4 + i*42 + 20, 
          32*16+10, i==0, Draw.FULL_SCREEN) {
        @Override
        public void click()
        {
          if (checked)
          {
            for (RadioButton button: buttons)
            {
              if (button != this)
              {
                button.setChecked(false);
              }
            }
          } else
          {
            checked = true;
          }

          for (int i = 0; i < buttons.length; i++)
          {
            if (buttons[i].isChecked())
            {
              currLayer = i;
              break;
            }
          }
        }
      };
    }

    perm = Permission.TRUE;

    text = new TextHandler();
    text.write("0 :     1 :     2 :", MainE.WIDTH - 32*4 + 2, 
        32*16+10, new EarthboundFont(2));
  }

  @Override
  public void update(){
    
    if (KeyHandler.keyClick(Key.LBUTTON) && checkMapBounds())
    {
      minx = maxx = mx;
      miny = maxy = my;
    }
    
    if(lButtonIsDown && checkMapBounds() && (!mouseSame || 
        !lButtonWasDown)) {
      
      if (mx < minx)
        minx = mx;
      if (mx > maxx)
        maxx = mx;
      if (my < miny)
        miny = my;
      if (my > maxy)
        maxy = my;
      
      changed = true;
      
      if (applyAll)
      {
        for (int l = 0; l < MoveMode.NUM_MOVEMENT_LAYERS; l++)
        {
          currPermissions[l].setPermissionAt(perm, mx, my); 
        }
      } else
      {
        currPermissions[currLayer].setPermissionAt(perm, mx, my); 
      }
      EditorMap.setUnsaved();
    }
    
    if (KeyHandler.keyRelease(Key.LBUTTON) && changed)
    {
      createRevision();
    }

    if (KeyHandler.keyClick(Key.LBUTTON))
    {
      if (checkPaletteBounds())
      {
        selX = getLiteralTileX()/2*2;
        selY = getLiteralTileY()/2*2;
        perm = Permission.get(getLiteralTileX()/2, 
            getLiteralTileY()/2);
      } else if (getLiteralTileX()/2 == 24 && getLiteralTileY()/2 == 15)
      {
        applyAll = !applyAll;
      }      
    }

    //Flood Fills tiles
    if(mButtonIsDown && !mButtonWasDown && checkMapBounds()){

      if (applyAll)
      {
        for (int l = 0; l < MoveMode.NUM_MOVEMENT_LAYERS; l++)
        {
          flood(mx, my, perm, l);
        }
      } else
      {
        flood(mx, my, perm, currLayer);
      }
      
      createRevision();
      EditorMap.setUnsaved();
    }

    //Creates a selection
    if(rStartDragging || lStartDragging){
      initX = getMouseX();
      initY = getMouseY();

      if(initX < 0){
        initX = 0;
      }else if(initX >= Editor.getMap().currentSublayer().getTiles()[0].length){
        initX = Editor.getMap().currentSublayer().getTiles()[0].length-1;
      }

      if(initY < 0){
        initY = 0;
      }else if(initY >= Editor.getMap().currentSublayer().getTiles().length){
        initY = Editor.getMap().currentSublayer().getTiles().length-1;
      }
    }

    if(lIsDragging && checkMapBounds()){
      createSelection(perm);
    }

    for (RadioButton button: buttons)
    {
      button.update();
    }
  }

  @Override
  public void render(){
    float x = EditorMap.getX();
    float y = EditorMap.getY();

    for (int i = 0; i < currPermissions[0].getHeight(); i++) {
      for (int j = 0; j < currPermissions[0].getWidth(); j++) {
        if(EditorMap.checkTileInBounds(x+j*SIZE, y+i*SIZE)){
          /*
          if(collision[i][j]){
            //Red transparent pixel
            Draw.rectZoom(x+j*SIZE, y+i*SIZE, SIZE, SIZE, 16, 16, 16, 16, 2,
                Editor.zoom(), EditorMap.getX(), EditorMap.getY());
          }else{
            //Green transparent pixel
            Draw.rectZoom(x+j*SIZE, y+i*SIZE, SIZE, SIZE, 17, 16, 17, 16, 2,
                Editor.zoom(), EditorMap.getX(), EditorMap.getY());
          }*/

          Draw.rectZoom(x+j*SIZE, y+i*SIZE, SIZE, SIZE, 
              currPermissions[currLayer].getPermissionAt(j, i).getTexX(), 
              currPermissions[currLayer].getPermissionAt(j, i).getTexY(), 
              currPermissions[currLayer].getPermissionAt(j, i).getTexX()+SIZE, 
              currPermissions[currLayer].getPermissionAt(j, i).getTexY()+SIZE, 
              2, Editor.zoom(), EditorMap.getX(), EditorMap.getY());
        }


      }
    }

    Draw.rect(MainE.WIDTH - 32*4, 26*16, 32*4, 128, 128, 66, 129, 
        67, 2);//draws gray background box

    if (applyAll)
    {
      Draw.rect(MainE.WIDTH - 32*4 + 96, 32*16-32, 32, 32, 
          126, 82, 142, 98, 2);//apply all arrow
    } else
    {
      Draw.rect(MainE.WIDTH - 32*4 + 96, 32*16-32, 32, 32, 
          147, 82, 163, 98, 2);
        //grayed-out apply all arrow
    }

    Draw.rect(MainE.WIDTH - 32*4, 26*16, 128, 96, 128, 0, 192, 48, 2);
    //draws the palette


    for (RadioButton button: buttons)
    {
      button.render();
    }

    text.render();

    EditorMap.renderGrid();

    selection.play(selX*16, selY*16);

  }

  /**
   * Creates a new selection
   */
  private void createSelection(Permission type) {
    int firstX, lastX;
    int firstY, lastY;

    firstX = Math.min(initX, getMouseX());
    firstY = Math.min(initY, getMouseY());

    lastX = Math.max(initX, getMouseX());
    lastY = Math.max(initY, getMouseY());


    for(int i = firstY; i <= lastY; i++){
      for(int j = firstX; j <= lastX; j++){
        currPermissions[currLayer].setPermissionAt(type, j, i);
      }
    }
  }
  
  /**
   * Applies the data stored in the given permission arrangement
   * object to the map's movement permissions
   * @param arrangement The permissions to be applied
   */
  public void applyPermissions(PermissionArrangement arrangement)
  {
    applyPermissionsToCurrent(arrangement);
    applyPermissionsToPrevious(arrangement);
  }
  
  private void applyPermissionsToCurrent(PermissionArrangement arrangement)
  {
    int layer = arrangement.getLayer();
    int tx = arrangement.getTileX(), ty = arrangement.getTileY();
    
    for (int i = 0; i < arrangement.getHeight(); i++)
    {
      for (int j = 0; j < arrangement.getWidth(); j++)
      {
        currPermissions[layer].setPermissionAt(
            arrangement.getPermissionAt(j, i), 
            j + tx, i + ty);
      }
    }
  }
  
  private void applyPermissionsToPrevious(PermissionArrangement arrangement)
  {
    int layer = arrangement.getLayer();
    int tx = arrangement.getTileX(), ty = arrangement.getTileY();
    
    for (int i = 0; i < arrangement.getHeight(); i++)
    {
      for (int j = 0; j < arrangement.getWidth(); j++)
      {
        prevPermissions[layer].setPermissionAt( 
            arrangement.getPermissionAt(j, i), 
            j + tx, i + ty);
      }
    }
  }

  /**
   * Recursive method that flood fills collisions
   * 
   * @param mouseX starting tile x
   * @param mouseY starting tile y
   * @param type tile to search and destroy
   */
  private void flood(int mouseX, int mouseY, Permission type, int layer) {
    if (mouseX < 0 || mouseX >= currPermissions[0].getWidth() ||
        mouseY < 0 || mouseY >= currPermissions[0].getHeight()){
      return;
    }
    if(currPermissions[layer].getPermissionAt(mouseX, mouseY) == type){
      return;
    }
    
    if (mouseX < minx)
      minx = mouseX;
    if (mouseX > maxx)
      maxx = mouseX;
    if (mouseY < miny)
      miny = mouseY;
    if (mouseY > maxy)
      maxy = mouseY;

    currPermissions[layer].setPermissionAt(type, mouseX, mouseY);
    flood(mouseX-1, mouseY,   type, layer);
    flood(mouseX+1, mouseY,   type, layer);
    flood(mouseX,   mouseY+1, type, layer);
    flood(mouseX,   mouseY-1, type, layer); 
  }

  private boolean checkPaletteBounds()
  {
    return getLiteralTileX() >= LEFT_BOUND && getLiteralTileY() <= 
        UP_BOUND && getLiteralTileY() >= LOW_BOUND && 
        !(getLiteralTileX() > RIGHT_BOUND_IF_TOP_ROW && 
            (getLiteralTileY() == TOP_ROW_1 || getLiteralTileY() == 
            TOP_ROW_2));
  }
  

  private void createRevision()
  {    
    PermissionArrangement[] prePerms;
    PermissionArrangement[] postPerms;
    
    if (!applyAll)
    {
      PermissionArrangement pre = new PermissionArrangement(
          new Permission[maxy-miny+1][maxx-minx+1], minx, miny, currLayer);
      PermissionArrangement post = new PermissionArrangement(
          new Permission[maxy-miny+1][maxx-minx+1], minx, miny, currLayer);
      
      for (int i = miny; i <= maxy; i++)
      {
        for (int j = minx; j <= maxx; j++)
        {
          pre.setPermissionAt(prevPermissions[currLayer].getPermissionAt(j, i), 
              j-minx, i-miny);
          post.setPermissionAt(currPermissions[currLayer].getPermissionAt(j, i), 
              j-minx, i-miny);                 
        }
      }
      
      prePerms = new PermissionArrangement[1];
      postPerms = new PermissionArrangement[1];
      
      prePerms[0] = pre;
      postPerms[0] = post;
    } else
    {
      prePerms = new PermissionArrangement[3];
      postPerms = new PermissionArrangement[3];
      
      for (int layer = 0; layer < prePerms.length; layer++)
      {
        prePerms[layer] = new PermissionArrangement(
            new Permission[maxy-miny+1][maxx-minx+1], minx, miny, layer);
        postPerms[layer] = new PermissionArrangement(
            new Permission[maxy-miny+1][maxx-minx+1], minx, miny, layer);
        
        for (int i = miny; i <= maxy; i++)
        {
          for (int j = minx; j <= maxx; j++)
          {
            prePerms[layer].setPermissionAt(prevPermissions[layer].getPermissionAt(j, i), 
                j-minx, i-miny);
            postPerms[layer].setPermissionAt(currPermissions[layer].getPermissionAt(j, i), 
                j-minx, i-miny);
            
            
          }
        }
      }
    }
    
    for (PermissionArrangement arr: postPerms)
    {
      applyPermissionsToPrevious(arr);
    }
    
    Editor.getMap().addRevision(
        new MovementPermissionRevision(prePerms, postPerms));
    changed = false;
  }
  

}