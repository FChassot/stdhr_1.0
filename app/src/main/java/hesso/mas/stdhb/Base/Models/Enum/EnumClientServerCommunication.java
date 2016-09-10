package hesso.mas.stdhb.Base.Models.Enum;

/**
 * Created by chf on 25.08.2016.
 *
 * This enum represents the different technologies who are used for the
 * communication between the client and the server.
 */
public enum EnumClientServerCommunication {

    UNDEFINED(0), ANDROJENA(1), RDF4J(2), REST(3), RESTOKHTTP(4), ;

    private Integer value ;

    private EnumClientServerCommunication(Integer value) {
        this.value = value;
    }

    public Integer showValue(){return value;}

}