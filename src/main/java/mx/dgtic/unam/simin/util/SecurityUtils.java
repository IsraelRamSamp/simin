package mx.dgtic.unam.simin.util;

import lombok.extern.slf4j.Slf4j;
import mx.dgtic.unam.simin.dto.CarteraDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtils {

    public static String getCorreoUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correo = (auth != null) ? auth.getName() : null;
        log.debug("Usuario autenticado: {}", correo);
        return correo;
    }

    public static boolean isAdmin() {
        return hasRole("ADMIN");
    }

    public static boolean isInversionista() {
        return hasRole("INVERSIONISTA");
    }

    public static boolean isAnalista() {
        return hasRole("ANALISTA");
    }

    public static boolean hasRole(String rol) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities() == null) {
            return false;
        }
        boolean tieneRol = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + rol));
        log.debug("Tiene rol '{}': {}", rol, tieneRol);
        return tieneRol;
    }

    public static boolean puedeVerCartera(CarteraDto cartera) {
        if (cartera == null) return false;
        if (isAdmin()) return true;
        boolean puedeVer = getCorreoUsuarioAutenticado().equals(cartera.getCorreoUsuario());
        log.debug("Permiso para ver cartera [{}]: {}", cartera.getIdCartera(), puedeVer);
        return puedeVer;
    }

    public static boolean puedeEditarCartera(CarteraDto cartera) {
        if (isAdmin()) return true;
        String correo = getCorreoUsuarioAutenticado();
        boolean puedeEditar = correo != null && correo.equalsIgnoreCase(cartera.getCorreoUsuario());
        log.debug("Permiso para editar cartera [{}]: {}", cartera.getIdCartera(), puedeEditar);
        return puedeEditar;
    }

    public static boolean puedeEliminarCartera(CarteraDto cartera) {
        return puedeEditarCartera(cartera);
    }

    public static boolean puedeCrearCarteras() {
        boolean permiso = isAdmin() || isInversionista() || isAnalista();
        log.debug("Permiso para crear carteras: {}", permiso);
        return permiso;
    }
}