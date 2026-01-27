package com.teoryul.mytesy.ui.appliancedetail

import com.teoryul.mytesy.ui.common.AppImage

enum class EcoMode(val label: String, val icon: AppImage) {
    EcoSmart("Eco Smart", AppImage.LeafIcon),
    EcoComfort("Eco Comfort", AppImage.TwoLeavesIcon),
    EcoNight("Eco Night", AppImage.ThreeLeavesIcon),
    Vacation("Vacation", AppImage.PalmTreesIcon),
    Off("Modes", AppImage.LeafIcon)
}