package hesso.mas.stdhb.Base.QueryBuilder;

/**
 * Created by chf on 21.09.2016.
 */
public class CitizenDbObject {

    private Literal mLiteral;
    private URI mUri;

    private String mValue;

    public CitizenDbObject(Literal aObject) {
        if (aObject instanceof  Literal) { mLiteral = aObject; }
    }

    public CitizenDbObject(URI aObject) {
        if (aObject instanceof  URI) { mUri = aObject; }
    }

    public CitizenDbObject(String aObject) {
        if (aObject instanceof  String) { mValue = aObject; }
    }
}
