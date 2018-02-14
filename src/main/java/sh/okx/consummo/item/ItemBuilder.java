package sh.okx.consummo.item;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class ItemBuilder {
  private ItemStack item;
  private ItemMeta meta;

  public ItemBuilder(Material material) {
    this.item = new ItemStack(material);
    this.meta = item.getItemMeta();
  }

  public ItemBuilder() {
    this(Material.AIR);
  }

  public ItemBuilder durability(short durability) {
    item.setDurability(durability);
    return this;
  }

  public ItemBuilder amount(int amount) {
    item.setAmount(amount);
    return this;
  }

  public ItemBuilder type(Material material) {
    item.setType(material);
    return this;
  }

  public ItemBuilder data(MaterialData data) {
    item.setData(data);
    return this;
  }

  public ItemBuilder lore(String... lore) {
    meta.setLore(Arrays.asList(lore));
    return this;
  }

  public ItemBuilder name(String displayName) {
    meta.setDisplayName(displayName);
    return this;
  }

  public ItemBuilder enchant(Enchantment enchantment, int level) {
    meta.addEnchant(enchantment, level, true);
    return this;
  }

  public ItemBuilder flag(ItemFlag... flags) {
    meta.addItemFlags(flags);
    return this;
  }

  public ItemBuilder unbreakable() {
    meta.setUnbreakable(true);
    return this;
  }

  public ItemStack build() {
    item.setItemMeta(meta);
    return item;
  }
}
