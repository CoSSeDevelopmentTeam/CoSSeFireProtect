package net.comorevi.NoExplodeNoIgniteForNukkit;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.event.block.*;
import cn.nukkit.event.player.PlayerBucketEmptyEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.event.Listener;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.utils.TextFormat;

public class NoExplodeNoIgnite extends PluginBase implements Listener {

    private static final String prefix = TextFormat.GRAY + "システム>>"+ TextFormat.RED + "FireProtect" + TextFormat.GRAY + ">> " + TextFormat.WHITE;
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		if (event.getEntity().getName().equals("Creeper") && event.getPosition().getLevel().getName().equals("resource")) return;
		event.setCancelled();
		getServer().broadcastMessage(NoExplodeNoIgnite.prefix + "爆発をキャンセルしました。\n - 座標情報: "+event.getPosition().getFloorX()+","+event.getPosition().getFloorY()+","+event.getPosition().getFloorZ()+","+event.getPosition().getLevel().getName());
	}
	
	@EventHandler
	public void onIgnite(BlockIgniteEvent event) {
		switch (event.getCause()) {
			case SPREAD:
			case LAVA:
				event.setCancelled();
				getServer().broadcastMessage(NoExplodeNoIgnite.prefix + "ブロックへの延焼をキャンセルしました。");
				break;
			case FLINT_AND_STEEL:
				event.setCancelled();
				getServer().broadcastMessage(NoExplodeNoIgnite.prefix + "ブロックへの着火をキャンセルしました。\n - " + TextFormat.YELLOW + event.getEntity().getName() + TextFormat.WHITE + "が火打石を使用しました。");
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
					getServer().broadcastMessage(NoExplodeNoIgnite.prefix + "マグマ・水ブロックの設置をキャンセルしました。\n - " + TextFormat.YELLOW + event.getPlayer().getName() + TextFormat.WHITE + "が制限されたアイテムを使用しました。");
				}
		}
	}

	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent event) {
		if (event.getBucket().getName().equals("Lava Bucket") && !event.getPlayer().isOp()) {
			event.setCancelled();
			getServer().broadcastMessage(NoExplodeNoIgnite.prefix + "マグマは流せません。\n - " + TextFormat.YELLOW + event.getPlayer().getName() + TextFormat.WHITE + "がバケツからマグマを流そうとしました。");
		}
	}

	@EventHandler
	public void onLiquidFlow(LiquidFlowEvent event) {
		if (event.getSource().getId() == BlockID.LAVA) {
			event.setCancelled();
			getServer().broadcastMessage(NoExplodeNoIgnite.prefix + "マグマの拡大をキャンセルしました。\n - X:" + event.getSource().x + " ,Y:" + event.getSource().y + " ,Z:" + event.getSource().z + " , Level:" + event.getSource().level.getName());
			event.getSource().getLevel().setBlock(event.getSource().getLocation(), Block.get(BlockID.COBBLE));
		}
	}
}