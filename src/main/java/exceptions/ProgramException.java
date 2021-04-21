package exceptions;

import java.io.Serial;

/**
 * Encapsulation of an exception for the illegal arguments in the system.
 *
 * @author Nodirjon Tadjiev
 * @version 1.0
 */
public class ProgramException extends Exception {

    /*
     Serial version UID for this exception type
     */
    @Serial
    private static final long serialVersionUID = 1888536871327098678L;

    /*
      Instantiates a new {@link ProgramException} with the give message.
      @param message the message of the exception
     */
    public ProgramException(final String message) {
        super(message);
    }
}
