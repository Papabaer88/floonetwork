# FlooNetwork

This is a lightweight Paper plugin inspired by the Floo Network from the Harry Potter universe.

Features:
- register fireplaces on vanilla fire/campfire blocks
- teleport between registered fireplaces
- use a custom item named `Floo Powder` without any resource pack
- works on Paper 1.21.3 / Paper build `paper-26.3-252.jar`

## Build

```bash
./gradlew clean build
```

The generated jar will be in `build/libs`.

## Commands

```text
/floo create <name>
/floo remove <name>
/floo tp <name>
/floo list
/floo powder
```

## How it works

- Right-click a fire/campfire with a `Floo Powder` item to teleport to a random registered fireplace.
- Sneak + right-click a fire/campfire with Floo Powder to register the block as a new fireplace.
- Or use `/floo create <name>` while looking at a fire block.

This version intentionally avoids custom textures and resource packs.
