package hesso.mas.stdhb.Base.Exceptions;

/**
 * Created by chf on 26.08.2016.
 *
 * Exception class of the STDHR application
 *
 */
public class CitizenExceptions extends Exception
{
    /**
     * Generate a new Exception
     *
     * @param aMessage Message of the Exception
     */
    public CitizenExceptions(String aMessage) {
        super(aMessage);
    }

    /**
     * Generate a new Exception
     *
     * @param aMessage Message of the Exception
     * @param aThrowable The Throwable class is the superclass of all errors and exceptions in the Java language.
     *                  Only objects that are instances of this class (or one of its subclasses) are thrown by
     *                  the Java Virtual Machine or can be thrown by the Java throw statement.
     */
    public CitizenExceptions(String aMessage, Throwable aThrowable) {
        super(aMessage, aThrowable);
    }

    /**
     *
     * @param aMessage
     * @param aInnereException
     */
    public CitizenExceptions(String aMessage, Exception aInnereException) {
        super(aMessage, aInnereException);
    }

    /**
     *
     * @param aCause
     */
    public CitizenExceptions(Throwable aCause) {
        super(aCause);
        // TODO Auto-generated constructor stub
    }
}
