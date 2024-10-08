package yadic.registry;

import java.lang.reflect.Modifier;

import yadic.annotation.Register;
import yadic.annotation.SelfRegister;

final class TypesUtils
{
    static boolean isAbstractReferenceType(Class<?> type)
    {
        return !type.isPrimitive() && (type.isInterface() || Modifier.isAbstract(
                type.getModifiers()));
    }

    static boolean isAnnotatedType(Class<?> type)
    {
        return type.isAnnotationPresent(Register.class) || type.isAnnotationPresent(
                SelfRegister.class);
    }
}
