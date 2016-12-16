package hesso.mas.stdhb.Base.Models.Class;

/**
 * Created by chf on 01.10.2016.
 *
 * This class represents a cultural object type which will be displayed in
 * a special ListView (in the settings activity)
 */
public class CulturalObjectType {

    // Member variables
    private String mCode = null;

    private String mName = null;

    private boolean mSelected = false;

    // public constructor
    public CulturalObjectType(
        String code,
        String name,
        boolean selected) {

        super();

        this.mCode = code;
        this.mName = name;
        this.mSelected = selected;
    }

    // Setter
    public void setCode(String code) {
        this.mCode = code;
    }

    // Getter
    public String getCode() {
        return mCode;
    }

    // Setter
    public void setName(String name) {
        this.mName = name;
    }

    // Getter
    public String getName() {
        return mName;
    }

    // Setter
    public void setSelected(boolean selected) {
        this.mSelected = selected;
    }

    // Getter
    public boolean isSelected() {
        return mSelected;
    }

}