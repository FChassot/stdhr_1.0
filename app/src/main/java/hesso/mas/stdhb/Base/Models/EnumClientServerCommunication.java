package hesso.mas.stdhb.Base.Models;

/**
 * Created by chf on 25.08.2016.
 *
 *
 */
public enum EnumClientServerCommunication {

    UNDEFINED(0), SOAP(1), REST(2), OKHTTP(3), RDF4J(4), ANDROJENA(5);

    //RETROFIT, SPRING, VOLLEY

    private Integer value ;

    private EnumClientServerCommunication(Integer value) {
        this.value = value;
    }

    public Integer showValue(){return value;}

}