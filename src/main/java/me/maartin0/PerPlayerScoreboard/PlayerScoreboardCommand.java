package me.maartin0.PerPlayerScoreboard;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.ScoreboardObjectiveArgumentType;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PlayerScoreboardCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("scores")
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            if (source == null) return 1;
                            ServerPlayerEntity player = source.getPlayer();
                            if (player == null) return 1;
                            source.getPlayer().networkHandler.sendPacket(new ScoreboardDisplayS2CPacket(ScoreboardDisplaySlot.SIDEBAR, null));
                            source.sendFeedback(() -> Text.of("Cleared objectives in sidebar"), false);
                            return 0;
                        }).then(CommandManager.argument("objective", ScoreboardObjectiveArgumentType.scoreboardObjective())
                            .executes(context -> {
                                ServerCommandSource source = context.getSource();
                                if (source == null) return 1;
                                ServerPlayerEntity player = source.getPlayer();
                                if (player == null) return 1;
                                ServerScoreboard scoreboard = source.getServer().getScoreboard();
                                ScoreboardObjective newObjective = ScoreboardObjectiveArgumentType.getObjective(context, "objective");
                                scoreboard.addScoreboardObjective(newObjective);
                                source.getPlayer().networkHandler.sendPacket(new ScoreboardDisplayS2CPacket(ScoreboardDisplaySlot.SIDEBAR, newObjective));
                                source.sendFeedback(() -> Text.of(String.format("Set sidebar to display objective %s", newObjective.getName())), false);
                                return 0;
                            })
                        )
        );
    }
}
