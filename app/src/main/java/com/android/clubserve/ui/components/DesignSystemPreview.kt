package com.android.clubserve.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.clubserve.ui.theme.ClubServeTheme

@Composable
fun DesignSystemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Title3EmphasizedText(text = "Title 3 Emphasized (20/25/-0.45)")
        
        HeadlineText(text = "Headline (17/22/-0.43)")
        
        HeadlineRegularCenteredText(text = "Headline Regular Centered (17/22/-0.43)")
        
        NormalText(text = "Normal (16/21/-0.31)")
        
        CalloutEmphasizedText(text = "Callout Emphasized (16/21/-0.31)")
        
        SubheadlineRegularText(text = "Subheadline Regular (15/20/-0.23)")
        
        SubheadlineEmphCenteredText(text = "Subheadline Emph Centered (15/20/-0.23)")
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Sample with colors
        NormalText(text = "Normal Text with Blue Color", color = Color.Blue)
        SubheadlineRegularText(text = "Subheadline Regular with Gray Color", color = Color.Gray)
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
fun DesignSystemPreview() {
    ClubServeTheme {
        DesignSystemScreen()
    }
}
