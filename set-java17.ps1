$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot"
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path
Write-Host "Java 17 active pour cette session" -ForegroundColor Green
mvn --version