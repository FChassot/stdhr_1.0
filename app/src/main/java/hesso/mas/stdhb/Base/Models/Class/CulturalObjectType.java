package hesso.mas.stdhb.Base.Models.Class;

/**
 * Created by chf on 01.10.2016.
 *
 * This class represents a cultural object type which will be displayed in
 * a special listview (in the settings activity)
 */
public class CulturalObjectType {

    // member variables
    String code = null;

    String name = null;

    boolean selected = false;

    // public constructor
    public CulturalObjectType(
        String aCode,
        String aName,
        boolean selected) {

        super();

        this.code = aCode;
        this.name = aName;
        this.selected = selected;
    }

    // Setter
    public void setCode(String aCode) {
        this.code = aCode;
    }

    // Getter
    public String getCode() {
        return code;
    }

    // Setter
    public void setName(String aName) {
        this.name = aName;
    }

    // Getter
    public String getName() {
        return name;
    }

    // Setter
    public void setSelected(boolean aSelected) {
        this.selected = aSelected;
    }

    // Getter
    public boolean isSelected() {
        return selected;
    }

}