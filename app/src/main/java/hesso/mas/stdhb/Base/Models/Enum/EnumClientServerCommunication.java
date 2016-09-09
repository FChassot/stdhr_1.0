package hesso.mas.stdhb.Base.Models.Enum;

/**
 * Created by chf on 25.08.2016.
 *
 *
 */
public enum EnumClientServerCommunication {

    UNDEFINED(0), RDF4J(1), ANDROJENA(2), REST(3), OKHTTP(4), ;

    //RETROFIT, SPRING, VOLLEY, SOAP

    private Integer value ;

    private EnumClientServerCommunication(Integer value) {
        this.value = value;
    }

    public Integer showValue(){return value;}

}