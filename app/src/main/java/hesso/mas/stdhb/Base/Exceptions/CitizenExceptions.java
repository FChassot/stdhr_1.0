package hesso.mas.stdhb.Base.Exceptions;

/**
 * Created by chf on 26.08.2016.
 *
 * Exception class of our STDHR application
 *
 */
public class CitizenExceptions extends Exception

{
    /**
     *
     * @param message
     */
    public CitizenExceptions(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param throwable
     */
    public CitizenExceptions(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     *
     * @param cause
     */
    public CitizenExceptions(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
