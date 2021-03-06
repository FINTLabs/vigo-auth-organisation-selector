package no.vigoiks.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.pool2.factory.PoolConfig;
import org.springframework.ldap.pool2.factory.PooledContextSource;
import org.springframework.ldap.pool2.validation.DefaultDirContextValidator;
import org.springframework.ldap.transaction.compensating.manager.TransactionAwareContextSourceProxy;

@Configuration
public class LdapConfiguration {

    private final Environment env;

    public LdapConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(env.getRequiredProperty("fint.ldap.url"));
        contextSource.setUserDn(env.getRequiredProperty("fint.ldap.user"));
        contextSource.setPassword(env.getRequiredProperty("fint.ldap.password"));
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }

}
