package me.xtrm.xeon.loader.utils.classData;

public interface ClassData {
    String getName();
    ClassData getSuperclass();
    boolean isAssignableFrom(ClassData clData);
    boolean isInterface();
    boolean isFinal();
    boolean isPublic();
    boolean isCustom();
}
