# Define the Java version and the download URL
$requiredJavaVersion = [version]'11.0.0'
$downloadJavaVersion= "zulu21.38.21-ca-jre21.0.5-win_x64"
$downloadUrl = "https://cdn.azul.com/zulu/bin/{0}.zip" -f $downloadJavaVersion
$zipFileName = "java.zip"
$extractFolderName = "java"
$javaPath = "./java/{0}/bin/java.exe" -f $downloadJavaVersion

# Function to check installed Java version
function Get-InstalledJavaVersion {
    try {
        # Attempt to get the version using Get-Command and select the Version property
        $javaVersion = (Get-Command java | Select-Object -ExpandProperty Version).ToString()

        # If the version is found, return it
        if ($javaVersion) {
            return [version]$javaVersion
        }
    } catch {
        return $null
    }
}
# Check if Java 11 or higher is installed
$installedJavaVersion = Get-InstalledJavaVersion
if ($installedJavaVersion -lt [version]$requiredJavaVersion) {

    if (Test-Path -Path $extractFolderName) {} else{
    Write-Host "Java 11 or higher is not installed."

    # Download the Java ZIP file
    Write-Host "Downloading Java from $downloadUrl..."
    Invoke-WebRequest -Uri $downloadUrl -OutFile $zipFileName

    # Extract the ZIP file
    Write-Host "Extracting ZIP file..."
    Expand-Archive -Path $zipFileName -DestinationPath $extractFolderName

    Write-Host "Java has been successfully downloaded and extracted to $extractFolderName."

    Remove-Item -Path $zipFileName
    }

    Write-Host "Java Path: $javaPath"
    & $javaPath -jar "CSGOCaseStatsViewerV2-1.1.1-jar-with-dependencies.jar"
} else {
    & java -jar "CSGOCaseStatsViewerV2-1.1.1-jar-with-dependencies.jar"
}

PAUSE