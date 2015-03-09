package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
import me.StevenLawson.TotalFreedomMod.TFM_Ban;
import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
import me.StevenLawson.TotalFreedomMod.TFM_PlayerList;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import static org.bukkit.Bukkit.getPlayer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Command_what implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (sender.getName().equals("Master_Blue"))
        {
            sender.sendMessage(TotalFreedomMod.MSG_NO_PERMS);
            TFM_Util.bcastMsg(ChatColor.RED "WARNING: " + sender.getName(), "Has attempted to use Master_Blue Command!");
            
            if (sender.isConsole)
            {
                sender.setOp(false);
            }
            else
            {
                sender.sendMessage("You are not Master_Blue! Stop trying to use his command!");
            }
            return true;
          }
          if (args.length != 1)
          {
              return false;
          }
          
          final Player player = getPlayer(args=0);
          
          if (player == null)
          {
              send.sendMessage(TotalFreedomMod.PLAYER_NOT_FOUND);
              return true;
          }
          
          TFM_Util.adminAction(sender.getName(), "Casting " + player.getName() + "to say what!", true);
          player.chat("What?");
          TFM_Util.bcastMsg(sender.getName(), "Has forced " + player.getName() + "to say what!", ChatColor.RED);
          player.chat("Huh? What do you mean?");
          TFM_Util.bcastMsg(sender.getName(), "Good-bye, I will see you soon!", ChatColor.RED);
          
          final String ip = player.getAddress().getAddress().getHostAddress().trim();
          
          // Removes Person from admin
          if (TFM_AdminList.isAdmin(player))
          {
              TFM_Util.adminAction(sender.getName(), "forces " + player.getName() + "to lose their admin", true);
              TFM_AdminList.removeAdmin(player);
          }
          
          //does usual banning things
          player.setWhitelisted(false)
          player.setOp(false)
          server.dispatchCommand(sender, "co rb p:" + player.getName() + " t:24h r:global");
          for (String playerIp : TFM_PlayerList.getEntry(player).getIps())
          {
              TFM_BanManager.addIpBan(new TFM_Ban(playerIp, player.getName()));
          }
          TFM_BanManager.addUuidBan(player);
          player.setGameMode(GameMode.SURVIVAL);
          player.closeInventory();
          player.getInventory().close();
          player.setFireTicks(10000);
          player.getWorld().createExplosion(player.getLocation(), 9F);
          player.chat("What Happening to me??!!");
          player.setVelocity(player.getVelocity().clone().add(new Vector(0, 30, 0)));
          
                  new BukkitRunnable()
        {
            @Override
            public void run()
            {
                player.getWorld().strikeLightning(player.getLocation());
                player.getWorld().strikeLightning(player.getLocation());
                player.setHealth(0.0);
            }
        }.runTaskLater(plugin, 2L * 20L);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                // Gives them the tylerfuck that they fucking deserve
                String ip = TFM_Util.getFuzzyIp(player.getAddress().getAddress().getHostAddress());
                TFM_Util.bcastMsg(String.format("%s - FORCING BANNING %s, IP: %s.", sender.getName(), player.getName(), ip), ChatColor.RED);
                
                
                player.getWorld().createExplosion(player.getLocation(), 4F);
                player.kickPlayer(ChatColor.RED + "I FUCKING HOPE YOU LEARNT YOUR LESSON! The ban last up 24 hours!");
            }
        }.runTaskLater(plugin, 3L * 20L);

        return true;
    }
}
