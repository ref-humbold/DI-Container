package yadic.resolver.exception;

import yadic.DiException;

public class SetterInvocationException
        extends DiException
{
    private static final long serialVersionUID = 3175636363176361357L;

    public SetterInvocationException(String s, Throwable t)
    {
        super(s, t);
    }
}
