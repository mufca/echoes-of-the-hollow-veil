# Echoes of the Hollow Veil

**Echoes of the Hollow Veil** is a solo dark-fantasy RPG project inspired by classic MUDs, built in Java 21 with the [LibGDX](https://libgdx.com/) framework.
The game focuses on narrative, character systems, a moody atmosphere, and some minor visual effects powered by custom shaders.
Currently, this is very early version - no gameplay is available. 
---
## Requirements
- Java 21+
- Gradle (recommended: use the included wrapper `./gradlew`)
- GPU compatible with OpenGL 3.2+
- Runs on desktop platforms only (Windows, Linux, macOS).  
  *(Mobile and browser targets are not supported.)*
---
## Running the Project
Bash:
```bash
./gradlew lwjgl3:run
```
Windows (PowerShell / CMD):
```bat
gradlew.bat lwjgl3:run
```
# Project Structure
- `core/src` – core game logic, screens, UI
- `lwjgl3/` – desktop launcher and configuration
- `core/assets/` – images, fonts, shaders
- `gradle/wrapper` – Gradle wrapper configuration
- `gradlew`, `gradlew.bat` – wrapper executables
- `build.gradle`, `gradle.properties` – build configuration

---
# Building a Release
To create a runnable JAR package:
**Linux / macOS**
```bash
./gradlew lwjgl3:dist
```
**Windows**
```bat
gradlew.bat lwjgl3:dist
```
The distributable package will be located under:
```
lwjgl3/build/libs/
```
---
# Roadmap

### Done
- Project initialization and Gradle setup
- Post-processing shaders framework
- Location system

### In Progress
- Character logic layer (stats, inventory, skills)
- Dialog and event system
- Exploration prototype
- Basic UI and screen system

### Planned
- Playable demo

# License

MIT License

# Acknowledgments
- My gratitude goes to [Tommy Ettinger](https://github.com/tommyettinger) for crafting the [TextraTypist](https://github.com/tommyettinger/textratypist) library — a tool that gives words the life and presence they deserve.