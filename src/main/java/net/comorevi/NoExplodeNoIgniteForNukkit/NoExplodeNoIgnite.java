package net.comorevi.NoExplodeNoIgniteForNukkit;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLava;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.BlockUpdateEvent;
import cn.nukkit.event.player.PlayerBucketEmptyEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.event.Listener;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.block.BlockIgniteEvent;
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
		event.setCancelled();
		getServer().broadcastMessage(NoExplodeNoIgnite.prefix + "エンティティの爆発をキャンセルしました。");
	}
	
	@EventHandler
	public void onIgnite(BlockIgniteEvent event) {
		switch (event.getCause()) {
			case LAVA: 
			case SPREAD:
				event.setCancelled();
				getServer().broadcastMessage(NoExplodeNoIgnite.prefix + "ブロックへの着火をキャンセルしました。");
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
    public void onBlockSpread(BlockUpdateEvent event) {
        Block block = event.getBlock();
        if (block instanceof BlockLava) {
            event.setCancelled();
            getServer().broadcastMessage(NoExplodeNoIgnite.prefix + "マグマの拡大をキャンセルしました。\n - X:" + block.x + " ,Y:" + block.y + " ,Z:" + block.z + " , Level:" + block.level.getName());
        }
    }

}