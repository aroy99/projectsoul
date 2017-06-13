
// probably not needed ehgh -Jason
package komorebi.projsoul.attack;

public class ElementalTest {
	ElementalProperty type;
	
	public ElementalTest(ElementalProperty type){
		this.type=type;
	}
	
	public double findEffectiveness(ElementalProperty attack, ElementalProperty entity){
		
		switch(entity){
			case FIRE:
				switch(attack){
					case FIRE: case WIND:
						return 1;
					case WATER:
						return 1.3;
					case EARTH:
						return 0.7;
				}
			case WATER:
				switch(attack){
					case WATER: case EARTH:
						return 1;
					case WIND:
						return 1.3;
					case FIRE:
						return 0.7;
				}
			case WIND:
				switch(attack){
					case WIND: case FIRE:
						return 1;
					case EARTH:
						return 1.3;
					case WATER:
						return 0.7;
				}
			case EARTH:
				switch(attack){
					case EARTH: case WATER:
						return 1;
					case FIRE:
						return 1.3;
					case WIND:
						return 0.7;
				}
		default:
			return 1;
		
		}
		
		
		
		
		
		
			
	}
	
}
