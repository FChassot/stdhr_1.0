package hesso.mas.stdhb.Base.Models.Enum;

/**
 * Created by chf on 25.08.2016.
 *
 * This enum represents the different technologies who can be used for the
 * communication between the client and the server sparql.
 */
public enum EnumClientServerCommunication {

    // RDF4J(2)
    UNDEFINED(0), ANDROJENA(1), REST(2) ;

    private Integer value ;

    private EnumClientServerCommunication(Integer value) {
        this.value = value;
    }

    public Integer showValue(){return value;}

}