package dicontainer.dictionary.exception;

import dicontainer.DIException;

public class NotDerivedTypeException
        extends DIException
{
    private static final long serialVersionUID = -3180961583302361880L;

    public NotDerivedTypeException(String s)
    {
        super(s);
    }
}
