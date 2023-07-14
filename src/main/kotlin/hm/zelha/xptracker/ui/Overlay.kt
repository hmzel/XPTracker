package hm.zelha.xptracker.ui

import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.provideDelegate
import gg.essential.universal.UKeyboard
import hm.zelha.xptracker.core.Config
import hm.zelha.xptracker.util.XPCalculator

class Overlay : UIContainer() {
    private var isDragging = false
    private var dragOffset = 0f to 0f
    private val text by UIText("Prestige Progress") childOf this

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
    }

    fun update(prestige: Int, level: Int, neededXP: Double) {
        val currentPrestigeXP = XPCalculator.getTotalXPForLevelAtPrestige(prestige, level, neededXP)
        val currentPrestigeRequiredXP = XPCalculator.getTotalPrestigeXP(prestige)
        val percent = currentPrestigeXP / currentPrestigeRequiredXP
        val xpString = String.format("%.0f/%.0f  %.0f%%", currentPrestigeXP, currentPrestigeRequiredXP, percent * 100)
        text.setText(xpString)
    }
}
