package komorebi.projsoul.entities;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.HUD;
import komorebi.projsoul.engine.MagicBar;

public class Bruno extends Player {
  public Bruno(float x, float y) {

    super(x,y);

    character = Characters.BRUNO;

    upAni =    new Animation(10, 8, 12);
    downAni =  new Animation(8, 8, 12);
    leftAni =  new Animation(12, 8, 12);
    rightAni = new Animation(12, 8, 12);

    hurtUpAni = new Animation(2,8,24,31,12);
    hurtDownAni = new Animation(2,8,24,29,12);
    hurtRightAni = new Animation(2,8,32,32,12);
    hurtLeftAni = new Animation(2,8,32,32,12);

    downAni.add(162,761,24,30);
    downAni.add(191,761,24,30);
    downAni.add(218,759,24,32);
    downAni.add(246,760,24,31);
    downAni.add(274,761,24,30);
    downAni.add(246,760,24,31);
    downAni.add(218,759,24,32);
    downAni.add(191,761,24,30);
    
    upAni.add(204,514,24,32);
    upAni.add(232,515,24,31);
    upAni.add(260,515,24,31);
    upAni.add(288,516,23,30);
    upAni.add(260,515,24,31);
    upAni.add(232,515,24,31);
    upAni.add(204,514,24,32);
    upAni.add(177,516,23,30);
    upAni.add(149,516,24,30);
    upAni.add(177,516,23,30);

    rightAni.add(150,618,22,32,0,true);
    rightAni.add(175,620,25,30,0,true);
    rightAni.add(203,621,24,29,0,true);
    rightAni.add(175,620,25,30,0,true);
    rightAni.add(150,618,22,32,0,true);
    rightAni.add(125,619,22,31,0,true);
    rightAni.add(99,619,22,31,0,true);
    rightAni.add(66,620,28,30,0,true);
    rightAni.add(35,621,28,29,0,true);
    rightAni.add(66,620,28,30,0,true);
    rightAni.add(99,619,22,31,0,true);
    rightAni.add(125,619,22,31,0,true);

    leftAni.add(150,618,22,32);
    leftAni.add(175,620,25,30);
    leftAni.add(203,621,24,29);
    leftAni.add(175,620,25,30);
    leftAni.add(150,618,22,32);
    leftAni.add(125,619,22,31);
    leftAni.add(99,619,22,31);
    leftAni.add(66,620,28,30);
    leftAni.add(35,621,28,29);
    leftAni.add(66,620,28,30);
    leftAni.add(99,619,22,31);
    leftAni.add(125,619,22,31);

    hurtUpAni.add(231,551);
    hurtUpAni.add(362,761);

    hurtDownAni.add(154, 727);
    hurtDownAni.add(396, 764);

    hurtRightAni.add(195,654,true);
    hurtRightAni.add(426,763,true);

    hurtLeftAni.add(195,654);
    hurtLeftAni.add(426,763);
    
    magic = new MagicBar(40);
    health = new HUD(55);
    
    attack = 45;
    defense = 60;

  }

  @Override
  public void renderAttack() {
    // TODO Auto-generated method stub
    
  }
}
