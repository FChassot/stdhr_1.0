package hesso.mas.stdhb.Base.Models;

/**
 * Created by chf on 25.08.2016.
 */
public enum EnumClientServerCommunication {

    UNDEFINED(0), SOAP(1), REST(2), RETROFIT(3), SPRING(4), OKHTTP(5), VOLLEY(6), RDF4J(7) ;

    private Integer value ;

    private EnumClientServerCommunication(Integer value) {
        this.value = value;
    }

    public Integer showValue(){return value;}

}