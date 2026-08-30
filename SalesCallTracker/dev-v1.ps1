$ErrorActionPreference="Continue"

$javaCandidates=@(
    "$env:ProgramFiles\Android\Android Studio\jbr",
    "$env:LOCALAPPDATA\Programs\Android Studio\jbr"
)
$java=$javaCandidates | Where-Object { Test-Path "$_\bin\java.exe" } | Select-Object -First 1
if(!$java){ Write-Host "Android Studio JDK not found" -ForegroundColor Red; exit 1 }

$env:JAVA_HOME=$java
$env:Path="$env:JAVA_HOME\bin;$env:Path"

Write-Host "===== SALES CALL TRACKER DEV PIPELINE =====" -ForegroundColor Cyan

Write-Host "`n[1] Git status" -ForegroundColor Yellow
git branch --show-current
git status --short

Write-Host "`n[2] Tests" -ForegroundColor Yellow
.\gradlew.bat test --no-daemon
if($LASTEXITCODE -ne 0){
    Write-Host "TEST FAILED - no commit created." -ForegroundColor Red
    exit $LASTEXITCODE
}

Write-Host "`n[3] Building Debug APK" -ForegroundColor Yellow
.\gradlew.bat :app:assembleDebug --no-daemon
if($LASTEXITCODE -ne 0){
    Write-Host "BUILD FAILED - no commit created." -ForegroundColor Red
    exit $LASTEXITCODE
}

$apk="$PWD\app\build\outputs\apk\debug\app-debug.apk"

Write-Host "`n[4] APK READY" -ForegroundColor Green
Write-Host $apk

$adb="$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"

if(Test-Path $adb){
    Write-Host "`n[5] Installing APK" -ForegroundColor Yellow
    & $adb install -r $apk

    if($LASTEXITCODE -eq 0){
        Write-Host "Launching SalesCallTracker..." -ForegroundColor Green
        & $adb shell am start -n "com.example.salescalltracker/.MainActivity"
    }
}

Write-Host "`n[6] Git checkpoint" -ForegroundColor Yellow
git add -A
git commit -m "Auto checkpoint: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"

Write-Host "`n===== PIPELINE COMPLETE =====" -ForegroundColor Green
Write-Host "Branch : $(git branch --show-current)"
Write-Host "APK    : $apk"
