package ec.edu.monster.ws_eureka_bank_restful_java;

import ec.edu.monster.ws.CuentaResource;
import ec.edu.monster.ws.LoginResource;
import ec.edu.monster.ws.MovimientoResource;
import ec.edu.monster.ws_eureka_bank_restful_java.resources.JakartaEE10Resource;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.Set;

/**
 * Configures Jakarta RESTful Web Services for the application.
 * @author Juneau
 */
@ApplicationPath("resources")
public class JakartaRestConfiguration extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        // Registra explícitamente las clases de recursos REST
        resources.add(JakartaEE10Resource.class);
        resources.add(LoginResource.class);
        resources.add(CuentaResource.class);
        resources.add(MovimientoResource.class);
        return resources;
    }
}