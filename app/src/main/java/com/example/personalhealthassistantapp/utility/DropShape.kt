package com.example.personalhealthassistantapp.utility

import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density

class DropShape : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: Density,
    ): Outline {
        val path = Path().apply {
            moveTo(size.width / 2f, 0f)
            cubicTo(
                size.width, size.height / 3f,
                size.width, size.height * 0.75f,
                size.width / 2f, size.height
            )
            cubicTo(
                0f, size.height * 0.75f,
                0f, size.height / 3f,
                size.width / 2f, 0f
            )
            close()
        }
        return Outline.Generic(path)   }
}
