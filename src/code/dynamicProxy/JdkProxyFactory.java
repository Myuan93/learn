package code.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author: zhouk
 * @date: 2022/2/15 9:51 下午
 * @description:使用proxy实现方法的拦截
 */
public class JdkProxyFactory implements InvocationHandler {

    private Object targetObject;

    public Object createProxyInstance(Object targetObject) {
        this.targetObject = targetObject;
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),
                targetObject.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //方法执行前处理...
        Object result = method.invoke(targetObject, args);
        //方法执行后处理...
        return result;
    }
}
