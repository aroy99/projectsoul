/**
 * TileSetEditor.java		Jul 28, 2016, 2:35:58 PM
 */
package komorebi.projsoul.tileseteditor;

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

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Playable;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class TileSetEditor implements Playable {

  private int max, min;


  //replace Tile[][] with int[][] of tile IDs
  private int[][] left;
  private int[][] right;

  ArrayList<int[][]> history;
  int[][] redo;
  private int historyIndex;
  private boolean changeMade;

  private boolean justSaved;
  private int justSavedCount;

  private static final int HEIGHT = 256;
  private static final int OFFX = 8;

  private int selX, selY, selSx, selSy;
  private int clipX, clipY, clipSx, clipSy;
  private int swapX, swapY, swapSx, swapSy;
  private int delX, delY, delSx, delSy;

  private int selTiles[][];
  private int clipTiles[][];

  private boolean mouseIsDown, mouseWasDown, mouseClick, mouseRelease;
  private boolean rightIsDown, rightWasDown, rightClick;
  private boolean scrollIsDown, scrollWasDown, scrollClick, scrollRelease;

  private boolean selMode, swapMode;
  private boolean eraser;
  private boolean switchSel;

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

    int texNum = 0;
    boolean hasFiles = true;

    while (hasFiles)
    {
      try 
      {
        Draw.addSpreadsheetTexture(texNum);
        texNum++;
      } catch (IOException e)
      {
        hasFiles = false;
      }
    }

    left = new int[32*texNum][8];

    for (int i=0, k=0; i<left.length; i++)
    {
      for (int j=0; j<left[0].length; j++, k++)
      {
        left[i][j] = k;
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
    
    if (!selMode){
      if (left() && mouseClick) //Creates a new selection on the palette
      {
        selTiles = new int[1][1];

        selX = getMouseTx();
        selY = getMouseTy();
        selSx = 1;
        selSy = 1;

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
        selSx = hiX-lowX+1;
        selSy = hiY-lowY+1;
        selTiles = new int[selSx][selSy];

        for (int i=0; i<selSx; i++)
        {
          for (int j=0; j<selSy; j++)
          {
            selTiles[i][j] = left[15-selY+min+j][selX+i];
          }
        }


      } 

      if (mouseRelease && changeMade)
      {
        updateHistory();
        changeMade = false;
      }

      if ((rightClick) && right())
      {
        clearRightSelection();
        selMode = true;

        swapX = getMouseTx();
        swapY = getMouseTy();

        anchorSwapX = getMouseTx();
        anchorSwapY = getMouseTy();
      }

    } else
    {
      if (left() && mouseClick)
      {
        selMode = false;
        clearLeftSelection();
      }

      if (KeyHandler.keyClick(Key.LEFT) && swapX > 9)
      {
        copy();

        for (int j=0; j<clipTiles[0].length; j++)
        {
          for (int i=0; i<clipTiles.length; i++)
          {
            right[15-swapY+j][swapX-10+i] = right[15-swapY+j][swapX-9+i];
          }

          right[15-swapY+j][swapX-10+clipTiles.length] = Draw.BLANK_TILE;
        }

        swapX--;
      } else if (KeyHandler.keyClick(Key.RIGHT) && swapX+swapSx < 17)
      {
        copy();

        for (int j=0; j<clipTiles[0].length; j++)
        {
          for (int i=clipTiles.length-1; i>=0; i--)
          {
            right[15-swapY+j][swapX-8+i] = right[15-swapY+j][swapX-9+i];
          }

          right[15-swapY+j][swapX-9] = Draw.BLANK_TILE;
        }

        swapX++;
      } else if (KeyHandler.keyClick(Key.UP) && swapY < 15)
      {
        copy();

        for (int i=0; i<clipTiles.length; i++)
        {
          for (int j=0; j<clipTiles[0].length; j++)
          {
            right[14-swapY+j][swapX-9+i] = right[15-swapY+j][swapX-9+i];
          }

          right[14-swapY+clipTiles[0].length][swapX-9+i] = Draw.BLANK_TILE;
        }

        swapY++;
      } else if (KeyHandler.keyClick(Key.DOWN) && swapY-swapSy >= 0)
      {
        copy();

        for (int i=0; i<clipTiles.length; i++)
        {
          for (int j=clipTiles[0].length-1; j>=0; j--)
          {
            right[16-swapY+j][swapX-9+i] = right[15-swapY+j][swapX-9+i];
          }

          right[15-swapY][swapX-9+i] = Draw.BLANK_TILE;
        }

        swapY--;
      }

      if (KeyHandler.controlDown()) 
      {
        if (KeyHandler.keyClick(Key.Q))
        {
          copy();
          swapMode = true;
        } else if (KeyHandler.keyClick(Key.C))
        {
          copy();
        } else if (KeyHandler.keyClick(Key.V))
        {
          paste();
        }
      }

      if (switchSel)
      {
        switchSel = false;
        swapX = getMouseTx();
        swapY = getMouseTy();
        swapSx = 1;
        swapSy = 1;

        anchorSwapX = getMouseTx();
        anchorSwapY = getMouseTy();
      }

      if (!swapMode && (KeyHandler.keyClick(Key.SPACE) || mouseClick))
      {
        clearRightSelection();
      } 

      if (rightClick)
      {
        switchSel = true;
        clearRightSelection();
      }

      if (!swapMode)
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
          swapSx = hiX-lowX+1;
          swapSy = hiY-lowY+1;

        }
      } else 
      {        
        if (mouseClick && right())
        {

          if (validSwap())
          {

            
            selTiles = new int[1][1];
            selTiles[0][0] = Draw.BLANK_TILE;
            selX=0;
            selY=15;
             

            int temp;
            for (int i=0; i < clipTiles.length; i++)
            {
              for (int j=0; j < clipTiles[0].length; j++)
              {
                temp = right[15-getMouseTy()+j][getMouseTx()-9+i];
                right[15-getMouseTy()+j][getMouseTx()-9+i] = right[15-swapY+j][swapX-9+i];
                right[15-swapY+j][swapX-9+i] = temp;
                //Performs the swap
              }
            }

            selMode = false;
            swapMode = false;

            updateHistory();


          }
        } else if (mouseClick && left())
        {
          clearLeftSelection();
        } else if (mouseClick && right() && validSwap())
        {
          selTiles = new int[1][1];
          selTiles[0][0] = Draw.BLANK_TILE;
          selX=0;
          selY=15;

        }

      }
    }

    if (scrollClick)
    {      
      delX = getMouseTx();
      delY = getMouseTy();

      anchorDelX = getMouseTx();
      anchorDelY = getMouseTy();

      delSx = 1;
      delSy = 1;
      
      selX = selY = selSx = selSy = swapX = swapY = swapSx = swapSy = 0;

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
          right[15-delY+j][delX-9+i] = Draw.BLANK_TILE;
        }
      }
      eraser = false;

      updateHistory();
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

    if (((KeyHandler.keyDown(Key.DOWN) && !selMode) ||(left() && dWheel<0))&& max<left.length && 
        !mouseIsDown)
    {
      max++;
      min++;
      selY++; //Scroll palette down
    }

    if (((KeyHandler.keyDown(Key.UP) && !selMode) || (left() && dWheel>0))&& min>0 && 
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
        Draw.tile(j*16, HEIGHT-16-(i*16)+(min*16), Draw.getTexX(left[i][j]), 
            Draw.getTexY(left[i][j]), 
            Draw.getTexture(left[i][j]));
      }
    }



    for (int i=0; i<right.length; i++)
    {
      for (int j=0; j<right[0].length; j++)
      {
        Draw.tile(OFFX*16+j*16+16, HEIGHT-16-(i*16), Draw.getTexX(right[i][j]), 
            Draw.getTexY(right[i][j]),
            Draw.getTexture(right[i][j]));

      }
    }


    if (!selMode)
    {
      for (int i=0; i<selSx; i++)
      {
        for (int j=0; j<selSy; j++)
        {
          Draw.rect(selX*16+i*16, selY*16-j*16, 16, 16, 220, 3, 221, 4, 6);
        }
      }
    } else
    {
      for (int i=0; i<swapSx; i++)
      {
        for (int j=0; j<swapSy; j++)
        {
          Draw.rect(swapX*16+i*16, swapY*16-j*16, 16, 16, 220, 3, 221, 4, 6);
        }
      }

      if (swapMode && right())
      {
        for (int i=0; i<clipTiles.length; i++)
        {
          for (int j=0; j<clipTiles[0].length; j++)
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
    if (getMouseTx()+clipTiles.length-9>8) return false;
    if (15-getMouseTy()+clipTiles[0].length>16) return false;
    //The rectangle is partially off the screen
    if (getMouseTx()>=swapX && getMouseTx()<swapX+clipTiles.length &&
        getMouseTy()<=swapY && getMouseTy()>swapY-clipTiles[0].length) 
      return false;
    //Top left corner intersects
    if ((getMouseTx()+clipTiles.length-1)>=swapX && 
        (getMouseTx()+clipTiles.length-1)<swapX+clipTiles.length &&
        getMouseTy()<=swapY && getMouseTy()>swapY-clipTiles[0].length)
      return false;
    //Top right corner intersects
    if (getMouseTx()>=swapX && getMouseTx()<swapX+clipTiles.length &&
        (getMouseTy()-clipTiles[0].length+1)<=swapY &&
        (getMouseTy()-clipTiles[0].length+1)>swapY-clipTiles[0].length) 
      return false;
    //Bottom left corner intersects
    if ((getMouseTx()+clipTiles.length-1) >= swapX && 
        (getMouseTx()+clipTiles.length-1) <  swapX+clipTiles.length &&
        (getMouseTy()-clipTiles[0].length+1) <= swapY &&
        (getMouseTy()-clipTiles[0].length+1) >  swapY-clipTiles[0].length){
      return false;
    }
    //Bottom right corner intersects
    return true;
  }

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

      for (int[] tile : right) {
        for (int i : tile) {
          writer.print(i + " ");
        }
        writer.println();
      }
    //DEBUG Save message
      save = true;
      writer.close();
      if (name.length()>5)
      {
        if(name.substring(name.length()-5).equals(".tset")){
          Display.setTitle("Project Soul Tile Editor - " + name);
        }else{
          Display.setTitle("Project Soul Tile Editor - " + name + ".tset");
        }
      } else
      {
        Display.setTitle("Project Soul Tile Editor - " + name + ".tset");
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

        right = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
          String[] str = reader.readLine().split(" ");
          int index = 0;
          for (int j = 0; j < cols; j++, index++) {
            if(str[index].equals("")){
              index++;  //pass this token, it's blank
            }
            right[i][j] = Integer.parseInt(str[index]);
          }
        }

        Display.setTitle("Project Soul Editor - "+saveName);

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
      redo = new int[16][8];
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
    int[][] tiles = new int[right.length][right[0].length];

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
    right = new int[16][8];

    for (int i=0; i<right.length; i++)
    {
      for (int j=0; j<right[0].length; j++)
      {
        right[i][j] = Draw.BLANK_TILE;
      }
    }

    selX = 0;
    selY = 0;

    selTiles = new int[1][1];
    selTiles[0][0]=Draw.BLANK_TILE;

    history = new ArrayList<int[][]>();
    updateHistory();

    historyIndex = 0;
  }

  private void copy()
  {
    clipX = swapX;
    clipY = swapY;
    clipSx = swapSx;
    clipSy = swapSy;

    clipTiles = new int[clipSx][clipSy];

    for (int i=0; i<clipTiles.length; i++)
    {
      for (int j=0; j<clipTiles[0].length; j++)
      {
        clipTiles[i][j] = right[15-clipY+j][clipX-9+i];
      }
    }
  }

  private void paste()
  {       
    for (int i=0; i<Math.min(clipTiles.length, 8-(swapX-9)); i++)
    {
      for (int j=0; j<Math.min(clipTiles[0].length, swapY+1); j++)
      {
        right[15-swapY+j][swapX-9+i] = clipTiles[i][j];
      }
    }
  }

  private void clearLeftSelection()
  {
    selX = selY = selSx = selSy = 0;

  }

  private void clearRightSelection()
  {
    swapMode = false;

    if (!switchSel)
    {
      selMode = false;
    }

    swapX = swapY = swapSx = swapSy = 0;
  }


}
