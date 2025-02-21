## 📌 **GPS Plugin - Usage Guide**  

### 🔧 **Installation**  
1. Download the plugin JAR file and place it in your **`plugins/`** folder.  
2. Restart or reload your Minecraft server.  


---

### 📜 **Commands**  

#### 📍 **/gps `<x>` `<z>`**  
➡️ Creates a **GPS Compass** that points to the specified coordinates.  
✅ **Requires 10 compasses** in your inventory.  
📌 The compass will have:  
- Name: **GPS**  
- Lore: `"Heading to (x, z)"`  
- NBT tag ensuring authenticity (cannot be faked).  

**Example Usage:**  
```sh
/gps 100 200
```
🎯 This will create a GPS Compass pointing to **(100, 200)**.  

❌ **If you do not have 10 compasses, you will get an error message.**  

---

#### 🔄 **/reroute `<x>` `<z>`**  
➡️ Updates an existing GPS Compass to **point to new coordinates**.  
✅ The player must **already have a GPS Compass**.  

**Example Usage:**  
```sh
/reroute 300 -150
```
🎯 Your GPS Compass will now **point to (300, -150)**.  

❌ **If you do not have a valid GPS Compass, the command will not work.**  



### ❓ **Troubleshooting**  
❌ **"You do not have a GPS compass" when using `/reroute`**  
✔️ Ensure you're holding a **GPS Compass** created by `/gps`.  
✔️ The plugin uses an **NBT tag**, so renaming a normal compass won’t work.  

❌ **"You need at least 10 compasses" when using `/gps`**  
✔️ Ensure you have **at least 10 normal compasses** in your inventory.  

---

### 💡 **Extra Notes**  
- The GPS Compass **does not track automatically**; it uses a **lodestone system**.  
- The plugin is **fully optimized** and prevents abuse (e.g., fake compasses).  
- Works with **Minecraft 1.16+** (including later versions).  

---

### 🛠 **Credits**  
✅ **Developer:** TNJS  
✅ **Version:** 1.1.0
🚀 **Enjoy your adventure with the GPS Plugin!** 🌍


README written by a LLM (Because im a lazy developer :D )
