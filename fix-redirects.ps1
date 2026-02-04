$ErrorActionPreference = "Stop"

# Configuration
$KeycloakUrl = "http://localhost:8180"
$AdminUser = "admin"
$AdminPassword = "admin"
$Realm = "medinsight"
$ClientId = "medinsight-frontend"

Write-Host "Getting Admin Token..."
$tokenResponse = Invoke-RestMethod -Uri "$KeycloakUrl/realms/master/protocol/openid-connect/token" -Method Post -Body @{
    client_id     = "admin-cli"
    username      = $AdminUser
    password      = $AdminPassword
    grant_type    = "password"
}
$token = $tokenResponse.access_token

# Get Client UUID
$clients = Invoke-RestMethod -Uri "$KeycloakUrl/admin/realms/$Realm/clients?clientId=$ClientId" -Method Get -Headers @{ Authorization = "Bearer $token" }
if ($clients.Count -eq 0) {
    Write-Error "Client not found!"
}
$id = $clients[0].id

Write-Host "Updating Redirect URIs..."
$updateJson = @{
    redirectUris = @(
        "http://localhost:4200/*",
        "http://localhost:4200", 
        "http://localhost:4200/",
        "http://localhost:4200/dashboard"
    )
    webOrigins = @(
        "*", 
        "http://localhost:4200"
    )
} | ConvertTo-Json

Invoke-RestMethod -Uri "$KeycloakUrl/admin/realms/$Realm/clients/$id" -Method Put -Headers @{ Authorization = "Bearer $token"; "Content-Type" = "application/json" } -Body $updateJson

Write-Host "Redirect URIs Fixed!"
