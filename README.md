# Consummo

Feel free to copy any of the APIs into your own code.

## Available APIs

- [Updater](#updater) - automatically update your plugin
- [Scheduler](#scheduler) - easily change execution of code into different threads
- [GUI](#gui) - create simple GUIs with listeners
- [Item Builder](#item builder) - create items in one line

# Updater

This API allows you to automatically update your plugin, with your spigot resource ID.

This uses both Spigot's "latest version" API and SpiGet.

You may also provide a callback to this API to be run when your plugin is updated.

## Usage

```java
new AutoUpdater(plugin, 1234567).schedule(/* check for updates every 5000 ticks */ 5000,
    /* This is called async */
    u -> System.out.println("Downloaded new update v" + u.getVersion() 
        + ". Title: " + u.getTitle() 
        + ", changelog: " + u.getDescription()), 
    /* stop checking for updates once one has been downloaded */ true);
```

# Scheduler

This API allows you to easily change between an asnyc thread and Bukkit's main thread.
This means you can easily make web requests and then do something with the response in Bukkit's main thread, 
without having to indent a lot.

## Usage
```java
byte[] response = new byte[1024];

new SchedulerChain()
    .async(() -> {
      try {
        url.openStream().read(response);
        return true;
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    })
    .sync(() -> Bukkit.broadcastMessage("Response was " + new String(response)))
    .start(plugin);
```

You can either return a boolean, or return nothing. 
If you return a boolean and it is false, then execution of the chain will stop.
If you true or you return nothing, the execution will continue to the next task.

Without this API, it might look something like this:

```java
Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
  byte[] response = new byte[1024];

  try {
    url.openStream().read(response);
  } catch (IOException e) {
    e.printStackTrace();
    return;
  }
  
  Bukkit.getScheduler().runTask(plugin, () -> {
    Bukkit.broadcastMessage("Response was " + new String(response));
  });
});
```

# GUI

You can create GUIs in combination with the [Item Builder](#item-builder) very easily.

For example, if I wanted to create a GUI with one sign that said "Hello" when I clicked it, one that did nothing, and another that said "Goodbye" when I clicked it and closed the GUI, it would look like this:

```java
Gui gui = new Gui(this, "Example GUI", 1);
gui.register(new ItemBuilder(Material.SIGN)
    .name(ChatColor.GREEN + "Hello!")
    .lore("This sign says hello!").build(),
    3, e -> e.getWhoClicked().sendMessage("Hello!"));
gui.register(new ItemBuilder(Material.SIGN)
    .name(ChatColor.GRAY + "The nothing sign")
    .lore("This sign does absolutely nothing...").build(),
    4);
gui.register(new ItemBuilder(Material.SIGN)
    .name(ChatColor.RED + "Goodbye")
    .lore("This sign closes the GUI and says goodbye.").build(),
    5, e -> {
      HumanEntity clicker = e.getWhoClicked();
      clicker.sendMessage("Goodbye!");
      clicker.closeInventory();
    });

// when the player closes the GUI, unregister the listener to prevent memory leaks
// if you are showing this to multiple people at various intervals (ie through a command), this is not needed.
gui.setUnregisterOnClose(true);

gui.open(player);
```

# Item Builder

The item builder is a simple way to make items.

It has the following methods:

- <code>**ItemStack**     build()</code>
- <code>**ItemBuilder**   amount(int amount </code>
- <code>**ItemBuilder**   data(MaterialData data)</code>
- <code>**ItemBuilder** 	durability(short durability)</code>
- <code>**ItemBuilder** 	enchant(Enchantment enchantment, int level)</code>
- <code>**ItemBuilder** 	flag(ItemFlag... flags)</code>
- <code>**ItemBuilder** 	lore(String... lore)</code>
- <code>**ItemBuilder** 	name(String displayName)</code>
- <code>**ItemBuilder** 	type(Material material)</code>
- <code>**ItemBuilder** 	unbreakable()</code>

You can either construct it with no arguments, or provide a `Material`.