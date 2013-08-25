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

## Retirement Notice ##

As of 2013-08-26, LiquidUU is officially **retired**. My reasoning is as follows:

   * IC<sup>2</sup> has been outwardly stagnant for more than half a year now.
While this is changing now, I've since discovered that I don't really miss it
-- I'm playing quite happily without IC<sup>2</sup>.

   * Liquid UU-Matter (by which I mean the actual thing the mod creates) was
never all that useful. I played some fairly long-term worlds with the mod
available and never actually felt a need to liquify my UU-matter (mostly
because I don't really produce much of the stuff in the first place). As
sub-points to this:

       * Liquid duplication is a cheap trick, and while the conversion ratios
    have gotten some balance, being able to duplicate liquids is not exceptionally
    useful. In the case of Forestry liquids, it's easy enough to just get more once
    you've gotten some in the first place; in the case of oil, one either doesn't
    use it, or doesn't want infinite quantities of it.

       * Accelerating IC<sup>2</sup> machine operations is nice... but it's
    also a lot more expensive than just waiting for processing to finish. Since
    time is rarely such an important resource that one cannot wait for the
    maceration of a stack of iron (or something) to finish, the accelerator becomes
    a fairly useless toy to keep around.

   * Liquid electrolysis, the last work-in-progress which was never finished,
was aiming in the same general direction as liquid duplication -- storing EUs
in liquid form is not exceptionally useful, given that you can always just make
more of them, *and* that you can store large amounts easily enough in their
natural form.

If anyone is interested in taking over the maintenance of this mod (I'm not sure
why you would), please feel free to [contact me](http://narc.ro/contact) in some
way and I'll help you pick it up.

## Compiling ##

To compile this mod, you will need a working MCP+Forge+MCPDeobfuscate install with
[mcp_interface](https://github.com/FunnyMan3595/mcp_deobfuscate/tree/mcp_interface)
plugged in. Instructions for that are in the README.txt files provided in both branches
of the mcp_deobfuscate repository. You may also like [the instructions I've written for 
setting up MCP+Forge+mcp_deobfuscate projects to automatically build with Jenkins](http://www.narc.ro/building-with-mcpdeobfuscate).

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
