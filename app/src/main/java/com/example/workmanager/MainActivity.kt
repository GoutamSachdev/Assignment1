package com.example.workmanager

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.workmanager.ui.theme.MyWorker
import com.example.workmanager.ui.theme.WorkManagerTheme
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.Executors

class MainActivity : ComponentActivity(), MyWorker.Callback  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                  // Greeting("Android")
                    //AppNavigation()
                    //fetchApi()
                    contactList()

                }
            }
        }
    }
    override fun onWorkCompleted(message: String) {
        // Update UI with success message
        // This function will be called from MyWorker
        // You can update the state of your Composable here
        Log.d("WOrkmanger",message)
    }
}
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "LoginScreen") {
        composable("LoginScreen") {
            LoginScreen(navController = navController, onLoginClick = {
                navController.navigate("LoginScreen")
            })
        }
        composable("fetchApi") {
            fetchApi()
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context= LocalContext.current
    val dataBuilder = Data.Builder()


    Column {
        Button(onClick = {
            dataBuilder.putString("email", "1111")
            dataBuilder.putString("count", "10")
            val inputData = dataBuilder.build()
            val request = OneTimeWorkRequestBuilder<MyWorker>()
                .setConstraints(Constraints(NetworkType.UNMETERED))
                .setInputData(inputData)
                .build()

            WorkManager.getInstance(context).enqueue(request)
        }) {
            Text("start Worker")
        }
    }
}
@Composable
fun fetchApi( modifier: Modifier = Modifier) {
    val context= LocalContext.current

    Column {
        Button(onClick = {
            val okHttp= OkHttpClient()
            val requestBody= FormBody.Builder()
                .add("title","SIBA")
                .add("price","123")
                .add("description","https://www.google.com/imgres?q=siba&imgurl=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fd%2Fd3%2FSIBA_Logo.jpg&imgrefurl=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FSukkur_IBA_University&docid=6hFqphEcW8jLYM&tbnid=6OAcpkMEOTuPeM&vet=12ahUKEwjxl5bO3tqFAxXyQvEDHbrVCwMQM3oECBMQAA..i&w=655&h=655&hcb=2&itg=1&ved=2ahUKEwjxl5bO3tqFAxXyQvEDHbrVCwMQM3oECBMQAA")
                .add("Image","SIBA")
                .add("category","University")
                .build()


            val request= Request.Builder()
                .url("https://fakestoreapi.com/products")
                .post(requestBody)
                .build()
            okHttp.newCall(request).enqueue(ResponseHandler())
        }) {
            Text("${GlobalStrings.myString}")
        }
    }
}
object GlobalStrings {
    var myString: String = "Hello, World!"

    fun resetMyString(newValue: String) {
        myString = newValue
    }
}
class ResponseHandler():Callback{
    override fun onResponse(call: Call, response: Response) {
        response.isSuccessful
       // Log.d("Response",response.body?.string()+response.isSuccessful?:"not found")
        GlobalStrings.resetMyString(response.body?.string()?:"not found")
    }

    override fun onFailure(call: Call, e: IOException) {

    }
}
@Composable
fun LoginScreen(navController: NavController ,onLoginClick: () -> Unit) {
    val context = LocalContext.current

    val dataBuilder = Data.Builder()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surface)
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surface)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                dataBuilder.putString("email", email)
                dataBuilder.putString("password", password)
                val inputData = dataBuilder.build()
                val request = OneTimeWorkRequestBuilder<MyWorker>()
                    .setConstraints(Constraints(NetworkType.UNMETERED))
                    .setInputData(inputData)
                    .build()


                val workManager = WorkManager.getInstance(context).enqueue(request)
                navController.navigate("fetchApi")

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(Color.Black)
        ) {
            Text("Login", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Not a member? Sign up now",
            color = Color.White,
            modifier = Modifier.clickable {

            }
        )
    }
}

@Composable
fun contactList(){
    val context = LocalContext.current
    val contacts= getALlcontact(context)
    LazyColumn(  verticalArrangement = Arrangement.SpaceEvenly,
        content ={
        items(contacts){
            Text(text = it.name, fontWeight =  FontWeight.Bold)
            Text(text = it.phoneNumber, fontWeight =  FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp).border()
            )
        }
    } )
}
fun getALlcontact(context: Context): List<Contact>{

    val contact = mutableListOf<Contact>()
    if(context.checkSelfPermission(android.Manifest.permission.READ_CONTACTS)
        != PackageManager.PERMISSION_GRANTED){

    }else{
        val uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection= arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER)

       val dataCursor= context.contentResolver.query(uri,
            projection,
            null,
            null,
            null)

        dataCursor.use {
            while(it!!.moveToNext()){
                val phone = it.getString(1)
                val name = it.getString(0)
                val contacts = Contact(name, phone)
                contact.add(contacts)
            }
        }
    }

    return contact
}
data class Contact (val name: String,val phoneNumber:String)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

    WorkManagerTheme {
        Greeting("Android")
    }
}