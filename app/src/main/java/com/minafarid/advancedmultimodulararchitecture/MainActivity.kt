package com.minafarid.advancedmultimodulararchitecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.minafarid.advancedmultimodulararchitecture.ui.theme.AdvancedMultiModularArchitectureTheme
import com.shang.datastore.AppSettings
import com.shang.datastore.AppSettingsSerializer
import com.shang.datastore.Language
import com.shang.datastore.Location
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    lateinit var appSettingsDataStore: DataStore<AppSettings>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appSettingsDataStore = DataStoreFactory.create(
            serializer = AppSettingsSerializer(),
            produceFile = { dataStoreFile("app_settings.json") },
            scope = MainScope(),
        )

        enableEdgeToEdge()
        setContent {
            AdvancedMultiModularArchitectureTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = stringResource(R.string.app_name),
//                        data = DataProvider.userName,
//                        data2 = Shang.data,
//                        mapId = MapProvider.mapId,
//                        modifier = Modifier.padding(innerPadding),
//                    )
                    SettingScreen(appSettingsDataStore, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun SettingScreen(appSettingsDataStore: DataStore<AppSettings>, modifier: Modifier) {
    val scope = rememberCoroutineScope()
    val appSetting by appSettingsDataStore.data.collectAsState(initial = AppSettings())

    Column(modifier = modifier) {
        Text(text = "language : ${appSetting.language}")
        Text(text = "last location")
        appSetting.lastKnownLocation.forEach {
            Text("${it.lat} ${it.long}")
            Spacer(modifier = Modifier.height(16.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))

        val newLocation = Location(37.123, 122.908)
        Language.values().forEach { language ->
            DropdownMenuItem(text = { Text(language.name) }, onClick = {
                scope.launch {
                    appSettingsDataStore.updateData { currentSetting ->
                        currentSetting.copy(
                            language = language,
                            lastKnownLocation = currentSetting.lastKnownLocation.add(newLocation),
                        )
                    }
                }
            })
        }
    }
}

@Composable
fun Greeting(
    name: String,
    data: String,
    data2: String,
    mapId: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name! $data $data2 $mapId",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
//    AdvancedMultiModularArchitectureTheme {
//        Greeting("Android")
//    }
}
