package hesso.mas.stdhb.DataAccess.Services;

import java.util.ArrayList;
import java.util.List;

import hesso.mas.stdhb.DataAccess.Sparql.CitizenRequests;
import hesso.mas.stdhb.DataAccess.QueryEngine.CitizenDbObject;
import hesso.mas.stdhb.DataAccess.QueryEngine.QueryEngine;

/**
 * Created by chf on 07.10.2016.
 *
 * This class provides methods to retrieve synchronously Business Data on the
 * Citizen Sparql DB
 */
public class CitizenServices {

    public void CitizenServices() {}

    /**
     * This method allow to retrieve the possible subjects in the Citizen DB
     *
     * @return a list of the subjects
     */
    public List<String> getCulturalObjectSubjects() {

        List<String> lList = new ArrayList<>();

        String lQuery = CitizenRequests.getSubjectQuery();

        List<CitizenDbObject> lResponse = QueryEngine.request(lQuery);

        if (lResponse != null) {
            for (CitizenDbObject lObjet : lResponse) {
                lList.add(lObjet.GetValue("subject"));
            }
        }

        return lList;
    }
}
