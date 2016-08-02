package froggy;

import com.sun.xml.internal.ws.api.model.ExceptionType;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.*;

/**
 * Created by ekaterina on 01.08.16.
 */
public class Main {
    String result = "";
    MyClass a;
    Class aClass;

    public static void main(String[] args){
        Main m = new Main();
        m.go();
    }

    public void go(){
        a = new MyClass();
        aClass = a.getClass();

        result += "<class>";

            className();
            staticAttributes();
            staticMethods();
            object();

        result += "\n</class>";
        System.out.println(result);
        writeFile(result);
    }

    private void className() {
        result += "\n\t<name>";
        result += aClass.getName();
        result += "</name>";
    }

    private void staticAttributes(){
        //Получаем поля
        Field[] allFields = aClass.getDeclaredFields();
        for (Field field : allFields) {//Итератор через все поля
            if(field.toString().contains("static")) {//Только для статика
                result += "\n\t<static_attributes>";

                result += "\n\t\t<name>" + field.getName() + "</name>";
                field.setAccessible(true);

                try {
                    result += "\n\t\t<value>" + field.get(aClass) + "</value>";//TODO
                }catch (IllegalAccessException e){
                    result += "\n\t\t<value>"  +  "PRIVATE" + "</value>";
                }

                result += "\n\t\t<access>" + getAccess(field.toString()) +"</access>";

                result += "\n\t</static_attributes>";
            }
        }
    }

    private void staticMethods() {
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            if(method.toString().contains("static")) {
                result += "\n\t<static_methods>";

                result += "\n\t\t<name>"+ method.getName() +"</name>";
                result += "\n\t\t<return_type>"+ method.getReturnType() +"</return_type>";

                staticArguments(method);
                staticExceptions(method);

                result += "\n\t\t<access>" + getAccess(method.toString()) + "</access>";

                result += "\n\t</static_methods>";
            }
        }
    }

    private void staticArguments(Method method) {
        objectMethodArguments(method);
    }


    private void staticExceptions(Method method) {
        objectMethodExceptions(method);
    }

    private void object() {
        result += "\n\t<object>";

            objectFields();
            objectMethods();

        result += "\n\t</object>";
    }

    private void objectFields(){
        //Получаем поля
        Field[] allFields = aClass.getDeclaredFields();
        for (Field field : allFields) {//Итератор через все поля
            if(!field.toString().contains("static")) {//Только для статика
                result += "\n\t\t<field>";
                result += "\n\t\t\t<name>" + field.getName() + "</name>";

                try {
                    result += "\n\t\t\t<value>" + field.get(aClass) + "</value>";//TODO не отдает private значения
                } catch (IllegalAccessException e) {
                    result += "\n\t\t\t<value>" + "Private variable" + "</value>";
                }
                result += "\n\t\t\t<access>" + getAccess(field.toString())  + "</access>";//TODO
                result += "\n\t\t</field>";
            }
        }
    }

    private void objectMethods() {
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            if (!method.toString().contains("static")) {
                result += "\n\t\t<methods>";
                result += "\n\t\t\t<name>" + method.getName() + "</name>";
                result += "\n\t\t\t<return_type>" + method.getReturnType().getName() + "</return_type>";

                objectMethodArguments(method);
                objectMethodExceptions(method);

                result += "\n\t\t\t<access>" + getAccess(method.toString()) + "</access>";

                result += "\n\t\t</methods>";
            }
        }
    }

    private void objectMethodExceptions(Method method) {
        result += "\n\t\t\t<exceptions>";
            Class[] exceptions = method.getExceptionTypes();
        System.out.println(exceptions);
            for (Class exception : exceptions) {
                result += "\n\t\t\t\t<name>" + exception.getName() + "</name>";
            }

        result += "\n\t\t\t</exceptions>";
    }

    private void objectMethodArguments(Method method) {
        Class[] paramTypes = method.getParameterTypes();
        int count = 0;
        for (Class paramType : paramTypes) {
            String arg = "arg" + count;
            result += "\n\t\t\t<arguments>";
            result += "\n\t\t\t\t<name>" + arg + "</name>";
            result += "\n\t\t\t\t<type>" + paramType.getName() + "</type>";
            result += "\n\t\t\t</arguments>";
            count++;
        }
    }

    public String getAccess(String modificators) {
        String access = "package protected";

        if(modificators.contains("public"))
            access = "public";

        if(modificators.contains("protected"))
            access = "protected";

        if(modificators.contains("private"))
            access = "private";

        return access;
    }

    private static void writeFile(String paramString)
    {
        try
        {
            BufferedWriter localBufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("parsed.xml"), "ascii"));

            localBufferedWriter.write(paramString);
            localBufferedWriter.close();
        }
        catch (IOException localIOException) {}
    }
}
