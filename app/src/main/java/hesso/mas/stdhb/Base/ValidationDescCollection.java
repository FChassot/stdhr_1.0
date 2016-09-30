package hesso.mas.stdhb.Base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chf on 30.09.2016.
 */
public class ValidationDescCollection {

    public List<String> mListOfValidationDesc = new ArrayList<>();

    public void add(String aValDesc) {
        mListOfValidationDesc.add(aValDesc);
    }

    public int count() { return mListOfValidationDesc.size(); }
}
