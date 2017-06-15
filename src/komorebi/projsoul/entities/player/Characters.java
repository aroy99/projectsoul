package komorebi.projsoul.entities.player;

import komorebi.projsoul.engine.InitializableAnimation;
import komorebi.projsoul.entities.NPCType;
import komorebi.projsoul.entities.sprites.SpriteSet;

/**
 * Holds all(read: most) of the character sprites
 * 
 * @author Andrew Faulkenberry
 */
public enum Characters {

  CASPIAN(
      NPCType.CHRONO.getNewSpriteSet(),
    new SpriteSet(
      new InitializableAnimation(2,8,14,33,11)
      {
        public void initialize() {
          add(30, 246, true);
          add(141, 246, true);
        }

      }, 
      new InitializableAnimation(2,8,14,33,11)
      {
        public void initialize() {
          add(30, 246);
          add(141, 246);
        }

      },
      new InitializableAnimation(2,8,16,35,11)
      {

        public void initialize() {
          add(8,204);
          add(141, 205);
        }

      }, 
      new InitializableAnimation(2,8,16,34,11)
      {

        public void initialize() {
          add(8, 162);
          add(141, 163);
        }

      }

      )
  ),
  FLANNERY(
      NPCType.MAGUS.getNewSpriteSet(),
      new SpriteSet(
      new InitializableAnimation(2,8,24,31,12)
      {
        public void initialize() {
          add(346,225);
          add(347,261);
        }

      }, 
      new InitializableAnimation(2,8,24,31,12)
      {
        public void initialize() {
          add(346,225,true);
          add(347,261,true);
        }

      },
      new InitializableAnimation(2,8,26,29,12)
      {

        public void initialize() {
          add(374,227);
          add(376, 263);
        }

      }, 
      new InitializableAnimation(2,8,28,31,12)
      {

        public void initialize() {
          add(314, 224);
          add(314, 261);
        }

      }

      )
  ),
  SIERRA(
      NPCType.MARLE.getNewSpriteSet(),
      new SpriteSet(
      new InitializableAnimation(2,8,22,28,12)
      {
        public void initialize() {
          add(887,442,true);
          add(914,442,true);
        }

      }, 
      new InitializableAnimation(2,8,24,31,12)
      {
        public void initialize() {
          add(887,442);
          add(914,442);
        }

      },
      new InitializableAnimation(2,8,14,32,12)
      {

        public void initialize() {
          add(704,561);
          add(754,645);
        }

      }, 
      new InitializableAnimation(2,8,16,32,12)
      {

        public void initialize() {
          add(701, 681);
          add(751, 682);
        }

      }

      )
  ),
  BRUNO(
      NPCType.ROBO.getNewSpriteSet(),
      new SpriteSet(
      new InitializableAnimation(2,8,32,32,12)
      {
        public void initialize() {
          add(195,654);
          add(426,763);
        }

      }, 
      new InitializableAnimation(2,8,32,32,12)
      {
        public void initialize() {

          add(195,654,true);
          add(426,763,true);
        }

      },
      new InitializableAnimation(2,8,24,31,12)
      {

        public void initialize() {
          add(231,551);
          add(362,761);
        }

      }, 
      new InitializableAnimation(2,8,24,29,12)
      {

        public void initialize() {
          add(154, 727);
          add(396, 764);
        }

      }

      )
      );

  private SpriteSet walkingSprites;
  private SpriteSet hurtSprites;

  private Characters(SpriteSet walk, SpriteSet hurt)
  {
    this.walkingSprites = walk;
    this.hurtSprites = hurt;
  }

  public SpriteSet getNewWalkingSprites()
  {
    return walkingSprites.duplicate();
  }

  public SpriteSet getNewHurtSprites()
  {
    return hurtSprites.duplicate();
  }

  public String getNameFormatted()
  {
    String lower = this.toString().toLowerCase();
    return lower.substring(0, 1).toUpperCase() + lower.substring(1);
  }


  public static Characters getCharacter(int i)
  {
    for (Characters c: Characters.values())
    {
      if (c.ordinal() == i) {
        return c;
      }
    }

    return null;
  }
}
