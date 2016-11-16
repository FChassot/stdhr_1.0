package hesso.mas.stdhb.Client.Gui.Radar.RadarHelper;

/**
 * Created by chf on 27.09.2016.
 *
 * Return object of the private method <see cref="..."/>.
 */
public class RadarViewPosition {

    // Member variables
    private int mPositionX;

    private int mPositionY;

    // Constructor
    public RadarViewPosition(
            int aPositionX,
            int aPositionY) {

        mPositionX = aPositionX;
        mPositionY = aPositionY;
    }

    // Getter
    public Integer getX(){ return mPositionX;}

    // Getter
    public Integer getY(){ return mPositionY;}
}
