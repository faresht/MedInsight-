$ErrorActionPreference = "Stop"

# Configuration
$KeycloakUrl = "http://localhost:8180"
$AdminUser = "admin"
$AdminPassword = "admin"
$Realm = "medinsight"

Write-Host "Waiting for Keycloak to be ready..."
# Simple wait loop
for ($i=0; $i -lt 30; $i++) {
    try {
        $res = Invoke-RestMethod -Uri "$KeycloakUrl/health/ready" -Method Get -ErrorAction SilentlyContinue
        if ($res.status -eq "UP") { break }
    } catch {}
    Start-Sleep -Seconds 2
}

Write-Host "Getting Admin Token..."
$tokenResponse = Invoke-RestMethod -Uri "$KeycloakUrl/realms/master/protocol/openid-connect/token" -Method Post -Body @{
    client_id     = "admin-cli"
    username      = $AdminUser
    password      = $AdminPassword
    grant_type    = "password"
}
$token = $tokenResponse.access_token

Write-Host "Activating 'medinsight' Theme..."
$updateJson = @{
    loginTheme = "medinsight"
} | ConvertTo-Json

Invoke-RestMethod -Uri "$KeycloakUrl/admin/realms/$Realm" -Method Put -Headers @{ Authorization = "Bearer $token"; "Content-Type" = "application/json" } -Body $updateJson

Write-Host "Theme Activated! Login page is now branded."
