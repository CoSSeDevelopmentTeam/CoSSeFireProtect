package net.comorevi.np.fireprotect;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.entity.mob.EntityCreeper;
import cn.nukkit.event.block.*;
import cn.nukkit.event.player.PlayerBucketEmptyEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.event.Listener;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.utils.TextFormat;

public class FireProtect extends PluginBase implements Listener {

    private static final String prefix = TextFormat.GRAY + "システム>>"+ TextFormat.RED + "FireProtect" + TextFormat.GRAY + ">> " + TextFormat.WHITE;
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		if (event.getEntity().getId() == EntityCreeper.NETWORK_ID && event.getPosition().getLevel().getName().equals("resource")) return;
		event.setCancelled();
		getServer().broadcastMessage(FireProtect.prefix + "爆発をキャンセルしました。\n - 座標情報: "+event.getPosition().getFloorX()+","+event.getPosition().getFloorY()+","+event.getPosition().getFloorZ()+","+event.getPosition().getLevel().getName());
	}
	
	@EventHandler
	public void onIgnite(BlockIgniteEvent event) {
		switch (event.getCause()) {
			case SPREAD:
			case LAVA:
				if (!event.getBlock().getLevel().getName().equals("resource")) getServer().broadcastMessage(FireProtect.prefix + "ブロックへの着火・延焼をキャンセルしました。");
			case LIGHTNING:
				event.setCancelled();
				break;
			case FLINT_AND_STEEL:
				event.setCancelled();
				getServer().broadcastMessage(FireProtect.prefix + "ブロックへの着火をキャンセルしました。\n - " + TextFormat.YELLOW + event.getEntity().getName() + TextFormat.WHITE + "が火打石を使用しました。");
				break;
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		switch (event.getBlock().getId()) {
			case Block.LAVA:
			case Block.WATER:
				if (!event.getPlayer().isOp()) {
					event.setCancelled();
					getServer().broadcastMessage(FireProtect.prefix + "マグマ・水ブロックの設置をキャンセルしました。\n - " + TextFormat.YELLOW + event.getPlayer().getName() + TextFormat.WHITE + "が制限されたアイテムを使用しました。");
				}
		}
	}

	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent event) {
		if (event.getBucket().getName().equals("Lava Bucket") && !event.getPlayer().isOp()) {
			event.setCancelled();
			getServer().broadcastMessage(FireProtect.prefix + "マグマは流せません。\n - " + TextFormat.YELLOW + event.getPlayer().getName() + TextFormat.WHITE + "がバケツからマグマを流そうとしました。");
		}
	}

	@EventHandler
	public void onLiquidFlow(LiquidFlowEvent event) {
		if (event.getSource().getId() == BlockID.LAVA) {
			event.setCancelled();
			if (!event.getBlock().getLevel().getName().equals("resource")) getServer().broadcastMessage(FireProtect.prefix + "マグマの拡大をキャンセルしました。\n - X:" + event.getSource().x + " ,Y:" + event.getSource().y + " ,Z:" + event.getSource().z + " , Level:" + event.getSource().level.getName());
			event.getSource().getLevel().setBlock(event.getSource().getLocation(), Block.get(BlockID.COBBLE));
		}
	}
}