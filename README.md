# Offline LAN (Forge 1.19.2)

Host-side mod: lets offline-mode players join a world opened with "Open to LAN".
Only the HOST needs the jar. Joiners just need the same Minecraft/Forge version (and any other mods you run).

## Build (no install): GitHub Actions
1. Create a new GitHub repo and upload everything in this folder (including the hidden .github folder).
2. Open the Actions tab, let the 'Build mod' run finish (about 3-5 min).
3. Download the `lanoffline-jar` artifact, unzip it, and use the jar WITHOUT '-sources' / '-slim' in the name.

## Build locally
1. Install JDK 17.
2. Copy the Gradle wrapper from the official Forge 1.19.2 MDK (gradlew, gradlew.bat, gradle/) into this folder.
3. Run `./gradlew build`  ->  jar in build/libs/

## Use
Drop the jar in the host's mods folder, open the world to LAN, give others the LAN address/port.
Over the internet: use a tunnel/VPN (playit.gg, Radmin VPN, ZeroTier) or forward the port.

## Dedicated server
No mod needed: set `online-mode=false` in server.properties.

## Config
config/lanoffline-common.toml -> allowOfflineAccounts (default true)
