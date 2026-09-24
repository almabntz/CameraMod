# Camera Mod For Minecraft 

A lightweight NeoForge mod for Minecraft 1.21.1 that provides a basic cinematic camera keyframe and playback system.

The mod is designed as a simple production tool for creating camera movements that can be used during Minecraft recording sessions.

This repository was spawned from a template repository by Neoforge.
That template can be directly cloned to get started with a new
mod [here](https://docs.github.com/en/repositories/creating-and-managing-repositories/creating-a-repository-from-a-template).

## Requirements
- Minecraft 1.21.1
- Java 21
- NeoForge 1.21.1
- Git
- Gradle
- A Minecraft launcher that supports custom NeoForge installations


## Running the Mod

  #### 1. Clone the repository and boot it up in your prefered IDE (Im utilizing intellij)

```bash
git clone https://github.com/almabntz/CameraMod.git
cd CameraMod
```

#### 2. Build the mod

The project uses Gradle. If it is your first time building a gradle project, your IDE will download the required plug ins with the following comman in the terminal.

 On macOS or Linux, run:

```bash
./gradlew build
```

On Windows:

```bash
gradlew.bat build
```

A successful build will display:

```text
BUILD SUCCESSFUL
```

This command will compiled mod JAR that will be created within the project in the directory:

```text
build/libs/
```

#### 3.  Run the development client. This project includes a NeoForge development environment.

On macOS or Linux:

```bash
./gradlew runClient
```

On Windows:

```bash
gradlew.bat runClient
```

This launches Minecraft using the project's NeoForge development configuration.

## Using the Camera Commands

Once Minecraft is running, the mod provides the following commands.

#### Create a camera path

```text
/camera path new
```

This creates a new camera path named `default`.

#### Add a keyframe

```text
/camera keyframe add
```

The current player position and camera rotation are captured as a keyframe.

The first keyframe starts at tick `0`. Subsequent keyframes are currently placed 20 ticks after the previous keyframe, which corresponds to approximately one second in Minecraft.

#### Add multiple keyframes

For example:

```text
/camera path new
/camera keyframe add
```

Move the player to another location, then:

```text
/camera keyframe add
```

Repeat as needed.

#### Play the camera path

```text
/camera play
```

Playback interpolates the camera's position and rotation between the captured keyframes.

At least two keyframes are required for playback.

#### Stop playback

```text
/camera stop
```

#### Clear the current path

```text
/camera keyframe clear
```

This removes all keyframes from the active camera path.

## Example Workflow

A basic recording workflow looks like this:

```text
/camera path new
/camera keyframe add
```

Move the player to the next camera position:

```text
/camera keyframe add
```

Move to another position:

```text
/camera keyframe add
```

Then start playback:

```text
/camera play
```

The camera moves between the recorded keyframes rather than jumping directly from one position to the next.

## Project Structure

The camera system is separated into several responsibilities with java doc notes to help ease of editing for future projects. The individual packages involve consideration for expansion for more commands or paths:

```text
com.catface.cameramod
│
├── CameraMod
│
├── camera
│   ├── CameraKeyframe
│   ├── CameraState
│   ├── CameraPath
│   ├── CameraCapture
│   ├── CameraController
│   ├── MinecraftCameraController
│   ├── CameraPlayback
│   ├── CameraManager
│   ├── CameraTickHandler
│   └── EasingFunction
│
└── command
    ├── CameraCommand
    └── CameraCommandHandler
```

### Capture

`CameraCapture` is responsible for reading the current camera/player position and rotation and converting it into a keyframe.

### Storage

`CameraKeyframe` and `CameraPath` represent the camera data.

A path contains an ordered collection of keyframes.

### Playback

`CameraPlayback` handles:

* Playback state
* Finding the appropriate keyframes
* Interpolating camera position
* Interpolating camera rotation
* Applying easing functions

The playback system is separated from Minecraft-specific camera manipulation through the `CameraController` interface.

### Commands

`CameraCommand` contains the user-facing Brigadier commands.

`CameraCommandHandler` registers those commands with NeoForge's client command system.

## Interpolation

Camera movement currently uses linear interpolation.

For each pair of keyframes, the system calculates a normalized progress value between `0.0` and `1.0` and interpolates:

* X position
* Y position
* Z position
* Yaw
* Pitch

Rotation interpolation accounts for the `-180` / `180` degree boundary so the camera can take the shorter rotational path.

The playback system uses an `EasingFunction` interface, allowing additional easing styles to be added without changing the core playback logic.

## Development

To build the project:

```bash
./gradlew build
```

To launch the development client:

```bash
./gradlew runClient
```

The project targets Java 21 and Minecraft 1.21.1 using NeoForge.

## Current Scope and AI Assisted Uses

This project intentionally focuses on the core camera keyframe and playback workflow. With the limited time given, I do feel that a Minimum Viable Product was achieved. 
I used AI to help with my understanding of interpolation and how it needed to be considered for this plug in, as well as to help excelerate the delivery and understanding of camera key frames and how they should be handled. I referenced neoforge documentation and installed a lot of the 'plumbing' needed to wire up the plug in.

Current functionality includes:

* Creating a camera path
* Capturing camera keyframes
* Storing keyframes in memory
* Linear camera interpolation
* Position and rotation interpolation
* Starting and stopping playback
* Client-side Brigadier commands
* Separation between camera capture, storage, playback, and Minecraft integration

Potential future extensions include:

* Named camera paths
* User-defined keyframe timing
* Additional easing functions
* Editing or removing individual keyframes
* Camera preview and recording controls

Given more time I would dedicated efforts to understanding how to set up a Minecraft server so that I could test throughly. Additionaly, I would appreciate the ability to name paths. 

