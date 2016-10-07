package hesso.mas.stdhb.Base.Constants;

/**
 * Created by chf on 21.07.2016.
 *
 * Class for general Constants
 */
public class BaseConstants {

    public static final String Attr_Citizen_Server_URI =
            "http://ec2-52-39-53-29.us-west-2.compute.amazonaws.com:8080/openrdf-sesame/";

    public static final String Attr_Citizen_Repository_NAME =
            "CityZenDM";

    // constant used to define the default radius of search
    public static final String Attr_Default_Ray_Search = "500";

    // constant used to save the radius of search of the radar
    public static final String Attr_Search_Radius = "search_radius";

    // constant used to switch on/off the mode radar
    public static final String Attr_Radar_Switch = "radar_on_off";

    // constant used to identify the client-server communication type configured
    public static final String Attr_ClientServer_Communication = "client_server_communication";

    // constant used to identify the culturalInterest type configured
    public static final String Attr_CulturalInterest_type = "cultural_object_type";

    // constant used to define the type of search
    public static final String Attr_TypeOfSearch = "radar_type_of_search";

}
