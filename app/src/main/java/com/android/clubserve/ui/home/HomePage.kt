package com.android.clubserve.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import android.app.Activity
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.android.clubserve.R
import com.android.clubserve.data.remote.dto.MainCategoryDto
import com.android.clubserve.data.remote.dto.SubCategoryDto
import com.android.clubserve.data.repository.HomeDomainState
import com.android.clubserve.ui.components.CalloutEmphasizedText
import com.android.clubserve.ui.components.HeadlineText
import com.android.clubserve.ui.components.NormalText
import com.android.clubserve.ui.components.SubheadlineRegularText
import com.android.clubserve.ui.components.Title3EmphasizedText
import com.android.clubserve.ui.components.shimmer
import com.android.clubserve.ui.theme.ClubServeTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.android.clubserve.utils.LocationService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import android.Manifest
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Immutable
data class ClubCategory(
    val id: Int,
    val icon: ImageVector,
    val labelRes: Int = 0,
    val labelText: String? = null
)

@Immutable
data class BookingData(val id: Int, val title: String, val date: String, val time: String, val location: String)

@Immutable
data class OfferCardData(val id: Int, val title: String, val location: String, val price: String, val imageUrl: String? = null)

@Immutable
data class TopClubData(val id: Int, val name: String, val color: Color, val icon: ImageVector, val imageUrl: String? = null)

@Immutable
data class PersonData(val id: Int, val name: String, val level: String)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomePage(
    isLoggedIn: Boolean = false,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val locationService = remember { LocationService(context) }

    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(locationPermissionState.allPermissionsGranted) {
        if (locationPermissionState.allPermissionsGranted) {
            val locationName = locationService.getCurrentLocationName()
            locationName?.let { viewModel.updateLocation(it) }
        } else {
            locationPermissionState.launchMultiplePermissionRequest()
        }
    }

    val categories = remember(uiState.categories) {
        uiState.categories.mapIndexed { index, dto ->
            val icon = when (dto.categoryKey) {
                "space" -> Icons.Default.Stadium
                "class" -> Icons.Default.School
                "wellness-service" -> Icons.Default.Spa
                else -> Icons.Default.Category
            }
            ClubCategory(index, icon, labelRes = 0, labelText = dto.categoryName)
        }
    }

    val bookings = remember(uiState.bookings) {
        uiState.bookings.mapIndexed { index, _ ->
            // Since BookingDto is empty in model, we use N/A for now
            BookingData(index, "Booking from API", "N/A", "N/A", "N/A")
        }
    }

    val lovedByLocals = remember(uiState.lovedByLocals) {
        uiState.lovedByLocals.mapIndexed { index, dto ->
            OfferCardData(
                id = index,
                title = dto.subCategoryName,
                location = dto.businessInfoName ?: "N/A",
                price = if (dto.startingPricePerHour != null) "₱${dto.startingPricePerHour.substringBefore(".")}/hr" else "N/A",
                imageUrl = dto.fullImageUrl
            )
        }
    }

    val specialOffers = remember(uiState.subCategories) {
        uiState.subCategories.mapIndexed { index, dto ->
            OfferCardData(
                id = index,
                title = dto.subCategoryName,
                location = dto.businessInfoName ?: "N/A",
                price = if (dto.startingPricePerHour != null) "₱${dto.startingPricePerHour.substringBefore(".")}/hr" else "N/A",
                imageUrl = dto.fullImageUrl
            )
        }
    }

    val topClubs = remember(uiState.groups) {
        uiState.groups.mapIndexed { index, dto ->
            TopClubData(
                id = index,
                name = dto.name,
                color = when (index % 4) {
                    0 -> Color(0xFF6200EE)
                    1 -> Color(0xFF03A9F4)
                    2 -> Color(0xFF4CAF50)
                    else -> Color(0xFFFF9800)
                },
                icon = Icons.Default.Store,
                imageUrl = dto.fullImageUrl
            )
        }
    }

    val peopleToFollow = remember {
        listOf(
            PersonData(1, "Cameron Willia...", "Level 1.5"),
            PersonData(2, "Kristin Watson", "-"),
            PersonData(3, "Robert Fox", "Level 2.0"),
            PersonData(4, "Jane Cooper", "Level 1.2"),
            PersonData(5, "Guy Hawkins", "Level 3.0")
        )
    }

    val locationText = when {
        locationPermissionState.allPermissionsGranted -> uiState.location ?: stringResource(R.string.location_sample)
        else -> "No location"
    }

    if (uiState.isLoading) {
        LoadingHomePage()
    } else {
        HomeContent(
            isLoggedIn = isLoggedIn,
            categories = categories,
            bookings = bookings,
            lovedByLocals = lovedByLocals,
            specialOffers = specialOffers,
            topClubs = topClubs,
            peopleToFollow = peopleToFollow,
            location = locationText,
            onLocationClick = {
                if (locationPermissionState.allPermissionsGranted) {
                    coroutineScope.launch {
                        val locationName = locationService.getCurrentLocationName()
                        locationName?.let { viewModel.updateLocation(it) }
                    }
                } else {
                    locationPermissionState.launchMultiplePermissionRequest()
                }
            }
        )
    }
}

@Composable
fun LoadingHomePage() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF8F9FB),
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            // Header Shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .shimmer()
            )

            // Search Bar Shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmer()
            )

            repeat(3) {
                // Section Title Shimmer
                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .size(150.dp, 24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmer()
                )

                // Row of Cards Shimmer
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    userScrollEnabled = false
                ) {
                    items(3) {
                        Box(
                            modifier = Modifier
                                .size(330.dp, 230.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .shimmer()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun HomeContent(
    isLoggedIn: Boolean,
    categories: List<ClubCategory>,
    bookings: List<BookingData>,
    lovedByLocals: List<OfferCardData>,
    specialOffers: List<OfferCardData>,
    topClubs: List<TopClubData>,
    peopleToFollow: List<PersonData>,
    location: String? = null,
    onLocationClick: () -> Unit = {}
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF8F9FB),
        bottomBar = { HomeBottomNavigation() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            HomeBlackHeader()
            HomeSearchSection(isLoggedIn = isLoggedIn, location = location, onLocationClick = onLocationClick)
            
            if (isLoggedIn) {
                SectionHeader(title = stringResource(R.string.upcoming_bookings), showViewAll = true)
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(bookings, key = { it.id }) { BookingCard(it) }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            SectionHeader(title = stringResource(R.string.explore_clubs))
            CategoryRow(categories)
            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader(title = stringResource(R.string.loved_by_locals), showViewAll = true)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(lovedByLocals, key = { it.id }) { OfferCard(it) }
            }
            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader(title = stringResource(R.string.special_offers), showViewAll = true)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(specialOffers, key = { it.id }) { OfferCard(it) }
            }
            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader(title = stringResource(R.string.top_clubs))
            TopClubsRow(topClubs)
            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader(title = stringResource(R.string.play_near_you), showViewAll = true)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(lovedByLocals, key = { it.id }) { OfferCard(it) } // Using same data for demo
            }
            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader(title = stringResource(R.string.people_to_follow), showViewAll = true)
            PeopleToFollowRow(peopleToFollow)
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun HomeBlackHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(R.string.clubserve_logo_desc),
                modifier = Modifier.width(177.dp)
                    .height(28.dp)
            )
        }
        
        Box {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = stringResource(R.string.notifications),
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            // Yellow dot
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFD600))
                    .align(Alignment.TopEnd)
            )
        }
    }
}

@Composable
fun HomeSearchSection(isLoggedIn: Boolean, location: String? = null, onLocationClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onLocationClick() }
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF6200EE), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(4.dp))
                CalloutEmphasizedText(text = location ?: stringResource(R.string.location_sample))
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(20.dp))
            }
            if (isLoggedIn) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.fillMaxSize())
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF1F3F6)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                NormalText(
                    text = stringResource(R.string.search_hint),
                    color = Color(0xFF7074A0)
                )
            }
        }
    }
}

@Composable
fun BookingCard(booking: BookingData) {
    Card(
        modifier = Modifier
            .width(330.dp)
            .height(184.dp)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()
                .height(110.dp)
                .background(Color.LightGray))
            
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF03A9F4), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    CalloutEmphasizedText(
                        text = booking.title,
                        color = Color(0xFF0A0A0A)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    CalloutEmphasizedText(text = booking.date)
                }
                Row {
                    Spacer(modifier = Modifier.width(20.dp))
                    SubheadlineRegularText(text = booking.location, color = Color.Gray)
                    Spacer(modifier = Modifier.weight(1f))
                    SubheadlineRegularText(text = booking.time, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun CategoryRow(categories: List<ClubCategory>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(categories, key = { it.id }) { category ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier = Modifier.size(60.dp),
                    shape = CircleShape,
                    color = Color.Black
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                val text = category.labelText ?: if (category.labelRes != 0) stringResource(category.labelRes) else ""
                CalloutEmphasizedText(text = text)
            }
        }
    }
}

@Composable
fun OfferCard(data: OfferCardData) {
    Column(
        modifier = Modifier
            .width(330.dp)
            .padding(vertical = 4.dp)
    ) {
        AsyncImage(
            model = data.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(168.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.logo)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CalloutEmphasizedText(
                text = data.title,
                color = Color.Black
            )
            SubheadlineRegularText(
                text = stringResource(R.string.starts_at),
                color = Color.Gray
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_homepage),
                    contentDescription = null,
                    tint = Color(0xFF3B82F6),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                CalloutEmphasizedText(
                    text = data.location,
                    color = Color.Black
                )
            }
            CalloutEmphasizedText(
                text = data.price,
                color = Color.Black
            )
        }
    }
}

@Composable
fun TopClubsRow(clubs: List<TopClubData>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(clubs, key = { it.id }) { club ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier = Modifier.size(140.dp, 100.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = club.color
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (club.imageUrl != null) {
                            AsyncImage(
                                model = club.imageUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                error = painterResource(R.drawable.logo)
                            )
                        } else {
                            Icon(club.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                CalloutEmphasizedText(text = club.name)
            }
        }
    }
}

@Composable
fun PeopleToFollowRow(people: List<PersonData>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(people, key = { it.id }) { person ->
            Card(
                modifier = Modifier.width(330.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(Color.LightGray)) {
                        if (person.name.startsWith("Kristin")) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CalloutEmphasizedText("KW", color = Color.Gray)
                            }
                        } else {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.fillMaxSize())
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    CalloutEmphasizedText(text = person.name)
                    SubheadlineRegularText(text = person.level, color = Color.Gray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        SubheadlineRegularText(stringResource(R.string.follow), color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, showViewAll: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Title3EmphasizedText(text = title)
        if (showViewAll) {
            HeadlineText(
                text = stringResource(R.string.view_all),
                color = Color(0xFF0A0A0A)
            )
        }
    }
}

@Composable
fun HomeBottomNavigation() {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Home, contentDescription = stringResource(R.string.home)) },
            label = { SubheadlineRegularText(stringResource(R.string.home), color = Color.Unspecified) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Outlined.Search, contentDescription = stringResource(R.string.search)) },
            label = { SubheadlineRegularText(stringResource(R.string.search), color = Color.Unspecified) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.DateRange, contentDescription = stringResource(R.string.activity)) },
            label = { SubheadlineRegularText(stringResource(R.string.activity), color = Color.Unspecified) }
        )
        FloatingActionButton(
            onClick = { },
            containerColor = Color(0xFFF0F0F0),
            contentColor = Color.Black,
            elevation = FloatingActionButtonDefaults.elevation(0.dp),
            shape = CircleShape,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        ) {
            Row(modifier = Modifier.padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.book))
                SubheadlineRegularText(stringResource(R.string.book), color = Color.Black)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingHomePreview() {
    ClubServeTheme {
        LoadingHomePage()
    }
}

@Preview(showBackground = true, heightDp = 1500)
@Composable
fun HomeContentPreview() {
    ClubServeTheme {
        HomeContent(
            isLoggedIn = true,
            categories = listOf(
                ClubCategory(1, Icons.Default.Stadium, labelText = "Courts"),
                ClubCategory(2, Icons.Default.School, labelText = "Classes"),
                ClubCategory(3, Icons.Default.Spa, labelText = "Wellness"),
                ClubCategory(4, Icons.Default.FitnessCenter, labelText = "Fitness")
            ),
            bookings = listOf(
                BookingData(1, "Prosmash Alabang", "Jan 2, 2026", "2:00 - 3:00 PM", "Center Court"),
                BookingData(2, "Padel Court Manila", "Jan 5, 2026", "4:00 - 5:00 PM", "Court B")
            ),
            lovedByLocals = listOf(
                OfferCardData(1, "Padel court", "Prosmash Alabang", "₱650/hr"),
                OfferCardData(2, "Tennis session", "Manila Polo Club", "₱800/hr")
            ),
            specialOffers = listOf(
                OfferCardData(1, "Pro Coaching", "Prosmash Alabang", "₱650/hr"),
                OfferCardData(2, "Group Training", "Fitness First", "₱300/session")
            ),
            topClubs = listOf(
                TopClubData(1, "PadelCourt", Color(0xFF6200EE), Icons.Default.BlurOn),
                TopClubData(2, "Prosmash", Color(0xFF03A9F4), Icons.Default.Bolt)
            ),
            peopleToFollow = listOf(
                PersonData(1, "Cameron Willia...", "Level 1.5"),
                PersonData(2, "Kristin Watson", "-")
            ),
            location = "Alabang, Muntinlupa"
        )
    }
}

@Preview(showBackground = true, heightDp = 2000)
@Composable
fun HomePagePreview() {
    ClubServeTheme {
        HomePage(isLoggedIn = false)
    }
}
