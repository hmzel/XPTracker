package hm.zelha.xptracker.ui

import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.provideDelegate
import hm.zelha.xptracker.handlers.StatTracker

class PrestigeProgressOverlay : UIContainer() {
    private val text by UIText("Prestige Progress") childOf this

    init {
        constrain {
            width = ChildBasedMaxSizeConstraint()
            height = ChildBasedSizeConstraint()
        }
    }

    fun update() {
        text.setText(StatTracker.INSTANCE.xpString)
    }
}
