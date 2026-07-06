package com.android.clubserve.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppLogo(
    size: Dp = 48.dp,
    color: Color = Color.Black
) {
    Canvas(modifier = Modifier.size(size)) {
        val strokeWidth = size.toPx() * 0.15f
        val w = size.toPx()
        val h = size.toPx()
        
        // Stylized 'S'
        // Top curve
        drawArc(
            color = color,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            topLeft = androidx.compose.ui.geometry.Offset(strokeWidth/2, strokeWidth/2),
            size = androidx.compose.ui.geometry.Size(w - strokeWidth, h/2 - strokeWidth/2)
        )
        
        // Middle line (implicitly part of arcs or separate?)
        // Let's draw it more accurately to the design: 
        // A continuous path would be better.
        
        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(w * 0.8f, h * 0.2f)
            quadraticTo(w * 0.8f, 0f, w * 0.5f, 0f)
            quadraticTo(w * 0.2f, 0f, w * 0.2f, h * 0.25f)
            quadraticTo(w * 0.2f, h * 0.5f, w * 0.5f, h * 0.5f)
            quadraticTo(w * 0.8f, h * 0.5f, w * 0.8f, h * 0.75f)
            quadraticTo(w * 0.8f, h, w * 0.5f, h)
            quadraticTo(w * 0.2f, h, w * 0.2f, h * 0.8f)
        }
        
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppLogoPreview() {
    AppLogo()
}
