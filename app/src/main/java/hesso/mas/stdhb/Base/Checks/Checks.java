package hesso.mas.stdhb.Base.Checks;

import junit.framework.Assert;

/**
 * Created by chf on 08.09.2016.
 */
public final class Checks<T> {

    // It's not possible to instanciate this class
    private Checks()
    {
    }

    /**
     * Checks that the parameter isn't null.
     *
     * @param aObject The object that is to be tested.
     * @param <T> The type of the object.
     */
    public static <T> void AssertNotNull(T aObject)
    {
        if (aObject == null) {
            Assert.assertNotNull("Object cannot be null.");
        }
    }
}
