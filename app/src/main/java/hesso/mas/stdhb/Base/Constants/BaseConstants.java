package hesso.mas.stdhb.Base.Constants;

/**
 * Created by chf on 21.07.2016.
 *
 * Class for general constants. The class can't be instantiate because
 * it is declared as final.
 */
public final class BaseConstants {

    // constant which contains the URI of the Citizen (sparql) server
    public static final String Attr_Citizen_Server_URI =
            "http://ec2-52-39-53-29.us-west-2.compute.amazonaws.com:8080/openrdf-sesame/";

    // constant which contains the name of the repository
    public static final String Attr_Citizen_Repository_NAME = "CityZenDM";

    // constant used to define the default radius of search
    public static final int Attr_Default_Radius_Search = 5000;

    // constant used to save the radius of search of the radar
    public static final String Attr_Radius_Search = "Attr_Radius_Search";

    // constant used to save the mode on/off of the radar
    public static final String Attr_Radar_Switch = "Attr_Radar_Switch";

    // constant used to save the mode of communication client-server
    public static final String Attr_ClientServer_Communication = "Attr_Client_Server_Communication";

    // constant used to save the mode of communication client-server
    public static final String Attr_Subject_Search_Type = "Attr_Subject_Search_Type";

    // constant used to save the mode of communication client-server
    public static final String Attr_Subject_Selected = "Attr_Subject_Selected";

    // constant used to save the type of the cultural object to search
    public static final String Attr_CulturalObject_Type = "Attr_Cultural_Object_Type";

    // constant used to save the type of search
    public static final String Attr_TypeOfSearch = "Attr_Type_of_Search";

    // constant used to save the type of search
    public static final String Attr_Citizen_User_Text = "Citizen radar's user";

}
