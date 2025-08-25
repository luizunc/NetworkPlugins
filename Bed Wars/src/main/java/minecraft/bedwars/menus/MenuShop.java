package minecraft.bedwars.menus;

import minecraft.bedwars.Main;
import minecraft.bedwars.cosmetics.Cosmetic;
import minecraft.bedwars.cosmetics.types.*;
import minecraft.bedwars.game.BedWars;
import minecraft.bedwars.game.BedWarsTeam;
import minecraft.bedwars.game.shop.Shop;
import minecraft.bedwars.game.shop.ShopCategory;
import minecraft.bedwars.game.shop.ShopItem;
import minecraft.bedwars.hook.container.FavoritesContainer;
import minecraft.bedwars.menus.cosmetics.MenuCosmetics;
import minecraft.core.core.libraries.menu.PlayerMenu;
import minecraft.core.core.player.Profile;
import minecraft.core.core.utils.BukkitUtils;
import minecraft.core.core.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MenuShop extends PlayerMenu {
  
  public MenuShop(Profile profile) {
    super(profile.getPlayer(), "Loja - Bed Wars", 3);
  
    long max = 0;
    long owned = 0;
    long percentage = 100;
    String color = "&a";
    this.setItem(11, BukkitUtils.deserializeItemStack(
        "CHEST : 1 : nome>&aCosméticos da Partida : desc>&7Acesse todos os cosméticos\n&7disponíveis para personalizar\n&7sua experiência no jogo.\n \n&eClique para acessar!"));
    
    // Ícone do frasco de experiência para Habilidades
    this.setItem(13, BukkitUtils.deserializeItemStack(
        "EXP_BOTTLE : 1 : nome>&aHabilidades : desc>&7Tenha vantagens únicas para\n&7auxiliar você nas partidas.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou evoluir!"));
    
    this.setItem(15, BukkitUtils.deserializeItemStack(
        "ANVIL : 1 : nome>&aExperiencia de Jogo : desc>&7Acesse outros itens e\n&7funcionalidades da loja.\n \n&eClique para acessar!"));
    
    this.register(Main.getInstance());
    this.open();
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      evt.setCancelled(true);
      
      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }
        
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
          ItemStack item = evt.getCurrentItem();
          
          if (item != null && item.getType() != Material.AIR) {
            if (evt.getSlot() == 11) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmeticsPage(profile);
            } else if (evt.getSlot() == 13) {
              EnumSound.ENDERMAN_TELEPORT.play(this.player, 1.0f, 1.0f);
              player.sendMessage("§cEm breve.");
            } else if (evt.getSlot() == 15) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuExperienciaJogo(profile);
            }
          }
        }
      }
    }
  }
  
  public void cancel() {
    HandlerList.unregisterAll(this);
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(this.player)) {
      this.cancel();
    }
  }
  
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
      this.cancel();
    }
  }
  
  public static class MenuCosmeticsPage extends PlayerMenu {
    
    public MenuCosmeticsPage(Profile profile) {
      super(profile.getPlayer(), "Cosméticos da Partida", 5);
  
      List<ShopkeeperSkin> skins = Cosmetic.listByType(ShopkeeperSkin.class);
      long max = skins.size();
      long owned = skins.stream().filter(shopkeeperSkin -> shopkeeperSkin.has(profile)).count();
      long percentage = max == 0 ? 100 : (owned * 100) / max;
      String color = (owned == max) ? "&a" : (owned > max / 2) ? "&7" : "&c";
      skins.clear();
      this.setItem(10, BukkitUtils.deserializeItemStack(
          "SKULL_ITEM:3 : 1 : nome>&aSkins do Vendedor : desc>&7Altere a skin dos Vendedores\n&7durante a sua partida.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar! : skin>ewogICJ0aW1lc3RhbXAiIDogMTYyMzI1Mzg3NzQwNSwKICAicHJvZmlsZUlkIiA6ICJhMDVkZWVjMDdhMGU0MDc2ODdjYmRjNWRjYWNhODU4NiIsCiAgInByb2ZpbGVOYW1lIiA6ICJWaWxsYWdlciIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83YWY3YzA3ZDFkZWQ2MWIxZDMzMTI2ODViMzJlNDU2OGZmZGRhNzYyZWM4ZDgwODg5NWNjMzI5YTkzZDYwNmUwIgogICAgfQogIH0KfQ"));
  

  
      List<DeathMessage> messages = Cosmetic.listByType(DeathMessage.class);
      max = messages.size();
      owned = messages.stream().filter(deathMessage -> deathMessage.has(profile)).count();
      percentage = max == 0 ? 100 : (owned * 100) / max;
      color = (owned == max) ? "&a" : (owned > max / 2) ? "&7" : "&c";
      messages.clear();
      this.setItem(12, BukkitUtils.deserializeItemStack(
          "BOOK_AND_QUILL : 1 : nome>&aMensagens de Abate : desc>&7Anuncie o abate do seu inimigo de\n&7uma forma estilosa com mensagens de morte.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar!"));
      

  
      List<WinAnimation> animations = Cosmetic.listByType(WinAnimation.class);
      max = animations.size();
      owned = animations.stream().filter(animation -> animation.has(profile)).count();
      percentage = max == 0 ? 100 : (owned * 100) / max;
      color = (owned == max) ? "&a" : (owned > max / 2) ? "&7" : "&c";
      animations.clear();
      this.setItem(14, BukkitUtils.deserializeItemStack(
          "DRAGON_EGG : 1 : nome>&aComemorações : desc>&7Esbanje estilo nas suas vitórias\n&7com comemorações exclusivas.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar!"));
  
      List<DeathCry> deathcries = Cosmetic.listByType(DeathCry.class);
      max = deathcries.size();
      owned = deathcries.stream().filter(deathcry -> deathcry.has(profile)).count();
      percentage = max == 0 ? 100 : (owned * 100) / max;
      color = (owned == max) ? "&a" : (owned > max / 2) ? "&7" : "&c";
      deathcries.clear();
      this.setItem(16, BukkitUtils.deserializeItemStack(
          "GHAST_TEAR : 1 : nome>&aGritos de Morte : desc>&7Gritos de mortes são sons que\n&7irão ser reproduzidos toda vez\n&7que você morrer.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar!"));
      
      List<BreakEffect> breakEffects = Cosmetic.listByType(BreakEffect.class);
      max = breakEffects.size();
      owned = breakEffects.stream().filter(breakEffect -> breakEffect.has(profile)).count();
      percentage = max == 0 ? 100 : (owned * 100) / max;
      color = (owned == max) ? "&a" : (owned > max / 2) ? "&7" : "&c";
      breakEffects.clear();
      this.setItem(19, BukkitUtils.deserializeItemStack(
          "BED : 1 : nome>&aQuebra de Cama : desc>&7Deixa a sua marca quando quebrar\n&7uma cama do time oponente.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar!"));
      
      List<KillEffect> killeffects = Cosmetic.listByType(KillEffect.class);
      max = killeffects.size();
      owned = killeffects.stream().filter(killEffect -> killEffect.has(profile)).count();
      percentage = max == 0 ? 100 : (owned * 100) / max;
      color = (owned == max) ? "&a" : (owned > max / 2) ? "&7" : "&c";
      killeffects.clear();
      this.setItem(21, BukkitUtils.deserializeItemStack(
          "BONE : 1 : nome>&aEfeito de Abate : desc>&7Deixa a sua marca quando abater\n&7os seus oponentes.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar!"));
  
      List<WoodTypes> types = Cosmetic.listByType(WoodTypes.class);
      max = types.size();
      owned = types.stream().filter(killEffect -> killEffect.has(profile)).count();
      percentage = max == 0 ? 100 : (owned * 100) / max;
      color = (owned == max) ? "&a" : (owned > max / 2) ? "&7" : "&c";
      types.clear();
      this.setItem(23, BukkitUtils.deserializeItemStack(
          "WOOD : 1 : nome>&aTipos de Madeira : desc>&7Modifique o tipo da madeira que\n&7você irá receber dos vendedores!\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar!"));
  
      updatePreferredColorIcon(profile);
      
      this.setItem(40, BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cVoltar"));
    
      this.register(Main.getInstance());
      this.open();
    }
  
    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
      if (evt.getInventory().equals(this.getInventory())) {
        evt.setCancelled(true);
      
        if (evt.getWhoClicked().equals(this.player)) {
          Profile profile = Profile.getProfile(this.player.getName());
          if (profile == null) {
            this.player.closeInventory();
            return;
          }
        
          if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
            ItemStack item = evt.getCurrentItem();
          
            if (item != null && item.getType() != Material.AIR) {
                          if (evt.getSlot() == 10) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Skins do Vendedor", ShopkeeperSkin.class);

            } else if (evt.getSlot() == 12) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Mensagens de Abate", DeathMessage.class);

              } else if (evt.getSlot() == 14) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Comemorações", WinAnimation.class);
            } else if (evt.getSlot() == 16) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Gritos de Morte", DeathCry.class);

            } else if (evt.getSlot() == 19) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Quebra de Cama", BreakEffect.class);
            } else if (evt.getSlot() == 21) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Efeito de Abate", KillEffect.class);
            } else if (evt.getSlot() == 23) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                new MenuCosmetics<>(profile, "Tipos de Madeira", WoodTypes.class);
            } else if (evt.getSlot() == 25) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuPreferredColor(profile);
              // Atualizar o ícone após retornar do menu de cor preferida
              updatePreferredColorIcon(profile);
              } else if (evt.getSlot() == 40) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                new MenuShop(profile);
              }
            }
          }
        }
      }
    }
  
    public void cancel() {
      HandlerList.unregisterAll(this);
    }
  
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) {
      if (evt.getPlayer().equals(this.player)) {
        this.cancel();
      }
    }
  
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent evt) {
      if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
        this.cancel();
      }
    }
    
    private void updatePreferredColorIcon(Profile profile) {
      // Determinar a cor da lã baseada na cor preferida ou cor atual do time
      String woolColor = "0"; // Branco por padrão
      String woolName = "&aCor Preferida";
      boolean hasGlow = false;
      
      // Verificar se o jogador está em uma partida
      BedWars game = profile.getGame(BedWars.class);
      if (game != null) {
        BedWarsTeam team = game.getTeam(this.player);
        if (team != null) {
          // Usar a cor do time atual
          woolColor = BedWarsTeam.ids[team.getIndex()];
          woolName = "&aCor Preferida";
  }
      } else {
        // Se não está em partida, verificar cor preferida
        String corPreferida = profile.getDataContainer("bedwars", "preferred_color").getAsString();
        if (corPreferida != null && !corPreferida.isEmpty() && !corPreferida.equals("0")) {
          hasGlow = true; // Tem glow quando tem cor preferida
          // Mapeamento direto dos IDs
          switch (corPreferida) {
            case "1": // Vermelho
              woolColor = "14";
              woolName = "&aCor Preferida";
              break;
            case "2": // Azul
              woolColor = "11";
              woolName = "&aCor Preferida";
              break;
            case "3": // Verde Lima
              woolColor = "5";
              woolName = "&aCor Preferida";
              break;
            case "4": // Amarelo
              woolColor = "4";
              woolName = "&aCor Preferida";
              break;
            case "5": // Ciano
              woolColor = "9";
              woolName = "&aCor Preferida";
              break;
            case "6": // Branco
              woolColor = "0";
              woolName = "&aCor Preferida";
              break;
            case "7": // Rosa
              woolColor = "6";
              woolName = "&aCor Preferida";
              break;
            case "8": // Cinza
              woolColor = "7";
              woolName = "&aCor Preferida";
              break;
            default:
              woolColor = "0"; // Branco
              woolName = "&aCor Preferida";
              hasGlow = false;
              break;
          }
        } else {
          // Se não tem cor preferida (ou é "0"), mostrar lã branca
          woolColor = "0"; // Branco
          woolName = "&aCor Preferida";
          hasGlow = false;
        }
      }
      
      String itemString = "WOOL:" + woolColor + " : 1 : nome>" + woolName + " : desc>&7Escolha sua cor preferida de time\n&7para ter prioridade ao entrar em\n&7partidas.\n \n&7Exclusivo para &fIRON\n \n&eClique para selecionar!";
      
      this.setItem(25, BukkitUtils.deserializeItemStack(itemString));
    }
  }
  
  public static class MenuExperienciaJogo extends PlayerMenu {
    
    public MenuExperienciaJogo(Profile profile) {
      super(profile.getPlayer(), "Experiência de Jogo", 4);
      
      // Ícone de anvil no slot 11 para Editor de Inventário
      this.setItem(11, BukkitUtils.deserializeItemStack(
          "ANVIL : 1 : nome>&aEditor de Inventário : desc>&7Gerencie seus itens favoritos\n&7da loja do Bed Wars.\n \n&eClique para acessar!"));
      
      // Botão de voltar centralizado na row 4 (slot 31)
      this.setItem(31, BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cVoltar"));
      
      this.register(Main.getInstance());
      this.open();
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
      if (evt.getInventory().equals(this.getInventory())) {
        evt.setCancelled(true);
        
        if (evt.getWhoClicked().equals(this.player)) {
          Profile profile = Profile.getProfile(this.player.getName());
          if (profile == null) {
            this.player.closeInventory();
            return;
          }
          
          if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
            ItemStack item = evt.getCurrentItem();
            
            if (item != null && item.getType() != Material.AIR) {
              if (evt.getSlot() == 11) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                new MenuEditorInventario(profile, null);
              } else if (evt.getSlot() == 31) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                new MenuShop(profile);
              }
            }
          }
        }
      }
    }
    
    public void cancel() {
      HandlerList.unregisterAll(this);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) {
      if (evt.getPlayer().equals(this.player)) {
        this.cancel();
      }
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent evt) {
      if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
        this.cancel();
      }
    }
  }
  
  public static class MenuEditorInventario extends PlayerMenu {
    
    private static final List<Integer> SLOTS = Arrays.asList(19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);
    private ShopCategory category;
    private Map<ItemStack, ShopItem> items = new HashMap<>();
    private Map<Integer, ShopCategory> categories = new HashMap<>();
    
    public MenuEditorInventario(Profile profile, ShopCategory category) {
      super(profile.getPlayer(), "Editor de Inventário" + (category == null ? "" : " - " + category.getName()), 6);
      this.category = category;
      
      int id = 1;
      this.setItem(0, BukkitUtils.putProfileOnSkull(player, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>&bEditor de Inventário : desc>&7Para adicionar um item aos favoritos,\n&7clique no item com Shift+Clique.\n \n&eClique para ver!")));
      for (ShopCategory sc : Shop.listCategories()) {
        this.setItem(id, sc.getIcon());
        this.categories.put(id++, sc);
      }
      
      int categoryId = Shop.getCategoryId(category);
      for (int i = 0; i < 9; i++) {
        this.setItem(9 + i, BukkitUtils.deserializeItemStack("STAINED_GLASS_PANE:" +
            (i == categoryId ? "13" : "7") + " : 1 : nome>&8↑ &7Categorias : desc>&8↓ &7Itens"));
      }
      
      FavoritesContainer preferences = profile.getAbstractContainer("bedwars", "favorites", FavoritesContainer.class);
      List<ShopItem> items = category == null ? null : category.listItems();
      if (category == null) {
        SLOTS.forEach(slot -> {
          if (!preferences.hasQuickBuy(slot)) {
            this.setItem(slot, BukkitUtils.deserializeItemStack(
                "STAINED_GLASS_PANE:14 : 1 : nome>&cSlot vazio! : desc>&7Esse é um slot de Compra Fácil!\n&bShift Clique &7em qualquer item na\n&7loja para adicioná-lo aqui."));
            return;
          }
          
          String fav = preferences.getQuickBuy(slot);
          ShopCategory favCategory = Shop.getCategoryById(Integer.parseInt(fav.split(":")[0]));
          if (favCategory != null) {
            ShopItem item = favCategory.getItem(fav.split(":")[1]);
            if (item != null) {
              ItemStack icon = BukkitUtils.deserializeItemStack(item.getIcon().replace("{color}", "&a").replace("{price}", "0").replace("{tier}", "I"));
              ItemMeta meta = icon.getItemMeta();
              List<String> lore = meta.getLore();
              if (lore != null) {
                lore.add("");
                lore.add("§bShift clique para remover");
                lore.add("§bdos favoritos!");
                meta.setLore(lore);
                icon.setItemMeta(meta);
              }
              this.setItem(slot, icon);
              this.items.put(icon, item);
              return;
            }
          }
          
          preferences.setQuickBuy(slot, null);
        });
      } else {
        for (int index = 0; index < SLOTS.size(); index++) {
          if (items.size() == index) {
            break;
          }
          
          ShopItem item = items.get(index);
          ItemStack icon = BukkitUtils.deserializeItemStack(item.getIcon().replace("{color}", "&a").replace("{price}", "0").replace("{tier}", "I"));
          ItemMeta meta = icon.getItemMeta();
          List<String> lore = meta.getLore();
          
          lore.add("");
          if (preferences.hasQuickBuy(categoryId + ":" + item.getName())) {
            lore.add("§bShift clique para remover");
          } else {
            lore.add("§bShift clique para adicionar");
          }
          lore.add("§baos favoritos!");
          meta.setLore(lore);
          icon.setItemMeta(meta);
          this.setItem(SLOTS.get(index), icon);
          this.items.put(icon, item);
        }
      }
      
      // Botão para voltar centralizado na row 6 (slot 49)
      this.setItem(49, BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cVoltar"));
      
      this.open();
      this.register(Main.getInstance());
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
      if (evt.getInventory().equals(this.getInventory())) {
        evt.setCancelled(true);
        
        if (evt.getWhoClicked().equals(this.player)) {
          Profile profile = Profile.getProfile(this.player.getName());
          if (profile == null) {
            this.player.closeInventory();
            return;
          }
          
          if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory()) && evt.getCurrentItem() != null && evt.getCurrentItem().getType() != Material.AIR) {
            ItemStack item = evt.getCurrentItem();
            ShopItem si;
            
            if (evt.getSlot() == 0) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuEditorInventario(profile, null);
              return;
            }
            
            if (evt.getSlot() == 49) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuExperienciaJogo(profile);
              return;
            }
            
            ShopCategory category = categories.get(evt.getSlot());
            if (category != null) {
              if (category != this.category) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                new MenuEditorInventario(profile, category);
              }
            } else if ((si = items.get(item)) != null) {
              if (evt.getClick().name().contains("SHIFT")) {
                FavoritesContainer preferences = profile.getAbstractContainer("bedwars", "favorites", FavoritesContainer.class);
                if (this.category == null && preferences.hasQuickBuy(evt.getSlot())) {
                  preferences.setQuickBuy(evt.getSlot(), null);
                  player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                  player.sendMessage("§aItem removido dos favoritos!");
                  new MenuEditorInventario(profile, this.category);
                } else if (this.category != null) {
                  int categoryId = Shop.getCategoryId(this.category);
                  if (preferences.hasQuickBuy(categoryId + ":" + si.getName())) {
                    preferences.setQuickBuy(preferences.getQuickBuy(categoryId + ":" + si.getName()), null);
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                    player.sendMessage("§aItem removido dos favoritos!");
                    new MenuEditorInventario(profile, this.category);
                  } else {
                    new MenuEditorInventarioSelect(profile, si, item);
                  }
                }
              }
            }
          }
        }
      }
    }
    
    public void cancel() {
      category = null;
      items.clear();
      items = null;
      categories.clear();
      categories = null;
      HandlerList.unregisterAll(this);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) {
      if (evt.getPlayer().equals(this.player)) {
        this.cancel();
      }
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent evt) {
      if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
        this.cancel();
      }
    }
  }
  
  public static class MenuEditorInventarioSelect extends PlayerMenu {
    
    private static final List<Integer> SLOTS = Arrays.asList(
        19, 20, 21, 22, 23, 24,
        25, 28, 29, 30, 31, 32,
        33, 34, 37, 38, 39,
        40, 41, 42, 43);
    private ShopItem item;
    
    public MenuEditorInventarioSelect(Profile profile, ShopItem item, ItemStack stack) {
      super(profile.getPlayer(), "Selecione um slot...", 6);
      this.item = item;
      
      this.setItem(4, stack);
      
      for (int i = 0; i < 9; i++) {
        this.setItem(9 + i, BukkitUtils.deserializeItemStack("STAINED_GLASS_PANE:7 : 1 : nome>&8↑ &7Item : desc>&8↓ &7Slots"));
      }
      
      FavoritesContainer preferences = profile.getAbstractContainer("bedwars", "favorites", FavoritesContainer.class);
      for (int slot : SLOTS) {
        if (preferences.hasQuickBuy(slot)) {
          String fav = preferences.getQuickBuy(slot);
          ShopItem favItem = Objects.requireNonNull(Shop.getCategoryById(
              Integer.parseInt(fav.split(":")[0]))).getItem(fav.split(":")[1]);
          ItemStack icon = BukkitUtils.deserializeItemStack(favItem.getIcon().replace("{color}", "§a").replace("{price}", "0").replace("{tier}", "I"));
          ItemMeta meta = icon.getItemMeta();
          List<String> lore = meta.getLore();
          lore.clear();
          lore.add("§7Este slot já está sendo");
          lore.add("§7utilizado por algum item!");
          lore.add("");
          lore.add("§7Tente clicar apenas no");
          lore.add("§7slots em §2verde§7.");
          meta.setLore(lore);
          icon.setItemMeta(meta);
          this.setItem(slot, icon);
        } else {
          this.setItem(slot, BukkitUtils.deserializeItemStack(
              "STAINED_GLASS_PANE:13 : 1 : nome>&aUtilizar este slot : desc>§7Ao clicar neste vidro você\n§7irá colocar o item neste slot.\n \n§eClique para utilizar este slot!"));
        }
      }
      
      this.open();
      this.register(Main.getInstance());
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
      if (evt.getInventory().equals(this.getInventory())) {
        evt.setCancelled(true);
        
        if (evt.getWhoClicked() instanceof Player && evt.getWhoClicked().equals(player)) {
          ItemStack item = evt.getCurrentItem();
          Profile profile = Profile.getProfile(this.player.getName());
          
          if (profile == null) {
            player.closeInventory();
            return;
          }
          
          if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())
              && item != null && item.getType() != Material.AIR) {
            if (SLOTS.contains(evt.getSlot())) {
              if (item.getDurability() == 13) {
                EnumSound.ORB_PICKUP.play(this.player, 1.0f, 1.0f);
                FavoritesContainer preferences = profile.getAbstractContainer("bedwars", "favorites",
                    FavoritesContainer.class);
                
                preferences.setQuickBuy(evt.getSlot(), Shop.getCategoryId(this.item.getCategory()) +
                    ":" + this.item.getName());
                player.sendMessage("§aItem adicionado aos favoritos!");
                new MenuEditorInventario(profile, null);
              }
            }
          }
        }
      }
    }
    
    public void cancel() {
      this.item = null;
      HandlerList.unregisterAll(this);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) {
      if (evt.getPlayer().equals(this.player)) {
        this.cancel();
      }
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent evt) {
      if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
        this.cancel();
      }
    }
  }
}
