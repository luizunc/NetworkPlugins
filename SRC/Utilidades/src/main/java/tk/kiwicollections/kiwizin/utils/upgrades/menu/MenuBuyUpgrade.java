package tk.kiwicollections.kiwizin.utils.upgrades.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import tk.kiwicollections.kiwizin.utils.Language;
import tk.kiwicollections.kiwizin.utils.Main;
import tk.kiwicollections.kiwizin.utils.upgrades.object.Upgrade;
import tk.slicecollections.maxteer.Core;
import tk.slicecollections.maxteer.cash.CashException;
import tk.slicecollections.maxteer.cash.CashManager;
import tk.slicecollections.maxteer.libraries.menu.PlayerMenu;
import tk.slicecollections.maxteer.nms.NMS;
import tk.slicecollections.maxteer.player.Profile;
import tk.slicecollections.maxteer.utils.BukkitUtils;
import tk.slicecollections.maxteer.utils.StringUtils;
import tk.slicecollections.maxteer.utils.enums.EnumSound;

public class MenuBuyUpgrade extends PlayerMenu {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) throws CashException {
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
                        if (evt.getSlot() == 15) {
                            EnumSound.ENDERMAN_TELEPORT.play(this.player, 1.0F, 1.0F);
                            new MenuUpgrades(profile);
                        } else if (evt.getSlot() == 11) {
                            if (player.hasPermission(upgrade.getRole().getPermission())) {
                                player.sendMessage("§cVocê já possui este upgrade.");
                                return;
                            }
                            long cash = profile.getStats("mCoreProfile", "cash");

                            if (!(cash >= upgrade.getCash())) {
                                player.sendMessage("§cVocê não tem saldo suficiente.");
                                return;
                            }
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), upgrade.getCmd().replace("{player}", this.player.getName()));
                            Bukkit.getOnlinePlayers().forEach(playerr -> NMS.sendTitle(playerr, Language.upgrades$titles$header.replace("{player}", StringUtils.getFirstColor(upgrade.getRole().getName()) + " " + this.player.getName()), Language.upgrades$titles$footer.replace("{role}", upgrade.getRole().getName())));

                            CashManager.removeCash(profile, upgrade.getCash());
                            if (this.player.isOnline()) {
                                this.player.sendMessage(Language.upgrades$notification.replace("{role}", upgrade.getRole().getName().replace(" ", "")));
                            }
                            player.closeInventory();
                        }
                    }
                }
            }
        }
    }

    private Upgrade upgrade;

    public MenuBuyUpgrade(Profile profile, Upgrade upgrade) {
        super(profile.getPlayer(), "Confirmar compra", 3);

        this.upgrade = upgrade;

        this.setItem(11, BukkitUtils.deserializeItemStack(
                "STAINED_GLASS_PANE:13 : 1 : nome>&aConfirmar : desc>&7Comprar \"" + StringUtils.stripColors(upgrade.getRole().getName()) + "§7\"\n&7por &b" + StringUtils.formatNumber(upgrade.getCash()) + " Cash&7."));

        this.setItem(15, BukkitUtils.deserializeItemStack("STAINED_GLASS_PANE:14 : 1 : nome>&cCancelar : desc>&7Voltar para " + Main.getInstance().getConfig("upgrades").getString("title") + "."));

        this.register(Core.getInstance());
        this.open();
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
