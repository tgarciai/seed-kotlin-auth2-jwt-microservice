ext {
    Properties properties = new Properties()
}

def _secretsFile = file("$rootDir/secrets.properties")
if (_secretsFile.exists()) {
    _secretsFile.withInputStream { ext.secrets.load(it) }
}

for (key in ["publication.username", "publication.password"]) {
    if (hasProperty(key) && !secrets.hasProperty(key)) {
        secrets.setProperty(key, getProperty(key))
    }
}

ext["signing.keyId"] = secrets.getProperty("signing.keyId") ?: System.getenv("SIGNING_KEY_ID")
if (ext["signing.keyId"]) {
    ext["signing.secretKeyRingFile"] = rootProject.file(".credentials/${ext["signing.keyId"]}.gpg")
}
ext["signing.password"] = secrets.getProperty("signing.password") ?: System.getenv("SIGNING_PASSWORD")
