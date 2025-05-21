package mx.dgtic.unam.simin.security;

import mx.dgtic.unam.simin.security.jwt.JwtFilter;
import mx.dgtic.unam.simin.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/index", "/home", "/login", "/login-error",
                                "/bootstrap/**", "/icons/**", "/image/**", "/theme/**",
                                "/public/**", "/contenido-educativo", "/acerca-de", "/contacto", "/que-es-simin"
                        ).permitAll()

                        // API pública para login con JWT
                        .requestMatchers("/api/auth/**").permitAll()

                        // Endpoints protegidos con JWT
                        .requestMatchers("/api/**").authenticated()

                        // Permisos para usuarios autenticados por sesión
                        .requestMatchers("/instrumentos/view/**").hasAnyRole("INVERSIONISTA", "ANALISTA", "ADMIN")
                        .requestMatchers("/reportes/simulacion/*/eliminar").hasAnyRole("ADMIN", "ANALISTA", "INVERSIONISTA")
                        .requestMatchers("/instrumentos/consulta/**").hasAnyRole("INVERSIONISTA", "ANALISTA")
                        .requestMatchers("/usuario/perfil/**").hasAnyRole("INVERSIONISTA", "ANALISTA")
                        .requestMatchers("/usuarios/**", "/instrumentos/**", "/reportes/list").hasRole("ADMIN")
                        .requestMatchers("/reportes/mis-reportes").hasRole("INVERSIONISTA")
                        .requestMatchers("/reportes/analista").hasRole("ANALISTA")
                        .requestMatchers("/reportes/simulacion/**").authenticated()
                        .requestMatchers("/carteras/**", "/simulaciones/**").hasAnyRole("ADMIN", "INVERSIONISTA", "ANALISTA")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("correo")
                        .passwordParameter("contrasena")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login-error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .key("siminkey")
                        .tokenValiditySeconds(86400)
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/error/403")
                );

        // filtro JWT antes del de Spring Security
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder encoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }
}