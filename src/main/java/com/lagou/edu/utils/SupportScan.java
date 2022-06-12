package com.lagou.edu.utils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * SPRING扫描包下面的类
 *
 * @project common-utils
 * @fileName Scaner.java
 * @Description
 * @author light-zhang
 * @date 2019年5月5日
 * @version 1.0.0
 */
public class SupportScan implements ResourceLoaderAware {
    /**
     * Spring容器注入
     */
    private ResourceLoader resourceLoader;

    private ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    private static final String FULLTEXT_SACN_PACKAGE_PATH = "fulltext.scan.package";

    /**
     * set注入对象
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 利用spring提供的扫描包下面的类信息,再通过classfrom反射获得类信息
     *
     * @param scanPath
     * @return
     * @throws IOException
     */
    public Set<Class<?>> doScan(String scanPath) throws IOException {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                .concat(ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(scanPath))
                        .concat("/**/*.class"));
        Resource[] resources = resolver.getResources(packageSearchPath);
        MetadataReader metadataReader = null;
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                metadataReader = metadataReaderFactory.getMetadataReader(resource);
                try {
                    if (metadataReader.getClassMetadata().isConcrete()) {// 当类型不是抽象类或接口在添加到集合
                        classes.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }

    /**
     * 指定包下面的类信息
     *
     * @return 多个类信息
     */
    public static Set<Class<?>> classInfos() {
        try {
            String scanPath = "com.lagou.edu";
            return new SupportScan().doScan(scanPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}