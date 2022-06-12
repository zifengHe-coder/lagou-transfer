package com.lagou.edu.factory;

import com.lagou.edu.anno.Autowired;
import com.lagou.edu.anno.Component;
import com.lagou.edu.anno.Transactional;
import com.lagou.edu.utils.TransactionManager;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyFactory {

    public ProxyFactory() {}

    private static ProxyFactory proxyFactory = new ProxyFactory();

    public static ProxyFactory getInstance() {
        return proxyFactory;
    }
    /**
     * jdk 动态代理，增加事务处理
     * 委托对象必须实现接口
     * @param obj 委托对象
     * @return
     */
    public Object getTransactionProxy(Object obj) {
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Object result = null;
                        try {
                            //类上或成员变量含事务注解
                            System.out.println("添加事务处理" + method.getName());
                            TransactionManager.getInstance().beginTransaction();
                            result = method.invoke(obj,args);
                            TransactionManager.getInstance().commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                            TransactionManager.getInstance().rollback();
                            throw e;
                        }
                        return result;
                    }
                });
    }

    /**
     * cglib动态代理
     * 对象内部允许无实现接口
     * @param obj 委托对象
     * @return
     */
    public Object getCglibProxy(Object obj) {
        return Enhancer.create(obj.getClass(), new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                Object result = null;
                System.out.println("使用cglib代理");
                Transactional transactional = method.getAnnotation(Transactional.class);
                if (transactional != null) {
                    try {
                        //外部无事务注解，但方法上含注解情况
                        System.out.println("添加事务处理");
                        TransactionManager.getInstance().beginTransaction();
                        result = method.invoke(obj,objects);
                        TransactionManager.getInstance().commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                        TransactionManager.getInstance().rollback();
                        throw e;
                    }
                } else {
                    result = method.invoke(obj, objects);
                }
                return result;
            }
        });
    }
}
