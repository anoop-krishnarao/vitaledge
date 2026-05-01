# Vital Edge

A Minecraft Fabric client-side mod for 1.21.1.

Renders a 4-edge screen gradient that acts as an ambient armor durability indicator — no HUD text, no icons, no new items. The gradient color reflects your equipped armor's durability, modified by Y-height and biome, with a damage surge animation on hit.

## Download

- [Modrinth](https://modrinth.com/mod/vital-edge)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/vital-edge)

## Features

- Screen-edge color gradient driven by armor durability (green → amber → orange → red → pulsing red)
- Y-height modifier: lightens above Y=0, darkens below
- Biome hue blend across all 54 overworld biomes
- Damage surge heartbeat animation on hit
- Configurable via Mod Menu (edge thickness, gradient style, opacity, surge toggle, biome blend)
- No armor equipped = gradient off entirely
- Compatible with Iris + Sodium

## Requirements

- Minecraft 1.21.1
- Fabric Loader ≥ 0.16.0
- Fabric API
- Mod Menu (optional, for configuration)

## Building

```bash
./gradlew build
```

## Support

- [Ko-fi](https://ko-fi.com/anoopk)

## License

MIT — © 2026 Anoop K
