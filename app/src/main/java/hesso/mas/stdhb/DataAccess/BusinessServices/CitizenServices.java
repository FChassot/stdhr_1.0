package hesso.mas.stdhb.DataAccess.BusinessServices;

import java.util.ArrayList;
import java.util.List;

import hesso.mas.stdhb.DataAccess.QueryBuilder.Request.CitizenRequests;
import hesso.mas.stdhb.DataAccess.QueryEngine.CitizenDbObject;
import hesso.mas.stdhb.DataAccess.QueryEngine.QueryEngine;

/**
 * Created by chf on 07.10.2016.
 *
 *
 */
public class CitizenServices {

    /**
     * This method allow to retrieve the possible subjects in the Citizen DB
     *
     * @return a list of the subjects
     */
    public static List<String> getSujets() {

        List<String> lList = new ArrayList<>();

        String lQuery = CitizenRequests.getSujetQuery();

        List<CitizenDbObject> lResponse = QueryEngine.request(lQuery);

        if (lResponse != null) {
            for (CitizenDbObject lObjet : lResponse) {
                lList.add(lObjet.GetValue("sujet"));
            }
        }

        return lList;
    }
}
