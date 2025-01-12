package com.al4apps.lists.vialackner

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HandlerEffect() {

}

@Composable
fun tryProduceState(count: Int): State<Int> {
    return produceState(initialValue = 0) {
        while (value < count) {
            delay(1000)
            value += 1
        }
    }
}

@Composable
fun TrySideEffect(name: String) {
    SideEffect {
        Log.d("TrySideEffect", "Name is now: $name")
    }
    Log.d("TrySideEffect", "Hello, $name!")
}

@Composable
fun TryLaunchedEffect() {
    LaunchedEffect(true) {
        delay(1000) // перезапускается при каждой рекомпозиции
        println("Do something")
    }
}

@Composable
fun TryRememberCoroutineScope() {
    val scope = rememberCoroutineScope()
    Button(onClick = {
        scope.launch {
            delay(1000) // maybe networking4
            println("Do something")
        }
    }) {
        Text("Do something")
    }
} // Better use viewmodelScope in ViewModel

@Composable
fun TryRememberUpdatedState(
    onTimeout: () -> Unit
) {
    val updatedOnTimeout by rememberUpdatedState(newValue = onTimeout)
    LaunchedEffect(true) {
        delay(3000)
//        onTimeout() - так делать нельзя, так как onTimeOut не будет обновлен
        updatedOnTimeout()
    }
}

@Composable
fun TryDisposableEffect() {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        // Код, который выполняется при создании Composable
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                println("On pause")
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer = observer)
        onDispose {
            // Код, который выполняется при удалении Composable
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}