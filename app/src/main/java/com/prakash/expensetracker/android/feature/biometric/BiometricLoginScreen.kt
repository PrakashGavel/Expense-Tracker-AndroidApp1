package com.prakash.expensetracker.android.feature.biometric

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.prakash.expensetracker.android.R
import com.prakash.expensetracker.android.widget.ExpenseTextView

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun BiometricLoginScreen(navController: NavController) {
    val context = LocalContext.current
    var isAuthenticationError by remember { mutableStateOf(false) }

    // Trigger biometric authentication when the composable is first displayed
    LaunchedEffect(Unit) {
        authenticateWithBiometrics(context, navController) {
            isAuthenticationError = true
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            // Constraint references for layout elements
            val (nameRow, list, card, topBar) = createRefs()

            // Top bar image
            Image(
                painter = painterResource(id = R.drawable.ic_topbar),  // Ensure the resource exists
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            // Box layout for other content, adjusted as per your layout requirement
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(topBar.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (isAuthenticationError) {
                        ExpenseTextView(
                            text = "Authentication failed. Please try again.",
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        ExpenseTextView(
                            text = "Place your finger on the sensor",
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
fun authenticateWithBiometrics(
    context: Context,
    navController: NavController,
    onError: () -> Unit
) {
    val biometricPrompt = BiometricPrompt(
        context as FragmentActivity,
        ContextCompat.getMainExecutor(context),
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                // Navigate to the HomeScreen after successful authentication
                navController.navigate("/home") {  // Ensure this route matches NavHost
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                onError()
                Toast.makeText(context, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
            }
        }
    )

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Expense Tracker App")
        .setNegativeButtonText("Use Password")
        .build()

    biometricPrompt.authenticate(promptInfo)
}

@Preview(showBackground = true)
@Composable
fun PreviewBiometricLoginScreen() {
    BiometricLoginScreen(rememberNavController())
}
