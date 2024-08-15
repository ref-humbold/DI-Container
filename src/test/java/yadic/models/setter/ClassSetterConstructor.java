package yadic.models.setter;

import yadic.annotation.Dependency;
import yadic.models.basic.InterfaceBasic;

public class ClassSetterConstructor
        implements InterfaceSetter
{
    private InterfaceBasic basicObject;

    public ClassSetterConstructor(InterfaceBasic basicObject)
    {
        this.basicObject = basicObject;
    }

    @Override
    public InterfaceBasic getBasicObject()
    {
        return basicObject;
    }

    @Override
    @Dependency
    public void setBasicObject(InterfaceBasic basicObject)
    {
        this.basicObject = basicObject;
    }
}