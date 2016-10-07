package hesso.mas.stdhb.Base.Models.Class;

/**
 * Created by chf on 01.10.2016.
 *
 * This class represents a cultural object type which will be displayed in
 * the combo-listview
 */
public class CulturalObjectType {

    // member variables
    String code = null;
    String name = null;
    boolean selected = false;

    // public constructor
    public CulturalObjectType(String code, String name, boolean selected) {
        super();
        this.code = code;
        this.name = name;
        this.selected = selected;
    }

    // Getter
    public String getCode() {
        return code;
    }

    // Setter
    public void setCode(String code) {
        this.code = code;
    }

    // Getter
    public String getName() {
        return name;
    }

    // Setter
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}