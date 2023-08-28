package fr.koopa.vanish;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class VanishClass implements CommandExecutor, Listener {

    private static final ArrayList<Player> vanished = new ArrayList<>();
    // /vanish <on|off|joueur>

    private boolean DEBUG = false;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {

            Player player = (Player) sender;

            if(!player.hasPermission("vanish.pix")) {

                player.sendMessage("§cVous n'avez pas la permission d'utiliser cette commande !");
            }

            if(player.hasPermission("vanish.pix")){

                if(args.length == 0) {

                    if(!vanished.contains(player)) {
                        vanished.add(player);

                        for(Player players : Bukkit.getOnlinePlayers()) {
                            players.hidePlayer(player);
                        }
                        player.setCollidable(false);
                        player.setGlowing(true);
                        if (DEBUG) { listVanished(player);}

                        for (Player vanishedPlayer : vanished) {

                                player.showPlayer(vanishedPlayer);
                                vanishedPlayer.showPlayer(player);
                        }

                        player.sendMessage("§aVous venez de vous vanish !");
                        return false;
                    }

                    if(vanished.contains(player)) {

                        vanished.remove(player);

                        for(Player players : Bukkit.getOnlinePlayers()) {
                            players.showPlayer(player);
                        }

                        for (Player vanishedPlayer : vanished) {
                                player.hidePlayer(vanishedPlayer);
                        }

                        player.setCollidable(true);
                        player.setGlowing(false);
                        if (DEBUG) { listVanished(player);}

                        player.sendMessage("§cVous venez de retirer votre vanish !");
                        return false;
                    }
                }else if(args.length == 1) {

                    if(args[0].equalsIgnoreCase("on")) {

                        if(vanished.contains(player)) {
                            player.sendMessage("§cVous êtes déjà en vanish !");
                            return false;
                        }

                        vanished.add(player);

                        for(Player players : Bukkit.getOnlinePlayers()) {
                            players.hidePlayer(player);
                        }

                        player.setCollidable(false);
                        player.setGlowing(true);
                        if (DEBUG) { listVanished(player);}

                        for (Player vanishedPlayer : vanished) {
                            if(vanished.size() >= 2) {

                                player.showPlayer(vanishedPlayer);
                                vanishedPlayer.showPlayer(player);

                            }
                        }

                        player.sendMessage("§aVous venez de vous mettre en vanish !");
                        return false;

                    }else if(args[0].equalsIgnoreCase("off")) {

                        if(!vanished.contains(player)) {
                            player.sendMessage("§cVous n'êtiez pas en vanish !");
                            return false;
                        }

                        for(Player players : Bukkit.getOnlinePlayers()) {
                            players.showPlayer(player);
                        }

                        for (Player vanishedPlayer : vanished) {
                            if(vanished.size() >= 2) {

                                player.hidePlayer(vanishedPlayer);

                            }
                        }

                        vanished.remove(player);
                        player.setCollidable(true);
                        player.setGlowing(false);
                        if (DEBUG) { listVanished(player);}

                        player.sendMessage("§cVous venez de vous devanish !");
                        return false;
                    }

                    Player target = Bukkit.getPlayer(args[0]);
                    if(target == null){
                        player.sendMessage("§cJoueur non trouvé ! §7Utilisation : §f/vanish on|off|joueur");
                        return false;
                    }

                    if(!vanished.contains(target)) {
                        vanished.add(target);

                        for(Player players : Bukkit.getOnlinePlayers()) {
                            players.hidePlayer(target);
                        }

                        for (Player vanishedPlayer : vanished) {
                            if(vanished.size() >= 2) {

                                target.showPlayer(vanishedPlayer);
                                vanishedPlayer.showPlayer(target);

                            }
                        }
                        target.setCollidable(false);
                        target.setGlowing(true);
                        if (DEBUG) { listVanished(player);}

                        player.sendMessage("§aVous venez de vanish " + args[0] + " !");
                        target.sendMessage("§aVous venez d'être vanish !");
                        return false;
                    }

                    if(vanished.contains(target)) {

                        for(Player players : Bukkit.getOnlinePlayers()) {
                            players.showPlayer(target);
                        }

                        for (Player vanishedPlayer : vanished) {
                            if(vanished.size() >= 2) {

                                target.hidePlayer(vanishedPlayer);
                                vanishedPlayer.showPlayer(target);

                            }
                        }
                        vanished.remove(target);
                        target.setCollidable(true);
                        target.setGlowing(false);
                        if (DEBUG) { listVanished(player);}

                        player.sendMessage("§cVous venez de retirer le vanish de " + args[0] + " !");
                        target.sendMessage("§cVous vous êtes fait retirer votre vanish !");
                        return false;
                    }
                }else{
                    player.sendMessage("§7/vanish <on|off|joueur>");
                    return false;
                }

            }

        }else{
            sender.sendMessage("§cVous devez exécuter cette commande en tant que joueur !");
            return false;
        }

        return false;
    }

    private void listVanished(Player thePlayer) {
        if (vanished.size()==0) {
            thePlayer.sendMessage("Aucun player n'est vanished");
        } else {
            thePlayer.sendMessage(vanished.size()+" player vanished");
            for (Player player : vanished) {
                thePlayer.sendMessage(player.getName() + " is vanished");
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        for(Player player : vanished){
            event.getPlayer().hidePlayer(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        vanished.remove(event.getPlayer());
        event.getPlayer().setGlowing(false);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getAction().equals(Action.PHYSICAL)){
            if(event.getClickedBlock().getType() == Material.GOLD_PLATE ||
                    event.getClickedBlock().getType() == Material.STONE_PLATE ||
                    event.getClickedBlock().getType() == Material.WOOD_PLATE ||
                    event.getClickedBlock().getType() == Material.IRON_PLATE){
                if (vanished.contains(player)) {

                    event.setCancelled(true);
                }
            }
        }
    }
}