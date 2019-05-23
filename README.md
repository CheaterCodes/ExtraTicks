# ExtraTicks

    Not as gross as it sounds
    
A mod to improve vanilla performance issues without changing game mechanics.

## Introduction

This mod is intended to improve vanilla server performance by modifying parts of the game logic.
However, all modifications are developed in a way that the game logic works exactly the same as vanilla.
If you can find any difference between this and vanilla (apart from performance improvements) open an issue and I will fix it.

## Modules

For easier development the logic is split into multiple modulee, each patching a different part of the game.
The following list gives an overview about implemented fixes.

### Nether Portal Lookup

Whenever an entity travels through a nether portal minecraft checks all block in a big area for nether portal blocks.
This takes a lot of time and is therefore cached for a few seconds in vanilla.
However, if it's not cached the lookup still causes huge lag spikes.

This module keeps a per chunk list of all portal blocks in that chunk and uses that for the lookup.
This way the lookup time is drastically reduced.
