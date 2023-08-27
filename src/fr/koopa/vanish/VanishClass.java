package fr.koopa.vanish;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class VanishClass implements CommandExecutor, Listener {

    private ArrayList<Player> vanished = new ArrayList<>();
    // /vanish <on|off|joueur>

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
                            player.setCollidable(false);
                        }

                        player.sendMessage("§aVous venez de vous vanish !");
                        return false;
                    }

                    if(vanished.contains(player)) {
                        vanished.remove(player);

                        for(Player players : Bukkit.getOnlinePlayers()) {
                            players.showPlayer(player);
                        }

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

                        player.sendMessage("§aVous venez de vous mettre en vanish !");
                        return false;

                    }else if(args[0].equalsIgnoreCase("off")) {

                        if(!vanished.contains(player)) {
                            player.sendMessage("§cVous n'êtiez pas en vanish !");
                            return false;
                        }

                        vanished.remove(player);

                        for(Player players : Bukkit.getOnlinePlayers()) {
                            players.showPlayer(player);
                        }

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

                        player.sendMessage("§aVous venez de vanish " + args[0] + " !");
                        target.sendMessage("§aVous venez d'être vanish !");
                        return false;
                    }

                    if(vanished.contains(target)) {
                        vanished.remove(target);

                        for(Player players : Bukkit.getOnlinePlayers()) {
                            players.showPlayer(target);
                        }

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

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        for(Player player : vanished){
            event.getPlayer().hidePlayer(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        vanished.remove(event.getPlayer());
    }
}