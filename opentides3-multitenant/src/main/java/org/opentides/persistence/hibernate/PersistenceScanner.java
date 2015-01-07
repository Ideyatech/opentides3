/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opentides.persistence.hibernate;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.hibernate.MappingException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;
import org.springframework.util.ClassUtils;

public class PersistenceScanner implements PersistenceUnitPostProcessor {

    private static final String RESOURCE_PATTERN = "**/*.class";

    private String[] packagesToScan;

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private TypeFilter[] entityTypeFilters = new TypeFilter[] {
            new AnnotationTypeFilter(Entity.class, false),
            new AnnotationTypeFilter(Embeddable.class, false),
            new AnnotationTypeFilter(MappedSuperclass.class, false) };

    @Override
    public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
        String[] entities = scanPackages();
        for (String entity : entities) {
            pui.addManagedClassName(entity);
        }
    }

    /**
     * 
     * Set whether to use Spring-based scanning for entity classes in the
     * classpath instead of listing annotated classes explicitly.
     * 
     * <p>
     * 
     * Default is none. Specify packages to search for autodetection of your
     * entity classes in the classpath. This is analogous to
     * 
     * Spring's component-scan feature
     * (org.springframework.context.annotation.ClassPathBeanDefinitionScanner}).
     */

    public void setPackagesToScan(String[] packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    /**
     * 
     * Perform Spring-based scanning for entity classes.
     * 
     * @see #setPackagesToScan
     */

    protected String[] scanPackages() {
        Set<String> entities = new HashSet<String>();
        if (this.packagesToScan != null) {
            try {
                for (String pkg : this.packagesToScan) {
                    String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                            + ClassUtils.convertClassNameToResourcePath(pkg)
                            + RESOURCE_PATTERN;
                    Resource[] resources = this.resourcePatternResolver
                            .getResources(pattern);
                    MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(
                            this.resourcePatternResolver);
                    for (Resource resource : resources) {
                        if (resource.isReadable()) {
                            MetadataReader reader = readerFactory
                                    .getMetadataReader(resource);
                            String className = reader.getClassMetadata()
                                    .getClassName();
                            if (matchesFilter(reader, readerFactory)) {
                                entities.add(className);
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                throw new MappingException(
                        "Failed to scan classpath for unlisted classes", ex);
            }
        }
        return entities.toArray(new String[entities.size()]);
    }

    /**
     * 
     * Check whether any of the configured entity type filters matches the
     * current class descriptor contained in the metadata
     * 
     * reader.
     */

    private boolean matchesFilter(MetadataReader reader,
            MetadataReaderFactory readerFactory) throws IOException {
        if (this.entityTypeFilters != null) {
            for (TypeFilter filter : this.entityTypeFilters) {
                if (filter.match(reader, readerFactory)) {
                    return true;
                }
            }
        }
        return false;
    }
}
