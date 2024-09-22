package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.ui.theme.QuizAppTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                QuizApp()
            }
        }
    }
}


@Composable
fun QuizApp() {
    //Stores the current text in the OutlinedTextField
    var text = remember { mutableStateOf("") }
    //Saves the current text in the OutlinedTextField when a reminder is created
    var textStored = remember { mutableStateOf("") }
    //Establishes a host for the Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    //Establishes a coroutine scope for the Snackbar
    val coroutineScope = rememberCoroutineScope()
    //Stores the current question index
    var currentIndex = remember { mutableStateOf(0) }
    //List of questions and answers
    val questionList = remember { mutableStateListOf(
            "Shall I compare thee to a summer's day? ..." to "Thou art more lovely and more temperate",
            "My mistress' eyes are nothing like the sun; ..." to "Coral is far more red than her lips' red",
            "Love comforeth like sunshine after rain, ..." to "But Lust's effect is tempest after sun.",
            "Arma virumque cano" to "I sing of arms and a man",
            "Sing, oh muse, of the anger of Achilles" to "Aeneid"
        )
    }

    if (currentIndex.value < questionList.size) {
        val (question, answer) = questionList[(currentIndex.value)]

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            content = { padding ->
                Column(
                    Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Base card which is recomposed with each new question (on currentIndex incrementing)
                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(text = question, fontSize = 20.sp)
                        }
                    }
                    //Submission field for the user to input their answer
                    OutlinedTextField(
                        value = text.value,
                        onValueChange = { text.value = it },
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                    )
                    //Submit button which checks the user's answer
                    Button(
                        onClick = {
                            textStored.value = text.value
                            text.value = ""
                            if (textStored.value == answer) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Correct!",
                                    )
                                }
                                currentIndex.value++
                            } else {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Incorrect! Try again!",
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Check Your Answer!")
                    }
                }
            }
        )
    } else {
        //If the user has answered all the questions a button is displayed to restart the quiz
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            content = { padding ->
                Column(
                    Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            currentIndex.value = 0
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = "Would you like to try again?")
                    }
                }
            }
        )
    }
}