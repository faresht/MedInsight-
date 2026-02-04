import { KeycloakService } from 'keycloak-angular';

export function initializeKeycloak(keycloak: KeycloakService) {
    return () =>
        keycloak.init({
            config: {
                url: 'http://localhost:8180',
                realm: 'medinsight',
                clientId: 'medinsight-frontend'
            },
            initOptions: {
                checkLoginIframe: false
            },
            enableBearerInterceptor: true,
            bearerExcludedUrls: ['/assets']
        });
}
