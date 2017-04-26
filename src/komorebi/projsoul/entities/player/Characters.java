package komorebi.projsoul.entities.player;

import komorebi.projsoul.engine.InitializableAnimation;
import komorebi.projsoul.entities.sprites.SpriteSet;

public enum Characters {
  CASPIAN(
      new SpriteSet(
          new InitializableAnimation(6, 8, 11)
          {
            public void initialize() {
              add(3,247,21,32,0,true);
              add(30,246,14,33,0,true);
              add(52,245,14,34,0,true);
              add(72,247,22,32,0,true);
              add(99,246,14,33,0,true);
              add(120,245,15,34,0,true);

              setPausedFrame(99,246,14,33,0,true);

            }

          }, 
          new InitializableAnimation(6, 8, 11)
          {

            public void initialize() {
              add(3,247,21,32);
              add(30,246,14,33);
              add(52,245,14,34);
              add(72,247,22,32);
              add(99,246,14,33);
              add(120,245,15,34);

              setPausedFrame(99,246,14,33);

            }

          },
          new InitializableAnimation(6, 8, 11)
          {

            public void initialize() {
              add(8,204,16,35);
              add(28,207,18,32);
              add(50,206,18,33);
              add(71,204,16,35);
              add(91,207,18,32);
              add(113,206,18,33);
            }

          }, 
          new InitializableAnimation(6, 8, 11)
          {

            public void initialize() {
              add(8,162,16,34);
              add(28,164,17,32);
              add(49,161,18,35);
              add(71,162,16,34);
              add(91,164,17,32);
              add(112,161,18,35);
            }


          }
          ),
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
      new SpriteSet(
          new InitializableAnimation(6, 8, 12)
          {
            public void initialize() {
              add(8,91,16,32);
              add(30,91,16,32);
              add(52,92,22,31);
              add(78,92,20,32);
              add(102,92,16,32);
              add(123,93,18,31);

            }

          }, 
          new InitializableAnimation(6, 8, 12)
          {

            public void initialize() {
              add(8,91,16,32,0,true);
              add(30,91,16,32,0,true);
              add(52,92,22,31,0,true);
              add(78,92,20,32,0,true);
              add(102,92,16,32,0,true);
              add(123,93,18,31,0,true);

            }

          },
          new InitializableAnimation(6, 8, 12)
          {

            public void initialize() {
              add(6,131,21,32);
              add(30,131,24,32);
              add(58,132,23,31);
              add(84,131,21,32);
              add(109,131,24,32);
              add(137,132,23,31);
            }

          }, 
          new InitializableAnimation(6, 8, 12)
          {

            public void initialize() {
              add(6,52,23,31);
              add(31,52,22,31);
              add(57,52,23,32);
              add(83,53,23,31);
              add(110,52,23,32);
              add(137,52,22,31);
            }


          }
          ),
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
      new SpriteSet(
          new InitializableAnimation(6, 8, 12)
          {
            public void initialize() {
              add(626,279,18,31,0,true);
              add(648,278,17,32,0,true);
              add(669,278,18,32,0,true);
              add(688,280,18,30,0,true);
              add(709,278,18,32,0,true);
              add(730,278,19,32,0,true);
            }

          }, 
          new InitializableAnimation(6, 8, 12)
          {

            public void initialize() {
              add(626,279,18,31);
              add(648,278,17,32);
              add(669,278,18,32);
              add(688,280,18,30);
              add(709,278,18,32);
              add(730,278,19,32);

            }

          },
          new InitializableAnimation(6, 8, 12)
          {

            public void initialize() {
              add(633,237,14,33);
              add(652,238,13,32);
              add(668,237,16,33);
              add(687,237,14,33);
              add(706,238,13,32);
              add(726,237,16,33);
            }

          }, 
          new InitializableAnimation(6, 8, 12)
          {

            public void initialize() {
              add(631,192,15,35);
              add(650,194,14,33);
              add(668,192,16,35);
              add(688,192,15,35);
              add(707,194,14,33);
              add(724,192,16,35);
            }


          }
          ),
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
      new SpriteSet(
          new InitializableAnimation(12, 8, 12)
          {
            public void initialize() {
              add(150,618,22,32);
              add(175,620,25,30);
              add(203,621,24,29);
              add(175,620,25,30);
              add(150,618,22,32);
              add(125,619,22,31);
              add(99,619,22,31);
              add(66,620,28,30);
              add(35,621,28,29);
              add(66,620,28,30);
              add(99,619,22,31);
              add(125,619,22,31);

            }

          }, 
          new InitializableAnimation(12, 8, 12)
          {

            public void initialize() {
              add(150,618,22,32,0,true);
              add(175,620,25,30,0,true);
              add(203,621,24,29,0,true);
              add(175,620,25,30,0,true);
              add(150,618,22,32,0,true);
              add(125,619,22,31,0,true);
              add(99,619,22,31,0,true);
              add(66,620,28,30,0,true);
              add(35,621,28,29,0,true);
              add(66,620,28,30,0,true);
              add(99,619,22,31,0,true);
              add(125,619,22,31,0,true);

            }

          },
          new InitializableAnimation(10, 8, 12)
          {

            public void initialize() {
              add(204,514,24,32);
              add(232,515,24,31);
              add(260,515,24,31);
              add(288,516,23,30);
              add(260,515,24,31);
              add(232,515,24,31);
              add(204,514,24,32);
              add(177,516,23,30);
              add(149,516,24,30);
              add(177,516,23,30);
            }

          }, 
          new InitializableAnimation(8, 8, 12)
          {

            public void initialize() {
              add(162,761,24,30);
              add(191,761,24,30);
              add(218,759,24,32);
              add(246,760,24,31);
              add(274,761,24,30);
              add(246,760,24,31);
              add(218,759,24,32);
              add(191,761,24,30);
            }


          }
          ),
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
      if (c.ordinal() == i) return c;
    }

    return null;
  }
}
