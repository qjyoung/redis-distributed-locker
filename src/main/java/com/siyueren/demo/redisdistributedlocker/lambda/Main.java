package com.siyueren.demo.redisdistributedlocker.lambda;

import java.util.Arrays;
import java.util.List;

public class Main {
    
    public static void main(String[] args) {
        new Thread(() -> {
            task();
        });
        new Thread(Main::task).start();
        new Thread(System.out::println).start();
        foo(Main::task);
        List<Integer> nums = Arrays.asList(1, 2, 3);
        int sum = nums.stream().mapToInt(value -> value).sum();
        int sum2 = nums.stream().map(value -> value).reduce(Integer::sum).get();
        System.out.println("sum-" + sum);
        System.out.println("sum2-" + sum2);
    }
    
    public static void task() {
        System.out.println("线程任务");
    }
    
    public static void foo(CallBack callBack) {
        callBack.call();
        callBack.print();
        System.out.println("线程任务");
    }
}

@FunctionalInterface
interface CallBack {
    void call();
    
    default void print() {
        System.out.println("print");
    }
}

