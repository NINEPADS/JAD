package Test;

import java.lang.reflect.Array;
import java.nio.channels.Pipe.SourceChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

enum 星期 {
    天("星期天", 0), 一("星期一", 1), 二("星期二", 2), 三("星期三", 3), 四("星期四", 4), 五("星期五", 5), 六("星期六", 6);
    String 名; int 序;
    private 星期(String n, int i) {
        名 = n;
        序 = i;
    }
    public static void main(String[] args) {
        System.out.println(星期.天.名);
        System.out.println(星期.天);
        System.out.println(天.名);
        System.out.println(天);
        int[] arr = new int[1000];
        m(new ArrayList<B>());
       
    }
    static void m(List<? extends A> list){}
    static class T {
        public void set() {
            星期 x = new 星期("a", 2);
        }
    }
}
class A{
    int i;
    String str;
    A() {
        i = 1;
        str = "A";
    }
    static class c {
        A a = new A();
        void set() {
            System.out.println(a.i);
        }
    }
}
class B extends A{

}