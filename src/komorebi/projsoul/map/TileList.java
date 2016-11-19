/**
 * TileList.java    May 18, 2016, 8:42:36 PM
 */

package komorebi.projsoul.map;

/**
 * All of the tiles in the game and their ids
 * 
 * @author Aaron Roy
 */

public enum TileList {
  BLANK     (  0,   0,   0),    yBLANK    ( 64,  64,   0),
  GRASS     (  1,  16,   0),    yGRASS    ( 65,  80,   0),
  FLOWER    (  2,  32,   0),    yFLOWER   ( 66,  96,   0),
  G_DECOR1  (  3,  48,   0),    yG_DECOR1 ( 67, 112,   0),
  G_DECOR2  (  4,   0,  16),    yG_DECOR2 ( 68,  64,  16),
  HEDGE_L   (  5,  16,  16),    yHEDGE_L  ( 69,  80,  16),
  HEDGE_C   (  6,  32,  16),    yHEDGE_C  ( 70,  96,  16),
  HEDGE_R   (  7,  48,  16),    yHEDGE_R  ( 71, 112,  16),
  DOOR1     (  8,   0,  32),    yDOOR1    ( 72,  64,  32),
  DOOR2     (  9,  16,  32),    yDOOR2    ( 73,  80,  32),
  DOOR3_L   ( 10,  32,  32),    yDOOR3_L  ( 74,  96,  32),
  DOOR3_R   ( 11,  48,  32),    yDOOR3_R  ( 75, 112,  32),
  DOOR4     ( 12,   0,  48),    yDOOR4    ( 76,  64,  48),
  DOOR5     ( 13,  16,  48),    yDOOR5    ( 77,  80,  48),
  ROOF_UL   ( 14,  32,  48),    yROOF_UL  ( 78,  96,  48),
  ROOF_U    ( 15,  48,  48),    yROOF_U   ( 79, 112,  48),
  ROOF_UR   ( 16,   0,  64),    yROOF_UR  ( 80,  64,  64),
  ROOF_L    ( 17,  16,  64),    yROOF_L   ( 81,  80,  64),
  ROOF_R    ( 18,  32,  64),    yROOF_R   ( 82,  96,  64),
  ROOF_C    ( 19,  48,  64),    yROOF_C   ( 83, 112,  64),
  ROOF_BL   ( 20,   0,  80),    yROOF_BL  ( 84,  64,  80),
  ROOF_B    ( 21,  16,  80),    yROOF_B   ( 85,  80,  80),
  ROOF_BR   ( 22,  32,  80),    yROOF_BR  ( 86,  96,  80),
  WIN1      ( 23,  48,  80),    yWIN1     ( 87, 112,  80),
  WIN2      ( 24,   0,  96),    yWIN2     ( 88,  64,  96),
  JAIL_WIN  ( 25,  16,  96),    yJAIL_WIN ( 89,  80,  96),
  REST_WIN  ( 26,  32,  96),    yREST_WIN ( 90,  96,  96),
  CHURCH_WIN( 27,  48,  96),    yCHURCH_WI( 91, 112,  96),
  D_ARC1    ( 28,   0, 112),    yD_ARC1   ( 92,  64, 112),
  D_ARC2    ( 29,  16, 112),    yD_ARC2   ( 93,  80, 112),
  D_ARC3_L  ( 30,  32, 112),    yD_ARC3_L ( 94,  96, 112),
  D_ARC3_R  ( 31,  48, 112),    yD_ARC3_R ( 95, 112, 112),
  D_ARC4    ( 32,   0, 128),    yD_ARC4   ( 96,  64, 128),
  D_ARC5    ( 33,  16, 128),    yD_ARC5   ( 97,  80, 128),
  xBLANK    ( 34,  32, 128),    zBLANK    ( 98,  96, 128),
  xGRASS    ( 35,  48, 128),    zGRASS    ( 99, 112, 128),
  xFLOWER   ( 36,   0, 144),    zFLOWER   (100,  64, 144),
  xG_DECOR1 ( 37,  16, 144),    zG_DECOR1 (101,  80, 144),
  xG_DECOR2 ( 38,  32, 144),    zG_DECOR2 (102,  96, 144),
  xHEDGE_L  ( 39,  48, 144),    zHEDGE_L  (103, 112, 144),
  xHEDGE_C  ( 40,   0, 160),    zHEDGE_C  (104,  64, 160),
  xHEDGE_R  ( 41,  16, 160),    zHEDGE_R  (105,  80, 160),
  xDOOR1    ( 42,  32, 160),    zDOOR1    (106,  96, 160),
  xDOOR2    ( 43,  48, 160),    zDOOR2    (107, 112, 160),
  xDOOR3_L  ( 44,   0, 176),    zDOOR3_L  (108,  64, 176),
  xDOOR3_R  ( 45,  16, 176),    zDOOR3_R  (109,  80, 176),
  xDOOR4    ( 46,  32, 176),    zDOOR4    (110,  96, 176),
  xDOOR5    ( 47,  48, 176),    zDOOR5    (111, 112, 176),
  xROOF_UL  ( 48,   0, 192),    zROOF_UL  (112,  64, 192),
  xROOF_U   ( 49,  16, 192),    zROOF_U   (113,  80, 192),
  xROOF_UR  ( 50,  32, 192),    zROOF_UR  (114,  96, 192),
  xROOF_L   ( 51,  48, 192),    zROOF_L   (115, 112, 192),
  xROOF_R   ( 52,   0, 208),    zROOF_R   (116,  64, 208),
  xROOF_C   ( 53,  16, 208),    zROOF_C   (117,  80, 208),
  xROOF_BL  ( 54,  32, 208),    zROOF_BL  (118,  96, 208),
  xROOF_B   ( 55,  48, 208),    zROOF_B   (119, 112, 208),
  xROOF_BR  ( 56,   0, 224),    zROOF_BR  (120,  64, 224),
  xWIN1     ( 57,  16, 224),    zWIN1     (121,  80, 224),
  xWIN2     ( 58,  32, 224),    zWIN2     (122,  96, 224),
  xJAIL_WIN ( 59,  48, 224),    zJAIL_WIN (123, 112, 224),
  xREST_WIN ( 60,   0, 240),    zREST_WIN (124,  64, 240),
  xCHURCH_WI( 61,  16, 240),    zCHURCH_WI(125,  80, 240),
  xD_ARC1   ( 62,  32, 240),    zD_ARC1   (126,  96, 240),
  xD_ARC2   ( 63,  48, 240),    zD_ARC2   (127, 112, 240),

  aBLANK    (128, 128,   0),    cBLANK    (192, 192,   0),
  aGRASS    (129, 144,   0),    cGRASS    (193, 208,   0),
  aFLOWER   (130, 160,   0),    cFLOWER   (194, 224,   0),
  aG_DECOR1 (131, 176,   0),    cG_DECOR1 (195, 240,   0),
  aG_DECOR2 (132, 128,  16),    cG_DECOR2 (196, 192,  16),
  aHEDGE_L  (133, 144,  16),    cHEDGE_L  (197, 208,  16),
  aHEDGE_C  (134, 160,  16),    cHEDGE_C  (198, 224,  16),
  aHEDGE_R  (135, 176,  16),    cHEDGE_R  (199, 240,  16),
  aDOOR1    (136, 128,  32),    cDOOR1    (200, 192,  32),
  aDOOR2    (137, 144,  32),    cDOOR2    (201, 208,  32),
  aDOOR3_L  (138, 160,  32),    cDOOR3_L  (202, 224,  32),
  aDOOR3_R  (139, 176,  32),    cDOOR3_R  (203, 240,  32),
  aDOOR4    (140, 128,  48),    cDOOR4    (204, 192,  48),
  aDOOR5    (141, 144,  48),    cDOOR5    (205, 208,  48),
  aROOF_UL  (142, 160,  48),    cROOF_UL  (206, 224,  48),
  aROOF_U   (143, 176,  48),    cROOF_U   (207, 240,  48),
  aROOF_UR  (144, 128,  64),    cROOF_UR  (208, 192,  64),
  aROOF_L   (145, 144,  64),    cROOF_L   (209, 208,  64),
  aROOF_R   (146, 160,  64),    cROOF_R   (210, 224,  64),
  aROOF_C   (147, 176,  64),    cROOF_C   (211, 240,  64),
  aROOF_BL  (148, 128,  80),    cROOF_BL  (212, 192,  80),
  aROOF_B   (149, 144,  80),    cROOF_B   (213, 208,  80),
  aROOF_BR  (150, 160,  80),    cROOF_BR  (214, 224,  80),
  aWIN1     (151, 176,  80),    cWIN1     (215, 240,  80),
  aWIN2     (152, 128,  96),    cWIN2     (216, 192,  96),
  aJAIL_WIN (153, 144,  96),    cJAIL_WIN (217, 208,  96),
  aREST_WIN (154, 160,  96),    cREST_WIN (218, 224,  96),
  aCHURCH_WI(155, 176,  96),    cCHURCH_WI(219, 240,  96),
  aD_ARC1   (156, 128, 112),    cD_ARC1   (220, 192, 112),
  aD_ARC2   (157, 144, 112),    cD_ARC2   (221, 208, 112),
  aD_ARC3_L (158, 160, 112),    cD_ARC3_L (222, 224, 112),
  aD_ARC3_R (159, 176, 112),    cD_ARC3_R (223, 240, 112),
  aD_ARC4   (160, 128, 128),    cD_ARC4   (224, 192, 128),
  aD_ARC5   (161, 144, 128),    cD_ARC5   (225, 208, 128),
  bBLANK    (162, 160, 128),    dBLANK    (226, 224, 128),
  bGRASS    (163, 176, 128),    dGRASS    (227, 240, 128),
  bFLOWER   (164, 128, 144),    dFLOWER   (228, 192, 144),
  bG_DECOR1 (165, 144, 144),    dG_DECOR1 (229, 208, 144),
  bG_DECOR2 (166, 160, 144),    dG_DECOR2 (230, 224, 144),
  bHEDGE_L  (167, 176, 144),    dHEDGE_L  (231, 240, 144),
  bHEDGE_C  (168, 128, 160),    dHEDGE_C  (232, 192, 160),
  bHEDGE_R  (169, 144, 160),    dHEDGE_R  (233, 208, 160),
  bDOOR1    (170, 160, 160),    dDOOR1    (234, 224, 160),
  bDOOR2    (171, 176, 160),    dDOOR2    (235, 240, 160),
  bDOOR3_L  (172, 128, 176),    dDOOR3_L  (236, 192, 176),
  bDOOR3_R  (173, 144, 176),    dDOOR3_R  (237, 208, 176),
  bDOOR4    (174, 160, 176),    dDOOR4    (238, 224, 176),
  bDOOR5    (175, 176, 176),    dDOOR5    (239, 240, 176),
  bROOF_UL  (176, 128, 192),    dROOF_UL  (240, 192, 192),
  bROOF_U   (177, 144, 192),    dROOF_U   (241, 208, 192),
  bROOF_UR  (178, 160, 192),    dROOF_UR  (242, 224, 192),
  bROOF_L   (179, 176, 192),    dROOF_L   (243, 240, 192),
  bROOF_R   (180, 128, 208),    dROOF_R   (244, 192, 208),
  bROOF_C   (181, 144, 208),    dROOF_C   (245, 208, 208),
  bROOF_BL  (182, 160, 208),    dROOF_BL  (246, 224, 208),
  bROOF_B   (183, 176, 208),    dROOF_B   (247, 240, 208),
  bROOF_BR  (184, 128, 224),    dROOF_BR  (248, 192, 224),
  bWIN1     (185, 144, 224),    dWIN1     (249, 208, 224),
  bWIN2     (186, 160, 224),    dWIN2     (250, 224, 224),
  bJAIL_WIN (187, 176, 224),    dJAIL_WIN (251, 240, 224),
  bREST_WIN (188, 128, 240),    dREST_WIN (252, 192, 240),
  bCHURCH_WI(189, 144, 240),    dCHURCH_WI(253, 208, 240),
  bD_ARC1   (190, 160, 240),    dD_ARC1   (254, 224, 240),
  bD_ARC2   (191, 176, 240),    dD_ARC2   (255, 240, 240);
  
  private short id;
  private short x,y;

  /**
   * Creates a new Tile Type
   * 
   * @param num The numerical ID of this type 
   * @param x The x value of this tile
   * @param y The y value of this tile
   */
  TileList(int num, int x, int y){
    id = (short)num;
    this.x = (short)x;
    this.y = (short)y;
  }

  /**
   * Returns a tile's numerical id
   * 
   * @return id of this value
   */
  public int getID(){
    return id;
  }
  
  public int getX(){
    return x;
  }
  
  public int getY(){
    return y;
  }
  
  /**
   * A string value for this enum
   * 
   * @return The full name of the enum, or bleh if not found
   */
  public String toString(){
    switch (this) {
      case BLANK     : return "Blank"             ;
      case GRASS     : return "Grass"             ;
      case FLOWER    : return "Flower"            ;
      case G_DECOR1  : return "Grass Decoration 1";
      case G_DECOR2  : return "Grass Decoration 2";
      case HEDGE_L   : return "Hedge Left"        ;
      case HEDGE_C   : return "Hedge Center"      ;
      case HEDGE_R   : return "Hedge Right"       ;
      case DOOR1     : return "Door 1"            ;
      case DOOR2     : return "Door 2"            ;
      case DOOR3_L   : return "Door 3 Left"       ;
      case DOOR3_R   : return "Door 3 Right"      ;
      case DOOR4     : return "Door 4"            ;
      case DOOR5     : return "Door 5"            ;
      case ROOF_UL   : return "Roof Up Left"      ;
      case ROOF_U    : return "Roof Up"           ;
      case ROOF_UR   : return "Roof Up Right"     ;
      case ROOF_L    : return "Roof Left"         ;
      case ROOF_R    : return "Roof Right"        ;
      case ROOF_C    : return "Roof Center"       ;
      case ROOF_BL   : return "Roof Bottom Left"  ;
      case ROOF_B    : return "Roof Bottom"       ;
      case ROOF_BR   : return "Roof Bottom Right" ;
      case WIN1      : return "Window 1"          ;
      case WIN2      : return "Window 2"          ;
      case JAIL_WIN  : return "Jail Window"       ;
      case REST_WIN  : return "Rest Window"       ;
      case CHURCH_WIN: return "Church Window"     ;
      case D_ARC1    : return "Door Arc 1"        ;
      case D_ARC2    : return "Door Arc 2"        ;
      case D_ARC3_L  : return "Door Arc 3 Left"   ;
      case D_ARC3_R  : return "Door Arc 3 Right"  ;
      case D_ARC4    : return "Door Arc 4"        ;
      case D_ARC5    : return "Door Arc 5"        ;
      default        : return "bleh"              ;
    }
  }

  /**
   * Returns the TileList with the specified index
   * 
   * @param index The index of the tile
   * @return Tile that matched the index, BLANK if not found
   */
  public static TileList getTile(int index) {
    for(TileList tl: TileList.values()){
      if(tl.getID() == index){
        return tl;
      }
    }
    return BLANK;
  }
}