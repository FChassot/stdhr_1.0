package hesso.mas.stdhb.Base.Software;

/**
 *
 * Created by Frédéric Chassot (chf) on 01.07.2016.
 *
 * Cette classe contient des méthodes statiques destinées à contenir les
 * informations générales du programme.
 *
 */
public final class Software {

    private static String mVersion;

    private static String mNom;

    private static String mAuteur;

    // Contructeur privé, classe pas instanciable
    public Software() {};

    /**
     *
     * @param aVersion
     */
    public static void Version(String aVersion) { mVersion = aVersion; }

    /**
     *
     * @return retourne la version de l'application
     */
    public static String Version() { return mVersion; }

    /**
     * @param aNom
     */
    public static void Application(String aNom) { mNom = aNom; }

    /**
     *
     * @return retourne le nom de l'application
     */
    public static String Application() { return mNom; }

    /**
     * @param aAuteur
     */
    public static void Auteur(String aAuteur) { mAuteur = aAuteur; }

    /**
     *
     * @return retourne le nom de l'auteur du programme
     */
    public static String Auteur() { return mAuteur; }

}
