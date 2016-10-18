package komorebi.projsoul.entities;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.HUD;
import komorebi.projsoul.engine.MagicBar;

public class Sierra extends Player {

public Sierra(float x, float y) {
    
    super(x,y);
    
    character = Characters.SIERRA;
    
    upAni =    new Animation(6, 8, 12);
    downAni =  new Animation(6, 8, 12);
    leftAni =  new Animation(6, 8, 12);
    rightAni = new Animation(6, 8, 12);

    hurtUpAni = new Animation(2,8,14,32,12);
    hurtDownAni = new Animation(2,8,16,32,12);
    hurtRightAni = new Animation(2,8,24,31,12);
    hurtLeftAni = new Animation(2,8,22,28,12);

    downAni.add(631,192,15,35);
    downAni.add(650,194,14,33);
    downAni.add(668,192,16,35);
    downAni.add(688,192,15,35);
    downAni.add(707,194,14,33);
    downAni.add(724,192,16,35);

    upAni.add(633,237,14,33);
    upAni.add(652,238,13,32);
    upAni.add(668,237,16,33);
    upAni.add(687,237,14,33);
    upAni.add(706,238,13,32);
    upAni.add(726,237,16,33);

    rightAni.add(626,279,18,31);
    rightAni.add(648,278,17,32);
    rightAni.add(669,278,18,32);
    rightAni.add(688,280,18,30);
    rightAni.add(709,278,18,32);
    rightAni.add(730,278,19,32);

    leftAni.add(626,279,18,31,0,true);
    leftAni.add(648,278,17,32,0,true);
    leftAni.add(669,278,18,32,0,true);
    leftAni.add(688,280,18,30,0,true);
    leftAni.add(709,278,18,32,0,true);
    leftAni.add(730,278,19,32,0,true);

    hurtUpAni.add(704,561);
    hurtUpAni.add(754,645);

    hurtDownAni.add(701, 681);
    hurtDownAni.add(751, 682);

    hurtRightAni.add(887,442);
    hurtRightAni.add(914,442);

    hurtLeftAni.add(887,442,true);
    hurtLeftAni.add(914,442,true);
    
    magic = new MagicBar(60);
    health = new HUD(45);
    
    attack = 50;
    defense = 45;
    
  }

@Override
public void renderAttack() {
  // TODO Auto-generated method stub
  
}
}
