package app.config

import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests {
            it
                    .antMatchers("/css/**", "/index").permitAll()
                    .antMatchers("/user/**").hasRole("USER")
        }.formLogin {
            it
                    .loginPage("/login")
                    .failureUrl("/login-error")
        }
    }

    @Bean
    fun userDetailsServiceInMem(): UserDetailsService {
        val userDetails = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build()
        return InMemoryUserDetailsManager(userDetails)
    }
}