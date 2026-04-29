# VitalEdge

A Minecraft Fabric client-side mod for 1.21.x.

Renders a 4-edge screen gradient that acts as an ambient armor durability indicator — no HUD text, no icons, no new items or blocks. The gradient color reflects your equipped armor's durability, modified by Y-height, with a damage surge overlay on hit.

## Features

- Screen-edge color gradient driven by armor durability (green → amber → orange → red → pulsing red)
- Y-height modifier: lightens above Y=0, darkens below
- Damage surge animation on hit, colored by damage type
- Configurable via Mod Menu (edge thickness, Y clamps, surge toggle)
- No armor equipped = gradient off entirely

## Building

```bash
./gradlew build
```

## Development

- Java 21 (Temurin recommended via SDKMAN)
- IntelliJ IDEA
- Fabric Loom 1.14
