package hesso.mas.stdhb.Base.Exceptions;

/**
 * Created by chf on 26.08.2016.
 *
 * Exception class of the STDHR application
 *
 */
public class CityZenExceptions extends Exception
{
    /**
     * Generate a new Exception
     *
     * @param message Message of the Exception
     */
    public CityZenExceptions(String message) {
        super(message);
    }

    /**
     * Generate a new Exception
     *
     * @param message Message of the Exception
     * @param throwable The Throwable class is the superclass of all errors and exceptions in the
     *                   Java language.
     *                   Only objects that are instances of this class (or one of its subclasses) are
     *                   thrown by the Java Virtual Machine or can be thrown by the Java throw statement.
     */
    public CityZenExceptions(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     *
     * @param message
     * @param innereException
     */
    public CityZenExceptions(String message, Exception innereException) {
        super(message, innereException);
    }

    /**
     *
     * @param cause
     */
    public CityZenExceptions(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
