package com.lagou.edu.factory;

import com.lagou.edu.anno.*;
import com.lagou.edu.utils.SupportScan;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {

    private static Map<String, Object> map = new ConcurrentHashMap<>();

    private static Map<String, Class<?>> classIdMap = new HashMap<>();

    public static Map<String, Object> getMap() {
        return map;
    }

    public static void setMap(Map<String, Object> map) {
        BeanFactory.map = map;
    }

    static {

        try {
            Set<Class<?>> classInfos = SupportScan.classInfos();
            System.out.println(classInfos);
            for (Class<?> classInfo : classInfos) {
                String className = classInfo.getName().substring(classInfo.getName().lastIndexOf(".") + 1);
                String defaultId = className.substring(0, 1).toLowerCase(Locale.ROOT) + className.substring(1);//AccountDao -> accountDao
                String id = null;
                //所有类方法均用cglib动态代理，并处理方法中可能出现@Transaction的情况

                if (!className.contains("$")) {
                    Object obj = classInfo.newInstance();
                    Component component = classInfo.getAnnotation(Component.class);
                    if (component != null) {
                        //value为空，则取类名(首字母小写)
                        id = "".equals(component.value()) ? defaultId : component.value();
                        map.put(id, obj);
                        classIdMap.put(id, classInfo);
                        continue;
                    }
                    Repository repository = classInfo.getAnnotation(Repository.class);
                    if (repository != null) {
                        //value为空，则取类名(首字母小写)
                        id = "".equals(repository.value()) ? defaultId : repository.value();
                        map.put(id, obj);
                        classIdMap.put(id, classInfo);
                        continue;
                    }
                    Service service = classInfo.getAnnotation(Service.class);
                    if (service != null) {
                        //value为空，则取类名(首字母小写)
                        id = "".equals(service.value()) ? defaultId : service.value();
                        map.put(id, obj);
                        classIdMap.put(id, classInfo);
                        continue;
                    }

                }
            }

            //只有类含相关注解的才实现依赖注入
            for (Map.Entry<String, Class<?>> entry : classIdMap.entrySet()) {
                String id = entry.getKey();
                Class<?> clazz = entry.getValue();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    if (autowired != null) {
                        Object parentObject = map.get(id);
                        Method[] methods = parentObject.getClass().getMethods();
                        for (Method method : methods) {
                            if (method.getName().equalsIgnoreCase("set"+field.getName())) {
                                //字段上
                                //判断注入类本身是否含事务注解
                                Class<?> subClass = classIdMap.get(field.getName());
                                if (subClass.getAnnotation(Transactional.class)!=null) {
                                    method.invoke(parentObject,ProxyFactory.getInstance().getTransactionProxy(map.get(field.getName())));
                                } else {
                                    method.invoke(parentObject,map.get(field.getName()));
                                }
                            }
                        }
                    }
                }
                //bean本身含事务
                if (clazz.getAnnotation(Transactional.class) != null) map.put(id, ProxyFactory.getInstance().getTransactionProxy(map.get(id)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        InputStream resourceAsStream = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
//        SAXReader saxReader = new SAXReader();
//        try {
//            Document document = saxReader.read(resourceAsStream);
//            Element rootElement = document.getRootElement();
//            List<Element> beanList = rootElement.selectNodes("//bean");
//            for (int i = 0; i < beanList.size(); i++) {
//                Element element = beanList.get(i);
//                String id = element.attributeValue("id");
//                String clazz = element.attributeValue("class");
//                Class<?> aClass = Class.forName(clazz);
//                Object o = aClass.newInstance();
//                map.put(id, o);
//            }
//            List<Element> propertyList = rootElement.selectNodes("//property");
//            for (int i = 0; i < propertyList.size(); i++) {
//                Element element = propertyList.get(i);
//                String name = element.attributeValue("name");
//                String ref = element.attributeValue("ref");
//                Element parent = element.getParent();
//                String id = parent.attributeValue("id");
//                Object parentObject = map.get(id);
//                Method[] methods = parentObject.getClass().getMethods();
//                for (Method method : methods) {
//                    if (method.getName().equalsIgnoreCase("set"+name)) {
//                        method.invoke(parentObject,map.get(ref));
//                    }
//                }
//            }
//        } catch (DocumentException | ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
    }

    public static void init() {
        try {
            Set<Class<?>> classInfos = SupportScan.classInfos();
            System.out.println(classInfos);
            for (Class<?> classInfo : classInfos) {
                String className = classInfo.getName().substring(classInfo.getName().lastIndexOf(".") + 1);
                String defaultId = className.substring(0, 1).toLowerCase(Locale.ROOT) + className.substring(1);
                String id = null;
                //所有类方法均用cglib动态代理，并处理方法中可能出现@Transaction的情况

                if (!className.contains("$")) {
                    Object obj = classInfo.newInstance();
                    Component component = classInfo.getAnnotation(Component.class);
                    if (component != null) {
                        //value为空，则取类名(首字母小写)
                        id = "".equals(component.value()) ? defaultId : component.value();
                        map.put(id, obj);
                        classIdMap.put(id, classInfo);
                        continue;
                    }
                    Repository repository = classInfo.getAnnotation(Repository.class);
                    if (repository != null) {
                        //value为空，则取类名(首字母小写)
                        id = "".equals(repository.value()) ? defaultId : repository.value();
                        map.put(id, obj);
                        classIdMap.put(id, classInfo);
                        continue;
                    }
                    Service service = classInfo.getAnnotation(Service.class);
                    if (service != null) {
                        //value为空，则取类名(首字母小写)
                        id = "".equals(service.value()) ? defaultId : service.value();
                        map.put(id, obj);
                        classIdMap.put(id, classInfo);
                        continue;
                    }

                }
            }

            //只有类含相关注解的才实现依赖注入
            for (Map.Entry<String, Class<?>> entry : classIdMap.entrySet()) {
                String id = entry.getKey();
                Class<?> clazz = entry.getValue();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    if (autowired != null) {
                        Object parentObject = map.get(id);
                        Method[] methods = parentObject.getClass().getMethods();
                        for (Method method : methods) {
                            if (method.getName().equalsIgnoreCase("set"+field.getName())) {
                                //字段上
                                //判断注入类本身是否含事务注解
                                Class<?> subClass = classIdMap.get(field.getName());
                                if (subClass.getAnnotation(Transactional.class)!=null) {
                                    method.invoke(parentObject,subClass.getInterfaces().getClass().cast(ProxyFactory.getInstance().getTransactionProxy(map.get(field.getName()))));
                                } else {
                                    method.invoke(parentObject,map.get(field.getName()));
                                }
                            }
                        }
                    }
                }
                //bean本身含事务
                if (clazz.getAnnotation(Transactional.class) != null) map.put(id, ProxyFactory.getInstance().getTransactionProxy(map.get(id)));
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getBean(String id) {
        return map.get(id);
    }
}
