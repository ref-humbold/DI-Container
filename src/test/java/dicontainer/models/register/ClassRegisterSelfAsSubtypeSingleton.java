package dicontainer.models.register;

import dicontainer.ConstructionPolicy;
import dicontainer.annotation.Register;

@Register(value = ClassRegisterSelfAsSubtypeSingleton.class, policy = ConstructionPolicy.SINGLETON)
public class ClassRegisterSelfAsSubtypeSingleton
{
}
