# AtomRPG
RPG plugin for paper servers loosely inspired by WynnCraft and Hypixel Skyblock. This plugin is (will be) in a similar vein to plugins such as Slimefun which aims to bring a modpack like experience to a vanilla server through just the Bukkit API (and maybe some NMS). For now this plugin is currently very bare bones and still very WIP, currently I am working on building up the skeleton of this to build on top of.

## Coding Philosophies (I guess)
1. Avoid null: Where possible null is to be avoided as dealing with NPEs are a pain in the ass. Nulls should never be a possible return type within this code and should only be used as a null check from external libraries and APIs where this philosophy is not utilised. If a "No Value" is needed Java's Optional should be used.
2. Dependency Injection: Dependency Injection through Guice is utilised here
3. Configuration: YAML is used for config files and JSON is used to store POJOs
4. Records: Records are to be used for pure data type classes

## Building
Building is handled through gradle. >JDK 14 is not supported as they do not support records, JDK 17 is the version utilised and any other version is not tested