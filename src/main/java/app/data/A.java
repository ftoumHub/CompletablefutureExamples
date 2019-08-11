package app.data;

import app.util.Util;

public class A extends Base {
    public A(String val){
        super(val);
    }
    public A(int nr){
        super(nr);
    }
    public static A reducer(A x, A y){
        Util.printThreadIdAndName();
        return new A(x.val + y.val);
    }
}