package dicontainer.resolver.exception;

import dicontainer.DIException;

public class MissingDependenciesException
        extends DIException
{
    private static final long serialVersionUID = 7265031467533657162L;

    public MissingDependenciesException(String s)
    {
        super(s);
    }
}
