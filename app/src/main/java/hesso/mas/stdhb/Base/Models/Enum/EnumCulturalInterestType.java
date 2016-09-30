package hesso.mas.stdhb.Base.Models.Enum;

/**
 * Created by chf on 30.09.2016.
 */
public enum EnumCulturalInterestType {

    UNDEFINED(0), CULTURALPLACE(1), CULTURALEVENT(2), CULTURALPERSON(3), FOLKLORE(4), PHYSICALOBJECT(5),;

    private Integer value ;

    private EnumCulturalInterestType(Integer value) {
        this.value = value;
    }

    public Integer showValue(){return value;}
}
