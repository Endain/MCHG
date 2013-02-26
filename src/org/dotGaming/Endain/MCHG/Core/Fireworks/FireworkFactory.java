package org.dotGaming.Endain.MCHG.Core.Fireworks;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkFactory {
	// Capitol firework effect
	private static FireworkEffect capitol = FireworkEffect.builder().trail(true).flicker(false).with(Type.BURST).withColor(Color.RED, Color.MAROON, Color.YELLOW, Color.ORANGE).withFade(Color.WHITE, Color.GRAY).build();
	// District firework effects
	private static FireworkEffect d1 = FireworkEffect.builder().trail(true).flicker(false).with(Type.BURST).withColor(Color.fromRGB(0xFFD700), Color.fromRGB(0xC0C0C0), Color.fromRGB(0xB0C4DE), Color.fromRGB(0xD3D3D3)).withFade(Color.BLACK, Color.GRAY).build();
	private static FireworkEffect d2 = FireworkEffect.builder().trail(true).flicker(false).with(Type.BURST).withColor(Color.fromRGB(0xC0C0C0), Color.fromRGB(0x808080), Color.fromRGB(0xFFD700), Color.fromRGB(0xCD5C5C)).withFade(Color.BLACK, Color.GRAY).build();
	private static FireworkEffect d3 = FireworkEffect.builder().trail(true).flicker(false).with(Type.BURST).withColor(Color.fromRGB(0xFFD700), Color.fromRGB(0xC0C0C0), Color.fromRGB(0x44CC00), Color.fromRGB(0xC5EF1C)).withFade(Color.BLACK, Color.GRAY).build();
	private static FireworkEffect d4 = FireworkEffect.builder().trail(true).flicker(false).with(Type.BURST).withColor(Color.fromRGB(0x7B68EE), Color.fromRGB(0x00FFFF), Color.fromRGB(0xC0C0C0), Color.fromRGB(0x1E90FF)).withFade(Color.BLACK, Color.GRAY).build();
	private static FireworkEffect d5 = FireworkEffect.builder().trail(true).flicker(false).with(Type.BURST).withColor(Color.fromRGB(0x1E90FF), Color.fromRGB(0xB0C4DE), Color.fromRGB(0x9400D3), Color.fromRGB(0xFFD700)).withFade(Color.BLACK, Color.GRAY).build();
	private static FireworkEffect d6 = FireworkEffect.builder().trail(true).flicker(false).with(Type.BURST).withColor(Color.fromRGB(0xC0C0C0), Color.fromRGB(0x4682B4), Color.fromRGB(0x404040), Color.fromRGB(0xFFD700)).withFade(Color.BLACK, Color.GRAY).build();
	private static FireworkEffect d7 = FireworkEffect.builder().trail(true).flicker(false).with(Type.BURST).withColor(Color.fromRGB(0x8B4513), Color.fromRGB(0xCD853F), Color.fromRGB(0xC0C0C0), Color.fromRGB(0xB22222)).withFade(Color.BLACK, Color.GRAY).build();
	private static FireworkEffect d8 = FireworkEffect.builder().trail(true).flicker(false).with(Type.BURST).withColor(Color.fromRGB(0x9400D3), Color.fromRGB(0x4196E1), Color.fromRGB(0x00FFFF), Color.fromRGB(0xFF8C00)).withFade(Color.BLACK, Color.GRAY).build();
	private static FireworkEffect d9 = FireworkEffect.builder().trail(true).flicker(false).with(Type.BURST).withColor(Color.fromRGB(0xF5DEB3), Color.fromRGB(0xCD853F), Color.fromRGB(0xD2691E), Color.fromRGB(0xFFD700)).withFade(Color.BLACK, Color.GRAY).build();
	private static FireworkEffect d10 = FireworkEffect.builder().trail(true).flicker(false).with(Type.BURST).withColor(Color.fromRGB(0x8B4513), Color.fromRGB(0xCD853F), Color.fromRGB(0xB0C4DE), Color.fromRGB(0x7CFC00)).withFade(Color.BLACK, Color.GRAY).build();
	private static FireworkEffect d11 = FireworkEffect.builder().trail(true).flicker(false).with(Type.BURST).withColor(Color.fromRGB(0x008000), Color.fromRGB(0xFF0000), Color.fromRGB(0xFFD700), Color.fromRGB(0x7CFC00)).withFade(Color.BLACK, Color.GRAY).build();
	private static FireworkEffect d12 = FireworkEffect.builder().trail(true).flicker(false).with(Type.BURST).withColor(Color.fromRGB(0x000000), Color.fromRGB(0x8B0000), Color.fromRGB(0xDAA520), Color.fromRGB(0xC0C0C0)).withFade(Color.BLACK, Color.GRAY).build();
	// Default firework
	private static FireworkEffect def = FireworkEffect.builder().trail(true).flicker(true).with(Type.STAR).withColor(Color.BLUE, Color.AQUA).withFade(Color.GRAY, Color.BLACK).build();
	
	public static void launchFirework(Location loc, FireworkEffect fe, int power) {
		// Create a new firework
		Firework fw = (Firework)loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		// Get and set the metadata
		FireworkMeta fm = fw.getFireworkMeta();
		fm.setPower(power);
		fm.addEffect(fe);
		// Apply to our current firework
		fw.setFireworkMeta(fm);
	}
	
	public static FireworkEffect getDistrictFirework(int district) {
		if(district == 1)
			return d1;
		else if(district == 2)
			return d2;
		else if(district == 3)
			return d3;
		else if(district == 4)
			return d4;
		else if(district == 5)
			return d5;
		else if(district == 6)
			return d6;
		else if(district == 7)
			return d7;
		else if(district == 8)
			return d8;
		else if(district == 9)
			return d9;
		else if(district == 10)
			return d10;
		else if(district == 11)
			return d11;
		else if(district == 12)
			return d12;
		return def;
	}
	
	public static FireworkEffect getCapitolFirework() {
		return capitol;
	}
}
