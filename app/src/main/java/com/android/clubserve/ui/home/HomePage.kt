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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.clubserve.R
import com.android.clubserve.data.remote.dto.MainCategoryDto
import com.android.clubserve.data.remote.dto.SubCategoryDto
import com.android.clubserve.data.repository.HomeDomainState
import com.android.clubserve.ui.components.CalloutEmphasizedText
import com.android.clubserve.ui.components.HeadlineText
import com.android.clubserve.ui.components.NormalText
import com.android.clubserve.ui.components.SubheadlineRegularText
import com.android.clubserve.ui.components.Title3EmphasizedText
import com.android.clubserve.ui.theme.ClubServeTheme

@Immutable
data class ClubCategory(val id: Int, val icon: ImageVector, val labelRes: Int)

@Immutable
data class BookingData(val id: Int, val title: String, val date: String, val time: String, val location: String)

@Immutable
data class OfferCardData(val id: Int, val title: String, val location: String, val price: String)

@Immutable
data class TopClubData(val id: Int, val name: String, val color: Color, val icon: ImageVector)

@Immutable
data class PersonData(val id: Int, val name: String, val level: String)

@Composable
fun HomePage(isLoggedIn: Boolean = true) {
    val categories = remember {
        listOf(
            ClubCategory(1, Icons.Default.Stadium, R.string.courts),
            ClubCategory(2, Icons.Default.School, R.string.classes),
            ClubCategory(3, Icons.Default.Spa, R.string.wellness),
            ClubCategory(4, Icons.Default.FitnessCenter, R.string.fitness)
        )
    }

    val bookings = remember {
        listOf(
            BookingData(1, "Prosmash Alabang", "Jan 2, 2026", "2:00 - 3:00 PM", "Center Court"),
            BookingData(2, "Padel Court Manila", "Jan 5, 2026", "4:00 - 5:00 PM", "Court B"),
            BookingData(3, "The Zone Makati", "Jan 10, 2026", "10:00 - 11:00 AM", "Main Hall")
        )
    }

    val lovedByLocals = remember {
        listOf(
            OfferCardData(1, "Padel court", "Prosmash Alabang", "₱650/hr"),
            OfferCardData(2, "Tennis session", "Manila Polo Club", "₱800/hr"),
            OfferCardData(3, "Badminton", "Dragon Smash", "₱400/hr"),
            OfferCardData(4, "Yoga Flow", "Yoga Hub", "₱500/session")
        )
    }

    val specialOffers = remember {
        listOf(
            OfferCardData(1, "Pro Coaching", "Prosmash Alabang", "₱650/hr"),
            OfferCardData(2, "Group Training", "Fitness First", "₱300/session"),
            OfferCardData(3, "Weekend Pass", "Gold's Gym", "₱1,200"),
            OfferCardData(4, "First Timer", "Anytime Fitness", "Free")
        )
    }

    val topClubs = remember {
        listOf(
            TopClubData(1, "PadelCourt", Color(0xFF6200EE), Icons.Default.BlurOn),
            TopClubData(2, "Prosmash", Color(0xFF03A9F4), Icons.Default.Bolt),
            TopClubData(3, "Smashville", Color(0xFF4CAF50), Icons.Default.SportsTennis),
            TopClubData(4, "FitBase", Color(0xFFFF9800), Icons.Default.FitnessCenter)
        )
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

    HomeContent(
        isLoggedIn = isLoggedIn,
        categories = categories,
        bookings = bookings,
        lovedByLocals = lovedByLocals,
        specialOffers = specialOffers,
        topClubs = topClubs,
        peopleToFollow = peopleToFollow
    )
}

@Composable
fun HomeContent(
    isLoggedIn: Boolean,
    categories: List<ClubCategory>,
    bookings: List<BookingData>,
    lovedByLocals: List<OfferCardData>,
    specialOffers: List<OfferCardData>,
    topClubs: List<TopClubData>,
    peopleToFollow: List<PersonData>
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
            HomeSearchSection(isLoggedIn = isLoggedIn)
            
            SectionHeader(title = stringResource(R.string.upcoming_bookings), showViewAll = true)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(bookings, key = { it.id }) { BookingCard(it) }
            }

            SectionHeader(title = stringResource(R.string.explore_clubs))
            CategoryRow(categories)

            SectionHeader(title = stringResource(R.string.loved_by_locals), showViewAll = true)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(lovedByLocals, key = { it.id }) { OfferCard(it) }
            }

            SectionHeader(title = stringResource(R.string.special_offers), showViewAll = true)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(specialOffers, key = { it.id }) { OfferCard(it) }
            }

            SectionHeader(title = stringResource(R.string.top_clubs))
            TopClubsRow(topClubs)

            SectionHeader(title = stringResource(R.string.play_near_you), showViewAll = true)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(lovedByLocals, key = { it.id }) { OfferCard(it) } // Using same data for demo
            }

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
fun HomeSearchSection(isLoggedIn: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        if (isLoggedIn) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF6200EE), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    CalloutEmphasizedText(text = stringResource(R.string.location_sample))
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(20.dp))
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.fillMaxSize())
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
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
                CalloutEmphasizedText(text = stringResource(category.labelRes))
            }
        }
    }
}

@Composable
fun OfferCard(data: OfferCardData) {
    Card(
        modifier = Modifier
            .width(330.dp)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(160.dp).background(Color.LightGray))
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Title3EmphasizedText(text = data.title)
                    SubheadlineRegularText(text = stringResource(R.string.starts_at), color = Color.Gray)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF03A9F4), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        SubheadlineRegularText(text = data.location, color = Color.Gray)
                    }
                    CalloutEmphasizedText(text = data.price)
                }
            }
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
                        Icon(club.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
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

@Preview(showBackground = true, heightDp = 2000)
@Composable
fun HomePagePreview() {
    ClubServeTheme {
        HomePage(isLoggedIn = false)
    }
}
