package com.android.clubserve.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.clubserve.R
import com.android.clubserve.ui.components.AppLogo
import com.android.clubserve.ui.components.ClubServeTextField
import com.android.clubserve.ui.theme.ClubServeTheme

@Composable
fun LoginPage(
    viewModel: LoginViewModel = viewModel()
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()

    LoginContent(
        email = email,
        password = password,
        onEmailChange = viewModel::onEmailChanged,
        onPasswordChange = viewModel::onPasswordChanged,
        onLoginClick = viewModel::onLoginClicked,
        onForgotPasswordClick = viewModel::onForgotPasswordClicked
    )
}

@Composable
fun LoginContent(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(R.string.clubserve_logo_desc),
            modifier = Modifier.width(240.dp)
                .height(38.dp)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = stringResource(R.string.welcome_back),
            fontSize = 22.sp,
            style = TextStyle(
                fontWeight = FontWeight.W700,
                lineHeight = 28.sp,
                letterSpacing = 0.26.sp
            ),
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.email_address),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            ClubServeTextField(
                value = email,
                onValueChange = onEmailChange,
                placeholder = stringResource(R.string.enter_email_address)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.password),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            ClubServeTextField(
                value = password,
                onValueChange = onPasswordChange,
                placeholder = stringResource(R.string.enter_password),
                isPassword = true
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .width(140.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(text = stringResource(R.string.log_in), color = Color.White, fontSize = 18.sp)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = stringResource(R.string.forgot_password),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { onForgotPasswordClick() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPagePreview() {
    ClubServeTheme {
        LoginContent(
            email = "",
            password = "",
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onForgotPasswordClick = {}
        )
    }
}
