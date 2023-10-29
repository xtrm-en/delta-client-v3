package me.xtrm.delta.client.utils.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * Source: <a href="https://github.com/Fox2Code/PuzzleModLoader/blob/master/src/main/java/net/puzzle_mod_loader/launch/Java9Fix.java">Fox2Code/PuzzleModLoader</a>
 * @author Fox2Code
 */
public class Java9Fix {
	
    private static final boolean java8 = System.getProperty("java.version").startsWith("1.");
    private static Object unsafe;
    private static Method getURLs;
    private static Method fieldOffset;
    private static Method fieldPutBool;
    private static Method fieldGetObject;
    private static Method getModule;
    private static long moduleDescOffset;
    private static long moduleDescOpenOffset;

    static {
        Class<?> URLClassPath;
        try {
            URLClassPath = Class.forName("sun.misc.URLClassPath");
        } catch (ClassNotFoundException e) {
            try {
                URLClassPath = Class.forName("jdk.internal.loader.URLClassPath");
            } catch (ClassNotFoundException ex) {
                throw new InternalError("Unable to find URLClassPath class", ex);
            }
        }
        if (!java8) try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = field.get(null);
            fieldOffset = unsafeClass.getDeclaredMethod("objectFieldOffset", Field.class);
            fieldPutBool = unsafeClass.getDeclaredMethod("putBoolean", Object.class, long.class, boolean.class);
            try {
                //Disable Java9+ Reflection Warnings
                Method putObjectVolatile = unsafeClass.getDeclaredMethod("putObjectVolatile", Object.class, long.class, Object.class);
                Method staticFieldOffset = unsafeClass.getDeclaredMethod("staticFieldOffset", Field.class);
                Class<?> loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
                Field loggerField = loggerClass.getDeclaredField("logger");
                Long offset = (Long) staticFieldOffset.invoke(unsafe, loggerField);
                putObjectVolatile.invoke(unsafe, loggerClass, offset, null);
            } catch (ReflectiveOperationException ignored) {
                System.out.println("[Java9Fix]: Unable to disable invalid access logging");
            }
            try {
                getModule = Class.class.getDeclaredMethod("getModule");
                Class<?> module = Class.forName("java.lang.Module");
                moduleDescOffset = (Long) fieldOffset.invoke(unsafe, module.getDeclaredField("descriptor"));
                Class<?> desc = Class.forName("java.lang.module.ModuleDescriptor");
                moduleDescOpenOffset = (Long) fieldOffset.invoke(unsafe, desc.getDeclaredField("open"));
                fieldGetObject = unsafeClass.getDeclaredMethod("getObject", Object.class, long.class);
                openModule(Class.class);
                openModule(URLClassPath);
            } catch (ReflectiveOperationException ignored) {
                System.out.println("[Java9Fix]: Unable to disable reflections checks");
            }
        } catch (ReflectiveOperationException e) {
            throw new InternalError("Couldn't init bypass!", e);
        }
        try {
            getURLs = URLClassPath.getDeclaredMethod("getURLs");
            Java9Fix.setAccessible(getURLs);
        } catch (ReflectiveOperationException e) {
            throw new InternalError("Couldn't init bypass!", e);
        }
    }

    private static Field access;
    private static long accessOffset;

    public static void setAccessible(AccessibleObject field) throws ReflectiveOperationException {
        if (java8) {
            field.setAccessible(true);
        } else {
            if (access == null) {
                access = AccessibleObject.class.getDeclaredField("override");
                accessOffset = (Long) fieldOffset.invoke(unsafe, access);
                setAccessible(access);
            }
            fieldPutBool.invoke(unsafe, field, accessOffset, true);
        }
    }

    public static void openModule(Class<?> cl) throws ReflectiveOperationException {
        if (!java8 && fieldGetObject != null) {
            Object tmp = getModule.invoke(cl);
            tmp = fieldGetObject.invoke(unsafe, tmp, moduleDescOffset);
            fieldPutBool.invoke(unsafe, tmp, moduleDescOpenOffset, true);
        }
    }

    public static URL[] getURLs(ClassLoader classLoader) throws ReflectiveOperationException {
        Class<?> ccl = classLoader.getClass();
        Field ucp = null;
        while (ucp == null && ccl != null) {
            try {
                ucp = ccl.getDeclaredField("ucp");
            } catch (ReflectiveOperationException e) {
                ccl = ccl.getSuperclass();
            }
        }
        if (ucp == null) {
            throw new NoSuchFieldException("Unable to find URLClassPath field in the current class loader");
        }
        Java9Fix.setAccessible(ucp);
        return  (URL[]) getURLs.invoke(ucp.get(classLoader));
    }

    public static boolean isJava8() {
        return java8;
    }

    public static boolean isJava9() {
        return !java8;
    }

}