package com.android.clubserve.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.clubserve.R
import com.android.clubserve.ui.theme.ClubServeTheme
import com.android.clubserve.ui.theme.InputBackground
import com.android.clubserve.ui.theme.InputBorder

@Composable
fun ClubServeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {
    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(InputBackground, RoundedCornerShape(28.dp))
                        .border(1.dp, InputBorder, RoundedCornerShape(28.dp))
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Mastercard-like icon placeholder
                    Row {
                        Box(modifier = Modifier.size(16.dp).background(Color.Red, RoundedCornerShape(8.dp)))
                        Box(modifier = Modifier.offset(x = (-8).dp).size(16.dp).background(Color(0xFFFFA500).copy(alpha = 0.8f), RoundedCornerShape(8.dp)))
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(text = placeholder, color = Color.Gray, fontSize = 16.sp)
                        }
                        innerTextField()
                    }
                    
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.LightGray
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ClubServeTextFieldPreview() {
    ClubServeTheme {
        ClubServeTextField(
            value = "",
            onValueChange = {},
            placeholder = stringResource(R.string.enter_email_address)
        )
    }
}
