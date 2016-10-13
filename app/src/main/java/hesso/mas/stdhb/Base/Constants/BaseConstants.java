package hesso.mas.stdhb.Base.Constants;

/**
 * Created by chf on 21.07.2016.
 *
 * Class for general constants
 */
public final class BaseConstants {

    // constant which contains the URI of the Citizen (sparql) server
    public static final String Attr_Citizen_Server_URI =
            "http://ec2-52-39-53-29.us-west-2.compute.amazonaws.com:8080/openrdf-sesame/";

    // constant which contains the name of the repository
    public static final String Attr_Citizen_Repository_NAME =
            "CityZenDM";

    // constant used to define the default radius of search
    public static final String Attr_Default_Radius_Search = "500";

    // constant used to save the radius of search of the radar
    public static final String Attr_Search_Radius = "search_radius";

    // constant used to save the mode on/off of the radar
    public static final String Attr_Radar_Switch = "radar_on_off";

    // constant used to save the mode of communication client-server
    public static final String Attr_ClientServer_Communication = "client_server_communication";

    // constant used to save the mode of communication client-server
    public static final String Attr_Subject_Search_Type = "cultural_object_subject";

    // constant used to save the type of the cultural object to search
    public static final String Attr_CulturalObject_Type = "cultural_object_type";

    // constant used to save the type of search
    public static final String Attr_TypeOfSearch = "radar_type_of_search";

}
