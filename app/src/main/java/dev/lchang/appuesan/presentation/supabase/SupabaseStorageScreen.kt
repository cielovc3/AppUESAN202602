package dev.lchang.appuesan.presentation.supabase

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.lchang.appuesan.data.remote.SupabaseManager
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val BUCKET="imagenes"
@Composable
fun SupabaseStorageScreen(){
    val context= LocalContext.current
    val scope = rememberCoroutineScope()

    var subiendo by remember { mutableStateOf(value=false) }
    var urls by remember { mutableStateOf(value=emptyList<String>()) }
    var error by remember { mutableStateOf<String?>(value=null) }

    suspend fun cargarImagenes(){
        try{
            val archivos = SupabaseManager.client.storage.from(BUCKET).list()
            urls= archivos.map{archivo->
                SupabaseManager.client.storage.from(bucketId = BUCKET).publicUrl(path=archivo.name)
        }
            error = null

        }catch(e:Exception){
            error = e.message
        }
    }

    LaunchedEffect(key1 = Unit) { cargarImagenes() }

    //Permiso en tiemop de ejecución para cargar una imagen

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            subiendo = true
            error = null
            scope.launch {
                try{
                    val bytes = withContext(Dispatchers.IO){
                        context.contentResolver.openInputStream(uri)?.use{it.readBytes()}
                    } ?: throw Exception("No se pudo leer el archivo")

                    val nombreArchivo = "foto_${System.currentTimeMillis()}.jpg"
                    SupabaseManager.client.storage.from(bucketId = BUCKET).upload(path= nombreArchivo, data= bytes)
                    cargarImagenes()
                }catch(e:Exception){
                    error = e.message
                }finally{
                    subiendo = false
                }
            }
        }
    }

    //Mostrar imagenes en un LazyVerticalGrid
    Column(
        modifier = Modifier.fillMaxSize().padding(all=16.dp).statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Supabase Storage", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(height = 16.dp))
        Button(
            onClick = {
                pickImageLauncher.launch(
                    input = PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        ) {
            Text(if (subiendo) "Cargando..." else "Cargar imagen")
        }

        error?.let {
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(height = 16.dp))

        LazyVerticalGrid(columns = GridCells.Fixed(count = 2)) {
            items(urls) { url ->
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().padding().height(height = 128 .dp)
                )

            }

        }

    }
}
