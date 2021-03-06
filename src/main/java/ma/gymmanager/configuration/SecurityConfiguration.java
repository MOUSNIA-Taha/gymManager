package ma.gymmanager.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ma.gymmanager.Service.IUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private IUserService userService;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/").permitAll().antMatchers("/login").permitAll().antMatchers("/index")
        .hasAnyAuthority("ADHERENT", "COACH","ADMIN","COACH","SUPERADMIN")
        .antMatchers("/abonnements/**", "/adherents/**", "/entraineurs/**",
        "/groupes/**",
        "/plannings/", "/sports/**")
        .hasAuthority("ADMIN").antMatchers("/superadmin/**").hasAuthority("SUPERADMIN")
        .antMatchers("/plannings/consultation/**").hasAnyAuthority("ADHERENT",
        "COACH").anyRequest()
        .authenticated().and().csrf().disable().formLogin().loginPage("/login").failureUrl("/login?error=true")
        .defaultSuccessUrl("/index",
        true).usernameParameter("username").passwordParameter("password").and()
        .logout().logoutRequestMatcher(new
        AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").and()
        .exceptionHandling().accessDeniedPage("/access-denied");
        // http.authorizeRequests().antMatchers("/").permitAll().antMatchers("/login").permitAll().and().csrf().disable()
        //         .formLogin().loginPage("/login").failureUrl("/login?error=true").defaultSuccessUrl("/index", true)
        //         .usernameParameter("username").passwordParameter("password").and().logout()
        //         .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").and()
        //         .exceptionHandling().accessDeniedPage("/access-denied");
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/img/**",
                "/json/**", "/vendor/**", "/bootstrap/**", "/fontawesome-free/**", "/jquery/**", "/scss/**");
    }
}