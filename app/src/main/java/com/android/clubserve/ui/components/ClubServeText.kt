package com.android.clubserve.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.android.clubserve.ui.theme.Inter
import com.android.clubserve.ui.theme.TextDarkGray

@Composable
fun RobotoText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = TextDarkGray,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = TextStyle.Default,
    textAlign: TextAlign = TextAlign.Start,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    Text(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        maxLines = maxLines,
        textAlign = textAlign,
        overflow = overflow,
    )
}

@Composable
fun Title3EmphasizedText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = TextDarkGray,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE
) {
    RobotoText(
        text = text,
        modifier = modifier,
        color = color,
        style = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 25.sp,
            letterSpacing = (-0.45).sp
        ),
        textAlign = textAlign,
        maxLines = maxLines
    )
}

@Composable
fun HeadlineText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = TextDarkGray,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE
) {
    RobotoText(
        text = text,
        modifier = modifier,
        color = color,
        style = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 17.sp,
            lineHeight = 22.sp,
            letterSpacing = (-0.43).sp
        ),
        textAlign = textAlign,
        maxLines = maxLines
    )
}

@Composable
fun HeadlineRegularCenteredText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = TextDarkGray,
    maxLines: Int = Int.MAX_VALUE
) {
    RobotoText(
        text = text,
        modifier = modifier,
        color = color,
        style = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 17.sp,
            lineHeight = 22.sp,
            letterSpacing = (-0.43).sp
        ),
        textAlign = TextAlign.Center,
        maxLines = maxLines
    )
}

@Composable
fun NormalText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = TextDarkGray,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE
) {
    RobotoText(
        text = text,
        modifier = modifier,
        color = color,
        style = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 21.sp,
            letterSpacing = (-0.31).sp
        ),
        textAlign = textAlign,
        maxLines = maxLines
    )
}

@Composable
fun CalloutEmphasizedText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = TextDarkGray,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE
) {
    RobotoText(
        text = text,
        modifier = modifier,
        color = color,
        style = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 21.sp,
            letterSpacing = (-0.31).sp
        ),
        textAlign = textAlign,
        maxLines = maxLines
    )
}

@Composable
fun SubheadlineRegularText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = TextDarkGray,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE
) {
    RobotoText(
        text = text,
        modifier = modifier,
        color = color,
        style = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            lineHeight = 20.sp,
            letterSpacing = (-0.23).sp
        ),
        textAlign = textAlign,
        maxLines = maxLines
    )
}

@Composable
fun SubheadlineEmphCenteredText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = TextDarkGray,
    maxLines: Int = Int.MAX_VALUE
) {
    RobotoText(
        text = text,
        modifier = modifier,
        color = color,
        style = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            lineHeight = 20.sp,
            letterSpacing = (-0.23).sp
        ),
        textAlign = TextAlign.Center,
        maxLines = maxLines
    )
}
