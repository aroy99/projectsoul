/**
 * NPCType.java  Jun 10, 2016, 9:33:03 PM
 */
package komorebi.projsoul.entities;

import komorebi.projsoul.engine.InitializableAnimation;
import komorebi.projsoul.entities.sprites.NPCLoader;
import komorebi.projsoul.entities.sprites.SpriteSet;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * 
 * @author Andrew Faulkenberry
 */
public enum NPCType {
  POKEMON(
      new SpriteSet(
      //Left
      new InitializableAnimation(3,Person.ANI_SPEED,16,24,3)
      {
        public void initialize() {
          add(51, 0);
          add(67, 0);
          add(83, 0);
        } 

      },
      //Right
      new InitializableAnimation(3,Person.ANI_SPEED,16,24,3)
      {
        public void initialize() {
          add(51, 0, true);
          add(67, 0, true);
          add(83, 0, true);
        } 
      },
      //Up
      new InitializableAnimation(3,Person.ANI_SPEED,16,24,3)
      {
        public void initialize() {
          add(100, 0);
          add(117, 0);
          add(134, 0);
        } 
      },
      //Down
      new InitializableAnimation(3,Person.ANI_SPEED,16,24,3)
      {
        public void initialize() {
          add(1, 0);
          add(18, 0);
          add(35, 0);
        } 
      }

      )),
  NESS(
      new SpriteSet(
      //Left
      new InitializableAnimation(2,Person.ANI_SPEED,16,24,4)
      {
        public void initialize() {
          add(51, 0);
          add(68, 0);
        } 
      },
      //Right
      new InitializableAnimation(2,Person.ANI_SPEED,16,24,4){
        public void initialize() {
          add(51, 0, true);
          add(68, 0, true);
        }
      },
      //Up
      new InitializableAnimation(2,Person.ANI_SPEED,16,24,4)
      {

        public void initialize() {
          add(34, 0);
          add(34, 0, true);
        }

      },
      //Down
      new InitializableAnimation(2,Person.ANI_SPEED,16,24,4)
      {
        public void initialize() {
          add(0, 0);
          add(17, 0);
        }
      }
      )), 
  CHRONO(
      new SpriteSet(
      new InitializableAnimation(6, Person.ANI_SPEED, 11)
      {
        //Left
        public void initialize() {
          add(3,247,21,32,0,true);
          add(30,246,14,33,0,true);
          add(52,245,14,34,0,true);
          add(72,247,22,32,0,true);
          add(99,246,14,33,0,true);
          add(120,245,15,34,0,true);

          setPausedFrame(166,245,14,34,0,true);
        }

      }, 
      new InitializableAnimation(6, Person.ANI_SPEED, 11)
      {
        //Right
        public void initialize() {
          add(3,247,21,32, -4, 0);
          add(30,246,14,33);
          add(52,245,14,34);
          add(72,247,22,32, -5, 0);
          add(99,246,14,33);
          add(120,245,15,34);

          setPausedFrame(166,245,14,34);

        }

      },
      new InitializableAnimation(6, Person.ANI_SPEED, 11)
      {
        //Up
        public void initialize() {
          add(113,206,18,33, -3, 0);
          add(8,204,16,35);
          add(28,207,18,32, -1, 0);
          add(50,206,18,33, -2, 0);
          add(71,204,16,35, -2, 0);
          add(91,207,18,32, -3, 0);

          setPausedFrame(166, 207, 16, 33);
        }

      }, 
      new InitializableAnimation(6, Person.ANI_SPEED, 11)
      {
        //Down
        public void initialize() {
          add(71,162,16,34);
          add(91,164,17,32, -1, 0);
          add(112,161,18,35, -2, 0);
          add(8,162,16,34);
          add(28,164,17,32);
          add(49,161,18,35);

          setPausedFrame(166, 162, 16, 35);
        }


      }
      )
      ),
  MAGUS(
      new SpriteSet(
      new InitializableAnimation(6, Person.ANI_SPEED, 12)
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
      new InitializableAnimation(6, Person.ANI_SPEED, 12)
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
      new InitializableAnimation(6, Person.ANI_SPEED, 12)
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
      new InitializableAnimation(6, Person.ANI_SPEED, 12)
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
      )
    ),
  MARLE(
      new SpriteSet(
      new InitializableAnimation(6, Person.ANI_SPEED, 12)
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
      new InitializableAnimation(6, Person.ANI_SPEED, 12)
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
      new InitializableAnimation(6, Person.ANI_SPEED, 12)
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
      new InitializableAnimation(6, Person.ANI_SPEED, 12)
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
      )
  ),
  ROBO(
      new SpriteSet(
      new InitializableAnimation(12, Person.ANI_SPEED, 12)
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
      new InitializableAnimation(12, Person.ANI_SPEED, 12)
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
      new InitializableAnimation(10, Person.ANI_SPEED, 12)
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
      new InitializableAnimation(8, Person.ANI_SPEED, 12)
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
      )
  ),
  GUARD(
      new SpriteSet(
      //Left
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("guard_13"), 2, 0);
          add(NPCLoader.SPRITES.get("guard_14"), 1, 0);
          add(NPCLoader.SPRITES.get("guard_15"), 2, 0);
          add(NPCLoader.SPRITES.get("guard_16"), 2, 0);

          setPausedFrame(NPCLoader.SPRITES.get("guard_01"), 2, 0);
        } 
      },
      //Right
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("guard_09"), 3, 0);
          add(NPCLoader.SPRITES.get("guard_10"), 4, 0);
          add(NPCLoader.SPRITES.get("guard_11"), 2, 0);
          add(NPCLoader.SPRITES.get("guard_12"), 4, 0);

          setPausedFrame(NPCLoader.SPRITES.get("guard_03"), 3, 0);
        } 
      },
      //Up
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("guard_17"), 0, 0);
          add(NPCLoader.SPRITES.get("guard_18"), -1, 0);
          add(NPCLoader.SPRITES.get("guard_19"), -1, 0);
          add(NPCLoader.SPRITES.get("guard_20"), -1, 0);

          setPausedFrame(NPCLoader.SPRITES.get("guard_04"), -1, 0);
        } 
      },
      //Down
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("guard_05"), -1, 0);
          add(NPCLoader.SPRITES.get("guard_06"), 2, 0);
          add(NPCLoader.SPRITES.get("guard_07"), 2, 0);
          add(NPCLoader.SPRITES.get("guard_08"), 2, 0);

          setPausedFrame(NPCLoader.SPRITES.get("guard_02"), 1, 0);
        } 
      }
      )
      ),
  MAYOR(
      new SpriteSet(
          
      //Left
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("mayor_12"), 3, 0);
          add(NPCLoader.SPRITES.get("mayor_13"), 1, 0);
          add(NPCLoader.SPRITES.get("mayor_14"), 3, 0);
          add(NPCLoader.SPRITES.get("mayor_15"), 1, 0);

          setPausedFrame(NPCLoader.SPRITES.get("mayor_01"), 3, 0);
        } 
      },
      //Right
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("mayor_08"), 2, 0);
          add(NPCLoader.SPRITES.get("mayor_09"), 3, 0);
          add(NPCLoader.SPRITES.get("mayor_10"), 2, 0);
          add(NPCLoader.SPRITES.get("mayor_11"), 3, 0);

          setPausedFrame(NPCLoader.SPRITES.get("mayor_03"), 3, 0);
        } 
      },
      //Up
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("mayor_16"), 1, 0);
          add(NPCLoader.SPRITES.get("mayor_17"), -1, 0);
          add(NPCLoader.SPRITES.get("mayor_18"), 0, 0);
          add(NPCLoader.SPRITES.get("mayor_17"), -1, 0);

          setPausedFrame(NPCLoader.SPRITES.get("mayor_04"), 1, 0);
        } 
      },
      //Down
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("mayor_05"), 1, 0);
          add(NPCLoader.SPRITES.get("mayor_06"), 0, 0);
          add(NPCLoader.SPRITES.get("mayor_07"), -1, 0);
          add(NPCLoader.SPRITES.get("mayor_06"), 0, 0);

          setPausedFrame(NPCLoader.SPRITES.get("mayor_02"), 1, 0);
        } 
      }
      )
      ),
  CELIA(
      new SpriteSet(

      //Left
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("celia_12"), 2, 0);
          add(NPCLoader.SPRITES.get("celia_13"), 2, 0);
          add(NPCLoader.SPRITES.get("celia_14"), 2, 0);
          add(NPCLoader.SPRITES.get("celia_15"), 2, 0);

          setPausedFrame(NPCLoader.SPRITES.get("celia_01"), 2, 0);
        } 
      },
      //Right
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("celia_08"), 2, 0);
          add(NPCLoader.SPRITES.get("celia_09"), 2, 0);
          add(NPCLoader.SPRITES.get("celia_10"), 2, 0);
          add(NPCLoader.SPRITES.get("celia_11"), 2, 0);

          setPausedFrame(NPCLoader.SPRITES.get("celia_03"), 2, 0);
        } 
      },
      //Up
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("celia_16"), 1, 0);
          add(NPCLoader.SPRITES.get("celia_17"), 1, 0);
          add(NPCLoader.SPRITES.get("celia_18"), 1, 0);
          add(NPCLoader.SPRITES.get("celia_19"), 1, 0);

          setPausedFrame(NPCLoader.SPRITES.get("celia_04"), 1, 0);
        } 
      },
      //Down
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("celia_05"), 0, 0);
          add(NPCLoader.SPRITES.get("celia_06"), 0, 1);
          add(NPCLoader.SPRITES.get("celia_07"), 0, 0);
          add(NPCLoader.SPRITES.get("celia_06"), 0, 1);

          setPausedFrame(NPCLoader.SPRITES.get("celia_02"), 0, 0);
        } 
      }
      )
      ),
  KID_BOY(
      new SpriteSet(

      //Left
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("kid_boy_12"), 0, 0);
          add(NPCLoader.SPRITES.get("kid_boy_13"), -1, 0);
          add(NPCLoader.SPRITES.get("kid_boy_14"), 0, 0);
          add(NPCLoader.SPRITES.get("kid_boy_15"), -1, 0);

          setPausedFrame(NPCLoader.SPRITES.get("kid_boy_01"), 0, 0);
        } 
      },
      //Right
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("kid_boy_08"), 1, 0);
          add(NPCLoader.SPRITES.get("kid_boy_09"), 4, 0);
          add(NPCLoader.SPRITES.get("kid_boy_10"), 1, 0);
          add(NPCLoader.SPRITES.get("kid_boy_11"), 4, 0);

          setPausedFrame(NPCLoader.SPRITES.get("kid_boy_03"), 4, 0);
        } 
      },
      //Up
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("kid_boy_16"), 2, 0);
          add(NPCLoader.SPRITES.get("kid_boy_17"), 1, 0);
          add(NPCLoader.SPRITES.get("kid_boy_18"), 0, 0);
          add(NPCLoader.SPRITES.get("kid_boy_19"), 1, 0);

          setPausedFrame(NPCLoader.SPRITES.get("kid_boy_04"), 0, 0);
        } 
      },
      //Down
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("kid_boy_05"), 2, 0);
          add(NPCLoader.SPRITES.get("kid_boy_06"), 1, 0);
          add(NPCLoader.SPRITES.get("kid_boy_07"), 0, 0);
          add(NPCLoader.SPRITES.get("kid_boy_06"), 1, 0);

          setPausedFrame(NPCLoader.SPRITES.get("kid_boy_02"), 0, 0);
        } 
      }
      )
      ),
  KID_GIRL(
      new SpriteSet(
          
      //Left
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("kid_girl_12"), 2, 0);
          add(NPCLoader.SPRITES.get("kid_girl_13"), 2, 0);
          add(NPCLoader.SPRITES.get("kid_girl_14"), 2, 0);
          add(NPCLoader.SPRITES.get("kid_girl_15"), 2, 0);

          setPausedFrame(NPCLoader.SPRITES.get("kid_girl_01"), 1, 0);
        } 
      },
      //Right
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("kid_girl_08"), 0, 0);
          add(NPCLoader.SPRITES.get("kid_girl_09"), 2, 0);
          add(NPCLoader.SPRITES.get("kid_girl_10"), 0, 0);
          add(NPCLoader.SPRITES.get("kid_girl_11"), 2, 0);

          setPausedFrame(NPCLoader.SPRITES.get("kid_girl_03"), 1, 0);
        } 
      },
      //Up
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("kid_girl_16"), -1, 0);
          add(NPCLoader.SPRITES.get("kid_girl_17"), 0, 0);
          add(NPCLoader.SPRITES.get("kid_girl_18"), 0, 0);
          add(NPCLoader.SPRITES.get("kid_girl_17"), 0, 0);

          setPausedFrame(NPCLoader.SPRITES.get("kid_girl_04"), 0, 0);
        } 
      },
      //Down
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("kid_girl_05"), 0, 0);
          add(NPCLoader.SPRITES.get("kid_girl_06"), 0, 0);
          add(NPCLoader.SPRITES.get("kid_girl_07"), -1, 0);
          add(NPCLoader.SPRITES.get("kid_girl_06"), 0, 0);

          setPausedFrame(NPCLoader.SPRITES.get("kid_girl_02"), 0, 0);
        } 
      }
      )
      ),
  PEASANT(
      new SpriteSet(
          
      //Left
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("peasant_11"), 0, 0);
          add(NPCLoader.SPRITES.get("peasant_12"), 0, 0);
          add(NPCLoader.SPRITES.get("peasant_13"), 0, 0);
          add(NPCLoader.SPRITES.get("peasant_12"), 0, 0);

          setPausedFrame(NPCLoader.SPRITES.get("peasant_01"), 0, 0);
        } 
      },
      //Right
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("peasant_08"), 0, 0);
          add(NPCLoader.SPRITES.get("peasant_09"), 0, 0);
          add(NPCLoader.SPRITES.get("peasant_10"), 0, 0);
          add(NPCLoader.SPRITES.get("peasant_09"), 0, 0);

          setPausedFrame(NPCLoader.SPRITES.get("peasant_03"), 0, 0);
        } 
      },
      //Up
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("peasant_14"), 2, 0);
          add(NPCLoader.SPRITES.get("peasant_15"), 0, 0);
          add(NPCLoader.SPRITES.get("peasant_16"), 0, 0);
          add(NPCLoader.SPRITES.get("peasant_15"), 0, 0);

          setPausedFrame(NPCLoader.SPRITES.get("peasant_04"), 0, 0);
        } 
      },
      //Down
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("peasant_05"), 2, 0);
          add(NPCLoader.SPRITES.get("peasant_06"), 0, 0);
          add(NPCLoader.SPRITES.get("peasant_07"), 0, 0);
          add(NPCLoader.SPRITES.get("peasant_06"), 0, 0);

          setPausedFrame(NPCLoader.SPRITES.get("peasant_02"), 0, 0);
        } 
      }
      )
      ),
  DOCTOR(
      new SpriteSet(
      //Left
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("doctor_13"), 3, 0);
          add(NPCLoader.SPRITES.get("doctor_14"), -1, 0);
          add(NPCLoader.SPRITES.get("doctor_15"), 3, 0);
          add(NPCLoader.SPRITES.get("doctor_16"), -2, 0);

          setPausedFrame(NPCLoader.SPRITES.get("doctor_01"), 3, 0);
        } 
      },
      //Right
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("doctor_09"), 0, 0);
          add(NPCLoader.SPRITES.get("doctor_10"), 3, 0);
          add(NPCLoader.SPRITES.get("doctor_11"), 1, 0);
          add(NPCLoader.SPRITES.get("doctor_12"), 3, 0);

          setPausedFrame(NPCLoader.SPRITES.get("doctor_03"), 3, 0);
        } 
      },
      //Up
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("doctor_17"), -1, 0);
          add(NPCLoader.SPRITES.get("doctor_18"), 1, 1);
          add(NPCLoader.SPRITES.get("doctor_19"), 2, 0);
          add(NPCLoader.SPRITES.get("doctor_20"), 1, 1);

          setPausedFrame(NPCLoader.SPRITES.get("doctor_04"), 1, 0);
        } 
      },
      //Down
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("doctor_05"), -1, 0);
          add(NPCLoader.SPRITES.get("doctor_06"), 1, 1);
          add(NPCLoader.SPRITES.get("doctor_07"), 1, 0);
          add(NPCLoader.SPRITES.get("doctor_08"), 1, 1);

          setPausedFrame(NPCLoader.SPRITES.get("doctor_02"), 1, 0);
        } 
      }
      )
      ),
  PROFESSOR(
      new SpriteSet(
      //Left
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("professor_13"), 3, 0);
          add(NPCLoader.SPRITES.get("professor_14"), -1, 0);
          add(NPCLoader.SPRITES.get("professor_15"), 3, 0);
          add(NPCLoader.SPRITES.get("professor_16"), -2, 0);

          setPausedFrame(NPCLoader.SPRITES.get("professor_01"), 3, 0);
        } 
      },
      //Right
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("professor_09"), 1, 0);
          add(NPCLoader.SPRITES.get("professor_10"), 4, 0);
          add(NPCLoader.SPRITES.get("professor_11"), 1, 0);
          add(NPCLoader.SPRITES.get("professor_12"), 4, 0);

          setPausedFrame(NPCLoader.SPRITES.get("professor_03"), 4, 0);
        } 
      },
      //Up
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("professor_17"), -1, 0);
          add(NPCLoader.SPRITES.get("professor_18"), 1, 1);
          add(NPCLoader.SPRITES.get("professor_19"), 2, 0);
          add(NPCLoader.SPRITES.get("professor_20"), 1, 1);

          setPausedFrame(NPCLoader.SPRITES.get("professor_04"), 1, 0);
        } 
      },
      //Down
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("professor_05"), -1, 0);
          add(NPCLoader.SPRITES.get("professor_06"), 1, 1);
          add(NPCLoader.SPRITES.get("professor_07"), 1, 0);
          add(NPCLoader.SPRITES.get("professor_08"), 1, 1);

          setPausedFrame(NPCLoader.SPRITES.get("professor_02"), 1, 0);
        } 
      }
      )
          ),
  BANKTELLER(
      new SpriteSet(
      //Left
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("bank_teller_13"), 3, 0);
          add(NPCLoader.SPRITES.get("bank_teller_14"), -1, 0);
          add(NPCLoader.SPRITES.get("bank_teller_15"), 3, 0);
          add(NPCLoader.SPRITES.get("bank_teller_16"), -2, 0);

          setPausedFrame(NPCLoader.SPRITES.get("bank_teller_01"), 3, 0);
        } 
      },
      //Right
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("bank_teller_09"), 1, 0);
          add(NPCLoader.SPRITES.get("bank_teller_10"), 4, 0);
          add(NPCLoader.SPRITES.get("bank_teller_11"), 1, 0);
          add(NPCLoader.SPRITES.get("bank_teller_12"), 4, 0);

          setPausedFrame(NPCLoader.SPRITES.get("bank_teller_03"), 4, 0);
        } 
      },
      //Up
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("bank_teller_17"), 0, 0);
          add(NPCLoader.SPRITES.get("bank_teller_18"), 1, 1);
          add(NPCLoader.SPRITES.get("bank_teller_19"), 2, 0);
          add(NPCLoader.SPRITES.get("bank_teller_20"), 1, 1);

          setPausedFrame(NPCLoader.SPRITES.get("bank_teller_04"), 1, 0);
        } 
      },
      //Down
      new InitializableAnimation(4,Person.ANI_SPEED,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("bank_teller_05"), 0, 0);
          add(NPCLoader.SPRITES.get("bank_teller_06"), 1, 1);
          add(NPCLoader.SPRITES.get("bank_teller_07"), 1, 0);
          add(NPCLoader.SPRITES.get("bank_teller_08"), 1, 1);

          setPausedFrame(NPCLoader.SPRITES.get("bank_teller_02"), 1, 0);
        } 
      }
      )
      )
  ;
  
  private SpriteSet sprites;

  private NPCType(SpriteSet sprites)
  {
    this.sprites = sprites;
  }

  public SpriteSet getNewSpriteSet()
  {
    return sprites.duplicate();
  }

  /**
   * Takes in a string and returns its respective NPCType
   * 
   * @param s The input string
   * @return the corespondent NPCType
   */
  public static NPCType toEnum(String s){
    for(NPCType type: NPCType.values()){
      if(type.toString().equals(s)){
        return type;
      }
    }
    
    throw new InvalidParameterException("NPC Type " + s + " is not a valid type!");
  }

  /**
   * @return all of the strings in this enum
   */
  public static ArrayList<String> allStrings(){
    ArrayList<String> a = new ArrayList<String>();
    for(NPCType type : values()){
      a.add(type.toString());
    }

    return a;
  }
}
