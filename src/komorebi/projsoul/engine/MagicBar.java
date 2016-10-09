package komorebi.projsoul.engine;

public class MagicBar implements Renderable {

  int magic;
  int maxMagic;
  double proportion;
  
  static final int BAR_WIDTH = 199;
  static final int BAR_LEFT = 29;
  
  public MagicBar(int magic)
  {
    this.magic = magic;
    this.maxMagic = magic;
  }
  
  public void update()
  {
    proportion = (double) this.magic/maxMagic;
  }
  
  public void render()
  {
    Draw.rect(BAR_LEFT, 4, (int) (proportion*BAR_WIDTH), 16, 8, 302, 9, 303, 11);
    
    Draw.rect(26, 2, 204, 21, 0, 280, 204, 301, 11);
  }
  
  public void changeMagicBy(int dMagic)
  {
    magic += dMagic;
    System.out.println(magic);
    
    if (magic<0) magic = 0;
  }
  
  public boolean hasEnoughMagic(int magicNeeded)
  {
    return magic>=magicNeeded;
  }
}
