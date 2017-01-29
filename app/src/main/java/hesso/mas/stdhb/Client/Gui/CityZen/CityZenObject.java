package hesso.mas.stdhb.Client.Gui.CityZen;

import hesso.mas.stdhb.Base.Tools.MyString;

/**
 * Created by chf on 29.01.2017.
 */

public class CityZenObject {

    private String mDescription = MyString.EMPTY_STRING;
    private String mStart = MyString.EMPTY_STRING;
    private String mEnd = MyString.EMPTY_STRING;
    private String mTitle = MyString.EMPTY_STRING;

    public CityZenObject(
            String aTitle,
            String aDescription,
            String aStart,
            String aEnd
    ) {
        mTitle = aTitle;
        mDescription = aDescription;
        mStart = aStart;
        mEnd = aEnd;
    }

}
