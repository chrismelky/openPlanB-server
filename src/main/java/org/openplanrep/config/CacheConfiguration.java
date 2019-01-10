package org.openplanrep.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(org.openplanrep.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(org.openplanrep.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(org.openplanrep.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(org.openplanrep.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(org.openplanrep.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(org.openplanrep.domain.OptionSet.class.getName(), jcacheConfiguration);
            cm.createCache(org.openplanrep.domain.OptionSet.class.getName() + ".optionValues", jcacheConfiguration);
            cm.createCache(org.openplanrep.domain.OptionValue.class.getName(), jcacheConfiguration);
            cm.createCache(org.openplanrep.domain.Attribute.class.getName(), jcacheConfiguration);
            cm.createCache(org.openplanrep.domain.AttributeValue.class.getName(), jcacheConfiguration);
            cm.createCache(org.openplanrep.domain.OrgUnitLevel.class.getName(), jcacheConfiguration);
            cm.createCache(org.openplanrep.domain.OrgUnitGroupSet.class.getName(), jcacheConfiguration);
            cm.createCache(org.openplanrep.domain.OrgUnitGroupSet.class.getName() + ".attributeValues", jcacheConfiguration);
            cm.createCache(org.openplanrep.domain.OrgUnitGroup.class.getName(), jcacheConfiguration);
            cm.createCache(org.openplanrep.domain.OrgUnitGroup.class.getName() + ".attributeValues", jcacheConfiguration);
            cm.createCache(org.openplanrep.domain.OrganisationUnit.class.getName(), jcacheConfiguration);
            cm.createCache(org.openplanrep.domain.OrganisationUnit.class.getName() + ".attributeValues", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
