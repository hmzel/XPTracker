package hm.zelha.xptracker.mixin;

import hm.zelha.xptracker.core.Config;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {
    @Redirect(
        method = "renderScoreboard",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/scoreboard/ScorePlayerTeam;formatPlayerName(Lnet/minecraft/scoreboard/Team;Ljava/lang/String;)Ljava/lang/String;"
        )
    )
    public String removePlayerNames(Team team, String string) {
        if (team == null) return string;

        if (team instanceof ScorePlayerTeam && Config.INSTANCE.fixScoreboardSpacing) {
            return ((ScorePlayerTeam) team).getColorPrefix() + ((ScorePlayerTeam) team).getColorSuffix();
        } else {
            return team.formatString(string);
        }
    }
}
