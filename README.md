# **GPS Plugin**

A Bukkit/Spigot Minecraft plugin that allows players to create a GPS compass to navigate to specific coordinates. Players can reroute the compass a limited number of times before it breaks.

Join the support discord - https://dsc.gg/mosaic

## **Features**
- Players can set a GPS compass pointing to specified coordinates using `/gps <x> <z>`.
- The compass can be rerouted up to a configurable number of times using `/reroute <x> <z>`.
- The number of reroutes left is displayed in the item lore.
- The compass breaks after reaching the reroute limit, playing an item break sound.
- Requires players to have `gps.use` permission to use the commands.
- The maximum reroutes allowed is configurable in `config.yml`.
- Right-clicking a compass will create a line of particles that point in the direction of the destination.
---

## **Commands**
| Command | Description | Permission |
|---------|-------------|------------|
| `/gps <x> <z>` | Creates a GPS compass pointing to the given coordinates. Requires 10 compasses. | `gps.use` |
| `/reroute <x> <z>` | Updates the compass to a new location (limited reroutes). | `gps.use` |

---

## **Permissions**
| Permission | Description | Default |
|------------|-------------|---------|
| `gps.use` | Allows use of `/gps` and `/reroute` commands. | `true` |

---

## **Configuration (`config.yml`)**
You can configure the maximum number of reroutes before the compass breaks.

```yaml
max-reroutes: 3
```
(Default: `3` reroutes before breaking)

---

## **How It Works**
1. A player runs `/gps <x> <z>` while holding at least 10 compasses.
2. The command consumes 10 compasses and gives them a special GPS compass.
3. The GPS compass points to the entered coordinates.
4. The player can use `/reroute <x> <z>` to change the destination (up to the max reroutes set in `config.yml`).
5. Once the reroutes are exhausted, the compass disappears, playing an item break sound.

---

## **Installation**
1. **Download the plugin JAR** and place it in your `plugins` folder.
2. **Restart your server** to generate the config file.
3. **Edit `config.yml`** in the plugin folder if needed.
4. **Reload the server** to apply changes.


---

## **License**
This project is licensed under the **GNU General Public License v3.0 (GPL-3.0)**.  
You are free to use, modify, and distribute this plugin under the terms of the GPL-3.0 license.  

For more details, see the **[GNU GPL v3 License](https://www.gnu.org/licenses/gpl-3.0.en.html)**.

---

This fie was generated by an LLM
