package code.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author: zhouk
 * @date: 2022/2/15 9:27 下午
 * @description:动态代码代理的两种实现
 */
public class DynamicProxy {

    /**
     * 生成对象的代理对象，对被代理对象进行所有方法增强
     *
     * @param obj 原始对象
     * @return 被代理对象
     */
    public static Object getObject(final Object obj) {
        /**
         * 使用JDK动态代理实现创建对象的代理对象
         * 参数一：类加载器
         * 参数二：对象的接口
         * 参数三：调用处理器，代理对象中的方法被调用，都会在执行方法。对所有被代理对象的方法进行拦截
         */
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //方法执行前...
                //执行方法的调用
                Object object = method.invoke(obj, args);
                //方法执行后...
                return object;
            }
        });
    }

    /**
     * 使用CGLib创建动态代理对象
     * 第三方提供的的创建代理对象的方式CGLib
     * 被代理对象不能用final修饰
     * 使用的是Enhancer类创建代理对象
     *
     * @param obj
     * @return
     */
//    public static Object getObjectByCGLib(final Object obj) {
////        Object proxyObj = Enhancer.create(obj.getClass(), new MethodInterceptor() {
////            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
////                //方法执行前
////                long startTime = System.currentTimeMillis();
////                //执行方法的调用
////                Object invokeObject = method.invoke(obj, objects);
////                //方法执行后...
////                return invokeObject;
////            }
////        });
////        return proxyObj;
//    }

}
