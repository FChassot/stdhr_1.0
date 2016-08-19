package hesso.mas.stdhb.Base.Software;

/**
 *
 * Created by chf on 01.07.2016.
 *
 * This class provides static methods whishes contains general informations
 * of the program.
 *
 */
public final class Software {

    private static String mVersion;

    private static String mNom;

    private static String mAuteur;

    // Private constructor, this class isn't instanciable
    public Software() {};

    /**
     *
     * @param aVersion
     */
    public static void Version(String aVersion) { mVersion = aVersion; }

    /**
     * Provides the version of the program
     *
     * @return give the version back
     */
    public static String Version() { return mVersion; }

    /**
     * Set the name of the program
     *
     * @param aNom the name of the program
     */
    public static void Application(String aNom) { mNom = aNom; }

    /**
     * Retrieve the name of the program
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
     * Set the name of the creator of the program
     *
     * @return retourne le nom de l'auteur du programme
     */
    public static String Auteur() { return mAuteur; }

}
