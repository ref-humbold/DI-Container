package dicontainer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dicontainer.dictionary.exception.AbstractTypeException;
import dicontainer.dictionary.valuetypes.NullInstanceException;
import dicontainer.models.basic.*;
import dicontainer.models.constructor.*;
import dicontainer.models.diamond.ClassDiamondLeft;
import dicontainer.models.diamond.ClassDiamondTop;
import dicontainer.models.diamond.InterfaceDiamondLeft;
import dicontainer.models.diamond.InterfaceDiamondTop;
import dicontainer.models.setter.*;
import dicontainer.resolver.exception.IncorrectDependencySetterException;
import dicontainer.resolver.exception.MultipleAnnotatedConstructorsException;
import dicontainer.resolver.exception.NoSuitableConstructorException;

public class DiContainerTest
{
    private DiContainer testObject;

    @BeforeEach
    public void setUp()
    {
        testObject = new DiContainer();
    }

    @AfterEach
    public void tearDown()
    {
        testObject = null;
    }

    // region registerType (single class)

    @Test
    public void registerType_WhenSingleClass_ThenDifferentInstances()
    {
        // given
        testObject.registerType(ClassConstructorDefault.class);

        // when
        ClassConstructorDefault result1 = testObject.resolve(ClassConstructorDefault.class);
        ClassConstructorDefault result2 = testObject.resolve(ClassConstructorDefault.class);

        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull().isNotSameAs(result1);
    }

    @Test
    public void registerType_WhenSingleClassAsSingleton_ThenSameInstance()
    {
        // given
        testObject.registerType(ClassConstructorDefault.class, ConstructionPolicy.SINGLETON);

        // when
        ClassConstructorDefault result1 = testObject.resolve(ClassConstructorDefault.class);
        ClassConstructorDefault result2 = testObject.resolve(ClassConstructorDefault.class);

        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull().isSameAs(result1);
    }

    @Test
    public void registerType_WhenSingleClassChangesSingleton_ThenChangesInstances()
    {
        // given 1
        testObject.registerType(ClassConstructorDefault.class, ConstructionPolicy.SINGLETON);

        // when 1
        ClassConstructorDefault result11 = testObject.resolve(ClassConstructorDefault.class);
        ClassConstructorDefault result12 = testObject.resolve(ClassConstructorDefault.class);

        // then 1
        Assertions.assertThat(result11).isNotNull();
        Assertions.assertThat(result12).isNotNull().isSameAs(result11);

        // given 2
        testObject.registerType(ClassConstructorDefault.class, ConstructionPolicy.CONSTRUCT);

        // when 2
        ClassConstructorDefault result21 = testObject.resolve(ClassConstructorDefault.class);
        ClassConstructorDefault result22 = testObject.resolve(ClassConstructorDefault.class);

        // then 2
        Assertions.assertThat(result21).isNotNull();
        Assertions.assertThat(result22).isNotNull().isNotSameAs(result21);
    }

    @Test
    public void registerType_WhenSingleClassIsInterface_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.registerType(InterfaceBasic.class))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void registerType_WhenSingleClassIsAbstractClass_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.registerType(ClassBasicAbstract.class))
                  .isInstanceOf(AbstractTypeException.class);
    }

    // endregion
    // region registerType [inheritance]

    @Test
    public void registerType_WhenInheritanceFromInterface_ThenDifferentInstances()
    {
        // given
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class);

        // when
        InterfaceBasic result1 = testObject.resolve(InterfaceBasic.class);
        InterfaceBasic result2 = testObject.resolve(InterfaceBasic.class);

        // then
        Assertions.assertThat(result1).isNotNull().isInstanceOf(ClassConstructorDefault.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isNotSameAs(result1)
                  .isInstanceOf(ClassConstructorDefault.class);
    }

    @Test
    public void registerType_WhenInheritanceFromInterfaceAsSingleton_ThenSameInstances()
    {
        // given
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                ConstructionPolicy.SINGLETON);

        // when
        InterfaceBasic result1 = testObject.resolve(InterfaceBasic.class);
        InterfaceBasic result2 = testObject.resolve(InterfaceBasic.class);

        // then
        Assertions.assertThat(result1).isNotNull().isInstanceOf(ClassConstructorDefault.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isSameAs(result1)
                  .isInstanceOf(ClassConstructorDefault.class);
    }

    @Test
    public void registerType_WhenInheritanceFromInterfaceChangesSingleton_ThenChangeInstances()
    {
        // given 1
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                ConstructionPolicy.SINGLETON);

        // when 1
        InterfaceBasic result11 = testObject.resolve(InterfaceBasic.class);
        InterfaceBasic result12 = testObject.resolve(InterfaceBasic.class);

        // then 1
        Assertions.assertThat(result11).isNotNull().isInstanceOf(ClassConstructorDefault.class);
        Assertions.assertThat(result12)
                  .isNotNull()
                  .isSameAs(result11)
                  .isInstanceOf(ClassConstructorDefault.class);

        // given 2
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                ConstructionPolicy.CONSTRUCT);

        // when 2
        InterfaceBasic result21 = testObject.resolve(InterfaceBasic.class);
        InterfaceBasic result22 = testObject.resolve(InterfaceBasic.class);

        // then 2
        Assertions.assertThat(result21).isNotNull().isInstanceOf(ClassConstructorDefault.class);
        Assertions.assertThat(result22)
                  .isNotNull()
                  .isNotSameAs(result21)
                  .isInstanceOf(ClassConstructorDefault.class);
    }

    @Test
    public void registerType_WhenInheritanceFromInterfaceChangesClass_ThenInstanceIsDerived()
    {
        // given 1
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class);

        // when 1
        InterfaceBasic result1 = testObject.resolve(InterfaceBasic.class);

        // then 1
        Assertions.assertThat(result1).isNotNull().isInstanceOf(ClassConstructorDefault.class);

        // given 2
        testObject.registerType(InterfaceBasic.class,
                                ClassConstructorDefaultAndParameterized.class);

        // when 2
        InterfaceBasic result3 = testObject.resolve(InterfaceBasic.class);

        // then 2
        Assertions.assertThat(result3)
                  .isNotNull()
                  .isInstanceOf(ClassConstructorDefaultAndParameterized.class);
    }

    @Test
    public void registerType_WhenInheritanceFromAbstractClass_ThenInstanceIsDerived()
    {
        // given
        testObject.registerType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class);

        // when
        ClassBasicAbstract result = testObject.resolve(ClassBasicAbstract.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(ClassBasicInheritsFromAbstract.class);
    }

    @Test
    public void registerType_WhenInheritanceFromConcreteClass_ThenInstanceIsDerived()
    {
        // given
        testObject.registerType(ClassConstructorParameterized.class,
                                ClassConstructorSuperParameterized.class);

        // when
        ClassConstructorParameterized result =
                testObject.resolve(ClassConstructorParameterized.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(ClassConstructorSuperParameterized.class);
    }

    @Test
    public void registerType_WhenTwoStepsOfHierarchy_ThenInstanceIsDerived()
    {
        // given
        testObject.registerType(InterfaceBasic.class, ClassBasicAbstract.class)
                  .registerType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class);

        // when
        InterfaceBasic result = testObject.resolve(InterfaceBasic.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(ClassBasicInheritsFromAbstract.class);
    }

    // endregion
    // region registerInstance

    @Test
    public void registerInstance_WhenInterface_ThenRegisteredInstance()
    {
        // given
        ClassConstructorDefault instance = new ClassConstructorDefault();

        testObject.registerInstance(InterfaceBasic.class, instance);

        // when
        InterfaceBasic result = testObject.resolve(InterfaceBasic.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(ClassConstructorDefault.class)
                  .isSameAs(instance);
    }

    @Test
    public void registerInstance_WhenAbstractClass_ThenRegisteredInstance()
    {
        // given
        ClassBasicInheritsFromAbstract instance = new ClassBasicInheritsFromAbstract();

        testObject.registerInstance(ClassBasicAbstract.class, instance);

        // when
        ClassBasicAbstract result = testObject.resolve(ClassBasicAbstract.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(ClassBasicInheritsFromAbstract.class)
                  .isSameAs(instance);
    }

    @Test
    public void registerInstance_WhenSameConcreteClass_ThenRegisteredInstance()
    {
        // given
        ClassConstructorDefaultAndParameterized instance =
                new ClassConstructorDefaultAndParameterized();

        testObject.registerInstance(ClassConstructorDefaultAndParameterized.class, instance);

        // when
        ClassConstructorDefaultAndParameterized result =
                testObject.resolve(ClassConstructorDefaultAndParameterized.class);

        // then
        Assertions.assertThat(result).isNotNull().isSameAs(instance);
        Assertions.assertThat(result.getText()).isEqualTo(instance.getText());
    }

    @Test
    public void registerInstance_WhenDerivedConcreteClass_ThenRegisteredInstance()
    {
        // given
        ClassConstructorSuperParameterized instance = new ClassConstructorSuperParameterized();

        testObject.registerInstance(ClassConstructorParameterized.class, instance);

        // when
        ClassConstructorParameterized result =
                testObject.resolve(ClassConstructorParameterized.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isSameAs(instance)
                  .isInstanceOf(ClassConstructorSuperParameterized.class);
        Assertions.assertThat(result.getNumber()).isEqualTo(instance.getNumber());
    }

    @Test
    public void registerInstance_WhenInstanceIsNull_ThenNullInstanceException()
    {
        Assertions.assertThatThrownBy(
                () -> testObject.registerInstance(ClassConstructorDefaultAndParameterized.class,
                                                  null)).isInstanceOf(NullInstanceException.class);
    }

    // endregion
    // region resolve (@Dependency)

    @Test
    public void resolve_WhenMultipleAnnotatedConstructors_ThenMultipleAnnotatedConstructorsException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(ClassConstructorMultipleAnnotated.class))
                  .isInstanceOf(MultipleAnnotatedConstructorsException.class);
    }

    @Test
    public void resolve_WhenNoPublicConstructors_ThenNoSuitableConstructorException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassConstructorPrivate.class))
                  .isInstanceOf(NoSuitableConstructorException.class);
    }

    @Test
    public void resolve_WhenDependencySetterHasReturnType_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(ClassSetterIncorrectReturnType.class))
                  .isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void resolve_WhenDependencySetterHasNoParameters_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassSetterWithoutParameters.class))
                  .isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void resolve_WhenDependencySetterNameDoesNotStartWithSet_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassSetterIncorrectName.class))
                  .isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void resolve_WhenDependencySetterOnly_ThenInstanceIsResolved()
    {
        // given
        testObject.registerType(InterfaceSetter.class, ClassSetterSingle.class)
                  .registerType(InterfaceBasic.class, ClassConstructorDefault.class);

        // when
        InterfaceSetter result = testObject.resolve(InterfaceSetter.class);

        // then
        Assertions.assertThat(result).isNotNull().isInstanceOf(ClassSetterSingle.class);
        Assertions.assertThat(result.getBasicObject()).isNotNull();
    }

    @Test
    public void resolve_WhenDependencySetterAndConstructor_ThenInstanceIsResolved()
    {
        // given
        testObject.registerType(InterfaceSetter.class, ClassSetterConstructor.class)
                  .registerType(InterfaceBasic.class, ClassConstructorDefault.class);

        // when
        InterfaceSetter result = testObject.resolve(InterfaceSetter.class);

        // then
        Assertions.assertThat(result).isNotNull().isInstanceOf(ClassSetterConstructor.class);
        Assertions.assertThat(result.getBasicObject()).isNotNull();
    }

    @Test
    public void resolve_WhenDoubleDependencySetter_ThenIncorrectDependencySetterException()
    {
        // given
        testObject.registerType(InterfaceSetterMultipleParameters.class,
                                ClassSetterMultipleParameters.class);

        // then
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(InterfaceSetterMultipleParameters.class))
                  .isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void resolve_WhenMultipleDependencySetters_ThenInstanceIsResolved()
    {
        // given
        String string = "string";

        testObject.registerInstance(String.class, string)
                  .registerType(InterfaceSetterMultiple.class, ClassSetterMultiple.class)
                  .registerType(InterfaceBasic.class, ClassConstructorDefault.class)
                  .registerType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class);

        // when
        InterfaceSetterMultiple result = testObject.resolve(InterfaceSetterMultiple.class);

        // then
        Assertions.assertThat(result).isNotNull().isInstanceOf(ClassSetterMultiple.class);
        Assertions.assertThat(result.getBasicObject()).isNotNull();
        Assertions.assertThat(result.getStringObject()).isNotNull();
        Assertions.assertThat(result.getStringObject().getString()).isNotNull().isEqualTo(string);
    }

    // endregion
    // region buildUp

    @Test
    public void buildUp_WhenDependencySetterOnly_ThenInstanceIsBuiltUp()
    {
        // given
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class);

        InterfaceSetter instance = new ClassSetterSingle();

        // when
        InterfaceSetter result = testObject.buildUp(instance);

        // then
        Assertions.assertThat(result).isNotNull().isSameAs(instance);
        Assertions.assertThat(instance).isNotNull();
        Assertions.assertThat(result.getBasicObject()).isNotNull();
        Assertions.assertThat(instance.getBasicObject()).isNotNull();
    }

    @Test
    public void buildUp_WhenDependencySetterHasMultipleParameters_ThenIncorrectDependencySetterException()
    {
        // given
        InterfaceSetterMultipleParameters instance = new ClassSetterMultipleParameters();

        // then
        Assertions.assertThatThrownBy(() -> testObject.buildUp(instance))
                  .isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void buildUp_WhenMultipleDependencySetters_ThenInstanceIsBuiltUp()
    {
        // given
        String string = "string";
        InterfaceSetterMultiple instance = new ClassSetterMultiple();

        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class)
                  .registerType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class)
                  .registerInstance(String.class, string);

        // when
        InterfaceSetterMultiple result = testObject.buildUp(instance);

        // then
        Assertions.assertThat(result).isNotNull().isSameAs(instance);
        Assertions.assertThat(instance).isNotNull();
        Assertions.assertThat(result.getBasicObject()).isNotNull();
        Assertions.assertThat(instance.getBasicObject()).isNotNull();
        Assertions.assertThat(result.getStringObject()).isNotNull();
        Assertions.assertThat(instance.getStringObject()).isNotNull();
        Assertions.assertThat(result.getStringObject().getString()).isNotNull();
        Assertions.assertThat(instance.getStringObject().getString()).isNotNull();
        Assertions.assertThat(result.getStringObject().getString()).isEqualTo(string);
        Assertions.assertThat(instance.getStringObject().getString()).isEqualTo(string);
    }

    @Test
    public void buildUp_WhenComplexDependency_ThenInstanceIsBuiltUp()
    {
        // given
        String string = "string";

        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class)
                  .registerType(InterfaceDiamondLeft.class, ClassDiamondLeft.class)
                  .registerType(InterfaceDiamondTop.class, ClassDiamondTop.class)
                  .registerType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class)
                  .registerInstance(String.class, string);

        InterfaceDiamondLeft diamond1 = testObject.resolve(InterfaceDiamondLeft.class);
        InterfaceBasicStringGetter withString =
                testObject.resolve(InterfaceBasicStringGetter.class);
        InterfaceBasicComplexDependency instance =
                new ClassBasicComplexDependency(diamond1, withString);

        // when
        InterfaceBasicComplexDependency result = testObject.buildUp(instance);

        // then
        Assertions.assertThat(result).isNotNull().isSameAs(instance);
        Assertions.assertThat(instance).isNotNull();
        Assertions.assertThat(result.getBasicObject()).isNotNull();
        Assertions.assertThat(instance.getBasicObject()).isNotNull();
        Assertions.assertThat(result.getFirstObject()).isNotNull();
        Assertions.assertThat(instance.getFirstObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject()).isNotNull();
        Assertions.assertThat(instance.getSecondObject()).isNotNull();
        Assertions.assertThat(result.getFirstObject().getObject()).isNotNull();
        Assertions.assertThat(instance.getFirstObject().getObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject().getString()).isNotNull();
        Assertions.assertThat(instance.getSecondObject().getString()).isNotNull();
        Assertions.assertThat(result.getSecondObject().getString()).isEqualTo(string);
        Assertions.assertThat(instance.getSecondObject().getString()).isEqualTo(string);
    }

    // endregion
}
