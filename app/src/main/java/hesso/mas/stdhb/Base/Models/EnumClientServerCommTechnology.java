package hesso.mas.stdhb.Base.Models;

/**
 * Created by chf on 25.08.2016.
 */
public enum EnumClientServerCommTechnology {

    SOAP("Soap"), REST("Rest"), RETROFIT("Retrofit"), SPRING("SPring"), OKHTTP("OkHttp"), VOLLEY("Volley"), RDF4J("RDF4J") ;

    private String abreviation ;

    private EnumClientServerCommTechnology(String abreviation) {
        this.abreviation = abreviation ;
    }

    public String getAbreviation() {
        return  this.abreviation ;
    }
}
