This is a Minecraft mod.

It depends on [IC2][ic2] and [Buildcraft][bc], and optionally integrates with
[Forestry](http://forestry.sengir.net/wiki/),
[Not Enough Items](http://www.minecraftforum.net/topic/909223-/) and
[Thermal Expansion](http://thermalexpansion.wikispaces.com/).

It makes [UU-Matter][uum] from IC2 into a liquid, for use with liquid pipes and tanks.
It also makes the Buildcraft refinery able to use liquid UUM and other liquids to
duplicate those liquids. It further allows (most) IC2 basic machines, as well as any
willing to use the API, to use liquid UUM as a processing accelerator, to speed up
automated processing at a reasonable cost.

The canonical place to get information on LiquidUU is
[its thread on the IC2 forum](http://forum.industrial-craft.net/index.php?page=Thread&threadID=8269).

## Compiling ##

To compile this mod, you will need a working MCP+Forge+MCPDeobfuscate install with
[mcp_interface](https://github.com/FunnyMan3595/mcp_deobfuscate/tree/mcp_interface)
plugged in. Instructions for that are in the README.txt files provided in both branches
of the mcp_deobfuscate repository.

As a special note for compiling with Minecraft 1.4.6, the current mcp_interface assumes
that forge will create mcp/src/common, which is no longer the case as of recent Forges.
To get around this, once you've compiled and obfuscated your MCP+Forge install, either
symlink or rename mcp_dir/src/minecraft to src/common.

Note that, for a successful compile, you will need all of IC2, Buildcraft, Forestry, NEI,
and Thermal Expansion to be present in your lib/ folder -- the simplest way to do this is
just to copy them out of your minecraft {core,}mods/ into lib-obf/, and let
deobfuscate_libs.py make them usable and put them in the right place. Further note that
CodeChickenCore is **not** required -- the compile process does not need it.

Once you're set up, just clone this repository into your brand new mods/ directory, and
don't forget to also clone [NarcLib](https://github.com/narc0tiq/NarcLib), as it is a
dependency. After that, a simple "python runtime/recompile_mods.py LiquidUU" will create
the package for you, just like the readme of mcp_interface says.

## License ##

This mod is open-source under the terms of the
[Minecraft Mod Public License](http://www.mod-buildcraft.com/MMPL-1.0.txt),
version 1.0.1, included in this package as LICENSE.md.

[ic2]: http://www.industrial-craft.net/
[bc]: http://mod-buildcraft.com/
[uum]: http://wiki.industrial-craft.net/index.php?title=Matter
