package hesso.mas.stdhb.Client.Gui.Radar.RadarHelper;

/**
 * Created by chf on 27.09.2016.
 *
 * Return object of the private method <see cref="..."/>.
 */
public class RadarViewPosition {

    private Integer mPositionX;
    private Integer mPositionY;

    // Constructor
    public RadarViewPosition(Integer aX, Integer aY) {

        mPositionX = aX;
        mPositionY = aY;
    }

    // Getter
    public Integer getX(){ return mPositionX;}

    // Getter
    public Integer getY(){ return mPositionY;}
}
