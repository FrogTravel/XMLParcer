package froggy;

/**
 * Created by ekaterina on 01.08.16.
 */
public class MyClass {
    public static final int someVariable = 231;
    private String verySpecialWords = "Tralala";
    public static final int SIZE = 32;


    public int getSomeNumber(){
        return 526;
    }

    public static String getSomeWord(int a){
        if(a>6)
            return "More than six";
        else
            return "Less";
    }

    private static int get(){
        int ther = 2;
        try{
            ther = 5;
        }catch (NullPointerException e){
        }
        return ther;
    }


    private boolean getBool(){
        return true;
    }

}
