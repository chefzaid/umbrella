package io.simpleit.umbrella.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, io.simpleit.umbrella.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, io.simpleit.umbrella.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, io.simpleit.umbrella.domain.User.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.Authority.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.User.class.getName() + ".authorities");
            createCache(cm, io.simpleit.umbrella.domain.Address.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.Contact.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.Prospect.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.AppUser.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.AppUser.class.getName() + ".preferences");
            createCache(cm, io.simpleit.umbrella.domain.Employee.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.Employee.class.getName() + ".projects");
            createCache(cm, io.simpleit.umbrella.domain.Employee.class.getName() + ".timesheets");
            createCache(cm, io.simpleit.umbrella.domain.Employee.class.getName() + ".expenseNotes");
            createCache(cm, io.simpleit.umbrella.domain.Employee.class.getName() + ".documents");
            createCache(cm, io.simpleit.umbrella.domain.Employee.class.getName() + ".activityReports");
            createCache(cm, io.simpleit.umbrella.domain.Employee.class.getName() + ".paySlips");
            createCache(cm, io.simpleit.umbrella.domain.IdDocument.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.EmploymentContract.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.EmploymentContract.class.getName() + ".documents");
            createCache(cm, io.simpleit.umbrella.domain.EmploymentContract.class.getName() + ".amendments");
            createCache(cm, io.simpleit.umbrella.domain.ServiceContract.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.ServiceContract.class.getName() + ".documents");
            createCache(cm, io.simpleit.umbrella.domain.ServiceContract.class.getName() + ".amendments");
            createCache(cm, io.simpleit.umbrella.domain.Project.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.Client.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.Client.class.getName() + ".contacts");
            createCache(cm, io.simpleit.umbrella.domain.Formula.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.Document.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.TimeSheet.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.TimeSheet.class.getName() + ".lines");
            createCache(cm, io.simpleit.umbrella.domain.TimeSheetLine.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.ExpenseNote.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.ExpenseNote.class.getName() + ".expenses");
            createCache(cm, io.simpleit.umbrella.domain.Expense.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.MileageAllowance.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.Invoice.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.Invoice.class.getName() + ".lines");
            createCache(cm, io.simpleit.umbrella.domain.InvoiceLine.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.Wallet.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.ActivityReport.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.ActivityReport.class.getName() + ".operations");
            createCache(cm, io.simpleit.umbrella.domain.Operation.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.PaySlip.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.Enterprise.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.Enterprise.class.getName() + ".parameters");
            createCache(cm, io.simpleit.umbrella.domain.Enterprise.class.getName() + ".documents");
            createCache(cm, io.simpleit.umbrella.domain.Parameter.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.ParameterGroup.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.ExpenseType.class.getName());
            createCache(cm, io.simpleit.umbrella.domain.Notification.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
