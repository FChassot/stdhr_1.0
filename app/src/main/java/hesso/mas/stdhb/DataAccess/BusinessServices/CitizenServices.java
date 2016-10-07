package hesso.mas.stdhb.DataAccess.BusinessServices;

import java.util.ArrayList;
import java.util.List;

import hesso.mas.stdhb.DataAccess.QueryBuilder.Request.CitizenRequests;
import hesso.mas.stdhb.DataAccess.QueryEngine.Response.CitizenQueryResult;

/**
 * Created by chf on 07.10.2016.
 */
public class CitizenServices {

    /**
     *
     * @return
     */
    public static CitizenQueryResult getSujets() {

        List<String> lList = new ArrayList<>();

        String lQuery = CitizenRequests.getSujetQuery();

        CitizenQueryResult lResponse = new CitizenQueryResult(); // = QueryEngine

        return lResponse;
    }
}
