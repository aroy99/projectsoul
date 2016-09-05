/**
 * TileSetEditor.java		Jul 28, 2016, 2:35:58 PM
 */
package komorebi.projsoul.tileseteditor;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.map.TileList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class TileSetEditor implements Playable {

  private int max, min;

  private TileList[][] left;
  private TileList[][] right;

  ArrayList<TileList[][]> history;
  TileList[][] redo;
  private int historyIndex;
  private boolean changeMade;
  
  private boolean justSaved;
  private int justSavedCount;

  private static final int HEIGHT = 256;
  private static final int OFFX = 8;

  private int selX, selY;
  private int swapX, swapY;
  private int delX, delY, delSx, delSy;

  private TileList selTiles[][];
  private TileList swapTiles[][];

  private boolean mouseIsDown, mouseWasDown, mouseClick, mouseRelease;
  private boolean rightIsDown, rightWasDown, rightClick;
  private boolean scrollIsDown, scrollWasDown, scrollClick, scrollRelease;

  private boolean swapMode, swapLift;
  private boolean eraser;

  int prevMx, prevMy;
  int anchorX, anchorY;
  int anchorSwapX, anchorSwapY;
  int anchorDelX, anchorDelY;

  private boolean save;
  private boolean newSave;
  private String savePath, saveName;
  
  private boolean grid;

  public TileSetEditor()
  {

    save = true;
    newSave = false;

    min = 0;
    max = 16;

    left = new TileList[32][8];

    for (int i=0, k=0; i<left.length; i++)
    {
      for (int j=0; j<left[0].length; j++, k++)
      {
        left[i][j] = TileList.getTile(k);
      }
    }
    
    newFile();
  }

  /* (non-Javadoc)
   * @see komorebi.clyde.engine.Renderable#update()
   */
  @Override
  public void update() {
    int dWheel = Mouse.getDWheel();

    mouseWasDown = mouseIsDown;
    mouseIsDown = Mouse.isButtonDown(0);

    rightWasDown = rightIsDown;
    rightIsDown = Mouse.isButtonDown(1);

    scrollWasDown = scrollIsDown;
    scrollIsDown = Mouse.isButtonDown(2);

    mouseClick = mouseIsDown && !mouseWasDown;
    mouseRelease = !mouseIsDown && mouseWasDown;

    rightClick = rightIsDown && !rightWasDown;

    scrollClick = scrollIsDown && !scrollWasDown;
    scrollRelease = !scrollIsDown && scrollWasDown;

    if (!swapMode){
      if (left() && mouseClick) //Creates a new selection on the palette
      {
        selTiles = new TileList[1][1];

        selX = getMouseTx();
        selY = getMouseTy();

        anchorX = getMouseTx();
        anchorY = getMouseTy();

        selTiles[0][0] = left[31-(getMouseTy()+(16-min))][getMouseTx()];
      } else if (right() && (mouseClick || 
          (KeyHandler.controlDown() && mouseIsDown))) 
      {
        if (save) //Signifies that a change has been made to the file
        {
          Display.setTitle(Display.getTitle() + "*");
          save = false;
        }

        changeMade = true;

        int horiz, vert;

        if (15-getMouseTy()+selTiles[0].length>15) //Avoids array index excep.
        {
          vert = getMouseTy()+1;
        } else {
          vert = selTiles[0].length;
        }

        if (getMouseTx()-9+selTiles.length>7) //Avoids array index excep.
        {
          horiz = 16-getMouseTx()+1;
        } else {
          horiz = selTiles.length;
        }

        for (int i=0; i<vert; i++)
        {
          for (int j=0; j<horiz; j++)
          {
            //Places selection onto tileset
            right[15-getMouseTy()+i][getMouseTx()-9+j] = selTiles[j][i];
          }
        }

      } else if (left() && mouseIsDown && (prevMx!=getMouseTx() ||
          prevMy!=getMouseTy()))
      {
        //New multi-tile selection
        int lowX = Math.min(getMouseTx(), anchorX);
        int hiX = Math.max(getMouseTx(), anchorX);

        int lowY = Math.min(getMouseTy(), anchorY);
        int hiY = Math.max(getMouseTy(), anchorY);


        selX = lowX;
        selY = hiY;
        selTiles = new TileList[hiX-lowX+1][hiY-lowY+1];

        for (int i=0; i<selTiles.length; i++)
        {
          for (int j=0; j<selTiles[0].length; j++)
          {
            selTiles[i][j] = left[left.length-1-(selY-j+(16-min))][selX+i];
          }
        }

      } 

      if (mouseRelease && changeMade)
      {
        updateHistory();
        changeMade = false;
      }

      if (rightClick && right())
      {
        swapMode = true;
        swapTiles = new TileList[1][1];

        swapX = getMouseTx();
        swapY = getMouseTy();

        anchorSwapX = getMouseTx();
        anchorSwapY = getMouseTy();

        swapTiles[0][0] = right[15-getMouseTy()][getMouseTx()-9];

      }

      if (scrollClick)
      {
        delX = getMouseTx();
        delY = getMouseTy();

        anchorDelX = getMouseTx();
        anchorDelY = getMouseTy();

        delSx = 1;
        delSy = 1;

        eraser = true;
      }

      if (eraser && right() && (prevMx!=getMouseTx() || prevMy!=getMouseTy()))
      {
        int lowX = Math.min(getMouseTx(), anchorDelX);
        int hiX = Math.max(getMouseTx(), anchorDelX);

        int lowY = Math.min(getMouseTy(), anchorDelY);
        int hiY = Math.max(getMouseTy(), anchorDelY);


        delX = lowX;
        delY = hiY;
        delSx = hiX-lowX+1;
        delSy = hiY-lowY+1;

      }

      if (scrollIsDown && (KeyHandler.keyDown(Key.SPACE)))
      {
        eraser = false;
      }

      if (scrollRelease && eraser)
      {
        for (int i=0; i<delSx; i++)
        {
          for (int j=0; j<delSy; j++)
          {
            right[15-delY+j][delX-9+i] = TileList.BLANK;
          }
        }
        eraser = false;

        updateHistory();
      }

    } else
    {

      if (!rightIsDown && !swapLift) 
      {
        swapLift = true;
      }

      if (swapLift && KeyHandler.keyClick(Key.SPACE))
      {
        swapLift = false;
        swapMode = false;
      }

      if (!swapLift)
      {
        if (rightIsDown && right() && (prevMx!=getMouseTx() || prevMy!=getMouseTy()))
        {
          //New multi-tile selection
          int lowX = Math.min(getMouseTx(), anchorSwapX);
          int hiX = Math.max(getMouseTx(), anchorSwapX);

          int lowY = Math.min(getMouseTy(), anchorSwapY);
          int hiY = Math.max(getMouseTy(), anchorSwapY);

          swapX = lowX;
          swapY = hiY;
          swapTiles = new TileList[hiX-lowX+1][hiY-lowY+1];

          for (int i=0; i<swapTiles.length; i++)
          {
            for (int j=0; j<swapTiles[0].length; j++)
            {
              swapTiles[i][j] = right[15-swapY+j][swapX-9+i];
            }
          }
        }
      } else 
      {

        if (rightClick && right())
        {
          if (validSwap())
          {
            selTiles = new TileList[1][1];
            selTiles[0][0] = TileList.BLANK;
            selX=0;
            selY=15;

            TileList temp;
            for (int i=0; i<swapTiles.length; i++)
            {
              for (int j=0; j<swapTiles[0].length; j++)
              {
                temp = right[15-getMouseTy()+j][getMouseTx()-9+i];
                right[15-getMouseTy()+j][getMouseTx()-9+i] = right[15-swapY+j][swapX-9+i];
                right[15-swapY+j][swapX-9+i] = temp;
                //Performs the swap
              }
            }

            swapMode = false;
            swapLift = false;

            updateHistory();


          }
        } else if (mouseClick && left())
        {
          swapMode = false;
          swapLift = false;
        } else if (mouseClick && right() && validSwap())
        {
          selTiles = new TileList[1][1];
          selTiles[0][0] = TileList.BLANK;
          selX=0;
          selY=15;

          for (int i=0; i<swapTiles.length; i++)
          {
            for (int j=0; j<swapTiles[0].length; j++)
            {
              right[15-getMouseTy()+j][getMouseTx()-9+i] = right[15-swapY+j][swapX-9+i];
            }
          }

        }

      }
    }

    //Saves the file
    if (saveButton() && mouseClick)
    {
      if (!newSave)
      {
        newSave = newSave(); //New file
      } else
      {
        save(savePath, saveName); //Previously existing file
      }
      
      justSaved = true;
    }

    if (openButton() && mouseClick)
    {
      if (closeFile()) load();
    }
    
    if (newSaveButton() && mouseClick)
    {
      newSave = false;
      justSaved = newSave();
    }
    
    if (newButton() && mouseClick)
    {
      if (closeFile()) newFile();
    }
    
    if (gridButton() && mouseClick)
    {
      grid = !grid;
      MainT.switchGrid();
    }

    if ((KeyHandler.keyDown(Key.DOWN) ||(left() && dWheel<0))&& max<left.length && 
        !mouseIsDown)
    {
      max++;
      min++;
      selY++; //Scroll palette down
    }

    if ((KeyHandler.keyDown(Key.UP) || (left() && dWheel>0))&& min>0 && 
        !mouseIsDown)
    {
      max--;
      min--;
      selY--; //Scroll palette up
    }
    
    if ((KeyHandler.controlDown() && KeyHandler.keyClick(Key.Z)) || 
        (mouseClick && undoButton()) && historyIndex>0)
    {
      undo();
    } else if (((KeyHandler.controlDown() && KeyHandler.keyClick(Key.Y)) ||
        (mouseClick && redoButton()))&& redo!=null)
    {
      redo();
    }

    prevMx = getMouseTx();
    prevMy = getMouseTy();
    
    if (justSaved)
    {
      justSavedCount++;
      
      if (justSavedCount>=100)
      {
        justSavedCount=0;
        justSaved = false;
      }
    }
  }

  /* (non-Javadoc)
   * @see komorebi.clyde.engine.Renderable#render()
   */
  @Override
  public void render() {
    for (int i=min; i<max; i++)
    {
      for (int j=0; j<left[0].length; j++)
      {
        Draw.rect(j*16, HEIGHT-16-(i*16)+(min*16), 16, 16, left[i][j].getX(), 
            left[i][j].getY(), left[i][j].getX()+16, left[i][j].getY()+16, 9);
      }
    }


    for (int i=0; i<right.length; i++)
    {
      for (int j=0; j<right[0].length; j++)
      {
        if (right[i][j]!=null)
        {
          Draw.rect(OFFX*16+j*16+16, HEIGHT-16-(i*16), 16, 16, right[i][j].getX(), 
              right[i][j].getY(), right[i][j].getX()+16, right[i][j].getY()+16,
              9);
        }
      }
    }


    if (!swapMode)
    {
      for (int i=0; i<selTiles.length; i++)
      {
        for (int j=0; j<selTiles[0].length; j++)
        {
          Draw.rect(selX*16+i*16, selY*16-j*16, 16, 16, 220, 3, 221, 4, 6);
        }
      }
    } else
    {
      for (int i=0; i<swapTiles.length; i++)
      {
        for (int j=0; j<swapTiles[0].length; j++)
        {
          Draw.rect(swapX*16+i*16, swapY*16-j*16, 16, 16, 220, 3, 221, 4, 6);
        }
      }

      if (swapLift && right())
      {
        for (int i=0; i<swapTiles.length; i++)
        {
          for (int j=0; j<swapTiles[0].length; j++)
          {
            if (!validSwap())
            {
              Draw.rect(getMouseTx()*16+i*16, getMouseTy()*16-j*16, 16, 16, 
                  220, 0, 221, 1, 6);
            } else
            {
              Draw.rect(getMouseTx()*16+i*16, getMouseTy()*16-j*16, 16, 16, 
                  220, 6, 221, 7, 6);
            }

          }
        }
      }
    }

    if (eraser)
    {
      for (int i=0; i<delSx; i++)
      {
        for (int j=0; j<delSy; j++)
        {
          Draw.rect(delX*16+i*16, delY*16-j*16, 16, 16, 
              220, 9, 221, 10, 6);
        }
      }
    }

    Draw.rect(128, 0, 16, 16, 221, 54, 237, 70, 6);   //Draws save button
    Draw.rect(128, 16, 16, 16, 237, 54, 253, 70, 6);  //Draws the save as button
    Draw.rect(128, 32, 16, 16, 221, 72, 237, 88, 6);   //Draws open button
    Draw.rect(128, 48, 16, 16, 237, 72, 253, 88, 6);  //Draws new button
    

    
    if (redo!=null) Draw.rect(128, 224, 16, 16, 
        221, 36, 237, 52, 6);
    else Draw.rect(128, 224, 16, 16, 237, 36, 253, 52, 6);
    
    if (historyIndex>0) Draw.rect(128, 240, 16, 16, 221, 18, 237, 34, 6);
    else Draw.rect(128, 240, 16, 16, 237, 18, 253, 34, 6);
    
    if (!save) 
    {
      Draw.rect(136, 8, 7, 7, 201, 61, 208, 68, 6);
    } else if (justSaved)
    {
      Draw.rect(134, 8, 9, 7, 201, 69, 210, 76, 6);
    }
    
    if (grid)
    {
      Draw.rect(128, 208, 16, 16, 237, 90, 253, 106, 6);
    } else
    {
      Draw.rect(128, 208, 16, 16, 221, 90, 237, 106, 6);
    }
  }

  public boolean validSwap()
  {
    if (getMouseTx()+swapTiles.length-9>8) return false;
    if (15-getMouseTy()+swapTiles[0].length>16) return false;
    //The rectangle is partially off the screen
    if (getMouseTx()>=swapX && getMouseTx()<swapX+swapTiles.length &&
        getMouseTy()<=swapY && getMouseTy()>swapY-swapTiles[0].length) 
      return false;
    //Top left corner intersects
    if ((getMouseTx()+swapTiles.length-1)>=swapX && 
        (getMouseTx()+swapTiles.length-1)<swapX+swapTiles.length &&
        getMouseTy()<=swapY && getMouseTy()>swapY-swapTiles[0].length)
      return false;
    //Top right corner intersects
    if (getMouseTx()>=swapX && getMouseTx()<swapX+swapTiles.length &&
        (getMouseTy()-swapTiles[0].length+1)<=swapY &&
        (getMouseTy()-swapTiles[0].length+1)>swapY-swapTiles[0].length) 
      return false;
    //Bottom left corner intersects
    if ((getMouseTx()+swapTiles.length-1)>=swapX && 
        (getMouseTx()+swapTiles.length-1)<swapX+swapTiles.length &&
        (getMouseTy()-swapTiles[0].length+1)<=swapY &&
        (getMouseTy()-swapTiles[0].length+1)>swapY-swapTiles[0].length) 
      return false;
    //Bottom right corner intersects
    return true;
  }

  /* (non-Javadoc)
   * @see komorebi.clyde.engine.Playable#getInput()
   */
  @Override
  public void getInput() {
    // TODO Auto-generated method stub

  }

  public boolean left()
  {
    return (Mouse.getX()<128*MainT.scale);
  }

  public boolean right()
  {
    return (Mouse.getX()>144*MainT.scale);
  }

  public boolean saveButton()
  {
    return getMouseTx()==8 && getMouseTy()==0;
  }

  public boolean newSaveButton()
  {
    return getMouseTx()==8 && getMouseTy()==1;
  }
  
  public boolean openButton()
  {
    return getMouseTx()==8 && getMouseTy()==2;
  }
  
  public boolean newButton()
  {
    return getMouseTx()==8 && getMouseTy()==3;
  }
  
  public boolean redoButton()
  {
    return getMouseTx()==8 && getMouseTy()==14;
  }
  
  public boolean undoButton()
  {
    return getMouseTx()==8 && getMouseTy()==15;
  }
  
  public boolean gridButton()
  {
    return getMouseTx()==8 && getMouseTy()==13;
  }

  public int getMouseTx()
  {
    return (Mouse.getX()/MainT.scale) / 16;
  }

  public int getMouseTy()
  {
    return (Mouse.getY()/MainT.scale) / 16;
  }

  public boolean newSave() {

    JFileChooser chooser = new JFileChooser("res/tilesets/"){
      /**
       * I don't know what this does, but it does something...
       */
      private static final long serialVersionUID = 3881189161552826430L;

      @Override
      public void approveSelection(){
        File f = getSelectedFile();
        if(f.exists() && getDialogType() == SAVE_DIALOG){
          int result = JOptionPane.showConfirmDialog(this,
              "The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
          switch(result){
            case JOptionPane.YES_OPTION:
              super.approveSelection();
              return;
            case JOptionPane.NO_OPTION:
              return;
            case JOptionPane.CLOSED_OPTION:
              return;
            case JOptionPane.CANCEL_OPTION:
              cancelSelection();
              return;
            default:
              return;
          }
        }
        super.approveSelection();
      }
    };

    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "TileSet Files", "tset");
    chooser.setFileFilter(filter);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setDialogTitle("Enter the name of the tileset to save");
    int returnee = chooser.showSaveDialog(null);

    KeyHandler.reloadKeyboard();

    if(returnee == JFileChooser.APPROVE_OPTION){

      savePath = chooser.getSelectedFile().getAbsolutePath();
      saveName = chooser.getSelectedFile().getName();

      return save(savePath, saveName);
    }

    return false;
  }

  public boolean save(String path,String name) {
    PrintWriter writer;

    try {
      if(path.substring(path.length()-5).equals(".tset")){
        writer = new PrintWriter(path, "UTF-8");
      }else{
        writer = new PrintWriter(path+".tset", "UTF-8");
      }
      writer.println(right.length);
      writer.println(right[0].length);

      for (TileList[] tile : right) {
        for (TileList t : tile) {
          writer.print(t.getID() + " ");
        }
        writer.println();
      }
    //TODO Debug
      System.out.println("Save complete");
      save = true;
      writer.close();
      if (name.length()>5)
      {
        if(name.substring(name.length()-5).equals(".tset")){
          Display.setTitle("Clyde\'s Tile Editor - " + name);
        }else{
          Display.setTitle("Clyde\'s Tile Editor - " + name + ".tset");
        }
      } else
      {
        Display.setTitle("Clyde\'s Tile Editor - " + name + ".tset");
      }
      


      return true;
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      e.printStackTrace();
      return false;
    }

  }

  public void load(){

    JFileChooser chooser = new JFileChooser("res/tilesets/");
    /*FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Map Files", "map");*/
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "TileSet Files", "tset");
    chooser.setFileFilter(filter);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setDialogTitle("Enter the name of the tile set to load");
    int returnee = chooser.showOpenDialog(null);



    if(returnee == JFileChooser.APPROVE_OPTION){
      /*map = new EditorMap(chooser.getSelectedFile().getAbsolutePath(), 
          chooser.getSelectedFile().getName());
       */
      savePath = chooser.getSelectedFile().getAbsolutePath();
      saveName = chooser.getSelectedFile().getName();

      try {
        BufferedReader reader = new BufferedReader(new FileReader(
            new File(savePath)));
        int rows = Integer.parseInt(reader.readLine());
        int cols = Integer.parseInt(reader.readLine());

        right = new TileList[rows][cols];

        for (int i = 0; i < rows; i++) {
          String[] str = reader.readLine().split(" ");
          int index = 0;
          for (int j = 0; j < cols; j++, index++) {
            if(str[index].equals("")){
              index++;  //pass this token, it's blank
            }
            right[i][j] = TileList.getTile(Integer.parseInt(str[index]));
          }
        }

        Display.setTitle("Clyde\'s Editor - "+saveName);

        save = true;
        newSave = true;
        reader.close();
      } catch (IOException | NumberFormatException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, 
            "The file was not found / was corrupt.");


      }
    }
  }

  public void undo()
  {
    if (historyIndex>0)
    {
      redo = new TileList[16][8];
      for (int i=0; i<right.length; i++)
      {
        for (int j=0; j<right[0].length; j++)
        {
          redo[i][j]=history.get(history.size()-1)[i][j];
        }
      }
      
      history.remove(history.size()-1);
      historyIndex--;
      for (int i=0; i<right.length; i++)
      {
        for (int j=0; j<right[0].length; j++)
        {
          right[i][j]=history.get(historyIndex)[i][j];
        }
      }
    }
  }

  public void redo()
  {
    for (int i=0; i<right.length; i++)
    {
      for (int j=0; j<right[0].length; j++)
      {
        right[i][j]=redo[i][j];
      }
    }
    
    updateHistory();
  }

  public void updateHistory()
  {
    
    redo=null;
    TileList[][] tiles = new TileList[right.length][right[0].length];

    for (int i=0; i<tiles.length; i++)
    {
      for (int j=0; j<tiles[0].length; j++)
      {
        tiles[i][j] = right[i][j];
      }
    }
//Andrew is a nerd
    history.add(tiles);
    historyIndex++;
    
    if (history.size()>20)
    {
      history.remove(0);
      historyIndex--;
    }
   
  }
  
  public boolean closeFile()
  {
    boolean continyu = true; //clever
    if(!save){
      
      int returnee = JOptionPane.showConfirmDialog(null, "Would you like to save?");
      
      switch(returnee){
        case JFileChooser.APPROVE_OPTION:
          if (!newSave) continyu = newSave();
          else
          {
            continyu = save(savePath, saveName);
          }
          break;
        case JFileChooser.CANCEL_OPTION:
          continyu = true;
          break;
        default:
          continyu = false;
          break;
      }
    }
    return continyu;    
  }
  
  public void newFile()
  {
    right = new TileList[16][8];

    for (int i=0; i<right.length; i++)
    {
      for (int j=0; j<right[0].length; j++)
      {
        right[i][j] = TileList.BLANK;
      }
    }

    selX = 0;
    selY = 0;

    selTiles = new TileList[1][1];
    selTiles[0][0]=TileList.BLANK;

    history = new ArrayList<TileList[][]>();
    updateHistory();

    historyIndex = 0;
  }


}
