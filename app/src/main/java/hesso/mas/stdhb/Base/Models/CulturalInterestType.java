package hesso.mas.stdhb.Base.Models;

/**
 * Created by frede on 01.10.2016.
 */
public class CulturalInterestType {

    String code = null;
    String name = null;
    boolean selected = false;

    public CulturalInterestType(String code, String name, boolean selected) {
        super();
        this.code = code;
        this.name = name;
        this.selected = selected;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
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