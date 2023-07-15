package hm.zelha.xptracker.ui

import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.provideDelegate
import gg.essential.universal.UKeyboard
import hm.zelha.xptracker.core.Config
import hm.zelha.xptracker.handler.StatTracker
import hm.zelha.xptracker.util.XPCalculator

class Overlay : UIContainer() {
    private var isDragging = false
    private var dragOffset = 0f to 0f

    private val prestigeProgress by UIText("Prestige Progress").constrain {
        y = SiblingConstraint()
    } childOf this

    private val levelProgress by UIText("Level Progress").constrain {
        y = SiblingConstraint()
    } childOf this

    init {
        onMouseClick { event ->
            if (!UKeyboard.isShiftKeyDown()) return@onMouseClick
            isDragging = true
            dragOffset = event.absoluteX to event.absoluteY
        }

        onMouseRelease {
            isDragging = false
            Config.INSTANCE.markDirty()
        }

        onMouseDrag { mouseX, mouseY, _ ->
            if (!isDragging) return@onMouseDrag
            val absoluteX = mouseX + getLeft()
            val absoluteY = mouseY + getTop()

            val deltaX = absoluteX - dragOffset.first
            val deltaY = absoluteY - dragOffset.second
            dragOffset = absoluteX to absoluteY

            val newX = this.getLeft() + deltaX
            val newY = this.getTop() + deltaY

            Config.INSTANCE.x = newX
            Config.INSTANCE.y = newY
            this.setY(newY.pixels())
            this.setX(newX.pixels())
        }


        constrain {
            x = Config.INSTANCE.x.pixels()
            y = Config.INSTANCE.y.pixels()
            width = ChildBasedMaxSizeConstraint()
            height = ChildBasedSizeConstraint()
        }

        registerConfigListeners()

        // We have to manually call this because the config listeners are only called when the config is loaded,
        // and we are already past that point, so it won't be called
        if (Config.INSTANCE.prestigeProgression) {
            prestigeProgress.unhide()
        } else {
            prestigeProgress.hide()
        }

        if (Config.INSTANCE.levelProgression) {
            levelProgress.unhide()
        } else {
            levelProgress.hide()
        }
    }

    fun updateAll(
        prestige: Int = StatTracker.INSTANCE.prestige,
        level: Int = StatTracker.INSTANCE.level,
        neededXP: Double = StatTracker.INSTANCE.xpToNextLevel
    ) {
        if (Config.INSTANCE.prestigeProgression) {
            updatePrestigeProgressionText(prestige, level, neededXP)
        }

        if (Config.INSTANCE.levelProgression) {
            updateLevelProgressionText(prestige, level, neededXP)
        }
    }

    private fun updatePrestigeProgressionText(
        prestige: Int = StatTracker.INSTANCE.prestige,
        level: Int = StatTracker.INSTANCE.level,
        neededXP: Double = StatTracker.INSTANCE.xpToNextLevel,
        format: String = Config.INSTANCE.prestigeProgressionFormat
    ) {
        val currentPrestigeRequiredXP = XPCalculator.getTotalPrestigeXP(prestige)
        val currentPrestigeXP = XPCalculator.getTotalXPForLevelAtPrestige(prestige, level, neededXP)
        val prestigePercent = currentPrestigeXP / currentPrestigeRequiredXP

        prestigeProgress.setText(
            applyReplacements(
                format,
                mapOf(
                    "{prestige}" to prestige.toString(),
                    "{prestige_roman}" to StatTracker.INSTANCE.prestigeRoman,
                    "{level}" to level.toString(),
                    "{prestige_xp}" to String.format("%,.0f", currentPrestigeXP),
                    "{prestige_xp_needed}" to String.format("%,.0f", currentPrestigeRequiredXP),
                    "{prestige_xp_progress}" to String.format("%.2f", prestigePercent * 100),
                    "&" to "§"
                )
            )
        )
    }

    private fun updateLevelProgressionText(
        prestige: Int = StatTracker.INSTANCE.prestige,
        level: Int = StatTracker.INSTANCE.level,
        neededXP: Double = StatTracker.INSTANCE.xpToNextLevel,
        format: String = Config.INSTANCE.levelProgressionFormat
    ) {
        val currentLevelRequiredXP = XPCalculator.getNeededXPForLevel(prestige, level)
        val currentLevelXP = currentLevelRequiredXP - neededXP
        val levelPercent = currentLevelXP / currentLevelRequiredXP

        levelProgress.setText(
            applyReplacements(
                format,
                mapOf(
                    "{level}" to level.toString(),
                    "{level_xp}" to String.format("%,.0f", currentLevelXP),
                    "{level_xp_needed}" to String.format("%,.0f", currentLevelRequiredXP),
                    "{level_xp_progress}" to String.format("%.2f", levelPercent * 100),
                    "&" to "§"
                )
            )
        )
    }

    private fun applyReplacements(text: String, replacements: Map<String, String>): String {
        var result = text
        for ((key, value) in replacements) {
            result = result.replace(key, value)
        }

        return result
    }

    private fun registerConfigListeners() {
        Config.INSTANCE.registerListener<Boolean>("prestigeProgression") { enabled ->
            if (enabled) {
                prestigeProgress.unhide()
                updatePrestigeProgressionText()
            } else {
                prestigeProgress.hide()
            }
        }

        Config.INSTANCE.registerListener<String>("prestigeProgressionFormat") { format ->
            // For some reason when opening the GUI Vigilance sets this value as empty, which then causes
            // issues like the text being entirely empty. This is a workaround for that.
            if (format == "") return@registerListener
            if (!Config.INSTANCE.prestigeProgression) return@registerListener
            updatePrestigeProgressionText(format = format)
        }

        Config.INSTANCE.registerListener<Boolean>("levelProgression") { enabled ->
            if (enabled) {
                levelProgress.unhide()
                updateLevelProgressionText()
            } else {
                levelProgress.hide()
            }
        }

        Config.INSTANCE.registerListener<String>("levelProgressionFormat") { format ->
            // See comment on prestigeProgressionFormat listener
            if (format == "") return@registerListener
            if (!Config.INSTANCE.levelProgression) return@registerListener
            updateLevelProgressionText(format = format)
        }
    }
}
