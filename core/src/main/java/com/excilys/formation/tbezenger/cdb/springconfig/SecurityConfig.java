package com.excilys.formation.tbezenger.cdb.springconfig;


import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {	

  @Autowired
  private DataSource dataSource;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    auth.jdbcAuthentication().dataSource(dataSource)
        .usersByUsernameQuery("select username, password, enabled"
                + " from users where username=?")
        .authoritiesByUsernameQuery("select username, authority "
                + "from authorities where username=?")
    	.passwordEncoder(new BCryptPasswordEncoder());
  }
  

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.authorizeRequests().antMatchers("/editComputer").hasAnyRole("ADMIN","USER")
    						.antMatchers("/dashboard","/addComputer").hasAnyRole("USER","ADMIN")
    				        .anyRequest().authenticated()
	    .and().formLogin().defaultSuccessUrl("/dashboard").permitAll()
	    .and().logout()
			  .logoutUrl("/logout")
			  .logoutSuccessUrl("/login?logout").permitAll();
  }  
  
}

