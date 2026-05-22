package com.nammamela.app

import android.graphics.BitmapFactory
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nammamela.app.ui.theme.NammaMelaTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NammaMelaTheme {
                NammaMelaApp()
            }
        }
    }
}

private const val ALLOWED_MANAGER_EMAIL = "kavyanaidu2005@gmail.com"

private object Routes {
    const val Welcome = "welcome"
    const val CustomerLogin = "customerLogin"
    const val ManagerLogin = "managerLogin"
    const val Home = "home"
    const val Detail = "detail/{showId}"
    const val Cast = "cast/{showId}"
    const val Seats = "seats/{showId}"
    const val Confirmation = "confirmation/{showId}/{total}"
    const val ManagerDashboard = "managerDashboard"
    const val AddShow = "addShow"
    const val EditShow = "editShow/{showId}"

    fun detail(showId: String) = "detail/$showId"
    fun cast(showId: String) = "cast/$showId"
    fun seats(showId: String) = "seats/$showId"
    fun confirmation(showId: String, total: Int) = "confirmation/$showId/$total"
    fun editShow(showId: String) = "editShow/$showId"
}

@Composable
private fun NammaMelaApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("namma_mela_prefs", android.content.Context.MODE_PRIVATE) }
    val appState = remember {
        val savedShows = decodeShows(prefs.getString("shows", "").orEmpty())
        val savedFanPosts = decodeFanPosts(prefs.getString("fan_posts", "").orEmpty())
        NammaMelaAppState(
            initialManagerEmail = prefs.getString("manager_email", ALLOWED_MANAGER_EMAIL).orEmpty(),
            initialManagerHistory = prefs.getString("manager_history", "").orEmpty()
                .split("|")
                .filter { it.isNotBlank() },
            initialCustomerPhone = prefs.getString("customer_phone", "").orEmpty(),
            initialBookings = decodeBookings(prefs.getString("bookings", "").orEmpty()),
            initialShows = savedShows.ifEmpty { NammaMelaRepository.todayShows },
            initialFanPosts = savedFanPosts.ifEmpty { NammaMelaRepository.defaultFanPosts }
        )
    }
    var kannada by remember { mutableStateOf(false) }
    var pendingSeats by remember { mutableStateOf<List<Seat>>(emptyList()) }

    LaunchedEffect(appState.managerEmail, appState.managerHistory.joinToString("|")) {
        prefs.edit()
            .putString("manager_email", appState.managerEmail)
            .putString("manager_history", appState.managerHistory.joinToString("|"))
            .apply()
    }

    LaunchedEffect(appState.customerPhone, appState.bookings.joinToString("|") { it.storageKey() }) {
        prefs.edit()
            .putString("customer_phone", appState.customerPhone)
            .putString("bookings", encodeBookings(appState.bookings))
            .apply()
    }

    LaunchedEffect(appState.shows.joinToString("|") { it.storageKey() }) {
        prefs.edit()
            .putString("shows", encodeShows(appState.shows))
            .apply()
    }

    LaunchedEffect(appState.fanPosts.joinToString("|") { it.storageKey() }) {
        prefs.edit()
            .putString("fan_posts", encodeFanPosts(appState.fanPosts))
            .apply()
    }

    NavHost(navController = navController, startDestination = Routes.Welcome) {
        composable(Routes.Welcome) {
            WelcomeScreen(
                kannada = kannada,
                onToggleLanguage = { kannada = !kannada },
                onCustomer = { navController.navigate(Routes.CustomerLogin) },
                onManager = { navController.navigate(Routes.ManagerLogin) }
            )
        }
        composable(Routes.CustomerLogin) {
            CustomerLoginScreen(
                kannada = kannada,
                onBack = navController::popBackStack,
                onVerified = { phone ->
                    appState.customerPhone = phone
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Welcome)
                    }
                }
            )
        }
        composable(Routes.ManagerLogin) {
            ManagerLoginScreen(
                savedEmail = appState.managerEmail,
                onBack = navController::popBackStack,
                onLogin = { email ->
                    appState.saveManagerEmail(email)
                    navController.navigate(Routes.ManagerDashboard) {
                        popUpTo(Routes.Welcome)
                    }
                }
            )
        }
        composable(Routes.Home) {
            HomeScreen(
                kannada = kannada,
                appState = appState,
                onShowClick = { navController.navigate(Routes.detail(it)) },
                onBookClick = { navController.navigate(Routes.seats(it)) }
            )
        }
        composable(
            route = Routes.Detail,
            arguments = listOf(navArgument("showId") { type = NavType.StringType })
        ) { entry ->
            val showId = entry.arguments?.getString("showId").orEmpty()
            PlayDetailScreen(
                show = appState.show(showId),
                onBack = navController::popBackStack,
                onCast = { navController.navigate(Routes.cast(showId)) },
                onBook = { navController.navigate(Routes.seats(showId)) }
            )
        }
        composable(
            route = Routes.Cast,
            arguments = listOf(navArgument("showId") { type = NavType.StringType })
        ) { entry ->
            CastScreen(
                show = appState.show(entry.arguments?.getString("showId").orEmpty()),
                onBack = navController::popBackStack
            )
        }
        composable(
            route = Routes.Seats,
            arguments = listOf(navArgument("showId") { type = NavType.StringType })
        ) { entry ->
            val showId = entry.arguments?.getString("showId").orEmpty()
            SeatSelectionScreen(
                show = appState.show(showId),
                seats = appState.seatsFor(showId),
                onBack = navController::popBackStack,
                onConfirm = { seats ->
                    pendingSeats = seats
                    val total = BookingRules.calculateTotal(seats)
                    appState.bookSeats(showId, seats)
                    navController.navigate(Routes.confirmation(showId, total))
                }
            )
        }
        composable(
            route = Routes.Confirmation,
            arguments = listOf(
                navArgument("showId") { type = NavType.StringType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { entry ->
            BookingConfirmationScreen(
                show = appState.show(entry.arguments?.getString("showId").orEmpty()),
                seats = pendingSeats,
                total = entry.arguments?.getInt("total") ?: 0,
                onHome = {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Home) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.ManagerDashboard) {
            ManagerDashboardScreen(
                appState = appState,
                onAddShow = { navController.navigate(Routes.AddShow) },
                onEditShow = { navController.navigate(Routes.editShow(it)) },
                onLogout = {
                    navController.navigate(Routes.Welcome) {
                        popUpTo(Routes.ManagerDashboard) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.AddShow) {
            AddShowScreen(
                existingShow = null,
                onBack = navController::popBackStack,
                onSave = { show ->
                    appState.addShow(show)
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Routes.EditShow,
            arguments = listOf(navArgument("showId") { type = NavType.StringType })
        ) { entry ->
            val showId = entry.arguments?.getString("showId").orEmpty()
            AddShowScreen(
                existingShow = appState.show(showId),
                onBack = navController::popBackStack,
                onSave = { show ->
                    appState.updateShow(show)
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
private fun WelcomeScreen(
    kannada: Boolean,
    onToggleLanguage: () -> Unit,
    onCustomer: () -> Unit,
    onManager: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(onClick = onToggleLanguage) {
                    Icon(Icons.Default.Language, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (kannada) "ಕನ್ನಡ" else "English")
                }
            }

            Spacer(Modifier.weight(1f))
            AppLogoMark()
            Spacer(Modifier.height(18.dp))
            Text("NAMMA-MELA", fontSize = 34.sp, fontWeight = FontWeight.Black)
            Text(
                if (kannada) "ನಾಟಕವನ್ನು ಜೀವಂತಗೊಳಿಸುವುದು" else "Bringing Drama to Life",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.weight(1f))
            LargeActionButton(
                text = if (kannada) "ನಾನು ಗ್ರಾಹಕ" else "I'm a Customer",
                icon = Icons.Default.Person,
                onClick = onCustomer
            )
            Spacer(Modifier.height(12.dp))
            LargeActionButton(
                text = if (kannada) "ನಾನು ಮ್ಯಾನೇಜರ್" else "I'm a Manager",
                icon = Icons.Default.Settings,
                onClick = onManager
            )
        }
    }
}

@Composable
private fun CustomerLoginScreen(kannada: Boolean, onBack: () -> Unit, onVerified: (String) -> Unit) {
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }
    var generatedOtp by remember { mutableStateOf("") }
    var otpError by remember { mutableStateOf<String?>(null) }

    AuthScaffold(title = if (kannada) "ಗ್ರಾಹಕ ಲಾಗಿನ್" else "Customer Login", onBack = onBack) {
        Text(
            if (otpSent) "OTP sent to +91 ${phone.take(10)}" else "Welcome to Namma-Mela",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(18.dp))
        if (!otpSent) {
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it.filter(Char::isDigit).take(10) },
                label = { Text(if (kannada) "ಮೊಬೈಲ್ ಸಂಖ್ಯೆ" else "Mobile number") },
                prefix = { Text("+91 ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Button(
                enabled = phone.length == 10,
                onClick = {
                    generatedOtp = Random.nextInt(100000, 999999).toString()
                    otp = ""
                    otpError = null
                    otpSent = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (kannada) "OTP ಕಳುಹಿಸಿ" else "Send OTP")
            }
        } else {
            OutlinedTextField(
                value = otp,
                onValueChange = { otp = it.filter(Char::isDigit).take(6) },
                label = { Text("6 digit OTP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Demo OTP sent to +91 $phone: $generatedOtp",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            otpError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
            }
            TextButton(onClick = { otpSent = false }) {
                Text("Edit phone number")
            }
            Spacer(Modifier.height(8.dp))
            Button(
                enabled = otp.length == 6,
                onClick = {
                    if (otp == generatedOtp) {
                        onVerified(phone)
                    } else {
                        otpError = "Invalid OTP. Enter the OTP sent to this number."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (kannada) "ಪರಿಶೀಲಿಸಿ ಮುಂದುವರಿಸಿ" else "Verify & Continue")
            }
        }
    }
}

@Composable
private fun ManagerLoginScreen(savedEmail: String, onBack: () -> Unit, onLogin: (String) -> Unit) {
    var email by remember { mutableStateOf(savedEmail.ifBlank { ALLOWED_MANAGER_EMAIL }) }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }

    AuthScaffold(title = "Manager Login", onBack = onBack) {
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it.trim()
                emailError = null
            },
            label = { Text("Email") },
            supportingText = { Text("Only $ALLOWED_MANAGER_EMAIL is allowed") },
            isError = emailError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        emailError?.let {
            Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(Icons.Default.Visibility, contentDescription = "Show password")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(18.dp))
        Button(
            enabled = email.isNotBlank() && password.length >= 4,
            onClick = {
                if (email.equals(ALLOWED_MANAGER_EMAIL, ignoreCase = true)) {
                    onLogin(ALLOWED_MANAGER_EMAIL)
                } else {
                    emailError = "This app allows only $ALLOWED_MANAGER_EMAIL for manager login."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        TextButton(onClick = {}) {
            Text("Forgot Password?")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    kannada: Boolean,
    appState: NammaMelaAppState,
    onShowClick: (String) -> Unit,
    onBookClick: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("NAMMA-MELA", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Menu, contentDescription = "Menu") }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Notifications, contentDescription = "Notifications") }
                    IconButton(onClick = {}) { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") }
                }
            )
        },
        bottomBar = { CustomerBottomBar(selectedTab = selectedTab, onTabSelected = { selectedTab = it }) }
    ) { padding ->
        when (selectedTab) {
            0, 1 -> ShowsTab(
                padding = padding,
                title = if (selectedTab == 0) "Tonight's Shows" else "All Plays",
                kannada = kannada,
                appState = appState,
                onShowClick = onShowClick,
                onBookClick = onBookClick
            )
            2 -> BookingsTab(padding = padding, appState = appState)
            3 -> FanWallTab(padding = padding, appState = appState)
            4 -> CustomerProfileTab(padding = padding, appState = appState)
        }
    }
}

@Composable
private fun ShowsTab(
    padding: PaddingValues,
    title: String,
    kannada: Boolean,
    appState: NammaMelaAppState,
    onShowClick: (String) -> Unit,
    onBookClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                if (kannada && title == "Tonight's Shows") "ಇಂದಿನ ಪ್ರದರ್ಶನಗಳು" else title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black
            )
            Text(
                LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy")),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))
            DiscoveryStrip()
        }
        if (appState.shows.isNotEmpty()) {
            val featuredShow = appState.shows.first()
            item {
                FeaturedPosterAd(
                    show = featuredShow,
                    seatsAvailable = appState.availableSeatCount(featuredShow.id),
                    kannada = kannada,
                    onClick = { onShowClick(featuredShow.id) },
                    onBook = { onBookClick(featuredShow.id) }
                )
            }
            item {
                PosterAdRail(
                    shows = appState.shows,
                    kannada = kannada,
                    onShowClick = onShowClick,
                    onBookClick = onBookClick
                )
            }
            item {
                TrustAndSafetyCard()
            }
        }
        items(appState.shows, key = { it.id }) { show ->
            ShowCard(
                show = show,
                seatsAvailable = appState.availableSeatCount(show.id),
                kannada = kannada,
                onClick = { onShowClick(show.id) },
                onBook = { onBookClick(show.id) }
            )
        }
    }
}

@Composable
private fun BookingsTab(padding: PaddingValues, appState: NammaMelaAppState) {
    val customerBookings = appState.bookingsForCurrentCustomer()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("My Bookings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
        }
        if (customerBookings.isEmpty()) {
            item { EmptyState("No bookings yet", "Book a drama and your tickets will appear here.") }
        } else {
            items(customerBookings, key = { it.id }) { booking ->
                val show = appState.show(booking.showId)
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(show.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text("Seats: ${booking.seatLabels.joinToString(", ")}")
                        Text("Paid: ₹${booking.total}")
                        Text("Booked for +91 ${booking.phone}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun FanWallTab(padding: PaddingValues, appState: NammaMelaAppState) {
    var post by remember { mutableStateOf("") }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Fan Wall", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text("Share reviews, wishes, and drama memories.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            OutlinedTextField(
                value = post,
                onValueChange = { post = it.take(160) },
                label = { Text("Write something") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(
                enabled = post.isNotBlank(),
                onClick = {
                    appState.addFanPost(post)
                    post = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Post")
            }
        }
        items(appState.fanPosts, key = { it.id }) { fanPost ->
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
                Column(Modifier.padding(14.dp)) {
                    Text(fanPost.author, fontWeight = FontWeight.Bold)
                    Text(fanPost.message)
                }
            }
        }
    }
}

@Composable
private fun CustomerProfileTab(padding: PaddingValues, appState: NammaMelaAppState) {
    val customerBookings = appState.bookingsForCurrentCustomer()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Profile", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Avatar("Customer")
                    Text("Customer", fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleLarge)
                    Text("+91 ${appState.customerPhone.ifBlank { "Not logged in" }}")
                    Text("${customerBookings.size} previous bookings")
                }
            }
        }
        item {
            SectionTitle("Previous Data")
            Text(
                if (customerBookings.isEmpty()) "No previous ticket data yet for this number." else "Your latest bookings for this number are saved in My Bookings.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayDetailScreen(show: DramaShow, onBack: () -> Unit, onCast: () -> Unit, onBook: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(show.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite") }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = onBook,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Icon(Icons.Default.EventSeat, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Book Seats")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { PosterBanner(show) }
            item {
                Text(show.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                RatingLine(show.rating, show.reviewCount)
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    InfoCard("Date", "Today", Icons.Default.CalendarToday, Modifier.weight(1f))
                    InfoCard("Time", show.time, Icons.Default.TheaterComedy, Modifier.weight(1f))
                    InfoCard("Venue", show.venue, Icons.Default.LocationOn, Modifier.weight(1f))
                }
            }
            item {
                SectionTitle("Pricing")
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PriceChip("Front", show.pricing.front)
                    PriceChip("Middle", show.pricing.middle)
                    PriceChip("Back", show.pricing.back)
                }
            }
            item {
                SectionTitle("About")
                Text(show.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            item {
                SectionTitle("Cast")
                CastPreview(show.cast)
                TextButton(onClick = onCast) { Text("View Full Cast") }
            }
            item {
                SectionTitle("Reviews")
                show.reviews.take(2).forEach { ReviewRow(it) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CastScreen(show: DramaShow, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cast Members") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(show.cast) { member ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ActorAvatar(member)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(member.name, fontWeight = FontWeight.Bold)
                            Text("${member.character} • ${member.role}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SeatSelectionScreen(
    show: DramaShow,
    seats: List<Seat>,
    onBack: () -> Unit,
    onConfirm: (List<Seat>) -> Unit
) {
    val selected = remember { mutableStateListOf<Seat>() }
    val total = BookingRules.calculateTotal(selected)
    var showConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Seats") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Row(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("${selected.size} seats")
                        Text("₹$total", fontWeight = FontWeight.Black, fontSize = 22.sp)
                    }
                    Button(
                        enabled = selected.isNotEmpty() && BookingRules.isSelectionAllowed(selected),
                        onClick = { showConfirmDialog = true }
                    ) {
                        Text("Confirm Booking")
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(show.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(show.venue, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(18.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text("STAGE", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(18.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                seats.forEach { seat ->
                    val isSelected = selected.contains(seat)
                    SeatButton(
                        seat = seat,
                        selected = isSelected,
                        onClick = {
                            if (seat.available) {
                                if (isSelected) selected.remove(seat) else selected.add(seat)
                            }
                        }
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Legend("Available", Color(0xFF2F7D32))
                Legend("Selected", MaterialTheme.colorScheme.primary)
                Legend("Booked", Color(0xFF9CA3AF))
            }
            if (!BookingRules.isSelectionAllowed(selected)) {
                Spacer(Modifier.height(12.dp))
                Text("Maximum 10 seats per booking", color = MaterialTheme.colorScheme.error)
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirm booking?") },
            text = {
                Text("Book ${selected.size} seats (${selected.joinToString(", ") { it.label }}) for ₹$total?")
            },
            confirmButton = {
                Button(onClick = {
                    showConfirmDialog = false
                    onConfirm(selected.toList())
                }) {
                    Text("Yes, Book")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun BookingConfirmationScreen(show: DramaShow, seats: List<Seat>, total: Int, onHome: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(112.dp)
                .clip(CircleShape)
                .background(Color(0xFF2F7D32)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.BookOnline, contentDescription = null, tint = Color.White, modifier = Modifier.size(58.dp))
        }
        Spacer(Modifier.height(20.dp))
        Text("Booking Confirmed", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
        Text(show.name, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (seats.isNotEmpty()) {
            Text("Seats: ${seats.joinToString(", ") { it.label }}", fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(16.dp))
        Card(border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
            Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Ticket Total", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("₹$total", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
                Divider(Modifier.padding(vertical = 12.dp))
                Text("QR ticket placeholder", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(onClick = onHome, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Home")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManagerDashboardScreen(
    appState: NammaMelaAppState,
    onAddShow: () -> Unit,
    onEditShow: (String) -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Manager Dashboard") },
                actions = { TextButton(onClick = onLogout) { Text("Logout") } }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text("Signed in as ${appState.managerEmail}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (appState.managerHistory.isNotEmpty()) {
                    Text("Login history: ${appState.managerHistory.joinToString(", ")}", fontSize = 12.sp)
                }
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricCard("Shows", appState.shows.size.toString().padStart(2, '0'), Icons.Default.TheaterComedy, Modifier.weight(1f))
                    MetricCard("Seats", appState.bookedSeatKeys.size.toString(), Icons.Default.Chair, Modifier.weight(1f))
                    MetricCard("Revenue", "₹8.4k", Icons.Default.Dashboard, Modifier.weight(1f))
                }
            }
            item {
                Button(onClick = onAddShow, modifier = Modifier.fillMaxWidth()) {
                    Text("Add New Show")
                }
            }
            item {
                SectionTitle("Previous Bookings")
                if (appState.bookings.isEmpty()) {
                    Text("No customer bookings yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            items(appState.bookings.take(5), key = { it.id }) { booking ->
                val bookedShow = appState.show(booking.showId)
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(bookedShow.name, fontWeight = FontWeight.Bold)
                        Text("Customer: +91 ${booking.phone}")
                        Text("Seats: ${booking.seatLabels.joinToString(", ")} | Rs ${booking.total}")
                    }
                }
            }
            item {
                SectionTitle("Shows")
            }
            items(appState.shows, key = { it.id }) { show ->
                val booked = appState.bookedSeatCount(show.id)
                val available = appState.availableSeatCount(show.id)
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Avatar(show.name)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(show.name, fontWeight = FontWeight.Bold)
                            Text("Booked: $booked seats")
                            Text("Available: $available/${show.totalSeats} seats", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { onEditShow(show.id) }) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
                        IconButton(onClick = { appState.deleteShow(show.id) }) { Icon(Icons.Default.Delete, contentDescription = "Delete") }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddShowScreen(existingShow: DramaShow?, onBack: () -> Unit, onSave: (DramaShow) -> Unit) {
    val context = LocalContext.current
    var name by remember(existingShow?.id) { mutableStateOf(existingShow?.name.orEmpty()) }
    var venue by remember(existingShow?.id) { mutableStateOf(existingShow?.venue.orEmpty()) }
    var time by remember(existingShow?.id) { mutableStateOf(existingShow?.time.orEmpty()) }
    var duration by remember(existingShow?.id) { mutableStateOf(existingShow?.durationMinutes?.toString().orEmpty()) }
    var price by remember(existingShow?.id) { mutableStateOf(existingShow?.pricing?.middle?.toString().orEmpty()) }
    var persona by remember(existingShow?.id) { mutableStateOf(existingShow?.description.orEmpty()) }
    var leadActorName by remember(existingShow?.id) {
        mutableStateOf(existingShow?.cast?.firstOrNull { it.role.equals("Lead", true) || it.role.equals("Hero", true) }?.name.orEmpty())
    }
    var posterUri by remember(existingShow?.id) { mutableStateOf(existingShow?.posterUri?.let(Uri::parse)) }
    var leadActorPhotoUri by remember(existingShow?.id) { mutableStateOf(existingShow?.leadActorPhotoUri?.let(Uri::parse)) }
    val posterPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let { persistReadPermission(context, it) }
            posterUri = uri
        }
    )
    val leadActorPhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let { persistReadPermission(context, it) }
            leadActorPhotoUri = uri
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingShow == null) "Add Show" else "Edit Show") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(name, { name = it }, label = { Text("Play name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(venue, { venue = it }, label = { Text("Venue") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(time, { time = it }, label = { Text("Start time") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                duration,
                { duration = it.filter(Char::isDigit).take(4) },
                label = { Text("Duration in minutes") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                price,
                { price = it.filter(Char::isDigit) },
                label = { Text("Base ticket price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                persona,
                { persona = it },
                label = { Text("Persona / about the show") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                leadActorName,
                { leadActorName = it },
                label = { Text("Lead actor name") },
                modifier = Modifier.fillMaxWidth()
            )
            PosterUploadCard(
                title = "Lead Actor Photo",
                emptyText = "No lead actor photo selected",
                buttonText = "Add Actor Photo",
                changedButtonText = "Change Actor Photo",
                icon = Icons.Default.PhotoCamera,
                posterUri = leadActorPhotoUri,
                onPickPoster = { leadActorPhotoPicker.launch(arrayOf("image/*")) },
                onRemovePoster = { leadActorPhotoUri = null }
            )
            PosterUploadCard(
                title = "Show Poster",
                emptyText = "No poster selected",
                buttonText = "Upload Poster",
                changedButtonText = "Change Poster",
                icon = Icons.Default.BookOnline,
                posterUri = posterUri,
                onPickPoster = { posterPicker.launch(arrayOf("image/*")) },
                onRemovePoster = { posterUri = null }
            )
            if (posterUri != null) {
                Text(
                    "Poster saved. It will remain visible after restart on this device.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Button(
                enabled = name.isNotBlank() &&
                    venue.isNotBlank() &&
                    time.isNotBlank() &&
                    duration.isNotBlank() &&
                    persona.isNotBlank() &&
                    leadActorName.isNotBlank() &&
                    leadActorPhotoUri != null &&
                    posterUri != null,
                onClick = {
                    val basePrice = price.toIntOrNull()?.coerceAtLeast(20) ?: existingShow?.pricing?.middle ?: 50
                    val leadMember = CastMember(
                        name = leadActorName.trim(),
                        role = "Lead",
                        character = "Lead performer",
                        photoUri = leadActorPhotoUri.toString()
                    )
                    val existingSupportingCast = existingShow?.cast
                        ?.filterNot { it.role.equals("Lead", true) || it.role.equals("Hero", true) }
                        .orEmpty()
                    onSave(
                        DramaShow(
                            id = existingShow?.id ?: "show_${System.currentTimeMillis()}",
                            name = name.trim(),
                            nameKannada = existingShow?.nameKannada ?: name.trim(),
                            shortName = name.trim().take(8).uppercase(),
                            description = persona.trim(),
                            time = time.trim(),
                            durationMinutes = duration.toIntOrNull()?.coerceAtLeast(30) ?: existingShow?.durationMinutes ?: 180,
                            venue = venue.trim(),
                            seatsAvailable = existingShow?.seatsAvailable ?: 50,
                            totalSeats = existingShow?.totalSeats ?: 50,
                            pricing = Pricing(front = basePrice + 40, middle = basePrice, back = (basePrice - 20).coerceAtLeast(10)),
                            rating = existingShow?.rating ?: 4.5,
                            reviewCount = existingShow?.reviewCount ?: 0,
                            cast = listOf(leadMember) + existingSupportingCast,
                            reviews = existingShow?.reviews ?: emptyList(),
                            posterColors = existingShow?.posterColors ?: listOf(Color(0xFF386568), Color(0xFFD9A441)),
                            posterUri = posterUri.toString(),
                            leadActorPhotoUri = leadActorPhotoUri.toString()
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Show")
            }
            if (posterUri == null) {
                Text("Upload a poster before saving the show.", color = MaterialTheme.colorScheme.error)
            }
            if (leadActorPhotoUri == null) {
                Text("Add the lead actor photo before saving the show.", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun PosterUploadCard(
    title: String = "Poster",
    emptyText: String = "No poster selected",
    buttonText: String = "Upload Poster",
    changedButtonText: String = "Change Poster",
    icon: ImageVector = Icons.Default.BookOnline,
    posterUri: Uri?,
    onPickPoster: () -> Unit,
    onRemovePoster: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            PosterPreview(posterUri = posterUri, emptyText = emptyText, icon = icon)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilledTonalButton(onClick = onPickPoster, modifier = Modifier.weight(1f)) {
                    Icon(icon, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (posterUri == null) buttonText else changedButtonText)
                }
                if (posterUri != null) {
                    OutlinedButton(onClick = onRemovePoster) {
                        Text("Remove")
                    }
                }
            }
        }
    }
}

@Composable
private fun PosterPreview(
    posterUri: Uri?,
    emptyText: String = "No poster selected",
    icon: ImageVector = Icons.Default.TheaterComedy
) {
    val bitmap = rememberBitmapFromUri(posterUri?.toString())

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF006C67), Color(0xFFB63F2D), Color(0xFFFFC857)))),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Selected poster",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(52.dp))
                Spacer(Modifier.height(8.dp))
                Text(emptyText, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AuthScaffold(title: String, onBack: () -> Unit, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(40.dp))
        content()
    }
}

@Composable
private fun AppLogoMark() {
    Box(
        modifier = Modifier
            .size(116.dp)
            .clip(CircleShape)
            .background(Brush.radialGradient(listOf(Color(0xFFFFD978), Color(0xFF7A2E20)))),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(82.dp)
                .height(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFFFF4DF)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .align(Alignment.TopCenter)
                    .background(Color(0xFF12343B))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .align(Alignment.BottomCenter)
                    .background(Color(0xFF12343B))
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .width(14.dp)
                            .height(28.dp)
                            .clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                            .background(Color(0xFFD94F30))
                    )
                }
            }
        }
        Text(
            "NM",
            color = Color(0xFF12343B),
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun LargeActionButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(icon, contentDescription = null)
        Spacer(Modifier.width(10.dp))
        Text(text, fontSize = 17.sp)
    }
}

@Composable
private fun DiscoveryStrip() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF12343B))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFFFD978))
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("Shows near your village", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Live seats, QR tickets, trusted drama companies", color = Color.White.copy(alpha = 0.78f), fontSize = 12.sp)
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterPill("Tonight")
            FilterPill("Family")
            FilterPill("Front Row")
        }
    }
}

@Composable
private fun FilterPill(text: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun TrustAndSafetyCard() {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TrustMetric("100%", "secure booking", Modifier.weight(1f))
            TrustMetric("QR", "offline ticket", Modifier.weight(1f))
            TrustMetric("Live", "seat status", Modifier.weight(1f))
        }
    }
}

@Composable
private fun TrustMetric(value: String, label: String, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp, maxLines = 1)
    }
}

@Composable
private fun FeaturedPosterAd(
    show: DramaShow,
    seatsAvailable: Int,
    kannada: Boolean,
    onClick: () -> Unit,
    onBook: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            PosterArtwork(show = show, modifier = Modifier.fillMaxSize(), large = true)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0x33000000),
                                Color(0xDD000000)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(18.dp)
            ) {
                Text(
                    "Featured Tonight",
                    color = Color(0xFFFFD978),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    if (kannada) show.nameKannada else show.name,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "${show.time} • ${show.venue} • $seatsAvailable seats left",
                    color = Color.White.copy(alpha = 0.88f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = onBook, shape = RoundedCornerShape(8.dp)) {
                        Icon(Icons.Default.EventSeat, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Book Now")
                    }
                    OutlinedButton(
                        onClick = onClick,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color.White)
                    ) {
                        Text("Details", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun PosterAdRail(
    shows: List<DramaShow>,
    kannada: Boolean,
    onShowClick: (String) -> Unit,
    onBookClick: (String) -> Unit
) {
    Column {
        Text("Poster Ads", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(10.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(shows, key = { it.id }) { show ->
                PosterAdCard(
                    show = show,
                    title = if (kannada) show.nameKannada else show.name,
                    onClick = { onShowClick(show.id) },
                    onBook = { onBookClick(show.id) }
                )
            }
        }
    }
}

@Composable
private fun PosterAdCard(show: DramaShow, title: String, onClick: () -> Unit, onBook: () -> Unit) {
    Card(
        modifier = Modifier
            .width(166.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        PosterArtwork(
            show = show,
            modifier = Modifier
                .fillMaxWidth()
                .height(234.dp),
            large = false
        )
        Column(Modifier.padding(10.dp)) {
            Text(title, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(show.time, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onBook,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Text("Book", maxLines = 1)
            }
        }
    }
}

@Composable
private fun ShowCard(show: DramaShow, seatsAvailable: Int, kannada: Boolean, onClick: () -> Unit, onBook: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(Modifier.padding(12.dp)) {
            PosterThumb(show)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    if (kannada) show.nameKannada else show.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                InfoLine(Icons.Default.TheaterComedy, "${show.time} | ${show.durationMinutes / 60} hrs")
                InfoLine(Icons.Default.LocationOn, show.venue)
                Text(
                    "$seatsAvailable seats available",
                    color = if (seatsAvailable > 20) Color(0xFF2F7D32) else Color(0xFFB7791F),
                    fontWeight = FontWeight.SemiBold
                )
                Text("₹${show.pricing.back} - ₹${show.pricing.front}")
                RatingLine(show.rating, show.reviewCount)
                Spacer(Modifier.height(8.dp))
                Button(onClick = onBook, shape = RoundedCornerShape(8.dp)) {
                    Text(if (kannada) "ಈಗ ಬುಕ್ ಮಾಡಿ" else "Book Now")
                }
            }
        }
    }
}

@Composable
private fun PosterThumb(show: DramaShow) {
    PosterArtwork(
        show = show,
        modifier = Modifier
            .width(108.dp)
            .aspectRatio(0.68f),
        large = false
    )
}

@Composable
private fun PosterBanner(show: DramaShow) {
    PosterArtwork(
        show = show,
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        large = true
    )
}

@Composable
private fun PosterArtwork(show: DramaShow, modifier: Modifier, large: Boolean) {
    val bitmap = rememberBitmapFromUri(show.posterUri)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Brush.linearGradient(show.posterColors)),
        contentAlignment = Alignment.BottomStart
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = show.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = if (bitmap == null) 0.08f else 0.18f),
                            Color.Black.copy(alpha = 0.78f)
                        )
                    )
                )
        )
        if (bitmap == null) {
            Icon(
                Icons.Default.TheaterComedy,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.30f),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(if (large) 104.dp else 58.dp)
            )
            Text(
                show.shortName,
                color = Color.White.copy(alpha = 0.22f),
                fontWeight = FontWeight.Black,
                fontSize = if (large) 58.sp else 28.sp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp),
                maxLines = 2
            )
        }
        Column(Modifier.padding(if (large) 18.dp else 10.dp)) {
            Text(
                show.name.uppercase(),
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = if (large) 26.sp else 15.sp,
                maxLines = if (large) 2 else 3,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "${show.time}  •  Rs.${show.pricing.back}+",
                color = Color.White.copy(alpha = 0.88f),
                fontSize = if (large) 14.sp else 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun rememberBitmapFromUri(uriString: String?): android.graphics.Bitmap? {
    val context = LocalContext.current
    var bitmap by remember(uriString) { mutableStateOf<android.graphics.Bitmap?>(null) }
    LaunchedEffect(uriString) {
        if (uriString == null) {
            bitmap = null
            return@LaunchedEffect
        }
        withContext(Dispatchers.IO) {
            val result = runCatching {
                if (uriString.startsWith("http")) {
                    val url = URL(uriString)
                    BitmapFactory.decodeStream(url.openStream())
                } else {
                    context.contentResolver.openInputStream(Uri.parse(uriString))?.use {
                        BitmapFactory.decodeStream(it)
                    }
                }
            }.getOrNull()
            withContext(Dispatchers.Main) {
                bitmap = result
            }
        }
    }
    return bitmap
}

@Composable
private fun InfoLine(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(4.dp))
        Text(text, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun RatingLine(rating: Double, count: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(18.dp))
        Text(" $rating ($count reviews)", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun InfoCard(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
        Column(Modifier.padding(10.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            Text(value, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun PriceChip(label: String, price: Int) {
    Card(border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
        Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("₹$price", fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
}

@Composable
private fun CastPreview(cast: List<CastMember>) {
    Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
        cast.take(4).forEach { ActorAvatar(it) }
    }
}

@Composable
private fun ActorAvatar(member: CastMember) {
    val bitmap = rememberBitmapFromUri(member.photoUri)
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = member.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(member.name.take(1), fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

@Composable
private fun Avatar(name: String) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(name.take(1), fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

@Composable
private fun ReviewRow(review: Review) {
    Column(Modifier.padding(vertical = 6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Avatar(review.author)
            Spacer(Modifier.width(10.dp))
            Column {
                Text(review.author, fontWeight = FontWeight.Bold)
                RatingLine(review.rating, 0)
            }
        }
        Text(review.comment, modifier = Modifier.padding(top = 6.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SeatButton(seat: Seat, selected: Boolean, onClick: () -> Unit) {
    val color = when {
        !seat.available -> Color(0xFF9CA3AF)
        selected -> MaterialTheme.colorScheme.primary
        else -> Color(0xFF2F7D32)
    }
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(color)
            .clickable(enabled = seat.available, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(seat.label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}

@Composable
private fun Legend(text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(12.dp).clip(CircleShape).background(color))
        Spacer(Modifier.width(4.dp))
        Text(text, fontSize = 12.sp)
    }
}

@Composable
private fun CustomerBottomBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val items = listOf(
        "Home" to Icons.Default.Home,
        "Plays" to Icons.Default.TheaterComedy,
        "Bookings" to Icons.Default.BookOnline,
        "Fan Wall" to Icons.Default.Comment,
        "Profile" to Icons.Default.Person
    )
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                icon = { Icon(item.second, contentDescription = item.first) },
                label = { Text(item.first, maxLines = 1) }
            )
        }
    }
}

@Composable
private fun EmptyState(title: String, message: String) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Default.TheaterComedy, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(title, fontWeight = FontWeight.Black)
            Text(message, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun MetricCard(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
        Column(Modifier.padding(12.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
    }
}

data class DramaShow(
    val id: String,
    val name: String,
    val nameKannada: String,
    val shortName: String,
    val description: String,
    val time: String,
    val durationMinutes: Int,
    val venue: String,
    val seatsAvailable: Int,
    val totalSeats: Int,
    val pricing: Pricing,
    val rating: Double,
    val reviewCount: Int,
    val cast: List<CastMember>,
    val reviews: List<Review>,
    val posterColors: List<Color>,
    val posterUri: String? = null,
    val leadActorPhotoUri: String? = null
)

data class Pricing(val front: Int, val middle: Int, val back: Int)
data class CastMember(
    val name: String,
    val role: String,
    val character: String,
    val photoUri: String? = null
)
data class Review(val author: String, val rating: Double, val comment: String)
data class Seat(val label: String, val section: SeatSection, val price: Int, val available: Boolean)
data class Booking(val id: String, val showId: String, val phone: String, val seatLabels: List<String>, val total: Int)
data class FanPost(val id: String, val author: String, val message: String)

private fun Booking.storageKey(): String =
    listOf(id, showId, phone, seatLabels.joinToString(","), total.toString()).joinToString("~")

private fun encodeBookings(bookings: List<Booking>): String =
    bookings.joinToString("|") { it.storageKey() }

private fun decodeBookings(raw: String): List<Booking> =
    raw.split("|")
        .filter { it.isNotBlank() }
        .mapNotNull { record ->
            val parts = record.split("~")
            if (parts.size != 5) return@mapNotNull null
            Booking(
                id = parts[0],
                showId = parts[1],
                phone = parts[2],
                seatLabels = parts[3].split(",").filter { it.isNotBlank() },
                total = parts[4].toIntOrNull() ?: 0
            )
        }

enum class SeatSection { FRONT, MIDDLE, BACK }

object BookingRules {
    fun calculateTotal(seats: List<Seat>): Int = seats.sumOf { it.price }
    fun isSelectionAllowed(seats: List<Seat>): Boolean = seats.size <= 10
        }

private fun FanPost.storageKey(): String =
    listOf(id, author, message).joinToString("~")

private fun encodeFanPosts(posts: List<FanPost>): String {
    val array = JSONArray()
    posts.forEach { post ->
        array.put(
            JSONObject()
                .put("id", post.id)
                .put("author", post.author)
                .put("message", post.message)
        )
    }
    return array.toString()
}

private fun decodeFanPosts(raw: String): List<FanPost> {
    if (raw.isBlank()) return emptyList()
    return runCatching {
        val array = JSONArray(raw)
        List(array.length()) { index ->
            val item = array.getJSONObject(index)
            FanPost(
                id = item.optString("id"),
                author = item.optString("author"),
                message = item.optString("message")
            )
        }.filter { it.id.isNotBlank() && it.message.isNotBlank() }
    }.getOrDefault(emptyList())
}

private fun DramaShow.storageKey(): String =
    listOf(
        id,
        name,
        nameKannada,
        description,
        time,
        venue,
        durationMinutes.toString(),
        totalSeats.toString(),
        pricing.front.toString(),
        pricing.middle.toString(),
        pricing.back.toString(),
        posterUri.orEmpty(),
        leadActorPhotoUri.orEmpty(),
        cast.joinToString(",") { "${it.name}:${it.role}:${it.character}:${it.photoUri.orEmpty()}" },
        reviews.joinToString(",") { "${it.author}:${it.rating}:${it.comment}" }
    ).joinToString("~")

private fun encodeShows(shows: List<DramaShow>): String {
    val array = JSONArray()
    shows.forEach { show ->
        val castArray = JSONArray()
        show.cast.forEach { member ->
            castArray.put(
                JSONObject()
                    .put("name", member.name)
                    .put("role", member.role)
                    .put("character", member.character)
                    .put("photoUri", member.photoUri)
            )
        }

        val reviewArray = JSONArray()
        show.reviews.forEach { review ->
            reviewArray.put(
                JSONObject()
                    .put("author", review.author)
                    .put("rating", review.rating)
                    .put("comment", review.comment)
            )
        }

        val colorsArray = JSONArray()
        show.posterColors.forEach { color -> colorsArray.put(color.toArgb()) }

        array.put(
            JSONObject()
                .put("id", show.id)
                .put("name", show.name)
                .put("nameKannada", show.nameKannada)
                .put("shortName", show.shortName)
                .put("description", show.description)
                .put("time", show.time)
                .put("durationMinutes", show.durationMinutes)
                .put("venue", show.venue)
                .put("seatsAvailable", show.seatsAvailable)
                .put("totalSeats", show.totalSeats)
                .put("frontPrice", show.pricing.front)
                .put("middlePrice", show.pricing.middle)
                .put("backPrice", show.pricing.back)
                .put("rating", show.rating)
                .put("reviewCount", show.reviewCount)
                .put("posterUri", show.posterUri)
                .put("leadActorPhotoUri", show.leadActorPhotoUri)
                .put("posterColors", colorsArray)
                .put("cast", castArray)
                .put("reviews", reviewArray)
        )
    }
    return array.toString()
}

private fun decodeShows(raw: String): List<DramaShow> {
    if (raw.isBlank()) return emptyList()
    return runCatching {
        val array = JSONArray(raw)
        List(array.length()) { index ->
            val item = array.getJSONObject(index)
            val colors = item.optJSONArray("posterColors")?.let { colorArray ->
                List(colorArray.length()) { colorIndex -> Color(colorArray.getInt(colorIndex)) }
            }.orEmpty().ifEmpty { listOf(Color(0xFF386568), Color(0xFFD9A441)) }

            val cast = item.optJSONArray("cast")?.let { castArray ->
                List(castArray.length()) { castIndex ->
                    val member = castArray.getJSONObject(castIndex)
                    CastMember(
                        name = member.optString("name"),
                        role = member.optString("role"),
                        character = member.optString("character"),
                        photoUri = member.optString("photoUri").takeIf { it.isNotBlank() && it != "null" }
                    )
                }
            }.orEmpty()

            val reviews = item.optJSONArray("reviews")?.let { reviewArray ->
                List(reviewArray.length()) { reviewIndex ->
                    val review = reviewArray.getJSONObject(reviewIndex)
                    Review(
                        author = review.optString("author"),
                        rating = review.optDouble("rating", 0.0),
                        comment = review.optString("comment")
                    )
                }
            }.orEmpty()

            DramaShow(
                id = item.optString("id"),
                name = item.optString("name"),
                nameKannada = item.optString("nameKannada"),
                shortName = item.optString("shortName"),
                description = item.optString("description"),
                time = item.optString("time"),
                durationMinutes = item.optInt("durationMinutes", 180),
                venue = item.optString("venue"),
                seatsAvailable = item.optInt("seatsAvailable", 50),
                totalSeats = item.optInt("totalSeats", 50),
                pricing = Pricing(
                    front = item.optInt("frontPrice", 100),
                    middle = item.optInt("middlePrice", 50),
                    back = item.optInt("backPrice", 20)
                ),
                rating = item.optDouble("rating", 4.5),
                reviewCount = item.optInt("reviewCount", 0),
                cast = cast,
                reviews = reviews,
                posterColors = colors,
                posterUri = item.optString("posterUri").takeIf { it.isNotBlank() && it != "null" },
                leadActorPhotoUri = item.optString("leadActorPhotoUri").takeIf { it.isNotBlank() && it != "null" }
            )
        }.filter { it.id.isNotBlank() && it.name.isNotBlank() }
    }.getOrDefault(emptyList())
}

private fun persistReadPermission(context: android.content.Context, uri: Uri) {
    runCatching {
        context.contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }
}

private class NammaMelaAppState(
    initialManagerEmail: String,
    initialManagerHistory: List<String>,
    initialCustomerPhone: String,
    initialBookings: List<Booking>,
    initialShows: List<DramaShow>,
    initialFanPosts: List<FanPost>
) {
    val shows = mutableStateListOf<DramaShow>().apply { addAll(initialShows) }
    val bookings = mutableStateListOf<Booking>().apply { addAll(initialBookings) }
    val fanPosts = mutableStateListOf<FanPost>().apply { addAll(initialFanPosts) }
    val bookedSeatKeys = mutableStateListOf<String>().apply {
        addAll(initialBookings.flatMap { booking -> booking.seatLabels.map { "${booking.showId}:$it" } })
    }
    val managerHistory = mutableStateListOf<String>().apply { addAll(initialManagerHistory.distinct()) }
    var managerEmail by mutableStateOf(initialManagerEmail)
    var customerPhone by mutableStateOf(initialCustomerPhone)

    fun saveManagerEmail(email: String) {
        managerEmail = ALLOWED_MANAGER_EMAIL
        if (email.equals(ALLOWED_MANAGER_EMAIL, ignoreCase = true)) {
            managerHistory.remove(ALLOWED_MANAGER_EMAIL)
            managerHistory.add(0, ALLOWED_MANAGER_EMAIL)
            while (managerHistory.size > 5) managerHistory.removeLast()
        }
    }

    fun show(id: String): DramaShow = shows.firstOrNull { it.id == id } ?: shows.first()

    fun addShow(show: DramaShow) {
        shows.add(0, show)
    }

    fun updateShow(show: DramaShow) {
        val index = shows.indexOfFirst { it.id == show.id }
        if (index >= 0) shows[index] = show
    }

    fun deleteShow(showId: String) {
        shows.removeAll { it.id == showId }
        bookings.removeAll { it.showId == showId }
        bookedSeatKeys.removeAll { it.startsWith("$showId:") }
    }

    fun seatsFor(showId: String): List<Seat> {
        val show = show(showId)
        return NammaMelaRepository.baseSeatsFor(show).map { seat ->
            seat.copy(available = seat.available && "$showId:${seat.label}" !in bookedSeatKeys)
        }
    }

    fun availableSeatCount(showId: String): Int = seatsFor(showId).count { it.available }

    fun bookedSeatCount(showId: String): Int = bookedSeatKeys.count { it.startsWith("$showId:") }

    fun bookingsForCurrentCustomer(): List<Booking> =
        bookings.filter { it.phone == customerPhone }

    fun bookSeats(showId: String, seats: List<Seat>) {
        val availableLabels = seatsFor(showId).filter { it.available }.map { it.label }.toSet()
        val confirmedSeats = seats.filter { it.label in availableLabels }
        if (confirmedSeats.isEmpty()) return

        confirmedSeats.forEach { seat ->
            val key = "$showId:${seat.label}"
            if (key !in bookedSeatKeys) bookedSeatKeys.add(key)
        }
        bookings.add(
            0,
            Booking(
                id = "booking_${System.currentTimeMillis()}",
                showId = showId,
                phone = customerPhone,
                seatLabels = confirmedSeats.map { it.label },
                total = BookingRules.calculateTotal(confirmedSeats)
            )
        )
    }

    fun addFanPost(message: String) {
        fanPosts.add(
            0,
            FanPost(
                id = "fan_${System.currentTimeMillis()}",
                author = if (customerPhone.isBlank()) "Guest" else "+91 $customerPhone",
                message = message.trim()
            )
        )
    }
}

private object NammaMelaRepository {
    val defaultFanPosts = listOf(
        FanPost("fan_1", "Anitha", "Rayanna show songs were amazing."),
        FanPost("fan_2", "Kiran", "Please add more weekend dramas near our village.")
    )

    val todayShows = listOf(
        DramaShow(
            id = "ramayana",
            name = "Ramayana Drama",
            nameKannada = "ರಾಮಾಯಣ ನಾಟಕ",
            shortName = "RAMA",
            description = "An energetic village-stage retelling of Rama's journey, with live music, bold costumes, and a family-friendly performance by Sri Krishna Drama Company.",
            time = "7:00 PM",
            durationMinutes = 180,
            venue = "Village Square Stage",
            seatsAvailable = 45,
            totalSeats = 80,
            pricing = Pricing(front = 100, middle = 50, back = 20),
            rating = 4.8,
            reviewCount = 125,
            cast = listOf(
                CastMember("Ravi Kumar", "Hero", "Rama", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200&h=200&fit=crop"),
                CastMember("Meena S", "Heroine", "Sita", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&h=200&fit=crop"),
                CastMember("Shankar", "Villain", "Ravana"),
                CastMember("Nagesh", "Comedian", "Hanuman")
            ),
            reviews = listOf(
                Review("Kiran", 5.0, "Beautiful songs and a powerful Ravana performance."),
                Review("Anitha", 4.5, "Easy booking and good seats for my family.")
            ),
            posterColors = listOf(Color(0xFF7A2E20), Color(0xFFFFB84D)),
            posterUri = "https://images.unsplash.com/photo-1533488765986-dfa2a9939acd?w=500&h=800&fit=crop"
        ),
        DramaShow(
            id = "mahabharata",
            name = "Mahabharata Veera Gatha",
            nameKannada = "ಮಹಾಭಾರತ ವೀರ ಗಾಥೆ",
            shortName = "VEERA",
            description = "A grand dramatic night covering the dice game, exile, and Kurukshetra with traditional dialogue delivery and percussion.",
            time = "9:00 PM",
            durationMinutes = 210,
            venue = "Temple Grounds",
            seatsAvailable = 18,
            totalSeats = 70,
            pricing = Pricing(front = 120, middle = 70, back = 30),
            rating = 4.6,
            reviewCount = 88,
            cast = listOf(
                CastMember("Prakash", "Lead", "Arjuna", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&h=200&fit=crop"),
                CastMember("Devraj", "Lead", "Krishna"),
                CastMember("Suma", "Supporting", "Draupadi")
            ),
            reviews = listOf(
                Review("Mahesh", 4.5, "The war scene was staged very well."),
                Review("Lakshmi", 4.0, "Clear sound and comfortable booking.")
            ),
            posterColors = listOf(Color(0xFF2B5C69), Color(0xFFE0A137)),
            posterUri = "https://images.unsplash.com/photo-1514306191717-452ec28c7814?w=500&h=800&fit=crop"
        ),
        DramaShow(
            id = "sangolli",
            name = "Sangolli Rayanna",
            nameKannada = "ಸಂಗೊಳ್ಳಿ ರಾಯಣ್ಣ",
            shortName = "RAYANNA",
            description = "A patriotic historical play celebrating courage, loyalty, and resistance, performed with folk instruments and striking costumes.",
            time = "8:30 PM",
            durationMinutes = 160,
            venue = "Old Market Maidan",
            seatsAvailable = 8,
            totalSeats = 60,
            pricing = Pricing(front = 90, middle = 60, back = 25),
            rating = 4.9,
            reviewCount = 142,
            cast = listOf(
                CastMember("Basavaraj", "Hero", "Rayanna", "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200&h=200&fit=crop"),
                CastMember("Geetha", "Lead", "Kittur Chennamma"),
                CastMember("Manju", "Villain", "British Officer")
            ),
            reviews = listOf(
                Review("Ramesh", 5.0, "Gave me goosebumps. Must watch."),
                Review("Pooja", 5.0, "Loved the patriotic songs.")
            ),
            posterColors = listOf(Color(0xFF365E32), Color(0xFFC24B2B)),
            posterUri = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=500&h=800&fit=crop"
        )
    )

    fun show(id: String): DramaShow = todayShows.firstOrNull { it.id == id } ?: todayShows.first()

    fun baseSeatsFor(show: DramaShow): List<Seat> {
        val rows = listOf("A" to SeatSection.FRONT, "B" to SeatSection.FRONT, "C" to SeatSection.MIDDLE, "D" to SeatSection.MIDDLE, "E" to SeatSection.BACK)
        return rows.flatMapIndexed { rowIndex, row ->
            (1..10).map { number ->
                val price = when (row.second) {
                    SeatSection.FRONT -> show.pricing.front
                    SeatSection.MIDDLE -> show.pricing.middle
                    SeatSection.BACK -> show.pricing.back
                }
                Seat(
                    label = "${row.first}$number",
                    section = row.second,
                    price = price,
                    available = (number + rowIndex) % 4 != 0
                )
            }
        }
    }
}
