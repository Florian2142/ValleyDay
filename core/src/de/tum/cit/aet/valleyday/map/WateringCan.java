package de.tum.cit.aet.valleyday.map;

import de.tum.cit.aet.valleyday.texture.Textures;

public class WateringCan extends hiddenObject implements Item{

    public WateringCan(int x, int y, GameMap map) {
        super(x, y, map);
        super.texture = Textures.WATERING_CAN;
    }

    @Override
    public String pickup(Player player) {
        super.map.removeItem((int) super.getX(),(int) super.getY());
        super.map.reviveCrop(); // revives all crops if they are rotten
        return "Watering can was picked up! Revived the poor rotten crops!";
    }

}
