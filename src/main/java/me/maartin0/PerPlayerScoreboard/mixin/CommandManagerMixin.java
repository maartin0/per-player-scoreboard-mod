package me.maartin0.PerPlayerScoreboard.mixin;

import com.mojang.brigadier.CommandDispatcher;
import me.maartin0.PerPlayerScoreboard.PlayerScoreboardCommand;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin {
    @Shadow @Final private CommandDispatcher<ServerCommandSource> dispatcher;
    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(CallbackInfo info) {
        PlayerScoreboardCommand.register(dispatcher);
    }
}
