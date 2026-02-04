$ErrorActionPreference = "Stop"

# Configuration
$KeycloakUrl = "http://localhost:8180"
$AdminUser = "admin"
$AdminPassword = "admin"
$Realm = "medinsight"
$ClientId = "medinsight-frontend"
$BackendClientId = "medinsight-backend"

Write-Host "Getting Admin Token..."
$tokenResponse = Invoke-RestMethod -Uri "$KeycloakUrl/realms/master/protocol/openid-connect/token" -Method Post -Body @{
    client_id     = "admin-cli"
    username      = $AdminUser
    password      = $AdminPassword
    grant_type    = "password"
}
$token = $tokenResponse.access_token

Write-Host "Creating Realm '$Realm'..."
$realmJson = @{
    id = $Realm
    realm = $Realm
    enabled = $true
} | ConvertTo-Json
try {
    Invoke-RestMethod -Uri "$KeycloakUrl/admin/realms" -Method Post -Headers @{ Authorization = "Bearer $token"; "Content-Type" = "application/json" } -Body $realmJson
} catch {
    Write-Host "Realm might already exist, continuing..."
}

Write-Host "Creating Frontend Client '$ClientId'..."
$clientJson = @{
    clientId = $ClientId
    enabled = $true
    publicClient = $true
    redirectUris = @("http://localhost:4200/*")
    webOrigins = @("+")
    standardFlowEnabled = $true
    directAccessGrantsEnabled = $true
} | ConvertTo-Json
try {
    Invoke-RestMethod -Uri "$KeycloakUrl/admin/realms/$Realm/clients" -Method Post -Headers @{ Authorization = "Bearer $token"; "Content-Type" = "application/json" } -Body $clientJson
} catch {
    Write-Host "Client might already exist, continuing..."
}

Write-Host "Creating Backend Client '$BackendClientId'..."
$beClientJson = @{
    clientId = $BackendClientId
    enabled = $true
    publicClient = $false
    clientAuthenticatorType = "client-secret"
    secret = "backend-secret"
    serviceAccountsEnabled = $true
    standardFlowEnabled = $false
} | ConvertTo-Json
try {
    Invoke-RestMethod -Uri "$KeycloakUrl/admin/realms/$Realm/clients" -Method Post -Headers @{ Authorization = "Bearer $token"; "Content-Type" = "application/json" } -Body $beClientJson
} catch {
    Write-Host "Backend Client might already exist, continuing..."
}

Write-Host "Creating Test User 'doctor1'..."
$userJson = @{
    username = "doctor1"
    enabled = $true
    email = "doctor1@medinsight.com"
    firstName = "John"
    lastName = "House"
    credentials = @(@{
        type = "password"
        value = "password"
        temporary = $false
    })
} | ConvertTo-Json
try {
    Invoke-RestMethod -Uri "$KeycloakUrl/admin/realms/$Realm/users" -Method Post -Headers @{ Authorization = "Bearer $token"; "Content-Type" = "application/json" } -Body $userJson
} catch {
    Write-Host "User doctor1 might already exist..."
}

Write-Host "Creating Test User 'patient1'..."
$patJson = @{
    username = "patient1"
    enabled = $true
    email = "patient1@medinsight.com"
    firstName = "Jane"
    lastName = "Patient"
    credentials = @(@{
        type = "password"
        value = "password"
        temporary = $false
    })
} | ConvertTo-Json
try {
    Invoke-RestMethod -Uri "$KeycloakUrl/admin/realms/$Realm/users" -Method Post -Headers @{ Authorization = "Bearer $token"; "Content-Type" = "application/json" } -Body $patJson
} catch {
    Write-Host "User patient1 might already exist..."
}

Write-Host "Creating Test User 'admin1'..."
$admJson = @{
    username = "admin1"
    enabled = $true
    email = "admin1@medinsight.com"
    firstName = "Admin"
    lastName = "User"
    credentials = @(@{
        type = "password"
        value = "password"
        temporary = $false
    })
} | ConvertTo-Json
try {
    Invoke-RestMethod -Uri "$KeycloakUrl/admin/realms/$Realm/users" -Method Post -Headers @{ Authorization = "Bearer $token"; "Content-Type" = "application/json" } -Body $admJson
} catch {
    Write-Host "User admin1 might already exist..."
}

# Create Roles
Write-Host "Creating Roles..."
foreach ($roleName in @("MEDECIN", "PATIENT", "ADMIN")) {
    $rJson = @{ name = $roleName } | ConvertTo-Json
    try {
        Invoke-RestMethod -Uri "$KeycloakUrl/admin/realms/$Realm/roles" -Method Post -Headers @{ Authorization = "Bearer $token"; "Content-Type" = "application/json" } -Body $rJson
    } catch {
        Write-Host "Role $roleName might already exist..."
    }
}

# Assign Roles
function Assign-Role ($username, $roleName) {
    try {
        $u = Invoke-RestMethod -Uri "$KeycloakUrl/admin/realms/$Realm/users?username=$username" -Method Get -Headers @{ Authorization = "Bearer $token" }
        if ($u.Count -gt 0) {
            $uid = $u[0].id
            $r = Invoke-RestMethod -Uri "$KeycloakUrl/admin/realms/$Realm/roles/$roleName" -Method Get -Headers @{ Authorization = "Bearer $token" }
            $mapBody = @($r) | ConvertTo-Json
            Invoke-RestMethod -Uri "$KeycloakUrl/admin/realms/$Realm/users/$uid/role-mappings/realm" -Method Post -Headers @{ Authorization = "Bearer $token"; "Content-Type" = "application/json" } -Body $mapBody
            Write-Host "Assigned $roleName to $username"
        }
    } catch {
        Write-Host "Failed to assign $roleName to $username"
    }
}

Assign-Role "doctor1" "MEDECIN"
Assign-Role "patient1" "PATIENT"
Assign-Role "admin1" "ADMIN"

Write-Host "Keycloak Configuration Complete!"
